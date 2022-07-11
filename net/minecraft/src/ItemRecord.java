package net.minecraft.src;

public class ItemRecord extends Item {
	private String recordName;

	protected ItemRecord(int id, String record) {
		super(id);
		this.recordName = record;
		this.maxStackSize = 1;
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World worldObj, int x, int y, int z, int side) {
		if(worldObj.getBlockId(x, y, z) == Block.jukebox.blockID && worldObj.getBlockMetadata(x, y, z) == 0) {
			worldObj.setBlockMetadataWithNotify(x, y, z, this.shiftedIndex - Item.record13.shiftedIndex + 1);
			worldObj.playRecord(this.recordName, x, y, z);
			--itemStack.stackSize;
			return true;
		} else {
			return false;
		}
	}
}
