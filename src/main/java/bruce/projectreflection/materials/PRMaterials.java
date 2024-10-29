package bruce.projectreflection.materials;

import gregtech.api.unification.material.Material;

public class PRMaterials {
    public static Material SIRINIUM;

    public static void init() {
        SIRINIUM = MaterialHelper.dynamicBuilder("sirinium")
                .ingot()
                .build();
    }
}
