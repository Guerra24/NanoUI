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

import org.joml.Vector3f;

import net.luxvacuos.nanoui.rendering.GL;

public class UniformVec3 extends Uniform {
	private float currentX;
	private float currentY;
	private float currentZ;
	private boolean used = false;

	public UniformVec3(String name) {
		super(name);
	}

	public void loadVec3(Vector3f vector) {
		loadVec3(vector.x, vector.y, vector.z);
	}

	public void loadVec3(float x, float y, float z) {
		if (!used || x != currentX || y != currentY || z != currentZ) {
			this.currentX = x;
			this.currentY = y;
			this.currentZ = z;
			used = true;
			GL.glUniform3f(super.getLocation(), x, y, z);
		}
	}

}
