package net.minecraft.src;

import java.util.Random;

public class BlockIce extends BlockBreakable {
	public BlockIce(int id, int blockIndex) {
		super(id, blockIndex, Material.ice, false);
		this.slipperiness = 0.98F;
		this.setTickOnLoad(true);
	}

	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		return super.shouldSideBeRendered(blockAccess, x, y, z, 1 - side);
	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		Material var5 = world.getBlockMaterial(x, y - 1, z);
		if(var5.getIsSolid() || var5.getIsLiquid()) {
			world.setBlockWithNotify(x, y, z, Block.waterMoving.blockID);
		}

	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		if(world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 11 - Block.lightOpacity[this.blockID]) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, Block.waterStill.blockID);
		}

	}
}
