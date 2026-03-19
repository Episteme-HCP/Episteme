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
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
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
    private static MethodHandle MPFR_DIV;
    private static MethodHandle MPFR_SET; // Added MPFR_SET declaration
    private static MethodHandle MPFR_CMP_ABS;
    private static MethodHandle MPFR_FREE_STR;

    public static final StructLayout MPFR_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_INT.withName("prec"),
        ValueLayout.JAVA_INT.withName("sign"),
        ValueLayout.JAVA_INT.withName("exp"),
        MemoryLayout.paddingLayout(4),
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
                MPFR_SET = lookup(mpfr, "mpfr_set", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT)); // Corrected MPFR_SET
                MPFR_CMP_ABS = lookup(mpfr, "mpfr_cmpabs", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
                MPFR_FREE_STR = lookup(mpfr, "mpfr_free_str", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

                AVAILABLE = MPFR_INIT2 != null && MPFR_ADD != null && MPFR_MUL != null && MPFR_CMP_ABS != null && MPFR_SUB != null && MPFR_SET != null && MPFR_DIV != null;
                if (AVAILABLE) {
                    logger.info("Native MPFR Dense Backend initialized (Panama).");
                } else {
                    logger.warn("Native MPFR Dense Backend initialization partial - some handles missing:");
                    if (MPFR_INIT2 == null) logger.warn("  - mpfr_init2 NOT FOUND");
                    if (MPFR_ADD == null) logger.warn("  - mpfr_add NOT FOUND");
                    if (MPFR_MUL == null) logger.warn("  - mpfr_mul NOT FOUND");
                    if (MPFR_CMP_ABS == null) logger.warn("  - mpfr_cmpabs NOT FOUND");
                    if (MPFR_SUB == null) logger.warn("  - mpfr_sub NOT FOUND");
                    if (MPFR_SET == null) logger.warn("  - mpfr_set NOT FOUND");
                    if (MPFR_DIV == null) logger.warn("  - mpfr_div NOT FOUND");
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
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) return true;
        if (ring.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex) return true;
        return ring.zero() instanceof Real;
    }

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

    public java.util.Set<String> getCapabilities() {
        return java.util.Set.of("Transpose", "Add", "Subtract", "Scale", "Multiply", "Solve");
    }

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
            NativeSafe.invoke(MPFR_INIT2, temp1, prec);
            MemorySegment temp2 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp2, prec);

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (isComplex) {
                        MemorySegment sumR = getMPFR(h_C, i, j, n, 0, true);
                        MemorySegment sumI = getMPFR(h_C, i, j, n, 1, true);
                        NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, 0);
                        NativeSafe.invoke(MPFR_SET_STR, sumI, arena.allocateFrom("0"), 10, 0);

                        for (int l = 0; l < k; l++) {
                            MemorySegment ar = getMPFR(h_A, i, l, k, 0, true);
                            MemorySegment ai = getMPFR(h_A, i, l, k, 1, true);
                            MemorySegment br = getMPFR(h_B, l, j, n, 0, true);
                            MemorySegment bi = getMPFR(h_B, l, j, n, 1, true);

                            // ar*br - ai*bi
                            NativeSafe.invoke(MPFR_MUL, temp1, ar, br, 0);
                            NativeSafe.invoke(MPFR_MUL, temp2, ai, bi, 0);
                            NativeSafe.invoke(MPFR_SUB, temp1, temp1, temp2, 0);
                            NativeSafe.invoke(MPFR_ADD, sumR, sumR, temp1, 0);

                            // ar*bi + ai*br
                            NativeSafe.invoke(MPFR_MUL, temp1, ar, bi, 0);
                            NativeSafe.invoke(MPFR_MUL, temp2, ai, br, 0);
                            NativeSafe.invoke(MPFR_ADD, temp1, temp1, temp2, 0);
                            NativeSafe.invoke(MPFR_ADD, sumI, sumI, temp1, 0);
                        }
                    } else {
                        MemorySegment sum = getMPFR(h_C, i, j, n, 0, false);
                        NativeSafe.invoke(MPFR_SET_STR, sum, arena.allocateFrom("0"), 10, 0);
                        for (int l = 0; l < k; l++) {
                            NativeSafe.invoke(MPFR_MUL, temp1, getMPFR(h_A, i, l, k, 0, false), getMPFR(h_B, l, j, n, 0, false), 0);
                            NativeSafe.invoke(MPFR_ADD, sum, sum, temp1, 0);
                        }
                    }
                }
            }

            NativeSafe.invoke(MPFR_CLEAR, temp1);
            NativeSafe.invoke(MPFR_CLEAR, temp2);

            return backToMatrix(h_C, m, n, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            logger.error("MPFR multiply failed: {}", t.getMessage(), t);
            throw new RuntimeException("MPFR multiply failed: " + t.getMessage(), t);
        }
    }

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        if (a.cols() != b.dimension()) throw new IllegalArgumentException("Matrix-Vector dimension mismatch");
        int m = a.rows();
        int k = a.cols();
        
        boolean isComplex = ((Object)a.get(0, 0)) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_X = initVector(b, arena, prec, isComplex);
            MemorySegment h_Y = allocateVector(m, arena, prec, isComplex);
            
            MemorySegment temp1 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp1, prec);
            MemorySegment temp2 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp2, prec);
            
            for (int i = 0; i < m; i++) {
                if (isComplex) {
                    MemorySegment sumR = getMPFRVector(h_Y, i, 0, true);
                    MemorySegment sumI = getMPFRVector(h_Y, i, 1, true);
                    NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, sumI, arena.allocateFrom("0"), 10, 0);
                    
                    for (int l = 0; l < k; l++) {
                        MemorySegment ar = getMPFR(h_A, i, l, k, 0, true);
                        MemorySegment ai = getMPFR(h_A, i, l, k, 1, true);
                        MemorySegment xr = getMPFRVector(h_X, l, 0, true);
                        MemorySegment xi = getMPFRVector(h_X, l, 1, true);
                        
                        // ar*xr - ai*xi
                        NativeSafe.invoke(MPFR_MUL, temp1, ar, xr, 0);
                        NativeSafe.invoke(MPFR_MUL, temp2, ai, xi, 0);
                        NativeSafe.invoke(MPFR_SUB, temp1, temp1, temp2, 0);
                        NativeSafe.invoke(MPFR_ADD, sumR, sumR, temp1, 0);
                        
                        // ar*xi + ai*xr
                        NativeSafe.invoke(MPFR_MUL, temp1, ar, xi, 0);
                        NativeSafe.invoke(MPFR_MUL, temp2, ai, xr, 0);
                        NativeSafe.invoke(MPFR_ADD, temp1, temp1, temp2, 0);
                        NativeSafe.invoke(MPFR_ADD, sumI, sumI, temp1, 0);
                    }
                } else {
                    MemorySegment sum = getMPFRVector(h_Y, i, 0, false);
                    NativeSafe.invoke(MPFR_SET_STR, sum, arena.allocateFrom("0"), 10, 0);
                    for (int l = 0; l < k; l++) {
                        NativeSafe.invoke(MPFR_MUL, temp1, getMPFR(h_A, i, l, k, 0, false), getMPFRVector(h_X, l, 0, false), 0);
                        NativeSafe.invoke(MPFR_ADD, sum, sum, temp1, 0);
                    }
                }
            }
            
            NativeSafe.invoke(MPFR_CLEAR, temp1);
            NativeSafe.invoke(MPFR_CLEAR, temp2);
            
            return backToVector(h_Y, m, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR Matrix-Vector multiply failed", t);
        }
    }

    private MemorySegment allocateVector(int dimension, Arena arena, long prec, boolean isComplex) throws Throwable {
        int multiplier = isComplex ? 2 : 1;
        MemorySegment vec = arena.allocate(MPFR_LAYOUT, (long) dimension * multiplier);
        for (int i = 0; i < dimension * multiplier; i++) {
            NativeSafe.invoke(MPFR_INIT2, vec.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), prec);
        }
        return vec;
    }

    private MemorySegment initVector(Vector<Real> v, Arena arena, long prec, boolean isComplex) throws Throwable {
        int dim = v.dimension();
        MemorySegment vec = allocateVector(dim, arena, prec, isComplex);
        for (int i = 0; i < dim; i++) {
            Real val = v.get(i);
            if (isComplex) {
                org.episteme.core.mathematics.numbers.complex.Complex cv = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) val;
                NativeSafe.invoke(MPFR_SET_STR, getMPFRVector(vec, i, 0, true), arena.allocateFrom(cv.getReal().bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, getMPFRVector(vec, i, 1, true), arena.allocateFrom(cv.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
            } else {
                NativeSafe.invoke(MPFR_SET_STR, getMPFRVector(vec, i, 0, false), arena.allocateFrom(val.bigDecimalValue().toPlainString()), 10, 0);
            }
        }
        return vec;
    }

    private MemorySegment getMPFRVector(MemorySegment vec, int idx, int component, boolean isComplex) {
        int stride = isComplex ? 2 : 1;
        long index = (long) idx * stride + component;
        return vec.asSlice(index * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
    }

    private Vector<Real> backToVector(MemorySegment vec, int dim, Arena arena, org.episteme.core.mathematics.structures.rings.Ring<Real> ring, boolean isComplex) throws Throwable {
        java.util.List<Real> list = new java.util.ArrayList<>(dim);
        MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_INT);
        for (int i = 0; i < dim; i++) {
            if (isComplex) {
                Real r = readMPFR(getMPFRVector(vec, i, 0, true), expPtr, arena);
                Real im = readMPFR(getMPFRVector(vec, i, 1, true), expPtr, arena);
                list.add((Real) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, im));
            } else {
                list.add(readMPFR(getMPFRVector(vec, i, 0, false), expPtr, arena));
            }
        }
        return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(list, ring);
    }

    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) {
        checkDimensionsAdd(a, b);
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.get(0, 0)) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_B = initMatrix(b, arena, prec, isComplex);
            MemorySegment h_C = allocateMatrix(m, n, arena, prec, isComplex);

            int multiplier = isComplex ? 2 : 1;
            for (int i = 0; i < m * n * multiplier; i++) {
                MemorySegment ra = h_A.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rb = h_B.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rc = h_C.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_ADD, rc, ra, rb, 0);
            }
            return backToMatrix(h_C, m, n, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR add failed", t);
        }
    }

    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) {
        checkDimensionsAdd(a, b);
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.get(0, 0)) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_B = initMatrix(b, arena, prec, isComplex);
            MemorySegment h_C = allocateMatrix(m, n, arena, prec, isComplex);

            int multiplier = isComplex ? 2 : 1;
            for (int i = 0; i < m * n * multiplier; i++) {
                MemorySegment ra = h_A.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rb = h_B.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rc = h_C.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_SUB, rc, ra, rb, 0);
            }
            return backToMatrix(h_C, m, n, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR subtract failed", t);
        }
    }

    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) {
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.get(0, 0)) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_C = allocateMatrix(m, n, arena, prec, isComplex);
            MemorySegment s = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, s, prec);
            NativeSafe.invoke(MPFR_SET_STR, s, arena.allocateFrom(scalar.bigDecimalValue().toPlainString()), 10, 0);

            int multiplier = isComplex ? 2 : 1;
            for (int i = 0; i < m * n * multiplier; i++) {
                MemorySegment ra = h_A.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rc = h_C.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_MUL, rc, ra, s, 0);
            }
            return backToMatrix(h_C, m, n, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR scale failed", t);
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
            NativeSafe.invoke(MPFR_INIT2, mat.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), prec);
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
                    NativeSafe.invoke(MPFR_SET_STR, getMPFR(mat, i, j, cols, 0, true), arena.allocateFrom(cv.getReal().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, getMPFR(mat, i, j, cols, 1, true), arena.allocateFrom(cv.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                } else {
                    Real rv = (Real) val;
                    NativeSafe.invoke(MPFR_SET_STR, getMPFR(mat, i, j, cols, 0, false), arena.allocateFrom(rv.bigDecimalValue().toPlainString()), 10, 0);
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

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.get(0, 0)) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_C = allocateMatrix(n, m, arena, prec, isComplex);
            
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (isComplex) {
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_C, j, i, m, 0, true), getMPFR(h_A, i, j, n, 0, true), 0);
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_C, j, i, m, 1, true), getMPFR(h_A, i, j, n, 1, true), 0);
                    } else {
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_C, j, i, m, 0, false), getMPFR(h_A, i, j, n, 0, false), 0);
                    }
                }
            }
            return backToMatrix(h_C, n, m, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            logger.error("MPFR transpose failed: {}", t.getMessage(), t);
            throw new RuntimeException("MPFR transpose failed", t);
        }
    }

    private Matrix<Real> backToMatrix(MemorySegment mat, int rows, int cols, Arena arena, org.episteme.core.mathematics.structures.rings.Ring<Real> ring, boolean isComplex) throws Throwable {
        org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<Real> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<>(rows, cols, ring.zero());
        MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_INT);
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
        return new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<Real>(storage, this, ring);
    }

    private Real readMPFR(MemorySegment val, MemorySegment expPtr, Arena arena) throws Throwable {
        MemorySegment strPtr = (MemorySegment) NativeSafe.invoke(MPFR_GET_STR, MemorySegment.NULL, expPtr, 10, 0L, val, 0);
        if (strPtr.equals(MemorySegment.NULL)) {
             throw new RuntimeException("mpfr_get_str returned NULL");
        }
        String s = strPtr.reinterpret(1024).getString(0);
        int exp = expPtr.get(ValueLayout.JAVA_INT, 0);
        
        if (s.isEmpty() || s.equals("0")) {
             NativeSafe.invoke(MPFR_FREE_STR, strPtr);
             return Real.ZERO;
        }

        StringBuilder sb = new StringBuilder();
        if (s.startsWith("-")) {
            if (s.length() > 1) {
                sb.append("-0.").append(s.substring(1));
            } else {
                sb.append("-0"); // Should not happen with base 10
            }
        } else {
            sb.append("0.").append(s);
        }
        sb.append("E").append(exp);
        NativeSafe.invoke(MPFR_FREE_STR, strPtr);
        
        try {
            return Real.of(new java.math.BigDecimal(sb.toString()));
        } catch (NumberFormatException e) {
            logger.error("Failed to parse MPFR string: '{}' (exp={}) - Final string: '{}'", s, exp, sb);
            throw new RuntimeException("Invalid MPFR numeric string: " + sb + " (Original: " + s + ", Exp: " + exp + ")", e);
        }
    }

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        if (a.rows() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        int n = a.rows();
        boolean isComplex = ((Object)a.get(0, 0)) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_B = initVector(b, arena, prec, isComplex);
            
            // Partial Pivoting GEPP
            for (int k = 0; k < n; k++) {
                int pivot = k;
                for (int i = k + 1; i < n; i++) {
                    if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, isComplex), getMPFR(h_A, pivot, k, n, 0, isComplex)) > 0) {
                        pivot = i;
                    }
                }
                
                if (pivot != k) {
                    // Swap rows in A
                    for (int j = 0; j < n; j++) {
                        MemorySegment tempSwap = arena.allocate(MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, tempSwap, prec);
                        MemorySegment akj = getMPFR(h_A, k, j, n, 0, isComplex);
                        MemorySegment apj = getMPFR(h_A, pivot, j, n, 0, isComplex);
                        NativeSafe.invoke(MPFR_SET, tempSwap, akj, 0);
                        NativeSafe.invoke(MPFR_SET, akj, apj, 0);
                        NativeSafe.invoke(MPFR_SET, apj, tempSwap, 0);
                    }
                    // Swap rows in B
                    MemorySegment tempSwapB = arena.allocate(MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_INIT2, tempSwapB, prec);
                    MemorySegment bk = getMPFRVector(h_B, k, 0, isComplex);
                    MemorySegment bp = getMPFRVector(h_B, pivot, 0, isComplex);
                    NativeSafe.invoke(MPFR_SET, tempSwapB, bk, 0);
                    NativeSafe.invoke(MPFR_SET, bk, bp, 0);
                    NativeSafe.invoke(MPFR_SET, bp, tempSwapB, 0);
                }
                
                // Gaussian Elimination
                for (int i = k + 1; i < n; i++) {
                    MemorySegment factor = arena.allocate(MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_INIT2, factor, prec);
                    
                    MemorySegment aik = getMPFR(h_A, i, k, n, 0, isComplex);
                    MemorySegment akk = getMPFR(h_A, k, k, n, 0, isComplex);
                    
                    NativeSafe.invoke(MPFR_DIV, factor, aik, akk, 0);
                    
                    for (int j = k; j < n; j++) {
                        MemorySegment aij = getMPFR(h_A, i, j, n, 0, isComplex);
                        MemorySegment akj = getMPFR(h_A, k, j, n, 0, isComplex);
                        MemorySegment term = arena.allocate(MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, term, prec);
                        NativeSafe.invoke(MPFR_MUL, term, factor, akj, 0);
                        NativeSafe.invoke(MPFR_SUB, aij, aij, term, 0);
                    }
                    
                    MemorySegment bi = getMPFRVector(h_B, i, 0, isComplex);
                    MemorySegment bk = getMPFRVector(h_B, k, 0, isComplex);
                    MemorySegment termB = arena.allocate(MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_INIT2, termB, prec);
                    NativeSafe.invoke(MPFR_MUL, termB, factor, bk, 0);
                    NativeSafe.invoke(MPFR_SUB, bi, bi, termB, 0);
                }
            }
            
            // Back substitution
            MemorySegment h_X = allocateVector(n, arena, prec, isComplex);
            for (int i = n - 1; i >= 0; i--) {
                MemorySegment sum = arena.allocate(MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, sum, prec);
                NativeSafe.invoke(MPFR_SET_STR, sum, arena.allocateFrom("0"), 10, 0);
                
                for (int j = i + 1; j < n; j++) {
                    MemorySegment aij = getMPFR(h_A, i, j, n, 0, isComplex);
                    MemorySegment xj = getMPFRVector(h_X, j, 0, isComplex);
                    MemorySegment term = arena.allocate(MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_INIT2, term, prec);
                    NativeSafe.invoke(MPFR_MUL, term, aij, xj, 0);
                    NativeSafe.invoke(MPFR_ADD, sum, sum, term, 0);
                }
                
                MemorySegment bi = getMPFRVector(h_B, i, 0, isComplex);
                MemorySegment xi = getMPFRVector(h_X, i, 0, isComplex);
                MemorySegment aii = getMPFR(h_A, i, i, n, 0, isComplex);
                
                NativeSafe.invoke(MPFR_SUB, xi, bi, sum, 0);
                NativeSafe.invoke(MPFR_DIV, xi, xi, aii, 0);
            }
            
            return backToVector(h_X, n, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            logger.error("MPFR solve failed: {}", t.getMessage(), t);
            throw new RuntimeException("MPFR solve failed", t);
        }
    }

    @Override public Matrix<Real> inverse(Matrix<Real> a) { throw new UnsupportedOperationException(); }
    @Override public Real determinant(Matrix<Real> a) { throw new UnsupportedOperationException(); }

    private void checkDimensionsMultiply(Matrix<?> a, Matrix<?> b) {
        if (a.cols() != b.rows()) throw new IllegalArgumentException("Matrix dimensions do not match");
    }

    private void checkDimensionsAdd(Matrix<?> a, Matrix<?> b) {
        if (a.rows() != b.rows() || a.cols() != b.cols()) throw new IllegalArgumentException("Matrix dimensions do not match");
    }
}
