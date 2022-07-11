package net.minecraft.src;

import java.util.Random;

public class BlockLeaves extends BlockLeavesBase {
	private int leafTexIndex;
	private int decayCounter = 0;

	protected BlockLeaves(int id, int blockIndex) {
		super(id, blockIndex, Material.leaves, false);
		this.leafTexIndex = blockIndex;
		this.setTickOnLoad(true);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		this.decayCounter = 0;
		this.updateCurrentLeaves(world, x, y, z);
		super.onNeighborBlockChange(world, x, y, z, flag);
	}

	public void updateConnectedLeaves(World world, int x, int y, int z, int var5) {
		if(world.getBlockId(x, y, z) == this.blockID) {
			int var6 = world.getBlockMetadata(x, y, z);
			if(var6 != 0 && var6 == var5 - 1) {
				this.updateCurrentLeaves(world, x, y, z);
			}
		}
	}

	public void updateCurrentLeaves(World world, int x, int y, int z) {
		if(this.decayCounter++ < 100) {
			int var5 = world.getBlockMaterial(x, y - 1, z).isSolid() ? 16 : 0;
			int var6 = world.getBlockMetadata(x, y, z);
			if(var6 == 0) {
				var6 = 1;
				world.setBlockMetadataWithNotify(x, y, z, 1);
			}

			var5 = this.getConnectionStrength(world, x, y - 1, z, var5);
			var5 = this.getConnectionStrength(world, x, y, z - 1, var5);
			var5 = this.getConnectionStrength(world, x, y, z + 1, var5);
			var5 = this.getConnectionStrength(world, x - 1, y, z, var5);
			var5 = this.getConnectionStrength(world, x + 1, y, z, var5);
			int var7 = var5 - 1;
			if(var7 < 10) {
				var7 = 1;
			}

			if(var7 != var6) {
				world.setBlockMetadataWithNotify(x, y, z, var7);
				this.updateConnectedLeaves(world, x, y - 1, z, var6);
				this.updateConnectedLeaves(world, x, y + 1, z, var6);
				this.updateConnectedLeaves(world, x, y, z - 1, var6);
				this.updateConnectedLeaves(world, x, y, z + 1, var6);
				this.updateConnectedLeaves(world, x - 1, y, z, var6);
				this.updateConnectedLeaves(world, x + 1, y, z, var6);
			}

		}
	}

	private int getConnectionStrength(World world, int x, int y, int z, int var5) {
		int var6 = world.getBlockId(x, y, z);
		if(var6 == Block.wood.blockID) {
			return 16;
		} else {
			if(var6 == this.blockID) {
				int var7 = world.getBlockMetadata(x, y, z);
				if(var7 != 0 && var7 > var5) {
					return var7;
				}
			}

			return var5;
		}
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		int var6 = world.getBlockMetadata(x, y, z);
		if(var6 == 0) {
			this.decayCounter = 0;
			this.updateCurrentLeaves(world, x, y, z);
		} else if(var6 == 1) {
			this.removeLeaves(world, x, y, z);
		} else if(random.nextInt(10) == 0) {
			this.updateCurrentLeaves(world, x, y, z);
		}

	}

	private void removeLeaves(World world, int x, int y, int z) {
		this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
		world.setBlockWithNotify(x, y, z, 0);
	}

	public int quantityDropped(Random random) {
		return random.nextInt(20) == 0 ? 1 : 0;
	}

	public int idDropped(int count, Random random) {
		return Block.sapling.blockID;
	}

	public boolean isOpaqueCube() {
		return !this.graphicsLevel;
	}

	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		super.onEntityWalking(world, x, y, z, entity);
	}
}
