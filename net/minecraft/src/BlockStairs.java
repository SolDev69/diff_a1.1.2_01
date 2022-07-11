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

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 10;
	}

	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		return super.shouldSideBeRendered(blockAccess, x, y, z, side);
	}

	public void getCollidingBoundingBoxes(World worldObj, int x, int y, int z, AxisAlignedBB aabb, ArrayList collidingBoundingBoxes) {
		int var7 = worldObj.getBlockMetadata(x, y, z);
		if(var7 == 0) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(worldObj, x, y, z, aabb, collidingBoundingBoxes);
			this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(worldObj, x, y, z, aabb, collidingBoundingBoxes);
		} else if(var7 == 1) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(worldObj, x, y, z, aabb, collidingBoundingBoxes);
			this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(worldObj, x, y, z, aabb, collidingBoundingBoxes);
		} else if(var7 == 2) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
			super.getCollidingBoundingBoxes(worldObj, x, y, z, aabb, collidingBoundingBoxes);
			this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(worldObj, x, y, z, aabb, collidingBoundingBoxes);
		} else if(var7 == 3) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
			super.getCollidingBoundingBoxes(worldObj, x, y, z, aabb, collidingBoundingBoxes);
			this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(worldObj, x, y, z, aabb, collidingBoundingBoxes);
		}

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		if(!worldObj.multiplayerWorld) {
			if(worldObj.getBlockMaterial(x, y + 1, z).isSolid()) {
				worldObj.setBlockWithNotify(x, y, z, this.modelBlock.blockID);
			} else {
				this.updateState(worldObj, x, y, z);
				this.updateState(worldObj, x + 1, y - 1, z);
				this.updateState(worldObj, x - 1, y - 1, z);
				this.updateState(worldObj, x, y - 1, z - 1);
				this.updateState(worldObj, x, y - 1, z + 1);
				this.updateState(worldObj, x + 1, y + 1, z);
				this.updateState(worldObj, x - 1, y + 1, z);
				this.updateState(worldObj, x, y + 1, z - 1);
				this.updateState(worldObj, x, y + 1, z + 1);
			}

			this.modelBlock.onNeighborBlockChange(worldObj, x, y, z, id);
		}
	}

	private void updateState(World worldObj, int x, int y, int z) {
		if(this.isBlockStair(worldObj, x, y, z)) {
			byte var5 = -1;
			if(this.isBlockStair(worldObj, x + 1, y + 1, z)) {
				var5 = 0;
			}

			if(this.isBlockStair(worldObj, x - 1, y + 1, z)) {
				var5 = 1;
			}

			if(this.isBlockStair(worldObj, x, y + 1, z + 1)) {
				var5 = 2;
			}

			if(this.isBlockStair(worldObj, x, y + 1, z - 1)) {
				var5 = 3;
			}

			if(var5 < 0) {
				if(this.isBlockSolid(worldObj, x + 1, y, z) && !this.isBlockSolid(worldObj, x - 1, y, z)) {
					var5 = 0;
				}

				if(this.isBlockSolid(worldObj, x - 1, y, z) && !this.isBlockSolid(worldObj, x + 1, y, z)) {
					var5 = 1;
				}

				if(this.isBlockSolid(worldObj, x, y, z + 1) && !this.isBlockSolid(worldObj, x, y, z - 1)) {
					var5 = 2;
				}

				if(this.isBlockSolid(worldObj, x, y, z - 1) && !this.isBlockSolid(worldObj, x, y, z + 1)) {
					var5 = 3;
				}
			}

			if(var5 < 0) {
				if(this.isBlockStair(worldObj, x - 1, y - 1, z)) {
					var5 = 0;
				}

				if(this.isBlockStair(worldObj, x + 1, y - 1, z)) {
					var5 = 1;
				}

				if(this.isBlockStair(worldObj, x, y - 1, z - 1)) {
					var5 = 2;
				}

				if(this.isBlockStair(worldObj, x, y - 1, z + 1)) {
					var5 = 3;
				}
			}

			if(var5 >= 0) {
				worldObj.setBlockMetadataWithNotify(x, y, z, var5);
			}

		}
	}

	private boolean isBlockSolid(World worldObj, int x, int y, int z) {
		return worldObj.getBlockMaterial(x, y, z).isSolid();
	}

	private boolean isBlockStair(World worldObj, int x, int y, int z) {
		int var5 = worldObj.getBlockId(x, y, z);
		return var5 == 0 ? false : Block.blocksList[var5].getRenderType() == 10;
	}

	public void randomDisplayTick(World worldObj, int x, int y, int z, Random rand) {
		this.modelBlock.randomDisplayTick(worldObj, x, y, z, rand);
	}

	public void onBlockClicked(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		this.modelBlock.onBlockClicked(worldObj, x, y, z, entityPlayer);
	}

	public void onBlockDestroyedByPlayer(World worldObj, int x, int y, int z, int metadata) {
		this.modelBlock.onBlockDestroyedByPlayer(worldObj, x, y, z, metadata);
	}

	public float getBlockBrightness(IBlockAccess blockAccess, int x, int y, int z) {
		return this.modelBlock.getBlockBrightness(blockAccess, x, y, z);
	}

	public float getExplosionResistance(Entity entity) {
		return this.modelBlock.getExplosionResistance(entity);
	}

	public int getRenderBlockPass() {
		return this.modelBlock.getRenderBlockPass();
	}

	public int idDropped(int metadata, Random rand) {
		return this.modelBlock.idDropped(metadata, rand);
	}

	public int quantityDropped(Random rand) {
		return this.modelBlock.quantityDropped(rand);
	}

	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		return this.modelBlock.getBlockTextureFromSideAndMetadata(side, metadata);
	}

	public int getBlockTextureFromSide(int side) {
		return this.modelBlock.getBlockTextureFromSide(side);
	}

	public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		return this.modelBlock.getBlockTexture(blockAccess, x, y, z, side);
	}

	public int tickRate() {
		return this.modelBlock.tickRate();
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		return this.modelBlock.getSelectedBoundingBoxFromPool(worldObj, x, y, z);
	}

	public void velocityToAddToEntity(World worldObj, int x, int y, int z, Entity entity, Vec3D velocityVector) {
		this.modelBlock.velocityToAddToEntity(worldObj, x, y, z, entity, velocityVector);
	}

	public boolean isCollidable() {
		return this.modelBlock.isCollidable();
	}

	public boolean canCollideCheck(int metadata, boolean var2) {
		return this.modelBlock.canCollideCheck(metadata, var2);
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return this.modelBlock.canPlaceBlockAt(world, x, y, z);
	}

	public void onBlockAdded(World worldObj, int x, int y, int z) {
		this.onNeighborBlockChange(worldObj, x, y, z, 0);
		this.modelBlock.onBlockAdded(worldObj, x, y, z);
	}

	public void onBlockRemoval(World worldObj, int x, int y, int z) {
		this.modelBlock.onBlockRemoval(worldObj, x, y, z);
	}

	public void dropBlockAsItemWithChance(World worldObj, int x, int y, int z, int metadata, float chance) {
		this.modelBlock.dropBlockAsItemWithChance(worldObj, x, y, z, metadata, chance);
	}

	public void dropBlockAsItem(World worldObj, int x, int y, int z, int metadata) {
		this.modelBlock.dropBlockAsItem(worldObj, x, y, z, metadata);
	}

	public void onEntityWalking(World worldObj, int x, int y, int z, Entity entity) {
		this.modelBlock.onEntityWalking(worldObj, x, y, z, entity);
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		this.modelBlock.updateTick(worldObj, x, y, z, rand);
	}

	public boolean blockActivated(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		return this.modelBlock.blockActivated(worldObj, x, y, z, entityPlayer);
	}

	public void onBlockDestroyedByExplosion(World worldObj, int x, int y, int z) {
		this.modelBlock.onBlockDestroyedByExplosion(worldObj, x, y, z);
	}
}
