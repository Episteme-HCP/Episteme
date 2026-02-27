/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.analysis.fft.providers;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.analysis.fft.FFTProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.numbers.complex.Complex;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * Multicore implementation of FFTProvider using Fork/Join framework.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({FFTProvider.class})
public class MulticoreFFTProvider implements FFTProvider {

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public String getName() {
        return "Java Multicore FFT (CPU)";
    }

    @Override
    public Real[][] transform(Real[] real, Real[] imag) {
        return computeFFT(real, imag, true);
    }

    @Override
    public Real[][] inverseTransform(Real[] real, Real[] imag) {
        int n = real.length;
        Real[] negImag = new Real[n];
        for (int i = 0; i < n; i++) negImag[i] = imag[i].negate();
        Real[][] result = computeFFT(real, negImag, true);
        Real scale = Real.of(1.0 / n);
        for (int i = 0; i < n; i++) {
            result[0][i] = result[0][i].multiply(scale);
            result[1][i] = result[1][i].multiply(scale).negate();
        }
        return result;
    }

    @Override
    public double[][] transform(double[] real, double[] imag) {
        return computeFFTPrimitive(real, imag, true);
    }

    @Override
    public double[][] inverseTransform(double[] real, double[] imag) {
        int n = real.length;
        double[] negImag = new double[n];
        for (int i = 0; i < n; i++) negImag[i] = -imag[i];
        double[][] result = computeFFTPrimitive(real, negImag, true);
        double scale = 1.0 / n;
        for (int i = 0; i < n; i++) {
            result[0][i] *= scale;
            result[1][i] = -(result[1][i] * scale);
        }
        return result;
    }

    @Override
    public Complex[] transformComplex(Complex[] data) {
        int n = data.length;
        Real[] real = new Real[n];
        Real[] imag = new Real[n];
        for (int i = 0; i < n; i++) {
            real[i] = data[i].getReal();
            imag[i] = data[i].getImaginary();
        }
        Real[][] res = transform(real, imag);
        Complex[] out = new Complex[n];
        for (int i = 0; i < n; i++) out[i] = Complex.of(res[0][i], res[1][i]);
        return out;
    }

    @Override
    public Complex[] inverseTransformComplex(Complex[] data) {
        int n = data.length;
        Real[] real = new Real[n];
        Real[] imag = new Real[n];
        for (int i = 0; i < n; i++) {
            real[i] = data[i].getReal();
            imag[i] = data[i].getImaginary();
        }
        Real[][] res = inverseTransform(real, imag);
        Complex[] out = new Complex[n];
        for (int i = 0; i < n; i++) out[i] = Complex.of(res[0][i], res[1][i]);
        return out;
    }

    // ========== 2D PARALLEL FFT ==========

    @Override
    public double[][][] transform2D(double[][] real, double[][] imag) {
        return computeFFT2D(real, imag, false);
    }

    @Override
    public double[][][] inverseTransform2D(double[][] real, double[][] imag) {
        return computeFFT2D(real, imag, true);
    }

    @Override
    public Real[][][] transform2D(Real[][] real, Real[][] imag) {
        return computeFFT2DReal(real, imag, false);
    }

    @Override
    public Real[][][] inverseTransform2D(Real[][] real, Real[][] imag) {
        return computeFFT2DReal(real, imag, true);
    }

    // ========== 3D PARALLEL FFT ==========

    @Override
    public double[][][][] transform3D(double[][][] real, double[][][] imag) {
        return computeFFT3D(real, imag, false);
    }

    @Override
    public double[][][][] inverseTransform3D(double[][][] real, double[][][] imag) {
        return computeFFT3D(real, imag, true);
    }

    @Override
    public Real[][][][] transform3D(Real[][][] real, Real[][][] imag) {
        return computeFFT3DReal(real, imag, false);
    }

    @Override
    public Real[][][][] inverseTransform3D(Real[][][] real, Real[][][] imag) {
        return computeFFT3DReal(real, imag, true);
    }

    // --- Helpers 2D ---

    private double[][][] computeFFT2D(double[][] real, double[][] imag, boolean inverse) {
        int rows = real.length;
        int cols = real[0].length;
        Complex[][] data = new Complex[rows][cols];

        // Parallel Copy
        IntStream.range(0, rows).parallel().forEach(i -> {
            for (int j = 0; j < cols; j++) {
                data[i][j] = Complex.of(real[i][j], imag[i][j]);
            }
        });

        // 1. Rows (Parallel)
        IntStream.range(0, rows).parallel().forEach(i -> {
            data[i] = basicRecursiveFFT(data[i], inverse);
        });

        // 2. Columns (Parallel)
        IntStream.range(0, cols).parallel().forEach(j -> {
            Complex[] col = new Complex[rows];
            for (int i = 0; i < rows; i++) col[i] = data[i][j];
            col = basicRecursiveFFT(col, inverse);
            for (int i = 0; i < rows; i++) data[i][j] = col[i];
        });

        double[][][] out = new double[2][rows][cols];
        double scale = inverse ? 1.0 / (rows * cols) : 1.0;

        IntStream.range(0, rows).parallel().forEach(i -> {
            for (int j = 0; j < cols; j++) {
                out[0][i][j] = data[i][j].real() * scale;
                out[1][i][j] = data[i][j].imaginary() * scale;
            }
        });
        return out;
    }

    private Real[][][] computeFFT2DReal(Real[][] real, Real[][] imag, boolean inverse) {
        int rows = real.length;
        int cols = real[0].length;
        Complex[][] data = new Complex[rows][cols];

        IntStream.range(0, rows).parallel().forEach(i -> {
            for (int j = 0; j < cols; j++) {
                data[i][j] = Complex.of(real[i][j], imag[i][j]);
            }
        });

        IntStream.range(0, rows).parallel().forEach(i -> {
            data[i] = basicRecursiveFFT(data[i], inverse);
        });

        IntStream.range(0, cols).parallel().forEach(j -> {
            Complex[] col = new Complex[rows];
            for (int i = 0; i < rows; i++) col[i] = data[i][j];
            col = basicRecursiveFFT(col, inverse);
            for (int i = 0; i < rows; i++) data[i][j] = col[i];
        });

        Real[][][] out = new Real[2][rows][cols];
        Real scale = inverse ? Real.ONE.divide(Real.of(rows * cols)) : Real.ONE;

        IntStream.range(0, rows).parallel().forEach(i -> {
            for (int j = 0; j < cols; j++) {
                out[0][i][j] = data[i][j].getReal().multiply(scale);
                out[1][i][j] = data[i][j].getImaginary().multiply(scale);
            }
        });
        return out;
    }

    // --- Helpers 3D ---

    private double[][][][] computeFFT3D(double[][][] real, double[][][] imag, boolean inverse) {
        int n = real.length;
        int m = real[0].length;
        int d = real[0][0].length;
        Complex[][][] data = new Complex[n][m][d];

        IntStream.range(0, n).parallel().forEach(i -> {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < d; k++) {
                    data[i][j][k] = Complex.of(real[i][j][k], imag[i][j][k]);
                }
            }
        });

        // 1. Z-dimension (Depth)
        IntStream.range(0, n).parallel().forEach(i -> {
            for (int j = 0; j < m; j++) {
                data[i][j] = basicRecursiveFFT(data[i][j], inverse);
            }
        });

        // 2. Y-dimension (Cols)
        IntStream.range(0, n).parallel().forEach(i -> {
            Complex[] buffer = new Complex[m];
            for (int k = 0; k < d; k++) {
                for (int j = 0; j < m; j++) buffer[j] = data[i][j][k];
                buffer = basicRecursiveFFT(buffer, inverse);
                for (int j = 0; j < m; j++) data[i][j][k] = buffer[j];
            }
        });

        // 3. X-dimension (Rows)
        IntStream.range(0, m).parallel().forEach(j -> {
            Complex[] buffer = new Complex[n];
            for (int k = 0; k < d; k++) {
                for (int i = 0; i < n; i++) buffer[i] = data[i][j][k];
                buffer = basicRecursiveFFT(buffer, inverse);
                for (int i = 0; i < n; i++) data[i][j][k] = buffer[i];
            }
        });

        double[][][][] out = new double[2][n][m][d];
        double scale = inverse ? 1.0 / (n * m * d) : 1.0;

        IntStream.range(0, n).parallel().forEach(i -> {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < d; k++) {
                    out[0][i][j][k] = data[i][j][k].real() * scale;
                    out[1][i][j][k] = data[i][j][k].imaginary() * scale;
                }
            }
        });
        return out;
    }

    private Real[][][][] computeFFT3DReal(Real[][][] real, Real[][][] imag, boolean inverse) {
        int n = real.length;
        int m = real[0].length;
        int d = real[0][0].length;
        Complex[][][] data = new Complex[n][m][d];

        IntStream.range(0, n).parallel().forEach(i -> {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < d; k++) {
                    data[i][j][k] = Complex.of(real[i][j][k], imag[i][j][k]);
                }
            }
        });

        // 1. Z
        IntStream.range(0, n).parallel().forEach(i -> {
            for (int j = 0; j < m; j++) {
                data[i][j] = basicRecursiveFFT(data[i][j], inverse);
            }
        });

        // 2. Y
        IntStream.range(0, n).parallel().forEach(i -> {
            Complex[] buffer = new Complex[m];
            for (int k = 0; k < d; k++) {
                for (int j = 0; j < m; j++) buffer[j] = data[i][j][k];
                buffer = basicRecursiveFFT(buffer, inverse);
                for (int j = 0; j < m; j++) data[i][j][k] = buffer[j];
            }
        });

        // 3. X
        IntStream.range(0, m).parallel().forEach(j -> {
            Complex[] buffer = new Complex[n];
            for (int k = 0; k < d; k++) {
                for (int i = 0; i < n; i++) buffer[i] = data[i][j][k];
                buffer = basicRecursiveFFT(buffer, inverse);
                for (int i = 0; i < n; i++) data[i][j][k] = buffer[i];
            }
        });

        Real[][][][] out = new Real[2][n][m][d];
        Real scale = inverse ? Real.ONE.divide(Real.of(n * m * d)) : Real.ONE;

        IntStream.range(0, n).parallel().forEach(i -> {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < d; k++) {
                    out[0][i][j][k] = data[i][j][k].getReal().multiply(scale);
                    out[1][i][j][k] = data[i][j][k].getImaginary().multiply(scale);
                }
            }
        });
        return out;
    }

    private Real[][] computeFFT(Real[] real, Real[] imag, boolean parallel) {
        int n = real.length;
        if (n <= 1024 || !parallel) return computeLocalFFT(real, imag);

        int half = n / 2;
        Real[] evenReal = new Real[half], evenImag = new Real[half], oddReal = new Real[half], oddImag = new Real[half];
        for (int i = 0; i < half; i++) {
            evenReal[i] = real[2 * i]; evenImag[i] = imag[2 * i];
            oddReal[i] = real[2 * i + 1]; oddImag[i] = imag[2 * i + 1];
        }

        ForkJoinPool pool = ForkJoinPool.commonPool();
        RecursiveTask<Real[][]> evenTask = new RecursiveTask<>() { protected Real[][] compute() { return new MulticoreFFTProvider().computeFFT(evenReal, evenImag, evenReal.length > 1024); } };
        RecursiveTask<Real[][]> oddTask = new RecursiveTask<>() { protected Real[][] compute() { return new MulticoreFFTProvider().computeFFT(oddReal, oddImag, oddReal.length > 1024); } };

        pool.execute(evenTask);
        Real[][] oddResult = oddTask.invoke();
        Real[][] evenResult = evenTask.join();

        Real[] resReal = new Real[n], resImag = new Real[n];
        for (int k = 0; k < half; k++) {
            double angle = -2 * Math.PI * k / n;
            double cos = Math.cos(angle), sin = Math.sin(angle);
            double oddR = oddResult[0][k].doubleValue(), oddI = oddResult[1][k].doubleValue();
            double tReal = cos * oddR - sin * oddI, tImag = sin * oddR + cos * oddI;
            double evenR = evenResult[0][k].doubleValue(), evenI = evenResult[1][k].doubleValue();

            resReal[k] = Real.of(evenR + tReal); resImag[k] = Real.of(evenI + tImag);
            resReal[k + half] = Real.of(evenR - tReal); resImag[k + half] = Real.of(evenI - tImag);
        }
        return new Real[][] { resReal, resImag };
    }

    private Real[][] computeLocalFFT(Real[] real, Real[] imag) {
        int n = real.length;
        Complex[] data = new Complex[n];
        for (int i = 0; i < n; i++) data[i] = Complex.of(real[i], imag[i]);
        
        // Use a basic recursive FFT for the base case to avoid circularity with AlgorithmManager
        Complex[] transformed = basicRecursiveFFT(data, false);
        
        Real[] outReal = new Real[n], outImag = new Real[n];
        for (int i = 0; i < n; i++) {
            outReal[i] = transformed[i].getReal();
            outImag[i] = transformed[i].getImaginary();
        }
        return new Real[][] { outReal, outImag };
    }

    private Complex[] basicRecursiveFFT(Complex[] x, boolean inverse) {
        int n = x.length;
        if (n == 1) return new Complex[] { x[0] };

        Complex[] even = new Complex[n / 2];
        Complex[] odd = new Complex[n / 2];
        for (int i = 0; i < n / 2; i++) {
            even[i] = x[2 * i];
            odd[i] = x[2 * i + 1];
        }

        Complex[] q = basicRecursiveFFT(even, inverse);
        Complex[] r = basicRecursiveFFT(odd, inverse);

        Complex[] y = new Complex[n];
        double angle = (inverse ? 2 : -2) * Math.PI / n;
        Complex wn = Complex.of(Math.cos(angle), Math.sin(angle));
        Complex w = Complex.ONE;

        for (int k = 0; k < n / 2; k++) {
            Complex wr = w.multiply(r[k]);
            y[k] = q[k].add(wr);
            y[k + n / 2] = q[k].subtract(wr);
            w = w.multiply(wn);
        }
        return y;
    }

    private double[][] computeFFTPrimitive(double[] real, double[] imag, boolean parallel) {
        int n = real.length;
        if (n <= 1024 || !parallel) return computeLocalFFTPrimitive(real, imag);

        int half = n / 2;
        double[] evenReal = new double[half], evenImag = new double[half], oddReal = new double[half], oddImag = new double[half];
        for (int i = 0; i < half; i++) {
            evenReal[i] = real[2 * i]; evenImag[i] = imag[2 * i];
            oddReal[i] = real[2 * i + 1]; oddImag[i] = imag[2 * i + 1];
        }

        ForkJoinPool pool = ForkJoinPool.commonPool();
        RecursiveTask<double[][]> evenTask = new RecursiveTask<>() { protected double[][] compute() { return new MulticoreFFTProvider().computeFFTPrimitive(evenReal, evenImag, evenReal.length > 1024); } };
        RecursiveTask<double[][]> oddTask = new RecursiveTask<>() { protected double[][] compute() { return new MulticoreFFTProvider().computeFFTPrimitive(oddReal, oddImag, oddReal.length > 1024); } };

        pool.execute(evenTask);
        double[][] oddResult = oddTask.invoke();
        double[][] evenResult = evenTask.join();

        double[] resReal = new double[n], resImag = new double[n];
        for (int k = 0; k < half; k++) {
            double angle = -2 * Math.PI * k / n;
            double cos = Math.cos(angle), sin = Math.sin(angle);
            double oddR = oddResult[0][k], oddI = oddResult[1][k];
            double tReal = cos * oddR - sin * oddI, tImag = sin * oddR + cos * oddI;
            double evenR = evenResult[0][k], evenI = evenResult[1][k];

            resReal[k] = evenR + tReal; resImag[k] = evenI + tImag;
            resReal[k + half] = evenR - tReal; resImag[k + half] = evenI - tImag;
        }
        return new double[][] { resReal, resImag };
    }

    private double[][] computeLocalFFTPrimitive(double[] real, double[] imag) {
        int n = real.length;
        Complex[] data = new Complex[n];
        for (int i = 0; i < n; i++) data[i] = Complex.of(real[i], imag[i]);
        
        // Use a basic recursive FFT for the base case to avoid circularity with AlgorithmManager
        Complex[] transformed = basicRecursiveFFT(data, false);
        
        double[] outReal = new double[n], outImag = new double[n];
        for (int i = 0; i < n; i++) {
            outReal[i] = transformed[i].real();
            outImag[i] = transformed[i].imaginary();
        }
        return new double[][] { outReal, outImag };
    }
}
