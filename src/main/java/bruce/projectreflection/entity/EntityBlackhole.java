package bruce.projectreflection.entity;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

import java.util.List;

public class EntityBlackhole extends EntityFlying {
    public static final EntityEntry ENTRY = EntityEntryBuilder.create()
            .name("blackhole")
            .id(new ResourceLocation(PRConstants.modid, "blackhole"), 0)
            .entity(EntityBlackhole.class)
            .tracker(64, 1, true)
            .build();
    public int lastposX = 0;
    public int lastposZ = 0;
    public int radius;
    public int radius2;
    public float explosionCoefficient = 1.0F;
    public float explosionCoefficient2 = 1.0F;
    private int n = 1;
    private int nlimit;
    private int shell;
    private int leg;
    private int element;
    private int speed = 1;

    public EntityBlackhole(World worldIn) {
        super(worldIn);
        setSize(0.9f, 0.9f);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1000d);
    }

    public EntityBlackhole(double x, double y, double z, World world, int rad, float coefficient, float coefficient2) {
        this(world);
        this.setPosition(x, y, z);

        this.radius = rad;
        this.radius2 = this.radius * this.radius;

        this.explosionCoefficient = coefficient;
        this.explosionCoefficient2 = coefficient2;

        this.nlimit = this.radius2 * 4;
    }

    private static long mean(long[] values) {
        long sum = 0L;
        for (long v : values) {
            sum += v;
        }
        return sum / values.length;
    }


    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        lastposX = compound.getInteger("lastposX");
        lastposZ = compound.getInteger("lastposZ");
        radius = compound.getInteger("radius");
        radius2 = compound.getInteger("radius2");
        n = compound.getInteger("n");
        nlimit = compound.getInteger("nlimit");
        shell = compound.getInteger("shell");
        leg = compound.getInteger("leg");
        element = compound.getInteger("element");
        explosionCoefficient = compound.getFloat("explosionCoefficient");
        explosionCoefficient2 = compound.getFloat("explosionCoefficient2");
        speed = compound.getInteger("speed");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("lastposX", lastposX);
        compound.setInteger("lastposZ", lastposZ);
        compound.setInteger("radius", radius);
        compound.setInteger("radius2", radius2);
        compound.setInteger("n", n);
        compound.setInteger("nlimit", nlimit);
        compound.setInteger("shell", shell);
        compound.setInteger("leg", leg);
        compound.setInteger("element", element);
        compound.setFloat("explosionCoefficient", explosionCoefficient);
        compound.setFloat("explosionCoefficient2", explosionCoefficient2);
        compound.setInteger("speed", speed);
    }

    public boolean breakBlock() {
        breakColumn(this.lastposX, this.lastposZ);
        this.shell = (int) Math.floor((Math.sqrt(n) + 1) / 2);
        //Should never happen, but someone got a / by 0 exception here so who knows.
        if (shell == 0)
            shell = 1;
        int shell2 = this.shell * 2;
        this.leg = (int) Math.floor((this.n - (shell2 - 1) * (shell2 - 1)) / shell2);
        this.element = (this.n - (shell2 - 1) * (shell2 - 1)) - shell2 * this.leg - this.shell + 1;
        this.lastposX = this.leg == 0 ? this.shell : this.leg == 1 ? -this.element : this.leg == 2 ? -this.shell : this.element;
        this.lastposZ = this.leg == 0 ? this.element : this.leg == 1 ? this.shell : this.leg == 2 ? -this.element : -this.shell;
        this.n++;
        return this.n > this.nlimit;
    }

    private void breakColumn(int x, int z) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int dist = this.radius2 - (x * x + z * z);
        if (dist > 0) {
            dist = (int) Math.sqrt(dist);
            for (int y = (int) (dist / this.explosionCoefficient2); y > -dist / this.explosionCoefficient; y--) {
                pos.setPos(this.posX + x, this.posY + y, this.posZ + z);
                if (this.world.getBlockState(pos).getBlock().getExplosionResistance(this) <= 2_000_000 || pos.getY() > 0) {
                    IBlockState iblockstate = world.getBlockState(pos);
                    Block block = iblockstate.getBlock();
                    if (!block.isAir(iblockstate, world, pos)) {
                        block.dropBlockAsItemWithChance(world, pos, iblockstate, 0.02f, 0);
                        world.setBlockToAir(pos);
                    }
                }
            }
        }
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if (this.world.isRemote)
            return;
        //ProjectReflection.logger.info("Updating blackhole");

        AxisAlignedBB aabb = new AxisAlignedBB(posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius);
        List<Entity> entityList = world.getEntitiesWithinAABBExcludingEntity(this, aabb);
        //ProjectReflection.logger.info("Attracting entities");
        double strength = radius2 / 10;//GM
        double eventHorizon = strength / 60.0;
        for (Entity entity : entityList) {
            Vec3d attractionDirection = new Vec3d(posX, posY, posZ).subtract(new Vec3d(entity.posX, entity.posY, entity.posZ)).normalize();
            double distanceSq = getDistanceSq(entity);
            double dragFactor = -0.02;
            if (distanceSq <= eventHorizon * eventHorizon) {
                dragFactor = -0.5;
                //ProjectReflection.logger.info("Attacking {}",entity);
                if (entity instanceof EntityLivingBase) {
                    entity.attackEntityFrom(DamageSource.MAGIC, 3.0f);
                } else {
                    entity.setDead();
                }
            }

            Vec3d attractionForce = attractionDirection.scale(strength / distanceSq);
            Vec3d drag = new Vec3d(entity.motionX, entity.motionY, entity.motionZ).scale(dragFactor);
            Vec3d acceleration = attractionForce.subtract(drag);
            if (acceleration.dotProduct(attractionDirection) > 0) {
                entity.addVelocity(acceleration.x, acceleration.y, acceleration.z);
                entity.velocityChanged = true;
            }
        }

        for (int i = 0; i < speed; i++) {
            //ProjectReflection.logger.info("Breaking blocks");
            if (breakBlock()) {
                //ProjectReflection.logger.info("Finished breaking radius:{}",this.radius);
                this.setDead();
                break;
            }
        }
        if (world instanceof WorldServer) {
            MinecraftServer server = world.getMinecraftServer();
            double meanTickTime = mean(server.tickTimeArray) * 1.0E-6D;
            if (meanTickTime < 45.0) {
                speed++;
            } else {
                speed = Math.max(1, speed / 2);
                ProjectReflection.logger.info("Current speed:{}", speed);
            }
        }
        ProjectReflection.logger.info("Update complete");
    }
}
