package net.minecraft.src;

import java.io.IOException;

public class ChunkProviderLoadOrGenerate implements IChunkProvider {
	private Chunk blankChunk;
	private IChunkProvider chunkProvider;
	private IChunkLoader chunkLoader;
	private Chunk[] chunks = new Chunk[1024];
	private World worldObj;
	int lastQueriedChunkXPos = -999999999;
	int lastQueriedChunkZPos = -999999999;
	private Chunk lastQueriedChunk;

	public ChunkProviderLoadOrGenerate(World world, IChunkLoader chunkLoader, IChunkProvider chunkProvider) {
		this.blankChunk = new Chunk(world, new byte['\u8000'], 0, 0);
		this.blankChunk.isChunkRendered = true;
		this.blankChunk.neverSave = true;
		this.worldObj = world;
		this.chunkLoader = chunkLoader;
		this.chunkProvider = chunkProvider;
	}

	public boolean chunkExists(int chunkX, int chunkZ) {
		if(chunkX == this.lastQueriedChunkXPos && chunkZ == this.lastQueriedChunkZPos && this.lastQueriedChunk != null) {
			return true;
		} else {
			int var3 = chunkX & 31;
			int var4 = chunkZ & 31;
			int var5 = var3 + var4 * 32;
			return this.chunks[var5] != null && (this.chunks[var5] == this.blankChunk || this.chunks[var5].isAtLocation(chunkX, chunkZ));
		}
	}

	public Chunk provideChunk(int chunkX, int chunkZ) {
		if(chunkX == this.lastQueriedChunkXPos && chunkZ == this.lastQueriedChunkZPos && this.lastQueriedChunk != null) {
			return this.lastQueriedChunk;
		} else {
			int var3 = chunkX & 31;
			int var4 = chunkZ & 31;
			int var5 = var3 + var4 * 32;
			if(!this.chunkExists(chunkX, chunkZ)) {
				if(this.chunks[var5] != null) {
					this.chunks[var5].onChunkUnload();
					this.saveChunk(this.chunks[var5]);
					this.saveExtraChunkData(this.chunks[var5]);
				}

				Chunk var6 = this.getChunkAt(chunkX, chunkZ);
				if(var6 == null) {
					if(this.chunkProvider == null) {
						var6 = this.blankChunk;
					} else {
						var6 = this.chunkProvider.provideChunk(chunkX, chunkZ);
					}
				}

				this.chunks[var5] = var6;
				if(this.chunks[var5] != null) {
					this.chunks[var5].onChunkLoad();
				}

				if(!this.chunks[var5].isTerrainPopulated && this.chunkExists(chunkX + 1, chunkZ + 1) && this.chunkExists(chunkX, chunkZ + 1) && this.chunkExists(chunkX + 1, chunkZ)) {
					this.populate(this, chunkX, chunkZ);
				}

				if(this.chunkExists(chunkX - 1, chunkZ) && !this.provideChunk(chunkX - 1, chunkZ).isTerrainPopulated && this.chunkExists(chunkX - 1, chunkZ + 1) && this.chunkExists(chunkX, chunkZ + 1) && this.chunkExists(chunkX - 1, chunkZ)) {
					this.populate(this, chunkX - 1, chunkZ);
				}

				if(this.chunkExists(chunkX, chunkZ - 1) && !this.provideChunk(chunkX, chunkZ - 1).isTerrainPopulated && this.chunkExists(chunkX + 1, chunkZ - 1) && this.chunkExists(chunkX, chunkZ - 1) && this.chunkExists(chunkX + 1, chunkZ)) {
					this.populate(this, chunkX, chunkZ - 1);
				}

				if(this.chunkExists(chunkX - 1, chunkZ - 1) && !this.provideChunk(chunkX - 1, chunkZ - 1).isTerrainPopulated && this.chunkExists(chunkX - 1, chunkZ - 1) && this.chunkExists(chunkX, chunkZ - 1) && this.chunkExists(chunkX - 1, chunkZ)) {
					this.populate(this, chunkX - 1, chunkZ - 1);
				}
			}

			this.lastQueriedChunkXPos = chunkX;
			this.lastQueriedChunkZPos = chunkZ;
			this.lastQueriedChunk = this.chunks[var5];
			return this.chunks[var5];
		}
	}

	private Chunk getChunkAt(int posX, int posZ) {
		if(this.chunkLoader == null) {
			return null;
		} else {
			try {
				Chunk var3 = this.chunkLoader.loadChunk(this.worldObj, posX, posZ);
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
		if(this.chunkLoader != null) {
			try {
				this.chunkLoader.saveExtraChunkData(this.worldObj, chunk);
			} catch (Exception var3) {
				var3.printStackTrace();
			}

		}
	}

	private void saveChunk(Chunk chunk) {
		if(this.chunkLoader != null) {
			try {
				chunk.lastSaveTime = this.worldObj.worldTime;
				this.chunkLoader.saveChunk(this.worldObj, chunk);
			} catch (IOException var3) {
				var3.printStackTrace();
			}

		}
	}

	public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ) {
		Chunk var4 = this.provideChunk(chunkX, chunkZ);
		if(!var4.isTerrainPopulated) {
			var4.isTerrainPopulated = true;
			if(this.chunkProvider != null) {
				this.chunkProvider.populate(chunkProvider, chunkX, chunkZ);
				var4.setChunkModified();
			}
		}

	}

	public boolean saveChunks(boolean var1, IProgressUpdate progressUpdate) {
		int var3 = 0;
		int var4 = 0;
		int var5;
		if(progressUpdate != null) {
			for(var5 = 0; var5 < this.chunks.length; ++var5) {
				if(this.chunks[var5] != null && this.chunks[var5].needsSaving(var1)) {
					++var4;
				}
			}
		}

		var5 = 0;

		for(int var6 = 0; var6 < this.chunks.length; ++var6) {
			if(this.chunks[var6] != null) {
				if(var1 && !this.chunks[var6].neverSave) {
					this.saveExtraChunkData(this.chunks[var6]);
				}

				if(this.chunks[var6].needsSaving(var1)) {
					this.saveChunk(this.chunks[var6]);
					this.chunks[var6].isModified = false;
					++var3;
					if(var3 == 2 && !var1) {
						return false;
					}

					if(progressUpdate != null) {
						++var5;
						if(var5 % 10 == 0) {
							progressUpdate.setLoadingProgress(var5 * 100 / var4);
						}
					}
				}
			}
		}

		if(var1) {
			if(this.chunkLoader == null) {
				return true;
			}

			this.chunkLoader.saveExtraData();
		}

		return true;
	}

	public boolean unload100OldestChunks() {
		if(this.chunkLoader != null) {
			this.chunkLoader.chunkTick();
		}

		return this.chunkProvider.unload100OldestChunks();
	}

	public boolean canSave() {
		return true;
	}
}
