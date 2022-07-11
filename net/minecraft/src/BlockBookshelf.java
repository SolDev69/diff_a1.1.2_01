package net.minecraft.src;

import java.util.Random;

public class BlockBookshelf extends Block {
	public BlockBookshelf(int id, int blockIndex) {
		super(id, blockIndex, Material.wood);
	}

	public int getBlockTextureFromSide(int side) {
		return side <= 1 ? 4 : this.blockIndexInTexture;
	}

	public int quantityDropped(Random random) {
		return 0;
	}
}
