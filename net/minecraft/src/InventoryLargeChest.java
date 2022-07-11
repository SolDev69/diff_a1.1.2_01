package net.minecraft.src;

public class InventoryLargeChest implements IInventory {
	private String name;
	private IInventory upperChest;
	private IInventory lowerChest;

	public InventoryLargeChest(String name, IInventory upperChest, IInventory lowerChest) {
		this.name = name;
		this.upperChest = upperChest;
		this.lowerChest = lowerChest;
	}

	public int getSizeInventory() {
		return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
	}

	public ItemStack getStackInSlot(int slot) {
		return slot >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlot(slot - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(slot);
	}
}
