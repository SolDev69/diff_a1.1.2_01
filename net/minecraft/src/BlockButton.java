package net.minecraft.src;

import java.util.Random;

public class BlockButton extends Block {
	protected BlockButton(int id, int tex) {
		super(id, tex, Material.circuits);
		this.setTickOnLoad(true);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		return null;
	}

	public int tickRate() {
		return 20;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x - 1, y, z) ? true : (world.isBlockNormalCube(x + 1, y, z) ? true : (world.isBlockNormalCube(x, y, z - 1) ? true : world.isBlockNormalCube(x, y, z + 1)));
	}

	public void onBlockPlaced(World worldObj, int x, int y, int z, int metadata) {
		int var6 = worldObj.getBlockMetadata(x, y, z);
		int var7 = var6 & 8;
		var6 &= 7;
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

		worldObj.setBlockMetadataWithNotify(x, y, z, var6 + var7);
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
		}

		this.checkIfAttachedToBlock(worldObj, x, y, z);
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		if(this.checkIfAttachedToBlock(worldObj, x, y, z)) {
			int var6 = worldObj.getBlockMetadata(x, y, z) & 7;
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

	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		int var5 = blockAccess.getBlockMetadata(x, y, z);
		int var6 = var5 & 7;
		boolean var7 = (var5 & 8) > 0;
		float var8 = 0.375F;
		float var9 = 0.625F;
		float var10 = 0.1875F;
		float var11 = 0.125F;
		if(var7) {
			var11 = 0.0625F;
		}

		if(var6 == 1) {
			this.setBlockBounds(0.0F, var8, 0.5F - var10, var11, var9, 0.5F + var10);
		} else if(var6 == 2) {
			this.setBlockBounds(1.0F - var11, var8, 0.5F - var10, 1.0F, var9, 0.5F + var10);
		} else if(var6 == 3) {
			this.setBlockBounds(0.5F - var10, var8, 0.0F, 0.5F + var10, var9, var11);
		} else if(var6 == 4) {
			this.setBlockBounds(0.5F - var10, var8, 1.0F - var11, 0.5F + var10, var9, 1.0F);
		}

	}

	public void onBlockClicked(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		this.blockActivated(worldObj, x, y, z, entityPlayer);
	}

	public boolean blockActivated(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		int var6 = worldObj.getBlockMetadata(x, y, z);
		int var7 = var6 & 7;
		int var8 = 8 - (var6 & 8);
		if(var8 == 0) {
			return true;
		} else {
			worldObj.setBlockMetadataWithNotify(x, y, z, var7 + var8);
			worldObj.markBlocksDirty(x, y, z, x, y, z);
			worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
			worldObj.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			if(var7 == 1) {
				worldObj.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			} else if(var7 == 2) {
				worldObj.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			} else if(var7 == 3) {
				worldObj.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			} else if(var7 == 4) {
				worldObj.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
			} else {
				worldObj.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			}

			worldObj.scheduleBlockUpdate(x, y, z, this.blockID);
			return true;
		}
	}

	public void onBlockRemoval(World worldObj, int x, int y, int z) {
		int var5 = worldObj.getBlockMetadata(x, y, z);
		if((var5 & 8) > 0) {
			worldObj.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			int var6 = var5 & 7;
			if(var6 == 1) {
				worldObj.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			} else if(var6 == 2) {
				worldObj.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			} else if(var6 == 3) {
				worldObj.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			} else if(var6 == 4) {
				worldObj.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
			} else {
				worldObj.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			}
		}

		super.onBlockRemoval(worldObj, x, y, z);
	}

	public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int metadata) {
		return (blockAccess.getBlockMetadata(x, y, z) & 8) > 0;
	}

	public boolean isIndirectlyPoweringTo(World worldObj, int x, int y, int z, int side) {
		int var6 = worldObj.getBlockMetadata(x, y, z);
		if((var6 & 8) == 0) {
			return false;
		} else {
			int var7 = var6 & 7;
			return var7 == 5 && side == 1 ? true : (var7 == 4 && side == 2 ? true : (var7 == 3 && side == 3 ? true : (var7 == 2 && side == 4 ? true : var7 == 1 && side == 5)));
		}
	}

	public boolean canProvidePower() {
		return true;
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		int var6 = worldObj.getBlockMetadata(x, y, z);
		if((var6 & 8) != 0) {
			worldObj.setBlockMetadataWithNotify(x, y, z, var6 & 7);
			worldObj.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			int var7 = var6 & 7;
			if(var7 == 1) {
				worldObj.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			} else if(var7 == 2) {
				worldObj.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			} else if(var7 == 3) {
				worldObj.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			} else if(var7 == 4) {
				worldObj.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
			} else {
				worldObj.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			}

			worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
			worldObj.markBlocksDirty(x, y, z, x, y, z);
		}
	}

	public void setBlockBoundsForItemRender() {
		float var1 = 0.1875F;
		float var2 = 0.125F;
		float var3 = 0.125F;
		this.setBlockBounds(0.5F - var1, 0.5F - var2, 0.5F - var3, 0.5F + var1, 0.5F + var2, 0.5F + var3);
	}
}
