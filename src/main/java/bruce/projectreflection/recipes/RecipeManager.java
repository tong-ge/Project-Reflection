package bruce.projectreflection.recipes;

import bruce.projectreflection.recipes.handler.OreProcessingHandler;
import bruce.projectreflection.recipes.routines.CircuitRoutine;
import bruce.projectreflection.recipes.routines.PhenolicResinRoutine;
import bruce.projectreflection.recipes.routines.SuperIridiumAlloyRoutine;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Materials;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RecipeManager {
    public static void preInit() {
        OreProcessingHandler.init();
    }
    public static void init() {
        RecipeRemover.doRemove();
        MachineRecipes.register();
        PhenolicResinRoutine.init();
        SuperIridiumAlloyRoutine.init();
        CircuitRoutine.init();
        RecipeMaps.MASS_FABRICATOR_RECIPES.recipeBuilder()
                .circuitMeta(1)
                .EUt(256)
                .duration(3200)
                .fluidOutputs(Materials.UUMatter.getFluid(1))
                .buildAndRegister();
        RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder()
                .inputs(new ItemStack(Items.GLASS_BOTTLE, 3),
                        new ItemStack(Items.NETHER_WART),
                        new ItemStack(Items.RABBIT_FOOT)
                )
                .outputs(new ItemStack(Items.EXPERIENCE_BOTTLE, 3))
                .EUt(512)
                .duration(Integer.MAX_VALUE)
                .buildAndRegister();
    }
}
