package bruce.projectreflection.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

public class NuclearExplosion {
    private final World world;
    private final BlockPos blockPos;
    private final int radius;
    private final PlayerChunkMap playerChunkMap;
    private static final DamageSource damageSource=new DamageSource("nuclear")
            .setExplosion().setDamageBypassesArmor().setDamageAllowedInCreativeMode();
    public NuclearExplosion(World world,BlockPos pos,int radius)
    {
        this.world=world;
        this.blockPos=pos;
        this.radius=radius;
        if(world instanceof WorldServer)
        {
            playerChunkMap=((WorldServer) world).getPlayerChunkMap();
        }
        else {
            playerChunkMap=null;
        }
    }
    private void fillChunk(ChunkPos chunkPos)
    {
        Chunk chunk=world.getChunk(chunkPos.x,chunkPos.z);
        int xMax=chunkPos.getXEnd();
        int zMax=chunkPos.getZEnd();
        int xMin=chunkPos.getXStart();
        int zMin=chunkPos.getZStart();
        for(int x=xMin;x<=xMax;x++)
            for(int z=zMin;z<=zMax;z++)
            {
                int yMax=chunk.getHeightValue(x&15,z&15);
                for (int y=yMax;y>=1;y--)
                {
                    world.setBlockState(new BlockPos(x,y,z),Blocks.AIR.getDefaultState(),2);
                }
            }
        List<Entity> entityList=world.getEntitiesWithinAABB(Entity.class,
                new AxisAlignedBB(chunkPos.getXStart(),1, chunkPos.getZStart(),
                        chunkPos.getXEnd(), world.getHeight(),chunkPos.getZEnd()));
        for(Entity entity:entityList)
        {
            entity.attackEntityFrom(damageSource,Float.MAX_VALUE);
            if(entity instanceof EntityLivingBase)
            {
                ((EntityLivingBase) entity).setHealth(0);
                ((EntityLivingBase) entity).onDeath(damageSource);
            }
        }
        if(playerChunkMap!=null)
        {

            PlayerChunkMapEntry entry=playerChunkMap.getEntry(chunkPos.x,chunkPos.z);
            if (entry != null) {
                entry.sendPacket(new SPacketChunkData(chunk,65535));
            }
        }
    }
    public void doExplosion()
    {
        ChunkPos origin=new ChunkPos(blockPos);
        for(int x=origin.x-radius;x<=origin.x+radius;x++)
            for (int z= origin.z-radius;z<= origin.z+radius;z++)
            {
                fillChunk(new ChunkPos(x,z));
            }
    }
}
