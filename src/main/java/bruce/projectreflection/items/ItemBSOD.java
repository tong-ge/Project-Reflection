package bruce.projectreflection.items;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemBSOD extends Item {
    public ItemBSOD() {
        setRegistryName(PRConstants.modid, "bsod");
        setCreativeTab(PRConstants.tab);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ProjectReflection.proxy.blueScreenOfDeath();
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
