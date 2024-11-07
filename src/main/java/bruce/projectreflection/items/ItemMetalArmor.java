package bruce.projectreflection.items;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.items.behaviors.MetalArmorBehavior;
import bruce.projectreflection.materials.properties.PropertyArmor;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import gregtech.api.items.armor.ArmorMetaItem;
import gregtech.api.items.armor.IArmorLogic;
import gregtech.api.items.toolitem.IGTTool;
import gregtech.api.items.toolitem.IGTToolDefinition;
import gregtech.api.items.toolitem.ToolBuilder;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.material.properties.ToolProperty;
import gregtech.api.util.LocalizationUtils;
import gregtech.api.util.TextFormattingUtil;
import gregtech.common.items.behaviors.AbstractMaterialPartBehavior;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static gregtech.api.items.toolitem.ToolHelper.getToolTag;

/**
 * @author tong-ge
 */
public class ItemMetalArmor extends ArmorMetaItem<ArmorMetaItem<?>.ArmorMetaValueItem> {
    public static ItemMetalArmor INSTANCE = new ItemMetalArmor();

    private ItemMetalArmor() {
        setRegistryName(PRConstants.modid, "pr_meta_armor");
        setCreativeTab(PRConstants.tab);
    }

    public static ArmorMetaItem<?>.ArmorMetaValueItem HELMET;
    public static ArmorMetaItem<?>.ArmorMetaValueItem CHESTPLATE;
    public static ArmorMetaItem<?>.ArmorMetaValueItem LEGGINGS;
    public static ArmorMetaItem<?>.ArmorMetaValueItem BOOTS;

    @Override
    public void registerSubItems() {
        HELMET = this.addItem(1, "helmet").setArmorLogic(new Logic(EntityEquipmentSlot.HEAD));
        CHESTPLATE = this.addItem(2, "chestplate").setArmorLogic(new Logic(EntityEquipmentSlot.CHEST));
        LEGGINGS = this.addItem(3, "leggings").setArmorLogic(new Logic(EntityEquipmentSlot.LEGS));
        BOOTS = this.addItem(4, "boots").setArmorLogic(new Logic(EntityEquipmentSlot.FEET));
    }

    private static class Logic implements IArmorLogic {
        private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
                UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
                UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
                UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
        private final EntityEquipmentSlot armorType;
        private final MetalArmorBehavior behavior;

        public Logic(EntityEquipmentSlot slot) {
            this.armorType = slot;
            behavior = new MetalArmorBehavior(slot);
        }

        @Override
        public void addToolComponents(ArmorMetaItem.ArmorMetaValueItem metaValueItem) {
            metaValueItem.addComponents(behavior);
        }

        @Override
        public EntityEquipmentSlot getEquipmentSlot(ItemStack itemStack) {
            return armorType;
        }

        @Override
        public boolean canBreakWithDamage(ItemStack stack) {
            return true;
        }

        @Override
        public void damageArmor(EntityLivingBase entity, ItemStack itemStack, DamageSource source, int damage, EntityEquipmentSlot equipmentSlot) {
            behavior.applyArmorDamage(itemStack, damage);
        }

        @Override
        public @NotNull Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
            Multimap<String, AttributeModifier> multimap = HashMultimap.create();

            if (slot == this.armorType) {
                multimap.put(SharedMonsterAttributes.ARMOR.getName(),
                        new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier",
                                getArmorValue(slot, stack), 0));
                multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(),
                        new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor toughness",
                                getArmorToughness(slot, stack), 0));
            }

            return multimap;
        }

        private double getArmorValue(EntityEquipmentSlot slot, ItemStack stack) {
            PropertyArmor property = AbstractMaterialPartBehavior.getPartMaterial(stack).getProperty(PropertyArmor.KEY);
            return property.getArmorValue(slot);
        }

        private double getArmorToughness(EntityEquipmentSlot slot, ItemStack stack) {
            PropertyArmor property = AbstractMaterialPartBehavior.getPartMaterial(stack).getProperty(PropertyArmor.KEY);
            return property.getArmorToughness(slot);
        }

        @Override
        public String getArmorTexture(ItemStack itemStack, Entity entity, EntityEquipmentSlot entityEquipmentSlot, String s) {
            return String.format("projectreflection:textures/models/armor/%s_layer_%d%s.png", "metal", (entityEquipmentSlot == EntityEquipmentSlot.LEGS ? 2 : 1), s == null ? "" : s);
        }
    }
}
