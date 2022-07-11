package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet14BlockDig extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int face;
	public int status;

	public Packet14BlockDig() {
	}

	public Packet14BlockDig(int status, int x, int y, int z, int side) {
		this.status = status;
		this.xPosition = x;
		this.yPosition = y;
		this.zPosition = z;
		this.face = side;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.status = dataInputStream.read();
		this.xPosition = dataInputStream.readInt();
		this.yPosition = dataInputStream.read();
		this.zPosition = dataInputStream.readInt();
		this.face = dataInputStream.read();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.write(this.status);
		dataOutputStream.writeInt(this.xPosition);
		dataOutputStream.write(this.yPosition);
		dataOutputStream.writeInt(this.zPosition);
		dataOutputStream.write(this.face);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleBlockDig(this);
	}

	public int getPacketSize() {
		return 11;
	}
}
