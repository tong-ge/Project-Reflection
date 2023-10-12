package bruce.projectreflection.metatileentity;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.PRRecipeMaps;
import bruce.projectreflection.misc.NuclearExplosion;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.FuelMultiblockController;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.unification.material.Materials;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.List;

public class MetaTileEntityFission extends FuelMultiblockController {
    public MetaTileEntityFission() {
        super(new ResourceLocation(PRConstants.modid,"fission"),
                PRRecipeMaps.MODULAR_FISSION,
                4);
        this.recipeMapWorkable=new FissionWorkableHandler(this);
    }

    public double temperature=0.0;
    public int heatMultiplier=1;
    public static final double MAX_TEMP=10000.0;
    @Nonnull
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXDXX","XXXXX","XXXXX","XXXXX","XXXXX")
                .aisle("XXXXX","XOOOX","XOOOX","XOOOX","XXXXX")
                .aisle("XXXXX","XOOOX","XOOOX","XOOOX","XXXXX")
                .aisle("XXXXX","XOOOX","XOOOX","XOOOX","XXXXX")
                .aisle("XXXXX","XXCXX","XXXXX","XXXXX","XXXXX")
                .where('X',
                        states(MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.TUNGSTENSTEEL_ROBUST))
                                .setMinGlobalLimited(75)
                                .or(this.autoAbilities(false,true,true,true,true,true,true)))
                .where('D', abilities(MultiblockAbility.OUTPUT_ENERGY))
                .where('O',frames(Materials.TungstenSteel))
                .where('C',this.selfPredicate())
                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return Textures.ROBUST_TUNGSTENSTEEL_CASING;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntityFission();
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        textList.add(new TextComponentTranslation("堆温：%s%%",
                new DecimalFormat("00.00").format(getTemperaturePercentage()*100.0)));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setDouble("temperature",temperature);
        data.setInteger("heatMultiplier",heatMultiplier);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        temperature= data.getDouble("temperature");
        heatMultiplier=data.getInteger("heatMultiplier");
    }

    @Override
    public void writeInitialSyncData(PacketBuffer buf) {
        super.writeInitialSyncData(buf);
        buf.writeDouble(temperature);
    }

    @Override
    public void receiveInitialSyncData(PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        temperature=buf.readDouble();
    }
    public double getTemperaturePercentage()
    {
        return temperature/MAX_TEMP;
    }
    public void checkExplode()
    {
        if(temperature>MAX_TEMP)
        {
            explodeMultiblock(48.0f);
        }
    }

    @Override
    public void doExplosion(float explosionPower) {
        this.setExploded();
        this.getWorld().setBlockToAir(this.getPos());
        new NuclearExplosion(this.getWorld(),this.getPos(),(int)Math.ceil(explosionPower/16.0f)).doExplosion();
    }
}
