package net.minecraft.src;

import java.util.Random;

public class BlockFlowing extends BlockFluid {
	int numAdjacentSources = 0;
	boolean[] isOptimalFlowDirection = new boolean[4];
	int[] flowCost = new int[4];

	protected BlockFlowing(int var1, Material var2) {
		super(var1, var2);
	}

	private void updateFlow(World world, int x, int y, int z) {
		int var5 = world.getBlockMetadata(x, y, z);
		world.setBlockAndMetadata(x, y, z, this.blockID + 1, var5);
		world.markBlocksDirty(x, y, z, x, y, z);
		world.markBlockNeedsUpdate(x, y, z);
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		int var6 = this.getFlowDecay(world, x, y, z);
		boolean var7 = true;
		int var9;
		if(var6 > 0) {
			byte var8 = -100;
			this.numAdjacentSources = 0;
			int var11 = this.getSmallestFlowDecay(world, x - 1, y, z, var8);
			var11 = this.getSmallestFlowDecay(world, x + 1, y, z, var11);
			var11 = this.getSmallestFlowDecay(world, x, y, z - 1, var11);
			var11 = this.getSmallestFlowDecay(world, x, y, z + 1, var11);
			var9 = var11 + this.fluidType;
			if(var9 >= 8 || var11 < 0) {
				var9 = -1;
			}

			if(this.getFlowDecay(world, x, y + 1, z) >= 0) {
				int var10 = this.getFlowDecay(world, x, y + 1, z);
				if(var10 >= 8) {
					var9 = var10;
				} else {
					var9 = var10 + 8;
				}
			}

			if(this.numAdjacentSources >= 2 && this.material == Material.water) {
				if(world.isBlockNormalCube(x, y - 1, z)) {
					var9 = 0;
				} else if(world.getBlockMaterial(x, y - 1, z) == this.material && world.getBlockMetadata(x, y, z) == 0) {
					var9 = 0;
				}
			}

			if(this.material == Material.lava && var6 < 8 && var9 < 8 && var9 > var6 && random.nextInt(4) != 0) {
				var9 = var6;
				var7 = false;
			}

			if(var9 != var6) {
				var6 = var9;
				if(var9 < 0) {
					world.setBlockWithNotify(x, y, z, 0);
				} else {
					world.setBlockMetadataWithNotify(x, y, z, var9);
					world.scheduleBlockUpdate(x, y, z, this.blockID);
					world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
				}
			} else if(var7) {
				this.updateFlow(world, x, y, z);
			}
		} else {
			this.updateFlow(world, x, y, z);
		}

		if(this.liquidCanDisplaceBlock(world, x, y - 1, z)) {
			if(var6 >= 8) {
				world.setBlockAndMetadataWithNotify(x, y - 1, z, this.blockID, var6);
			} else {
				world.setBlockAndMetadataWithNotify(x, y - 1, z, this.blockID, var6 + 8);
			}
		} else if(var6 >= 0 && (var6 == 0 || this.blockBlocksFlow(world, x, y - 1, z))) {
			boolean[] var12 = this.getOptimalFlowDirections(world, x, y, z);
			var9 = var6 + this.fluidType;
			if(var6 >= 8) {
				var9 = 1;
			}

			if(var9 >= 8) {
				return;
			}

			if(var12[0]) {
				this.flowIntoBlock(world, x - 1, y, z, var9);
			}

			if(var12[1]) {
				this.flowIntoBlock(world, x + 1, y, z, var9);
			}

			if(var12[2]) {
				this.flowIntoBlock(world, x, y, z - 1, var9);
			}

			if(var12[3]) {
				this.flowIntoBlock(world, x, y, z + 1, var9);
			}
		}

	}

	private void flowIntoBlock(World world, int x, int y, int z, int metadata) {
		if(this.liquidCanDisplaceBlock(world, x, y, z)) {
			int var6 = world.getBlockId(x, y, z);
			if(var6 > 0) {
				if(this.material == Material.lava) {
					this.triggerLavaMixEffects(world, x, y, z);
				} else {
					Block.blocksList[var6].dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
				}
			}

			world.setBlockAndMetadataWithNotify(x, y, z, this.blockID, metadata);
		}

	}

	private int calculateFlowCost(World world, int x, int y, int z, int var5, int var6) {
		int var7 = 1000;

		for(int var8 = 0; var8 < 4; ++var8) {
			if((var8 != 0 || var6 != 1) && (var8 != 1 || var6 != 0) && (var8 != 2 || var6 != 3) && (var8 != 3 || var6 != 2)) {
				int var9 = x;
				int var11 = z;
				if(var8 == 0) {
					var9 = x - 1;
				}

				if(var8 == 1) {
					++var9;
				}

				if(var8 == 2) {
					var11 = z - 1;
				}

				if(var8 == 3) {
					++var11;
				}

				if(!this.blockBlocksFlow(world, var9, y, var11) && (world.getBlockMaterial(var9, y, var11) != this.material || world.getBlockMetadata(var9, y, var11) != 0)) {
					if(!this.blockBlocksFlow(world, var9, y - 1, var11)) {
						return var5;
					}

					if(var5 < 4) {
						int var12 = this.calculateFlowCost(world, var9, y, var11, var5 + 1, var8);
						if(var12 < var7) {
							var7 = var12;
						}
					}
				}
			}
		}

		return var7;
	}

	private boolean[] getOptimalFlowDirections(World world, int x, int y, int z) {
		int var5;
		int var6;
		for(var5 = 0; var5 < 4; ++var5) {
			this.flowCost[var5] = 1000;
			var6 = x;
			int var8 = z;
			if(var5 == 0) {
				var6 = x - 1;
			}

			if(var5 == 1) {
				++var6;
			}

			if(var5 == 2) {
				var8 = z - 1;
			}

			if(var5 == 3) {
				++var8;
			}

			if(!this.blockBlocksFlow(world, var6, y, var8) && (world.getBlockMaterial(var6, y, var8) != this.material || world.getBlockMetadata(var6, y, var8) != 0)) {
				if(!this.blockBlocksFlow(world, var6, y - 1, var8)) {
					this.flowCost[var5] = 0;
				} else {
					this.flowCost[var5] = this.calculateFlowCost(world, var6, y, var8, 1, var5);
				}
			}
		}

		var5 = this.flowCost[0];

		for(var6 = 1; var6 < 4; ++var6) {
			if(this.flowCost[var6] < var5) {
				var5 = this.flowCost[var6];
			}
		}

		for(var6 = 0; var6 < 4; ++var6) {
			this.isOptimalFlowDirection[var6] = this.flowCost[var6] == var5;
		}

		return this.isOptimalFlowDirection;
	}

	private boolean blockBlocksFlow(World world, int x, int y, int z) {
		int var5 = world.getBlockId(x, y, z);
		if(var5 != Block.doorWood.blockID && var5 != Block.doorSteel.blockID && var5 != Block.signStanding.blockID && var5 != Block.ladder.blockID && var5 != Block.reed.blockID) {
			if(var5 == 0) {
				return false;
			} else {
				Material var6 = Block.blocksList[var5].material;
				return var6.isSolid();
			}
		} else {
			return true;
		}
	}

	protected int getSmallestFlowDecay(World world, int x, int y, int z, int var5) {
		int var6 = this.getFlowDecay(world, x, y, z);
		if(var6 < 0) {
			return var5;
		} else {
			if(var6 == 0) {
				++this.numAdjacentSources;
			}

			if(var6 >= 8) {
				var6 = 0;
			}

			return var5 >= 0 && var6 >= var5 ? var5 : var6;
		}
	}

	private boolean liquidCanDisplaceBlock(World world, int x, int y, int z) {
		Material var5 = world.getBlockMaterial(x, y, z);
		return var5 == this.material ? false : (var5 == Material.lava ? false : !this.blockBlocksFlow(world, x, y, z));
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		if(world.getBlockId(x, y, z) == this.blockID) {
			world.scheduleBlockUpdate(x, y, z, this.blockID);
		}

	}
}
