package bruce.projectreflection.materials;

import gregtech.api.unification.material.info.MaterialIconType;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.util.GTUtility;
import net.minecraftforge.oredict.OreDictionary;

public class PROrePrefixes {
    public static final MaterialIconType crystalIcon = new MaterialIconType("crystal");
    public static final MaterialIconType clusterIcon = new MaterialIconType("cluster");
    public static final OrePrefix oreAbyssal = new OrePrefix("oreAbyssal", -1, null, MaterialIconType.ore,
            OrePrefix.Flags.ENABLE_UNIFICATION, OrePrefix.Conditions.hasOreProperty);
    public static final OrePrefix oreDread = new OrePrefix("oreDread", -1, null, MaterialIconType.ore,
            OrePrefix.Flags.ENABLE_UNIFICATION, OrePrefix.Conditions.hasOreProperty);
    public static final OrePrefix orePurifiedDread = new OrePrefix("orePurifiedDread", -1, null, MaterialIconType.ore,
            OrePrefix.Flags.ENABLE_UNIFICATION, OrePrefix.Conditions.hasOreProperty);
    public static final OrePrefix oreOmothol = new OrePrefix("oreOmothol", -1, null, MaterialIconType.ore,
            OrePrefix.Flags.ENABLE_UNIFICATION, OrePrefix.Conditions.hasOreProperty);
    public static final OrePrefix oreDark = new OrePrefix("oreDark", -1, null, MaterialIconType.ore,
            OrePrefix.Flags.ENABLE_UNIFICATION, OrePrefix.Conditions.hasOreProperty);
    public static final OrePrefix crystal = new OrePrefix("crystal", -1, null, crystalIcon,
            OrePrefix.Flags.ENABLE_UNIFICATION, OrePrefix.Conditions.hasOreProperty);
    public static final OrePrefix cluster = new OrePrefix("cluster", -1, null, clusterIcon,
            OrePrefix.Flags.ENABLE_UNIFICATION, OrePrefix.Conditions.hasOreProperty);
}