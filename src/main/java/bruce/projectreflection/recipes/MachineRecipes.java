package bruce.projectreflection.recipes;

import bruce.projectreflection.init.PRMetaTileEntityHandler;
import gregtech.loaders.recipe.CraftingComponent;
import gregtech.loaders.recipe.MetaTileEntityLoader;
import net.minecraft.item.ItemStack;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;

public class MachineRecipes {
    public static void register() {
        MetaTileEntityLoader.registerMachineRecipe(PRMetaTileEntityHandler.MASS_FABRICATORS,
                "CFC", "QMQ", "CFC",
                'M', CraftingComponent.HULL,
                'Q', CraftingComponent.CABLE_QUAD,
                'C', CraftingComponent.BETTER_CIRCUIT,
                'F', CraftingComponent.FIELD_GENERATOR);
        MetaTileEntityLoader.registerMachineRecipe(PRMetaTileEntityHandler.AURA_GENERATORS,
                "MGM", "MHM", "CWC",
                'M', CraftingComponent.MOTOR,
                'H', CraftingComponent.HULL,
                'C', CraftingComponent.CIRCUIT,
                'W', CraftingComponent.CABLE,
                'G', new ItemStack(BlocksTC.visGenerator));
    }
}
