package net.minecraft.src;

public class EntityFallingSand extends Entity {
	public int blockID;
	public int fallTime = 0;

	public EntityFallingSand(World var1) {
		super(var1);
	}

	public EntityFallingSand(World world, float x, float y, float z, int blockID) {
		super(world);
		this.blockID = blockID;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.yOffset = this.height / 2.0F;
		this.setPosition((double)x, (double)y, (double)z);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.canTriggerWalking = false;
		this.prevPosX = (double)x;
		this.prevPosY = (double)y;
		this.prevPosZ = (double)z;
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public void onUpdate() {
		if(this.blockID == 0) {
			this.setEntityDead();
		} else {
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			++this.fallTime;
			this.motionY -= (double)0.04F;
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= (double)0.98F;
			this.motionY *= (double)0.98F;
			this.motionZ *= (double)0.98F;
			int var1 = MathHelper.floor_double(this.posX);
			int var2 = MathHelper.floor_double(this.posY);
			int var3 = MathHelper.floor_double(this.posZ);
			if(this.worldObj.getBlockId(var1, var2, var3) == this.blockID) {
				this.worldObj.setBlockWithNotify(var1, var2, var3, 0);
			}

			if(this.onGround) {
				this.motionX *= (double)0.7F;
				this.motionZ *= (double)0.7F;
				this.motionY *= -0.5D;
				this.setEntityDead();
				if(!this.worldObj.canBlockBePlacedAt(this.blockID, var1, var2, var3, true) || !this.worldObj.setBlockWithNotify(var1, var2, var3, this.blockID)) {
					this.dropItem(this.blockID, 1);
				}
			} else if(this.fallTime > 100) {
				this.dropItem(this.blockID, 1);
				this.setEntityDead();
			}

		}
	}

	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setByte("Tile", (byte)this.blockID);
	}

	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		this.blockID = nbttagcompound.getByte("Tile") & 255;
	}
}
