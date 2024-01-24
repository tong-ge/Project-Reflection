package bruce.projectreflection.materials;

import gregtech.api.unification.material.Material;

public class EssenceMaterials {
    public static Material NETHERSTAR_ESSENCE;
    public static Material MAGMA_ESSENCE;
    public static Material CRYOGENIC_ESSENCE;

    public static void init() {
        NETHERSTAR_ESSENCE = MaterialHelper.dynamicBuilder("netherstar_essence", true, false)
                .color(0xffffff)
                .build();
        MAGMA_ESSENCE = MaterialHelper.dynamicBuilder("magma_essence", true, 2000, false)
                .color(0xffc000)
                .build();
        CRYOGENIC_ESSENCE = MaterialHelper.dynamicBuilder("cryogenic_essence", true, 4, false)
                .color(0x00c0ff)
                .build();
    }
}
