package net.minecraft.src;

import java.util.Random;

public class BlockFire extends Block {
	private int[] chanceToEncourageFire = new int[256];
	private int[] abilityToCatchFire = new int[256];

	protected BlockFire(int blockID, int tex) {
		super(blockID, tex, Material.fire);
		this.initializeBlock(Block.planks.blockID, 5, 20);
		this.initializeBlock(Block.wood.blockID, 5, 5);
		this.initializeBlock(Block.leaves.blockID, 30, 60);
		this.initializeBlock(Block.bookshelf.blockID, 30, 20);
		this.initializeBlock(Block.tnt.blockID, 15, 100);
		this.initializeBlock(Block.cloth.blockID, 30, 60);
		this.setTickOnLoad(true);
	}

	private void initializeBlock(int blockID, int fireChance, int fireAbility) {
		this.chanceToEncourageFire[blockID] = fireChance;
		this.abilityToCatchFire[blockID] = fireAbility;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 3;
	}

	public int quantityDropped(Random rand) {
		return 0;
	}

	public int tickRate() {
		return 10;
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		int var6 = worldObj.getBlockMetadata(x, y, z);
		if(var6 < 15) {
			worldObj.setBlockMetadataWithNotify(x, y, z, var6 + 1);
			worldObj.scheduleBlockUpdate(x, y, z, this.blockID);
		}

		if(!this.canNeighborBurn(worldObj, x, y, z)) {
			if(!worldObj.isBlockNormalCube(x, y - 1, z) || var6 > 3) {
				worldObj.setBlockWithNotify(x, y, z, 0);
			}

		} else if(!this.canBlockCatchFire(worldObj, x, y - 1, z) && var6 == 15 && rand.nextInt(4) == 0) {
			worldObj.setBlockWithNotify(x, y, z, 0);
		} else {
			if(var6 % 2 == 0 && var6 > 2) {
				this.tryToCatchBlockOnFire(worldObj, x + 1, y, z, 300, rand);
				this.tryToCatchBlockOnFire(worldObj, x - 1, y, z, 300, rand);
				this.tryToCatchBlockOnFire(worldObj, x, y - 1, z, 200, rand);
				this.tryToCatchBlockOnFire(worldObj, x, y + 1, z, 250, rand);
				this.tryToCatchBlockOnFire(worldObj, x, y, z - 1, 300, rand);
				this.tryToCatchBlockOnFire(worldObj, x, y, z + 1, 300, rand);

				for(int var7 = x - 1; var7 <= x + 1; ++var7) {
					for(int var8 = z - 1; var8 <= z + 1; ++var8) {
						for(int var9 = y - 1; var9 <= y + 4; ++var9) {
							if(var7 != x || var9 != y || var8 != z) {
								int var10 = 100;
								if(var9 > y + 1) {
									var10 += (var9 - (y + 1)) * 100;
								}

								int var11 = this.getChanceOfNeighborsEncouragingFire(worldObj, var7, var9, var8);
								if(var11 > 0 && rand.nextInt(var10) <= var11) {
									worldObj.setBlockWithNotify(var7, var9, var8, this.blockID);
								}
							}
						}
					}
				}
			}

		}
	}

	private void tryToCatchBlockOnFire(World worldObj, int x, int y, int z, int var5, Random rand) {
		int var7 = this.abilityToCatchFire[worldObj.getBlockId(x, y, z)];
		if(rand.nextInt(var5) < var7) {
			boolean var8 = worldObj.getBlockId(x, y, z) == Block.tnt.blockID;
			if(rand.nextInt(2) == 0) {
				worldObj.setBlockWithNotify(x, y, z, this.blockID);
			} else {
				worldObj.setBlockWithNotify(x, y, z, 0);
			}

			if(var8) {
				Block.tnt.onBlockDestroyedByPlayer(worldObj, x, y, z, 0);
			}
		}

	}

	private boolean canNeighborBurn(World worldObj, int x, int y, int z) {
		return this.canBlockCatchFire(worldObj, x + 1, y, z) ? true : (this.canBlockCatchFire(worldObj, x - 1, y, z) ? true : (this.canBlockCatchFire(worldObj, x, y - 1, z) ? true : (this.canBlockCatchFire(worldObj, x, y + 1, z) ? true : (this.canBlockCatchFire(worldObj, x, y, z - 1) ? true : this.canBlockCatchFire(worldObj, x, y, z + 1)))));
	}

	private int getChanceOfNeighborsEncouragingFire(World worldObj, int x, int y, int z) {
		byte var5 = 0;
		if(worldObj.getBlockId(x, y, z) != 0) {
			return 0;
		} else {
			int var6 = this.getChanceToEncourageFire(worldObj, x + 1, y, z, var5);
			var6 = this.getChanceToEncourageFire(worldObj, x - 1, y, z, var6);
			var6 = this.getChanceToEncourageFire(worldObj, x, y - 1, z, var6);
			var6 = this.getChanceToEncourageFire(worldObj, x, y + 1, z, var6);
			var6 = this.getChanceToEncourageFire(worldObj, x, y, z - 1, var6);
			var6 = this.getChanceToEncourageFire(worldObj, x, y, z + 1, var6);
			return var6;
		}
	}

	public boolean isCollidable() {
		return false;
	}

	public boolean canBlockCatchFire(IBlockAccess blockAccess, int x, int y, int z) {
		return this.chanceToEncourageFire[blockAccess.getBlockId(x, y, z)] > 0;
	}

	public int getChanceToEncourageFire(World worldObj, int x, int y, int z, int var5) {
		int var6 = this.chanceToEncourageFire[worldObj.getBlockId(x, y, z)];
		return var6 > var5 ? var6 : var5;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x, y - 1, z) || this.canNeighborBurn(world, x, y, z);
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		if(!worldObj.isBlockNormalCube(x, y - 1, z) && !this.canNeighborBurn(worldObj, x, y, z)) {
			worldObj.setBlockWithNotify(x, y, z, 0);
		}
	}

	public void onBlockAdded(World worldObj, int x, int y, int z) {
		if(!worldObj.isBlockNormalCube(x, y - 1, z) && !this.canNeighborBurn(worldObj, x, y, z)) {
			worldObj.setBlockWithNotify(x, y, z, 0);
		} else {
			worldObj.scheduleBlockUpdate(x, y, z, this.blockID);
		}
	}

	public void randomDisplayTick(World worldObj, int x, int y, int z, Random rand) {
		if(rand.nextInt(24) == 0) {
			worldObj.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "fire.fire", 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F);
		}

		int var6;
		float var7;
		float var8;
		float var9;
		if(!worldObj.isBlockNormalCube(x, y - 1, z) && !Block.fire.canBlockCatchFire(worldObj, x, y - 1, z)) {
			if(Block.fire.canBlockCatchFire(worldObj, x - 1, y, z)) {
				for(var6 = 0; var6 < 2; ++var6) {
					var7 = (float)x + rand.nextFloat() * 0.1F;
					var8 = (float)y + rand.nextFloat();
					var9 = (float)z + rand.nextFloat();
					worldObj.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(worldObj, x + 1, y, z)) {
				for(var6 = 0; var6 < 2; ++var6) {
					var7 = (float)(x + 1) - rand.nextFloat() * 0.1F;
					var8 = (float)y + rand.nextFloat();
					var9 = (float)z + rand.nextFloat();
					worldObj.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(worldObj, x, y, z - 1)) {
				for(var6 = 0; var6 < 2; ++var6) {
					var7 = (float)x + rand.nextFloat();
					var8 = (float)y + rand.nextFloat();
					var9 = (float)z + rand.nextFloat() * 0.1F;
					worldObj.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(worldObj, x, y, z + 1)) {
				for(var6 = 0; var6 < 2; ++var6) {
					var7 = (float)x + rand.nextFloat();
					var8 = (float)y + rand.nextFloat();
					var9 = (float)(z + 1) - rand.nextFloat() * 0.1F;
					worldObj.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(worldObj, x, y + 1, z)) {
				for(var6 = 0; var6 < 2; ++var6) {
					var7 = (float)x + rand.nextFloat();
					var8 = (float)(y + 1) - rand.nextFloat() * 0.1F;
					var9 = (float)z + rand.nextFloat();
					worldObj.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
				}
			}
		} else {
			for(var6 = 0; var6 < 3; ++var6) {
				var7 = (float)x + rand.nextFloat();
				var8 = (float)y + rand.nextFloat() * 0.5F + 0.5F;
				var9 = (float)z + rand.nextFloat();
				worldObj.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
			}
		}

	}
}
