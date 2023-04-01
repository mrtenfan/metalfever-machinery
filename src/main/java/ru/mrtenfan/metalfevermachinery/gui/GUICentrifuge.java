package ru.mrtenfan.metalfevermachinery.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import ru.mrtenfan.metalfevermachinery.container.ContainerCentrifuge;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityCentrifuge;

public class GUICentrifuge extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("metalfevermachinery:textures/GUI/centrifuge_gui.png");
	
	TileEntityCentrifuge centrifuge;

	public GUICentrifuge(InventoryPlayer inventory, TileEntityCentrifuge entity) {
		super(new ContainerCentrifuge(inventory, entity));

		centrifuge = entity;

		xSize = 176;
		ySize = 166;
	}

	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int posX = (this.width - this.xSize) / 2;
		int posY = (this.height - this.ySize) / 2;
		int mouseXoffset = mouseX - posX;
		int mouseYoffset = mouseY - posY;
		String name = this.centrifuge.hasCustomInventoryName() ? this.centrifuge.getInventoryName() : I18n.format(this.centrifuge.getInventoryName(), new Object[0]);

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2 - 32, 4, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 4, 4210752);
		
//		System.out.println(mouseXoffset + ":" + mouseYoffset);
		if (mouseXoffset >= 16 && mouseYoffset >= 22 && mouseXoffset <= 29 && mouseYoffset <= 63) {
			List<String> text = new ArrayList<String>();
			text.add(centrifuge.getInfoEnergyStored() + " / " + centrifuge.getInfoMaxEnergyStored() + " RF");
			drawHoveringText(text, mouseXoffset, mouseYoffset, mc.fontRenderer);
		}
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float p_146976_1_, int mouseX, int mouseY) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);

		if(centrifuge.isCentrifugetion()) {
			int j = centrifuge.getCentrifugetionProgressScaled(24);
			drawTexturedModalRect(guiLeft + 58, guiTop + 28, 176, 0, j, 24);
		}
		
		if(centrifuge.getInfoMaxEnergyStored() > 0) {
			int i = centrifuge.getEnergyStoredScaled(42);
			drawTexturedModalRect(guiLeft + 16, guiTop + 22, 176, 24, 14, 42 - i);
		}
	}
}
