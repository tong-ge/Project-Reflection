package bruce.projectreflection.items;

import bruce.projectreflection.PRConstants;
//import bruce.projectreflection.items.behaviors.ToolBehaviorIceBall;
import gregtech.api.GTValues;
import gregtech.api.items.toolitem.*;
import gregtech.common.items.ToolItems;
import gregtech.core.sound.GTSoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;

public class PRTools {
    public static IGTTool GRINDER;
    //public static IGTTool ICE_GUN;
    public static IGTTool HELMET;
    public static IGTTool CHESTPLATE;
    public static IGTTool LEGGINGS;
    public static IGTTool BOOTS;

    public static void init() {
        GRINDER = ToolItems.register(ItemGTSword.Builder.of(PRConstants.modid, "grinder")
                .toolStats(b -> b.crafting()
                        .attacking()
                        .attackDamage(3.0f)
                        .attackSpeed(3.0f)
                        .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_LV)
                )
                .oreDict(ToolOreDict.toolFile)
                .secondaryOreDicts("craftingToolFile", "toolGrinder")
                .sound(GTSoundEvents.CUT)
                .toolClasses(ToolClasses.FILE, ToolClasses.SWORD)
                .electric(GTValues.LV)
        );
        /*
        ICE_GUN = ToolItems.register(ItemGTTool.Builder.of(PRConstants.modid, "ice_gun")
                .toolStats(b -> b.attacking()
                        .attackDamage(3.0f)
                        .attackSpeed(-2.0f)
                        .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_HV)
                        .behaviors(ToolBehaviorIceBall.INSTANCE)
                )
                .electric(GTValues.HV)
        );

         */
        HELMET = ToolItems.register(ItemMetalArmor.Builder.of(EntityEquipmentSlot.HEAD, PRConstants.modid, "helmet").toolStats(b -> b));
        CHESTPLATE = ToolItems.register(ItemMetalArmor.Builder.of(EntityEquipmentSlot.CHEST, PRConstants.modid, "chestplate").toolStats(b -> b));
        LEGGINGS = ToolItems.register(ItemMetalArmor.Builder.of(EntityEquipmentSlot.LEGS, PRConstants.modid, "leggings").toolStats(b -> b));
        BOOTS = ToolItems.register(ItemMetalArmor.Builder.of(EntityEquipmentSlot.FEET, PRConstants.modid, "boots").toolStats(b -> b));
    }
}
