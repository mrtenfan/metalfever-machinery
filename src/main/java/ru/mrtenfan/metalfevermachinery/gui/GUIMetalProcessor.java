package ru.mrtenfan.metalfevermachinery.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import ru.mrtenfan.metalfevermachinery.container.ContainerMetalProcessor;
import ru.mrtenfan.metalfevermachinery.packages.NetworkHandler;
import ru.mrtenfan.metalfevermachinery.packages.PacketMetalProcessor.MetalProcessorHandler;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityMetalProcessor;

public class GUIMetalProcessor extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("metalfevermachinery:textures/GUI/metal_processor_gui.png");

	TileEntityMetalProcessor entity;

	GuiButton button;

	public GUIMetalProcessor(InventoryPlayer playerInv, TileEntityMetalProcessor entityRM) {
		super(new ContainerMetalProcessor(playerInv, entityRM));

		entity = entityRM;

		xSize = 176;
		ySize = 166;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		int posX = (this.width - this.xSize) / 2;
		int posY = (this.height - this.ySize) / 2;

		this.buttonList.add(button = new GuiButton(0, posX + 75, posY + 17, 18, 18, ""));

		super.initGui();
	}

	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int posX = (this.width - this.xSize) / 2;
		int posY = (this.height - this.ySize) / 2;
		int mouseXoffset = mouseX - posX;
		int mouseYoffset = mouseY - posY;
		String name = this.entity.hasCustomInventoryName() ? this.entity.getInventoryName() : I18n.format(this.entity.getInventoryName(), new Object[0]);

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 4, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 4, 4210752);

		//		System.out.println(mouseXoffset + ":" + mouseYoffset);
		if(mouseXoffset >= 27 && mouseYoffset >= 24 && mouseXoffset <= 40 && mouseYoffset <= 65) {
			List<String> text = new ArrayList<String>();
			text.add(entity.getInfoEnergyStored() + " / " + entity.getInfoMaxEnergyStored() + " RF");
			drawHoveringText(text, mouseXoffset, mouseYoffset, mc.fontRenderer);
		}
		if(mouseXoffset >= 75 && mouseYoffset >= 17 && mouseXoffset <= 93 && mouseYoffset <= 34) {
			List<String> text = new ArrayList<String>();
			text.add(entity.getModeName());
			drawHoveringText(text, mouseXoffset, mouseYoffset, mc.fontRenderer);
		}
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float p_146976_1_, int mouseX, int mouseY) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);

		if(entity.getInfoMaxEnergyStored() > 0) {
			int i = entity.getEnergyStoredScaled(42);
			drawTexturedModalRect(guiLeft + 27, guiTop + 24, 176, 55, 14, 42 - i);
		}

		int j = entity.getRollingProgressScaled(10);
		drawTexturedModalRect(guiLeft + 75, guiTop + 42, 176, 0+(5 * j), 20, 5);

		button.displayString = entity.getModeName().substring(0, 1);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.id == 0) {
			entity.setMode(entity.getNextMode(entity.getMode()));
			NetworkHandler.NETWORK.sendToServer(new MetalProcessorHandler(entity.getMode()));
		}
		super.actionPerformed(button);
	}
	
	public int getStringWidth(String str) {
		return fontRendererObj.getStringWidth(str);
	}
}
