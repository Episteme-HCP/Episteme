package org.jscience.core.mathematics.analysis.fft.providers;

import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.analysis.fft.FFTProvider;

/**
 * Standard single-threaded implementation of FFTProvider.
 */
@AutoService({FFTProvider.class, org.jscience.core.technical.algorithm.AlgorithmProvider.class})
public class StandardFFTProvider implements FFTProvider {

    public String getName() {
        return "Java Reference (Naive)";
    }

    @Override
    public double[][] transform(double[] real, double[] imag) {
        int n = real.length;
        Complex[] data = new Complex[n];
        for (int i = 0; i < n; i++) data[i] = Complex.of(real[i], imag[i]);
        Complex[] res = computeFFT(data, false);
        double[][] out = new double[2][n];
        for (int i = 0; i < n; i++) {
            out[0][i] = res[i].real();
            out[1][i] = res[i].imaginary();
        }
        return out;
    }

    @Override
    public double[][] inverseTransform(double[] real, double[] imag) {
        int n = real.length;
        Complex[] data = new Complex[n];
        for (int i = 0; i < n; i++) data[i] = Complex.of(real[i], imag[i]);
        Complex[] res = computeFFT(data, true);
        double[][] out = new double[2][n];
        for (int i = 0; i < n; i++) {
            out[0][i] = res[i].real() / n;
            out[1][i] = res[i].imaginary() / n;
        }
        return out;
    }

    @Override
    public Real[][] transform(Real[] real, Real[] imag) {
        int n = real.length;
        Complex[] data = new Complex[n];
        for (int i = 0; i < n; i++) data[i] = Complex.of(real[i], imag[i]);
        Complex[] res = computeFFT(data, false);
        Real[][] out = new Real[2][n];
        for (int i = 0; i < n; i++) {
            out[0][i] = res[i].getReal();
            out[1][i] = res[i].getImaginary();
        }
        return out;
    }

    @Override
    public Real[][] inverseTransform(Real[] real, Real[] imag) {
        int n = real.length;
        Complex[] data = new Complex[n];
        for (int i = 0; i < n; i++) data[i] = Complex.of(real[i], imag[i]);
        Complex[] res = computeFFT(data, true);
        Real[][] out = new Real[2][n];
        Real scale = Real.of(1.0 / n);
        for (int i = 0; i < n; i++) {
            out[0][i] = res[i].getReal().multiply(scale);
            out[1][i] = res[i].getImaginary().multiply(scale);
        }
        return out;
    }

    @Override
    public Complex[] transformComplex(Complex[] data) {
        return computeFFT(data, false);
    }

    @Override
    public Complex[] inverseTransformComplex(Complex[] data) {
        Complex[] res = computeFFT(data, true);
        double n = data.length;
        for (int i = 0; i < res.length; i++) {
            res[i] = res[i].divide(Complex.of(n, 0));
        }
        return res;
    }

    private Complex[] computeFFT(Complex[] x, boolean inverse) {
        int n = x.length;
        if (n == 1) return new Complex[] { x[0] };

        Complex[] even = new Complex[n / 2];
        Complex[] odd = new Complex[n / 2];
        for (int i = 0; i < n / 2; i++) {
            even[i] = x[2 * i];
            odd[i] = x[2 * i + 1];
        }

        Complex[] q = computeFFT(even, inverse);
        Complex[] r = computeFFT(odd, inverse);

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

    // ========== 2D FFT ==========

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

    private double[][][] computeFFT2D(double[][] real, double[][] imag, boolean inverse) {
        int rows = real.length;
        int cols = real[0].length;
        Complex[][] data = new Complex[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = Complex.of(real[i][j], imag[i][j]);
            }
        }

        // 1. Transform rows
        for (int i = 0; i < rows; i++) {
            data[i] = computeFFT(data[i], inverse);
        }

        // 2. Transform columns
        Complex[] colData = new Complex[rows];
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) colData[i] = data[i][j];
            Complex[] transformedCol = computeFFT(colData, inverse);
            for (int i = 0; i < rows; i++) data[i][j] = transformedCol[i];
        }

        // Inverse scaling is handled by 1D computeFFT calls if inverse=true?
        // Wait, computeFFT(inverse=true) does NOT scale by 1/N in the private method.
        // It scales in the public inverseTransform method.
        // So for 2D inverse, we need to divide by (rows * cols).
        
        double[][][] out = new double[2][rows][cols];
        double scale = inverse ? 1.0 / (rows * cols) : 1.0;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                out[0][i][j] = data[i][j].real() * scale;
                out[1][i][j] = data[i][j].imaginary() * scale;
            }
        }
        return out;
    }

    private Real[][][] computeFFT2DReal(Real[][] real, Real[][] imag, boolean inverse) {
        int rows = real.length;
        int cols = real[0].length;
        Complex[][] data = new Complex[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = Complex.of(real[i][j], imag[i][j]);
            }
        }

        for (int i = 0; i < rows; i++) {
            data[i] = computeFFT(data[i], inverse);
        }

        Complex[] colData = new Complex[rows];
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) colData[i] = data[i][j];
            Complex[] transformedCol = computeFFT(colData, inverse);
            for (int i = 0; i < rows; i++) data[i][j] = transformedCol[i];
        }

        Real[][][] out = new Real[2][rows][cols];
        Real scale = inverse ? Real.ONE.divide(Real.of(rows * cols)) : Real.ONE;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                out[0][i][j] = data[i][j].getReal().multiply(scale);
                out[1][i][j] = data[i][j].getImaginary().multiply(scale);
            }
        }
        return out;
    }

    // ========== 3D FFT ==========

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

    private double[][][][] computeFFT3D(double[][][] real, double[][][] imag, boolean inverse) {
        int n = real.length;
        int m = real[0].length;
        int d = real[0][0].length;
        Complex[][][] data = new Complex[n][m][d];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < d; k++) {
                    data[i][j][k] = Complex.of(real[i][j][k], imag[i][j][k]);
                }
            }
        }

        // 1. Rows (last dim) - actually let's stick to x, y, z convention
        // data[x][y][z]
        // FFT along Z (depth)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                data[i][j] = computeFFT(data[i][j], inverse);
            }
        }

        // FFT along Y (cols)
        Complex[] buffer = new Complex[m];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < d; k++) {
                for (int j = 0; j < m; j++) buffer[j] = data[i][j][k];
                Complex[] res = computeFFT(buffer, inverse);
                for (int j = 0; j < m; j++) data[i][j][k] = res[j];
            }
        }

        // FFT along X (rows)
        buffer = new Complex[n];
        for (int j = 0; j < m; j++) {
            for (int k = 0; k < d; k++) {
                for (int i = 0; i < n; i++) buffer[i] = data[i][j][k];
                Complex[] res = computeFFT(buffer, inverse);
                for (int i = 0; i < n; i++) data[i][j][k] = res[i];
            }
        }

        double[][][][] out = new double[2][n][m][d];
        double scale = inverse ? 1.0 / (n * m * d) : 1.0;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < d; k++) {
                    out[0][i][j][k] = data[i][j][k].real() * scale;
                    out[1][i][j][k] = data[i][j][k].imaginary() * scale;
                }
            }
        }
        return out;
    }

    private Real[][][][] computeFFT3DReal(Real[][][] real, Real[][][] imag, boolean inverse) {
        int n = real.length;
        int m = real[0].length;
        int d = real[0][0].length;
        Complex[][][] data = new Complex[n][m][d];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < d; k++) {
                    data[i][j][k] = Complex.of(real[i][j][k], imag[i][j][k]);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                data[i][j] = computeFFT(data[i][j], inverse);
            }
        }

        Complex[] buffer = new Complex[m];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < d; k++) {
                for (int j = 0; j < m; j++) buffer[j] = data[i][j][k];
                Complex[] res = computeFFT(buffer, inverse);
                for (int j = 0; j < m; j++) data[i][j][k] = res[j];
            }
        }

        buffer = new Complex[n];
        for (int j = 0; j < m; j++) {
            for (int k = 0; k < d; k++) {
                for (int i = 0; i < n; i++) buffer[i] = data[i][j][k];
                Complex[] res = computeFFT(buffer, inverse);
                for (int i = 0; i < n; i++) data[i][j][k] = res[i];
            }
        }

        Real[][][][] out = new Real[2][n][m][d];
        Real scale = inverse ? Real.ONE.divide(Real.of(n * m * d)) : Real.ONE;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < d; k++) {
                    out[0][i][j][k] = data[i][j][k].getReal().multiply(scale);
                    out[1][i][j][k] = data[i][j][k].getImaginary().multiply(scale);
                }
            }
        }
        return out;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
