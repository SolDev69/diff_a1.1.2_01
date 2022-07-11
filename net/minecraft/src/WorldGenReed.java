package net.minecraft.src;

import java.util.Random;

public class WorldGenReed extends WorldGenerator {
	public boolean generate(World world, Random rand, int x, int y, int z) {
		for(int var6 = 0; var6 < 20; ++var6) {
			int var7 = x + rand.nextInt(4) - rand.nextInt(4);
			int var8 = y;
			int var9 = z + rand.nextInt(4) - rand.nextInt(4);
			if(world.getBlockId(var7, y, var9) == 0 && (world.getBlockMaterial(var7 - 1, y - 1, var9) == Material.water || world.getBlockMaterial(var7 + 1, y - 1, var9) == Material.water || world.getBlockMaterial(var7, y - 1, var9 - 1) == Material.water || world.getBlockMaterial(var7, y - 1, var9 + 1) == Material.water)) {
				int var10 = 2 + rand.nextInt(rand.nextInt(3) + 1);

				for(int var11 = 0; var11 < var10; ++var11) {
					if(Block.reed.canBlockStay(world, var7, var8 + var11, var9)) {
						world.setBlock(var7, var8 + var11, var9, Block.reed.blockID);
					}
				}
			}
		}

		return true;
	}
}
