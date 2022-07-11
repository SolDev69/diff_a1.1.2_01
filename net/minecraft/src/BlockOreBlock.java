package net.minecraft.src;

public class BlockOreBlock extends Block {
	public BlockOreBlock(int id, int blockIndex) {
		super(id, Material.iron);
		this.blockIndexInTexture = blockIndex;
	}

	public int getBlockTextureFromSide(int side) {
		return side == 1 ? this.blockIndexInTexture - 16 : (side == 0 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture);
	}
}
