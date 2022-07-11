package net.minecraft.src;

public class ItemSpade extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{Block.grass, Block.dirt, Block.sand, Block.gravel, Block.snow, Block.blockSnow, Block.blockClay};

	public ItemSpade(int itemID, int material) {
		super(itemID, 1, material, blocksEffectiveAgainst);
	}

	public boolean canHarvestBlock(Block block) {
		return block == Block.snow ? true : block == Block.blockSnow;
	}
}
