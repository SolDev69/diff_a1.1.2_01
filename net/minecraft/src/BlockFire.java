package net.minecraft.src;

import java.util.Random;

public class BlockFire extends Block {
	private int[] chanceToEncourageFire = new int[256];
	private int[] abilityToCatchFire = new int[256];

	protected BlockFire(int id, int blockIndex) {
		super(id, blockIndex, Material.fire);
		this.initializeBlock(Block.planks.blockID, 5, 20);
		this.initializeBlock(Block.wood.blockID, 5, 5);
		this.initializeBlock(Block.leaves.blockID, 30, 60);
		this.initializeBlock(Block.bookshelf.blockID, 30, 20);
		this.initializeBlock(Block.tnt.blockID, 15, 100);
		this.initializeBlock(Block.cloth.blockID, 30, 60);
		this.setTickOnLoad(true);
	}

	private void initializeBlock(int blockID, int chance, int ability) {
		this.chanceToEncourageFire[blockID] = chance;
		this.abilityToCatchFire[blockID] = ability;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 3;
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public int tickRate() {
		return 10;
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		int var6 = world.getBlockMetadata(x, y, z);
		if(var6 < 15) {
			world.setBlockMetadataWithNotify(x, y, z, var6 + 1);
			world.scheduleBlockUpdate(x, y, z, this.blockID);
		}

		if(!this.canNeighborBurn(world, x, y, z)) {
			if(!world.isBlockNormalCube(x, y - 1, z) || var6 > 3) {
				world.setBlockWithNotify(x, y, z, 0);
			}

		} else if(!this.canBlockCatchFire(world, x, y - 1, z) && var6 == 15 && random.nextInt(4) == 0) {
			world.setBlockWithNotify(x, y, z, 0);
		} else {
			if(var6 % 2 == 0 && var6 > 2) {
				this.tryToCatchBlockOnFire(world, x + 1, y, z, 300, random);
				this.tryToCatchBlockOnFire(world, x - 1, y, z, 300, random);
				this.tryToCatchBlockOnFire(world, x, y - 1, z, 200, random);
				this.tryToCatchBlockOnFire(world, x, y + 1, z, 250, random);
				this.tryToCatchBlockOnFire(world, x, y, z - 1, 300, random);
				this.tryToCatchBlockOnFire(world, x, y, z + 1, 300, random);

				for(int var7 = x - 1; var7 <= x + 1; ++var7) {
					for(int var8 = z - 1; var8 <= z + 1; ++var8) {
						for(int var9 = y - 1; var9 <= y + 4; ++var9) {
							if(var7 != x || var9 != y || var8 != z) {
								int var10 = 100;
								if(var9 > y + 1) {
									var10 += (var9 - (y + 1)) * 100;
								}

								int var11 = this.getChanceOfNeighborsEncouragingFire(world, var7, var9, var8);
								if(var11 > 0 && random.nextInt(var10) <= var11) {
									world.setBlockWithNotify(var7, var9, var8, this.blockID);
								}
							}
						}
					}
				}
			}

		}
	}

	private void tryToCatchBlockOnFire(World world, int x, int y, int z, int chance, Random random) {
		int var7 = this.abilityToCatchFire[world.getBlockId(x, y, z)];
		if(random.nextInt(chance) < var7) {
			boolean var8 = world.getBlockId(x, y, z) == Block.tnt.blockID;
			if(random.nextInt(2) == 0) {
				world.setBlockWithNotify(x, y, z, this.blockID);
			} else {
				world.setBlockWithNotify(x, y, z, 0);
			}

			if(var8) {
				Block.tnt.onBlockDestroyedByPlayer(world, x, y, z, 0);
			}
		}

	}

	private boolean canNeighborBurn(World world, int x, int y, int z) {
		return this.canBlockCatchFire(world, x + 1, y, z) ? true : (this.canBlockCatchFire(world, x - 1, y, z) ? true : (this.canBlockCatchFire(world, x, y - 1, z) ? true : (this.canBlockCatchFire(world, x, y + 1, z) ? true : (this.canBlockCatchFire(world, x, y, z - 1) ? true : this.canBlockCatchFire(world, x, y, z + 1)))));
	}

	private int getChanceOfNeighborsEncouragingFire(World world, int x, int y, int z) {
		byte var5 = 0;
		if(world.getBlockId(x, y, z) != 0) {
			return 0;
		} else {
			int var6 = this.getChanceToEncourageFire(world, x + 1, y, z, var5);
			var6 = this.getChanceToEncourageFire(world, x - 1, y, z, var6);
			var6 = this.getChanceToEncourageFire(world, x, y - 1, z, var6);
			var6 = this.getChanceToEncourageFire(world, x, y + 1, z, var6);
			var6 = this.getChanceToEncourageFire(world, x, y, z - 1, var6);
			var6 = this.getChanceToEncourageFire(world, x, y, z + 1, var6);
			return var6;
		}
	}

	public boolean isCollidable() {
		return false;
	}

	public boolean canBlockCatchFire(IBlockAccess blockAccess, int x, int y, int z) {
		return this.chanceToEncourageFire[blockAccess.getBlockId(x, y, z)] > 0;
	}

	public int getChanceToEncourageFire(World world, int x, int y, int z, int flag) {
		int var6 = this.chanceToEncourageFire[world.getBlockId(x, y, z)];
		return var6 > flag ? var6 : flag;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x, y - 1, z) || this.canNeighborBurn(world, x, y, z);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		if(!world.isBlockNormalCube(x, y - 1, z) && !this.canNeighborBurn(world, x, y, z)) {
			world.setBlockWithNotify(x, y, z, 0);
		}
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		if(!world.isBlockNormalCube(x, y - 1, z) && !this.canNeighborBurn(world, x, y, z)) {
			world.setBlockWithNotify(x, y, z, 0);
		} else {
			world.scheduleBlockUpdate(x, y, z, this.blockID);
		}
	}
}
