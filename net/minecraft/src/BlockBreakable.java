package net.minecraft.src;

public class BlockBreakable extends Block {
	private boolean localFlag;

	protected BlockBreakable(int id, int blockIndex, Material material, boolean localFlag) {
		super(id, blockIndex, material);
		this.localFlag = localFlag;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		int var6 = blockAccess.getBlockId(x, y, z);
		return !this.localFlag && var6 == this.blockID ? false : super.shouldSideBeRendered(blockAccess, x, y, z, side);
	}
}
