package bruce.projectreflection.materials.properties;

import gregtech.api.unification.material.properties.IMaterialProperty;
import gregtech.api.unification.material.properties.MaterialProperties;
import gregtech.api.unification.material.properties.PropertyKey;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ArmorProperty implements IMaterialProperty {
    public static PropertyKey<ArmorProperty> KEY = new PropertyKey<>("armor", ArmorProperty.class);
    private final double[] armorValue;
    private final double[] toughness;
    private final int[] durability;
    private int enchantability;

    public ArmorProperty(double[] armorValue, int[] durability, double... toughness) {
        this.armorValue = armorValue;
        this.durability = durability;
        switch (toughness.length) {
            case 0:
                this.toughness = new double[4];
                break;
            case 1:
                this.toughness = new double[]{toughness[0], toughness[0], toughness[0], toughness[0]};
                break;
            default:
                this.toughness = toughness;
        }
        this.enchantability = 10;
    }

    public ArmorProperty(double[] armorValue, int durability, double... toughness) {
        this(armorValue, new int[]{durability, durability, durability, durability}, toughness);
    }

    public ArmorProperty(double totalArmorValue, int durability, double... toughness) {
        this(new double[]{totalArmorValue * 0.13, totalArmorValue * 0.33, totalArmorValue * 0.4, totalArmorValue * 0.14}, durability, toughness);
    }

    @Override
    public void verifyProperty(MaterialProperties materialProperties) {
        materialProperties.ensureSet(PropertyKey.INGOT, true);
    }

    public double getArmorValue(EntityEquipmentSlot slot) {
        return armorValue[slot.getIndex()];
    }

    public double getArmorToughness(EntityEquipmentSlot slot) {
        return toughness[slot.getIndex()];
    }

    public int getDurability(EntityEquipmentSlot slot) {
        return durability[slot.getIndex()];
    }

    public ArmorProperty setEnchantability(int enchantability) {
        this.enchantability = enchantability;
        return this;
    }

    public int getEnchantability() {
        return enchantability;
    }
}
