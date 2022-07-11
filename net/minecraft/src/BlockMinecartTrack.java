package net.minecraft.src;

import java.util.Random;

public class BlockMinecartTrack extends Block {
	protected BlockMinecartTrack(int id, int blockIndex) {
		super(id, blockIndex, Material.circuits);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3D vector1, Vec3D vector2) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.collisionRayTrace(world, x, y, z, vector1, vector2);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		int var5 = blockAccess.getBlockMetadata(x, y, z);
		if(var5 >= 2 && var5 <= 5) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
		} else {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		}

	}

	public int getRenderType() {
		return 9;
	}

	public int quantityDropped(Random random) {
		return 1;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x, y - 1, z);
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		world.setBlockMetadataWithNotify(x, y, z, 15);
		this.refreshTrackShape(world, x, y, z);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		int var6 = world.getBlockMetadata(x, y, z);
		boolean var7 = false;
		if(!world.isBlockNormalCube(x, y - 1, z)) {
			var7 = true;
		}

		if(var6 == 2 && !world.isBlockNormalCube(x + 1, y, z)) {
			var7 = true;
		}

		if(var6 == 3 && !world.isBlockNormalCube(x - 1, y, z)) {
			var7 = true;
		}

		if(var6 == 4 && !world.isBlockNormalCube(x, y, z - 1)) {
			var7 = true;
		}

		if(var6 == 5 && !world.isBlockNormalCube(x, y, z + 1)) {
			var7 = true;
		}

		if(var7) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
		} else if(flag > 0 && Block.blocksList[flag].canProvidePower() && MinecartTrackLogic.getNAdjacentTracks(new MinecartTrackLogic(this, world, x, y, z)) == 3) {
			this.refreshTrackShape(world, x, y, z);
		}

	}

	private void refreshTrackShape(World world, int x, int y, int z) {
		(new MinecartTrackLogic(this, world, x, y, z)).place(world.isBlockIndirectlyGettingPowered(x, y, z));
	}
}
