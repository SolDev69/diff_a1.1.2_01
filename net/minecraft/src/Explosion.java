package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Explosion {
	public void doExplosion(World world, Entity entity, double x, double y, double z, float power) {
		world.playSoundEffect(x, y, z, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
		HashSet var10 = new HashSet();
		float var11 = power;
		byte var12 = 16;

		int var13;
		int var14;
		int var15;
		double var25;
		double var27;
		double var29;
		for(var13 = 0; var13 < var12; ++var13) {
			for(var14 = 0; var14 < var12; ++var14) {
				for(var15 = 0; var15 < var12; ++var15) {
					if(var13 == 0 || var13 == var12 - 1 || var14 == 0 || var14 == var12 - 1 || var15 == 0 || var15 == var12 - 1) {
						double var16 = (double)((float)var13 / ((float)var12 - 1.0F) * 2.0F - 1.0F);
						double var18 = (double)((float)var14 / ((float)var12 - 1.0F) * 2.0F - 1.0F);
						double var20 = (double)((float)var15 / ((float)var12 - 1.0F) * 2.0F - 1.0F);
						double var22 = Math.sqrt(var16 * var16 + var18 * var18 + var20 * var20);
						var16 /= var22;
						var18 /= var22;
						var20 /= var22;
						float var24 = power * (0.7F + world.rand.nextFloat() * 0.6F);
						var25 = x;
						var27 = y;
						var29 = z;

						for(float var31 = 0.3F; var24 > 0.0F; var24 -= var31 * 0.75F) {
							int var32 = MathHelper.floor_double(var25);
							int var33 = MathHelper.floor_double(var27);
							int var34 = MathHelper.floor_double(var29);
							int var35 = world.getBlockId(var32, var33, var34);
							if(var35 > 0) {
								var24 -= (Block.blocksList[var35].getExplosionResistance(entity) + 0.3F) * var31;
							}

							if(var24 > 0.0F) {
								var10.add(new ChunkPosition(var32, var33, var34));
							}

							var25 += var16 * (double)var31;
							var27 += var18 * (double)var31;
							var29 += var20 * (double)var31;
						}
					}
				}
			}
		}

		power *= 2.0F;
		var13 = MathHelper.floor_double(x - (double)power - 1.0D);
		var14 = MathHelper.floor_double(x + (double)power + 1.0D);
		var15 = MathHelper.floor_double(y - (double)power - 1.0D);
		int var45 = MathHelper.floor_double(y + (double)power + 1.0D);
		int var17 = MathHelper.floor_double(z - (double)power - 1.0D);
		int var46 = MathHelper.floor_double(z + (double)power + 1.0D);
		List var19 = world.getEntitiesWithinAABBExcludingEntity(entity, AxisAlignedBB.getBoundingBoxFromPool((double)var13, (double)var15, (double)var17, (double)var14, (double)var45, (double)var46));
		Vec3D var47 = Vec3D.createVector(x, y, z);

		double var55;
		double var56;
		double var57;
		for(int var21 = 0; var21 < var19.size(); ++var21) {
			Entity var49 = (Entity)var19.get(var21);
			double var23 = var49.getDistance(x, y, z) / (double)power;
			if(var23 <= 1.0D) {
				var25 = var49.posX - x;
				var27 = var49.posY - y;
				var29 = var49.posZ - z;
				var55 = (double)MathHelper.sqrt_double(var25 * var25 + var27 * var27 + var29 * var29);
				var25 /= var55;
				var27 /= var55;
				var29 /= var55;
				var56 = (double)world.getBlockDensity(var47, var49.boundingBox);
				var57 = (1.0D - var23) * var56;
				var49.attackEntityFrom(entity, (int)((var57 * var57 + var57) / 2.0D * 8.0D * (double)power + 1.0D));
				var49.motionX += var25 * var57;
				var49.motionY += var27 * var57;
				var49.motionZ += var29 * var57;
			}
		}

		power = var11;
		ArrayList var48 = new ArrayList();
		var48.addAll(var10);

		for(int var50 = var48.size() - 1; var50 >= 0; --var50) {
			ChunkPosition var51 = (ChunkPosition)var48.get(var50);
			int var52 = var51.x;
			int var53 = var51.y;
			int var26 = var51.z;
			int var54 = world.getBlockId(var52, var53, var26);

			for(int var28 = 0; var28 < 1; ++var28) {
				var29 = (double)((float)var52 + world.rand.nextFloat());
				var55 = (double)((float)var53 + world.rand.nextFloat());
				var56 = (double)((float)var26 + world.rand.nextFloat());
				var57 = var29 - x;
				double var37 = var55 - y;
				double var39 = var56 - z;
				double var41 = (double)MathHelper.sqrt_double(var57 * var57 + var37 * var37 + var39 * var39);
				var57 /= var41;
				var37 /= var41;
				var39 /= var41;
				double var43 = 0.5D / (var41 / (double)power + 0.1D);
				var43 *= (double)(world.rand.nextFloat() * world.rand.nextFloat() + 0.3F);
				var57 *= var43;
				var37 *= var43;
				var39 *= var43;
				world.spawnParticle("explode", (var29 + x * 1.0D) / 2.0D, (var55 + y * 1.0D) / 2.0D, (var56 + z * 1.0D) / 2.0D, var57, var37, var39);
				world.spawnParticle("smoke", var29, var55, var56, var57, var37, var39);
			}

			if(var54 > 0) {
				Block.blocksList[var54].dropBlockAsItemWithChance(world, var52, var53, var26, world.getBlockMetadata(var52, var53, var26), 0.3F);
				world.setBlockWithNotify(var52, var53, var26, 0);
				Block.blocksList[var54].onBlockDestroyedByExplosion(world, var52, var53, var26);
			}
		}

	}
}
