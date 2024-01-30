package bruce.projectreflection;

import bruce.projectreflection.api.IGTMechCapability;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;

public class PRAbility {
    public static MultiblockAbility<IGTMechCapability> INPUT_MECH = new MultiblockAbility<>("input_mech");
    public static MultiblockAbility<IGTMechCapability> OUTPUT_MECH = new MultiblockAbility<>("output_mech");
}
