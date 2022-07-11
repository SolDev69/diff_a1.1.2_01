package net.minecraft.src;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

class ThreadDownloadImage extends Thread {
	final String location;
	final ImageBuffer buffer;
	final ThreadDownloadImageData imageData;

	ThreadDownloadImage(ThreadDownloadImageData downloadImageData, String location, ImageBuffer imageBuffer) {
		this.imageData = downloadImageData;
		this.location = location;
		this.buffer = imageBuffer;
	}

	public void run() {
		HttpURLConnection var1 = null;

		try {
			URL var2 = new URL(this.location);
			var1 = (HttpURLConnection)var2.openConnection();
			var1.setDoInput(true);
			var1.setDoOutput(false);
			var1.connect();
			if(var1.getResponseCode() == 404) {
				return;
			}

			if(this.buffer == null) {
				this.imageData.image = ImageIO.read(var1.getInputStream());
			} else {
				this.imageData.image = this.buffer.parseUserSkin(ImageIO.read(var1.getInputStream()));
			}
		} catch (Exception var6) {
			var6.printStackTrace();
		} finally {
			var1.disconnect();
		}

	}
}
