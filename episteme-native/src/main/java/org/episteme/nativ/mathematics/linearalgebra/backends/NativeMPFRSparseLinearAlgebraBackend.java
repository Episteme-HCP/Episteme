/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.backends.LinearAlgebraBackend;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;

import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.sparse.*;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Arbitrary-precision Sparse Linear Algebra backend using MPFR (via Panama).
 * Optimized for CSR storage.
 */
@com.google.auto.service.AutoService(LinearAlgebraProvider.class)
public class NativeMPFRSparseLinearAlgebraBackend<E> implements LinearAlgebraBackend<E>, LinearAlgebraProvider<E>, SparseLinearAlgebraProvider<E>, NativeBackend, CPUBackend {
    private static final Logger logger = LoggerFactory.getLogger(NativeMPFRSparseLinearAlgebraBackend.class);
    private static final Linker LINKER = Linker.nativeLinker();
    private static final boolean AVAILABLE;

    private static MethodHandle MPFR_INIT2;
    private static MethodHandle MPFR_CLEAR;
    private static MethodHandle MPFR_SET_STR;
    private static MethodHandle MPFR_GET_STR;
    private static MethodHandle MPFR_ADD;
    private static MethodHandle MPFR_SUB;
    private static MethodHandle MPFR_MUL;
    private static MethodHandle MPFR_DIV;
    private static MethodHandle MPFR_SET;
    private static MethodHandle MPFR_CMP;
    private static MethodHandle MPFR_SET_UI;
    private static MethodHandle MPFR_SQRT;
    private static MethodHandle MPFR_NEG;
    private static MethodHandle MPFR_EXP;
    private static MethodHandle MPFR_LOG;
    private static MethodHandle MPFR_LOG10;
    private static MethodHandle MPFR_SIN;
    private static MethodHandle MPFR_COS;
    private static MethodHandle MPFR_TAN;
    private static MethodHandle MPFR_ASIN;
    private static MethodHandle MPFR_ACOS;
    private static MethodHandle MPFR_ATAN;
    private static MethodHandle MPFR_SINH;
    private static MethodHandle MPFR_COSH;
    private static MethodHandle MPFR_TANH;
    private static MethodHandle MPFR_ASINH;
    private static MethodHandle MPFR_ACOSH;
    private static MethodHandle MPFR_ATANH;
    private static MethodHandle MPFR_CONST_PI;
    private static MethodHandle MPFR_ATAN2;
    private static MethodHandle MPFR_POW;
    private static MethodHandle MPFR_CBRT;
    private static MethodHandle MPFR_SET_D;

    public static final StructLayout MPFR_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_INT.withName("prec"),
        ValueLayout.JAVA_INT.withName("sign"),
        ValueLayout.JAVA_INT.withName("exp"),
        MemoryLayout.paddingLayout(4),
        ValueLayout.ADDRESS.withName("d")
    );

    static {
        boolean avail = false;
        try {
            Optional<SymbolLookup> mpfrLookup = NativeFFMLoader.loadLibrary("mpfr", Arena.global());
            if (mpfrLookup.isPresent()) {
                SymbolLookup mpfr = mpfrLookup.get();
                MPFR_INIT2 = lookup(mpfr, "mpfr_init2", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
                MPFR_CLEAR = lookup(mpfr, "mpfr_clear", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
                MPFR_SET_STR = lookup(mpfr, "mpfr_set_str", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
                MPFR_GET_STR = lookup(mpfr, "mpfr_get_str", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ADD = lookup(mpfr, "mpfr_add", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_SUB = lookup(mpfr, "mpfr_sub", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_MUL = lookup(mpfr, "mpfr_mul", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_DIV = lookup(mpfr, "mpfr_div", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_SET = lookup(mpfr, "mpfr_set", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_CMP = lookup(mpfr, "mpfr_cmp", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
                MPFR_SET_UI = lookup(mpfr, "mpfr_set_ui", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT));
                MPFR_SET_D = lookup(mpfr, "mpfr_set_d", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_INT));
                MPFR_SQRT = lookup(mpfr, "mpfr_sqrt", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_NEG = lookup(mpfr, "mpfr_neg", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_EXP = lookup(mpfr, "mpfr_exp", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_LOG = lookup(mpfr, "mpfr_log", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_LOG10 = lookup(mpfr, "mpfr_log10", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_SIN = lookup(mpfr, "mpfr_sin", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_COS = lookup(mpfr, "mpfr_cos", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_TAN = lookup(mpfr, "mpfr_tan", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ASIN = lookup(mpfr, "mpfr_asin", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ACOS = lookup(mpfr, "mpfr_acos", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ATAN = lookup(mpfr, "mpfr_atan", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_SINH = lookup(mpfr, "mpfr_sinh", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_COSH = lookup(mpfr, "mpfr_cosh", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_TANH = lookup(mpfr, "mpfr_tanh", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ASINH = lookup(mpfr, "mpfr_asinh", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ACOSH = lookup(mpfr, "mpfr_acosh", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ATANH = lookup(mpfr, "mpfr_atanh", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_CONST_PI = lookup(mpfr, "mpfr_const_pi", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ATAN2 = lookup(mpfr, "mpfr_atan2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_POW = lookup(mpfr, "mpfr_pow", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_CBRT = lookup(mpfr, "mpfr_cbrt", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

                avail = MPFR_INIT2 != null && MPFR_ADD != null && MPFR_MUL != null;
            }
        } catch (Throwable t) {
            logger.warn("Native MPFR Sparse Backend initialization failed: {}", t.getMessage());
        }
        AVAILABLE = avail;
    }

    private static MethodHandle lookup(SymbolLookup lookup, String name, FunctionDescriptor desc) {
        return lookup.find(name).map(s -> LINKER.downcallHandle(s, desc)).orElse(null);
    }

    @Override public boolean isAvailable() { return AVAILABLE; }
    @Override public String getId() { return "native-mpfr-sparse"; }
    @Override public String getName() { return "Native MPFR Sparse Linear Algebra Backend"; }
    @Override public String getDescription() { return "Native Arbitrary-Precision Sparse Linear Algebra using MPFR."; }
    @Override public String getType() { return "linearalgebra"; }
    @Override public int getPriority() { return 1500; }
    @Override public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() { return org.episteme.core.technical.backend.HardwareAccelerator.CPU; }
    @Override public boolean isLoaded() { return AVAILABLE; }
    @Override public String getNativeLibraryName() { return "mpfr"; }
    @Override public Object createBackend() { return this; }
    @Override public org.episteme.core.technical.backend.ExecutionContext createContext() { 
        return new org.episteme.core.technical.backend.ExecutionContext() {
            @Override public <T> T execute(org.episteme.core.technical.backend.Operation<T> op) { return op.compute(this); }
            @Override public void close() {}
        };
    }
    @Override public void shutdown() {}
    @Override public java.util.Map<String, String> getMetadata() { return java.util.Map.of("environment", "CPU (Panama/MPFR)"); }

    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        return true; 
    }

    @Override
    public double score(org.episteme.core.technical.algorithm.OperationContext context) {
        if (!AVAILABLE) return -1.0;
        double base = getPriority();
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
            base += 2000.0;
        }
        return base;
    }

    private long getPrecision() {
        org.episteme.core.mathematics.context.MathContext ctx = org.episteme.core.mathematics.context.MathContext.getCurrent();
        int digits = ctx.getJavaMathContext().getPrecision();
        if (digits <= 0) digits = 256; 
        return (long) (digits * 3.322) + 1;
    }

    private org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> toSparse(Matrix<E> a) {
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) return (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) a;
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(a.rows(), a.cols(), (E) a.getScalarRing().zero());
        for (int i=0; i<a.rows(); i++) {
            for (int j=0; j<a.cols(); j++) {
                E val = a.get(i, j);
                if (!val.equals(a.getScalarRing().zero())) storage.set(i, j, val);
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>(storage, (Ring<E>)a.getScalarRing());
    }

    private Real readMPFR(MemorySegment val, Arena arena) {
        try {
            return readMPFR_internal(val, arena.allocate(ValueLayout.JAVA_LONG), arena);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR Read failed", t);
        }
    }
    
    private Real readMPFR_internal(MemorySegment val, MemorySegment expPtr, Arena arena) throws Throwable {
        MemorySegment strPtr = (MemorySegment) NativeSafe.invoke(MPFR_GET_STR, MemorySegment.NULL, expPtr, 10, 0L, val, 0);
        if (strPtr == null || strPtr.equals(MemorySegment.NULL)) return Real.ZERO;
        String s = strPtr.reinterpret(Long.MAX_VALUE).getString(0);
        long exp = expPtr.get(ValueLayout.JAVA_LONG, 0);
        
        StringBuilder sb = new StringBuilder();
        if (s.startsWith("-")) { sb.append("-"); s = s.substring(1); }
        sb.append("0.").append(s).append("E").append(exp);
        return Real.of(sb.toString());
    }

    private void setMPFR(MemorySegment dest, E value, Arena arena, int skip) throws Throwable {
        String s = value.toString();
        NativeSafe.invoke(MPFR_SET_STR, dest, arena.allocateFrom(s), 10, 0);
    }

    private MemorySegment initVector(Matrix<E> a, Arena arena, long prec, boolean isComplex) throws Throwable {
        int n = a.rows() * a.cols();
        int multiplier = isComplex ? 2 : 1;
        MemorySegment h_v = arena.allocate(MPFR_LAYOUT, n * multiplier);
        for (int i = 0; i < n * multiplier; i++) {
            MemorySegment rc = h_v.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, rc, prec);
        }
        
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) a;
            int[] rowPtr = sa.getRowPointers();
            int[] colIdx = sa.getColIndices();
            Object[] vals = sa.getValues();
            for (int i = 0; i < sa.rows(); i++) {
                for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                    int pos = i * sa.cols() + colIdx[k];
                    if (isComplex) {
                        org.episteme.core.mathematics.numbers.complex.Complex cv = (org.episteme.core.mathematics.numbers.complex.Complex) vals[k];
                        setMPFR(h_v.asSlice(pos * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (E) cv.getReal(), arena, 0);
                        setMPFR(h_v.asSlice((pos * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (E) cv.getImaginary(), arena, 0);
                    } else {
                        setMPFR(h_v.asSlice(pos * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (E) vals[k], arena, 0);
                    }
                }
            }
        } else {
            for (int i = 0; i < a.rows(); i++) {
                for (int j = 0; j < a.cols(); j++) {
                    int pos = i * a.cols() + j;
                    if (isComplex) {
                        org.episteme.core.mathematics.numbers.complex.Complex cv = (org.episteme.core.mathematics.numbers.complex.Complex) a.get(i, j);
                        setMPFR(h_v.asSlice(pos * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (E) cv.getReal(), arena, 0);
                        setMPFR(h_v.asSlice((pos * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (E) cv.getImaginary(), arena, 0);
                    } else {
                        setMPFR(h_v.asSlice(pos * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (E) a.get(i, j), arena, 0);
                    }
                }
            }
        }
        return h_v;
    }

    private void dotProduct(MemorySegment v1, MemorySegment v2, int n, MemorySegment res, long prec, Arena arena, boolean isComplex) throws Throwable {
        MemorySegment sumR = res.asSlice(0, MPFR_LAYOUT.byteSize());
        MemorySegment sumI = isComplex ? res.asSlice(MPFR_LAYOUT.byteSize(), MPFR_LAYOUT.byteSize()) : null;
        
        NativeSafe.invoke(MPFR_SET_UI, sumR, 0L, 0);
        if (isComplex) NativeSafe.invoke(MPFR_SET_UI, sumI, 0L, 0);

        MemorySegment t1 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1, prec);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2, prec);

        for (int i = 0; i < n; i++) {
            if (isComplex) {
                MemorySegment aR = v1.asSlice(i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment aI = v1.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment bR = v2.asSlice(i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment bI = v2.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                
                // (aR - i*aI)*(bR + i*bI) = (aR*bR + aI*bI) + i(aR*bI - aI*bR)
                NativeSafe.invoke(MPFR_MUL, t1, aR, bR, 0);
                NativeSafe.invoke(MPFR_MUL, t2, aI, bI, 0);
                NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                
                NativeSafe.invoke(MPFR_MUL, t1, aR, bI, 0);
                NativeSafe.invoke(MPFR_MUL, t2, aI, bR, 0);
                NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
                NativeSafe.invoke(MPFR_ADD, sumI, sumI, t1, 0);
            } else {
                MemorySegment a = v1.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment b = v2.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_MUL, t1, a, b, 0);
                NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
            }
        }
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (a.cols() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        long prec = getPrecision();
        boolean isComplex = ((Object)sa.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_b = initVector(b.toMatrix(), arena, prec, isComplex);
            MemorySegment res = arena.allocate(MPFR_LAYOUT, sa.rows() * (isComplex ? 2 : 1));
            for (int i=0; i<sa.rows() * (isComplex ? 2 : 1); i++) NativeSafe.invoke(MPFR_INIT2, res.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), prec);
            
            spmv_internal(sa, h_b, res, prec, arena, isComplex);
            
            E[] resultArr = (E[]) new Object[sa.rows()];
            MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);
            for (int i = 0; i < sa.rows(); i++) {
                if (isComplex) {
                    Real re = readMPFR_internal(res.asSlice(i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), expPtr, arena);
                    Real im = readMPFR_internal(res.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), expPtr, arena);
                    resultArr[i] = (E) (Object) Complex.of(re, im);
                } else {
                    resultArr[i] = (E) (Object) readMPFR_internal(res.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), expPtr, arena);
                }
            }
            return Vector.of(java.util.Arrays.asList(resultArr), sa.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR SpMV failed", t);
        }
    }

    private void spmv_internal(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa, MemorySegment h_b, MemorySegment res, long prec, Arena arena, boolean isComplex) throws Throwable {
        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        Object[] vals = sa.getValues();
        
        MemorySegment sumR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sumR, prec);
        MemorySegment sumI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
        if (isComplex) NativeSafe.invoke(MPFR_INIT2, sumI, prec);
        
        MemorySegment t1 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1, prec);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2, prec);
        MemorySegment valR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, valR, prec);
        MemorySegment valI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
        if (isComplex) NativeSafe.invoke(MPFR_INIT2, valI, prec);

        for (int i = 0; i < sa.rows(); i++) {
            NativeSafe.invoke(MPFR_SET_UI, sumR, 0L, 0);
            if (isComplex) NativeSafe.invoke(MPFR_SET_UI, sumI, 0L, 0);
            
            for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                int col = colIdx[k];
                if (isComplex) {
                    org.episteme.core.mathematics.numbers.complex.Complex cv = (org.episteme.core.mathematics.numbers.complex.Complex) vals[k];
                    NativeSafe.invoke(MPFR_SET_STR, valR, arena.allocateFrom(cv.getReal().toString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, valI, arena.allocateFrom(cv.getImaginary().toString()), 10, 0);
                    
                    MemorySegment bR = h_b.asSlice(col * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment bI = h_b.asSlice((col * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    
                    // Complex mul: (valR + i*valI)*(bR + i*bI) = (valR*bR - valI*bI) + i(valR*bI + valI*bR)
                    NativeSafe.invoke(MPFR_MUL, t1, valR, bR, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, valI, bI, 0);
                    NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                    
                    NativeSafe.invoke(MPFR_MUL, t1, valR, bI, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, valI, bR, 0);
                    NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, sumI, sumI, t1, 0);
                } else {
                    NativeSafe.invoke(MPFR_SET_STR, valR, arena.allocateFrom(vals[k].toString()), 10, 0);
                    MemorySegment bval = h_b.asSlice(col * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_MUL, t1, valR, bval, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                }
            }
            NativeSafe.invoke(MPFR_SET, res.asSlice(i * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), sumR, 0);
            if (isComplex) NativeSafe.invoke(MPFR_SET, res.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), sumI, 0);
        }
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        long prec = getPrecision();
        org.episteme.core.mathematics.structures.rings.Ring<E> ring = (org.episteme.core.mathematics.structures.rings.Ring<E>) sa.getScalarRing();
        boolean isComplex = ((Object)sa.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(sa.rows(), sa.cols(), (E) ring.zero());
        
        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        Object[] vals = sa.getValues();
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment sR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sR, prec);
            MemorySegment sI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, sI, prec);
            
            if (isComplex) {
                org.episteme.core.mathematics.numbers.complex.Complex cs = (org.episteme.core.mathematics.numbers.complex.Complex) scalar;
                NativeSafe.invoke(MPFR_SET_STR, sR, arena.allocateFrom(cs.getReal().toString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, sI, arena.allocateFrom(cs.getImaginary().toString()), 10, 0);
            } else {
                NativeSafe.invoke(MPFR_SET_STR, sR, arena.allocateFrom(scalar.toString()), 10, 0);
            }

            MemorySegment t1 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1, prec);
            MemorySegment t2 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2, prec);
            MemorySegment valR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, valR, prec);
            MemorySegment valI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, valI, prec);
            
            MemorySegment resR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, resR, prec);
            MemorySegment resI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, resI, prec);

            for (int i = 0; i < sa.rows(); i++) {
                for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                    if (isComplex) {
                        org.episteme.core.mathematics.numbers.complex.Complex cv = (org.episteme.core.mathematics.numbers.complex.Complex) vals[k];
                        NativeSafe.invoke(MPFR_SET_STR, valR, arena.allocateFrom(cv.getReal().toString()), 10, 0);
                        NativeSafe.invoke(MPFR_SET_STR, valI, arena.allocateFrom(cv.getImaginary().toString()), 10, 0);
                        
                        // (valR + i*valI)*(sR + i*sI) = (valR*sR - valI*sI) + i(valR*sI + valI*sR)
                        NativeSafe.invoke(MPFR_MUL, t1, valR, sR, 0);
                        NativeSafe.invoke(MPFR_MUL, t2, valI, sI, 0);
                        NativeSafe.invoke(MPFR_SUB, resR, t1, t2, 0);
                        
                        NativeSafe.invoke(MPFR_MUL, t1, valR, sI, 0);
                        NativeSafe.invoke(MPFR_MUL, t2, valI, sR, 0);
                        NativeSafe.invoke(MPFR_ADD, resI, t1, t2, 0);
                        
                        storage.set(i, colIdx[k], (E) (Object) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena)));
                    } else {
                        NativeSafe.invoke(MPFR_SET_STR, valR, arena.allocateFrom(vals[k].toString()), 10, 0);
                        NativeSafe.invoke(MPFR_MUL, resR, valR, sR, 0);
                        storage.set(i, colIdx[k], (E) (Object) readMPFR(resR, arena));
                    }
                }
            }
            return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>(storage, (Ring<E>) sa.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR scalar multiply failed", t);
        }
    }

    @Override
    public Vector<E> multiply(Vector<E> a, E scalar) {
        Field<E> field = (Field<E>) a.getScalarRing();
        int n = a.dimension();
        long prec = getPrecision();
        boolean isComplex = ((Object)field.zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment sR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sR, prec);
            MemorySegment sI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, sI, prec);
            
            if (isComplex) {
                org.episteme.core.mathematics.numbers.complex.Complex cs = (org.episteme.core.mathematics.numbers.complex.Complex) scalar;
                NativeSafe.invoke(MPFR_SET_STR, sR, arena.allocateFrom(cs.getReal().toString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, sI, arena.allocateFrom(cs.getImaginary().toString()), 10, 0);
            } else {
                NativeSafe.invoke(MPFR_SET_STR, sR, arena.allocateFrom(scalar.toString()), 10, 0);
            }

            MemorySegment t1 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1, prec);
            MemorySegment t2 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2, prec);
            MemorySegment valR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, valR, prec);
            MemorySegment valI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, valI, prec);
            
            MemorySegment resR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, resR, prec);
            MemorySegment resI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, resI, prec);

            E[] resultArr = (E[]) new Object[n];
            for (int i = 0; i < n; i++) {
                E val = a.get(i);
                if (isComplex) {
                    org.episteme.core.mathematics.numbers.complex.Complex cv = (org.episteme.core.mathematics.numbers.complex.Complex) val;
                    NativeSafe.invoke(MPFR_SET_STR, valR, arena.allocateFrom(cv.getReal().toString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, valI, arena.allocateFrom(cv.getImaginary().toString()), 10, 0);
                    
                    NativeSafe.invoke(MPFR_MUL, t1, valR, sR, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, valI, sI, 0);
                    NativeSafe.invoke(MPFR_SUB, resR, t1, t2, 0);
                    
                    NativeSafe.invoke(MPFR_MUL, t1, valR, sI, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, valI, sR, 0);
                    NativeSafe.invoke(MPFR_ADD, resI, t1, t2, 0);
                    
                    resultArr[i] = (E) (Object) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena));
                } else {
                    NativeSafe.invoke(MPFR_SET_STR, valR, arena.allocateFrom(val.toString()), 10, 0);
                    NativeSafe.invoke(MPFR_MUL, resR, valR, sR, 0);
                    resultArr[i] = (E) (Object) readMPFR(resR, arena);
                }
            }
            return Vector.of(java.util.Arrays.asList(resultArr), a.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR Vector multiply failed", t);
        }
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (a.rows() != b.rows() || a.cols() != b.cols()) throw new IllegalArgumentException("Dimension mismatch");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sb = toSparse(b);
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(sa.rows(), sa.cols(), (E) sa.getScalarRing().zero());
        
        java.util.Set<Integer> rows = new java.util.HashSet<>();
        for (int i=0; i<sa.rows(); i++) rows.add(i);
        for (int i=0; i<sb.rows(); i++) rows.add(i);

        for (int i : rows) {
            java.util.Map<Integer, E> rowMap = new java.util.HashMap<>();
            int[] rpa = sa.getRowPointers();
            int[] cia = sa.getColIndices();
            Object[] va = sa.getValues();
            for (int k=rpa[i]; k < rpa[i+1]; k++) rowMap.put(cia[k], (E) va[k]);

            int[] rpb = sb.getRowPointers();
            int[] cib = sb.getColIndices();
            Object[] vb = sb.getValues();
            org.episteme.core.mathematics.structures.rings.Ring<E> ring = (org.episteme.core.mathematics.structures.rings.Ring<E>) sa.getScalarRing();
            for (int k=rpb[i]; k < rpb[i+1]; k++) {
                rowMap.merge(cib[k], (E) vb[k], (v1, v2) -> ring.add(v1, v2));
            }
            for (java.util.Map.Entry<Integer, E> entry : rowMap.entrySet()) {
                if (!entry.getValue().equals(ring.zero())) {
                    storage.set(i, entry.getKey(), entry.getValue());
                }
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, (Ring<E>)sa.getScalarRing());
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        org.episteme.core.mathematics.structures.rings.Ring<E> ring = (org.episteme.core.mathematics.structures.rings.Ring<E>) a.getScalarRing();
        return add(a, scale(ring.negate(ring.one()), b));
    }

    @Override
    public E dot(Vector<E> v1, Vector<E> v2) {
        if (v1.dimension() != v2.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " is not available.");
        
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined()) {
            boolean isComplex = ((Object)v1.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
            MemorySegment h_v1 = initVector(v1.toMatrix(), arena, prec, isComplex);
            MemorySegment h_v2 = initVector(v2.toMatrix(), arena, prec, isComplex);
            
            int resSize = isComplex ? 2 : 1;
            MemorySegment res = arena.allocate(MPFR_LAYOUT, resSize);
            for (int i=0; i<resSize; i++) NativeSafe.invoke(MPFR_INIT2, res.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), prec);
            
            dotProduct(h_v1, h_v2, v1.dimension(), res, prec, arena, isComplex);
            
            if (isComplex) {
                Real re = readMPFR(res.asSlice(0, MPFR_LAYOUT.byteSize()), arena);
                Real im = readMPFR(res.asSlice(MPFR_LAYOUT.byteSize(), MPFR_LAYOUT.byteSize()), arena);
                return (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(re, im);
            } else {
                return (E) (Object) readMPFR(res, arena);
            }
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR dot failed", t);
        }
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        return bicgstab(a, b, Vector.zeros(b.dimension(), (org.episteme.core.mathematics.structures.rings.Ring<E>) b.getScalarRing()), (E)Real.of("1e-20"), 1000);
    }

    public Vector<E> solve(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> a, Vector<E> b) {
        return solve((Matrix<E>)a, b);
    }

    @Override
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        return GenericSparseSolvers.bicgstab(this, a, b, x0, tolerance, maxIterations, (Field<E>) a.getScalarRing());
    }

    @Override
    public Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        return GenericSparseSolvers.gmres(this, a, b, x0, tolerance, maxIterations, restarts, (Field<E>) a.getScalarRing());
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        int rows = sa.rows();
        int cols = sa.cols();
        
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> newStorage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(cols, rows, (E) sa.getScalarRing().zero());
        
        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        Object[] values = sa.getValues();
        
        for (int i = 0; i < rows; i++) {
            for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                newStorage.set(colIdx[k], i, (E) values[k]);
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>(newStorage, (Ring<E>) sa.getScalarRing());
    }

    @Override public Matrix<E> exp(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.exp(a); return applyTranscendental(a, "exp"); }
    @Override public Matrix<E> log(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.log(a); return applyTranscendental(a, "log"); }
    @Override public Matrix<E> log10(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.log10(a); return applyTranscendental(a, "log10"); }
    @Override public Matrix<E> sin(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.sin(a); return applyTranscendental(a, "sin"); }
    @Override public Matrix<E> cos(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.cos(a); return applyTranscendental(a, "cos"); }
    @Override public Matrix<E> tan(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.tan(a); return applyTranscendental(a, "tan"); }
    @Override public Matrix<E> asin(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.asin(a); return applyTranscendental(a, "asin"); }
    @Override public Matrix<E> acos(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.acos(a); return applyTranscendental(a, "acos"); }
    @Override public Matrix<E> atan(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.atan(a); return applyTranscendental(a, "atan"); }
    @Override public Matrix<E> sinh(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.sinh(a); return applyTranscendental(a, "sinh"); }
    @Override public Matrix<E> cosh(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.cosh(a); return applyTranscendental(a, "cosh"); }
    @Override public Matrix<E> tanh(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.tanh(a); return applyTranscendental(a, "tanh"); }
    @Override public Matrix<E> asinh(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.asinh(a); return applyTranscendental(a, "asinh"); }
    @Override public Matrix<E> acosh(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.acosh(a); return applyTranscendental(a, "acosh"); }
    @Override public Matrix<E> atanh(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.atanh(a); return applyTranscendental(a, "atanh"); }
    @Override public Matrix<E> sqrt(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.sqrt(a); return applyTranscendental(a, "sqrt"); }
    @Override public Matrix<E> cbrt(Matrix<E> a) { if (isComplex(a)) return LinearAlgebraBackend.super.cbrt(a); return applyTranscendental(a, "cbrt"); }

    private boolean isComplex(Matrix<E> a) {
        return ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    public Matrix<E> applyTranscendental(Matrix<E> a, String op, Object... args) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " is not available.");
        long prec = getPrecision();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            (org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>) sa.getStorage();
        
        try (Arena arena = Arena.ofConfined()) {
            for (java.util.Map.Entry<Long, E> entry : storage.getData().entrySet()) {
                long key = entry.getKey();
                int r = (int) (key >>> 32);
                int c = (int) (key & 0xFFFFFFFFL);
                E val = entry.getValue();
                
                E result;
                if (isComplex) {
                    result = (E) complexTranscendental((org.episteme.core.mathematics.numbers.complex.Complex) val, op, prec, arena, args);
                } else {
                    result = (E) realTranscendental((Real) val, op, prec, arena, args);
                }
                storage.set(r, c, result);
            }
        }
        return sa;
    }

    private org.episteme.core.mathematics.numbers.complex.Complex complexTranscendental(org.episteme.core.mathematics.numbers.complex.Complex z, String op, long prec, Arena arena, Object... args) {
         try {
             MemorySegment resR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, resR, prec);
             MemorySegment resI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, resI, prec);
             MemorySegment aR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, aR, prec);
             MemorySegment aI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, aI, prec);
             
             NativeSafe.invoke(MPFR_SET_STR, aR, arena.allocateFrom(z.getReal().toString()), 10, 0);
             NativeSafe.invoke(MPFR_SET_STR, aI, arena.allocateFrom(z.getImaginary().toString()), 10, 0);
             
             NativeMPFRDenseLinearAlgebraBackend<E> dense = new NativeMPFRDenseLinearAlgebraBackend<>();
             switch (op.toLowerCase()) {
                 case "exp" -> dense.complexExp(resR, resI, aR, aI, prec, arena);
                 case "log" -> dense.complexLog(resR, resI, aR, aI, prec, arena);
                 case "log10" -> dense.complexLog10(resR, resI, aR, aI, prec, arena);
                 case "sin" -> dense.complexSin(resR, resI, aR, aI, prec, arena);
                 case "cos" -> dense.complexCos(resR, resI, aR, aI, prec, arena);
                 case "tan" -> dense.complexTan(resR, resI, aR, aI, prec, arena);
                 case "asin" -> dense.complexAsin(resR, resI, aR, aI, prec, arena);
                 case "acos" -> dense.complexAcos(resR, resI, aR, aI, prec, arena);
                 case "atan" -> dense.complexAtan(resR, resI, aR, aI, prec, arena);
                 case "sinh" -> dense.complexSinh(resR, resI, aR, aI, prec, arena);
                 case "cosh" -> dense.complexCosh(resR, resI, aR, aI, prec, arena);
                 case "tanh" -> dense.complexTanh(resR, resI, aR, aI, prec, arena);
                 case "sqrt" -> dense.complexSqrt(resR, resI, aR, aI, prec, arena);
                 case "cbrt" -> dense.complexCbrt(resR, resI, aR, aI, prec, arena);
                 case "pow" -> {
                     if (args.length > 0 && args[0] instanceof org.episteme.core.mathematics.numbers.complex.Complex exp) {
                         MemorySegment eR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, eR, prec);
                         MemorySegment eI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, eI, prec);
                         NativeSafe.invoke(MPFR_SET_STR, eR, arena.allocateFrom(exp.getReal().toString()), 10, 0);
                         NativeSafe.invoke(MPFR_SET_STR, eI, arena.allocateFrom(exp.getImaginary().toString()), 10, 0);
                         dense.complexPow(resR, resI, aR, aI, eR, eI, prec, arena);
                     }
                 }
                 default -> throw new UnsupportedOperationException("Op " + op + " not implemented for complex sparse");
             }
             
             Real r = readMPFR(resR, arena);
             Real i = readMPFR(resI, arena);
             return org.episteme.core.mathematics.numbers.complex.Complex.of(r, i);
         } catch (Throwable t) {
             throw new RuntimeException("MPFR Sparse complex transcendental failed", t);
         }
    }

    private Real realTranscendental(Real v, String op, long prec, Arena arena, Object... args) {
        try {
            MemorySegment res = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, res, prec);
            MemorySegment val = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, val, prec);
            NativeSafe.invoke(MPFR_SET_STR, val, arena.allocateFrom(v.toString()), 10, 0);
            
            MethodHandle handle = switch (op.toLowerCase()) {
                case "exp" -> MPFR_EXP;
                case "log" -> MPFR_LOG;
                case "log10" -> MPFR_LOG10;
                case "sin" -> MPFR_SIN;
                case "cos" -> MPFR_COS;
                case "tan" -> MPFR_TAN;
                case "asin" -> MPFR_ASIN;
                case "acos" -> MPFR_ACOS;
                case "atan" -> MPFR_ATAN;
                case "sinh" -> MPFR_SINH;
                case "cosh" -> MPFR_COSH;
                case "tanh" -> MPFR_TANH;
                case "sqrt" -> MPFR_SQRT;
                case "cbrt" -> MPFR_CBRT;
                default -> null;
            };
            
            if (handle != null) {
                NativeSafe.invoke(handle, res, val, 0);
            } else if (op.equalsIgnoreCase("pow") && args.length > 0 && args[0] instanceof Real exp) {
                MemorySegment e = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, e, prec);
                NativeSafe.invoke(MPFR_SET_STR, e, arena.allocateFrom(exp.toString()), 10, 0);
                NativeSafe.invoke(MPFR_POW, res, val, e, 0);
            } else {
                 throw new UnsupportedOperationException("Op " + op + " not implemented for real sparse");
            }
            
            return readMPFR(res, arena);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR Sparse real transcendental failed", t);
        }
    }

    @Override public E norm(Vector<E> v) { 
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined()) {
            boolean isComplex = ((Object)v.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
            MemorySegment h_v = initVector(v.toMatrix(), arena, prec, isComplex);
            MemorySegment res = arena.allocate(MPFR_LAYOUT, isComplex ? 2 : 1);
            for (int i=0; i<(isComplex?2:1); i++) NativeSafe.invoke(MPFR_INIT2, res.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), prec);
            dotProduct(h_v, h_v, v.dimension(), res, prec, arena, isComplex);
            MemorySegment realPart = res.asSlice(0, MPFR_LAYOUT.byteSize());
            NativeSafe.invoke(MPFR_SQRT, realPart, realPart, 0);
            Real val = readMPFR(realPart, arena);
            if (isComplex) {
                return (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(val, org.episteme.core.mathematics.numbers.real.Real.ZERO);
            } else {
                // If E is Real or just a generic Number, return it directly
                return (E) (Object) val;
            }
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR norm failed", t);
        }
    }

    @Override public E determinant(Matrix<E> a) { return GenericLU.determinant(a, (Field<E>) a.getScalarRing()); }
    @Override public Matrix<E> inverse(Matrix<E> a) { return GenericLU.inverse(a, (Field<E>) a.getScalarRing(), this); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<E> lu(Matrix<E> a) { return GenericLU.decompose(a, (Field<E>) a.getScalarRing(), this); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<E> qr(Matrix<E> a) { return GenericQR.decompose(a, (Field<E>) a.getScalarRing(), this); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<E> svd(Matrix<E> a) { return GenericSVD.decompose(a, (Field<E>) a.getScalarRing()); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<E> cholesky(Matrix<E> a) { return GenericCholesky.decompose(a, (Field<E>) a.getScalarRing()); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<E> eigen(Matrix<E> a) { return GenericEigen.decompose(a, (Field<E>) a.getScalarRing()); }
}
