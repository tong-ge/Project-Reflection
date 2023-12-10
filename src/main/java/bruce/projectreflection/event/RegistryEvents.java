package bruce.projectreflection.event;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.recipes.RecipeManager;
import bruce.projectreflection.materials.MaterialHelper;
import gregtech.api.unification.material.event.MaterialEvent;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = PRConstants.modid)
public class RegistryEvents {
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
}
