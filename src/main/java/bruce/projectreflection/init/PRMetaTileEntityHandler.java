package bruce.projectreflection.init;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.metatileentity.MetaTileEntityFission;
import bruce.projectreflection.metatileentity.MetaTileEntityVisGenerator;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.util.ResourceLocation;


public final class PRMetaTileEntityHandler {
    //private static final SimpleGeneratorMetaTileEntity[] HYDRAULIC_GENERATORS=new SimpleGeneratorMetaTileEntity[3];
    public static final MetaTileEntity[] AURA_GENERATORS=new MetaTileEntity[3];
    private static int currentMTEID=1;
    private static int getAvailableMTEID()
    {
        for (int id=currentMTEID;id<=32767;id++)
        {
            MetaTileEntity mte= GregTechAPI.MTE_REGISTRY.getObjectById(id);
            if(mte==null)
            {
                currentMTEID=id;
                return id;
            }
        }
        return -1;
    }

    private static void register(MetaTileEntity sample)
    {
        int id=getAvailableMTEID();
        if(id<0)
        {
            throw new ArrayIndexOutOfBoundsException("no mte id available");
        }
        MetaTileEntities.registerMetaTileEntity(id,sample);
        ProjectReflection.logger.info("mte {} registered as {}",sample.metaTileEntityId,id);
    }
    public static void main(String[] args)
    {
        for (int i=0;i<3;i++)
        {
            AURA_GENERATORS[i]=new MetaTileEntityVisGenerator(new ResourceLocation(PRConstants.modid,
                    String.format("vis.%s", PRConstants.V[i+1])
            ),i+1);

            //register(HYDRAULIC_GENERATORS[i]);
            register(AURA_GENERATORS[i]);
        }
        register(new MetaTileEntityFission());
    }
}
