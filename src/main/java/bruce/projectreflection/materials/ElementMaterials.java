package bruce.projectreflection.materials;

import gregtech.api.unification.Elements;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraftforge.fml.common.Loader;

public class ElementMaterials {
    //thermal
    public static Material MANA;
    //thaumcraft
    public static Material VOID_METAL;
    //abyssalcraft
    public static Material CORALIUM;
    public static Material DREADIUM;
    public static Material ABYSSALNITE;
    public static Material ETHAXIUM;
    public static Material SCHRABIDIUM;

    public static void init() {
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
                .element(PRElements.Vd)
                .build();
        CORALIUM = MaterialHelper.dynamicBuilder("coralium")
                .gem()
                .element(PRElements.Cor)
                .ore()
                .flags(MaterialFlags.HIGH_SIFTER_OUTPUT)
                .color(0x169265)
                .build();
        DREADIUM = MaterialHelper.dynamicBuilder("dreadium")
                .ingot()
                .color(0x880101)
                .iconSet(MaterialIconSet.SHINY)
                .flags(MaterialFlags.GENERATE_PLATE,
                        MaterialFlags.GENERATE_ROD)
                .element(PRElements.Dr1)
                .build();
        ABYSSALNITE = MaterialHelper.dynamicBuilder("abyssalnite")
                .ingot()
                .color(0x4a1c89)
                .iconSet(MaterialIconSet.METALLIC)
                .flags(MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD)
                .element(PRElements.An)
                .build();
        ETHAXIUM = MaterialHelper.dynamicBuilder("ethaxium")
                .ingot()
                .color(0x969e8a)
                .iconSet(MaterialIconSet.METALLIC)
                .flags(MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD, MaterialFlags.GENERATE_DOUBLE_PLATE)
                .element(PRElements.Et)
                .blast(5000)
                .build();
        SCHRABIDIUM = MaterialHelper.dynamicBuilder("schrabidium")
                .ingot()
                .color(0x32FFFF)
                .iconSet(MaterialIconSet.MAGNETIC)
                .flags(MaterialFlags.IS_MAGNETIC)
                .element(PRElements.Sa)

                .build();

    }

    public static void orePrefix() {
        if (Loader.isModLoaded("thermalfoundation")) {
            OrePrefix.dust.setIgnored(ElementMaterials.MANA);
        }
        if (Loader.isModLoaded("thaumcraft")) {
            OrePrefix.ingot.setIgnored(ElementMaterials.VOID_METAL);
            OrePrefix.ingot.setIgnored(Materials.Brass);
        }
        if (Loader.isModLoaded("abyssalcraft")) {
            OrePrefix.gem.setIgnored(ElementMaterials.CORALIUM);
            OrePrefix.ingot.setIgnored(ElementMaterials.DREADIUM);
            OrePrefix.ingot.setIgnored(ElementMaterials.ETHAXIUM);
            OrePrefix.block.setIgnored(ElementMaterials.CORALIUM);
            OrePrefix.block.setIgnored(ElementMaterials.DREADIUM);
            OrePrefix.block.setIgnored(ElementMaterials.ETHAXIUM);
            OrePrefix.ingot.setIgnored(ABYSSALNITE);
        }
    }
}
