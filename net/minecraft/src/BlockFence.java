package net.minecraft.src;

import java.util.ArrayList;

public class BlockFence extends Block {
	public BlockFence(int blockID, int tex) {
		super(blockID, tex, Material.wood);
	}

	public void getCollidingBoundingBoxes(World worldObj, int x, int y, int z, AxisAlignedBB aabb, ArrayList collidingBoundingBoxes) {
		collidingBoundingBoxes.add(AxisAlignedBB.getBoundingBoxFromPool((double)x, (double)y, (double)z, (double)(x + 1), (double)y + 1.5D, (double)(z + 1)));
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.getBlockId(x, y - 1, z) == this.blockID ? false : (!world.getBlockMaterial(x, y - 1, z).isSolid() ? false : super.canPlaceBlockAt(world, x, y, z));
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 11;
	}
}
