package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByteArray extends NBTBase {
	public byte[] byteArray;

	public NBTTagByteArray() {
	}

	public NBTTagByteArray(byte[] value) {
		this.byteArray = value;
	}

	void writeTagContents(DataOutput dataOutput) throws IOException {
		dataOutput.writeInt(this.byteArray.length);
		dataOutput.write(this.byteArray);
	}

	void readTagContents(DataInput dataInput) throws IOException {
		int var2 = dataInput.readInt();
		this.byteArray = new byte[var2];
		dataInput.readFully(this.byteArray);
	}

	public byte getType() {
		return (byte)7;
	}

	public String toString() {
		return "[" + this.byteArray.length + " bytes]";
	}
}
