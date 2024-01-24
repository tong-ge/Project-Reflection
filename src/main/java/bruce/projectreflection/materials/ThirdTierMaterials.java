package bruce.projectreflection.materials;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraftforge.fml.common.Loader;

public class ThirdTierMaterials {

    public static Material SUPER_IRIDIUM_ALLOY;

    public static void init() {

        SUPER_IRIDIUM_ALLOY = MaterialHelper.dynamicBuilder("super_iridium_alloy")
                .ingot()
                .components(new MaterialStack(Materials.NiobiumTitanium, 6), new MaterialStack(SecondTierMaterials.ALLOY_IRIDIUM, 3))
                .flags(
                        MaterialFlags.GENERATE_PLATE,
                        MaterialFlags.GENERATE_DENSE,
                        MaterialFlags.NO_UNIFICATION
                )
                .color(0xc2c1df)
                .iconSet(MaterialIconSet.SHINY)
                .build();
    }

    public static void orePrefix() {

    }
}
