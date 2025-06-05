package bruce.projectreflection.lib;

import gregtech.api.GTValues;
import gregtech.api.GregTechAPI;
import gregtech.api.util.GTUtility;

import java.util.Random;

public class FloatEnergy {
    public static double FU_PER_EU = (double) V((byte) GTValues.OpV) / (double) GTValues.V[GTValues.OpV];

    public static float V(byte tier) {
        return Float.intBitsToFloat((tier + 1) << 24);
    }

    public static float VA(byte tier) {
        return Float.intBitsToFloat((tier << 24) | 0x00f00000);
    }

    public static float VH(byte tier) {
        return Float.intBitsToFloat((tier << 24) | 0x00800000);
    }

    public static float VHA(byte tier) {
        return Float.intBitsToFloat((tier << 24) | 0x00700000);
    }

    public static float euToFU(long eu) {
        return (float) (eu * FU_PER_EU);
    }

    public static long fuToEU(float fu) {
//return (long) ((double)fu/FU_PER_EU);
        int fuBits = Float.floatToRawIntBits(fu);
        int tier = fuBits >>> 24;
        if (tier > 32) {
            throw new IllegalArgumentException(String.format("Tier: %d in %x is too big", tier, fuBits));
        }
        return (long) fuBits << (tier * 2 - 16);
    }

    public static byte getTier(float fu) {
        if (Float.floatToRawIntBits(fu) == 0 || Float.floatToRawIntBits(fu) == Integer.MIN_VALUE)
            return 0;
        byte rawTier = (byte) (Float.floatToRawIntBits(fu) >>> 24);
        return (Float.floatToRawIntBits(fu) & 0x00ffffff) == 0 ? (byte) (rawTier + 1) : rawTier;
    }

    public static void main(String[] args) {
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            long EUt = rand.nextInt() & Integer.MAX_VALUE;
            float FUt = euToFU(EUt);
            int tier = GTUtility.getTierByVoltage(EUt);
            int floatTier = getTier(FUt);
            assert tier == floatTier;
            if (tier < 32) {
                System.out.printf("Testing %d EU/t to %g FU/t(approximate to %d EU/t) at %d tier(max=%d EU/t)\n", EUt, FUt, fuToEU(EUt), tier, 8L << (tier * 2));
            }
        }
    }
}
