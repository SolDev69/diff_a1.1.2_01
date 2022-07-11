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

	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		if(side != 0 && side != 1) {
			int var3 = this.getState(metadata);
			if((var3 == 0 || var3 == 2) ^ side <= 3) {
				return this.blockIndexInTexture;
			} else {
				int var4 = var3 / 2 + (side & 1 ^ var3);
				var4 += (metadata & 4) / 4;
				int var5 = this.blockIndexInTexture - (metadata & 8) * 2;
				if((var4 & 1) != 0) {
					var5 = -var5;
				}

				return var5;
			}
		} else {
			return this.blockIndexInTexture;
		}
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 7;
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(worldObj, x, y, z);
		return super.getSelectedBoundingBoxFromPool(worldObj, x, y, z);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(worldObj, x, y, z);
		return super.getCollisionBoundingBoxFromPool(worldObj, x, y, z);
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

	public void onBlockClicked(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		this.blockActivated(worldObj, x, y, z, entityPlayer);
	}

	public boolean blockActivated(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		if(this.material == Material.iron) {
			return true;
		} else {
			int var6 = worldObj.getBlockMetadata(x, y, z);
			if((var6 & 8) != 0) {
				if(worldObj.getBlockId(x, y - 1, z) == this.blockID) {
					this.blockActivated(worldObj, x, y - 1, z, entityPlayer);
				}

				return true;
			} else {
				if(worldObj.getBlockId(x, y + 1, z) == this.blockID) {
					worldObj.setBlockMetadataWithNotify(x, y + 1, z, (var6 ^ 4) + 8);
				}

				worldObj.setBlockMetadataWithNotify(x, y, z, var6 ^ 4);
				worldObj.markBlocksDirty(x, y - 1, z, x, y, z);
				if(Math.random() < 0.5D) {
					worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_open", 1.0F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
				} else {
					worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_close", 1.0F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
				}

				return true;
			}
		}
	}

	public void onPoweredBlockChange(World worldObj, int x, int y, int z, boolean var5) {
		int var6 = worldObj.getBlockMetadata(x, y, z);
		if((var6 & 8) != 0) {
			if(worldObj.getBlockId(x, y - 1, z) == this.blockID) {
				this.onPoweredBlockChange(worldObj, x, y - 1, z, var5);
			}

		} else {
			boolean var7 = (worldObj.getBlockMetadata(x, y, z) & 4) > 0;
			if(var7 != var5) {
				if(worldObj.getBlockId(x, y + 1, z) == this.blockID) {
					worldObj.setBlockMetadataWithNotify(x, y + 1, z, (var6 ^ 4) + 8);
				}

				worldObj.setBlockMetadataWithNotify(x, y, z, var6 ^ 4);
				worldObj.markBlocksDirty(x, y - 1, z, x, y, z);
				if(Math.random() < 0.5D) {
					worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_open", 1.0F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
				} else {
					worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_close", 1.0F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
				}

			}
		}
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		int var6 = worldObj.getBlockMetadata(x, y, z);
		if((var6 & 8) != 0) {
			if(worldObj.getBlockId(x, y - 1, z) != this.blockID) {
				worldObj.setBlockWithNotify(x, y, z, 0);
			}

			if(id > 0 && Block.blocksList[id].canProvidePower()) {
				this.onNeighborBlockChange(worldObj, x, y - 1, z, id);
			}
		} else {
			boolean var7 = false;
			if(worldObj.getBlockId(x, y + 1, z) != this.blockID) {
				worldObj.setBlockWithNotify(x, y, z, 0);
				var7 = true;
			}

			if(!worldObj.isBlockNormalCube(x, y - 1, z)) {
				worldObj.setBlockWithNotify(x, y, z, 0);
				var7 = true;
				if(worldObj.getBlockId(x, y + 1, z) == this.blockID) {
					worldObj.setBlockWithNotify(x, y + 1, z, 0);
				}
			}

			if(var7) {
				this.dropBlockAsItem(worldObj, x, y, z, var6);
			} else if(id > 0 && Block.blocksList[id].canProvidePower()) {
				boolean var8 = worldObj.isBlockIndirectlyGettingPowered(x, y, z) || worldObj.isBlockIndirectlyGettingPowered(x, y + 1, z);
				this.onPoweredBlockChange(worldObj, x, y, z, var8);
			}
		}

	}

	public int idDropped(int metadata, Random rand) {
		return (metadata & 8) != 0 ? 0 : (this.material == Material.iron ? Item.doorSteel.shiftedIndex : Item.doorWood.shiftedIndex);
	}

	public MovingObjectPosition collisionRayTrace(World worldObj, int x, int y, int z, Vec3D vector1, Vec3D vector2) {
		this.setBlockBoundsBasedOnState(worldObj, x, y, z);
		return super.collisionRayTrace(worldObj, x, y, z, vector1, vector2);
	}

	public int getState(int metadata) {
		return (metadata & 4) == 0 ? metadata - 1 & 3 : metadata & 3;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return y >= 127 ? false : world.isBlockNormalCube(x, y - 1, z) && super.canPlaceBlockAt(world, x, y, z) && super.canPlaceBlockAt(world, x, y + 1, z);
	}
}
