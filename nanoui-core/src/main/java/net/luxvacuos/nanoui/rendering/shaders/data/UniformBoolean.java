/*
 * This file is part of NanoUI
 * 
 * Copyright (C) 2016-2018 Guerra24
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

package net.luxvacuos.nanoui.rendering.shaders.data;

import net.luxvacuos.nanoui.rendering.GL;

public class UniformBoolean extends Uniform {

	private boolean currentBool;
	private boolean used = false;

	public UniformBoolean(String name) {
		super(name);
	}

	public void loadBoolean(boolean bool) {
		if (!used || currentBool != bool) {
			GL.glUniform1i(super.getLocation(), bool ? 1 : 0);
			used = true;
			currentBool = bool;
		}
	}

}
