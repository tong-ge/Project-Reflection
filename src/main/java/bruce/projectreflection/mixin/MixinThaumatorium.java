package bruce.projectreflection.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.tiles.TileThaumcraftInventory;
import thaumcraft.common.tiles.crafting.TileThaumatorium;

import java.util.ArrayList;

@Mixin(value = TileThaumatorium.class, remap = false)
public abstract class MixinThaumatorium extends TileThaumcraftInventory implements ITickable {
    @Shadow
    public ArrayList<String> recipePlayer;
    @Shadow
    public int currentCraft;

    private MixinThaumatorium(int size) {
        super(size);
    }

    @Inject(method = "update", at = @At("HEAD"), cancellable = true, remap = true)
    private void pre_update(CallbackInfo ci) {
        if (this.currentCraft < 0)
            return;
        EntityPlayer p = this.world.getPlayerEntityByName(this.recipePlayer.get(this.currentCraft));
        if (p == null || p.getDistanceSq(this.pos) > 64) {
            if (!this.world.isRemote) {
                ci.cancel();
            }
        }
    }
}
