package net.minecraft.src;

import java.io.File;

class WorldIso extends World {
	final CanvasIsomPreview isomPreview;

	WorldIso(CanvasIsomPreview var1, File var2, String var3) {
		super(var2, var3);
		this.isomPreview = var1;
	}

	protected IChunkProvider getChunkProvider(File file) {
		return new ChunkProviderIso(this, new ChunkLoader(file, false));
	}
}
