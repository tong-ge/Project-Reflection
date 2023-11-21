package bruce.projectreflection.misc;

import bruce.projectreflection.PRConstants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class UpdateContainerHelper {
    public static void updateContainer(World worldIn, BlockPos posIn, int inventorySize, ItemStackHandler inventory) {
        List<EntityLivingBase> entityList = worldIn.getEntitiesWithinAABB(EntityLivingBase.class,
                new AxisAlignedBB(posIn.add(3, 3, 3), posIn.add(-3, -3, -3)));
        EntityPig entityPig = null;
        if (entityList.isEmpty()) {
            entityPig = new EntityPig(worldIn);
            entityPig.setPosition(0.5 + posIn.getX(), 1.0 + posIn.getY(), 0.5 + posIn.getZ());
            worldIn.spawnEntity(entityPig);
            entityList.add(entityPig);
        }
        for (int s = 0; s < inventorySize; s++) {
            ItemStack stack = inventory.getStackInSlot(s);
            if (stack != null && !stack.isEmpty()) {
                Item item = stack.getItem();
                try {
                    item.onUpdate(stack, worldIn, entityList.get(PRConstants.rand.nextInt(entityList.size())), s, false);
                } catch (Exception e) {
                    if (PRConstants.inDev) {
                        e.printStackTrace();
                    }
                }
                inventory.setStackInSlot(s, stack);
            }
        }
        if (entityPig != null)
            entityPig.setDead();
    }
}
