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

package net.luxvacuos.nanoui.input.callbacks;

import java.util.BitSet;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyboardKeyCallback extends GLFWKeyCallback {
	private BitSet keys = new BitSet(65536);
	private BitSet ignore = new BitSet(65536);
	
	private final long windowID;
	
	public KeyboardKeyCallback(long windowID) {
		this.windowID = windowID;
	}

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if(window != this.windowID) return; //only care about the window this callback is assigned to
		if(key < 0 || key > 65535) {
			System.out.println("WARNING: Caught invalid key! (Key: "+key+", scancode: "+scancode+", pressed: "+(action != GLFW.GLFW_RELEASE)+")");
			return;
		}
		
		this.keys.set(key, (action != GLFW.GLFW_RELEASE));
		if(action == GLFW.GLFW_RELEASE && this.ignore.get(key)) this.ignore.clear(key);
	}
	
	public boolean isKeyPressed(int keycode) {
		return this.keys.get(keycode);
	}
	
	public boolean isKeyIgnored(int keycode) {
		return this.ignore.get(keycode);
	}
	
	public void setKeyIgnored(int keycode) {
		this.ignore.set(keycode);
	}

}
