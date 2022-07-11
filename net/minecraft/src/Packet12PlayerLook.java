package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet12PlayerLook extends Packet10Flying {
	public Packet12PlayerLook() {
		this.rotating = true;
	}

	public Packet12PlayerLook(float yaw, float pitch, boolean onGround) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
		this.rotating = true;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.yaw = dataInputStream.readFloat();
		this.pitch = dataInputStream.readFloat();
		super.readPacketData(dataInputStream);
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeFloat(this.yaw);
		dataOutputStream.writeFloat(this.pitch);
		super.writePacket(dataOutputStream);
	}

	public int getPacketSize() {
		return 9;
	}
}
