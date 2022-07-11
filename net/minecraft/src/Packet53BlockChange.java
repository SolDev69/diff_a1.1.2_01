package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet53BlockChange extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int type;
	public int metadata;

	public Packet53BlockChange() {
		this.isChunkDataPacket = true;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.xPosition = dataInputStream.readInt();
		this.yPosition = dataInputStream.read();
		this.zPosition = dataInputStream.readInt();
		this.type = dataInputStream.read();
		this.metadata = dataInputStream.read();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.xPosition);
		dataOutputStream.write(this.yPosition);
		dataOutputStream.writeInt(this.zPosition);
		dataOutputStream.write(this.type);
		dataOutputStream.write(this.metadata);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleBlockChange(this);
	}

	public int getPacketSize() {
		return 11;
	}
}
