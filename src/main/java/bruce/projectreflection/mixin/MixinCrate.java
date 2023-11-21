package bruce.projectreflection.mixin;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.misc.UpdateContainerHelper;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.metatileentities.storage.MetaTileEntityCrate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.List;

@Mixin(value = MetaTileEntityCrate.class, remap = false)
public abstract class MixinCrate extends MetaTileEntity {
    @Shadow
    @Final
    private int inventorySize;
    @Shadow
    private ItemStackHandler inventory;

    private MixinCrate(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
    }

    @Override
    public void update() {
        super.update();
        UpdateContainerHelper.updateContainer(getWorld(), getPos(), inventorySize, inventory);
    }
}
