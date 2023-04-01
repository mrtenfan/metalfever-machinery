package ru.mrtenfan.metalfevermachinery.blocks;

import java.util.Random;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ru.mrtenfan.metalfevermachinery.MetalFeverMachinery;
import ru.mrtenfan.metalfevermachinery.init.MFMBlocks;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityAssembler;

public class Assembler extends BlockContainer {

	private boolean isActive;
	private Random rand;
	private static boolean keepInventory = false;
	private String texPath= "metalfevermachinery:";
	@SideOnly(Side.CLIENT)
	protected IIcon BlockIconFront;
	protected IIcon BlockIconSide;
	protected IIcon BlockIconTop;
	protected IIcon BlockIconDown;

	public Assembler(boolean work) {
		super(Material.iron);
		if(!work)
		this.setCreativeTab(MetalFeverMachinery.TabMachineryMain);
		this.setHardness(7.5F);
		this.setResistance(10F);
		this.setHarvestLevel("pickaxe", 1);
		this.setStepSound(soundTypeMetal);
		rand = new Random();
		this.setBlockTextureName("metalfevermachinery:machine_side");
		this.setBlockName("assembler");
		this.isActive = work;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemFromBlock(MFMBlocks.assembler);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityAssembler();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// Регистрируем путь до png-текстур для разных сторон блока
		BlockIconFront = par1IconRegister.registerIcon(this.isActive ? texPath + "assembler_front_active" : texPath + "assembler_front");
		BlockIconSide = par1IconRegister.registerIcon(texPath + "machine_side");
		BlockIconTop = par1IconRegister.registerIcon(texPath + "machine_top");
		BlockIconDown = par1IconRegister.registerIcon(texPath + "machine_bottom");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		if (side == 0)  
			return BlockIconDown; 
		if (side == 1)  
			return BlockIconTop; 
		if (meta == 2 && side == 2)  
			return BlockIconFront; 
		if (meta == 3 && side == 5)  
			return BlockIconFront; 
		if (meta == 0 && side == 3)  
			return BlockIconFront; 
		if (meta == 1 && side == 4)  
			return BlockIconFront; 
		return BlockIconSide;
	}

	// Этот метод вызывается когда блок ставится кем-то
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityPlayer, ItemStack itemStack){
		int i = MathHelper.floor_double((double)(entityPlayer.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		world.setBlockMetadataWithNotify(x, y, z, i, 2);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}else if (!player.isSneaking()) {
			TileEntityAssembler entity = (TileEntityAssembler) world.getTileEntity(x, y, z);
			if (entity != null) {
				FMLNetworkHandler.openGui(player, MetalFeverMachinery.instance, MFMBlocks.GUI_ID_assembler, world, x, y, z);
			}
			return true;
		} else {
			return false;
		}
	}

	public static void updateBlockState(boolean isAlloying, World world, int xCoord, int yCoord, int zCoord) {

		int i = world.getBlockMetadata(xCoord, yCoord, zCoord);
		TileEntity entity = world.getTileEntity(xCoord, yCoord, zCoord);
		keepInventory = true;

		if (isAlloying) 
			world.setBlock(xCoord, yCoord, zCoord, MFMBlocks.assembler_active);
		else 
			world.setBlock(xCoord, yCoord, zCoord, MFMBlocks.assembler);

		keepInventory = false;
		world.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, i, 2);

		if (entity != null) {
			entity.validate();
			world.setTileEntity(xCoord, yCoord, zCoord, entity);
		}
	}

	public void breakBlock(World world, int x, int y, int z, Block oldblock, int oldMetadata) {
		if (!keepInventory) {
			TileEntityAssembler entity = (TileEntityAssembler) world.getTileEntity(x, y, z);

			if(entity != null) {
				for(int i = 0; i < entity.getSizeInventory(); i++) {
					ItemStack itemstack = entity.getStackInSlot(i);

					if(itemstack != null) {
						float f = this.rand.nextFloat() * 0.8F + 0.1F;
						float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
						float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

						while(itemstack.stackSize > 0) {
							int j = this.rand.nextInt(21) + 10;

							if(j > itemstack.stackSize) {
								j = itemstack.stackSize;
							}

							itemstack.stackSize -=j;

							EntityItem item = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage())); 

							if(itemstack.hasTagCompound()) {
								item.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
							}

							world.spawnEntityInWorld(item);
						}
					}
				}

				world.func_147453_f(x, y, z, oldblock);
			}
		}

		super.breakBlock(world, x, y, z, oldblock, oldMetadata);
	}

	public Item getItem(World world, int x, int y, int z) {
		return Item.getItemFromBlock(MFMBlocks.assembler);
	}
}
