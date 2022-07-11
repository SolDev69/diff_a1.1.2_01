package net.minecraft.src;

public class ItemPainting extends Item {
	public ItemPainting(int var1) {
		super(var1);
		this.maxDamage = 64;
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World worldObj, int x, int y, int z, int side) {
		if(side == 0) {
			return false;
		} else if(side == 1) {
			return false;
		} else {
			byte var8 = 0;
			if(side == 4) {
				var8 = 1;
			}

			if(side == 3) {
				var8 = 2;
			}

			if(side == 5) {
				var8 = 3;
			}

			EntityPainting var9 = new EntityPainting(worldObj, x, y, z, var8);
			if(var9.onValidSurface()) {
				worldObj.spawnEntityInWorld(var9);
				--itemStack.stackSize;
			}

			return true;
		}
	}
}
