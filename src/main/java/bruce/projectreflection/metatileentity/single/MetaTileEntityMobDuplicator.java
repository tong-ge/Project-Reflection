package bruce.projectreflection.metatileentity.single;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.RecipeLogicEnergy;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SimpleMachineMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.client.renderer.texture.Textures;
import gregtechfoodoption.recipe.properties.MobOnTopProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class MetaTileEntityMobDuplicator extends SimpleMachineMetaTileEntity {
    public MetaTileEntityMobDuplicator(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId, PRRecipeMaps.MOB_DUPLICATOR, Textures.MULTIBLOCK_WORKABLE_OVERLAY, tier, true);
    }

    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityMobDuplicator(this.metaTileEntityId, this.getTier());
    }

    @Override
    protected RecipeLogicEnergy createWorkable(RecipeMap<?> recipeMap) {
        return new MobDuplicatorRecipeLogic(this, recipeMap, () -> energyContainer);
    }

    private static class MobDuplicatorRecipeLogic extends RecipeLogicEnergy {
        private ResourceLocation entityResourceLocation;
        private AxisAlignedBB boundingBox;

        public MobDuplicatorRecipeLogic(MetaTileEntity tileEntity, RecipeMap<?> recipeMap, Supplier<IEnergyContainer> energyContainer) {
            super(tileEntity, recipeMap, energyContainer);
            boundingBox = null;
        }

        protected List<Entity> getEntitiesInProximity() {
            if (this.boundingBox == null) {
                this.boundingBox = new AxisAlignedBB(metaTileEntity.getPos().up());
            }

            return metaTileEntity.getWorld().getEntitiesWithinAABB(Entity.class, this.boundingBox);
        }
        @Override
        public boolean checkRecipe(@NotNull Recipe recipe) {
            if (!getEntitiesInProximity().isEmpty()) {
                return false;
            }
            entityResourceLocation = recipe.getProperty(MobOnTopProperty.getInstance(), null);
            BlockPos pos = metaTileEntity.getPos();
            NBTTagCompound entityNBT = new NBTTagCompound();
            entityNBT.setString("id", entityResourceLocation.toString());
            Entity entity = AnvilChunkLoader.readWorldEntityPos(entityNBT, metaTileEntity.getWorld(), pos.getX() + 0.5,
                    pos.getY() + 1.0, pos.getZ() + 0.5, false);
            if (entity == null) {
                return false;
            }
            entity.setDead();
            return super.checkRecipe(recipe);
        }

        @Override
        protected void outputRecipeOutputs() {
            BlockPos pos = metaTileEntity.getPos();
            NBTTagCompound entityNBT = new NBTTagCompound();
            if (entityResourceLocation != null) {
                entityNBT.setString("id", entityResourceLocation.toString());
                double x = pos.getX() + PRConstants.rand.nextDouble();
                double y = pos.getY() + 1.0;
                double z = pos.getZ() + PRConstants.rand.nextDouble();
                Entity entity = AnvilChunkLoader.readWorldEntityPos(entityNBT, metaTileEntity.getWorld(), x, y, z,
                        true);
                if (entity != null) {
                    entity.setLocationAndAngles(x, y, z, PRConstants.rand.nextFloat() * 360.0F, entity.rotationPitch);
                    if (entity instanceof EntityLiving) {
                        ((EntityLiving) entity).onInitialSpawn(metaTileEntity.getWorld()
                                .getDifficultyForLocation(new BlockPos(entity)), null);
                    }
                }
            }
        }

        @Override
        public @NotNull NBTTagCompound serializeNBT() {
            NBTTagCompound compound = super.serializeNBT();
            compound.setString("entity", entityResourceLocation.toString());
            return compound;
        }

        @Override
        public void deserializeNBT(@NotNull NBTTagCompound compound) {
            super.deserializeNBT(compound);
            entityResourceLocation = new ResourceLocation(compound.getString("entity"));
        }
    }
}
