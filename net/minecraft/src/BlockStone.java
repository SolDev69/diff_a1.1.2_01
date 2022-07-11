package net.minecraft.src;

import java.util.Random;

public class BlockStone extends Block {
	public BlockStone(int id, int tex) {
		super(id, tex, Material.rock);
	}

	public int idDropped(int metadata, Random rand) {
		return Block.cobblestone.blockID;
	}
}
