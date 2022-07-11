package net.minecraft.src;

import java.util.Random;

public class MapGenBase {
	protected int range = 8;
	protected Random rand = new Random();

	public void generate(ChunkProviderGenerate chunkProviderGenerate, World world, int var3, int var4, byte[] data) {
		int var6 = this.range;
		this.rand.setSeed(world.randomSeed);
		long var7 = this.rand.nextLong() / 2L * 2L + 1L;
		long var9 = this.rand.nextLong() / 2L * 2L + 1L;

		for(int var11 = var3 - var6; var11 <= var3 + var6; ++var11) {
			for(int var12 = var4 - var6; var12 <= var4 + var6; ++var12) {
				this.rand.setSeed((long)var11 * var7 + (long)var12 * var9 ^ world.randomSeed);
				this.recursiveGenerate(world, var11, var12, var3, var4, data);
			}
		}

	}

	protected void recursiveGenerate(World world, int var2, int var3, int var4, int var5, byte[] data) {
	}
}
