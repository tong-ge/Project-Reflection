package bruce.projectreflection.materials;

import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.FluidProperty;
import gregtech.api.unification.material.properties.GemProperty;
import gregtech.api.unification.material.properties.PropertyKey;
import net.minecraft.block.material.Material;

import java.util.Arrays;

public class GTMaterials {
    public static void init() {
        Materials.EnderPearl.setFormula("BeK4N5Ma6", true);
        Materials.Blaze.setFormula("CSMa", true);
        Materials.IridiumMetalResidue.setProperty(PropertyKey.GEM, new GemProperty());
        Materials.NetherStar.setProperty(PropertyKey.FLUID, new FluidProperty());
        Arrays.asList(Materials.Iron, Materials.Gold, Materials.Copper, Materials.Tin, Materials.Silver, Materials.Lead, Materials.Cinnabar).forEach(PROrePrefixes.cluster::setIgnored);
    }
}
