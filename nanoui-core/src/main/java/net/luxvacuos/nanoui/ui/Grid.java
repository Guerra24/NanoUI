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

import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgIntersectScissor;
import static org.lwjgl.nanovg.NanoVG.nvgRect;
import static org.lwjgl.nanovg.NanoVG.nvgRestore;
import static org.lwjgl.nanovg.NanoVG.nvgSave;
import static org.lwjgl.nanovg.NanoVG.nvgStroke;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeColor;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeWidth;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import net.luxvacuos.nanoui.rendering.nanovg.themes.Theme;

public class Grid extends Component {

	private List<Component> components = new ArrayList<>();
	private Queue<Component> componentsToAdd = new LinkedList<>(), componentsToRemove = new LinkedList<>();
	private Root gridRoot;

	public Grid(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		gridRoot = new Root(x, y, w, h);
	}

	@Override
	public void render(float delta) {
		long vg = window.getNVGID();
		nvgSave(vg);
		nvgIntersectScissor(vg, fx, fy, fw, fh);
		if (Theme.DEBUG) {
			nvgBeginPath(vg);
			nvgRect(vg, fx, fy, fw, fh);
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugC);
			nvgStroke(vg);
		}
		for (Component component : components) {
			component.render(delta);
		}
		nvgRestore(vg);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		gridRoot.rootX = fx;
		gridRoot.rootY = fy;
		gridRoot.rootW = fw;
		gridRoot.rootH = fh;
		while (!componentsToAdd.isEmpty()) {
			Component comp = componentsToAdd.poll();
			comp.root = gridRoot;
			comp.init(window);
			components.add(comp);
		}
		while (!componentsToRemove.isEmpty()) {
			Component comp = componentsToRemove.poll();
			comp.dispose();
			components.remove(comp);
		}
		for (Component component : components) {
			component.update(delta);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		for (Component component : components) {
			component.dispose();
		}
		components.clear();
	}

	public void addComponent(Component component) {
		componentsToAdd.add(component);
	}

	public void addAllComponents(List<Component> components) {
		for (Component component : components)
			componentsToAdd.add(component);
	}

	public void removeComponent(Component component) {
		componentsToRemove.add(component);
	}

}
