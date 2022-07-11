package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet21PickupSpawn extends Packet {
	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte rotation;
	public byte pitch;
	public byte roll;
	public int itemID;
	public int count;

	public Packet21PickupSpawn() {
	}

	public Packet21PickupSpawn(EntityItem entityItem) {
		this.entityId = entityItem.entityID;
		this.itemID = entityItem.item.itemID;
		this.count = entityItem.item.stackSize;
		this.xPosition = MathHelper.floor_double(entityItem.posX * 32.0D);
		this.yPosition = MathHelper.floor_double(entityItem.posY * 32.0D);
		this.zPosition = MathHelper.floor_double(entityItem.posZ * 32.0D);
		this.rotation = (byte)((int)(entityItem.motionX * 128.0D));
		this.pitch = (byte)((int)(entityItem.motionY * 128.0D));
		this.roll = (byte)((int)(entityItem.motionZ * 128.0D));
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.entityId = dataInputStream.readInt();
		this.itemID = dataInputStream.readShort();
		this.count = dataInputStream.readByte();
		this.xPosition = dataInputStream.readInt();
		this.yPosition = dataInputStream.readInt();
		this.zPosition = dataInputStream.readInt();
		this.rotation = dataInputStream.readByte();
		this.pitch = dataInputStream.readByte();
		this.roll = dataInputStream.readByte();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.entityId);
		dataOutputStream.writeShort(this.itemID);
		dataOutputStream.writeByte(this.count);
		dataOutputStream.writeInt(this.xPosition);
		dataOutputStream.writeInt(this.yPosition);
		dataOutputStream.writeInt(this.zPosition);
		dataOutputStream.writeByte(this.rotation);
		dataOutputStream.writeByte(this.pitch);
		dataOutputStream.writeByte(this.roll);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handlePickupSpawn(this);
	}

	public int getPacketSize() {
		return 22;
	}
}
