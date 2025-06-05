package bruce.projectreflection.materials;

import bruce.projectreflection.PRConfig;
import bruce.projectreflection.PRConstants;
import bruce.projectreflection.lib.DynamicRegistryHandler;
import gregtech.api.GregTechAPI;
import gregtech.api.fluids.FluidBuilder;
import gregtech.api.fluids.FluidState;
import gregtech.api.fluids.store.FluidStorageKeys;
import gregtech.api.unification.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.io.File;


public class MaterialHelper {
    //private static HashMap<String, Integer> idMap;
    private static DynamicRegistryHandler<String, Material> registryHandler;

    private static int retrieveIdForName(ResourceLocation name) {
        if (!PRConstants.modid.equals(name.getNamespace())) {
            throw new IllegalArgumentException("only supports projectreflection materials");
        }
        return registryHandler.retrieveIdForName(name.toString());
    }

    public static Material.Builder dynamicBuilder(String modid, String name, boolean autoFluid, @Nullable FluidBuilder fallback, boolean gaseous) {
        return dynamicBuilder(new ResourceLocation(modid, name), autoFluid, fallback, gaseous);
    }

    public static Material.Builder dynamicBuilder(ResourceLocation name, boolean autoFluid, @Nullable FluidBuilder fallback, boolean gaseous) {
        Material.Builder builder = new Material.Builder(retrieveIdForName(name), name);
        Fluid fluid = FluidRegistry.getFluid(name.getPath());
        if (autoFluid) {
            if (fluid != null) {
                System.out.println("Registering material for existing fluid:" + name);
                builder.fluid(fluid,
                        fluid.isGaseous() ? FluidStorageKeys.GAS : FluidStorageKeys.LIQUID,
                        fluid.isGaseous() ? FluidState.GAS : FluidState.LIQUID);
            } else if (fallback != null) {
                System.out.println("Registering material with new fluid:" + name);
                builder.fluid(gaseous ? FluidStorageKeys.GAS : FluidStorageKeys.LIQUID, fallback);
            }
        }
        return builder;
    }

    public static Material.Builder dynamicBuilder(String name, boolean autoFluid, int temperature, boolean gaseous) {
        return dynamicBuilder(PRConstants.modid, name, autoFluid,
                new FluidBuilder().temperature(temperature).state(gaseous ? FluidState.GAS : FluidState.LIQUID),
                gaseous);
    }

    public static Material.Builder dynamicBuilder(String name, boolean autoFluid, boolean gaseous) {
        return dynamicBuilder(PRConstants.modid, name, autoFluid,
                new FluidBuilder().state(gaseous ? FluidState.GAS : FluidState.LIQUID),
                gaseous);
    }
    public static Material.Builder dynamicBuilder(String name) {
        return dynamicBuilder(name, false, false);
    }

    public static void init() {
        if (PRConfig.debug) {
            System.out.println("Registering materials");
        }
        //ModFluid.init();
        initIDMap();
    }


    public static void orePrefix() {
        registryHandler.writeRegistryToFile();
        MaterialMutations.onOrePrefix();
        //Materials.HSSE.setProperty(PropertyArmor.KEY,new PropertyArmor(20,3072,3).setEnchantability(20));
    }

    public static void initIDMap() {
        registryHandler = new DynamicRegistryHandler<>(GregTechAPI.materialManager.getRegistry(PRConstants.modid),
                22050, 32000,
                new File(Loader.instance().getConfigDir(), "projectreflection_material_id_map.json"));
    }
}
