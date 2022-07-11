package net.minecraft.src;

import java.util.Random;

public class BlockFarmland extends Block {
	protected BlockFarmland(int blockID) {
		super(blockID, Material.grass);
		this.blockIndexInTexture = 87;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
		this.setLightOpacity(255);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		return AxisAlignedBB.getBoundingBoxFromPool((double)(x + 0), (double)(y + 0), (double)(z + 0), (double)(x + 1), (double)(y + 1), (double)(z + 1));
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		return side == 1 && metadata > 0 ? this.blockIndexInTexture - 1 : (side == 1 ? this.blockIndexInTexture : 2);
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		if(rand.nextInt(5) == 0) {
			if(this.isWaterNearby(worldObj, x, y, z)) {
				worldObj.setBlockMetadataWithNotify(x, y, z, 7);
			} else {
				int var6 = worldObj.getBlockMetadata(x, y, z);
				if(var6 > 0) {
					worldObj.setBlockMetadataWithNotify(x, y, z, var6 - 1);
				} else if(!this.isCropsNearby(worldObj, x, y, z)) {
					worldObj.setBlockWithNotify(x, y, z, Block.dirt.blockID);
				}
			}
		}

	}

	public void onEntityWalking(World worldObj, int x, int y, int z, Entity entity) {
		if(worldObj.rand.nextInt(4) == 0) {
			worldObj.setBlockWithNotify(x, y, z, Block.dirt.blockID);
		}

	}

	private boolean isCropsNearby(World worldObj, int x, int y, int z) {
		byte var5 = 0;

		for(int var6 = x - var5; var6 <= x + var5; ++var6) {
			for(int var7 = z - var5; var7 <= z + var5; ++var7) {
				if(worldObj.getBlockId(var6, y + 1, var7) == Block.crops.blockID) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isWaterNearby(World worldObj, int x, int y, int z) {
		for(int var5 = x - 4; var5 <= x + 4; ++var5) {
			for(int var6 = y; var6 <= y + 1; ++var6) {
				for(int var7 = z - 4; var7 <= z + 4; ++var7) {
					if(worldObj.getBlockMaterial(var5, var6, var7) == Material.water) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		super.onNeighborBlockChange(worldObj, x, y, z, id);
		Material var6 = worldObj.getBlockMaterial(x, y + 1, z);
		if(var6.isSolid()) {
			worldObj.setBlockWithNotify(x, y, z, Block.dirt.blockID);
		}

	}

	public int idDropped(int metadata, Random rand) {
		return Block.dirt.idDropped(0, rand);
	}
}
