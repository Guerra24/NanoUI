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

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.unix.X11.Display;
import com.sun.jna.platform.unix.X11.Pixmap;
import com.sun.jna.platform.unix.X11.Window;

public interface XComposite extends Library {

	public static XComposite INSTANCE = (XComposite) Native.loadLibrary("Xcomposite", XComposite.class);

	public static int CompositeRedirectAutomatic = 0;
	public static int CompositeRedirectManual = 1;

	public class Status {

	}

	public class XserverRegion {

	}

	public boolean XCompositeQueryExtension(Display dpy, int event_base_return, int error_base_return);

	public Status XCompositeQueryVersion(Display dpy, int major_version_return, int minor_version_return);

	public int XCompositeVersion();

	public void XCompositeRedirectWindow(Display dpy, Window window, int update);

	public void XCompositeRedirectSubwindows(Display dpy, Window window, int update);

	public void XCompositeUnredirectWindow(Display dpy, Window window, int update);

	public void XCompositeUnredirectSubwindows(Display dpy, Window window, int update);

	public XserverRegion XCompositeCreateRegionFromBorderClip(Display dpy, Window window);

	public Pixmap XCompositeNameWindowPixmap(Display dpy, Window window);

	public Window XCompositeGetOverlayWindow(Display dpy, Window window);

	public void XCompositeReleaseOverlayWindow(Display dpy, Window window);

}
