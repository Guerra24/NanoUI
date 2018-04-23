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

import org.joml.Vector4f;

import net.luxvacuos.nanoui.rendering.GL;

public class UniformVec4 extends Uniform {
	private float currentX;
	private float currentY;
	private float currentZ;
	private float currentW;
	private boolean used = false;

	public UniformVec4(String name) {
		super(name);
	}

	public void loadVec4(Vector4f vector) {
		loadVec4(vector.x, vector.y, vector.z, vector.w);
	}

	public void loadVec4(float x, float y, float z, float w) {
		if (!used || x != currentX || y != currentY || z != currentZ || w != currentW) {
			this.currentX = x;
			this.currentY = y;
			this.currentZ = z;
			this.currentW = w;
			used = true;
			GL.glUniform4f(super.getLocation(), x, y, z, w);
		}
	}

}
