package ru.mrtenfan.metalfevermachinery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.mrtenfan.metalfevermachinery.container.slots.SlotOutput;
import ru.mrtenfan.metalfevermachinery.crafting.AssemblerRecipe;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityAssembler;

public class ContainerAssembler extends Container {
	
	TileEntityAssembler assembler;
	private int lastCookTime;
	private int lastEnergy;
	
	public ContainerAssembler(InventoryPlayer playerInv, TileEntityAssembler entity) {
		
		assembler = entity;
		
        for (int l = 0; l < 3; ++l) {
            for (int i1 = 0; i1 < 3; ++i1) {
                this.addSlotToContainer(new Slot(assembler, i1 + l * 3, 44 + i1 * 18, 16 + l * 18)); //craft matrix
            }
        }
        
        this.addSlotToContainer(new SlotOutput(playerInv.player, assembler, 9, 138, 34)); //output
		
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
		crafting.sendProgressBarUpdate(this, 0, this.assembler.cookTime);
		crafting.sendProgressBarUpdate(this, 1, this.assembler.getInfoEnergyStored());
	}

	//	Tile Entity 0-9: 0-9
	//	Player Inventory 9-36: 10-36
	//	Player Inventory 0-8: 37-46
	@Override
    public ItemStack transferStackInSlot(EntityPlayer playerInv, int slotIn) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotIn);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(slotIn == 9) {
            	if(!this.mergeItemStack(itemstack1, 10, 46, true)) {
            		return null;
            	}
            	
            	slot.onSlotChange(itemstack1, itemstack);
            }else if(slotIn > 9) {
            	if(AssemblerRecipe.isUsedInRecipe(itemstack1)) {
            		if(!this.mergeItemStack(itemstack1, 0, 9, false)) {
            			return null;
            		}
            	}else if(slotIn >= 10 && slotIn < 36) {
            		if(!this.mergeItemStack(itemstack1, 37, 46, false)) {
            			return null;
            		}
            	}else if(slotIn >= 37 && slotIn < 47 && !this.mergeItemStack(itemstack1, 10, 36, false)) {
            		return null;
            	}
            }else if(!this.mergeItemStack(itemstack1, 10, 46, false)) {
            	return null;
            }
            
//            else if (slotIn != 1 && slotIn != 0) {
//                if (AssemblerRecipe.getAssemblingResult(itemStacks) != null) {
//                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
//                        return null;
//                    }
//                }
//                else if (slotIn >= 3 && slotIn < 30) {
//                    if (!this.mergeItemStack(itemstack1, 30, 45, false))
//                    {
//                        return null;
//                    }
//                }
//                else if (slotIn >= 30 && slotIn < 45 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
//                    return null;
//                }
//            }
//            if (!this.mergeItemStack(itemstack1, 3, 45, false)) {
//                return null;
//            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(playerInv, itemstack1);
        }

        return itemstack;
    }

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return assembler.isUseableByPlayer(player);
	}
	
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for(int i = 0; i < this.crafters.size(); i++) {
			ICrafting par1 = (ICrafting)this.crafters.get(i);
			if (this.lastCookTime != this.assembler.cookTime) {
				par1.sendProgressBarUpdate(this, 0, this.assembler.cookTime);
			}
			if (this.lastEnergy != this.assembler.getInfoEnergyStored()) {
				par1.sendProgressBarUpdate(this, 1, this.assembler.getInfoEnergyStored());
			}
		}

		this.lastCookTime = this.assembler.cookTime;
		this.lastEnergy = this.assembler.getInfoEnergyStored();
	}
	
	public void updateProgressBar(int i, int j) {
		if (i == 0) {
			assembler.cookTime = j;
		}
		if (i == 1) {
			assembler.getEnergyStorage().setEnergyStored(j);
		}
	}
}
