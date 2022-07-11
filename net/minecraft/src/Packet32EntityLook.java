package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet32EntityLook extends Packet30Entity {
	public Packet32EntityLook() {
		this.rotating = true;
	}

	public Packet32EntityLook(int entityID, byte yaw, byte pitch) {
		super(entityID);
		this.yaw = yaw;
		this.pitch = pitch;
		this.rotating = true;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		super.readPacketData(dataInputStream);
		this.yaw = dataInputStream.readByte();
		this.pitch = dataInputStream.readByte();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		super.writePacket(dataOutputStream);
		dataOutputStream.writeByte(this.yaw);
		dataOutputStream.writeByte(this.pitch);
	}

	public int getPacketSize() {
		return 6;
	}
}
