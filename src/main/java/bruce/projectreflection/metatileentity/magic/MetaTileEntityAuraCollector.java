package bruce.projectreflection.metatileentity.magic;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.materials.FirstTierMaterials;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import bruce.projectreflection.recipes.properties.AuraProperty;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.RecipeLogicEnergy;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SimpleMachineMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.world.aura.AuraHandler;

import java.util.Arrays;
import java.util.function.Supplier;

public class MetaTileEntityAuraCollector extends SimpleMachineMetaTileEntity {
    //private static RecipeMap<SimpleRecipeBuilder> recipeMap = new RecipeMap<>("aura_collector", 1, 0, 0, 1, new SimpleRecipeBuilder(), false);

    public MetaTileEntityAuraCollector(int tier) {
        super(new ResourceLocation(PRConstants.modid,
                String.format("aura_collector.%s", PRConstants.V[tier])
        ), PRRecipeMaps.AURA_COLLECTOR, Textures.MULTIBLOCK_WORKABLE_OVERLAY, tier, true);
    }

    /*
        public static void initRecipeMap() {
            recipeMap.recipeBuilder().circuitMeta(1).fluidOutputs(FirstTierMaterials.AURA.getFluid(1)).EUt(20).duration(10).buildAndRegister();
            recipeMap.recipeBuilder().notConsumable(new ItemStack(BlocksTC.arcaneWorkbenchCharger)).fluidOutputs(FirstTierMaterials.AURA.getFluid(1)).EUt(20).duration(10).buildAndRegister();
        }
    */
    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityAuraCollector(this.getTier());
    }

    @Override
    protected RecipeLogicEnergy createWorkable(RecipeMap<?> recipeMap) {
        return new AuraCollectorRecipeLogic(this, recipeMap, () -> energyContainer);
    }

    private static class AuraCollectorRecipeLogic extends RecipeLogicEnergy {
        private static final AuraProperty.AuraPropertyStruct defaultStruct = new AuraProperty.AuraPropertyStruct(12, false);

        public AuraCollectorRecipeLogic(MetaTileEntity tileEntity, RecipeMap<?> recipeMap, Supplier<IEnergyContainer> energyContainer) {
            super(tileEntity, recipeMap, energyContainer);
        }

        @Override
        protected boolean checkPreviousRecipe() {
            return super.checkPreviousRecipe() && this.checkRecipe(this.previousRecipe);
        }

        private float checkExtendedVis(float vis) {
            return spendExtendedVis(vis, true);
        }

        private float spendExtendedVis(float vis, boolean simulate) {

            float totalDrained = 0f;
            totalLoop:
            for (int xx = -1; xx <= 1; ++xx) {
                for (int zz = -1; zz <= 1; ++zz) {
                    float drained = AuraHandler.drainVis(metaTileEntity.getWorld(), metaTileEntity.getPos().add(xx * 16, 0, zz * 16), vis - totalDrained, simulate);
                    totalDrained += drained;
                    if (totalDrained > vis) {
                        //ProjectReflection.logger.info("{} drained",totalDrained);
                        break totalLoop;
                    }
                }
            }
            if (!simulate) ProjectReflection.logger.info("{} drained", totalDrained);
            return totalDrained;
        }


        @Override
        public boolean checkRecipe(@NotNull Recipe recipe) {
            //return AuraHandler.drainVis(metaTileEntity.getWorld(), metaTileEntity.getPos(),12.8f,true)>=12.0f;
            AuraProperty.AuraPropertyStruct propertyStruct = recipe.getProperty(AuraProperty.INSTANCE, defaultStruct);
            if (propertyStruct.isExtended) {
                return checkExtendedVis(propertyStruct.auraConsumption) >= propertyStruct.auraConsumption;
            } else
                return AuraHandler.getVis(metaTileEntity.getWorld(), metaTileEntity.getPos()) > propertyStruct.auraConsumption;
        }

        @Override
        protected boolean setupAndConsumeRecipeInputs(@NotNull Recipe recipe, @NotNull IItemHandlerModifiable importInventory) {
            /*
            if (isExtended(recipe)) {
                spendExtendedVis(AURA_CONSUMPTION,false);
            } else
                AuraHandler.drainVis(metaTileEntity.getWorld(), metaTileEntity.getPos(), AURA_CONSUMPTION, false);
            //ProjectReflection.logger.info("{} aura drained", auraDrained);

             */
            AuraProperty.AuraPropertyStruct propertyStruct = recipe.getProperty(AuraProperty.INSTANCE,
                    defaultStruct);
            if (propertyStruct.isExtended) {
                spendExtendedVis(propertyStruct.auraConsumption, false);
            } else {
                AuraHandler.drainVis(metaTileEntity.getWorld(), metaTileEntity.getPos(), propertyStruct.auraConsumption, false);
            }
            return super.setupAndConsumeRecipeInputs(recipe, importInventory);
        }
    }
}
