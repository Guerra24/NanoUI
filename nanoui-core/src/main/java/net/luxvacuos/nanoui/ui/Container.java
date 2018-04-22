/*
 * This file is part of NanoUI
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
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

import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgIntersectScissor;
import static org.lwjgl.nanovg.NanoVG.nvgRect;
import static org.lwjgl.nanovg.NanoVG.nvgRestore;
import static org.lwjgl.nanovg.NanoVG.nvgSave;
import static org.lwjgl.nanovg.NanoVG.nvgStroke;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeColor;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeWidth;

import net.luxvacuos.nanoui.rendering.glfw.Window;
import net.luxvacuos.nanoui.rendering.nanovg.themes.Theme;

public class Container extends Component {

	protected RootComponent comp;

	public Container(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		comp = new RootComponent(x, y - h, w, h);
	}

	@Override
	public void init(Window window) {
		comp.init(window);
	}

	@Override
	public void render(Window window) {
		long vg = window.getNVGID();
		nvgSave(window.getNVGID());
		nvgIntersectScissor(vg, rootComponent.rootX + alignedX, window.getHeight() - rootComponent.rootY - alignedY - h,
				w, h);
		comp.render();
		if (Theme.DEBUG) {
			nvgBeginPath(vg);
			nvgRect(vg, rootComponent.rootX + alignedX, window.getHeight() - rootComponent.rootY - alignedY - h, w, h);
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugC);
			nvgStroke(vg);
		}
		nvgRestore(vg);
	}

	@Override
	public void update(float delta, Window window) {
		comp.update(delta);
		super.update(delta, window);
	}

	@Override
	public void alwaysUpdate(float delta, Window window) {
		super.alwaysUpdate(delta, window);
		comp.alwaysUpdate(delta, rootComponent.rootX + alignedX, rootComponent.rootY + alignedY + h, w, h);
	}

	@Override
	public void dispose(Window window) {
		comp.dispose();
		super.dispose(window);
	}

	public void setLayout(ILayout layout) {
		comp.setLayout(layout);
	}

	public void addComponent(Component component) {
		comp.addComponent(component);
	}

	public void removeComponent(Component component) {
		comp.removeComponent(component);
	}

}
