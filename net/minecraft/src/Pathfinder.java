package net.minecraft.src;

public class Pathfinder {
	private IBlockAccess worldMap;
	private Path path = new Path();
	private MCHashTable pointMap = new MCHashTable();
	private PathPoint[] pathOptions = new PathPoint[32];

	public Pathfinder(IBlockAccess worldMap) {
		this.worldMap = worldMap;
	}

	public PathEntity createEntityPathTo(Entity entity1, Entity entity2, float var3) {
		return this.createEntityPathTo(entity1, entity2.posX, entity2.boundingBox.minY, entity2.posZ, var3);
	}

	public PathEntity createEntityPathTo(Entity entity, int x, int boundingBoxY, int z, float var5) {
		return this.createEntityPathTo(entity, (double)((float)x + 0.5F), (double)((float)boundingBoxY + 0.5F), (double)((float)z + 0.5F), var5);
	}

	private PathEntity createEntityPathTo(Entity entity, double x, double boundingBoxY, double z, float var8) {
		this.path.clearPath();
		this.pointMap.clearMap();
		PathPoint var9 = this.openPoint(MathHelper.floor_double(entity.boundingBox.minX), MathHelper.floor_double(entity.boundingBox.minY), MathHelper.floor_double(entity.boundingBox.minZ));
		PathPoint var10 = this.openPoint(MathHelper.floor_double(x - (double)(entity.width / 2.0F)), MathHelper.floor_double(boundingBoxY), MathHelper.floor_double(z - (double)(entity.width / 2.0F)));
		PathPoint var11 = new PathPoint(MathHelper.floor_float(entity.width + 1.0F), MathHelper.floor_float(entity.height + 1.0F), MathHelper.floor_float(entity.width + 1.0F));
		PathEntity var12 = this.addToPath(entity, var9, var10, var11, var8);
		return var12;
	}

	private PathEntity addToPath(Entity entity, PathPoint pathPoint1, PathPoint pathPoint2, PathPoint pathPoint3, float var5) {
		pathPoint1.totalPathDistance = 0.0F;
		pathPoint1.distanceToNext = pathPoint1.distanceTo(pathPoint2);
		pathPoint1.distanceToTarget = pathPoint1.distanceToNext;
		this.path.clearPath();
		this.path.addPoint(pathPoint1);
		PathPoint var6 = pathPoint1;

		while(!this.path.isPathEmpty()) {
			PathPoint var7 = this.path.dequeue();
			if(var7.hash == pathPoint2.hash) {
				return this.createEntityPath(pathPoint1, pathPoint2);
			}

			if(var7.distanceTo(pathPoint2) < var6.distanceTo(pathPoint2)) {
				var6 = var7;
			}

			var7.isFirst = true;
			int var8 = this.findPathOptions(entity, var7, pathPoint3, pathPoint2, var5);

			for(int var9 = 0; var9 < var8; ++var9) {
				PathPoint var10 = this.pathOptions[var9];
				float var11 = var7.totalPathDistance + var7.distanceTo(var10);
				if(!var10.isAssigned() || var11 < var10.totalPathDistance) {
					var10.previous = var7;
					var10.totalPathDistance = var11;
					var10.distanceToNext = var10.distanceTo(pathPoint2);
					if(var10.isAssigned()) {
						this.path.changeDistance(var10, var10.totalPathDistance + var10.distanceToNext);
					} else {
						var10.distanceToTarget = var10.totalPathDistance + var10.distanceToNext;
						this.path.addPoint(var10);
					}
				}
			}
		}

		if(var6 == pathPoint1) {
			return null;
		} else {
			return this.createEntityPath(pathPoint1, var6);
		}
	}

	private int findPathOptions(Entity entity, PathPoint pathPoint1, PathPoint pathPoint2, PathPoint pathPoint3, float var5) {
		int var6 = 0;
		byte var7 = 0;
		if(this.getVerticalOffset(entity, pathPoint1.xCoord, pathPoint1.yCoord + 1, pathPoint1.zCoord, pathPoint2) > 0) {
			var7 = 1;
		}

		PathPoint var8 = this.getSafePoint(entity, pathPoint1.xCoord, pathPoint1.yCoord, pathPoint1.zCoord + 1, pathPoint2, var7);
		PathPoint var9 = this.getSafePoint(entity, pathPoint1.xCoord - 1, pathPoint1.yCoord, pathPoint1.zCoord, pathPoint2, var7);
		PathPoint var10 = this.getSafePoint(entity, pathPoint1.xCoord + 1, pathPoint1.yCoord, pathPoint1.zCoord, pathPoint2, var7);
		PathPoint var11 = this.getSafePoint(entity, pathPoint1.xCoord, pathPoint1.yCoord, pathPoint1.zCoord - 1, pathPoint2, var7);
		if(var8 != null && !var8.isFirst && var8.distanceTo(pathPoint3) < var5) {
			this.pathOptions[var6++] = var8;
		}

		if(var9 != null && !var9.isFirst && var9.distanceTo(pathPoint3) < var5) {
			this.pathOptions[var6++] = var9;
		}

		if(var10 != null && !var10.isFirst && var10.distanceTo(pathPoint3) < var5) {
			this.pathOptions[var6++] = var10;
		}

		if(var11 != null && !var11.isFirst && var11.distanceTo(pathPoint3) < var5) {
			this.pathOptions[var6++] = var11;
		}

		return var6;
	}

	private PathPoint getSafePoint(Entity entity, int var2, int var3, int var4, PathPoint pathPoint, int var6) {
		PathPoint var7 = null;
		if(this.getVerticalOffset(entity, var2, var3, var4, pathPoint) > 0) {
			var7 = this.openPoint(var2, var3, var4);
		}

		if(var7 == null && this.getVerticalOffset(entity, var2, var3 + var6, var4, pathPoint) > 0) {
			var7 = this.openPoint(var2, var3 + var6, var4);
			var3 += var6;
		}

		if(var7 != null) {
			int var8 = 0;

			int var10;
			for(boolean var9 = false; var3 > 0 && (var10 = this.getVerticalOffset(entity, var2, var3 - 1, var4, pathPoint)) > 0; --var3) {
				if(var10 < 0) {
					return null;
				}

				++var8;
				if(var8 >= 4) {
					return null;
				}
			}

			if(var3 > 0) {
				var7 = this.openPoint(var2, var3, var4);
			}
		}

		return var7;
	}

	private final PathPoint openPoint(int x, int y, int z) {
		int var4 = x | y << 10 | z << 20;
		PathPoint var5 = (PathPoint)this.pointMap.lookup(var4);
		if(var5 == null) {
			var5 = new PathPoint(x, y, z);
			this.pointMap.addKey(var4, var5);
		}

		return var5;
	}

	private int getVerticalOffset(Entity entity, int x, int y, int z, PathPoint pathPoint) {
		for(int var6 = x; var6 < x + pathPoint.xCoord; ++var6) {
			for(int var7 = y; var7 < y + pathPoint.yCoord; ++var7) {
				for(int var8 = z; var8 < z + pathPoint.zCoord; ++var8) {
					Material var9 = this.worldMap.getBlockMaterial(x, y, z);
					if(var9.getIsSolid()) {
						return 0;
					}

					if(var9 == Material.water || var9 == Material.lava) {
						return -1;
					}
				}
			}
		}

		return 1;
	}

	private PathEntity createEntityPath(PathPoint pathPoint1, PathPoint pathPoint2) {
		int var3 = 1;

		PathPoint var4;
		for(var4 = pathPoint2; var4.previous != null; var4 = var4.previous) {
			++var3;
		}

		PathPoint[] var5 = new PathPoint[var3];
		var4 = pathPoint2;
		--var3;

		for(var5[var3] = pathPoint2; var4.previous != null; var5[var3] = var4) {
			var4 = var4.previous;
			--var3;
		}

		return new PathEntity(var5);
	}
}
