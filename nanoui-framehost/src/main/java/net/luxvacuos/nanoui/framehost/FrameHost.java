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

import static com.sun.jna.platform.unix.X11.Button1Mask;
import static com.sun.jna.platform.unix.X11.ButtonPress;
import static com.sun.jna.platform.unix.X11.ButtonRelease;
import static com.sun.jna.platform.unix.X11.ConfigureNotify;
import static com.sun.jna.platform.unix.X11.ConfigureRequest;
import static com.sun.jna.platform.unix.X11.CreateNotify;
import static com.sun.jna.platform.unix.X11.DestroyNotify;
import static com.sun.jna.platform.unix.X11.GrabModeAsync;
import static com.sun.jna.platform.unix.X11.IsViewable;
import static com.sun.jna.platform.unix.X11.KeyPress;
import static com.sun.jna.platform.unix.X11.MapNotify;
import static com.sun.jna.platform.unix.X11.MapRequest;
import static com.sun.jna.platform.unix.X11.Mod1Mask;
import static com.sun.jna.platform.unix.X11.MotionNotify;
import static com.sun.jna.platform.unix.X11.ReparentNotify;
import static com.sun.jna.platform.unix.X11.SubstructureNotifyMask;
import static com.sun.jna.platform.unix.X11.SubstructureRedirectMask;
import static com.sun.jna.platform.unix.X11.UnmapNotify;
import static net.luxvacuos.nanoui.framehost.Variables.BORDER_SIZE;
import static net.luxvacuos.nanoui.framehost.Variables.TITLEBAR_SIZE;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import com.sun.jna.NativeLong;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.Display;
import com.sun.jna.platform.unix.X11.Window;
import com.sun.jna.platform.unix.X11.WindowByReference;
import com.sun.jna.platform.unix.X11.XButtonEvent;
import com.sun.jna.platform.unix.X11.XConfigureEvent;
import com.sun.jna.platform.unix.X11.XConfigureRequestEvent;
import com.sun.jna.platform.unix.X11.XCreateWindowEvent;
import com.sun.jna.platform.unix.X11.XDestroyWindowEvent;
import com.sun.jna.platform.unix.X11.XErrorEvent;
import com.sun.jna.platform.unix.X11.XErrorHandler;
import com.sun.jna.platform.unix.X11.XEvent;
import com.sun.jna.platform.unix.X11.XKeyEvent;
import com.sun.jna.platform.unix.X11.XMapEvent;
import com.sun.jna.platform.unix.X11.XMapRequestEvent;
import com.sun.jna.platform.unix.X11.XMotionEvent;
import com.sun.jna.platform.unix.X11.XReparentEvent;
import com.sun.jna.platform.unix.X11.XUnmapEvent;
import com.sun.jna.platform.unix.X11.XWindowAttributes;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import net.luxvacuos.nanoui.core.Sync;
import net.luxvacuos.nanoui.core.TaskManager;
import net.luxvacuos.nanoui.core.Variables;
import net.luxvacuos.nanoui.framehost.X11Ext.XWindowChanges;
import net.luxvacuos.nanoui.rendering.glfw.RenderingAPI;
import net.luxvacuos.nanoui.rendering.glfw.WindowManager;

public class FrameHost {

	private static Display display;
	private static Window root;
	private boolean wmDetected;
	private Map<Window, FrameWindow> clients = new HashMap<>();
	private Vector2f dragStartPos = new Vector2f();
	private Vector2f dragStartFramePos = new Vector2f();
	private Vector2f dragStartFrameSize = new Vector2f();

	public static final X11 x11 = X11.INSTANCE;
	public static final X11Ext x11ext = X11Ext.INSTANCE;
	public static final XComposite xcomp = XComposite.INSTANCE;

	public FrameHost() {
	}

	public void run() {
		GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		Variables.api = RenderingAPI.GL;
		TaskManager.tm = new TaskManager();
		create();
		try {
			init();
			loop();
		} finally {
			dispose();
		}
	}

	private void create() {
		display = x11.XOpenDisplay(":1");
		if (display == null)
			throw new IllegalStateException("Failed to open X display.");
		root = x11.XDefaultRootWindow(display);
	}

	private void init() {
		wmDetected = false;
		x11.XSetErrorHandler(new XErrorHandler() {

			@Override
			public int apply(Display display, XErrorEvent errorEvent) {
				wmDetected = true;
				return 0;
			}
		});
		// xcomp.XCompositeRedirectSubwindows(display, root,
		// CompositeRedirectAutomatic);
		x11.XSelectInput(display, root, new NativeLong(SubstructureRedirectMask | SubstructureNotifyMask));
		// overlay = xcomp.XCompositeGetOverlayWindow(display, root);

		x11.XSync(display, false);
		if (wmDetected) {
			throw new IllegalStateException("Detected another window manager on display.");
		}
		x11.XSetErrorHandler(new ErrorHandler());
		x11ext.XGrabServer(display);
		WindowByReference returnedRoot = new WindowByReference(), returnedParent = new WindowByReference();
		PointerByReference topLevelWindows = new PointerByReference();
		IntByReference numTopLevelWindows = new IntByReference();
		x11.XQueryTree(display, root, returnedRoot, returnedParent, topLevelWindows, numTopLevelWindows);
		if (topLevelWindows.getValue() != null) {
			long[] win = topLevelWindows.getValue().getLongArray(0, numTopLevelWindows.getValue());
			for (int i = 0; i < numTopLevelWindows.getValue(); i++) {
				frame(new Window(win[i]), true);
			}
			x11.XFree(topLevelWindows.getPointer());
		}
		x11ext.XUngrabServer(display);
	}

	private void loop() {
		Sync sync = new Sync();
		while (true) {
			TaskManager.tm.updateMainThread();
			WindowManager.update();
			while (x11.XPending(display) != 0) {
				XEvent e = new XEvent();
				x11.XNextEvent(display, e);
				System.out.println("Received event " + e.type);
				switch (e.type) {
				case CreateNotify:
					onCreateNotify((XCreateWindowEvent) e.readField("xcreatewindow"));
					break;
				case DestroyNotify:
					onDestroyNotify((XDestroyWindowEvent) e.readField("xdestroywindow"));
					break;
				case ReparentNotify:
					onReparentNotify((XReparentEvent) e.readField("xreparent"));
					break;
				case ConfigureRequest:
					onConfigureRequest((XConfigureRequestEvent) e.readField("xconfigurerequest"));
					break;
				case MapRequest:
					onMapRequest((XMapRequestEvent) e.readField("xmaprequest"));
					break;
				case MapNotify:
					onMapNotify((XMapEvent) e.readField("xmap"));
					break;
				case ButtonPress:
					onButtonPress((XButtonEvent) e.readField("xbutton"));
					break;
				case ButtonRelease:
					onButtonRelease((XButtonEvent) e.readField("xbutton"));
					break;
				case UnmapNotify:
					onUnmapNotify((XUnmapEvent) e.readField("xunmap"));
					break;
				case ConfigureNotify:
					onConfigureNotify((XConfigureEvent) e.readField("xconfigure"));
					break;
				case MotionNotify:
					XMotionEvent xmotion = (XMotionEvent) e.readField("xmotion");
					while (x11.XCheckTypedWindowEvent(display, xmotion.window, MotionNotify, e))
						;
					xmotion = (XMotionEvent) e.readField("xmotion");
					onMotionNotify(xmotion);
					break;
				case KeyPress:
					onKeyPress((XKeyEvent) e.readField("xkey"));
				default:
					System.out.println("Event ignored");
					break;
				}
			}
			sync.sync(60);
		}
	}

	private void onCreateNotify(XCreateWindowEvent e) {
	}

	private void onDestroyNotify(XDestroyWindowEvent e) {
	}

	private void onReparentNotify(XReparentEvent e) {
	}

	private void onConfigureNotify(XConfigureEvent e) {
	}

	private void onMapNotify(XMapEvent e) {
	}

	private void onConfigureRequest(XConfigureRequestEvent e) {
		XWindowChanges changes = new XWindowChanges();

		if (clients.containsKey(e.window)) {
			changes.x = BORDER_SIZE;
			changes.y = BORDER_SIZE + TITLEBAR_SIZE;
			changes.width = e.width;
			changes.height = e.height;
			changes.border_width = e.border_width;
			changes.sibling = e.above;
			changes.stack_mode = e.detail;
			x11ext.XConfigureWindow(display, e.window, e.value_mask, changes);
			System.out.println("Resize " + e.window + " to " + e.width + " " + e.height);

			changes.x = e.x;
			changes.y = e.y;
			Window frame = clients.get(e.window).getNativeFrameWindow();
			changes.width += BORDER_SIZE * 2;
			changes.height += BORDER_SIZE * 2 + TITLEBAR_SIZE;
			x11ext.XConfigureWindow(display, frame, e.value_mask, changes);
			System.out.println("Resize [" + frame + "] to " + e.width + " " + e.height);
		} else {
			changes.x = e.x;
			changes.y = e.y;
			changes.width = e.width;
			changes.height = e.height;
			changes.border_width = e.border_width;
			changes.sibling = e.above;
			changes.stack_mode = e.detail;

			x11ext.XConfigureWindow(display, e.window, e.value_mask, changes);
			System.out.println("Resize " + e.window + " to " + e.width + " " + e.height);
		}
	}

	private void onMapRequest(XMapRequestEvent e) {
		frame(e.window, false);
		x11.XMapWindow(display, e.window);
	}

	private void frame(Window window, boolean created) {

		XWindowAttributes xWindowAttrs = new XWindowAttributes();
		x11.XGetWindowAttributes(display, window, xWindowAttrs);
		if (created) {
			if (xWindowAttrs.override_redirect || xWindowAttrs.map_state != IsViewable) {
				return;
			}
		}
		FrameWindow f = new FrameWindow(xWindowAttrs.x - BORDER_SIZE, xWindowAttrs.y - BORDER_SIZE - TITLEBAR_SIZE,
				xWindowAttrs.width + BORDER_SIZE * 2, xWindowAttrs.height + BORDER_SIZE * 2 + TITLEBAR_SIZE, window);
		f.start();
		Window frame = f.getNativeFrameWindow();

		// Window frame = x11.XCreateSimpleWindow(display, root, xWindowAttrs.x,
		// xWindowAttrs.y, xWindowAttrs.width,
		// xWindowAttrs.height, BORDER_WIDTH, BORDER_COLOR, BG_COLOR);
		x11.XSelectInput(display, frame, new NativeLong(SubstructureRedirectMask | SubstructureNotifyMask));
		x11ext.XAddToSaveSet(display, window);
		x11ext.XReparentWindow(display, window, frame, BORDER_SIZE, BORDER_SIZE + TITLEBAR_SIZE);
		x11.XMapWindow(display, frame);
		clients.put(window, f);
		// b. Move windows with alt + left button.
		// x11ext.XGrabButton(display, Button1, AnyModifier, window, false,
		// ButtonPressMask | ButtonReleaseMask | ButtonMotionMask, GrabModeAsync,
		// GrabModeAsync, null, null);
		// c. Kill windows with alt + f4.
		x11.XGrabKey(display, (int) x11.XKeysymToKeycode(display, x11.XStringToKeysym("F4")), Mod1Mask, window, 0,
				GrabModeAsync, GrabModeAsync);
		// d. Switch windows with alt + tab.
		x11.XGrabKey(display, (int) x11.XKeysymToKeycode(display, x11.XStringToKeysym("Tab")), Mod1Mask, window, 0,
				GrabModeAsync, GrabModeAsync);
		System.out.println("Framed window " + window + " [" + frame + "]");
	}

	private void onUnmapNotify(XUnmapEvent e) {
		if (!clients.containsKey(e.window)) {
			System.out.println("Ignored UnmapNotify for non-client window " + e.window);
			return;
		}
		if (e.event == root) {
			System.out.println("Ignore UnmapNotify for reparented pre-existing window " + e.window);
			return;
		}
		unframe(e.window);
	}

	private void unframe(Window window) {
		Window frame = clients.get(window).getNativeFrameWindow();
		if (frame == null)
			return;

		// x11.XUnmapWindow(display, frame);
		// x11ext.XReparentWindow(display, window, root, 0, 0);
		// x11ext.XRemoveFromSaveSet(display, window);
		clients.get(window).closeWindowI();
		clients.remove(window);
		System.out.println("Unframed window " + window + " [" + frame + "]");
	}

	private void onButtonPress(XButtonEvent e) {
		Window frame = clients.get(e.window).getNativeFrameWindow();
		if (frame == null)
			return;

		dragStartPos.set(e.x_root, e.y_root);

		WindowByReference returnedRoot = new WindowByReference();
		IntByReference x = new IntByReference(), y = new IntByReference(), width = new IntByReference(),
				height = new IntByReference(), borderWidth = new IntByReference(), depth = new IntByReference();
		x11.XGetGeometry(display, frame, returnedRoot, x, y, width, height, borderWidth, depth);
		dragStartFramePos.set(x.getValue(), y.getValue());
		dragStartFrameSize.set(width.getValue(), height.getValue());
		x11ext.XRaiseWindow(display, frame);
	}

	private void onButtonRelease(XButtonEvent e) {
	}

	private void onMotionNotify(XMotionEvent e) {
		Window frame = clients.get(e.window).getNativeFrameWindow();
		if (frame == null)
			return;
		Vector2f dragPos = new Vector2f(e.x_root, e.y_root);
		Vector2f delta = new Vector2f();
		dragPos.sub(dragStartPos, delta);
		if ((e.state & Button1Mask) != 0) {
			Vector2f sizeDelta = new Vector2f(delta.x, delta.y);
			Vector2f destFrameSize = new Vector2f();
			dragStartFrameSize.add(sizeDelta, destFrameSize);
			x11ext.XResizeWindow(display, frame, (int) destFrameSize.x + BORDER_SIZE * 2,
					(int) destFrameSize.y + BORDER_SIZE * 2 + TITLEBAR_SIZE);
			x11ext.XResizeWindow(display, e.window, (int) destFrameSize.x, (int) destFrameSize.y);
		}
	}

	private void onKeyPress(XKeyEvent e) {
		System.out.println("Key");
	}

	private void dispose() {
		x11.XCloseDisplay(display);
		WindowManager.closeAllDisplays();
		GLFW.glfwTerminate();
	}

	public static Display getDisplay() {
		return display;
	}

	public static Window getRoot() {
		return root;
	}

}
