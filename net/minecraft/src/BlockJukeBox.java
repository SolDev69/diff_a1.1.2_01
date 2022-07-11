package net.minecraft.src;

public class BlockJukeBox extends Block {
	protected BlockJukeBox(int id, int blockIndex) {
		super(id, blockIndex, Material.wood);
	}

	public int getBlockTextureFromSide(int side) {
		return this.blockIndexInTexture + (side == 1 ? 1 : 0);
	}

	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		int var6 = world.getBlockMetadata(x, y, z);
		if(var6 > 0) {
			this.ejectRecord(world, x, y, z, var6);
			return true;
		} else {
			return false;
		}
	}

	public void ejectRecord(World world, int x, int y, int z, int var5) {
		world.playRecord((String)null, x, y, z);
		world.setBlockMetadataWithNotify(x, y, z, 0);
		int var6 = Item.record13.shiftedIndex + var5 - 1;
		float var7 = 0.7F;
		double var8 = (double)(world.rand.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
		double var10 = (double)(world.rand.nextFloat() * var7) + (double)(1.0F - var7) * 0.2D + 0.6D;
		double var12 = (double)(world.rand.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
		EntityItem var14 = new EntityItem(world, (double)x + var8, (double)y + var10, (double)z + var12, new ItemStack(var6));
		var14.delayBeforeCanPickup = 10;
		world.spawnEntityInWorld(var14);
	}

	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int var5, float var6) {
		if(!world.multiplayerWorld) {
			if(var5 > 0) {
				this.ejectRecord(world, x, y, z, var5);
			}

			super.dropBlockAsItemWithChance(world, x, y, z, var5, var6);
		}
	}
}
