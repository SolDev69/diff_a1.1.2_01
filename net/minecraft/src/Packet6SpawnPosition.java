package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet6SpawnPosition extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.xPosition = dataInputStream.readInt();
		this.yPosition = dataInputStream.readInt();
		this.zPosition = dataInputStream.readInt();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.xPosition);
		dataOutputStream.writeInt(this.yPosition);
		dataOutputStream.writeInt(this.zPosition);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleSpawnPosition(this);
	}

	public int getPacketSize() {
		return 12;
	}
}
