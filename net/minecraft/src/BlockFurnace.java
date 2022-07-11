package net.minecraft.src;

import java.util.Random;

public class BlockFurnace extends BlockContainer {
	private final boolean isActive;

	protected BlockFurnace(int blockID, boolean isActive) {
		super(blockID, Material.rock);
		this.isActive = isActive;
		this.blockIndexInTexture = 45;
	}

	public int idDropped(int metadata, Random rand) {
		return Block.stoneOvenIdle.blockID;
	}

	public void onBlockAdded(World worldObj, int x, int y, int z) {
		super.onBlockAdded(worldObj, x, y, z);
		this.setDefaultDirection(worldObj, x, y, z);
	}

	private void setDefaultDirection(World worldObj, int x, int y, int z) {
		int var5 = worldObj.getBlockId(x, y, z - 1);
		int var6 = worldObj.getBlockId(x, y, z + 1);
		int var7 = worldObj.getBlockId(x - 1, y, z);
		int var8 = worldObj.getBlockId(x + 1, y, z);
		byte var9 = 3;
		if(Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6]) {
			var9 = 3;
		}

		if(Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5]) {
			var9 = 2;
		}

		if(Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8]) {
			var9 = 5;
		}

		if(Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7]) {
			var9 = 4;
		}

		worldObj.setBlockMetadataWithNotify(x, y, z, var9);
	}

	public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		if(side == 1) {
			return Block.stone.blockIndexInTexture;
		} else if(side == 0) {
			return Block.stone.blockIndexInTexture;
		} else {
			int var6 = blockAccess.getBlockMetadata(x, y, z);
			return side != var6 ? this.blockIndexInTexture : (this.isActive ? this.blockIndexInTexture + 16 : this.blockIndexInTexture - 1);
		}
	}

	public void randomDisplayTick(World worldObj, int x, int y, int z, Random rand) {
		if(this.isActive) {
			int var6 = worldObj.getBlockMetadata(x, y, z);
			float var7 = (float)x + 0.5F;
			float var8 = (float)y + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
			float var9 = (float)z + 0.5F;
			float var10 = 0.52F;
			float var11 = rand.nextFloat() * 0.6F - 0.3F;
			if(var6 == 4) {
				worldObj.spawnParticle("smoke", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
				worldObj.spawnParticle("flame", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
			} else if(var6 == 5) {
				worldObj.spawnParticle("smoke", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
				worldObj.spawnParticle("flame", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
			} else if(var6 == 2) {
				worldObj.spawnParticle("smoke", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
				worldObj.spawnParticle("flame", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
			} else if(var6 == 3) {
				worldObj.spawnParticle("smoke", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
				worldObj.spawnParticle("flame", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
			}

		}
	}

	public int getBlockTextureFromSide(int side) {
		return side == 1 ? Block.stone.blockID : (side == 0 ? Block.stone.blockID : (side == 3 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture));
	}

	public boolean blockActivated(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		TileEntityFurnace var6 = (TileEntityFurnace)worldObj.getBlockTileEntity(x, y, z);
		entityPlayer.displayGUIFurnace(var6);
		return true;
	}

	public static void updateFurnaceBlockState(boolean isActive, World worldObj, int x, int y, int z) {
		int var5 = worldObj.getBlockMetadata(x, y, z);
		TileEntity var6 = worldObj.getBlockTileEntity(x, y, z);
		if(isActive) {
			worldObj.setBlockWithNotify(x, y, z, Block.stoneOvenActive.blockID);
		} else {
			worldObj.setBlockWithNotify(x, y, z, Block.stoneOvenIdle.blockID);
		}

		worldObj.setBlockMetadataWithNotify(x, y, z, var5);
		worldObj.setBlockTileEntity(x, y, z, var6);
	}

	protected TileEntity getBlockEntity() {
		return new TileEntityFurnace();
	}
}
