package bruce.projectreflection.recipes.handler;

import bruce.projectreflection.recipes.builder.AuraCollectorRecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.FuelRecipeBuilder;
import gregtech.api.recipes.builders.PrimitiveRecipeBuilder;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;

public class PRRecipeMaps {
    /*
    public static RecipeMap<FuelRecipeBuilder> NETHERSTAR_GENERATOR=new RecipeMap<>("netherstar_generator",
            0,0,1,1,new FuelRecipeBuilder(),false)
            .allowEmptyOutput();
    public static RecipeMap<FuelRecipeBuilder> MAGMA_GENERATOR=new RecipeMap<>("magma_generator",
            0,0,1,1,new FuelRecipeBuilder(),false);
    public static RecipeMap<FuelRecipeBuilder> REDSTONE_GENERATOR=new RecipeMap<>("redstone_generator",
            1,0,1,1,new FuelRecipeBuilder(),false);
    public static RecipeMap<FuelRecipeBuilder> CRYOGENIC_TURBINE=new RecipeMap<>("cryogenic_turbine",
            1,0,1,1,new FuelRecipeBuilder(),false);

     */
    public static RecipeMap<SimpleRecipeBuilder> PETROTHEUM_MACERATOR = new RecipeMap<>(
            "petrotheum_macerator",
            1, 3, 1, 0,
            new SimpleRecipeBuilder(), false);
    /*
    public static RecipeMap<SimpleRecipeBuilder> AEROTHEUM_CENTRIFUGE = new RecipeMap<>(
            "aerotheum_centrifuge",
            1, 6, 1, 0,
            new SimpleRecipeBuilder(), false);
            */
    public static RecipeMap<PrimitiveRecipeBuilder> CERAMIC_OVEN = new RecipeMap<>(
            "ceramic_oven",
            3, 2, 0, 0, new PrimitiveRecipeBuilder(), false);
    public static RecipeMap<AuraCollectorRecipeBuilder> AURA_COLLECTOR = new RecipeMap<>("aura_collector",
            1, 0, 0, 1, new AuraCollectorRecipeBuilder(), false);
}
