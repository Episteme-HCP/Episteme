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
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
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
@SuppressWarnings("unchecked")
public class NativeMPFRDenseLinearAlgebraProvider<E> implements LinearAlgebraBackend<E>, NativeBackend, CPUBackend {

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
    private static MethodHandle MPFR_SET;
    private static MethodHandle MPFR_CMP_ABS;
    private static MethodHandle MPFR_FREE_STR;
    private static MethodHandle MPFR_SET_UI;
    private static MethodHandle MPFR_CMP;
    private static MethodHandle MPFR_ZERO_P;
    private static MethodHandle MPFR_NEG;
    private static MethodHandle MPFR_SET_D;

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
                MPFR_SET = lookup(mpfr, "mpfr_set", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_CMP_ABS = lookup(mpfr, "mpfr_cmpabs", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
                MPFR_FREE_STR = lookup(mpfr, "mpfr_free_str", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
                MPFR_SET_UI = lookup(mpfr, "mpfr_set_ui", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT));
                MPFR_CMP = lookup(mpfr, "mpfr_cmp", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
                MPFR_ZERO_P = lookup(mpfr, "mpfr_zero_p", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                MPFR_NEG = lookup(mpfr, "mpfr_neg", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_SET_D = lookup(mpfr, "mpfr_set_d", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_INT));

                AVAILABLE = MPFR_INIT2 != null && MPFR_ADD != null && MPFR_MUL != null && MPFR_CMP_ABS != null && MPFR_SUB != null && MPFR_SET != null && MPFR_DIV != null && MPFR_SET_UI != null && MPFR_SET_D != null;
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
    @Override public String getName() { return "Native MPFR Dense Linear Algebra Backend"; }
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
        return true; 
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
        return java.util.Set.of("Transpose", "Add", "Subtract", "Scale", "Multiply", "Solve", "Dot", "Norm", "LU");
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initVector(a, arena, prec, isComplex);
            MemorySegment h_B = initVector(b, arena, prec, isComplex);
            MemorySegment sumR = arena.allocate(MPFR_LAYOUT);
            MemorySegment sumI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            NativeSafe.invoke(MPFR_INIT2, sumR, prec);
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, sumI, prec);
            NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, 0);
            if (isComplex) NativeSafe.invoke(MPFR_SET_STR, sumI, arena.allocateFrom("0"), 10, 0);

            MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
            MemorySegment t2 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t1, prec);
            NativeSafe.invoke(MPFR_INIT2, t2, prec);

            int n = a.dimension();
            for (int i = 0; i < n; i++) {
                if (isComplex) {
                    MemorySegment ar = getMPFRVector(h_A, i, 0, true);
                    MemorySegment ai = getMPFRVector(h_A, i, 1, true);
                    MemorySegment br = getMPFRVector(h_B, i, 0, true);
                    MemorySegment bi = getMPFRVector(h_B, i, 1, true);
                    // r = ar*br - ai*(-bi) = ar*br + ai*bi (Hermitian dot)
                    // Wait! Standard dot is usually sum(ai * bi)
                    // If Hermitian, we use conjugate of b? Or a?
                    // HighPrecisionComplianceTest doesn't specify Hermitian.
                    // Assume standard dot for now.
                    NativeSafe.invoke(MPFR_MUL, t1, ar, br, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, ai, bi, 0);
                    NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);

                    NativeSafe.invoke(MPFR_MUL, t1, ar, bi, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, ai, br, 0);
                    NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, sumI, sumI, t1, 0);
                } else {
                    NativeSafe.invoke(MPFR_MUL, t1, getMPFRVector(h_A, i, 0, false), getMPFRVector(h_B, i, 0, false), 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                }
            }
            MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);
            if (isComplex) {
                Real r = readMPFR(sumR, expPtr, arena);
                Real im = readMPFR(sumI, expPtr, arena);
                return (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, im);
            } else {
                return (E) (Object) readMPFR(sumR, expPtr, arena);
            }
        } catch (Throwable t) {
            throw new RuntimeException("MPFR dot failed", t);
        }
    }

    @Override
    public E norm(Vector<E> a) {
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initVector(a, arena, prec, isComplex);
            MemorySegment sumR = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, sumR, prec);
            NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, 0);

            MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t1, prec);

            int n = a.dimension();
            for (int i = 0; i < n; i++) {
                if (isComplex) {
                    MemorySegment ar = getMPFRVector(h_A, i, 0, true);
                    MemorySegment ai = getMPFRVector(h_A, i, 1, true);
                    NativeSafe.invoke(MPFR_MUL, t1, ar, ar, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                    NativeSafe.invoke(MPFR_MUL, t1, ai, ai, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                } else {
                    MemorySegment ar = getMPFRVector(h_A, i, 0, false);
                    NativeSafe.invoke(MPFR_MUL, t1, ar, ar, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                }
            }
            // Add mpfr_sqrt handle if not present
            SymbolLookup mpfr = NativeFFMLoader.loadLibrary("mpfr", Arena.global()).get();
            MethodHandle sqrt = lookup(mpfr, "mpfr_sqrt", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
            NativeSafe.invoke(sqrt, sumR, sumR, 0);

            MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);
            if (isComplex) {
                Real r = readMPFR(sumR, expPtr, arena);
                return (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, Real.ZERO);
            } else {
                return (E) (Object) readMPFR(sumR, expPtr, arena);
            }
        } catch (Throwable t) {
            throw new RuntimeException("MPFR norm failed", t);
        }
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();

        if (k != b.rows()) throw new IllegalArgumentException("Matrix dimensions do not match.");

        // Detect complex using the scalar ring instead of probing elements to avoid ClassCastException
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0; // MPFR_RNDN
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_B = initMatrix(b, arena, prec, isComplex);
            MemorySegment h_C = allocateMatrix(m, n, arena, prec, isComplex);

            MemorySegment temp1 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp1, prec);
            MemorySegment temp2 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp2, prec); // MPFR_RNDN
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (isComplex) {
                        MemorySegment sumR = getMPFR(h_C, i, j, n, 0, true);
                        MemorySegment sumI = getMPFR(h_C, i, j, n, 1, true);
                        NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, rnd);
                        NativeSafe.invoke(MPFR_SET_STR, sumI, arena.allocateFrom("0"), 10, rnd);

                        for (int l = 0; l < k; l++) {
                            MemorySegment ar = getMPFR(h_A, i, l, k, 0, true);
                            MemorySegment ai = getMPFR(h_A, i, l, k, 1, true);
                            MemorySegment br = getMPFR(h_B, l, j, n, 0, true);
                            MemorySegment bi = getMPFR(h_B, l, j, n, 1, true);

                            // ar*br - ai*bi
                            NativeSafe.invoke(MPFR_MUL, temp1, ar, br, rnd);
                            NativeSafe.invoke(MPFR_MUL, temp2, ai, bi, rnd);
                            NativeSafe.invoke(MPFR_SUB, temp1, temp1, temp2, rnd);
                            NativeSafe.invoke(MPFR_ADD, sumR, sumR, temp1, rnd);

                            // ar*bi + ai*br
                            NativeSafe.invoke(MPFR_MUL, temp1, ar, bi, rnd);
                            NativeSafe.invoke(MPFR_MUL, temp2, ai, br, rnd);
                            NativeSafe.invoke(MPFR_ADD, temp1, temp1, temp2, rnd);
                            NativeSafe.invoke(MPFR_ADD, sumI, sumI, temp1, rnd);
                        }
                    } else {
                        MemorySegment sum = getMPFR(h_C, i, j, n, 0, false);
                        NativeSafe.invoke(MPFR_SET_STR, sum, arena.allocateFrom("0"), 10, rnd);

                        for (int l = 0; l < k; l++) {
                            MemorySegment a1 = getMPFR(h_A, i, l, k, 0, false);
                            MemorySegment b1 = getMPFR(h_B, l, j, n, 0, false);
                            NativeSafe.invoke(MPFR_MUL, temp1, a1, b1, rnd);
                            NativeSafe.invoke(MPFR_ADD, sum, sum, temp1, rnd);
                        }
                    }
                }
            }

            NativeSafe.invoke(MPFR_CLEAR, temp1);
            NativeSafe.invoke(MPFR_CLEAR, temp2);
            Matrix<E> res = (Matrix<E>) backToMatrix_internal(h_C, m, n, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, m * k * (isComplex ? 2 : 1));
            clearMPFRArray(h_B, k * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_C, m * n * (isComplex ? 2 : 1));

            return res;
        } catch (Throwable t) {
            logger.error("MPFR multiply failed: {}", t.getMessage(), t);
            throw new RuntimeException("MPFR multiply failed: " + t.getMessage(), t);
        }
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (a.cols() != b.dimension()) throw new IllegalArgumentException("Matrix-Vector dimension mismatch");
        int m = a.rows();
        int k = a.cols();
        
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        int rnd = 0; // MPFR_RNDN
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
                    NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, rnd);
                    NativeSafe.invoke(MPFR_SET_STR, sumI, arena.allocateFrom("0"), 10, rnd);
                    
                    for (int l = 0; l < k; l++) {
                        MemorySegment ar = getMPFR(h_A, i, l, k, 0, true);
                        MemorySegment ai = getMPFR(h_A, i, l, k, 1, true);
                        MemorySegment xr = getMPFRVector(h_X, l, 0, true);
                        MemorySegment xi = getMPFRVector(h_X, l, 1, true);
                        
                        // ar*xr - ai*xi
                        NativeSafe.invoke(MPFR_MUL, temp1, ar, xr, rnd);
                        NativeSafe.invoke(MPFR_MUL, temp2, ai, xi, rnd);
                        NativeSafe.invoke(MPFR_SUB, temp1, temp1, temp2, rnd);
                        NativeSafe.invoke(MPFR_ADD, sumR, sumR, temp1, rnd);
                        
                        // ar*xi + ai*xr
                        NativeSafe.invoke(MPFR_MUL, temp1, ar, xi, rnd);
                        NativeSafe.invoke(MPFR_MUL, temp2, ai, xr, rnd);
                        NativeSafe.invoke(MPFR_ADD, temp1, temp1, temp2, rnd);
                        NativeSafe.invoke(MPFR_ADD, sumI, sumI, temp1, rnd);
                    }
                } else {
                    MemorySegment sum = getMPFRVector(h_Y, i, 0, false);
                    NativeSafe.invoke(MPFR_SET_STR, sum, arena.allocateFrom("0"), 10, rnd);
                    for (int l = 0; l < k; l++) {
                        NativeSafe.invoke(MPFR_MUL, temp1, getMPFR(h_A, i, l, k, 0, false), getMPFRVector(h_X, l, 0, false), rnd);
                        NativeSafe.invoke(MPFR_ADD, sum, sum, temp1, rnd);
                    }
                }
            }
            
            NativeSafe.invoke(MPFR_CLEAR, temp1);
            NativeSafe.invoke(MPFR_CLEAR, temp2);
            
            Vector<E> res = (Vector<E>) backToVector_internal(h_Y, m, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, m * k * (isComplex ? 2 : 1));
            clearMPFRArray(h_X, k * (isComplex ? 2 : 1));
            clearMPFRArray(h_Y, m * (isComplex ? 2 : 1));
            return res;
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

    private MemorySegment initVector(Vector<E> v, Arena arena, long prec, boolean isComplex) throws Throwable {
        int dim = v.dimension();
        MemorySegment vec = allocateVector(dim, arena, prec, isComplex);
        for (int i = 0; i < dim; i++) {
            Object val = v.get(i);
            if (isComplex) {
                org.episteme.core.mathematics.numbers.complex.Complex cv = (org.episteme.core.mathematics.numbers.complex.Complex) val;
                NativeSafe.invoke(MPFR_SET_STR, getMPFRVector(vec, i, 0, true), arena.allocateFrom(cv.getReal().bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, getMPFRVector(vec, i, 1, true), arena.allocateFrom(cv.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
            } else {
                Real rv = (Real) val;
                NativeSafe.invoke(MPFR_SET_STR, getMPFRVector(vec, i, 0, false), arena.allocateFrom(rv.bigDecimalValue().toPlainString()), 10, 0);
            }
        }
        return vec;
    }

    private MemorySegment getMPFRVector(MemorySegment vec, int idx, int component, boolean isComplex) {
        int stride = isComplex ? 2 : 1;
        long index = (long) idx * stride + component;
        return vec.asSlice(index * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
    }

    private Vector<E> backToVector_internal(MemorySegment vec, int dim, Arena arena, org.episteme.core.mathematics.structures.rings.Ring<E> ring, boolean isComplex) throws Throwable {
        java.util.List<E> list = new java.util.ArrayList<E>(dim);
        MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);
        for (int i = 0; i < dim; i++) {
            if (isComplex) {
                Real r = readMPFR(getMPFRVector(vec, i, 0, true), expPtr, arena);
                Real img = readMPFR(getMPFRVector(vec, i, 1, true), expPtr, arena);
                list.add((E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, img));
            } else {
                list.add((E) (Object) readMPFR(getMPFRVector(vec, i, 0, false), expPtr, arena));
            }
        }
        return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(list, ring);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        checkDimensionsAdd(a, b);
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
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
            Matrix<E> res = backToMatrix_internal(h_C, m, n, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, m * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_B, m * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_C, m * n * (isComplex ? 2 : 1));
            return res;
        } catch (Throwable t) {
            throw new RuntimeException("MPFR add failed", t);
        }
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        checkDimensionsAdd(a, b);
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision(); // MPFR_RNDN

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
            Matrix<E> res = backToMatrix_internal(h_C, m, n, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, m * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_B, m * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_C, m * n * (isComplex ? 2 : 1));
            return res;
        } catch (Throwable t) {
            throw new RuntimeException("MPFR subtract failed", t);
        }
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
    
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_C = allocateMatrix(m, n, arena, prec, isComplex);
    
            if (isComplex) {
                org.episteme.core.mathematics.numbers.complex.Complex cs = (org.episteme.core.mathematics.numbers.complex.Complex)(Object)scalar;
                MemorySegment sR = arena.allocate(MPFR_LAYOUT);
                MemorySegment sI = arena.allocate(MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, sR, prec);
                NativeSafe.invoke(MPFR_INIT2, sI, prec);
                NativeSafe.invoke(MPFR_SET_STR, sR, arena.allocateFrom(cs.getReal().bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, sI, arena.allocateFrom(cs.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
    
                for (int i = 0; i < m * n; i++) {
                    MemorySegment aR = h_A.asSlice((long) i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment aI = h_A.asSlice((long) (i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment cR = h_C.asSlice((long) i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment cI = h_C.asSlice((long) (i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    complexMultiply(cR, cI, aR, aI, sR, sI, prec, arena);
                }
            } else {
                MemorySegment s = arena.allocate(MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, s, prec);
                String valStr = (scalar instanceof Real rs) ? rs.bigDecimalValue().toPlainString() : scalar.toString();
                NativeSafe.invoke(MPFR_SET_STR, s, arena.allocateFrom(valStr), 10, 0);
    
                for (int i = 0; i < m * n; i++) {
                    MemorySegment ra = h_A.asSlice((long) i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment rc = h_C.asSlice((long) i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_MUL, rc, ra, s, 0);
                }
            }
            Matrix<E> res = backToMatrix_internal(h_C, m, n, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, m * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_C, m * n * (isComplex ? 2 : 1));
            return res;
        } catch (Throwable t) {
            throw new RuntimeException("MPFR scale failed", t);
        }
    }

    private long getPrecision() {
        int digits = org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext().getPrecision();
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

    private MemorySegment initMatrix(Matrix<E> m, Arena arena, long prec, boolean isComplex) throws Throwable {
        int rows = m.rows();
        int cols = m.cols();
        MemorySegment mat = allocateMatrix(rows, cols, arena, prec, isComplex);
        MatrixStorage<?> storage = m.getStorage(); // Bypasses implicit GenericMatrix casts
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Object val = storage.get(i, j);
                if (isComplex) {
                    org.episteme.core.mathematics.numbers.complex.Complex cv = (org.episteme.core.mathematics.numbers.complex.Complex) val;
                    setMPFR(getMPFR(mat, i, j, cols, 0, true), cv.getReal(), arena, 0);
                    setMPFR(getMPFR(mat, i, j, cols, 1, true), cv.getImaginary(), arena, 0);
                } else {
                    setMPFR(getMPFR(mat, i, j, cols, 0, false), (Real) val, arena, 0);
                }
            }
        }
        return mat;
    }

    private void setMPFR(MemorySegment mpfr, Real val, Arena arena, int rnd) throws Throwable {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealDouble rd) {
            NativeSafe.invoke(MPFR_SET_D, mpfr, rd.doubleValue(), rnd);
        } else {
            NativeSafe.invoke(MPFR_SET_STR, mpfr, arena.allocateFrom(val.bigDecimalValue().toPlainString()), 10, rnd);
        }
    }

    private MemorySegment getMPFR(MemorySegment mat, int row, int col, int cols, int component, boolean isComplex) {
        int stride = isComplex ? 2 : 1;
        long index = ((long) row * cols + col) * stride + component;
        return mat.asSlice(index * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
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
            Matrix<E> res = backToMatrix_internal(h_C, n, m, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, m * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_C, n * m * (isComplex ? 2 : 1));
            return res;
        } catch (Throwable t) {
            logger.error("MPFR transpose failed: {}", t.getMessage(), t);
            throw new RuntimeException("MPFR transpose failed", t);
        }
    }

    private Matrix<E> backToMatrix_internal(MemorySegment mat, int rows, int cols, Arena arena, org.episteme.core.mathematics.structures.rings.Ring<E> ring, boolean isComplex) throws Throwable {
        org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(rows, cols, (E)ring.zero());
        MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isComplex) {
                    Real r = readMPFR(getMPFR(mat, i, j, cols, 0, true), expPtr, arena);
                    Real img = readMPFR(getMPFR(mat, i, j, cols, 1, true), expPtr, arena);
                    storage.set(i, j, (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, img));
                } else {
                    storage.set(i, j, (E) (Object) readMPFR(getMPFR(mat, i, j, cols, 0, false), expPtr, arena));
                }
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(storage, (LinearAlgebraProvider<E>) this, ring);
    }
    
    private void clearMPFRArray(MemorySegment mat, int count) {
        if (mat == MemorySegment.NULL) return;
        for (int i = 0; i < count; i++) {
            try { NativeSafe.invoke(MPFR_CLEAR, mat.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT)); } catch (Throwable t) {}
        }
    }

    private Real readMPFR(MemorySegment val, MemorySegment expPtr, Arena arena) throws Throwable {
        MemorySegment strPtr = (MemorySegment) NativeSafe.invoke(MPFR_GET_STR, MemorySegment.NULL, expPtr, 10, 0L, val, 0);
        if (strPtr == null || strPtr.equals(MemorySegment.NULL)) {
            return Real.ZERO;
        }
        
        String s = NativeSafe.scavenge(strPtr, 1024, arena, "mpfr_get_str").segment().getString(0);
        // mpfr_exp_t is long, but on Windows long is 32-bit.
        long exp = expPtr.get(ValueLayout.JAVA_INT, 0); 
        
        if (s.isEmpty() || s.equals("0")) {
             NativeSafe.invoke(MPFR_FREE_STR, strPtr);
             return Real.ZERO;
        }

        // Special handling for MPFR NaN/Inf
        String su = s.toUpperCase();
        if (su.equals("NAN") || su.contains("@NAN@")) {
            NativeSafe.invoke(MPFR_FREE_STR, strPtr);
            return Real.NaN;
        }
        if (su.equals("INF") || su.contains("@INF@") || su.contains("INFINITY")) {
            boolean neg = s.startsWith("-");
            NativeSafe.invoke(MPFR_FREE_STR, strPtr);
            return neg ? Real.NEGATIVE_INFINITY : Real.POSITIVE_INFINITY;
        }

        StringBuilder sb = new StringBuilder();
        if (s.startsWith("-")) {
            if (s.length() > 1) {
                sb.append("-0.").append(s.substring(1));
            } else {
                sb.append("-0"); 
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
            if (s.contains("@")) return Real.NaN;
            throw new RuntimeException("Invalid MPFR numeric string: " + sb + " (Original: " + s + ", Exp: " + exp + ")", e);
        }
    }
    @Override
    public QRResult<E> qr(Matrix<E> a) {
        return GenericQR.decompose(a, (Field<E>) a.getScalarRing());
    }

    @Override
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        return GenericCholesky.decompose(a, (Field<E>) a.getScalarRing());
    }

    @Override
    public SVDResult<E> svd(Matrix<E> a) {
        return GenericSVD.decompose(a, (Field<E>) a.getScalarRing());
    }

    @Override
    public EigenResult<E> eigen(Matrix<E> a) {
        return GenericEigen.decompose(a, (Field<E>) a.getScalarRing());
    }

    @Override
    public Vector<E> solve(QRResult<E> qr, Vector<E> b) {
        return GenericQR.solve(qr, b, (Field<E>) b.getScalarRing());
    }

    @Override
    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        return GenericCholesky.solve(cholesky, b, (Field<E>) b.getScalarRing());
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        if (a.rows() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        int n = a.rows();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0; // MPFR_RNDN

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_B = initVector(b, arena, prec, isComplex);
            
            
            MemorySegment tempFactorR = MemorySegment.NULL, tempFactorI = MemorySegment.NULL;
            MemorySegment tempFactor = MemorySegment.NULL, sumR = MemorySegment.NULL, sumI = MemorySegment.NULL;
            MemorySegment sum = MemorySegment.NULL, term = MemorySegment.NULL;
            
            if (isComplex) {
                tempFactorR = arena.allocate(MPFR_LAYOUT); tempFactorI = arena.allocate(MPFR_LAYOUT);
                sumR = arena.allocate(MPFR_LAYOUT); sumI = arena.allocate(MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, tempFactorR, prec); NativeSafe.invoke(MPFR_INIT2, tempFactorI, prec);
                NativeSafe.invoke(MPFR_INIT2, sumR, prec); NativeSafe.invoke(MPFR_INIT2, sumI, prec);
            } else {
                tempFactor = arena.allocate(MPFR_LAYOUT); sum = arena.allocate(MPFR_LAYOUT); term = arena.allocate(MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, tempFactor, prec); NativeSafe.invoke(MPFR_INIT2, sum, prec); NativeSafe.invoke(MPFR_INIT2, term, prec);
            }
            
            // Partial Pivoting GEPP
            for (int k = 0; k < n; k++) {
                int pivot = k;
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        if (compareComplexMagnitude(h_A, i, pivot, k, n, arena, prec)) {
                            pivot = i;
                        }
                    } else {
                        if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, pivot, k, n, 0, false)) > 0) {
                            pivot = i;
                        }
                    }
                }
                
                if (pivot != k) {
                    swapRows(h_A, k, pivot, n, isComplex, arena, prec);
                    swapRowsVector(h_B, k, pivot, isComplex, arena, prec);
                }
                
                // Gaussian Elimination
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        complexDivide(tempFactorR, tempFactorI, 
                            getMPFR(h_A, i, k, n, 0, true), getMPFR(h_A, i, k, n, 1, true),
                            getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true),
                            prec, arena);
                        
                        for (int j = k; j < n; j++) {
                            complexSubtractMul(h_A, i, j, tempFactorR, tempFactorI, h_A, k, j, n, arena, prec);
                        }
                        complexSubtractMulVector(h_B, i, tempFactorR, tempFactorI, h_B, k, arena, prec);
                    } else {
                        NativeSafe.invoke(MPFR_DIV, tempFactor, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, k, k, n, 0, false), rnd);
                        
                        for (int j = k; j < n; j++) {
                            subtractMulReal(h_A, i, j, tempFactor, h_A, k, j, n, arena, prec);
                        }
                        subtractMulVectorReal(h_B, i, tempFactor, h_B, k, arena, prec);
                    }
                }
            }
            
            // Back substitution
            MemorySegment h_X = allocateVector(n, arena, prec, isComplex);
            for (int i = n - 1; i >= 0; i--) {
                if (isComplex) {
                    NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, sumI, arena.allocateFrom("0"), 10, 0);
                    
                    for (int j = i + 1; j < n; j++) {
                        complexAddMul(sumR, sumI, 
                            getMPFR(h_A, i, j, n, 0, true), getMPFR(h_A, i, j, n, 1, true),
                            getMPFRVector(h_X, j, 0, true), getMPFRVector(h_X, j, 1, true),
                            prec, arena);
                    }
                    
                    MemorySegment xiR = getMPFRVector(h_X, i, 0, true);
                    MemorySegment xiI = getMPFRVector(h_X, i, 1, true);
                    NativeSafe.invoke(MPFR_SUB, xiR, getMPFRVector(h_B, i, 0, true), sumR, 0);
                    NativeSafe.invoke(MPFR_SUB, xiI, getMPFRVector(h_B, i, 1, true), sumI, 0);
                    
                    complexDivide(xiR, xiI, xiR, xiI, 
                        getMPFR(h_A, i, i, n, 0, true), getMPFR(h_A, i, i, n, 1, true),
                        prec, arena);
                } else {
                    NativeSafe.invoke(MPFR_SET_STR, sum, arena.allocateFrom("0"), 10, 0);
                    
                    for (int j = i + 1; j < n; j++) {
                        NativeSafe.invoke(MPFR_MUL, term, getMPFR(h_A, i, j, n, 0, false), getMPFRVector(h_X, j, 0, false), 0);
                        NativeSafe.invoke(MPFR_ADD, sum, sum, term, 0);
                    }
                    
                    MemorySegment xi = getMPFRVector(h_X, i, 0, false);
                    NativeSafe.invoke(MPFR_SUB, xi, getMPFRVector(h_B, i, 0, false), sum, rnd);
                    NativeSafe.invoke(MPFR_DIV, xi, xi, getMPFR(h_A, i, i, n, 0, false), rnd);
                }
            }
            
            Vector<E> res = backToVector_internal(h_X, n, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, n * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_B, n * (isComplex ? 2 : 1));
            clearMPFRArray(h_X, n * (isComplex ? 2 : 1));
            
            if (isComplex) {
                NativeSafe.invoke(MPFR_CLEAR, tempFactorR); NativeSafe.invoke(MPFR_CLEAR, tempFactorI);
                NativeSafe.invoke(MPFR_CLEAR, sumR); NativeSafe.invoke(MPFR_CLEAR, sumI);
            } else {
                NativeSafe.invoke(MPFR_CLEAR, tempFactor); NativeSafe.invoke(MPFR_CLEAR, sum); NativeSafe.invoke(MPFR_CLEAR, term);
            }
            
            return res;
        } catch (Throwable t) {
            logger.error("MPFR solve failed: {}", t.getMessage(), t);
            throw new RuntimeException("MPFR solve failed", t);
        }
    }


    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        int n = a.rows();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision(); // MPFR_RNDN

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_Inv = allocateMatrix(n, n, arena, prec, isComplex);
            int rnd = 0;
            // Initialize Identity
            for (int i = 0; i < n; i++) {
                if (isComplex) {
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_Inv, i, i, n, 0, true), 1L, rnd);
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_Inv, i, i, n, 1, true), 0L, rnd);
                } else {
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_Inv, i, i, n, 0, false), 1L, rnd);
                }
            }

            // Gauss-Jordan Elimination
            for (int k = 0; k < n; k++) {
                // Pivot search
                int pivot = k;
                if (isComplex) {
                    for (int i = k + 1; i < n; i++) {
                        if (compareComplexMagnitude(h_A, i, pivot, k, n, arena, prec)) pivot = i;
                    }
                } else {
                    for (int i = k + 1; i < n; i++) {
                        if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, pivot, k, n, 0, false)) > 0) pivot = i;
                    }
                }

                if (pivot != k) {
                    swapRows(h_A, k, pivot, n, isComplex, arena, prec);
                    swapRows(h_Inv, k, pivot, n, isComplex, arena, prec);
                }
                
                // Check for singular matrix
                if (isComplex) {
                    if ((int) NativeSafe.invoke(MPFR_ZERO_P, getMPFR(h_A, k, k, n, 0, true)) != 0 &&
                        (int) NativeSafe.invoke(MPFR_ZERO_P, getMPFR(h_A, k, k, n, 1, true)) != 0) {
                        throw new ArithmeticException("Singular matrix");
                    }
                } else {
                    if ((int) NativeSafe.invoke(MPFR_ZERO_P, getMPFR(h_A, k, k, n, 0, false)) != 0) {
                        throw new ArithmeticException("Singular matrix");
                    }
                }

                // Normalize pivot row
                if (isComplex) {
                    MemorySegment pR = arena.allocate(MPFR_LAYOUT);
                    MemorySegment pI = arena.allocate(MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_INIT2, pR, prec);
                    NativeSafe.invoke(MPFR_INIT2, pI, prec);
                    NativeSafe.invoke(MPFR_SET, pR, getMPFR(h_A, k, k, n, 0, true), rnd);
                    NativeSafe.invoke(MPFR_SET, pI, getMPFR(h_A, k, k, n, 1, true), rnd);
                    
                    for (int j = 0; j < n; j++) {
                        complexDivide(getMPFR(h_A, k, j, n, 0, true), getMPFR(h_A, k, j, n, 1, true),
                            getMPFR(h_A, k, j, n, 0, true), getMPFR(h_A, k, j, n, 1, true),
                            pR, pI, prec, arena);
                        complexDivide(getMPFR(h_Inv, k, j, n, 0, true), getMPFR(h_Inv, k, j, n, 1, true),
                            getMPFR(h_Inv, k, j, n, 0, true), getMPFR(h_Inv, k, j, n, 1, true),
                            pR, pI, prec, arena);
                    }
                    // Set A[k][k] to 1+0i explicitly for stability
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_A, k, k, n, 0, true), 1L, rnd);
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_A, k, k, n, 1, true), 0L, rnd);
                } else {
                    MemorySegment pivotVal = arena.allocate(MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_INIT2, pivotVal, prec);
                    NativeSafe.invoke(MPFR_SET, pivotVal, getMPFR(h_A, k, k, n, 0, false), rnd);
                    
                    for (int j = 0; j < n; j++) {
                        NativeSafe.invoke(MPFR_DIV, getMPFR(h_A, k, j, n, 0, false), getMPFR(h_A, k, j, n, 0, false), pivotVal, rnd);
                        NativeSafe.invoke(MPFR_DIV, getMPFR(h_Inv, k, j, n, 0, false), getMPFR(h_Inv, k, j, n, 0, false), pivotVal, rnd);
                    }
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_A, k, k, n, 0, false), 1L, rnd);
                }
                
                // Eliminate other rows
                for (int i = 0; i < n; i++) {
                    if (i == k) continue;
                    if (isComplex) {
                        MemorySegment fR = arena.allocate(MPFR_LAYOUT);
                        MemorySegment fI = arena.allocate(MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, fR, prec);
                        NativeSafe.invoke(MPFR_INIT2, fI, prec);
                        NativeSafe.invoke(MPFR_SET, fR, getMPFR(h_A, i, k, n, 0, true), 0);
                        NativeSafe.invoke(MPFR_SET, fI, getMPFR(h_A, i, k, n, 1, true), 0);
                        
                        for (int j = 0; j < n; j++) {
                            complexSubtractMul(h_A, i, j, fR, fI, h_A, k, j, n, arena, prec);
                            complexSubtractMul(h_Inv, i, j, fR, fI, h_Inv, k, j, n, arena, prec);
                        }
                    } else {
                        MemorySegment factor = arena.allocate(MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, factor, prec);
                        NativeSafe.invoke(MPFR_SET, factor, getMPFR(h_A, i, k, n, 0, false), 0);
                        
                        for (int j = 0; j < n; j++) {
                            subtractMulReal(h_A, i, j, factor, h_A, k, j, n, arena, prec);
                            subtractMulReal(h_Inv, i, j, factor, h_Inv, k, j, n, arena, prec);
                        }
                    }
                }
            }
            Matrix<E> res = backToMatrix_internal(h_Inv, n, n, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, n * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_Inv, n * n * (isComplex ? 2 : 1));
            return res;
        } catch (Throwable t) {
            logger.error("MPFR inverse failed: {}", t.getMessage());
            throw new RuntimeException("MPFR inverse failed", t);
        }
    }

    @Override
    public E determinant(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        int n = a.rows();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision(); // MPFR_RNDN

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment[] detComp = new MemorySegment[2]; // detComp[0] for real, detComp[1] for imag
            detComp[0] = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, detComp[0], prec);
            NativeSafe.invoke(MPFR_SET_UI, detComp[0], 1L, 0); // Initialize det to 1
            if (isComplex) {
                detComp[1] = arena.allocate(MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, detComp[1], prec);
                NativeSafe.invoke(MPFR_SET_UI, detComp[1], 0L, 0); // Initialize imag part to 0
            }

            for (int k = 0; k < n; k++) {
                int pivot = k;
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        if (compareComplexMagnitude(h_A, i, pivot, k, n, arena, prec)) pivot = i;
                    } else {
                        if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, pivot, k, n, 0, false)) > 0) pivot = i;
                    }
                }
                
                if (pivot != k) {
                    swapRows(h_A, k, pivot, n, isComplex, arena, prec);
                    NativeSafe.invoke(MPFR_NEG, detComp[0], detComp[0], 0); // Flip sign of determinant
                    if (isComplex) NativeSafe.invoke(MPFR_NEG, detComp[1], detComp[1], 0);
                }
                
                // Multiply determinant by A[k][k]
                if (isComplex) {
                    complexMultiply(detComp[0], detComp[1], detComp[0], detComp[1], 
                        getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true), prec, arena);
                } else {
                    NativeSafe.invoke(MPFR_MUL, detComp[0], detComp[0], getMPFR(h_A, k, k, n, 0, false), 0);
                }
                
                // Check for zero determinant
                if (isComplex) {
                    if ((int) NativeSafe.invoke(MPFR_ZERO_P, getMPFR(h_A, k, k, n, 0, true)) != 0 &&
                        (int) NativeSafe.invoke(MPFR_ZERO_P, getMPFR(h_A, k, k, n, 1, true)) != 0) {
                        return (E) a.getScalarRing().zero(); // Determinant is 0
                    }
                } else {
                    if ((int) NativeSafe.invoke(MPFR_ZERO_P, getMPFR(h_A, k, k, n, 0, false)) != 0) {
                        return (E) a.getScalarRing().zero(); // Determinant is 0
                    }
                }

                // Eliminate
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        MemorySegment fR = arena.allocate(MPFR_LAYOUT);
                        MemorySegment fI = arena.allocate(MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, fR, prec);
                        NativeSafe.invoke(MPFR_INIT2, fI, prec);
                        complexDivide(fR, fI, getMPFR(h_A, i, k, n, 0, true), getMPFR(h_A, i, k, n, 1, true),
                            getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true), prec, arena);
                        for (int j = k; j < n; j++) complexSubtractMul(h_A, i, j, fR, fI, h_A, k, j, n, arena, prec);
                    } else {
                        MemorySegment factor = arena.allocate(MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, factor, prec);
                        NativeSafe.invoke(MPFR_DIV, factor, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, k, k, n, 0, false), 0);
                        for (int j = k; j < n; j++) subtractMulReal(h_A, i, j, factor, h_A, k, j, n, arena, prec);
                    }
                }
            }
            
            // The determinant is now in detComp
            MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);
            E resDet;
            if (isComplex) {
                resDet = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(
                    readMPFR(detComp[0], expPtr, arena),
                    readMPFR(detComp[1], expPtr, arena)
                );
            } else {
                resDet = (E) (Object) readMPFR(detComp[0], expPtr, arena);
            }
            NativeSafe.invoke(MPFR_CLEAR, detComp[0]);
            if (isComplex) NativeSafe.invoke(MPFR_CLEAR, detComp[1]);
            clearMPFRArray(h_A, n * n * (isComplex ? 2 : 1));
            return resDet;
        } catch (Throwable t) {
            logger.error("MPFR determinant failed: {}", t.getMessage());
            throw new RuntimeException("MPFR determinant failed", t);
        }
    }



    private void subtractMulReal(MemorySegment h_Mat, int r, int c, MemorySegment factor, MemorySegment h_Source, int rs, int cs, int cols, Arena arena, long prec) {
        MemorySegment dst = getMPFR(h_Mat, r, c, cols, 0, false);
        MemorySegment src = getMPFR(h_Source, rs, cs, cols, 0, false);
        MemorySegment term = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, term, prec);
        NativeSafe.invoke(MPFR_MUL, term, factor, src, 0);
        NativeSafe.invoke(MPFR_SUB, dst, dst, term, 0);
    }

    private void subtractMulVectorReal(MemorySegment h_Vec, int r, MemorySegment factor, MemorySegment h_Source, int rs, Arena arena, long prec) {
        MemorySegment dst = getMPFRVector(h_Vec, r, 0, false);
        MemorySegment src = getMPFRVector(h_Source, rs, 0, false);
        MemorySegment term = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, term, prec);
        NativeSafe.invoke(MPFR_MUL, term, factor, src, 0);
        NativeSafe.invoke(MPFR_SUB, dst, dst, term, 0);
    }

    private boolean compareComplexMagnitude(MemorySegment h_Mat, int r1, int r2, int col, int n, Arena arena, long prec) throws Throwable {
        MemorySegment mag1 = arena.allocate(MPFR_LAYOUT);
        MemorySegment mag2 = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, mag1, prec);
        NativeSafe.invoke(MPFR_INIT2, mag2, prec);
        
        complexMagnitudeSquared(mag1, getMPFR(h_Mat, r1, col, n, 0, true), getMPFR(h_Mat, r1, col, n, 1, true), prec, arena);
        complexMagnitudeSquared(mag2, getMPFR(h_Mat, r2, col, n, 0, true), getMPFR(h_Mat, r2, col, n, 1, true), prec, arena);
        
        return (int) NativeSafe.invoke(MPFR_CMP, mag1, mag2) > 0;
    }

    private void complexMagnitudeSquared(MemorySegment res, MemorySegment r, MemorySegment i, long prec, Arena arena) {
        MemorySegment t = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, t, prec);
        NativeSafe.invoke(MPFR_MUL, res, r, r, 0);
        NativeSafe.invoke(MPFR_MUL, t, i, i, 0);
        NativeSafe.invoke(MPFR_ADD, res, res, t, 0);
    }

    private void complexDivide(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment bR, MemorySegment bI, long prec, Arena arena) {
        MemorySegment denom = arena.allocate(MPFR_LAYOUT);
        MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT);
        MemorySegment outR = arena.allocate(MPFR_LAYOUT);
        MemorySegment outI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, denom, prec);
        NativeSafe.invoke(MPFR_INIT2, t1, prec);
        NativeSafe.invoke(MPFR_INIT2, t2, prec);
        NativeSafe.invoke(MPFR_INIT2, outR, prec);
        NativeSafe.invoke(MPFR_INIT2, outI, prec);
        
        // denom = bR^2 + bI^2
        NativeSafe.invoke(MPFR_MUL, denom, bR, bR, 0);
        NativeSafe.invoke(MPFR_MUL, t1, bI, bI, 0);
        NativeSafe.invoke(MPFR_ADD, denom, denom, t1, 0);
        
        // outR = (aR*bR + aI*bI) / denom
        NativeSafe.invoke(MPFR_MUL, t1, aR, bR, 0);
        NativeSafe.invoke(MPFR_MUL, t2, aI, bI, 0);
        NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
        NativeSafe.invoke(MPFR_DIV, outR, t1, denom, 0);
        
        // outI = (aI*bR - aR*bI) / denom
        NativeSafe.invoke(MPFR_MUL, t1, aI, bR, 0);
        NativeSafe.invoke(MPFR_MUL, t2, aR, bI, 0);
        NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
        NativeSafe.invoke(MPFR_DIV, outI, t1, denom, 0);

        NativeSafe.invoke(MPFR_SET, resR, outR, 0);
        NativeSafe.invoke(MPFR_SET, resI, outI, 0);

        NativeSafe.invoke(MPFR_CLEAR, denom);
        NativeSafe.invoke(MPFR_CLEAR, t1);
        NativeSafe.invoke(MPFR_CLEAR, t2);
        NativeSafe.invoke(MPFR_CLEAR, outR);
        NativeSafe.invoke(MPFR_CLEAR, outI);
    }

    private void complexSubtractMul(MemorySegment h_Mat, int r, int c, MemorySegment fR, MemorySegment fI, MemorySegment h_Src, int rs, int cs, int n, Arena arena, long prec) {
        MemorySegment dstR = getMPFR(h_Mat, r, c, n, 0, true);
        MemorySegment dstI = getMPFR(h_Mat, r, c, n, 1, true);
        MemorySegment srcR = getMPFR(h_Src, rs, cs, n, 0, true);
        MemorySegment srcI = getMPFR(h_Src, rs, cs, n, 1, true);
        
        MemorySegment tR = arena.allocate(MPFR_LAYOUT);
        MemorySegment tI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, tR, prec);
        NativeSafe.invoke(MPFR_INIT2, tI, prec);
        
        // t = f * src
        NativeSafe.invoke(MPFR_MUL, tR, fR, srcR, 0);
        NativeSafe.invoke(MPFR_MUL, tI, fI, srcI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tI, 0); // tR = fR*srcR - fI*srcI
        
        NativeSafe.invoke(MPFR_MUL, tI, fR, srcI, 0);
        MemorySegment tEmp = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tEmp, prec);
        NativeSafe.invoke(MPFR_MUL, tEmp, fI, srcR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0); // tI = fR*srcI + fI*srcR
        
        // dst = dst - t
        NativeSafe.invoke(MPFR_SUB, dstR, dstR, tR, 0);
        NativeSafe.invoke(MPFR_SUB, dstI, dstI, tI, 0);
    }

    private void complexSubtractMulVector(MemorySegment h_Vec, int r, MemorySegment fR, MemorySegment fI, MemorySegment h_Src, int rs, Arena arena, long prec) {
        MemorySegment dstR = getMPFRVector(h_Vec, r, 0, true);
        MemorySegment dstI = getMPFRVector(h_Vec, r, 1, true);
        MemorySegment srcR = getMPFRVector(h_Src, rs, 0, true);
        MemorySegment srcI = getMPFRVector(h_Src, rs, 1, true);
        
        MemorySegment tR = arena.allocate(MPFR_LAYOUT);
        MemorySegment tI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, tR, prec);
        NativeSafe.invoke(MPFR_INIT2, tI, prec);
        
        NativeSafe.invoke(MPFR_MUL, tR, fR, srcR, 0);
        NativeSafe.invoke(MPFR_MUL, tI, fI, srcI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tI, 0);
        
        NativeSafe.invoke(MPFR_MUL, tI, fR, srcI, 0);
        MemorySegment tEmp = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tEmp, prec);
        NativeSafe.invoke(MPFR_MUL, tEmp, fI, srcR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0);
        
        NativeSafe.invoke(MPFR_SUB, dstR, dstR, tR, 0);
        NativeSafe.invoke(MPFR_SUB, dstI, dstI, tI, 0);
    }

    private void complexMultiply(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment bR, MemorySegment bI, long prec, Arena arena) {
        MemorySegment tR = arena.allocate(MPFR_LAYOUT);
        MemorySegment tI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, tR, prec);
        NativeSafe.invoke(MPFR_INIT2, tI, prec);
        
        // tR = aR*bR - aI*bI
        NativeSafe.invoke(MPFR_MUL, tR, aR, bR, 0);
        MemorySegment tEmp = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tEmp, prec);
        NativeSafe.invoke(MPFR_MUL, tEmp, aI, bI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tEmp, 0);
        
        // tI = aR*bI + aI*bR
        NativeSafe.invoke(MPFR_MUL, tI, aR, bI, 0);
        NativeSafe.invoke(MPFR_MUL, tEmp, aI, bR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0);
        
        NativeSafe.invoke(MPFR_SET, resR, tR, 0);
        NativeSafe.invoke(MPFR_SET, resI, tI, 0);
    }

    private void complexAddMul(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment bR, MemorySegment bI, long prec, Arena arena) {
        MemorySegment tR = arena.allocate(MPFR_LAYOUT);
        MemorySegment tI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, tR, prec);
        NativeSafe.invoke(MPFR_INIT2, tI, prec);
        
        // t = a * b
        NativeSafe.invoke(MPFR_MUL, tR, aR, bR, 0);
        NativeSafe.invoke(MPFR_MUL, tI, aI, bI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tI, 0);
        
        NativeSafe.invoke(MPFR_MUL, tI, aR, bI, 0);
        MemorySegment tEmp = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tEmp, prec);
        NativeSafe.invoke(MPFR_MUL, tEmp, aI, bR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0);
        
        // res = res + t
        NativeSafe.invoke(MPFR_ADD, resR, resR, tR, 0);
        NativeSafe.invoke(MPFR_ADD, resI, resI, tI, 0);
    }

    private void swapRows(MemorySegment h_Mat, int r1, int r2, int n, boolean isComplex, Arena arena, long prec) {
        int multiplier = isComplex ? 2 : 1;
        for (int j = 0; j < n * multiplier; j++) {
            MemorySegment m1 = h_Mat.asSlice(((long)r1 * n * multiplier + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            MemorySegment m2 = h_Mat.asSlice(((long)r2 * n * multiplier + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            MemorySegment tmp = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, tmp, prec);
            NativeSafe.invoke(MPFR_SET, tmp, m1, 0);
            NativeSafe.invoke(MPFR_SET, m1, m2, 0);
            NativeSafe.invoke(MPFR_SET, m2, tmp, 0);
        }
    }

    private void swapRowsVector(MemorySegment h_Vec, int r1, int r2, boolean isComplex, Arena arena, long prec) {
        int multiplier = isComplex ? 2 : 1;
        for (int j = 0; j < multiplier; j++) {
            MemorySegment m1 = getMPFRVector(h_Vec, r1, j, isComplex);
            MemorySegment m2 = getMPFRVector(h_Vec, r2, j, isComplex);
            MemorySegment tmp = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, tmp, prec);
            NativeSafe.invoke(MPFR_SET, tmp, m1, 0);
            NativeSafe.invoke(MPFR_SET, m1, m2, 0);
            NativeSafe.invoke(MPFR_SET, m2, tmp, 0);
        }
    }

    private void checkDimensionsAdd(Matrix<?> a, Matrix<?> b) {
        if (a.rows() != b.rows() || a.cols() != b.cols()) throw new IllegalArgumentException("Matrix dimensions do not match");
    }
}
