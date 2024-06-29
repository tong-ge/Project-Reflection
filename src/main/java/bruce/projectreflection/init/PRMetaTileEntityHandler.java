package bruce.projectreflection.init;

import bruce.projectreflection.PRLabs;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.metatileentities.MetaTileEntities;


public final class PRMetaTileEntityHandler {
    private static int id = 1;
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

    private static void register(MetaTileEntity... samples)
    {
        for (MetaTileEntity sample : samples) {
            int id = getAvailableMTEID();
            MetaTileEntities.registerMetaTileEntity(id, sample);
            PRLabs.logger.info("mte {} registered as {}", sample.metaTileEntityId, id);
        }
    }

    public static void registerAllMetaTileEntities()
    {
    }
}
