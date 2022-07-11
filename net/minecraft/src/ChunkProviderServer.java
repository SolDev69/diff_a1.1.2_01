package net.minecraft.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChunkProviderServer implements IChunkProvider {
	private Set droppedChunksSet = new HashSet();
	private Chunk chunk;
	private IChunkProvider serverChunkProvider;
	private IChunkLoader serverChunkLoader;
	private Map id2ChunkMap = new HashMap();
	private List loadedChunks = new ArrayList();
	private WorldServer worldObj;

	public ChunkProviderServer(WorldServer worldServer, IChunkLoader chunkLoader, IChunkProvider chunkProvider) {
		this.chunk = new Chunk(worldServer, new byte['\u8000'], 0, 0);
		this.chunk.isChunkRendered = true;
		this.chunk.neverSave = true;
		this.worldObj = worldServer;
		this.serverChunkLoader = chunkLoader;
		this.serverChunkProvider = chunkProvider;
	}

	public boolean chunkExists(int x, int z) {
		ChunkCoordinates var3 = new ChunkCoordinates(x, z);
		return this.id2ChunkMap.containsKey(var3);
	}

	public void dropChunk(int var1, int var2) {
		int var3 = var1 * 16 + 8 - this.worldObj.spawnX;
		int var4 = var2 * 16 + 8 - this.worldObj.spawnZ;
		byte var5 = 20;
		if(var3 < -var5 || var3 > var5 || var4 < -var5 || var4 > var5) {
			this.droppedChunksSet.add(new ChunkCoordinates(var1, var2));
		}

	}

	public Chunk loadChunk(int x, int z) {
		ChunkCoordinates var3 = new ChunkCoordinates(x, z);
		this.droppedChunksSet.remove(new ChunkCoordinates(x, z));
		Chunk var4 = (Chunk)this.id2ChunkMap.get(var3);
		if(var4 == null) {
			var4 = this.loadAndSaveChunk(x, z);
			if(var4 == null) {
				if(this.serverChunkProvider == null) {
					var4 = this.chunk;
				} else {
					var4 = this.serverChunkProvider.provideChunk(x, z);
				}
			}

			this.id2ChunkMap.put(var3, var4);
			this.loadedChunks.add(var4);
			if(var4 != null) {
				var4.onChunkLoad();
			}

			if(!var4.isTerrainPopulated && this.chunkExists(x + 1, z + 1) && this.chunkExists(x, z + 1) && this.chunkExists(x + 1, z)) {
				this.populate(this, x, z);
			}

			if(this.chunkExists(x - 1, z) && !this.provideChunk(x - 1, z).isTerrainPopulated && this.chunkExists(x - 1, z + 1) && this.chunkExists(x, z + 1) && this.chunkExists(x - 1, z)) {
				this.populate(this, x - 1, z);
			}

			if(this.chunkExists(x, z - 1) && !this.provideChunk(x, z - 1).isTerrainPopulated && this.chunkExists(x + 1, z - 1) && this.chunkExists(x, z - 1) && this.chunkExists(x + 1, z)) {
				this.populate(this, x, z - 1);
			}

			if(this.chunkExists(x - 1, z - 1) && !this.provideChunk(x - 1, z - 1).isTerrainPopulated && this.chunkExists(x - 1, z - 1) && this.chunkExists(x, z - 1) && this.chunkExists(x - 1, z)) {
				this.populate(this, x - 1, z - 1);
			}
		}

		return var4;
	}

	public Chunk provideChunk(int x, int z) {
		ChunkCoordinates var3 = new ChunkCoordinates(x, z);
		Chunk var4 = (Chunk)this.id2ChunkMap.get(var3);
		return var4 == null ? (this.worldObj.worldChunkLoadOverride ? this.loadChunk(x, z) : this.chunk) : var4;
	}

	private Chunk loadAndSaveChunk(int x, int z) {
		if(this.serverChunkLoader == null) {
			return null;
		} else {
			try {
				Chunk var3 = this.serverChunkLoader.loadChunk(this.worldObj, x, z);
				if(var3 != null) {
					var3.lastSaveTime = this.worldObj.worldTime;
				}

				return var3;
			} catch (Exception var4) {
				var4.printStackTrace();
				return null;
			}
		}
	}

	private void saveExtraChunkData(Chunk chunk) {
		if(this.serverChunkLoader != null) {
			try {
				this.serverChunkLoader.saveExtraChunkData(this.worldObj, chunk);
			} catch (Exception var3) {
				var3.printStackTrace();
			}

		}
	}

	private void saveChunk(Chunk chunk) {
		if(this.serverChunkLoader != null) {
			try {
				chunk.lastSaveTime = this.worldObj.worldTime;
				this.serverChunkLoader.saveChunk(this.worldObj, chunk);
			} catch (IOException var3) {
				var3.printStackTrace();
			}

		}
	}

	public void populate(IChunkProvider chunkProvider, int x, int z) {
		Chunk var4 = this.provideChunk(x, z);
		if(!var4.isTerrainPopulated) {
			var4.isTerrainPopulated = true;
			if(this.serverChunkProvider != null) {
				this.serverChunkProvider.populate(chunkProvider, x, z);
				var4.setChunkModified();
			}
		}

	}

	public boolean saveChunks(boolean flag, IProgressUpdate progressUpdate) {
		int var3 = 0;

		for(int var4 = 0; var4 < this.loadedChunks.size(); ++var4) {
			Chunk var5 = (Chunk)this.loadedChunks.get(var4);
			if(flag && !var5.neverSave) {
				this.saveExtraChunkData(var5);
			}

			if(var5.needsSaving(flag)) {
				this.saveChunk(var5);
				var5.isModified = false;
				++var3;
				if(var3 == 2 && !flag) {
					return false;
				}
			}
		}

		if(flag) {
			if(this.serverChunkLoader == null) {
				return true;
			}

			this.serverChunkLoader.saveExtraData();
		}

		return true;
	}

	public boolean unload100OldestChunks() {
		if(!this.worldObj.levelSaving) {
			for(int var1 = 0; var1 < 16; ++var1) {
				if(!this.droppedChunksSet.isEmpty()) {
					ChunkCoordinates var2 = (ChunkCoordinates)this.droppedChunksSet.iterator().next();
					Chunk var3 = this.provideChunk(var2.posX, var2.posZ);
					var3.onChunkUnload();
					this.saveChunk(var3);
					this.saveExtraChunkData(var3);
					this.droppedChunksSet.remove(var2);
					this.id2ChunkMap.remove(var2);
					this.loadedChunks.remove(var3);
				}
			}

			if(this.serverChunkLoader != null) {
				this.serverChunkLoader.chunkTick();
			}
		}

		return this.serverChunkProvider.unload100OldestChunks();
	}

	public boolean canSave() {
		return !this.worldObj.levelSaving;
	}
}
