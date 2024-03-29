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

package net.luxvacuos.nanoui.core;

import net.luxvacuos.nanoui.rendering.glfw.RenderingAPI;

public class Variables {
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	public static boolean DEBUG = false;
	public static int SCROLLBAR_SIZE = 16;
	public static boolean GLES = false;
	public static String THEME = "Nano";
	
	public static RenderingAPI api;
	
	public static int UPS = 60;
	public static int FPS = 60;
	
}
