package bruce.projectreflection.init;

import bruce.projectreflection.materials.MaterialHelper;
import bruce.projectreflection.network.PRNetwork;
import gregtech.api.recipes.RecipeMaps;
import gregtech.common.items.MetaItems;
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
    }
}
