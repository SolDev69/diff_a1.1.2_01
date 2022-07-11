package net.minecraft.src;

import java.util.Random;

public class BlockOre extends Block {
	public BlockOre(int id, int blockIndex) {
		super(id, blockIndex, Material.rock);
	}

	public int idDropped(int count, Random random) {
		return this.blockID == Block.oreCoal.blockID ? Item.coal.shiftedIndex : (this.blockID == Block.oreDiamond.blockID ? Item.diamond.shiftedIndex : this.blockID);
	}

	public int quantityDropped(Random random) {
		return 1;
	}
}
