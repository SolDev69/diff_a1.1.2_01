package net.minecraft.src;

import java.util.Random;

public class BlockStationary extends BlockFluid {
	protected BlockStationary(int var1, Material var2) {
		super(var1, var2);
		this.setTickOnLoad(false);
		if(var2 == Material.lava) {
			this.setTickOnLoad(true);
		}

	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		super.onNeighborBlockChange(world, x, y, z, flag);
		if(world.getBlockId(x, y, z) == this.blockID) {
			this.setNotStationary(world, x, y, z);
		}

	}

	private void setNotStationary(World world, int x, int y, int z) {
		int var5 = world.getBlockMetadata(x, y, z);
		world.editingBlocks = true;
		world.setBlockAndMetadata(x, y, z, this.blockID - 1, var5);
		world.markBlocksDirty(x, y, z, x, y, z);
		world.scheduleBlockUpdate(x, y, z, this.blockID - 1);
		world.editingBlocks = false;
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		if(this.material == Material.lava) {
			int var6 = random.nextInt(3);

			for(int var7 = 0; var7 < var6; ++var7) {
				x += random.nextInt(3) - 1;
				++y;
				z += random.nextInt(3) - 1;
				int var8 = world.getBlockId(x, y, z);
				if(var8 == 0) {
					if(this.isFlammable(world, x - 1, y, z) || this.isFlammable(world, x + 1, y, z) || this.isFlammable(world, x, y, z - 1) || this.isFlammable(world, x, y, z + 1) || this.isFlammable(world, x, y - 1, z) || this.isFlammable(world, x, y + 1, z)) {
						world.setBlockWithNotify(x, y, z, Block.fire.blockID);
						return;
					}
				} else if(Block.blocksList[var8].material.getIsSolid()) {
					return;
				}
			}
		}

	}

	private boolean isFlammable(World world, int x, int y, int z) {
		return world.getBlockMaterial(x, y, z).getCanBurn();
	}
}
