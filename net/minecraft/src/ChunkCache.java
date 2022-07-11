package net.minecraft.src;

public class ChunkCache implements IBlockAccess {
	private int chunkX;
	private int chunkZ;
	private Chunk[][] chunkArray;
	private World worldObj;

	public ChunkCache(World world, int x, int var3, int z, int var5, int var6, int var7) {
		this.worldObj = world;
		this.chunkX = x >> 4;
		this.chunkZ = z >> 4;
		int var8 = var5 >> 4;
		int var9 = var7 >> 4;
		this.chunkArray = new Chunk[var8 - this.chunkX + 1][var9 - this.chunkZ + 1];

		for(int var10 = this.chunkX; var10 <= var8; ++var10) {
			for(int var11 = this.chunkZ; var11 <= var9; ++var11) {
				this.chunkArray[var10 - this.chunkX][var11 - this.chunkZ] = world.getChunkFromChunkCoords(var10, var11);
			}
		}

	}

	public int getBlockId(int blockX, int blockY, int blockZ) {
		if(blockY < 0) {
			return 0;
		} else if(blockY >= 128) {
			return 0;
		} else {
			int var4 = (blockX >> 4) - this.chunkX;
			int var5 = (blockZ >> 4) - this.chunkZ;
			return this.chunkArray[var4][var5].getBlockID(blockX & 15, blockY, blockZ & 15);
		}
	}

	public int getBlockMetadata(int x, int y, int z) {
		if(y < 0) {
			return 0;
		} else if(y >= 128) {
			return 0;
		} else {
			int var4 = (x >> 4) - this.chunkX;
			int var5 = (z >> 4) - this.chunkZ;
			return this.chunkArray[var4][var5].getBlockMetadata(x & 15, y, z & 15);
		}
	}

	public Material getBlockMaterial(int x, int y, int z) {
		int var4 = this.getBlockId(x, y, z);
		return var4 == 0 ? Material.air : Block.blocksList[var4].material;
	}

	public boolean isBlockNormalCube(int x, int y, int z) {
		Block var4 = Block.blocksList[this.getBlockId(x, y, z)];
		return var4 == null ? false : var4.isOpaqueCube();
	}
}
