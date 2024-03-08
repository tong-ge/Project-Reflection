package bruce.projectreflection.items.codebook;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.misc.StringUtils;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.SimpleTextWidget;
import gregtech.api.gui.widgets.TextFieldWidget;
import gregtech.api.items.gui.ItemUIFactory;
import gregtech.api.items.gui.PlayerInventoryHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemCodebook extends Item implements ItemUIFactory {
    private static final int MAX_PC = 8;

    public ItemCodebook() {
        setRegistryName(PRConstants.modid, "codebook");
        setTranslationKey("codebook");
        setCreativeTab(PRConstants.tab);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (!world.isRemote) {
            if (player.isSneaking()) {
                PlayerInventoryHolder holder = new PlayerInventoryHolder(player, hand);
                holder.openUI();
            } else {
                int pc = CodeInterpreter.getPC(heldItem);
                String instruction = CodeInterpreter.getContent(heldItem, pc);
                if (CodeInterpreter.interpretCode(player, heldItem, instruction)) {
                    CodeInterpreter.setPC(heldItem, pc + 1);
                }
            }
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, heldItem);
    }

    @Override
    public ModularUI createUI(PlayerInventoryHolder holder, EntityPlayer entityPlayer) {
        ModularUI.Builder builder = ModularUI.defaultBuilder(140);
        ItemStack stack = holder.getCurrentItem();
        initNBT(stack);
        //pc pointer
        builder.label(8, 6, "PC:", 0x000000);
        builder.widget(new TextFieldWidget(20, 4, 60, 16, true,
                () -> String.valueOf(CodeInterpreter.getPC(stack)),
                text -> CodeInterpreter.setPC(stack, StringUtils.parseIntOrZero(text)))
                .setValidator(StringUtils::isNaturalOrNull));
        //page
        builder.label(90, 6, "Page:", 0x000000);
        builder.widget(new TextFieldWidget(100, 4, 60, 16, true,
                () -> "" + getPage(stack),
                text -> setPage(stack, StringUtils.parseIntOrZero(text)))
                .setValidator(StringUtils::isNaturalOrNull));
        for (int i = 0; i < MAX_PC; i++) {
            int finalI = i;
            builder.widget(new SimpleTextWidget(10, 20 * i + 35, "", () -> CodeInterpreter.getPC(stack) == getPage(stack) * 8 + finalI ? ">" : ""));
            builder.widget(new TextFieldWidget(20, 20 * i + 30, 152, 16, true,
                    () -> CodeInterpreter.getContent(stack, getPage(stack) * 8 + finalI), content -> CodeInterpreter.setContent(stack, getPage(stack) * 8 + finalI, content))
                    .setValidator(ItemCodebook::isContentValid));
        }
        builder.bindPlayerInventory(entityPlayer.inventory, 200);
        return builder.build(holder, entityPlayer);
    }

    private static boolean isContentValid(String input) {
        return true;
    }

    private static int getPage(ItemStack stack) {
        return stack.getTagCompound().getInteger("page");
    }

    private static void setPage(ItemStack stack, int page) {
        stack.getTagCompound().setInteger("page", page);
    }

    private static void initNBT(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            tagCompound.setInteger("pc", 0);
            stack.setTagCompound(tagCompound);
        }
    }

}
