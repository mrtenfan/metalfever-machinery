package ru.mrtenfan.metalfevermachinery.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.mrtenfan.metalfevermachinery.container.slots.SlotOutput;
import ru.mrtenfan.metalfevermachinery.crafting.ElectricalFurnaceRecipe;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityElectricalFurnace;

public class ContainerElectricalFurnace extends Container {

	TileEntityElectricalFurnace elecFurn;
	private int lastCookTime;
	private int lastEnergy;

	public ContainerElectricalFurnace(InventoryPlayer playerInv, TileEntityElectricalFurnace entity) {

		elecFurn = entity;

		this.addSlotToContainer(new Slot(elecFurn, 0, 56, 27)); //input
		this.addSlotToContainer(new SlotOutput(playerInv.player, elecFurn, 1, 116, 35)); //output

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
		}
	}

	public void addCraftingToCrafters(ICrafting crafting) {
		super.addCraftingToCrafters(crafting);
		crafting.sendProgressBarUpdate(this, 0, this.elecFurn.cookTime);
		crafting.sendProgressBarUpdate(this, 1, this.elecFurn.getInfoEnergyStored());
	}

	//	Tile Entity 0-1: 0-1
	//	Player Inventory 1-28: 2-28
	//	Player Inventory 0-8: 29-38
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIn) {
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(slotIn);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if(slotIn == 1) { 
				if(!this.mergeItemStack(itemstack1, 1, 38, true))
					return null;

				slot.onSlotChange(itemstack1, itemstack);
			}else if(slotIn != 0) {
				if(ElectricalFurnaceRecipe.getSmeltingResult(itemstack1) != null) {
					if(!this.mergeItemStack(itemstack1, 0, 1, false)) {
						return null;
					}
				}else if(slotIn >= 2 && slotIn < 28) {
					if (!this.mergeItemStack(itemstack1, 30, 38, false)) {
						return null;
						}
				}else if(slotIn >= 28 && slotIn < 39 && !this.mergeItemStack(itemstack1, 2, 28, false)) {
					return null;
				}
			}else if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
				return null;
			}
			
			if (itemstack1.stackSize == 0)
				slot.putStack((ItemStack)null);
			else
				slot.onSlotChanged();
			if (itemstack1.stackSize == itemstack.stackSize)
				return null;
			slot.onPickupFromSlot(player, itemstack1);
		}
		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return elecFurn.isUseableByPlayer(player);
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for(int i = 0; i < this.crafters.size(); i++) {
			ICrafting par1 = (ICrafting)this.crafters.get(i);
			if (this.lastCookTime != this.elecFurn.cookTime) {
				par1.sendProgressBarUpdate(this, 0, this.elecFurn.cookTime);
			}
			if (this.lastEnergy != this.elecFurn.getInfoEnergyStored()) {
				par1.sendProgressBarUpdate(this, 1, this.elecFurn.getInfoEnergyStored());
			}
		}

		this.lastCookTime = this.elecFurn.cookTime;
		this.lastEnergy = this.elecFurn.getInfoEnergyStored();
	}

	public void updateProgressBar(int i, int j) {
		if (i == 0) {
			elecFurn.cookTime = j;
		}
		if (i == 1) {
			elecFurn.getEnergyStorage().setEnergyStored(j);
		}
	}
}
