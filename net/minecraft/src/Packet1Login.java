package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet1Login extends Packet {
	public int protocolVersion;
	public String username;
	public String password;

	public Packet1Login() {
	}

	public Packet1Login(String username, String password, int protocolVersion) {
		this.username = username;
		this.password = password;
		this.protocolVersion = protocolVersion;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.protocolVersion = dataInputStream.readInt();
		this.username = dataInputStream.readUTF();
		this.password = dataInputStream.readUTF();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.protocolVersion);
		dataOutputStream.writeUTF(this.username);
		dataOutputStream.writeUTF(this.password);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleLogin(this);
	}

	public int getPacketSize() {
		return 4 + this.username.length() + this.password.length() + 4;
	}
}
