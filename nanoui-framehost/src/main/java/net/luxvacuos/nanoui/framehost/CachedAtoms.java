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

import java.util.HashMap;
import java.util.Map;

import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.Atom;

public class CachedAtoms {

	private static Map<String, Atom> ATOMS = new HashMap<>();

	public static Atom getAtom(String name) {
		if (ATOMS.containsKey(name)) {
			return ATOMS.get(name);
		}
		Atom a = X11.INSTANCE.XInternAtom(FrameHost.getDisplay(), name, false);
		ATOMS.put(name, a);
		return a;
	}

}
