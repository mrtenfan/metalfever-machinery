package ru.mrtenfan.metalfevermachinery.init;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MFMLibOreDict {
	
	public static final String[] INGOTS = new String[] {"ingotDiamingot", "ingotEmeringot", "ingotGlowingot", "ingotLapingot", "ingotRedingot", "ingotPodstavia"};
	
	public static final String[] NUGGETS = new String[] {"nuggetDiamingot", "nuggetEmeringot", "nuggetGlowingot", "nuggetLapingot", "nuggetRedingot"};

	public static final String[] PLATES = new String[] {"plateDiamingot", "plateEmeringot", "plateGlowingot", "plateLapingot", "plateRedingot"};
	
	public static final String[] DUSTS = new String[] {"dustDiamond", "dustEmerald", "dustLapis"};
	
//	public static final String[] 
	
	public static void initEntries() {
		OreDictionary.registerOre("orePodstavia", MFMBlocks.podstavia_ore);
		
		int i;
		for(i = 0; i < INGOTS.length; i++) {
			OreDictionary.registerOre(INGOTS[i], new ItemStack(MFMItems.ingots, 1, i));
		}
		for(i = 0; i < NUGGETS.length; i++) {
			OreDictionary.registerOre(NUGGETS[i], new ItemStack(MFMItems.nuggets, 1, i));
		}
		for(i = 0; i < PLATES.length; i++) {
			OreDictionary.registerOre(PLATES[i], new ItemStack(MFMItems.plates, 1, i));
		}
		for(i = 0; i < DUSTS.length; i++) {
			OreDictionary.registerOre(DUSTS[i], new ItemStack(MFMItems.dusts, 1, i));
		}
	}
}
