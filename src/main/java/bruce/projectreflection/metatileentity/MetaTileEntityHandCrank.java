package bruce.projectreflection.metatileentity;

import bruce.projectreflection.capability.recipelogic.RecipeLogicHandCrank;
import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.ColourMultiplier;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.NotifiableFluidTank;
import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.ImageWidget;
import gregtech.api.gui.widgets.LabelWidget;
import gregtech.api.items.itemhandlers.GTItemStackHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Objects;

public class MetaTileEntityHandCrank extends MetaTileEntity {
    private final RecipeLogicHandCrank workable;
    protected final ICubeRenderer renderer;

    public MetaTileEntityHandCrank(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap, ICubeRenderer renderer) {
        super(metaTileEntityId);
        this.renderer = renderer;
        this.workable = new RecipeLogicHandCrank(this, recipeMap);
        this.initializeInventory();
    }

    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        IVertexOperation[] colouredPipeline = ArrayUtils.add(pipeline, new ColourMultiplier(GTUtility.convertRGBtoOpaqueRGBA_CL(this.getPaintingColorForRendering())));
        this.getBaseRenderer().render(renderState, translation, colouredPipeline);
        this.renderer.renderOrientedState(renderState, translation, pipeline, this.getFrontFacing(), this.workable.isActive(), this.workable.isWorkingEnabled());
    }

    protected ICubeRenderer getBaseRenderer() {
        return Textures.PRIMITIVE_PUMP;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntityHandCrank(this.metaTileEntityId, this.getRecipeMap(), this.renderer);
    }

    protected ModularUI.Builder createGuiTemplate(EntityPlayer player) {
        RecipeMap<?> workableRecipeMap = this.workable.getRecipeMap();
        Objects.requireNonNull(workableRecipeMap);
        int yOffset = 0;
        if (workableRecipeMap.getMaxInputs() >= 6 || workableRecipeMap.getMaxFluidInputs() >= 6 || workableRecipeMap.getMaxOutputs() >= 6 || workableRecipeMap.getMaxFluidOutputs() >= 6) {
            yOffset = 9;
        }
        Objects.requireNonNull((AbstractRecipeLogic) this.workable);
        ModularUI.Builder var10000 = workableRecipeMap.createUITemplate(this.workable::getProgressPercent,
                        this.importItems, this.exportItems, this.importFluids, this.exportFluids, yOffset)
                .widget(new LabelWidget(5, 5, this.getMetaFullName()));
        ImageWidget var8 = (new ImageWidget(79, 42 + yOffset, 18, 18, GuiTextures.INDICATOR_NO_ENERGY)).setIgnoreColor(true);
        AbstractRecipeLogic var10002 = this.workable;
        Objects.requireNonNull(var10002);
        return var10000.widget(var8.setPredicate(var10002::isHasNotEnoughEnergy))
                .bindPlayerInventory(player.inventory, GuiTextures.SLOT, yOffset);
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return this.createGuiTemplate(entityPlayer).build(this.getHolder(), entityPlayer);
    }

    public boolean isActive() {
        return this.workable.isActive() && this.workable.isWorkingEnabled();
    }

    @Override
    public boolean onRightClick(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, CuboidRayTraceResult hitResult) {
        if (workable.canBeClicked(playerIn)) {
            workable.onClick(playerIn);
            return true;
        }
        return super.onRightClick(playerIn, hand, facing, hitResult);
    }

    protected IItemHandlerModifiable createImportItemHandler() {
        return this.workable == null ? new GTItemStackHandler(this, 0) : new NotifiableItemStackHandler(this, this.workable.getRecipeMap().getMaxInputs(), this, false);
    }

    protected IItemHandlerModifiable createExportItemHandler() {
        return this.workable == null ? new GTItemStackHandler(this, 0) : new NotifiableItemStackHandler(this, this.workable.getRecipeMap().getMaxOutputs(), this, true);
    }

    protected FluidTankList createImportFluidHandler() {
        if (this.workable == null) {
            return new FluidTankList(false);
        } else {
            NotifiableFluidTank[] fluidImports = new NotifiableFluidTank[this.workable.getRecipeMap().getMaxFluidInputs()];
            for (int i = 0; i < fluidImports.length; ++i) {
                NotifiableFluidTank filteredFluidHandler = new NotifiableFluidTank(1000, this, false);
                fluidImports[i] = filteredFluidHandler;
            }
            return new FluidTankList(false, fluidImports);
        }
    }

    protected FluidTankList createExportFluidHandler() {
        if (this.workable == null) {
            return new FluidTankList(false);
        } else {
            FluidTank[] fluidExports = new FluidTank[this.workable.getRecipeMap().getMaxFluidOutputs()];
            for (int i = 0; i < fluidExports.length; ++i) {
                fluidExports[i] = new NotifiableFluidTank(1000, this, true);
            }
            return new FluidTankList(false, fluidExports);
        }
    }

    public boolean checkRecipe(Recipe recipe, boolean consumesIfSuccess) {
        return true;
    }
/*
    @Override public SoundEvent getSound() {
        return this.workable.getRecipeMap().getSound();
    }

 */
}
