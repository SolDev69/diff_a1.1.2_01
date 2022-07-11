package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet4UpdateTime extends Packet {
	public long time;

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.time = dataInputStream.readLong();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeLong(this.time);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleUpdateTime(this);
	}

	public int getPacketSize() {
		return 8;
	}
}
