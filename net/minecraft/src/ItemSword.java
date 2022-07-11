package net.minecraft.src;

public class ItemSword extends Item {
	private int weaponDamage;

	public ItemSword(int id, int strength) {
		super(id);
		this.maxStackSize = 1;
		this.maxDamage = 32 << strength;
		if(strength == 3) {
			this.maxDamage *= 4;
		}

		this.weaponDamage = 4 + strength * 2;
	}

	public float getStrVsBlock(ItemStack itemStack, Block block) {
		return 1.5F;
	}

	public void hitEntity(ItemStack itemStack, EntityLiving entityLiving) {
		itemStack.damageItem(1);
	}

	public void onBlockDestroyed(ItemStack itemStack, int id, int x, int y, int z) {
		itemStack.damageItem(2);
	}

	public int getDamageVsEntity(Entity entity) {
		return this.weaponDamage;
	}

	public boolean isFull3D() {
		return true;
	}
}
