package bruce.projectreflection;

import bruce.projectreflection.api.IGTMechCapability;
import bruce.projectreflection.api.IHeatCapability;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;

public class PRAbility {
    public static final MultiblockAbility<IGTMechCapability> INPUT_MECH = new MultiblockAbility<>("input_mech");
    public static final MultiblockAbility<IGTMechCapability> OUTPUT_MECH = new MultiblockAbility<>("output_mech");
    public static final MultiblockAbility<IHeatCapability> HEAT = new MultiblockAbility<>("heat");
}
