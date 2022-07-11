package net.minecraft.src;

public class ItemRedstone extends Item {
	public ItemRedstone(int var1) {
		super(var1);
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World worldObj, int x, int y, int z, int side) {
		if(side == 0) {
			--y;
		}

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

		if(worldObj.getBlockId(x, y, z) != 0) {
			return false;
		} else {
			if(Block.redstoneWire.canPlaceBlockAt(worldObj, x, y, z)) {
				--itemStack.stackSize;
				worldObj.setBlockWithNotify(x, y, z, Block.redstoneWire.blockID);
			}

			return true;
		}
	}
}
