package net.minecraft.src;

import java.util.Random;

public class NoiseGeneratorOctaves extends NoiseGenerator {
	private NoiseGeneratorPerlin[] generatorCollection;
	private int octaves;

	public NoiseGeneratorOctaves(Random random, int octaves) {
		this.octaves = octaves;
		this.generatorCollection = new NoiseGeneratorPerlin[octaves];

		for(int var3 = 0; var3 < octaves; ++var3) {
			this.generatorCollection[var3] = new NoiseGeneratorPerlin(random);
		}

	}

	public double generateNoiseOctaves(double x, double z) {
		double var5 = 0.0D;
		double var7 = 1.0D;

		for(int var9 = 0; var9 < this.octaves; ++var9) {
			var5 += this.generatorCollection[var9].generateNoise(x * var7, z * var7) / var7;
			var7 /= 2.0D;
		}

		return var5;
	}

	public double[] generateNoiseOctaves(double[] data, double var2, double var4, double var6, int x, int y, int z, double var11, double var13, double var15) {
		if(data == null) {
			data = new double[x * y * z];
		} else {
			for(int var17 = 0; var17 < data.length; ++var17) {
				data[var17] = 0.0D;
			}
		}

		double var20 = 1.0D;

		for(int var19 = 0; var19 < this.octaves; ++var19) {
			this.generatorCollection[var19].populateNoiseArray(data, var2, var4, var6, x, y, z, var11 * var20, var13 * var20, var15 * var20, var20);
			var20 /= 2.0D;
		}

		return data;
	}
}
