package net.minecraft.src;

class MCHashEntry2 {
	final long hashEntry;
	Object valueEntry;
	MCHashEntry2 nextEntry;
	final int slotHash;

	MCHashEntry2(int slotHash, long hashEntry, Object valueEntry, MCHashEntry2 nextEntry) {
		this.valueEntry = valueEntry;
		this.nextEntry = nextEntry;
		this.hashEntry = hashEntry;
		this.slotHash = slotHash;
	}

	public final long getHash() {
		return this.hashEntry;
	}

	public final Object getValue() {
		return this.valueEntry;
	}

	public final boolean equals(Object object) {
		if(!(object instanceof MCHashEntry2)) {
			return false;
		} else {
			MCHashEntry2 var2 = (MCHashEntry2)object;
			Long var3 = Long.valueOf(this.getHash());
			Long var4 = Long.valueOf(var2.getHash());
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
		return MCHashTable2.getHash(this.hashEntry);
	}

	public final String toString() {
		return this.getHash() + "=" + this.getValue();
	}
}
