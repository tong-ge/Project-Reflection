package bruce.projectreflection.metatileentity;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.PRRecipeMaps;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SimpleGeneratorMetaTileEntity;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockTurbineCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.common.metatileentities.multi.electric.generator.MetaTileEntityLargeTurbine;
import net.minecraft.util.ResourceLocation;


public final class PRMetaTileEntityHandler {
    private static final SimpleGeneratorMetaTileEntity[] HYDRAULIC_GENERATORS=new SimpleGeneratorMetaTileEntity[3];
    private static int getAvailableMTEID()
    {
        for (int id=1;id<=32767;id++)
        {
            MetaTileEntity mte= GregTechAPI.MTE_REGISTRY.getObjectById(id);
            if(mte==null)
            {
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
            HYDRAULIC_GENERATORS[i]=new SimpleGeneratorMetaTileEntity(
                    new ResourceLocation(PRConstants.modid,
                            String.format("hydraulic.%s", PRConstants.V[i+1])
                    ),
                    PRRecipeMaps.HYDRAULIC_GENERATOR,
                    Textures.STEAM_TURBINE_OVERLAY,
                    i+1,
                    tier->16000 * (1 << tier - 1));
            register(HYDRAULIC_GENERATORS[i]);
        }
        register(new MetaTileEntityLargeTurbine(
                new ResourceLocation(PRConstants.modid,"large_turbine.hydraulic"),
                PRRecipeMaps.HYDRAULIC_GENERATOR,
                3,
                MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.STEEL_TURBINE_CASING),
                MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.STEEL_GEARBOX),
                Textures.SOLID_STEEL_CASING,
                false,
                Textures.LARGE_STEAM_TURBINE_OVERLAY)
        );
    }
}
