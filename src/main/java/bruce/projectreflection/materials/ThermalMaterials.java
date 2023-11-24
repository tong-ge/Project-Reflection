package bruce.projectreflection.materials;

import gregtech.api.fluids.fluidType.FluidType;
import gregtech.api.unification.Elements;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.materials.ElementMaterials;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraftforge.fml.common.Loader;

public class ThermalMaterials {
    public static Material PYROTHEUM;
    public static Material CRYOTHEUM;
    public static Material PETROTHEUM;
    public static Material AEROTHEUM;
    public static Material MANA;

    public static void init() {

        PYROTHEUM = PRMaterials.dynamicBuilder("pyrotheum")
                .dust()
                .fluid()
                .color(0xff9a3c)
                .fluidTemp(4000)
                .build();
        CRYOTHEUM = PRMaterials.dynamicBuilder("cryotheum")
                .dust()
                .fluid()
                .color(0x01f3f6)
                .fluidTemp(50)
                .build();
        PETROTHEUM = PRMaterials.dynamicBuilder("petrotheum")
                .dust()
                .fluid()
                .fluidTemp(350)
                .color(0x6b585b)
                .build();
        AEROTHEUM = PRMaterials.dynamicBuilder("aerotheum")
                .dust()
                .fluid(FluidType.getByName("gas"))
                .color(0xd6ff68)
                .build();
        MANA = PRMaterials.dynamicBuilder("mana")
                .dust()
                .fluid()
                .color(0x5e48ff)
                .element(Elements.Ma)
                .build();
    }

    public static void orePrefix() {
        if (Loader.isModLoaded("thermalfoundation")) {
            OrePrefix.dust.setIgnored(PYROTHEUM);
            OrePrefix.dust.setIgnored(CRYOTHEUM);
            OrePrefix.dust.setIgnored(PETROTHEUM);
            OrePrefix.dust.setIgnored(AEROTHEUM);
            OrePrefix.dust.setIgnored(MANA);
        }
    }
}
