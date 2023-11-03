package bruce.projectreflection;

import bruce.projectreflection.init.PRMetaTileEntityHandler;
import gregtech.api.unification.material.Materials;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;


@Mod(modid = PRConstants.modid,dependencies = "required-after:gregtech",useMetadata = true)
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

}
