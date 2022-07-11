package net.minecraft.src;

import java.util.Random;

public class WorldGenFlowers extends WorldGenerator {
	private int plantBlockId;

	public WorldGenFlowers(int plantBlockID) {
		this.plantBlockId = plantBlockID;
	}

	public boolean generate(World world, Random rand, int x, int y, int z) {
		for(int var6 = 0; var6 < 64; ++var6) {
			int var7 = x + rand.nextInt(8) - rand.nextInt(8);
			int var8 = y + rand.nextInt(4) - rand.nextInt(4);
			int var9 = z + rand.nextInt(8) - rand.nextInt(8);
			if(world.getBlockId(var7, var8, var9) == 0 && ((BlockFlower)Block.blocksList[this.plantBlockId]).canBlockStay(world, var7, var8, var9)) {
				world.setBlock(var7, var8, var9, this.plantBlockId);
			}
		}

		return true;
	}
}
