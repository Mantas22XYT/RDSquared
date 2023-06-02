package com.mojang.minecraft;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Textures {
	private static HashMap idMap = new HashMap();

	public static int loadTexture(String var0, int var1) {
		try {
			if(idMap.containsKey(var0)) {
				return ((Integer)idMap.get(var0)).intValue();
			} else {
				IntBuffer var2 = BufferUtils.createIntBuffer(1);
				var2.clear();
				GL11.glGenTextures(var2);
				int var3 = var2.get(0);
				idMap.put(var0, Integer.valueOf(var3));
				System.out.println("DEBUG: " + var0 + " -> " + var3);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var3);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, var1);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, var1);
				BufferedImage var4 = ImageIO.read(Textures.class.getResourceAsStream(var0));
				int var5 = var4.getWidth();
				int var6 = var4.getHeight();
				ByteBuffer var7 = BufferUtils.createByteBuffer(var5 * var6 * 4);
				int[] var8 = new int[var5 * var6];
				var4.getRGB(0, 0, var5, var6, var8, 0, var5);

				for(int var9 = 0; var9 < var8.length; ++var9) {
					int var10 = var8[var9] >> 24 & 255;
					int var11 = var8[var9] >> 16 & 255;
					int var12 = var8[var9] >> 8 & 255;
					int var13 = var8[var9] & 255;
					var8[var9] = var10 << 24 | var13 << 16 | var12 << 8 | var11;
				}

				var7.asIntBuffer().put(var8);
				GLU.gluBuild2DMipmaps(3553, 6408, var5, var6, 6408, 5121, var7);
				return var3;
			}
		} catch (IOException var14) {
			throw new RuntimeException("!!");
		}
	}
}
