package ru.mrtenfan.metalfevermachinery.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import ru.mrtenfan.metalfevermachinery.container.ContainerAssembler;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityAssembler;

public class GUIAssembler extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("metalfevermachinery:textures/GUI/assembly_table_GUI.png");
	
	TileEntityAssembler assembler;

	public GUIAssembler(InventoryPlayer inventory, TileEntityAssembler entity) {
		super(new ContainerAssembler(inventory, entity));

		assembler = entity;

		xSize = 176;
		ySize = 166;
	}

	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int posX = (this.width - this.xSize) / 2;
		int posY = (this.height - this.ySize) / 2;
		int mouseXoffset = mouseX - posX;
		int mouseYoffset = mouseY - posY;
		String name = this.assembler.hasCustomInventoryName() ? this.assembler.getInventoryName() : I18n.format(this.assembler.getInventoryName(), new Object[0]);

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 4, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 4, 4210752);
		
//		System.out.println(mouseXoffset + ":" + mouseYoffset);
		if (mouseXoffset >= 19 && mouseYoffset >= 21 && mouseXoffset <= 32 && mouseYoffset <= 62) {
			List<String> text = new ArrayList<String>();
			text.add(assembler.getInfoEnergyStored() + " / " + assembler.getInfoMaxEnergyStored() + " RF");
			drawHoveringText(text, mouseXoffset, mouseYoffset, mc.fontRenderer);
		}
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float p_146976_1_, int mouseX, int mouseY) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);

		if(assembler.getInfoMaxEnergyStored() > 0) {
			int i = assembler.getEnergyStoredScaled(42);
			drawTexturedModalRect(guiLeft + 19, guiTop + 21, 176, 17, 14, 42 - i);
		}

		if(assembler.isAssembly()) {
			int j = assembler.getAlloyProgressScaled(24);
			drawTexturedModalRect(guiLeft + 103, guiTop + 33, 176, 0, j, 17);
		}
	}
}
