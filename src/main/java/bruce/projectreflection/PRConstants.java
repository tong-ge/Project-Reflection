package bruce.projectreflection;

import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.FuelRecipeBuilder;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class PRConstants {
    public static final String modid="projectreflection";
    public static final String[] V={"ulv","lv","mv","hv","ev","iv","luv","zpm","uv","uhv","uev","uiv","uxv","opv","max"};

    public static final ItemStack ASH= OreDictUnifier.get(OrePrefix.dustTiny, Materials.Ash);
    public static final CreativeTabs tab=new CreativeTabs("ProjectReflection") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.COMMAND_BLOCK);
        }
    };
}
