package net.minecraft.src;

import java.util.Random;

public class BlockSnow extends Block {
	protected BlockSnow(int id, int blockIndex) {
		super(id, blockIndex, Material.snow);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setTickOnLoad(true);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		int var5 = world.getBlockId(x, y - 1, z);
		return var5 != 0 && Block.blocksList[var5].isOpaqueCube() ? world.getBlockMaterial(x, y - 1, z).getIsSolid() : false;
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		this.canSnowStay(world, x, y, z);
	}

	private boolean canSnowStay(World world, int x, int y, int z) {
		if(!this.canPlaceBlockAt(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
			return false;
		} else {
			return true;
		}
	}

	public int idDropped(int count, Random random) {
		return Item.snowball.shiftedIndex;
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		if(world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 11) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
		}

	}

	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		Material var6 = blockAccess.getBlockMaterial(x, y, z);
		return side == 1 ? true : (var6 == this.material ? false : super.shouldSideBeRendered(blockAccess, x, y, z, side));
	}
}
