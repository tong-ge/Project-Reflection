package bruce.projectreflection;

import net.minecraftforge.common.config.Config;

@Config(modid = PRConstants.modid)
public class PRConfig {
    public static boolean disruptRecipes = false;
    public static boolean debug = PRConstants.inDev;
    public static boolean verboseDebug = false;
}