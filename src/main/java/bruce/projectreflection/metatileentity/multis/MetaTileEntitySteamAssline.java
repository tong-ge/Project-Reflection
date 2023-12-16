package bruce.projectreflection.metatileentity.multis;


import gregtech.api.GTValues;
import gregtech.api.capability.GregtechTileCapabilities;
import gregtech.api.capability.IControllable;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.capability.impl.SteamMultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapSteamMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockFireboxCasing;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.BlockTurbineCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

import java.util.List;

import static gregtech.api.util.RelativeDirection.*;

public class MetaTileEntitySteamAssline extends RecipeMapSteamMultiblockController {

    public MetaTileEntitySteamAssline(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, RecipeMaps.ASSEMBLER_RECIPES, 1.0);
        this.recipeMapWorkable = new SteamAsslineWorkable(this, RecipeMaps.ASSEMBLER_RECIPES, steamFluidTank, 1.0);
        this.importFluids = new FluidTankList(true);
    }

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        FactoryBlockPattern pattern = FactoryBlockPattern.start(FRONT, UP, RIGHT)
                .aisle("FIF", "RTR", "SAG", " Y ")
                .aisle("FIF", "RTR", "DAG", " Y ").setRepeatable(3, 15)
                .aisle("FOF", "RTR", "DAG", " Y ")
                .where('S', selfPredicate())
                .where('F', states(getCasingState())
                        .or(autoAbilities(false, true, false, false, true))
                        .or(abilities(MultiblockAbility.IMPORT_FLUIDS)))
                .where('O', abilities(MultiblockAbility.STEAM_EXPORT_ITEMS)
                        .addTooltips("gregtech.multiblock.pattern.location_end"))
                .where('Y', states(getCasingState())
                        .or(abilities(MultiblockAbility.STEAM)
                                .setMinGlobalLimited(1)))
                .where('I', metaTileEntities(MetaTileEntities.ITEM_IMPORT_BUS[GTValues.ULV]))
                .where('G', states(getGrateState()))
                .where('A',
                        states(getCasingState()))
                .where('R', states(Blocks.GLASS.getDefaultState()))
                .where('T',
                        states(MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.BRONZE_GEARBOX)))
                .where('D', states(getGrateState()))
                .where(' ', any());
        return pattern.build();
    }


    private IBlockState getGrateState() {
        return MetaBlocks.BOILER_FIREBOX_CASING.getState(BlockFireboxCasing.FireboxCasingType.BRONZE_FIREBOX);
    }


    private IBlockState getCasingState() {
        return MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.BRONZE_BRICKS);
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return Textures.BRONZE_PLATED_BRICKS;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntitySteamAssline(this.metaTileEntityId);
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        //ProjectReflection.logger.info("Structure formed");
        this.inputInventory = new ItemHandlerList(getAbilities(MultiblockAbility.IMPORT_ITEMS));
        this.importFluids = new FluidTankList(true, getAbilities(MultiblockAbility.IMPORT_FLUIDS));
    }

    public void invalidateStructure() {
        super.invalidateStructure();
        this.importFluids = new FluidTankList(true);
    }

    private static class SteamAsslineWorkable extends SteamMultiblockRecipeLogic {
        public SteamAsslineWorkable(RecipeMapSteamMultiblockController tileEntity, RecipeMap<?> recipeMap, IMultipleTankHandler steamFluidTank, double conversionRate) {
            super(tileEntity, recipeMap, steamFluidTank, conversionRate);
            setWorkingEnabled(false);
        }

        @Override
        protected boolean shouldSearchForRecipes() {
            return isWorkingEnabled() && super.shouldSearchForRecipes();
        }

        @Override
        protected void trySearchNewRecipe() {
            //ProjectReflection.logger.info("Try searching new recipes, random={}", PRConstants.rand.nextInt());
            super.trySearchNewRecipe();
            if (this.invalidInputsForRecipes) {
                setWorkingEnabled(false);
            }
        }

        private void polluteAura(float pollution) {
            AuraHelper.polluteAura(metaTileEntity.getWorld(), metaTileEntity.getPos(), pollution, false);
        }

        private void releaseAllPollution() {
            float pollution = 1.0f;
            for (GTRecipeInput input : this.previousRecipe.getInputs()) {
                ItemStack stack = input.getInputStacks()[0];
                AspectList ot = ThaumcraftCraftingManager.getObjectTags(stack);
                for (Aspect aspect : ot.getAspects()) {
                    pollution += Math.max(ot.getAmount(aspect), 1f) * stack.getCount();
                }
            }
            polluteAura(pollution);
        }

        @Override
        protected void completeRecipe() {
            super.completeRecipe();
            setWorkingEnabled(false);
            polluteAura(1.0f);
        }

        @Override
        protected void decreaseProgress() {
            //ProjectReflection.logger.info("Not enough steam! random={}", PRConstants.rand.nextInt());
            releaseAllPollution();
            setWorkingEnabled(false);
            this.progressTime = 0;
            setMaxProgress(0);
            this.recipeEUt = 0;
            this.fluidOutputs = null;
            this.itemOutputs = null;
            //this.hasNotEnoughEnergy = false;
            this.wasActiveAndNeedsUpdate = true;
            this.parallelRecipesPerformed = 0;
            this.overclockResults = new int[]{0, 0};
        }

        @Override
        public long getMaxVoltage() {
            return getSteamFluidTankCombined().getCapacity() / 2000;
        }

        @Override
        public void setWorkingEnabled(boolean workingEnabled) {
            super.setWorkingEnabled(workingEnabled);
            if (workingEnabled) {
                forceRecipeRecheck();
            }
        }
    }

    @Override
    public boolean checkRecipe(@NotNull Recipe recipe, boolean consumeIfSuccess) {
        if (!consumeIfSuccess) {
            // check ordered items
            List<GTRecipeInput> inputs = recipe.getInputs();
            List<IItemHandlerModifiable> itemInputInventory = getAbilities(MultiblockAbility.IMPORT_ITEMS);

            // slot count is not enough, so don't try to match it
            if (itemInputInventory.size() < inputs.size()) return false;

            for (int i = 0; i < inputs.size(); i++) {
                if (!inputs.get(i).acceptsStack(itemInputInventory.get(i).getStackInSlot(0))) {
                    IControllable controllable = this.getCapability(GregtechTileCapabilities.CAPABILITY_CONTROLLABLE, null);
                    //ProjectReflection.logger.info("Input item not ordered:{} desired {}",itemInputInventory.get(i).getStackInSlot(0),inputs.get(i));
                    controllable.setWorkingEnabled(false);
                    return false;
                }
            }
            // check ordered fluids
            inputs = recipe.getFluidInputs();
            List<IFluidTank> fluidInputInventory = getAbilities(MultiblockAbility.IMPORT_FLUIDS);

            // slot count is not enough, so don't try to match it
            if (fluidInputInventory.size() < inputs.size()) return false;

            for (int i = 0; i < inputs.size(); i++) {
                if (!inputs.get(i).acceptsFluid(fluidInputInventory.get(i).getFluid())) {
                    IControllable controllable = this.getCapability(GregtechTileCapabilities.CAPABILITY_CONTROLLABLE, null);
                    //ProjectReflection.logger.info("Input fluid not ordered:{} desired {}",fluidInputInventory.get(i).getFluid(),inputs.get(i));
                    controllable.setWorkingEnabled(false);
                    return false;
                }
            }
        }
        return super.checkRecipe(recipe, consumeIfSuccess);
    }

}
