package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class WorldManager implements IWorldAccess {
	private MinecraftServer mcServer;

	public WorldManager(MinecraftServer var1) {
		this.mcServer = var1;
	}

	public void spawnParticle(String name, double x, double y, double z, double var8, double var10, double var12) {
	}

	public void obtainEntitySkin(Entity entity) {
		this.mcServer.entityTracker.trackEntity(entity);
	}

	public void releaseEntitySkin(Entity entity) {
		this.mcServer.entityTracker.untrackEntity(entity);
	}

	public void playSound(String name, double x, double y, double z, float volume, float pitch) {
	}

	public void markBlockRangeNeedsUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
	}

	public void updateAllRenderers() {
	}

	public void markBlockAndNeighborsNeedsUpdate(int x, int y, int z) {
		this.mcServer.configManager.markBlockNeedsUpdate(x, y, z);
	}

	public void playRecord(String name, int x, int y, int z) {
	}

	public void doNothingWithTileEntity(int x, int y, int z, TileEntity tileEntity) {
		this.mcServer.configManager.sentTileEntityToPlayer(x, y, z, tileEntity);
	}
}
