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
	protected float x, y, w, h, fx, fy, fw, fh, mx, my, mw, mh, hx, hy, hw, hh;
	protected float ml, mt, mr, mb, pl, pt, pr, pb;
	protected ComponentState componentState = ComponentState.NONE;
	protected Window window;
	protected HorizontalAlignment horizontalAlignment = HorizontalAlignment.RIGHT;
	protected VerticalAlignment verticalAlignment = VerticalAlignment.TOP;
	boolean initialized;

	@Override
	public void init(Window window) {
		initialized = true;
		this.window = window;
	}

	@Override
	public void update(float delta) {
		componentState = ComponentState.NONE;
		switch (horizontalAlignment) {
		case LEFT:
			hx = x;
			hw = w;
			break;
		case CENTER:
			hx = x - w / 2f + root.rootW / 2f;
			hw = w;
			break;
		case RIGHT:
			hx = root.rootW - x - w;
			hw = w;
			break;
		case STRETCH:
			hx = root.rootX;
			hw = root.rootW;
			break;
		}
		switch (verticalAlignment) {
		case TOP:
			hy = y;
			hh = h;
			break;
		case CENTER:
			hy = y - h / 2f + root.rootH / 2f;
			hh = h;
			break;
		case BOTTOM:
			hy = root.rootH - y - h;
			hh = h;
			break;
		case STRETCH:
			hy = root.rootY;
			hh = root.rootH;
			break;
		}
		mx = hx + ml;
		my = hy + mt;
		mw = hw + mr;
		mh = hh + mb;
		fx = mx + pl;
		fy = my + pt;
		fw = mw - pr;
		fh = mh - pb;
	}

	@Override
	public void dispose() {
	}

	public void setMargin(float ml, float mt, float mr, float mb) {
		this.ml = ml;
		this.mt = mt;
		this.mr = mr;
		this.mb = mb;
	}

	public void setPadding(float pl, float pt, float pr, float pb) {
		this.pl = pl;
		this.pt = pt;
		this.pr = pr;
		this.pb = pb;
	}

	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}

	public enum HorizontalAlignment {
		LEFT, CENTER, RIGHT, STRETCH
	}

	public enum VerticalAlignment {
		TOP, CENTER, BOTTOM, STRETCH
	}

}
