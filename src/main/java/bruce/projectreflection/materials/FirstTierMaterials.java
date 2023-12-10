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

    public static void init() {
        Fluid cryotheum = FluidRegistry.getFluid("cryotheum");
        Fluid petrotheum = FluidRegistry.getFluid("petrotheum");
        Fluid aerotheum = FluidRegistry.getFluid("aerotheum");
        Fluid mana = FluidRegistry.getFluid("mana");
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
