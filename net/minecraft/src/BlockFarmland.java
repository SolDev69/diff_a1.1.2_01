package net.minecraft.src;

import java.util.Random;

public class BlockFarmland extends Block {
	protected BlockFarmland(int id) {
		super(id, Material.grass);
		this.blockIndexInTexture = 87;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
		this.setLightOpacity(255);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return AxisAlignedBB.getBoundingBoxFromPool((double)(x + 0), (double)(y + 0), (double)(z + 0), (double)(x + 1), (double)(y + 1), (double)(z + 1));
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		if(random.nextInt(5) == 0) {
			if(this.isWaterNearby(world, x, y, z)) {
				world.setBlockMetadataWithNotify(x, y, z, 7);
			} else {
				int var6 = world.getBlockMetadata(x, y, z);
				if(var6 > 0) {
					world.setBlockMetadataWithNotify(x, y, z, var6 - 1);
				} else if(!this.isCropsNearby(world, x, y, z)) {
					world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
				}
			}
		}

	}

	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		if(world.rand.nextInt(4) == 0) {
			world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
		}

	}

	private boolean isCropsNearby(World world, int x, int y, int z) {
		byte var5 = 0;

		for(int var6 = x - var5; var6 <= x + var5; ++var6) {
			for(int var7 = z - var5; var7 <= z + var5; ++var7) {
				if(world.getBlockId(var6, y + 1, var7) == Block.crops.blockID) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isWaterNearby(World world, int x, int y, int z) {
		for(int var5 = x - 4; var5 <= x + 4; ++var5) {
			for(int var6 = y; var6 <= y + 1; ++var6) {
				for(int var7 = z - 4; var7 <= z + 4; ++var7) {
					if(world.getBlockMaterial(var5, var6, var7) == Material.water) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		super.onNeighborBlockChange(world, x, y, z, flag);
		Material var6 = world.getBlockMaterial(x, y + 1, z);
		if(var6.isSolid()) {
			world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
		}

	}

	public int idDropped(int count, Random random) {
		return Block.dirt.idDropped(0, random);
	}
}
