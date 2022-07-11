package net.minecraft.src;

class MCHashEntry {
	final int hashEntry;
	Object valueEntry;
	MCHashEntry nextEntry;
	final int slotHash;

	MCHashEntry(int slotHash, int hashEntry, Object valueEntry, MCHashEntry nextEntry) {
		this.valueEntry = valueEntry;
		this.nextEntry = nextEntry;
		this.hashEntry = hashEntry;
		this.slotHash = slotHash;
	}

	public final int getHash() {
		return this.hashEntry;
	}

	public final Object getValue() {
		return this.valueEntry;
	}

	public final boolean equals(Object object) {
		if(!(object instanceof MCHashEntry)) {
			return false;
		} else {
			MCHashEntry var2 = (MCHashEntry)object;
			Integer var3 = Integer.valueOf(this.getHash());
			Integer var4 = Integer.valueOf(var2.getHash());
			if(var3 == var4 || var3 != null && var3.equals(var4)) {
				Object var5 = this.getValue();
				Object var6 = var2.getValue();
				if(var5 == var6 || var5 != null && var5.equals(var6)) {
					return true;
				}
			}

			return false;
		}
	}

	public final int hashCode() {
		return MCHashTable.getHash(this.hashEntry);
	}

	public final String toString() {
		return this.getHash() + "=" + this.getValue();
	}
}
