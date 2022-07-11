package net.minecraft.src;

import java.util.Random;

public class BlockFlower extends Block {
	protected BlockFlower(int id, int blockIndex) {
		super(id, Material.plants);
		this.blockIndexInTexture = blockIndex;
		this.setTickOnLoad(true);
		float var3 = 0.2F;
		this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 3.0F, 0.5F + var3);
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return this.canThisPlantGrowOnThisBlockID(world.getBlockId(x, y - 1, z));
	}

	protected boolean canThisPlantGrowOnThisBlockID(int id) {
		return id == Block.grass.blockID || id == Block.dirt.blockID || id == Block.tilledField.blockID;
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		super.onNeighborBlockChange(world, x, y, z, flag);
		this.g(world, x, y, z);
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		this.g(world, x, y, z);
	}

	protected final void g(World var1, int var2, int var3, int var4) {
		if(!this.canBlockStay(var1, var2, var3, var4)) {
			this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
			var1.setBlockWithNotify(var2, var3, var4, 0);
		}

	}

	public boolean canBlockStay(World world, int x, int y, int z) {
		return (world.getBlockLightValue(x, y, z) >= 8 || world.canBlockSeeTheSky(x, y, z)) && this.canThisPlantGrowOnThisBlockID(world.getBlockId(x, y - 1, z));
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 1;
	}
}
