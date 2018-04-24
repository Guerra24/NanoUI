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

package net.luxvacuos.nanoui.compositor;

import net.luxvacuos.nanoui.core.Application;
import net.luxvacuos.nanoui.ui.Button;
import net.luxvacuos.nanoui.ui.Page;

public class TestApp extends Application {

	public static void main(String[] args) {
		new TestApp();
	}

	@Override
	public void onLaunched() {
		Page p = new Page();
		p.addComponent(new Button(0, 0, 200, 20, "Test Btn"));
		super.currentFrame.navigate(p);
		super.setWindowTitle("TestApp");
	}

	@Override
	public void onClosed() {
	}

}
