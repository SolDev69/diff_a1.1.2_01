package net.minecraft.src;

public class NextTickListEntry implements Comparable {
	private static long nextTickEntryID = 0L;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int blockID;
	public long scheduledTime;
	private long tickEntryID = nextTickEntryID++;

	public NextTickListEntry(int xCoord, int yCoord, int zCoord, int blockID) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.zCoord = zCoord;
		this.blockID = blockID;
	}

	public boolean equals(Object object) {
		if(!(object instanceof NextTickListEntry)) {
			return false;
		} else {
			NextTickListEntry var2 = (NextTickListEntry)object;
			return this.xCoord == var2.xCoord && this.yCoord == var2.yCoord && this.zCoord == var2.zCoord && this.blockID == var2.blockID;
		}
	}

	public int hashCode() {
		return (this.xCoord * 128 * 1024 + this.zCoord * 128 + this.yCoord) * 256 + this.blockID;
	}

	public NextTickListEntry setScheduledTime(long scheduledTime) {
		this.scheduledTime = scheduledTime;
		return this;
	}

	public int comparer(NextTickListEntry var1) {
		return this.scheduledTime < var1.scheduledTime ? -1 : (this.scheduledTime > var1.scheduledTime ? 1 : (this.tickEntryID < var1.tickEntryID ? -1 : (this.tickEntryID > var1.tickEntryID ? 1 : 0)));
	}

	public int compareTo(Object nextTickListEntry) {
		return this.comparer((NextTickListEntry)nextTickListEntry);
	}
}
