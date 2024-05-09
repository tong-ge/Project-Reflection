package bruce.projectreflection.metatileentity.multis;

import bruce.projectreflection.api.MaterialContainer;
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
import gregtech.api.unification.stack.MaterialStack;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MetaTileEntityStewStoolStove extends MultiblockWithDisplayBase {
    private final MaterialContainer materialContainer = new MaterialContainer();
    protected IItemHandlerModifiable inputInventory;
    protected IItemHandlerModifiable outputInventory;
    protected IMultipleTankHandler inputFluidInventory;
    protected IMultipleTankHandler outputFluidInventory;
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
    }
    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        resetTileAbilities();
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
        for (MaterialStack stack : materialContainer) {
            textList.add(new TextComponentString(stack.toString()));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data = super.writeToNBT(data);
        data.setTag("Stacks", materialContainer.writeToNBT());
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        materialContainer.readFromNBT(data.getTagList("Stacks", Constants.NBT.TAG_COMPOUND));
    }

    @Override
    protected void updateFormedValid() {
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
}
