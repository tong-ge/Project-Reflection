package bruce.projectreflection.materials;

import bruce.projectreflection.PRConstants;
import gregtech.api.GregTechAPI;
import gregtech.api.fluids.FluidBuilder;
import gregtech.api.fluids.FluidState;
import gregtech.api.fluids.store.FluidStorageKeys;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.registry.MaterialRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nullable;

public class MaterialHelper {

    private static int id = 1;

    private static int getNextAvailableId(String modid) {
        MaterialRegistry registry = GregTechAPI.materialManager.getRegistry(modid);
        while (id < 32000) {
            Material material = registry.getObjectById(id);
            if (material == null) {
                return id;
            }
            id++;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public static Material.Builder dynamicBuilder(String modid, String name, boolean autoFluid, @Nullable FluidBuilder fallback, boolean gaseous) {

        Material.Builder builder = new Material.Builder(getNextAvailableId(modid), new ResourceLocation(modid, name));
        Fluid fluid = FluidRegistry.getFluid(name);
        if (autoFluid) {
            if (fluid != null) {
                builder.fluid(fluid,
                        fluid.isGaseous() ? FluidStorageKeys.GAS : FluidStorageKeys.LIQUID,
                        fluid.isGaseous() ? FluidState.GAS : FluidState.LIQUID);
            } else if (fallback != null) {
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
        FirstTierMaterials.init();
        SecondTierMaterials.init();
        ThirdTierMaterials.init();
        GTMaterials.init();
        orePrefix();
    }

    private static void orePrefix() {
        FirstTierMaterials.orePrefix();
        SecondTierMaterials.orePrefix();
        ThirdTierMaterials.orePrefix();
    }
}
