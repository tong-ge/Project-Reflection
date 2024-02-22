package bruce.projectreflection.items;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.entity.EntityBlackhole;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class ItemManifold extends Item {
    private static final DamageSource MANIFOLD_EXPLOSION = new DamageSource("explosion").setExplosion().setDamageIsAbsolute().setMagicDamage().setDamageBypassesArmor().setDamageAllowedInCreativeMode();

    public ItemManifold() {
        setRegistryName(PRConstants.modid, "manifold");
        setTranslationKey("manifold");
        setCreativeTab(PRConstants.tab);
        setMaxStackSize(1);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        createStack(stack);
        return null;
    }

    private static boolean initialized(ItemStack stack) {
        NBTTagCompound stackCompound = stack.getTagCompound();
        if (stackCompound == null)
            return false;
        return stackCompound.hasKey("creationTime");
    }

    private static boolean isExploding(ItemStack stack) {
        NBTTagCompound stackCompound = stack.getTagCompound();
        if (stackCompound == null)
            return false;
        return stackCompound.hasKey("explosionSpeed");
    }

    private static ItemStack createStack(ItemStack stack) {
        NBTTagCompound stackCompound = stack.getTagCompound();
        if (stackCompound == null) stackCompound = new NBTTagCompound();
        stackCompound.setLong("creationTime", System.currentTimeMillis());
        int[] verbosity = PRConstants.rand.ints(1024).toArray();
        stackCompound.setIntArray("verbosity", verbosity);
        stack.setTagCompound(stackCompound);
        return stack;
    }
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if (!initialized(stack)) {
            createStack(stack);
        }
        /*
        NBTTagCompound compound=stack.getTagCompound();
        if(compound.hasKey("explosionSpeed"))
        {
            ExplosionFleija explosionFleija=new ExplosionFleija((int)entityIn.posX,(int) entityIn.posY,(int) entityIn.posZ,worldIn,100,1,1);
            explosionFleija.readFromNbt(compound,"explosion");
            int explosionSpeed=compound.getInteger("explosionSpeed");
            for(int i=0;i<explosionSpeed;i++) {
                if (explosionFleija.update()) {
                    entityIn.attackEntityFrom(MANIFOLD_EXPLOSION,Float.MAX_VALUE);
                }
            }
            explosionFleija.saveToNbt(compound,"explosion");
            compound.setInteger("explosionSpeed",1+explosionSpeed);
            stack.setTagCompound(compound);
        }
         */
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        World world = entityItem.world;
        ItemStack stack = entityItem.getItem();
        if (!world.isRemote) {
            /*
            NBTTagCompound compound=stack.getTagCompound();
            ExplosionFleija explosionFleija=new ExplosionFleija((int)entityItem.posX,(int) entityItem.posY,(int) entityItem.posZ,world,100,1,1);
            if(compound.hasKey("explosionSpeed"))
            {
                explosionFleija.readFromNbt(compound,"explosion");
                int explosionSpeed=compound.getInteger("explosionSpeed");
                for(int i=0;i<explosionSpeed;i++) {
                    if (explosionFleija.update()) {
                        entityItem.setDead();
                        return false;
                    }
                }
                explosionFleija.saveToNbt(compound,"explosion");
                compound.setInteger("explosionSpeed",1+explosionSpeed);
                stack.setTagCompound(compound);
                return true;
            }
            else
            */
            if (entityItem.onGround) {
                //explosionFleija.saveToNbt(compound,"explosion");
                //compound.setInteger("explosionSpeed",1);
                EntityBlackhole entityBlackhole = new EntityBlackhole(entityItem.posX, entityItem.posY, entityItem.posZ, world, 100, 1, 1);
                //entityBlackhole.radius=100;
                world.spawnEntity(entityBlackhole);
                entityItem.setDead();
            }
        }
        return false;
    }

}
