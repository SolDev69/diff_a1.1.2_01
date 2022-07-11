package net.minecraft.src;

public class PathPoint {
	public final int xCoord;
	public final int yCoord;
	public final int zCoord;
	public final int hash;
	int index = -1;
	float totalPathDistance;
	float distanceToNext;
	float distanceToTarget;
	PathPoint previous;
	public boolean isFirst = false;

	public PathPoint(int xCoord, int yCoord, int zCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.zCoord = zCoord;
		this.hash = xCoord | yCoord << 10 | zCoord << 20;
	}

	public float distanceTo(PathPoint pathPoint) {
		float var2 = (float)(pathPoint.xCoord - this.xCoord);
		float var3 = (float)(pathPoint.yCoord - this.yCoord);
		float var4 = (float)(pathPoint.zCoord - this.zCoord);
		return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
	}

	public boolean equals(Object object) {
		return ((PathPoint)object).hash == this.hash;
	}

	public int hashCode() {
		return this.hash;
	}

	public boolean isAssigned() {
		return this.index >= 0;
	}

	public String toString() {
		return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
	}
}
