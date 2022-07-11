package net.minecraft.src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.Timer;

public class GuiStatsComponent extends JComponent {
	private int[] memoryUse = new int[256];
	private int updateCounter = 0;
	private String[] displayStrings = new String[10];

	public GuiStatsComponent() {
		this.setPreferredSize(new Dimension(256, 196));
		this.setMinimumSize(new Dimension(256, 196));
		this.setMaximumSize(new Dimension(256, 196));
		(new Timer(500, new GuiStatsListener(this))).start();
		this.setBackground(Color.BLACK);
	}

	private void update() {
		long var1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.gc();
		this.displayStrings[0] = "Memory use: " + var1 / 1024L / 1024L + " mb (" + Runtime.getRuntime().freeMemory() * 100L / Runtime.getRuntime().maxMemory() + "% free)";
		this.displayStrings[1] = "Threads: " + NetworkManager.numReadThreads + " + " + NetworkManager.numWriteThreads;
		this.memoryUse[this.updateCounter++ & 255] = (int)(var1 * 100L / Runtime.getRuntime().maxMemory());
		this.repaint();
	}

	public void paint(Graphics graphics) {
		graphics.setColor(new Color(0xFFFFFF));
		graphics.fillRect(0, 0, 256, 192);

		int var2;
		for(var2 = 0; var2 < 256; ++var2) {
			int var3 = this.memoryUse[var2 + this.updateCounter & 255];
			graphics.setColor(new Color(var3 + 28 << 16));
			graphics.fillRect(var2, 100 - var3, 1, var3);
		}

		graphics.setColor(Color.BLACK);

		for(var2 = 0; var2 < this.displayStrings.length; ++var2) {
			String var4 = this.displayStrings[var2];
			if(var4 != null) {
				graphics.drawString(var4, 32, 116 + var2 * 16);
			}
		}

	}

	static void update(GuiStatsComponent component) {
		component.update();
	}
}
