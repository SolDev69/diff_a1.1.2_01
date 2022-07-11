package net.minecraft.src;

import java.util.Random;

public class WorldGenMinable extends WorldGenerator {
	private int minableBlockId;
	private int numberOfBlocks;

	public WorldGenMinable(int minableBlockID, int numberOfBlocks) {
		this.minableBlockId = minableBlockID;
		this.numberOfBlocks = numberOfBlocks;
	}

	public boolean generate(World world, Random rand, int x, int y, int z) {
		float var6 = rand.nextFloat() * (float)Math.PI;
		double var7 = (double)((float)(x + 8) + MathHelper.sin(var6) * (float)this.numberOfBlocks / 8.0F);
		double var9 = (double)((float)(x + 8) - MathHelper.sin(var6) * (float)this.numberOfBlocks / 8.0F);
		double var11 = (double)((float)(z + 8) + MathHelper.cos(var6) * (float)this.numberOfBlocks / 8.0F);
		double var13 = (double)((float)(z + 8) - MathHelper.cos(var6) * (float)this.numberOfBlocks / 8.0F);
		double var15 = (double)(y + rand.nextInt(3) + 2);
		double var17 = (double)(y + rand.nextInt(3) + 2);

		for(int var19 = 0; var19 <= this.numberOfBlocks; ++var19) {
			double var20 = var7 + (var9 - var7) * (double)var19 / (double)this.numberOfBlocks;
			double var22 = var15 + (var17 - var15) * (double)var19 / (double)this.numberOfBlocks;
			double var24 = var11 + (var13 - var11) * (double)var19 / (double)this.numberOfBlocks;
			double var26 = rand.nextDouble() * (double)this.numberOfBlocks / 16.0D;
			double var28 = (double)(MathHelper.sin((float)var19 * (float)Math.PI / (float)this.numberOfBlocks) + 1.0F) * var26 + 1.0D;
			double var30 = (double)(MathHelper.sin((float)var19 * (float)Math.PI / (float)this.numberOfBlocks) + 1.0F) * var26 + 1.0D;

			for(int var32 = (int)(var20 - var28 / 2.0D); var32 <= (int)(var20 + var28 / 2.0D); ++var32) {
				for(int var33 = (int)(var22 - var30 / 2.0D); var33 <= (int)(var22 + var30 / 2.0D); ++var33) {
					for(int var34 = (int)(var24 - var28 / 2.0D); var34 <= (int)(var24 + var28 / 2.0D); ++var34) {
						double var35 = ((double)var32 + 0.5D - var20) / (var28 / 2.0D);
						double var37 = ((double)var33 + 0.5D - var22) / (var30 / 2.0D);
						double var39 = ((double)var34 + 0.5D - var24) / (var28 / 2.0D);
						if(var35 * var35 + var37 * var37 + var39 * var39 < 1.0D && world.getBlockId(var32, var33, var34) == Block.stone.blockID) {
							world.setBlock(var32, var33, var34, this.minableBlockId);
						}
					}
				}
			}
		}

		return true;
	}
}
