package bruce.projectreflection.materials;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraftforge.fml.common.Loader;

public class SecondTierMaterials {


    public static Material ALLOY_IRIDIUM;
    public static Material MITHRIL;
    public static Material VINTEUM;

    public static void init() {
        MITHRIL = MaterialHelper.dynamicBuilder("mithril")
                .ingot()
                .fluid()
                .blast(5000)
                .color(0x428fdb)
                .iconSet(MaterialIconSet.DULL)
                .flags(MaterialFlags.GENERATE_PLATE,
                        MaterialFlags.GENERATE_DOUBLE_PLATE,
                        MaterialFlags.GENERATE_FOIL,
                        MaterialFlags.GENERATE_FINE_WIRE,
                        MaterialFlags.GENERATE_SMALL_GEAR,
                        MaterialFlags.GENERATE_FRAME)
                .components(new MaterialStack(Materials.PlatinumGroupSludge, 4), new MaterialStack(FirstTierMaterials.THAUMIUM, 1))
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

        VINTEUM = MaterialHelper.dynamicBuilder("vinteum")
                .ingot()
                .ore()
                .color(0x64c8ff)
                .iconSet(MaterialIconSet.METALLIC)
                .components(
                        new MaterialStack(FirstTierMaterials.THAUMIUM, 1)
                )
                .flags(MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_DOUBLE_PLATE)
                .build();
    }

    public static void orePrefix() {
        if (Loader.isModLoaded("thermalfoundation")) {
            OrePrefix.ingot.setIgnored(MITHRIL);
        }
    }
}
