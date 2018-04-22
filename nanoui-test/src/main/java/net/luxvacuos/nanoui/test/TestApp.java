/*
 * This file is part of NanoUI
 * 
 * Copyright (C) 2017 Guerra24
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

package net.luxvacuos.nanoui.test;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import net.luxvacuos.nanoui.bootstrap.Bootstrap;
import net.luxvacuos.nanoui.core.App;
import net.luxvacuos.nanoui.core.AppUI;
import net.luxvacuos.nanoui.core.Variables;
import net.luxvacuos.nanoui.core.states.AbstractState;
import net.luxvacuos.nanoui.core.states.StateMachine;
import net.luxvacuos.nanoui.input.KeyboardHandler;
import net.luxvacuos.nanoui.rendering.nanovg.themes.Theme;
import net.luxvacuos.nanoui.ui.Button;
import net.luxvacuos.nanoui.ui.ComponentWindow;
import net.luxvacuos.nanoui.ui.EditBox;

public class TestApp extends AbstractState {

	private ComponentWindow window;

	public TestApp() {
		super("_main");
	}

	@Override
	public void start() {

		window = new ComponentWindow(AppUI.getMainWindow());
		window.initApp();

		EditBox ttbox = new EditBox(10, 10, 400, 30, "");
		window.addComponent(ttbox);
		Button btn = new Button(0,0,200,30, "Button");
		window.addComponent(btn);

		AppUI.getMainWindow().setVisible(true);
		super.start();
	}

	@Override
	public void end() {
		window.disposeApp();
		super.end();
	}

	@Override
	public void update(float delta) {
		window.updateApp(delta);
		window.alwaysUpdateApp(delta);
		KeyboardHandler kbh = AppUI.getMainWindow().getKeyboardHandler();
		if (kbh.isShiftPressed() && kbh.isKeyPressed(GLFW.GLFW_KEY_ESCAPE))
			StateMachine.stop();
	}

	@Override
	public void render(float alpha) {
		AppUI.clearBuffer(GL11.GL_COLOR_BUFFER_BIT);
		AppUI.clearColors(0f, 0f, 0f, 1);
		window.renderApp();
	}

	public static void main(String[] args) {
		new Bootstrap(args);
		Variables.WIDTH = 800;
		Variables.HEIGHT = 600;
		Variables.X = 200;
		Variables.Y = 200;
		Variables.TITLE = "";
		new App(new TestApp());
	}

}
