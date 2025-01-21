package bruce.projectreflection.recipes;

import bruce.projectreflection.items.PRMetaItems;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import net.minecraft.init.Items;

public class RecipeManager {
    public static void preInit() {

    }
    public static void init() {
        MachineRecipes.register();
        PRRecipeMaps.RESEARCH_TABLE_RECIPES.recipeBuilder()
                .input(Items.PAPER)
                .input("dyeBlue")
                .outputs(PRMetaItems.BLUEPRINT.getStackForm(1))
                .EUt(30)
                .duration(20)
                .buildAndRegister();
    }
}
