package bruce.projectreflection.metatileentity.multis;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.api.MaterialContainer;
import bruce.projectreflection.recipes.chemical.Reaction;
import codechicken.lib.raytracer.CuboidRayTraceResult;
import gregtech.api.GTValues;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockWithDisplayBase;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.unification.FluidUnifier;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.DustProperty;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.stack.MaterialStack;
import gregtech.api.util.GTTransferUtils;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class MetaTileEntityStewStoolStove extends MultiblockWithDisplayBase {
    private final MaterialContainer materialContainer = new MaterialContainer();
    protected IItemHandlerModifiable inputInventory;
    protected IItemHandlerModifiable outputInventory;
    protected IMultipleTankHandler inputFluidInventory;
    protected IMultipleTankHandler outputFluidInventory;
    boolean outputMode = false;

    private double temperature = -1.0;
    public MetaTileEntityStewStoolStove(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
        resetTileAbilities();
    }

    @Override
    public boolean hasMaintenanceMechanics() {
        return false;
    }
    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        initializeAbilities();
        if (temperature < 0) {
            temperature = getBiomeTemperature();
        }
    }
    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        resetTileAbilities();
    }


    private double getBiomeTemperature() {
        Biome biome = getWorld().getBiome(this.getPos());
        return (biome.getDefaultTemperature() * 58.88 + 213.39);
    }
    protected void initializeAbilities() {
        this.inputInventory = new ItemHandlerList(getAbilities(MultiblockAbility.IMPORT_ITEMS));
        this.inputFluidInventory = new FluidTankList(true, getAbilities(MultiblockAbility.IMPORT_FLUIDS));
        this.outputInventory = new ItemHandlerList(getAbilities(MultiblockAbility.EXPORT_ITEMS));
        this.outputFluidInventory = new FluidTankList(true, getAbilities(MultiblockAbility.EXPORT_FLUIDS));
        //this.energyContainer = new EnergyContainerList(getAbilities(MultiblockAbility.INPUT_ENERGY));
    }

    private void resetTileAbilities() {
        this.inputInventory = new ItemStackHandler(0);
        this.inputFluidInventory = new FluidTankList(true);
        this.outputInventory = new ItemStackHandler(0);
        this.outputFluidInventory = new FluidTankList(true);
        //this.energyContainer = new EnergyContainerList(Lists.newArrayList());
    }

    @Override
    public TraceabilityPredicate autoAbilities() {
        TraceabilityPredicate predicate = super.autoAbilities();
        return predicate.or(abilities(MultiblockAbility.IMPORT_ITEMS))
                .or(abilities(MultiblockAbility.IMPORT_FLUIDS))
                .or(abilities(MultiblockAbility.EXPORT_ITEMS))
                .or(abilities(MultiblockAbility.EXPORT_FLUIDS));
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        textList.add(new TextComponentTranslation("Temperature:%s °C", String.format("%.2f", temperature - 273.15)));
        for (MaterialStack stack : materialContainer) {
            textList.add(new TextComponentTranslation("%s * %s mB", stack.material.getLocalizedName(),
                    String.format("%.3g", (double) stack.amount / (GTValues.M / 144))));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data = super.writeToNBT(data);
        data.setTag("Contents", materialContainer.writeToNBT());
        data.setDouble("Temperature", temperature);
        data.setBoolean("outputMode", outputMode);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        materialContainer.readFromNBT(data.getTagList("Contents", Constants.NBT.TAG_COMPOUND));
        temperature = data.getDouble("Temperature");
        outputMode = data.getBoolean("outputMode");
    }

    private void doInput() {
        int slots = this.inputInventory.getSlots();
        for (int i = 0; i < slots; i++) {
            ItemStack stack = inputInventory.getStackInSlot(i);
            if (stack != null && !stack.isEmpty()) {
                MaterialStack materialStack = OreDictUnifier.getMaterial(stack);
                if (materialStack != null && materialStack.amount > 0) {
                    materialContainer.insertMaterial(materialStack);
                    stack.shrink(1);
                }
            }
        }
        FluidStack fluidStack = inputFluidInventory.drain(1000, false);
        if (fluidStack != null) {
            Material material = FluidUnifier.getMaterialFromFluid(fluidStack.getFluid());
            if (material != null) {
                materialContainer.insertMaterial(new MaterialStack(material, fluidStack.amount * (GTValues.M / 144)));
                inputFluidInventory.drain(fluidStack, true);
            }
        }
    }

    private void doChemicalReaction() {
        Reaction[] reactions = materialContainer.findAllReactions(this.temperature);
        if (reactions.length != 0) {
            for (int i = 0; i < (int) temperature; i++) {
                Reaction reaction = reactions[PRConstants.rand.nextInt(reactions.length)];
                if (reaction.canHappenAt(this.temperature)) {
                    ProjectReflection.logger.info("Reacting {}, dH={},criticalTemperature={}", reaction.getReactionEquation(),
                            reaction.getDh(), reaction.getCriticalTemperature());
                    this.temperature -= materialContainer.react(reaction);
                }
            }
        }
        materialContainer.invalidate();
    }

    private void doOutput() {
        for (MaterialStack materialStack : this.materialContainer) {
            if (materialStack.material.hasProperty(PropertyKey.DUST) && materialStack.amount >= GTValues.M) {
                ItemStack dustStack = OreDictUnifier.getDust(materialStack);
                if (GTTransferUtils.addItemsToItemHandler(this.outputInventory, true, Collections.singletonList(dustStack))) {
                    MaterialStack extracted = OreDictUnifier.getMaterial(dustStack);
                    this.materialContainer.extractMaterial(new MaterialStack(extracted.material, extracted.amount * dustStack.getCount()), false);
                    GTTransferUtils.addItemsToItemHandler(this.outputInventory, false, Collections.singletonList(dustStack));
                }
            } else if (materialStack.material.hasProperty(PropertyKey.FLUID) && materialStack.amount >= (GTValues.M / 144)) {
                Fluid fluid = materialStack.material.getFluid();
                int mB = (int) (materialStack.amount * 144L / GTValues.M);
                FluidStack fluidStack = new FluidStack(fluid, mB);
                if (GTTransferUtils.addFluidsToFluidHandler(this.outputFluidInventory, true, Collections.singletonList(fluidStack))) {
                    MaterialStack extracted = new MaterialStack(materialStack.material, mB * (GTValues.M / 144));
                    this.materialContainer.extractMaterial(extracted, false);
                    GTTransferUtils.addFluidsToFluidHandler(this.outputFluidInventory, false, Collections.singletonList(fluidStack));
                }
            }
        }
    }

    @Override
    protected void updateFormedValid() {
        if (this.getOffsetTimer() % 20 == 0) {
            if (!outputMode)
                doInput();
            doChemicalReaction();
            //Biome biome=getWorld().getBiome(this.getPos());
            double difference = getBiomeTemperature() - temperature;
            this.temperature += 0.01 * difference;
            if (outputMode)
                doOutput();
        }
    }

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXXXX", "XYYYX", "XYYYX")
                .aisleRepeatable(3, 14, "XXXXX", "Y###Y", "Y###Y")
                .aisle("XXXXX", "XY@YX", "XYYYX")
                .where('X', states(MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.PRIMITIVE_BRICKS)))
                .where('Y', states(MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.PRIMITIVE_BRICKS)).or(this.autoAbilities()))
                .where('#', air())
                .where('@', selfPredicate())
                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return Textures.PRIMITIVE_BRICKS;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityStewStoolStove(this.metaTileEntityId);
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, CuboidRayTraceResult hitResult) {
        this.outputMode = !outputMode;
        return true;
    }
}
