package com.mojang.minecraft.level;

import com.mojang.minecraft.Player;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.phys.AABB;
import org.lwjgl.opengl.GL11;

public class Chunk {
	public AABB aabb;
	public final Level level;
	public final int x0;
	public final int y0;
	public final int z0;
	public final int x1;
	public final int y1;
	public final int z1;
	public final float x;
	public final float y;
	public final float z;
	private boolean dirty = true;
	private int lists = -1;
	public long dirtiedTime = 0L;
	private static Tesselator t = Tesselator.instance;
	public static int updates = 0;
	private static long totalTime = 0L;
	private static int totalUpdates = 0;

	public Chunk(Level var1, int var2, int var3, int var4, int var5, int var6, int var7) {
		this.level = var1;
		this.x0 = var2;
		this.y0 = var3;
		this.z0 = var4;
		this.x1 = var5;
		this.y1 = var6;
		this.z1 = var7;
		this.x = (float)(var2 + var5) / 2.0F;
		this.y = (float)(var3 + var6) / 2.0F;
		this.z = (float)(var4 + var7) / 2.0F;
		this.aabb = new AABB((float)var2, (float)var3, (float)var4, (float)var5, (float)var6, (float)var7);
		this.lists = GL11.glGenLists(2);
	}

	private void rebuild(int var1) {
		this.dirty = false;
		++updates;
		long var2 = System.nanoTime();
		GL11.glNewList(this.lists + var1, GL11.GL_COMPILE);
		t.init();
		int var4 = 0;

		for(int var5 = this.x0; var5 < this.x1; ++var5) {
			for(int var6 = this.y0; var6 < this.y1; ++var6) {
				for(int var7 = this.z0; var7 < this.z1; ++var7) {
					int var8 = this.level.getTile(var5, var6, var7);
					if(var8 > 0) {
						Tile.tiles[var8].render(t, this.level, var1, var5, var6, var7);
						++var4;
					}
				}
			}
		}

		t.flush();
		GL11.glEndList();
		long var9 = System.nanoTime();
		if(var4 > 0) {
			totalTime += var9 - var2;
			++totalUpdates;
		}

	}

	public void rebuild() {
		this.rebuild(0);
		this.rebuild(1);
	}

	public void render(int var1) {
		GL11.glCallList(this.lists + var1);
	}

	public void setDirty() {
		if(!this.dirty) {
			this.dirtiedTime = System.currentTimeMillis();
		}

		this.dirty = true;
	}

	public boolean isDirty() {
		return this.dirty;
	}

	public float distanceToSqr(Player var1) {
		float var2 = var1.x - this.x;
		float var3 = var1.y - this.y;
		float var4 = var1.z - this.z;
		return var2 * var2 + var3 * var3 + var4 * var4;
	}
}
