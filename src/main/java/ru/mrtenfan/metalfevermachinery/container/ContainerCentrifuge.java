package ru.mrtenfan.metalfevermachinery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.mrtenfan.metalfevermachinery.container.slots.SlotCentrifuge;
import ru.mrtenfan.metalfevermachinery.crafting.CentrifugeRecipe;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityCentrifuge;

public class ContainerCentrifuge extends Container {
	
	TileEntityCentrifuge centrifuge;
	private int lastCookTime;
	private int lastEnergy;

	public ContainerCentrifuge(InventoryPlayer playerInv, TileEntityCentrifuge entity) {
		
		centrifuge = entity;
		
		this.addSlotToContainer(new Slot(centrifuge, 0, 36, 32)); //input
        this.addSlotToContainer(new SlotCentrifuge(playerInv.player, centrifuge, 1, 115, 32)); //output 1
        this.addSlotToContainer(new SlotCentrifuge(playerInv.player, centrifuge, 2, 115, 6)); //output 2
        this.addSlotToContainer(new SlotCentrifuge(playerInv.player, centrifuge, 3, 142, 32)); //output 3
        this.addSlotToContainer(new SlotCentrifuge(playerInv.player, centrifuge, 4, 115, 60)); //output 4
        this.addSlotToContainer(new SlotCentrifuge(playerInv.player, centrifuge, 5, 88, 32)); //output 5

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
		crafting.sendProgressBarUpdate(this, 0, this.centrifuge.cookTime);
		crafting.sendProgressBarUpdate(this, 1, this.centrifuge.getInfoEnergyStored());
	}

	//	Tile Entity 0-5: 0-5
	//	Player Inventory 6-33: 6-32
	//	Player Inventory 0-8: 33-42
	@Override
    public ItemStack transferStackInSlot(EntityPlayer playerInv, int slotIn) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotIn);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if(slotIn >= 0 && slotIn < 6) {
            	if(!this.mergeItemStack(itemstack1, 5, 42, true)) {
            		return null;
            	}
        	
            	slot.onSlotChange(itemstack1, itemstack);
            }else if(slotIn != 0) {
            	if(CentrifugeRecipe.hasResult(itemstack1)) {
            		if(!this.mergeItemStack(itemstack1, 0, 1, false)) {
            			return null;
            		}
            	}else if(slotIn >= 6 && slotIn < 33) {
            		if(!this.mergeItemStack(itemstack1, 33, 42, false)) {
            			return null;
            		}
            	}else if(slotIn >= 33 && slotIn < 43 && !this.mergeItemStack(itemstack1, 6, 32, false)) {
            		return null;
            	}
            }else if(!this.mergeItemStack(itemstack1, 6, 42, false)) {
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
		return centrifuge.isUseableByPlayer(player);
	}
	
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for(int i = 0; i < this.crafters.size(); i++) {
			ICrafting par1 = (ICrafting)this.crafters.get(i);
			if (this.lastCookTime != this.centrifuge.cookTime) {
				par1.sendProgressBarUpdate(this, 0, this.centrifuge.cookTime);
			}
			if (this.lastEnergy != this.centrifuge.getInfoEnergyStored()) {
				par1.sendProgressBarUpdate(this, 1, this.centrifuge.getInfoEnergyStored());
			}
		}

		this.lastCookTime = this.centrifuge.cookTime;
		this.lastEnergy = this.centrifuge.getInfoEnergyStored();
	}
	
	public void updateProgressBar(int i, int j) {
		if (i == 0) {
			centrifuge.cookTime = j;
		}
		if (i == 1) {
			centrifuge.getEnergyStorage().setEnergyStored(j);
		}
	}
}
