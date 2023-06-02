package com.mojang.minecraft;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;
import java.util.ArrayList;

public class Entity {
	protected Level level;
	public float xo;
	public float yo;
	public float zo;
	public float x;
	public float y;
	public float z;
	public float xd;
	public float yd;
	public float zd;
	public float yRot;
	public float xRot;
	public AABB bb;
	public boolean onGround = false;
	public boolean removed = false;
	protected float heightOffset = 0.0F;
	protected float bbWidth = 0.6F;
	protected float bbHeight = 1.8F;

	public Entity(Level var1) {
		this.level = var1;
		this.resetPos();
	}

	protected void resetPos() {
		float var1 = (float)Math.random() * (float)this.level.width;
		float var2 = (float)(this.level.depth + 10);
		float var3 = (float)Math.random() * (float)this.level.height;
		this.setPos(var1, var2, var3);
	}

	public void remove() {
		this.removed = true;
	}

	protected void setSize(float var1, float var2) {
		this.bbWidth = var1;
		this.bbHeight = var2;
	}

	protected void setPos(float var1, float var2, float var3) {
		this.x = var1;
		this.y = var2;
		this.z = var3;
		float var4 = this.bbWidth / 2.0F;
		float var5 = this.bbHeight / 2.0F;
		this.bb = new AABB(var1 - var4, var2 - var5, var3 - var4, var1 + var4, var2 + var5, var3 + var4);
	}

	public void turn(float var1, float var2) {
		this.yRot = (float)((double)this.yRot + (double)var1 * 0.15D);
		this.xRot = (float)((double)this.xRot - (double)var2 * 0.15D);
		if(this.xRot < -90.0F) {
			this.xRot = -90.0F;
		}

		if(this.xRot > 90.0F) {
			this.xRot = 90.0F;
		}

	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
	}

	public void move(float var1, float var2, float var3) {
		float var4 = var1;
		float var5 = var2;
		float var6 = var3;
		ArrayList var7 = this.level.getCubes(this.bb.expand(var1, var2, var3));

		int var8;
		for(var8 = 0; var8 < var7.size(); ++var8) {
			var2 = ((AABB)var7.get(var8)).clipYCollide(this.bb, var2);
		}

		this.bb.move(0.0F, var2, 0.0F);

		for(var8 = 0; var8 < var7.size(); ++var8) {
			var1 = ((AABB)var7.get(var8)).clipXCollide(this.bb, var1);
		}

		this.bb.move(var1, 0.0F, 0.0F);

		for(var8 = 0; var8 < var7.size(); ++var8) {
			var3 = ((AABB)var7.get(var8)).clipZCollide(this.bb, var3);
		}

		this.bb.move(0.0F, 0.0F, var3);
		this.onGround = var5 != var2 && var5 < 0.0F;
		if(var4 != var1) {
			this.xd = 0.0F;
		}

		if(var5 != var2) {
			this.yd = 0.0F;
		}

		if(var6 != var3) {
			this.zd = 0.0F;
		}

		this.x = (this.bb.x0 + this.bb.x1) / 2.0F;
		this.y = this.bb.y0 + this.heightOffset;
		this.z = (this.bb.z0 + this.bb.z1) / 2.0F;
	}

	public void moveRelative(float var1, float var2, float var3) {
		float var4 = var1 * var1 + var2 * var2;
		if(var4 >= 0.01F) {
			var4 = var3 / (float)Math.sqrt((double)var4);
			var1 *= var4;
			var2 *= var4;
			float var5 = (float)Math.sin((double)this.yRot * Math.PI / 180.0D);
			float var6 = (float)Math.cos((double)this.yRot * Math.PI / 180.0D);
			this.xd += var1 * var6 - var2 * var5;
			this.zd += var2 * var6 + var1 * var5;
		}
	}

	public boolean isLit() {
		int var1 = (int)this.x;
		int var2 = (int)this.y;
		int var3 = (int)this.z;
		return this.level.isLit(var1, var2, var3);
	}
}
