package bruce.projectreflection.materials;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import gregtech.api.GregTechAPI;
import gregtech.api.fluids.fluidType.FluidType;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.registry.MaterialRegistry;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

public class PRMaterials {
    public static Material PYROTHEUM;
    public static Material CRYOTHEUM;
    public static Material PETROTHEUM;
    public static Material AEROTHEUM;
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

        PYROTHEUM = dynamicBuilder("thermalfoundation", "pyrotheum")
                .dust()
                .fluid()
                .color(0xff9a3c)
                .fluidTemp(4000)
                .build();
        CRYOTHEUM = dynamicBuilder("thermalfoundation", "cryotheum")
                .dust()
                .fluid()
                .color(0x01f3f6)
                .fluidTemp(50)
                .build();
        PETROTHEUM = dynamicBuilder("thermalfoundation", "petrotheum")
                .dust()
                .fluid()
                .fluidTemp(350)
                .color(0x6b585b)
                .build();
        AEROTHEUM = dynamicBuilder("thermalfoundation", "aerotheum")
                .dust()
                .fluid(FluidType.getByName("gas"))
                .color(0xd6ff68)
                .build();
        orePrefix();
    }

    private static void orePrefix() {
        if (Loader.isModLoaded("thermalfoundation")) {
            OrePrefix.dust.setIgnored(PYROTHEUM);
            OrePrefix.dust.setIgnored(CRYOTHEUM);
            OrePrefix.dust.setIgnored(PETROTHEUM);
            OrePrefix.dust.setIgnored(AEROTHEUM);
        }
    }
}
