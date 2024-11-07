package bruce.projectreflection.init;

import bruce.projectreflection.PRLabs;
import bruce.projectreflection.misc.DynamicRegistryHandler;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.io.File;


public final class PRMetaTileEntityHandler {
    //private static int id = 1;
    /*
    private static int getAvailableMTEID()
    {
        while (id < 32000)
        {
            MetaTileEntity mte= GregTechAPI.MTE_REGISTRY.getObjectById(id);
            if(mte==null)
            {
                return id;
            }
            id++;
        }
        throw new ArrayIndexOutOfBoundsException("no mte id available");
    }

     */
    private static final DynamicRegistryHandler<ResourceLocation, MetaTileEntity> registryHandler = new DynamicRegistryHandler<>(
            GregTechAPI.MTE_REGISTRY, 11000, 32000,
            new File(Loader.instance().getConfigDir(), "projectreflection_mte_id_map.json"));
    private static void register(MetaTileEntity... samples)
    {

        for (MetaTileEntity sample : samples) {
            //int id = getAvailableMTEID();
            int id = registryHandler.retrieveIdForName(sample.metaTileEntityId);
            MetaTileEntities.registerMetaTileEntity(id, sample);
            PRLabs.logger.info("mte {} registered as {}", sample.metaTileEntityId, id);
        }
    }

    public static void writeRegistryToFile() {
        registryHandler.writeRegistryToFile();
    }
    public static void registerAllMetaTileEntities()
    {

    }
}
