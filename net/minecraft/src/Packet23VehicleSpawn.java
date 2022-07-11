package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet23VehicleSpawn extends Packet {
	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int type;

	public Packet23VehicleSpawn() {
	}

	public Packet23VehicleSpawn(Entity entity, int type) {
		this.entityId = entity.entityID;
		this.xPosition = MathHelper.floor_double(entity.posX * 32.0D);
		this.yPosition = MathHelper.floor_double(entity.posY * 32.0D);
		this.zPosition = MathHelper.floor_double(entity.posZ * 32.0D);
		this.type = type;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.entityId = dataInputStream.readInt();
		this.type = dataInputStream.readByte();
		this.xPosition = dataInputStream.readInt();
		this.yPosition = dataInputStream.readInt();
		this.zPosition = dataInputStream.readInt();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.entityId);
		dataOutputStream.writeByte(this.type);
		dataOutputStream.writeInt(this.xPosition);
		dataOutputStream.writeInt(this.yPosition);
		dataOutputStream.writeInt(this.zPosition);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleVehicleSpawn(this);
	}

	public int getPacketSize() {
		return 17;
	}
}
