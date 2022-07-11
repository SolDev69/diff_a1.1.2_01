package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Logger;

public class PlayerNBTManager {
	public static Logger logger = Logger.getLogger("Minecraft");
	private File playerNBT;

	public PlayerNBTManager(File playerNBT) {
		this.playerNBT = playerNBT;
		playerNBT.mkdir();
	}

	public void writePlayerNBT(EntityPlayerMP entityPlayerMP) {
		try {
			NBTTagCompound var2 = new NBTTagCompound();
			entityPlayerMP.writeToNBT(var2);
			File var3 = new File(this.playerNBT, "_tmp_.dat");
			File var4 = new File(this.playerNBT, entityPlayerMP.username + ".dat");
			CompressedStreamTools.writeCompressed(var2, new FileOutputStream(var3));
			if(var4.exists()) {
				var4.delete();
			}

			var3.renameTo(var4);
		} catch (Exception var5) {
			logger.warning("Failed to save player data for " + entityPlayerMP.username);
		}

	}

	public void readPlayerNBT(EntityPlayerMP entityPlayerMP) {
		try {
			File var2 = new File(this.playerNBT, entityPlayerMP.username + ".dat");
			if(var2.exists()) {
				NBTTagCompound var3 = CompressedStreamTools.readCompressed(new FileInputStream(var2));
				if(var3 != null) {
					entityPlayerMP.readFromNBT(var3);
				}
			}
		} catch (Exception var4) {
			logger.warning("Failed to load player data for " + entityPlayerMP.username);
		}

	}
}
