package net.minecraft.src;

public class ChunkCoordIntPair {
	public int chunkXPos;
	public int chunkZPos;

	public ChunkCoordIntPair(int chunkXPos, int chunkZPos) {
		this.chunkXPos = chunkXPos;
		this.chunkZPos = chunkZPos;
	}

	public int hashCode() {
		return this.chunkXPos << 8 | this.chunkZPos;
	}

	public boolean equals(Object var1) {
		ChunkCoordIntPair var2 = (ChunkCoordIntPair)var1;
		return var2.chunkXPos == this.chunkXPos && var2.chunkZPos == this.chunkZPos;
	}

	public double a(Entity entity) {
		double var2 = (double)(this.chunkXPos * 16 + 8);
		double var4 = (double)(this.chunkZPos * 16 + 8);
		double var6 = var2 - entity.posX;
		double var8 = var4 - entity.posZ;
		return var6 * var6 + var8 * var8;
	}
}
