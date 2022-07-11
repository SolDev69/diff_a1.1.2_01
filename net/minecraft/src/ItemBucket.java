package net.minecraft.src;

public class ItemBucket extends Item {
	private int isFull;

	public ItemBucket(int id, int liquid) {
		super(id);
		this.maxStackSize = 1;
		this.maxDamage = 64;
		this.isFull = liquid;
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
		MovingObjectPosition var24 = worldObj.rayTraceBlocks_do(var13, var23, this.isFull == 0);
		if(var24 == null) {
			return itemStack;
		} else {
			if(var24.typeOfHit == 0) {
				int var25 = var24.blockX;
				int var26 = var24.blockY;
				int var27 = var24.blockZ;
				if(this.isFull == 0) {
					if(worldObj.getBlockMaterial(var25, var26, var27) == Material.water && worldObj.getBlockMetadata(var25, var26, var27) == 0) {
						worldObj.setBlockWithNotify(var25, var26, var27, 0);
						return new ItemStack(Item.bucketWater);
					}

					if(worldObj.getBlockMaterial(var25, var26, var27) == Material.lava && worldObj.getBlockMetadata(var25, var26, var27) == 0) {
						worldObj.setBlockWithNotify(var25, var26, var27, 0);
						return new ItemStack(Item.bucketLava);
					}
				} else {
					if(this.isFull < 0) {
						return new ItemStack(Item.bucketEmpty);
					}

					if(var24.sideHit == 0) {
						--var26;
					}

					if(var24.sideHit == 1) {
						++var26;
					}

					if(var24.sideHit == 2) {
						--var27;
					}

					if(var24.sideHit == 3) {
						++var27;
					}

					if(var24.sideHit == 4) {
						--var25;
					}

					if(var24.sideHit == 5) {
						++var25;
					}

					if(worldObj.getBlockId(var25, var26, var27) == 0 || !worldObj.getBlockMaterial(var25, var26, var27).isSolid()) {
						worldObj.setBlockAndMetadataWithNotify(var25, var26, var27, this.isFull, 0);
						return new ItemStack(Item.bucketEmpty);
					}
				}
			} else if(this.isFull == 0 && var24.entityHit instanceof EntityCow) {
				return new ItemStack(Item.bucketMilk);
			}

			return itemStack;
		}
	}
}
