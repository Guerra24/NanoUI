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

package net.luxvacuos.nanoui.rendering.opengl;

import static org.lwjgl.nanovg.NanoVG.nvgCreateFontMem;
import static org.lwjgl.nanovg.NanoVG.nvgCreateImageMem;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
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
import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.nanoui.core.Application;
import net.luxvacuos.nanoui.core.TaskManager;
import net.luxvacuos.nanoui.core.exception.LoadTextureException;
import net.luxvacuos.nanoui.rendering.IResourceLoader;
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
public class GLResourceLoader implements IResourceLoader {
	/**
	 * VAOs List
	 */
	private List<Integer> vaos = new ArrayList<Integer>();
	/**
	 * VBOs List
	 */
	private List<Integer> vbos = new ArrayList<Integer>();
	private AbstractWindow window;

	public GLResourceLoader(AbstractWindow abstractWindow) {
		this.window = abstractWindow;
	}

	/**
	 * Load an array of positions and a dimension
	 * 
	 * @param positions
	 *            Array of Positions
	 * @param dimensions
	 *            Dimension
	 * @return RawModel
	 */
	@Override
	public RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}

	@Override
	public Font loadNVGFont(String filename, String name) {
		return loadNVGFont(filename, name, 150);
	}

	@Override
	public Font loadNVGFont(String filename, String name, int size) {
		int font[] = new int[1];
		ByteBuffer buffer[] = new ByteBuffer[1];
		try {
			buffer[0] = ioResourceToByteBuffer("assets/fonts/" + filename + ".ttf", size * 1024);

			if (isThread(Application.getRenderThreadID()))
				font[0] = nvgCreateFontMem(window.getNVGID(), name, buffer[0], 0);
			else {
				Thread main = Thread.currentThread();
				TaskManager.tm.addTaskRenderThread(() -> {
					font[0] = nvgCreateFontMem(window.getNVGID(), name, buffer[0], 0);
					main.interrupt();
				});
				try {
					Thread.sleep(Long.MAX_VALUE);
				} catch (InterruptedException e) {
				}
			}

			font[0] = nvgCreateFontMem(window.getNVGID(), name, buffer[0], 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Font(name, buffer[0], font[0]);
	}

	@Override
	public int loadNVGTexture(String file) {
		ByteBuffer buffer[] = new ByteBuffer[1];
		int tex[] = new int[1];
		try {
			buffer[0] = ioResourceToByteBuffer("assets/textures/menu/" + file + ".png", 1024 * 1024);

			if (isThread(Application.getRenderThreadID()))
				tex[0] = nvgCreateImageMem(window.getNVGID(), 0, buffer[0]);
			else {
				Thread main = Thread.currentThread();
				TaskManager.tm.addTaskRenderThread(() -> {
					tex[0] = nvgCreateImageMem(window.getNVGID(), 0, buffer[0]);
					main.interrupt();
				});
				try {
					Thread.sleep(Long.MAX_VALUE);
				} catch (InterruptedException e) {
				}
			}

		} catch (Exception e) {
			throw new LoadTextureException(file, e);
		}
		return tex[0];
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

	public static boolean isThread(long id) {
		return Thread.currentThread().getId() == id;
	}

	/**
	 * Create VAO
	 * 
	 * @return VaoID
	 */
	private int createVAO() {
		int vaoID = glGenVertexArrays();
		vaos.add(vaoID);
		glBindVertexArray(vaoID);
		return vaoID;
	}

	/**
	 * Store The Data in Attribute List
	 * 
	 * @param attributeNumber
	 *            Number
	 * @param coordinateSize
	 *            Coord Size
	 * @param data
	 *            Data
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = glGenBuffers();
		vbos.add(vboID);
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		glVertexAttribPointer(attributeNumber, coordinateSize, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	/**
	 * Unbids the VAO
	 * 
	 */
	private void unbindVAO() {
		glBindVertexArray(0);
	}

	/**
	 * Clear All VAOs, VBOs and Textures
	 * 
	 */
	@Override
	public void dispose() {
		for (int vao : vaos) {
			glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			glDeleteBuffers(vbo);
		}
	}

}