package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

public class ChunkLoader implements IChunkLoader {
	private File saveDir;
	private boolean createIfNecessary;

	public ChunkLoader(File saveDir, boolean createIfNecessary) {
		this.saveDir = saveDir;
		this.createIfNecessary = createIfNecessary;
	}

	private File chunkFileForXZ(int x, int z) {
		String var3 = "c." + Integer.toString(x, 36) + "." + Integer.toString(z, 36) + ".dat";
		String var4 = Integer.toString(x & 63, 36);
		String var5 = Integer.toString(z & 63, 36);
		File var6 = new File(this.saveDir, var4);
		if(!var6.exists()) {
			if(!this.createIfNecessary) {
				return null;
			}

			var6.mkdir();
		}

		var6 = new File(var6, var5);
		if(!var6.exists()) {
			if(!this.createIfNecessary) {
				return null;
			}

			var6.mkdir();
		}

		var6 = new File(var6, var3);
		return !var6.exists() && !this.createIfNecessary ? null : var6;
	}

	public Chunk loadChunk(World world, int x, int z) {
		File var4 = this.chunkFileForXZ(x, z);
		if(var4 != null && var4.exists()) {
			try {
				FileInputStream var5 = new FileInputStream(var4);
				NBTTagCompound var6 = CompressedStreamTools.readCompressed(var5);
				if(!var6.hasKey("Level")) {
					System.out.println("Chunk file at " + x + "," + z + " is missing level data, skipping");
					return null;
				}

				if(!var6.getCompoundTag("Level").hasKey("Blocks")) {
					System.out.println("Chunk file at " + x + "," + z + " is missing block data, skipping");
					return null;
				}

				Chunk var7 = loadChunkIntoWorldFromCompound(world, var6.getCompoundTag("Level"));
				if(!var7.isAtLocation(x, z)) {
					System.out.println("Chunk file at " + x + "," + z + " is in the wrong location; relocating. (Expected " + x + ", " + z + ", got " + var7.xPosition + ", " + var7.zPosition + ")");
					var6.setInteger("xPos", x);
					var6.setInteger("zPos", z);
					var7 = loadChunkIntoWorldFromCompound(world, var6.getCompoundTag("Level"));
				}

				return var7;
			} catch (Exception var8) {
				var8.printStackTrace();
			}
		}

		return null;
	}

	public void saveChunk(World world, Chunk chunk) {
		world.checkSessionLock();
		File var3 = this.chunkFileForXZ(chunk.xPosition, chunk.zPosition);
		if(var3.exists()) {
			world.sizeOnDisk -= var3.length();
		}

		try {
			File var4 = new File(this.saveDir, "tmp_chunk.dat");
			FileOutputStream var5 = new FileOutputStream(var4);
			NBTTagCompound var6 = new NBTTagCompound();
			NBTTagCompound var7 = new NBTTagCompound();
			var6.setTag("Level", var7);
			this.storeChunkInCompound(chunk, world, var7);
			CompressedStreamTools.writeCompressed(var6, var5);
			var5.close();
			if(var3.exists()) {
				var3.delete();
			}

			var4.renameTo(var3);
			world.sizeOnDisk += var3.length();
		} catch (Exception var8) {
			var8.printStackTrace();
		}

	}

	public void storeChunkInCompound(Chunk chunk, World world, NBTTagCompound nbttagcompound) {
		world.checkSessionLock();
		nbttagcompound.setInteger("xPos", chunk.xPosition);
		nbttagcompound.setInteger("zPos", chunk.zPosition);
		nbttagcompound.setLong("LastUpdate", world.worldTime);
		nbttagcompound.setByteArray("Blocks", chunk.blocks);
		nbttagcompound.setByteArray("Data", chunk.data.data);
		nbttagcompound.setByteArray("SkyLight", chunk.skylightMap.data);
		nbttagcompound.setByteArray("BlockLight", chunk.blocklightMap.data);
		nbttagcompound.setByteArray("HeightMap", chunk.heightMap);
		nbttagcompound.setBoolean("TerrainPopulated", chunk.isTerrainPopulated);
		chunk.hasEntities = false;
		NBTTagList var4 = new NBTTagList();

		Iterator var6;
		NBTTagCompound var8;
		for(int var5 = 0; var5 < chunk.entities.length; ++var5) {
			var6 = chunk.entities[var5].iterator();

			while(var6.hasNext()) {
				Entity var7 = (Entity)var6.next();
				chunk.hasEntities = true;
				var8 = new NBTTagCompound();
				if(var7.addEntityID(var8)) {
					var4.setTag(var8);
				}
			}
		}

		nbttagcompound.setTag("Entities", var4);
		NBTTagList var9 = new NBTTagList();
		var6 = chunk.chunkTileEntityMap.values().iterator();

		while(var6.hasNext()) {
			TileEntity var10 = (TileEntity)var6.next();
			var8 = new NBTTagCompound();
			var10.writeToNBT(var8);
			var9.setTag(var8);
		}

		nbttagcompound.setTag("TileEntities", var9);
	}

	public static Chunk loadChunkIntoWorldFromCompound(World world, NBTTagCompound nbttagcompound) {
		int var2 = nbttagcompound.getInteger("xPos");
		int var3 = nbttagcompound.getInteger("zPos");
		Chunk var4 = new Chunk(world, var2, var3);
		var4.blocks = nbttagcompound.getByteArray("Blocks");
		var4.data = new NibbleArray(nbttagcompound.getByteArray("Data"));
		var4.skylightMap = new NibbleArray(nbttagcompound.getByteArray("SkyLight"));
		var4.blocklightMap = new NibbleArray(nbttagcompound.getByteArray("BlockLight"));
		var4.heightMap = nbttagcompound.getByteArray("HeightMap");
		var4.isTerrainPopulated = nbttagcompound.getBoolean("TerrainPopulated");
		if(!var4.data.isValid()) {
			var4.data = new NibbleArray(var4.blocks.length);
		}

		if(var4.heightMap == null || !var4.skylightMap.isValid()) {
			var4.heightMap = new byte[256];
			var4.skylightMap = new NibbleArray(var4.blocks.length);
			var4.generateSkylightMap();
		}

		if(!var4.blocklightMap.isValid()) {
			var4.blocklightMap = new NibbleArray(var4.blocks.length);
			var4.doNothing();
		}

		NBTTagList var5 = nbttagcompound.getTagList("Entities");
		if(var5 != null) {
			for(int var6 = 0; var6 < var5.tagCount(); ++var6) {
				NBTTagCompound var7 = (NBTTagCompound)var5.tagAt(var6);
				Entity var8 = EntityList.createEntityFromNBT(var7, world);
				var4.hasEntities = true;
				if(var8 != null) {
					var4.addEntity(var8);
				}
			}
		}

		NBTTagList var10 = nbttagcompound.getTagList("TileEntities");
		if(var10 != null) {
			for(int var11 = 0; var11 < var10.tagCount(); ++var11) {
				NBTTagCompound var12 = (NBTTagCompound)var10.tagAt(var11);
				TileEntity var9 = TileEntity.createAndLoadEntity(var12);
				if(var9 != null) {
					var4.addTileEntity(var9);
				}
			}
		}

		return var4;
	}

	public void chunkTick() {
	}

	public void saveExtraData() {
	}

	public void saveExtraChunkData(World world, Chunk chunk) {
	}
}
