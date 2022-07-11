package net.minecraft.src;

public class ItemSaddle extends Item {
	public ItemSaddle(int var1) {
		super(var1);
		this.maxStackSize = 1;
		this.maxDamage = 64;
	}

	public void saddleEntity(ItemStack itemStack, EntityLiving entityLiving) {
		if(entityLiving instanceof EntityPig) {
			EntityPig var3 = (EntityPig)entityLiving;
			if(!var3.saddled) {
				var3.saddled = true;
				--itemStack.stackSize;
			}
		}

	}

	public void hitEntity(ItemStack itemStack, EntityLiving entityLiving) {
		this.saddleEntity(itemStack, entityLiving);
	}
}
