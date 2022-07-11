package net.minecraft.src;

public class EntityCreeper extends EntityMob {
	int timeSinceIgnited;
	int lastActiveTime;
	int fuseDuration = 30;
	int creeperState = -1;

	public EntityCreeper(World var1) {
		super(var1);
		this.texture = "/mob/creeper.png";
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
	}

	protected void updateEntityActionState() {
		this.lastActiveTime = this.timeSinceIgnited;
		if(this.timeSinceIgnited > 0 && this.creeperState < 0) {
			--this.timeSinceIgnited;
		}

		if(this.creeperState >= 0) {
			this.creeperState = 2;
		}

		super.updateEntityActionState();
		if(this.creeperState != 1) {
			this.creeperState = -1;
		}

	}

	protected String getHurtSound() {
		return "mob.creeper";
	}

	protected String getDeathSound() {
		return "mob.creeperdeath";
	}

	public void onDeath(Entity entity) {
		super.onDeath(entity);
		if(entity instanceof EntitySkeleton) {
			this.dropItem(Item.record13.shiftedIndex + this.rand.nextInt(2), 1);
		}

	}

	protected void attackEntity(Entity entity, float damage) {
		if(this.creeperState <= 0 && damage < 3.0F || this.creeperState > 0 && damage < 7.0F) {
			if(this.timeSinceIgnited == 0) {
				this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
			}

			this.creeperState = 1;
			++this.timeSinceIgnited;
			if(this.timeSinceIgnited == this.fuseDuration) {
				this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 3.0F);
				this.setEntityDead();
			}

			this.hasAttacked = true;
		}

	}

	protected int getDropItemId() {
		return Item.gunpowder.shiftedIndex;
	}
}
