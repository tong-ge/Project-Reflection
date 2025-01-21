package bruce.projectreflection.items.behaviors;

import gregtech.common.items.behaviors.AbstractMaterialPartBehavior;
import net.minecraft.item.ItemStack;

public class CoilBehavior extends AbstractMaterialPartBehavior {
    @Override
    public int getPartMaxDurability(ItemStack itemStack) {
        return 0;
    }
}
