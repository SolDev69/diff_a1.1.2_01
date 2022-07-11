package net.minecraft.src;

public class ItemPainting extends Item {
	public ItemPainting(int var1) {
		super(var1);
		this.maxDamage = 64;
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer entityPlayer, World world, int x, int y, int z, int var7) {
		if(var7 == 0) {
			return false;
		} else if(var7 == 1) {
			return false;
		} else {
			byte var8 = 0;
			if(var7 == 4) {
				var8 = 1;
			}

			if(var7 == 3) {
				var8 = 2;
			}

			if(var7 == 5) {
				var8 = 3;
			}

			EntityPainting var9 = new EntityPainting(world, x, y, z, var8);
			if(var9.onValidSurface()) {
				world.spawnEntityInWorld(var9);
				--stack.stackSize;
			}

			return true;
		}
	}
}
