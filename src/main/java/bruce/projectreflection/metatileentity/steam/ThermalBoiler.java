package bruce.projectreflection.metatileentity.steam;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.PRLabs;
import bruce.projectreflection.capability.PRCapabilities;
import bruce.projectreflection.capability.energy.IHeatReceiver;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.ColourMultiplier;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.capability.impl.CommonFluidFilters;
import gregtech.api.capability.impl.FilteredFluidHandler;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.gui.widgets.TankWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.unification.material.Materials;
import gregtech.api.util.GTTransferUtils;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import org.apache.commons.lang3.ArrayUtils;

public class ThermalBoiler extends MetaTileEntity implements IHeatReceiver {
    public static ThermalBoiler DEBUG_BOILER = new ThermalBoiler(new ResourceLocation(PRConstants.modid, "debug_boiler"),
            Textures.STEAM_BRICKED_CASING_STEEL, Textures.FLUID_HEATER_OVERLAY, 64, 45, 1);
    protected final ICubeRenderer baseTexture;
    protected final ICubeRenderer overlay;
    protected final int baseSteamOutput;//64
    protected final int heatCapacity;//640000
    protected final int steamCapacity;//64000
    protected final int cooldownInterval;
    protected final int cooldownRate;
    protected int temperature = 0;
    protected int timeBeforeCooldown = 0;
    protected int timeBeforePlace = 0;
    protected IFluidTank waterFluidTank;
    protected IFluidTank steamFluidTank;

    public ThermalBoiler(ResourceLocation metaTileEntityId,
                         ICubeRenderer baseTexture,
                         ICubeRenderer overlay,
                         int baseSteamOutput,
                         int cooldownInterval,
                         int cooldownRate) {
        super(metaTileEntityId);
        this.baseTexture = baseTexture;
        this.overlay = overlay;
        this.baseSteamOutput = baseSteamOutput;
        this.cooldownInterval = cooldownInterval;
        this.cooldownRate = cooldownRate;

        this.steamCapacity = baseSteamOutput * 1000;
        this.heatCapacity = baseSteamOutput * 2000;
        this.initializeInventory();
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new ThermalBoiler(metaTileEntityId, baseTexture, overlay, baseSteamOutput, cooldownInterval, cooldownRate);
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return ModularUI.builder(GuiTextures.BACKGROUND, 176, 166)
                .widget(new ProgressWidget(() -> (double) this.temperature / (double) this.heatCapacity, 96, 26, 10, 54)
                        .setProgressBar(GuiTextures.FLUID_SLOT, GuiTextures.PROGRESS_BAR_BOILER_HEAT, ProgressWidget.MoveType.VERTICAL)

                )
                .widget(new TankWidget(waterFluidTank, 83, 26, 10, 54)
                        .setBackgroundTexture(GuiTextures.FLUID_SLOT)
                        .setContainerClicking(true, true)
                )
                .widget(new TankWidget(steamFluidTank, 70, 26, 10, 54)
                        .setBackgroundTexture(GuiTextures.FLUID_SLOT))
                .bindPlayerInventory(entityPlayer.inventory)
                .build(getHolder(), entityPlayer);
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        IVertexOperation[] colouredPipeline = ArrayUtils.add(pipeline,
                new ColourMultiplier(GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering())));
        baseTexture.render(renderState, translation, colouredPipeline);
        overlay.renderOrientedState(renderState, translation, pipeline, getFrontFacing(), this.temperature >= this.getTemperaturePerWork(), true);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side) {
        if (side == EnumFacing.DOWN && capability == PRCapabilities.CAPABILITY_HEAT_RECEIVER) {
            return (T) this;
        }
        return super.getCapability(capability, side);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        return super.writeToNBT(data);
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
    }

    @Override
    public void receiveHeat(int amount) {
        this.temperature += amount;
        PRLabs.logger.info("Heat received: {} HU", amount);
        this.timeBeforeCooldown = this.cooldownInterval;
    }

    //TODO: calcification
    protected int getTemperaturePerWork() {
        return 80;
    }

    @Override
    protected FluidTankList createImportFluidHandler() {
        this.waterFluidTank = new FilteredFluidHandler(4000).setFilter(CommonFluidFilters.BOILER_FLUID);
        return new FluidTankList(false, waterFluidTank);
    }

    @Override
    protected FluidTankList createExportFluidHandler() {
        this.steamFluidTank = new FluidTank(this.steamCapacity);
        return new FluidTankList(false);
    }

    private void updateCurrentTemperature() {
        if (timeBeforeCooldown == 0) {
            if (temperature > 0) {
                temperature -= cooldownRate;
                timeBeforeCooldown = cooldownInterval;
            }
        } else --timeBeforeCooldown;
    }

    protected void generateSteam() {
        int works = this.temperature / getTemperaturePerWork();
        if (works > 0) {
            FluidStack drained = waterFluidTank.drain(works, true);
            if (drained != null) {
                int heatDrained = getTemperaturePerWork() * drained.amount;
                this.temperature -= heatDrained;
                PRLabs.logger.info("Heat drained: {} HU", heatDrained);
                int fillAmount = 160 * drained.amount;
                int filledSteam = steamFluidTank.fill(Materials.Steam.getFluid(fillAmount), true);
                if (filledSteam < fillAmount) {
                    doExplosion(2.0f);
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (!getWorld().isRemote) {
            updateCurrentTemperature();
            if (this.temperature > getTemperaturePerWork()) {
                generateSteam();
            }

            if (timeBeforePlace == 0) {
                if (steamFluidTank.getFluidAmount() > steamCapacity / 2) {
                    FluidUtil.tryPlaceFluid(null,
                            getWorld(),
                            getPos().up(),
                            new FluidTankList(false, this.steamFluidTank),
                            Materials.Steam.getFluid(1000));

                }
                if (steamFluidTank.getFluidAmount() > steamCapacity / 2 + steamCapacity / 4) {
                    //dithered quantization
                    timeBeforePlace = (int) ((500.0 / this.baseSteamOutput) + PRConstants.generalRandom.nextDouble());
                } else {
                    timeBeforePlace = (int) ((1000.0 / this.baseSteamOutput) + PRConstants.generalRandom.nextDouble());
                }
                PRLabs.logger.info("Output at {} mB/t", 1000.0 / timeBeforePlace);
            } else --timeBeforePlace;
        }
    }
}
