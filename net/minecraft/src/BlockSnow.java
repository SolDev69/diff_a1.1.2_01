package net.minecraft.src;

import java.util.Random;

public class BlockSnow extends Block {
	protected BlockSnow(int id, int tex) {
		super(id, tex, Material.snow);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setTickOnLoad(true);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		int var5 = world.getBlockId(x, y - 1, z);
		return var5 != 0 && Block.blocksList[var5].isOpaqueCube() ? world.getBlockMaterial(x, y - 1, z).getIsSolid() : false;
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		this.canSnowStay(worldObj, x, y, z);
	}

	private boolean canSnowStay(World worldObj, int x, int y, int z) {
		if(!this.canPlaceBlockAt(worldObj, x, y, z)) {
			this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
			worldObj.setBlockWithNotify(x, y, z, 0);
			return false;
		} else {
			return true;
		}
	}

	public void harvestBlock(World worldObj, int x, int y, int z, int metadata) {
		int var6 = Item.snowball.shiftedIndex;
		float var7 = 0.7F;
		double var8 = (double)(worldObj.rand.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
		double var10 = (double)(worldObj.rand.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
		double var12 = (double)(worldObj.rand.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
		EntityItem var14 = new EntityItem(worldObj, (double)x + var8, (double)y + var10, (double)z + var12, new ItemStack(var6));
		var14.delayBeforeCanPickup = 10;
		worldObj.spawnEntityInWorld(var14);
		worldObj.setBlockWithNotify(x, y, z, 0);
	}

	public int idDropped(int metadata, Random rand) {
		return Item.snowball.shiftedIndex;
	}

	public int quantityDropped(Random rand) {
		return 0;
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		if(worldObj.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 11) {
			this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
			worldObj.setBlockWithNotify(x, y, z, 0);
		}

	}

	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		Material var6 = blockAccess.getBlockMaterial(x, y, z);
		return side == 1 ? true : (var6 == this.material ? false : super.shouldSideBeRendered(blockAccess, x, y, z, side));
	}
}
