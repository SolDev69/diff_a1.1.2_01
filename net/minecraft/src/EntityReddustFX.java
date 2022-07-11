package net.minecraft.src;

public class EntityReddustFX extends EntityFX {
	float reddustParticleScale;

	public EntityReddustFX(World var1, double var2, double var4, double var6) {
		this(var1, var2, var4, var6, 1.0F);
	}

	public EntityReddustFX(World var1, double var2, double var4, double var6, float var8) {
		super(var1, var2, var4, var6, 0.0D, 0.0D, 0.0D);
		this.motionX *= (double)0.1F;
		this.motionY *= (double)0.1F;
		this.motionZ *= (double)0.1F;
		this.particleRed = (float)(Math.random() * (double)0.3F) + 0.7F;
		this.particleGreen = this.particleBlue = (float)(Math.random() * (double)0.1F);
		this.particleScale *= 0.75F;
		this.particleScale *= var8;
		this.reddustParticleScale = this.particleScale;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
		this.particleMaxAge = (int)((float)this.particleMaxAge * var8);
		this.noClip = false;
	}

	public void renderParticle(Tessellator tessellator, float renderPartialTick, float xOffset, float yOffset, float zOffset, float xOffset2, float zOffset2) {
		float var8 = ((float)this.particleAge + renderPartialTick) / (float)this.particleMaxAge * 32.0F;
		if(var8 < 0.0F) {
			var8 = 0.0F;
		}

		if(var8 > 1.0F) {
			var8 = 1.0F;
		}

		this.particleScale = this.reddustParticleScale * var8;
		super.renderParticle(tessellator, renderPartialTick, xOffset, yOffset, zOffset, xOffset2, zOffset2);
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setEntityDead();
		}

		this.particleTextureIndex = 7 - this.particleAge * 8 / this.particleMaxAge;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		if(this.posY == this.prevPosY) {
			this.motionX *= 1.1D;
			this.motionZ *= 1.1D;
		}

		this.motionX *= (double)0.96F;
		this.motionY *= (double)0.96F;
		this.motionZ *= (double)0.96F;
		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
		}

	}
}