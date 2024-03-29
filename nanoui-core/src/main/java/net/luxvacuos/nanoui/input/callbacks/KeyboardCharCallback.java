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

import org.lwjgl.glfw.GLFWCharCallback;

import com.badlogic.gdx.utils.Array;

public class KeyboardCharCallback extends GLFWCharCallback {
	private Array<String> queue;
	private String lastChar = "";
	private long lastPress;
	private boolean enabled = false;
	
	private final long windowID;

	public KeyboardCharCallback(long windowID) {
		this.queue = new Array<String>(5);
		this.windowID = windowID;
	}
	
	public void setEndabled(boolean flag) {
		this.enabled = flag;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public boolean hasData() {
		return (this.enabled && this.queue.size > 0);
	}
	
	public Array<String> getData() {
		if(!this.enabled) return null;
		this.queue.shrink();
		Array<String> data = new Array<String>(this.queue);
		this.queue.clear();
		this.queue.ensureCapacity(5);
		return data;
	}
 
	@Override
	public void invoke(long window, int codepoint) {
		if(this.enabled && (this.windowID == window)) {
			String charr;
			
			charr = new String(Character.toChars(codepoint));
			
			if(this.lastChar.equals(charr) && ((System.currentTimeMillis() - this.lastPress) < 50)) return; //0.05 seconds
			
			this.lastChar = charr;
			this.lastPress = System.currentTimeMillis();
			this.queue.add(charr);
		}
	}

}
