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

import net.luxvacuos.nanoui.rendering.glfw.Window;
import net.luxvacuos.nanoui.rendering.nanovg.themes.Theme;

public class Page {

	private List<Component> components = new ArrayList<>();
	private Queue<Component> componentsToAdd = new LinkedList<>(), componentsToRemove = new LinkedList<>();

	protected Root root;
	private Window window;

	public Page() {
	}

	public void init(Window window) {
		this.window = window;
	}

	public void render(float delta) {
		long vg = window.getNVGID();
		window.setViewport(0, 0, (int) root.rootW, (int) root.rootH);
		window.beingNVGFrame();
		nvgSave(vg);
		nvgIntersectScissor(vg, root.rootX, root.rootY, root.rootW, root.rootH);
		if (Theme.DEBUG) {
			nvgBeginPath(vg);
			nvgRect(vg, root.rootX, root.rootY, root.rootW, root.rootH);
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugE);
			nvgStroke(vg);
		}
		for (Component component : components) {
			component.render(delta);
		}
		nvgRestore(vg);
		window.endNVGFrame();
	}

	public void update(float delta) {
		while (!componentsToAdd.isEmpty()) {
			Component comp = componentsToAdd.poll();
			comp.root = root;
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

	public void dispose() {
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
