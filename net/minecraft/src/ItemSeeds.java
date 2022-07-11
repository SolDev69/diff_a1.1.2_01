package net.minecraft.src;

public class ItemSeeds extends Item {
	private int blockType;

	public ItemSeeds(int id, int type) {
		super(id);
		this.blockType = type;
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World worldObj, int x, int y, int z, int side) {
		if(side != 1) {
			return false;
		} else {
			int var8 = worldObj.getBlockId(x, y, z);
			if(var8 == Block.tilledField.blockID) {
				worldObj.setBlockWithNotify(x, y + 1, z, this.blockType);
				--itemStack.stackSize;
				return true;
			} else {
				return false;
			}
		}
	}
}
