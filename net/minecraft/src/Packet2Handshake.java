package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet2Handshake extends Packet {
	public String username;

	public Packet2Handshake() {
	}

	public Packet2Handshake(String username) {
		this.username = username;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.username = dataInputStream.readUTF();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(this.username);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleHandshake(this);
	}

	public int getPacketSize() {
		return 4 + this.username.length() + 4;
	}
}
