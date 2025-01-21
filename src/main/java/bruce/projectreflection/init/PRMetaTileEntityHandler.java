package bruce.projectreflection.init;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.PRLabs;
import bruce.projectreflection.metatileentity.MetaTileEntityHandCrank;
import bruce.projectreflection.metatileentity.MetaTileEntityHandCrankTool;
import bruce.projectreflection.metatileentity.multi.MetaTileEntitySuperconductorSmelter;
import bruce.projectreflection.metatileentity.multi.part.MteFluxHatch;
import bruce.projectreflection.misc.DynamicRegistryHandler;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import gregtech.api.GTValues;
import gregtech.api.GregTechAPI;
import gregtech.api.items.toolitem.ToolHelper;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SimpleMachineMetaTileEntity;
import gregtech.api.recipes.RecipeMaps;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.items.IScribeTools;
import thaumcraft.common.items.tools.ItemScribingTools;

import java.io.File;
import java.util.stream.IntStream;


public final class PRMetaTileEntityHandler {
    public static SimpleMachineMetaTileEntity[] CHEMICAL_DEHYDRATOR = null;
    public static MetaTileEntityHandCrank RESEARCH_TABLE = null;
    public static MetaTileEntity MANUAL_LATHE = null;
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
        CHEMICAL_DEHYDRATOR = IntStream.range(1, 9).mapToObj(i -> new SimpleMachineMetaTileEntity(
                new ResourceLocation(PRConstants.modid, "dehydrator." + GTValues.VN[i].toLowerCase()),
                PRRecipeMaps.DEHYDRATOR_RECIPES,
                Textures.FLUID_SOLIDIFIER_OVERLAY, i, true)).toArray(SimpleMachineMetaTileEntity[]::new);
        register(CHEMICAL_DEHYDRATOR);
        if (Loader.isModLoaded("thaumcraft")) {
            RESEARCH_TABLE = new MetaTileEntityHandCrankTool(new ResourceLocation(PRConstants.modid, "research_table"),
                    PRRecipeMaps.RESEARCH_TABLE_RECIPES,
                    tool -> tool.getItem() instanceof IScribeTools && tool.getItemDamage() < tool.getMaxDamage(),
                    tool -> tool.setItemDamage(tool.getItemDamage() + 1),
                    Textures.SCANNER_OVERLAY);
            register(RESEARCH_TABLE);
        }
        MANUAL_LATHE = new MetaTileEntityHandCrankTool(new ResourceLocation(PRConstants.modid, "manual_lathe"),
                RecipeMaps.LATHE_RECIPES,
                tool -> ToolHelper.isTool(tool, "file"),
                tool -> ToolHelper.damageItemWhenCrafting(tool, null),
                Textures.LATHE_OVERLAY);
        register(MANUAL_LATHE);
        register(MetaTileEntitySuperconductorSmelter.SAMPLE);

        FLUX_INPUT_HATCH = IntStream.range(0, 9).mapToObj(i -> new MteFluxHatch(new ResourceLocation(PRConstants.modid, "flux_input." + GTValues.VN[i].toLowerCase()),
                i, false)).toArray(MetaTileEntity[]::new);
        FLUX_OUTPUT_HATCH = IntStream.range(0, 9).mapToObj(i -> new MteFluxHatch(new ResourceLocation(PRConstants.modid, "flux_output." + GTValues.VN[i].toLowerCase()),
                i, true)).toArray(MetaTileEntity[]::new);
        register(FLUX_INPUT_HATCH);
        register(FLUX_OUTPUT_HATCH);
    }
}
