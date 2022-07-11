package net.minecraft.src;

public class TileEntityFurnace extends TileEntity implements IInventory {
	private ItemStack[] furnaceItemStacks = new ItemStack[3];
	private int furnaceBurnTime = 0;
	private int currentItemBurnTime = 0;
	private int furnaceCookTime = 0;

	public int getSizeInventory() {
		return this.furnaceItemStacks.length;
	}

	public ItemStack getStackInSlot(int slot) {
		return this.furnaceItemStacks[slot];
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList var2 = nbttagcompound.getTagList("Items");
		this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

		for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
			NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");
			if(var5 >= 0 && var5 < this.furnaceItemStacks.length) {
				this.furnaceItemStacks[var5] = new ItemStack(var4);
			}
		}

		this.furnaceBurnTime = nbttagcompound.getShort("BurnTime");
		this.furnaceCookTime = nbttagcompound.getShort("CookTime");
		this.currentItemBurnTime = this.getItemBurnTime(this.furnaceItemStacks[1]);
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setShort("BurnTime", (short)this.furnaceBurnTime);
		nbttagcompound.setShort("CookTime", (short)this.furnaceCookTime);
		NBTTagList var2 = new NBTTagList();

		for(int var3 = 0; var3 < this.furnaceItemStacks.length; ++var3) {
			if(this.furnaceItemStacks[var3] != null) {
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				this.furnaceItemStacks[var3].writeToNBT(var4);
				var2.setTag(var4);
			}
		}

		nbttagcompound.setTag("Items", var2);
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isBurning() {
		return this.furnaceBurnTime > 0;
	}

	public void updateEntity() {
		boolean var1 = this.furnaceBurnTime > 0;
		boolean var2 = false;
		if(this.furnaceBurnTime > 0) {
			--this.furnaceBurnTime;
		}

		if(!this.worldObj.multiplayerWorld) {
			if(this.furnaceBurnTime == 0 && this.canSmelt()) {
				this.currentItemBurnTime = this.furnaceBurnTime = this.getItemBurnTime(this.furnaceItemStacks[1]);
				if(this.furnaceBurnTime > 0) {
					var2 = true;
					if(this.furnaceItemStacks[1] != null) {
						--this.furnaceItemStacks[1].stackSize;
						if(this.furnaceItemStacks[1].stackSize == 0) {
							this.furnaceItemStacks[1] = null;
						}
					}
				}
			}

			if(this.isBurning() && this.canSmelt()) {
				++this.furnaceCookTime;
				if(this.furnaceCookTime == 200) {
					this.furnaceCookTime = 0;
					this.smeltItem();
					var2 = true;
				}
			} else {
				this.furnaceCookTime = 0;
			}

			if(var1 != this.furnaceBurnTime > 0) {
				var2 = true;
				BlockFurnace.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
		}

		if(var2) {
			this.onInventoryChanged();
		}

	}

	private boolean canSmelt() {
		if(this.furnaceItemStacks[0] == null) {
			return false;
		} else {
			int var1 = this.getCookedItem(this.furnaceItemStacks[0].getItem().shiftedIndex);
			return var1 < 0 ? false : (this.furnaceItemStacks[2] == null ? true : (this.furnaceItemStacks[2].itemID != var1 ? false : (this.furnaceItemStacks[2].stackSize < this.getInventoryStackLimit() && this.furnaceItemStacks[2].stackSize < this.furnaceItemStacks[2].getMaxStackSize() ? true : this.furnaceItemStacks[2].stackSize < Item.itemsList[var1].getItemStackLimit())));
		}
	}

	public void smeltItem() {
		if(this.canSmelt()) {
			int var1 = this.getCookedItem(this.furnaceItemStacks[0].getItem().shiftedIndex);
			if(this.furnaceItemStacks[2] == null) {
				this.furnaceItemStacks[2] = new ItemStack(var1, 1);
			} else if(this.furnaceItemStacks[2].itemID == var1) {
				++this.furnaceItemStacks[2].stackSize;
			}

			--this.furnaceItemStacks[0].stackSize;
			if(this.furnaceItemStacks[0].stackSize <= 0) {
				this.furnaceItemStacks[0] = null;
			}

		}
	}

	private int getCookedItem(int blockID) {
		return blockID == Block.oreIron.blockID ? Item.ingotIron.shiftedIndex : (blockID == Block.oreGold.blockID ? Item.ingotGold.shiftedIndex : (blockID == Block.oreDiamond.blockID ? Item.diamond.shiftedIndex : (blockID == Block.sand.blockID ? Block.glass.blockID : (blockID == Item.porkRaw.shiftedIndex ? Item.porkCooked.shiftedIndex : (blockID == Block.cobblestone.blockID ? Block.stone.blockID : (blockID == Item.clay.shiftedIndex ? Item.brick.shiftedIndex : -1))))));
	}

	private int getItemBurnTime(ItemStack stack) {
		if(stack == null) {
			return 0;
		} else {
			int var2 = stack.getItem().shiftedIndex;
			return var2 < 256 && Block.blocksList[var2].material == Material.wood ? 300 : (var2 == Item.stick.shiftedIndex ? 100 : (var2 == Item.coal.shiftedIndex ? 1600 : (var2 == Item.bucketLava.shiftedIndex ? 20000 : 0)));
		}
	}
}
