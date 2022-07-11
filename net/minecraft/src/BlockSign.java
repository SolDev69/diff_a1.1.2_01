package net.minecraft.src;

import java.util.Random;

public class BlockSign extends BlockContainer {
	private Class signEntityClass;
	private boolean isFreestanding;

	protected BlockSign(int id, Class signEntityClass, boolean isFreestanding) {
		super(id, Material.wood);
		this.isFreestanding = isFreestanding;
		this.blockIndexInTexture = 4;
		this.signEntityClass = signEntityClass;
		float var4 = 0.25F;
		float var5 = 1.0F;
		this.setBlockBounds(0.5F - var4, 0.0F, 0.5F - var4, 0.5F + var4, var5, 0.5F + var4);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		if(!this.isFreestanding) {
			int var5 = blockAccess.getBlockMetadata(x, y, z);
			float var6 = 0.28125F;
			float var7 = 0.78125F;
			float var8 = 0.0F;
			float var9 = 1.0F;
			float var10 = 0.125F;
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			if(var5 == 2) {
				this.setBlockBounds(var8, var6, 1.0F - var10, var9, var7, 1.0F);
			}

			if(var5 == 3) {
				this.setBlockBounds(var8, var6, 0.0F, var9, var7, var10);
			}

			if(var5 == 4) {
				this.setBlockBounds(1.0F - var10, var6, var8, 1.0F, var7, var9);
			}

			if(var5 == 5) {
				this.setBlockBounds(0.0F, var6, var8, var10, var7, var9);
			}

		}
	}

	public int getRenderType() {
		return -1;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	protected TileEntity getBlockEntity() {
		try {
			return (TileEntity)this.signEntityClass.newInstance();
		} catch (Exception var2) {
			throw new RuntimeException(var2);
		}
	}

	public int idDropped(int count, Random random) {
		return Item.sign.shiftedIndex;
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		boolean var6 = false;
		if(this.isFreestanding) {
			if(!world.getBlockMaterial(x, y - 1, z).isSolid()) {
				var6 = true;
			}
		} else {
			int var7 = world.getBlockMetadata(x, y, z);
			var6 = true;
			if(var7 == 2 && world.getBlockMaterial(x, y, z + 1).isSolid()) {
				var6 = false;
			}

			if(var7 == 3 && world.getBlockMaterial(x, y, z - 1).isSolid()) {
				var6 = false;
			}

			if(var7 == 4 && world.getBlockMaterial(x + 1, y, z).isSolid()) {
				var6 = false;
			}

			if(var7 == 5 && world.getBlockMaterial(x - 1, y, z).isSolid()) {
				var6 = false;
			}
		}

		if(var6) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
		}

		super.onNeighborBlockChange(world, x, y, z, flag);
	}
}
