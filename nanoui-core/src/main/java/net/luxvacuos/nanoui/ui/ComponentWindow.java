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

import java.util.List;

import net.luxvacuos.nanoui.rendering.glfw.Window;

public class ComponentWindow {

	protected RootComponent rootComponent;
	protected Window window;

	public ComponentWindow(Window window) {
		this.window = window;
		rootComponent = new RootComponent(0, window.getHeight(), window.getWidth(), window.getHeight());
	}

	public void initApp() {
		rootComponent.init(window);
	}

	public void renderApp() {
		window.beingNVGFrame();
		rootComponent.render();
		window.endNVGFrame();
	}

	public void updateApp(float delta) {
		rootComponent.update(delta);
	}

	public void alwaysUpdateApp(float delta) {
		rootComponent.alwaysUpdate(delta, 0, window.getHeight(), window.getWidth(), window.getHeight());
	}

	public void disposeApp() {
		rootComponent.dispose();
	}

	public void addComponent(Component component) {
		rootComponent.addComponent(component);
	}

	public void addAllComponents(List<Component> components) {
		rootComponent.addAllComponents(components);
	}

	public void setLayout(ILayout layout) {
		rootComponent.setLayout(layout);
	}

	public void removeComponent(Component component) {
		rootComponent.removeComponent(component);
	}

	public float getFinalW() {
		return rootComponent.getFinalW();
	}

	public float getFinalH() {
		return rootComponent.getFinalH();
	}

}
