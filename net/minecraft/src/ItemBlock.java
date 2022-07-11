package net.minecraft.src;

public class ItemBlock extends Item {
	private int blockID;

	public ItemBlock(int var1) {
		super(var1);
		this.blockID = var1 + 256;
		this.setIconIndex(Block.blocksList[var1 + 256].getBlockTextureFromSide(2));
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer entityPlayer, World world, int x, int y, int z, int var7) {
		if(world.getBlockId(x, y, z) == Block.snow.blockID) {
			var7 = 0;
		} else {
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
		}

		if(stack.stackSize == 0) {
			return false;
		} else {
			if(world.canBlockBePlacedAt(this.blockID, x, y, z, false)) {
				Block var8 = Block.blocksList[this.blockID];
				if(world.setBlockWithNotify(x, y, z, this.blockID)) {
					Block.blocksList[this.blockID].onBlockPlaced(world, x, y, z, var7);
					world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), var8.stepSound.getStepSound(), (var8.stepSound.getVolume() + 1.0F) / 2.0F, var8.stepSound.getPitch() * 0.8F);
					--stack.stackSize;
				}
			}

			return true;
		}
	}
}
