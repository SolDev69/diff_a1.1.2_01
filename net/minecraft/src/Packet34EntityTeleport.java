package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet34EntityTeleport extends Packet {
	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte yaw;
	public byte pitch;

	public Packet34EntityTeleport() {
	}

	public Packet34EntityTeleport(Entity entity) {
		this.entityId = entity.entityID;
		this.xPosition = MathHelper.floor_double(entity.posX * 32.0D);
		this.yPosition = MathHelper.floor_double(entity.posY * 32.0D);
		this.zPosition = MathHelper.floor_double(entity.posZ * 32.0D);
		this.yaw = (byte)((int)(entity.rotationYaw * 256.0F / 360.0F));
		this.pitch = (byte)((int)(entity.rotationPitch * 256.0F / 360.0F));
	}

	public Packet34EntityTeleport(int entityID, int xPosition, int yPosition, int zPosition, byte yaw, byte pitch) {
		this.entityId = entityID;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.zPosition = zPosition;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.entityId = dataInputStream.readInt();
		this.xPosition = dataInputStream.readInt();
		this.yPosition = dataInputStream.readInt();
		this.zPosition = dataInputStream.readInt();
		this.yaw = (byte)dataInputStream.read();
		this.pitch = (byte)dataInputStream.read();
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.entityId);
		dataOutputStream.writeInt(this.xPosition);
		dataOutputStream.writeInt(this.yPosition);
		dataOutputStream.writeInt(this.zPosition);
		dataOutputStream.write(this.yaw);
		dataOutputStream.write(this.pitch);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleEntityTeleport(this);
	}

	public int getPacketSize() {
		return 34;
	}
}
