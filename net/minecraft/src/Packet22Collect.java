package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet22Collect extends Packet {
	public int collectedEntityId;
	public int collectorEntityId;

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.collectedEntityId = dataInputStream.readInt();
		this.collectorEntityId = dataInputStream.readInt();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.collectedEntityId);
		dataOutputStream.writeInt(this.collectorEntityId);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleCollect(this);
	}

	public int getPacketSize() {
		return 8;
	}
}
