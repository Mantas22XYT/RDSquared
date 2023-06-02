package com.mojang.minecraft.character;

public class Vertex {
	public Vec3 pos;
	public float u;
	public float v;

	public Vertex(float var1, float var2, float var3, float var4, float var5) {
		this(new Vec3(var1, var2, var3), var4, var5);
	}

	public Vertex remap(float var1, float var2) {
		return new Vertex(this, var1, var2);
	}

	public Vertex(Vertex var1, float var2, float var3) {
		this.pos = var1.pos;
		this.u = var2;
		this.v = var3;
	}

	public Vertex(Vec3 var1, float var2, float var3) {
		this.pos = var1;
		this.u = var2;
		this.v = var3;
	}
}
