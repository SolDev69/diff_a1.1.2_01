package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet24MobSpawn extends Packet {
	public int entityId;
	public byte type;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte yaw;
	public byte pitch;

	public Packet24MobSpawn() {
	}

	public Packet24MobSpawn(EntityLiving entityLiving) {
		this.entityId = entityLiving.entityID;
		this.type = (byte)EntityList.getEntityID(entityLiving);
		this.xPosition = MathHelper.floor_double(entityLiving.posX * 32.0D);
		this.yPosition = MathHelper.floor_double(entityLiving.posY * 32.0D);
		this.zPosition = MathHelper.floor_double(entityLiving.posZ * 32.0D);
		this.yaw = (byte)((int)(entityLiving.rotationYaw * 256.0F / 360.0F));
		this.pitch = (byte)((int)(entityLiving.rotationPitch * 256.0F / 360.0F));
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.entityId = dataInputStream.readInt();
		this.type = dataInputStream.readByte();
		this.xPosition = dataInputStream.readInt();
		this.yPosition = dataInputStream.readInt();
		this.zPosition = dataInputStream.readInt();
		this.yaw = dataInputStream.readByte();
		this.pitch = dataInputStream.readByte();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.entityId);
		dataOutputStream.writeByte(this.type);
		dataOutputStream.writeInt(this.xPosition);
		dataOutputStream.writeInt(this.yPosition);
		dataOutputStream.writeInt(this.zPosition);
		dataOutputStream.writeByte(this.yaw);
		dataOutputStream.writeByte(this.pitch);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleMobSpawn(this);
	}

	public int getPacketSize() {
		return 19;
	}
}
