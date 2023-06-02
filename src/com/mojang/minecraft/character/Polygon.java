package com.mojang.minecraft.character;

import org.lwjgl.opengl.GL11;

public class Polygon {
	public Vertex[] vertices;
	public int vertexCount;

	public Polygon(Vertex[] var1) {
		this.vertexCount = 0;
		this.vertices = var1;
		this.vertexCount = var1.length;
	}

	public Polygon(Vertex[] var1, int var2, int var3, int var4, int var5) {
		this(var1);
		var1[0] = var1[0].remap((float)var4, (float)var3);
		var1[1] = var1[1].remap((float)var2, (float)var3);
		var1[2] = var1[2].remap((float)var2, (float)var5);
		var1[3] = var1[3].remap((float)var4, (float)var5);
	}

	public void render() {
		GL11.glColor3f(1.0F, 1.0F, 1.0F);

		for(int var1 = 3; var1 >= 0; --var1) {
			Vertex var2 = this.vertices[var1];
			GL11.glTexCoord2f(var2.u / 63.999F, var2.v / 31.999F);
			GL11.glVertex3f(var2.pos.x, var2.pos.y, var2.pos.z);
		}

	}
}
