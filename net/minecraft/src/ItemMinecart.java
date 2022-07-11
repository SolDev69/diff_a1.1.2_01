package net.minecraft.src;

public class ItemMinecart extends Item {
	public int minecartType;

	public ItemMinecart(int id, int type) {
		super(id);
		this.maxStackSize = 1;
		this.minecartType = type;
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World worldObj, int x, int y, int z, int side) {
		int var8 = worldObj.getBlockId(x, y, z);
		if(var8 == Block.minecartTrack.blockID) {
			worldObj.spawnEntityInWorld(new EntityMinecart(worldObj, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.minecartType));
			--itemStack.stackSize;
			return true;
		} else {
			return false;
		}
	}
}
