package net.minecraft.src;

import java.util.Random;

public class BlockChest extends BlockContainer {
	private Random random = new Random();

	protected BlockChest(int id) {
		super(id, Material.wood);
		this.blockIndexInTexture = 26;
	}

	public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		if(side == 1) {
			return this.blockIndexInTexture - 1;
		} else if(side == 0) {
			return this.blockIndexInTexture - 1;
		} else {
			int var6 = blockAccess.getBlockId(x, y, z - 1);
			int var7 = blockAccess.getBlockId(x, y, z + 1);
			int var8 = blockAccess.getBlockId(x - 1, y, z);
			int var9 = blockAccess.getBlockId(x + 1, y, z);
			int var10;
			int var11;
			int var12;
			byte var13;
			if(var6 != this.blockID && var7 != this.blockID) {
				if(var8 != this.blockID && var9 != this.blockID) {
					byte var14 = 3;
					if(Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var7]) {
						var14 = 3;
					}

					if(Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var6]) {
						var14 = 2;
					}

					if(Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var9]) {
						var14 = 5;
					}

					if(Block.opaqueCubeLookup[var9] && !Block.opaqueCubeLookup[var8]) {
						var14 = 4;
					}

					return side == var14 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
				} else if(side != 4 && side != 5) {
					var10 = 0;
					if(var8 == this.blockID) {
						var10 = -1;
					}

					var11 = blockAccess.getBlockId(var8 == this.blockID ? x - 1 : x + 1, y, z - 1);
					var12 = blockAccess.getBlockId(var8 == this.blockID ? x - 1 : x + 1, y, z + 1);
					if(side == 3) {
						var10 = -1 - var10;
					}

					var13 = 3;
					if((Block.opaqueCubeLookup[var6] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var12]) {
						var13 = 3;
					}

					if((Block.opaqueCubeLookup[var7] || Block.opaqueCubeLookup[var12]) && !Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var11]) {
						var13 = 2;
					}

					return (side == var13 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + var10;
				} else {
					return this.blockIndexInTexture;
				}
			} else if(side != 2 && side != 3) {
				var10 = 0;
				if(var6 == this.blockID) {
					var10 = -1;
				}

				var11 = blockAccess.getBlockId(x - 1, y, var6 == this.blockID ? z - 1 : z + 1);
				var12 = blockAccess.getBlockId(x + 1, y, var6 == this.blockID ? z - 1 : z + 1);
				if(side == 4) {
					var10 = -1 - var10;
				}

				var13 = 5;
				if((Block.opaqueCubeLookup[var8] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var9] && !Block.opaqueCubeLookup[var12]) {
					var13 = 5;
				}

				if((Block.opaqueCubeLookup[var9] || Block.opaqueCubeLookup[var12]) && !Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var11]) {
					var13 = 4;
				}

				return (side == var13 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + var10;
			} else {
				return this.blockIndexInTexture;
			}
		}
	}

	public int getBlockTextureFromSide(int side) {
		return side == 1 ? this.blockIndexInTexture - 1 : (side == 0 ? this.blockIndexInTexture - 1 : (side == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture));
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		int var5 = 0;
		if(world.getBlockId(x - 1, y, z) == this.blockID) {
			++var5;
		}

		if(world.getBlockId(x + 1, y, z) == this.blockID) {
			++var5;
		}

		if(world.getBlockId(x, y, z - 1) == this.blockID) {
			++var5;
		}

		if(world.getBlockId(x, y, z + 1) == this.blockID) {
			++var5;
		}

		return var5 > 1 ? false : (this.isThereANeighborChest(world, x - 1, y, z) ? false : (this.isThereANeighborChest(world, x + 1, y, z) ? false : (this.isThereANeighborChest(world, x, y, z - 1) ? false : !this.isThereANeighborChest(world, x, y, z + 1))));
	}

	private boolean isThereANeighborChest(World worldObj, int x, int y, int z) {
		return worldObj.getBlockId(x, y, z) != this.blockID ? false : (worldObj.getBlockId(x - 1, y, z) == this.blockID ? true : (worldObj.getBlockId(x + 1, y, z) == this.blockID ? true : (worldObj.getBlockId(x, y, z - 1) == this.blockID ? true : worldObj.getBlockId(x, y, z + 1) == this.blockID)));
	}

	public void onBlockRemoval(World worldObj, int x, int y, int z) {
		TileEntityChest var5 = (TileEntityChest)worldObj.getBlockTileEntity(x, y, z);

		for(int var6 = 0; var6 < var5.getSizeInventory(); ++var6) {
			ItemStack var7 = var5.getStackInSlot(var6);
			if(var7 != null) {
				float var8 = this.random.nextFloat() * 0.8F + 0.1F;
				float var9 = this.random.nextFloat() * 0.8F + 0.1F;
				float var10 = this.random.nextFloat() * 0.8F + 0.1F;

				while(var7.stackSize > 0) {
					int var11 = this.random.nextInt(21) + 10;
					if(var11 > var7.stackSize) {
						var11 = var7.stackSize;
					}

					var7.stackSize -= var11;
					EntityItem var12 = new EntityItem(worldObj, (double)((float)x + var8), (double)((float)y + var9), (double)((float)z + var10), new ItemStack(var7.itemID, var11, var7.itemDmg));
					float var13 = 0.05F;
					var12.motionX = (double)((float)this.random.nextGaussian() * var13);
					var12.motionY = (double)((float)this.random.nextGaussian() * var13 + 0.2F);
					var12.motionZ = (double)((float)this.random.nextGaussian() * var13);
					worldObj.spawnEntityInWorld(var12);
				}
			}
		}

		super.onBlockRemoval(worldObj, x, y, z);
	}

	public boolean blockActivated(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		Object var6 = (TileEntityChest)worldObj.getBlockTileEntity(x, y, z);
		if(worldObj.isBlockNormalCube(x, y + 1, z)) {
			return true;
		} else if(worldObj.getBlockId(x - 1, y, z) == this.blockID && worldObj.isBlockNormalCube(x - 1, y + 1, z)) {
			return true;
		} else if(worldObj.getBlockId(x + 1, y, z) == this.blockID && worldObj.isBlockNormalCube(x + 1, y + 1, z)) {
			return true;
		} else if(worldObj.getBlockId(x, y, z - 1) == this.blockID && worldObj.isBlockNormalCube(x, y + 1, z - 1)) {
			return true;
		} else if(worldObj.getBlockId(x, y, z + 1) == this.blockID && worldObj.isBlockNormalCube(x, y + 1, z + 1)) {
			return true;
		} else {
			if(worldObj.getBlockId(x - 1, y, z) == this.blockID) {
				var6 = new InventoryLargeChest("Large chest", (TileEntityChest)worldObj.getBlockTileEntity(x - 1, y, z), (IInventory)var6);
			}

			if(worldObj.getBlockId(x + 1, y, z) == this.blockID) {
				var6 = new InventoryLargeChest("Large chest", (IInventory)var6, (TileEntityChest)worldObj.getBlockTileEntity(x + 1, y, z));
			}

			if(worldObj.getBlockId(x, y, z - 1) == this.blockID) {
				var6 = new InventoryLargeChest("Large chest", (TileEntityChest)worldObj.getBlockTileEntity(x, y, z - 1), (IInventory)var6);
			}

			if(worldObj.getBlockId(x, y, z + 1) == this.blockID) {
				var6 = new InventoryLargeChest("Large chest", (IInventory)var6, (TileEntityChest)worldObj.getBlockTileEntity(x, y, z + 1));
			}

			entityPlayer.displayGUIChest((IInventory)var6);
			return true;
		}
	}

	protected TileEntity getBlockEntity() {
		return new TileEntityChest();
	}
}
