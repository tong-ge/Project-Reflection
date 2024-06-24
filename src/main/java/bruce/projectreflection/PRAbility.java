package bruce.projectreflection;

import bruce.projectreflection.api.IGTMechCapability;
import bruce.projectreflection.api.IHeatCapability;
import gregtech.api.capability.IRotorHolder;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;

public class PRAbility {
    public static final MultiblockAbility<IGTMechCapability> INPUT_MECH = new MultiblockAbility<>("input_mech");
    public static final MultiblockAbility<IGTMechCapability> OUTPUT_MECH = new MultiblockAbility<>("output_mech");
    public static final MultiblockAbility<IHeatCapability> HEAT = new MultiblockAbility<>("heat");
    public static final MultiblockAbility<IRotorHolder> LL_ROTOR_HOLDER = new MultiblockAbility<>("rotor_holder");
}
