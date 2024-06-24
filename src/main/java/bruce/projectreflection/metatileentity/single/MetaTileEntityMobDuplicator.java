package bruce.projectreflection.metatileentity.single;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.misc.MathUtils;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.RecipeLogicEnergy;
import gregtech.api.metatileentity.ITieredMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SimpleMachineMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.client.renderer.texture.Textures;
import gregtechfoodoption.recipe.properties.MobOnTopProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.fml.common.Loader;
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
        private double x;
        private double y;
        private double z;
        private final int tier;
        private final int limit;

        public MobDuplicatorRecipeLogic(MetaTileEntity tileEntity, RecipeMap<?> recipeMap, Supplier<IEnergyContainer> energyContainer) {
            super(tileEntity, recipeMap, energyContainer);
            boundingBox = null;
            tier = ((ITieredMetaTileEntity) tileEntity).getTier();
            limit = Math.round(tier * tier * 0.67f);
        }

        protected List<Entity> getEntitiesInProximity() {
            if (this.boundingBox == null) {
                this.boundingBox = new AxisAlignedBB(metaTileEntity.getPos().add(-tier, -tier, -tier), metaTileEntity.getPos().add(tier + 1, tier + 1, tier + 1));
            }

            return metaTileEntity.getWorld().getEntitiesWithinAABB(Entity.class, this.boundingBox);
        }
        @Override
        public boolean checkRecipe(@NotNull Recipe recipe) {
            entityResourceLocation = recipe.getProperty(MobOnTopProperty.getInstance(), null);
            int mobCount = 0;
            for (Entity mob : getEntitiesInProximity()) {
                if (EntityList.isMatchingName(mob, entityResourceLocation)) {
                    mobCount++;
                    if (mobCount >= limit)
                        return false;
                }
            }

            BlockPos pos = metaTileEntity.getPos();
            NBTTagCompound entityNBT = new NBTTagCompound();
            entityNBT.setString("id", entityResourceLocation.toString());

            x = pos.getX() + MathUtils.randbetween(PRConstants.rand, -tier, tier + 1);
            y = pos.getY() + MathUtils.randbetween(PRConstants.rand, 0, tier + 1);
            z = pos.getZ() + MathUtils.randbetween(PRConstants.rand, -tier, tier + 1);
            Entity entity = AnvilChunkLoader.readWorldEntityPos(entityNBT, metaTileEntity.getWorld(), x, y, z, false);
            if (entity == null) {
                return false;
            }
            entity.setLocationAndAngles(x, y, z, PRConstants.rand.nextFloat() * 360.0F, entity.rotationPitch);
            if (entity.isEntityInsideOpaqueBlock()) {
                return false;
            }
            entity.setDead();
            return super.checkRecipe(recipe);
        }

        @Override
        protected void outputRecipeOutputs() {
            if (entityResourceLocation != null) {
                NBTTagCompound entityNBT = new NBTTagCompound();
                entityNBT.setString("id", entityResourceLocation.toString());
                Entity entity = AnvilChunkLoader.readWorldEntityPos(entityNBT, metaTileEntity.getWorld(), x, y, z,
                        true);
                if (entity != null) {
                    entity.setLocationAndAngles(x, y, z, PRConstants.rand.nextFloat() * 360.0F, entity.rotationPitch);
                    ProjectReflection.logger.info("Mob spawned at {},{},{}", x, y, z);
                    if (entity instanceof EntityLiving) {
                        EntityLiving living = (EntityLiving) entity;
                        if (!this.itemOutputs.isEmpty()) {
                            Multimap map = HashMultimap.create();
                            map.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier("Loonium Modififer Health", 2, 1));
                            map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier("Loonium Modififer Damage", 1.5, 1));
                            living.getAttributeMap().applyAttributeModifiers(map);

                            living.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, entity instanceof EntityCreeper ? 100 : Integer.MAX_VALUE, 0));
                            living.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, entity instanceof EntityCreeper ? 100 : Integer.MAX_VALUE, 0));
                            if (Loader.isModLoaded("roguelike")) {
                                living.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 1, tier - 1));
                            }
                            NBTTagList nbtTagList = new NBTTagList();
                            for (ItemStack stack : this.itemOutputs) {
                                nbtTagList.appendTag(stack.writeToNBT(new NBTTagCompound()));
                            }
                            entity.getEntityData().setTag(PRConstants.TAG_ITEMSTACK_TO_DROP, nbtTagList);

                        }
                        living.onInitialSpawn(metaTileEntity.getWorld()
                                .getDifficultyForLocation(new BlockPos(entity)), null);
                    }
                }
            }
        }

        @Override
        public @NotNull NBTTagCompound serializeNBT() {
            NBTTagCompound compound = super.serializeNBT();
            compound.setString("entity", entityResourceLocation.toString());
            compound.setDouble("positionX", x);
            compound.setDouble("positionY", y);
            compound.setDouble("positionZ", z);
            return compound;
        }

        @Override
        public void deserializeNBT(@NotNull NBTTagCompound compound) {
            super.deserializeNBT(compound);
            entityResourceLocation = new ResourceLocation(compound.getString("entity"));
            x = compound.getDouble("positionX");
            y = compound.getDouble("positionY");
            z = compound.getDouble("positionZ");
        }
    }
}
