package ru.mrtenfan.metalfevermachinery;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.mrtenfan.metalfevermachinery.init.MFMBlocks;

public class TabMachineryMain extends CreativeTabs {

	public TabMachineryMain(String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return new ItemStack(MFMBlocks.assembler).getItem();
	}
}
