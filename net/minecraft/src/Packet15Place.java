package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet15Place extends Packet {
	public int id;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int direction;

	public Packet15Place() {
	}

	public Packet15Place(int id, int x, int y, int z, int direction) {
		this.id = id;
		this.xPosition = x;
		this.yPosition = y;
		this.zPosition = z;
		this.direction = direction;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.id = dataInputStream.readShort();
		this.xPosition = dataInputStream.readInt();
		this.yPosition = dataInputStream.read();
		this.zPosition = dataInputStream.readInt();
		this.direction = dataInputStream.read();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeShort(this.id);
		dataOutputStream.writeInt(this.xPosition);
		dataOutputStream.write(this.yPosition);
		dataOutputStream.writeInt(this.zPosition);
		dataOutputStream.write(this.direction);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handlePlace(this);
	}

	public int getPacketSize() {
		return 12;
	}
}
