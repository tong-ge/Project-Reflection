package bruce.projectreflection.materials;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraftforge.fml.common.Loader;

public class ThaumcraftMaterials {
    public static Material THAUMIUM;
    public static Material VOID_METAL;

    public static void init() {
        THAUMIUM = PRMaterials.dynamicBuilder("thaumium")
                .ingot(3)
                .fluid()
                .color(0x51437c)
                .flags(
                        MaterialFlags.DISABLE_DECOMPOSITION,
                        MaterialFlags.GENERATE_PLATE,
                        MaterialFlags.GENERATE_ROD,
                        MaterialFlags.GENERATE_FRAME,
                        MaterialFlags.GENERATE_GEAR,
                        MaterialFlags.GENERATE_DENSE
                )
                .components(new MaterialStack(Materials.Iron, 1), new MaterialStack(ThermalMaterials.MANA, 1))
                .build();
        VOID_METAL = PRMaterials.dynamicBuilder("void")
                .ingot(4)
                .fluid()
                .color(0x63186f)
                .flags(
                        MaterialFlags.DISABLE_DECOMPOSITION,
                        MaterialFlags.GENERATE_PLATE,
                        MaterialFlags.GENERATE_ROD,
                        MaterialFlags.GENERATE_FRAME,
                        MaterialFlags.GENERATE_GEAR,
                        MaterialFlags.GENERATE_DENSE
                )
                .build();
    }

    public static void orePrefix() {
        if (Loader.isModLoaded("thaumcraft")) {
            OrePrefix.ingot.setIgnored(THAUMIUM);
            OrePrefix.ingot.setIgnored(VOID_METAL);
            OrePrefix.ingot.setIgnored(Materials.Brass);
        }
    }
}
