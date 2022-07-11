package net.minecraft.src;

public class GuiConnectFailed extends GuiScreen {
	private String message;
	private String description;

	public GuiConnectFailed(String msg, String desc) {
		this.message = msg;
		this.description = desc;
	}

	public void updateScreen() {
	}

	protected void keyTyped(char character, int key) {
	}

	public void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, "Back to title screen"));
	}

	protected void actionPerformed(GuiButton button) {
		if(button.id == 0) {
			this.mc.displayGuiScreen(new GuiMainMenu());
		}

	}

	public void drawScreen(int mouseX, int mouseY, float renderPartialTick) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.message, this.width / 2, this.height / 2 - 50, 0xFFFFFF);
		this.drawCenteredString(this.fontRenderer, this.description, this.width / 2, this.height / 2 - 10, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, renderPartialTick);
	}
}
