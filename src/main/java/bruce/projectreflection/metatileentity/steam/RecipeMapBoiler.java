package bruce.projectreflection.metatileentity.steam;

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
import gregtech.api.util.GTTransferUtils;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.common.metatileentities.steam.boiler.SteamBoiler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class RecipeMapBoiler extends SteamBoiler {
    protected final RecipeMap<?> recipeMap;
    protected final ICubeRenderer texture;
    private final int baseSteamOutput;
    private final int cooldownInterval;
    private final int cooldownRate;

    protected NotifiableFluidTank[] fuelFluidImports;
    protected NotifiableFluidTank[] byproductFluidExports;

    public RecipeMapBoiler(ResourceLocation metaTileEntityId,
                           boolean isHighPressure,
                           ICubeRenderer texture,
                           @Nonnull
                           RecipeMap<?> recipeMap,
                           int baseSteamOutput,
                           int cooldownInterval,
                           int cooldownRate
    ) {
        super(metaTileEntityId, isHighPressure, texture);
        this.texture = texture;
        this.recipeMap = recipeMap;
        this.baseSteamOutput = baseSteamOutput;
        this.cooldownInterval = cooldownInterval;
        this.cooldownRate = cooldownRate;
        this.initializeInventory();
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
                this.cooldownRate);
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        ModularUI.Builder builder = createUITemplate(entityPlayer);
        if (this.importItems.getSlots() > 0)
            builder.slot(this.importItems, 0, 115, 62, GuiTextures.SLOT_STEAM.get(this.isHighPressure),
                    GuiTextures.COAL_OVERLAY_STEAM.get(this.isHighPressure));
        if (this.exportItems.getSlots() > 0)
            builder.slot(this.exportItems, 0, 115, 26, true, false,
                    GuiTextures.SLOT_STEAM.get(this.isHighPressure),
                    GuiTextures.DUST_OVERLAY_STEAM.get(this.isHighPressure));
        if (this.fuelFluidImports.length > 0)
            builder.widget((new TankWidget(this.fuelFluidImports[0], 119, 26, 10, 54))
                    .setBackgroundTexture(GuiTextures.PROGRESS_BAR_BOILER_EMPTY.get(this.isHighPressure)));
        builder.progressBar(this::getFuelLeftPercent, 115, 44, 18, 18,
                GuiTextures.PROGRESS_BAR_BOILER_FUEL.get(this.isHighPressure),
                ProgressWidget.MoveType.VERTICAL);
        return builder.build(this.getHolder(), entityPlayer);
    }

    public IItemHandlerModifiable createExportItemHandler() {
        return this.recipeMap == null ? super.createExportItemHandler() : new NotifiableItemStackHandler(this,
                this.recipeMap.getMaxInputs(),
                this, true);
    }

    public IItemHandlerModifiable createImportItemHandler() {
        return this.recipeMap == null ? super.createImportItemHandler() : new NotifiableItemStackHandler(this,
                this.recipeMap.getMaxOutputs(),
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
}
