package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet29DestroyEntity extends Packet {
	public int entityId;

	public Packet29DestroyEntity() {
	}

	public Packet29DestroyEntity(int entityID) {
		this.entityId = entityID;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.entityId = dataInputStream.readInt();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.entityId);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleDestroyEntity(this);
	}

	public int getPacketSize() {
		return 4;
	}
}
