package bruce.projectreflection.materials;

import bruce.projectreflection.materials.properties.ArmorProperty;
import gregtech.api.GregTechAPI;
import gregtech.api.fluids.FluidBuilder;
import gregtech.api.fluids.store.FluidStorageKeys;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.FluidProperty;
import gregtech.api.unification.material.properties.PropertyKey;

public class MaterialMutations {
    private static void registerArmors() {
        Materials.Neutronium.setProperty(ArmorProperty.KEY, new ArmorProperty(1024, 65535, 5));
        Materials.Iron.setProperty(ArmorProperty.KEY, new ArmorProperty(new double[]{2.0, 5.0, 6.0, 2.0}, new int[]{195, 225, 240, 165}).setEnchantability(9));
        Materials.Gold.setProperty(ArmorProperty.KEY, new ArmorProperty(new double[]{1.0, 3.0, 5.0, 2.0}, new int[]{91, 105, 112, 77}).setEnchantability(25));
    }

    private static void registerOreFluids() {
        for (Material material : GregTechAPI.materialManager.getRegisteredMaterials()) {
            if (material.hasProperty(PropertyKey.ORE)) {
                FluidProperty prop = material.getProperty(PropertyKey.FLUID);
                if (prop == null) {
                    prop = new FluidProperty();
                    prop.setPrimaryKey(FluidStorageKeys.LIQUID);
                    material.setProperty(PropertyKey.FLUID, prop);
                }
                prop.enqueueRegistration(PRFluidStorageKeys.SLUDGE, new FluidBuilder());
                prop.enqueueRegistration(PRFluidStorageKeys.SOLUTION, new FluidBuilder());
            }
        }
    }

    public static void onOrePrefix() {
        registerArmors();
        registerOreFluids();
    }
}
