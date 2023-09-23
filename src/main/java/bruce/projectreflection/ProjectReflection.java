package bruce.projectreflection;

import bruce.projectreflection.metatileentity.PRMetaTileEntityHandler;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.FuelRecipeBuilder;
import gregtech.api.unification.material.Materials;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;


@Mod(modid = ProjectReflection.modid,dependencies = "required-after:gregtech",useMetadata = true)
public class ProjectReflection {
    public static final String modid="projectreflection";
    public static final String[] V={"ulv","lv","mv","hv","ev","iv","luv","zpm","uv","uhv","uev","uiv","uxv","opv","max"};
    public static RecipeMap<FuelRecipeBuilder> HYDRAULIC_GENERATOR=new RecipeMap<>("hydraulic_generator",
            0,0,1,0,new FuelRecipeBuilder(),false)
            .allowEmptyOutput();
    public static final CreativeTabs tab=new CreativeTabs("ProjectReflection") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.COMMAND_BLOCK);
        }
    };
    public static Logger logger;
    @Mod.EventHandler
    public void main(FMLPreInitializationEvent event)
    {
        logger=event.getModLog();
        PRMetaTileEntityHandler.main(new String[0]);
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        HYDRAULIC_GENERATOR.recipeBuilder()
                .fluidInputs(Materials.Water.getFluid(5000))
                .EUt(32)
                .duration(10)
                .buildAndRegister();
    }
}
