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

import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.Display;
import com.sun.jna.platform.unix.X11.XErrorEvent;
import com.sun.jna.platform.unix.X11.XErrorHandler;

public class ErrorHandler implements XErrorHandler {

	@Override
	public int apply(Display display, XErrorEvent e) {
		int MAX_ERROR_TEXT_LENGTH = 1024;
		byte error_text[] = new byte[MAX_ERROR_TEXT_LENGTH];
		X11.INSTANCE.XGetErrorText(display, e.error_code, error_text, error_text.length);
		System.out.println("Received X error:\n" + "    Request: " + e.request_code + "\n" + "    Error code: "
				+ e.error_code + " - " + new String(error_text) + "\n" + "    Resource ID: " + e.resourceid);
		return 0;
	}

}
