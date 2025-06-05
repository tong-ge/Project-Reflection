package bruce.projectreflection.capability.recipelogic;

import bruce.projectreflection.metatileentity.MteHandCrank;
import gregtech.api.GTValues;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public class RecipeLogicHandCrank extends AbstractRecipeLogic {
    private int euStored;
    private static final int EU_CAPACITY = 250;
    private static final int KINETIC_ENERGY_PER_CLICK = 100;

    public RecipeLogicHandCrank(MetaTileEntity tileEntity, RecipeMap<?> recipeMap) {
        super(tileEntity, recipeMap);
        euStored = 0;
    }

    @Override
    protected long getEnergyInputPerSecond() {
        return 0;
    }

    @Override
    protected long getEnergyStored() {
        return euStored;
    }

    @Override
    protected long getEnergyCapacity() {
        return EU_CAPACITY;
    }

    @Override
    protected boolean drawEnergy(int amount, boolean simulate) {
        if (amount > getEnergyStored())
            return false;
        if (!simulate)
            euStored -= amount;
        return true;
    }

    @Override
    public long getMaxVoltage() {
        return GTValues.V[GTValues.LV];
    }

    public boolean canBeClicked(EntityPlayer player) {
        if (player.isSneaking())
            return false;
        if (player.getFoodStats().getFoodLevel() <= 6) {
            return false;
        }
        if (player instanceof FakePlayer) {
            return false;
        }
        if (this.isActive()) {
            return this.hasNotEnoughEnergy;
        }
        Recipe recipe = this.findRecipe(this.getMaxVoltage(), this.getInputInventory(), this.getInputTank());
        if (recipe == null) {
            return false;
        }
        MteHandCrank controller = (MteHandCrank) this.metaTileEntity;
        return controller.checkRecipe(recipe, false);
    }

    public void onClick(EntityPlayer player) {
        if (!(player instanceof EntityPlayerMP))
            return;
        player.getEntityWorld().playSound(null, this.metaTileEntity.getPos(), this.getRecipeMap().getSound(),
                SoundCategory.BLOCKS, 1.0f, 1.0f);
        int totalEnergy = this.euStored + KINETIC_ENERGY_PER_CLICK;
        float damage = 0.05f * (totalEnergy - EU_CAPACITY);
        if (damage > 0) {
            this.euStored = EU_CAPACITY;
            if (this.isActive)
                player.attackEntityFrom(DamageSource.GENERIC, damage);
        } else {
            this.euStored = totalEnergy;
            player.addExhaustion(0.25f);
        }

    }

    public boolean checkRecipe(@NotNull Recipe recipe) {
        MteHandCrank controller = (MteHandCrank) this.metaTileEntity;
        if (controller.checkRecipe(recipe, false)) {
            //controller.checkRecipe(recipe, true);
            return super.checkRecipe(recipe);
        } else {
            return false;
        }
    }

    @Override
    public void deserializeNBT(@NotNull NBTTagCompound compound) {
        super.deserializeNBT(compound);
        euStored = compound.getInteger("Energy");
    }

    @Override
    public @NotNull NBTTagCompound serializeNBT() {
        NBTTagCompound compound = super.serializeNBT();
        compound.setInteger("Energy", euStored);
        return compound;
    }

    @Override
    protected void decreaseProgress() {
    }

    @Override
    protected boolean setupAndConsumeRecipeInputs(@NotNull Recipe recipe, @NotNull IItemHandlerModifiable importInventory, @NotNull IMultipleTankHandler importFluids) {
        if (super.setupAndConsumeRecipeInputs(recipe, importInventory, importFluids)) {
            MteHandCrank controller = (MteHandCrank) this.metaTileEntity;
            return controller.checkRecipe(recipe, true);
        }
        return false;
    }
}
