package net.minecraft.src;

public class ItemRedstone extends Item {
	public ItemRedstone(int var1) {
		super(var1);
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer entityPlayer, World world, int x, int y, int z, int var7) {
		if(var7 == 0) {
			--y;
		}

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

		if(world.getBlockId(x, y, z) != 0) {
			return false;
		} else {
			if(Block.redstoneWire.canPlaceBlockAt(world, x, y, z)) {
				--stack.stackSize;
				world.setBlockWithNotify(x, y, z, Block.redstoneWire.blockID);
			}

			return true;
		}
	}
}
