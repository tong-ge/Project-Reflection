package bruce.projectreflection.event;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.PRLabs;
import bruce.projectreflection.misc.ItemUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

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

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == Side.SERVER) {
            switch (event.phase) {
                case START: {
                    EntityPlayer player = event.player;
                    if (player != null) {
                        NBTTagCompound nbt = player.getEntityData();
                        distrRandom.setSeed(nbt.getLong("UUIDLeast"));
                        if (player.world.getTotalWorldTime() % 20 == distrRandom.nextInt(20)) {
                            long startTime = System.currentTimeMillis();
                            player.writeEntityToNBT(nbt);//回写
                            long endTime = System.currentTimeMillis();
                            long timeDiff = endTime - startTime;
                            if (PRConstants.inDev) {
                                PRLabs.logger.info("This check took {}/50 ms", timeDiff);
                            }
                            float damage = ((float) timeDiff) / 50f;
                            if (damage >= 0.9f) {
                                player.attackEntityFrom(DamageSource.MAGIC, damage);
                            }
                            player.addExhaustion(damage);
                        }
                    }
                    break;
                }
                case END: {
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Default phase");
                }
            }
        } else {
            //TODO client-side logic
        }
    }
}
