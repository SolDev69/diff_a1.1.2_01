package net.minecraft.src;

import java.util.Random;

public abstract class BlockFluid extends Block {
	protected int fluidType = 1;

	protected BlockFluid(int var1, Material var2) {
		super(var1, (var2 == Material.lava ? 14 : 12) * 16 + 13, var2);
		float var3 = 0.0F;
		float var4 = 0.0F;
		if(var2 == Material.lava) {
			this.fluidType = 2;
		}

		this.setBlockBounds(0.0F + var4, 0.0F + var3, 0.0F + var4, 1.0F + var4, 1.0F + var3, 1.0F + var4);
		this.setTickOnLoad(true);
	}

	public static float getFluidHeightPercent(int var0) {
		if(var0 >= 8) {
			var0 = 0;
		}

		float var1 = (float)(var0 + 1) / 9.0F;
		return var1;
	}

	public int getBlockTextureFromSide(int side) {
		return side != 0 && side != 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
	}

	protected int getFlowDecay(World world, int x, int y, int z) {
		return world.getBlockMaterial(x, y, z) != this.material ? -1 : world.getBlockMetadata(x, y, z);
	}

	protected int getEffectiveFlowDecay(IBlockAccess blockAccess, int x, int y, int z) {
		if(blockAccess.getBlockMaterial(x, y, z) != this.material) {
			return -1;
		} else {
			int var5 = blockAccess.getBlockMetadata(x, y, z);
			if(var5 >= 8) {
				var5 = 0;
			}

			return var5;
		}
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean canCollideCheck(int var1, boolean var2) {
		return var2 && var1 == 0;
	}

	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		Material var6 = blockAccess.getBlockMaterial(x, y, z);
		return var6 == this.material ? false : (var6 == Material.ice ? false : (side == 1 ? true : super.shouldSideBeRendered(blockAccess, x, y, z, side)));
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public int getRenderType() {
		return 4;
	}

	public int idDropped(int count, Random random) {
		return 0;
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	private Vec3D getFlowVector(IBlockAccess blockAccess, int x, int y, int z) {
		Vec3D var5 = Vec3D.createVector(0.0D, 0.0D, 0.0D);
		int var6 = this.getEffectiveFlowDecay(blockAccess, x, y, z);

		for(int var7 = 0; var7 < 4; ++var7) {
			int var8 = x;
			int var10 = z;
			if(var7 == 0) {
				var8 = x - 1;
			}

			if(var7 == 1) {
				var10 = z - 1;
			}

			if(var7 == 2) {
				++var8;
			}

			if(var7 == 3) {
				++var10;
			}

			int var11 = this.getEffectiveFlowDecay(blockAccess, var8, y, var10);
			int var12;
			if(var11 < 0) {
				if(!blockAccess.getBlockMaterial(var8, y, var10).getIsSolid()) {
					var11 = this.getEffectiveFlowDecay(blockAccess, var8, y - 1, var10);
					if(var11 >= 0) {
						var12 = var11 - (var6 - 8);
						var5 = var5.addVector((double)((var8 - x) * var12), (double)((y - y) * var12), (double)((var10 - z) * var12));
					}
				}
			} else if(var11 >= 0) {
				var12 = var11 - var6;
				var5 = var5.addVector((double)((var8 - x) * var12), (double)((y - y) * var12), (double)((var10 - z) * var12));
			}
		}

		if(blockAccess.getBlockMetadata(x, y, z) >= 8) {
			boolean var13 = false;
			if(var13 || this.shouldSideBeRendered(blockAccess, x, y, z - 1, 2)) {
				var13 = true;
			}

			if(var13 || this.shouldSideBeRendered(blockAccess, x, y, z + 1, 3)) {
				var13 = true;
			}

			if(var13 || this.shouldSideBeRendered(blockAccess, x - 1, y, z, 4)) {
				var13 = true;
			}

			if(var13 || this.shouldSideBeRendered(blockAccess, x + 1, y, z, 5)) {
				var13 = true;
			}

			if(var13 || this.shouldSideBeRendered(blockAccess, x, y + 1, z - 1, 2)) {
				var13 = true;
			}

			if(var13 || this.shouldSideBeRendered(blockAccess, x, y + 1, z + 1, 3)) {
				var13 = true;
			}

			if(var13 || this.shouldSideBeRendered(blockAccess, x - 1, y + 1, z, 4)) {
				var13 = true;
			}

			if(var13 || this.shouldSideBeRendered(blockAccess, x + 1, y + 1, z, 5)) {
				var13 = true;
			}

			if(var13) {
				var5 = var5.normalize().addVector(0.0D, -6.0D, 0.0D);
			}
		}

		var5 = var5.normalize();
		return var5;
	}

	public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3D vector) {
		Vec3D var7 = this.getFlowVector(world, x, y, z);
		vector.xCoord += var7.xCoord;
		vector.yCoord += var7.yCoord;
		vector.zCoord += var7.zCoord;
	}

	public int tickRate() {
		return this.material == Material.water ? 5 : (this.material == Material.lava ? 30 : 0);
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		super.updateTick(world, x, y, z, random);
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		this.checkForHarden(world, x, y, z);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		this.checkForHarden(world, x, y, z);
	}

	private void checkForHarden(World world, int x, int y, int z) {
		if(world.getBlockId(x, y, z) == this.blockID) {
			if(this.material == Material.lava) {
				boolean var5 = false;
				if(var5 || world.getBlockMaterial(x, y, z - 1) == Material.water) {
					var5 = true;
				}

				if(var5 || world.getBlockMaterial(x, y, z + 1) == Material.water) {
					var5 = true;
				}

				if(var5 || world.getBlockMaterial(x - 1, y, z) == Material.water) {
					var5 = true;
				}

				if(var5 || world.getBlockMaterial(x + 1, y, z) == Material.water) {
					var5 = true;
				}

				if(var5 || world.getBlockMaterial(x, y + 1, z) == Material.water) {
					var5 = true;
				}

				if(var5) {
					int var6 = world.getBlockMetadata(x, y, z);
					if(var6 == 0) {
						world.setBlockWithNotify(x, y, z, Block.obsidian.blockID);
					} else if(var6 <= 4) {
						world.setBlockWithNotify(x, y, z, Block.cobblestone.blockID);
					}

					this.triggerLavaMixEffects(world, x, y, z);
				}
			}

		}
	}

	protected void triggerLavaMixEffects(World world, int x, int y, int z) {
		world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

		for(int var5 = 0; var5 < 8; ++var5) {
			world.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + 1.2D, (double)z + Math.random(), 0.0D, 0.0D, 0.0D);
		}

	}
}
