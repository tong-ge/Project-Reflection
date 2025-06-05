package bruce.projectreflection.event;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.PRLabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Random;

@Mod.EventBusSubscriber(modid = PRConstants.modid)
public class MiscEvents {
    private static final Random distrRandom = new Random();

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
