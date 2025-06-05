package bruce.projectreflection.client;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.items.PRMetaItems;
import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.IElectricItem;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = PRConstants.modid)
public class ClientEvents {
    @SubscribeEvent
    public static void itemColors(ColorHandlerEvent.Item event) {
        ItemColors colors = event.getItemColors();
    }
}
