package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet5PlayerInventory extends Packet {
	public int inventoryType;
	public ItemStack[] inventory;

	public Packet5PlayerInventory() {
	}

	public Packet5PlayerInventory(int invType, ItemStack[] invContents) {
		this.inventoryType = invType;
		this.inventory = new ItemStack[invContents.length];

		for(int var3 = 0; var3 < this.inventory.length; ++var3) {
			this.inventory[var3] = invContents[var3] == null ? null : invContents[var3].copy();
		}

	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.inventoryType = dataInputStream.readInt();
		short var2 = dataInputStream.readShort();
		this.inventory = new ItemStack[var2];

		for(int var3 = 0; var3 < var2; ++var3) {
			short var4 = dataInputStream.readShort();
			if(var4 >= 0) {
				byte var5 = dataInputStream.readByte();
				short var6 = dataInputStream.readShort();
				this.inventory[var3] = new ItemStack(var4, var5, var6);
			}
		}

	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.inventoryType);
		dataOutputStream.writeShort(this.inventory.length);

		for(int var2 = 0; var2 < this.inventory.length; ++var2) {
			if(this.inventory[var2] == null) {
				dataOutputStream.writeShort(-1);
			} else {
				dataOutputStream.writeShort((short)this.inventory[var2].itemID);
				dataOutputStream.writeByte((byte)this.inventory[var2].stackSize);
				dataOutputStream.writeShort((short)this.inventory[var2].itemDmg);
			}
		}

	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handlePlayerInventory(this);
	}

	public int getPacketSize() {
		return 6 + this.inventory.length * 5;
	}
}
