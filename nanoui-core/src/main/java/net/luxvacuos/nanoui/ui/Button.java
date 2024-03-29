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

public class Button extends Component {

	protected String text = "missigno", font = "Poppins-Regular", entypo = "Entypo";
	protected String preicon;
	protected OnAction onPress, rightPress;
	protected float fontSize = 22;
	protected boolean pressed = false, pressedRight = false;

	public Button(float x, float y, float w, float h, String text) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.text = text;
	}

	@Override
	public void render(float delta) {
		Theme.renderButton(window.getNVGID(), componentState, preicon, text, font, entypo, root.rootX + fx,
				root.rootY + fy, w, h, false, fontSize);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		MouseHandler mh = window.getMouseHandler();
		if (insideButton(mh)) {
			componentState = ComponentState.HOVER;
			if (pressed)
				componentState = ComponentState.PRESSED;
		}
		if (onPress != null)
			if (pressed(mh) || pressed) {
				if (!pressed(mh) && pressed)
					onPress.onAction();
				pressed = pressed(mh);
			}
		if (rightPress != null)
			if (pressedRight(mh) || pressedRight) {
				if (!pressedRight(mh) && pressedRight)
					rightPress.onAction();
				pressedRight = pressedRight(mh);
			}

	}

	public boolean insideButton(MouseHandler mh) {
		return mh.getX() >= root.rootX + fx && mh.getY() > root.rootY + fy && mh.getX() < root.rootX + fx + w
				&& mh.getY() <= root.rootY + fy + h;
	}

	public boolean pressed(MouseHandler mh) {
		if (insideButton(mh))
			return mh.isButtonPressed(0);
		else
			return false;
	}

	public boolean pressedRight(MouseHandler mh) {
		if (insideButton(mh))
			return mh.isButtonPressed(1);
		else
			return false;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setOnButtonPress(OnAction onPress) {
		this.onPress = onPress;
	}

	public void setOnButtonRightPress(OnAction rightPress) {
		this.rightPress = rightPress;
	}

	public void setEntypo(String entypo) {
		this.entypo = entypo;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public void setPreicon(String preicon) {
		this.preicon = preicon;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

}
