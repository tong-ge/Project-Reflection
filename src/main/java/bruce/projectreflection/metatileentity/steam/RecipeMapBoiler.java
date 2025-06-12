package bruce.projectreflection.metatileentity.steam;

import bruce.projectreflection.PRConstants;
import gregtech.api.GTValues;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.NotifiableFluidTank;
import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.gui.widgets.TankWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.unification.material.Materials;
import gregtech.api.util.GTTransferUtils;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.common.metatileentities.steam.boiler.SteamBoiler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RecipeMapBoiler extends SteamBoiler {
    protected final RecipeMap<?> recipeMap;
    protected final ICubeRenderer texture;
    private final int baseSteamOutput;
    private final int cooldownInterval;
    private final int cooldownRate;
    private final boolean isSafe;

    protected NotifiableFluidTank[] fuelFluidImports;
    protected NotifiableFluidTank[] byproductFluidExports;

    public RecipeMapBoiler(ResourceLocation metaTileEntityId,
                           boolean isHighPressure,
                           ICubeRenderer texture,
                           @Nonnull
                           RecipeMap<?> recipeMap,
                           int baseSteamOutput,
                           int cooldownInterval,
                           int cooldownRate,
                           boolean isSafe) {
        super(metaTileEntityId, isHighPressure, texture);
        this.texture = texture;
        this.recipeMap = recipeMap;
        this.baseSteamOutput = baseSteamOutput;
        this.cooldownInterval = cooldownInterval;
        this.cooldownRate = cooldownRate;
        this.isSafe = isSafe;
        this.initializeInventory();
    }

    public RecipeMapBoiler(ResourceLocation metaTileEntityId,
                           boolean isHighPressure,
                           ICubeRenderer texture,
                           @Nonnull
                           RecipeMap<?> recipeMap,
                           int baseSteamOutput,
                           int cooldownInterval,
                           int cooldownRate) {
        this(metaTileEntityId,
                isHighPressure,
                texture,
                recipeMap,
                baseSteamOutput,
                cooldownInterval,
                cooldownRate,
                true);
    }

    @Override
    protected int getBaseSteamOutput() {
        return this.baseSteamOutput;
    }

    @Override
    protected void tryConsumeNewFuel() {
        Recipe recipe = this.recipeMap.findRecipe(GTValues.V[GTValues.LV],
                this.importItems, this.importFluids);
        if (recipe != null) {
            int burnTime = recipe.getDuration();
            if (recipe.matches(true, this.importItems, this.importFluids)) {
                GTTransferUtils.addItemsToItemHandler(this.exportItems, false,
                        recipe.getResultItemOutputs(1, 1, this.recipeMap));
                GTTransferUtils.addFluidsToFluidHandler(this.exportFluids, false,
                        recipe.getResultFluidOutputs(1, 1, this.recipeMap));
                this.setFuelMaxBurnTime(burnTime);
            }
        }
    }

    @Override
    protected int getCooldownInterval() {
        return this.cooldownInterval;
    }

    @Override
    protected int getCoolDownRate() {
        return this.cooldownRate;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new RecipeMapBoiler(this.metaTileEntityId,
                this.isHighPressure,
                this.texture,
                this.recipeMap,
                this.baseSteamOutput,
                this.cooldownInterval,
                this.cooldownRate, isSafe);
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        ModularUI.Builder builder = createUITemplate(entityPlayer);
        if (this.importItems.getSlots() > 0)
            builder.slot(this.importItems, 0, 122, 62, GuiTextures.SLOT_STEAM.get(this.isHighPressure),
                    GuiTextures.COAL_OVERLAY_STEAM.get(this.isHighPressure));
        if (this.exportItems.getSlots() > 0)
            builder.slot(this.exportItems, 0, 122, 26, true, false,
                    GuiTextures.SLOT_STEAM.get(this.isHighPressure),
                    GuiTextures.DUST_OVERLAY_STEAM.get(this.isHighPressure));
        if (this.fuelFluidImports.length > 0)
            builder.widget((new TankWidget(this.fuelFluidImports[0], 109, 26, 10, 54))
                    .setBackgroundTexture(GuiTextures.PROGRESS_BAR_BOILER_EMPTY.get(this.isHighPressure)));
        builder.progressBar(this::getFuelLeftPercent, 120, 44, 18, 18,
                GuiTextures.PROGRESS_BAR_BOILER_FUEL.get(this.isHighPressure),
                ProgressWidget.MoveType.VERTICAL,
                this.recipeMap);
        return builder.build(this.getHolder(), entityPlayer);
    }

    public IItemHandlerModifiable createExportItemHandler() {
        return this.recipeMap == null ? super.createExportItemHandler() : new NotifiableItemStackHandler(this,
                this.recipeMap.getMaxOutputs(),
                this, true);
    }

    public IItemHandlerModifiable createImportItemHandler() {
        return this.recipeMap == null ? super.createImportItemHandler() : new NotifiableItemStackHandler(this,
                this.recipeMap.getMaxInputs(),
                this, false);
    }

    @Override
    protected FluidTankList createImportFluidHandler() {
        FluidTankList superHandler = super.createImportFluidHandler();
        if (this.recipeMap == null) {
            this.fuelFluidImports = new NotifiableFluidTank[0];
            return superHandler;
        }
        this.fuelFluidImports = new NotifiableFluidTank[this.recipeMap.getMaxFluidInputs()];
        for (int i = 0; i < this.fuelFluidImports.length; i++) {
            fuelFluidImports[i] = new NotifiableFluidTank(16000, this, false);
        }
        return new FluidTankList(false, superHandler, this.fuelFluidImports);
    }

    @Override
    protected FluidTankList createExportFluidHandler() {
        FluidTankList superHandler = super.createExportFluidHandler();
        if (this.recipeMap == null) {
            this.fuelFluidImports = new NotifiableFluidTank[0];
            return superHandler;
        }
        this.byproductFluidExports = new NotifiableFluidTank[this.recipeMap.getMaxFluidOutputs()];
        for (int i = 0; i < this.byproductFluidExports.length; i++) {
            byproductFluidExports[i] = new NotifiableFluidTank(16000, this, true);
        }
        return new FluidTankList(false, superHandler, this.fuelFluidImports);
    }

    protected boolean tryPlaceSteam(EnumFacing... allowedFaces) {
        ArrayList<EnumFacing> facings = new ArrayList<>(Arrays.asList(allowedFaces));
        Collections.shuffle(facings, PRConstants.generalRandom);
        for (EnumFacing face : facings) {
            BlockPos pos = this.getPos().offset(face);
            if (this.getWorld().getBlockState(pos).getMaterial() == Material.AIR) {
                IFluidHandler iFluidHandler = this.getCoverCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face);
                if (iFluidHandler != null) {
                    FluidStack fs = iFluidHandler.drain(Materials.Steam.getFluid(2000), false);
                    int keep = PRConstants.generalRandom.nextInt(1000);
                    if (fs != null && fs.amount >= 1000 + keep) {
                        fs.amount = 1000;
                        fs = iFluidHandler.drain(fs, true);
                        this.getWorld().setBlockState(pos, Materials.Steam.getFluid().getBlock().getDefaultState());
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void pushFluidsIntoNearbyHandlers(EnumFacing... allowedFaces) {
        if (!tryPlaceSteam(allowedFaces)) {
            if (isSafe) {
                super.pushFluidsIntoNearbyHandlers(allowedFaces);
            } else if (this.steamFluidTank.getCapacity() - this.steamFluidTank.getFluidAmount() < 1000) {
                this.doExplosion((float) this.baseSteamOutput / 20.0f);
            }
        }
    }
}
