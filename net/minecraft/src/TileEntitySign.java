package net.minecraft.src;

public class TileEntitySign extends TileEntity {
	public String[] signText = new String[]{"", "", "", ""};
	public int lineBeingEdited = -1;

	public void writeToNBT(NBTTagCompound compoundTag) {
		super.writeToNBT(compoundTag);
		compoundTag.setString("Text1", this.signText[0]);
		compoundTag.setString("Text2", this.signText[1]);
		compoundTag.setString("Text3", this.signText[2]);
		compoundTag.setString("Text4", this.signText[3]);
	}

	public void readFromNBT(NBTTagCompound compoundTag) {
		super.readFromNBT(compoundTag);

		for(int var2 = 0; var2 < 4; ++var2) {
			this.signText[var2] = compoundTag.getString("Text" + (var2 + 1));
			if(this.signText[var2].length() > 15) {
				this.signText[var2] = this.signText[var2].substring(0, 15);
			}
		}

	}
}
