package com.mojang.minecraft.level;

import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.phys.AABB;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Level {
	private static final int TILE_UPDATE_INTERVAL = 400;
	public final int width;
	public final int height;
	public final int depth;
	private byte[] blocks;
	private int[] lightDepths;
	private ArrayList levelListeners = new ArrayList();
	private Random random = new Random();
	int unprocessed = 0;

	public Level(int var1, int var2, int var3) {
		this.width = var1;
		this.height = var2;
		this.depth = var3;
		this.blocks = new byte[var1 * var2 * var3];
		this.lightDepths = new int[var1 * var2];
		boolean var4 = this.load();
		if(!var4) {
			this.generateMap();
		}

		this.calcLightDepths(0, 0, var1, var2);
	}

	private void generateMap() {
		int var1 = this.width;
		int var2 = this.height;
		int var3 = this.depth;
		int[] var4 = (new PerlinNoiseFilter(0)).read(var1, var2);
		int[] var5 = (new PerlinNoiseFilter(0)).read(var1, var2);
		int[] var6 = (new PerlinNoiseFilter(1)).read(var1, var2);
		int[] var7 = (new PerlinNoiseFilter(1)).read(var1, var2);

		for(int var8 = 0; var8 < var1; ++var8) {
			for(int var9 = 0; var9 < var3; ++var9) {
				for(int var10 = 0; var10 < var2; ++var10) {
					int var11 = var4[var8 + var10 * this.width];
					int var12 = var5[var8 + var10 * this.width];
					int var13 = var6[var8 + var10 * this.width];
					if(var13 < 128) {
						var12 = var11;
					}

					int var14 = var11;
					if(var12 > var11) {
						var14 = var12;
					}

					var14 = var14 / 8 + var3 / 3;
					int var15 = var7[var8 + var10 * this.width] / 8 + var3 / 3;
					if(var15 > var14 - 2) {
						var15 = var14 - 2;
					}

					int var16 = (var9 * this.height + var10) * this.width + var8;
					int var17 = 0;
					if(var9 == var14) {
						var17 = Tile.grass.id;
					}

					if(var9 < var14) {
						var17 = Tile.dirt.id;
					}

					if(var9 <= var15) {
						var17 = Tile.rock.id;
					}

					this.blocks[var16] = (byte)var17;
				}
			}
		}

	}

	public boolean load() {
		try {
			DataInputStream var1 = new DataInputStream(new GZIPInputStream(new FileInputStream(new File("level.dat"))));
			var1.readFully(this.blocks);
			this.calcLightDepths(0, 0, this.width, this.height);

			for(int var2 = 0; var2 < this.levelListeners.size(); ++var2) {
				((LevelListener)this.levelListeners.get(var2)).allChanged();
			}

			var1.close();
			return true;
		} catch (Exception var3) {
			var3.printStackTrace();
			return false;
		}
	}

	public void save() {
		try {
			DataOutputStream var1 = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(new File("level.dat"))));
			var1.write(this.blocks);
			var1.close();
		} catch (Exception var2) {
			var2.printStackTrace();
		}

	}

	public void calcLightDepths(int var1, int var2, int var3, int var4) {
		for(int var5 = var1; var5 < var1 + var3; ++var5) {
			for(int var6 = var2; var6 < var2 + var4; ++var6) {
				int var7 = this.lightDepths[var5 + var6 * this.width];

				int var8;
				for(var8 = this.depth - 1; var8 > 0 && !this.isLightBlocker(var5, var8, var6); --var8) {
				}

				this.lightDepths[var5 + var6 * this.width] = var8;
				if(var7 != var8) {
					int var9 = var7 < var8 ? var7 : var8;
					int var10 = var7 > var8 ? var7 : var8;

					for(int var11 = 0; var11 < this.levelListeners.size(); ++var11) {
						((LevelListener)this.levelListeners.get(var11)).lightColumnChanged(var5, var6, var9, var10);
					}
				}
			}
		}

	}

	public void addListener(LevelListener var1) {
		this.levelListeners.add(var1);
	}

	public void removeListener(LevelListener var1) {
		this.levelListeners.remove(var1);
	}

	public boolean isLightBlocker(int var1, int var2, int var3) {
		Tile var4 = Tile.tiles[this.getTile(var1, var2, var3)];
		return var4 == null ? false : var4.blocksLight();
	}

	public ArrayList getCubes(AABB var1) {
		ArrayList var2 = new ArrayList();
		int var3 = (int)var1.x0;
		int var4 = (int)(var1.x1 + 1.0F);
		int var5 = (int)var1.y0;
		int var6 = (int)(var1.y1 + 1.0F);
		int var7 = (int)var1.z0;
		int var8 = (int)(var1.z1 + 1.0F);
		if(var3 < 0) {
			var3 = 0;
		}

		if(var5 < 0) {
			var5 = 0;
		}

		if(var7 < 0) {
			var7 = 0;
		}

		if(var4 > this.width) {
			var4 = this.width;
		}

		if(var6 > this.depth) {
			var6 = this.depth;
		}

		if(var8 > this.height) {
			var8 = this.height;
		}

		for(int var9 = var3; var9 < var4; ++var9) {
			for(int var10 = var5; var10 < var6; ++var10) {
				for(int var11 = var7; var11 < var8; ++var11) {
					Tile var12 = Tile.tiles[this.getTile(var9, var10, var11)];
					if(var12 != null) {
						AABB var13 = var12.getAABB(var9, var10, var11);
						if(var13 != null) {
							var2.add(var13);
						}
					}
				}
			}
		}

		return var2;
	}

	public boolean setTile(int var1, int var2, int var3, int var4) {
		if(var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.depth && var3 < this.height) {
			if(var4 == this.blocks[(var2 * this.height + var3) * this.width + var1]) {
				return false;
			} else {
				this.blocks[(var2 * this.height + var3) * this.width + var1] = (byte)var4;
				this.calcLightDepths(var1, var3, 1, 1);

				for(int var5 = 0; var5 < this.levelListeners.size(); ++var5) {
					((LevelListener)this.levelListeners.get(var5)).tileChanged(var1, var2, var3);
				}

				return true;
			}
		} else {
			return false;
		}
	}

	public boolean isLit(int var1, int var2, int var3) {
		return var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.depth && var3 < this.height ? var2 >= this.lightDepths[var1 + var3 * this.width] : true;
	}

	public int getTile(int var1, int var2, int var3) {
		return var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.depth && var3 < this.height ? this.blocks[(var2 * this.height + var3) * this.width + var1] : 0;
	}

	public boolean isSolidTile(int var1, int var2, int var3) {
		Tile var4 = Tile.tiles[this.getTile(var1, var2, var3)];
		return var4 == null ? false : var4.isSolid();
	}

	public void tick() {
		this.unprocessed += this.width * this.height * this.depth;
		int var1 = this.unprocessed / 400;
		this.unprocessed -= var1 * 400;

		for(int var2 = 0; var2 < var1; ++var2) {
			int var3 = this.random.nextInt(this.width);
			int var4 = this.random.nextInt(this.depth);
			int var5 = this.random.nextInt(this.height);
			Tile var6 = Tile.tiles[this.getTile(var3, var4, var5)];
			if(var6 != null) {
				var6.tick(this, var3, var4, var5, this.random);
			}
		}

	}
}
