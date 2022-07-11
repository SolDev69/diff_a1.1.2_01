package net.minecraft.src;

import java.util.Random;

public class BlockReed extends Block {
	protected BlockReed(int id, int tex) {
		super(id, Material.plants);
		this.blockIndexInTexture = tex;
		float var3 = 0.375F;
		this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 1.0F, 0.5F + var3);
		this.setTickOnLoad(true);
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		if(worldObj.getBlockId(x, y + 1, z) == 0) {
			int var6;
			for(var6 = 1; worldObj.getBlockId(x, y - var6, z) == this.blockID; ++var6) {
			}

			if(var6 < 3) {
				int var7 = worldObj.getBlockMetadata(x, y, z);
				if(var7 == 15) {
					worldObj.setBlockWithNotify(x, y + 1, z, this.blockID);
					worldObj.setBlockMetadataWithNotify(x, y, z, 0);
				} else {
					worldObj.setBlockMetadataWithNotify(x, y, z, var7 + 1);
				}
			}
		}

	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		int var5 = world.getBlockId(x, y - 1, z);
		return var5 == this.blockID ? true : (var5 != Block.grass.blockID && var5 != Block.dirt.blockID ? false : (world.getBlockMaterial(x - 1, y - 1, z) == Material.water ? true : (world.getBlockMaterial(x + 1, y - 1, z) == Material.water ? true : (world.getBlockMaterial(x, y - 1, z - 1) == Material.water ? true : world.getBlockMaterial(x, y - 1, z + 1) == Material.water))));
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		this.checkBlockCoordValid(worldObj, x, y, z);
	}

	protected final void checkBlockCoordValid(World worldObj, int x, int y, int z) {
		if(!this.canBlockStay(worldObj, x, y, z)) {
			this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
			worldObj.setBlockWithNotify(x, y, z, 0);
		}

	}

	public boolean canBlockStay(World world, int x, int y, int z) {
		return this.canPlaceBlockAt(world, x, y, z);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		return null;
	}

	public int idDropped(int metadata, Random rand) {
		return Item.reed.shiftedIndex;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 1;
	}
}
