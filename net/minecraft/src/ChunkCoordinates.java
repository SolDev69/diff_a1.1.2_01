package net.minecraft.src;

final class ChunkCoordinates {
	public final int posX;
	public final int posZ;

	public ChunkCoordinates(int posX, int posZ) {
		this.posX = posX;
		this.posZ = posZ;
	}

	public boolean equals(Object object) {
		if(!(object instanceof ChunkCoordinates)) {
			return false;
		} else {
			ChunkCoordinates var2 = (ChunkCoordinates)object;
			return this.posX == var2.posX && this.posZ == var2.posZ;
		}
	}

	public int hashCode() {
		return this.posX << 16 ^ this.posZ;
	}
}
