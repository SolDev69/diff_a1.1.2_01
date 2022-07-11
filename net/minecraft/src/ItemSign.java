package net.minecraft.src;

public class ItemSign extends Item {
	public ItemSign(int var1) {
		super(var1);
		this.maxDamage = 64;
		this.maxStackSize = 1;
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer entityPlayer, World world, int x, int y, int z, int var7) {
		if(var7 == 0) {
			return false;
		} else if(!world.getBlockMaterial(x, y, z).isSolid()) {
			return false;
		} else {
			if(var7 == 1) {
				++y;
			}

			if(var7 == 2) {
				--z;
			}

			if(var7 == 3) {
				++z;
			}

			if(var7 == 4) {
				--x;
			}

			if(var7 == 5) {
				++x;
			}

			if(!Block.signStanding.canPlaceBlockAt(world, x, y, z)) {
				return false;
			} else {
				if(var7 == 1) {
					world.setBlockAndMetadataWithNotify(x, y, z, Block.signStanding.blockID, MathHelper.floor_double((double)((entityPlayer.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15);
				} else {
					world.setBlockAndMetadataWithNotify(x, y, z, Block.signWall.blockID, var7);
				}

				--stack.stackSize;
				entityPlayer.displayGUIEditSign((TileEntitySign)world.getBlockTileEntity(x, y, z));
				return true;
			}
		}
	}
}
