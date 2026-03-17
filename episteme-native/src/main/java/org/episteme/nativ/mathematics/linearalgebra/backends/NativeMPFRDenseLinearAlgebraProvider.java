/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.core.mathematics.linearalgebra.backends.LinearAlgebraBackend;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.auto.service.AutoService;

/**
 * High-performance Arbitrary Precision Linear Algebra backend using libmpfr.
 * Binds directly to MPFR via Project Panama (FFM).
 */
@AutoService({LinearAlgebraBackend.class, Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, CPUBackend.class})
public class NativeMPFRDenseLinearAlgebraProvider implements LinearAlgebraBackend<Real>, NativeBackend, CPUBackend {

    private static final Logger logger = LoggerFactory.getLogger(NativeMPFRDenseLinearAlgebraProvider.class);
    private static final Linker LINKER = Linker.nativeLinker();
    private static boolean AVAILABLE = false;

    // MPFR Handles
    private static MethodHandle MPFR_INIT2;
    private static MethodHandle MPFR_CLEAR;
    private static MethodHandle MPFR_SET_STR;
    private static MethodHandle MPFR_GET_STR;
    private static MethodHandle MPFR_ADD;
    private static MethodHandle MPFR_SUB;
    private static MethodHandle MPFR_MUL;
    @SuppressWarnings("unused")
    private static MethodHandle MPFR_DIV;
    private static MethodHandle MPFR_FREE_STR;

    public static final StructLayout MPFR_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_LONG.withName("prec"),
        ValueLayout.JAVA_INT.withName("sign"),
        MemoryLayout.paddingLayout(4),
        ValueLayout.JAVA_LONG.withName("exp"),
        ValueLayout.ADDRESS.withName("d")
    );

    static {
        try {
            // Using the Arena-based version which returns Optional<SymbolLookup>
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
                MPFR_FREE_STR = lookup(mpfr, "mpfr_free_str", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

                AVAILABLE = MPFR_INIT2 != null && MPFR_ADD != null && MPFR_MUL != null && MPFR_SUB != null;
                if (AVAILABLE) {
                    logger.info("Native MPFR Backend initialized (Panama).");
                }
            }
        } catch (Throwable t) {
            logger.warn("Failed to initialize MPFR Backend: {}", t.getMessage());
        }
    }

    private static MethodHandle lookup(SymbolLookup lookup, String name, FunctionDescriptor desc) {
        return lookup.find(name).map(s -> LINKER.downcallHandle(s, desc)).orElse(null);
    }

    @Override public boolean isAvailable() { return AVAILABLE && !isExplicitlyDisabled(); }
    @Override public String getId() { return "native-mpfr-dense"; }
    @Override public String getName() { return "Native MPFR Arbitrary-Precision Backend"; }
    @Override public String getDescription() { return "High-performance Arbitrary Precision Linear Algebra backend using libmpfr bound via Project Panama."; }
    
    @Override
    public String getType() {
        return "linearalgebra";
    }

    @Override
    public int getPriority() {
        return 120;
    }

    @Override
    public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.CPU;
    }
    @Override public boolean isLoaded() { return AVAILABLE; }
    @Override public String getNativeLibraryName() { return "mpfr"; }
    @Override public Object createBackend() { return this; }
    @Override public String getEnvironmentInfo() { return AVAILABLE ? "CPU (Panama/MPFR)" : "N/A"; }

    @Override
    public double score(OperationContext context) {
        if (!AVAILABLE) return -1.0;
        return 50.0;
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.ExecutionContext() {
            @Override public <T> T execute(org.episteme.core.technical.backend.Operation<T> op) { return op.compute(this); }
            @Override public void close() {}
        };
    }

    @Override public void shutdown() {}

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        checkDimensionsMultiply(a, b);
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();

        boolean isComplex = ((Object)a.get(0, 0)) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_B = initMatrix(b, arena, prec, isComplex);
            MemorySegment h_C = allocateMatrix(m, n, arena, prec, isComplex);

            MemorySegment temp1 = arena.allocate(MPFR_LAYOUT);
            MPFR_INIT2.invoke(temp1, prec);
            MemorySegment temp2 = arena.allocate(MPFR_LAYOUT);
            MPFR_INIT2.invoke(temp2, prec);

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (isComplex) {
                        MemorySegment sumR = getMPFR(h_C, i, j, n, 0, true);
                        MemorySegment sumI = getMPFR(h_C, i, j, n, 1, true);
                        MPFR_SET_STR.invoke(sumR, arena.allocateFrom("0"), 10, 0);
                        MPFR_SET_STR.invoke(sumI, arena.allocateFrom("0"), 10, 0);

                        for (int l = 0; l < k; l++) {
                            MemorySegment ar = getMPFR(h_A, i, l, k, 0, true);
                            MemorySegment ai = getMPFR(h_A, i, l, k, 1, true);
                            MemorySegment br = getMPFR(h_B, l, j, n, 0, true);
                            MemorySegment bi = getMPFR(h_B, l, j, n, 1, true);

                            // ar*br - ai*bi
                            MPFR_MUL.invoke(temp1, ar, br, 0);
                            MPFR_MUL.invoke(temp2, ai, bi, 0);
                            MPFR_SUB.invoke(temp1, temp1, temp2, 0);
                            MPFR_ADD.invoke(sumR, sumR, temp1, 0);

                            // ar*bi + ai*br
                            MPFR_MUL.invoke(temp1, ar, bi, 0);
                            MPFR_MUL.invoke(temp2, ai, br, 0);
                            MPFR_ADD.invoke(temp1, temp1, temp2, 0);
                            MPFR_ADD.invoke(sumI, sumI, temp1, 0);
                        }
                    } else {
                        MemorySegment sum = getMPFR(h_C, i, j, n, 0, false);
                        MPFR_SET_STR.invoke(sum, arena.allocateFrom("0"), 10, 0);
                        for (int l = 0; l < k; l++) {
                            MPFR_MUL.invoke(temp1, getMPFR(h_A, i, l, k, 0, false), getMPFR(h_B, l, j, n, 0, false), 0);
                            MPFR_ADD.invoke(sum, sum, temp1, 0);
                        }
                    }
                }
            }

            MPFR_CLEAR.invoke(temp1);
            MPFR_CLEAR.invoke(temp2);

            return backToMatrix(h_C, m, n, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR multiply failed", t);
        }
    }

    private long getPrecision() {
        int digits = MathContext.getCurrent().getJavaMathContext().getPrecision();
        if (digits <= 0) digits = 50;
        return (long) (digits * 3.322) + 1;
    }

    private MemorySegment allocateMatrix(int rows, int cols, Arena arena, long prec, boolean isComplex) throws Throwable {
        int multiplier = isComplex ? 2 : 1;
        MemorySegment mat = arena.allocate(MPFR_LAYOUT, (long) rows * cols * multiplier);
        for (int i = 0; i < rows * cols * multiplier; i++) {
            MPFR_INIT2.invoke(mat.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), prec);
        }
        return mat;
    }

    private MemorySegment initMatrix(Matrix<Real> m, Arena arena, long prec, boolean isComplex) throws Throwable {
        int rows = m.rows();
        int cols = m.cols();
        MemorySegment mat = allocateMatrix(rows, cols, arena, prec, isComplex);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Real val = m.get(i, j);
                if (isComplex) {
                    org.episteme.core.mathematics.numbers.complex.Complex cv = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) val;
                    MPFR_SET_STR.invoke(getMPFR(mat, i, j, cols, 0, true), arena.allocateFrom(cv.getReal().bigDecimalValue().toPlainString()), 10, 0);
                    MPFR_SET_STR.invoke(getMPFR(mat, i, j, cols, 1, true), arena.allocateFrom(cv.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                } else {
                    Real rv = (Real) val;
                    MPFR_SET_STR.invoke(getMPFR(mat, i, j, cols, 0, false), arena.allocateFrom(rv.bigDecimalValue().toPlainString()), 10, 0);
                }
            }
        }
        return mat;
    }

    private MemorySegment getMPFR(MemorySegment mat, int row, int col, int cols, int component, boolean isComplex) {
        int stride = isComplex ? 2 : 1;
        long index = ((long) row * cols + col) * stride + component;
        return mat.asSlice(index * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
    }

    private Matrix<Real> backToMatrix(MemorySegment mat, int rows, int cols, Arena arena, org.episteme.core.mathematics.structures.rings.Ring<Real> ring, boolean isComplex) throws Throwable {
        org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<Real> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<>(rows, cols, ring.zero());
        MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isComplex) {
                    Real r = readMPFR(getMPFR(mat, i, j, cols, 0, true), expPtr, arena);
                    Real im = readMPFR(getMPFR(mat, i, j, cols, 1, true), expPtr, arena);
                    storage.set(i, j, (Real) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, im));
                } else {
                    storage.set(i, j, readMPFR(getMPFR(mat, i, j, cols, 0, false), expPtr, arena));
                }
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<Real>(storage.getData(), rows, cols, ring);
    }

    private Real readMPFR(MemorySegment val, MemorySegment expPtr, Arena arena) throws Throwable {
        MemorySegment strPtr = (MemorySegment) MPFR_GET_STR.invoke(MemorySegment.NULL, expPtr, 10, 0L, val, 0);
        String s = strPtr.reinterpret(1024).getString(0);
        long exp = expPtr.get(ValueLayout.JAVA_LONG, 0);
        StringBuilder sb = new StringBuilder();
        if (s.startsWith("-")) { sb.append("-0.").append(s.substring(1)); } 
        else { sb.append("0.").append(s); }
        sb.append("E").append(exp);
        MPFR_FREE_STR.invoke(strPtr);
        return Real.of(new java.math.BigDecimal(sb.toString()));
    }

    @Override public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) { throw new UnsupportedOperationException(); }
    @Override public Matrix<Real> inverse(Matrix<Real> a) { throw new UnsupportedOperationException(); }
    @Override public Real determinant(Matrix<Real> a) { throw new UnsupportedOperationException(); }
    @Override public Matrix<Real> transpose(Matrix<Real> a) { throw new UnsupportedOperationException(); }

    private void checkDimensionsMultiply(Matrix<?> a, Matrix<?> b) {
        if (a.cols() != b.rows()) throw new IllegalArgumentException("Matrix dimensions do not match");
    }
}
