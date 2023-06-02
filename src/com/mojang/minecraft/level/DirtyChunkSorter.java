package com.mojang.minecraft.level;

import com.mojang.minecraft.Player;
import java.util.Comparator;

public class DirtyChunkSorter implements Comparator {
	private Player player;
	private Frustum frustum;
	private long now = System.currentTimeMillis();

	public DirtyChunkSorter(Player var1, Frustum var2) {
		this.player = var1;
		this.frustum = var2;
	}

	public int compare(Chunk var1, Chunk var2) {
		boolean var3 = this.frustum.isVisible(var1.aabb);
		boolean var4 = this.frustum.isVisible(var2.aabb);
		if(var3 && !var4) {
			return -1;
		} else if(var4 && !var3) {
			return 1;
		} else {
			int var5 = (int)((this.now - var1.dirtiedTime) / 2000L);
			int var6 = (int)((this.now - var2.dirtiedTime) / 2000L);
			return var5 < var6 ? -1 : (var5 > var6 ? 1 : (var1.distanceToSqr(this.player) < var2.distanceToSqr(this.player) ? -1 : 1));
		}
	}

	public int compare(Object var1, Object var2) {
		return this.compare((Chunk)var1, (Chunk)var2);
	}
}
