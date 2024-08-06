package bruce.projectreflection.recipes.handler;

import bruce.projectreflection.items.PRTools;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.material.properties.ToolProperty;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.loaders.recipe.handlers.ToolRecipeHandler;

import static gregtech.api.unification.material.info.MaterialFlags.GENERATE_PLATE;

public class PRToolRecipeHandler {
    public static void init() {
        OrePrefix.plate.addProcessingHandler(PropertyKey.TOOL, PRToolRecipeHandler::processTool);
    }

    private static void processTool(OrePrefix prefix, Material material, ToolProperty property) {
        UnificationEntry plate = new UnificationEntry(OrePrefix.plate, material);
        if (material.hasFlag(GENERATE_PLATE)) {
            ToolRecipeHandler.addToolRecipe(material, PRTools.HELMET, false,
                    "PPP", "PhP",
                    'P', plate);
            ToolRecipeHandler.addToolRecipe(material, PRTools.CHESTPLATE, false,
                    "PhP", "PPP", "PPP",
                    'P', plate);
            ToolRecipeHandler.addToolRecipe(material, PRTools.LEGGINGS, false,
                    "PPP", "PhP", "P P",
                    'P', plate);
            ToolRecipeHandler.addToolRecipe(material, PRTools.BOOTS, false,
                    "P P", "PhP",
                    'P', plate);
        }
    }
}
