package bruce.projectreflection;

import com.google.gson.Gson;
import gregtech.api.GTValues;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class PRConstants {
    public static final String modid="projectreflection";
    public static final String[] V = Arrays.stream(GTValues.VN).map(String::toLowerCase).collect(Collectors.toList()).toArray(new String[0]);
    public static final boolean inDev = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    public static final CreativeTabs tab=new CreativeTabs("ProjectReflection") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.COMMAND_BLOCK);
        }
    };
    public static final Random structureRandom = new Random();
    public static final String TAG_ITEMSTACK_TO_DROP = "projectreflection:mobDuplicatorItemStackToDrop";
    public static final Gson gson = new Gson();
}
