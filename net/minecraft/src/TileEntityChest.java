package net.minecraft.src;

public class TileEntityChest extends TileEntity implements IInventory {
	private ItemStack[] chestContents = new ItemStack[36];

	public int getSizeInventory() {
		return 27;
	}

	public ItemStack getStackInSlot(int slot) {
		return this.chestContents[slot];
	}

	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.chestContents[slot] = stack;
		if(stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList var2 = nbttagcompound.getTagList("Items");
		this.chestContents = new ItemStack[this.getSizeInventory()];

		for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
			NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
			int var5 = var4.getByte("Slot") & 255;
			if(var5 >= 0 && var5 < this.chestContents.length) {
				this.chestContents[var5] = new ItemStack(var4);
			}
		}

	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		NBTTagList var2 = new NBTTagList();

		for(int var3 = 0; var3 < this.chestContents.length; ++var3) {
			if(this.chestContents[var3] != null) {
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				this.chestContents[var3].writeToNBT(var4);
				var2.setTag(var4);
			}
		}

		nbttagcompound.setTag("Items", var2);
	}

	public int getInventoryStackLimit() {
		return 64;
	}
}
