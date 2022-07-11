package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet3Chat extends Packet {
	public String message;

	public Packet3Chat() {
	}

	public Packet3Chat(String msg) {
		this.message = msg;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.message = dataInputStream.readUTF();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(this.message);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleChat(this);
	}

	public int getPacketSize() {
		return this.message.length();
	}
}
