package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString extends NBTBase {
	public String stringValue;

	public NBTTagString() {
	}

	public NBTTagString(String value) {
		this.stringValue = value;
		if(value == null) {
			throw new IllegalArgumentException("Empty string not allowed");
		}
	}

	void writeTagContents(DataOutput dataOutput) throws IOException {
		dataOutput.writeUTF(this.stringValue);
	}

	void readTagContents(DataInput dataInput) throws IOException {
		this.stringValue = dataInput.readUTF();
	}

	public byte getType() {
		return (byte)8;
	}

	public String toString() {
		return "" + this.stringValue;
	}
}
