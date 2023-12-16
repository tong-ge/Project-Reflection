package bruce.projectreflection;

import bruce.projectreflection.init.PRMetaTileEntityHandler;
import bruce.projectreflection.recipes.ThaumcraftRecipes;
import gregtech.api.unification.material.Materials;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;


@Mod(modid = PRConstants.modid, dependencies = "required-after:gregtech;" +
        "required-after:ceramics;" +
        "required-after:gcym;" +
        "required-after:thaumcraft;" +
        "after:thermalfoundation;" +
        "after:abyssalcraft;" +
        "after:twilightforest;" +
        "after:draconicevolution;" +
        "after:gregtechfoodoption", useMetadata = true)
public class ProjectReflection {

    public static Logger logger;
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger=event.getModLog();
        PRMetaTileEntityHandler.registerAllMetaTileEntities();
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ThaumcraftRecipes.register();
    }

}
