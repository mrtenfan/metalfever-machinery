package ru.mrtenfan.metalfevermachinery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.mrtenfan.metalfevermachinery.container.slots.SlotOutput;
import ru.mrtenfan.metalfevermachinery.crafting.ThermoMaterialFurnaceRecipe;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityThermoMaterialFurnace;

public class ContainerThermoMaterialFurnace extends Container {
	
	TileEntityThermoMaterialFurnace alloySmelt;
	private int lastCookTime;
	private int lastEnergy;
	private int lastTemperature;

	public ContainerThermoMaterialFurnace(InventoryPlayer playerInv, TileEntityThermoMaterialFurnace entity) {
		
		alloySmelt = entity;

        this.addSlotToContainer(new Slot(alloySmelt, 0, 40, 27)); //input
        this.addSlotToContainer(new Slot(alloySmelt, 1, 62, 27)); //input
        this.addSlotToContainer(new SlotOutput(playerInv.player, alloySmelt, 2, 116, 35)); //output

		for (int i = 0; i < 3; ++i){
	        for (int j = 0; j < 9; ++j){
	            this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
	          }
	         }
	    for (int i = 0; i < 9; ++i){
	        this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
	    }
	}
	
	public void addCraftingToCrafters(ICrafting crafting) {
		super.addCraftingToCrafters(crafting);
		crafting.sendProgressBarUpdate(this, 0, this.alloySmelt.cookTime);
		crafting.sendProgressBarUpdate(this, 1, this.alloySmelt.getInfoEnergyStored());
		crafting.sendProgressBarUpdate(this, 2, this.alloySmelt.temperature);
	}

	//	Tile Entity 0-2: 0-2
	//	Player Inventory 3-29: 3-29
	//	Player Inventory 0-8: 30-39
	@Override
    public ItemStack transferStackInSlot(EntityPlayer playerInv, int slotIn) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotIn);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if(slotIn == 2) {
            	if(!this.mergeItemStack(itemstack1, 3, 39, true)) {
            		return null;
            	}
            	
            	slot.onSlotChange(itemstack1, itemstack);
            }else if(slotIn != 1 && slotIn != 0) {
            	if(ThermoMaterialFurnaceRecipe.isUsedForRecipe(itemstack1)) {
            		if(!this.mergeItemStack(itemstack1, 0, 2, false)) {
            			return null;
            		}
            	}else if(slotIn >= 3 && slotIn < 29) {
            		if(!this.mergeItemStack(itemstack1, 30, 38, false)) {
            			return null;
            		}
            	}else if(slotIn >= 29 && slotIn < 39 && !this.mergeItemStack(itemstack1, 3, 29, false)) {
            		return null;
            	}
            }else if(!this.mergeItemStack(itemstack1, 3, 39, false)) {
            	return null;
            }

			if (itemstack1.stackSize == 0)
				slot.putStack((ItemStack)null);
			else
				slot.onSlotChanged();
			if (itemstack1.stackSize == itemstack.stackSize)
				return null;
			slot.onPickupFromSlot(playerInv, itemstack1);
		}
		return itemstack;
    }

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return alloySmelt.isUseableByPlayer(player);
	}
	
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for(int i = 0; i < this.crafters.size(); i++) {
			ICrafting par1 = (ICrafting)this.crafters.get(i);
			if (this.lastCookTime != this.alloySmelt.cookTime) {
				par1.sendProgressBarUpdate(this, 0, this.alloySmelt.cookTime);
			}
			if (this.lastEnergy != this.alloySmelt.getInfoEnergyStored()) {
				par1.sendProgressBarUpdate(this, 1, this.alloySmelt.getInfoEnergyStored());
			}
			if (this.lastTemperature != this.alloySmelt.temperature) {
				par1.sendProgressBarUpdate(this, 2, this.alloySmelt.temperature);
			}
		}

		this.lastCookTime = this.alloySmelt.cookTime;
		this.lastEnergy = this.alloySmelt.getInfoEnergyStored();
		this.lastTemperature = this.alloySmelt.temperature;
	}
	
	public void updateProgressBar(int i, int j) {
		if (i == 0) {
			alloySmelt.cookTime = j;
		}
		if (i == 1) {
			alloySmelt.getEnergyStorage().setEnergyStored(j);
		}
		if(i == 2) {
			alloySmelt.temperature = j;
		}
	}
}
