package bruce.projectreflection.materials;

import gregtech.api.fluids.FluidBuilder;
import gregtech.api.fluids.FluidState;
import gregtech.api.fluids.store.FluidStorageKeys;
import gregtech.api.unification.Elements;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;

public class FirstTierMaterials {
    public static Material CORALIUM;
    public static Material PYROTHEUM;
    public static Material CRYOTHEUM;
    public static Material PETROTHEUM;
    public static Material AEROTHEUM;
    public static Material MANA;
    public static Material VOID_METAL;
    public static Material ALLOY_ADVANCED;
    public static Material PHENOLIC_RESIN;
    public static Material FORMALDEHYDE;

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
        MANA = MaterialHelper.dynamicBuilder("mana", true, false)
                .dust()
                .color(0x5e48ff)
                .element(Elements.Ma)
                .build();
        VOID_METAL = MaterialHelper.dynamicBuilder("void", true, false)
                .ingot(4)
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
        CORALIUM = MaterialHelper.dynamicBuilder("coralium")
                .gem()
                .element(PRElements.Cor)
                .ore()
                .flags(MaterialFlags.HIGH_SIFTER_OUTPUT)
                .color(0x169265)
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
        if (Loader.isModLoaded("abyssalcraft")) {
            OrePrefix.gem.setIgnored(CORALIUM);
            OrePrefix.block.setIgnored(CORALIUM);
        }
    }
}
