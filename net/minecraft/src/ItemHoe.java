package net.minecraft.src;

public class ItemHoe extends Item {
	public ItemHoe(int itemID, int maxDamage) {
		super(itemID);
		this.maxStackSize = 1;
		this.maxDamage = 32 << maxDamage;
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer entityPlayer, World world, int x, int y, int z, int var7) {
		int var8 = world.getBlockId(x, y, z);
		Material var9 = world.getBlockMaterial(x, y + 1, z);
		if((var9.isSolid() || var8 != Block.grass.blockID) && var8 != Block.dirt.blockID) {
			return false;
		} else {
			Block var10 = Block.tilledField;
			world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), var10.stepSound.getStepSound(), (var10.stepSound.getVolume() + 1.0F) / 2.0F, var10.stepSound.getPitch() * 0.8F);
			world.setBlockWithNotify(x, y, z, var10.blockID);
			stack.damageItem(1);
			if(world.rand.nextInt(8) == 0 && var8 == Block.grass.blockID) {
				byte var11 = 1;

				for(int var12 = 0; var12 < var11; ++var12) {
					float var13 = 0.7F;
					float var14 = world.rand.nextFloat() * var13 + (1.0F - var13) * 0.5F;
					float var15 = 1.2F;
					float var16 = world.rand.nextFloat() * var13 + (1.0F - var13) * 0.5F;
					EntityItem var17 = new EntityItem(world, (double)((float)x + var14), (double)((float)y + var15), (double)((float)z + var16), new ItemStack(Item.seeds));
					var17.delayBeforeCanPickup = 10;
					world.spawnEntityInWorld(var17);
				}
			}

			return true;
		}
	}
}
