package net.minecraft.src;

import java.util.Random;

public class BlockTorch extends Block {
	protected BlockTorch(int id, int blockIndex) {
		super(id, blockIndex, Material.circuits);
		this.setTickOnLoad(true);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 2;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x - 1, y, z) ? true : (world.isBlockNormalCube(x + 1, y, z) ? true : (world.isBlockNormalCube(x, y, z - 1) ? true : (world.isBlockNormalCube(x, y, z + 1) ? true : world.isBlockNormalCube(x, y - 1, z))));
	}

	public void onBlockPlaced(World world, int x, int y, int z, int notifyFlag) {
		int var6 = world.getBlockMetadata(x, y, z);
		if(notifyFlag == 1 && world.isBlockNormalCube(x, y - 1, z)) {
			var6 = 5;
		}

		if(notifyFlag == 2 && world.isBlockNormalCube(x, y, z + 1)) {
			var6 = 4;
		}

		if(notifyFlag == 3 && world.isBlockNormalCube(x, y, z - 1)) {
			var6 = 3;
		}

		if(notifyFlag == 4 && world.isBlockNormalCube(x + 1, y, z)) {
			var6 = 2;
		}

		if(notifyFlag == 5 && world.isBlockNormalCube(x - 1, y, z)) {
			var6 = 1;
		}

		world.setBlockMetadataWithNotify(x, y, z, var6);
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		super.updateTick(world, x, y, z, random);
		if(world.getBlockMetadata(x, y, z) == 0) {
			this.onBlockAdded(world, x, y, z);
		}

	}

	public void onBlockAdded(World world, int x, int y, int z) {
		if(world.isBlockNormalCube(x - 1, y, z)) {
			world.setBlockMetadataWithNotify(x, y, z, 1);
		} else if(world.isBlockNormalCube(x + 1, y, z)) {
			world.setBlockMetadataWithNotify(x, y, z, 2);
		} else if(world.isBlockNormalCube(x, y, z - 1)) {
			world.setBlockMetadataWithNotify(x, y, z, 3);
		} else if(world.isBlockNormalCube(x, y, z + 1)) {
			world.setBlockMetadataWithNotify(x, y, z, 4);
		} else if(world.isBlockNormalCube(x, y - 1, z)) {
			world.setBlockMetadataWithNotify(x, y, z, 5);
		}

		this.checkIfAttachedToBlock(world, x, y, z);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		if(this.checkIfAttachedToBlock(world, x, y, z)) {
			int var6 = world.getBlockMetadata(x, y, z);
			boolean var7 = false;
			if(!world.isBlockNormalCube(x - 1, y, z) && var6 == 1) {
				var7 = true;
			}

			if(!world.isBlockNormalCube(x + 1, y, z) && var6 == 2) {
				var7 = true;
			}

			if(!world.isBlockNormalCube(x, y, z - 1) && var6 == 3) {
				var7 = true;
			}

			if(!world.isBlockNormalCube(x, y, z + 1) && var6 == 4) {
				var7 = true;
			}

			if(!world.isBlockNormalCube(x, y - 1, z) && var6 == 5) {
				var7 = true;
			}

			if(var7) {
				this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
				world.setBlockWithNotify(x, y, z, 0);
			}
		}

	}

	private boolean checkIfAttachedToBlock(World world, int x, int y, int z) {
		if(!this.canPlaceBlockAt(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
			return false;
		} else {
			return true;
		}
	}

	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3D vector1, Vec3D vector2) {
		int var7 = world.getBlockMetadata(x, y, z) & 7;
		float var8 = 0.15F;
		if(var7 == 1) {
			this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
		} else if(var7 == 2) {
			this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
		} else if(var7 == 3) {
			this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
		} else if(var7 == 4) {
			this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
		} else {
			var8 = 0.1F;
			this.setBlockBounds(0.5F - var8, 0.0F, 0.5F - var8, 0.5F + var8, 0.6F, 0.5F + var8);
		}

		return super.collisionRayTrace(world, x, y, z, vector1, vector2);
	}
}
