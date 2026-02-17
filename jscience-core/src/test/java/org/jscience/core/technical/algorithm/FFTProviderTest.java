package org.jscience.core.technical.algorithm;

import org.jscience.core.technical.algorithm.fft.MulticoreFFTProvider;
import org.jscience.core.technical.algorithm.fft.StandardFFTProvider;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FFTProviderTest {

    private final FFTProvider standard = new StandardFFTProvider();
    private final FFTProvider multicore = new MulticoreFFTProvider();

    @Test
    void test2DReversibilityDouble_Standard() {
        test2DReversibilityDouble(standard, 16, 16);
    }

    @Test
    void test2DReversibilityDouble_Multicore() {
        test2DReversibilityDouble(multicore, 64, 64);
    }

    @Test
    void test3DReversibilityDouble_Standard() {
        test3DReversibilityDouble(standard, 8, 8, 8);
    }

    @Test
    void test3DReversibilityDouble_Multicore() {
        test3DReversibilityDouble(multicore, 16, 16, 16);
    }

    private void test2DReversibilityDouble(FFTProvider provider, int rows, int cols) {
        double[][] real = new double[rows][cols];
        double[][] imag = new double[rows][cols];
        Random rand = new Random(42);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                real[i][j] = rand.nextDouble();
                imag[i][j] = rand.nextDouble();
            }
        }

        double[][][] transformed = provider.transform2D(real, imag);
        double[][][] restored = provider.inverseTransform2D(transformed[0], transformed[1]);

        double maxError = 0.0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maxError = Math.max(maxError, Math.abs(real[i][j] - restored[0][i][j]));
                maxError = Math.max(maxError, Math.abs(imag[i][j] - restored[1][i][j]));
            }
        }
        System.out.println(provider.getName() + " 2D Error: " + maxError);
        assertTrue(maxError < 1e-10, "2D FFT Reversibility failed for " + provider.getName());
    }

    private void test3DReversibilityDouble(FFTProvider provider, int n, int m, int d) {
        double[][][] real = new double[n][m][d];
        double[][][] imag = new double[n][m][d];
        Random rand = new Random(42);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < d; k++) {
                    real[i][j][k] = rand.nextDouble();
                    imag[i][j][k] = rand.nextDouble();
                }
            }
        }

        double[][][][] transformed = provider.transform3D(real, imag);
        double[][][][] restored = provider.inverseTransform3D(transformed[0], transformed[1]);

        double maxError = 0.0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < d; k++) {
                    maxError = Math.max(maxError, Math.abs(real[i][j][k] - restored[0][i][j][k]));
                    maxError = Math.max(maxError, Math.abs(imag[i][j][k] - restored[1][i][j][k]));
                }
            }
        }
        System.out.println(provider.getName() + " 3D Error: " + maxError);
        assertTrue(maxError < 1e-10, "3D FFT Reversibility failed for " + provider.getName());
    }
}
