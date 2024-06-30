package bruce.projectreflection.items.behaviors;

import bruce.projectreflection.misc.MathUtils;
import com.gildedgames.the_aether.entities.projectile.crystals.EntityIceyBall;
import gregtech.api.items.toolitem.behavior.IToolBehavior;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ToolBehaviorIceBall implements IToolBehavior {
    public static IToolBehavior INSTANCE = new ToolBehaviorIceBall();

    private ToolBehaviorIceBall() {
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        Vec3d vec3d = MathUtils.getVectorForRotation(Math.min(player.rotationPitch, 21.0f), player.rotationYaw);
        if (!player.capabilities.isCreativeMode) {
            stack.damageItem(1, player);
        }
        if (!world.isRemote) {
            EntityIceyBall ball = new EntityIceyBall(world, player.posX, player.posY + player.eyeHeight, player.posZ, false);
            ball.shootingEntity = player;
            ball.smotionX = vec3d.x * 1.5;
            ball.smotionY = vec3d.y * 1.5;
            ball.smotionZ = vec3d.z * 1.5;
            ball.smacked = true;
            world.spawnEntity(ball);
            player.getCooldownTracker().setCooldown(stack.getItem(), (int) Math.ceil(20.0 / player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getAttributeValue()));
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }
}
