/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.fft;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.FFTProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.mathematics.analysis.transform.DiscreteFourierTransform;
import org.jscience.core.mathematics.numbers.complex.Complex;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Multicore implementation of FFTProvider using Fork/Join framework.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({FFTProvider.class, AlgorithmProvider.class})
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
