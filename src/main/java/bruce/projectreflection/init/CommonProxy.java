package bruce.projectreflection.init;

import bruce.projectreflection.blocks.BlockSteam;
import bruce.projectreflection.materials.MaterialHelper;
import bruce.projectreflection.network.PRNetwork;
import gregtech.api.fluids.GTFluidRegistration;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Materials;
import gregtech.common.items.MetaItems;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    private static void modifyRecipeMaps() {
        RecipeMaps.CENTRIFUGE_RECIPES.setMaxFluidInputs(2);
        RecipeMaps.MIXER_RECIPES.setMaxInputs(9);
        RecipeMaps.MIXER_RECIPES.setMaxOutputs(2);
    }
    public void preInit(FMLPreInitializationEvent event) {
        PRNetwork.registerPackets();
        modifyRecipeMaps();
        PRMetaTileEntityHandler.registerAllMetaTileEntities();
        PRMetaTileEntityHandler.writeRegistryToFile();
        Fluid fluid = Materials.Steam.getFluid();
        if (fluid != null && fluid.getBlock() == null) {
            GTFluidRegistration.INSTANCE.registerFluidBlock(BlockSteam.INSTANCE);
            fluid.setBlock(BlockSteam.INSTANCE);
        }
    }
}
