package bruce.projectreflection.materials;

import bruce.projectreflection.PRConstants;
import com.meteor.extrabotany.common.block.fluid.ModFluid;
import gregtech.api.GTValues;
import gregtech.api.fluids.FluidBuilder;
import gregtech.api.fluids.FluidState;
import gregtech.api.fluids.attribute.FluidAttributes;
import gregtech.api.fluids.store.FluidStorageKey;
import gregtech.api.fluids.store.FluidStorageKeys;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraftforge.fml.common.Loader;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.config.ConfigBlocks;

import static bruce.projectreflection.materials.ElementMaterials.ABYSSALNITE;
import static bruce.projectreflection.materials.ElementMaterials.SCHRABIDIUM;
import static gregtech.api.unification.material.info.MaterialFlags.*;

public class FirstTierMaterials {
    public static Material PYROTHEUM;
    public static Material CRYOTHEUM;
    public static Material PETROTHEUM;
    public static Material AEROTHEUM;
    public static Material ALLOY_ADVANCED;
    public static Material PHENOLIC_RESIN;
    public static Material FORMALDEHYDE;
    public static Material LIQUIFIED_CORALIUM;
    public static Material ABYSSAL_STONE;
    public static Material DREAD_STONE;
    public static Material ABYSSALNITE_STONE;
    public static Material DARK_STONE;
    public static Material OMOTHOL_STONE;
    public static Material THAUMIUM;
    public static Material JADE;
    public static Material AURA;
    public static Material SAS3;
    public static Material SCHRABIDIC;
    public static Material UNSTABLE;
    public static Material MOON;
    public static Material LIQUID_DEATH;
    public static Material FLUIDIED_MANA;
    public static void init() {
        THAUMIUM = MaterialHelper.dynamicBuilder("thaumium")
                .ingot(3)
                .fluid()
                .color(0x51437c)
                .flags(
                        DISABLE_DECOMPOSITION,
                        GENERATE_PLATE,
                        GENERATE_ROD,
                        GENERATE_FRAME,
                        GENERATE_GEAR,
                        GENERATE_DOUBLE_PLATE
                )
                .components(new MaterialStack(Materials.Iron, 1), new MaterialStack(ElementMaterials.MANA, 1))
                .build();
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
                        NO_UNIFICATION,
                        GENERATE_PLATE
                )
                .build();
        PHENOLIC_RESIN = MaterialHelper.dynamicBuilder("phenolic_resin", false, false)
                .polymer()
                .color(0x897054)
                .components(new MaterialStack(Materials.Carbon, 7),
                        new MaterialStack(Materials.Hydrogen, 8),
                        new MaterialStack(Materials.Oxygen, 2))
                .flags(DISABLE_DECOMPOSITION,
                        GENERATE_PLATE,
                        GENERATE_FOIL,
                        GENERATE_ROD
                )
                .build();
        FORMALDEHYDE = MaterialHelper.dynamicBuilder("formaldehyde", true, true)
                .color(0x95a13a)
                .flags(DISABLE_DECOMPOSITION)
                .components(new MaterialStack(Materials.Carbon, 1),
                        new MaterialStack(Materials.Hydrogen, 2),
                        new MaterialStack(Materials.Oxygen, 1)
                )
                .build();
        LIQUIFIED_CORALIUM = MaterialHelper.dynamicBuilder("liquified_coralium", true, false)
                .ingot()
                .color(0x169265)
                .iconSet(MaterialIconSet.SHINY)
                .flags(DISABLE_DECOMPOSITION, GENERATE_PLATE, GENERATE_ROD)
                .components(new MaterialStack(ElementMaterials.CORALIUM, 1))
                .build();

        DARK_STONE = MaterialHelper.dynamicBuilder("dark_stone")
                .dust()
                .color(0)
                .iconSet(MaterialIconSet.ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(new MaterialStack(Materials.Magnesia, 1),
                        new MaterialStack(Materials.SiliconDioxide, 1),
                        new MaterialStack(Materials.GraniteBlack, 1)
                )
                .build();
        ABYSSAL_STONE = MaterialHelper.dynamicBuilder("abyssal_stone")
                .dust()
                .color(0x169265)
                .iconSet(MaterialIconSet.ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(new MaterialStack(Materials.Magnesia, 1),
                        new MaterialStack(Materials.SiliconDioxide, 1))
                .build();
        DREAD_STONE = MaterialHelper.dynamicBuilder("dread_stone")
                .dust()
                .color(0x880101)
                .iconSet(MaterialIconSet.ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(new MaterialStack(Materials.Magnesia, 1),
                        new MaterialStack(Materials.SiliconDioxide, 1),
                        new MaterialStack(ElementMaterials.DREADIUM, 1)
                )
                .build();
        ABYSSALNITE_STONE = MaterialHelper.dynamicBuilder("abyssalnite_stone")
                .dust()
                .color(0x4a1c89)
                .iconSet(MaterialIconSet.ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(new MaterialStack(Materials.Magnesia, 1),
                        new MaterialStack(Materials.SiliconDioxide, 1),
                        new MaterialStack(ABYSSALNITE, 1)
                )
                .build();
        OMOTHOL_STONE = MaterialHelper.dynamicBuilder("omothol_stone")
                .dust()
                .color(0x969e8a)
                .iconSet(MaterialIconSet.ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(new MaterialStack(Materials.Magnesia, 1),
                        new MaterialStack(Materials.SiliconDioxide, 1),
                        new MaterialStack(ElementMaterials.ETHAXIUM, 1)
                )
                .build();
        JADE = MaterialHelper.dynamicBuilder("jade")
                .gem(2)
                .ore()
                .color(0x006400)
                .iconSet(MaterialIconSet.RUBY)
                .flags(MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD)
                .components(new MaterialStack(Materials.Sodium, 2),
                        new MaterialStack(Materials.Oxygen, 1),
                        new MaterialStack(Materials.Sapphire, 5),
                        new MaterialStack(Materials.SiliconDioxide, 12)
                )
                .build();
        AURA = MaterialHelper.dynamicBuilder("aura", true, true)
                .color(0xffc0ff)
                .build();
        SAS3 = MaterialHelper.dynamicBuilder("sas_3", true, false)
                .color(0x4ffffc)
                .components(new MaterialStack(SCHRABIDIUM, 1), new MaterialStack(Materials.Sulfur, 3))
                .flags(DISABLE_DECOMPOSITION)
                .build();
        SCHRABIDIC = MaterialHelper.dynamicBuilder(PRConstants.modid, "schrabidic", true, new FluidBuilder().state(FluidState.LIQUID).attribute(FluidAttributes.ACID), false)
                .color(0x006B6B)
                .components(
                        new MaterialStack(Materials.Hydrogen, 2),
                        new MaterialStack(SCHRABIDIUM, 1), new MaterialStack(Materials.Oxygen, 4))
                .flags(DISABLE_DECOMPOSITION)
                .build();
        UNSTABLE = MaterialHelper.dynamicBuilder("unstable")
                .color(0xFFFFFF)
                .ingot()
                .build();
        MOON = MaterialHelper.dynamicBuilder("moon")
                .gem(4)
                .color(0x6f7c85)
                .build();
        LIQUID_DEATH = MaterialHelper.dynamicBuilder("liquid_death")
                .fluid(ConfigBlocks.FluidDeath.instance, FluidStorageKeys.LIQUID, FluidState.LIQUID)
                .color(0x260056)
                .build();

        FLUIDIED_MANA = MaterialHelper.dynamicBuilder("fluidedmana")
                .fluid(ModFluid.fluidMana, FluidStorageKeys.LIQUID, FluidState.LIQUID)
                .color(0x005887)
                .components(new MaterialStack(ElementMaterials.MANA, 1))
                .flags(DISABLE_DECOMPOSITION)
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
            OrePrefix.ingot.setIgnored(THAUMIUM);
        }
        if (Loader.isModLoaded("extrautils2")) {
            OrePrefix.ingot.setIgnored(UNSTABLE);
            OrePrefix.gem.setIgnored(MOON);
        }
        if (Loader.isModLoaded("abyssalcraft")) {

            OrePrefix.ingot.setIgnored(LIQUIFIED_CORALIUM);

            PROrePrefixes.oreDark.addSecondaryMaterial(new MaterialStack(DARK_STONE, GTValues.M));
            PROrePrefixes.oreDread.addSecondaryMaterial(new MaterialStack(DREAD_STONE, GTValues.M));
            PROrePrefixes.oreAbyssal.addSecondaryMaterial(new MaterialStack(ABYSSAL_STONE, GTValues.M));
            PROrePrefixes.orePurifiedDread.addSecondaryMaterial(new MaterialStack(ABYSSALNITE_STONE, GTValues.M));
            PROrePrefixes.oreOmothol.addSecondaryMaterial(new MaterialStack(OMOTHOL_STONE, GTValues.M));
        }
    }
}
