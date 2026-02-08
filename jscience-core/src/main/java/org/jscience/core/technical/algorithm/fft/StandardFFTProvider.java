package org.jscience.core.technical.algorithm.fft;

import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.FFTProvider;

/**
 * Standard single-threaded implementation of FFTProvider.
 */
@AutoService({FFTProvider.class, org.jscience.core.technical.algorithm.AlgorithmProvider.class})
public class StandardFFTProvider implements FFTProvider {

    @Override
    public String getName() {
        return "Java Standard (Single-threaded)";
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

    @Override
    public boolean isAvailable() {
        return true;
    }
}
