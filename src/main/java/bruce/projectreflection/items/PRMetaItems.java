package bruce.projectreflection.items;

import bruce.projectreflection.PRConstants;
import gregtech.api.GTValues;
import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.IElectricItem;
import gregtech.api.items.metaitem.ElectricStats;
import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.items.metaitem.StandardMetaItem;
import gregtech.api.items.metaitem.stats.IItemColorProvider;
import net.minecraft.item.ItemStack;

public class PRMetaItems extends StandardMetaItem {
    //public static boolean registered = false;
    public static PRMetaItems INSTANCE = new PRMetaItems();
    public static MetaItem<?>.MetaValueItem[] BATTERIES = new MetaItem<?>.MetaValueItem[GTValues.UV];
    @Override
    public void registerSubItems() {
        for (int id = 1; id < GTValues.UHV; id++) {
            BATTERIES[id - 1] = this.addItem(id, String.format("energy.module.%s", GTValues.VN[id].toLowerCase()))
                    .addComponents(ElectricStats.createBattery(GTValues.V[GTValues.LV] * 72000 * id, id, false), (IItemColorProvider) (
                            (stack, tintIndex) -> {
                                ItemStack copy = stack.copy();
                                IElectricItem electricItem = copy.getCapability(GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM, null);
                                if (electricItem != null) {
                                    double charge = (double) electricItem.getCharge();
                                    double maxCharge = (double) electricItem.getMaxCharge();
                                    int color = (int) Math.round((double) 0xff * (charge / maxCharge));
                                    return 0xff000000 | (color << 16) | (color << 8) | color;
                                }
                                return 0xffffffff;
                            }));
        }
    }

    private PRMetaItems() {
        setRegistryName(PRConstants.modid, "pr_meta_item");
        setCreativeTab(PRConstants.tab);
    }
}
