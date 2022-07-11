package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockRedstoneTorch extends BlockTorch {
	private boolean torchActive = false;
	private static List torchUpdates = new ArrayList();

	private boolean checkForBurnout(World world, int x, int y, int z, boolean var5) {
		if(var5) {
			torchUpdates.add(new RedstoneUpdateInfo(x, y, z, world.worldTime));
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

	protected BlockRedstoneTorch(int id, int blockIndex, boolean torchActive) {
		super(id, blockIndex);
		this.torchActive = torchActive;
		this.setTickOnLoad(true);
	}

	public int tickRate() {
		return 2;
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		if(world.getBlockMetadata(x, y, z) == 0) {
			super.onBlockAdded(world, x, y, z);
		}

		if(this.torchActive) {
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
		}

	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		if(this.torchActive) {
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
		}

	}

	public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int unused) {
		if(!this.torchActive) {
			return false;
		} else {
			int var6 = blockAccess.getBlockMetadata(x, y, z);
			return var6 == 5 && unused == 1 ? false : (var6 == 3 && unused == 3 ? false : (var6 == 4 && unused == 2 ? false : (var6 == 1 && unused == 5 ? false : var6 != 2 || unused != 4)));
		}
	}

	private boolean isIndirectlyPowered(World world, int x, int y, int z) {
		int var5 = world.getBlockMetadata(x, y, z);
		return var5 == 5 && world.isBlockIndirectlyProvidingPowerTo(x, y - 1, z, 0) ? true : (var5 == 3 && world.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2) ? true : (var5 == 4 && world.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3) ? true : (var5 == 1 && world.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4) ? true : var5 == 2 && world.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5))));
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		boolean var6 = this.isIndirectlyPowered(world, x, y, z);

		while(torchUpdates.size() > 0 && world.worldTime - ((RedstoneUpdateInfo)torchUpdates.get(0)).updateTime > 100L) {
			torchUpdates.remove(0);
		}

		if(this.torchActive) {
			if(var6) {
				world.setBlockAndMetadataWithNotify(x, y, z, Block.torchRedstoneIdle.blockID, world.getBlockMetadata(x, y, z));
				if(this.checkForBurnout(world, x, y, z, true)) {
					world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

					for(int var7 = 0; var7 < 5; ++var7) {
						double var8 = (double)x + random.nextDouble() * 0.6D + 0.2D;
						double var10 = (double)y + random.nextDouble() * 0.6D + 0.2D;
						double var12 = (double)z + random.nextDouble() * 0.6D + 0.2D;
						world.spawnParticle("smoke", var8, var10, var12, 0.0D, 0.0D, 0.0D);
					}
				}
			}
		} else if(!var6 && !this.checkForBurnout(world, x, y, z, false)) {
			world.setBlockAndMetadataWithNotify(x, y, z, Block.torchRedstoneActive.blockID, world.getBlockMetadata(x, y, z));
		}

	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		super.onNeighborBlockChange(world, x, y, z, flag);
		world.scheduleBlockUpdate(x, y, z, this.blockID);
	}

	public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int flag) {
		return flag == 0 ? this.isPoweringTo(world, x, y, z, flag) : false;
	}

	public int idDropped(int count, Random random) {
		return Block.torchRedstoneActive.blockID;
	}

	public boolean canProvidePower() {
		return true;
	}
}
