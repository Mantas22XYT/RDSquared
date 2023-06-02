package com.mojang.minecraft.character;

import org.lwjgl.opengl.GL11;

public class Cube {
	private Vertex[] vertices;
	private Polygon[] polygons;
	private int xTexOffs;
	private int yTexOffs;
	public float x;
	public float y;
	public float z;
	public float xRot;
	public float yRot;
	public float zRot;
	private boolean compiled = false;
	private int list = 0;

	public Cube(int var1, int var2) {
		this.xTexOffs = var1;
		this.yTexOffs = var2;
	}

	public void setTexOffs(int var1, int var2) {
		this.xTexOffs = var1;
		this.yTexOffs = var2;
	}

	public void addBox(float var1, float var2, float var3, int var4, int var5, int var6) {
		this.vertices = new Vertex[8];
		this.polygons = new Polygon[6];
		float var7 = var1 + (float)var4;
		float var8 = var2 + (float)var5;
		float var9 = var3 + (float)var6;
		Vertex var10 = new Vertex(var1, var2, var3, 0.0F, 0.0F);
		Vertex var11 = new Vertex(var7, var2, var3, 0.0F, 8.0F);
		Vertex var12 = new Vertex(var7, var8, var3, 8.0F, 8.0F);
		Vertex var13 = new Vertex(var1, var8, var3, 8.0F, 0.0F);
		Vertex var14 = new Vertex(var1, var2, var9, 0.0F, 0.0F);
		Vertex var15 = new Vertex(var7, var2, var9, 0.0F, 8.0F);
		Vertex var16 = new Vertex(var7, var8, var9, 8.0F, 8.0F);
		Vertex var17 = new Vertex(var1, var8, var9, 8.0F, 0.0F);
		this.vertices[0] = var10;
		this.vertices[1] = var11;
		this.vertices[2] = var12;
		this.vertices[3] = var13;
		this.vertices[4] = var14;
		this.vertices[5] = var15;
		this.vertices[6] = var16;
		this.vertices[7] = var17;
		this.polygons[0] = new Polygon(new Vertex[]{var15, var11, var12, var16}, this.xTexOffs + var6 + var4, this.yTexOffs + var6, this.xTexOffs + var6 + var4 + var6, this.yTexOffs + var6 + var5);
		this.polygons[1] = new Polygon(new Vertex[]{var10, var14, var17, var13}, this.xTexOffs + 0, this.yTexOffs + var6, this.xTexOffs + var6, this.yTexOffs + var6 + var5);
		this.polygons[2] = new Polygon(new Vertex[]{var15, var14, var10, var11}, this.xTexOffs + var6, this.yTexOffs + 0, this.xTexOffs + var6 + var4, this.yTexOffs + var6);
		this.polygons[3] = new Polygon(new Vertex[]{var12, var13, var17, var16}, this.xTexOffs + var6 + var4, this.yTexOffs + 0, this.xTexOffs + var6 + var4 + var4, this.yTexOffs + var6);
		this.polygons[4] = new Polygon(new Vertex[]{var11, var10, var13, var12}, this.xTexOffs + var6, this.yTexOffs + var6, this.xTexOffs + var6 + var4, this.yTexOffs + var6 + var5);
		this.polygons[5] = new Polygon(new Vertex[]{var14, var15, var16, var17}, this.xTexOffs + var6 + var4 + var6, this.yTexOffs + var6, this.xTexOffs + var6 + var4 + var6 + var4, this.yTexOffs + var6 + var5);
	}

	public void setPos(float var1, float var2, float var3) {
		this.x = var1;
		this.y = var2;
		this.z = var3;
	}

	public void render() {
		if(!this.compiled) {
			this.compile();
		}

		float var1 = 57.29578F;
		GL11.glPushMatrix();
		GL11.glTranslatef(this.x, this.y, this.z);
		GL11.glRotatef(this.zRot * var1, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(this.yRot * var1, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(this.xRot * var1, 1.0F, 0.0F, 0.0F);
		GL11.glCallList(this.list);
		GL11.glPopMatrix();
	}

	private void compile() {
		this.list = GL11.glGenLists(1);
		GL11.glNewList(this.list, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);

		for(int var1 = 0; var1 < this.polygons.length; ++var1) {
			this.polygons[var1].render();
		}

		GL11.glEnd();
		GL11.glEndList();
		this.compiled = true;
	}
}
