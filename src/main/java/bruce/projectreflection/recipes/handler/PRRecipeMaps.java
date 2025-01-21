package bruce.projectreflection.recipes.handler;

import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.BlastRecipeBuilder;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.core.sound.GTSoundEvents;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import thaumcraft.common.lib.SoundsTC;

public class PRRecipeMaps {
    public static final RecipeMap<SimpleRecipeBuilder> DEHYDRATOR_RECIPES = new RecipeMap<>("dehydrator",
            1, 3, 1, 1, new SimpleRecipeBuilder(), false)
            .setSound(GTSoundEvents.FURNACE);
    public static final RecipeMap<SimpleRecipeBuilder> RESEARCH_TABLE_RECIPES = new RecipeMap<>("research_table",
            2, 1, 1, 0, new SimpleRecipeBuilder(), false);
    public static final RecipeMap<BlastRecipeBuilder> SUPERCONDUCTOR_SMELTER = new RecipeMap<>("superconductor_smelter", 9, 3, 3, 1, new BlastRecipeBuilder(), false);
}
