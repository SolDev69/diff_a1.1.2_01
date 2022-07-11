package net.minecraft.src;

import java.util.Random;

public class BlockObsidian extends BlockStone {
	public BlockObsidian(int var1, int var2) {
		super(var1, var2);
	}

	public int quantityDropped(Random random) {
		return 1;
	}

	public int idDropped(int count, Random random) {
		return Block.obsidian.blockID;
	}
}
