package net.minecraft.src;

public class MetadataChunkBlock {
	public final EnumSkyBlock skyBlock;
	public int x;
	public int y;
	public int z;
	public int maxX;
	public int maxY;
	public int maxZ;

	public MetadataChunkBlock(EnumSkyBlock skyBlock, int x, int y, int z, int maxX, int maxY, int maxZ) {
		this.skyBlock = skyBlock;
		this.x = x;
		this.y = y;
		this.z = z;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public void updateLight(World world) {
		int var2 = this.maxX - this.x;
		int var3 = this.maxY - this.y;
		int var4 = this.maxZ - this.z;
		int var5 = var2 * var3 * var4;
		if(var5 <= '\u8000') {
			for(int var6 = this.x; var6 <= this.maxX; ++var6) {
				for(int var7 = this.z; var7 <= this.maxZ; ++var7) {
					if(world.blockExists(var6, 0, var7)) {
						for(int var8 = this.y; var8 <= this.maxY; ++var8) {
							if(var8 >= 0 && var8 < 128) {
								int var9 = world.getSavedLightValue(this.skyBlock, var6, var8, var7);
								boolean var10 = false;
								int var11 = world.getBlockId(var6, var8, var7);
								int var12 = Block.lightOpacity[var11];
								if(var12 == 0) {
									var12 = 1;
								}

								int var13 = 0;
								if(this.skyBlock == EnumSkyBlock.Sky) {
									if(world.canExistingBlockSeeTheSky(var6, var8, var7)) {
										var13 = 15;
									}
								} else if(this.skyBlock == EnumSkyBlock.Block) {
									var13 = Block.lightValue[var11];
								}

								int var14;
								int var20;
								if(var12 >= 15 && var13 == 0) {
									var20 = 0;
								} else {
									var14 = world.getSavedLightValue(this.skyBlock, var6 - 1, var8, var7);
									int var15 = world.getSavedLightValue(this.skyBlock, var6 + 1, var8, var7);
									int var16 = world.getSavedLightValue(this.skyBlock, var6, var8 - 1, var7);
									int var17 = world.getSavedLightValue(this.skyBlock, var6, var8 + 1, var7);
									int var18 = world.getSavedLightValue(this.skyBlock, var6, var8, var7 - 1);
									int var19 = world.getSavedLightValue(this.skyBlock, var6, var8, var7 + 1);
									var20 = var14;
									if(var15 > var14) {
										var20 = var15;
									}

									if(var16 > var20) {
										var20 = var16;
									}

									if(var17 > var20) {
										var20 = var17;
									}

									if(var18 > var20) {
										var20 = var18;
									}

									if(var19 > var20) {
										var20 = var19;
									}

									var20 -= var12;
									if(var20 < 0) {
										var20 = 0;
									}

									if(var13 > var20) {
										var20 = var13;
									}
								}

								if(var9 != var20) {
									world.setLightValue(this.skyBlock, var6, var8, var7, var20);
									var14 = var20 - 1;
									if(var14 < 0) {
										var14 = 0;
									}

									world.neighborLightPropagationChanged(this.skyBlock, var6 - 1, var8, var7, var14);
									world.neighborLightPropagationChanged(this.skyBlock, var6, var8 - 1, var7, var14);
									world.neighborLightPropagationChanged(this.skyBlock, var6, var8, var7 - 1, var14);
									if(var6 + 1 >= this.maxX) {
										world.neighborLightPropagationChanged(this.skyBlock, var6 + 1, var8, var7, var14);
									}

									if(var8 + 1 >= this.maxY) {
										world.neighborLightPropagationChanged(this.skyBlock, var6, var8 + 1, var7, var14);
									}

									if(var7 + 1 >= this.maxZ) {
										world.neighborLightPropagationChanged(this.skyBlock, var6, var8, var7 + 1, var14);
									}
								}
							}
						}
					}
				}
			}

		}
	}

	public boolean getLightUpdated(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		if(minX >= this.x && minY >= this.y && minZ >= this.z && maxX <= this.maxX && maxY <= this.maxY && maxZ <= this.maxZ) {
			return true;
		} else {
			byte var7 = 1;
			if(minX >= this.x - var7 && minY >= this.y - var7 && minZ >= this.z - var7 && maxX <= this.maxX + var7 && maxY <= this.maxY + var7 && maxZ <= this.maxZ + var7) {
				int var8 = this.maxX - this.x;
				int var9 = this.maxY - this.y;
				int var10 = this.maxZ - this.z;
				if(minX > this.x) {
					minX = this.x;
				}

				if(minY > this.y) {
					minY = this.y;
				}

				if(minZ > this.z) {
					minZ = this.z;
				}

				if(maxX < this.maxX) {
					maxX = this.maxX;
				}

				if(maxY < this.maxY) {
					maxY = this.maxY;
				}

				if(maxZ < this.maxZ) {
					maxZ = this.maxZ;
				}

				int var11 = maxX - minX;
				int var12 = maxY - minY;
				int var13 = maxZ - minZ;
				int var14 = var8 * var9 * var10;
				int var15 = var11 * var12 * var13;
				if(var15 - var14 <= 2) {
					this.x = minX;
					this.y = minY;
					this.z = minZ;
					this.maxX = maxX;
					this.maxY = maxY;
					this.maxZ = maxZ;
					return true;
				}
			}

			return false;
		}
	}
}
