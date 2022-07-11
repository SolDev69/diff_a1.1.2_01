package net.minecraft.src;

import java.util.Random;

public class BlockGrass extends Block {
	protected BlockGrass(int id) {
		super(id, Material.grass);
		this.blockIndexInTexture = 3;
		this.setTickOnLoad(true);
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		if(world.getBlockLightValue(x, y + 1, z) < 4 && world.getBlockMaterial(x, y + 1, z).getCanBlockGrass()) {
			if(random.nextInt(4) != 0) {
				return;
			}

			world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
		} else if(world.getBlockLightValue(x, y + 1, z) >= 9) {
			int var6 = x + random.nextInt(3) - 1;
			int var7 = y + random.nextInt(5) - 3;
			int var8 = z + random.nextInt(3) - 1;
			if(world.getBlockId(var6, var7, var8) == Block.dirt.blockID && world.getBlockLightValue(var6, var7 + 1, var8) >= 4 && !world.getBlockMaterial(var6, var7 + 1, var8).getCanBlockGrass()) {
				world.setBlockWithNotify(var6, var7, var8, Block.grass.blockID);
			}
		}

	}

	public int idDropped(int count, Random random) {
		return Block.dirt.idDropped(0, random);
	}
}
