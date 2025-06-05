package bruce.projectreflection;

import bruce.projectreflection.capability.energy.IExtendedEnergyStorage;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import net.minecraftforge.energy.IEnergyStorage;

public class PRAbility {
    public static final MultiblockAbility<IExtendedEnergyStorage> INPUT_RF = new MultiblockAbility<>("input_rf");
    public static final MultiblockAbility<IExtendedEnergyStorage> OUTPUT_RF = new MultiblockAbility<>("output_rf");
}
