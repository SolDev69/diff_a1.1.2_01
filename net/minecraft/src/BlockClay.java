package net.minecraft.src;

import java.util.Random;

public class BlockClay extends Block {
	public BlockClay(int id, int blockIndex) {
		super(id, blockIndex, Material.clay);
	}

	public int idDropped(int count, Random random) {
		return Item.clay.shiftedIndex;
	}

	public int quantityDropped(Random random) {
		return 4;
	}
}
