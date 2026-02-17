/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.technical.algorithm.fft;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.FFTProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.backend.nativ.LibraryBackend;

/**
 * FFTW3 implementation of FFTProvider using Project Panama.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({FFTProvider.class, AlgorithmProvider.class, LibraryBackend.class})
public class NativeFFTProvider implements FFTProvider, LibraryBackend {

    private static MethodHandle DPLAN_R2C_1D;
    private static MethodHandle DPLAN_C2R_1D;
    private static MethodHandle DPLAN_DFT_1D; // For Complex-to-Complex
    private static MethodHandle DEXECUTE;
    private static MethodHandle DDESTROY_PLAN;

    private static boolean initialized = false;
    private static boolean available = false;

    @Override
    public boolean isLoaded() {
        ensureInitialized();
        return available;
    }

    @Override
    public String getNativeLibraryName() {
        return "fftw3";
    }

    private static synchronized void ensureInitialized() {
        if (initialized) return;
        initialized = true;
        try {
            SymbolLookup lookup = org.jscience.nativ.technical.backend.nativ.NativeLibraryLoader.loadLibrary("fftw3");
            Linker linker = Linker.nativeLinker();

            DPLAN_R2C_1D = linker.downcallHandle(lookup.find("fftw_plan_dft_r2c_1d").get(),
                    FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                            ValueLayout.JAVA_INT));
            DPLAN_C2R_1D = linker.downcallHandle(lookup.find("fftw_plan_dft_c2r_1d").get(),
                    FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                            ValueLayout.JAVA_INT));
            
            // fftw_plan fftw_plan_dft_1d(int n, fftw_complex *in, fftw_complex *out, int sign, unsigned flags);
            DPLAN_DFT_1D = linker.downcallHandle(lookup.find("fftw_plan_dft_1d").get(),
                    FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

            DEXECUTE = linker.downcallHandle(lookup.find("fftw_execute").get(),
                    FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
            DDESTROY_PLAN = linker.downcallHandle(lookup.find("fftw_destroy_plan").get(),
                    FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
            
            available = true;
        } catch (Throwable t) {
            // Silently mark as unavailable — this is expected when FFTW3 is not installed.
            // The benchmark UI will show "Unavailable" status.
            available = false;
        }
    }

    @Override
    public String getName() {
        return "Native FFTW3 (Wrapper)";
    }

    @Override
    public boolean isAvailable() {
        ensureInitialized();
        return available;
    }

    @Override
    public double[][] transform(double[] real, double[] imag) {
        ensureInitialized();
        if (!available) throw new UnsupportedOperationException("FFTW3 library not available");

        int n = real.length;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment in = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            for (int i = 0; i < n; i++)
                in.setAtIndex(ValueLayout.JAVA_DOUBLE, i, real[i]);
            // Output for r2c is (n/2+1) complex numbers
            MemorySegment out = arena.allocate(ValueLayout.JAVA_DOUBLE, (n / 2 + 1) * 2);

            MemorySegment plan = (MemorySegment) DPLAN_R2C_1D.invokeExact(n, in, out, 1 << 6); // FFTW_ESTIMATE
            DEXECUTE.invokeExact(plan);
            DDESTROY_PLAN.invokeExact(plan);

            double[] re = new double[n / 2 + 1];
            double[] im = new double[n / 2 + 1];
            for (int i = 0; i < n / 2 + 1; i++) {
                re[i] = out.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 2);
                im[i] = out.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 2 + 1);
            }
            return new double[][] { re, im };
        } catch (Throwable e) {
            throw new RuntimeException("FFT execution failed", e);
        }
    }

    @Override
    public double[][] inverseTransform(double[] real, double[] imag) {
        ensureInitialized();
        if (!available) throw new UnsupportedOperationException("FFTW3 library not available");

        // FFTW c2r destroys the input buffer and expects hermitically-symmetric data
        int outN = (real.length - 1) * 2;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment in = arena.allocate(ValueLayout.JAVA_DOUBLE, real.length * 2);
            for (int i = 0; i < real.length; i++) {
                in.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 2, real[i]);
                in.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 2 + 1, imag[i]);
            }
            MemorySegment out = arena.allocate(ValueLayout.JAVA_DOUBLE, outN);

            MemorySegment plan = (MemorySegment) DPLAN_C2R_1D.invokeExact(outN, in, out, 1 << 6);
            DEXECUTE.invokeExact(plan);
            DDESTROY_PLAN.invokeExact(plan);

            double[] resultReal = new double[outN];
            for (int i = 0; i < outN; i++)
                resultReal[i] = out.getAtIndex(ValueLayout.JAVA_DOUBLE, i) / outN;
            return new double[][] { resultReal, new double[outN] };
        } catch (Throwable e) {
            throw new RuntimeException("Inverse FFT execution failed", e);
        }
    }

    @Override
    public Real[][] transform(Real[] real, Real[] imag) {
        double[] r = new double[real.length];
        double[] im = new double[imag.length];
        for (int i = 0; i < r.length; i++) r[i] = real[i].doubleValue();
        for (int i = 0; i < im.length; i++) im[i] = imag[i].doubleValue();

        double[][] res = transform(r, im);
        Real[] resR = new Real[res[0].length];
        Real[] resI = new Real[res[1].length];
        for (int i = 0; i < resR.length; i++) resR[i] = Real.of(res[0][i]);
        for (int i = 0; i < resI.length; i++) resI[i] = Real.of(res[1][i]);
        return new Real[][] { resR, resI };
    }

    @Override
    public Real[][] inverseTransform(Real[] real, Real[] imag) {
        double[] r = new double[real.length];
        double[] im = new double[imag.length];
        for (int i = 0; i < r.length; i++) r[i] = real[i].doubleValue();
        for (int i = 0; i < im.length; i++) im[i] = imag[i].doubleValue();

        double[][] res = inverseTransform(r, im);
        Real[] resR = new Real[res[0].length];
        Real[] resI = new Real[res[1].length];
        for (int i = 0; i < resR.length; i++) resR[i] = Real.of(res[0][i]);
        for (int i = 0; i < resI.length; i++) resI[i] = Real.of(res[1][i]);
        return new Real[][] { resR, resI };
    }

    @Override
    public Complex[] transformComplex(Complex[] data) {
        ensureInitialized();
        if (!available) throw new UnsupportedOperationException("FFTW3 library not available");

        int n = data.length;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment in = arena.allocate(ValueLayout.JAVA_DOUBLE, n * 2);
            for (int i = 0; i < n; i++) {
                in.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 2, data[i].real());
                in.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 2 + 1, data[i].imaginary());
            }
            MemorySegment out = arena.allocate(ValueLayout.JAVA_DOUBLE, n * 2);

            // -1 for Forward transform
            MemorySegment plan = (MemorySegment) DPLAN_DFT_1D.invokeExact(n, in, out, -1, 1 << 6); 
            DEXECUTE.invokeExact(plan);
            DDESTROY_PLAN.invokeExact(plan);

            Complex[] result = new Complex[n];
            for (int i = 0; i < n; i++) {
                double re = out.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 2);
                double im = out.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 2 + 1);
                result[i] = Complex.of(re, im);
            }
            return result;
        } catch (Throwable e) {
            throw new RuntimeException("Complex FFT execution failed", e);
        }
    }

    @Override
    public Complex[] inverseTransformComplex(Complex[] data) {
        ensureInitialized();
        if (!available) throw new UnsupportedOperationException("FFTW3 library not available");

        int n = data.length;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment in = arena.allocate(ValueLayout.JAVA_DOUBLE, n * 2);
            for (int i = 0; i < n; i++) {
                in.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 2, data[i].real());
                in.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 2 + 1, data[i].imaginary());
            }
            MemorySegment out = arena.allocate(ValueLayout.JAVA_DOUBLE, n * 2);

            // 1 for Backward transform
            MemorySegment plan = (MemorySegment) DPLAN_DFT_1D.invokeExact(n, in, out, 1, 1 << 6); 
            DEXECUTE.invokeExact(plan);
            DDESTROY_PLAN.invokeExact(plan);

            Complex[] result = new Complex[n];
            for (int i = 0; i < n; i++) {
                double re = out.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 2) / n;
                double im = out.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 2 + 1) / n;
                result[i] = Complex.of(re, im);
            }
            return result;
        } catch (Throwable e) {
            throw new RuntimeException("Complex Inverse FFT execution failed", e);
        }
    }
    @Override
    public double[][][] transform2D(double[][] real, double[][] imag) {
        throw new UnsupportedOperationException("2D FFT not implemented in NativeFFTProvider yet");
    }

    @Override
    public double[][][] inverseTransform2D(double[][] real, double[][] imag) {
        throw new UnsupportedOperationException("2D Inverse FFT not implemented in NativeFFTProvider yet");
    }

    @Override
    public double[][][][] transform3D(double[][][] real, double[][][] imag) {
        throw new UnsupportedOperationException("3D FFT not implemented in NativeFFTProvider yet");
    }

    @Override
    public double[][][][] inverseTransform3D(double[][][] real, double[][][] imag) {
        throw new UnsupportedOperationException("3D Inverse FFT not implemented in NativeFFTProvider yet");
    }

    @Override
    public Real[][][] transform2D(Real[][] real, Real[][] imag) {
        throw new UnsupportedOperationException("2D FFT (Real) not implemented in NativeFFTProvider yet");
    }

    @Override
    public Real[][][] inverseTransform2D(Real[][] real, Real[][] imag) {
        throw new UnsupportedOperationException("2D Inverse FFT (Real) not implemented in NativeFFTProvider yet");
    }

    @Override
    public Real[][][][] transform3D(Real[][][] real, Real[][][] imag) {
        throw new UnsupportedOperationException("3D FFT (Real) not implemented in NativeFFTProvider yet");
    }

    @Override
    public Real[][][][] inverseTransform3D(Real[][][] real, Real[][][] imag) {
        throw new UnsupportedOperationException("3D Inverse FFT (Real) not implemented in NativeFFTProvider yet");
    }
}
