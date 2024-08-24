package com.mojang.minecraft;

import com.mojang.minecraft.level.Level;
import org.lwjgl.input.Keyboard;

public class Player extends Entity {
	public Player(Level var1) {
		super(var1);
		this.heightOffset = 1.62F;
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		float var1 = 0.0F;
		float var2 = 0.0F;
		if(Keyboard.isKeyDown(Keyboard.KEY_R)) {
			this.resetPos();
		}

		/*if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				var2-=2.0F;
			} else {
				--var2;
			}
		} I don't want this code to see the light of day. */

		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			--var2;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			++var2;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			--var1;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			++var1;
		}

		if((Keyboard.isKeyDown(Keyboard.KEY_SPACE)) && this.onGround) {
			this.yd = 0.5F;
		}

		if((Keyboard.isKeyDown(Keyboard.KEY_SPACE)) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && this.onGround) {
			this.yd = 0.75F;
		}

		this.moveRelative(var1, var2, this.onGround ? 0.1F : 0.02F);
		this.yd = (float)((double)this.yd - 0.08D);
		this.move(this.xd, this.yd, this.zd);
		this.xd *= 0.91F;
		this.yd *= 0.98F;
		this.zd *= 0.91F;
		if (this.onGround && !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            this.xd *= 0.7f;
            this.zd *= 0.7f;
        }
	}
}
