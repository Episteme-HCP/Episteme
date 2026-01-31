/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.analysis.transform;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.DoubleBuffer;
import org.jscience.core.technical.backend.math.FFTBackend;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.Operation;

/**
 * FFTW3 implementation of FFTBackend using Project Panama.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class PanamaFFTBackend implements FFTBackend {

    private static final MethodHandle DPLAN_R2C_1D;
    private static final MethodHandle DPLAN_C2R_1D;
    private static final MethodHandle DPLAN_C2C_1D;
    private static final MethodHandle DPLAN_C2C_2D;
    private static final MethodHandle DEXECUTE;
    private static final MethodHandle DDESTROY_PLAN;
    private static final boolean AVAILABLE;

    public static final int FFTW_ESTIMATE = 64;
    public static final int FFTW_MEASURE = 0;
    public static final int FFTW_PATIENT = 32;
    public static final int FFTW_EXHAUSTIVE = 8;
    
    private final int flags;

    public PanamaFFTBackend() {
        this(FFTW_ESTIMATE);
    }

    public PanamaFFTBackend(int flags) {
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
    public ExecutionContext createContext() {
        return new ExecutionContext() {
            @Override
            public <T> T execute(Operation<T> operation) {
                return operation.compute(this);
            }

            @Override
            public void close() {
                // No-op
            }
        };
    }

    @Override
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

    @Override
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

    /**
     * Standard Complex-to-Complex FFT.
     * @param sign -1 for forward, 1 for backward
     */
    public void transformComplex(int n, DoubleBuffer input, DoubleBuffer output, int sign) {
        if (!AVAILABLE) throw new UnsupportedOperationException("FFTW3 library not found");
        try {
            MemorySegment plan = (MemorySegment) DPLAN_C2C_1D.invokeExact(n, MemorySegment.ofBuffer(input), MemorySegment.ofBuffer(output), sign, flags);
            try {
                DEXECUTE.invokeExact(plan);
            } finally {
                DDESTROY_PLAN.invokeExact(plan);
            }
        } catch (Throwable t) {
            throw new RuntimeException("C2C FFT execution failed", t);
        }
    }

    public void transformComplex2D(int rows, int cols, DoubleBuffer input, DoubleBuffer output, int sign) {
        if (!AVAILABLE) throw new UnsupportedOperationException("FFTW3 library not found");
        try {
            MemorySegment plan = (MemorySegment) DPLAN_C2C_2D.invokeExact(rows, cols, MemorySegment.ofBuffer(input), MemorySegment.ofBuffer(output), sign, flags);
            try {
                DEXECUTE.invokeExact(plan);
            } finally {
                DDESTROY_PLAN.invokeExact(plan);
            }
        } catch (Throwable t) {
            throw new RuntimeException("C2C 2D FFT execution failed", t);
        }
    }
}






