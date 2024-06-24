package bruce.projectreflection.metatileentity.multis.multiblockpart;

import bruce.projectreflection.PRAbility;
import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.capability.GregtechDataCodes;
import gregtech.api.capability.IRotorHolder;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.damagesources.DamageSources;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.resources.IGuiTexture;
import gregtech.api.metatileentity.ITieredMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockWithDisplayBase;
import gregtech.api.util.RelativeDirection;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.items.behaviors.AbstractMaterialPartBehavior;
import gregtech.common.items.behaviors.TurbineRotorBehavior;
import gregtech.common.metatileentities.multi.electric.generator.MetaTileEntityLargeTurbine;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockNotifiablePart;
import gregtech.core.advancement.AdvancementTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MetaTileEntityLLRotorHolder extends MetaTileEntityMultiblockNotifiablePart implements IMultiblockAbilityPart<IRotorHolder>, IRotorHolder {
    private final InventoryRotorHolder inventory;
    private final int maxSpeed;
    private int currentSpeed;
    private int rotorColor = -1;
    private boolean isRotorSpinning;
    private boolean frontFaceFree;

    public MetaTileEntityLLRotorHolder(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId, tier, false);
        this.inventory = new InventoryRotorHolder(this);
        this.maxSpeed = 2000 + 1000 * tier;
    }

    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityLLRotorHolder(this.metaTileEntityId, this.getTier());
    }

    public IItemHandlerModifiable getImportItems() {
        return this.inventory;
    }

    protected ModularUI createUI(@NotNull EntityPlayer entityPlayer) {
        return ModularUI.defaultBuilder().label(6, 6, this.getMetaFullName()).slot(this.inventory, 0, 79, 36, GuiTextures.SLOT, GuiTextures.TURBINE_OVERLAY).bindPlayerInventory(entityPlayer.inventory).build(this.getHolder(), entityPlayer);
    }

    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("gregtech.machine.rotor_holder.tooltip1"));
        tooltip.add(I18n.format("gregtech.machine.rotor_holder.tooltip2"));
        tooltip.add(I18n.format("gregtech.universal.disabled"));
    }

    public void addToolUsages(ItemStack stack, @Nullable World world, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("gregtech.tool_action.screwdriver.access_covers"));
        tooltip.add(I18n.format("gregtech.tool_action.wrench.set_facing"));
        super.addToolUsages(stack, world, tooltip, advanced);
    }

    public MultiblockAbility<IRotorHolder> getAbility() {
        return PRAbility.LL_ROTOR_HOLDER;
    }

    public void update() {
        super.update();
        if (!this.getWorld().isRemote) {
            if (this.getOffsetTimer() % 20L == 0L) {
                boolean isFrontFree = this.checkTurbineFaceFree();
                if (isFrontFree != this.frontFaceFree) {
                    this.frontFaceFree = isFrontFree;
                    this.writeCustomData(GregtechDataCodes.FRONT_FACE_FREE, (buf) -> {
                        buf.writeBoolean(this.frontFaceFree);
                    });
                }
            }

            MultiblockWithDisplayBase controller = (MultiblockWithDisplayBase) this.getController();
            if (controller != null && controller.isActive()) {
                if (this.currentSpeed < this.maxSpeed) {
                    this.setCurrentSpeed(this.currentSpeed + 1);
                }

                if (this.getOffsetTimer() % 20L == 0L) {
                    this.damageRotor(1 + controller.getNumMaintenanceProblems());
                }
            } else if (!this.hasRotor()) {
                this.setCurrentSpeed(0);
            } else if (this.currentSpeed > 0) {
                this.setCurrentSpeed(Math.max(0, this.currentSpeed - 3));
            }

        }
    }

    void setCurrentSpeed(int speed) {
        if (this.currentSpeed != speed) {
            this.currentSpeed = speed;
            this.setRotorSpinning(this.currentSpeed > 0);
            this.markDirty();
        }

    }

    void setRotorSpinning(boolean spinning) {
        if (this.isRotorSpinning != spinning) {
            this.isRotorSpinning = spinning;
            this.writeCustomData(GregtechDataCodes.IS_ROTOR_LOOPING, (buf) -> {
                buf.writeBoolean(this.isRotorSpinning);
            });
        }

    }

    public void registerAbilities(@NotNull List<IRotorHolder> abilityList) {
        abilityList.add(this);
    }

    public boolean canPartShare() {
        return false;
    }

    public boolean isFrontFaceFree() {
        return this.frontFaceFree;
    }

    private boolean checkTurbineFaceFree() {
        EnumFacing front = this.getFrontFacing();
        EnumFacing upwards = front.getAxis() == EnumFacing.Axis.Y ? EnumFacing.NORTH : EnumFacing.UP;

        for (int left = -1; left <= 1; ++left) {
            for (int up = -1; up <= 1; ++up) {
                BlockPos checkPos = RelativeDirection.offsetPos(this.getPos(), front, upwards, false, up, left, 1);
                IBlockState state = this.getWorld().getBlockState(checkPos);
                if (!state.getBlock().isAir(state, this.getWorld(), checkPos)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean onRotorHolderInteract(@NotNull EntityPlayer player) {
        if (player.isCreative()) {
            return false;
        } else if (!this.getWorld().isRemote && this.isRotorSpinning) {
            float damageApplied = (float) Math.min(1, this.currentSpeed / 1000);
            player.attackEntityFrom(DamageSources.getTurbineDamage(), damageApplied);
            AdvancementTriggers.ROTOR_HOLDER_DEATH.trigger((EntityPlayerMP) player);
            return true;
        } else {
            return this.isRotorSpinning;
        }
    }

    public boolean hasRotor() {
        return this.rotorColor != -1;
    }

    protected void setRotorColor(int color) {
        this.rotorColor = color;
    }

    protected int getRotorColor() {
        return this.rotorColor;
    }

    public int getRotorSpeed() {
        return this.currentSpeed;
    }

    public int getRotorEfficiency() {
        return this.inventory.getRotorEfficiency();
    }

    public int getRotorPower() {
        return this.inventory.getRotorPower();
    }

    public int getRotorDurabilityPercent() {
        return this.inventory.getRotorDurabilityPercent();
    }

    public void damageRotor(int amount) {
        this.inventory.damageRotor(amount);
    }

    public int getMaxRotorHolderSpeed() {
        return this.maxSpeed;
    }

    public int getHolderPowerMultiplier() {
        int tierDifference = this.getTierDifference();
        return tierDifference == -1 ? -1 : (int) Math.pow(2.0, this.getTierDifference());
    }

    public int getHolderEfficiency() {
        int tierDifference = this.getTierDifference();
        return tierDifference == -1 ? -1 : 100 + 10 * tierDifference;
    }

    private int getTierDifference() {
        return this.getController() instanceof ITieredMetaTileEntity ? this.getTier() - ((ITieredMetaTileEntity) this.getController()).getTier() : -1;
    }

    public boolean onRightClick(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, CuboidRayTraceResult hitResult) {
        return this.onRotorHolderInteract(playerIn) || super.onRightClick(playerIn, hand, facing, hitResult);
    }

    public boolean onWrenchClick(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, CuboidRayTraceResult hitResult) {
        return this.onRotorHolderInteract(playerIn) || super.onWrenchClick(playerIn, hand, facing, hitResult);
    }

    public boolean onScrewdriverClick(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, CuboidRayTraceResult hitResult) {
        return this.onRotorHolderInteract(playerIn);
    }

    public void onLeftClick(EntityPlayer player, EnumFacing facing, CuboidRayTraceResult hitResult) {
        this.onRotorHolderInteract(player);
    }

    public void clearMachineInventory(NonNullList<ItemStack> itemBuffer) {
        super.clearMachineInventory(itemBuffer);
        clearInventory(itemBuffer, this.inventory);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setTag("inventory", this.inventory.serializeNBT());
        data.setInteger("currentSpeed", this.currentSpeed);
        data.setBoolean("Spinning", this.isRotorSpinning);
        data.setBoolean("FrontFree", this.frontFaceFree);
        return data;
    }

    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.inventory.deserializeNBT(data.getCompoundTag("inventory"));
        this.currentSpeed = data.getInteger("currentSpeed");
        this.isRotorSpinning = data.getBoolean("Spinning");
        this.frontFaceFree = data.getBoolean("FrontFree");
    }

    public void receiveCustomData(int dataId, PacketBuffer buf) {
        super.receiveCustomData(dataId, buf);
        if (dataId == GregtechDataCodes.IS_ROTOR_LOOPING) {
            this.isRotorSpinning = buf.readBoolean();
            this.scheduleRenderUpdate();
        } else if (dataId == GregtechDataCodes.FRONT_FACE_FREE) {
            this.frontFaceFree = buf.readBoolean();
        }

    }

    public void writeInitialSyncData(PacketBuffer buf) {
        super.writeInitialSyncData(buf);
        buf.writeBoolean(this.isRotorSpinning);
        buf.writeInt(this.rotorColor);
        buf.writeBoolean(this.frontFaceFree);
    }

    public void receiveInitialSyncData(PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        this.isRotorSpinning = buf.readBoolean();
        this.rotorColor = buf.readInt();
        this.frontFaceFree = buf.readBoolean();
        this.scheduleRenderUpdate();
    }

    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        Textures.ROTOR_HOLDER_OVERLAY.renderSided(this.getFrontFacing(), renderState, translation, pipeline);
        Textures.LARGE_TURBINE_ROTOR_RENDERER.renderSided(renderState, translation, pipeline, this.getFrontFacing(), this.getController() != null, this.hasRotor(), this.isRotorSpinning, this.getRotorColor());
    }

    private static class InventoryRotorHolder extends NotifiableItemStackHandler {
        private final MetaTileEntityLLRotorHolder mte;

        public InventoryRotorHolder(MetaTileEntityLLRotorHolder mte) {
            super(mte, 1, null, false);
            this.mte = mte;
        }

        public int getSlotLimit(int slot) {
            return 1;
        }

        protected void onLoad() {
            mte.rotorColor = this.getRotorColor();
        }

        public void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            mte.setRotorColor(this.getRotorColor());
            mte.scheduleRenderUpdate();
        }

        private @Nullable ItemStack getTurbineStack() {
            return !this.hasRotor() ? null : this.getStackInSlot(0);
        }

        private @Nullable TurbineRotorBehavior getTurbineBehavior() {
            ItemStack stack = this.getStackInSlot(0);
            return stack.isEmpty() ? null : TurbineRotorBehavior.getInstanceFor(stack);
        }

        private boolean hasRotor() {
            return this.getTurbineBehavior() != null;
        }

        private int getRotorColor() {
            if (!this.hasRotor()) {
                return -1;
            } else {
                this.getTurbineBehavior();
                return TurbineRotorBehavior.getPartMaterial(this.getStackInSlot(0)).getMaterialRGB();
            }
        }

        private int getRotorDurabilityPercent() {
            return !this.hasRotor() ? 0 : this.getTurbineBehavior().getRotorDurabilityPercent(this.getStackInSlot(0));
        }

        private int getRotorEfficiency() {
            if (!this.hasRotor()) {
                return -1;
            } else {
                this.getTurbineBehavior();
                return TurbineRotorBehavior.getRotorEfficiency(this.getTurbineStack());
            }
        }

        private int getRotorPower() {
            if (!this.hasRotor()) {
                return -1;
            } else {
                this.getTurbineBehavior();
                return TurbineRotorBehavior.getRotorPower(this.getTurbineStack());
            }
        }

        private void damageRotor(int damageAmount) {
            if (this.hasRotor()) {
                if (this.getTurbineBehavior().getPartMaxDurability(this.getTurbineStack()) <= AbstractMaterialPartBehavior.getPartDamage(this.getTurbineStack()) + damageAmount) {
                    MultiblockRecipeLogic holder = (MultiblockRecipeLogic) mte.getController().getRecipeLogic();
                    if (holder != null && holder.isWorking()) {
                        holder.invalidate();
                    }
                }

                this.getTurbineBehavior().applyRotorDamage(this.getStackInSlot(0), damageAmount);
            }
        }

        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return TurbineRotorBehavior.getInstanceFor(stack) != null && super.isItemValid(slot, stack);
        }

        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack itemStack = super.extractItem(slot, amount, simulate);
            if (!simulate && itemStack != ItemStack.EMPTY) {
                mte.setRotorColor(-1);
            }

            return itemStack;
        }
    }
}
