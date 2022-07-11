package net.minecraft.src;

import java.util.Random;

public class BlockFurnace extends BlockContainer {
	private final boolean isActive;

	protected BlockFurnace(int id, boolean isActive) {
		super(id, Material.rock);
		this.isActive = isActive;
		this.blockIndexInTexture = 45;
	}

	public int idDropped(int count, Random random) {
		return Block.stoneOvenIdle.blockID;
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.setDefaultDirection(world, x, y, z);
	}

	private void setDefaultDirection(World world, int x, int y, int z) {
		int var5 = world.getBlockId(x, y, z - 1);
		int var6 = world.getBlockId(x, y, z + 1);
		int var7 = world.getBlockId(x - 1, y, z);
		int var8 = world.getBlockId(x + 1, y, z);
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

		world.setBlockMetadataWithNotify(x, y, z, var9);
	}

	public int getBlockTextureFromSide(int side) {
		return side == 1 ? Block.stone.blockID : (side == 0 ? Block.stone.blockID : (side == 3 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture));
	}

	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		TileEntityFurnace var6 = (TileEntityFurnace)world.getBlockTileEntity(x, y, z);
		entityPlayer.displayGUIFurnace(var6);
		return true;
	}

	public static void updateFurnaceBlockState(boolean isActive, World world, int x, int y, int z) {
		int var5 = world.getBlockMetadata(x, y, z);
		TileEntity var6 = world.getBlockTileEntity(x, y, z);
		if(isActive) {
			world.setBlockWithNotify(x, y, z, Block.stoneOvenActive.blockID);
		} else {
			world.setBlockWithNotify(x, y, z, Block.stoneOvenIdle.blockID);
		}

		world.setBlockMetadataWithNotify(x, y, z, var5);
		world.setBlockTileEntity(x, y, z, var6);
	}

	protected TileEntity getBlockEntity() {
		return new TileEntityFurnace();
	}
}
