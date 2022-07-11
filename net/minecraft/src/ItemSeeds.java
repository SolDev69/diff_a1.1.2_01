package net.minecraft.src;

public class ItemSeeds extends Item {
	private int blockType;

	public ItemSeeds(int itemID, int blockType) {
		super(itemID);
		this.blockType = blockType;
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer entityPlayer, World world, int x, int y, int z, int var7) {
		if(var7 != 1) {
			return false;
		} else {
			int var8 = world.getBlockId(x, y, z);
			if(var8 == Block.tilledField.blockID) {
				world.setBlockWithNotify(x, y + 1, z, this.blockType);
				--stack.stackSize;
				return true;
			} else {
				return false;
			}
		}
	}
}
