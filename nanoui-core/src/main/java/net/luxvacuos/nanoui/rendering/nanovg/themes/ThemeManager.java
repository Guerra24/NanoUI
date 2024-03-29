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

package net.luxvacuos.nanoui.rendering.nanovg.themes;

import java.util.HashMap;
import java.util.Map;

public class ThemeManager {

	private static Map<String, ITheme> themes = new HashMap<>();

	private ThemeManager() {
	}

	public static void addTheme(ITheme theme) {
		themes.put(theme.getName(), theme);
	}

	public static void setTheme(String name) {
		ITheme theme = themes.get(name);
		if (theme != null)
			Theme.setTheme(theme);
	}

	public static Map<String, ITheme> getThemes() {
		return themes;
	}

}
