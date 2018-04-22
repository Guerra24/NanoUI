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

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;

import org.lwjgl.nanovg.NVGColor;

import net.luxvacuos.nanoui.rendering.glfw.Window;
import net.luxvacuos.nanoui.rendering.nanovg.themes.Theme;

public class Text extends Component {
	protected String text, font = "Poppins-Regular";
	protected int align = NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE;
	protected float fontSize = 24;
	protected NVGColor color = Theme.rgba(255, 255, 255, 255, NVGColor.create());

	public Text(String text, float x, float y) {
		this.text = text;
		this.x = x;
		this.y = y;
	}

	@Override
	public void render(Window window) {
		w = Theme.renderText(window.getNVGID(), text, font, align, rootComponent.rootX + alignedX,
				window.getHeight() - rootComponent.rootY - alignedY, fontSize, color) - rootComponent.rootX - alignedX;
	}

	public void setAlign(int align) {
		this.align = align;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setColor(float r, float g, float b, float a) {
		color.r(r);
		color.g(g);
		color.b(b);
		color.a(a);
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	@Override
	public void setAlignment(Alignment alignment) {
		throw new UnsupportedOperationException("Please use setAlign method for text alignment");
	}

}
