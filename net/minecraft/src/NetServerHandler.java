package net.minecraft.src;

import java.io.IOException;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

public class NetServerHandler extends NetHandler implements ICommandListener {
	public static Logger logger = Logger.getLogger("Minecraft");
	public NetworkManager netManager;
	public boolean connectionClosed = false;
	private MinecraftServer mcServer;
	private EntityPlayerMP playerEntity;
	private int playerInAirTime = 0;
	private double lastPosX;
	private double lastPosY;
	private double lastPosZ;
	private boolean hasMoved = true;
	private ItemStack heldItem = null;

	public NetServerHandler(MinecraftServer var1, NetworkManager var2, EntityPlayerMP var3) {
		this.mcServer = var1;
		this.netManager = var2;
		var2.setNetHandler(this);
		this.playerEntity = var3;
		var3.playerNetServerHandler = this;
	}

	public void handlePackets() throws IOException {
		this.netManager.processReadPackets();
		if(this.playerInAirTime++ % 20 == 0) {
			this.netManager.addToSendQueue(new Packet0KeepAlive());
		}

	}

	public void kickPlayer(String var1) {
		this.netManager.addToSendQueue(new Packet255KickDisconnect(var1));
		this.netManager.serverShutdown();
		this.mcServer.configManager.playerLoggedOut(this.playerEntity);
		this.connectionClosed = true;
	}

	public void handleFlying(Packet10Flying packet) {
		double var2;
		if(!this.hasMoved) {
			var2 = packet.yPosition - this.lastPosY;
			if(packet.xPosition == this.lastPosX && var2 * var2 < 0.01D && packet.zPosition == this.lastPosZ) {
				this.hasMoved = true;
			}
		}

		if(this.hasMoved) {
			this.lastPosX = this.playerEntity.posX;
			this.lastPosY = this.playerEntity.posY;
			this.lastPosZ = this.playerEntity.posZ;
			var2 = this.playerEntity.posX;
			double var4 = this.playerEntity.posY;
			double var6 = this.playerEntity.posZ;
			float var8 = this.playerEntity.rotationYaw;
			float var9 = this.playerEntity.rotationPitch;
			double var10;
			if(packet.moving) {
				var2 = packet.xPosition;
				var4 = packet.yPosition;
				var6 = packet.zPosition;
				var10 = packet.stance - packet.yPosition;
				if(var10 > 1.65D || var10 < 0.1D) {
					this.kickPlayer("Illegal stance");
					logger.warning(this.playerEntity.username + " had an illegal stance: " + var10);
				}

				this.playerEntity.managedPosY = packet.stance;
			}

			if(packet.rotating) {
				var8 = packet.yaw;
				var9 = packet.pitch;
			}

			this.playerEntity.onUpdateEntity();
			this.playerEntity.ySize = 0.0F;
			this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, var8, var9);
			var10 = var2 - this.playerEntity.posX;
			double var12 = var4 - this.playerEntity.posY;
			double var14 = var6 - this.playerEntity.posZ;
			float var16 = 0.0625F;
			boolean var17 = this.mcServer.worldMngr.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.boundingBox.copy().removeCoord((double)var16, (double)var16, (double)var16)).size() == 0;
			this.playerEntity.moveEntity(var10, var12, var14);
			var10 = var2 - this.playerEntity.posX;
			var12 = var4 - this.playerEntity.posY;
			if(var12 > -0.5D || var12 < 0.5D) {
				var12 = 0.0D;
			}

			var14 = var6 - this.playerEntity.posZ;
			double var18 = var10 * var10 + var12 * var12 + var14 * var14;
			boolean var20 = false;
			if(var18 > 0.0625D) {
				var20 = true;
				logger.warning(this.playerEntity.username + " moved wrongly!");
			}

			this.playerEntity.setPositionAndRotation(var2, var4, var6, var8, var9);
			boolean var21 = this.mcServer.worldMngr.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.boundingBox.copy().removeCoord((double)var16, (double)var16, (double)var16)).size() == 0;
			if(var17 && (var20 || !var21)) {
				this.teleportTo(this.lastPosX, this.lastPosY, this.lastPosZ, var8, var9);
				return;
			}

			this.playerEntity.onGround = packet.onGround;
			this.mcServer.configManager.serverUpdateMountedMovingPlayer(this.playerEntity);
		}

	}

	public void teleportTo(double var1, double var3, double var5, float var7, float var8) {
		this.hasMoved = false;
		this.lastPosX = var1;
		this.lastPosY = var3;
		this.lastPosZ = var5;
		this.playerEntity.setPositionAndRotation(var1, var3, var5, var7, var8);
		this.playerEntity.playerNetServerHandler.sendPacket(new Packet13PlayerLookMove(var1, var3 + (double)1.62F, var3, var5, var7, var8, false));
	}

	public void handleBlockDig(Packet14BlockDig packet) {
		this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = this.heldItem;
		boolean var2 = this.mcServer.worldMngr.disableSpawnProtection = this.mcServer.configManager.isOp(this.playerEntity.username);
		boolean var3 = false;
		if(packet.status == 0) {
			var3 = true;
		}

		if(packet.status == 1) {
			var3 = true;
		}

		if(var3) {
			double var4 = this.playerEntity.posY;
			this.playerEntity.posY = this.playerEntity.managedPosY;
			MovingObjectPosition var6 = this.playerEntity.rayTrace(4.0D, 1.0F);
			this.playerEntity.posY = var4;
			if(var6 == null) {
				return;
			}

			if(var6.blockX != packet.xPosition || var6.blockY != packet.yPosition || var6.blockZ != packet.zPosition || var6.sideHit != packet.face) {
				return;
			}
		}

		int var18 = packet.xPosition;
		int var5 = packet.yPosition;
		int var19 = packet.zPosition;
		int var7 = packet.face;
		int var8 = (int)MathHelper.abs((float)(var18 - this.mcServer.worldMngr.spawnX));
		int var9 = (int)MathHelper.abs((float)(var19 - this.mcServer.worldMngr.spawnZ));
		if(var8 > var9) {
			var9 = var8;
		}

		if(packet.status == 0) {
			if(var9 > 16 || var2) {
				this.playerEntity.theItemInWorldManager.onBlockClicked(var18, var5, var19);
			}
		} else if(packet.status == 2) {
			this.playerEntity.theItemInWorldManager.blockRemoving();
		} else if(packet.status == 1) {
			if(var9 > 16 || var2) {
				this.playerEntity.theItemInWorldManager.updateBlockRemoving(var18, var5, var19, var7);
			}
		} else if(packet.status == 3) {
			double var10 = this.playerEntity.posX - ((double)var18 + 0.5D);
			double var12 = this.playerEntity.posY - ((double)var5 + 0.5D);
			double var14 = this.playerEntity.posZ - ((double)var19 + 0.5D);
			double var16 = var10 * var10 + var12 * var12 + var14 * var14;
			if(var16 < 256.0D) {
				this.playerEntity.playerNetServerHandler.sendPacket(new Packet53BlockChange(var18, var5, var19, this.mcServer.worldMngr));
			}
		}

		this.mcServer.worldMngr.disableSpawnProtection = false;
	}

	public void handlePlace(Packet15Place packet) {
		boolean var2 = this.mcServer.worldMngr.disableSpawnProtection = this.mcServer.configManager.isOp(this.playerEntity.username);
		int var3 = packet.xPosition;
		int var4 = packet.yPosition;
		int var5 = packet.zPosition;
		int var6 = packet.direction;
		int var7 = (int)MathHelper.abs((float)(var3 - this.mcServer.worldMngr.spawnX));
		int var8 = (int)MathHelper.abs((float)(var5 - this.mcServer.worldMngr.spawnZ));
		if(var7 > var8) {
			var8 = var7;
		}

		if(var8 > 16 || var2) {
			ItemStack var9 = packet.id >= 0 ? new ItemStack(packet.id) : null;
			this.playerEntity.theItemInWorldManager.activeBlockOrUseItem(this.playerEntity, this.mcServer.worldMngr, var9, var3, var4, var5, var6);
		}

		this.playerEntity.playerNetServerHandler.sendPacket(new Packet53BlockChange(var3, var4, var5, this.mcServer.worldMngr));
		this.mcServer.worldMngr.disableSpawnProtection = false;
	}

	public void handleErrorMessage(String message) {
		logger.info(this.playerEntity.username + " lost connection: " + message);
		this.mcServer.configManager.playerLoggedOut(this.playerEntity);
		this.connectionClosed = true;
	}

	public void registerPacket(Packet packet) {
		logger.warning(this.getClass() + " wasn\'t prepared to deal with a " + packet.getClass());
		this.kickPlayer("Protocol error, unexpected packet");
	}

	public void sendPacket(Packet var1) {
		this.netManager.addToSendQueue(var1);
	}

	public void handleBlockItemSwitch(Packet16BlockItemSwitch packet) {
		int var2 = packet.id;
		this.playerEntity.inventory.currentItem = this.playerEntity.inventory.mainInventory.length - 1;
		if(var2 == 0) {
			this.heldItem = null;
		} else {
			this.heldItem = new ItemStack(var2);
		}

		this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = this.heldItem;
		this.mcServer.entityTracker.sendPacketToTrackedPlayers(this.playerEntity, new Packet16BlockItemSwitch(this.playerEntity.entityID, var2));
	}

	public void handlePickupSpawn(Packet21PickupSpawn packet) {
		double var2 = (double)packet.xPosition / 32.0D;
		double var4 = (double)packet.yPosition / 32.0D;
		double var6 = (double)packet.zPosition / 32.0D;
		EntityItem var8 = new EntityItem(this.mcServer.worldMngr, var2, var4, var6, new ItemStack(packet.itemID, packet.count));
		var8.motionX = (double)packet.rotation / 128.0D;
		var8.motionY = (double)packet.pitch / 128.0D;
		var8.motionZ = (double)packet.roll / 128.0D;
		var8.delayBeforeCanPickup = 10;
		this.mcServer.worldMngr.spawnEntityInWorld(var8);
	}

	public void handleChat(Packet3Chat packet) {
		String var2 = packet.message;
		if(var2.length() > 100) {
			this.kickPlayer("Chat message too long");
		} else {
			var2 = var2.trim();

			for(int var3 = 0; var3 < var2.length(); ++var3) {
				if(" !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(var2.charAt(var3)) < 0) {
					this.kickPlayer("Illegal characters in chat");
					return;
				}
			}

			if(var2.startsWith("/")) {
				this.handleSlashCommand(var2);
			} else {
				var2 = "<" + this.playerEntity.username + "> " + var2;
				logger.info(var2);
				this.mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat(var2));
			}

		}
	}

	private void handleSlashCommand(String var1) {
		if(var1.toLowerCase().startsWith("/me ")) {
			var1 = "* " + this.playerEntity.username + " " + var1.substring(var1.indexOf(" ")).trim();
			logger.info(var1);
			this.mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat(var1));
		} else if(var1.toLowerCase().startsWith("/tell ")) {
			String[] var2 = var1.split(" ");
			if(var2.length >= 3) {
				var1 = var1.substring(var1.indexOf(" ")).trim();
				var1 = var1.substring(var1.indexOf(" ")).trim();
				var1 = "\u00a77" + this.playerEntity.username + " whispers " + var1;
				logger.info(var1 + " to " + var2[1]);
				if(!this.mcServer.configManager.sendPacketToPlayer(var2[1], new Packet3Chat(var1))) {
					this.sendPacket(new Packet3Chat("\u00a7cThere\'s no player by that name online."));
				}
			}
		} else {
			int var3;
			if(var1.toLowerCase().equalsIgnoreCase("/home")) {
				logger.info(this.playerEntity.username + " returned home");
				var3 = this.mcServer.worldMngr.getTopSolidOrLiquidBlock(this.mcServer.worldMngr.spawnX, this.mcServer.worldMngr.spawnZ);
				this.teleportTo((double)this.mcServer.worldMngr.spawnX + 0.5D, (double)var3 + 1.5D, (double)this.mcServer.worldMngr.spawnZ + 0.5D, 0.0F, 0.0F);
			} else if(var1.toLowerCase().equalsIgnoreCase("/iron")) {
				if(MinecraftServer.playerList.containsKey(this.playerEntity.username)) {
					logger.info(this.playerEntity.username + " failed to iron!");
					this.sendPacket(new Packet3Chat("\u00a7cYou can\'t /iron again so soon!"));
				} else {
					MinecraftServer.playerList.put(this.playerEntity.username, Integer.valueOf(6000));
					logger.info(this.playerEntity.username + " ironed!");

					for(var3 = 0; var3 < 4; ++var3) {
						this.playerEntity.dropPlayerItem(new ItemStack(Item.ingotIron, 1));
					}
				}
			} else if(var1.toLowerCase().equalsIgnoreCase("/wood")) {
				if(MinecraftServer.playerList.containsKey(this.playerEntity.username)) {
					logger.info(this.playerEntity.username + " failed to wood!");
					this.sendPacket(new Packet3Chat("\u00a7cYou can\'t /wood again so soon!"));
				} else {
					MinecraftServer.playerList.put(this.playerEntity.username, Integer.valueOf(6000));
					logger.info(this.playerEntity.username + " wooded!");

					for(var3 = 0; var3 < 4; ++var3) {
						this.playerEntity.dropPlayerItem(new ItemStack(Block.sapling, 1));
					}
				}
			} else {
				String var4;
				if(this.mcServer.configManager.isOp(this.playerEntity.username)) {
					var4 = var1.substring(1);
					logger.info(this.playerEntity.username + " issued server command: " + var4);
					this.mcServer.addCommand(var4, this);
				} else {
					var4 = var1.substring(1);
					logger.info(this.playerEntity.username + " tried command: " + var4);
				}
			}
		}

	}

	public void handleArmAnimation(Packet18ArmAnimation packet) {
		if(packet.animate == 1) {
			this.playerEntity.swingItem();
		}

	}

	public void handleKickDisconnect(Packet255KickDisconnect packet) {
		this.netManager.networkShutdown("Quitting");
	}

	public int getNumChunkDataPackets() {
		return this.netManager.getNumChunkDataPackets();
	}

	public void addHelpCommandMessage(String helpCommandMessage) {
		this.sendPacket(new Packet3Chat("\u00a77" + helpCommandMessage));
	}

	public String getUsername() {
		return this.playerEntity.username;
	}

	public void handlePlayerInventory(Packet5PlayerInventory packet) {
		if(packet.inventoryType == -1) {
			this.playerEntity.inventory.mainInventory = packet.inventory;
		}

		if(packet.inventoryType == -2) {
			this.playerEntity.inventory.craftingInventory = packet.inventory;
		}

		if(packet.inventoryType == -3) {
			this.playerEntity.inventory.armorInventory = packet.inventory;
		}

	}

	public void sendInventoryPackets() {
		this.netManager.addToSendQueue(new Packet5PlayerInventory(-1, this.playerEntity.inventory.mainInventory));
		this.netManager.addToSendQueue(new Packet5PlayerInventory(-2, this.playerEntity.inventory.craftingInventory));
		this.netManager.addToSendQueue(new Packet5PlayerInventory(-3, this.playerEntity.inventory.armorInventory));
	}

	public void handleComplexEntity(Packet59ComplexEntity packet) {
		TileEntity var2 = this.mcServer.worldMngr.getBlockTileEntity(packet.xCoord, packet.yCoord, packet.zCoord);
		if(var2 != null) {
			var2.readFromNBT(packet.tileEntityNBT);
			var2.onInventoryChanged();
		}

	}
}
