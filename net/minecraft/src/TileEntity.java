package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class TileEntity {
	private static Map nameToClassMap = new HashMap();
	private static Map classToNameMap = new HashMap();
	public World worldObj;
	public int xCoord;
	public int yCoord;
	public int zCoord;

	private static void addMapping(Class clazz, String tileEntityName) {
		if(classToNameMap.containsKey(tileEntityName)) {
			throw new IllegalArgumentException("Duplicate id: " + tileEntityName);
		} else {
			nameToClassMap.put(tileEntityName, clazz);
			classToNameMap.put(clazz, tileEntityName);
		}
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		this.xCoord = nbttagcompound.getInteger("x");
		this.yCoord = nbttagcompound.getInteger("y");
		this.zCoord = nbttagcompound.getInteger("z");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		String var2 = (String)classToNameMap.get(this.getClass());
		if(var2 == null) {
			throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
		} else {
			nbttagcompound.setString("id", var2);
			nbttagcompound.setInteger("x", this.xCoord);
			nbttagcompound.setInteger("y", this.yCoord);
			nbttagcompound.setInteger("z", this.zCoord);
		}
	}

	public void updateEntity() {
	}

	public static TileEntity createAndLoadEntity(NBTTagCompound nbttagcompound) {
		TileEntity var1 = null;

		try {
			Class var2 = (Class)nameToClassMap.get(nbttagcompound.getString("id"));
			if(var2 != null) {
				var1 = (TileEntity)var2.newInstance();
			}
		} catch (Exception var3) {
			var3.printStackTrace();
		}

		if(var1 != null) {
			var1.readFromNBT(nbttagcompound);
		} else {
			System.out.println("Skipping TileEntity with id " + nbttagcompound.getString("id"));
		}

		return var1;
	}

	public void onInventoryChanged() {
		this.worldObj.updateTileEntityChunkAndDoNothing(this.xCoord, this.yCoord, this.zCoord, this);
	}

	static {
		addMapping(TileEntityFurnace.class, "Furnace");
		addMapping(TileEntityChest.class, "Chest");
		addMapping(TileEntitySign.class, "Sign");
		addMapping(TileEntityMobSpawner.class, "MobSpawner");
	}
}
