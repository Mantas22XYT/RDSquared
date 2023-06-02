package com.mojang.minecraft;

public class HitResult {
	public int type;
	public int x;
	public int y;
	public int z;
	public int f;

	public HitResult(int var1, int var2, int var3, int var4, int var5) {
		this.type = var1;
		this.x = var2;
		this.y = var3;
		this.z = var4;
		this.f = var5;
	}
}
