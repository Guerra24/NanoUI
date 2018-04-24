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

import static net.luxvacuos.nanoui.framehost.Variables.BORDER_SIZE;
import static net.luxvacuos.nanoui.framehost.Variables.TITLEBAR_SIZE;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;

import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.XTextProperty;

import net.luxvacuos.nanoui.framehost.FrameHost;
import net.luxvacuos.nanoui.framehost.FrameWindow;
import net.luxvacuos.nanoui.rendering.glfw.Window;
import net.luxvacuos.nanoui.rendering.nanovg.themes.Theme.ButtonStyle;
import net.luxvacuos.nanoui.ui.Component.HorizontalAlignment;
import net.luxvacuos.nanoui.ui.Component.VerticalAlignment;
import net.luxvacuos.nanoui.ui.Grid;
import net.luxvacuos.nanoui.ui.Page;

public class FramePage extends Page {

	private FrameWindow frame;

	public FramePage(FrameWindow frame) {
		this.frame = frame;
	}

	@Override
	public void init(Window window) {
		super.init(window);
		Grid grid = new Grid(0, 0, root.rootW - BORDER_SIZE * 2, TITLEBAR_SIZE);
		grid.setHorizontalAlignment(HorizontalAlignment.STRETCH);
		grid.setMargin(BORDER_SIZE, BORDER_SIZE, 0, 0);
		grid.setPadding(0, 0, BORDER_SIZE * 2, 0);

		TitleBarButton btnClose = new TitleBarButton(0, 0, 30, 30);
		btnClose.setStyle(ButtonStyle.CLOSE);
		btnClose.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		btnClose.setOnButtonPress(() -> {
			frame.closeWindow();
		});
		grid.addComponent(btnClose);

		TitleBarButton btnMax = new TitleBarButton(31, 0, 30, 30);
		btnMax.setStyle(ButtonStyle.MAXIMIZE);
		btnMax.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		btnMax.setOnButtonPress(() -> {
			frame.toggleMaximize();
			if (frame.isMaximized())
				btnMax.setStyle(ButtonStyle.RESTORE);
			else
				btnMax.setStyle(ButtonStyle.MAXIMIZE);
		});
		if (frame.isResizable())
			grid.addComponent(btnMax);

		TitleBarButton btnMin = new TitleBarButton(62, 0, 30, 30);
		btnMin.setStyle(ButtonStyle.MINIMIZE);
		btnMin.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		btnMin.setOnButtonPress(() -> {
		});
		grid.addComponent(btnMin);

		XTextProperty t = new XTextProperty();
		X11.INSTANCE.XGetWMName(FrameHost.getDisplay(), frame.getNativeWindow(), t);

		TitleBarText title = new TitleBarText(t.value, 0, 0);
		title.setVerticalAlignment(VerticalAlignment.CENTER);
		title.setHorizontalAlignment(HorizontalAlignment.CENTER);
		title.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);

		grid.addComponent(title);

		super.addComponent(grid);
	}

}
