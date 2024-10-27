package bruce.projectreflection.items;

import bruce.projectreflection.PRConstants;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import gregtech.api.items.toolitem.IGTTool;
import gregtech.api.items.toolitem.IGTToolDefinition;
import gregtech.api.items.toolitem.ToolBuilder;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.ToolProperty;
import gregtech.api.util.LocalizationUtils;
import gregtech.api.util.TextFormattingUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
public class ItemMetalArmor extends ItemArmor implements IGTTool {
    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
            UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
            UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
            UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    public static final String ARMOR_VALUE_KEY = "ArmorValue";
    private final String domain;
    private final String id;
    private final int tier;
    private final IGTToolDefinition toolStats;
    private final Supplier<ItemStack> markerItem;

    public ItemMetalArmor(EntityEquipmentSlot equipmentSlotIn,
                          String domain,
                          String id,
                          int tier,
                          IGTToolDefinition toolStats,
                          Supplier<ItemStack> markerItem) {
        super(ArmorMaterial.IRON, 0, equipmentSlotIn);
        this.domain = domain;
        this.id = id;
        this.tier = tier;
        this.toolStats = toolStats;
        this.markerItem = markerItem;
        this.setMaxStackSize(1);
        this.setCreativeTab(PRConstants.tab);
        this.setTranslationKey("pr.armor." + id + ".name");
        this.setRegistryName(domain, id);
    }

    public boolean hasColor(ItemStack stack) {
        ToolProperty toolProperty = this.getToolProperty(stack);
        return toolProperty != null;
    }

    /**
     * Return the color for the specified armor ItemStack.
     */
    public int getColor(ItemStack stack) {
        if (!this.hasColor(stack)) {
            return 16777215;
        } else {
            Material material = this.getToolMaterial(stack);
            return material.getMaterialRGB();
        }
    }

    @Override
    public String getDomain() {
        return this.domain;
    }

    @Override
    public String getToolId() {
        return this.id;
    }

    @Override
    public boolean isElectric() {
        return tier > -1;
    }

    @Override
    public int getElectricTier() {
        return tier;
    }

    @Override
    public IGTToolDefinition getToolStats() {
        return toolStats;
    }

    @Override
    public @Nullable SoundEvent getSound() {
        return null;
    }

    @Override
    public boolean playSoundOnBlockDestroy() {
        return false;
    }

    @Override
    public @Nullable String getOreDictName() {
        return null;
    }

    @Override
    public @NotNull List<String> getSecondaryOreDicts() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public @Nullable Supplier<ItemStack> getMarkerItem() {
        return markerItem;
    }

    public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            this.definition$getSubItems(items);
        }

    }

    public @NotNull String getItemStackDisplayName(@NotNull ItemStack stack) {
        return LocalizationUtils.format(this.getTranslationKey(), this.getToolMaterial(stack).getLocalizedName());
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return getTotalEnchantability(stack);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return definition$getIsRepairable(toRepair, repair);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, "metal", (slot == EntityEquipmentSlot.LEGS ? 2 : 1), type == null ? "" : String.format("_%s", type));
    }

    @Override
    public boolean hasOverlay(ItemStack stack) {
        return super.hasOverlay(stack);
    }

    private double getArmorValue(EntityEquipmentSlot slot, ItemStack stack) {
        NBTTagCompound toolTag = getToolTag(stack);
        if (toolTag.hasKey(ARMOR_VALUE_KEY, Constants.NBT.TAG_FLOAT)) {
            return toolTag.getDouble(ARMOR_VALUE_KEY);
        }
        double armorValue = (double) ArmorMaterial.IRON.getDamageReductionAmount(slot)
                * getMaterialAttackDamage(stack)
                / ToolMaterial.IRON.getAttackDamage();

        toolTag.setDouble(ARMOR_VALUE_KEY, armorValue);
        return armorValue;
    }

    private double getArmorToughness(EntityEquipmentSlot slot, ItemStack stack) {
        return Math.max(0.0, getTotalHarvestLevel(stack) - 1);
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

    public int getDamage(@NotNull ItemStack stack) {
        return this.definition$getDamage(stack);
    }

    public int getMaxDamage(@NotNull ItemStack stack) {
        return this.definition$getMaxDamage(stack);
    }

    public void setDamage(@NotNull ItemStack stack, int damage) {
        this.definition$setDamage(stack, damage);
    }

    public boolean showDurabilityBar(@NotNull ItemStack stack) {
        return false;
    }

    public double getDurabilityForDisplay(@NotNull ItemStack stack) {
        return this.definition$getDurabilityForDisplay(stack);
    }

    public static class Builder extends ToolBuilder<ItemMetalArmor> {
        private final EntityEquipmentSlot slot;

        @NotNull
        public static ItemMetalArmor.Builder of(@NotNull EntityEquipmentSlot slot, @NotNull String domain, @NotNull String id) {
            return new ItemMetalArmor.Builder(slot, domain, id);
        }

        public Builder(@NotNull EntityEquipmentSlot slot, @NotNull String domain, @NotNull String id) {
            super(domain, id);
            this.slot = slot;
        }

        public Supplier<ItemMetalArmor> supply() {
            return () -> {
                return new ItemMetalArmor(slot, this.domain, this.id, this.tier, this.toolStats, this.markerItem);
            };
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        definition$addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(I18n.format("item.projectreflection.armor.tooltip.armor", TextFormattingUtil.formatNumbers(getArmorValue(this.armorType, stack))));
        tooltip.add(I18n.format("item.projectreflection.armor.tooltip.toughness", TextFormattingUtil.formatNumbers(getArmorToughness(this.armorType, stack))));
    }
}
