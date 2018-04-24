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

import static org.lwjgl.nanovg.NanoVG.nvgDeleteImage;

import net.luxvacuos.nanoui.core.TaskManager;
import net.luxvacuos.nanoui.rendering.nanovg.themes.Theme;

public class Image extends Component {

	private int image;
	private boolean deleteOnClose = true;

	public Image(float x, float y, float w, float h, int image) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.image = image;
	}

	public Image(float x, float y, float w, float h, int image, boolean deleteOnClose) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.image = image;
		this.deleteOnClose = deleteOnClose;
	}

	@Override
	public void render(float delta) {
		Theme.renderImage(window.getNVGID(), root.rootX + fx, root.rootY + fy, w, h, image, 1);
	}

	@Override
	public void dispose() {
		if (deleteOnClose)
			TaskManager.tm.addTaskRenderThread(() -> nvgDeleteImage(window.getNVGID(), image));
	}

	public void setImage(int image) {
		this.image = image;
	}

}
