package ru.mrtenfan.metalfevermachinery;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.mrtenfan.metalfevermachinery.init.MFMItems;

public class TabMachineryRes extends CreativeTabs {

	public TabMachineryRes(String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return new ItemStack(MFMItems.ingots).getItem();
	}
}
