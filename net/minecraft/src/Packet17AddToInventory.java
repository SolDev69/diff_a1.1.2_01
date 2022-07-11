package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet17AddToInventory extends Packet {
	public int itemID;
	public int count;
	public int itemDamage;

	public Packet17AddToInventory() {
	}

	public Packet17AddToInventory(ItemStack stack, int count) {
		this.itemID = stack.itemID;
		this.count = count;
		this.itemDamage = stack.itemDmg;
		if(count == 0) {
			boolean count1 = true;
		}

	}

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
