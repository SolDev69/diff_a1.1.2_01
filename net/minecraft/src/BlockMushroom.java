package net.minecraft.src;

public class BlockMushroom extends BlockFlower {
	protected BlockMushroom(int var1, int var2) {
		super(var1, var2);
		float var3 = 0.2F;
		this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 2.0F, 0.5F + var3);
	}

	protected boolean canThisPlantGrowOnThisBlockID(int id) {
		return Block.opaqueCubeLookup[id];
	}

	public boolean canBlockStay(World world, int x, int y, int z) {
		return world.getBlockLightValue(x, y, z) <= 13 && this.canThisPlantGrowOnThisBlockID(world.getBlockId(x, y - 1, z));
	}
}
