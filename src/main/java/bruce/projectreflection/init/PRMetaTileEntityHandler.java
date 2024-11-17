package bruce.projectreflection.init;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.PRLabs;
import bruce.projectreflection.misc.DynamicRegistryHandler;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import gregtech.api.GTValues;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SimpleMachineMetaTileEntity;
import gregtech.api.recipes.RecipeMaps;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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
        register(IntStream.range(1, 9).mapToObj(i -> new SimpleMachineMetaTileEntity(
                new ResourceLocation(PRConstants.modid, "dehydrator." + GTValues.VN[i].toLowerCase()),
                PRRecipeMaps.DEHYDRATOR_RECIPES,
                Textures.FLUID_SOLIDIFIER_OVERLAY, i, true)).toArray(SimpleMachineMetaTileEntity[]::new));
    }
}
