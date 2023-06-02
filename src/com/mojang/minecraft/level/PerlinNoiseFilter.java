package com.mojang.minecraft.level;

import java.util.Random;

public class PerlinNoiseFilter {
	Random random = new Random();
	int seed = this.random.nextInt();
	int levels = 0;
	int fuzz = 32;

	public PerlinNoiseFilter(int var1) {
		this.levels = var1;
	}

	public int[] read(int var1, int var2) {
		Random var3 = new Random();
		int[] var4 = new int[var1 * var2];
		int var5 = this.levels;
		int var6 = var1 >> var5;

		int var7;
		int var8;
		for(var7 = 0; var7 < var2; var7 += var6) {
			for(var8 = 0; var8 < var1; var8 += var6) {
				var4[var8 + var7 * var1] = (var3.nextInt(256) - 128) * this.fuzz;
			}
		}

		for(var6 = var1 >> var5; var6 > 1; var6 /= 2) {
			var7 = 256 * (var6 << var5);
			var8 = var6 / 2;

			int var9;
			int var10;
			int var11;
			int var12;
			int var13;
			int var14;
			int var15;
			for(var9 = 0; var9 < var2; var9 += var6) {
				for(var10 = 0; var10 < var1; var10 += var6) {
					var11 = var4[(var10 + 0) % var1 + (var9 + 0) % var2 * var1];
					var12 = var4[(var10 + var6) % var1 + (var9 + 0) % var2 * var1];
					var13 = var4[(var10 + 0) % var1 + (var9 + var6) % var2 * var1];
					var14 = var4[(var10 + var6) % var1 + (var9 + var6) % var2 * var1];
					var15 = (var11 + var13 + var12 + var14) / 4 + var3.nextInt(var7 * 2) - var7;
					var4[var10 + var8 + (var9 + var8) * var1] = var15;
				}
			}

			for(var9 = 0; var9 < var2; var9 += var6) {
				for(var10 = 0; var10 < var1; var10 += var6) {
					var11 = var4[var10 + var9 * var1];
					var12 = var4[(var10 + var6) % var1 + var9 * var1];
					var13 = var4[var10 + (var9 + var6) % var1 * var1];
					var14 = var4[(var10 + var8 & var1 - 1) + (var9 + var8 - var6 & var2 - 1) * var1];
					var15 = var4[(var10 + var8 - var6 & var1 - 1) + (var9 + var8 & var2 - 1) * var1];
					int var16 = var4[(var10 + var8) % var1 + (var9 + var8) % var2 * var1];
					int var17 = (var11 + var12 + var16 + var14) / 4 + var3.nextInt(var7 * 2) - var7;
					int var18 = (var11 + var13 + var16 + var15) / 4 + var3.nextInt(var7 * 2) - var7;
					var4[var10 + var8 + var9 * var1] = var17;
					var4[var10 + (var9 + var8) * var1] = var18;
				}
			}
		}

		int[] var19 = new int[var1 * var2];

		for(var7 = 0; var7 < var2; ++var7) {
			for(var8 = 0; var8 < var1; ++var8) {
				var19[var8 + var7 * var1] = var4[var8 % var1 + var7 % var2 * var1] / 512 + 128;
			}
		}

		return var19;
	}
}
