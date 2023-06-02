package com.mojang.minecraft.level;

import com.mojang.minecraft.phys.AABB;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Frustum {
	public float[][] m_Frustum = new float[6][4];
	public static final int RIGHT = 0;
	public static final int LEFT = 1;
	public static final int BOTTOM = 2;
	public static final int TOP = 3;
	public static final int BACK = 4;
	public static final int FRONT = 5;
	public static final int A = 0;
	public static final int B = 1;
	public static final int C = 2;
	public static final int D = 3;
	private static Frustum frustum = new Frustum();
	private FloatBuffer _proj = BufferUtils.createFloatBuffer(16);
	private FloatBuffer _modl = BufferUtils.createFloatBuffer(16);
	private FloatBuffer _clip = BufferUtils.createFloatBuffer(16);
	float[] proj = new float[16];
	float[] modl = new float[16];
	float[] clip = new float[16];

	public static Frustum getFrustum() {
		frustum.calculateFrustum();
		return frustum;
	}

	private void normalizePlane(float[][] var1, int var2) {
		float var3 = (float)Math.sqrt((double)(var1[var2][0] * var1[var2][0] + var1[var2][1] * var1[var2][1] + var1[var2][2] * var1[var2][2]));
		var1[var2][0] /= var3;
		var1[var2][1] /= var3;
		var1[var2][2] /= var3;
		var1[var2][3] /= var3;
	}

	private void calculateFrustum() {
		this._proj.clear();
		this._modl.clear();
		this._clip.clear();
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, this._proj);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, this._modl);
		this._proj.flip().limit(16);
		this._proj.get(this.proj);
		this._modl.flip().limit(16);
		this._modl.get(this.modl);
		this.clip[0] = this.modl[0] * this.proj[0] + this.modl[1] * this.proj[4] + this.modl[2] * this.proj[8] + this.modl[3] * this.proj[12];
		this.clip[1] = this.modl[0] * this.proj[1] + this.modl[1] * this.proj[5] + this.modl[2] * this.proj[9] + this.modl[3] * this.proj[13];
		this.clip[2] = this.modl[0] * this.proj[2] + this.modl[1] * this.proj[6] + this.modl[2] * this.proj[10] + this.modl[3] * this.proj[14];
		this.clip[3] = this.modl[0] * this.proj[3] + this.modl[1] * this.proj[7] + this.modl[2] * this.proj[11] + this.modl[3] * this.proj[15];
		this.clip[4] = this.modl[4] * this.proj[0] + this.modl[5] * this.proj[4] + this.modl[6] * this.proj[8] + this.modl[7] * this.proj[12];
		this.clip[5] = this.modl[4] * this.proj[1] + this.modl[5] * this.proj[5] + this.modl[6] * this.proj[9] + this.modl[7] * this.proj[13];
		this.clip[6] = this.modl[4] * this.proj[2] + this.modl[5] * this.proj[6] + this.modl[6] * this.proj[10] + this.modl[7] * this.proj[14];
		this.clip[7] = this.modl[4] * this.proj[3] + this.modl[5] * this.proj[7] + this.modl[6] * this.proj[11] + this.modl[7] * this.proj[15];
		this.clip[8] = this.modl[8] * this.proj[0] + this.modl[9] * this.proj[4] + this.modl[10] * this.proj[8] + this.modl[11] * this.proj[12];
		this.clip[9] = this.modl[8] * this.proj[1] + this.modl[9] * this.proj[5] + this.modl[10] * this.proj[9] + this.modl[11] * this.proj[13];
		this.clip[10] = this.modl[8] * this.proj[2] + this.modl[9] * this.proj[6] + this.modl[10] * this.proj[10] + this.modl[11] * this.proj[14];
		this.clip[11] = this.modl[8] * this.proj[3] + this.modl[9] * this.proj[7] + this.modl[10] * this.proj[11] + this.modl[11] * this.proj[15];
		this.clip[12] = this.modl[12] * this.proj[0] + this.modl[13] * this.proj[4] + this.modl[14] * this.proj[8] + this.modl[15] * this.proj[12];
		this.clip[13] = this.modl[12] * this.proj[1] + this.modl[13] * this.proj[5] + this.modl[14] * this.proj[9] + this.modl[15] * this.proj[13];
		this.clip[14] = this.modl[12] * this.proj[2] + this.modl[13] * this.proj[6] + this.modl[14] * this.proj[10] + this.modl[15] * this.proj[14];
		this.clip[15] = this.modl[12] * this.proj[3] + this.modl[13] * this.proj[7] + this.modl[14] * this.proj[11] + this.modl[15] * this.proj[15];
		this.m_Frustum[0][0] = this.clip[3] - this.clip[0];
		this.m_Frustum[0][1] = this.clip[7] - this.clip[4];
		this.m_Frustum[0][2] = this.clip[11] - this.clip[8];
		this.m_Frustum[0][3] = this.clip[15] - this.clip[12];
		this.normalizePlane(this.m_Frustum, 0);
		this.m_Frustum[1][0] = this.clip[3] + this.clip[0];
		this.m_Frustum[1][1] = this.clip[7] + this.clip[4];
		this.m_Frustum[1][2] = this.clip[11] + this.clip[8];
		this.m_Frustum[1][3] = this.clip[15] + this.clip[12];
		this.normalizePlane(this.m_Frustum, 1);
		this.m_Frustum[2][0] = this.clip[3] + this.clip[1];
		this.m_Frustum[2][1] = this.clip[7] + this.clip[5];
		this.m_Frustum[2][2] = this.clip[11] + this.clip[9];
		this.m_Frustum[2][3] = this.clip[15] + this.clip[13];
		this.normalizePlane(this.m_Frustum, 2);
		this.m_Frustum[3][0] = this.clip[3] - this.clip[1];
		this.m_Frustum[3][1] = this.clip[7] - this.clip[5];
		this.m_Frustum[3][2] = this.clip[11] - this.clip[9];
		this.m_Frustum[3][3] = this.clip[15] - this.clip[13];
		this.normalizePlane(this.m_Frustum, 3);
		this.m_Frustum[4][0] = this.clip[3] - this.clip[2];
		this.m_Frustum[4][1] = this.clip[7] - this.clip[6];
		this.m_Frustum[4][2] = this.clip[11] - this.clip[10];
		this.m_Frustum[4][3] = this.clip[15] - this.clip[14];
		this.normalizePlane(this.m_Frustum, 4);
		this.m_Frustum[5][0] = this.clip[3] + this.clip[2];
		this.m_Frustum[5][1] = this.clip[7] + this.clip[6];
		this.m_Frustum[5][2] = this.clip[11] + this.clip[10];
		this.m_Frustum[5][3] = this.clip[15] + this.clip[14];
		this.normalizePlane(this.m_Frustum, 5);
	}

	public boolean pointInFrustum(float var1, float var2, float var3) {
		for(int var4 = 0; var4 < 6; ++var4) {
			if(this.m_Frustum[var4][0] * var1 + this.m_Frustum[var4][1] * var2 + this.m_Frustum[var4][2] * var3 + this.m_Frustum[var4][3] <= 0.0F) {
				return false;
			}
		}

		return true;
	}

	public boolean sphereInFrustum(float var1, float var2, float var3, float var4) {
		for(int var5 = 0; var5 < 6; ++var5) {
			if(this.m_Frustum[var5][0] * var1 + this.m_Frustum[var5][1] * var2 + this.m_Frustum[var5][2] * var3 + this.m_Frustum[var5][3] <= -var4) {
				return false;
			}
		}

		return true;
	}

	public boolean cubeFullyInFrustum(float var1, float var2, float var3, float var4, float var5, float var6) {
		for(int var7 = 0; var7 < 6; ++var7) {
			if(this.m_Frustum[var7][0] * var1 + this.m_Frustum[var7][1] * var2 + this.m_Frustum[var7][2] * var3 + this.m_Frustum[var7][3] <= 0.0F) {
				return false;
			}

			if(this.m_Frustum[var7][0] * var4 + this.m_Frustum[var7][1] * var2 + this.m_Frustum[var7][2] * var3 + this.m_Frustum[var7][3] <= 0.0F) {
				return false;
			}

			if(this.m_Frustum[var7][0] * var1 + this.m_Frustum[var7][1] * var5 + this.m_Frustum[var7][2] * var3 + this.m_Frustum[var7][3] <= 0.0F) {
				return false;
			}

			if(this.m_Frustum[var7][0] * var4 + this.m_Frustum[var7][1] * var5 + this.m_Frustum[var7][2] * var3 + this.m_Frustum[var7][3] <= 0.0F) {
				return false;
			}

			if(this.m_Frustum[var7][0] * var1 + this.m_Frustum[var7][1] * var2 + this.m_Frustum[var7][2] * var6 + this.m_Frustum[var7][3] <= 0.0F) {
				return false;
			}

			if(this.m_Frustum[var7][0] * var4 + this.m_Frustum[var7][1] * var2 + this.m_Frustum[var7][2] * var6 + this.m_Frustum[var7][3] <= 0.0F) {
				return false;
			}

			if(this.m_Frustum[var7][0] * var1 + this.m_Frustum[var7][1] * var5 + this.m_Frustum[var7][2] * var6 + this.m_Frustum[var7][3] <= 0.0F) {
				return false;
			}

			if(this.m_Frustum[var7][0] * var4 + this.m_Frustum[var7][1] * var5 + this.m_Frustum[var7][2] * var6 + this.m_Frustum[var7][3] <= 0.0F) {
				return false;
			}
		}

		return true;
	}

	public boolean cubeInFrustum(float var1, float var2, float var3, float var4, float var5, float var6) {
		for(int var7 = 0; var7 < 6; ++var7) {
			if(this.m_Frustum[var7][0] * var1 + this.m_Frustum[var7][1] * var2 + this.m_Frustum[var7][2] * var3 + this.m_Frustum[var7][3] <= 0.0F && this.m_Frustum[var7][0] * var4 + this.m_Frustum[var7][1] * var2 + this.m_Frustum[var7][2] * var3 + this.m_Frustum[var7][3] <= 0.0F && this.m_Frustum[var7][0] * var1 + this.m_Frustum[var7][1] * var5 + this.m_Frustum[var7][2] * var3 + this.m_Frustum[var7][3] <= 0.0F && this.m_Frustum[var7][0] * var4 + this.m_Frustum[var7][1] * var5 + this.m_Frustum[var7][2] * var3 + this.m_Frustum[var7][3] <= 0.0F && this.m_Frustum[var7][0] * var1 + this.m_Frustum[var7][1] * var2 + this.m_Frustum[var7][2] * var6 + this.m_Frustum[var7][3] <= 0.0F && this.m_Frustum[var7][0] * var4 + this.m_Frustum[var7][1] * var2 + this.m_Frustum[var7][2] * var6 + this.m_Frustum[var7][3] <= 0.0F && this.m_Frustum[var7][0] * var1 + this.m_Frustum[var7][1] * var5 + this.m_Frustum[var7][2] * var6 + this.m_Frustum[var7][3] <= 0.0F && this.m_Frustum[var7][0] * var4 + this.m_Frustum[var7][1] * var5 + this.m_Frustum[var7][2] * var6 + this.m_Frustum[var7][3] <= 0.0F) {
				return false;
			}
		}

		return true;
	}

	public boolean isVisible(AABB var1) {
		return this.cubeInFrustum(var1.x0, var1.y0, var1.z0, var1.x1, var1.y1, var1.z1);
	}
}
