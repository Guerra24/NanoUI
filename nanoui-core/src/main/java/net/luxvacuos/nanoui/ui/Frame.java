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

package net.luxvacuos.nanoui.ui;

import net.luxvacuos.nanoui.rendering.glfw.Window;

public class Frame extends Component {

	private Page currentPage;

	@Override
	public void init(Window window) {
		super.init(window);
	}

	@Override
	public void render(float delta) {
		if (currentPage != null)
			currentPage.render(delta);
	}

	@Override
	public void update(float delta) {
		if (currentPage != null)
			currentPage.update(delta);
	}

	@Override
	public void dispose() {
	}

	public void navigate(Page page) {
		if (currentPage != null)
			currentPage.dispose();
		currentPage = page;
		currentPage.root = super.root;
		currentPage.init(super.window);
	}

	/**
	 * <b> INTERNAL USE <b>
	 */
	public void setRoot(Root root) {
		super.root = root;
	}

}
