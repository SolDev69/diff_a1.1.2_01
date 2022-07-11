package net.minecraft.src;

import java.util.Random;

public class BlockDoor extends Block {
	protected BlockDoor(int var1, Material var2) {
		super(var1, var2);
		this.blockIndexInTexture = 97;
		if(var2 == Material.iron) {
			++this.blockIndexInTexture;
		}

		float var3 = 0.5F;
		float var4 = 1.0F;
		this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var4, 0.5F + var3);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 7;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		this.setDoorRotation(this.getState(blockAccess.getBlockMetadata(x, y, z)));
	}

	public void setDoorRotation(int metadata) {
		float var2 = 0.1875F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
		if(metadata == 0) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
		}

		if(metadata == 1) {
			this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}

		if(metadata == 2) {
			this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
		}

		if(metadata == 3) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
		}

	}

	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		this.blockActivated(world, x, y, z, entityPlayer);
	}

	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		if(this.material == Material.iron) {
			return true;
		} else {
			int var6 = world.getBlockMetadata(x, y, z);
			if((var6 & 8) != 0) {
				if(world.getBlockId(x, y - 1, z) == this.blockID) {
					this.blockActivated(world, x, y - 1, z, entityPlayer);
				}

				return true;
			} else {
				if(world.getBlockId(x, y + 1, z) == this.blockID) {
					world.setBlockMetadataWithNotify(x, y + 1, z, (var6 ^ 4) + 8);
				}

				world.setBlockMetadataWithNotify(x, y, z, var6 ^ 4);
				world.markBlocksDirty(x, y - 1, z, x, y, z);
				if(Math.random() < 0.5D) {
					world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_open", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
				} else {
					world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_close", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
				}

				return true;
			}
		}
	}

	public void onPoweredBlockChange(World world, int x, int y, int z, boolean flag) {
		int var6 = world.getBlockMetadata(x, y, z);
		if((var6 & 8) != 0) {
			if(world.getBlockId(x, y - 1, z) == this.blockID) {
				this.onPoweredBlockChange(world, x, y - 1, z, flag);
			}

		} else {
			boolean var7 = (world.getBlockMetadata(x, y, z) & 4) > 0;
			if(var7 != flag) {
				if(world.getBlockId(x, y + 1, z) == this.blockID) {
					world.setBlockMetadataWithNotify(x, y + 1, z, (var6 ^ 4) + 8);
				}

				world.setBlockMetadataWithNotify(x, y, z, var6 ^ 4);
				world.markBlocksDirty(x, y - 1, z, x, y, z);
				if(Math.random() < 0.5D) {
					world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_open", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
				} else {
					world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_close", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
				}

			}
		}
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		int var6 = world.getBlockMetadata(x, y, z);
		if((var6 & 8) != 0) {
			if(world.getBlockId(x, y - 1, z) != this.blockID) {
				world.setBlockWithNotify(x, y, z, 0);
			}

			if(flag > 0 && Block.blocksList[flag].canProvidePower()) {
				this.onNeighborBlockChange(world, x, y - 1, z, flag);
			}
		} else {
			boolean var7 = false;
			if(world.getBlockId(x, y + 1, z) != this.blockID) {
				world.setBlockWithNotify(x, y, z, 0);
				var7 = true;
			}

			if(!world.isBlockNormalCube(x, y - 1, z)) {
				world.setBlockWithNotify(x, y, z, 0);
				var7 = true;
				if(world.getBlockId(x, y + 1, z) == this.blockID) {
					world.setBlockWithNotify(x, y + 1, z, 0);
				}
			}

			if(var7) {
				this.dropBlockAsItem(world, x, y, z, var6);
			} else if(flag > 0 && Block.blocksList[flag].canProvidePower()) {
				boolean var8 = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);
				this.onPoweredBlockChange(world, x, y, z, var8);
			}
		}

	}

	public int idDropped(int count, Random random) {
		return (count & 8) != 0 ? 0 : (this.material == Material.iron ? Item.doorSteel.shiftedIndex : Item.doorWood.shiftedIndex);
	}

	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3D vector1, Vec3D vector2) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.collisionRayTrace(world, x, y, z, vector1, vector2);
	}

	public int getState(int flag) {
		return (flag & 4) == 0 ? flag - 1 & 3 : flag & 3;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return y >= 127 ? false : world.isBlockNormalCube(x, y - 1, z) && super.canPlaceBlockAt(world, x, y, z) && super.canPlaceBlockAt(world, x, y + 1, z);
	}
}
