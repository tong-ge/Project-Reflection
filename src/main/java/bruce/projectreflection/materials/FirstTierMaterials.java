package bruce.projectreflection.materials;

import gregtech.api.fluids.fluidType.FluidType;
import gregtech.api.unification.Elements;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraftforge.fml.common.Loader;

public class FirstTierMaterials {
    public static Material PYROTHEUM;
    public static Material CRYOTHEUM;
    public static Material PETROTHEUM;
    public static Material AEROTHEUM;
    public static Material MANA;
    public static Material VOID_METAL;
    public static Material ALLOY_ADVANCED;

    public static void init() {

        PYROTHEUM = MaterialHelper.dynamicBuilder("pyrotheum")
                .dust()
                .fluid()
                .color(0xff9a3c)
                .fluidTemp(4000)
                .build();
        CRYOTHEUM = MaterialHelper.dynamicBuilder("cryotheum")
                .dust()
                .fluid()
                .color(0x01f3f6)
                .fluidTemp(50)
                .build();
        PETROTHEUM = MaterialHelper.dynamicBuilder("petrotheum")
                .dust()
                .fluid()
                .fluidTemp(350)
                .color(0x6b585b)
                .build();
        AEROTHEUM = MaterialHelper.dynamicBuilder("aerotheum")
                .dust()
                .fluid(FluidType.getByName("gas"))
                .color(0xd6ff68)
                .build();
        MANA = MaterialHelper.dynamicBuilder("mana")
                .dust()
                .fluid()
                .color(0x5e48ff)
                .element(Elements.Ma)
                .build();
        VOID_METAL = MaterialHelper.dynamicBuilder("void")
                .ingot(4)
                .fluid()
                .color(0x63186f)
                .flags(
                        MaterialFlags.DISABLE_DECOMPOSITION,
                        MaterialFlags.GENERATE_PLATE,
                        MaterialFlags.GENERATE_ROD,
                        MaterialFlags.GENERATE_FRAME,
                        MaterialFlags.GENERATE_GEAR,
                        MaterialFlags.GENERATE_DOUBLE_PLATE
                )
                .build();
        ALLOY_ADVANCED = MaterialHelper.dynamicBuilder("alloy_advanced")
                .ingot()
                .components(
                        new MaterialStack(Materials.WroughtIron, 1),
                        new MaterialStack(Materials.Bronze, 1),
                        new MaterialStack(Materials.Tin, 1)
                )
                .flags(
                        MaterialFlags.NO_SMELTING,
                        MaterialFlags.DISABLE_DECOMPOSITION,
                        MaterialFlags.GENERATE_PLATE
                )
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
        if (Loader.isModLoaded("thaumcraft")) {
            OrePrefix.ingot.setIgnored(VOID_METAL);
            OrePrefix.ingot.setIgnored(Materials.Brass);
        }
    }
}
