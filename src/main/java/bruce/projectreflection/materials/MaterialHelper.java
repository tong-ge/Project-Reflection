package bruce.projectreflection.materials;

import bruce.projectreflection.PRConstants;
import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.registry.MaterialRegistry;
import net.minecraft.util.ResourceLocation;

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

    public static Material.Builder dynamicBuilder(String modid, String name) {
        return new Material.Builder(getNextAvailableId(modid), new ResourceLocation(modid, name));
    }

    public static Material.Builder dynamicBuilder(String name) {
        return dynamicBuilder(PRConstants.modid, name);
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
