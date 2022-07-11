package net.minecraft.src;

import java.util.Random;

public class BlockSand extends Block {
	public static boolean fallInstantly = false;

	public BlockSand(int id, int blockIndex) {
		super(id, blockIndex, Material.sand);
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		world.scheduleBlockUpdate(x, y, z, this.blockID);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		world.scheduleBlockUpdate(x, y, z, this.blockID);
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		this.tryToFall(world, x, y, z);
	}

	private void tryToFall(World world, int x, int y, int z) {
		if(canFallBelow(world, x, y - 1, z) && y >= 0) {
			EntityFallingSand var8 = new EntityFallingSand(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, this.blockID);
			if(fallInstantly) {
				while(!var8.isDead) {
					var8.onUpdate();
				}
			} else {
				world.spawnEntityInWorld(var8);
			}
		}

	}

	public int tickRate() {
		return 3;
	}

	public static boolean canFallBelow(World world, int x, int y, int z) {
		int var4 = world.getBlockId(x, y, z);
		if(var4 == 0) {
			return true;
		} else if(var4 == Block.fire.blockID) {
			return true;
		} else {
			Material var5 = Block.blocksList[var4].material;
			return var5 == Material.water ? true : var5 == Material.lava;
		}
	}
}
