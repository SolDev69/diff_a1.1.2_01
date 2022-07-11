package net.minecraft.src;

import java.util.Random;

public class BlockLadder extends Block {
	protected BlockLadder(int id, int blockIndex) {
		super(id, blockIndex, Material.circuits);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		int var5 = world.getBlockMetadata(x, y, z);
		float var6 = 0.125F;
		if(var5 == 2) {
			this.setBlockBounds(0.0F, 0.0F, 1.0F - var6, 1.0F, 1.0F, 1.0F);
		}

		if(var5 == 3) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var6);
		}

		if(var5 == 4) {
			this.setBlockBounds(1.0F - var6, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}

		if(var5 == 5) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, var6, 1.0F, 1.0F);
		}

		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 8;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x - 1, y, z) ? true : (world.isBlockNormalCube(x + 1, y, z) ? true : (world.isBlockNormalCube(x, y, z - 1) ? true : world.isBlockNormalCube(x, y, z + 1)));
	}

	public void onBlockPlaced(World world, int x, int y, int z, int notifyFlag) {
		int var6 = world.getBlockMetadata(x, y, z);
		if((var6 == 0 || notifyFlag == 2) && world.isBlockNormalCube(x, y, z + 1)) {
			var6 = 2;
		}

		if((var6 == 0 || notifyFlag == 3) && world.isBlockNormalCube(x, y, z - 1)) {
			var6 = 3;
		}

		if((var6 == 0 || notifyFlag == 4) && world.isBlockNormalCube(x + 1, y, z)) {
			var6 = 4;
		}

		if((var6 == 0 || notifyFlag == 5) && world.isBlockNormalCube(x - 1, y, z)) {
			var6 = 5;
		}

		world.setBlockMetadataWithNotify(x, y, z, var6);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		int var6 = world.getBlockMetadata(x, y, z);
		boolean var7 = false;
		if(var6 == 2 && world.isBlockNormalCube(x, y, z + 1)) {
			var7 = true;
		}

		if(var6 == 3 && world.isBlockNormalCube(x, y, z - 1)) {
			var7 = true;
		}

		if(var6 == 4 && world.isBlockNormalCube(x + 1, y, z)) {
			var7 = true;
		}

		if(var6 == 5 && world.isBlockNormalCube(x - 1, y, z)) {
			var7 = true;
		}

		if(!var7) {
			this.dropBlockAsItem(world, x, y, z, var6);
			world.setBlockWithNotify(x, y, z, 0);
		}

		super.onNeighborBlockChange(world, x, y, z, flag);
	}

	public int quantityDropped(Random random) {
		return 1;
	}
}
