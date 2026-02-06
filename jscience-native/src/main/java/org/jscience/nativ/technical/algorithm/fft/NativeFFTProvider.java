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
import java.nio.DoubleBuffer;
import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.FFTProvider;

/**
 * FFTW3 implementation of FFTProvider using Project Panama.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class NativeFFTProvider implements FFTProvider {

    private static final MethodHandle DPLAN_R2C_1D;
    private static final MethodHandle DPLAN_C2R_1D;
    @SuppressWarnings("unused")
    private static final MethodHandle DPLAN_C2C_1D;
    @SuppressWarnings("unused")
    private static final MethodHandle DPLAN_C2C_2D;
    private static final MethodHandle DEXECUTE;
    private static final MethodHandle DDESTROY_PLAN;
    private static final boolean AVAILABLE;

    public static final int FFTW_ESTIMATE = 64;
    
    private final int flags;

    public NativeFFTProvider() {
        this(FFTW_ESTIMATE);
    }

    public NativeFFTProvider(int flags) {
        this.flags = flags;
    }

    static {
        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup = SymbolLookup.libraryLookup("fftw3", Arena.global());
        
        if (lookup.find("fftw_plan_dft_r2c_1d").isPresent()) {
            DPLAN_R2C_1D = linker.downcallHandle(lookup.find("fftw_plan_dft_r2c_1d").get(),
                FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
            DPLAN_C2R_1D = linker.downcallHandle(lookup.find("fftw_plan_dft_c2r_1d").get(),
                FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
            DPLAN_C2C_1D = linker.downcallHandle(lookup.find("fftw_plan_dft_1d").get(),
                FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
            DPLAN_C2C_2D = linker.downcallHandle(lookup.find("fftw_plan_dft_2d").get(),
                FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
            DEXECUTE = linker.downcallHandle(lookup.find("fftw_execute").get(),
                FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
            DDESTROY_PLAN = linker.downcallHandle(lookup.find("fftw_destroy_plan").get(),
                FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
            AVAILABLE = true;
        } else {
            DPLAN_R2C_1D = DPLAN_C2R_1D = DPLAN_C2C_1D = DPLAN_C2C_2D = DEXECUTE = DDESTROY_PLAN = null;
            AVAILABLE = false;
        }
    }

    @Override
    public boolean isAvailable() {
        return AVAILABLE;
    }

    @Override
    public String getName() {
        return "FFTW3 Native Backend";
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public double[][] transform(double[] real, double[] imag) {
        int n = real.length;
        // Use complex-to-complex if imag is not null and has non-zero values
        // For now, simplify and use r2c if imag is null, else c2c
        // But FFTW's dft_1d is general.
        // We'll use interleaved buffers for FFTW
        DoubleBuffer in = DoubleBuffer.allocate(n * 2);
        for (int i = 0; i < n; i++) {
            in.put(i * 2, real[i]);
            in.put(i * 2 + 1, imag == null ? 0 : imag[i]);
        }
        DoubleBuffer out = DoubleBuffer.allocate(n * 2);
        
        c2c(n, in, out, -1); // -1 is FFTW_FORWARD
        
        double[] resReal = new double[n];
        double[] resImag = new double[n];
        for (int i = 0; i < n; i++) {
            resReal[i] = out.get(i * 2);
            resImag[i] = out.get(i * 2 + 1);
        }
        return new double[][] { resReal, resImag };
    }

    @Override
    public double[][] inverseTransform(double[] real, double[] imag) {
        int n = real.length;
        DoubleBuffer in = DoubleBuffer.allocate(n * 2);
        for (int i = 0; i < n; i++) {
            in.put(i * 2, real[i]);
            in.put(i * 2 + 1, imag[i]);
        }
        DoubleBuffer out = DoubleBuffer.allocate(n * 2);
        
        c2c(n, in, out, 1); // 1 is FFTW_BACKWARD
        
        double[] resReal = new double[n];
        double[] resImag = new double[n];
        double scale = 1.0 / n;
        for (int i = 0; i < n; i++) {
            resReal[i] = out.get(i * 2) * scale;
            resImag[i] = out.get(i * 2 + 1) * scale;
        }
        return new double[][] { resReal, resImag };
    }

    @Override
    public Real[][] transform(Real[] real, Real[] imag) {
        double[] r = new double[real.length];
        double[] i = new double[imag.length];
        for (int k = 0; k < real.length; k++) {
            r[k] = real[k].doubleValue();
            i[k] = imag[k].doubleValue();
        }
        double[][] res = transform(r, i);
        Real[][] out = new Real[2][real.length];
        for (int k = 0; k < real.length; k++) {
            out[0][k] = Real.of(res[0][k]);
            out[1][k] = Real.of(res[1][k]);
        }
        return out;
    }

    @Override
    public Real[][] inverseTransform(Real[] real, Real[] imag) {
        double[] r = new double[real.length];
        double[] i = new double[imag.length];
        for (int k = 0; k < real.length; k++) {
            r[k] = real[k].doubleValue();
            i[k] = imag[k].doubleValue();
        }
        double[][] res = inverseTransform(r, i);
        Real[][] out = new Real[2][real.length];
        for (int k = 0; k < real.length; k++) {
            out[0][k] = Real.of(res[0][k]);
            out[1][k] = Real.of(res[1][k]);
        }
        return out;
    }

    @Override
    public Complex[] transformComplex(Complex[] data) {
        double[] r = new double[data.length];
        double[] i = new double[data.length];
        for (int k = 0; k < data.length; k++) {
            r[k] = data[k].real();
            i[k] = data[k].imaginary();
        }
        double[][] res = transform(r, i);
        Complex[] out = new Complex[data.length];
        for (int k = 0; k < data.length; k++) {
            out[k] = Complex.of(res[0][k], res[1][k]);
        }
        return out;
    }

    @Override
    public Complex[] inverseTransformComplex(Complex[] data) {
        double[] r = new double[data.length];
        double[] i = new double[data.length];
        for (int k = 0; k < data.length; k++) {
            r[k] = data[k].real();
            i[k] = data[k].imaginary();
        }
        double[][] res = inverseTransform(r, i);
        Complex[] out = new Complex[data.length];
        for (int k = 0; k < data.length; k++) {
            out[k] = Complex.of(res[0][k], res[1][k]);
        }
        return out;
    }

    private void c2c(int n, DoubleBuffer input, DoubleBuffer output, int sign) {
        if (!AVAILABLE) throw new UnsupportedOperationException("FFTW3 library not found");
        try {
            MemorySegment plan = (MemorySegment) DPLAN_C2C_1D.invokeExact(n, MemorySegment.ofBuffer(input), MemorySegment.ofBuffer(output), sign, flags);
            try {
                DEXECUTE.invokeExact(plan);
            } finally {
                DDESTROY_PLAN.invokeExact(plan);
            }
        } catch (Throwable t) {
            throw new RuntimeException("FFTW C2C execution failed", t);
        }
    }

    public void forward(int n, DoubleBuffer input, DoubleBuffer output) {
        if (!AVAILABLE) throw new UnsupportedOperationException("FFTW3 library not found");
        try {
            MemorySegment plan = (MemorySegment) DPLAN_R2C_1D.invokeExact(n, MemorySegment.ofBuffer(input), MemorySegment.ofBuffer(output), flags);
            try {
                DEXECUTE.invokeExact(plan);
            } finally {
                DDESTROY_PLAN.invokeExact(plan);
            }
        } catch (Throwable t) {
            throw new RuntimeException("FFT execution failed", t);
        }
    }

    public void backward(int n, DoubleBuffer input, DoubleBuffer output) {
        if (!AVAILABLE) throw new UnsupportedOperationException("FFTW3 library not found");
        try {
            MemorySegment plan = (MemorySegment) DPLAN_C2R_1D.invokeExact(n, MemorySegment.ofBuffer(input), MemorySegment.ofBuffer(output), flags);
            try {
                DEXECUTE.invokeExact(plan);
            } finally {
                DDESTROY_PLAN.invokeExact(plan);
            }
        } catch (Throwable t) {
            throw new RuntimeException("IFFT execution failed", t);
        }
    }
}
