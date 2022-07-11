package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockStairs extends Block {
	private Block modelBlock;

	protected BlockStairs(int id, Block modelBlock) {
		super(id, modelBlock.blockIndexInTexture, modelBlock.material);
		this.modelBlock = modelBlock;
		this.setHardness(modelBlock.hardness);
		this.setResistance(modelBlock.resistance / 3.0F);
		this.setStepSound(modelBlock.stepSound);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 10;
	}

	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		return super.shouldSideBeRendered(blockAccess, x, y, z, side);
	}

	public void getCollidingBoundingBoxes(World world, int x, int y, int z, AxisAlignedBB aabb, ArrayList arrayList) {
		int var7 = world.getBlockMetadata(x, y, z);
		if(var7 == 0) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, arrayList);
			this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, arrayList);
		} else if(var7 == 1) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, arrayList);
			this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, arrayList);
		} else if(var7 == 2) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, arrayList);
			this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, arrayList);
		} else if(var7 == 3) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, arrayList);
			this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, arrayList);
		}

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		if(!world.multiplayerWorld) {
			if(world.getBlockMaterial(x, y + 1, z).isSolid()) {
				world.setBlockWithNotify(x, y, z, this.modelBlock.blockID);
			} else {
				this.updateState(world, x, y, z);
				this.updateState(world, x + 1, y - 1, z);
				this.updateState(world, x - 1, y - 1, z);
				this.updateState(world, x, y - 1, z - 1);
				this.updateState(world, x, y - 1, z + 1);
				this.updateState(world, x + 1, y + 1, z);
				this.updateState(world, x - 1, y + 1, z);
				this.updateState(world, x, y + 1, z - 1);
				this.updateState(world, x, y + 1, z + 1);
			}

			this.modelBlock.onNeighborBlockChange(world, x, y, z, flag);
		}
	}

	private void updateState(World world, int x, int y, int z) {
		if(this.isBlockStair(world, x, y, z)) {
			byte var5 = -1;
			if(this.isBlockStair(world, x + 1, y + 1, z)) {
				var5 = 0;
			}

			if(this.isBlockStair(world, x - 1, y + 1, z)) {
				var5 = 1;
			}

			if(this.isBlockStair(world, x, y + 1, z + 1)) {
				var5 = 2;
			}

			if(this.isBlockStair(world, x, y + 1, z - 1)) {
				var5 = 3;
			}

			if(var5 < 0) {
				if(this.isBlockSolid(world, x + 1, y, z) && !this.isBlockSolid(world, x - 1, y, z)) {
					var5 = 0;
				}

				if(this.isBlockSolid(world, x - 1, y, z) && !this.isBlockSolid(world, x + 1, y, z)) {
					var5 = 1;
				}

				if(this.isBlockSolid(world, x, y, z + 1) && !this.isBlockSolid(world, x, y, z - 1)) {
					var5 = 2;
				}

				if(this.isBlockSolid(world, x, y, z - 1) && !this.isBlockSolid(world, x, y, z + 1)) {
					var5 = 3;
				}
			}

			if(var5 < 0) {
				if(this.isBlockStair(world, x - 1, y - 1, z)) {
					var5 = 0;
				}

				if(this.isBlockStair(world, x + 1, y - 1, z)) {
					var5 = 1;
				}

				if(this.isBlockStair(world, x, y - 1, z - 1)) {
					var5 = 2;
				}

				if(this.isBlockStair(world, x, y - 1, z + 1)) {
					var5 = 3;
				}
			}

			if(var5 >= 0) {
				world.setBlockMetadataWithNotify(x, y, z, var5);
			}

		}
	}

	private boolean isBlockSolid(World world, int x, int y, int z) {
		return world.getBlockMaterial(x, y, z).isSolid();
	}

	private boolean isBlockStair(World world, int x, int y, int z) {
		int var5 = world.getBlockId(x, y, z);
		return var5 == 0 ? false : Block.blocksList[var5].getRenderType() == 10;
	}

	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		this.modelBlock.onBlockClicked(world, x, y, z, entityPlayer);
	}

	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int flag) {
		this.modelBlock.onBlockDestroyedByPlayer(world, x, y, z, flag);
	}

	public float getExplosionResistance(Entity entity) {
		return this.modelBlock.getExplosionResistance(entity);
	}

	public int idDropped(int count, Random random) {
		return this.modelBlock.idDropped(count, random);
	}

	public int quantityDropped(Random random) {
		return this.modelBlock.quantityDropped(random);
	}

	public int getBlockTextureFromSide(int side) {
		return this.modelBlock.getBlockTextureFromSide(side);
	}

	public int tickRate() {
		return this.modelBlock.tickRate();
	}

	public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3D vector) {
		this.modelBlock.velocityToAddToEntity(world, x, y, z, entity, vector);
	}

	public boolean isCollidable() {
		return this.modelBlock.isCollidable();
	}

	public boolean canCollideCheck(int var1, boolean var2) {
		return this.modelBlock.canCollideCheck(var1, var2);
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return this.modelBlock.canPlaceBlockAt(world, x, y, z);
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		this.onNeighborBlockChange(world, x, y, z, 0);
		this.modelBlock.onBlockAdded(world, x, y, z);
	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		this.modelBlock.onBlockRemoval(world, x, y, z);
	}

	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int var5, float var6) {
		this.modelBlock.dropBlockAsItemWithChance(world, x, y, z, var5, var6);
	}

	public void dropBlockAsItem(World world, int var2, int var3, int var4, int var5) {
		this.modelBlock.dropBlockAsItem(world, var2, var3, var4, var5);
	}

	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		this.modelBlock.onEntityWalking(world, x, y, z, entity);
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		this.modelBlock.updateTick(world, x, y, z, random);
	}

	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		return this.modelBlock.blockActivated(world, x, y, z, entityPlayer);
	}

	public void onBlockDestroyedByExplosion(World world, int x, int y, int z) {
		this.modelBlock.onBlockDestroyedByExplosion(world, x, y, z);
	}
}
