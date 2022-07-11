package net.minecraft.src;

public class ItemBoat extends Item {
	public ItemBoat(int var1) {
		super(var1);
		this.maxStackSize = 1;
	}

	public ItemStack onItemRightClick(ItemStack itemStack, World worldObj, EntityPlayer entityPlayer) {
		float var4 = 1.0F;
		float var5 = entityPlayer.prevRotationPitch + (entityPlayer.rotationPitch - entityPlayer.prevRotationPitch) * var4;
		float var6 = entityPlayer.prevRotationYaw + (entityPlayer.rotationYaw - entityPlayer.prevRotationYaw) * var4;
		double var7 = entityPlayer.prevPosX + (entityPlayer.posX - entityPlayer.prevPosX) * (double)var4;
		double var9 = entityPlayer.prevPosY + (entityPlayer.posY - entityPlayer.prevPosY) * (double)var4;
		double var11 = entityPlayer.prevPosZ + (entityPlayer.posZ - entityPlayer.prevPosZ) * (double)var4;
		Vec3D var13 = Vec3D.createVector(var7, var9, var11);
		float var14 = MathHelper.cos(-var6 * 0.017453292F - (float)Math.PI);
		float var15 = MathHelper.sin(-var6 * 0.017453292F - (float)Math.PI);
		float var16 = -MathHelper.cos(-var5 * 0.017453292F);
		float var17 = MathHelper.sin(-var5 * 0.017453292F);
		float var18 = var15 * var16;
		float var20 = var14 * var16;
		double var21 = 5.0D;
		Vec3D var23 = var13.addVector((double)var18 * var21, (double)var17 * var21, (double)var20 * var21);
		MovingObjectPosition var24 = worldObj.rayTraceBlocks_do(var13, var23, true);
		if(var24 == null) {
			return itemStack;
		} else {
			if(var24.typeOfHit == 0) {
				int var25 = var24.blockX;
				int var26 = var24.blockY;
				int var27 = var24.blockZ;
				worldObj.spawnEntityInWorld(new EntityBoat(worldObj, (double)((float)var25 + 0.5F), (double)((float)var26 + 1.5F), (double)((float)var27 + 0.5F)));
				--itemStack.stackSize;
			}

			return itemStack;
		}
	}
}
