package net.minecraft.src;

import java.util.Random;

public class BlockSapling extends BlockFlower {
	protected BlockSapling(int var1, int var2) {
		super(var1, var2);
		float var3 = 0.4F;
		this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 2.0F, 0.5F + var3);
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		super.updateTick(world, x, y, z, random);
		if(world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(5) == 0) {
			int var6 = world.getBlockMetadata(x, y, z);
			if(var6 < 15) {
				world.setBlockMetadataWithNotify(x, y, z, var6 + 1);
			} else {
				world.setBlock(x, y, z, 0);
				Object var7 = new WorldGenTrees();
				if(random.nextInt(10) == 0) {
					var7 = new WorldGenBigTree();
				}

				if(!((WorldGenerator)var7).generate(world, random, x, y, z)) {
					world.setBlock(x, y, z, this.blockID);
				}
			}
		}

	}
}
