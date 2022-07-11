package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet17AddToInventory extends Packet {
	public int itemID;
	public int count;
	public int itemDamage;

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.itemID = dataInputStream.readShort();
		this.count = dataInputStream.readByte();
		this.itemDamage = dataInputStream.readShort();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeShort(this.itemID);
		dataOutputStream.writeByte(this.count);
		dataOutputStream.writeShort(this.itemDamage);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleAddToInventory(this);
	}

	public int getPacketSize() {
		return 5;
	}
}
