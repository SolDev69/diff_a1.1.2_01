package net.minecraft.src;

import java.util.Random;

public class BlockRedstoneOre extends Block {
	private boolean glowing;

	public BlockRedstoneOre(int id, int blockIndex, boolean glowing) {
		super(id, blockIndex, Material.rock);
		if(glowing) {
			this.setTickOnLoad(true);
		}

		this.glowing = glowing;
	}

	public int tickRate() {
		return 30;
	}

	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		this.glow(world, x, y, z);
		super.onBlockClicked(world, x, y, z, entityPlayer);
	}

	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		this.glow(world, x, y, z);
		super.onEntityWalking(world, x, y, z, entity);
	}

	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		this.glow(world, x, y, z);
		return super.blockActivated(world, x, y, z, entityPlayer);
	}

	private void glow(World world, int x, int y, int z) {
		this.sparkle(world, x, y, z);
		if(this.blockID == Block.oreRedstone.blockID) {
			world.setBlockWithNotify(x, y, z, Block.oreRedstoneGlowing.blockID);
		}

	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		if(this.blockID == Block.oreRedstoneGlowing.blockID) {
			world.setBlockWithNotify(x, y, z, Block.oreRedstone.blockID);
		}

	}

	public int idDropped(int count, Random random) {
		return Item.redstone.shiftedIndex;
	}

	public int quantityDropped(Random random) {
		return 4 + random.nextInt(2);
	}

	private void sparkle(World world, int x, int y, int z) {
		Random var5 = world.rand;
		double var6 = 0.0625D;

		for(int var8 = 0; var8 < 6; ++var8) {
			double var9 = (double)((float)x + var5.nextFloat());
			double var11 = (double)((float)y + var5.nextFloat());
			double var13 = (double)((float)z + var5.nextFloat());
			if(var8 == 0 && !world.isBlockNormalCube(x, y + 1, z)) {
				var11 = (double)(y + 1) + var6;
			}

			if(var8 == 1 && !world.isBlockNormalCube(x, y - 1, z)) {
				var11 = (double)(y + 0) - var6;
			}

			if(var8 == 2 && !world.isBlockNormalCube(x, y, z + 1)) {
				var13 = (double)(z + 1) + var6;
			}

			if(var8 == 3 && !world.isBlockNormalCube(x, y, z - 1)) {
				var13 = (double)(z + 0) - var6;
			}

			if(var8 == 4 && !world.isBlockNormalCube(x + 1, y, z)) {
				var9 = (double)(x + 1) + var6;
			}

			if(var8 == 5 && !world.isBlockNormalCube(x - 1, y, z)) {
				var9 = (double)(x + 0) - var6;
			}

			if(var9 < (double)x || var9 > (double)(x + 1) || var11 < 0.0D || var11 > (double)(y + 1) || var13 < (double)z || var13 > (double)(z + 1)) {
				world.spawnParticle("reddust", var9, var11, var13, 0.0D, 0.0D, 0.0D);
			}
		}

	}
}
