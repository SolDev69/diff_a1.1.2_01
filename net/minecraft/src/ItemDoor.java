package net.minecraft.src;

public class ItemDoor extends Item {
	private Material material;

	public ItemDoor(int itemID, Material material) {
		super(itemID);
		this.material = material;
		this.maxDamage = 64;
		this.maxStackSize = 1;
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer entityPlayer, World world, int x, int y, int z, int var7) {
		if(var7 != 1) {
			return false;
		} else {
			++y;
			Block var8;
			if(this.material == Material.wood) {
				var8 = Block.doorWood;
			} else {
				var8 = Block.doorSteel;
			}

			if(!var8.canPlaceBlockAt(world, x, y, z)) {
				return false;
			} else {
				int var9 = MathHelper.floor_double((double)((entityPlayer.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
				byte var10 = 0;
				byte var11 = 0;
				if(var9 == 0) {
					var11 = 1;
				}

				if(var9 == 1) {
					var10 = -1;
				}

				if(var9 == 2) {
					var11 = -1;
				}

				if(var9 == 3) {
					var10 = 1;
				}

				int var12 = (world.isBlockNormalCube(x - var10, y, z - var11) ? 1 : 0) + (world.isBlockNormalCube(x - var10, y + 1, z - var11) ? 1 : 0);
				int var13 = (world.isBlockNormalCube(x + var10, y, z + var11) ? 1 : 0) + (world.isBlockNormalCube(x + var10, y + 1, z + var11) ? 1 : 0);
				boolean var14 = world.getBlockId(x - var10, y, z - var11) == var8.blockID || world.getBlockId(x - var10, y + 1, z - var11) == var8.blockID;
				boolean var15 = world.getBlockId(x + var10, y, z + var11) == var8.blockID || world.getBlockId(x + var10, y + 1, z + var11) == var8.blockID;
				boolean var16 = false;
				if(var14 && !var15) {
					var16 = true;
				} else if(var13 > var12) {
					var16 = true;
				}

				if(var16) {
					var9 = var9 - 1 & 3;
					var9 += 4;
				}

				world.setBlockWithNotify(x, y, z, var8.blockID);
				world.setBlockMetadataWithNotify(x, y, z, var9);
				world.setBlockWithNotify(x, y + 1, z, var8.blockID);
				world.setBlockMetadataWithNotify(x, y + 1, z, var9 + 8);
				--stack.stackSize;
				return true;
			}
		}
	}
}
