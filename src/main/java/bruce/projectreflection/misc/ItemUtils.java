package bruce.projectreflection.misc;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLong;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class ItemUtils {
    public static boolean hasUUID(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            return false;
        return compound.hasKey("uuidMost") && compound.hasKey("uuidLeast");
    }

    public static void setUUID(ItemStack stack, UUID uuid) {
        stack.setTagInfo("uuidMost", new NBTTagLong(uuid.getMostSignificantBits()));
        stack.setTagInfo("uuidLeast", new NBTTagLong(uuid.getLeastSignificantBits()));
    }

    @Nullable
    public static UUID getUUID(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            return null;
        if (!compound.hasKey("uuidMost") || !compound.hasKey("uuidLeast"))
            return null;
        long uuidMost = compound.getLong("uuidMost");
        long uuidLeast = compound.getLong("uuidLeast");
        return new UUID(uuidMost, uuidLeast);
    }

    public static UUID redispatchUUID(ItemStack stack) {
        UUID uuid = UUID.randomUUID();
        setUUID(stack, uuid);
        return uuid;
    }

    @Nonnull
    public static UUID initOrGetUUID(ItemStack stack) {
        if (hasUUID(stack)) {
            return Objects.requireNonNull(getUUID(stack));
        } else {
            return redispatchUUID(stack);
        }
    }

    public static ItemStack stripUUID_NBTs(ItemStack stack) {
        if (!ItemUtils.hasUUID(stack))
            return stack;
        ItemStack stack1 = stack.copy();
        NBTTagCompound compound = stack1.getTagCompound();
        if (compound != null) {
            compound.removeTag("uuidMost");
            compound.removeTag("uuidLeast");
        }
        return stack1;
    }

}
