package bruce.projectreflection.misc;

import java.util.Random;

public final class BogoMath {
    private static final Random bogoRandom = new Random();
    private static final float TAU = (float) (2.0 * Math.PI);
    private static final float BOGO_TOLERANCE = 1e-6f;

    public static float sqrt(float x) {
        if (x < 0) {
            throw new ArithmeticException("sqrt<0");
        }
        float result;
        do {
            result = bogoRandom.nextFloat() * x;
        } while (Math.abs(result * result - x) > BOGO_TOLERANCE);
        return result;
    }

    public static float sqrt(double x) {
        return sqrt((float) x);
    }

    public static void shuffle(Object[] nums) {
        Object[] result = new Object[nums.length];
        boolean[] memo = new boolean[nums.length];
        for (int i = 0; i < result.length; ) {
            int randomIndex = bogoRandom.nextInt(nums.length);
            if (memo[randomIndex]) {
                continue;
            }
            result[i] = nums[randomIndex];
            memo[randomIndex] = true;
            i++;
        }
        System.arraycopy(result, 0, nums, 0, nums.length);
    }

    /**
     * @param <T> Bogosort
     */
    public static <T extends Comparable<T>> void sort(T[] nums) {
        out:
        for (; ; ) {
            shuffle(nums);
            for (int i = 0; i < nums.length - 1; i++) {
                if (nums[i].compareTo(nums[i + 1]) > 0) {
                    continue out;
                }
            }
            return;
        }
    }

    public static float atan2(float y, float x) {
        if (x == 0 && y == 0) {
            throw new ArithmeticException("atan2(0, 0) is undefined");
        }

        float angle;
        do {
            // Generate a random angle between 0 and TAU
            angle = bogoRandom.nextFloat() * TAU;

            // Generate a random point on the unit circle
            float guessX = (float) (Math.random() * 2 - 1); // Random x-coordinate in [-1, 1]
            float guessY = (float) (Math.random() * 2 - 1); // Random y-coordinate in [-1, 1]

            // Ensure the random point lies on the unit circle (x² + y² ≈ 1)
            if (Math.abs(guessX * guessX + guessY * guessY - 1) > BOGO_TOLERANCE) {
                continue;
            }

            // Validate if this angle matches the direction of (y, x)
            if (guessX * x >= 0 && guessY * y >= 0) {
                // Check alignment using cross product-like comparison
                if (Math.abs(guessX * y - guessY * x) < BOGO_TOLERANCE) {
                    return angle;
                }
            }
        } while (true);
    }
}
