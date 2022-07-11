package net.minecraft.src;

import java.util.Random;

public class BlockTorch extends Block {
	protected BlockTorch(int id, int tex) {
		super(id, tex, Material.circuits);
		this.setTickOnLoad(true);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 2;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x - 1, y, z) ? true : (world.isBlockNormalCube(x + 1, y, z) ? true : (world.isBlockNormalCube(x, y, z - 1) ? true : (world.isBlockNormalCube(x, y, z + 1) ? true : world.isBlockNormalCube(x, y - 1, z))));
	}

	public void onBlockPlaced(World worldObj, int x, int y, int z, int metadata) {
		int var6 = worldObj.getBlockMetadata(x, y, z);
		if(metadata == 1 && worldObj.isBlockNormalCube(x, y - 1, z)) {
			var6 = 5;
		}

		if(metadata == 2 && worldObj.isBlockNormalCube(x, y, z + 1)) {
			var6 = 4;
		}

		if(metadata == 3 && worldObj.isBlockNormalCube(x, y, z - 1)) {
			var6 = 3;
		}

		if(metadata == 4 && worldObj.isBlockNormalCube(x + 1, y, z)) {
			var6 = 2;
		}

		if(metadata == 5 && worldObj.isBlockNormalCube(x - 1, y, z)) {
			var6 = 1;
		}

		worldObj.setBlockMetadataWithNotify(x, y, z, var6);
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		super.updateTick(worldObj, x, y, z, rand);
		if(worldObj.getBlockMetadata(x, y, z) == 0) {
			this.onBlockAdded(worldObj, x, y, z);
		}

	}

	public void onBlockAdded(World worldObj, int x, int y, int z) {
		if(worldObj.isBlockNormalCube(x - 1, y, z)) {
			worldObj.setBlockMetadataWithNotify(x, y, z, 1);
		} else if(worldObj.isBlockNormalCube(x + 1, y, z)) {
			worldObj.setBlockMetadataWithNotify(x, y, z, 2);
		} else if(worldObj.isBlockNormalCube(x, y, z - 1)) {
			worldObj.setBlockMetadataWithNotify(x, y, z, 3);
		} else if(worldObj.isBlockNormalCube(x, y, z + 1)) {
			worldObj.setBlockMetadataWithNotify(x, y, z, 4);
		} else if(worldObj.isBlockNormalCube(x, y - 1, z)) {
			worldObj.setBlockMetadataWithNotify(x, y, z, 5);
		}

		this.checkIfAttachedToBlock(worldObj, x, y, z);
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		if(this.checkIfAttachedToBlock(worldObj, x, y, z)) {
			int var6 = worldObj.getBlockMetadata(x, y, z);
			boolean var7 = false;
			if(!worldObj.isBlockNormalCube(x - 1, y, z) && var6 == 1) {
				var7 = true;
			}

			if(!worldObj.isBlockNormalCube(x + 1, y, z) && var6 == 2) {
				var7 = true;
			}

			if(!worldObj.isBlockNormalCube(x, y, z - 1) && var6 == 3) {
				var7 = true;
			}

			if(!worldObj.isBlockNormalCube(x, y, z + 1) && var6 == 4) {
				var7 = true;
			}

			if(!worldObj.isBlockNormalCube(x, y - 1, z) && var6 == 5) {
				var7 = true;
			}

			if(var7) {
				this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
				worldObj.setBlockWithNotify(x, y, z, 0);
			}
		}

	}

	private boolean checkIfAttachedToBlock(World worldObj, int x, int y, int z) {
		if(!this.canPlaceBlockAt(worldObj, x, y, z)) {
			this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
			worldObj.setBlockWithNotify(x, y, z, 0);
			return false;
		} else {
			return true;
		}
	}

	public MovingObjectPosition collisionRayTrace(World worldObj, int x, int y, int z, Vec3D vector1, Vec3D vector2) {
		int var7 = worldObj.getBlockMetadata(x, y, z) & 7;
		float var8 = 0.15F;
		if(var7 == 1) {
			this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
		} else if(var7 == 2) {
			this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
		} else if(var7 == 3) {
			this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
		} else if(var7 == 4) {
			this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
		} else {
			var8 = 0.1F;
			this.setBlockBounds(0.5F - var8, 0.0F, 0.5F - var8, 0.5F + var8, 0.6F, 0.5F + var8);
		}

		return super.collisionRayTrace(worldObj, x, y, z, vector1, vector2);
	}

	public void randomDisplayTick(World worldObj, int x, int y, int z, Random rand) {
		int var6 = worldObj.getBlockMetadata(x, y, z);
		double var7 = (double)((float)x + 0.5F);
		double var9 = (double)((float)y + 0.7F);
		double var11 = (double)((float)z + 0.5F);
		double var13 = (double)0.22F;
		double var15 = (double)0.27F;
		if(var6 == 1) {
			worldObj.spawnParticle("smoke", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
			worldObj.spawnParticle("flame", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
		} else if(var6 == 2) {
			worldObj.spawnParticle("smoke", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
			worldObj.spawnParticle("flame", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
		} else if(var6 == 3) {
			worldObj.spawnParticle("smoke", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
			worldObj.spawnParticle("flame", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
		} else if(var6 == 4) {
			worldObj.spawnParticle("smoke", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
			worldObj.spawnParticle("flame", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
		} else {
			worldObj.spawnParticle("smoke", var7, var9, var11, 0.0D, 0.0D, 0.0D);
			worldObj.spawnParticle("flame", var7, var9, var11, 0.0D, 0.0D, 0.0D);
		}

	}
}
