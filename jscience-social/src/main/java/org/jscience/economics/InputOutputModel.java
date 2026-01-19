package org.jscience.economics;


/**
 * Leontief Input-Output Model for inter-industry dependencies.
 * (I - A)X = D
 */
public final class InputOutputModel {

    private InputOutputModel() {}

    /**
     * Calculates total production X required to meet final demand D.
     * X = (I - A)^-1 * D
     * 
     * @param technicalCoefficients Matrix A (n x n)
     * @param finalDemand Vector D
     * @return Total output Vector X
     */
    public static double[] solveTotalOutput(double[][] technicalCoefficients, double[] finalDemand) {
        int n = finalDemand.length;
        double[][] iminusA = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                iminusA[i][j] = (i == j ? 1.0 : 0.0) - technicalCoefficients[i][j];
            }
        }

        // Solve using Gaussian elimination (simplified for small N)
        return solveSystem(iminusA, finalDemand);
    }

    /**
     * Calculates the Leontief Inverse (I - A)^-1.
     * Elements represent total impact (direct + indirect).
     */
    public static double[][] calculateLeontiefInverse(double[][] a) {
        int n = a.length;
        double[][] identity = new double[n][n];
        double[][] iminusA = new double[n][n];
        for (int i = 0; i < n; i++) {
            identity[i][i] = 1.0;
            for (int j = 0; j < n; j++) {
                iminusA[i][j] = (i == j ? 1 : 0) - a[i][j];
            }
        }
        return invert(iminusA);
    }

    /**
     * Calculates multipliers for each sector.
     * Sum of columns in Leontief Inverse.
     */
    public static double[] calculateMultipliers(double[][] leontiefInverse) {
        int n = leontiefInverse.length;
        double[] multipliers = new double[n];
        for (int j = 0; j < n; j++) {
            double sum = 0;
            for (int i = 0; i < n; i++) {
                sum += leontiefInverse[i][j];
            }
            multipliers[j] = sum;
        }
        return multipliers;
    }

    private static double[] solveSystem(double[][] m, double[] b) {
        int n = b.length;
        double[][] a = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(m[i], 0, a[i], 0, n);
            a[i][n] = b[i];
        }

        for (int i = 0; i < n; i++) {
            // Find pivot
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(a[j][i]) > Math.abs(a[pivot][i])) pivot = j;
            }
            double[] temp = a[i]; a[i] = a[pivot]; a[pivot] = temp;

            for (int j = i + 1; j < n; j++) {
                double factor = a[j][i] / a[i][i];
                for (int k = i; k <= n; k++) a[j][k] -= factor * a[i][k];
            }
        }

        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) sum += a[i][j] * x[j];
            x[i] = (a[i][n] - sum) / a[i][i];
        }
        return x;
    }

    private static double[][] invert(double[][] m) {
        int n = m.length;
        double[][] res = new double[n][n];
        for (int i = 0; i < n; i++) {
            double[] b = new double[n];
            b[i] = 1.0;
            double[] x = solveSystem(m, b);
            for (int j = 0; j < n; j++) res[j][i] = x[j];
        }
        return res;
    }
}
