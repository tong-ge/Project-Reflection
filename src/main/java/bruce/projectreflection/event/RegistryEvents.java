package bruce.projectreflection.event;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.items.ItemBSOD;
import bruce.projectreflection.materials.FirstTierMaterials;
import bruce.projectreflection.materials.PRStoneType;
import bruce.projectreflection.recipes.RecipeManager;
import bruce.projectreflection.materials.MaterialHelper;
import com.shinoow.abyssalcraft.api.block.ACBlocks;
import com.shinoow.abyssalcraft.common.blocks.BlockACStone;
import gregtech.api.GregTechAPI;
import gregtech.api.event.HighTierEvent;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.event.MaterialEvent;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.ore.StoneType;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.loaders.recipe.CraftingComponent;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBSOD());
    }

    @SubscribeEvent
    public static void registerComponents(GregTechAPI.RegisterEvent<CraftingComponent> event) {
        ProjectReflection.logger.info("Overriding GTCEU Registries...");
        CraftingComponent.STICK_MAGNETIC.appendIngredients(Stream.of(new Object[][]{
                {4, new UnificationEntry(OrePrefix.stick, FirstTierMaterials.NEODYMIUM_MAGNET)},
                {5, new UnificationEntry(OrePrefix.stick, FirstTierMaterials.NEODYMIUM_MAGNET)},
                {6, new UnificationEntry(OrePrefix.stickLong, FirstTierMaterials.NEODYMIUM_MAGNET)},
                {7, new UnificationEntry(OrePrefix.stickLong, FirstTierMaterials.NEODYMIUM_MAGNET)},
                {8, new UnificationEntry(OrePrefix.block, FirstTierMaterials.NEODYMIUM_MAGNET)}
        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])
        ));
        CraftingComponent.HULL_PLATE.appendIngredients(Stream.of(new Object[][]{
                {4, new UnificationEntry(OrePrefix.stick, FirstTierMaterials.NEODYMIUM_MAGNET)},
                {5, new UnificationEntry(OrePrefix.stick, FirstTierMaterials.NEODYMIUM_MAGNET)},
                {6, new UnificationEntry(OrePrefix.stickLong, FirstTierMaterials.NEODYMIUM_MAGNET)},
                {7, new UnificationEntry(OrePrefix.stickLong, FirstTierMaterials.NEODYMIUM_MAGNET)},
                {8, new UnificationEntry(OrePrefix.block, FirstTierMaterials.NEODYMIUM_MAGNET)}
        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])
        ));

    }

}
