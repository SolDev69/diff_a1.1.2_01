package net.minecraft.src;

public class BlockLever extends Block {
	protected BlockLever(int id, int blockIndex) {
		super(id, blockIndex, Material.circuits);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 12;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x - 1, y, z) ? true : (world.isBlockNormalCube(x + 1, y, z) ? true : (world.isBlockNormalCube(x, y, z - 1) ? true : (world.isBlockNormalCube(x, y, z + 1) ? true : world.isBlockNormalCube(x, y - 1, z))));
	}

	public void onBlockPlaced(World world, int x, int y, int z, int notifyFlag) {
		int var6 = world.getBlockMetadata(x, y, z);
		int var7 = var6 & 8;
		var6 &= 7;
		if(notifyFlag == 1 && world.isBlockNormalCube(x, y - 1, z)) {
			var6 = 5 + world.rand.nextInt(2);
		}

		if(notifyFlag == 2 && world.isBlockNormalCube(x, y, z + 1)) {
			var6 = 4;
		}

		if(notifyFlag == 3 && world.isBlockNormalCube(x, y, z - 1)) {
			var6 = 3;
		}

		if(notifyFlag == 4 && world.isBlockNormalCube(x + 1, y, z)) {
			var6 = 2;
		}

		if(notifyFlag == 5 && world.isBlockNormalCube(x - 1, y, z)) {
			var6 = 1;
		}

		world.setBlockMetadataWithNotify(x, y, z, var6 + var7);
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		if(world.isBlockNormalCube(x - 1, y, z)) {
			world.setBlockMetadataWithNotify(x, y, z, 1);
		} else if(world.isBlockNormalCube(x + 1, y, z)) {
			world.setBlockMetadataWithNotify(x, y, z, 2);
		} else if(world.isBlockNormalCube(x, y, z - 1)) {
			world.setBlockMetadataWithNotify(x, y, z, 3);
		} else if(world.isBlockNormalCube(x, y, z + 1)) {
			world.setBlockMetadataWithNotify(x, y, z, 4);
		} else if(world.isBlockNormalCube(x, y - 1, z)) {
			world.setBlockMetadataWithNotify(x, y, z, 5 + world.rand.nextInt(2));
		}

		this.checkIfAttachedToBlock(world, x, y, z);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		if(this.checkIfAttachedToBlock(world, x, y, z)) {
			int var6 = world.getBlockMetadata(x, y, z) & 7;
			boolean var7 = false;
			if(!world.isBlockNormalCube(x - 1, y, z) && var6 == 1) {
				var7 = true;
			}

			if(!world.isBlockNormalCube(x + 1, y, z) && var6 == 2) {
				var7 = true;
			}

			if(!world.isBlockNormalCube(x, y, z - 1) && var6 == 3) {
				var7 = true;
			}

			if(!world.isBlockNormalCube(x, y, z + 1) && var6 == 4) {
				var7 = true;
			}

			if(!world.isBlockNormalCube(x, y - 1, z) && var6 == 5) {
				var7 = true;
			}

			if(var7) {
				this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
				world.setBlockWithNotify(x, y, z, 0);
			}
		}

	}

	private boolean checkIfAttachedToBlock(World world, int x, int y, int z) {
		if(!this.canPlaceBlockAt(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
			return false;
		} else {
			return true;
		}
	}

	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		int var5 = blockAccess.getBlockMetadata(x, y, z) & 7;
		float var6 = 0.1875F;
		if(var5 == 1) {
			this.setBlockBounds(0.0F, 0.2F, 0.5F - var6, var6 * 2.0F, 0.8F, 0.5F + var6);
		} else if(var5 == 2) {
			this.setBlockBounds(1.0F - var6 * 2.0F, 0.2F, 0.5F - var6, 1.0F, 0.8F, 0.5F + var6);
		} else if(var5 == 3) {
			this.setBlockBounds(0.5F - var6, 0.2F, 0.0F, 0.5F + var6, 0.8F, var6 * 2.0F);
		} else if(var5 == 4) {
			this.setBlockBounds(0.5F - var6, 0.2F, 1.0F - var6 * 2.0F, 0.5F + var6, 0.8F, 1.0F);
		} else {
			var6 = 0.25F;
			this.setBlockBounds(0.5F - var6, 0.0F, 0.5F - var6, 0.5F + var6, 0.6F, 0.5F + var6);
		}

	}

	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		this.blockActivated(world, x, y, z, entityPlayer);
	}

	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		int var6 = world.getBlockMetadata(x, y, z);
		int var7 = var6 & 7;
		int var8 = 8 - (var6 & 8);
		world.setBlockMetadataWithNotify(x, y, z, var7 + var8);
		world.markBlocksDirty(x, y, z, x, y, z);
		world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, var8 > 0 ? 0.6F : 0.5F);
		world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
		if(var7 == 1) {
			world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
		} else if(var7 == 2) {
			world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
		} else if(var7 == 3) {
			world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
		} else if(var7 == 4) {
			world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
		} else {
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
		}

		return true;
	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		int var5 = world.getBlockMetadata(x, y, z);
		if((var5 & 8) > 0) {
			world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			int var6 = var5 & 7;
			if(var6 == 1) {
				world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			} else if(var6 == 2) {
				world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			} else if(var6 == 3) {
				world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			} else if(var6 == 4) {
				world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
			} else {
				world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			}
		}

		super.onBlockRemoval(world, x, y, z);
	}

	public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int unused) {
		return (blockAccess.getBlockMetadata(x, y, z) & 8) > 0;
	}

	public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int flag) {
		int var6 = world.getBlockMetadata(x, y, z);
		if((var6 & 8) == 0) {
			return false;
		} else {
			int var7 = var6 & 7;
			return var7 == 5 && flag == 1 ? true : (var7 == 4 && flag == 2 ? true : (var7 == 3 && flag == 3 ? true : (var7 == 2 && flag == 4 ? true : var7 == 1 && flag == 5)));
		}
	}

	public boolean canProvidePower() {
		return true;
	}
}
