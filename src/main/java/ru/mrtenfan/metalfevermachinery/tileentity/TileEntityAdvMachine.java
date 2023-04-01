package ru.mrtenfan.metalfevermachinery.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.tileentity.IEnergyInfo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import ru.mrtenfan.metalfevermachinery.ConfigFile;

public class TileEntityAdvMachine extends TileEntity implements IEnergyInfo, IEnergyReceiver {

	protected EnergyStorage energyStorage = new EnergyStorage(ConfigFile.AMSettings[0], ConfigFile.AMSettings[1]);

	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);

		energyStorage.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);

		energyStorage.writeToNBT(nbt);
	}
	
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return energyStorage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return energyStorage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return energyStorage.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return energyStorage.getMaxEnergyStored() > 0;
	}
	
	@Override
	public int getInfoEnergyPerTick() {
		return 0;
	}

	@Override
	public int getInfoMaxEnergyPerTick() {
		return 0;
	}

	@Override
	public int getInfoEnergyStored() {
		return energyStorage.getEnergyStored();
	}

	@Override
	public int getInfoMaxEnergyStored() {
		return energyStorage.getMaxEnergyStored();
	}
	
	public EnergyStorage getEnergyStorage() {
		return energyStorage;
	}
}
