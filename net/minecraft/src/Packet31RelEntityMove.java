package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet31RelEntityMove extends Packet30Entity {
	public Packet31RelEntityMove() {
	}

	public Packet31RelEntityMove(int entityID, byte xPosition, byte yPosition, byte zPosition) {
		super(entityID);
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.zPosition = zPosition;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		super.readPacketData(dataInputStream);
		this.xPosition = dataInputStream.readByte();
		this.yPosition = dataInputStream.readByte();
		this.zPosition = dataInputStream.readByte();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		super.writePacket(dataOutputStream);
		dataOutputStream.writeByte(this.xPosition);
		dataOutputStream.writeByte(this.yPosition);
		dataOutputStream.writeByte(this.zPosition);
	}

	public int getPacketSize() {
		return 7;
	}
}
