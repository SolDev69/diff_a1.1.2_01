package net.minecraft.src;

public class EntityDiggingFX extends EntityFX {
	public EntityDiggingFX(World worldObj, double x, double y, double z, double motionX, double motionY, double motionZ, Block block) {
		super(worldObj, x, y, z, motionX, motionY, motionZ);
		this.particleTextureIndex = block.blockIndexInTexture;
		this.particleGravity = block.blockParticleGravity;
		this.particleRed = this.particleGreen = this.particleBlue = 0.6F;
		this.particleScale /= 2.0F;
	}

	public int getFXLayer() {
		return 1;
	}

	public void renderParticle(Tessellator tessellator, float renderPartialTick, float xOffset, float yOffset, float zOffset, float xOffset2, float zOffset2) {
		float var8 = ((float)(this.particleTextureIndex % 16) + this.particleTextureJitterX / 4.0F) / 16.0F;
		float var9 = var8 + 0.015609375F;
		float var10 = ((float)(this.particleTextureIndex / 16) + this.particleTextureJitterY / 4.0F) / 16.0F;
		float var11 = var10 + 0.015609375F;
		float var12 = 0.1F * this.particleScale;
		float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)renderPartialTick - interpPosX);
		float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)renderPartialTick - interpPosY);
		float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)renderPartialTick - interpPosZ);
		float var16 = this.getBrightness(renderPartialTick);
		tessellator.setColorOpaque_F(var16 * this.particleRed, var16 * this.particleGreen, var16 * this.particleBlue);
		tessellator.addVertexWithUV((double)(var13 - xOffset * var12 - xOffset2 * var12), (double)(var14 - yOffset * var12), (double)(var15 - zOffset * var12 - zOffset2 * var12), (double)var8, (double)var11);
		tessellator.addVertexWithUV((double)(var13 - xOffset * var12 + xOffset2 * var12), (double)(var14 + yOffset * var12), (double)(var15 - zOffset * var12 + zOffset2 * var12), (double)var8, (double)var10);
		tessellator.addVertexWithUV((double)(var13 + xOffset * var12 + xOffset2 * var12), (double)(var14 + yOffset * var12), (double)(var15 + zOffset * var12 + zOffset2 * var12), (double)var9, (double)var10);
		tessellator.addVertexWithUV((double)(var13 + xOffset * var12 - xOffset2 * var12), (double)(var14 - yOffset * var12), (double)(var15 + zOffset * var12 - zOffset2 * var12), (double)var9, (double)var11);
	}
}
