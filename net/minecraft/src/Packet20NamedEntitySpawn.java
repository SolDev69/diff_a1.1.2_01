package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet20NamedEntitySpawn extends Packet {
	public int entityId;
	public String name;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte rotation;
	public byte pitch;
	public int currentItem;

	public Packet20NamedEntitySpawn() {
	}

	public Packet20NamedEntitySpawn(EntityPlayer entityPlayer) {
		this.entityId = entityPlayer.entityID;
		this.name = entityPlayer.username;
		this.xPosition = MathHelper.floor_double(entityPlayer.posX * 32.0D);
		this.yPosition = MathHelper.floor_double(entityPlayer.posY * 32.0D);
		this.zPosition = MathHelper.floor_double(entityPlayer.posZ * 32.0D);
		this.rotation = (byte)((int)(entityPlayer.rotationYaw * 256.0F / 360.0F));
		this.pitch = (byte)((int)(entityPlayer.rotationPitch * 256.0F / 360.0F));
		ItemStack var2 = entityPlayer.inventory.getCurrentItem();
		this.currentItem = var2 == null ? 0 : var2.itemID;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.entityId = dataInputStream.readInt();
		this.name = dataInputStream.readUTF();
		this.xPosition = dataInputStream.readInt();
		this.yPosition = dataInputStream.readInt();
		this.zPosition = dataInputStream.readInt();
		this.rotation = dataInputStream.readByte();
		this.pitch = dataInputStream.readByte();
		this.currentItem = dataInputStream.readShort();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.entityId);
		dataOutputStream.writeUTF(this.name);
		dataOutputStream.writeInt(this.xPosition);
		dataOutputStream.writeInt(this.yPosition);
		dataOutputStream.writeInt(this.zPosition);
		dataOutputStream.writeByte(this.rotation);
		dataOutputStream.writeByte(this.pitch);
		dataOutputStream.writeShort(this.currentItem);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleNamedEntitySpawn(this);
	}

	public int getPacketSize() {
		return 28;
	}
}
