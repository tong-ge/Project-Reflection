package bruce.projectreflection.materials;

import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.GemProperty;
import gregtech.api.unification.material.properties.PropertyKey;

public class GTMaterials {
    public static void init() {
        Materials.EnderPearl.setFormula("BeK4N5Ma6", true);
        Materials.Blaze.setFormula("CSMa", true);
        Materials.IridiumMetalResidue.setProperty(PropertyKey.GEM, new GemProperty());
    }
}
