package bruce.projectreflection.metatileentity.primitive;

import bruce.projectreflection.api.IHeatCapability;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.GTValues;
import gregtech.api.capability.GregtechDataCodes;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.metatileentity.IDataInfoProvider;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.util.TextFormattingUtil;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static gregtech.api.capability.GregtechDataCodes.IS_WORKING;

public class MetaTileEntityFirebox extends MetaTileEntityMultiblockPart implements IDataInfoProvider, IMultiblockAbilityPart<IHeatCapability>, IHeatCapability {
    private int timeBeforeCoolingDown;

    @Override
    public IItemHandlerModifiable createImportItemHandler() {
        return new ItemStackHandler(1) {
            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (TileEntityFurnace.getItemBurnTime(stack) <= 0)
                    return stack;
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private int fuelBurnTimeLeft;
    private int fuelMaxBurnTime;
    private int currentTemperature;
    private boolean isBurning;
    private boolean wasBurningAndNeedsUpdate;

    public MetaTileEntityFirebox(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId, tier);
        //this.containerInventory = new ItemStackHandler(1);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityFirebox(this.metaTileEntityId, this.getTier());
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        ModularUI.Builder builder = ModularUI.builder(GuiTextures.BACKGROUND, 176, 148)
                .label(10, 5, getMetaFullName()).slot(importItems, 0,
                        79, 36, true, true, GuiTextures.SLOT)
                .progressBar(this::getFuelLeftPercent, 79, 18, 18, 18,
                        GuiTextures.PROGRESS_BAR_BOILER_FUEL.get(true), ProgressWidget.MoveType.VERTICAL)
                .bindPlayerInventory(entityPlayer.inventory, GuiTextures.SLOT, 7, 66);
        return builder.build(getHolder(), entityPlayer);
    }

    protected void tryConsumeNewFuel() {
        ItemStack fuelInSlot = importItems.extractItem(0, 1, true);
        if (fuelInSlot.isEmpty()) return;
        // Prevent consuming buckets with burn time
        if (FluidUtil.getFluidHandler(fuelInSlot) != null) {
            return;
        }
        int burnTime = TileEntityFurnace.getItemBurnTime(fuelInSlot);
        if (burnTime <= 0) return;
        importItems.extractItem(0, 1, false);
        setFuelMaxBurnTime(burnTime);
    }

    public void setFuelMaxBurnTime(int fuelMaxBurnTime) {
        this.fuelMaxBurnTime = fuelMaxBurnTime;
        this.fuelBurnTimeLeft = fuelMaxBurnTime;
        if (!getWorld().isRemote) {
            markDirty();
        }
    }

    private void updateCurrentTemperature() {
        if (fuelMaxBurnTime > 0) {
            if (getOffsetTimer() % 12 == 0) {
                if (fuelBurnTimeLeft % 2 == 0 && currentTemperature < getMaxTemperature())
                    currentTemperature++;
                fuelBurnTimeLeft -= (1 + this.getTier());
                if (fuelBurnTimeLeft <= 0) {
                    this.fuelBurnTimeLeft = 0;
                    this.fuelMaxBurnTime = 0;
                    this.timeBeforeCoolingDown = getCooldownInterval();
                    //boiler has no fuel now, so queue burning state update
                    this.wasBurningAndNeedsUpdate = true;
                }
            }
        } else if (timeBeforeCoolingDown == 0) {
            if (currentTemperature > 0) {
                currentTemperature -= getCoolDownRate();
                timeBeforeCoolingDown = getCooldownInterval();
            }
        } else --timeBeforeCoolingDown;
    }

    protected int getCoolDownRate() {
        return 1;
    }

    protected int getCooldownInterval() {
        return 40 + this.getTier() * 5;
    }

    @Override
    public int getTemperature() {
        return this.currentTemperature;
    }

    @Override
    public int getMaxTemperature() {
        return Math.max(500, 100 + this.getTier() * 900);
    }

    @Override
    public void update() {
        super.update();
        if (!this.getWorld().isRemote) {
            this.updateCurrentTemperature();
            if (this.getOffsetTimer() % 10L == 0L) {
                this.applyTemperatureEffects();
            }

            if (this.fuelMaxBurnTime <= 0) {
                this.tryConsumeNewFuel();
                if (this.fuelBurnTimeLeft > 0) {
                    if (this.wasBurningAndNeedsUpdate) {
                        this.wasBurningAndNeedsUpdate = false;
                    } else {
                        this.setBurning(true);
                    }
                }
            }

            if (this.wasBurningAndNeedsUpdate) {
                this.wasBurningAndNeedsUpdate = false;
                this.setBurning(false);
            }
        }

    }

    private void applyTemperatureEffects() {
        if (this.currentTemperature > this.getMaxTemperature()) {
            doExplosion((float) this.currentTemperature / 500f);
        } else {
            if (this.currentTemperature > 0.9f * this.getMaxTemperature()) {
                final float x = getPos().getX() + 0.5F;
                final float y = getPos().getY() + 0.5F;
                final float z = getPos().getZ() + 0.5F;

                ((WorldServer) getWorld()).spawnParticle(EnumParticleTypes.SMOKE_LARGE,
                        x + getFrontFacing().getXOffset() * 0.6,
                        y + getFrontFacing().getYOffset() * 0.6,
                        z + getFrontFacing().getZOffset() * 0.6,
                        7 + GTValues.RNG.nextInt(3),
                        getFrontFacing().getXOffset() / 2.0,
                        getFrontFacing().getYOffset() / 2.0,
                        getFrontFacing().getZOffset() / 2.0, 0.1);
            }
            float damage = this.currentTemperature / 100f;
            if (damage > Float.MIN_NORMAL) {
                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(this.getPos().add(0, 1, 0));
                List<EntityLivingBase> entities = this.getWorld().getEntitiesWithinAABB(EntityLivingBase.class,
                        axisAlignedBB);
                for (EntityLivingBase entityLivingBase : entities) {
                    entityLivingBase.attackEntityFrom(DamageSource.HOT_FLOOR, damage);
                }
            }
        }
    }

    public void setBurning(boolean burning) {
        this.isBurning = burning;
        if (!getWorld().isRemote) {
            markDirty();
            writeCustomData(IS_WORKING, buf -> buf.writeBoolean(burning));
        }
    }

    private double getFuelLeftPercent() {
        return this.fuelMaxBurnTime == 0 ? 0.0 : (double) this.fuelBurnTimeLeft / ((double) this.fuelMaxBurnTime);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("FuelBurnTimeLeft", this.fuelBurnTimeLeft);
        data.setInteger("FuelMaxBurnTime", this.fuelMaxBurnTime);
        data.setInteger("CurrentTemperature", this.currentTemperature);
        //data.setTag("ContainerInventory", this.containerInventory.serializeNBT());
        return data;
    }

    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.fuelBurnTimeLeft = data.getInteger("FuelBurnTimeLeft");
        this.fuelMaxBurnTime = data.getInteger("FuelMaxBurnTime");
        this.currentTemperature = data.getInteger("CurrentTemperature");
        //this.containerInventory.deserializeNBT(data.getCompoundTag("ContainerInventory"));
        this.isBurning = this.fuelBurnTimeLeft > 0;
    }

    @Override
    public boolean isActive() {
        return this.isBurning;
    }

    public void writeInitialSyncData(PacketBuffer buf) {
        super.writeInitialSyncData(buf);
        buf.writeBoolean(this.isBurning);
    }

    public void receiveInitialSyncData(PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        this.isBurning = buf.readBoolean();
    }

    public void receiveCustomData(int dataId, PacketBuffer buf) {
        super.receiveCustomData(dataId, buf);
        if (dataId == GregtechDataCodes.IS_WORKING) {
            this.isBurning = buf.readBoolean();
            this.scheduleRenderUpdate();
        }

    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
    }

    @Override
    public @NotNull List<ITextComponent> getDataInfo() {
        return Arrays.asList(
                new TextComponentTranslation("projectreflection.firebox.current_heat",
                        TextFormattingUtil.formatNumbers(this.currentTemperature)),
                new TextComponentTranslation("projectreflection.firebox.max_heat",
                        TextFormattingUtil.formatNumbers(this.getMaxTemperature()))
        );
    }

    @Override
    public MultiblockAbility<IHeatCapability> getAbility() {
        return null;
    }

    @Override
    public void registerAbilities(List<IHeatCapability> abilityList) {

    }
}
