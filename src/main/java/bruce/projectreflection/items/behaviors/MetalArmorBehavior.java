package bruce.projectreflection.items.behaviors;

import bruce.projectreflection.materials.properties.ArmorProperty;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.common.items.behaviors.AbstractMaterialPartBehavior;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

public class MetalArmorBehavior extends AbstractMaterialPartBehavior {
    private final EntityEquipmentSlot slot;

    public MetalArmorBehavior(EntityEquipmentSlot slot) {
        this.slot = slot;
    }

    @Override
    public int getPartMaxDurability(ItemStack itemStack) {
        Material material = getPartMaterial(itemStack);
        ArmorProperty property = material.getProperty(ArmorProperty.KEY);
        return property.getDurability(slot);
    }

    @Override
    public int getItemStackColor(ItemStack itemStack, int tintIndex) {
        Material material = getPartMaterial(itemStack);
        int materialColor = material.getMaterialRGB();
        double dB = (double) (materialColor & 0xFF) / (double) 0xFF;
        double dG = (double) ((materialColor >> 8) & 0xFF) / (double) 0xFF;
        double dR = (double) ((materialColor >> 16) & 0xFF) / (double) 0xFF;
        int ironColor = Materials.Iron.getMaterialRGB();
        double ironB = (double) (ironColor & 0xFF) / (double) 0xFF;
        double ironG = (double) ((ironColor >> 8) & 0xFF) / (double) 0xFF;
        double ironR = (double) ((ironColor >> 16) & 0xFF) / (double) 0xFF;
        return ((int) (Math.min(dB / ironB, 1.0) * 0xFF)) | ((int) (Math.min(dG / ironG, 1.0) * 0xFF) << 8) | ((int) (Math.min(dR / ironR, 1.0) * 0xFF) << 16);
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

    public static void setPartMaterial(ItemStack itemStack, @NotNull Material material) {
        if (!material.hasFlag(MaterialFlags.GENERATE_PLATE)) {
            throw new IllegalArgumentException("Armor material must have a Plate!");
        } else {
            NBTTagCompound compound = getOrCreatePartStatsTag(itemStack);
            compound.setString("Material", material.getRegistryName());
        }
    }
}
