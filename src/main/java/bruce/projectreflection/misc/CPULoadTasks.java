package bruce.projectreflection.misc;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;

import java.util.function.Function;

public class CPULoadTasks {
    public static void matrixMultiplication() {
        int size = 100;
        int[][] matrixA = new int[size][size];
        int[][] matrixB = new int[size][size];
        int[][] result = new int[size][size];

        // 初始化矩阵
        initializeMatrix(matrixA);
        initializeMatrix(matrixB);

        // 执行矩阵乘法
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
    }


    public static long calculateFibonacci(int n) {
        long[] fib = new long[n + 1];
        fib[0] = 0;
        fib[1] = 1;

        for (int i = 2; i <= n; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
        }
        return fib[n];
    }

    public static void generatePrimesUpToLimit(int limit) {
        boolean[] isPrime = new boolean[limit + 1];
        for (int i = 2; i <= limit; i++) {
            isPrime[i] = true;
        }

        for (int i = 2; i * i <= limit; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= limit; j += i) {
                    isPrime[j] = false;
                }
            }
        }
    }

    public static double integrateFunction(double a, double b, Function<Number, Number> function, int numSteps) {
        double delta = (b - a) / numSteps;
        double sum = 0.0;

        for (int i = 0; i < numSteps; i++) {
            double x = a + (i + 0.5) * delta;
            double y = (double) function.apply(x); // 你可以替换为你想要积分的函数
            sum += y * delta;
        }

        return sum;
    }

    private static void initializeMatrix(int[][] matrix) {
        //int value = 1;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = PRConstants.rand.nextInt();
            }
        }
    }
}
