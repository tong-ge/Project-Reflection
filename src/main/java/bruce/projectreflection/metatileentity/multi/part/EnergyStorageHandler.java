package bruce.projectreflection.metatileentity.multi.part;

import bruce.projectreflection.capability.energy.IExtendedEnergyStorage;
import gregtech.api.metatileentity.MTETrait;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public class EnergyStorageHandler extends MTETrait implements IExtendedEnergyStorage {
    protected long energy;
    protected long capacity;
    private final int maxReceive;
    private final int maxExtract;

    public EnergyStorageHandler(@NotNull MetaTileEntity metaTileEntity, long capacity, int maxReceive, int maxExtract) {
        super(metaTileEntity);
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    @Override
    public @NotNull String getName() {
        return "EnergyStorage";
    }

    @Override
    public <T> T getCapability(Capability<T> capability) {
        return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(this) : null;
    }

    @Override
    public @NotNull NBTTagCompound serializeNBT() {
        NBTTagCompound compound = super.serializeNBT();
        compound.setLong("EnergyStored", this.energy);
        return compound;
    }

    @Override
    public void deserializeNBT(@NotNull NBTTagCompound compound) {
        super.deserializeNBT(compound);
        this.energy = compound.getLong("EnergyStored");
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;
        return (int) receiveEnergyInternal(Math.min(this.maxReceive, maxReceive), simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        return (int) extractEnergyInternal(Math.min(this.maxExtract, maxExtract), simulate);
    }

    @Override
    public int getEnergyStored() {
        return energy > (long) Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity > (long) Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }

    @Override
    public long receiveEnergyInternal(long maxReceive, boolean simulate) {
        long energyReceived = Math.min(capacity - energy, maxReceive);
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    @Override
    public long extractEnergyInternal(long maxExtract, boolean simulate) {
        long energyExtracted = Math.min(energy, maxExtract);
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public long getEnergyStoredLong() {
        return energy;
    }

    @Override
    public long getMaxEnergyStoredLong() {
        return capacity;
    }
}
