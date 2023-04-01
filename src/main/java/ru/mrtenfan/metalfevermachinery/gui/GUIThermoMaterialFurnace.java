package ru.mrtenfan.metalfevermachinery.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import ru.mrtenfan.metalfevermachinery.container.ContainerThermoMaterialFurnace;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityThermoMaterialFurnace;

public class GUIThermoMaterialFurnace extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("metalfevermachinery:textures/GUI/thermo-material_furnace_gui.png");
	
	TileEntityThermoMaterialFurnace alloySmelt;

	public GUIThermoMaterialFurnace(InventoryPlayer inventory, TileEntityThermoMaterialFurnace entity) {
		super(new ContainerThermoMaterialFurnace(inventory, entity));

		alloySmelt = entity;

		xSize = 176;
		ySize = 166;
	}

	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int posX = (this.width - this.xSize) / 2;
		int posY = (this.height - this.ySize) / 2;
		int mouseXoffset = mouseX - posX;
		int mouseYoffset = mouseY - posY;
		String name = this.alloySmelt.hasCustomInventoryName() ? this.alloySmelt.getInventoryName() : I18n.format(this.alloySmelt.getInventoryName(), new Object[0]);

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 4, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 4, 4210752);
		
//		System.out.println(mouseXoffset + ":" + mouseYoffset);
		if (mouseXoffset >= 17 && mouseYoffset >= 22 && mouseXoffset <= 30 && mouseYoffset <= 64) {
			List<String> text = new ArrayList<String>();
			text.add(alloySmelt.getInfoEnergyStored() + " / " + alloySmelt.getInfoMaxEnergyStored() + " RF");
			drawHoveringText(text, mouseXoffset, mouseYoffset, mc.fontRenderer);
		}
		if (mouseXoffset >= 42 && mouseYoffset >= 48 && mouseXoffset <= 55 && mouseYoffset <= 62) {
			List<String> text = new ArrayList<String>();
			text.add(alloySmelt.temperature + " / " + alloySmelt.maxTemperature + " °C");
			drawHoveringText(text, mouseXoffset, mouseYoffset, mc.fontRenderer);
		}
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float p_146976_1_, int mouseX, int mouseY) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);

		if(alloySmelt.getInfoMaxEnergyStored() > 0) {
			int i = alloySmelt.getEnergyStoredScaled(42);
			drawTexturedModalRect(guiLeft + 17, guiTop + 22, 176, 31, 14, 42 - i);
		}

		if(alloySmelt.isAlloying()) {
			int j = alloySmelt.getAlloyProgressScaled(24);
			drawTexturedModalRect(guiLeft + 83, guiTop + 34, 176, 0, j, 17);
		}

		int j = alloySmelt.getTemperatureScaled(14);
		drawTexturedModalRect(guiLeft + 41, guiTop + 61 - j, 176, 31 - j, 14, j);
	}
}
