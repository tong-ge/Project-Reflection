package bruce.projectreflection.event;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.entity.EntityBlackhole;
import bruce.projectreflection.items.*;
import bruce.projectreflection.items.codebook.ItemCodebook;
import bruce.projectreflection.materials.MaterialHelper;
import bruce.projectreflection.materials.PRStoneType;
import bruce.projectreflection.recipes.RecipeManager;
import gregtech.api.GregTechAPI;
import gregtech.api.event.HighTierEvent;
import gregtech.api.unification.material.event.MaterialEvent;
import gregtech.api.unification.material.event.PostMaterialEvent;
import gregtech.loaders.recipe.CraftingComponent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;

@Mod.EventBusSubscriber(modid = PRConstants.modid)
public class RegistryEvents {
    @SubscribeEvent
    public static void enableHighTier(HighTierEvent event) {
        event.enableHighTier();
    }
    @SubscribeEvent
    public static void onMaterial(MaterialEvent event) {
        MaterialHelper.init();
    }

    @SubscribeEvent
    public static void onPrefix(PostMaterialEvent event) {
        MaterialHelper.orePrefix();
    }

    @SubscribeEvent
    public static void preRegisterRecipes(RegistryEvent.Register<IRecipe> event) {
        RecipeManager.preInit();
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        RecipeManager.init();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        if (Loader.isModLoaded("abyssalcraft")) {
            PRStoneType.ABYSSAL_STONE.name.getBytes();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void preRegisterItems(RegistryEvent.Register<Item> event) {

    }
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemManifold(),
                new ItemCodebook());
    }

    @SubscribeEvent
    public static void registerComponents(GregTechAPI.RegisterEvent<CraftingComponent> event) {
        ProjectReflection.logger.info("Overriding GTCEU Registries...");
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().register(EntityBlackhole.ENTRY);
    }
}
