package ru.mrtenfan.metalfevermachinery.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import ru.mrtenfan.MTFCore.Debuging;
import ru.mrtenfan.metalfevermachinery.MetalFeverMachinery;
import ru.mrtenfan.metalfevermachinery.items.DustsClass;
import ru.mrtenfan.metalfevermachinery.items.IngotsClass;
import ru.mrtenfan.metalfevermachinery.items.NuggetsClass;
import ru.mrtenfan.metalfevermachinery.items.PlatesClass;

public class MFMItems {
	
	public static Item ingots;
	public static Item nuggets;
	public static Item dusts;
	public static Item plates;

	public static void preInit() {
		ingots = new IngotsClass();
		nuggets = new NuggetsClass();
		dusts = new DustsClass();
		plates = new PlatesClass();
	}

	public static void init() {
		gameRegister(ingots, "item.ingots");
		gameRegister(nuggets, "item.nuggets");
		gameRegister(dusts, "item.dusts");
		gameRegister(plates, "item.plates");
	}

	private static void gameRegister(Object object, String name) {
		if(object instanceof Item) {
			GameRegistry.registerItem((Item)object, name);
			MetalFeverMachinery.addRegisteredName(name);
		} else
			Debuging.errorOutput("Something goes wrong with game registering " + name, MetalFeverMachinery.modName);
	}
}
