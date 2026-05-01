/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import com.google.auto.service.AutoService;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import static org.episteme.nativ.mathematics.numbers.real.backends.NativeMPFRNumbers.*;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.episteme.nativ.mathematics.numbers.real.NativeRealBig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.nativ.technical.backend.nativ.ResourceTracker;
import org.episteme.nativ.mathematics.numbers.real.backends.NativeMPFRNumbers;


/**
 * High-performance Arbitrary Precision Linear Algebra backend using libmpfr.
 * Binds directly to MPFR via Project Panama (FFM).
 */
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, CPUBackend.class})
public class NativeMPFRDenseLinearAlgebraBackend<E> implements LinearAlgebraProvider<E>, NativeBackend, CPUBackend {

    private static final Logger logger = LoggerFactory.getLogger("org.episteme.core.mathematics.NativeDiagnostics");
    private static final boolean AVAILABLE = NativeMPFRNumbers.AVAILABLE;

    // Redundant handles removed, using NativeMPFRNumbers

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
        return 5;
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
        if (ring == null) return false;
        Object zero = ring.zero();
        boolean isReal = ring instanceof org.episteme.core.mathematics.sets.Reals || 
                         zero instanceof org.episteme.core.mathematics.numbers.real.Real;
        boolean isComplex = ring instanceof org.episteme.core.mathematics.sets.Complexes || 
                         zero instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        return isReal || isComplex;
    }

    
    // Explicitly check for disabled backend
    @Override
    public boolean isExplicitlyDisabled() {
        return "true".equalsIgnoreCase(System.getProperty("episteme.backend.mpfr.disabled"));
    }

    @Override
    public double score(OperationContext context) {
        if (!AVAILABLE) return -1.0;
        
        double base = getPriority(); 
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
            base += 1000.0; // King of precision
        }
        return base;
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
    public java.util.Map<String, String> getMetadata() {
        return java.util.Map.of(
            "environment", getEnvironmentInfo(),
            "precision", "full (MPFR)",
            "capabilities", "Transpose,Add,Subtract,Scale,Multiply,Inverse,Determinant,Solve,Dot,Norm,LU,QR,Cholesky,SVD,Eigen,Exp,Log,Log10,Sin,Cos,Tan,Asin,Acos,Atan,Sinh,Cosh,Tanh,Asinh,Acosh,Atanh,Sqrt,Cbrt,Pow"
        );
    }

    private static void diag(String msg) {
        logger.debug("[MPFR-DIAG] {}", msg);
    }

    private static void track(ResourceTracker tracker, MemorySegment p) {
        if (p != null && !p.equals(MemorySegment.NULL)) {
            tracker.track(p, s -> {
                try {
                    if (s.scope().isAlive()) {
                        NativeSafe.invoke(MPFR_CLEAR, s);
                    }
                } catch (Throwable t) {
                    // ResourceTracker handles robustness
                }
            });
        }
    }


    private static long getPrecision() {
        org.episteme.core.mathematics.context.MathContext ctx = org.episteme.core.mathematics.context.MathContext.getCurrent();
        int digits = ctx.getJavaMathContext().getPrecision();
        if (digits <= 0) digits = 256; 
        long prec = (long) (digits * 3.322) + 1;
        diag("[MPFR-DIAG] Requested Precision: " + prec + " bits (from " + digits + " digits)");
        return prec;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E dot(Vector<E> a, Vector<E> b) {
        checkDimensionsDot(a, b);
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            long prec = getPrecision();
            MemorySegment h_A = initVector(a, arena, tracker, prec, isComplex);
            MemorySegment h_B = initVector(b, arena, tracker, prec, isComplex);
            MemorySegment sumR = arena.allocate(MPFR_LAYOUT);
            MemorySegment sumI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            NativeSafe.invoke(MPFR_INIT2, sumR, (int) prec); 
            tracker.track(sumR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            NativeSafe.invoke(MPFR_SET_UI, sumR, 0L, 0);
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, sumI, (int) prec);
                tracker.track(sumI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_SET_UI, sumI, 0L, 0);
            }

            MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t1, (int) prec);
            tracker.track(t1, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            MemorySegment t2 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t2, (int) prec);
            tracker.track(t2, s -> NativeSafe.invoke(MPFR_CLEAR, s));

            int n = a.dimension();
            for (int i = 0; i < n; i++) {
                if (isComplex) {
                    MemorySegment ar = getMPFRVector(h_A, i, 0, true);
                    MemorySegment ai = getMPFRVector(h_A, i, 1, true);
                    MemorySegment br = getMPFRVector(h_B, i, 0, true);
                    MemorySegment bi = getMPFRVector(h_B, i, 1, true);
                    // Hermitian dot: sum(a_i^H * b_i) = sum((ar - i*ai) * (br + i*bi))
                    // Real part: ar*br + ai*bi
                    NativeSafe.invoke(MPFR_MUL, t1, ar, br, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, ai, bi, 0);
                    NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);

                    // Imag part: ar*bi - ai*br
                    NativeSafe.invoke(MPFR_MUL, t1, ar, bi, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, ai, br, 0);
                    NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
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
    @SuppressWarnings("unchecked")
    public E norm(Vector<E> a) {
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initVector(a, arena, tracker, prec, isComplex);
            MemorySegment sumR = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, sumR, (int) prec);
            tracker.track(sumR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            NativeSafe.invoke(MPFR_SET_UI, sumR, 0L, 0);

            MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t1, (int) prec);
            tracker.track(t1, s -> NativeSafe.invoke(MPFR_CLEAR, s));

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
            NativeSafe.invoke(MPFR_SQRT, sumR, sumR, 0);

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

        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0; 
        
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment h_B = initMatrix(b, arena, tracker, prec, isComplex);
            MemorySegment h_C = allocateMatrix(m, n, arena, tracker, prec, isComplex);

            MemorySegment temp1 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp1, c_long((int) prec));
            tracker.track(temp1, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            MemorySegment temp2 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp2, c_long((int) prec)); 
            tracker.track(temp2, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            
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

            return backToMatrix_internal(h_C, m, n, arena, a.getScalarRing(), isComplex);
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
        
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment h_X = initVector(b, arena, tracker, prec, isComplex);
            MemorySegment h_Y = allocateVector(m, arena, tracker, prec, isComplex);
            
            MemorySegment temp1 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp1, (int) prec);
            tracker.track(temp1, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            MemorySegment temp2 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp2, (int) prec);
            tracker.track(temp2, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            
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
            
            return backToVector_internal(h_Y, m, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR Matrix-Vector multiply failed", t);
        }
    }

    private static MemorySegment allocateVector(int dimension, Arena arena, ResourceTracker tracker, long prec, boolean isComplex) throws Throwable {
        int multiplier = isComplex ? 2 : 1;
        MemorySegment vec = arena.allocate(MPFR_LAYOUT, (long) dimension * multiplier);
        for (int i = 0; i < dimension * multiplier; i++) {
            MemorySegment slot = vec.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, slot, c_long((int) prec));
            NativeSafe.invoke(MPFR_SET_UI, slot, 0L, 0); 
        }
        tracker.track(vec, s -> clearMPFRArray(s, dimension * multiplier));
        return vec;
    }

    private MemorySegment initVector(Vector<E> v, Arena arena, ResourceTracker tracker, long prec, boolean isComplex) throws Throwable {
        int dim = v.dimension();
        MemorySegment vec = allocateVector(dim, arena, tracker, prec, isComplex);
        for (int i = 0; i < dim; i++) {
            Object val = v.get(i);
            if (isComplex) {
                Real re = getRealPart(val);
                Real im = getImagPart(val);
                NativeSafe.invoke(MPFR_SET_STR, getMPFRVector(vec, i, 0, true), arena.allocateFrom(re.bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, getMPFRVector(vec, i, 1, true), arena.allocateFrom(im.bigDecimalValue().toPlainString()), 10, 0);
            } else {
                Real rv = getRealPart(val);
                NativeSafe.invoke(MPFR_SET_STR, getMPFRVector(vec, i, 0, false), arena.allocateFrom(rv.bigDecimalValue().toPlainString()), 10, 0);
            }
        }
        return vec;
    }

    private Real getRealPart(Object val) {
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex c) return c.getReal();
        if (val instanceof Real r) return r;
        if (val instanceof Number n) return Real.of(n.doubleValue());
        return Real.of(val.toString());
    }

    private Real getImagPart(Object val) {
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex c) return c.getImaginary();
        return Real.ZERO;
    }

    private static MemorySegment getMPFRVector(MemorySegment vec, int idx, int component, boolean isComplex) {
        int stride = isComplex ? 2 : 1;
        long index = (long) idx * stride + component;
        return vec.asSlice(index * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
    }

    @SuppressWarnings("unchecked")
    private Vector<E> backToVector_internal(MemorySegment h_Vec, int n, Arena arena, org.episteme.core.mathematics.structures.rings.Ring<E> ring, boolean isComplex) throws Throwable {
        try {
            java.util.List<E> list = new java.util.ArrayList<E>(n);
            MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);
            boolean useRealDouble = !isComplex && ring instanceof org.episteme.core.mathematics.sets.Reals;
            for (int i = 0; i < n; i++) {
                if (isComplex) {
                    NativeRealBig r = (NativeRealBig) readMPFR(getMPFRVector(h_Vec, i, 0, true), expPtr, arena);
                    NativeRealBig img = (NativeRealBig) readMPFR(getMPFRVector(h_Vec, i, 1, true), expPtr, arena);
                    list.add((E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, img));
                } else {
                    NativeRealBig res = (NativeRealBig) readMPFR(getMPFRVector(h_Vec, i, 0, false), expPtr, arena);
                    if (useRealDouble) {
                        list.add((E) (Object) org.episteme.core.mathematics.numbers.real.RealDouble.create(res.doubleValue()));
                    } else {
                        list.add((E) (Object) res);
                    }
                }
            }
            return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(list), (LinearAlgebraProvider<E>) this, ring);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR backToVector failed", t);
        }
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        checkDimensionsAdd(a, b);
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();

        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment h_B = initMatrix(b, arena, tracker, prec, isComplex);
            MemorySegment h_C = allocateMatrix(m, n, arena, tracker, prec, isComplex);

            int multiplier = isComplex ? 2 : 1;
            for (int i = 0; i < m * n * multiplier; i++) {
                MemorySegment ra = h_A.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rb = h_B.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rc = h_C.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_ADD, rc, ra, rb, 0);
            }
            return backToMatrix_internal(h_C, m, n, arena, a.getScalarRing(), isComplex);
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

        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment h_B = initMatrix(b, arena, tracker, prec, isComplex);
            MemorySegment h_C = allocateMatrix(m, n, arena, tracker, prec, isComplex);

            int multiplier = isComplex ? 2 : 1;
            for (int i = 0; i < m * n * multiplier; i++) {
                MemorySegment ra = h_A.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rb = h_B.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rc = h_C.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_SUB, rc, ra, rb, 0);
            }
            return backToMatrix_internal(h_C, m, n, arena, a.getScalarRing(), isComplex);
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
    
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment h_C = allocateMatrix(m, n, arena, tracker, prec, isComplex);
    
            MemorySegment sR = arena.allocate(MPFR_LAYOUT);
            MemorySegment sI = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, sR, (int) prec);
            tracker.track(sR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            NativeSafe.invoke(MPFR_INIT2, sI, (int) prec);
            tracker.track(sI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
    
            if (isComplex) {
                org.episteme.core.mathematics.numbers.complex.Complex cs = (org.episteme.core.mathematics.numbers.complex.Complex) scalar;
                NativeSafe.invoke(MPFR_SET_STR, sR, arena.allocateFrom(cs.getReal().bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, sI, arena.allocateFrom(cs.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
    
                for (int i = 0; i < m * n; i++) {
                    MemorySegment aR = h_A.asSlice((long) i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment aI = h_A.asSlice((long) (i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment cR = h_C.asSlice((long) i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment cI = h_C.asSlice((long) (i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    complexMultiply(cR, cI, aR, aI, sR, sI, (int) prec, arena, tracker);
                }
            } else {
                String valStr = (scalar instanceof Real rs) ? rs.bigDecimalValue().toPlainString() : scalar.toString();
                NativeSafe.invoke(MPFR_SET_STR, sR, arena.allocateFrom(valStr), 10, 0);
    
                for (int i = 0; i < m * n; i++) {
                    MemorySegment ra = h_A.asSlice((long) i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment rc = h_C.asSlice((long) i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_MUL, rc, ra, sR, 0);
                }
            }
            return backToMatrix_internal(h_C, m, n, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR scale failed", t);
        }
    }


    private static MemorySegment allocateMatrix(int rows, int cols, Arena arena, ResourceTracker tracker, long prec, boolean isComplex) throws Throwable {
        int multiplier = isComplex ? 2 : 1;
        MemorySegment mat = arena.allocate(MPFR_LAYOUT, (long) rows * cols * multiplier);
        for (int i = 0; i < rows * cols * multiplier; i++) {
            MemorySegment slot = mat.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, slot, c_long((int) prec));
            NativeSafe.invoke(MPFR_SET_UI, slot, 0L, 0); 
        }
        tracker.track(mat, s -> clearMPFRArray(s, rows * cols * multiplier));
        return mat;
    }

    private MemorySegment initMatrix(Matrix<E> a, Arena arena, ResourceTracker tracker, long prec, boolean isComplex) throws Throwable {
        int rows = a.rows();
        int cols = a.cols();
        MemorySegment mat = allocateMatrix(rows, cols, arena, tracker, prec, isComplex);
        MatrixStorage<?> storage = a.getStorage(); 
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Object val = storage.get(i, j);
                if (isComplex) {
                    Real re = getRealPart(val);
                    Real im = getImagPart(val);
                    setMPFR(getMPFR(mat, i, j, cols, 0, true), re, arena, 0);
                    setMPFR(getMPFR(mat, i, j, cols, 1, true), im, arena, 0);
                } else {
                    setMPFR(getMPFR(mat, i, j, cols, 0, false), getRealPart(val), arena, 0);
                }
            }
        }
        
        return mat;
    }

    private static void setMPFR(MemorySegment mpfr, Real val, Arena arena, int rnd) throws Throwable {
        if (val instanceof NativeRealBig nrb) {
            NativeSafe.invoke(MPFR_SET, mpfr, nrb.getPtr(), rnd);
        } else if (val instanceof org.episteme.core.mathematics.numbers.real.RealDouble rd) {
            NativeSafe.invoke(MPFR_SET_D, mpfr, rd.doubleValue(), rnd);
        } else {
            NativeSafe.invoke(MPFR_SET_STR, mpfr, arena.allocateFrom(val.bigDecimalValue().toPlainString()), 10, rnd);
        }
    }

    private static MemorySegment getMPFR(MemorySegment mat, int row, int col, int cols, int component, boolean isComplex) {
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
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment h_C = allocateMatrix(n, m, arena, tracker, prec, isComplex);
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
            return backToMatrix_internal(h_C, n, m, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            logger.error("MPFR transpose failed: {}", t.getMessage(), t);
            throw new RuntimeException("MPFR transpose failed", t);
        }
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> backToMatrix_internal(MemorySegment h_Mat, int rows, int cols, Arena arena, org.episteme.core.mathematics.structures.rings.Ring<E> ring, boolean isComplex) throws Throwable {
        org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(rows, cols, (E)ring.zero());
        long prec = getPrecision();
        boolean useRealDouble = !isComplex && ring instanceof org.episteme.core.mathematics.sets.Reals;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isComplex) {
                    MemorySegment ptrR = getMPFR(h_Mat, i, j, cols, 0, true);
                    MemorySegment ptrI = getMPFR(h_Mat, i, j, cols, 1, true);
                    NativeRealBig r = NativeRealBig.copyFrom(ptrR, (int) prec);
                    NativeRealBig im = NativeRealBig.copyFrom(ptrI, (int) prec);
                    storage.set(i, j, (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, im));
                } else {
                    MemorySegment ptr = getMPFR(h_Mat, i, j, cols, 0, false);
                    NativeRealBig res = NativeRealBig.copyFrom(ptr, (int) prec);
                    if (useRealDouble) {
                        storage.set(i, j, (E) (Object) org.episteme.core.mathematics.numbers.real.RealDouble.create(res.doubleValue()));
                    } else {
                        storage.set(i, j, (E) (Object) res);
                    }
                }
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(storage, (LinearAlgebraProvider<E>) this, ring);
    }
    
    private static void clearMPFRArray(MemorySegment mat, int count) {
        if (mat == MemorySegment.NULL || !mat.scope().isAlive()) return;
        for (int i = 0; i < count; i++) {
            try { 
                if (mat.scope().isAlive()) {
                    NativeSafe.invoke(MPFR_CLEAR, mat.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT)); 
                }
            } catch (Throwable t) {}
        }
    }

    private Real readMPFR(MemorySegment mpfr_t, MemorySegment expPtr, Arena arena) {
        return NativeRealBig.copyFrom(mpfr_t, getPrecision());
    }
    @Override
    public EigenResult<E> eigen(Matrix<E> a) {
        return GenericEigen.decompose(a, (Field<E>) a.getScalarRing(), this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public LUResult<E> lu(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        int n = a.rows();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0;

        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            int[] perm = new int[n];
            for (int i = 0; i < n; i++) perm[i] = i;

            MemorySegment factorR = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, factorR, (int) prec);
            tracker.track(factorR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            MemorySegment factorI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, factorI, (int) prec);
                tracker.track(factorI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            }

            for (int k = 0; k < n; k++) {
                int pivotRow = k;
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        if (compareComplexMagnitude(h_A, i, pivotRow, k, n, arena, tracker, (int) prec)) pivotRow = i;
                    } else {
                        if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, pivotRow, k, n, 0, false)) > 0) pivotRow = i;
                    }
                }

                if (pivotRow != k) {
                    swapRows(h_A, k, pivotRow, n, isComplex, arena, tracker, (int) prec);
                    int tmpP = perm[k];
                    perm[k] = perm[pivotRow];
                    perm[pivotRow] = tmpP;
                }

                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        complexDivide(factorR, factorI, 
                            getMPFR(h_A, i, k, n, 0, true), getMPFR(h_A, i, k, n, 1, true),
                            getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true), (int) prec, arena, tracker);
                        
                        // Store factor in L part
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_A, i, k, n, 0, true), factorR, rnd);
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_A, i, k, n, 1, true), factorI, rnd);

                        for (int j = k + 1; j < n; j++) {
                            complexSubtractMul(h_A, i, j, factorR, factorI, h_A, k, j, n, arena, tracker, (int) prec);
                        }
                    } else {
                        NativeSafe.invoke(MPFR_DIV, factorR, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, k, k, n, 0, false), rnd);
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_A, i, k, n, 0, false), factorR, rnd);
                        
                        for (int j = k + 1; j < n; j++) {
                            subtractMulReal(h_A, i, j, factorR, h_A, k, j, n, arena, tracker, (int) prec);
                        }
                    }
                }
            }

            // Extract L and U
            MemorySegment h_L = allocateMatrix(n, n, arena, tracker, prec, isComplex);
            MemorySegment h_U = allocateMatrix(n, n, arena, tracker, prec, isComplex);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i > j) {
                        if (isComplex) {
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_L, i, j, n, 0, true), getMPFR(h_A, i, j, n, 0, true), rnd);
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_L, i, j, n, 1, true), getMPFR(h_A, i, j, n, 1, true), rnd);
                        } else {
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_L, i, j, n, 0, false), getMPFR(h_A, i, j, n, 0, false), rnd);
                        }
                    } else if (i == j) {
                        if (isComplex) {
                            NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_L, i, j, n, 0, true), 1L, rnd);
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_U, i, j, n, 0, true), getMPFR(h_A, i, j, n, 0, true), rnd);
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_U, i, j, n, 1, true), getMPFR(h_A, i, j, n, 1, true), rnd);
                        } else {
                            NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_L, i, j, n, 0, false), 1L, rnd);
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_U, i, j, n, 0, false), getMPFR(h_A, i, j, n, 0, false), rnd);
                        }
                    } else {
                        if (isComplex) {
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_U, i, j, n, 0, true), getMPFR(h_A, i, j, n, 0, true), rnd);
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_U, i, j, n, 1, true), getMPFR(h_A, i, j, n, 1, true), rnd);
                        } else {
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_U, i, j, n, 0, false), getMPFR(h_A, i, j, n, 0, false), rnd);
                        }
                    }
                }
            }

            Matrix<E> lMat = backToMatrix_internal(h_L, n, n, arena, a.getScalarRing(), isComplex);
            Matrix<E> uMat = backToMatrix_internal(h_U, n, n, arena, a.getScalarRing(), isComplex);
            
            Object[] pData = new Object[n];
            for (int i = 0; i < n; i++) {
                if (isComplex) pData[i] = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(Real.of(perm[i]));
                else pData[i] = (E) (Object) Real.of(perm[i]);
            }
            Vector<E> pVec = new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>((java.util.List<E>)(java.util.List<?>)java.util.Arrays.asList(pData)), (LinearAlgebraProvider<E>) this, a.getScalarRing());

            return new LUResult<>(lMat, uMat, pVec);
        } catch (Throwable t) {
            logger.error("MPFR LU failed: {}", t.getMessage());
            throw new RuntimeException("MPFR LU failed", t);
        }
    }






    @Override public Matrix<E> exp(Matrix<E> a) { return applyTranscendental(a, "exp"); }
    @Override public Matrix<E> log(Matrix<E> a) { return applyTranscendental(a, "log"); }
    @Override public Matrix<E> log10(Matrix<E> a) { return applyTranscendental(a, "log10"); }
    @Override public Matrix<E> sin(Matrix<E> a) { return applyTranscendental(a, "sin"); }
    @Override public Matrix<E> cos(Matrix<E> a) { return applyTranscendental(a, "cos"); }
    @Override public Matrix<E> tan(Matrix<E> a) { return applyTranscendental(a, "tan"); }
    @Override public Matrix<E> asin(Matrix<E> a) { return applyTranscendental(a, "asin"); }
    @Override public Matrix<E> acos(Matrix<E> a) { return applyTranscendental(a, "acos"); }
    @Override public Matrix<E> atan(Matrix<E> a) { return applyTranscendental(a, "atan"); }
    @Override public Matrix<E> sinh(Matrix<E> a) { return applyTranscendental(a, "sinh"); }
    @Override public Matrix<E> cosh(Matrix<E> a) { return applyTranscendental(a, "cosh"); }
    @Override public Matrix<E> tanh(Matrix<E> a) { return applyTranscendental(a, "tanh"); }
    @Override public Matrix<E> asinh(Matrix<E> a) { return applyTranscendental(a, "asinh"); }
    @Override public Matrix<E> acosh(Matrix<E> a) { return applyTranscendental(a, "acosh"); }
    @Override public Matrix<E> atanh(Matrix<E> a) { return applyTranscendental(a, "atanh"); }
    @Override public Matrix<E> sqrt(Matrix<E> a) { return applyTranscendental(a, "sqrt"); }
    @Override public Matrix<E> cbrt(Matrix<E> a) { return applyTranscendental(a, "cbrt"); }
    @Override public Matrix<E> pow(Matrix<E> a, E exponent) { return applyTranscendental(a, "pow", exponent); }

    @SuppressWarnings("unchecked")
    public Matrix<E> applyTranscendental(Matrix<E> a, String op, Object... args) {
        if (op.equals("pow")) return executePow(a, (E) args[0]);
        int rows = a.rows();
        int cols = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0;

        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment h_Res = allocateMatrix(rows, cols, arena, tracker, prec, isComplex);
            MethodHandle handle = getHandle(op);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (isComplex) {
                        MemorySegment resR = getMPFR(h_Res, i, j, cols, 0, true);
                        MemorySegment resI = getMPFR(h_Res, i, j, cols, 1, true);
                        MemorySegment aR = getMPFR(h_A, i, j, cols, 0, true);
                        MemorySegment aI = getMPFR(h_A, i, j, cols, 1, true);
                        invokeComplexOp(op, resR, resI, aR, aI, (int) prec, arena, tracker);
                    } else if (handle != null) {
                        NativeSafe.invoke(handle, getMPFR(h_Res, i, j, cols, 0, false), getMPFR(h_A, i, j, cols, 0, false), rnd);
                    } else {
                        throw new UnsupportedOperationException("Native helper for " + op + " not found");
                    }
                }
            }
            return backToMatrix_internal(h_Res, rows, cols, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            logger.error("MPFR transcendental {} failed: {}", op, t.getMessage());
            throw new RuntimeException("MPFR transcendental " + op + " failed", t);
        }
    }

    private Matrix<E> executePow(Matrix<E> a, E exponent) {
        int rows = a.rows();
        int cols = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0;

        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment h_Res = allocateMatrix(rows, cols, arena, tracker, prec, isComplex);
            
            MemorySegment h_ExponentR = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, h_ExponentR, (int) prec);
            tracker.track(h_ExponentR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            MemorySegment h_ExponentI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, h_ExponentI, (int) prec);
                tracker.track(h_ExponentI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            }

            if (isComplex) {
                org.episteme.core.mathematics.numbers.complex.Complex ce = (org.episteme.core.mathematics.numbers.complex.Complex) exponent;
                NativeSafe.invoke(MPFR_SET_STR, h_ExponentR, arena.allocateFrom(ce.getReal().bigDecimalValue().toPlainString()), 10, rnd);
                NativeSafe.invoke(MPFR_SET_STR, h_ExponentI, arena.allocateFrom(ce.getImaginary().bigDecimalValue().toPlainString()), 10, rnd);
            } else {
                NativeSafe.invoke(MPFR_SET_STR, h_ExponentR, arena.allocateFrom(((org.episteme.core.mathematics.numbers.real.Real)exponent).bigDecimalValue().toPlainString()), 10, rnd);
            }

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (isComplex) {
                        MemorySegment resR = getMPFR(h_Res, i, j, cols, 0, true);
                        MemorySegment resI = getMPFR(h_Res, i, j, cols, 1, true);
                        MemorySegment aR = getMPFR(h_A, i, j, cols, 0, true);
                        MemorySegment aI = getMPFR(h_A, i, j, cols, 1, true);
                        
                        MemorySegment logAR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, logAR, (int) prec);
                        MemorySegment logAI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, logAI, (int) prec);
                        try {
                            complexLog(logAR, logAI, aR, aI, (int) prec, arena, null);
                            
                            MemorySegment prodR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, prodR, (int) prec);
                            MemorySegment prodI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, prodI, (int) prec);
                            try {
                                complexMultiply(prodR, prodI, h_ExponentR, h_ExponentI, logAR, logAI, (int) prec, arena, null);
                                complexExp(resR, resI, prodR, prodI, (int) prec, arena, null);
                            } finally {
                                NativeSafe.invoke(MPFR_CLEAR, prodR); NativeSafe.invoke(MPFR_CLEAR, prodI);
                            }
                        } finally {
                            NativeSafe.invoke(MPFR_CLEAR, logAR); NativeSafe.invoke(MPFR_CLEAR, logAI);
                        }
                    } else {
                        NativeSafe.invoke(MPFR_POW, getMPFR(h_Res, i, j, cols, 0, false), getMPFR(h_A, i, j, cols, 0, false), h_ExponentR, rnd);
                    }
                }
            }
            return backToMatrix_internal(h_Res, rows, cols, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            logger.error("MPFR pow failed: {}", t.getMessage());
            throw new RuntimeException("MPFR pow failed", t);
        }
    }

    @Override
    public QRResult<E> qr(Matrix<E> a) {
        return GenericQR.decompose(a, (Field<E>) a.getScalarRing(), this);
    }

    @Override
    public SVDResult<E> svd(Matrix<E> a) {
        return GenericSVD.decompose(a, (Field<E>) a.getScalarRing(), this);
    }

    @Override
    public Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        return GenericLU.solve(lu, b, (Field<E>) b.getScalarRing(), this);
    }

    @Override
    public Vector<E> solve(QRResult<E> qr, Vector<E> b) {
        return GenericQR.solve(qr, b, (Field<E>) b.getScalarRing(), this);
    }

    @Override
    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        return GenericCholesky.solve(cholesky, b, (Field<E>) b.getScalarRing(), this);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (a.rows() != a.cols()) {
            Matrix<E> at = transpose(a);
            if (a.rows() > a.cols()) {
                // Overdetermined: Normal Equations A^T A x = A^T b
                return solve(multiply(at, a), multiply(at, b));
            } else {
                // Underdetermined: Minimum Norm x = A^T (A A^T)^{-1} b
                return multiply(at, solve(multiply(a, at), b));
            }
        }
        return solveSquare(a, b);
    }

    private Vector<E> solveSquare(Matrix<E> a, Vector<E> b) {
        if (a.rows() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        int n = a.rows();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0; // MPFR_RNDN

        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment h_B = initVector(b, arena, tracker, prec, isComplex);
            
            MemorySegment tempFactorR = MemorySegment.NULL, tempFactorI = MemorySegment.NULL;
            MemorySegment tempFactor = MemorySegment.NULL, sumR = MemorySegment.NULL, sumI = MemorySegment.NULL;
            MemorySegment sum = MemorySegment.NULL, term = MemorySegment.NULL;
            
            if (isComplex) {
                tempFactorR = arena.allocate(MPFR_LAYOUT); tempFactorI = arena.allocate(MPFR_LAYOUT);
                sumR = arena.allocate(MPFR_LAYOUT); sumI = arena.allocate(MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, tempFactorR, (int) prec); tracker.track(tempFactorR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_INIT2, tempFactorI, (int) prec); tracker.track(tempFactorI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_INIT2, sumR, (int) prec); tracker.track(sumR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_INIT2, sumI, (int) prec); tracker.track(sumI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            } else {
                tempFactor = arena.allocate(MPFR_LAYOUT); sum = arena.allocate(MPFR_LAYOUT); term = arena.allocate(MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, tempFactor, (int) prec); tracker.track(tempFactor, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_INIT2, sum, (int) prec); tracker.track(sum, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_INIT2, term, (int) prec); tracker.track(term, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            }
            
            // Partial Pivoting GEPP
            for (int k = 0; k < n; k++) {
                int pivot = k;
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        if (compareComplexMagnitude(h_A, i, pivot, k, n, arena, tracker, (int) prec)) pivot = i;
                    } else {
                        if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, pivot, k, n, 0, false)) > 0) pivot = i;
                    }
                }
                
                if (pivot != k) {
                    swapRows(h_A, k, pivot, n, isComplex, arena, tracker, (int) prec);
                    swapRowsVector(h_B, k, pivot, isComplex, arena, tracker, (int) prec);
                }
                
                // Gaussian Elimination with stability check
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        MemorySegment absK = arena.allocate(MPFR_LAYOUT); tracker.track(absK, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        NativeSafe.invoke(MPFR_INIT2, absK, (int) prec);
                        complexMagnitude(absK, getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true), (int) prec, arena, tracker);
                        double dVal = (double) NativeSafe.invoke(MPFR_GET_D, absK, 0);
                        if (dVal <= 1e-60) throw new ArithmeticException("Singular matrix during elimination (pivot below epsilon)");

                        complexDivide(tempFactorR, tempFactorI, 
                            getMPFR(h_A, i, k, n, 0, true), getMPFR(h_A, i, k, n, 1, true),
                            getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true),
                            (int) prec, arena, tracker);
                        
                        for (int j = k; j < n; j++) {
                            complexSubtractMul(h_A, i, j, tempFactorR, tempFactorI, h_A, k, j, n, arena, tracker, (int) prec);
                        }
                        complexSubtractMulVector(h_B, i, tempFactorR, tempFactorI, h_B, k, arena, tracker, (int) prec);
                    } else {
                        MemorySegment absK = arena.allocate(MPFR_LAYOUT); tracker.track(absK, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        NativeSafe.invoke(MPFR_INIT2, absK, (int) prec);
                        NativeSafe.invoke(MPFR_ABS, absK, getMPFR(h_A, k, k, n, 0, false), 0);
                        double dVal = (double) NativeSafe.invoke(MPFR_GET_D, absK, 0);
                        if (dVal <= 1e-60) throw new ArithmeticException("Singular matrix during elimination (pivot below epsilon)");

                        NativeSafe.invoke(MPFR_DIV, tempFactor, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, k, k, n, 0, false), rnd);
                        for (int j = k; j < n; j++) {
                            subtractMulReal(h_A, i, j, tempFactor, h_A, k, j, n, arena, tracker, (int) prec);
                        }
                        subtractMulVectorReal(h_B, i, tempFactor, h_B, k, arena, tracker, (int) prec);
                    }
                }
            }
            
            // Back substitution
            MemorySegment h_X = allocateVector(n, arena, tracker, prec, isComplex);
            for (int i = n - 1; i >= 0; i--) {
                if (isComplex) {
                    NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, sumI, arena.allocateFrom("0"), 10, 0);
                    
                    for (int j = i + 1; j < n; j++) {
                        complexAddMul(sumR, sumI, 
                            getMPFR(h_A, i, j, n, 0, true), getMPFR(h_A, i, j, n, 1, true),
                            getMPFRVector(h_X, j, 0, true), getMPFRVector(h_X, j, 1, true),
                            (int) prec, arena, tracker);
                    }
                    
                    MemorySegment xiR = getMPFRVector(h_X, i, 0, true);
                    MemorySegment xiI = getMPFRVector(h_X, i, 1, true);
                    NativeSafe.invoke(MPFR_SUB, xiR, getMPFRVector(h_B, i, 0, true), sumR, 0);
                    NativeSafe.invoke(MPFR_SUB, xiI, getMPFRVector(h_B, i, 1, true), sumI, 0);
                    
                    complexDivide(xiR, xiI, xiR, xiI, 
                        getMPFR(h_A, i, i, n, 0, true), getMPFR(h_A, i, i, n, 1, true),
                        (int) prec, arena, tracker);
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
            
            return backToVector_internal(h_X, n, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            logger.error("MPFR solve failed: {}", t.getMessage(), t);
            throw new RuntimeException("MPFR solve failed", t);
        }
    }


    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (a.rows() != a.cols()) {
            Matrix<E> at = transpose(a);
            if (a.rows() > a.cols()) {
                return multiply(inverse(multiply(at, a)), at);
            } else {
                return multiply(at, inverse(multiply(a, at)));
            }
        }
        int n = a.rows();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0;

        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment h_Inv = allocateMatrix(n, n, arena, tracker, prec, isComplex);
            
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
                        if (compareComplexMagnitude(h_A, i, pivot, k, n, arena, tracker, (int) prec)) pivot = i;
                    }
                } else {
                    for (int i = k + 1; i < n; i++) {
                        if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, pivot, k, n, 0, false)) > 0) pivot = i;
                    }
                }

                if (pivot != k) {
                    swapRows(h_A, k, pivot, n, isComplex, arena, tracker, (int) prec);
                    swapRows(h_Inv, k, pivot, n, isComplex, arena, tracker, (int) prec);
                }
                
                // Check for singular matrix with magnitude-based epsilon
                MemorySegment absK = arena.allocate(MPFR_LAYOUT); tracker.track(absK, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_INIT2, absK, (int) prec);

                if (isComplex) {
                    complexMagnitude(absK, getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true), (int) prec, arena, tracker);
                } else {
                    NativeSafe.invoke(MPFR_ABS, absK, getMPFR(h_A, k, k, n, 0, false), 0);
                }
                double dVal = (double) NativeSafe.invoke(MPFR_GET_D, absK, 0);
                if (dVal <= 1e-60) throw new ArithmeticException("Singular matrix (magnitude below epsilon)");

                // Normalize pivot row
                if (isComplex) {
                    MemorySegment pR = arena.allocate(MPFR_LAYOUT); tracker.track(pR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                    MemorySegment pI = arena.allocate(MPFR_LAYOUT); tracker.track(pI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                    NativeSafe.invoke(MPFR_INIT2, pR, (int) prec);
                    NativeSafe.invoke(MPFR_INIT2, pI, (int) prec);
                    
                    NativeSafe.invoke(MPFR_SET, pR, getMPFR(h_A, k, k, n, 0, true), rnd);
                    NativeSafe.invoke(MPFR_SET, pI, getMPFR(h_A, k, k, n, 1, true), rnd);
                    
                    for (int j = 0; j < n; j++) {
                        complexDivide(getMPFR(h_A, k, j, n, 0, true), getMPFR(h_A, k, j, n, 1, true),
                            getMPFR(h_A, k, j, n, 0, true), getMPFR(h_A, k, j, n, 1, true),
                            pR, pI, (int) prec, arena, tracker);
                        complexDivide(getMPFR(h_Inv, k, j, n, 0, true), getMPFR(h_Inv, k, j, n, 1, true),
                            getMPFR(h_Inv, k, j, n, 0, true), getMPFR(h_Inv, k, j, n, 1, true),
                            pR, pI, (int) prec, arena, tracker);
                    }
                    // Set A[k][k] to 1+0i explicitly for stability
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_A, k, k, n, 0, true), 1L, rnd);
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_A, k, k, n, 1, true), 0L, rnd);
                } else {
                    MemorySegment pivotVal = arena.allocate(MPFR_LAYOUT); tracker.track(pivotVal, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                    NativeSafe.invoke(MPFR_INIT2, pivotVal, (int) prec);
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
                        MemorySegment fR = arena.allocate(MPFR_LAYOUT); tracker.track(fR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        MemorySegment fI = arena.allocate(MPFR_LAYOUT); tracker.track(fI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        NativeSafe.invoke(MPFR_INIT2, fR, (int) prec);
                        NativeSafe.invoke(MPFR_INIT2, fI, (int) prec);
                        NativeSafe.invoke(MPFR_SET, fR, getMPFR(h_A, i, k, n, 0, true), 0);
                        NativeSafe.invoke(MPFR_SET, fI, getMPFR(h_A, i, k, n, 1, true), 0);
                        
                        for (int j = 0; j < n; j++) {
                            complexSubtractMul(h_A, i, j, fR, fI, h_A, k, j, n, arena, tracker, (int) prec);
                            complexSubtractMul(h_Inv, i, j, fR, fI, h_Inv, k, j, n, arena, tracker, (int) prec);
                        }
                    } else {
                        MemorySegment factor = arena.allocate(MPFR_LAYOUT); tracker.track(factor, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        NativeSafe.invoke(MPFR_INIT2, factor, (int) prec);
                        NativeSafe.invoke(MPFR_SET, factor, getMPFR(h_A, i, k, n, 0, false), 0);
                        
                        for (int j = 0; j < n; j++) {
                            subtractMulReal(h_A, i, j, factor, h_A, k, j, n, arena, tracker, (int) prec);
                            subtractMulReal(h_Inv, i, j, factor, h_Inv, k, j, n, arena, tracker, (int) prec);
                        }
                    }
                }
            }
            return backToMatrix_internal(h_Inv, n, n, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            logger.error("MPFR inverse failed: {}", t.getMessage());
            throw new RuntimeException("MPFR inverse failed: " + t.toString(), t);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E determinant(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        int n = a.rows();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision(); // MPFR_RNDN

        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment detR = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, detR, (int) prec); tracker.track(detR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            NativeSafe.invoke(MPFR_SET_UI, detR, 1L, 0); // Initialize det to 1
            MemorySegment detI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, detI, (int) prec); tracker.track(detI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_SET_UI, detI, 0L, 0); // Initialize imag part to 0
            }

            for (int k = 0; k < n; k++) {
                int pivot = k;
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        if (compareComplexMagnitude(h_A, i, pivot, k, n, arena, tracker, (int) prec)) pivot = i;
                    } else {
                        if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, pivot, k, n, 0, false)) > 0) pivot = i;
                    }
                }
                
                if (pivot != k) {
                    swapRows(h_A, k, pivot, n, isComplex, arena, tracker, (int) prec);
                    NativeSafe.invoke(MPFR_NEG, detR, detR, 0); // Flip sign of determinant
                    if (isComplex) NativeSafe.invoke(MPFR_NEG, detI, detI, 0);
                }
                
                // Multiply determinant by A[k][k]
                if (isComplex) {
                    complexMultiply(detR, detI, detR, detI, 
                        getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true), (int) prec, arena, tracker);
                } else {
                    NativeSafe.invoke(MPFR_MUL, detR, detR, getMPFR(h_A, k, k, n, 0, false), 0);
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
                        NativeSafe.invoke(MPFR_INIT2, fR, (int) prec); tracker.track(fR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        NativeSafe.invoke(MPFR_INIT2, fI, (int) prec); tracker.track(fI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        complexDivide(fR, fI, getMPFR(h_A, i, k, n, 0, true), getMPFR(h_A, i, k, n, 1, true),
                            getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true), (int) prec, arena, tracker);
                        for (int j = k; j < n; j++) complexSubtractMul(h_A, i, j, fR, fI, h_A, k, j, n, arena, tracker, (int) prec);
                    } else {
                        MemorySegment factor = arena.allocate(MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, factor, (int) prec); tracker.track(factor, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        NativeSafe.invoke(MPFR_DIV, factor, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, k, k, n, 0, false), 0);
                        for (int j = k; j < n; j++) subtractMulReal(h_A, i, j, factor, h_A, k, j, n, arena, tracker, (int) prec);
                    }
                }
            }
            
            // The determinant is now in detR/detI
            MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG); 
            if (isComplex) {
                return (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(
                    readMPFR(detR, expPtr, arena),
                    readMPFR(detI, expPtr, arena)
                );
            } else {
                return (E) (Object) readMPFR(detR, expPtr, arena);
            }
        } catch (Throwable t) {
            logger.error("MPFR determinant failed: {}", t.getMessage());
            throw new RuntimeException("MPFR determinant failed", t);
        }
    }

    @Override
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        int n = a.rows();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();

        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment h_L = allocateMatrix(n, n, arena, tracker, prec, isComplex);
            
            // Temporary variables for summation
            MemorySegment sumR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sumR, (int) prec);
            tracker.track(sumR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            MemorySegment sumI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, sumI, (int) prec);
                tracker.track(sumI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            }

            MemorySegment termR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, termR, (int) prec);
            tracker.track(termR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            MemorySegment termI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, termI, (int) prec);
                tracker.track(termI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            }

            for (int j = 0; j < n; j++) {
                // 1. Diagonal element: L[j][j] = sqrt(A[j][j] - sum(L[j][k] * conj(L[j][k])))
                NativeSafe.invoke(MPFR_SET_UI, sumR, 0L, 0);
                if (isComplex) NativeSafe.invoke(MPFR_SET_UI, sumI, 0L, 0);

                for (int k = 0; k < j; k++) {
                    if (isComplex) {
                        MemorySegment ljkR = getMPFR(h_L, j, k, n, 0, true);
                        MemorySegment ljkI = getMPFR(h_L, j, k, n, 1, true);
                        // L[j][k] * conj(L[j][k]) = r^2 + i^2 (purely real)
                        NativeSafe.invoke(MPFR_MUL, termR, ljkR, ljkR, 0);
                        NativeSafe.invoke(MPFR_MUL, termI, ljkI, ljkI, 0);
                        NativeSafe.invoke(MPFR_ADD, termR, termR, termI, 0);
                        NativeSafe.invoke(MPFR_ADD, sumR, sumR, termR, 0);
                    } else {
                        MemorySegment ljk = getMPFR(h_L, j, k, n, 0, false);
                        NativeSafe.invoke(MPFR_MUL, termR, ljk, ljk, 0);
                        NativeSafe.invoke(MPFR_ADD, sumR, sumR, termR, 0);
                    }
                }

                MemorySegment ljjR = getMPFR(h_L, j, j, n, 0, isComplex);
                NativeSafe.invoke(MPFR_SUB, ljjR, getMPFR(h_A, j, j, n, 0, isComplex), sumR, 0);
                
                // check for positive definite
                if ((int) NativeSafe.invoke(MPFR_CMP_SI, ljjR, 0L) <= 0) {
                    throw new ArithmeticException("Matrix is not positive definite at index " + j);
                }
                NativeSafe.invoke(MPFR_SQRT, ljjR, ljjR, 0);
                if (isComplex) NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_L, j, j, n, 1, true), 0L, 0);

                // 2. Off-diagonal elements: L[i][j] = (A[i][j] - sum(L[i][k] * conj(L[j][k]))) / L[j][j]
                for (int i = j + 1; i < n; i++) {
                    NativeSafe.invoke(MPFR_SET_UI, sumR, 0L, 0);
                    if (isComplex) NativeSafe.invoke(MPFR_SET_UI, sumI, 0L, 0);

                    for (int k = 0; k < j; k++) {
                        if (isComplex) {
                            // sum += L[i][k] * conj(L[j][k])
                            MemorySegment likR = getMPFR(h_L, i, k, n, 0, true);
                            MemorySegment likI = getMPFR(h_L, i, k, n, 1, true);
                            MemorySegment ljkR = getMPFR(h_L, j, k, n, 0, true);
                            MemorySegment ljkI = getMPFR(h_L, j, k, n, 1, true);
                            
                            // (a+bi)(c-di) = (ac+bd) + i(bc-ad)
                            NativeSafe.invoke(MPFR_MUL, termR, likR, ljkR, 0); // ac
                            NativeSafe.invoke(MPFR_MUL, termI, likI, ljkI, 0); // bd
                            NativeSafe.invoke(MPFR_ADD, termR, termR, termI, 0); // ac+bd
                            
                            NativeSafe.invoke(MPFR_MUL, termI, likI, ljkR, 0); // bc
                            MemorySegment ad = arena.allocate(MPFR_LAYOUT);
                            NativeSafe.invoke(MPFR_INIT2, ad, (int) prec);
                            tracker.track(ad, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                            NativeSafe.invoke(MPFR_MUL, ad, likR, ljkI, 0); // ad
                            NativeSafe.invoke(MPFR_SUB, termI, termI, ad, 0); // bc-ad
                            
                            NativeSafe.invoke(MPFR_ADD, sumR, sumR, termR, 0);
                            NativeSafe.invoke(MPFR_ADD, sumI, sumI, termI, 0);
                        } else {
                            NativeSafe.invoke(MPFR_MUL, termR, getMPFR(h_L, i, k, n, 0, false), getMPFR(h_L, j, k, n, 0, false), 0);
                            NativeSafe.invoke(MPFR_ADD, sumR, sumR, termR, 0);
                        }
                    }

                    if (isComplex) {
                        MemorySegment lijR = getMPFR(h_L, i, j, n, 0, true);
                        MemorySegment lijI = getMPFR(h_L, i, j, n, 1, true);
                        NativeSafe.invoke(MPFR_SUB, lijR, getMPFR(h_A, i, j, n, 0, true), sumR, 0);
                        NativeSafe.invoke(MPFR_SUB, lijI, getMPFR(h_A, i, j, n, 1, true), sumI, 0);
                        
                        // Divide by L[j][j] (which is real)
                        NativeSafe.invoke(MPFR_DIV, lijR, lijR, ljjR, 0);
                        NativeSafe.invoke(MPFR_DIV, lijI, lijI, ljjR, 0);
                    } else {
                        MemorySegment lij = getMPFR(h_L, i, j, n, 0, false);
                        NativeSafe.invoke(MPFR_SUB, lij, getMPFR(h_A, i, j, n, 0, false), sumR, 0);
                        NativeSafe.invoke(MPFR_DIV, lij, lij, ljjR, 0);
                    }
                }
            }

            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<>(backToMatrix_internal(h_L, n, n, arena, a.getScalarRing(), isComplex));
        } catch (Throwable t) {
            logger.error("MPFR cholesky failed: {}", t.getMessage());
            throw new RuntimeException("MPFR cholesky failed", t);
        }
    }



    private static void subtractMulReal(MemorySegment h_Mat, int r, int c, MemorySegment factor, MemorySegment h_Source, int rs, int cs, int cols, Arena arena, ResourceTracker tracker, int prec) {
        MemorySegment dst = getMPFR(h_Mat, r, c, cols, 0, false);
        MemorySegment src = getMPFR(h_Source, rs, cs, cols, 0, false);
        MemorySegment term = arena.allocate(MPFR_LAYOUT); track(tracker, term);
        NativeSafe.invoke(MPFR_INIT2, term, prec);
        NativeSafe.invoke(MPFR_MUL, term, factor, src, 0);
        NativeSafe.invoke(MPFR_SUB, dst, dst, term, 0);
    }

    private static void subtractMulVectorReal(MemorySegment h_Vec, int r, MemorySegment factor, MemorySegment h_Source, int rs, Arena arena, ResourceTracker tracker, int prec) {
        MemorySegment dst = getMPFRVector(h_Vec, r, 0, false);
        MemorySegment src = getMPFRVector(h_Source, rs, 0, false);
        MemorySegment term = arena.allocate(MPFR_LAYOUT); track(tracker, term);
        NativeSafe.invoke(MPFR_INIT2, term, prec);
        NativeSafe.invoke(MPFR_MUL, term, factor, src, 0);
        NativeSafe.invoke(MPFR_SUB, dst, dst, term, 0);
    }

    private boolean compareComplexMagnitude(MemorySegment h_Mat, int r1, int r2, int col, int n, Arena arena, ResourceTracker tracker, int prec) throws Throwable {
        MemorySegment mag1 = arena.allocate(MPFR_LAYOUT); track(tracker, mag1);
        MemorySegment mag2 = arena.allocate(MPFR_LAYOUT); track(tracker, mag2);
        NativeSafe.invoke(MPFR_INIT2, mag1, prec);
        NativeSafe.invoke(MPFR_INIT2, mag2, prec);
        
        complexMagnitudeSquared(mag1, getMPFR(h_Mat, r1, col, n, 0, true), getMPFR(h_Mat, r1, col, n, 1, true), prec, arena, tracker);
        complexMagnitudeSquared(mag2, getMPFR(h_Mat, r2, col, n, 0, true), getMPFR(h_Mat, r2, col, n, 1, true), prec, arena, tracker);
        
        return (int) NativeSafe.invoke(MPFR_CMP, mag1, mag2) > 0;
    }

    private static void complexMagnitudeSquared(MemorySegment res, MemorySegment r, MemorySegment i, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment t = arena.allocate(MPFR_LAYOUT); track(tracker, t);
        NativeSafe.invoke(MPFR_INIT2, t, prec);
        NativeSafe.invoke(MPFR_MUL, res, r, r, 0);
        NativeSafe.invoke(MPFR_MUL, t, i, i, 0);
        NativeSafe.invoke(MPFR_ADD, res, res, t, 0);
    }

    private static void complexDivide(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment bR, MemorySegment bI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment denom = arena.allocate(MPFR_LAYOUT); track(tracker, denom);
        MemorySegment t1 = arena.allocate(MPFR_LAYOUT); track(tracker, t1);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT); track(tracker, t2);
        MemorySegment outR = arena.allocate(MPFR_LAYOUT); track(tracker, outR);
        MemorySegment outI = arena.allocate(MPFR_LAYOUT); track(tracker, outI);

        NativeSafe.invoke(MPFR_INIT2, denom, prec);
        NativeSafe.invoke(MPFR_INIT2, t1, prec);
        NativeSafe.invoke(MPFR_INIT2, t2, prec);
        NativeSafe.invoke(MPFR_INIT2, outR, prec);
        NativeSafe.invoke(MPFR_INIT2, outI, prec);
        
        NativeSafe.invoke(MPFR_MUL, denom, bR, bR, 0);
        NativeSafe.invoke(MPFR_MUL, t1, bI, bI, 0);
        NativeSafe.invoke(MPFR_ADD, denom, denom, t1, 0);
        
        NativeSafe.invoke(MPFR_MUL, t1, aR, bR, 0);
        NativeSafe.invoke(MPFR_MUL, t2, aI, bI, 0);
        NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
        NativeSafe.invoke(MPFR_DIV, outR, t1, denom, 0);
        
        NativeSafe.invoke(MPFR_MUL, t1, aI, bR, 0);
        NativeSafe.invoke(MPFR_MUL, t2, aR, bI, 0);
        NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
        NativeSafe.invoke(MPFR_DIV, outI, t1, denom, 0);

        NativeSafe.invoke(MPFR_SET, resR, outR, 0);
        NativeSafe.invoke(MPFR_SET, resI, outI, 0);
    }

    private static void complexSubtractMul(MemorySegment h_Mat, int r, int c, MemorySegment fR, MemorySegment fI, MemorySegment h_Src, int rs, int cs, int n, Arena arena, ResourceTracker tracker, int prec) {
        MemorySegment dstR = getMPFR(h_Mat, r, c, n, 0, true);
        MemorySegment dstI = getMPFR(h_Mat, r, c, n, 1, true);
        MemorySegment srcR = getMPFR(h_Src, rs, cs, n, 0, true);
        MemorySegment srcI = getMPFR(h_Src, rs, cs, n, 1, true);
        
        MemorySegment tR = arena.allocate(MPFR_LAYOUT); track(tracker, tR);
        MemorySegment tI = arena.allocate(MPFR_LAYOUT); track(tracker, tI);
        MemorySegment tEmp = arena.allocate(MPFR_LAYOUT); track(tracker, tEmp);

        NativeSafe.invoke(MPFR_INIT2, tR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, tI, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, tEmp, (int) prec);
        
        NativeSafe.invoke(MPFR_MUL, tR, fR, srcR, 0);
        NativeSafe.invoke(MPFR_MUL, tI, fI, srcI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tI, 0);
        
        NativeSafe.invoke(MPFR_MUL, tI, fR, srcI, 0);
        NativeSafe.invoke(MPFR_MUL, tEmp, fI, srcR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0);
        
        NativeSafe.invoke(MPFR_SUB, dstR, dstR, tR, 0);
        NativeSafe.invoke(MPFR_SUB, dstI, dstI, tI, 0);
    }

    private static void complexSubtractMulVector(MemorySegment h_Vec, int r, MemorySegment fR, MemorySegment fI, MemorySegment h_Src, int rs, Arena arena, ResourceTracker tracker, int prec) {
        MemorySegment dstR = getMPFRVector(h_Vec, r, 0, true);
        MemorySegment dstI = getMPFRVector(h_Vec, r, 1, true);
        MemorySegment srcR = getMPFRVector(h_Src, rs, 0, true);
        MemorySegment srcI = getMPFRVector(h_Src, rs, 1, true);
        
        MemorySegment tR = arena.allocate(MPFR_LAYOUT); track(tracker, tR);
        MemorySegment tI = arena.allocate(MPFR_LAYOUT); track(tracker, tI);
        MemorySegment tEmp = arena.allocate(MPFR_LAYOUT); track(tracker, tEmp);

        NativeSafe.invoke(MPFR_INIT2, tR, prec);
        NativeSafe.invoke(MPFR_INIT2, tI, prec);
        NativeSafe.invoke(MPFR_INIT2, tEmp, (int) prec);
        
        NativeSafe.invoke(MPFR_MUL, tR, fR, srcR, 0);
        NativeSafe.invoke(MPFR_MUL, tI, fI, srcI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tI, 0);
        
        NativeSafe.invoke(MPFR_MUL, tI, fR, srcI, 0);
        NativeSafe.invoke(MPFR_MUL, tEmp, fI, srcR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0);
        
        NativeSafe.invoke(MPFR_SUB, dstR, dstR, tR, 0);
        NativeSafe.invoke(MPFR_SUB, dstI, dstI, tI, 0);
    }

    private static void complexMultiply(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment bR, MemorySegment bI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment tR = arena.allocate(MPFR_LAYOUT); track(tracker, tR);
        MemorySegment tI = arena.allocate(MPFR_LAYOUT); track(tracker, tI);
        MemorySegment tEmp = arena.allocate(MPFR_LAYOUT); track(tracker, tEmp);

        NativeSafe.invoke(MPFR_INIT2, tR, prec);
        NativeSafe.invoke(MPFR_INIT2, tI, prec);
        NativeSafe.invoke(MPFR_INIT2, tEmp, prec);
        
        NativeSafe.invoke(MPFR_MUL, tR, aR, bR, 0);
        NativeSafe.invoke(MPFR_MUL, tEmp, aI, bI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tEmp, 0);
        
        NativeSafe.invoke(MPFR_MUL, tI, aR, bI, 0);
        NativeSafe.invoke(MPFR_MUL, tEmp, aI, bR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0);
        
        NativeSafe.invoke(MPFR_SET, resR, tR, 0);
        NativeSafe.invoke(MPFR_SET, resI, tI, 0);
    }

    private static void complexAddMul(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment bR, MemorySegment bI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment tR = arena.allocate(MPFR_LAYOUT); track(tracker, tR);
        MemorySegment tI = arena.allocate(MPFR_LAYOUT); track(tracker, tI);
        MemorySegment tEmp = arena.allocate(MPFR_LAYOUT); track(tracker, tEmp);

        NativeSafe.invoke(MPFR_INIT2, tR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, tI, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, tEmp, (int) prec);
        
        NativeSafe.invoke(MPFR_MUL, tR, aR, bR, 0);
        NativeSafe.invoke(MPFR_MUL, tI, aI, bI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tI, 0);
        
        NativeSafe.invoke(MPFR_MUL, tI, aR, bI, 0);
        NativeSafe.invoke(MPFR_MUL, tEmp, aI, bR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0);
        
        NativeSafe.invoke(MPFR_ADD, resR, resR, tR, 0);
        NativeSafe.invoke(MPFR_ADD, resI, resI, tI, 0);
    }

    private static void swapRows(MemorySegment h_Mat, int r1, int r2, int cols, boolean isComplex, Arena arena, ResourceTracker tracker, int prec) {
        int multiplier = isComplex ? 2 : 1;
        MemorySegment tmp = arena.allocate(MPFR_LAYOUT); track(tracker, tmp);
        NativeSafe.invoke(MPFR_INIT2, tmp, prec);
        for (int j = 0; j < cols * multiplier; j++) {
            MemorySegment m1 = h_Mat.asSlice((long)(r1 * cols * multiplier + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            MemorySegment m2 = h_Mat.asSlice((long)(r2 * cols * multiplier + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_SET, tmp, m1, 0);
            NativeSafe.invoke(MPFR_SET, m1, m2, 0);
            NativeSafe.invoke(MPFR_SET, m2, tmp, 0);
        }
    }

    private static void swapRowsVector(MemorySegment h_Vec, int r1, int r2, boolean isComplex, Arena arena, ResourceTracker tracker, int prec) {
        int multiplier = isComplex ? 2 : 1;
        MemorySegment tmp = arena.allocate(MPFR_LAYOUT); track(tracker, tmp);
        NativeSafe.invoke(MPFR_INIT2, tmp, prec);
        for (int j = 0; j < multiplier; j++) {
            MemorySegment m1 = getMPFRVector(h_Vec, r1, j, isComplex);
            MemorySegment m2 = getMPFRVector(h_Vec, r2, j, isComplex);
            NativeSafe.invoke(MPFR_SET, tmp, m1, 0);
            NativeSafe.invoke(MPFR_SET, m1, m2, 0);
            NativeSafe.invoke(MPFR_SET, m2, tmp, 0);
        }
    }

    // --- Complex Transcendental Arithmetic ---
    
    private static void complexMagnitude(MemorySegment rop, MemorySegment opR, MemorySegment opI, int prec, Arena arena, ResourceTracker tracker) {
        if (MPFR_HYPOT != null) {
            NativeSafe.invoke(MPFR_HYPOT, rop, opR, opI, 0);
        } else {
            MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
            MemorySegment t2 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t1, prec);
            NativeSafe.invoke(MPFR_INIT2, t2, prec);
            try {
                NativeSafe.invoke(MPFR_MUL, t1, opR, opR, 0);
                NativeSafe.invoke(MPFR_MUL, t2, opI, opI, 0);
                NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                NativeSafe.invoke(MPFR_SQRT, rop, t1, 0);
            } finally {
                NativeSafe.invoke(MPFR_CLEAR, t1);
                NativeSafe.invoke(MPFR_CLEAR, t2);
            }
        }
    }

    public static void complexExp(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment expR = arena.allocate(MPFR_LAYOUT);
        MemorySegment cosI = arena.allocate(MPFR_LAYOUT);
        MemorySegment sinI = arena.allocate(MPFR_LAYOUT);

        NativeSafe.invoke(MPFR_INIT2, expR, prec);
        NativeSafe.invoke(MPFR_INIT2, cosI, prec);
        NativeSafe.invoke(MPFR_INIT2, sinI, prec);
        
        try {
            NativeSafe.invoke(MPFR_EXP, expR, aR, 0);
            NativeSafe.invoke(MPFR_COS, cosI, aI, 0);
            NativeSafe.invoke(MPFR_SIN, sinI, aI, 0);
            
            NativeSafe.invoke(MPFR_MUL, resR, expR, cosI, 0);
            NativeSafe.invoke(MPFR_MUL, resI, expR, sinI, 0);
        } finally {
            NativeSafe.invoke(MPFR_CLEAR, expR);
            NativeSafe.invoke(MPFR_CLEAR, cosI);
            NativeSafe.invoke(MPFR_CLEAR, sinI);
        }
    }

    public static void complexLog(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        if (((Number) NativeSafe.invoke(MPFR_NAN_P, aR)).intValue() != 0 || ((Number) NativeSafe.invoke(MPFR_NAN_P, aI)).intValue() != 0) {
            NativeSafe.invoke(MPFR_SET_NAN, resR);
            NativeSafe.invoke(MPFR_SET_NAN, resI);
            return;
        }

        MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT);
        MemorySegment pi = arena.allocate(MPFR_LAYOUT);
        MemorySegment zero = arena.allocate(MPFR_LAYOUT);
        
        NativeSafe.invoke(MPFR_INIT2, t1, prec);
        NativeSafe.invoke(MPFR_INIT2, t2, prec);
        NativeSafe.invoke(MPFR_INIT2, pi, prec);
        NativeSafe.invoke(MPFR_INIT2, zero, prec);
    
        try {
            NativeSafe.invoke(MPFR_MUL, t1, aR, aR, 0);
            NativeSafe.invoke(MPFR_MUL, t2, aI, aI, 0);
            NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);

            if (((Number) NativeSafe.invoke(MPFR_ZERO_P, t1)).intValue() != 0) {
                NativeSafe.invoke(MPFR_SET_INF, resR, -1);
            } else {
                NativeSafe.invoke(MPFR_LOG, t1, t1, 0);
                NativeSafe.invoke(MPFR_SET_D, t2, 0.5, 0);
                NativeSafe.invoke(MPFR_MUL, resR, t1, t2, 0);
            }
            
            if (MPFR_ATAN2 != null) {
                NativeSafe.invoke(MPFR_ATAN2, resI, aI, aR, 0);
            } else {
                NativeSafe.invoke(MPFR_CONST_PI, pi, 0);
                NativeSafe.invoke(MPFR_SET_UI, zero, 0L, 0);
                int cmpX = (int) NativeSafe.invoke(MPFR_CMP, aR, zero);
                int cmpY = (int) NativeSafe.invoke(MPFR_CMP, aI, zero);
                
                if (cmpX > 0) {
                    NativeSafe.invoke(MPFR_DIV, resI, aI, aR, 0);
                    NativeSafe.invoke(MPFR_ATAN, resI, resI, 0);
                } else if (cmpX < 0) {
                    NativeSafe.invoke(MPFR_DIV, resI, aI, aR, 0);
                    NativeSafe.invoke(MPFR_ATAN, resI, resI, 0);
                    if (cmpY >= 0) NativeSafe.invoke(MPFR_ADD, resI, resI, pi, 0);
                    else NativeSafe.invoke(MPFR_SUB, resI, resI, pi, 0);
                } else {
                    if (cmpY > 0) {
                        MemorySegment two = arena.allocate(MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, two, prec);
                        NativeSafe.invoke(MPFR_SET_UI, two, 2L, 0);
                        NativeSafe.invoke(MPFR_DIV, resI, pi, two, 0);
                        NativeSafe.invoke(MPFR_CLEAR, two);
                    } else if (cmpY < 0) {
                        MemorySegment two = arena.allocate(MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, two, prec);
                        NativeSafe.invoke(MPFR_SET_UI, two, 2L, 0);
                        NativeSafe.invoke(MPFR_DIV, resI, pi, two, 0);
                        NativeSafe.invoke(MPFR_NEG, resI, resI, 0);
                        NativeSafe.invoke(MPFR_CLEAR, two);
                    } else {
                        NativeSafe.invoke(MPFR_SET_UI, resI, 0L, 0);
                    }
                }
            }
        } finally {
            NativeSafe.invoke(MPFR_CLEAR, t1);
            NativeSafe.invoke(MPFR_CLEAR, t2);
            NativeSafe.invoke(MPFR_CLEAR, pi);
            NativeSafe.invoke(MPFR_CLEAR, zero);
        }
    }

    public static void complexLog10(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        complexLog(resR, resI, aR, aI, prec, arena, tracker);
        MemorySegment log10 = arena.allocate(MPFR_LAYOUT); track(tracker, log10);
        MemorySegment ten = arena.allocate(MPFR_LAYOUT); track(tracker, ten);
        NativeSafe.invoke(MPFR_INIT2, log10, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, ten, (int) prec);
        NativeSafe.invoke(MPFR_SET_UI, ten, 10L, 0);
        NativeSafe.invoke(MPFR_LOG, log10, ten, 0);
        NativeSafe.invoke(MPFR_DIV, resR, resR, log10, 0);
        NativeSafe.invoke(MPFR_DIV, resI, resI, log10, 0);
    }

    public static void complexSin(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment sR = arena.allocate(MPFR_LAYOUT); track(tracker, sR);
        MemorySegment cR = arena.allocate(MPFR_LAYOUT); track(tracker, cR);
        MemorySegment shI = arena.allocate(MPFR_LAYOUT); track(tracker, shI);
        MemorySegment chI = arena.allocate(MPFR_LAYOUT); track(tracker, chI);
        NativeSafe.invoke(MPFR_INIT2, sR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, cR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, shI, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, chI, (int) prec);
        NativeSafe.invoke(MPFR_SIN, sR, aR, 0);
        NativeSafe.invoke(MPFR_COS, cR, aR, 0);
        NativeSafe.invoke(MPFR_SINH, shI, aI, 0);
        NativeSafe.invoke(MPFR_COSH, chI, aI, 0);
        NativeSafe.invoke(MPFR_MUL, resR, sR, chI, 0);
        NativeSafe.invoke(MPFR_MUL, resI, cR, shI, 0);
    }

    public static void complexCos(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment sR = arena.allocate(MPFR_LAYOUT); track(tracker, sR);
        MemorySegment cR = arena.allocate(MPFR_LAYOUT); track(tracker, cR);
        MemorySegment shI = arena.allocate(MPFR_LAYOUT); track(tracker, shI);
        MemorySegment chI = arena.allocate(MPFR_LAYOUT); track(tracker, chI);
        NativeSafe.invoke(MPFR_INIT2, sR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, cR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, shI, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, chI, (int) prec);
        NativeSafe.invoke(MPFR_SIN, sR, aR, 0);
        NativeSafe.invoke(MPFR_COS, cR, aR, 0);
        NativeSafe.invoke(MPFR_SINH, shI, aI, 0);
        NativeSafe.invoke(MPFR_COSH, chI, aI, 0);
        NativeSafe.invoke(MPFR_MUL, resR, cR, chI, 0);
        NativeSafe.invoke(MPFR_MUL, resI, sR, shI, 0);
        NativeSafe.invoke(MPFR_NEG, resI, resI, 0);
    }

    public static void complexTan(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment sR = arena.allocate(MPFR_LAYOUT); track(tracker, sR);
        MemorySegment sI = arena.allocate(MPFR_LAYOUT); track(tracker, sI);
        MemorySegment cR = arena.allocate(MPFR_LAYOUT); track(tracker, cR);
        MemorySegment cI = arena.allocate(MPFR_LAYOUT); track(tracker, cI);
        NativeSafe.invoke(MPFR_INIT2, sR, prec);
        NativeSafe.invoke(MPFR_INIT2, sI, prec);
        NativeSafe.invoke(MPFR_INIT2, cR, prec);
        NativeSafe.invoke(MPFR_INIT2, cI, prec);
        complexSin(sR, sI, aR, aI, prec, arena, tracker);
        complexCos(cR, cI, aR, aI, prec, arena, tracker);
        complexDivide(resR, resI, sR, sI, cR, cI, prec, arena, tracker);
    }

    public static void complexSinh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment shR = arena.allocate(MPFR_LAYOUT); track(tracker, shR);
        MemorySegment chR = arena.allocate(MPFR_LAYOUT); track(tracker, chR);
        MemorySegment sI = arena.allocate(MPFR_LAYOUT); track(tracker, sI);
        MemorySegment cI = arena.allocate(MPFR_LAYOUT); track(tracker, cI);
        NativeSafe.invoke(MPFR_INIT2, shR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, chR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, sI, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, cI, (int) prec);
        NativeSafe.invoke(MPFR_SINH, shR, aR, 0);
        NativeSafe.invoke(MPFR_COSH, chR, aR, 0);
        NativeSafe.invoke(MPFR_SIN, sI, aI, 0);
        NativeSafe.invoke(MPFR_COS, cI, aI, 0);
        NativeSafe.invoke(MPFR_MUL, resR, shR, cI, 0);
        NativeSafe.invoke(MPFR_MUL, resI, chR, sI, 0);
    }

    public static void complexCosh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment shR = arena.allocate(MPFR_LAYOUT); track(tracker, shR);
        MemorySegment chR = arena.allocate(MPFR_LAYOUT); track(tracker, chR);
        MemorySegment sI = arena.allocate(MPFR_LAYOUT); track(tracker, sI);
        MemorySegment cI = arena.allocate(MPFR_LAYOUT); track(tracker, cI);
        NativeSafe.invoke(MPFR_INIT2, shR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, chR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, sI, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, cI, (int) prec);
        NativeSafe.invoke(MPFR_SINH, shR, aR, 0);
        NativeSafe.invoke(MPFR_COSH, chR, aR, 0);
        NativeSafe.invoke(MPFR_SIN, sI, aI, 0);
        NativeSafe.invoke(MPFR_COS, cI, aI, 0);
        NativeSafe.invoke(MPFR_MUL, resR, chR, cI, 0);
        NativeSafe.invoke(MPFR_MUL, resI, shR, sI, 0);
    }

    public static void complexTanh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment sR = arena.allocate(MPFR_LAYOUT); track(tracker, sR);
        MemorySegment sI = arena.allocate(MPFR_LAYOUT); track(tracker, sI);
        MemorySegment cR = arena.allocate(MPFR_LAYOUT); track(tracker, cR);
        MemorySegment cI = arena.allocate(MPFR_LAYOUT); track(tracker, cI);
        NativeSafe.invoke(MPFR_INIT2, sR, prec);
        NativeSafe.invoke(MPFR_INIT2, sI, prec);
        NativeSafe.invoke(MPFR_INIT2, cR, prec);
        NativeSafe.invoke(MPFR_INIT2, cI, prec);
        complexSinh(sR, sI, aR, aI, prec, arena, tracker);
        complexCosh(cR, cI, aR, aI, prec, arena, tracker);
        complexDivide(resR, resI, sR, sI, cR, cI, prec, arena, tracker);
    }

    public static void complexSqrt(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment norm = arena.allocate(MPFR_LAYOUT); track(tracker, norm);
        MemorySegment t1 = arena.allocate(MPFR_LAYOUT); track(tracker, t1);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT); track(tracker, t2);
        MemorySegment zero = arena.allocate(MPFR_LAYOUT); track(tracker, zero);
        NativeSafe.invoke(MPFR_INIT2, norm, prec);
        NativeSafe.invoke(MPFR_INIT2, t1, prec);
        NativeSafe.invoke(MPFR_INIT2, t2, prec);
        NativeSafe.invoke(MPFR_INIT2, zero, prec);
        NativeSafe.invoke(MPFR_SET_UI, zero, 0L, 0);
        
        if (MPFR_HYPOT != null) {
            NativeSafe.invoke(MPFR_HYPOT, norm, aR, aI, 0);
        } else {
            NativeSafe.invoke(MPFR_MUL, t1, aR, aR, 0);
            NativeSafe.invoke(MPFR_MUL, t2, aI, aI, 0);
            NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
            NativeSafe.invoke(MPFR_SQRT, norm, t1, 0);
        }
        
        NativeSafe.invoke(MPFR_ADD, t1, norm, aR, 0);
        NativeSafe.invoke(MPFR_SET_D, t2, 0.5, 0);
        NativeSafe.invoke(MPFR_MUL, t1, t1, t2, 0);
        if ((int) NativeSafe.invoke(MPFR_CMP, t1, zero) < 0) NativeSafe.invoke(MPFR_SET, t1, zero, 0);
        NativeSafe.invoke(MPFR_SQRT, resR, t1, 0);
        
        NativeSafe.invoke(MPFR_SUB, t1, norm, aR, 0);
        NativeSafe.invoke(MPFR_MUL, t1, t1, t2, 0);
        if ((int) NativeSafe.invoke(MPFR_CMP, t1, zero) < 0) NativeSafe.invoke(MPFR_SET, t1, zero, 0);
        NativeSafe.invoke(MPFR_SQRT, resI, t1, 0);
        if ((int) NativeSafe.invoke(MPFR_CMP, aI, zero) < 0) NativeSafe.invoke(MPFR_NEG, resI, resI, 0);
    }

    public static void complexAsin(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment izR = arena.allocate(MPFR_LAYOUT); track(tracker, izR);
        MemorySegment izI = arena.allocate(MPFR_LAYOUT); track(tracker, izI);
        MemorySegment z2R = arena.allocate(MPFR_LAYOUT); track(tracker, z2R);
        MemorySegment z2I = arena.allocate(MPFR_LAYOUT); track(tracker, z2I);
        MemorySegment oneMinusZ2R = arena.allocate(MPFR_LAYOUT); track(tracker, oneMinusZ2R);
        MemorySegment oneMinusZ2I = arena.allocate(MPFR_LAYOUT); track(tracker, oneMinusZ2I);
        MemorySegment sqrtR = arena.allocate(MPFR_LAYOUT); track(tracker, sqrtR);
        MemorySegment sqrtI = arena.allocate(MPFR_LAYOUT); track(tracker, sqrtI);
        MemorySegment sumR = arena.allocate(MPFR_LAYOUT); track(tracker, sumR);
        MemorySegment sumI = arena.allocate(MPFR_LAYOUT); track(tracker, sumI);
        MemorySegment logR = arena.allocate(MPFR_LAYOUT); track(tracker, logR);
        MemorySegment logI = arena.allocate(MPFR_LAYOUT); track(tracker, logI);
        
        NativeSafe.invoke(MPFR_INIT2, izR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, izI, (int) prec);
        NativeSafe.invoke(MPFR_NEG, izR, aI, 0);
        NativeSafe.invoke(MPFR_SET, izI, aR, 0);
        
        NativeSafe.invoke(MPFR_INIT2, z2R, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, z2I, (int) prec);
        complexMultiply(z2R, z2I, aR, aI, aR, aI, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_INIT2, oneMinusZ2R, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, oneMinusZ2I, (int) prec);
        NativeSafe.invoke(MPFR_SET_UI, oneMinusZ2R, 1L, 0);
        NativeSafe.invoke(MPFR_SET_UI, oneMinusZ2I, 0L, 0);
        NativeSafe.invoke(MPFR_SUB, oneMinusZ2R, oneMinusZ2R, z2R, 0);
        NativeSafe.invoke(MPFR_SUB, oneMinusZ2I, oneMinusZ2I, z2I, 0);
        
        NativeSafe.invoke(MPFR_INIT2, sqrtR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, sqrtI, (int) prec);
        complexSqrt(sqrtR, sqrtI, oneMinusZ2R, oneMinusZ2I, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_INIT2, sumR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, sumI, (int) prec);
        NativeSafe.invoke(MPFR_ADD, sumR, izR, sqrtR, 0);
        NativeSafe.invoke(MPFR_ADD, sumI, izI, sqrtI, 0);
        
        NativeSafe.invoke(MPFR_INIT2, logR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, logI, (int) prec);
        complexLog(logR, logI, sumR, sumI, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_SET, resR, logI, 0);
        NativeSafe.invoke(MPFR_NEG, resI, logR, 0);
    }

    public static void complexAcos(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment asinR = arena.allocate(MPFR_LAYOUT); track(tracker, asinR);
        MemorySegment asinI = arena.allocate(MPFR_LAYOUT); track(tracker, asinI);
        MemorySegment piHalf = arena.allocate(MPFR_LAYOUT); track(tracker, piHalf);
        MemorySegment two = arena.allocate(MPFR_LAYOUT); track(tracker, two);
        
        NativeSafe.invoke(MPFR_INIT2, asinR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, asinI, (int) prec);
        complexAsin(asinR, asinI, aR, aI, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_INIT2, piHalf, (int) prec);
        NativeSafe.invoke(MPFR_CONST_PI, piHalf, 0);
        NativeSafe.invoke(MPFR_INIT2, two, (int) prec);
        NativeSafe.invoke(MPFR_SET_UI, two, 2L, 0);
        NativeSafe.invoke(MPFR_DIV, piHalf, piHalf, two, 0);
        
        NativeSafe.invoke(MPFR_SUB, resR, piHalf, asinR, 0);
        NativeSafe.invoke(MPFR_NEG, resI, asinI, 0);
    }

    public static void complexAtan(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment izR = arena.allocate(MPFR_LAYOUT); track(tracker, izR);
        MemorySegment izI = arena.allocate(MPFR_LAYOUT); track(tracker, izI);
        MemorySegment numR = arena.allocate(MPFR_LAYOUT); track(tracker, numR);
        MemorySegment numI = arena.allocate(MPFR_LAYOUT); track(tracker, numI);
        MemorySegment denR = arena.allocate(MPFR_LAYOUT); track(tracker, denR);
        MemorySegment denI = arena.allocate(MPFR_LAYOUT); track(tracker, denI);
        MemorySegment divR = arena.allocate(MPFR_LAYOUT); track(tracker, divR);
        MemorySegment divI = arena.allocate(MPFR_LAYOUT); track(tracker, divI);
        MemorySegment logR = arena.allocate(MPFR_LAYOUT); track(tracker, logR);
        MemorySegment logI = arena.allocate(MPFR_LAYOUT); track(tracker, logI);
        MemorySegment check = arena.allocate(MPFR_LAYOUT); track(tracker, check);
        
        NativeSafe.invoke(MPFR_INIT2, izR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, izI, (int) prec);
        NativeSafe.invoke(MPFR_NEG, izR, aI, 0);
        NativeSafe.invoke(MPFR_SET, izI, aR, 0);
        
        NativeSafe.invoke(MPFR_INIT2, numR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, numI, (int) prec);
        NativeSafe.invoke(MPFR_SET_UI, numR, 1L, 0);
        NativeSafe.invoke(MPFR_SET_UI, numI, 0L, 0);
        NativeSafe.invoke(MPFR_SUB, numR, numR, izR, 0);
        NativeSafe.invoke(MPFR_SUB, numI, numI, izI, 0);
        
        NativeSafe.invoke(MPFR_INIT2, denR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, denI, (int) prec);
        NativeSafe.invoke(MPFR_SET_UI, denR, 1L, 0);
        NativeSafe.invoke(MPFR_SET_UI, denI, 0L, 0);
        NativeSafe.invoke(MPFR_ADD, denR, denR, izR, 0);
        NativeSafe.invoke(MPFR_ADD, denI, denI, izI, 0);
        
        NativeSafe.invoke(MPFR_INIT2, divR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, divI, (int) prec);
        complexDivide(divR, divI, numR, numI, denR, denI, prec, arena, tracker);
        complexLog(logR, logI, divR, divI, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_SET, resR, logI, 0);
        NativeSafe.invoke(MPFR_NEG, resR, resR, 0);
        NativeSafe.invoke(MPFR_INIT2, check, (int) prec);
        NativeSafe.invoke(MPFR_SET_UI, check, 2L, 0);
        NativeSafe.invoke(MPFR_DIV, resR, resR, check, 0);
        NativeSafe.invoke(MPFR_SET, resI, logR, 0);
        NativeSafe.invoke(MPFR_DIV, resI, resI, check, 0);
    }

    public static void complexAsinh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment z2R = arena.allocate(MPFR_LAYOUT); track(tracker, z2R);
        MemorySegment z2I = arena.allocate(MPFR_LAYOUT); track(tracker, z2I);
        MemorySegment sqrtR = arena.allocate(MPFR_LAYOUT); track(tracker, sqrtR);
        MemorySegment sqrtI = arena.allocate(MPFR_LAYOUT); track(tracker, sqrtI);
        
        NativeSafe.invoke(MPFR_INIT2, z2R, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, z2I, (int) prec);
        complexMultiply(z2R, z2I, aR, aI, aR, aI, prec, arena, tracker);
        
        MemorySegment one = arena.allocate(MPFR_LAYOUT); track(tracker, one);
        NativeSafe.invoke(MPFR_INIT2, one, prec);
        NativeSafe.invoke(MPFR_SET_UI, one, 1L, 0);
        NativeSafe.invoke(MPFR_ADD, z2R, z2R, one, 0);
        
        NativeSafe.invoke(MPFR_INIT2, sqrtR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, sqrtI, (int) prec);
        complexSqrt(sqrtR, sqrtI, z2R, z2I, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_ADD, z2R, aR, sqrtR, 0);
        NativeSafe.invoke(MPFR_ADD, z2I, aI, sqrtI, 0);
        complexLog(resR, resI, z2R, z2I, (int) prec, arena, tracker);
    }

    public static void complexAcosh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment zp1R = arena.allocate(MPFR_LAYOUT); track(tracker, zp1R);
        MemorySegment zm1R = arena.allocate(MPFR_LAYOUT); track(tracker, zm1R);
        MemorySegment s1R = arena.allocate(MPFR_LAYOUT); track(tracker, s1R);
        MemorySegment s1I = arena.allocate(MPFR_LAYOUT); track(tracker, s1I);
        MemorySegment s2R = arena.allocate(MPFR_LAYOUT); track(tracker, s2R);
        MemorySegment s2I = arena.allocate(MPFR_LAYOUT); track(tracker, s2I);
        MemorySegment prodR = arena.allocate(MPFR_LAYOUT); track(tracker, prodR);
        MemorySegment prodI = arena.allocate(MPFR_LAYOUT); track(tracker, prodI);
        
        NativeSafe.invoke(MPFR_INIT2, zp1R, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, zm1R, (int) prec);
        NativeSafe.invoke(MPFR_SET_UI, zp1R, 1L, 0);
        NativeSafe.invoke(MPFR_ADD, zp1R, aR, zp1R, 0);
        NativeSafe.invoke(MPFR_SET_UI, zm1R, 1L, 0);
        NativeSafe.invoke(MPFR_SUB, zm1R, aR, zm1R, 0);
        
        NativeSafe.invoke(MPFR_INIT2, s1R, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, s1I, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, s2R, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, s2I, (int) prec);
        complexSqrt(s1R, s1I, zp1R, aI, prec, arena, tracker);
        complexSqrt(s2R, s2I, zm1R, aI, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_INIT2, prodR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, prodI, (int) prec);
        complexMultiply(prodR, prodI, s1R, s1I, s2R, s2I, (int) prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_ADD, prodR, aR, prodR, 0);
        NativeSafe.invoke(MPFR_ADD, aI, aI, prodI, 0); // Corrected sum: aI + prodI
        complexLog(resR, resI, prodR, prodI, (int) prec, arena, tracker);
    }

    public static void complexAtanh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment p1R = arena.allocate(MPFR_LAYOUT); track(tracker, p1R);
        MemorySegment m1R = arena.allocate(MPFR_LAYOUT); track(tracker, m1R);
        MemorySegment m1I = arena.allocate(MPFR_LAYOUT); track(tracker, m1I);
        MemorySegment divR = arena.allocate(MPFR_LAYOUT); track(tracker, divR);
        MemorySegment divI = arena.allocate(MPFR_LAYOUT); track(tracker, divI);
        MemorySegment half = arena.allocate(MPFR_LAYOUT); track(tracker, half);
        
        NativeSafe.invoke(MPFR_INIT2, p1R, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, m1R, (int) prec);
        NativeSafe.invoke(MPFR_SET_UI, p1R, 1L, 0);
        NativeSafe.invoke(MPFR_ADD, p1R, p1R, aR, 0);
        NativeSafe.invoke(MPFR_SET_UI, m1R, 1L, 0);
        NativeSafe.invoke(MPFR_SUB, m1R, m1R, aR, 0);
        
        NativeSafe.invoke(MPFR_INIT2, m1I, (int) prec);
        NativeSafe.invoke(MPFR_NEG, m1I, aI, 0);
        
        NativeSafe.invoke(MPFR_INIT2, divR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, divI, (int) prec);
        complexDivide(divR, divI, p1R, aI, m1R, m1I, (int) prec, arena, tracker);
        complexLog(resR, resI, divR, divI, (int) prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_INIT2, half, (int) prec);
        NativeSafe.invoke(MPFR_SET_D, half, 0.5, 0);
        NativeSafe.invoke(MPFR_MUL, resR, resR, half, 0);
        NativeSafe.invoke(MPFR_MUL, resI, resI, half, 0);
    }

    public static void complexCbrt(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment logR = arena.allocate(MPFR_LAYOUT); track(tracker, logR);
        MemorySegment logI = arena.allocate(MPFR_LAYOUT); track(tracker, logI);
        MemorySegment three = arena.allocate(MPFR_LAYOUT); track(tracker, three);
        NativeSafe.invoke(MPFR_INIT2, logR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, logI, (int) prec);
        complexLog(logR, logI, aR, aI, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_INIT2, three, (int) prec);
        NativeSafe.invoke(MPFR_SET_UI, three, 3L, 0);
        NativeSafe.invoke(MPFR_DIV, logR, logR, three, 0);
        NativeSafe.invoke(MPFR_DIV, logI, logI, three, 0);
        complexExp(resR, resI, logR, logI, prec, arena, tracker);
    }

    public static void complexPow(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment eR, MemorySegment eI, int prec, Arena arena, ResourceTracker tracker) {
        MemorySegment logR = arena.allocate(MPFR_LAYOUT); track(tracker, logR);
        MemorySegment logI = arena.allocate(MPFR_LAYOUT); track(tracker, logI);
        NativeSafe.invoke(MPFR_INIT2, logR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, logI, (int) prec);
        complexLog(logR, logI, aR, aI, prec, arena, tracker);
        
        MemorySegment wLogR = arena.allocate(MPFR_LAYOUT); track(tracker, wLogR);
        MemorySegment wLogI = arena.allocate(MPFR_LAYOUT); track(tracker, wLogI);
        NativeSafe.invoke(MPFR_INIT2, wLogR, (int) prec);
        NativeSafe.invoke(MPFR_INIT2, wLogI, (int) prec);
        complexMultiply(wLogR, wLogI, eR, eI, logR, logI, prec, arena, tracker);
        
        complexExp(resR, resI, wLogR, wLogI, prec, arena, tracker);
    }
    private void checkDimensionsAdd(Matrix<?> a, Matrix<?> b) {
        if (a.rows() != b.rows() || a.cols() != b.cols()) throw new IllegalArgumentException("Matrix dimensions do not match");
    }

    private void checkDimensionsDot(Vector<?> a, Vector<?> b) {
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Vector dimensions do not match");
    }

    public static MethodHandle getHandle(String function) {
        return switch (function.toLowerCase()) {
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
            case "asinh" -> MPFR_ASINH;
            case "acosh" -> MPFR_ACOSH;
            case "atanh" -> MPFR_ATANH;
            case "sqrt" -> MPFR_SQRT;
            case "cbrt" -> MPFR_CBRT;
            case "pow" -> MPFR_POW;
            default -> null;
        };
    }

    public static void invokeComplexOp(String function, MemorySegment rcR, MemorySegment rcI, MemorySegment raR, MemorySegment raI, int prec, Arena arena, ResourceTracker tracker) {
        switch (function.toLowerCase()) {
            case "exp" -> complexExp(rcR, rcI, raR, raI, prec, arena, tracker);
            case "log" -> complexLog(rcR, rcI, raR, raI, prec, arena, tracker);
            case "log10" -> complexLog10(rcR, rcI, raR, raI, prec, arena, tracker);
            case "sin" -> complexSin(rcR, rcI, raR, raI, prec, arena, tracker);
            case "cos" -> complexCos(rcR, rcI, raR, raI, prec, arena, tracker);
            case "tan" -> complexTan(rcR, rcI, raR, raI, prec, arena, tracker);
            case "sinh" -> complexSinh(rcR, rcI, raR, raI, prec, arena, tracker);
            case "cosh" -> complexCosh(rcR, rcI, raR, raI, prec, arena, tracker);
            case "tanh" -> complexTanh(rcR, rcI, raR, raI, prec, arena, tracker);
            case "sqrt" -> complexSqrt(rcR, rcI, raR, raI, prec, arena, tracker);
            case "asin" -> complexAsin(rcR, rcI, raR, raI, prec, arena, tracker);
            case "acos" -> complexAcos(rcR, rcI, raR, raI, prec, arena, tracker);
            case "atan" -> complexAtan(rcR, rcI, raR, raI, prec, arena, tracker);
            case "asinh" -> complexAsinh(rcR, rcI, raR, raI, prec, arena, tracker);
            case "acosh" -> complexAcosh(rcR, rcI, raR, raI, prec, arena, tracker);
            case "atanh" -> complexAtanh(rcR, rcI, raR, raI, prec, arena, tracker);
            case "cbrt" -> complexCbrt(rcR, rcI, raR, raI, prec, arena, tracker);
            default -> throw new UnsupportedOperationException("Native complex op not implemented: " + function);
        }
    }
}
