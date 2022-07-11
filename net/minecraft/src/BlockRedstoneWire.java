package net.minecraft.src;

import java.util.Random;

public class BlockRedstoneWire extends Block {
	private boolean wiresProvidePower = true;

	public BlockRedstoneWire(int id, int blockIndex) {
		super(id, blockIndex, Material.circuits);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 5;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x, y - 1, z);
	}

	private void updateAndPropagateCurrentStrength(World world, int x, int y, int z) {
		int var5 = world.getBlockMetadata(x, y, z);
		int var6 = 0;
		this.wiresProvidePower = false;
		boolean var7 = world.isBlockIndirectlyGettingPowered(x, y, z);
		this.wiresProvidePower = true;
		int var8;
		int var9;
		int var10;
		if(var7) {
			var6 = 15;
		} else {
			for(var8 = 0; var8 < 4; ++var8) {
				var9 = x;
				var10 = z;
				if(var8 == 0) {
					var9 = x - 1;
				}

				if(var8 == 1) {
					++var9;
				}

				if(var8 == 2) {
					var10 = z - 1;
				}

				if(var8 == 3) {
					++var10;
				}

				var6 = this.getMaxCurrentStrength(world, var9, y, var10, var6);
				if(world.isBlockNormalCube(var9, y, var10) && !world.isBlockNormalCube(x, y + 1, z)) {
					var6 = this.getMaxCurrentStrength(world, var9, y + 1, var10, var6);
				} else if(!world.isBlockNormalCube(var9, y, var10)) {
					var6 = this.getMaxCurrentStrength(world, var9, y - 1, var10, var6);
				}
			}

			if(var6 > 0) {
				--var6;
			} else {
				var6 = 0;
			}
		}

		if(var5 != var6) {
			world.setBlockMetadataWithNotify(x, y, z, var6);
			world.markBlocksDirty(x, y, z, x, y, z);
			if(var6 > 0) {
				--var6;
			}

			for(var8 = 0; var8 < 4; ++var8) {
				var9 = x;
				var10 = z;
				int var11 = y - 1;
				if(var8 == 0) {
					var9 = x - 1;
				}

				if(var8 == 1) {
					++var9;
				}

				if(var8 == 2) {
					var10 = z - 1;
				}

				if(var8 == 3) {
					++var10;
				}

				if(world.isBlockNormalCube(var9, y, var10)) {
					var11 += 2;
				}

				int var12 = this.getMaxCurrentStrength(world, var9, y, var10, -1);
				if(var12 >= 0 && var12 != var6) {
					this.updateAndPropagateCurrentStrength(world, var9, y, var10);
				}

				var12 = this.getMaxCurrentStrength(world, var9, var11, var10, -1);
				if(var12 >= 0 && var12 != var6) {
					this.updateAndPropagateCurrentStrength(world, var9, var11, var10);
				}
			}

			if(var5 == 0 || var6 == 0) {
				world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
				world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
				world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
				world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
				world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
				world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
				world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
			}
		}

	}

	private void notifyWireNeighborsOfNeighborChange(World world, int x, int y, int z) {
		if(world.getBlockId(x, y, z) == this.blockID) {
			world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
		}
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.updateAndPropagateCurrentStrength(world, x, y, z);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
		world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
		this.notifyWireNeighborsOfNeighborChange(world, x - 1, y, z);
		this.notifyWireNeighborsOfNeighborChange(world, x + 1, y, z);
		this.notifyWireNeighborsOfNeighborChange(world, x, y, z - 1);
		this.notifyWireNeighborsOfNeighborChange(world, x, y, z + 1);
		if(world.isBlockNormalCube(x - 1, y, z)) {
			this.notifyWireNeighborsOfNeighborChange(world, x - 1, y + 1, z);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x - 1, y - 1, z);
		}

		if(world.isBlockNormalCube(x + 1, y, z)) {
			this.notifyWireNeighborsOfNeighborChange(world, x + 1, y + 1, z);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x + 1, y - 1, z);
		}

		if(world.isBlockNormalCube(x, y, z - 1)) {
			this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z - 1);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z - 1);
		}

		if(world.isBlockNormalCube(x, y, z + 1)) {
			this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z + 1);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z + 1);
		}

	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
		world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
		this.updateAndPropagateCurrentStrength(world, x, y, z);
		this.notifyWireNeighborsOfNeighborChange(world, x - 1, y, z);
		this.notifyWireNeighborsOfNeighborChange(world, x + 1, y, z);
		this.notifyWireNeighborsOfNeighborChange(world, x, y, z - 1);
		this.notifyWireNeighborsOfNeighborChange(world, x, y, z + 1);
		if(world.isBlockNormalCube(x - 1, y, z)) {
			this.notifyWireNeighborsOfNeighborChange(world, x - 1, y + 1, z);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x - 1, y - 1, z);
		}

		if(world.isBlockNormalCube(x + 1, y, z)) {
			this.notifyWireNeighborsOfNeighborChange(world, x + 1, y + 1, z);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x + 1, y - 1, z);
		}

		if(world.isBlockNormalCube(x, y, z - 1)) {
			this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z - 1);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z - 1);
		}

		if(world.isBlockNormalCube(x, y, z + 1)) {
			this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z + 1);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z + 1);
		}

	}

	private int getMaxCurrentStrength(World world, int x, int y, int z, int var5) {
		if(world.getBlockId(x, y, z) != this.blockID) {
			return var5;
		} else {
			int var6 = world.getBlockMetadata(x, y, z);
			return var6 > var5 ? var6 : var5;
		}
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		int var6 = world.getBlockMetadata(x, y, z);
		boolean var7 = this.canPlaceBlockAt(world, x, y, z);
		if(!var7) {
			this.dropBlockAsItem(world, x, y, z, var6);
			world.setBlockWithNotify(x, y, z, 0);
		} else {
			this.updateAndPropagateCurrentStrength(world, x, y, z);
		}

		super.onNeighborBlockChange(world, x, y, z, flag);
	}

	public int idDropped(int count, Random random) {
		return Item.redstone.shiftedIndex;
	}

	public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int flag) {
		return !this.wiresProvidePower ? false : this.isPoweringTo(world, x, y, z, flag);
	}

	public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int unused) {
		if(!this.wiresProvidePower) {
			return false;
		} else if(blockAccess.getBlockMetadata(x, y, z) == 0) {
			return false;
		} else if(unused == 1) {
			return true;
		} else {
			boolean var6 = isPowerProviderOrWire(blockAccess, x - 1, y, z) || !blockAccess.isBlockNormalCube(x - 1, y, z) && isPowerProviderOrWire(blockAccess, x - 1, y - 1, z);
			boolean var7 = isPowerProviderOrWire(blockAccess, x + 1, y, z) || !blockAccess.isBlockNormalCube(x + 1, y, z) && isPowerProviderOrWire(blockAccess, x + 1, y - 1, z);
			boolean var8 = isPowerProviderOrWire(blockAccess, x, y, z - 1) || !blockAccess.isBlockNormalCube(x, y, z - 1) && isPowerProviderOrWire(blockAccess, x, y - 1, z - 1);
			boolean var9 = isPowerProviderOrWire(blockAccess, x, y, z + 1) || !blockAccess.isBlockNormalCube(x, y, z + 1) && isPowerProviderOrWire(blockAccess, x, y - 1, z + 1);
			if(!blockAccess.isBlockNormalCube(x, y + 1, z)) {
				if(blockAccess.isBlockNormalCube(x - 1, y, z) && isPowerProviderOrWire(blockAccess, x - 1, y + 1, z)) {
					var6 = true;
				}

				if(blockAccess.isBlockNormalCube(x + 1, y, z) && isPowerProviderOrWire(blockAccess, x + 1, y + 1, z)) {
					var7 = true;
				}

				if(blockAccess.isBlockNormalCube(x, y, z - 1) && isPowerProviderOrWire(blockAccess, x, y + 1, z - 1)) {
					var8 = true;
				}

				if(blockAccess.isBlockNormalCube(x, y, z + 1) && isPowerProviderOrWire(blockAccess, x, y + 1, z + 1)) {
					var9 = true;
				}
			}

			return !var8 && !var7 && !var6 && !var9 && unused >= 2 && unused <= 5 ? true : (unused == 2 && var8 && !var6 && !var7 ? true : (unused == 3 && var9 && !var6 && !var7 ? true : (unused == 4 && var6 && !var8 && !var9 ? true : unused == 5 && var7 && !var8 && !var9)));
		}
	}

	public boolean canProvidePower() {
		return this.wiresProvidePower;
	}

	public static boolean isPowerProviderOrWire(IBlockAccess blockAccess, int x, int y, int z) {
		int var4 = blockAccess.getBlockId(x, y, z);
		return var4 == Block.redstoneWire.blockID ? true : (var4 == 0 ? false : Block.blocksList[var4].canProvidePower());
	}
}
