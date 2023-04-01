package ru.mrtenfan.metalfevermachinery.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ru.mrtenfan.metalfevermachinery.container.ContainerAlloySmelter;
import ru.mrtenfan.metalfevermachinery.container.ContainerAssembler;
import ru.mrtenfan.metalfevermachinery.container.ContainerCentrifuge;
import ru.mrtenfan.metalfevermachinery.container.ContainerElectricalFurnace;
import ru.mrtenfan.metalfevermachinery.container.ContainerMetalProcessor;
import ru.mrtenfan.metalfevermachinery.container.ContainerThermoMaterialFurnace;
import ru.mrtenfan.metalfevermachinery.init.MFMBlocks;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityAlloySmelter;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityAssembler;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityCentrifuge;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityElectricalFurnace;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityMetalProcessor;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityThermoMaterialFurnace;

public class GUIHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		
		if(entity != null) {
			switch(ID){
			case MFMBlocks.GUI_ID_assembler:
				if(entity instanceof TileEntityAssembler)
					return new ContainerAssembler(player.inventory, (TileEntityAssembler)entity);
			case MFMBlocks.GUI_ID_electrical_furnace:
				if(entity instanceof TileEntityElectricalFurnace)
					return new ContainerElectricalFurnace(player.inventory, (TileEntityElectricalFurnace)entity);
			case MFMBlocks.GUI_ID_alloy_smelter:
				if(entity instanceof TileEntityAlloySmelter)
					return new ContainerAlloySmelter(player.inventory, (TileEntityAlloySmelter)entity);
			case MFMBlocks.GUI_ID_centrifuge:
				if(entity instanceof TileEntityCentrifuge)
					return new ContainerCentrifuge(player.inventory, (TileEntityCentrifuge)entity);
			case MFMBlocks.GUI_ID_thermomaterial_furnace:
				if(entity instanceof TileEntityThermoMaterialFurnace)
					return new ContainerThermoMaterialFurnace(player.inventory, (TileEntityThermoMaterialFurnace)entity);
			case MFMBlocks.GUI_ID_metal_processor:
				if(entity instanceof TileEntityMetalProcessor)
					return new ContainerMetalProcessor(player.inventory, (TileEntityMetalProcessor)entity);
			}
			
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		
		if(entity != null) {
			switch(ID){
			case MFMBlocks.GUI_ID_assembler:
				if(entity instanceof TileEntityAssembler)
					return new GUIAssembler(player.inventory, (TileEntityAssembler)entity);
			case MFMBlocks.GUI_ID_electrical_furnace:
				if(entity instanceof TileEntityElectricalFurnace)
					return new GUIElectricalFurnace(player.inventory, (TileEntityElectricalFurnace)entity);
			case MFMBlocks.GUI_ID_alloy_smelter:
				if(entity instanceof TileEntityAlloySmelter)
					return new GUIAlloySmelter(player.inventory, (TileEntityAlloySmelter)entity);
			case MFMBlocks.GUI_ID_centrifuge:
				if(entity instanceof TileEntityCentrifuge)
					return new GUICentrifuge(player.inventory, (TileEntityCentrifuge)entity);
			case MFMBlocks.GUI_ID_thermomaterial_furnace:
				if(entity instanceof TileEntityThermoMaterialFurnace)
					return new GUIThermoMaterialFurnace(player.inventory, (TileEntityThermoMaterialFurnace)entity);
			case MFMBlocks.GUI_ID_metal_processor:
				if(entity instanceof TileEntityMetalProcessor)
					return new GUIMetalProcessor(player.inventory, (TileEntityMetalProcessor)entity);
			}
		}
		return null;
	}
}
