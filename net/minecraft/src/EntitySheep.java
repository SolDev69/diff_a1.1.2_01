package net.minecraft.src;

public class EntitySheep extends EntityAnimal {
	public boolean sheared = false;

	public EntitySheep(World var1) {
		super(var1);
		this.texture = "/mob/sheep.png";
		this.setSize(0.9F, 1.3F);
	}

	public boolean attackEntityFrom(Entity entity, int damage) {
		if(!this.sheared && entity instanceof EntityLiving) {
			this.sheared = true;
			int var3 = 1 + this.rand.nextInt(3);

			for(int var4 = 0; var4 < var3; ++var4) {
				EntityItem var5 = this.entityDropItem(Block.cloth.blockID, 1, 1.0F);
				var5.motionY += (double)(this.rand.nextFloat() * 0.05F);
				var5.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
				var5.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
			}
		}

		return super.attackEntityFrom(entity, damage);
	}

	public void writeEntityToNBT(NBTTagCompound compoundTag) {
		super.writeEntityToNBT(compoundTag);
		compoundTag.setBoolean("Sheared", this.sheared);
	}

	public void readEntityFromNBT(NBTTagCompound compoundTag) {
		super.readEntityFromNBT(compoundTag);
		this.sheared = compoundTag.getBoolean("Sheared");
	}

	protected String getLivingSound() {
		return "mob.sheep";
	}

	protected String getHurtSound() {
		return "mob.sheep";
	}

	protected String getDeathSound() {
		return "mob.sheep";
	}
}
