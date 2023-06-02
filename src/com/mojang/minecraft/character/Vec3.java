package com.mojang.minecraft.character;

public class Vec3 {
	public float x;
	public float y;
	public float z;

	public Vec3(float var1, float var2, float var3) {
		this.x = var1;
		this.y = var2;
		this.z = var3;
	}

	public Vec3 interpolateTo(Vec3 var1, float var2) {
		float var3 = this.x + (var1.x - this.x) * var2;
		float var4 = this.y + (var1.y - this.y) * var2;
		float var5 = this.z + (var1.z - this.z) * var2;
		return new Vec3(var3, var4, var5);
	}

	public void set(float var1, float var2, float var3) {
		this.x = var1;
		this.y = var2;
		this.z = var3;
	}
}
