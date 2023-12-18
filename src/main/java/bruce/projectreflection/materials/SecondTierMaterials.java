package bruce.projectreflection.materials;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraftforge.fml.common.Loader;

public class SecondTierMaterials {

    public static Material THAUMIUM;
    public static Material ALLOY_IRIDIUM;
    public static Material SODIUM_ABYSSALNITE;
    public static Material CALCIUM_ABYSSALNITE;

    public static void init() {

        THAUMIUM = MaterialHelper.dynamicBuilder("thaumium")
                .ingot(3)
                .fluid()
                .color(0x51437c)
                .flags(
                        MaterialFlags.DISABLE_DECOMPOSITION,
                        MaterialFlags.GENERATE_PLATE,
                        MaterialFlags.GENERATE_ROD,
                        MaterialFlags.GENERATE_FRAME,
                        MaterialFlags.GENERATE_GEAR,
                        MaterialFlags.GENERATE_DOUBLE_PLATE
                )
                .components(new MaterialStack(Materials.Iron, 1), new MaterialStack(ElementMaterials.MANA, 1))
                .build();
        ALLOY_IRIDIUM = MaterialHelper.dynamicBuilder("alloy_iridium")
                .ingot()
                .components(
                        new MaterialStack(Materials.Iridium, 4),
                        new MaterialStack(FirstTierMaterials.ALLOY_ADVANCED, 4),
                        new MaterialStack(Materials.Diamond, 1)
                )
                .flags(
                        MaterialFlags.NO_UNIFICATION,
                        MaterialFlags.GENERATE_PLATE
                )
                .color(0xc2c1df)
                .iconSet(MaterialIconSet.METALLIC)
                .build();
        SODIUM_ABYSSALNITE = MaterialHelper.dynamicBuilder("sodium_abyssalnite")
                .gem()
                .color(0x4a1c89)
                .iconSet(MaterialIconSet.LAPIS)
                .flags(MaterialFlags.DISABLE_DECOMPOSITION, MaterialFlags.CRYSTALLIZABLE)
                .components(
                        new MaterialStack(Materials.Sodium, 2),
                        new MaterialStack(FirstTierMaterials.ABYSSALNATE, 1)
                )
                .build();
        CALCIUM_ABYSSALNITE = MaterialHelper.dynamicBuilder("calcium_abyssalnite")
                .gem()
                .color(0x4a1c89)
                .iconSet(MaterialIconSet.ROUGH)
                .flags(MaterialFlags.DISABLE_DECOMPOSITION)
                .components(
                        new MaterialStack(Materials.Calcium, 1),
                        new MaterialStack(FirstTierMaterials.ABYSSALNATE, 1)
                )
                .build();
    }

    public static void orePrefix() {
        if (Loader.isModLoaded("thaumcraft")) {
            OrePrefix.ingot.setIgnored(SecondTierMaterials.THAUMIUM);
        }
    }
}
