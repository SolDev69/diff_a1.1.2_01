package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet33RelEntityMoveLook extends Packet30Entity {
	public Packet33RelEntityMoveLook() {
		this.rotating = true;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		super.readPacketData(dataInputStream);
		this.xPosition = dataInputStream.readByte();
		this.yPosition = dataInputStream.readByte();
		this.zPosition = dataInputStream.readByte();
		this.yaw = dataInputStream.readByte();
		this.pitch = dataInputStream.readByte();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		super.writePacket(dataOutputStream);
		dataOutputStream.writeByte(this.xPosition);
		dataOutputStream.writeByte(this.yPosition);
		dataOutputStream.writeByte(this.zPosition);
		dataOutputStream.writeByte(this.yaw);
		dataOutputStream.writeByte(this.pitch);
	}

	public int getPacketSize() {
		return 9;
	}
}
