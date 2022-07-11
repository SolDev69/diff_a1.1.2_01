package net.minecraft.src;

public class ItemPickaxe extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{Block.cobblestone, Block.stairDouble, Block.stairSingle, Block.stone, Block.cobblestoneMossy, Block.oreIron, Block.blockSteel, Block.oreCoal, Block.blockGold, Block.oreGold, Block.oreDiamond, Block.blockDiamond, Block.ice};
	private int harvestLevel;

	public ItemPickaxe(int id, int harvestLevel) {
		super(id, 2, harvestLevel, blocksEffectiveAgainst);
		this.harvestLevel = harvestLevel;
	}

	public boolean canHarvestBlock(Block block) {
		return block == Block.obsidian ? this.harvestLevel == 3 : (block != Block.blockDiamond && block != Block.oreDiamond ? (block != Block.blockGold && block != Block.oreGold ? (block != Block.blockSteel && block != Block.oreIron ? (block != Block.oreRedstone && block != Block.oreRedstoneGlowing ? (block.material == Material.rock ? true : block.material == Material.iron) : this.harvestLevel >= 2) : this.harvestLevel >= 1) : this.harvestLevel >= 2) : this.harvestLevel >= 2);
	}
}
