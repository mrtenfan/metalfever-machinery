package ru.mrtenfan.metalfevermachinery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.mrtenfan.metalfevermachinery.container.slots.SlotOutput;
import ru.mrtenfan.metalfevermachinery.crafting.MetalProcessorRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.MetalProcessorRecipe.MetalProcessorModes;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityMetalProcessor;

public class ContainerMetalProcessor extends Container {
	
	public TileEntityMetalProcessor entity;
	private int lastCookTime;
	private int lastEnergy;
	private int lastMode;

	public TileEntityMetalProcessor getEntity() {
		return entity;
	}
	
	public ContainerMetalProcessor(InventoryPlayer playerInv, TileEntityMetalProcessor MetalProcessor) {
		entity = MetalProcessor;

        this.addSlotToContainer(new Slot(entity, 0, 50, 37)); //input
//        this.addSlotToContainer(new SlotShape(playerInv.player, entity, 1, 76, 18)); //shape
        this.addSlotToContainer(new SlotOutput(playerInv.player, entity, 2, 106, 37)); //output

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
		crafting.sendProgressBarUpdate(this, 0, this.entity.cookTime);
		crafting.sendProgressBarUpdate(this, 1, this.entity.getInfoEnergyStored());
		crafting.sendProgressBarUpdate(this, 2, this.entity.getMode().ordinal());
	}

	//	Tile Entity 0-2: 0-2
	//	Player Inventory 3-29: 3-29
	//	Player Inventory 0-8: 30-38
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
            	if(MetalProcessorRecipe.usedForCrafting(itemstack1)) {
            		if(!this.mergeItemStack(itemstack1, 0, 1, false)) {
            			return null;
            		}
            	}else if(slotIn >= 3 && slotIn < 29) {
            		if(!this.mergeItemStack(itemstack1, 30, 38, false)) {
            			return null;
            		}
            	}else if(slotIn >= 29 && slotIn < 39 && !this.mergeItemStack(itemstack1, 3, 29, false)) {
            		return null;
            	}
            }else if(!this.mergeItemStack(itemstack1, 3, 38, false)) {
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
		return entity.isUseableByPlayer(player);
	}
	
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for(int i = 0; i < this.crafters.size(); i++) {
			ICrafting par1 = (ICrafting)this.crafters.get(i);
			if (this.lastCookTime != this.entity.cookTime) {
				par1.sendProgressBarUpdate(this, 0, this.entity.cookTime);
			}
			if (this.lastEnergy != this.entity.getInfoEnergyStored()) {
				par1.sendProgressBarUpdate(this, 1, this.entity.getInfoEnergyStored());
			}
			if(this.lastMode != this.entity.getMode().ordinal()) {
				par1.sendProgressBarUpdate(this, 2, this.entity.getMode().ordinal());
			}
		}

		this.lastCookTime = this.entity.cookTime;
		this.lastEnergy = this.entity.getInfoEnergyStored();
		this.lastMode = this.entity.getMode().ordinal();
	}
	
	public void updateProgressBar(int i, int j) {
		if(i == 0) {
			entity.cookTime = j;
		}
		if(i == 1) {
			entity.getEnergyStorage().setEnergyStored(j);
		}
		if(i == 2) {
			entity.setMode(MetalProcessorModes.values()[j]);
		}
	}
}
