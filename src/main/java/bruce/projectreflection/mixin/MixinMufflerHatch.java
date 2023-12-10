package bruce.projectreflection.mixin;

import gregtech.api.items.itemhandlers.GTItemStackHandler;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMufflerHatch;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.api.aura.AuraHelper;

import java.util.List;

@Mixin(value = MetaTileEntityMufflerHatch.class,remap = false)
public abstract class MixinMufflerHatch extends MetaTileEntityMultiblockPart {
    @Final
    @Shadow
    private int recoveryChance;
    @Final
    @Shadow
    private GTItemStackHandler inventory;
    private MixinMufflerHatch(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId, tier);
    }

    @Inject(method = "recoverItemsTable",at = @At("HEAD"))
    private void beforeRecover(List<ItemStack> recoveryItems, CallbackInfo ci)
    {
        int stacksize=0;
        for(ItemStack stack:recoveryItems)
        {
            stacksize+=stack.getCount();
        }
        float pollution=(100.0f - (float)recoveryChance)/100.0f*(float)stacksize;
        AuraHelper.polluteAura(getWorld(),getPos(),pollution,false);
        //ProjectReflection.logger.info("Added pollution {} on {}",pollution,getWorld().getWorldTime());
    }

    /**
    @ModifyArg(method = "recoverItemsTable",
            at=@At(
                    value = "INVOKE",
                    target = "Lgregtech/api/util/GTTransferUtils;insertItem(Lnet/minecraftforge/items/IItemHandler;Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/item/ItemStack;"
            )
    )
    private ItemStack fixAshMath(ItemStack ash)
    {
        return ash.copy();
    }

     */
    @Inject(method = "checkFrontFaceFree",at=@At("HEAD"),cancellable = true)
    private void checkInventoryBlocked(CallbackInfoReturnable<Boolean> cir)
    {
        int slots=inventory.getSlots();
        for(int i=0;i<slots;i++)
        {
            ItemStack stack=inventory.getStackInSlot(i);
            if(stack.getCount() < stack.getMaxStackSize())
            {
                return;
            }
        }
        cir.setReturnValue(false);
    }
}
