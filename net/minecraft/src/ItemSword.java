package net.minecraft.src;

public class ItemSword extends Item {
	private int weaponDamage;

	public ItemSword(int itemID, int maxDamage) {
		super(itemID);
		this.maxStackSize = 1;
		this.maxDamage = 32 << maxDamage;
		if(maxDamage == 3) {
			this.maxDamage *= 4;
		}

		this.weaponDamage = 4 + maxDamage * 2;
	}

	public float getStrVsBlock(ItemStack stack, Block block) {
		return 1.5F;
	}

	public void onBlockDestroyed(ItemStack stack, int x, int y, int z, int var5) {
		stack.damageItem(2);
	}
}
