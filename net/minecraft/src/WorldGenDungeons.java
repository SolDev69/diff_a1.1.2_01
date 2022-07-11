package net.minecraft.src;

import java.util.Random;

public class WorldGenDungeons extends WorldGenerator {
	public boolean generate(World world, Random rand, int x, int y, int z) {
		byte var6 = 3;
		int var7 = rand.nextInt(2) + 2;
		int var8 = rand.nextInt(2) + 2;
		int var9 = 0;

		int var10;
		int var11;
		int var12;
		for(var10 = x - var7 - 1; var10 <= x + var7 + 1; ++var10) {
			for(var11 = y - 1; var11 <= y + var6 + 1; ++var11) {
				for(var12 = z - var8 - 1; var12 <= z + var8 + 1; ++var12) {
					Material var13 = world.getBlockMaterial(var10, var11, var12);
					if(var11 == y - 1 && !var13.isSolid()) {
						return false;
					}

					if(var11 == y + var6 + 1 && !var13.isSolid()) {
						return false;
					}

					if((var10 == x - var7 - 1 || var10 == x + var7 + 1 || var12 == z - var8 - 1 || var12 == z + var8 + 1) && var11 == y && world.getBlockId(var10, var11, var12) == 0 && world.getBlockId(var10, var11 + 1, var12) == 0) {
						++var9;
					}
				}
			}
		}

		if(var9 >= 1 && var9 <= 5) {
			for(var10 = x - var7 - 1; var10 <= x + var7 + 1; ++var10) {
				for(var11 = y + var6; var11 >= y - 1; --var11) {
					for(var12 = z - var8 - 1; var12 <= z + var8 + 1; ++var12) {
						if(var10 != x - var7 - 1 && var11 != y - 1 && var12 != z - var8 - 1 && var10 != x + var7 + 1 && var11 != y + var6 + 1 && var12 != z + var8 + 1) {
							world.setBlockWithNotify(var10, var11, var12, 0);
						} else if(var11 >= 0 && !world.getBlockMaterial(var10, var11 - 1, var12).isSolid()) {
							world.setBlockWithNotify(var10, var11, var12, 0);
						} else if(world.getBlockMaterial(var10, var11, var12).isSolid()) {
							if(var11 == y - 1 && rand.nextInt(4) != 0) {
								world.setBlockWithNotify(var10, var11, var12, Block.cobblestoneMossy.blockID);
							} else {
								world.setBlockWithNotify(var10, var11, var12, Block.cobblestone.blockID);
							}
						}
					}
				}
			}

			var10 = 0;

			while(var10 < 2) {
				var11 = 0;

				while(true) {
					if(var11 < 3) {
						label204: {
							var12 = x + rand.nextInt(var7 * 2 + 1) - var7;
							int var14 = z + rand.nextInt(var8 * 2 + 1) - var8;
							if(world.getBlockId(var12, y, var14) == 0) {
								int var15 = 0;
								if(world.getBlockMaterial(var12 - 1, y, var14).isSolid()) {
									++var15;
								}

								if(world.getBlockMaterial(var12 + 1, y, var14).isSolid()) {
									++var15;
								}

								if(world.getBlockMaterial(var12, y, var14 - 1).isSolid()) {
									++var15;
								}

								if(world.getBlockMaterial(var12, y, var14 + 1).isSolid()) {
									++var15;
								}

								if(var15 == 1) {
									world.setBlockWithNotify(var12, y, var14, Block.chest.blockID);
									TileEntityChest var16 = (TileEntityChest)world.getBlockTileEntity(var12, y, var14);

									for(int var17 = 0; var17 < 8; ++var17) {
										ItemStack var18 = this.pickCheckLootItem(rand);
										if(var18 != null) {
											var16.setInventorySlotContents(rand.nextInt(var16.getSizeInventory()), var18);
										}
									}
									break label204;
								}
							}

							++var11;
							continue;
						}
					}

					++var10;
					break;
				}
			}

			world.setBlockWithNotify(x, y, z, Block.mobSpawner.blockID);
			TileEntityMobSpawner var19 = (TileEntityMobSpawner)world.getBlockTileEntity(x, y, z);
			var19.mobID = this.pickMobSpawner(rand);
			return true;
		} else {
			return false;
		}
	}

	private ItemStack pickCheckLootItem(Random random) {
		int var2 = random.nextInt(11);
		return var2 == 0 ? new ItemStack(Item.saddle) : (var2 == 1 ? new ItemStack(Item.ingotIron, random.nextInt(4) + 1) : (var2 == 2 ? new ItemStack(Item.bread) : (var2 == 3 ? new ItemStack(Item.wheat, random.nextInt(4) + 1) : (var2 == 4 ? new ItemStack(Item.gunpowder, random.nextInt(4) + 1) : (var2 == 5 ? new ItemStack(Item.silk, random.nextInt(4) + 1) : (var2 == 6 ? new ItemStack(Item.bucketEmpty) : (var2 == 7 && random.nextInt(100) == 0 ? new ItemStack(Item.appleGold) : (var2 == 8 && random.nextInt(2) == 0 ? new ItemStack(Item.redstone, random.nextInt(4) + 1) : (var2 == 9 && random.nextInt(10) == 0 ? new ItemStack(Item.itemsList[Item.record13.shiftedIndex + random.nextInt(2)]) : null)))))))));
	}

	private String pickMobSpawner(Random random) {
		int var2 = random.nextInt(4);
		return var2 == 0 ? "Skeleton" : (var2 == 1 ? "Zombie" : (var2 == 2 ? "Zombie" : (var2 == 3 ? "Spider" : "")));
	}
}
