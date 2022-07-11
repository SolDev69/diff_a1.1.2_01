package net.minecraft.src;

public abstract class BlockContainer extends Block {
	protected BlockContainer(int var1, Material var2) {
		super(var1, var2);
		isBlockContainer[var1] = true;
	}

	protected BlockContainer(int var1, int var2, Material var3) {
		super(var1, var2, var3);
	}

	public void onBlockAdded(World worldObj, int x, int y, int z) {
		super.onBlockAdded(worldObj, x, y, z);
		worldObj.setBlockTileEntity(x, y, z, this.getBlockEntity());
	}

	public void onBlockRemoval(World worldObj, int x, int y, int z) {
		super.onBlockRemoval(worldObj, x, y, z);
		worldObj.removeBlockTileEntity(x, y, z);
	}

	protected abstract TileEntity getBlockEntity();
}
