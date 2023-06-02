package com.mojang.minecraft.level;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Tesselator {
	private static final int MAX_MEMORY_USE = 4194304;
	private static final int MAX_FLOATS = 524288;
	private FloatBuffer buffer = BufferUtils.createFloatBuffer(524288);
	private float[] array = new float[524288];
	private int vertices = 0;
	private float u;
	private float v;
	private float r;
	private float g;
	private float b;
	private boolean hasColor = false;
	private boolean hasTexture = false;
	private int len = 3;
	private int p = 0;
	public static Tesselator instance = new Tesselator();

	public void flush() {
		this.buffer.clear();
		this.buffer.put(this.array, 0, this.p);
		this.buffer.flip();
		if(this.hasTexture && this.hasColor) {
			GL11.glInterleavedArrays(GL11.GL_T2F_C3F_V3F, 0, (FloatBuffer)this.buffer);
		} else if(this.hasTexture) {
			GL11.glInterleavedArrays(GL11.GL_T2F_V3F, 0, (FloatBuffer)this.buffer);
		} else if(this.hasColor) {
			GL11.glInterleavedArrays(GL11.GL_C3F_V3F, 0, (FloatBuffer)this.buffer);
		} else {
			GL11.glInterleavedArrays(GL11.GL_V3F, 0, (FloatBuffer)this.buffer);
		}

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		if(this.hasTexture) {
			GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		}

		if(this.hasColor) {
			GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		}

		GL11.glDrawArrays(GL11.GL_QUADS, GL11.GL_POINTS, this.vertices);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		if(this.hasTexture) {
			GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		}

		if(this.hasColor) {
			GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		}

		this.clear();
	}

	private void clear() {
		this.vertices = 0;
		this.buffer.clear();
		this.p = 0;
	}

	public void init() {
		this.clear();
		this.hasColor = false;
		this.hasTexture = false;
	}

	public void tex(float var1, float var2) {
		if(!this.hasTexture) {
			this.len += 2;
		}

		this.hasTexture = true;
		this.u = var1;
		this.v = var2;
	}

	public void color(float var1, float var2, float var3) {
		if(!this.hasColor) {
			this.len += 3;
		}

		this.hasColor = true;
		this.r = var1;
		this.g = var2;
		this.b = var3;
	}

	public void vertexUV(float var1, float var2, float var3, float var4, float var5) {
		this.tex(var4, var5);
		this.vertex(var1, var2, var3);
	}

	public void vertex(float var1, float var2, float var3) {
		if(this.hasTexture) {
			this.array[this.p++] = this.u;
			this.array[this.p++] = this.v;
		}

		if(this.hasColor) {
			this.array[this.p++] = this.r;
			this.array[this.p++] = this.g;
			this.array[this.p++] = this.b;
		}

		this.array[this.p++] = var1;
		this.array[this.p++] = var2;
		this.array[this.p++] = var3;
		++this.vertices;
		if(this.vertices % 4 == 0 && this.p >= 524288 - this.len * 4) {
			this.flush();
		}

	}
}
