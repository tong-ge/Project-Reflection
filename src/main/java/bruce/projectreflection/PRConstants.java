package bruce.projectreflection;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Random;

public class PRConstants {
    public static final String modid="projectreflection";
    public static final String[] V={"ulv","lv","mv","hv","ev","iv","luv","zpm","uv","uhv","uev","uiv","uxv","opv","max"};

    //public static final ItemStack ASH= OreDictUnifier.get(OrePrefix.dustTiny, Materials.Ash);
    public static final boolean inDev = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    public static final CreativeTabs tab=new CreativeTabs("ProjectReflection") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.COMMAND_BLOCK);
        }
    };
    public static final Random rand=new Random();
}
