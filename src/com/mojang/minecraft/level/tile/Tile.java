package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.Tesselator;
import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import java.util.Random;

public class Tile {
	public static final Tile[] tiles = new Tile[256];
	public static final Tile empty = null;
	public static final Tile rock = new Tile(1, 1);
	public static final Tile grass = new GrassTile(2);
	public static final Tile dirt = new DirtTile(3, 2);
	public static final Tile stoneBrick = new Tile(4, 16);
	public static final Tile wood = new Tile(5, 4);
	public static final Tile bush = new Bush(6);
	public static final Tile water = new Tile(7, 13);
	public static final Tile seawater = new Tile(8, 14);
	public static final Tile hardrock = new Tile(9, 17);
	public static final Tile lava = new Tile(10, 30);
	public static final Tile grass2 = new Tile(11, 31);
	
	public int tex;
	public final int id;

	protected Tile(int var1) {
		tiles[var1] = this;
		this.id = var1;
	}

	protected Tile(int var1, int var2) {
		this(var1);
		this.tex = var2;
	}

	public void render(Tesselator var1, Level var2, int var3, int var4, int var5, int var6) {
		float var7 = 1.0F;
		float var8 = 0.8F;
		float var9 = 0.6F;
		if(this.shouldRenderFace(var2, var4, var5 - 1, var6, var3)) {
			var1.color(var7, var7, var7);
			this.renderFace(var1, var4, var5, var6, 0);
		}

		if(this.shouldRenderFace(var2, var4, var5 + 1, var6, var3)) {
			var1.color(var7, var7, var7);
			this.renderFace(var1, var4, var5, var6, 1);
		}

		if(this.shouldRenderFace(var2, var4, var5, var6 - 1, var3)) {
			var1.color(var8, var8, var8);
			this.renderFace(var1, var4, var5, var6, 2);
		}

		if(this.shouldRenderFace(var2, var4, var5, var6 + 1, var3)) {
			var1.color(var8, var8, var8);
			this.renderFace(var1, var4, var5, var6, 3);
		}

		if(this.shouldRenderFace(var2, var4 - 1, var5, var6, var3)) {
			var1.color(var9, var9, var9);
			this.renderFace(var1, var4, var5, var6, 4);
		}

		if(this.shouldRenderFace(var2, var4 + 1, var5, var6, var3)) {
			var1.color(var9, var9, var9);
			this.renderFace(var1, var4, var5, var6, 5);
		}

	}

	private boolean shouldRenderFace(Level var1, int var2, int var3, int var4, int var5) {
		return !var1.isSolidTile(var2, var3, var4) && var1.isLit(var2, var3, var4) ^ var5 == 1;
	}

	protected int getTexture(int var1) {
		return this.tex;
	}

	public void renderFace(Tesselator var1, int var2, int var3, int var4, int var5) {
		int var6 = this.getTexture(var5);
		float var7 = (float)(var6 % 16) / 16.0F;
		float var8 = var7 + 0.999F / 16.0F;
		float var9 = (float)(var6 / 16) / 16.0F;
		float var10 = var9 + 0.999F / 16.0F;
		float var11 = (float)var2 + 0.0F;
		float var12 = (float)var2 + 1.0F;
		float var13 = (float)var3 + 0.0F;
		float var14 = (float)var3 + 1.0F;
		float var15 = (float)var4 + 0.0F;
		float var16 = (float)var4 + 1.0F;
		if(var5 == 0) {
			var1.vertexUV(var11, var13, var16, var7, var10);
			var1.vertexUV(var11, var13, var15, var7, var9);
			var1.vertexUV(var12, var13, var15, var8, var9);
			var1.vertexUV(var12, var13, var16, var8, var10);
		}

		if(var5 == 1) {
			var1.vertexUV(var12, var14, var16, var8, var10);
			var1.vertexUV(var12, var14, var15, var8, var9);
			var1.vertexUV(var11, var14, var15, var7, var9);
			var1.vertexUV(var11, var14, var16, var7, var10);
		}

		if(var5 == 2) {
			var1.vertexUV(var11, var14, var15, var8, var9);
			var1.vertexUV(var12, var14, var15, var7, var9);
			var1.vertexUV(var12, var13, var15, var7, var10);
			var1.vertexUV(var11, var13, var15, var8, var10);
		}

		if(var5 == 3) {
			var1.vertexUV(var11, var14, var16, var7, var9);
			var1.vertexUV(var11, var13, var16, var7, var10);
			var1.vertexUV(var12, var13, var16, var8, var10);
			var1.vertexUV(var12, var14, var16, var8, var9);
		}

		if(var5 == 4) {
			var1.vertexUV(var11, var14, var16, var8, var9);
			var1.vertexUV(var11, var14, var15, var7, var9);
			var1.vertexUV(var11, var13, var15, var7, var10);
			var1.vertexUV(var11, var13, var16, var8, var10);
		}

		if(var5 == 5) {
			var1.vertexUV(var12, var13, var16, var7, var10);
			var1.vertexUV(var12, var13, var15, var8, var10);
			var1.vertexUV(var12, var14, var15, var8, var9);
			var1.vertexUV(var12, var14, var16, var7, var9);
		}

	}

	public void renderFaceNoTexture(Tesselator var1, int var2, int var3, int var4, int var5) {
		float var6 = (float)var2 + 0.0F;
		float var7 = (float)var2 + 1.0F;
		float var8 = (float)var3 + 0.0F;
		float var9 = (float)var3 + 1.0F;
		float var10 = (float)var4 + 0.0F;
		float var11 = (float)var4 + 1.0F;
		if(var5 == 0) {
			var1.vertex(var6, var8, var11);
			var1.vertex(var6, var8, var10);
			var1.vertex(var7, var8, var10);
			var1.vertex(var7, var8, var11);
		}

		if(var5 == 1) {
			var1.vertex(var7, var9, var11);
			var1.vertex(var7, var9, var10);
			var1.vertex(var6, var9, var10);
			var1.vertex(var6, var9, var11);
		}

		if(var5 == 2) {
			var1.vertex(var6, var9, var10);
			var1.vertex(var7, var9, var10);
			var1.vertex(var7, var8, var10);
			var1.vertex(var6, var8, var10);
		}

		if(var5 == 3) {
			var1.vertex(var6, var9, var11);
			var1.vertex(var6, var8, var11);
			var1.vertex(var7, var8, var11);
			var1.vertex(var7, var9, var11);
		}

		if(var5 == 4) {
			var1.vertex(var6, var9, var11);
			var1.vertex(var6, var9, var10);
			var1.vertex(var6, var8, var10);
			var1.vertex(var6, var8, var11);
		}

		if(var5 == 5) {
			var1.vertex(var7, var8, var11);
			var1.vertex(var7, var8, var10);
			var1.vertex(var7, var9, var10);
			var1.vertex(var7, var9, var11);
		}

	}

	public final AABB getTileAABB(int var1, int var2, int var3) {
		return new AABB((float)var1, (float)var2, (float)var3, (float)(var1 + 1), (float)(var2 + 1), (float)(var3 + 1));
	}

	public AABB getAABB(int var1, int var2, int var3) {
		return new AABB((float)var1, (float)var2, (float)var3, (float)(var1 + 1), (float)(var2 + 1), (float)(var3 + 1));
	}

	public boolean blocksLight() {
		return true;
	}

	public boolean isSolid() {
		return true;
	}

	public void tick(Level var1, int var2, int var3, int var4, Random var5) {
	}

	public void destroy(Level var1, int var2, int var3, int var4, ParticleEngine var5) {
		byte var6 = 4;

		for(int var7 = 0; var7 < var6; ++var7) {
			for(int var8 = 0; var8 < var6; ++var8) {
				for(int var9 = 0; var9 < var6; ++var9) {
					float var10 = (float)var2 + ((float)var7 + 0.5F) / (float)var6;
					float var11 = (float)var3 + ((float)var8 + 0.5F) / (float)var6;
					float var12 = (float)var4 + ((float)var9 + 0.5F) / (float)var6;
					var5.add(new Particle(var1, var10, var11, var12, var10 - (float)var2 - 0.5F, var11 - (float)var3 - 0.5F, var12 - (float)var4 - 0.5F, this.tex));
				}
			}
		}

	}
}
