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

import org.joml.Matrix4f;

import net.luxvacuos.nanoui.rendering.GL;

public class UniformMatrix extends Uniform {

	private Matrix4f current;
	private boolean used = false;
	private float[] fm = new float[16];

	public UniformMatrix(String name) {
		super(name);
	}

	public void loadMatrix(Matrix4f matrix) {
		if (!used || !matrix.equals(current)) {
			matrix.get(fm);
			GL.glUniformMatrix4fv(super.getLocation(), false, fm);
		}
	}

}
