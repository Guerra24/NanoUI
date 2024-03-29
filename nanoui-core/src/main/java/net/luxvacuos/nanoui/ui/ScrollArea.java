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

import static org.lwjgl.nanovg.NanoVG.nvgIntersectScissor;
import static org.lwjgl.nanovg.NanoVG.nvgRestore;
import static org.lwjgl.nanovg.NanoVG.nvgSave;

import net.luxvacuos.nanoui.core.Variables;
import net.luxvacuos.nanoui.input.MouseHandler;
import net.luxvacuos.nanoui.rendering.glfw.Window;
import net.luxvacuos.nanoui.rendering.nanovg.themes.Theme;
import net.luxvacuos.nanoui.util.Maths;

public class ScrollArea extends Component {

	protected RootComponent comp;
	protected float maxW, maxH, scrollW, scrollH;

	private boolean moveV;

	public ScrollArea(float x, float y, float w, float h, float maxW, float maxH) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.maxW = maxW;
		this.maxH = maxH;
		comp = new RootComponent(x, y - h, w, h);
		super.resizeH = true;
		super.resizeV = true;
	}

	@Override
	public void init(Window window) {
		comp.init(window);
		super.init(window);
	}

	@Override
	public void render(Window window) {
		Theme.renderBox(window.getNVGID(), rootComponent.rootX + alignedX,
				window.getHeight() - rootComponent.rootY - alignedY - h, w, h,
				Theme.setColor(0.6f, 0.6f, 0.6f, 0f, Theme.colorB), 0, 0, 0, 0);
		nvgSave(window.getNVGID());
		nvgIntersectScissor(window.getNVGID(), rootComponent.rootX + alignedX,
				window.getHeight() - rootComponent.rootY - alignedY - h, w, h);
		comp.renderManual();
		nvgRestore(window.getNVGID());
		Theme.renderScrollBarV(window.getNVGID(), componentState, rootComponent.rootX + alignedX,
				window.getHeight() - rootComponent.rootY - alignedY - h, w, h, scrollH / maxH,
				Maths.clamp(h - maxH, 20));
	}

	@Override
	public void update(float delta, Window window) {
		super.update(delta, window);
		comp.update(delta);
		MouseHandler mh = window.getMouseHandler();
		int scrollBarSize = Variables.SCROLLBAR_SIZE;
		if (mh.isButtonPressed(0)) {
			if (mh.getX() > rootComponent.rootX + alignedX + w - scrollBarSize
					&& mh.getX() < rootComponent.rootX + alignedX + w
					&& mh.getY() > rootComponent.rootY + alignedY + h - scrollBarSize
					&& mh.getY() < rootComponent.rootY + alignedY + h) {
				scrollH -= 200 * delta;
			}
			if (mh.getX() > rootComponent.rootX + alignedX + w - scrollBarSize
					&& mh.getX() < rootComponent.rootX + alignedX + w && mh.getY() > rootComponent.rootY + alignedY
					&& mh.getY() < rootComponent.rootY + alignedY + scrollBarSize) {
				scrollH += 200 * delta;
			}
		}
		if ((mh.isButtonPressed(0) && scrollBarV(scrollBarSize, mh)) || moveV) {
			moveV = mh.isButtonPressed(0);
			scrollH -= mh.getDY();
		}
		scrollH -= mh.getYWheel() * 16;
		scrollH = Maths.clamp(scrollH, 0, maxH);
	}

	@Override
	public void alwaysUpdate(float delta, Window window) {
		super.alwaysUpdate(delta, window);
		comp.alwaysUpdate(delta, rootComponent.rootX + alignedX, rootComponent.rootY - alignedY + h + scrollH, w, h);
		maxH = Maths.clamp(-h + -comp.getFinalH(), 0);
	}

	@Override
	public void dispose(Window window) {
		comp.dispose();
		super.dispose(window);
	}

	public void setLayout(ILayout layout) {
		comp.setLayout(layout);
	}

	private boolean scrollBarV(float scrollBarSize, MouseHandler mh) {
		float scrollv = (h / (maxH + h / 2f)) * (h / 2);
		return mh.getX() > rootComponent.rootX + alignedX + w - scrollBarSize
				&& mh.getX() < rootComponent.rootX + alignedX + w - scrollBarSize + scrollBarSize
				&& mh.getY() > rootComponent.rootY + alignedY + scrollBarSize
						+ (h - 8 - scrollv) * (1 - (scrollH / maxH))
				&& mh.getY() < rootComponent.rootY + alignedY + scrollBarSize
						+ (h - 8 - scrollv) * (1 - (scrollH / maxH)) + scrollv - scrollBarSize * 2f + 8;
	}

	public void addComponent(Component component) {
		comp.addComponent(component);
	}

}
