package net.minecraft.src;

public class ItemSign extends Item {
	public ItemSign(int var1) {
		super(var1);
		this.maxDamage = 64;
		this.maxStackSize = 1;
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World worldObj, int x, int y, int z, int side) {
		if(side == 0) {
			return false;
		} else if(!worldObj.getBlockMaterial(x, y, z).isSolid()) {
			return false;
		} else {
			if(side == 1) {
				++y;
			}

			if(side == 2) {
				--z;
			}

			if(side == 3) {
				++z;
			}

			if(side == 4) {
				--x;
			}

			if(side == 5) {
				++x;
			}

			if(!Block.signStanding.canPlaceBlockAt(worldObj, x, y, z)) {
				return false;
			} else {
				if(side == 1) {
					worldObj.setBlockAndMetadataWithNotify(x, y, z, Block.signStanding.blockID, MathHelper.floor_double((double)((entityPlayer.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15);
				} else {
					worldObj.setBlockAndMetadataWithNotify(x, y, z, Block.signWall.blockID, side);
				}

				--itemStack.stackSize;
				entityPlayer.displayGUIEditSign((TileEntitySign)worldObj.getBlockTileEntity(x, y, z));
				return true;
			}
		}
	}
}
