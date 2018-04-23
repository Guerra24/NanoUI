/*
 * This file is part of NanoUI
 * 
 * Copyright (C) 2017-2018 Guerra24
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.nanoui.core;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import net.luxvacuos.nanoui.rendering.GL;
import net.luxvacuos.nanoui.rendering.glfw.PixelBufferHandle;
import net.luxvacuos.nanoui.rendering.glfw.RenderingAPI;
import net.luxvacuos.nanoui.rendering.glfw.Window;
import net.luxvacuos.nanoui.rendering.glfw.WindowHandle;
import net.luxvacuos.nanoui.rendering.glfw.WindowManager;
import net.luxvacuos.nanoui.rendering.nanovg.NVGFramebuffers;
import net.luxvacuos.nanoui.rendering.nanovg.themes.NanoTheme;
import net.luxvacuos.nanoui.rendering.nanovg.themes.ThemeManager;
import net.luxvacuos.nanoui.ui.Font;
import net.luxvacuos.nanoui.ui.Frame;
import net.luxvacuos.nanoui.ui.Root;

public abstract class Application {

	private static RenderingAPI api;
	private static long renderThreadID;
	private static Font poppinsRegular, poppinsLight, poppinsMedium, poppinsBold, poppinsSemiBold, entypo;

	private Thread renderThread;
	private Thread mainThread;
	private Window window;
	private WindowHandle handle;
	private boolean running = true;

	private String windowTitle = "";

	protected Frame currentFrame;
	private Root root;

	public Application() {
		mainThread = Thread.currentThread();
		TaskManager.tm = new TaskManager();
		run();
	}

	private void init() {
		if (Variables.GLES)
			api = RenderingAPI.GLES;
		else
			api = RenderingAPI.GL;

		GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		handle = WindowManager.generateHandle(Variables.WIDTH, Variables.HEIGHT, windowTitle);
		var pb = new PixelBufferHandle();
		pb.setSrgbCapable(1);
		handle.setPixelBuffer(pb);
		window = WindowManager.generateWindow(handle);
	}

	private void initRender() {
		WindowManager.createWindow(handle, window, true);
		NVGFramebuffers.init(api);
		GL.init(api);

		ThemeManager.addTheme(new NanoTheme());
		ThemeManager.setTheme(Variables.THEME);

		root = new Root(0, 0, window.getWidth(), window.getHeight());

		var loader = window.getResourceLoader();
		poppinsRegular = loader.loadNVGFont("Poppins-Regular", "Poppins-Regular");
		poppinsLight = loader.loadNVGFont("Poppins-Light", "Poppins-Light");
		poppinsMedium = loader.loadNVGFont("Poppins-Medium", "Poppins-Medium");
		poppinsBold = loader.loadNVGFont("Poppins-Bold", "Poppins-Bold");
		poppinsSemiBold = loader.loadNVGFont("Poppins-SemiBold", "Poppins-SemiBold");
		// entypo = loader.loadNVGFont("Entypo", "Entypo", 40);
	}

	private void run() {

		init();

		renderThread = new Thread(() -> {
			initRender();
			mainThread.interrupt();
			sleepThread(Long.MAX_VALUE);
			int fps = Variables.FPS;
			float deltaRender = 0f;
			while (running) {
				TaskManager.tm.updateRenderThread();
				deltaRender = window.getDelta();
				GL.glClear(GL.GL_COLOR_BUFFER_BIT);
				currentFrame.render(deltaRender);
				window.updateDisplay(fps);
			}
			disposeRender();
			mainThread.interrupt();
		});
		renderThread.setName("Render Thread");
		renderThread.start();
		renderThreadID = renderThread.getId();

		sleepThread(Long.MAX_VALUE);
		// Init currentFrame while renderThread is sleeping
		currentFrame = new Frame();
		currentFrame.setRoot(root);
		currentFrame.init(window);
		
		renderThread.interrupt(); // Resume renderThread execution
		onLaunched();
		float deltaUpdate = 0;
		float accumulator = 0f;
		float interval = 1f / Variables.UPS;
		Sync sync = new Sync();
		while (running) {
			TaskManager.tm.updateMainThread();
			deltaUpdate = window.getDelta();
			accumulator += deltaUpdate;
			while (accumulator >= interval) {
				WindowManager.update();
				// TODO: Improve This
				root.rootW = window.getWidth();
				root.rootH = window.getHeight();
				currentFrame.update(interval);
				accumulator -= interval;
			}
			if (window.isCloseRequested()) {
				onClosed();
				running = false;
			}
			sync.sync(Variables.UPS);
		}
		sleepThread(Long.MAX_VALUE);
		dispose();
	}

	private void dispose() {
		WindowManager.closeAllDisplays();
		GLFW.glfwTerminate();
	}

	private void disposeRender() {
		poppinsRegular.dispose();
		poppinsLight.dispose();
		poppinsMedium.dispose();
		poppinsBold.dispose();
		poppinsSemiBold.dispose();
		// entypo.dispose();
		window.dispose();
	}

	public abstract void onLaunched();

	public abstract void onClosed();

	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
		TaskManager.tm.addTaskMainThread(() -> window.setWindowTitle(windowTitle));
	}

	public static RenderingAPI getRenderingAPI() {
		return api;
	}

	public static long getRenderThreadID() {
		return renderThreadID;
	}

	private static void sleepThread(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}
}
