package net.minecraft.src;

public class ItemDoor extends Item {
	private Material material;

	public ItemDoor(int id, Material material) {
		super(id);
		this.material = material;
		this.maxDamage = 64;
		this.maxStackSize = 1;
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World worldObj, int x, int y, int z, int side) {
		if(side != 1) {
			return false;
		} else {
			++y;
			Block var8;
			if(this.material == Material.wood) {
				var8 = Block.doorWood;
			} else {
				var8 = Block.doorSteel;
			}

			if(!var8.canPlaceBlockAt(worldObj, x, y, z)) {
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

				int var12 = (worldObj.isBlockNormalCube(x - var10, y, z - var11) ? 1 : 0) + (worldObj.isBlockNormalCube(x - var10, y + 1, z - var11) ? 1 : 0);
				int var13 = (worldObj.isBlockNormalCube(x + var10, y, z + var11) ? 1 : 0) + (worldObj.isBlockNormalCube(x + var10, y + 1, z + var11) ? 1 : 0);
				boolean var14 = worldObj.getBlockId(x - var10, y, z - var11) == var8.blockID || worldObj.getBlockId(x - var10, y + 1, z - var11) == var8.blockID;
				boolean var15 = worldObj.getBlockId(x + var10, y, z + var11) == var8.blockID || worldObj.getBlockId(x + var10, y + 1, z + var11) == var8.blockID;
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

				worldObj.setBlockWithNotify(x, y, z, var8.blockID);
				worldObj.setBlockMetadataWithNotify(x, y, z, var9);
				worldObj.setBlockWithNotify(x, y + 1, z, var8.blockID);
				worldObj.setBlockMetadataWithNotify(x, y + 1, z, var9 + 8);
				--itemStack.stackSize;
				return true;
			}
		}
	}
}
