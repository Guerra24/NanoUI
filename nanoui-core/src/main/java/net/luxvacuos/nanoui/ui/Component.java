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

package net.luxvacuos.nanoui.ui;

import net.luxvacuos.nanoui.rendering.glfw.Window;

public abstract class Component implements IComponent {

	protected Root root;
	protected float x, y, w, h;
	protected ComponentState componentState = ComponentState.NONE;
	protected Window window;
	boolean initialized;

	@Override
	public void init(Window window) {
		initialized = true;
		this.window = window;
	}

	@Override
	public void update(float delta) {
		componentState = ComponentState.NONE;
	}
	
	@Override
	public void dispose() {
	}

}
