package net.minecraft.src;

public class InventoryCrafting implements IInventory {
	private ItemStack[] stackList;
	private int gridSize;
	private CraftingInventoryCB craftingInventory;

	public InventoryCrafting(CraftingInventoryCB var1, int var2, int var3) {
		this.gridSize = var2 * var3;
		this.stackList = new ItemStack[this.gridSize];
		this.craftingInventory = var1;
	}

	public InventoryCrafting(CraftingInventoryCB var1, ItemStack[] var2) {
		this.gridSize = var2.length;
		this.stackList = var2;
		this.craftingInventory = var1;
	}

	public int getSizeInventory() {
		return this.gridSize;
	}

	public ItemStack getStackInSlot(int slot) {
		return this.stackList[slot];
	}

	public String getInvName() {
		return "Crafting";
	}

	public ItemStack decrStackSize(int slot, int stackSize) {
		if(this.stackList[slot] != null) {
			ItemStack var3;
			if(this.stackList[slot].stackSize <= stackSize) {
				var3 = this.stackList[slot];
				this.stackList[slot] = null;
				this.craftingInventory.onCraftMatrixChanged(this);
				return var3;
			} else {
				var3 = this.stackList[slot].splitStack(stackSize);
				if(this.stackList[slot].stackSize == 0) {
					this.stackList[slot] = null;
				}

				this.craftingInventory.onCraftMatrixChanged(this);
				return var3;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		this.stackList[slot] = itemStack;
		this.craftingInventory.onCraftMatrixChanged(this);
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public void onInventoryChanged() {
	}
}
