package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet16BlockItemSwitch extends Packet {
	public int entityId;
	public int id;

	public Packet16BlockItemSwitch() {
	}

	public Packet16BlockItemSwitch(int entityId, int id) {
		this.entityId = entityId;
		this.id = id;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.entityId = dataInputStream.readInt();
		this.id = dataInputStream.readShort();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.entityId);
		dataOutputStream.writeShort(this.id);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleBlockItemSwitch(this);
	}

	public int getPacketSize() {
		return 6;
	}
}
