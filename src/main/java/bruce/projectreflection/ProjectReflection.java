package bruce.projectreflection;

import bruce.projectreflection.metatileentity.PRMetaTileEntityHandler;
import gregtech.api.unification.material.Materials;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;


@Mod(modid = PRConstants.modid,dependencies = "required-after:gregtech",useMetadata = true)
public class ProjectReflection {

    public static Logger logger;
    @Mod.EventHandler
    public void main(FMLPreInitializationEvent event)
    {
        logger=event.getModLog();
        PRMetaTileEntityHandler.main(new String[0]);
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        PRRecipeMaps.HYDRAULIC_GENERATOR.recipeBuilder()
                .fluidInputs(Materials.Water.getFluid(5000))
                .EUt(32)
                .duration(10)
                .buildAndRegister();
    }

}
