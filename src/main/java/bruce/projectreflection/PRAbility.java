package bruce.projectreflection;

import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import net.minecraftforge.energy.IEnergyStorage;

public class PRAbility {
    public static final MultiblockAbility<IEnergyStorage> INPUT_RF = new MultiblockAbility<>("input_rf");
    public static final MultiblockAbility<IEnergyStorage> OUTPUT_RF = new MultiblockAbility<>("output_rf");
}
