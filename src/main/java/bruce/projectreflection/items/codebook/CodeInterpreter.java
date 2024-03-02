package bruce.projectreflection.items.codebook;

import bruce.projectreflection.misc.StringUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.Locale;

public class CodeInterpreter {
    private static void doJump(int pc, ItemStack stack) {
        setPC(stack, pc);
    }

    private static int getAddressOrValue(String anno, ItemStack stack) {
        if (StringUtils.isNumeric(anno)) {
            return Integer.parseInt(anno);
        } else if (anno.startsWith("*")) {
            int address = getAddressOrValue(anno.substring(1), stack);
            return getAddressOrValue(getContent(stack, address), stack);
        } else if (anno.equals("a") || anno.equals("acc")) {
            return getAcc(stack);
        }
        return 0;
    }

    public static boolean interpretCode(EntityPlayer player, ItemStack stack, String code) {
        String lowerCode = code.toLowerCase(Locale.ENGLISH);
        try {
            if (lowerCode.equals("hlt")) {
                return false;
            }
            if (lowerCode.startsWith("jmp ")) {
                int pc = getAddressOrValue(lowerCode.substring(4), stack);
                doJump(pc, stack);
                return false;
            }
            if (lowerCode.startsWith("mov ")) {
                String params = lowerCode.substring(4);
                if (params.startsWith("acc,")) {
                    int value = getAddressOrValue(params.substring(4), stack);
                    setAcc(stack, value);
                } else if (params.startsWith("*")) {
                    int comma = params.indexOf(",");
                    int addressCode = getAddressOrValue(params.substring(1, comma), stack);
                    int value = getAddressOrValue(params.substring(comma + 1), stack);
                    setContent(stack, addressCode, String.valueOf(value));
                }
            }
            if (lowerCode.startsWith("add ")) {
                int pc = getAddressOrValue(lowerCode.substring(4), stack);
                setAcc(stack, getAcc(stack) + pc);
            }
            if (lowerCode.startsWith("sub ")) {
                int pc = getAddressOrValue(lowerCode.substring(4), stack);
                setAcc(stack, getAcc(stack) + pc);
            }
        } catch (Exception e) {
            player.sendMessage(new TextComponentString(e.getMessage() + " at " + code).setStyle(new Style().setColor(TextFormatting.RED)));
        }
        return true;
    }

    static String getContent(ItemStack stack, int i) {

        return stack.getTagCompound().getString("content" + i);
    }

    static int getPC(ItemStack stack) {
        return stack.getTagCompound().getInteger("pc");
    }

    static void setPC(ItemStack stack, int pc) {
        stack.getTagCompound().setInteger("pc", pc);
    }

    private static int getAcc(ItemStack stack) {
        return stack.getTagCompound().getInteger("acc");
    }

    private static void setAcc(ItemStack stack, int pc) {
        stack.getTagCompound().setInteger("acc", pc);
    }

    static void setContent(ItemStack stack, int i, String content) {
        stack.getTagCompound().setString("content" + i, content);
    }
}
