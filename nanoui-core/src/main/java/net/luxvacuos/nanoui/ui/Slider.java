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

import net.luxvacuos.nanoui.input.MouseHandler;
import net.luxvacuos.nanoui.rendering.nanovg.themes.Theme;
import net.luxvacuos.nanoui.util.Maths;

public class Slider extends Component {

	private float pos, precision;
	private OnAction onPress;
	private boolean customPrecision, move;

	public Slider(float x, float y, float w, float h, float position) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.pos = position;
	}

	@Override
	public void update(float delta) {
		MouseHandler mh = window.getMouseHandler();
		if ((mh.isButtonPressed(0) && insideSlider(mh)) || move) {
			move = mh.isButtonPressed(0);
			pos = (mh.getX() - root.rootX - fx) / w;
			if (customPrecision)
				pos = (float) (Math.floor(pos * precision) / precision);
			pos = Maths.clamp(pos, 0, 1);
			if (onPress != null)
				onPress.onAction();
		}
		super.update(delta);
	}

	@Override
	public void render(float delta) {
		Theme.renderSlider(window.getNVGID(), componentState, pos, root.rootX + fx, root.rootY + fy, w, h);
	}

	public boolean insideSlider(MouseHandler mh) {
		return mh.getX() > root.rootX + fx - 6 && mh.getY() > root.rootY + y && mh.getX() < root.rootX + fx + w + 6
				&& mh.getY() < root.rootY + fy + h;
	}

	public void setOnPress(OnAction onPress) {
		this.onPress = onPress;
	}

	public void useCustomPrecision(boolean customPrecision) {
		this.customPrecision = customPrecision;
	}

	public void setPrecision(float precision) {
		this.precision = precision;
	}

	public float getPosition() {
		return pos;
	}

}
