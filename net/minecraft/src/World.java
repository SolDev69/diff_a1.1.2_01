package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class World implements IBlockAccess {
	private List lightingToUpdate;
	public List loadedEntityList;
	private List unloadedEntityList;
	private TreeSet scheduledTickTreeSet;
	private Set scheduledTickSet;
	public List loadedTileEntityList;
	public long worldTime;
	public boolean snowCovered;
	private long skyColor;
	private long fogColor;
	private long cloudColor;
	public int skylightSubtracted;
	protected int updateLCG;
	protected int DIST_HASH_MAGIC;
	public boolean editingBlocks;
	public static float[] lightBrightnessTable = new float[16];
	private final long lockTimestamp;
	protected int autosavePeriod;
	public List playerEntities;
	public int difficultySetting;
	public Object fontRenderer;
	public Random rand;
	public int spawnX;
	public int spawnY;
	public int spawnZ;
	public boolean isNewWorld;
	protected List worldAccesses;
	private IChunkProvider chunkProvider;
	public File saveDirectory;
	public long randomSeed;
	private NBTTagCompound nbtCompoundPlayer;
	public long sizeOnDisk;
	public final String levelName;
	public boolean worldChunkLoadOverride;
	private ArrayList collidingBoundingBoxes;
	private Set positionsToUpdate;
	private int soundCounter;
	private List entitiesWithinAABBExcludingEntity;
	public boolean multiplayerWorld;

	public static NBTTagCompound getLevelData(File file, String world) {
		File var2 = new File(file, "saves");
		File var3 = new File(var2, world);
		if(!var3.exists()) {
			return null;
		} else {
			File var4 = new File(var3, "level.dat");
			if(var4.exists()) {
				try {
					NBTTagCompound var5 = CompressedStreamTools.readCompressed(new FileInputStream(var4));
					NBTTagCompound var6 = var5.getCompoundTag("Data");
					return var6;
				} catch (Exception var7) {
					var7.printStackTrace();
				}
			}

			return null;
		}
	}

	public static void deleteWorld(File file, String world) {
		File var2 = new File(file, "saves");
		File var3 = new File(var2, world);
		if(var3.exists()) {
			deleteWorldFiles(var3.listFiles());
			var3.delete();
		}
	}

	private static void deleteWorldFiles(File[] files) {
		for(int var1 = 0; var1 < files.length; ++var1) {
			if(files[var1].isDirectory()) {
				deleteWorldFiles(files[var1].listFiles());
			}

			files[var1].delete();
		}

	}

	public World(File file, String name) {
		this(file, name, (new Random()).nextLong());
	}

	public World(String name) {
		this.lightingToUpdate = new ArrayList();
		this.loadedEntityList = new ArrayList();
		this.unloadedEntityList = new ArrayList();
		this.scheduledTickTreeSet = new TreeSet();
		this.scheduledTickSet = new HashSet();
		this.loadedTileEntityList = new ArrayList();
		this.worldTime = 0L;
		this.snowCovered = false;
		this.skyColor = 8961023L;
		this.fogColor = 12638463L;
		this.cloudColor = 16777215L;
		this.skylightSubtracted = 0;
		this.updateLCG = (new Random()).nextInt();
		this.DIST_HASH_MAGIC = 1013904223;
		this.editingBlocks = false;
		this.lockTimestamp = System.currentTimeMillis();
		this.autosavePeriod = 40;
		this.playerEntities = new ArrayList();
		this.rand = new Random();
		this.isNewWorld = false;
		this.worldAccesses = new ArrayList();
		this.randomSeed = 0L;
		this.sizeOnDisk = 0L;
		this.collidingBoundingBoxes = new ArrayList();
		this.positionsToUpdate = new HashSet();
		this.soundCounter = this.rand.nextInt(12000);
		this.entitiesWithinAABBExcludingEntity = new ArrayList();
		this.multiplayerWorld = false;
		this.levelName = name;
		this.chunkProvider = this.getChunkProvider(this.saveDirectory);
		this.calculateInitialSkylight();
	}

	public World(File file, String name, long seed) {
		this.lightingToUpdate = new ArrayList();
		this.loadedEntityList = new ArrayList();
		this.unloadedEntityList = new ArrayList();
		this.scheduledTickTreeSet = new TreeSet();
		this.scheduledTickSet = new HashSet();
		this.loadedTileEntityList = new ArrayList();
		this.worldTime = 0L;
		this.snowCovered = false;
		this.skyColor = 8961023L;
		this.fogColor = 12638463L;
		this.cloudColor = 16777215L;
		this.skylightSubtracted = 0;
		this.updateLCG = (new Random()).nextInt();
		this.DIST_HASH_MAGIC = 1013904223;
		this.editingBlocks = false;
		this.lockTimestamp = System.currentTimeMillis();
		this.autosavePeriod = 40;
		this.playerEntities = new ArrayList();
		this.rand = new Random();
		this.isNewWorld = false;
		this.worldAccesses = new ArrayList();
		this.randomSeed = 0L;
		this.sizeOnDisk = 0L;
		this.collidingBoundingBoxes = new ArrayList();
		this.positionsToUpdate = new HashSet();
		this.soundCounter = this.rand.nextInt(12000);
		this.entitiesWithinAABBExcludingEntity = new ArrayList();
		this.multiplayerWorld = false;
		this.levelName = name;
		file.mkdirs();
		this.saveDirectory = new File(file, name);
		this.saveDirectory.mkdirs();

		File var5;
		try {
			var5 = new File(this.saveDirectory, "session.lock");
			DataOutputStream var6 = new DataOutputStream(new FileOutputStream(var5));

			try {
				var6.writeLong(this.lockTimestamp);
			} finally {
				var6.close();
			}
		} catch (IOException var13) {
			throw new RuntimeException("Failed to check session lock, aborting");
		}

		var5 = new File(this.saveDirectory, "level.dat");
		this.isNewWorld = !var5.exists();
		if(var5.exists()) {
			try {
				NBTTagCompound var14 = CompressedStreamTools.readCompressed(new FileInputStream(var5));
				NBTTagCompound var7 = var14.getCompoundTag("Data");
				this.randomSeed = var7.getLong("RandomSeed");
				this.spawnX = var7.getInteger("SpawnX");
				this.spawnY = var7.getInteger("SpawnY");
				this.spawnZ = var7.getInteger("SpawnZ");
				this.worldTime = var7.getLong("Time");
				this.sizeOnDisk = var7.getLong("SizeOnDisk");
				this.snowCovered = var7.getBoolean("SnowCovered");
				if(var7.hasKey("Player")) {
					this.nbtCompoundPlayer = var7.getCompoundTag("Player");
				}
			} catch (Exception var11) {
				var11.printStackTrace();
			}
		} else {
			this.snowCovered = this.rand.nextInt(4) == 0;
		}

		boolean var15 = false;
		if(this.randomSeed == 0L) {
			this.randomSeed = seed;
			var15 = true;
		}

		this.chunkProvider = this.getChunkProvider(this.saveDirectory);
		if(var15) {
			this.worldChunkLoadOverride = true;
			this.spawnX = 0;
			this.spawnY = 64;

			for(this.spawnZ = 0; !this.findSpawn(this.spawnX, this.spawnZ); this.spawnZ += this.rand.nextInt(64) - this.rand.nextInt(64)) {
				this.spawnX += this.rand.nextInt(64) - this.rand.nextInt(64);
			}

			this.worldChunkLoadOverride = false;
		}

		this.calculateInitialSkylight();
	}

	protected IChunkProvider getChunkProvider(File file) {
		return new ChunkProviderLoadOrGenerate(this, new ChunkLoader(file, true), new ChunkProviderGenerate(this, this.randomSeed));
	}

	public void setSpawnLocation() {
		if(this.spawnY <= 0) {
			this.spawnY = 64;
		}

		while(this.getFirstUncoveredBlock(this.spawnX, this.spawnZ) == 0) {
			this.spawnX += this.rand.nextInt(8) - this.rand.nextInt(8);
			this.spawnZ += this.rand.nextInt(8) - this.rand.nextInt(8);
		}

	}

	private boolean findSpawn(int x, int z) {
		int var3 = this.getFirstUncoveredBlock(x, z);
		return var3 == Block.sand.blockID;
	}

	private int getFirstUncoveredBlock(int x, int z) {
		int var3;
		for(var3 = 63; this.getBlockId(x, var3 + 1, z) != 0; ++var3) {
		}

		return this.getBlockId(x, var3, z);
	}

	public void spawnPlayerWithLoadedChunks(EntityPlayer entityPlayer) {
		try {
			if(this.nbtCompoundPlayer != null) {
				entityPlayer.readFromNBT(this.nbtCompoundPlayer);
				this.nbtCompoundPlayer = null;
			}

			this.spawnEntityInWorld(entityPlayer);
		} catch (Exception var3) {
			var3.printStackTrace();
		}

	}

	public void saveWorld(boolean var1, IProgressUpdate var2) {
		if(this.chunkProvider.canSave()) {
			if(var2 != null) {
				var2.displayProgressMessage("Saving level");
			}

			this.saveLevel();
			if(var2 != null) {
				var2.displayLoadingString("Saving chunks");
			}

			this.chunkProvider.saveChunks(var1, var2);
		}
	}

	private void saveLevel() {
		this.checkSessionLock();
		NBTTagCompound var1 = new NBTTagCompound();
		var1.setLong("RandomSeed", this.randomSeed);
		var1.setInteger("SpawnX", this.spawnX);
		var1.setInteger("SpawnY", this.spawnY);
		var1.setInteger("SpawnZ", this.spawnZ);
		var1.setLong("Time", this.worldTime);
		var1.setLong("SizeOnDisk", this.sizeOnDisk);
		var1.setBoolean("SnowCovered", this.snowCovered);
		var1.setLong("LastPlayed", System.currentTimeMillis());
		EntityPlayer var2 = null;
		if(this.playerEntities.size() > 0) {
			var2 = (EntityPlayer)this.playerEntities.get(0);
		}

		NBTTagCompound var3;
		if(var2 != null) {
			var3 = new NBTTagCompound();
			var2.writeToNBT(var3);
			var1.setCompoundTag("Player", var3);
		}

		var3 = new NBTTagCompound();
		var3.setTag("Data", var1);

		try {
			File var4 = new File(this.saveDirectory, "level.dat_new");
			File var5 = new File(this.saveDirectory, "level.dat_old");
			File var6 = new File(this.saveDirectory, "level.dat");
			CompressedStreamTools.writeCompressed(var3, new FileOutputStream(var4));
			if(var5.exists()) {
				var5.delete();
			}

			var6.renameTo(var5);
			if(var6.exists()) {
				var6.delete();
			}

			var4.renameTo(var6);
			if(var4.exists()) {
				var4.delete();
			}
		} catch (Exception var7) {
			var7.printStackTrace();
		}

	}

	public boolean saveWorld(int var1) {
		if(!this.chunkProvider.canSave()) {
			return true;
		} else {
			if(var1 == 0) {
				this.saveLevel();
			}

			return this.chunkProvider.saveChunks(false, (IProgressUpdate)null);
		}
	}

	public int getBlockId(int x, int y, int z) {
		return x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000 ? (y < 0 ? 0 : (y >= 128 ? 0 : this.getChunkFromChunkCoords(x >> 4, z >> 4).getBlockID(x & 15, y, z & 15))) : 0;
	}

	public boolean blockExists(int x, int y, int z) {
		return y >= 0 && y < 128 ? this.chunkExists(x >> 4, z >> 4) : false;
	}

	public boolean checkChunksExist(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		if(maxY >= 0 && minY < 128) {
			minX >>= 4;
			minY >>= 4;
			minZ >>= 4;
			maxX >>= 4;
			maxY >>= 4;
			maxZ >>= 4;

			for(int var7 = minX; var7 <= maxX; ++var7) {
				for(int var8 = minZ; var8 <= maxZ; ++var8) {
					if(!this.chunkExists(var7, var8)) {
						return false;
					}
				}
			}

			return true;
		} else {
			return false;
		}
	}

	private boolean chunkExists(int xPos, int zPos) {
		return this.chunkProvider.chunkExists(xPos, zPos);
	}

	public Chunk getChunkFromBlockCoords(int xPos, int zPos) {
		return this.getChunkFromChunkCoords(xPos >> 4, zPos >> 4);
	}

	public Chunk getChunkFromChunkCoords(int xPos, int zPos) {
		return this.chunkProvider.provideChunk(xPos, zPos);
	}

	public boolean setBlockAndMetadata(int x, int y, int z, int id, int metadata) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y < 0) {
				return false;
			} else if(y >= 128) {
				return false;
			} else {
				Chunk var6 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				return var6.setBlockIDWithMetadata(x & 15, y, z & 15, id, metadata);
			}
		} else {
			return false;
		}
	}

	public boolean setBlock(int x, int y, int z, int id) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y < 0) {
				return false;
			} else if(y >= 128) {
				return false;
			} else {
				Chunk var5 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				return var5.setBlockID(x & 15, y, z & 15, id);
			}
		} else {
			return false;
		}
	}

	public Material getBlockMaterial(int x, int y, int z) {
		int var4 = this.getBlockId(x, y, z);
		return var4 == 0 ? Material.air : Block.blocksList[var4].material;
	}

	public int getBlockMetadata(int x, int y, int z) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y < 0) {
				return 0;
			} else if(y >= 128) {
				return 0;
			} else {
				Chunk var4 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				x &= 15;
				z &= 15;
				return var4.getBlockMetadata(x, y, z);
			}
		} else {
			return 0;
		}
	}

	public void setBlockMetadataWithNotify(int x, int y, int z, int metadata) {
		this.setBlockMetadata(x, y, z, metadata);
	}

	public boolean setBlockMetadata(int x, int y, int z, int metadata) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y < 0) {
				return false;
			} else if(y >= 128) {
				return false;
			} else {
				Chunk var5 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				x &= 15;
				z &= 15;
				var5.setBlockMetadata(x, y, z, metadata);
				return true;
			}
		} else {
			return false;
		}
	}

	public boolean setBlockWithNotify(int x, int y, int z, int id) {
		if(this.setBlock(x, y, z, id)) {
			this.notifyBlockChange(x, y, z, id);
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlockAndMetadataWithNotify(int x, int y, int z, int id, int metadata) {
		if(this.setBlockAndMetadata(x, y, z, id, metadata)) {
			this.notifyBlockChange(x, y, z, id);
			return true;
		} else {
			return false;
		}
	}

	public void markBlockNeedsUpdate(int x, int y, int z) {
		for(int var4 = 0; var4 < this.worldAccesses.size(); ++var4) {
			((IWorldAccess)this.worldAccesses.get(var4)).markBlockAndNeighborsNeedsUpdate(x, y, z);
		}

	}

	protected void notifyBlockChange(int x, int y, int z, int id) {
		this.markBlockNeedsUpdate(x, y, z);
		this.notifyBlocksOfNeighborChange(x, y, z, id);
	}

	public void markBlocksDirtyVertical(int x, int z, int minY, int maxY) {
		if(minY > maxY) {
			int var5 = maxY;
			maxY = minY;
			minY = var5;
		}

		this.markBlocksDirty(x, minY, z, x, maxY, z);
	}

	public void markBlocksDirty(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		for(int var7 = 0; var7 < this.worldAccesses.size(); ++var7) {
			((IWorldAccess)this.worldAccesses.get(var7)).markBlockRangeNeedsUpdate(minX, minY, minZ, maxX, maxY, maxZ);
		}

	}

	public void notifyBlocksOfNeighborChange(int x, int y, int z, int id) {
		this.notifyBlockOfNeighborChange(x - 1, y, z, id);
		this.notifyBlockOfNeighborChange(x + 1, y, z, id);
		this.notifyBlockOfNeighborChange(x, y - 1, z, id);
		this.notifyBlockOfNeighborChange(x, y + 1, z, id);
		this.notifyBlockOfNeighborChange(x, y, z - 1, id);
		this.notifyBlockOfNeighborChange(x, y, z + 1, id);
	}

	private void notifyBlockOfNeighborChange(int x, int y, int z, int id) {
		if(!this.editingBlocks && !this.multiplayerWorld) {
			Block var5 = Block.blocksList[this.getBlockId(x, y, z)];
			if(var5 != null) {
				var5.onNeighborBlockChange(this, x, y, z, id);
			}

		}
	}

	public boolean canBlockSeeTheSky(int x, int y, int z) {
		return this.getChunkFromChunkCoords(x >> 4, z >> 4).canBlockSeeTheSky(x & 15, y, z & 15);
	}

	public int getBlockLightValue(int x, int y, int z) {
		return this.getBlockLightValue_do(x, y, z, true);
	}

	public int getBlockLightValue_do(int x, int y, int z, boolean update) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			int var5;
			if(update) {
				var5 = this.getBlockId(x, y, z);
				if(var5 == Block.stairSingle.blockID || var5 == Block.tilledField.blockID) {
					int var6 = this.getBlockLightValue_do(x, y + 1, z, false);
					int var7 = this.getBlockLightValue_do(x + 1, y, z, false);
					int var8 = this.getBlockLightValue_do(x - 1, y, z, false);
					int var9 = this.getBlockLightValue_do(x, y, z + 1, false);
					int var10 = this.getBlockLightValue_do(x, y, z - 1, false);
					if(var7 > var6) {
						var6 = var7;
					}

					if(var8 > var6) {
						var6 = var8;
					}

					if(var9 > var6) {
						var6 = var9;
					}

					if(var10 > var6) {
						var6 = var10;
					}

					return var6;
				}
			}

			if(y < 0) {
				return 0;
			} else if(y >= 128) {
				var5 = 15 - this.skylightSubtracted;
				if(var5 < 0) {
					var5 = 0;
				}

				return var5;
			} else {
				Chunk var11 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				x &= 15;
				z &= 15;
				return var11.getBlockLightValue(x, y, z, this.skylightSubtracted);
			}
		} else {
			return 15;
		}
	}

	public boolean canExistingBlockSeeTheSky(int x, int y, int z) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y < 0) {
				return false;
			} else if(y >= 128) {
				return true;
			} else if(!this.chunkExists(x >> 4, z >> 4)) {
				return false;
			} else {
				Chunk var4 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				x &= 15;
				z &= 15;
				return var4.canBlockSeeTheSky(x, y, z);
			}
		} else {
			return false;
		}
	}

	public int getHeightValue(int blockX, int blockZ) {
		if(blockX >= -32000000 && blockZ >= -32000000 && blockX < 32000000 && blockZ <= 32000000) {
			if(!this.chunkExists(blockX >> 4, blockZ >> 4)) {
				return 0;
			} else {
				Chunk var3 = this.getChunkFromChunkCoords(blockX >> 4, blockZ >> 4);
				return var3.getHeightValue(blockX & 15, blockZ & 15);
			}
		} else {
			return 0;
		}
	}

	public void neighborLightPropagationChanged(EnumSkyBlock skyBlock, int x, int y, int z, int lightValue) {
		if(this.blockExists(x, y, z)) {
			if(skyBlock == EnumSkyBlock.Sky) {
				if(this.canExistingBlockSeeTheSky(x, y, z)) {
					lightValue = 15;
				}
			} else if(skyBlock == EnumSkyBlock.Block) {
				int var6 = this.getBlockId(x, y, z);
				if(Block.lightValue[var6] > lightValue) {
					lightValue = Block.lightValue[var6];
				}
			}

			if(this.getSavedLightValue(skyBlock, x, y, z) != lightValue) {
				this.scheduleLightingUpdate(skyBlock, x, y, z, x, y, z);
			}

		}
	}

	public int getSavedLightValue(EnumSkyBlock skyBlock, int x, int y, int z) {
		if(y >= 0 && y < 128 && x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			int var5 = x >> 4;
			int var6 = z >> 4;
			if(!this.chunkExists(var5, var6)) {
				return 0;
			} else {
				Chunk var7 = this.getChunkFromChunkCoords(var5, var6);
				return var7.getSavedLightValue(skyBlock, x & 15, y, z & 15);
			}
		} else {
			return skyBlock.defaultLightValue;
		}
	}

	public void setLightValue(EnumSkyBlock skyBlock, int x, int y, int z, int lightValue) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y >= 0) {
				if(y < 128) {
					if(this.chunkExists(x >> 4, z >> 4)) {
						Chunk var6 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
						var6.setLightValue(skyBlock, x & 15, y, z & 15, lightValue);

						for(int var7 = 0; var7 < this.worldAccesses.size(); ++var7) {
							((IWorldAccess)this.worldAccesses.get(var7)).markBlockAndNeighborsNeedsUpdate(x, y, z);
						}

					}
				}
			}
		}
	}

	public float getBrightness(int x, int y, int z) {
		return lightBrightnessTable[this.getBlockLightValue(x, y, z)];
	}

	public boolean isDaytime() {
		return this.skylightSubtracted < 4;
	}

	public MovingObjectPosition rayTraceBlocks(Vec3D var1, Vec3D var2) {
		return this.rayTraceBlocks_do(var1, var2, false);
	}

	public MovingObjectPosition rayTraceBlocks_do(Vec3D var1, Vec3D var2, boolean var3) {
		if(!Double.isNaN(var1.xCoord) && !Double.isNaN(var1.yCoord) && !Double.isNaN(var1.zCoord)) {
			if(!Double.isNaN(var2.xCoord) && !Double.isNaN(var2.yCoord) && !Double.isNaN(var2.zCoord)) {
				int var4 = MathHelper.floor_double(var2.xCoord);
				int var5 = MathHelper.floor_double(var2.yCoord);
				int var6 = MathHelper.floor_double(var2.zCoord);
				int var7 = MathHelper.floor_double(var1.xCoord);
				int var8 = MathHelper.floor_double(var1.yCoord);
				int var9 = MathHelper.floor_double(var1.zCoord);
				int var10 = 20;

				while(var10-- >= 0) {
					if(Double.isNaN(var1.xCoord) || Double.isNaN(var1.yCoord) || Double.isNaN(var1.zCoord)) {
						return null;
					}

					if(var7 == var4 && var8 == var5 && var9 == var6) {
						return null;
					}

					double var11 = 999.0D;
					double var13 = 999.0D;
					double var15 = 999.0D;
					if(var4 > var7) {
						var11 = (double)var7 + 1.0D;
					}

					if(var4 < var7) {
						var11 = (double)var7 + 0.0D;
					}

					if(var5 > var8) {
						var13 = (double)var8 + 1.0D;
					}

					if(var5 < var8) {
						var13 = (double)var8 + 0.0D;
					}

					if(var6 > var9) {
						var15 = (double)var9 + 1.0D;
					}

					if(var6 < var9) {
						var15 = (double)var9 + 0.0D;
					}

					double var17 = 999.0D;
					double var19 = 999.0D;
					double var21 = 999.0D;
					double var23 = var2.xCoord - var1.xCoord;
					double var25 = var2.yCoord - var1.yCoord;
					double var27 = var2.zCoord - var1.zCoord;
					if(var11 != 999.0D) {
						var17 = (var11 - var1.xCoord) / var23;
					}

					if(var13 != 999.0D) {
						var19 = (var13 - var1.yCoord) / var25;
					}

					if(var15 != 999.0D) {
						var21 = (var15 - var1.zCoord) / var27;
					}

					boolean var29 = false;
					byte var35;
					if(var17 < var19 && var17 < var21) {
						if(var4 > var7) {
							var35 = 4;
						} else {
							var35 = 5;
						}

						var1.xCoord = var11;
						var1.yCoord += var25 * var17;
						var1.zCoord += var27 * var17;
					} else if(var19 < var21) {
						if(var5 > var8) {
							var35 = 0;
						} else {
							var35 = 1;
						}

						var1.xCoord += var23 * var19;
						var1.yCoord = var13;
						var1.zCoord += var27 * var19;
					} else {
						if(var6 > var9) {
							var35 = 2;
						} else {
							var35 = 3;
						}

						var1.xCoord += var23 * var21;
						var1.yCoord += var25 * var21;
						var1.zCoord = var15;
					}

					Vec3D var30 = Vec3D.createVector(var1.xCoord, var1.yCoord, var1.zCoord);
					var7 = (int)(var30.xCoord = (double)MathHelper.floor_double(var1.xCoord));
					if(var35 == 5) {
						--var7;
						++var30.xCoord;
					}

					var8 = (int)(var30.yCoord = (double)MathHelper.floor_double(var1.yCoord));
					if(var35 == 1) {
						--var8;
						++var30.yCoord;
					}

					var9 = (int)(var30.zCoord = (double)MathHelper.floor_double(var1.zCoord));
					if(var35 == 3) {
						--var9;
						++var30.zCoord;
					}

					int var31 = this.getBlockId(var7, var8, var9);
					int var32 = this.getBlockMetadata(var7, var8, var9);
					Block var33 = Block.blocksList[var31];
					if(var31 > 0 && var33.canCollideCheck(var32, var3)) {
						MovingObjectPosition var34 = var33.collisionRayTrace(this, var7, var8, var9, var1, var2);
						if(var34 != null) {
							return var34;
						}
					}
				}

				return null;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public void playSoundAtEntity(Entity entity, String sound, float volume, float pitch) {
		for(int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
			((IWorldAccess)this.worldAccesses.get(var5)).playSound(sound, entity.posX, entity.posY - (double)entity.yOffset, entity.posZ, volume, pitch);
		}

	}

	public void playSoundEffect(double posX, double posY, double posZ, String sound, float volume, float pitch) {
		for(int var10 = 0; var10 < this.worldAccesses.size(); ++var10) {
			((IWorldAccess)this.worldAccesses.get(var10)).playSound(sound, posX, posY, posZ, volume, pitch);
		}

	}

	public void playRecord(String record, int x, int y, int z) {
		for(int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
			((IWorldAccess)this.worldAccesses.get(var5)).playRecord(record, x, y, z);
		}

	}

	public void spawnParticle(String particle, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
		for(int var14 = 0; var14 < this.worldAccesses.size(); ++var14) {
			((IWorldAccess)this.worldAccesses.get(var14)).spawnParticle(particle, posX, posY, posZ, motionX, motionY, motionZ);
		}

	}

	public boolean spawnEntityInWorld(Entity entity) {
		int var2 = MathHelper.floor_double(entity.posX / 16.0D);
		int var3 = MathHelper.floor_double(entity.posZ / 16.0D);
		boolean var4 = false;
		if(entity instanceof EntityPlayer) {
			var4 = true;
		}

		if(!var4 && !this.chunkExists(var2, var3)) {
			return false;
		} else {
			if(entity instanceof EntityPlayer) {
				this.playerEntities.add((EntityPlayer)entity);
				System.out.println("Player count: " + this.playerEntities.size());
			}

			this.getChunkFromChunkCoords(var2, var3).addEntity(entity);
			this.loadedEntityList.add(entity);
			this.obtainEntitySkin(entity);
			return true;
		}
	}

	protected void obtainEntitySkin(Entity entity) {
		for(int var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
			((IWorldAccess)this.worldAccesses.get(var2)).obtainEntitySkin(entity);
		}

	}

	protected void releaseEntitySkin(Entity entity) {
		for(int var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
			((IWorldAccess)this.worldAccesses.get(var2)).releaseEntitySkin(entity);
		}

	}

	public void setEntityDead(Entity entity) {
		entity.setEntityDead();
		if(entity instanceof EntityPlayer) {
			this.playerEntities.remove((EntityPlayer)entity);
			System.out.println("Player count: " + this.playerEntities.size());
		}

	}

	public void addWorldAccess(IWorldAccess worldAccess) {
		this.worldAccesses.add(worldAccess);
	}

	public void removeWorldAccess(IWorldAccess worldAccess) {
		this.worldAccesses.remove(worldAccess);
	}

	public List getCollidingBoundingBoxes(Entity entity, AxisAlignedBB aabb) {
		this.collidingBoundingBoxes.clear();
		int var3 = MathHelper.floor_double(aabb.minX);
		int var4 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int var5 = MathHelper.floor_double(aabb.minY);
		int var6 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int var7 = MathHelper.floor_double(aabb.minZ);
		int var8 = MathHelper.floor_double(aabb.maxZ + 1.0D);

		for(int var9 = var3; var9 < var4; ++var9) {
			for(int var10 = var7; var10 < var8; ++var10) {
				if(this.blockExists(var9, 64, var10)) {
					for(int var11 = var5 - 1; var11 < var6; ++var11) {
						Block var12 = Block.blocksList[this.getBlockId(var9, var11, var10)];
						if(var12 != null) {
							var12.getCollidingBoundingBoxes(this, var9, var11, var10, aabb, this.collidingBoundingBoxes);
						}
					}
				}
			}
		}

		double var14 = 0.25D;
		List var15 = this.getEntitiesWithinAABBExcludingEntity(entity, aabb.expand(var14, var14, var14));

		for(int var16 = 0; var16 < var15.size(); ++var16) {
			AxisAlignedBB var13 = ((Entity)var15.get(var16)).getBoundingBox();
			if(var13 != null && var13.intersectsWith(aabb)) {
				this.collidingBoundingBoxes.add(var13);
			}

			var13 = entity.getCollisionBox((Entity)var15.get(var16));
			if(var13 != null && var13.intersectsWith(aabb)) {
				this.collidingBoundingBoxes.add(var13);
			}
		}

		return this.collidingBoundingBoxes;
	}

	public int calculateSkylightSubtracted(float renderPartialTick) {
		float var2 = this.getCelestialAngle(renderPartialTick);
		float var3 = 1.0F - (MathHelper.cos(var2 * (float)Math.PI * 2.0F) * 2.0F + 0.5F);
		if(var3 < 0.0F) {
			var3 = 0.0F;
		}

		if(var3 > 1.0F) {
			var3 = 1.0F;
		}

		return (int)(var3 * 11.0F);
	}

	public Vec3D getSkyColor(float renderPartialTick) {
		float var2 = this.getCelestialAngle(renderPartialTick);
		float var3 = MathHelper.cos(var2 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if(var3 < 0.0F) {
			var3 = 0.0F;
		}

		if(var3 > 1.0F) {
			var3 = 1.0F;
		}

		float var4 = (float)(this.skyColor >> 16 & 255L) / 255.0F;
		float var5 = (float)(this.skyColor >> 8 & 255L) / 255.0F;
		float var6 = (float)(this.skyColor & 255L) / 255.0F;
		var4 *= var3;
		var5 *= var3;
		var6 *= var3;
		return Vec3D.createVector((double)var4, (double)var5, (double)var6);
	}

	public float getCelestialAngle(float renderPartialTick) {
		int var2 = (int)(this.worldTime % 24000L);
		float var3 = ((float)var2 + renderPartialTick) / 24000.0F - 0.25F;
		if(var3 < 0.0F) {
			++var3;
		}

		if(var3 > 1.0F) {
			--var3;
		}

		float var4 = var3;
		var3 = 1.0F - (float)((Math.cos((double)var3 * Math.PI) + 1.0D) / 2.0D);
		var3 = var4 + (var3 - var4) / 3.0F;
		return var3;
	}

	public Vec3D getCloudColor(float renderPartialTick) {
		float var2 = this.getCelestialAngle(renderPartialTick);
		float var3 = MathHelper.cos(var2 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if(var3 < 0.0F) {
			var3 = 0.0F;
		}

		if(var3 > 1.0F) {
			var3 = 1.0F;
		}

		float var4 = (float)(this.cloudColor >> 16 & 255L) / 255.0F;
		float var5 = (float)(this.cloudColor >> 8 & 255L) / 255.0F;
		float var6 = (float)(this.cloudColor & 255L) / 255.0F;
		var4 *= var3 * 0.9F + 0.1F;
		var5 *= var3 * 0.9F + 0.1F;
		var6 *= var3 * 0.85F + 0.15F;
		return Vec3D.createVector((double)var4, (double)var5, (double)var6);
	}

	public Vec3D getFogColor(float renderPartialTick) {
		float var2 = this.getCelestialAngle(renderPartialTick);
		float var3 = MathHelper.cos(var2 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if(var3 < 0.0F) {
			var3 = 0.0F;
		}

		if(var3 > 1.0F) {
			var3 = 1.0F;
		}

		float var4 = (float)(this.fogColor >> 16 & 255L) / 255.0F;
		float var5 = (float)(this.fogColor >> 8 & 255L) / 255.0F;
		float var6 = (float)(this.fogColor & 255L) / 255.0F;
		var4 *= var3 * 0.94F + 0.06F;
		var5 *= var3 * 0.94F + 0.06F;
		var6 *= var3 * 0.91F + 0.09F;
		return Vec3D.createVector((double)var4, (double)var5, (double)var6);
	}

	public int getTopSolidOrLiquidBlock(int x, int z) {
		Chunk var3 = this.getChunkFromBlockCoords(x, z);
		int var4 = 127;
		x &= 15;

		for(z &= 15; var4 > 0; --var4) {
			int var5 = var3.getBlockID(x, var4, z);
			if(var5 != 0 && (Block.blocksList[var5].material.getIsSolid() || Block.blocksList[var5].material.getIsLiquid())) {
				return var4 + 1;
			}
		}

		return -1;
	}

	public int getPrecipitationHeight(int x, int z) {
		return this.getChunkFromBlockCoords(x, z).getHeightValue(x & 15, z & 15);
	}

	public float getStarBrightness(float renderPartialTick) {
		float var2 = this.getCelestialAngle(renderPartialTick);
		float var3 = 1.0F - (MathHelper.cos(var2 * (float)Math.PI * 2.0F) * 2.0F + 0.75F);
		if(var3 < 0.0F) {
			var3 = 0.0F;
		}

		if(var3 > 1.0F) {
			var3 = 1.0F;
		}

		return var3 * var3 * 0.5F;
	}

	public void scheduleBlockUpdate(int x, int y, int z, int id) {
		NextTickListEntry var5 = new NextTickListEntry(x, y, z, id);
		byte var6 = 8;
		if(this.checkChunksExist(x - var6, y - var6, z - var6, x + var6, y + var6, z + var6)) {
			if(id > 0) {
				var5.setScheduledTime((long)Block.blocksList[id].tickRate() + this.worldTime);
			}

			if(!this.scheduledTickSet.contains(var5)) {
				this.scheduledTickSet.add(var5);
				this.scheduledTickTreeSet.add(var5);
			}
		}

	}

	public void updateEntities() {
		this.loadedEntityList.removeAll(this.unloadedEntityList);

		int var1;
		Entity var2;
		int var3;
		int var4;
		for(var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
			var2 = (Entity)this.unloadedEntityList.get(var1);
			var3 = var2.chunkCoordX;
			var4 = var2.chunkCoordZ;
			if(var2.addedToChunk && this.chunkExists(var3, var4)) {
				this.getChunkFromChunkCoords(var3, var4).removeEntity(var2);
			}
		}

		for(var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
			this.releaseEntitySkin((Entity)this.unloadedEntityList.get(var1));
		}

		this.unloadedEntityList.clear();

		for(var1 = 0; var1 < this.loadedEntityList.size(); ++var1) {
			var2 = (Entity)this.loadedEntityList.get(var1);
			if(var2.ridingEntity != null) {
				if(!var2.ridingEntity.isDead && var2.ridingEntity.riddenByEntity == var2) {
					continue;
				}

				var2.ridingEntity.riddenByEntity = null;
				var2.ridingEntity = null;
			}

			if(!var2.isDead) {
				this.updateEntity(var2);
			}

			if(var2.isDead) {
				var3 = var2.chunkCoordX;
				var4 = var2.chunkCoordZ;
				if(var2.addedToChunk && this.chunkExists(var3, var4)) {
					this.getChunkFromChunkCoords(var3, var4).removeEntity(var2);
				}

				this.loadedEntityList.remove(var1--);
				this.releaseEntitySkin(var2);
			}
		}

		for(var1 = 0; var1 < this.loadedTileEntityList.size(); ++var1) {
			TileEntity var5 = (TileEntity)this.loadedTileEntityList.get(var1);
			var5.updateEntity();
		}

	}

	protected void updateEntity(Entity entity) {
		int var2 = MathHelper.floor_double(entity.posX);
		int var3 = MathHelper.floor_double(entity.posZ);
		byte var4 = 16;
		if(this.checkChunksExist(var2 - var4, 0, var3 - var4, var2 + var4, 128, var3 + var4)) {
			entity.lastTickPosX = entity.posX;
			entity.lastTickPosY = entity.posY;
			entity.lastTickPosZ = entity.posZ;
			entity.prevRotationYaw = entity.rotationYaw;
			entity.prevRotationPitch = entity.rotationPitch;
			if(entity.ridingEntity != null) {
				entity.updateRidden();
			} else {
				entity.onUpdate();
			}

			int var5 = MathHelper.floor_double(entity.posX / 16.0D);
			int var6 = MathHelper.floor_double(entity.posY / 16.0D);
			int var7 = MathHelper.floor_double(entity.posZ / 16.0D);
			if(!entity.addedToChunk || entity.chunkCoordX != var5 || entity.chunkCoordY != var6 || entity.chunkCoordZ != var7) {
				if(entity.addedToChunk && this.chunkExists(entity.chunkCoordX, entity.chunkCoordZ)) {
					this.getChunkFromChunkCoords(entity.chunkCoordX, entity.chunkCoordZ).removeEntityAtIndex(entity, entity.chunkCoordY);
				}

				if(this.chunkExists(var5, var7)) {
					this.getChunkFromChunkCoords(var5, var7).addEntity(entity);
				} else {
					entity.addedToChunk = false;
					System.out.println("Removing entity because it\'s not in a chunk!!");
					entity.setEntityDead();
				}
			}

			if(entity.riddenByEntity != null) {
				if(!entity.riddenByEntity.isDead && entity.riddenByEntity.ridingEntity == entity) {
					this.updateEntity(entity.riddenByEntity);
				} else {
					entity.riddenByEntity.ridingEntity = null;
					entity.riddenByEntity = null;
				}
			}

			if(Double.isNaN(entity.posX) || Double.isInfinite(entity.posX)) {
				entity.posX = entity.lastTickPosX;
			}

			if(Double.isNaN(entity.posY) || Double.isInfinite(entity.posY)) {
				entity.posY = entity.lastTickPosY;
			}

			if(Double.isNaN(entity.posZ) || Double.isInfinite(entity.posZ)) {
				entity.posZ = entity.lastTickPosZ;
			}

			if(Double.isNaN((double)entity.rotationPitch) || Double.isInfinite((double)entity.rotationPitch)) {
				entity.rotationPitch = entity.prevRotationPitch;
			}

			if(Double.isNaN((double)entity.rotationYaw) || Double.isInfinite((double)entity.rotationYaw)) {
				entity.rotationYaw = entity.prevRotationYaw;
			}

		}
	}

	public boolean checkIfAABBIsClear(AxisAlignedBB aabb) {
		List var2 = this.getEntitiesWithinAABBExcludingEntity((Entity)null, aabb);

		for(int var3 = 0; var3 < var2.size(); ++var3) {
			Entity var4 = (Entity)var2.get(var3);
			if(!var4.isDead && var4.preventEntitySpawning) {
				return false;
			}
		}

		return true;
	}

	public boolean getIsAnyLiquid(AxisAlignedBB aabb) {
		int var2 = MathHelper.floor_double(aabb.minX);
		int var3 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int var4 = MathHelper.floor_double(aabb.minY);
		int var5 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int var6 = MathHelper.floor_double(aabb.minZ);
		int var7 = MathHelper.floor_double(aabb.maxZ + 1.0D);
		if(aabb.minX < 0.0D) {
			--var2;
		}

		if(aabb.minY < 0.0D) {
			--var4;
		}

		if(aabb.minZ < 0.0D) {
			--var6;
		}

		for(int var8 = var2; var8 < var3; ++var8) {
			for(int var9 = var4; var9 < var5; ++var9) {
				for(int var10 = var6; var10 < var7; ++var10) {
					Block var11 = Block.blocksList[this.getBlockId(var8, var9, var10)];
					if(var11 != null && var11.material.getIsLiquid()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isBoundingBoxBurning(AxisAlignedBB aabb) {
		int var2 = MathHelper.floor_double(aabb.minX);
		int var3 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int var4 = MathHelper.floor_double(aabb.minY);
		int var5 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int var6 = MathHelper.floor_double(aabb.minZ);
		int var7 = MathHelper.floor_double(aabb.maxZ + 1.0D);

		for(int var8 = var2; var8 < var3; ++var8) {
			for(int var9 = var4; var9 < var5; ++var9) {
				for(int var10 = var6; var10 < var7; ++var10) {
					int var11 = this.getBlockId(var8, var9, var10);
					if(var11 == Block.fire.blockID || var11 == Block.lavaMoving.blockID || var11 == Block.lavaStill.blockID) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean handleMaterialAcceleration(AxisAlignedBB aabb, Material material, Entity entity) {
		int var4 = MathHelper.floor_double(aabb.minX);
		int var5 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int var6 = MathHelper.floor_double(aabb.minY);
		int var7 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int var8 = MathHelper.floor_double(aabb.minZ);
		int var9 = MathHelper.floor_double(aabb.maxZ + 1.0D);
		boolean var10 = false;
		Vec3D var11 = Vec3D.createVector(0.0D, 0.0D, 0.0D);

		for(int var12 = var4; var12 < var5; ++var12) {
			for(int var13 = var6; var13 < var7; ++var13) {
				for(int var14 = var8; var14 < var9; ++var14) {
					Block var15 = Block.blocksList[this.getBlockId(var12, var13, var14)];
					if(var15 != null && var15.material == material) {
						double var16 = (double)((float)(var13 + 1) - BlockFluid.getFluidHeightPercent(this.getBlockMetadata(var12, var13, var14)));
						if((double)var7 >= var16) {
							var10 = true;
							var15.velocityToAddToEntity(this, var12, var13, var14, entity, var11);
						}
					}
				}
			}
		}

		if(var11.lengthVector() > 0.0D) {
			var11 = var11.normalize();
			double var18 = 0.004D;
			entity.motionX += var11.xCoord * var18;
			entity.motionY += var11.yCoord * var18;
			entity.motionZ += var11.zCoord * var18;
		}

		return var10;
	}

	public boolean isMaterialInBB(AxisAlignedBB aabb, Material material) {
		int var3 = MathHelper.floor_double(aabb.minX);
		int var4 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int var5 = MathHelper.floor_double(aabb.minY);
		int var6 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int var7 = MathHelper.floor_double(aabb.minZ);
		int var8 = MathHelper.floor_double(aabb.maxZ + 1.0D);

		for(int var9 = var3; var9 < var4; ++var9) {
			for(int var10 = var5; var10 < var6; ++var10) {
				for(int var11 = var7; var11 < var8; ++var11) {
					Block var12 = Block.blocksList[this.getBlockId(var9, var10, var11)];
					if(var12 != null && var12.material == material) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isAABBInMaterial(AxisAlignedBB aabb, Material material) {
		int var3 = MathHelper.floor_double(aabb.minX);
		int var4 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int var5 = MathHelper.floor_double(aabb.minY);
		int var6 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int var7 = MathHelper.floor_double(aabb.minZ);
		int var8 = MathHelper.floor_double(aabb.maxZ + 1.0D);

		for(int var9 = var3; var9 < var4; ++var9) {
			for(int var10 = var5; var10 < var6; ++var10) {
				for(int var11 = var7; var11 < var8; ++var11) {
					Block var12 = Block.blocksList[this.getBlockId(var9, var10, var11)];
					if(var12 != null && var12.material == material) {
						int var13 = this.getBlockMetadata(var9, var10, var11);
						double var14 = (double)(var10 + 1);
						if(var13 < 8) {
							var14 = (double)(var10 + 1) - (double)var13 / 8.0D;
						}

						if(var14 >= aabb.minY) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public void createExplosion(Entity var1, double var2, double var4, double var6, float var8) {
		(new Explosion()).doExplosion(this, var1, var2, var4, var6, var8);
	}

	public float getBlockDensity(Vec3D vector, AxisAlignedBB aabb) {
		double var3 = 1.0D / ((aabb.maxX - aabb.minX) * 2.0D + 1.0D);
		double var5 = 1.0D / ((aabb.maxY - aabb.minY) * 2.0D + 1.0D);
		double var7 = 1.0D / ((aabb.maxZ - aabb.minZ) * 2.0D + 1.0D);
		int var9 = 0;
		int var10 = 0;

		for(float var11 = 0.0F; var11 <= 1.0F; var11 = (float)((double)var11 + var3)) {
			for(float var12 = 0.0F; var12 <= 1.0F; var12 = (float)((double)var12 + var5)) {
				for(float var13 = 0.0F; var13 <= 1.0F; var13 = (float)((double)var13 + var7)) {
					double var14 = aabb.minX + (aabb.maxX - aabb.minX) * (double)var11;
					double var16 = aabb.minY + (aabb.maxY - aabb.minY) * (double)var12;
					double var18 = aabb.minZ + (aabb.maxZ - aabb.minZ) * (double)var13;
					if(this.rayTraceBlocks(Vec3D.createVector(var14, var16, var18), vector) == null) {
						++var9;
					}

					++var10;
				}
			}
		}

		return (float)var9 / (float)var10;
	}

	public void extinguishFire(int x, int y, int z, int side) {
		if(side == 0) {
			--y;
		}

		if(side == 1) {
			++y;
		}

		if(side == 2) {
			--z;
		}

		if(side == 3) {
			++z;
		}

		if(side == 4) {
			--x;
		}

		if(side == 5) {
			++x;
		}

		if(this.getBlockId(x, y, z) == Block.fire.blockID) {
			this.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 2.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.8F);
			this.setBlockWithNotify(x, y, z, 0);
		}

	}

	public Entity createDebugPlayer(Class playerClass) {
		return null;
	}

	public String getDebugLoadedEntities() {
		return "All: " + this.loadedEntityList.size();
	}

	public TileEntity getBlockTileEntity(int x, int y, int z) {
		Chunk var4 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
		return var4 != null ? var4.getChunkBlockTileEntity(x & 15, y, z & 15) : null;
	}

	public void setBlockTileEntity(int x, int y, int z, TileEntity tileEntity) {
		Chunk var5 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
		if(var5 != null) {
			var5.setChunkBlockTileEntity(x & 15, y, z & 15, tileEntity);
		}

	}

	public void removeBlockTileEntity(int x, int y, int z) {
		Chunk var4 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
		if(var4 != null) {
			var4.removeChunkBlockTileEntity(x & 15, y, z & 15);
		}

	}

	public boolean isBlockNormalCube(int x, int y, int z) {
		Block var4 = Block.blocksList[this.getBlockId(x, y, z)];
		return var4 == null ? false : var4.isOpaqueCube();
	}

	public void saveWorldIndirectly(IProgressUpdate var1) {
		this.saveWorld(true, var1);
	}

	public boolean updatingLighting() {
		int var1 = 1000;

		while(this.lightingToUpdate.size() > 0) {
			--var1;
			if(var1 <= 0) {
				return true;
			}

			((MetadataChunkBlock)this.lightingToUpdate.remove(this.lightingToUpdate.size() - 1)).updateLight(this);
		}

		return false;
	}

	public void scheduleLightingUpdate(EnumSkyBlock skyBlock, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.scheduleLightingUpdate_do(skyBlock, minX, minY, minZ, maxX, maxY, maxZ, true);
	}

	public void scheduleLightingUpdate_do(EnumSkyBlock skyBlock, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean update) {
		int var9 = (maxX + minX) / 2;
		int var10 = (maxZ + minZ) / 2;
		if(this.blockExists(var9, 64, var10)) {
			int var11 = this.lightingToUpdate.size();
			if(update) {
				int var12 = 4;
				if(var12 > var11) {
					var12 = var11;
				}

				for(int var13 = 0; var13 < var12; ++var13) {
					MetadataChunkBlock var14 = (MetadataChunkBlock)this.lightingToUpdate.get(this.lightingToUpdate.size() - var13 - 1);
					if(var14.skyBlock == skyBlock && var14.getLightUpdated(minX, minY, minZ, maxX, maxY, maxZ)) {
						return;
					}
				}
			}

			this.lightingToUpdate.add(new MetadataChunkBlock(skyBlock, minX, minY, minZ, maxX, maxY, maxZ));
			if(this.lightingToUpdate.size() > 100000) {
				while(this.lightingToUpdate.size() > '\uc350') {
					this.updatingLighting();
				}
			}

		}
	}

	public void calculateInitialSkylight() {
		int var1 = this.calculateSkylightSubtracted(1.0F);
		if(var1 != this.skylightSubtracted) {
			this.skylightSubtracted = var1;
		}

	}

	public void tick() {
		this.chunkProvider.unload100OldestChunks();
		int var1 = this.calculateSkylightSubtracted(1.0F);
		if(var1 != this.skylightSubtracted) {
			this.skylightSubtracted = var1;

			for(int var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
				((IWorldAccess)this.worldAccesses.get(var2)).updateAllRenderers();
			}
		}

		++this.worldTime;
		if(this.worldTime % (long)this.autosavePeriod == 0L) {
			this.saveWorld(false, (IProgressUpdate)null);
		}

		this.tickUpdates(false);
		this.updateBlocksAndPlayCaveSounds();
	}

	protected void updateBlocksAndPlayCaveSounds() {
		this.positionsToUpdate.clear();

		int var3;
		int var4;
		int var6;
		int var7;
		for(int var1 = 0; var1 < this.playerEntities.size(); ++var1) {
			EntityPlayer var2 = (EntityPlayer)this.playerEntities.get(var1);
			var3 = MathHelper.floor_double(var2.posX / 16.0D);
			var4 = MathHelper.floor_double(var2.posZ / 16.0D);
			byte var5 = 9;

			for(var6 = -var5; var6 <= var5; ++var6) {
				for(var7 = -var5; var7 <= var5; ++var7) {
					this.positionsToUpdate.add(new ChunkCoordIntPair(var6 + var3, var7 + var4));
				}
			}
		}

		if(this.soundCounter > 0) {
			--this.soundCounter;
		}

		Iterator var12 = this.positionsToUpdate.iterator();

		while(var12.hasNext()) {
			ChunkCoordIntPair var13 = (ChunkCoordIntPair)var12.next();
			var3 = var13.chunkXPos * 16;
			var4 = var13.chunkZPos * 16;
			Chunk var14 = this.getChunkFromChunkCoords(var13.chunkXPos, var13.chunkZPos);
			int var8;
			int var9;
			int var10;
			if(this.soundCounter == 0) {
				this.updateLCG = this.updateLCG * 3 + this.DIST_HASH_MAGIC;
				var6 = this.updateLCG >> 2;
				var7 = var6 & 15;
				var8 = var6 >> 8 & 15;
				var9 = var6 >> 16 & 127;
				var10 = var14.getBlockID(var7, var9, var8);
				var7 += var3;
				var8 += var4;
				if(var10 == 0 && this.getBlockLightValue(var7, var9, var8) <= this.rand.nextInt(8) && this.getSavedLightValue(EnumSkyBlock.Sky, var7, var9, var8) <= 0) {
					EntityPlayer var11 = this.getClosestPlayer((double)var7 + 0.5D, (double)var9 + 0.5D, (double)var8 + 0.5D, 8.0D);
					if(var11 != null && var11.getDistanceSq((double)var7 + 0.5D, (double)var9 + 0.5D, (double)var8 + 0.5D) > 4.0D) {
						this.playSoundEffect((double)var7 + 0.5D, (double)var9 + 0.5D, (double)var8 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
						this.soundCounter = this.rand.nextInt(12000) + 6000;
					}
				}
			}

			if(this.snowCovered && this.rand.nextInt(4) == 0) {
				this.updateLCG = this.updateLCG * 3 + this.DIST_HASH_MAGIC;
				var6 = this.updateLCG >> 2;
				var7 = var6 & 15;
				var8 = var6 >> 8 & 15;
				var9 = this.getTopSolidOrLiquidBlock(var7 + var3, var8 + var4);
				if(var9 >= 0 && var9 < 128 && var14.getSavedLightValue(EnumSkyBlock.Block, var7, var9, var8) < 10) {
					var10 = var14.getBlockID(var7, var9 - 1, var8);
					if(var14.getBlockID(var7, var9, var8) == 0 && Block.snow.canPlaceBlockAt(this, var7 + var3, var9, var8 + var4)) {
						this.setBlockWithNotify(var7 + var3, var9, var8 + var4, Block.snow.blockID);
					}

					if(var10 == Block.waterStill.blockID && var14.getBlockMetadata(var7, var9 - 1, var8) == 0) {
						this.setBlockWithNotify(var7 + var3, var9 - 1, var8 + var4, Block.ice.blockID);
					}
				}
			}

			for(var6 = 0; var6 < 80; ++var6) {
				this.updateLCG = this.updateLCG * 3 + this.DIST_HASH_MAGIC;
				var7 = this.updateLCG >> 2;
				var8 = var7 & 15;
				var9 = var7 >> 8 & 15;
				var10 = var7 >> 16 & 127;
				byte var15 = var14.blocks[var8 << 11 | var9 << 7 | var10];
				if(Block.tickOnLoad[var15]) {
					Block.blocksList[var15].updateTick(this, var8 + var3, var10, var9 + var4, this.rand);
				}
			}
		}

	}

	public boolean tickUpdates(boolean var1) {
		int var2 = this.scheduledTickTreeSet.size();
		if(var2 != this.scheduledTickSet.size()) {
			throw new IllegalStateException("TickNextTick list out of synch");
		} else {
			if(var2 > 1000) {
				var2 = 1000;
			}

			for(int var3 = 0; var3 < var2; ++var3) {
				NextTickListEntry var4 = (NextTickListEntry)this.scheduledTickTreeSet.first();
				if(!var1 && var4.scheduledTime > this.worldTime) {
					break;
				}

				this.scheduledTickTreeSet.remove(var4);
				this.scheduledTickSet.remove(var4);
				byte var5 = 8;
				if(this.checkChunksExist(var4.xCoord - var5, var4.yCoord - var5, var4.zCoord - var5, var4.xCoord + var5, var4.yCoord + var5, var4.zCoord + var5)) {
					int var6 = this.getBlockId(var4.xCoord, var4.yCoord, var4.zCoord);
					if(var6 == var4.blockID && var6 > 0) {
						Block.blocksList[var6].updateTick(this, var4.xCoord, var4.yCoord, var4.zCoord, this.rand);
					}
				}
			}

			return this.scheduledTickTreeSet.size() != 0;
		}
	}

	public void randomDisplayUpdates(int posX, int posY, int posZ) {
		byte var4 = 16;
		Random var5 = new Random();

		for(int var6 = 0; var6 < 1000; ++var6) {
			int var7 = posX + this.rand.nextInt(var4) - this.rand.nextInt(var4);
			int var8 = posY + this.rand.nextInt(var4) - this.rand.nextInt(var4);
			int var9 = posZ + this.rand.nextInt(var4) - this.rand.nextInt(var4);
			int var10 = this.getBlockId(var7, var8, var9);
			if(var10 > 0) {
				Block.blocksList[var10].randomDisplayTick(this, var7, var8, var9, var5);
			}
		}

	}

	public List getEntitiesWithinAABBExcludingEntity(Entity entity, AxisAlignedBB aabb) {
		this.entitiesWithinAABBExcludingEntity.clear();
		int var3 = MathHelper.floor_double((aabb.minX - 2.0D) / 16.0D);
		int var4 = MathHelper.floor_double((aabb.maxX + 2.0D) / 16.0D);
		int var5 = MathHelper.floor_double((aabb.minZ - 2.0D) / 16.0D);
		int var6 = MathHelper.floor_double((aabb.maxZ + 2.0D) / 16.0D);

		for(int var7 = var3; var7 <= var4; ++var7) {
			for(int var8 = var5; var8 <= var6; ++var8) {
				if(this.chunkExists(var7, var8)) {
					this.getChunkFromChunkCoords(var7, var8).getEntitiesWithinAABBForEntity(entity, aabb, this.entitiesWithinAABBExcludingEntity);
				}
			}
		}

		return this.entitiesWithinAABBExcludingEntity;
	}

	public List getEntitiesWithinAABB(Class entityClass, AxisAlignedBB aabb) {
		int var3 = MathHelper.floor_double((aabb.minX - 2.0D) / 16.0D);
		int var4 = MathHelper.floor_double((aabb.maxX + 2.0D) / 16.0D);
		int var5 = MathHelper.floor_double((aabb.minZ - 2.0D) / 16.0D);
		int var6 = MathHelper.floor_double((aabb.maxZ + 2.0D) / 16.0D);
		ArrayList var7 = new ArrayList();

		for(int var8 = var3; var8 <= var4; ++var8) {
			for(int var9 = var5; var9 <= var6; ++var9) {
				if(this.chunkExists(var8, var9)) {
					this.getChunkFromChunkCoords(var8, var9).getEntitiesOfTypeWithinAAAB(entityClass, aabb, var7);
				}
			}
		}

		return var7;
	}

	public List getLoadedEntityList() {
		return this.loadedEntityList;
	}

	public void updateTileEntityChunkAndDoNothing(int x, int y, int z, TileEntity tileEntity) {
		if(this.blockExists(x, y, z)) {
			this.getChunkFromBlockCoords(x, z).setChunkModified();
		}

		for(int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
			((IWorldAccess)this.worldAccesses.get(var5)).doNothingWithTileEntity(x, y, z, tileEntity);
		}

	}

	public int countEntities(Class entityClass) {
		int var2 = 0;

		for(int var3 = 0; var3 < this.loadedEntityList.size(); ++var3) {
			Entity var4 = (Entity)this.loadedEntityList.get(var3);
			if(entityClass.isAssignableFrom(var4.getClass())) {
				++var2;
			}
		}

		return var2;
	}

	public void addLoadedEntities(List list) {
		this.loadedEntityList.addAll(list);

		for(int var2 = 0; var2 < list.size(); ++var2) {
			this.obtainEntitySkin((Entity)list.get(var2));
		}

	}

	public void unloadEntities(List list) {
		this.unloadedEntityList.addAll(list);
	}

	public void dropOldChunks() {
		while(this.chunkProvider.unload100OldestChunks()) {
		}

	}

	public boolean canBlockBePlacedAt(int id, int x, int y, int z, boolean ignoreBB) {
		int var6 = this.getBlockId(x, y, z);
		Block var7 = Block.blocksList[var6];
		Block var8 = Block.blocksList[id];
		AxisAlignedBB var9 = var8.getCollisionBoundingBoxFromPool(this, x, y, z);
		if(ignoreBB) {
			var9 = null;
		}

		return var9 != null && !this.checkIfAABBIsClear(var9) ? false : (var7 != Block.waterMoving && var7 != Block.waterStill && var7 != Block.lavaMoving && var7 != Block.lavaStill && var7 != Block.fire && var7 != Block.snow ? id > 0 && var7 == null && var8.canPlaceBlockAt(this, x, y, z) : true);
	}

	public PathEntity getPathToEntity(Entity var1, Entity var2, float var3) {
		int var4 = MathHelper.floor_double(var1.posX);
		int var5 = MathHelper.floor_double(var1.posY);
		int var6 = MathHelper.floor_double(var1.posZ);
		int var7 = (int)(var3 + 16.0F);
		int var8 = var4 - var7;
		int var9 = var5 - var7;
		int var10 = var6 - var7;
		int var11 = var4 + var7;
		int var12 = var5 + var7;
		int var13 = var6 + var7;
		ChunkCache var14 = new ChunkCache(this, var8, var9, var10, var11, var12, var13);
		return (new Pathfinder(var14)).createEntityPathTo(var1, var2, var3);
	}

	public PathEntity getEntityPathToXYZ(Entity entity, int var2, int var3, int var4, float var5) {
		int var6 = MathHelper.floor_double(entity.posX);
		int var7 = MathHelper.floor_double(entity.posY);
		int var8 = MathHelper.floor_double(entity.posZ);
		int var9 = (int)(var5 + 8.0F);
		int var10 = var6 - var9;
		int var11 = var7 - var9;
		int var12 = var8 - var9;
		int var13 = var6 + var9;
		int var14 = var7 + var9;
		int var15 = var8 + var9;
		ChunkCache var16 = new ChunkCache(this, var10, var11, var12, var13, var14, var15);
		return (new Pathfinder(var16)).createEntityPathTo(entity, var2, var3, var4, var5);
	}

	public boolean isBlockProvidingPowerTo(int x, int y, int z, int side) {
		int var5 = this.getBlockId(x, y, z);
		return var5 == 0 ? false : Block.blocksList[var5].isIndirectlyPoweringTo(this, x, y, z, side);
	}

	public boolean isBlockGettingPowered(int x, int y, int z) {
		return this.isBlockProvidingPowerTo(x, y - 1, z, 0) ? true : (this.isBlockProvidingPowerTo(x, y + 1, z, 1) ? true : (this.isBlockProvidingPowerTo(x, y, z - 1, 2) ? true : (this.isBlockProvidingPowerTo(x, y, z + 1, 3) ? true : (this.isBlockProvidingPowerTo(x - 1, y, z, 4) ? true : this.isBlockProvidingPowerTo(x + 1, y, z, 5)))));
	}

	public boolean isBlockIndirectlyProvidingPowerTo(int x, int y, int z, int side) {
		if(this.isBlockNormalCube(x, y, z)) {
			return this.isBlockGettingPowered(x, y, z);
		} else {
			int var5 = this.getBlockId(x, y, z);
			return var5 == 0 ? false : Block.blocksList[var5].isPoweringTo(this, x, y, z, side);
		}
	}

	public boolean isBlockIndirectlyGettingPowered(int x, int y, int z) {
		return this.isBlockIndirectlyProvidingPowerTo(x, y - 1, z, 0) ? true : (this.isBlockIndirectlyProvidingPowerTo(x, y + 1, z, 1) ? true : (this.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2) ? true : (this.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3) ? true : (this.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4) ? true : this.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5)))));
	}

	public EntityPlayer getClosestPlayerToEntity(Entity entity, double distance) {
		return this.getClosestPlayer(entity.posX, entity.posY, entity.posZ, distance);
	}

	public EntityPlayer getClosestPlayer(double posX, double posY, double posZ, double distance) {
		double var9 = -1.0D;
		EntityPlayer var11 = null;

		for(int var12 = 0; var12 < this.playerEntities.size(); ++var12) {
			EntityPlayer var13 = (EntityPlayer)this.playerEntities.get(var12);
			double var14 = var13.getDistanceSq(posX, posY, posZ);
			if((distance < 0.0D || var14 < distance * distance) && (var9 == -1.0D || var14 < var9)) {
				var9 = var14;
				var11 = var13;
			}
		}

		return var11;
	}

	public void setChunkData(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, byte[] blocks) {
		int var8 = minX >> 4;
		int var9 = minZ >> 4;
		int var10 = minX + maxX - 1 >> 4;
		int var11 = minZ + maxZ - 1 >> 4;
		int var12 = 0;
		int var13 = minY;
		int var14 = minY + maxY;
		if(minY < 0) {
			var13 = 0;
		}

		if(var14 > 128) {
			var14 = 128;
		}

		for(int var15 = var8; var15 <= var10; ++var15) {
			int var16 = minX - var15 * 16;
			int var17 = minX + maxX - var15 * 16;
			if(var16 < 0) {
				var16 = 0;
			}

			if(var17 > 16) {
				var17 = 16;
			}

			for(int var18 = var9; var18 <= var11; ++var18) {
				int var19 = minZ - var18 * 16;
				int var20 = minZ + maxZ - var18 * 16;
				if(var19 < 0) {
					var19 = 0;
				}

				if(var20 > 16) {
					var20 = 16;
				}

				var12 = this.getChunkFromChunkCoords(var15, var18).setChunkData(blocks, var16, var13, var19, var17, var14, var20, var12);
				this.markBlocksDirty(var15 * 16 + var16, var13, var18 * 16 + var19, var15 * 16 + var17, var14, var18 * 16 + var20);
			}
		}

	}

	public void sendQuittingDisconnectingPacket() {
	}

	public void checkSessionLock() {
		try {
			File var1 = new File(this.saveDirectory, "session.lock");
			DataInputStream var2 = new DataInputStream(new FileInputStream(var1));

			try {
				if(var2.readLong() != this.lockTimestamp) {
					throw new MinecraftException("The save is being accessed from another location, aborting");
				}
			} finally {
				var2.close();
			}

		} catch (IOException var7) {
			throw new MinecraftException("Failed to check session lock, aborting");
		}
	}

	public void setWorldTime(long time) {
		this.worldTime = time;
	}

	public void joinEntityInSurroundings(Entity entity) {
		int var2 = MathHelper.floor_double(entity.posX / 16.0D);
		int var3 = MathHelper.floor_double(entity.posZ / 16.0D);
		byte var4 = 2;

		for(int var5 = var2 - var4; var5 <= var2 + var4; ++var5) {
			for(int var6 = var3 - var4; var6 <= var3 + var4; ++var6) {
				this.getChunkFromChunkCoords(var5, var6);
			}
		}

		if(!this.loadedEntityList.contains(entity)) {
			System.out.println("REINSERTING PLAYER!");
			this.loadedEntityList.add(entity);
		}

	}

	static {
		float var0 = 0.05F;

		for(int var1 = 0; var1 <= 15; ++var1) {
			float var2 = 1.0F - (float)var1 / 15.0F;
			lightBrightnessTable[var1] = (1.0F - var2) / (var2 * 3.0F + 1.0F) * (1.0F - var0) + var0;
		}

	}
}
