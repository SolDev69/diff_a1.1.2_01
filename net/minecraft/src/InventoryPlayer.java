package net.minecraft.src;

public class InventoryPlayer implements IInventory {
	public ItemStack[] mainInventory = new ItemStack[37];
	public ItemStack[] armorInventory = new ItemStack[4];
	public ItemStack[] craftingInventory = new ItemStack[4];
	public int currentItem = 0;
	private EntityPlayer player;
	public boolean inventoryChanged = false;

	public InventoryPlayer(EntityPlayer entityPlayer) {
		this.player = entityPlayer;
	}

	public ItemStack getCurrentItem() {
		return this.mainInventory[this.currentItem];
	}

	private int storeItemStack(int itemID) {
		for(int var2 = 0; var2 < this.mainInventory.length; ++var2) {
			if(this.mainInventory[var2] != null && this.mainInventory[var2].itemID == itemID && this.mainInventory[var2].stackSize < this.mainInventory[var2].getMaxStackSize() && this.mainInventory[var2].stackSize < this.getInventoryStackLimit()) {
				return var2;
			}
		}

		return -1;
	}

	private int getFirstEmptyStack() {
		for(int var1 = 0; var1 < this.mainInventory.length; ++var1) {
			if(this.mainInventory[var1] == null) {
				return var1;
			}
		}

		return -1;
	}

	private int storePartialItemStack(int itemID, int var2) {
		int var3 = this.storeItemStack(itemID);
		if(var3 < 0) {
			var3 = this.getFirstEmptyStack();
		}

		if(var3 < 0) {
			return var2;
		} else {
			if(this.mainInventory[var3] == null) {
				this.mainInventory[var3] = new ItemStack(itemID, 0);
			}

			int var4 = var2;
			if(var2 > this.mainInventory[var3].getMaxStackSize() - this.mainInventory[var3].stackSize) {
				var4 = this.mainInventory[var3].getMaxStackSize() - this.mainInventory[var3].stackSize;
			}

			if(var4 > this.getInventoryStackLimit() - this.mainInventory[var3].stackSize) {
				var4 = this.getInventoryStackLimit() - this.mainInventory[var3].stackSize;
			}

			if(var4 == 0) {
				return var2;
			} else {
				var2 -= var4;
				this.mainInventory[var3].stackSize += var4;
				this.mainInventory[var3].animationsToGo = 5;
				return var2;
			}
		}
	}

	public void decrementAnimations() {
		for(int var1 = 0; var1 < this.mainInventory.length; ++var1) {
			if(this.mainInventory[var1] != null && this.mainInventory[var1].animationsToGo > 0) {
				--this.mainInventory[var1].animationsToGo;
			}
		}

	}

	public boolean addItemStackToInventory(ItemStack stack) {
		if(stack.itemDmg == 0) {
			stack.stackSize = this.storePartialItemStack(stack.itemID, stack.stackSize);
			if(stack.stackSize == 0) {
				return true;
			}
		}

		int var2 = this.getFirstEmptyStack();
		if(var2 >= 0) {
			this.mainInventory[var2] = stack;
			this.mainInventory[var2].animationsToGo = 5;
			return true;
		} else {
			return false;
		}
	}

	public void setInventorySlotContents(int slots, ItemStack stack) {
		ItemStack[] var3 = this.mainInventory;
		if(slots >= var3.length) {
			slots -= var3.length;
			var3 = this.armorInventory;
		}

		if(slots >= var3.length) {
			slots -= var3.length;
			var3 = this.craftingInventory;
		}

		var3[slots] = stack;
	}

	public float getStrVsBlock(Block block) {
		float var2 = 1.0F;
		if(this.mainInventory[this.currentItem] != null) {
			var2 *= this.mainInventory[this.currentItem].getStrVsBlock(block);
		}

		return var2;
	}

	public NBTTagList writeToNBT(NBTTagList nbttaglist) {
		int var2;
		NBTTagCompound var3;
		for(var2 = 0; var2 < this.mainInventory.length; ++var2) {
			if(this.mainInventory[var2] != null) {
				var3 = new NBTTagCompound();
				var3.setByte("Slot", (byte)var2);
				this.mainInventory[var2].writeToNBT(var3);
				nbttaglist.setTag(var3);
			}
		}

		for(var2 = 0; var2 < this.armorInventory.length; ++var2) {
			if(this.armorInventory[var2] != null) {
				var3 = new NBTTagCompound();
				var3.setByte("Slot", (byte)(var2 + 100));
				this.armorInventory[var2].writeToNBT(var3);
				nbttaglist.setTag(var3);
			}
		}

		for(var2 = 0; var2 < this.craftingInventory.length; ++var2) {
			if(this.craftingInventory[var2] != null) {
				var3 = new NBTTagCompound();
				var3.setByte("Slot", (byte)(var2 + 80));
				this.craftingInventory[var2].writeToNBT(var3);
				nbttaglist.setTag(var3);
			}
		}

		return nbttaglist;
	}

	public void readFromNBT(NBTTagList nbttaglist) {
		this.mainInventory = new ItemStack[36];
		this.armorInventory = new ItemStack[4];
		this.craftingInventory = new ItemStack[4];

		for(int var2 = 0; var2 < nbttaglist.tagCount(); ++var2) {
			NBTTagCompound var3 = (NBTTagCompound)nbttaglist.tagAt(var2);
			int var4 = var3.getByte("Slot") & 255;
			if(var4 >= 0 && var4 < this.mainInventory.length) {
				this.mainInventory[var4] = new ItemStack(var3);
			}

			if(var4 >= 80 && var4 < this.craftingInventory.length + 80) {
				this.craftingInventory[var4 - 80] = new ItemStack(var3);
			}

			if(var4 >= 100 && var4 < this.armorInventory.length + 100) {
				this.armorInventory[var4 - 100] = new ItemStack(var3);
			}
		}

	}

	public int getSizeInventory() {
		return this.mainInventory.length + 4;
	}

	public ItemStack getStackInSlot(int slot) {
		ItemStack[] var2 = this.mainInventory;
		if(slot >= var2.length) {
			slot -= var2.length;
			var2 = this.armorInventory;
		}

		if(slot >= var2.length) {
			slot -= var2.length;
			var2 = this.craftingInventory;
		}

		return var2[slot];
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean canHarvestBlock(Block block) {
		if(block.material != Material.rock && block.material != Material.iron && block.material != Material.craftedSnow && block.material != Material.snow) {
			return true;
		} else {
			ItemStack var2 = this.getStackInSlot(this.currentItem);
			return var2 != null ? var2.canHarvestBlock(block) : false;
		}
	}

	public int getTotalArmorValue() {
		int var1 = 0;
		int var2 = 0;
		int var3 = 0;

		for(int var4 = 0; var4 < this.armorInventory.length; ++var4) {
			if(this.armorInventory[var4] != null && this.armorInventory[var4].getItem() instanceof ItemArmor) {
				int var5 = this.armorInventory[var4].getMaxDamage();
				int var6 = this.armorInventory[var4].itemDmg;
				int var7 = var5 - var6;
				var2 += var7;
				var3 += var5;
				int var8 = ((ItemArmor)this.armorInventory[var4].getItem()).damageReduceAmount;
				var1 += var8;
			}
		}

		if(var3 == 0) {
			return 0;
		} else {
			return (var1 - 1) * var2 / var3 + 1;
		}
	}

	public void damageArmor(int damage) {
		for(int var2 = 0; var2 < this.armorInventory.length; ++var2) {
			if(this.armorInventory[var2] != null && this.armorInventory[var2].getItem() instanceof ItemArmor) {
				this.armorInventory[var2].damageItem(damage);
				if(this.armorInventory[var2].stackSize == 0) {
					this.armorInventory[var2].onItemDestroyedByUse(this.player);
					this.armorInventory[var2] = null;
				}
			}
		}

	}

	public void dropAllItems() {
		int var1;
		for(var1 = 0; var1 < this.mainInventory.length; ++var1) {
			if(this.mainInventory[var1] != null) {
				this.player.dropPlayerItemWithRandomChoice(this.mainInventory[var1], true);
				this.mainInventory[var1] = null;
			}
		}

		for(var1 = 0; var1 < this.armorInventory.length; ++var1) {
			if(this.armorInventory[var1] != null) {
				this.player.dropPlayerItemWithRandomChoice(this.armorInventory[var1], true);
				this.armorInventory[var1] = null;
			}
		}

	}
}
