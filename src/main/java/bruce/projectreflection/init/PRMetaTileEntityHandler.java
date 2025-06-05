package bruce.projectreflection.init;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.PRLabs;
import bruce.projectreflection.metatileentity.MteEnervationGenerator;
import bruce.projectreflection.metatileentity.MteHandCrank;
import bruce.projectreflection.metatileentity.multi.part.MteFluxHatch;
import bruce.projectreflection.lib.DynamicRegistryHandler;
import bruce.projectreflection.metatileentity.multi.steam.MteBiogasDigester;
import bruce.projectreflection.metatileentity.steam.RecipeMapBoiler;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import gregtech.api.GTValues;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SimpleMachineMetaTileEntity;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.stream.IntStream;


public final class PRMetaTileEntityHandler {
    public static RecipeMapBoiler SOLID_FUEL_BOILER = null;
    public static RecipeMapBoiler SOLID_FUEL_BOILER_HP = null;
    public static RecipeMapBoiler FLUID_FUEL_BOILER = null;
    public static RecipeMapBoiler FLUID_FUEL_BOILER_HP = null;
    public static MetaTileEntity[] FLUX_INPUT_HATCH = null;
    public static MetaTileEntity[] FLUX_OUTPUT_HATCH = null;
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
        SOLID_FUEL_BOILER = new RecipeMapBoiler(new ResourceLocation(PRConstants.modid, "solid_fuel_boiler"),
                false, Textures.COAL_BOILER_OVERLAY, PRRecipeMaps.SOLID_BOILER_FUELS, 120, 45, 1);
        SOLID_FUEL_BOILER_HP = new RecipeMapBoiler(new ResourceLocation(PRConstants.modid, "solid_fuel_boiler_hp"),
                true, Textures.COAL_BOILER_OVERLAY, PRRecipeMaps.SOLID_BOILER_FUELS, 300, 40, 1);
        FLUID_FUEL_BOILER = new RecipeMapBoiler(new ResourceLocation(PRConstants.modid, "fluid_fuel_boiler"),
                false, Textures.LAVA_BOILER_OVERLAY, PRRecipeMaps.FLUID_BOILER_FUELS, 240, 45, 1);
        FLUID_FUEL_BOILER_HP = new RecipeMapBoiler(new ResourceLocation(PRConstants.modid, "fluid_fuel_boiler_hp"),
                true, Textures.LAVA_BOILER_OVERLAY, PRRecipeMaps.FLUID_BOILER_FUELS, 600, 45, 1);

        FLUX_INPUT_HATCH = IntStream.range(0, 9).mapToObj(i -> new MteFluxHatch(new ResourceLocation(PRConstants.modid, "flux_input." + GTValues.VN[i].toLowerCase()),
                i, false)).toArray(MetaTileEntity[]::new);
        FLUX_OUTPUT_HATCH = IntStream.range(0, 9).mapToObj(i -> new MteFluxHatch(new ResourceLocation(PRConstants.modid, "flux_output." + GTValues.VN[i].toLowerCase()),
                i, true)).toArray(MetaTileEntity[]::new);

        register(FLUX_INPUT_HATCH);
        register(FLUX_OUTPUT_HATCH);
        register(MteBiogasDigester.SAMPLE);
        register(MteEnervationGenerator.SAMPLES);
        register(SOLID_FUEL_BOILER);
        register(SOLID_FUEL_BOILER_HP);
        register(FLUID_FUEL_BOILER);
        register(FLUID_FUEL_BOILER_HP);
    }
}
