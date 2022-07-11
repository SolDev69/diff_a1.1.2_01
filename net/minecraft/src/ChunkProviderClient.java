package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkProviderClient implements IChunkProvider {
	private Chunk blankChunk;
	private Map chunkMapping = new HashMap();
	private List chunkListing = new ArrayList();
	private World worldObj;

	public ChunkProviderClient(World worldObj) {
		this.blankChunk = new Chunk(worldObj, new byte['\u8000'], 0, 0);
		this.blankChunk.isChunkRendered = true;
		this.blankChunk.neverSave = true;
		this.worldObj = worldObj;
	}

	public boolean chunkExists(int chunkX, int chunkZ) {
		ChunkCoordinates var3 = new ChunkCoordinates(chunkX, chunkZ);
		return this.chunkMapping.containsKey(var3);
	}

	public void unloadChunk(int chunkX, int chunkZ) {
		Chunk var3 = this.provideChunk(chunkX, chunkZ);
		if(!var3.isChunkRendered) {
			var3.onChunkUnload();
		}

		this.chunkMapping.remove(new ChunkCoordinates(chunkX, chunkZ));
		this.chunkListing.remove(var3);
	}

	public Chunk loadChunk(int chunkX, int chunkZ) {
		ChunkCoordinates var3 = new ChunkCoordinates(chunkX, chunkZ);
		byte[] var4 = new byte['\u8000'];
		Chunk var5 = new Chunk(this.worldObj, var4, chunkX, chunkZ);
		Arrays.fill(var5.skylightMap.data, (byte)-1);
		this.chunkMapping.put(var3, var5);
		var5.isChunkLoaded = true;
		return var5;
	}

	public Chunk provideChunk(int chunkX, int chunkZ) {
		ChunkCoordinates var3 = new ChunkCoordinates(chunkX, chunkZ);
		Chunk var4 = (Chunk)this.chunkMapping.get(var3);
		return var4 == null ? this.blankChunk : var4;
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

	public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ) {
	}
}
