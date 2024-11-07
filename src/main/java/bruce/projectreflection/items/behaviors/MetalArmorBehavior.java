package bruce.projectreflection.items.behaviors;

import bruce.projectreflection.materials.properties.PropertyArmor;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.material.properties.ToolProperty;
import gregtech.common.items.behaviors.AbstractMaterialPartBehavior;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class MetalArmorBehavior extends AbstractMaterialPartBehavior {
    private final EntityEquipmentSlot slot;

    public MetalArmorBehavior(EntityEquipmentSlot slot) {
        this.slot = slot;
    }

    @Override
    public int getPartMaxDurability(ItemStack itemStack) {
        Material material = getPartMaterial(itemStack);
        PropertyArmor property = material.getProperty(PropertyArmor.KEY);
        return property.getDurability(slot);
    }

    public void applyArmorDamage(ItemStack itemStack, int damageApplied) {
        int rotorDurability = this.getPartMaxDurability(itemStack);
        int resultDamage = getPartDamage(itemStack) + damageApplied;
        if (resultDamage >= rotorDurability) {
            itemStack.shrink(1);
        } else {
            this.setPartDamage(itemStack, resultDamage);
        }

    }
}
