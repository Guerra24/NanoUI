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

package net.luxvacuos.nanoui.core.exception;

public class LoadShaderException extends RuntimeException {

	private static final long serialVersionUID = -5645376169749026843L;

	public LoadShaderException() {
		super();
	}

	public LoadShaderException(String error) {
		super(error);
	}

	public LoadShaderException(Exception e) {
		super(e);
	}

	public LoadShaderException(Throwable cause) {
		super(cause);
	}

	public LoadShaderException(String message, Throwable cause) {
		super(message, cause);
	}

}
