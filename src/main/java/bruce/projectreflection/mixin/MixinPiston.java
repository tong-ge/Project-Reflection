package bruce.projectreflection.mixin;

import net.minecraft.block.BlockPistonBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockPistonBase.class)
public abstract class MixinPiston {
    @Inject(method = "doMove", at = @At("HEAD"))
    private void preMoveBlocks(World world, BlockPos pos, EnumFacing dir, boolean extending, CallbackInfo ci) {
        if (!world.isRemote) {

        }
    }
}
