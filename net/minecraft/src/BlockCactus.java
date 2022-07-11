package net.minecraft.src;

import java.util.Random;

public class BlockCactus extends Block {
	protected BlockCactus(int id, int blockIndex) {
		super(id, blockIndex, Material.cactus);
		this.setTickOnLoad(true);
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		if(world.getBlockId(x, y + 1, z) == 0) {
			int var6;
			for(var6 = 1; world.getBlockId(x, y - var6, z) == this.blockID; ++var6) {
			}

			if(var6 < 3) {
				int var7 = world.getBlockMetadata(x, y, z);
				if(var7 == 15) {
					world.setBlockWithNotify(x, y + 1, z, this.blockID);
					world.setBlockMetadataWithNotify(x, y, z, 0);
				} else {
					world.setBlockMetadataWithNotify(x, y, z, var7 + 1);
				}
			}
		}

	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		float var5 = 0.0625F;
		return AxisAlignedBB.getBoundingBoxFromPool((double)((float)x + var5), (double)y, (double)((float)z + var5), (double)((float)(x + 1) - var5), (double)((float)(y + 1) - var5), (double)((float)(z + 1) - var5));
	}

	public int getBlockTextureFromSide(int side) {
		return side == 1 ? this.blockIndexInTexture - 1 : (side == 0 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 13;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return !super.canPlaceBlockAt(world, x, y, z) ? false : this.canBlockStay(world, x, y, z);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		if(!this.canBlockStay(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
		}

	}

	public boolean canBlockStay(World world, int x, int y, int z) {
		if(world.getBlockMaterial(x - 1, y, z).isSolid()) {
			return false;
		} else if(world.getBlockMaterial(x + 1, y, z).isSolid()) {
			return false;
		} else if(world.getBlockMaterial(x, y, z - 1).isSolid()) {
			return false;
		} else if(world.getBlockMaterial(x, y, z + 1).isSolid()) {
			return false;
		} else {
			int var5 = world.getBlockId(x, y - 1, z);
			return var5 == Block.cactus.blockID || var5 == Block.sand.blockID;
		}
	}

	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		entity.attackEntityFrom((Entity)null, 1);
	}
}
