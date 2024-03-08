package bruce.projectreflection.recipes;

import bruce.projectreflection.init.PRMetaTileEntityHandler;
import gregtech.loaders.recipe.CraftingComponent;
import gregtech.loaders.recipe.MetaTileEntityLoader;
import mysticalmechanics.handler.RegistryHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
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
        MetaTileEntityLoader.registerMachineRecipe(PRMetaTileEntityHandler.AURA_COLLECTORS,
                "MGM", "MHM", "CWC",
                'M', CraftingComponent.MOTOR,
                'H', CraftingComponent.HULL,
                'C', CraftingComponent.CIRCUIT,
                'W', CraftingComponent.CABLE,
                'G', new ItemStack(ItemsTC.resonator));
        MetaTileEntityLoader.registerMachineRecipe(PRMetaTileEntityHandler.MECH_IMPORT_HATCHES,
                " E ", "SHS", " E ",
                'E', CraftingComponent.SPRING,
                'S', new ItemStack(RegistryHandler.IRON_AXLE),
                'H', CraftingComponent.HULL);
        MetaTileEntityLoader.registerMachineRecipe(PRMetaTileEntityHandler.MECH_EXPORT_HATCHES,
                " S ", "EHE", " S ",
                'E', CraftingComponent.SPRING,
                'S', new ItemStack(RegistryHandler.IRON_AXLE),
                'H', CraftingComponent.HULL);
        if (!Loader.isModLoaded("pollution")) {
            MetaTileEntityLoader.registerMachineRecipe(PRMetaTileEntityHandler.FLUX_MUFFLERS,
                    "PBP", "BHB", "PRP",
                    'P', CraftingComponent.PLATE,
                    'B', new ItemStack(BlocksTC.bellows),
                    'H', CraftingComponent.HULL,
                    'R', CraftingComponent.ROTOR);
        }
    }
}
