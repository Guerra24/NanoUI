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

package net.luxvacuos.nanoui.framehost.ui;

import net.luxvacuos.nanoui.rendering.nanovg.themes.Theme;
import net.luxvacuos.nanoui.rendering.nanovg.themes.Theme.ButtonStyle;
import net.luxvacuos.nanoui.ui.Button;

public class TitleBarButton extends Button {

	private ButtonStyle style = ButtonStyle.NONE;

	public TitleBarButton(float x, float y, float w, float h) {
		super(x, y, w, h, "");
	}

	@Override
	public void render(float delta) {
		Theme.renderTitleBarButton(window.getNVGID(), componentState, root.rootX + fx, root.rootY + fy, w, h, style,
				false);
	}

	public void setStyle(ButtonStyle style) {
		this.style = style;
	}

}
