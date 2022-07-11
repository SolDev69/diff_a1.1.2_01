package net.minecraft.src;

public class EntityGiantZombie extends EntityMob {
	public EntityGiantZombie(World var1) {
		super(var1);
		this.texture = "/mob/zombie.png";
		this.moveSpeed = 0.5F;
		this.attackStrength = 50;
		this.health *= 10;
		this.yOffset *= 6.0F;
		this.setSize(this.width * 6.0F, this.height * 6.0F);
	}

	protected float getBlockPathWeight(int x, int y, int z) {
		return this.worldObj.getBrightness(x, y, z) - 0.5F;
	}
}
