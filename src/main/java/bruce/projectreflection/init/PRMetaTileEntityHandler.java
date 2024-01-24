package bruce.projectreflection.init;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.metatileentity.magic.MetaTileEntityAuraCollector;
import bruce.projectreflection.metatileentity.multis.MetaTileEntityCeramicOven;
import bruce.projectreflection.metatileentity.multis.MetaTileEntityLargeSteamCompressor;
import bruce.projectreflection.metatileentity.multis.MetaTileEntitySteamAssline;
import bruce.projectreflection.metatileentity.multis.multiblockpart.MetaTileEntityFluxMuffler;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SimpleGeneratorMetaTileEntity;
import gregtech.api.metatileentity.SimpleMachineMetaTileEntity;
import gregtech.api.metatileentity.TieredMetaTileEntity;
import gregtech.api.recipes.RecipeMaps;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.common.metatileentities.electric.MetaTileEntitySingleCombustion;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;
import net.minecraft.util.ResourceLocation;


public final class PRMetaTileEntityHandler {
    private static final SimpleGeneratorMetaTileEntity[] SEMIFLUID_GENERATORS = new SimpleGeneratorMetaTileEntity[3];
    public static final SimpleMachineMetaTileEntity[] MASS_FABRICATORS = new SimpleMachineMetaTileEntity[3];
    public static final TieredMetaTileEntity[] AURA_COLLECTORS = new TieredMetaTileEntity[3];
    public static final MetaTileEntityMultiblockPart[] FLUX_MUFFLERS = new MetaTileEntityMultiblockPart[9];

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

    private static void register(MetaTileEntity sample)
    {
        int id=getAvailableMTEID();
        MetaTileEntities.registerMetaTileEntity(id,sample);
        ProjectReflection.logger.info("mte {} registered as {}",sample.metaTileEntityId,id);
    }

    public static void registerAllMetaTileEntities()
    {
        for (int i=0;i<3;i++)
        {
            SEMIFLUID_GENERATORS[i] = new MetaTileEntitySingleCombustion(
                    new ResourceLocation(PRConstants.modid,
                            String.format("semifluid.%s", PRConstants.V[i + 1])
                    ),
                    RecipeMaps.SEMI_FLUID_GENERATOR_FUELS,
                    Textures.COMBUSTION_GENERATOR_OVERLAY,
                    i+1,
                    tier->16000 * (1 << tier - 1));
            AURA_COLLECTORS[i] = /*new MetaTileEntityVisGenerator(new ResourceLocation(PRConstants.modid,
                    String.format("vis.%s", PRConstants.V[i+1])
            ),i+1);*/
                    new MetaTileEntityAuraCollector(i + 1);
            MASS_FABRICATORS[i] = new SimpleMachineMetaTileEntity(new ResourceLocation(PRConstants.modid,
                    String.format("mass_fabricator.%s", PRConstants.V[i + 3])),
                    RecipeMaps.MASS_FABRICATOR_RECIPES,
                    Textures.MULTIBLOCK_WORKABLE_OVERLAY, i + 3, true);
            //NETHER_STAR_GENERATORS[i]=new MetaTileEntityNetherStarGenerator(i);

            register(SEMIFLUID_GENERATORS[i]);
            register(AURA_COLLECTORS[i]);
            register(MASS_FABRICATORS[i]);
            //register(NETHER_STAR_GENERATORS[i]);

        }
        for (int i = 0; i < 9; i++) {
            FLUX_MUFFLERS[i] = new MetaTileEntityFluxMuffler(
                    new ResourceLocation(
                            PRConstants.modid,
                            String.format("muffler.%s", PRConstants.V[i + 1])),
                    i);
            register(FLUX_MUFFLERS[i]);

        }
        register(new MetaTileEntityLargeSteamCompressor(new ResourceLocation(PRConstants.modid, "large_steam_compressor")));
        register(new MetaTileEntityCeramicOven(new ResourceLocation(PRConstants.modid, "ceramic_oven")));
        register(new MetaTileEntitySteamAssline(new ResourceLocation(PRConstants.modid, "steam_assline")));
    }
}
