package net.minecraft.src;

public class BlockSponge extends Block {
	protected BlockSponge(int id) {
		super(id, Material.sponge);
		this.blockIndexInTexture = 48;
	}

	public void onBlockAdded(World worldObj, int x, int y, int z) {
		byte var5 = 2;

		for(int var6 = x - var5; var6 <= x + var5; ++var6) {
			for(int var7 = y - var5; var7 <= y + var5; ++var7) {
				for(int var8 = z - var5; var8 <= z + var5; ++var8) {
					if(worldObj.getBlockMaterial(var6, var7, var8) == Material.water) {
					}
				}
			}
		}

	}

	public void onBlockRemoval(World worldObj, int x, int y, int z) {
		byte var5 = 2;

		for(int var6 = x - var5; var6 <= x + var5; ++var6) {
			for(int var7 = y - var5; var7 <= y + var5; ++var7) {
				for(int var8 = z - var5; var8 <= z + var5; ++var8) {
					worldObj.notifyBlocksOfNeighborChange(var6, var7, var8, worldObj.getBlockId(var6, var7, var8));
				}
			}
		}

	}
}
