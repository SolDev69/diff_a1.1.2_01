package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockRedstoneTorch extends BlockTorch {
	private boolean torchActive = false;
	private static List torchUpdates = new ArrayList();

	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		return side == 1 ? Block.redstoneWire.getBlockTextureFromSideAndMetadata(side, metadata) : super.getBlockTextureFromSideAndMetadata(side, metadata);
	}

	private boolean checkForBurnout(World worldObj, int x, int y, int z, boolean var5) {
		if(var5) {
			torchUpdates.add(new RedstoneUpdateInfo(x, y, z, worldObj.worldTime));
		}

		int var6 = 0;

		for(int var7 = 0; var7 < torchUpdates.size(); ++var7) {
			RedstoneUpdateInfo var8 = (RedstoneUpdateInfo)torchUpdates.get(var7);
			if(var8.x == x && var8.y == y && var8.z == z) {
				++var6;
				if(var6 >= 8) {
					return true;
				}
			}
		}

		return false;
	}

	protected BlockRedstoneTorch(int id, int tex, boolean torchActive) {
		super(id, tex);
		this.torchActive = torchActive;
		this.setTickOnLoad(true);
	}

	public int tickRate() {
		return 2;
	}

	public void onBlockAdded(World worldObj, int x, int y, int z) {
		if(worldObj.getBlockMetadata(x, y, z) == 0) {
			super.onBlockAdded(worldObj, x, y, z);
		}

		if(this.torchActive) {
			worldObj.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
		}

	}

	public void onBlockRemoval(World worldObj, int x, int y, int z) {
		if(this.torchActive) {
			worldObj.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
		}

	}

	public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int metadata) {
		if(!this.torchActive) {
			return false;
		} else {
			int var6 = blockAccess.getBlockMetadata(x, y, z);
			return var6 == 5 && metadata == 1 ? false : (var6 == 3 && metadata == 3 ? false : (var6 == 4 && metadata == 2 ? false : (var6 == 1 && metadata == 5 ? false : var6 != 2 || metadata != 4)));
		}
	}

	private boolean isIndirectlyPowered(World worldObj, int x, int y, int z) {
		int var5 = worldObj.getBlockMetadata(x, y, z);
		return var5 == 5 && worldObj.isBlockIndirectlyProvidingPowerTo(x, y - 1, z, 0) ? true : (var5 == 3 && worldObj.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2) ? true : (var5 == 4 && worldObj.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3) ? true : (var5 == 1 && worldObj.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4) ? true : var5 == 2 && worldObj.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5))));
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		boolean var6 = this.isIndirectlyPowered(worldObj, x, y, z);

		while(torchUpdates.size() > 0 && worldObj.worldTime - ((RedstoneUpdateInfo)torchUpdates.get(0)).updateTime > 100L) {
			torchUpdates.remove(0);
		}

		if(this.torchActive) {
			if(var6) {
				worldObj.setBlockAndMetadataWithNotify(x, y, z, Block.torchRedstoneIdle.blockID, worldObj.getBlockMetadata(x, y, z));
				if(this.checkForBurnout(worldObj, x, y, z, true)) {
					worldObj.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 2.6F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.8F);

					for(int var7 = 0; var7 < 5; ++var7) {
						double var8 = (double)x + rand.nextDouble() * 0.6D + 0.2D;
						double var10 = (double)y + rand.nextDouble() * 0.6D + 0.2D;
						double var12 = (double)z + rand.nextDouble() * 0.6D + 0.2D;
						worldObj.spawnParticle("smoke", var8, var10, var12, 0.0D, 0.0D, 0.0D);
					}
				}
			}
		} else if(!var6 && !this.checkForBurnout(worldObj, x, y, z, false)) {
			worldObj.setBlockAndMetadataWithNotify(x, y, z, Block.torchRedstoneActive.blockID, worldObj.getBlockMetadata(x, y, z));
		}

	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		super.onNeighborBlockChange(worldObj, x, y, z, id);
		worldObj.scheduleBlockUpdate(x, y, z, this.blockID);
	}

	public boolean isIndirectlyPoweringTo(World worldObj, int x, int y, int z, int side) {
		return side == 0 ? this.isPoweringTo(worldObj, x, y, z, side) : false;
	}

	public int idDropped(int metadata, Random rand) {
		return Block.torchRedstoneActive.blockID;
	}

	public boolean canProvidePower() {
		return true;
	}

	public void randomDisplayTick(World worldObj, int x, int y, int z, Random rand) {
		if(this.torchActive) {
			int var6 = worldObj.getBlockMetadata(x, y, z);
			double var7 = (double)((float)x + 0.5F) + (double)(rand.nextFloat() - 0.5F) * 0.2D;
			double var9 = (double)((float)y + 0.7F) + (double)(rand.nextFloat() - 0.5F) * 0.2D;
			double var11 = (double)((float)z + 0.5F) + (double)(rand.nextFloat() - 0.5F) * 0.2D;
			double var13 = (double)0.22F;
			double var15 = (double)0.27F;
			if(var6 == 1) {
				worldObj.spawnParticle("reddust", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
			} else if(var6 == 2) {
				worldObj.spawnParticle("reddust", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
			} else if(var6 == 3) {
				worldObj.spawnParticle("reddust", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
			} else if(var6 == 4) {
				worldObj.spawnParticle("reddust", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
			} else {
				worldObj.spawnParticle("reddust", var7, var9, var11, 0.0D, 0.0D, 0.0D);
			}

		}
	}
}
