package ru.mrtenfan.metalfevermachinery.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import ru.mrtenfan.metalfevermachinery.ConfigFile;
import ru.mrtenfan.metalfevermachinery.blocks.ThermoMaterialFurnace;
import ru.mrtenfan.metalfevermachinery.crafting.ThermoMaterialFurnaceRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.ThermoMaterialFurnaceRecipe.TMFurnaceRecipe;

public class TileEntityThermoMaterialFurnace extends TileEntityAdvMachine implements ISidedInventory {

	private String localyzedName;
	private ItemStack slots[];

	public static int alloyingSpeed = ConfigFile.alloyingSpeedTMF; //in ticks
	public int cookTime;
	public int temperature;
	public boolean isRedstone;
	public final int maxTemperature = 2002;

	private static final int[] slots_in = new int[] {0, 1};
	private static final int[] slots_out = new int[] {2};

	public TileEntityThermoMaterialFurnace() {
		slots = new ItemStack[3];
	}

	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return slots[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (slots[i] != null) {
			if (slots[i].stackSize <= j ) {
				ItemStack itemStack = slots[i];
				slots[i] = null;
				return itemStack;
			}

			ItemStack itemStack1 = slots[i].splitStack(j);

			if (slots[i].stackSize == 0)
				slots[i] = null;

			return itemStack1;
		}else
			return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		slots[i] = itemStack;
		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
			itemStack.stackSize = getInventoryStackLimit();
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.localyzedName : "container.thermo-material_furnace";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.localyzedName != null && this.localyzedName.length() > 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return slot == 0 ? true : false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? slots_out : slots_in;
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack itemStack, int var3) {
		return this.isItemValidForSlot(var1, itemStack);
	}

	@Override
	public boolean canExtractItem(int var1, ItemStack itemStack, int var3) {
		return var3 != 0 || var1 != 1;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList list = nbt.getTagList("Items", 10);
		slots = new ItemStack[getSizeInventory()];

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound nbt1 = (NBTTagCompound)list.getCompoundTagAt(i);
			byte b0 = nbt1.getByte("Slot");

			if (b0 >= 0 && b0 < slots.length) {
				slots[b0] = ItemStack.loadItemStackFromNBT(nbt1);
			}
		}

		cookTime = nbt.getShort("CookTime");
		temperature = nbt.getShort("Temperature");
		isRedstone = nbt.getBoolean("IsRedstone");
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort("CookTime", (short)cookTime);
		nbt.setShort("Temperature", (short)temperature);
		nbt.setBoolean("IsRedstone", this.isRedstone);
		NBTTagList list = new NBTTagList();

		for (int i = 0; i < slots.length; i++ ) {
			if (slots[i] != null) {
				NBTTagCompound nbt1 = new NBTTagCompound();
				nbt1.setByte("Slot", (byte)i);
				slots[i].writeToNBT(nbt1);
				list.appendTag(nbt1);
			}
		}

		nbt.setTag("Items", list);
	}

	public boolean isAlloying() {
		return cookTime > 0;
	}

	public boolean hasEnergy() {
		return energyStorage.getEnergyStored() > 0;
	}
	
	private boolean canAlloy(TMFurnaceRecipe recipe) {
		if (slots[0] == null || slots[1] == null)
			return false;
		
		ItemStack itemStack = recipe.getResult(); 
		if(itemStack == null) return false;
		int i = itemStack.stackSize;
		
		if(slots[2] == null) return true;
		if(!slots[2].isItemEqual(itemStack)) return false;
		else return slots[2].stackSize + i <= 64;
		
	}

	private boolean tempEqualRecipe(TMFurnaceRecipe recipe) {
		return temperature >= recipe.getTemp();
	}
	
	private void alloyItem(TMFurnaceRecipe recipe) {
		if (canAlloy(recipe)) {
			ItemStack itemStack = recipe.getResult();
		    int[] number = recipe.getInputNumbers(slots[0], slots[1]);
			
			if (slots[2] == null)
				slots[2] = itemStack.copy();
			else if (slots[2].isItemEqual(itemStack)) 
				slots[2].stackSize += itemStack.stackSize;
			
			
			for (int i = 0; i < 2; i++) {
				if (slots[i].stackSize <= 0)
					slots[i] = new ItemStack(slots[i].getItem().setFull3D());
				else
					slots[i].stackSize -= number[i];
				
				
				if (slots[i].stackSize <= 0)
					slots[i] = null;
			}
		}
	}
	
	public void updateEntity() {
		boolean flag = this.cookTime > 0;
		boolean flag1 = false;
		boolean flag2 = this.temperature > 0;
		TMFurnaceRecipe recipe = ThermoMaterialFurnaceRecipe.getAlloyingRecipe(slots[0], slots[1]);
		int RFPerTick = (recipe != null ? recipe.getRFPerTick() : 0);
		int RFPerTemp = ConfigFile.RFPerTempTMF;

		if(this.hasEnergy() && this.canAlloy(recipe) && this.tempEqualRecipe(recipe)) {
			this.cookTime++;
			energyStorage.modifyEnergyStored(-RFPerTick * 1);	
			                                            //energyMod
			
			if(this.cookTime == alloyingSpeed) {
				this.cookTime = 0;
				this.alloyItem(recipe);
				flag1 = true;
			}
		}else {
			cookTime = 0;
		}
		
		if(this.hasEnergy() &&(this.canAlloy(recipe) || this.isRedstone) && this.temperature <= maxTemperature) {
			this.temperature++;
			energyStorage.modifyEnergyStored(-RFPerTemp * 1);
		} else if(!(temperature <= 0)) {
				this.temperature--;
		}

		if (flag != this.hasEnergy() || flag2 != this.hasEnergy()) {
			flag1 = true;
			ThermoMaterialFurnace.updateBlockState(this.cookTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.temperature > 0);
		}

		if (flag1) {
			this.markDirty(); 
		}		
		if(ConfigFile.enableDamageFromTemp) {
			if(temperature > 300 && temperature < 1001)
				ThermoMaterialFurnace.updateTick(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 1F);
			else if(temperature > 1000)
				ThermoMaterialFurnace.updateTick(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 2F);
		}
	}

	@Override
	public int getInfoMaxEnergyPerTick() {
		return 500 * 1;
	}                                  //energyMod

	public int getEnergyStoredScaled(int i) {
		return getInfoEnergyStored() * i / getInfoMaxEnergyStored();
	}

	public int getAlloyProgressScaled(int i) {
		return this.cookTime * i / alloyingSpeed;
	}
	
	public int getTemperatureScaled(int i ) {
		return (this.temperature+2) * i / maxTemperature;
	}

	public void updateRedstone() {
		this.isRedstone = !this.isRedstone;
	}
}
