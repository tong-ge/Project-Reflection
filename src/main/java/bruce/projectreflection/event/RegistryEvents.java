package bruce.projectreflection.event;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.materials.PRMaterials;
import gregtech.api.unification.material.event.MaterialEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = PRConstants.modid)
public class RegistryEvents {
    @SubscribeEvent
    public static void onMaterial(MaterialEvent event) {
        PRMaterials.init();
    }
}
