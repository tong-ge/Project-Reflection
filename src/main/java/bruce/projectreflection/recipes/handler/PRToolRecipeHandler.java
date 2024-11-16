package bruce.projectreflection.recipes.handler;

import bruce.projectreflection.items.ItemMetalArmor;
import bruce.projectreflection.items.behaviors.MetalArmorBehavior;
import bruce.projectreflection.materials.properties.ArmorProperty;
import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.recipes.ModHandler;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.common.items.behaviors.AbstractMaterialPartBehavior;
import net.minecraft.item.ItemStack;

import static gregtech.api.unification.material.info.MaterialFlags.GENERATE_PLATE;

public class PRToolRecipeHandler {
    public static void init() {
        OrePrefix.plate.addProcessingHandler(ArmorProperty.KEY, PRToolRecipeHandler::processTool);
    }

    @SuppressWarnings("rawtypes")
    private static void addShapedRecipe(Material material, MetaItem.MetaValueItem item, Object... recipe) {
        ItemStack armorStack = item.getStackForm();
        MetalArmorBehavior.setPartMaterial(armorStack, material);
        ModHandler.addShapedRecipe(String.format("%s_%s", item.unlocalizedName, material.getName()), armorStack, recipe);
    }

    private static void processTool(OrePrefix prefix, Material material, ArmorProperty property) {
        UnificationEntry plate = new UnificationEntry(OrePrefix.plate, material);
        if (material.hasFlag(GENERATE_PLATE)) {
            addShapedRecipe(material, ItemMetalArmor.HELMET,
                    "PPP", "PhP",
                    'P', plate);
            addShapedRecipe(material, ItemMetalArmor.CHESTPLATE,
                    "PhP", "PPP", "PPP",
                    'P', plate);
            addShapedRecipe(material, ItemMetalArmor.LEGGINGS,
                    "PPP", "PhP", "P P",
                    'P', plate);
            addShapedRecipe(material, ItemMetalArmor.BOOTS,
                    "P P", "PhP",
                    'P', plate);

        }
    }
}
