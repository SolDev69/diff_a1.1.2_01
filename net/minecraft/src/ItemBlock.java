package net.minecraft.src;

public class ItemBlock extends Item {
	private int blockID;

	public ItemBlock(int var1) {
		super(var1);
		this.blockID = var1 + 256;
		this.setIconIndex(Block.blocksList[var1 + 256].getBlockTextureFromSide(2));
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World worldObj, int x, int y, int z, int side) {
		if(worldObj.getBlockId(x, y, z) == Block.snow.blockID) {
			side = 0;
		} else {
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
		}

		if(itemStack.stackSize == 0) {
			return false;
		} else {
			if(worldObj.canBlockBePlacedAt(this.blockID, x, y, z, false)) {
				Block var8 = Block.blocksList[this.blockID];
				if(worldObj.setBlockWithNotify(x, y, z, this.blockID)) {
					Block.blocksList[this.blockID].onBlockPlaced(worldObj, x, y, z, side);
					worldObj.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), var8.stepSound.getStepSound(), (var8.stepSound.getVolume() + 1.0F) / 2.0F, var8.stepSound.getPitch() * 0.8F);
					--itemStack.stackSize;
				}
			}

			return true;
		}
	}
}
