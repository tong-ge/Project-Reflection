package bruce.projectreflection.event;

import bruce.projectreflection.PRConstants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = PRConstants.modid)
public class MiscEvents {

    @SubscribeEvent
    public static void onPlayerAttack(LivingAttackEvent event) {
        System.out.println(event.getSource().getTrueSource());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDrops(LivingDropsEvent event) {
        EntityLivingBase e = event.getEntityLiving();
        if (e.getEntityData().hasKey(PRConstants.TAG_ITEMSTACK_TO_DROP)) {
            event.getDrops().clear();
            NBTTagList list = e.getEntityData().getTagList(PRConstants.TAG_ITEMSTACK_TO_DROP, Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound cmp = list.getCompoundTagAt(i);
                ItemStack stack = new ItemStack(cmp);

                event.getDrops().add(new EntityItem(e.world, e.posX, e.posY, e.posZ, stack));
            }
        }
    }
}
