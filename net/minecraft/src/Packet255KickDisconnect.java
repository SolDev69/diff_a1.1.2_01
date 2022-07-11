package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet255KickDisconnect extends Packet {
	public String reason;

	public Packet255KickDisconnect() {
	}

	public Packet255KickDisconnect(String reason) {
		this.reason = reason;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.reason = dataInputStream.readUTF();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(this.reason);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleKickDisconnect(this);
	}

	public int getPacketSize() {
		return this.reason.length();
	}
}
