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

package net.luxvacuos.nanoui.rendering.api.nanovg.themes;

import static net.luxvacuos.nanoui.rendering.api.nanovg.themes.Theme.colorA;
import static net.luxvacuos.nanoui.rendering.api.nanovg.themes.Theme.colorB;
import static net.luxvacuos.nanoui.rendering.api.nanovg.themes.Theme.paintA;
import static net.luxvacuos.nanoui.rendering.api.nanovg.themes.Theme.paintB;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.nanovg.NanoVG.NVG_CCW;
import static org.lwjgl.nanovg.NanoVG.NVG_CW;
import static org.lwjgl.nanovg.NanoVG.NVG_HOLE;
import static org.lwjgl.nanovg.NanoVG.NVG_PI;
import static org.lwjgl.nanovg.NanoVG.nnvgText;
import static org.lwjgl.nanovg.NanoVG.nnvgTextBreakLines;
import static org.lwjgl.nanovg.NanoVG.nvgArc;
import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgClosePath;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgFillPaint;
import static org.lwjgl.nanovg.NanoVG.nvgFontBlur;
import static org.lwjgl.nanovg.NanoVG.nvgFontFace;
import static org.lwjgl.nanovg.NanoVG.nvgFontSize;
import static org.lwjgl.nanovg.NanoVG.nvgImagePattern;
import static org.lwjgl.nanovg.NanoVG.nvgImageSize;
import static org.lwjgl.nanovg.NanoVG.nvgIntersectScissor;
import static org.lwjgl.nanovg.NanoVG.nvgLineTo;
import static org.lwjgl.nanovg.NanoVG.nvgLinearGradient;
import static org.lwjgl.nanovg.NanoVG.nvgMoveTo;
import static org.lwjgl.nanovg.NanoVG.nvgPathWinding;
import static org.lwjgl.nanovg.NanoVG.nvgRect;
import static org.lwjgl.nanovg.NanoVG.nvgRestore;
import static org.lwjgl.nanovg.NanoVG.nvgRoundedRectVarying;
import static org.lwjgl.nanovg.NanoVG.nvgSave;
import static org.lwjgl.nanovg.NanoVG.nvgScissor;
import static org.lwjgl.nanovg.NanoVG.nvgStroke;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeColor;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeWidth;
import static org.lwjgl.nanovg.NanoVG.nvgText;
import static org.lwjgl.nanovg.NanoVG.nvgTextAlign;
import static org.lwjgl.nanovg.NanoVG.nvgTextBounds;
import static org.lwjgl.nanovg.NanoVG.nvgTextMetrics;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memUTF8;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NVGTextRow;

import net.luxvacuos.nanoui.core.AppUI;
import net.luxvacuos.nanoui.core.Variables;
import net.luxvacuos.nanoui.rendering.api.nanovg.themes.Theme.ButtonStyle;

public class NanoTheme implements ITheme {

	private final FloatBuffer lineh = BufferUtils.createFloatBuffer(1);
	private final NVGTextRow.Buffer rows = NVGTextRow.create(3);

	protected NVGColor buttonColor = Theme.rgba(51, 51, 51, 255), buttonHighlight = Theme.rgba(80, 80, 80, 255),
			buttonTextColor = Theme.rgba(255, 255, 255, 255);
	protected NVGColor toggleButtonColor = Theme.setColor(1f, 1f, 1f, 1f),
			toggleButtonHighlight = Theme.setColor(0.5f, 1f, 0.5f, 1f);
	protected NVGColor contextButtonColor = Theme.setColor("#646464C8"),
			contextButtonHighlight = Theme.setColor("#FFFFFFC8");
	protected NVGColor titleBarButtonColor = Theme.setColor("#00000000"),
			titleBarButtonHighlight = Theme.setColor("#444444FF"),
			titleBarButtonCloseHighlight = Theme.setColor("#E81123FF");
	protected NVGColor flashColor = Theme.setColor("#F48642FF");

	public NanoTheme() {
	}

	@Override
	public void renderTitlebar(long vg, float w, NVGColor color) {
		nvgSave(vg);
		nvgIntersectScissor(vg, 0, 1, w, Variables.TITLEBAR_HEIGHT);
		nvgBeginPath(vg);
		nvgRect(vg, 0, 1, w, Variables.TITLEBAR_HEIGHT);
		nvgFillColor(vg, color);
		nvgFill(vg);
		if (Theme.DEBUG) {
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}
		nvgRestore(vg);
	}

	@Override
	public float renderTitleBarText(long vg, String text, String font, int align, float x, float y, float fontSize) {
		nvgSave(vg);
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, align);

		nvgFontBlur(vg, 0);
		if (AppUI.getMainWindow().isActive())
			nvgFillColor(vg, Theme.rgba(255, 255, 255, 255, colorA));
		else
			nvgFillColor(vg, Theme.rgba(102, 102, 102, 255, colorA));
		nvgText(vg, x, y, text);
		float[] bounds = new float[4];
		nvgTextBounds(vg, x, y, text, bounds);
		if (Theme.DEBUG) {
			nvgIntersectScissor(vg, bounds[0], bounds[1], bounds[2] - bounds[0], bounds[3] - bounds[1]);
			nvgBeginPath(vg);
			nvgRect(vg, bounds[0], bounds[1], bounds[2] - bounds[0], bounds[3] - bounds[1]);
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}
		nvgRestore(vg);
		return bounds[2];
	}

	@Override
	public void renderTitleBarButton(long vg, float x, float y, float w, float h, ButtonStyle style,
			boolean highlight) {
		nvgSave(vg);
		nvgIntersectScissor(vg, x, y, w, h);
		nvgBeginPath(vg);
		nvgRect(vg, x, y, w, h);
		if (highlight)
			if (style.equals(ButtonStyle.CLOSE))
				nvgFillColor(vg, titleBarButtonCloseHighlight);
			else
				nvgFillColor(vg, titleBarButtonHighlight);
		else
			nvgFillColor(vg, titleBarButtonColor);
		nvgFill(vg);

		if (Theme.DEBUG) {
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}

		switch (style) {
		case CLOSE:
			nvgFontSize(vg, 10f);
			nvgFontFace(vg, "Segoe MDL2");
			nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
			if (AppUI.getMainWindow().isActive() || highlight)
				nvgFillColor(vg, Theme.rgba(255, 255, 255, 255, colorA));
			else
				nvgFillColor(vg, Theme.rgba(102, 102, 102, 255, colorA));
			nvgText(vg, x + w * 0.5f, y + h * 0.5f, Theme.ICON_CHROME_CLOSE);
			break;
		case MAXIMIZE:
			nvgFontSize(vg, 10f);
			nvgFontFace(vg, "Segoe MDL2");
			nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
			if (AppUI.getMainWindow().isActive() || highlight)
				nvgFillColor(vg, Theme.rgba(255, 255, 255, 255, colorA));
			else
				nvgFillColor(vg, Theme.rgba(102, 102, 102, 255, colorA));
			nvgText(vg, x + w * 0.5f, y + h * 0.5f, Theme.ICON_CHROME_MAXIMIZE);
			break;
		case MINIMIZE:
			nvgFontSize(vg, 10f);
			nvgFontFace(vg, "Segoe MDL2");
			nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
			if (AppUI.getMainWindow().isActive() || highlight)
				nvgFillColor(vg, Theme.rgba(255, 255, 255, 255, colorA));
			else
				nvgFillColor(vg, Theme.rgba(102, 102, 102, 255, colorA));
			nvgText(vg, x + w * 0.5f, y + h * 0.5f + 1f, Theme.ICON_CHROME_MINIMIZE);
			break;
		case RESTORE:
			nvgFontSize(vg, 10f);
			nvgFontFace(vg, "Segoe MDL2");
			nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
			if (AppUI.getMainWindow().isActive() || highlight)
				nvgFillColor(vg, Theme.rgba(255, 255, 255, 255, colorA));
			else
				nvgFillColor(vg, Theme.rgba(102, 102, 102, 255, colorA));
			nvgText(vg, x + w * 0.5f, y + h * 0.5f, Theme.ICON_CHROME_RESTORE);
			break;
		case LEFT_ARROW:
			nvgFontSize(vg, 12f);
			nvgFontFace(vg, "Segoe MDL2");
			nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
			if (AppUI.getMainWindow().isActive() || highlight)
				nvgFillColor(vg, Theme.rgba(255, 255, 255, 255, colorA));
			else
				nvgFillColor(vg, Theme.rgba(102, 102, 102, 255, colorA));
			nvgText(vg, x + w * 0.5f, y + h * 0.5f, Theme.ICON_CHROME_BACK);
			break;
		case RIGHT_ARROW:
			nvgFontSize(vg, 12f);
			nvgFontFace(vg, "Segoe MDL2");
			nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
			if (AppUI.getMainWindow().isActive() || highlight)
				nvgFillColor(vg, Theme.rgba(255, 255, 255, 255, colorA));
			else
				nvgFillColor(vg, Theme.rgba(102, 102, 102, 255, colorA));
			nvgText(vg, x + w * 0.5f, y + h * 0.5f, Theme.ICON_CHROME_BACK_MIRRORED);
			break;
		case NONE:
			break;
		}
		nvgRestore(vg);
	}

	@Override
	public float renderText(long vg, String text, String font, int align, float x, float y, float fontSize,
			NVGColor color) {
		nvgSave(vg);
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, align);
		nvgFillColor(vg, color);
		nvgText(vg, x, y, text);
		float[] bounds = new float[4];
		nvgTextBounds(vg, x, y, text, bounds);
		if (Theme.DEBUG) {
			nvgIntersectScissor(vg, bounds[0], bounds[1], bounds[2] - bounds[0], bounds[3] - bounds[1]);
			nvgBeginPath(vg);
			nvgRect(vg, bounds[0], bounds[1], bounds[2] - bounds[0], bounds[3] - bounds[1]);
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}
		nvgRestore(vg);
		return bounds[2];
	}

	@Override
	public void renderImage(long vg, float x, float y, float w, float h, int image, float alpha) {
		NVGPaint imgPaint = paintB;
		nvgSave(vg);
		nvgIntersectScissor(vg, x, y, w, h);
		nvgImagePattern(vg, x, y, w, h, 0, image, alpha, imgPaint);
		nvgBeginPath(vg);
		nvgRect(vg, x, y, w, h);
		nvgFillPaint(vg, imgPaint);
		nvgFill(vg);
		if (Theme.DEBUG) {
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}
		nvgRestore(vg);
	}

	@Override
	public void renderImage(long vg, float x, float y, int image, float alpha) {
		NVGPaint imgPaint = paintB;
		int[] iw = new int[1], ih = new int[1];
		nvgSave(vg);
		nvgImageSize(vg, image, iw, ih);
		nvgIntersectScissor(vg, x, y, iw[0], ih[0]);
		nvgImagePattern(vg, x, y, iw[0], ih[0], 0, image, alpha, imgPaint);
		nvgBeginPath(vg);
		nvgRect(vg, x, y, iw[0], iw[0]);
		nvgFillPaint(vg, imgPaint);
		nvgFill(vg);
		if (Theme.DEBUG) {
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}
		nvgRestore(vg);
	}

	@Override
	public void renderEditBoxBase(long vg, float x, float y, float w, float h, boolean selected) {
		nvgSave(vg);
		nvgIntersectScissor(vg, x, y, w, h);
		nvgBeginPath(vg);
		nvgRect(vg, x + 1, y + 1, w - 2, h - 2);
		if (selected)
			nvgFillColor(vg, Theme.rgba(150, 150, 150, 255, colorA));
		else
			nvgFillColor(vg, Theme.rgba(100, 100, 100, 255, colorA));
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgRect(vg, x + 0.5f, y + 0.5f, w - 1, h - 1);
		if (selected)
			nvgStrokeColor(vg, Theme.rgba(50, 50, 50, 255, colorA));
		else
			nvgStrokeColor(vg, Theme.rgba(70, 70, 70, 255, colorA));
		nvgStrokeWidth(vg, 1);
		nvgStroke(vg);
		if (Theme.DEBUG) {
			nvgBeginPath(vg);
			nvgRect(vg, x, y, w, h);
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}
		nvgRestore(vg);
	}

	@Override
	public void renderEditBox(long vg, String text, String font, float x, float y, float w, float h, float fontSize,
			boolean selected) {
		float[] bounds = new float[4];
		renderEditBoxBase(vg, x, y, w, h, selected);
		nvgSave(vg);
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
		nvgFillColor(vg, buttonTextColor);
		nvgText(vg, x + h * 0.3f, y + h * 0.5f, text);
		nvgTextBounds(vg, x + h * 0.3f, y + h * 0.5f, text, bounds);
		nvgBeginPath(vg);
		if (selected) {
			nvgMoveTo(vg, bounds[2], y + 5f);
			nvgLineTo(vg, bounds[2], y + h - 5f);
			nvgStrokeColor(vg, Theme.rgba(0, 0, 0, 255, colorA));
			nvgStroke(vg);
		}
		if (Theme.DEBUG) {
			nvgBeginPath(vg);
			nvgRect(vg, x, y, w, h);
			nvgIntersectScissor(vg, bounds[0], bounds[1], bounds[2] - bounds[0], bounds[3] - bounds[1]);
			nvgRect(vg, bounds[0], bounds[1], bounds[2] - bounds[0], bounds[3] - bounds[1]);
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}
		nvgRestore(vg);
	}

	@Override
	public void renderButton(long vg, String preicon, String text, String font, String entypo, float x, float y,
			float w, float h, boolean highlight, float fontSize, float preiconSize) {
		float tw, iw = 0;
		nvgSave(vg);
		nvgIntersectScissor(vg, x, y, w, h);
		nvgBeginPath(vg);
		nvgRect(vg, x, y, w, h);
		if (highlight)
			nvgFillColor(vg, buttonHighlight);
		else
			nvgFillColor(vg, buttonColor);
		nvgFill(vg);

		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		tw = nvgTextBounds(vg, 0, 0, text, (FloatBuffer) null);
		if (preicon != null) {
			nvgFontSize(vg, preiconSize);
			nvgFontFace(vg, entypo);
			iw = nvgTextBounds(vg, 0, 0, preicon, (FloatBuffer) null);
			iw += h * 0.15f;
		}
		float[] preiconBounds = new float[4];
		if (preicon != null) {
			nvgFontSize(vg, preiconSize);
			nvgFontFace(vg, entypo);
			nvgFillColor(vg, buttonTextColor);
			if (text.isEmpty()) {
				nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
				nvgText(vg, x + w * 0.5f, y + h * 0.5f, preicon);
				nvgTextBounds(vg, x + w * 0.5f, y + h * 0.5f, preicon, preiconBounds);
			} else {
				nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
				nvgText(vg, x + w * 0.5f - tw * 0.5f - iw * 0.75f, y + h * 0.5f, preicon);
				nvgTextBounds(vg, x + w * 0.5f - tw * 0.5f - iw * 0.75f, y + h * 0.5f, preicon, preiconBounds);
			}
		}
		float[] bounds = new float[4];

		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
		nvgFillColor(vg, buttonTextColor);
		nvgTextBounds(vg, x + w * 0.5f - tw * 0.5f + iw * 0.25f, y + h * 0.5f, text, bounds);
		nvgText(vg, x + w * 0.5f - tw * 0.5f + iw * 0.25f, y + h * 0.5f, text);
		if (Theme.DEBUG) {
			nvgBeginPath(vg);
			nvgRect(vg, x, y, w, h);
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);

			nvgBeginPath(vg);
			nvgMoveTo(vg, bounds[0], bounds[1]);
			nvgLineTo(vg, bounds[2], bounds[1]);
			nvgLineTo(vg, bounds[2], bounds[3]);
			nvgLineTo(vg, bounds[0], bounds[3]);
			nvgLineTo(vg, bounds[0], bounds[1]);
			if (preicon != null) {
				nvgMoveTo(vg, preiconBounds[0], preiconBounds[1]);
				nvgLineTo(vg, preiconBounds[2], preiconBounds[1]);
				nvgLineTo(vg, preiconBounds[2], preiconBounds[3]);
				nvgLineTo(vg, preiconBounds[0], preiconBounds[3]);
				nvgLineTo(vg, preiconBounds[0], preiconBounds[1]);
			}
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugC);
			nvgStroke(vg);
		}
		nvgRestore(vg);
	}

	@Override
	public void renderContexMenuButton(long vg, String text, String font, float x, float y, float w, float h,
			float fontSize, boolean highlight) {
		nvgSave(vg);

		nvgBeginPath(vg);
		nvgRect(vg, x, y, w, h);
		if (highlight)
			nvgFillColor(vg, contextButtonHighlight);
		else
			nvgFillColor(vg, contextButtonColor);
		nvgFill(vg);

		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
		nvgFillColor(vg, Theme.rgba(255, 255, 255, 255, colorA));
		nvgText(vg, x + 10f, y + h * 0.5f, text);
		nvgRestore(vg);
	}

	@Override
	public void renderToggleButton(long vg, String text, String font, float x, float y, float w, float h,
			float fontSize, boolean status) {
		nvgSave(vg);
		nvgIntersectScissor(vg, x, y, w, h);
		nvgBeginPath(vg);
		nvgRect(vg, x, y, w, h);
		if (status)
			nvgFillColor(vg, Theme.setColor(toggleButtonHighlight.r() - 0.4f, toggleButtonHighlight.g() - 0.4f,
					toggleButtonHighlight.b() - 0.4f, 1f, colorA));
		else
			nvgFillColor(vg, Theme.rgba(0, 0, 0, 255, colorA));
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgRect(vg, x + 3, y + 3, w - 6, h - 6);
		nvgPathWinding(vg, NVG_HOLE);
		nvgRect(vg, x, y, w, h);
		if (status)
			nvgFillColor(vg, toggleButtonHighlight);
		else
			nvgFillColor(vg, toggleButtonColor);
		nvgFill(vg);

		nvgBeginPath(vg);
		if (status)
			nvgRect(vg, x + w - h + 5, y + 5, h - 10, h - 10);
		else
			nvgRect(vg, x + 5, y + 5, h - 10, h - 10);
		nvgFillColor(vg, toggleButtonColor);
		nvgFill(vg);
		if (Theme.DEBUG) {
			nvgBeginPath(vg);
			nvgRect(vg, x, y, w, h);
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}
		nvgRestore(vg);
	}

	@Override
	public void renderSpinner(long vg, float cx, float cy, float r, float t) {
		float a0 = 0.0f + t * 6;
		float a1 = NVG_PI + t * 6;
		float r0 = r;
		float r1 = r * 0.75f;
		float ax, ay, bx, by;
		NVGPaint paint = paintA;

		nvgSave(vg);
		nvgBeginPath(vg);
		nvgArc(vg, cx, cy, r0, a0, a1, NVG_CW);
		nvgArc(vg, cx, cy, r1, a1, a0, NVG_CCW);
		nvgClosePath(vg);
		ax = cx + (float) Math.cos(a0) * (r0 + r1) * 0.5f;
		ay = cy + (float) Math.sin(a0) * (r0 + r1) * 0.5f;
		bx = cx + (float) Math.cos(a1) * (r0 + r1) * 0.5f;
		by = cy + (float) Math.sin(a1) * (r0 + r1) * 0.5f;
		nvgLinearGradient(vg, ax, ay, bx, by, Theme.rgba(0, 0, 0, 0, colorA), Theme.rgba(0, 0, 0, 128, colorB), paint);
		nvgFillPaint(vg, paint);
		nvgFill(vg);

		nvgRestore(vg);
	}

	@Override
	public float renderParagraph(long vg, float x, float y, float width, float fontSize, String font, String text,
			int align, NVGColor color) {
		if (text == null)
			text = "";
		ByteBuffer paragraph = memUTF8(text);

		nvgSave(vg);
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, align);
		nvgTextMetrics(vg, null, null, lineh);

		long start = memAddress(paragraph);
		long end = start + paragraph.remaining();
		int nrows;
		float yy = y;
		while ((nrows = nnvgTextBreakLines(vg, start, end, width, memAddress(rows), 3)) != 0) {
			for (int i = 0; i < nrows; i++) {
				NVGTextRow row = rows.get(i);
				nvgFillColor(vg, color);
				nnvgText(vg, x, yy, row.start(), row.end());
				yy += lineh.get(0);
			}
			start = rows.get(nrows - 1).next();
		}
		if (Theme.DEBUG) {
			nvgIntersectScissor(vg, x, y, width, yy - y);
			nvgBeginPath(vg);
			nvgRect(vg, x, y, width, yy - y);
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}
		nvgRestore(vg);
		return yy - y;
	}

	@Override
	public void renderBox(long vg, float x, float y, float w, float h, NVGColor color, float rt, float lt, float rb,
			float lb) {
		nvgSave(vg);
		nvgIntersectScissor(vg, x, y, w, h);
		nvgBeginPath(vg);
		nvgRoundedRectVarying(vg, x, y, w, h, lt, rt, lb, rb);
		nvgFillColor(vg, color);
		nvgFill(vg);
		if (Theme.DEBUG) {
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}
		nvgRestore(vg);
	}

	@Override
	public void renderSlider(long vg, float pos, float x, float y, float w, float h) {

		nvgSave(vg);
		// Slot
		nvgBeginPath(vg);
		nvgRect(vg, x - 6, y, w + 12, h);
		nvgFillColor(vg, Theme.rgba(71, 71, 71, 255, colorA));
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgRect(vg, x + 0.5f - 6, y + 0.5f, w - 1 + 12, h - 1);
		nvgStrokeColor(vg, Theme.rgba(0, 0, 0, 255, colorA));
		nvgStroke(vg);

		// Knob
		nvgBeginPath(vg);
		nvgRect(vg, x + (pos * w) - 5, y + 1, 10, h - 2);
		nvgFillColor(vg, Theme.rgba(200, 200, 200, 255, colorB));
		nvgFill(vg);
		if (Theme.DEBUG) {
			nvgBeginPath(vg);
			nvgRect(vg, x, y, w, h);
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}
		nvgRestore(vg);
	}

	@Override
	public void renderScrollBarV(long vg, float x, float y, float w, float h, float pos, float sizeV) {
		float scrollBarSize = Variables.SCROLLBAR_SIZE;

		nvgSave(vg);
		nvgIntersectScissor(vg, x, y, w, h);

		nvgSave(vg);
		nvgIntersectScissor(vg, x + w - scrollBarSize, y + scrollBarSize, scrollBarSize, h - scrollBarSize * 2f);
		nvgBeginPath(vg);
		nvgRect(vg, x + w - scrollBarSize, y + scrollBarSize, scrollBarSize, h - scrollBarSize * 2f);
		nvgFillColor(vg, Theme.rgba(128, 128, 128, 140, colorB));
		nvgFill(vg);

		if (Theme.DEBUG) {
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}
		nvgRestore(vg);

		nvgSave(vg);
		nvgIntersectScissor(vg, x + w - scrollBarSize, y + scrollBarSize + pos * (h - scrollBarSize * 2f - sizeV),
				scrollBarSize, sizeV);
		nvgBeginPath(vg);
		nvgRect(vg, x + w - scrollBarSize, y + scrollBarSize + pos * (h - scrollBarSize * 2f - sizeV), scrollBarSize,
				sizeV);
		nvgFillColor(vg, Theme.rgba(220, 220, 220, 255, colorB));
		nvgFill(vg);
		if (Theme.DEBUG) {
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}
		nvgRestore(vg);

		nvgSave(vg);
		nvgIntersectScissor(vg, x + w - scrollBarSize, y, scrollBarSize, scrollBarSize);
		nvgBeginPath(vg);
		nvgRect(vg, x + w - scrollBarSize, y, scrollBarSize, scrollBarSize);
		nvgFillColor(vg, Theme.rgba(80, 80, 80, 140, colorB));
		nvgFill(vg);
		if (Theme.DEBUG) {
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}
		nvgRestore(vg);

		nvgSave(vg);
		nvgIntersectScissor(vg, x + w - scrollBarSize, y + h - scrollBarSize, scrollBarSize, scrollBarSize);
		nvgBeginPath(vg);
		nvgRect(vg, x + w - scrollBarSize, y + h - scrollBarSize, scrollBarSize, scrollBarSize);
		nvgFillColor(vg, Theme.rgba(80, 80, 80, 140, colorB));
		nvgFill(vg);

		if (Theme.DEBUG) {
			nvgStrokeWidth(vg, Theme.DEBUG_STROKE);
			nvgStrokeColor(vg, Theme.debugB);
			nvgStroke(vg);
		}
		nvgRestore(vg);

		nvgBeginPath(vg);
		nvgMoveTo(vg, x + w - scrollBarSize / 2 - 6, y + scrollBarSize / 2 + 4);
		nvgLineTo(vg, x + w - scrollBarSize / 2, y + scrollBarSize / 2 - 4);
		nvgLineTo(vg, x + w - scrollBarSize / 2 + 6, y + scrollBarSize / 2 + 4);
		nvgMoveTo(vg, x + w - scrollBarSize / 2 - 6, y + h - scrollBarSize / 2 - 4);
		nvgLineTo(vg, x + w - scrollBarSize / 2, y + h - scrollBarSize / 2 + 4);
		nvgLineTo(vg, x + w - scrollBarSize / 2 + 6, y + h - scrollBarSize / 2 - 4);
		nvgStrokeColor(vg, Theme.rgba(0, 0, 0, 255, colorA));
		nvgStroke(vg);

		nvgRestore(vg);
	}

	@Override
	public void renderDropDownButton(long vg, float x, float y, float w, float h, float fontSize, String font,
			String entypo, String text, boolean inside) {
		nvgBeginPath(vg);
		nvgRect(vg, x + 1, y + 1, w - 2, h - 2);
		if (inside)
			nvgFillColor(vg, buttonHighlight);
		else
			nvgFillColor(vg, buttonColor);
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgRect(vg, x + 0.5f, y + 0.5f, w - 1, h - 1);
		nvgStrokeColor(vg, Theme.rgba(0, 0, 0, 100, colorA));
		nvgStroke(vg);

		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgFillColor(vg, buttonTextColor);
		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
		nvgText(vg, x + h * 0.3f, y + h * 0.5f, text);

		nvgFontSize(vg, h * 1.3f);
		nvgFontFace(vg, entypo);
		nvgFillColor(vg, Theme.rgba(100, 100, 100, 96, colorA));
		nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
		nvgText(vg, x + w - h * 0.5f, y + h * 0.5f, Theme.ICON_CHEVRON_RIGHT);
	}

	@Override
	public void renderTaskbarWindowButton(long vg, String preicon, String text, String font, String entypo, float x,
			float y, float w, float h, boolean highlight, boolean active, boolean flash, float fontSize) {
		float tw, iw = 0;
		nvgSave(vg);

		nvgBeginPath(vg);
		nvgRect(vg, x + 1, y + 1, w - 2, h - 2);
		if (highlight)
			nvgFillColor(vg, buttonHighlight);
		else
			nvgFillColor(vg, buttonColor);
		nvgFill(vg);
		
		if (flash) {
			nvgBeginPath(vg);
			nvgRect(vg, x + 1, y + 1, w - 2, h - 2);
			nvgFillColor(vg, flashColor);
			nvgFill(vg);
		}

		nvgBeginPath(vg);
		nvgRect(vg, x + 1, y + h - 4, w - 2, 3);
		nvgFillColor(vg, Theme.rgba(255, 255, 255, 255, colorA));
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgRect(vg, x + 0.5f, y + 0.5f, w - 1, h - 1);
		nvgStrokeColor(vg, Theme.rgba(0, 0, 0, 100, colorA));
		nvgStroke(vg);

		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		tw = nvgTextBounds(vg, 0, 0, text, (FloatBuffer) null);
		if (preicon != null) {
			nvgFontSize(vg, h * 0.5f);
			nvgFontFace(vg, entypo);
			iw = nvgTextBounds(vg, 0, 0, preicon, (FloatBuffer) null);
			iw += h * 0.15f;
		}

		if (preicon != null) {
			nvgFontSize(vg, h * 0.5f);
			nvgFontFace(vg, entypo);
			nvgFillColor(vg, buttonTextColor);
			if (text.isEmpty()) {
				nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
				nvgText(vg, x + w * 0.5f, y + h * 0.5f, preicon);
			} else {
				nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
				nvgText(vg, x + w * 0.5f - tw * 0.5f - iw * 0.75f, y + h * 0.5f, preicon);
			}
		}

		nvgSave(vg);
		nvgScissor(vg, x, y, w, h);
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
		nvgFillColor(vg, buttonTextColor);
		nvgText(vg, x + w * 0.5f - tw * 0.5f + iw * 0.25f, y + h * 0.5f, text);
		nvgRestore(vg);
		nvgRestore(vg);
	}

}
