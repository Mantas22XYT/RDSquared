package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.Tesselator;
import com.mojang.minecraft.phys.AABB;
import java.util.Random;

public class Bush extends Tile {
	protected Bush(int var1) {
		super(var1);
		this.tex = 15;
	}

	public void tick(Level var1, int var2, int var3, int var4, Random var5) {
		int var6 = var1.getTile(var2, var3 - 1, var4);
		if(!var1.isLit(var2, var3, var4) || var6 != Tile.dirt.id && var6 != Tile.grass.id) {
			var1.setTile(var2, var3, var4, 0);
		}

	}

	public void render(Tesselator var1, Level var2, int var3, int var4, int var5, int var6) {
		if(!(var2.isLit(var4, var5, var6) ^ var3 != 1)) {
			int var7 = this.getTexture(15);
			float var8 = (float)(var7 % 16) / 16.0F;
			float var9 = var8 + 0.999F / 16.0F;
			float var10 = (float)(var7 / 16) / 16.0F;
			float var11 = var10 + 0.999F / 16.0F;
			byte var12 = 2;
			var1.color(1.0F, 1.0F, 1.0F);

			for(int var13 = 0; var13 < var12; ++var13) {
				float var14 = (float)(Math.sin((double)var13 * Math.PI / (double)var12 + Math.PI * 0.25D) * 0.5D);
				float var15 = (float)(Math.cos((double)var13 * Math.PI / (double)var12 + Math.PI * 0.25D) * 0.5D);
				float var16 = (float)var4 + 0.5F - var14;
				float var17 = (float)var4 + 0.5F + var14;
				float var18 = (float)var5 + 0.0F;
				float var19 = (float)var5 + 1.0F;
				float var20 = (float)var6 + 0.5F - var15;
				float var21 = (float)var6 + 0.5F + var15;
				var1.vertexUV(var16, var19, var20, var9, var10);
				var1.vertexUV(var17, var19, var21, var8, var10);
				var1.vertexUV(var17, var18, var21, var8, var11);
				var1.vertexUV(var16, var18, var20, var9, var11);
				var1.vertexUV(var17, var19, var21, var8, var10);
				var1.vertexUV(var16, var19, var20, var9, var10);
				var1.vertexUV(var16, var18, var20, var9, var11);
				var1.vertexUV(var17, var18, var21, var8, var11);
			}

		}
	}

	public AABB getAABB(int var1, int var2, int var3) {
		return null;
	}

	public boolean blocksLight() {
		return false;
	}

	public boolean isSolid() {
		return false;
	}
}
