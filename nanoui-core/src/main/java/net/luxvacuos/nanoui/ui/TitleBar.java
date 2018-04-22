/*
 * This file is part of NanoUI
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

import org.lwjgl.nanovg.NVGColor;

import net.luxvacuos.nanoui.core.Variables;
import net.luxvacuos.nanoui.input.MouseHandler;
import net.luxvacuos.nanoui.rendering.api.glfw.Window;
import net.luxvacuos.nanoui.rendering.api.nanovg.themes.Theme;

public class TitleBar implements ITitleBar {

	private boolean enabled = true, dragging, pressed;
	private RootComponent left, right, center;
	private Event drag;
	private float time;
	private boolean count;
	private ComponentWindow window;
	private NVGColor active = Theme.setColor("#292929FF"), inactive = Theme.setColor("#000000FF");

	public TitleBar(ComponentWindow window) {
		this.window = window;
		left = new RootComponent(0, 0, this.window.rootComponent.getWidth(), Variables.TITLEBAR_HEIGHT);
		right = new RootComponent(0, 0, this.window.rootComponent.getWidth(), Variables.TITLEBAR_HEIGHT);
		center = new RootComponent(0, 0, this.window.rootComponent.getWidth(), Variables.TITLEBAR_HEIGHT);
	}

	@Override
	public void render(Window window) {
		if (enabled) {
			if (window.isActive())
				Theme.renderTitlebar(window.getNVGID(), window.getWidth(), active);
			else
				Theme.renderTitlebar(window.getNVGID(), window.getWidth(), inactive);
			left.render(window);
			right.render(window);
			center.render(window);
		}
	}

	@Override
	public void update(float delta, Window window) {
		if (enabled) {
			MouseHandler mh = window.getMouseHandler();

			if ((mh.isButtonPressed(0) && isInside(window, mh)) || dragging) {
				dragging = mh.isButtonPressed(0);
				if (drag != null)
					drag.event(window);
			}
			if (mh.isButtonPressed(0) && isInside(window, mh) || pressed) {
				if (!pressed) {
					count = true;
					if (time != 0) {
						// this.window.toggleMaximize();
						time = 0;
						count = false;
					}
				}
				pressed = mh.isButtonPressed(0);
			}
			if (count) {
				time += 1 * delta;
				if (time > 0.5f) {
					count = false;
					time = 0;
				}
			}
			left.update(delta, window);
			right.update(delta, window);
			center.update(delta, window);
		}
	}

	@Override
	public void alwaysUpdate(float delta, Window window) {
		if (enabled) {
			float titleBarHeight = Variables.TITLEBAR_HEIGHT;
			left.alwaysUpdate(delta, window, 0, window.getHeight() - 1, window.getWidth(), titleBarHeight);
			right.alwaysUpdate(delta, window, 0, window.getHeight() - 1, window.getWidth(), titleBarHeight);
			center.alwaysUpdate(delta, window, 0, window.getHeight() - 1, window.getWidth(), titleBarHeight);
		}
	}

	@Override
	public boolean isInside(Window window, MouseHandler mh) {
		return mh.getX() >= left.getFinalW() && mh.getY() < window.getHeight()
				&& mh.getX() < window.getWidth() + right.getFinalW()
				&& mh.getY() > window.getHeight() - Variables.TITLEBAR_HEIGHT;
	}

	@Override
	public boolean isInside(Window window, int x, int y) {
		if (window.isMaximized())
			return x >= left.getFinalW() && y < Variables.TITLEBAR_HEIGHT && x < window.getWidth() + right.getFinalW()
					&& y > 0;
		else
			return x >= left.getFinalW() && y < Variables.TITLEBAR_HEIGHT && x < window.getWidth() + right.getFinalW()
					&& y > 0;
	}

	@Override
	public void setOnDrag(Event event) {
		this.drag = event;
	}

	@Override
	public RootComponent getLeft() {
		return left;
	}

	@Override
	public RootComponent getRight() {
		return right;
	}

	@Override
	public RootComponent getCenter() {
		return center;
	}

	@Override
	public void dispose(Window window) {
		left.dispose(window);
		center.dispose(window);
		right.dispose(window);
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isDragging() {
		return dragging;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void setColor(String hex) {
		active.r(Integer.valueOf(hex.substring(1, 3), 16) / 255f);
		active.g(Integer.valueOf(hex.substring(3, 5), 16) / 255f);
		active.b(Integer.valueOf(hex.substring(5, 7), 16) / 255f);
		active.a(Integer.valueOf(hex.substring(7, 9), 16) / 255f);
	}

}
