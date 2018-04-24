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

package net.luxvacuos.nanoui.rendering;

import static org.lwjgl.nanovg.NanoVG.nvgCreateFontMem;
import static org.lwjgl.nanovg.NanoVG.nvgCreateImageMem;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import net.luxvacuos.nanoui.core.exception.LoadTextureException;
import net.luxvacuos.nanoui.rendering.glfw.AbstractWindow;
import net.luxvacuos.nanoui.rendering.objects.RawModel;
import net.luxvacuos.nanoui.ui.Font;

/**
 * This objects handles all loading methods from any type of data, models,
 * textures, fonts, etc.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class NoMTResourceLoader implements IResourceLoader {
	private AbstractWindow window;

	public NoMTResourceLoader(AbstractWindow abstractWindow) {
		this.window = abstractWindow;
	}

	@Override
	public RawModel loadToVAO(float[] positions, int dimensions) {
		return null;
	}

	@Override
	public Font loadNVGFont(String filename, String name) {
		return loadNVGFont(filename, name, 150);
	}

	@Override
	public Font loadNVGFont(String filename, String name, int size) {
		ByteBuffer buffer;
		int font;
		try {
			buffer = ioResourceToByteBuffer("assets/fonts/" + filename + ".ttf", size * 1024);
			font = nvgCreateFontMem(window.getNVGID(), name, buffer, 0);
			return new Font(name, buffer, font);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int loadNVGTexture(String file) {
		ByteBuffer buffer;
		int tex;
		try {
			buffer = ioResourceToByteBuffer("assets/textures/menu/" + file + ".png", 1024 * 1024);
			tex = nvgCreateImageMem(window.getNVGID(), 0, buffer);
		} catch (Exception e) {
			throw new LoadTextureException(file, e);
		}
		return tex;
	}

	/**
	 * Reads the specified resource and returns the raw data as a ByteBuffer.
	 *
	 * @param resource
	 *            the resource to read
	 * @param bufferSize
	 *            the initial buffer size
	 *
	 * @return the resource data
	 *
	 * @throws IOException
	 *             if an IO error occurs
	 */
	public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
		ByteBuffer buffer;

		File file = new File(resource);
		if (file.isFile()) {
			FileInputStream fis = new FileInputStream(file);
			FileChannel fc = fis.getChannel();

			buffer = memAlloc((int) fc.size() + 1);

			while (fc.read(buffer) != -1)
				;

			fis.close();
			fc.close();
		} else {
			buffer = memAlloc(bufferSize);
			try (InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
				if (source == null)
					throw new FileNotFoundException(resource);
				try (ReadableByteChannel rbc = Channels.newChannel(source)) {
					while (true) {
						int bytes = rbc.read(buffer);
						if (bytes == -1)
							break;
						if (buffer.remaining() == 0)
							buffer = resizeBuffer(buffer, buffer.capacity() * 2);
					}
				}
			}
		}
		buffer.put((byte) 0);
		buffer.flip();
		return buffer;
	}

	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = memAlloc(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		memFree(buffer);
		return newBuffer;
	}

	@Override
	public void dispose() {
	}

}