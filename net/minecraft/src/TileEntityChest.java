package net.minecraft.src;

public class TileEntityChest extends TileEntity implements IInventory {
	private ItemStack[] chestContents = new ItemStack[36];

	public int getSizeInventory() {
		return 27;
	}

	public ItemStack getStackInSlot(int slot) {
		return this.chestContents[slot];
	}

	public ItemStack decrStackSize(int slot, int stackSize) {
		if(this.chestContents[slot] != null) {
			ItemStack var3;
			if(this.chestContents[slot].stackSize <= stackSize) {
				var3 = this.chestContents[slot];
				this.chestContents[slot] = null;
				this.onInventoryChanged();
				return var3;
			} else {
				var3 = this.chestContents[slot].splitStack(stackSize);
				if(this.chestContents[slot].stackSize == 0) {
					this.chestContents[slot] = null;
				}

				this.onInventoryChanged();
				return var3;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		this.chestContents[slot] = itemStack;
		if(itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	public String getInvName() {
		return "Chest";
	}

	public void readFromNBT(NBTTagCompound compoundTag) {
		super.readFromNBT(compoundTag);
		NBTTagList var2 = compoundTag.getTagList("Items");
		this.chestContents = new ItemStack[this.getSizeInventory()];

		for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
			NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
			int var5 = var4.getByte("Slot") & 255;
			if(var5 >= 0 && var5 < this.chestContents.length) {
				this.chestContents[var5] = new ItemStack(var4);
			}
		}

	}

	public void writeToNBT(NBTTagCompound compoundTag) {
		super.writeToNBT(compoundTag);
		NBTTagList var2 = new NBTTagList();

		for(int var3 = 0; var3 < this.chestContents.length; ++var3) {
			if(this.chestContents[var3] != null) {
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				this.chestContents[var3].writeToNBT(var4);
				var2.setTag(var4);
			}
		}

		compoundTag.setTag("Items", var2);
	}

	public int getInventoryStackLimit() {
		return 64;
	}
}
