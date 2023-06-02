package com.mojang.minecraft.level;

import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.Player;
import com.mojang.minecraft.Textures;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.phys.AABB;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.lwjgl.opengl.GL11;

public class LevelRenderer implements LevelListener {
	public static final int MAX_REBUILDS_PER_FRAME = 8;
	public static final int CHUNK_SIZE = 16;
	private Level level;
	private Chunk[] chunks;
	private int xChunks;
	private int yChunks;
	private int zChunks;

	public LevelRenderer(Level var1) {
		this.level = var1;
		var1.addListener(this);
		this.xChunks = var1.width / 16;
		this.yChunks = var1.depth / 16;
		this.zChunks = var1.height / 16;
		this.chunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];

		for(int var2 = 0; var2 < this.xChunks; ++var2) {
			for(int var3 = 0; var3 < this.yChunks; ++var3) {
				for(int var4 = 0; var4 < this.zChunks; ++var4) {
					int var5 = var2 * 16;
					int var6 = var3 * 16;
					int var7 = var4 * 16;
					int var8 = (var2 + 1) * 16;
					int var9 = (var3 + 1) * 16;
					int var10 = (var4 + 1) * 16;
					if(var8 > var1.width) {
						var8 = var1.width;
					}

					if(var9 > var1.depth) {
						var9 = var1.depth;
					}

					if(var10 > var1.height) {
						var10 = var1.height;
					}

					this.chunks[(var2 + var3 * this.xChunks) * this.zChunks + var4] = new Chunk(var1, var5, var6, var7, var8, var9, var10);
				}
			}
		}

	}

	public List getAllDirtyChunks() {
		ArrayList var1 = null;

		for(int var2 = 0; var2 < this.chunks.length; ++var2) {
			Chunk var3 = this.chunks[var2];
			if(var3.isDirty()) {
				if(var1 == null) {
					var1 = new ArrayList();
				}

				var1.add(var3);
			}
		}

		return var1;
	}

	public void render(Player var1, int var2) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		int var3 = Textures.loadTexture("/terrain.png", 9728);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var3);
		Frustum var4 = Frustum.getFrustum();

		for(int var5 = 0; var5 < this.chunks.length; ++var5) {
			if(var4.isVisible(this.chunks[var5].aabb)) {
				this.chunks[var5].render(var2);
			}
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	public void updateDirtyChunks(Player var1) {
		List var2 = this.getAllDirtyChunks();
		if(var2 != null) {
			Collections.sort(var2, new DirtyChunkSorter(var1, Frustum.getFrustum()));

			for(int var3 = 0; var3 < 8 && var3 < var2.size(); ++var3) {
				((Chunk)var2.get(var3)).rebuild();
			}

		}
	}

	public void pick(Player var1, Frustum var2) {
		Tesselator var3 = Tesselator.instance;
		float var4 = 3.0F;
		AABB var5 = var1.bb.grow(var4, var4, var4);
		int var6 = (int)var5.x0;
		int var7 = (int)(var5.x1 + 1.0F);
		int var8 = (int)var5.y0;
		int var9 = (int)(var5.y1 + 1.0F);
		int var10 = (int)var5.z0;
		int var11 = (int)(var5.z1 + 1.0F);
		GL11.glInitNames();
		GL11.glPushName(0);
		GL11.glPushName(0);

		for(int var12 = var6; var12 < var7; ++var12) {
			GL11.glLoadName(var12);
			GL11.glPushName(0);

			for(int var13 = var8; var13 < var9; ++var13) {
				GL11.glLoadName(var13);
				GL11.glPushName(0);

				for(int var14 = var10; var14 < var11; ++var14) {
					Tile var15 = Tile.tiles[this.level.getTile(var12, var13, var14)];
					if(var15 != null && var2.isVisible(var15.getTileAABB(var12, var13, var14))) {
						GL11.glLoadName(var14);
						GL11.glPushName(0);

						for(int var16 = 0; var16 < 6; ++var16) {
							GL11.glLoadName(var16);
							var3.init();
							var15.renderFaceNoTexture(var3, var12, var13, var14, var16);
							var3.flush();
						}

						GL11.glPopName();
					}
				}

				GL11.glPopName();
			}

			GL11.glPopName();
		}

		GL11.glPopName();
		GL11.glPopName();
	}

	public void renderHit(HitResult var1) {
		Tesselator var2 = Tesselator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, ((float)Math.sin((double)System.currentTimeMillis() / 100.0D) * 0.2F + 0.4F) * 0.5F);
		var2.init();
		Tile.rock.renderFaceNoTexture(var2, var1.x, var1.y, var1.z, var1.f);
		var2.flush();
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void setDirty(int var1, int var2, int var3, int var4, int var5, int var6) {
		var1 /= 16;
		var4 /= 16;
		var2 /= 16;
		var5 /= 16;
		var3 /= 16;
		var6 /= 16;
		if(var1 < 0) {
			var1 = 0;
		}

		if(var2 < 0) {
			var2 = 0;
		}

		if(var3 < 0) {
			var3 = 0;
		}

		if(var4 >= this.xChunks) {
			var4 = this.xChunks - 1;
		}

		if(var5 >= this.yChunks) {
			var5 = this.yChunks - 1;
		}

		if(var6 >= this.zChunks) {
			var6 = this.zChunks - 1;
		}

		for(int var7 = var1; var7 <= var4; ++var7) {
			for(int var8 = var2; var8 <= var5; ++var8) {
				for(int var9 = var3; var9 <= var6; ++var9) {
					this.chunks[(var7 + var8 * this.xChunks) * this.zChunks + var9].setDirty();
				}
			}
		}

	}

	public void tileChanged(int var1, int var2, int var3) {
		this.setDirty(var1 - 1, var2 - 1, var3 - 1, var1 + 1, var2 + 1, var3 + 1);
	}

	public void lightColumnChanged(int var1, int var2, int var3, int var4) {
		this.setDirty(var1 - 1, var3 - 1, var2 - 1, var1 + 1, var4 + 1, var2 + 1);
	}

	public void allChanged() {
		this.setDirty(0, 0, 0, this.level.width, this.level.depth, this.level.height);
	}
}
