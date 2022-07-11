package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BlockPressurePlate extends Block {
	private EnumMobType triggerMobType;

	protected BlockPressurePlate(int id, int blockIndex, EnumMobType triggerMobType) {
		super(id, blockIndex, Material.rock);
		this.triggerMobType = triggerMobType;
		this.setTickOnLoad(true);
		float var4 = 0.0625F;
		this.setBlockBounds(var4, 0.0F, var4, 1.0F - var4, 0.03125F, 1.0F - var4);
	}

	public int tickRate() {
		return 20;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x, y - 1, z);
	}

	public void onBlockAdded(World world, int x, int y, int z) {
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		boolean var6 = false;
		if(!world.isBlockNormalCube(x, y - 1, z)) {
			var6 = true;
		}

		if(var6) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
		}

	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		if(world.getBlockMetadata(x, y, z) != 0) {
			this.setStateIfMobInteractsWithPlate(world, x, y, z);
		}
	}

	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(world.getBlockMetadata(x, y, z) != 1) {
			this.setStateIfMobInteractsWithPlate(world, x, y, z);
		}
	}

	private void setStateIfMobInteractsWithPlate(World world, int x, int y, int z) {
		boolean var5 = world.getBlockMetadata(x, y, z) == 1;
		boolean var6 = false;
		float var7 = 0.125F;
		List var8 = null;
		if(this.triggerMobType == EnumMobType.everything) {
			var8 = world.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getBoundingBoxFromPool((double)((float)x + var7), (double)y, (double)((float)z + var7), (double)((float)(x + 1) - var7), (double)y + 0.25D, (double)((float)(z + 1) - var7)));
		}

		if(this.triggerMobType == EnumMobType.mobs) {
			var8 = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBoxFromPool((double)((float)x + var7), (double)y, (double)((float)z + var7), (double)((float)(x + 1) - var7), (double)y + 0.25D, (double)((float)(z + 1) - var7)));
		}

		if(this.triggerMobType == EnumMobType.players) {
			var8 = world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBoxFromPool((double)((float)x + var7), (double)y, (double)((float)z + var7), (double)((float)(x + 1) - var7), (double)y + 0.25D, (double)((float)(z + 1) - var7)));
		}

		if(var8.size() > 0) {
			var6 = true;
		}

		if(var6 && !var5) {
			world.setBlockMetadataWithNotify(x, y, z, 1);
			world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			world.markBlocksDirty(x, y, z, x, y, z);
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
		}

		if(!var6 && var5) {
			world.setBlockMetadataWithNotify(x, y, z, 0);
			world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			world.markBlocksDirty(x, y, z, x, y, z);
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
		}

		if(var6) {
			world.scheduleBlockUpdate(x, y, z, this.blockID);
		}

	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		int var5 = world.getBlockMetadata(x, y, z);
		if(var5 > 0) {
			world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
		}

		super.onBlockRemoval(world, x, y, z);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		boolean var5 = blockAccess.getBlockMetadata(x, y, z) == 1;
		float var6 = 0.0625F;
		if(var5) {
			this.setBlockBounds(var6, 0.0F, var6, 1.0F - var6, 0.03125F, 1.0F - var6);
		} else {
			this.setBlockBounds(var6, 0.0F, var6, 1.0F - var6, 0.0625F, 1.0F - var6);
		}

	}

	public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int unused) {
		return blockAccess.getBlockMetadata(x, y, z) > 0;
	}

	public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int flag) {
		return world.getBlockMetadata(x, y, z) == 0 ? false : flag == 1;
	}

	public boolean canProvidePower() {
		return true;
	}
}
