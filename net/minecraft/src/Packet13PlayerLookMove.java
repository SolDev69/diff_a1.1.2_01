package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet13PlayerLookMove extends Packet10Flying {
	public Packet13PlayerLookMove() {
		this.rotating = true;
		this.moving = true;
	}

	public Packet13PlayerLookMove(double xPosition, double yPosition, double stance, double zPosition, float yaw, float pitch, boolean onGround) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.stance = stance;
		this.zPosition = zPosition;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
		this.rotating = true;
		this.moving = true;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.xPosition = dataInputStream.readDouble();
		this.yPosition = dataInputStream.readDouble();
		this.stance = dataInputStream.readDouble();
		this.zPosition = dataInputStream.readDouble();
		this.yaw = dataInputStream.readFloat();
		this.pitch = dataInputStream.readFloat();
		super.readPacketData(dataInputStream);
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeDouble(this.xPosition);
		dataOutputStream.writeDouble(this.yPosition);
		dataOutputStream.writeDouble(this.stance);
		dataOutputStream.writeDouble(this.zPosition);
		dataOutputStream.writeFloat(this.yaw);
		dataOutputStream.writeFloat(this.pitch);
		super.writePacket(dataOutputStream);
	}

	public int getPacketSize() {
		return 41;
	}
}
