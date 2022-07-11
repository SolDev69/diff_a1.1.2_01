package net.minecraft.src;

import java.util.Random;

public class WorldGenCactus extends WorldGenerator {
	public boolean generate(World world, Random rand, int x, int y, int z) {
		for(int var6 = 0; var6 < 10; ++var6) {
			int var7 = x + rand.nextInt(8) - rand.nextInt(8);
			int var8 = y + rand.nextInt(4) - rand.nextInt(4);
			int var9 = z + rand.nextInt(8) - rand.nextInt(8);
			if(world.getBlockId(var7, var8, var9) == 0) {
				int var10 = 1 + rand.nextInt(rand.nextInt(3) + 1);

				for(int var11 = 0; var11 < var10; ++var11) {
					if(Block.cactus.canBlockStay(world, var7, var8 + var11, var9)) {
						world.setBlock(var7, var8 + var11, var9, Block.cactus.blockID);
					}
				}
			}
		}

		return true;
	}
}
