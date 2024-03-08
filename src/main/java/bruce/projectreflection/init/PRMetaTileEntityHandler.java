package bruce.projectreflection.init;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.metatileentity.magic.MetaTileEntityAuraCollector;
import bruce.projectreflection.metatileentity.multis.*;
import bruce.projectreflection.metatileentity.multis.multiblockpart.MetaTileEntityFluxMuffler;
import bruce.projectreflection.metatileentity.multis.multiblockpart.MetaTileEntityMysticalMechanicsHatch;
import bruce.projectreflection.metatileentity.multis.multiblockpart.MetaTileEntitySteamMechanicsHatch;
import bruce.projectreflection.metatileentity.multis.mysticalmechanics.MetaTileEntityMechanicalBender;
import bruce.projectreflection.metatileentity.multis.mysticalmechanics.MetaTileEntityMechanicalCompressor;
import bruce.projectreflection.metatileentity.multis.mysticalmechanics.generators.MetaTileEntityMechanicalCombustionEngine;
import bruce.projectreflection.metatileentity.multis.mysticalmechanics.generators.MetaTileEntityMechanicalGasTurbine;
import bruce.projectreflection.metatileentity.multis.mysticalmechanics.generators.MetaTileEntityMechanicalSteamTurbine;
import bruce.projectreflection.metatileentity.multis.mysticalmechanics.MetaTileEntityMechanicalWiremill;
import gregtech.api.GTValues;
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
import net.minecraftforge.fml.common.Loader;


public final class PRMetaTileEntityHandler {
    private static final SimpleGeneratorMetaTileEntity[] SEMIFLUID_GENERATORS = new SimpleGeneratorMetaTileEntity[3];
    public static final SimpleMachineMetaTileEntity[] MASS_FABRICATORS = new SimpleMachineMetaTileEntity[3];
    public static final TieredMetaTileEntity[] AURA_COLLECTORS = new TieredMetaTileEntity[3];
    public static final MetaTileEntityMultiblockPart[] FLUX_MUFFLERS = new MetaTileEntityMultiblockPart[9];
    public static final MetaTileEntityMultiblockPart[] MECH_EXPORT_HATCHES = new MetaTileEntityMultiblockPart[9];
    public static final MetaTileEntityMultiblockPart[] MECH_IMPORT_HATCHES = new MetaTileEntityMultiblockPart[9];

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
            AURA_COLLECTORS[i] = new MetaTileEntityAuraCollector(i + 1);
            MASS_FABRICATORS[i] = new SimpleMachineMetaTileEntity(new ResourceLocation(PRConstants.modid,
                    String.format("mass_fabricator.%s", PRConstants.V[i + 3])),
                    RecipeMaps.MASS_FABRICATOR_RECIPES,
                    Textures.MULTIBLOCK_WORKABLE_OVERLAY, i + 3, true);

            register(SEMIFLUID_GENERATORS[i]);
            register(AURA_COLLECTORS[i]);
            register(MASS_FABRICATORS[i]);

        }
        for (int i = 0; i < 9; i++) {
            if (!Loader.isModLoaded("pollution")) {
                FLUX_MUFFLERS[i] = new MetaTileEntityFluxMuffler(
                        new ResourceLocation(
                                PRConstants.modid,
                                String.format("muffler.%s", GTValues.VN[i])),
                        i);
                register(FLUX_MUFFLERS[i]);
            }
            MECH_EXPORT_HATCHES[i] = new MetaTileEntityMysticalMechanicsHatch(
                    new ResourceLocation(PRConstants.modid,
                            String.format("mech_export_hatch.%s", GTValues.VN[i])),
                    i,
                    true
            );
            MECH_IMPORT_HATCHES[i] = new MetaTileEntityMysticalMechanicsHatch(
                    new ResourceLocation(PRConstants.modid,
                            String.format("mech_import_hatch.%s", GTValues.VN[i])),
                    i,
                    false
            );
            register(MECH_EXPORT_HATCHES[i]);
            register(MECH_IMPORT_HATCHES[i]);
        }
        //register(new MetaTileEntityLargeSteamCompressor(new ResourceLocation(PRConstants.modid, "large_steam_compressor")));
        register(new MetaTileEntityCeramicOven(new ResourceLocation(PRConstants.modid, "ceramic_oven")));
        //register(new MetaTileEntitySteamAssline(new ResourceLocation(PRConstants.modid, "steam_assline")));
        register(new MetaTileEntityMechanicalSteamTurbine(new ResourceLocation(PRConstants.modid, "mechanical_steam_turbine")));
        register(new MetaTileEntityMechanicalGasTurbine(new ResourceLocation(PRConstants.modid, "mechanical_gas_turbine")));
        register(new MetaTileEntityIndustrialMobFarm(new ResourceLocation(PRConstants.modid, "industrial_mob_farm")));
        register(new MetaTileEntityMechanicalWiremill(new ResourceLocation(PRConstants.modid, "mechanical_wiremill")));
        register(new MetaTileEntityMechanicalBender(new ResourceLocation(PRConstants.modid, "mechanical_bender")));
        register(new MetaTileEntityMechanicalCombustionEngine(new ResourceLocation(PRConstants.modid, "mechanical_combustion_engine")));
        register(new MetaTileEntityMechanicalCompressor(new ResourceLocation(PRConstants.modid, "mechanical_compressor")));
        register(new MetaTileEntitySteamMechanicsHatch(new ResourceLocation(PRConstants.modid, "steam_mechanical_hatch")));
        register(new MetaTileEntityMagicalGenerator(new ResourceLocation(PRConstants.modid, "magical_generator")));
    }
}
