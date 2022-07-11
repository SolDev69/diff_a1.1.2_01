package net.minecraft.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.MinecraftServer;

public class PlayerManager {
	private List players = new ArrayList();
	private MCHashTable2 playerInstances = new MCHashTable2();
	private List playerInstancesToUpdate = new ArrayList();
	private MinecraftServer mcServer;

	public PlayerManager(MinecraftServer mcServer) {
		this.mcServer = mcServer;
	}

	public void updatePlayerInstances() throws IOException {
		for(int var1 = 0; var1 < this.playerInstancesToUpdate.size(); ++var1) {
			((PlayerInstance)this.playerInstancesToUpdate.get(var1)).onUpdate();
		}

		this.playerInstancesToUpdate.clear();
	}

	private PlayerInstance getPlayerInstance(int var1, int var2, boolean var3) {
		long var4 = (long)var1 + 2147483647L | (long)var2 + 2147483647L << 32;
		PlayerInstance var6 = (PlayerInstance)this.playerInstances.lookup(var4);
		if(var6 == null && var3) {
			var6 = new PlayerInstance(this, var1, var2);
			this.playerInstances.addKey(var4, var6);
		}

		return var6;
	}

	public void sendTileEntity(Packet packet, int x, int y, int z) {
		int var5 = x >> 4;
		int var6 = z >> 4;
		PlayerInstance var7 = this.getPlayerInstance(var5, var6, false);
		if(var7 != null) {
			var7.sendTileEntity(packet);
		}

	}

	public void markBlockNeedsUpdate(int x, int y, int z) {
		int var4 = x >> 4;
		int var5 = z >> 4;
		PlayerInstance var6 = this.getPlayerInstance(var4, var5, false);
		if(var6 != null) {
			var6.markBlockNeedsUpdate(x & 15, y, z & 15);
		}

	}

	public void addPlayer(EntityPlayerMP entityPlayerMP) {
		this.mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat("\u00a7e" + entityPlayerMP.username + " joined the game."));
		int var2 = (int)entityPlayerMP.posX >> 4;
		int var3 = (int)entityPlayerMP.posZ >> 4;
		entityPlayerMP.managedPosX = entityPlayerMP.posX;
		entityPlayerMP.managedPosZ = entityPlayerMP.posZ;

		for(int var4 = var2 - 10; var4 <= var2 + 10; ++var4) {
			for(int var5 = var3 - 10; var5 <= var3 + 10; ++var5) {
				this.getPlayerInstance(var4, var5, true).addPlayer(entityPlayerMP);
			}
		}

		this.players.add(entityPlayerMP);
	}

	public void removePlayer(EntityPlayerMP entityPlayerMP) {
		this.mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat("\u00a7e" + entityPlayerMP.username + " left the game."));
		int var2 = (int)entityPlayerMP.posX >> 4;
		int var3 = (int)entityPlayerMP.posZ >> 4;

		for(int var4 = var2 - 10; var4 <= var2 + 10; ++var4) {
			for(int var5 = var3 - 10; var5 <= var3 + 10; ++var5) {
				PlayerInstance var6 = this.getPlayerInstance(var4, var5, false);
				if(var6 != null) {
					var6.removePlayer(entityPlayerMP);
				}
			}
		}

		this.players.remove(entityPlayerMP);
	}

	private boolean a(int var1, int var2, int var3, int var4) {
		int var5 = var1 - var3;
		int var6 = var2 - var4;
		return var5 >= -10 && var5 <= 10 ? var6 >= -10 && var6 <= 10 : false;
	}

	public void updateMountedMovingPlayer(EntityPlayerMP entityPlayerMP) {
		int var2 = (int)entityPlayerMP.posX >> 4;
		int var3 = (int)entityPlayerMP.posZ >> 4;
		double var4 = entityPlayerMP.managedPosX - entityPlayerMP.posX;
		double var6 = entityPlayerMP.managedPosZ - entityPlayerMP.posZ;
		double var8 = var4 * var4 + var6 * var6;
		if(var8 >= 64.0D) {
			int var10 = (int)entityPlayerMP.managedPosX >> 4;
			int var11 = (int)entityPlayerMP.managedPosZ >> 4;
			int var12 = var2 - var10;
			int var13 = var3 - var11;
			if(var12 != 0 || var13 != 0) {
				for(int var14 = var2 - 10; var14 <= var2 + 10; ++var14) {
					for(int var15 = var3 - 10; var15 <= var3 + 10; ++var15) {
						if(!this.a(var14, var15, var10, var11)) {
							this.getPlayerInstance(var14, var15, true).addPlayer(entityPlayerMP);
						}

						if(!this.a(var14 - var12, var15 - var13, var2, var3)) {
							PlayerInstance var16 = this.getPlayerInstance(var14 - var12, var15 - var13, false);
							if(var16 != null) {
								var16.removePlayer(entityPlayerMP);
							}
						}
					}
				}

				entityPlayerMP.managedPosX = entityPlayerMP.posX;
				entityPlayerMP.managedPosZ = entityPlayerMP.posZ;
			}
		}
	}

	public int getMaxTrackingDistance() {
		return 144;
	}

	static MinecraftServer getMinecraftServer(PlayerManager playerManager) {
		return playerManager.mcServer;
	}

	static MCHashTable2 getPlayerInstances(PlayerManager playerManager) {
		return playerManager.playerInstances;
	}

	static List getPlayerInstancesToUpdate(PlayerManager playerManager) {
		return playerManager.playerInstancesToUpdate;
	}
}
