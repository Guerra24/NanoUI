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

package net.luxvacuos.nanoui.framehost;

import static org.lwjgl.glfw.GLFWNativeX11.glfwGetX11Window;

import static net.luxvacuos.nanoui.framehost.Variables.*;

import org.lwjgl.nanovg.NVGColor;

import net.luxvacuos.nanoui.core.TaskManager;
import net.luxvacuos.nanoui.core.Variables;
import net.luxvacuos.nanoui.input.MouseHandler;
import net.luxvacuos.nanoui.rendering.GL;
import net.luxvacuos.nanoui.rendering.glfw.PixelBufferHandle;
import net.luxvacuos.nanoui.rendering.glfw.Window;
import net.luxvacuos.nanoui.rendering.glfw.WindowHandle;
import net.luxvacuos.nanoui.rendering.glfw.WindowManager;
import net.luxvacuos.nanoui.rendering.nanovg.themes.NanoTheme;
import net.luxvacuos.nanoui.rendering.nanovg.themes.Theme;
import net.luxvacuos.nanoui.rendering.nanovg.themes.ThemeManager;
import net.luxvacuos.nanoui.rendering.nanovg.themes.Theme.BackgroundStyle;

public class Frame extends Thread {
	private WindowHandle handle;
	private Window window;
	private com.sun.jna.platform.unix.X11.Window nativeWindow;
	private boolean running = true;

	private BackgroundStyle backgroundStyle = BackgroundStyle.SOLID;
	private NVGColor backgroundColor = Theme.setColor("#000000FF");
	private boolean decorations = true, titleBar = true, maximized = false;
	private int ft, fb, fr, fl;

	private int x, y;

	public Frame(int width, int height) {
		handle = WindowManager.generateHandle(width, height, "");
		var pb = new PixelBufferHandle();
		pb.setSrgbCapable(1);
		handle.setPixelBuffer(pb).isDecorated(false).isVisible(false);
		window = WindowManager.generateWindow(handle);
		nativeWindow = new com.sun.jna.platform.unix.X11.Window(glfwGetX11Window(window.getID()));
	}

	@Override
	public void run() {
		WindowManager.createWindow(handle, window, true);
		GL.init(Variables.api);
		ThemeManager.addTheme(new NanoTheme());
		ThemeManager.setTheme(Variables.THEME);

		float delta;
		int fps = 30;
		while (running) {
			delta = window.getDelta();
			GL.glClearColor(0, 0, 0, 1);
			GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			window.setViewport(0, 0, window.getWidth(), window.getHeight());
			window.beingNVGFrame();
			long vg = window.getNVGID();
			Theme.renderWindow(vg, BORDER_SIZE, BORDER_SIZE + TITLEBAR_SIZE, window.getWidth() - BORDER_SIZE * 2,
					window.getHeight() - BORDER_SIZE * 2 - TITLEBAR_SIZE, backgroundStyle, backgroundColor, decorations,
					titleBar, maximized, ft, fb, fr, fl, BORDER_SIZE, TITLEBAR_SIZE);
			window.endNVGFrame();
			window.updateDisplay(fps);
		}
		window.dispose();
		TaskManager.tm.addTaskMainThread(() -> window.closeDisplay());
	}

	public void closeWindow() {
		running = false;
	}

	public com.sun.jna.platform.unix.X11.Window getNativeWindow() {
		return nativeWindow;
	}

}
