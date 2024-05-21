package bruce.projectreflection;

import bruce.projectreflection.init.CommonProxy;
import bruce.projectreflection.init.PRMetaTileEntityHandler;
import bruce.projectreflection.recipes.ThaumcraftRecipes;
import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.Materials;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;


@Mod(modid = PRConstants.modid, dependencies = "required-after:gregtech;" +
        "required-after:ceramics;" +
        "required-after:gcym;" +
        "required-after:thaumcraft;" +
        "required-after:extrabotany;" +
        "required-after:mysticalmechanics;" +
        "after:thermalfoundation;" +
        "after:abyssalcraft;" +
        "after:twilightforest;" +
        "after:draconicevolution;" +
        "after:gregtechfoodoption;" +
        "before:thaumicperiphery;" +
        "required-before:pollution", useMetadata = true)
public class ProjectReflection {
    public static SoundEvent UPDATE_SUPPRESSOR;

    public static Logger logger;
    @SidedProxy(modId = PRConstants.modid, clientSide = "bruce.projectreflection.client.ClientProxy", serverSide = "bruce.projectreflection.init.CommonProxy")
    public static CommonProxy proxy;
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger=event.getModLog();
        proxy.preInit(event);
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        UPDATE_SUPPRESSOR = GregTechAPI.soundManager.registerSound(PRConstants.modid, "jumper");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ThaumcraftRecipes.register();
    }

}
