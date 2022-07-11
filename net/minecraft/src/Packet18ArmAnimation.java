package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet18ArmAnimation extends Packet {
	public int entityId;
	public int animate;

	public Packet18ArmAnimation() {
	}

	public Packet18ArmAnimation(Entity entity, int animate) {
		this.entityId = entity.entityID;
		this.animate = animate;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.entityId = dataInputStream.readInt();
		this.animate = dataInputStream.readByte();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.entityId);
		dataOutputStream.writeByte(this.animate);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleArmAnimation(this);
	}

	public int getPacketSize() {
		return 5;
	}
}
