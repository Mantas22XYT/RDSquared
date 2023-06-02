package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import java.util.Random;

public class GrassTile extends Tile {
	protected GrassTile(int var1) {
		super(var1);
		this.tex = 3;
	}

	protected int getTexture(int var1) {
		return var1 == 1 ? 0 : (var1 == 0 ? 2 : 3);
	}

	public void tick(Level var1, int var2, int var3, int var4, Random var5) {
		if(!var1.isLit(var2, var3, var4)) {
			var1.setTile(var2, var3, var4, Tile.dirt.id);
		} else {
			for(int var6 = 0; var6 < 4; ++var6) {
				int var7 = var2 + var5.nextInt(3) - 1;
				int var8 = var3 + var5.nextInt(5) - 3;
				int var9 = var4 + var5.nextInt(3) - 1;
				if(var1.getTile(var7, var8, var9) == Tile.dirt.id && var1.isLit(var7, var8, var9)) {
					var1.setTile(var7, var8, var9, Tile.grass.id);
				}
			}
		}

	}
}
