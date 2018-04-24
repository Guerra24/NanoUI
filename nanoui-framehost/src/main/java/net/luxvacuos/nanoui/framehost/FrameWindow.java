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

import static net.luxvacuos.nanoui.framehost.Variables.BORDER_SIZE;
import static net.luxvacuos.nanoui.framehost.Variables.TITLEBAR_SIZE;
import static org.lwjgl.glfw.GLFWNativeX11.glfwGetX11Window;

import org.lwjgl.nanovg.NVGColor;

import com.sun.jna.NativeLong;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.WindowByReference;
import com.sun.jna.platform.unix.X11.XClientMessageEvent;
import com.sun.jna.platform.unix.X11.XEvent;
import com.sun.jna.platform.unix.X11.XSizeHints;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;

import net.luxvacuos.nanoui.core.TaskManager;
import net.luxvacuos.nanoui.core.Variables;
import net.luxvacuos.nanoui.framehost.ui.FramePage;
import net.luxvacuos.nanoui.input.MouseHandler;
import net.luxvacuos.nanoui.rendering.GL;
import net.luxvacuos.nanoui.rendering.NoMTResourceLoader;
import net.luxvacuos.nanoui.rendering.glfw.PixelBufferHandle;
import net.luxvacuos.nanoui.rendering.glfw.Window;
import net.luxvacuos.nanoui.rendering.glfw.WindowHandle;
import net.luxvacuos.nanoui.rendering.glfw.WindowManager;
import net.luxvacuos.nanoui.rendering.nanovg.themes.NanoTheme;
import net.luxvacuos.nanoui.rendering.nanovg.themes.Theme;
import net.luxvacuos.nanoui.rendering.nanovg.themes.Theme.BackgroundStyle;
import net.luxvacuos.nanoui.rendering.nanovg.themes.ThemeManager;
import net.luxvacuos.nanoui.ui.Font;
import net.luxvacuos.nanoui.ui.Frame;
import net.luxvacuos.nanoui.ui.Root;

public class FrameWindow extends Thread {
	private WindowHandle handle;
	private Window window;
	private com.sun.jna.platform.unix.X11.Window nativeFrameWindow, nativeWindow;
	private boolean running = true;

	private BackgroundStyle backgroundStyle = BackgroundStyle.SOLID;
	private NVGColor backgroundColor = Theme.setColor("#000000FF");
	private boolean decorations = true, titleBar = true, maximized = false, dragging, resizable = true;
	private int ft, fb, fr, fl;
	private int x, y, w, h, dragX, dragY;
	private int oldX, oldY, oldW, oldH;

	private Font poppinsRegular, poppinsLight, poppinsMedium, poppinsBold, poppinsSemiBold;

	private FramePage content;
	private Frame currentFrame;
	private Root root;

	public FrameWindow(int x, int y, int width, int height, com.sun.jna.platform.unix.X11.Window nativeWindow) {
		this.w = width;
		this.h = height;
		handle = WindowManager.generateHandle(width, height, "");
		var pb = new PixelBufferHandle();
		pb.setSrgbCapable(1);
		handle.setPixelBuffer(pb).isDecorated(false).isVisible(false);
		window = WindowManager.generateWindow(handle);
		window.setResourceLoader(new NoMTResourceLoader(window));
		// window.setPosition(x, y);
		nativeFrameWindow = new com.sun.jna.platform.unix.X11.Window(glfwGetX11Window(window.getID()));
		this.nativeWindow = nativeWindow;

		init();
	}

	private void init() {
		XSizeHints hints = X11.INSTANCE.XAllocSizeHints();
		NativeLongByReference ret = new NativeLongByReference();
		X11Ext.INSTANCE.XGetWMNormalHints(FrameHost.getDisplay(), nativeWindow, hints, ret);
		if (hints.min_width == hints.max_width && hints.min_height == hints.max_height)
			resizable = false;

		root = new Root(0, 0, w, h);
		currentFrame = new Frame();
		currentFrame.setRoot(root);
		currentFrame.init(window);
		content = new FramePage(this);
		currentFrame.navigate(content);
	}

	@Override
	public void run() {
		WindowManager.createWindow(handle, window, true);
		GL.init(Variables.api);
		ThemeManager.addTheme(new NanoTheme());
		ThemeManager.setTheme(Variables.THEME);

		var loader = window.getResourceLoader();
		poppinsRegular = loader.loadNVGFont("Poppins-Regular", "Poppins-Regular");
		poppinsLight = loader.loadNVGFont("Poppins-Light", "Poppins-Light");
		poppinsMedium = loader.loadNVGFont("Poppins-Medium", "Poppins-Medium");
		poppinsBold = loader.loadNVGFont("Poppins-Bold", "Poppins-Bold");
		poppinsSemiBold = loader.loadNVGFont("Poppins-SemiBold", "Poppins-SemiBold");

		float delta;
		int fps = 60;
		while (running) {
			delta = window.getDelta();
			GL.glClearColor(0, 0, 0, 1);
			GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

			w = window.getWidth();
			h = window.getHeight();
			root.rootW = w;
			root.rootH = h;

			currentFrame.update(delta);

			window.setViewport(0, 0, w, h);
			window.beingNVGFrame();
			long vg = window.getNVGID();
			Theme.renderWindow(vg, BORDER_SIZE, BORDER_SIZE + TITLEBAR_SIZE, w - BORDER_SIZE * 2,
					h - BORDER_SIZE * 2 - TITLEBAR_SIZE, backgroundStyle, backgroundColor, decorations, titleBar,
					maximized, ft, fb, fr, fl, BORDER_SIZE, TITLEBAR_SIZE);
			window.endNVGFrame();

			currentFrame.render(delta);

			MouseHandler mh = window.getMouseHandler();
			if (mh.isButtonPressed(0) || dragging) {
				WindowByReference wr = new WindowByReference(), cr = new WindowByReference();
				IntByReference x = new IntByReference(), y = new IntByReference(), i = new IntByReference(),
						m = new IntByReference();
				X11.INSTANCE.XQueryPointer(FrameHost.getDisplay(), FrameHost.getRoot(), wr, cr, x, y, i, i, m);
				if (!dragging) {
					dragX = mh.getXI();
					dragY = mh.getYI();
				}
				if (!mh.isButtonPressed(0) && dragging) {
					dragX = -1;
					dragY = -1;
				} else {
					this.x = x.getValue() - dragX;
					this.y = y.getValue() - dragY;
					X11Ext.INSTANCE.XMoveWindow(FrameHost.getDisplay(), nativeFrameWindow, this.x, this.y);
				}
				dragging = mh.isButtonPressed(0);
			}

			window.updateDisplay(fps);
		}
		currentFrame.dispose();
		poppinsRegular.dispose();
		poppinsLight.dispose();
		poppinsMedium.dispose();
		poppinsBold.dispose();
		poppinsSemiBold.dispose();
		window.dispose();
		TaskManager.tm.addTaskMainThread(() -> window.closeDisplay());
	}

	public void closeWindow() {
		XClientMessageEvent event = new XClientMessageEvent();
		event.type = X11.ClientMessage;
		event.message_type = CachedAtoms.getAtom("WM_PROTOCOLS");
		event.window = nativeWindow;
		event.format = 32;
		event.data.setType(NativeLong[].class);
		event.data.l[0] = CachedAtoms.getAtom("WM_DELETE_WINDOW");

		XEvent e = new XEvent();
		e.setTypedValue(event);

		if (X11.INSTANCE.XSendEvent(FrameHost.getDisplay(), nativeWindow, 0, new NativeLong(0), e) != 0) {
			System.out.println("Deleted Window: " + nativeWindow);
		}
	}

	public void toggleMaximize() {
		if (!resizable)
			return;
		maximized = !maximized;
		if (maximized) {
			oldX = x;
			oldY = y;
			oldW = w;
			oldH = h;
			int screen = X11.INSTANCE.XDefaultScreen(FrameHost.getDisplay());
			int sw = X11.INSTANCE.XDisplayWidth(FrameHost.getDisplay(), screen);
			int sh = X11.INSTANCE.XDisplayHeight(FrameHost.getDisplay(), screen);
			x = -BORDER_SIZE;
			y = -BORDER_SIZE;
			w = sw + BORDER_SIZE * 2;
			h = sh + BORDER_SIZE * 2;
			X11Ext.INSTANCE.XMoveWindow(FrameHost.getDisplay(), nativeFrameWindow, x, y);
			X11Ext.INSTANCE.XResizeWindow(FrameHost.getDisplay(), nativeFrameWindow, w, h);
			X11Ext.INSTANCE.XResizeWindow(FrameHost.getDisplay(), nativeWindow, w - BORDER_SIZE * 2,
					h - BORDER_SIZE * 2 - TITLEBAR_SIZE);
			X11.INSTANCE.XSync(FrameHost.getDisplay(), false);
		} else {
			x = oldX;
			y = oldY;
			w = oldW;
			h = oldH;
			X11Ext.INSTANCE.XMoveWindow(FrameHost.getDisplay(), nativeFrameWindow, x, y);
			X11Ext.INSTANCE.XResizeWindow(FrameHost.getDisplay(), nativeFrameWindow, w, h);
			X11Ext.INSTANCE.XResizeWindow(FrameHost.getDisplay(), nativeWindow, w - BORDER_SIZE * 2,
					h - BORDER_SIZE * 2 - TITLEBAR_SIZE);
			X11.INSTANCE.XSync(FrameHost.getDisplay(), false);
		}
	}

	public boolean isMaximized() {
		return maximized;
	}

	public boolean isResizable() {
		return resizable;
	}

	void closeWindowI() {
		running = false;
	}

	public com.sun.jna.platform.unix.X11.Window getNativeFrameWindow() {
		return nativeFrameWindow;
	}

	public com.sun.jna.platform.unix.X11.Window getNativeWindow() {
		return nativeWindow;
	}

}
