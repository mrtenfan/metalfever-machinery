package ru.mrtenfan.metalfevermachinery.container.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOutput extends Slot {

	public SlotOutput(EntityPlayer player, IInventory inventory , int i, int j, int k) {
		super(inventory, i, j, k);
	}
	
	public boolean isItemValid(ItemStack itemStack) {
		return false;
	}
}
