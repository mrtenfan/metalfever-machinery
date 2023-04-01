package ru.mrtenfan.metalfevermachinery.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import ru.mrtenfan.metalfevermachinery.MetalFeverMachinery;

public class PodstaviaOre extends Block {

	public PodstaviaOre(Material mat, Float hard, Float resist, String tool, int level) {
		super(mat);
		this.setCreativeTab(MetalFeverMachinery.TabMachineryRes);
        this.setHardness(hard);
        this.setResistance(resist);
        this.setHarvestLevel(tool, level);
        this.setBlockTextureName("metalfevermachinery:podstavia_ore");
        this.setBlockName("podstavia_ore");
	}
}
