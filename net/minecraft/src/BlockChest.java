package net.minecraft.src;

import java.util.Random;

public class BlockChest extends BlockContainer {
	private Random random = new Random();

	protected BlockChest(int id) {
		super(id, Material.wood);
		this.blockIndexInTexture = 26;
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

	private boolean isThereANeighborChest(World world, int x, int y, int z) {
		return world.getBlockId(x, y, z) != this.blockID ? false : (world.getBlockId(x - 1, y, z) == this.blockID ? true : (world.getBlockId(x + 1, y, z) == this.blockID ? true : (world.getBlockId(x, y, z - 1) == this.blockID ? true : world.getBlockId(x, y, z + 1) == this.blockID)));
	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		TileEntityChest var5 = (TileEntityChest)world.getBlockTileEntity(x, y, z);

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
					EntityItem var12 = new EntityItem(world, (double)((float)x + var8), (double)((float)y + var9), (double)((float)z + var10), new ItemStack(var7.itemID, var11, var7.itemDmg));
					float var13 = 0.05F;
					var12.motionX = (double)((float)this.random.nextGaussian() * var13);
					var12.motionY = (double)((float)this.random.nextGaussian() * var13 + 0.2F);
					var12.motionZ = (double)((float)this.random.nextGaussian() * var13);
					world.spawnEntityInWorld(var12);
				}
			}
		}

		super.onBlockRemoval(world, x, y, z);
	}

	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		Object var6 = (TileEntityChest)world.getBlockTileEntity(x, y, z);
		if(world.isBlockNormalCube(x, y + 1, z)) {
			return true;
		} else if(world.getBlockId(x - 1, y, z) == this.blockID && world.isBlockNormalCube(x - 1, y + 1, z)) {
			return true;
		} else if(world.getBlockId(x + 1, y, z) == this.blockID && world.isBlockNormalCube(x + 1, y + 1, z)) {
			return true;
		} else if(world.getBlockId(x, y, z - 1) == this.blockID && world.isBlockNormalCube(x, y + 1, z - 1)) {
			return true;
		} else if(world.getBlockId(x, y, z + 1) == this.blockID && world.isBlockNormalCube(x, y + 1, z + 1)) {
			return true;
		} else {
			if(world.getBlockId(x - 1, y, z) == this.blockID) {
				var6 = new InventoryLargeChest("Large chest", (TileEntityChest)world.getBlockTileEntity(x - 1, y, z), (IInventory)var6);
			}

			if(world.getBlockId(x + 1, y, z) == this.blockID) {
				var6 = new InventoryLargeChest("Large chest", (IInventory)var6, (TileEntityChest)world.getBlockTileEntity(x + 1, y, z));
			}

			if(world.getBlockId(x, y, z - 1) == this.blockID) {
				var6 = new InventoryLargeChest("Large chest", (TileEntityChest)world.getBlockTileEntity(x, y, z - 1), (IInventory)var6);
			}

			if(world.getBlockId(x, y, z + 1) == this.blockID) {
				var6 = new InventoryLargeChest("Large chest", (IInventory)var6, (TileEntityChest)world.getBlockTileEntity(x, y, z + 1));
			}

			entityPlayer.displayGUIChest((IInventory)var6);
			return true;
		}
	}

	protected TileEntity getBlockEntity() {
		return new TileEntityChest();
	}
}
