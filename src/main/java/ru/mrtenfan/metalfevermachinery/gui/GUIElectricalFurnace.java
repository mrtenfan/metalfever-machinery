package ru.mrtenfan.metalfevermachinery.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import ru.mrtenfan.metalfevermachinery.container.ContainerElectricalFurnace;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityElectricalFurnace;

public class GUIElectricalFurnace extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("metalfevermachinery:textures/GUI/electrical_furnace_gui.png");
	
	TileEntityElectricalFurnace elecFurn;

	public GUIElectricalFurnace(InventoryPlayer inventory, TileEntityElectricalFurnace entity) {
		super(new ContainerElectricalFurnace(inventory, entity));

		elecFurn = entity;

		xSize = 176;
		ySize = 166;
	}

	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int posX = (this.width - this.xSize) / 2;
		int posY = (this.height - this.ySize) / 2;
		int mouseXoffset = mouseX - posX;
		int mouseYoffset = mouseY - posY;
		String name = this.elecFurn.hasCustomInventoryName() ? this.elecFurn.getInventoryName() : I18n.format(this.elecFurn.getInventoryName(), new Object[0]);

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 4, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 4, 4210752);
		
//		System.out.println(mouseXoffset + ":" + mouseYoffset);
		if (mouseXoffset >= 19 && mouseYoffset >= 22 && mouseXoffset <= 32 && mouseYoffset <= 64) {
			List<String> text = new ArrayList<String>();
			text.add(elecFurn.getInfoEnergyStored() + " / " + elecFurn.getInfoMaxEnergyStored() + " RF");
			drawHoveringText(text, mouseXoffset, mouseYoffset, mc.fontRenderer);
		}
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float p_146976_1_, int mouseX, int mouseY) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);

		if(elecFurn.getInfoMaxEnergyStored() > 0) {
			int i = elecFurn.getEnergyStoredScaled(42);
			drawTexturedModalRect(guiLeft + 19, guiTop + 21, 176, 30, 14, 42 - i);
		}

		if(elecFurn.isSmelting()) {
			int j = elecFurn.getAlloyProgressScaled(14);
			drawTexturedModalRect(guiLeft + 56, guiTop + 60 - j, 176, 14 - j, 14, j);
			j = elecFurn.getAlloyProgressScaled(24);
			drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 14, j, 17);
		}
	}
}
