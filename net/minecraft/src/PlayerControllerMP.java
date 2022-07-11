package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerControllerMP extends PlayerController {
	private int currentBlockX = -1;
	private int currentBlockY = -1;
	private int currentBlockZ = -1;
	private float curBlockDamageMP = 0.0F;
	private float prevBlockDamageMP = 0.0F;
	private float stepSoundTickCounter = 0.0F;
	private int blockHitDelay = 0;
	private boolean isHittingBlock = false;
	private NetClientHandler netClientHandler;
	private int currentPlayerItem = 0;

	public PlayerControllerMP(Minecraft minecraft, NetClientHandler netClientHandler) {
		super(minecraft);
		this.netClientHandler = netClientHandler;
	}

	public void flipPlayer(EntityPlayer entityPlayer) {
		entityPlayer.rotationYaw = -180.0F;
	}

	public boolean sendBlockRemoved(int x, int y, int z, int side) {
		this.netClientHandler.addToSendQueue(new Packet14BlockDig(3, x, y, z, side));
		int var5 = this.mc.theWorld.getBlockId(x, y, z);
		int var6 = this.mc.theWorld.getBlockMetadata(x, y, z);
		boolean var7 = super.sendBlockRemoved(x, y, z, side);
		ItemStack var8 = this.mc.thePlayer.getCurrentEquippedItem();
		if(var8 != null) {
			var8.onDestroyBlock(var5, x, y, z);
			if(var8.stackSize == 0) {
				var8.onItemDestroyedByUse(this.mc.thePlayer);
				this.mc.thePlayer.destroyCurrentEquippedItem();
			}
		}

		if(var7 && this.mc.thePlayer.canHarvestBlock(Block.blocksList[var5])) {
			Block.blocksList[var5].dropBlockAsItem(this.mc.theWorld, x, y, z, var6);
		}

		return var7;
	}

	public void clickBlock(int x, int y, int z, int side) {
		this.isHittingBlock = true;
		this.netClientHandler.addToSendQueue(new Packet14BlockDig(0, x, y, z, side));
		int var5 = this.mc.theWorld.getBlockId(x, y, z);
		if(var5 > 0 && this.curBlockDamageMP == 0.0F) {
			Block.blocksList[var5].onBlockClicked(this.mc.theWorld, x, y, z, this.mc.thePlayer);
		}

		if(var5 > 0 && Block.blocksList[var5].blockStrength(this.mc.thePlayer) >= 1.0F) {
			this.sendBlockRemoved(x, y, z, side);
		}

	}

	public void resetBlockRemoving() {
		if(this.isHittingBlock) {
			this.isHittingBlock = false;
			this.netClientHandler.addToSendQueue(new Packet14BlockDig(2, 0, 0, 0, 0));
			this.curBlockDamageMP = 0.0F;
			this.blockHitDelay = 0;
		}
	}

	public void sendBlockRemoving(int x, int y, int z, int side) {
		this.isHittingBlock = true;
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet14BlockDig(1, x, y, z, side));
		if(this.blockHitDelay > 0) {
			--this.blockHitDelay;
		} else {
			if(x == this.currentBlockX && y == this.currentBlockY && z == this.currentBlockZ) {
				int var5 = this.mc.theWorld.getBlockId(x, y, z);
				if(var5 == 0) {
					return;
				}

				Block var6 = Block.blocksList[var5];
				this.curBlockDamageMP += var6.blockStrength(this.mc.thePlayer);
				if(this.stepSoundTickCounter % 4.0F == 0.0F && var6 != null) {
					this.mc.sndManager.playSound(var6.stepSound.getStepSound(), (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, (var6.stepSound.getVolume() + 1.0F) / 8.0F, var6.stepSound.getPitch() * 0.5F);
				}

				++this.stepSoundTickCounter;
				if(this.curBlockDamageMP >= 1.0F) {
					this.sendBlockRemoved(x, y, z, side);
					this.curBlockDamageMP = 0.0F;
					this.prevBlockDamageMP = 0.0F;
					this.stepSoundTickCounter = 0.0F;
					this.blockHitDelay = 5;
				}
			} else {
				this.curBlockDamageMP = 0.0F;
				this.prevBlockDamageMP = 0.0F;
				this.stepSoundTickCounter = 0.0F;
				this.currentBlockX = x;
				this.currentBlockY = y;
				this.currentBlockZ = z;
			}

		}
	}

	public void setPartialTime(float renderPartialTick) {
		if(this.curBlockDamageMP <= 0.0F) {
			this.mc.ingameGUI.damageGuiPartialTime = 0.0F;
			this.mc.renderGlobal.damagePartialTime = 0.0F;
		} else {
			float var2 = this.prevBlockDamageMP + (this.curBlockDamageMP - this.prevBlockDamageMP) * renderPartialTick;
			this.mc.ingameGUI.damageGuiPartialTime = var2;
			this.mc.renderGlobal.damagePartialTime = var2;
		}

	}

	public float getBlockReachDistance() {
		return 4.0F;
	}

	public void onWorldChange(World world) {
		super.onWorldChange(world);
	}

	public void onUpdate() {
		this.syncCurrentPlayItem();
		this.prevBlockDamageMP = this.curBlockDamageMP;
	}

	private void syncCurrentPlayItem() {
		ItemStack var1 = this.mc.thePlayer.inventory.getCurrentItem();
		int var2 = 0;
		if(var1 != null) {
			var2 = var1.itemID;
		}

		if(var2 != this.currentPlayerItem) {
			this.currentPlayerItem = var2;
			this.netClientHandler.addToSendQueue(new Packet16BlockItemSwitch(0, this.currentPlayerItem));
		}

	}

	public boolean onPlayerRightClick(EntityPlayer entityPlayer, World world, ItemStack itemStack, int x, int y, int z, int side) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet15Place(itemStack != null ? itemStack.itemID : -1, x, y, z, side));
		return super.onPlayerRightClick(entityPlayer, world, itemStack, x, y, z, side);
	}

	public EntityPlayer createPlayer(World world) {
		return new EntityClientPlayerMP(this.mc, world, this.mc.session, this.netClientHandler);
	}
}
