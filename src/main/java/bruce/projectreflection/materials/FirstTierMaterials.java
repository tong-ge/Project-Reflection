package bruce.projectreflection.materials;

import gregtech.api.unification.Elements;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraftforge.fml.common.Loader;

public class FirstTierMaterials {
    public static Material PYROTHEUM;
    public static Material CRYOTHEUM;
    public static Material PETROTHEUM;
    public static Material AEROTHEUM;
    public static Material ALLOY_ADVANCED;
    public static Material PHENOLIC_RESIN;
    public static Material FORMALDEHYDE;
    public static Material ABYSSALNITE;
    public static Material ABYSSALNATE;
    public static void init() {
        PYROTHEUM = MaterialHelper.dynamicBuilder("pyrotheum",
                        true, 4000, false)
                .dust()
                .color(0xff9a3c)
                .build();
        CRYOTHEUM = MaterialHelper.dynamicBuilder("cryotheum",
                        true, 50, false)
                .dust()
                .color(0x01f3f6).build();
        PETROTHEUM = MaterialHelper.dynamicBuilder("petrotheum",
                        true, 350, false)
                .dust()
                .color(0x6b585b)
                .build();
        AEROTHEUM = MaterialHelper.dynamicBuilder("aerotheum",
                        true, true)
                .dust().color(0xd6ff68).build();
        ALLOY_ADVANCED = MaterialHelper.dynamicBuilder("alloy_advanced")
                .ingot()
                .color(0x515044)
                .components(
                        new MaterialStack(Materials.WroughtIron, 1),
                        new MaterialStack(Materials.Bronze, 1),
                        new MaterialStack(Materials.Tin, 1)
                )
                .flags(
                        MaterialFlags.NO_UNIFICATION,
                        MaterialFlags.GENERATE_PLATE
                )
                .build();
        PHENOLIC_RESIN = MaterialHelper.dynamicBuilder("phenolic_resin", false, false)
                .polymer()
                .color(0x897054)
                .components(new MaterialStack(Materials.Carbon, 7),
                        new MaterialStack(Materials.Hydrogen, 8),
                        new MaterialStack(Materials.Oxygen, 2))
                .flags(MaterialFlags.DISABLE_DECOMPOSITION,
                        MaterialFlags.GENERATE_PLATE,
                        MaterialFlags.GENERATE_FOIL,
                        MaterialFlags.GENERATE_ROD
                )
                .build();
        FORMALDEHYDE = MaterialHelper.dynamicBuilder("formaldehyde", true, true)
                .color(0x95a13a)
                .flags(MaterialFlags.DISABLE_DECOMPOSITION)
                .components(new MaterialStack(Materials.Carbon, 1),
                        new MaterialStack(Materials.Hydrogen, 2),
                        new MaterialStack(Materials.Oxygen, 1)
                )
                .build();
        ABYSSALNITE = MaterialHelper.dynamicBuilder("abyssalnite")
                .ingot()
                .color(0x4a1c89)
                .iconSet(MaterialIconSet.DULL)
                .flags(MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD, MaterialFlags.DISABLE_DECOMPOSITION)
                .components(
                        new MaterialStack(ElementMaterials.ABYSSALNIUM, 1),
                        new MaterialStack(Materials.Oxygen, 1)
                )
                .build();
        ABYSSALNATE = MaterialHelper.dynamicBuilder("abyssalnate")
                .gem()
                .color(0x4a1c89)
                .iconSet(MaterialIconSet.DIAMOND)
                .flags(MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD, MaterialFlags.DISABLE_DECOMPOSITION)
                .components(
                        new MaterialStack(ElementMaterials.ABYSSALNIUM, 1),
                        new MaterialStack(Materials.Oxygen, 2)
                )
                .build();
    }

    public static void orePrefix() {
        if (Loader.isModLoaded("thermalfoundation")) {
            OrePrefix.dust.setIgnored(PYROTHEUM);
            OrePrefix.dust.setIgnored(CRYOTHEUM);
            OrePrefix.dust.setIgnored(PETROTHEUM);
            OrePrefix.dust.setIgnored(AEROTHEUM);
        }
        if (Loader.isModLoaded("thaumcraft")) {
            OrePrefix.ingot.setIgnored(Materials.Brass);
        }
        if (Loader.isModLoaded("abyssalcraft")) {
            OrePrefix.ingot.setIgnored(ABYSSALNITE);
        }
    }
}
