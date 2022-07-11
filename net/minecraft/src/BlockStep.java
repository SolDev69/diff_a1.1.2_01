package net.minecraft.src;

import java.util.Random;

public class BlockStep extends Block {
	private boolean blockType;

	public BlockStep(int id, boolean blockType) {
		super(id, 6, Material.rock);
		this.blockType = blockType;
		if(!blockType) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}

		this.setLightOpacity(255);
	}

	public int getBlockTextureFromSide(int side) {
		return side <= 1 ? 6 : 5;
	}

	public boolean isOpaqueCube() {
		return this.blockType;
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		if(this == Block.stairSingle) {
		}
	}

	public void onBlockAdded(World worldObj, int x, int y, int z) {
		if(this != Block.stairSingle) {
			super.onBlockAdded(worldObj, x, y, z);
		}

		int var5 = worldObj.getBlockId(x, y - 1, z);
		if(var5 == stairSingle.blockID) {
			worldObj.setBlockWithNotify(x, y, z, 0);
			worldObj.setBlockWithNotify(x, y - 1, z, Block.stairDouble.blockID);
		}

	}

	public int idDropped(int metadata, Random rand) {
		return Block.stairSingle.blockID;
	}

	public boolean renderAsNormalBlock() {
		return this.blockType;
	}

	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		if(this != Block.stairSingle) {
			super.shouldSideBeRendered(blockAccess, x, y, z, side);
		}

		return side == 1 ? true : (!super.shouldSideBeRendered(blockAccess, x, y, z, side) ? false : (side == 0 ? true : blockAccess.getBlockId(x, y, z) != this.blockID));
	}
}
