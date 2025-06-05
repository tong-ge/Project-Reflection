package bruce.projectreflection.metatileentity;

import bruce.projectreflection.PRLabs;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.items.itemhandlers.GTItemStackHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.client.renderer.ICubeRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MteHandCrankTool extends MteHandCrank {
    private final GTItemStackHandler toolInventory;
    private final Predicate<ItemStack> isTool;
    private final Consumer<ItemStack> damageTool;

    public MteHandCrankTool(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap, Predicate<ItemStack> isTool, Consumer<ItemStack> damageTool, ICubeRenderer overlay) {
        super(metaTileEntityId, recipeMap, overlay);
        toolInventory = new GTItemStackHandler(this, 1);
        this.isTool = isTool;
        this.damageTool = damageTool;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MteHandCrankTool(this.metaTileEntityId, getRecipeMap(), isTool, damageTool, renderer);
    }

    @Override
    protected ModularUI.Builder createGuiTemplate(EntityPlayer player) {
        int yOffset = 0;
        Objects.requireNonNull(getRecipeMap());
        if (getRecipeMap().getMaxInputs() >= 6 || getRecipeMap().getMaxFluidInputs() >= 6 || getRecipeMap().getMaxOutputs() >= 6 || getRecipeMap().getMaxFluidOutputs() >= 6) {
            yOffset = 9;
        }
        ModularUI.Builder builder = super.createGuiTemplate(player);
        builder.widget(new SlotWidget(this.toolInventory, 0, 79, 62 + yOffset,
                true, true, false)
                .setBackgroundTexture(GuiTextures.SLOT, GuiTextures.TOOL_SLOT_OVERLAY)
                .setTooltipText("gregtech.gui.tool_slot.tooltip")
        );
        return builder;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setTag("ToolInventory", this.toolInventory.serializeNBT());
        return data;
    }

    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.toolInventory.deserializeNBT(data.getCompoundTag("ToolInventory"));
    }

    @Override
    public boolean checkRecipe(Recipe recipe, boolean consumesIfSuccess) {
        ItemStack itemStack = toolInventory.getStackInSlot(0);
        if (isTool.test(itemStack)) {
            if (consumesIfSuccess) {
                damageTool.accept(itemStack);
                PRLabs.logger.info("Tool consumed at:", new Exception());
            }
            return true;
        }
        return false;
    }

    @Override
    public void clearMachineInventory(NonNullList<ItemStack> itemBuffer) {
        super.clearMachineInventory(itemBuffer);
        clearInventory(itemBuffer, this.toolInventory);
    }
}
