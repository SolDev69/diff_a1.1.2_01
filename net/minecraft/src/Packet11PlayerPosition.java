package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet11PlayerPosition extends Packet10Flying {
	public Packet11PlayerPosition() {
		this.moving = true;
	}

	public Packet11PlayerPosition(double x, double minY, double y, double z, boolean onGround) {
		this.xPosition = x;
		this.yPosition = minY;
		this.stance = y;
		this.zPosition = z;
		this.onGround = onGround;
		this.moving = true;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.xPosition = dataInputStream.readDouble();
		this.yPosition = dataInputStream.readDouble();
		this.stance = dataInputStream.readDouble();
		this.zPosition = dataInputStream.readDouble();
		super.readPacketData(dataInputStream);
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeDouble(this.xPosition);
		dataOutputStream.writeDouble(this.yPosition);
		dataOutputStream.writeDouble(this.stance);
		dataOutputStream.writeDouble(this.zPosition);
		super.writePacket(dataOutputStream);
	}

	public int getPacketSize() {
		return 33;
	}
}
