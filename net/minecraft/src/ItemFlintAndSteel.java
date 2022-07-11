package net.minecraft.src;

public class ItemFlintAndSteel extends Item {
	public ItemFlintAndSteel(int var1) {
		super(var1);
		this.maxStackSize = 1;
		this.maxDamage = 64;
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer entityPlayer, World world, int x, int y, int z, int var7) {
		if(var7 == 0) {
			--y;
		}

		if(var7 == 1) {
			++y;
		}

		if(var7 == 2) {
			--z;
		}

		if(var7 == 3) {
			++z;
		}

		if(var7 == 4) {
			--x;
		}

		if(var7 == 5) {
			++x;
		}

		int var8 = world.getBlockId(x, y, z);
		if(var8 == 0) {
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "fire.ignite", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
			world.setBlockWithNotify(x, y, z, Block.fire.blockID);
		}

		stack.damageItem(1);
		return true;
	}
}
