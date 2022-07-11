package net.minecraft.src;

public class BlockLeavesBase extends Block {
	protected boolean graphicsLevel;

	protected BlockLeavesBase(int id, int tex, Material material, boolean graphicsLevel) {
		super(id, tex, material);
		this.graphicsLevel = graphicsLevel;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		int var6 = blockAccess.getBlockId(x, y, z);
		return !this.graphicsLevel && var6 == this.blockID ? false : super.shouldSideBeRendered(blockAccess, x, y, z, side);
	}
}
