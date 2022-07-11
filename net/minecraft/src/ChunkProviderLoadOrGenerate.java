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

	public boolean chunkExists(int x, int z) {
		if(x == this.lastQueriedChunkXPos && z == this.lastQueriedChunkZPos && this.lastQueriedChunk != null) {
			return true;
		} else {
			int var3 = x & 31;
			int var4 = z & 31;
			int var5 = var3 + var4 * 32;
			return this.chunks[var5] != null && (this.chunks[var5] == this.blankChunk || this.chunks[var5].isAtLocation(x, z));
		}
	}

	public Chunk provideChunk(int x, int z) {
		if(x == this.lastQueriedChunkXPos && z == this.lastQueriedChunkZPos && this.lastQueriedChunk != null) {
			return this.lastQueriedChunk;
		} else {
			int var3 = x & 31;
			int var4 = z & 31;
			int var5 = var3 + var4 * 32;
			if(!this.chunkExists(x, z)) {
				if(this.chunks[var5] != null) {
					this.chunks[var5].onChunkUnload();
					this.saveChunk(this.chunks[var5]);
					this.saveExtraChunkData(this.chunks[var5]);
				}

				Chunk var6 = this.getChunkAt(x, z);
				if(var6 == null) {
					if(this.chunkProvider == null) {
						var6 = this.blankChunk;
					} else {
						var6 = this.chunkProvider.provideChunk(x, z);
					}
				}

				this.chunks[var5] = var6;
				if(this.chunks[var5] != null) {
					this.chunks[var5].onChunkLoad();
				}

				if(!this.chunks[var5].isTerrainPopulated && this.chunkExists(x + 1, z + 1) && this.chunkExists(x, z + 1) && this.chunkExists(x + 1, z)) {
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

			this.lastQueriedChunkXPos = x;
			this.lastQueriedChunkZPos = z;
			this.lastQueriedChunk = this.chunks[var5];
			return this.chunks[var5];
		}
	}

	private Chunk getChunkAt(int x, int z) {
		if(this.chunkLoader == null) {
			return null;
		} else {
			try {
				Chunk var3 = this.chunkLoader.loadChunk(this.worldObj, x, z);
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

	public void populate(IChunkProvider chunkProvider, int x, int z) {
		Chunk var4 = this.provideChunk(x, z);
		if(!var4.isTerrainPopulated) {
			var4.isTerrainPopulated = true;
			if(this.chunkProvider != null) {
				this.chunkProvider.populate(chunkProvider, x, z);
				var4.setChunkModified();
			}
		}

	}

	public boolean saveChunks(boolean flag, IProgressUpdate progressUpdate) {
		int var3 = 0;
		int var4 = 0;
		int var5;
		if(progressUpdate != null) {
			for(var5 = 0; var5 < this.chunks.length; ++var5) {
				if(this.chunks[var5] != null && this.chunks[var5].needsSaving(flag)) {
					++var4;
				}
			}
		}

		var5 = 0;

		for(int var6 = 0; var6 < this.chunks.length; ++var6) {
			if(this.chunks[var6] != null) {
				if(flag && !this.chunks[var6].neverSave) {
					this.saveExtraChunkData(this.chunks[var6]);
				}

				if(this.chunks[var6].needsSaving(flag)) {
					this.saveChunk(this.chunks[var6]);
					this.chunks[var6].isModified = false;
					++var3;
					if(var3 == 2 && !flag) {
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

		if(flag) {
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
