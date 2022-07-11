package net.minecraft.src;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.minecraft.server.MinecraftServer;

public class EntityPlayerMP extends EntityPlayer {
	public NetServerHandler playerNetServerHandler;
	public MinecraftServer mcServer;
	public ItemInWorldManager theItemInWorldManager;
	public double managedPosX;
	public double managedPosZ;
	public List loadedChunks = new LinkedList();
	public Set loadChunks = new HashSet();
	public double managedPosY;

	public EntityPlayerMP(MinecraftServer minecraftServer, World world, String username, ItemInWorldManager itemManager) {
		super(world);
		int var5 = world.spawnX + this.rand.nextInt(20) - 10;
		int var6 = world.spawnZ + this.rand.nextInt(20) - 10;
		int var7 = world.getTopSolidOrLiquidBlock(var5, var6);
		this.setLocationAndAngles((double)var5 + 0.5D, (double)var7, (double)var6 + 0.5D, 0.0F, 0.0F);
		this.mcServer = minecraftServer;
		this.stepHeight = 0.0F;
		itemManager.thisPlayer = this;
		this.username = username;
		this.theItemInWorldManager = itemManager;
		this.yOffset = 0.0F;
	}

	public void onUpdate() {
	}

	public void onDeath(Entity entity) {
	}

	public boolean attackEntityFrom(Entity entity, int damage) {
		return false;
	}

	public void heal(int health) {
	}

	public void onUpdateEntity() {
		super.onUpdate();
		ChunkCoordIntPair var1 = null;
		double var2 = 0.0D;

		for(int var4 = 0; var4 < this.loadedChunks.size(); ++var4) {
			ChunkCoordIntPair var5 = (ChunkCoordIntPair)this.loadedChunks.get(var4);
			double var6 = var5.a(this);
			if(var4 == 0 || var6 < var2) {
				var1 = var5;
				var2 = var5.a(this);
			}
		}

		if(var1 != null) {
			boolean var8 = false;
			if(var2 < 1024.0D) {
				var8 = true;
			}

			if(this.playerNetServerHandler.getNumChunkDataPackets() < 2) {
				var8 = true;
			}

			if(var8) {
				this.loadedChunks.remove(var1);
				this.playerNetServerHandler.sendPacket(new Packet51MapChunk(var1.chunkXPos * 16, 0, var1.chunkZPos * 16, 16, 128, 16, this.mcServer.worldMngr));
				List var9 = this.mcServer.worldMngr.getTileEntityList(var1.chunkXPos * 16, 0, var1.chunkZPos * 16, var1.chunkXPos * 16 + 16, 128, var1.chunkZPos * 16 + 16);

				for(int var10 = 0; var10 < var9.size(); ++var10) {
					TileEntity var7 = (TileEntity)var9.get(var10);
					this.playerNetServerHandler.sendPacket(new Packet59ComplexEntity(var7.xCoord, var7.yCoord, var7.zCoord, var7));
				}
			}
		}

	}

	public void onLivingUpdate() {
		this.motionX = this.motionY = this.motionZ = 0.0D;
		this.isJumping = false;
		super.onLivingUpdate();
	}

	public void onItemPickup(Entity entity, int var2) {
		if(!entity.isDead && entity instanceof EntityItem) {
			this.playerNetServerHandler.sendPacket(new Packet17AddToInventory(((EntityItem)entity).item, var2));
			this.mcServer.entityTracker.sendPacketToTrackedPlayers(entity, new Packet22Collect(entity.entityID, this.entityID));
		}

		super.onItemPickup(entity, var2);
	}

	public void swingItem() {
		if(!this.isSwinging) {
			this.swingProgressInt = -1;
			this.isSwinging = true;
			this.mcServer.entityTracker.sendPacketToTrackedPlayers(this, new Packet18ArmAnimation(this, 1));
		}

	}

	protected float getEyeHeight() {
		return 1.62F;
	}
}
