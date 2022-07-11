package net.minecraft.src;

import java.io.IOException;

public class ChunkProviderIso implements IChunkProvider {
	private Chunk[] chunks = new Chunk[256];
	private World worldObj;
	private IChunkLoader chunkLoader;
	byte[] blocks = new byte['\u8000'];

	public ChunkProviderIso(World worldObj, IChunkLoader chunkLoader) {
		this.worldObj = worldObj;
		this.chunkLoader = chunkLoader;
	}

	public boolean chunkExists(int chunkX, int chunkZ) {
		int var3 = chunkX & 15 | (chunkZ & 15) * 16;
		return this.chunks[var3] != null && this.chunks[var3].isAtLocation(chunkX, chunkZ);
	}

	public Chunk provideChunk(int chunkX, int chunkZ) {
		int var3 = chunkX & 15 | (chunkZ & 15) * 16;

		try {
			if(!this.chunkExists(chunkX, chunkZ)) {
				Chunk var4 = this.getChunkAt(chunkX, chunkZ);
				if(var4 == null) {
					var4 = new Chunk(this.worldObj, this.blocks, chunkX, chunkZ);
					var4.isChunkRendered = true;
					var4.neverSave = true;
				}

				this.chunks[var3] = var4;
			}

			return this.chunks[var3];
		} catch (Exception var5) {
			var5.printStackTrace();
			return null;
		}
	}

	private synchronized Chunk getChunkAt(int chunkX, int chunkZ) {
		try {
			return this.chunkLoader.loadChunk(this.worldObj, chunkX, chunkZ);
		} catch (IOException var4) {
			var4.printStackTrace();
			return null;
		}
	}

	public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ) {
	}

	public boolean saveChunks(boolean var1, IProgressUpdate progressUpdate) {
		return true;
	}

	public boolean unload100OldestChunks() {
		return false;
	}

	public boolean canSave() {
		return false;
	}
}
