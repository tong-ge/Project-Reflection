package bruce.projectreflection.items;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.network.MessageClientSpecialAttack;
import bruce.projectreflection.network.PRNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemBSOD extends Item {
    public ItemBSOD() {
        setRegistryName(PRConstants.modid, "bsod");
        setCreativeTab(PRConstants.tab);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (playerIn instanceof EntityPlayerMP) {
            PRNetwork.NETWORK_WRAPPER.sendTo(new MessageClientSpecialAttack(MessageClientSpecialAttack.SpecialAttackType.SLEEP_ATTACK, 30000), (EntityPlayerMP) playerIn);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
