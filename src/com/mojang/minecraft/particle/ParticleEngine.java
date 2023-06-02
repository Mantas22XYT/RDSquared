package com.mojang.minecraft.particle;

import com.mojang.minecraft.Player;
import com.mojang.minecraft.Textures;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.Tesselator;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;

public class ParticleEngine {
	protected Level level;
	private List particles = new ArrayList();

	public ParticleEngine(Level var1) {
		this.level = var1;
	}

	public void add(Particle var1) {
		this.particles.add(var1);
	}

	public void tick() {
		for(int var1 = 0; var1 < this.particles.size(); ++var1) {
			Particle var2 = (Particle)this.particles.get(var1);
			var2.tick();
			if(var2.removed) {
				this.particles.remove(var1--);
			}
		}

	}

	public void render(Player var1, float var2, int var3) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		int var4 = Textures.loadTexture("/terrain.png", 9728);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4);
		float var5 = -((float)Math.cos((double)var1.yRot * Math.PI / 180.0D));
		float var6 = -((float)Math.sin((double)var1.yRot * Math.PI / 180.0D));
		float var7 = -var6 * (float)Math.sin((double)var1.xRot * Math.PI / 180.0D);
		float var8 = var5 * (float)Math.sin((double)var1.xRot * Math.PI / 180.0D);
		float var9 = (float)Math.cos((double)var1.xRot * Math.PI / 180.0D);
		Tesselator var10 = Tesselator.instance;
		GL11.glColor4f(0.8F, 0.8F, 0.8F, 1.0F);
		var10.init();

		for(int var11 = 0; var11 < this.particles.size(); ++var11) {
			Particle var12 = (Particle)this.particles.get(var11);
			if(var12.isLit() ^ var3 == 1) {
				var12.render(var10, var2, var5, var9, var6, var7, var8);
			}
		}

		var10.flush();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
}
