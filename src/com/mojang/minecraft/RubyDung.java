package com.mojang.minecraft;

import com.mojang.minecraft.character.Zombie;
import com.mojang.minecraft.level.Chunk;
import com.mojang.minecraft.level.Frustum;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelRenderer;
import com.mojang.minecraft.level.Tesselator;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.particle.ParticleEngine;
import java.awt.Component;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class RubyDung implements Runnable {
	private static final boolean FULLSCREEN_MODE = false;
	private int width;
	private int height;
	private FloatBuffer fogColor0 = BufferUtils.createFloatBuffer(4);
	private FloatBuffer fogColor1 = BufferUtils.createFloatBuffer(4);
	private Timer timer = new Timer(20.0F);
	private Level level;
	private LevelRenderer levelRenderer;
	private Player player;
	private int paintTexture = 1;
	private ParticleEngine particleEngine;
	private ArrayList zombies = new ArrayList();
	private IntBuffer viewportBuffer = BufferUtils.createIntBuffer(16);
	private IntBuffer selectBuffer = BufferUtils.createIntBuffer(2000);
	private HitResult hitResult = null;
	FloatBuffer lb = BufferUtils.createFloatBuffer(16);

	public void init() throws LWJGLException, IOException {
		int var1 = 16710650;
		int var2 = 920330;
		float var3 = 0.5F;
		float var4 = 0.8F;
		float var5 = 1.0F;
		this.fogColor0.put(new float[]{(float)(var1 >> 16 & 255) / 255.0F, (float)(var1 >> 8 & 255) / 255.0F, (float)(var1 & 255) / 255.0F, 1.0F});
		this.fogColor0.flip();
		this.fogColor1.put(new float[]{(float)(var2 >> 16 & 255) / 255.0F, (float)(var2 >> 8 & 255) / 255.0F, (float)(var2 & 255) / 255.0F, 1.0F});
		this.fogColor1.flip();
		Display.setDisplayMode(new DisplayMode(1024, 768));
		Display.create();
		Keyboard.create();
		Mouse.create();
		this.width = Display.getDisplayMode().getWidth();
		this.height = Display.getDisplayMode().getHeight();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(var3, var4, var5, 0.0F);
		GL11.glClearDepth(1.0D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		this.level = new Level(512, 512, 128);
		this.levelRenderer = new LevelRenderer(this.level);
		this.player = new Player(this.level);
		this.particleEngine = new ParticleEngine(this.level);
		Mouse.setGrabbed(true);
		

		for(int var6 = 0; var6 < 10; ++var6) {
			Zombie var7 = new Zombie(this.level, 128.0F, 0.0F, 128.0F);
			var7.resetPos();
			this.zombies.add(var7);
		}

	}

	public void destroy() {
		this.level.save();
		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
	}

	public void run() {
		try {
			this.init();
		} catch (Exception var9) {
			JOptionPane.showMessageDialog((Component)null, var9.toString(), "Failed to start RD^2", 0);
			System.exit(0);
		}

		long var1 = System.currentTimeMillis();
		int var3 = 0;

		try {
			while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Display.isCloseRequested()) {
				this.timer.advanceTime();

				for(int var4 = 0; var4 < this.timer.ticks; ++var4) {
					this.tick();
				}

				this.render(this.timer.a);
				++var3;

				while(System.currentTimeMillis() >= var1 + 1000L) {
					System.out.println("DEBUG: " + var3 + " fps, " + Chunk.updates + " chunk updates");
					Chunk.updates = 0;
					var1 += 1000L;
					var3 = 0;
				}
			}
		} catch (Exception var10) {
			var10.printStackTrace();
		} finally {
			this.destroy();
		}

	}

	public void tick() {
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					this.level.save();
				}

				if(Keyboard.getEventKey() == Keyboard.KEY_1) {
					this.paintTexture = 1;
				}

				if(Keyboard.getEventKey() == Keyboard.KEY_2) {
					this.paintTexture = 2;
				}

				if(Keyboard.getEventKey() == Keyboard.KEY_3) {
					this.paintTexture = 3;
				}

				if(Keyboard.getEventKey() == Keyboard.KEY_4) {
					this.paintTexture = 4;
				}

				if(Keyboard.getEventKey() == Keyboard.KEY_5) {
					this.paintTexture = 5;
				}

				if(Keyboard.getEventKey() == Keyboard.KEY_6) {
					this.paintTexture = 6;
				}
				
				if(Keyboard.getEventKey() == Keyboard.KEY_7) {
					this.paintTexture = 7;
				}
				
				if(Keyboard.getEventKey() == Keyboard.KEY_8) {
					this.paintTexture = 8;
				}
				
				if(Keyboard.getEventKey() == Keyboard.KEY_9) {
					this.paintTexture = 9;
				}
				
				if(Keyboard.getEventKey() == Keyboard.KEY_0) {
					this.paintTexture = 10;
				}
				
				if(Keyboard.getEventKey() == Keyboard.KEY_P) {
					this.paintTexture = 11;
				}
				
				while(Keyboard.getEventKey() == Keyboard.KEY_G) {
					this.zombies.add(new Zombie(this.level, this.player.x, this.player.y, this.player.z));
				}
			}
		}

		this.level.tick();
		this.particleEngine.tick();

		for(int var1 = 0; var1 < this.zombies.size(); ++var1) {
			((Zombie)this.zombies.get(var1)).tick();
			if(((Zombie)this.zombies.get(var1)).removed) {
				this.zombies.remove(var1--);
			}
		}

		this.player.tick();
	}

	private void moveCameraToPlayer(float var1) {
		GL11.glTranslatef(0.0F, 0.0F, -0.3F);
		GL11.glRotatef(this.player.xRot, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(this.player.yRot, 0.0F, 1.0F, 0.0F);
		float var2 = this.player.xo + (this.player.x - this.player.xo) * var1;
		float var3 = this.player.yo + (this.player.y - this.player.yo) * var1;
		float var4 = this.player.zo + (this.player.z - this.player.zo) * var1;
		GL11.glTranslatef(-var2, -var3, -var4);
	}

	private void setupCamera(float var1) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(70.0F, (float)this.width / (float)this.height, 0.05F, 1000.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		this.moveCameraToPlayer(var1);
	}

	private void setupPickCamera(float var1, int var2, int var3) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		this.viewportBuffer.clear();
		GL11.glGetInteger(GL11.GL_VIEWPORT, this.viewportBuffer);
		this.viewportBuffer.flip();
		this.viewportBuffer.limit(16);
		GLU.gluPickMatrix((float)var2, (float)var3, 5.0F, 5.0F, this.viewportBuffer);
		GLU.gluPerspective(70.0F, (float)this.width / (float)this.height, 0.05F, 1000.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		this.moveCameraToPlayer(var1);
	}

	private void pick(float var1) {
		this.selectBuffer.clear();
		GL11.glSelectBuffer(this.selectBuffer);
		GL11.glRenderMode(GL11.GL_SELECT);
		this.setupPickCamera(var1, this.width / 2, this.height / 2);
		this.levelRenderer.pick(this.player, Frustum.getFrustum());
		int var2 = GL11.glRenderMode(GL11.GL_RENDER);
		this.selectBuffer.flip();
		this.selectBuffer.limit(this.selectBuffer.capacity());
		long var3 = 0L;
		int[] var5 = new int[10];
		int var6 = 0;

		for(int var7 = 0; var7 < var2; ++var7) {
			int var8 = this.selectBuffer.get();
			long var9 = (long)this.selectBuffer.get();
			this.selectBuffer.get();
			int var13;
			if(var9 >= var3 && var7 != 0) {
				for(var13 = 0; var13 < var8; ++var13) {
					this.selectBuffer.get();
				}
			} else {
				var3 = var9;
				var6 = var8;

				for(var13 = 0; var13 < var8; ++var13) {
					var5[var13] = this.selectBuffer.get();
				}
			}
		}

		if(var6 > 0) {
			this.hitResult = new HitResult(var5[0], var5[1], var5[2], var5[3], var5[4]);
		} else {
			this.hitResult = null;
		}

	}

	public void render(float var1) {
		float var2 = (float)Mouse.getDX();
		float var3 = (float)Mouse.getDY();
		this.player.turn(var2, var3);
		this.pick(var1);

		int var8;
		while(Mouse.next()) {
			if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.hitResult != null) {
				Tile var4 = Tile.tiles[this.level.getTile(this.hitResult.x, this.hitResult.y, this.hitResult.z)];
				boolean var5 = this.level.setTile(this.hitResult.x, this.hitResult.y, this.hitResult.z, 0);
				if(var4 != null && var5) {
					var4.destroy(this.level, this.hitResult.x, this.hitResult.y, this.hitResult.z, this.particleEngine);
				}
			}

			if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState() && this.hitResult != null) {
				int var7 = this.hitResult.x;
				var8 = this.hitResult.y;
				int var6 = this.hitResult.z;
				if(this.hitResult.f == 0) {
					--var8;
				}

				if(this.hitResult.f == 1) {
					++var8;
				}

				if(this.hitResult.f == 2) {
					--var6;
				}

				if(this.hitResult.f == 3) {
					++var6;
				}

				if(this.hitResult.f == 4) {
					--var7;
				}

				if(this.hitResult.f == 5) {
					++var7;
				}

				this.level.setTile(var7, var8, var6, this.paintTexture);
			}
		}

		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		this.setupCamera(var1);
		GL11.glEnable(GL11.GL_CULL_FACE);
		Frustum var9 = Frustum.getFrustum();
		this.levelRenderer.updateDirtyChunks(this.player);
		this.setupFog(0);
		GL11.glEnable(GL11.GL_FOG);
		this.levelRenderer.render(this.player, 0);

		Zombie var10;
		for(var8 = 0; var8 < this.zombies.size(); ++var8) {
			var10 = (Zombie)this.zombies.get(var8);
			if(var10.isLit() && var9.isVisible(var10.bb)) {
				((Zombie)this.zombies.get(var8)).render(var1);
			}
		}

		this.particleEngine.render(this.player, var1, 0);
		this.setupFog(1);
		this.levelRenderer.render(this.player, 1);

		for(var8 = 0; var8 < this.zombies.size(); ++var8) {
			var10 = (Zombie)this.zombies.get(var8);
			if(!var10.isLit() && var9.isVisible(var10.bb)) {
				((Zombie)this.zombies.get(var8)).render(var1);
			}
		}

		this.particleEngine.render(this.player, var1, 1);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
		if(this.hitResult != null) {
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			this.levelRenderer.renderHit(this.hitResult);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}

		this.drawGui(var1);
		Display.update();
	}

	private void drawGui(float var1) {
		int var2 = this.width * 240 / this.height;
		int var3 = this.height * 240 / this.height;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)var2, (double)var3, 0.0D, 100.0D, 300.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(var2 - 16), 16.0F, 0.0F);
		Tesselator var4 = Tesselator.instance;
		GL11.glScalef(16.0F, 16.0F, 16.0F);
		GL11.glRotatef(30.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-1.5F, 0.5F, -0.5F);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		int var5 = Textures.loadTexture("/terrain.png", 9728);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		var4.init();
		Tile.tiles[this.paintTexture].render(var4, this.level, 0, -2, 0, 0);
		var4.flush();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
		int var6 = var2 / 2;
		int var7 = var3 / 2;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var4.init();
		var4.vertex((float)(var6 + 1), (float)(var7 - 4), 0.0F);
		var4.vertex((float)(var6 - 0), (float)(var7 - 4), 0.0F);
		var4.vertex((float)(var6 - 0), (float)(var7 + 5), 0.0F);
		var4.vertex((float)(var6 + 1), (float)(var7 + 5), 0.0F);
		var4.vertex((float)(var6 + 5), (float)(var7 - 0), 0.0F);
		var4.vertex((float)(var6 - 4), (float)(var7 - 0), 0.0F);
		var4.vertex((float)(var6 - 4), (float)(var7 + 1), 0.0F);
		var4.vertex((float)(var6 + 5), (float)(var7 + 1), 0.0F);
		var4.flush();
	}

	private void setupFog(int var1) {
		if(var1 == 0) {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			GL11.glFogf(GL11.GL_FOG_DENSITY, 0.001F);
			GL11.glFog(GL11.GL_FOG_COLOR, this.fogColor0);
			GL11.glDisable(GL11.GL_LIGHTING);
		} else if(var1 == 1) {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			GL11.glFogf(GL11.GL_FOG_DENSITY, 0.06F);
			GL11.glFog(GL11.GL_FOG_COLOR, this.fogColor1);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			float var2 = 0.6F;
			GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(var2, var2, var2, 1.0F));
		}

	}

	private FloatBuffer getBuffer(float var1, float var2, float var3, float var4) {
		this.lb.clear();
		this.lb.put(var1).put(var2).put(var3).put(var4);
		this.lb.flip();
		return this.lb;
	}

	public static void checkError() {
		int var0 = GL11.glGetError();
		if(var0 != 0) {
			throw new IllegalStateException(GLU.gluErrorString(var0));
		}
	}

	public static void main(String[] var0) throws LWJGLException {
		(new Thread(new RubyDung())).start();
	}
}
