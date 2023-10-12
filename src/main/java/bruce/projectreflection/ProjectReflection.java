package bruce.projectreflection;

import bruce.projectreflection.init.PRMetaTileEntityHandler;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.materials.ElementMaterials;
import gregtech.api.unification.material.properties.IngotProperty;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;


@Mod(modid = PRConstants.modid,dependencies = "required-after:gregtech",useMetadata = true)
public class ProjectReflection {

    public static Logger logger;
    @Mod.EventHandler
    public void onLoad(FMLPreInitializationEvent event)
    {
        logger=event.getModLog();
        PRMetaTileEntityHandler.main(new String[0]);
    }
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event)
    {
        PRRecipeMaps.MODULAR_FISSION.recipeBuilder()
                .input(OrePrefix.stick,Materials.Uranium235)
                .output(OrePrefix.dustSmall,Materials.Barium,2)
                .fluidOutputs(Materials.Krypton.getFluid(500))
                .EUt(50)
                .duration(200000)
                .buildAndRegister();
    }

}
