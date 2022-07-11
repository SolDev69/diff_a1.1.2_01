package net.minecraft.src;

public class ItemHoe extends Item {
	public ItemHoe(int id, int strength) {
		super(id);
		this.maxStackSize = 1;
		this.maxDamage = 32 << strength;
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World worldObj, int x, int y, int z, int side) {
		int var8 = worldObj.getBlockId(x, y, z);
		Material var9 = worldObj.getBlockMaterial(x, y + 1, z);
		if((var9.isSolid() || var8 != Block.grass.blockID) && var8 != Block.dirt.blockID) {
			return false;
		} else {
			Block var10 = Block.tilledField;
			worldObj.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), var10.stepSound.getStepSound(), (var10.stepSound.getVolume() + 1.0F) / 2.0F, var10.stepSound.getPitch() * 0.8F);
			worldObj.setBlockWithNotify(x, y, z, var10.blockID);
			itemStack.damageItem(1);
			if(worldObj.rand.nextInt(8) == 0 && var8 == Block.grass.blockID) {
				byte var11 = 1;

				for(int var12 = 0; var12 < var11; ++var12) {
					float var13 = 0.7F;
					float var14 = worldObj.rand.nextFloat() * var13 + (1.0F - var13) * 0.5F;
					float var15 = 1.2F;
					float var16 = worldObj.rand.nextFloat() * var13 + (1.0F - var13) * 0.5F;
					EntityItem var17 = new EntityItem(worldObj, (double)((float)x + var14), (double)((float)y + var15), (double)((float)z + var16), new ItemStack(Item.seeds));
					var17.delayBeforeCanPickup = 10;
					worldObj.spawnEntityInWorld(var17);
				}
			}

			return true;
		}
	}

	public boolean isFull3D() {
		return true;
	}
}
