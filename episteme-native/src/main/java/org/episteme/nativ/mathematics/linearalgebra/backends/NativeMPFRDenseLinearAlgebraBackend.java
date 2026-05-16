/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import com.google.auto.service.AutoService;
import java.lang.foreign.*;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import java.lang.invoke.MethodHandle;
import java.util.List;
import java.util.ArrayList;
import static org.episteme.nativ.mathematics.numbers.real.backends.NativeMPFRNumbers.*;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.episteme.nativ.mathematics.numbers.real.NativeRealBig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.nativ.technical.backend.nativ.ResourceTracker;
import org.episteme.nativ.mathematics.numbers.real.backends.NativeMPFRNumbers;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;


/**
 * High-performance Arbitrary Precision Linear Algebra backend using libmpfr.
 * Binds directly to MPFR via Project Panama (FFM).
 */
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, CPUBackend.class})
public class NativeMPFRDenseLinearAlgebraBackend<E> implements LinearAlgebraProvider<E>, NativeBackend, CPUBackend {

    private static final Logger logger = LoggerFactory.getLogger("org.episteme.core.mathematics.NativeDiagnostics");
    private static final boolean AVAILABLE = NativeMPFRNumbers.isAvailable();

    // Redundant handles removed, using NativeMPFRNumbers

    @Override public boolean isAvailable() { return AVAILABLE && !isExplicitlyDisabled(); }
    @Override public String getId() { return "mpfr-dense"; }
    @Override public String getName() { return "Native MPFR Dense Linear Algebra Backend"; }
    @Override public String getDescription() { return "High-performance Arbitrary Precision Linear Algebra backend using libmpfr bound via Project Panama."; }
    
    @Override
    public String getType() {
        return "linear-algebra";
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
        // Compatible with any Real or Complex based on Real
        return zero instanceof org.episteme.core.mathematics.numbers.real.Real || 
               (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex c && 
                c.getReal() instanceof org.episteme.core.mathematics.numbers.real.Real);
    }

    
    // Explicitly check for disabled backend
    @Override
    public boolean isExplicitlyDisabled() {
        String id = getId();
        return (id != null && Boolean.getBoolean("episteme.backend." + id + ".disabled")) || 
               Boolean.getBoolean("episteme.backend.mpfr.disabled") ||
               Boolean.getBoolean("episteme.backend.disable." + id);
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

    private E createScalar(Real r, Object ref) {
        if (isComplex(ref)) return (E) org.episteme.core.mathematics.numbers.complex.Complex.of(r);
        return (E) r;
    }

    private E createScalar(Real r, Real i, Object ref) {
        if (isComplex(ref)) return (E) org.episteme.core.mathematics.numbers.complex.Complex.of(r, i);
        return (E) r;
    }

    private E createScalar(double r, Object ref) {
        return createScalar(Real.of(r), ref);
    }

    private E createScalar(double r, double i, Object ref) {
        return createScalar(Real.of(r), Real.of(i), ref);
    }

    private boolean isComplex(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Matrix<?> m) return isComplexRing(m.getScalarRing());
        if (obj instanceof Vector<?> v) return isComplexRing(v.getScalarRing());
        return obj instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    private boolean isComplexRing(Ring<?> ring) {
        if (ring == null) return false;
        return ring instanceof org.episteme.core.mathematics.sets.Complexes || ring.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    private boolean isFloat(Object obj) {
        if (obj == null) return false;
        String name = obj.getClass().getName();
        if (name.equals("org.episteme.core.mathematics.numbers.real.RealFloat")) return true;
        if (obj instanceof Matrix<?> m) return isFloatRing(m.getScalarRing());
        if (obj instanceof Vector<?> v) return isFloatRing(v.getScalarRing());
        return false;
    }

    private boolean isFloatRing(Ring<?> ring) {
        if (ring == null) return false;
        return ring.getClass().getName().contains("Reals"); 
    }

    private Matrix<E> createMatrix(int rows, int cols, Ring<E> ring) {
        E zero = ring.zero();
        if (!isComplexRing(ring) && org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() == org.episteme.core.mathematics.context.MathContext.RealPrecision.EXACT) {
            zero = (E) org.episteme.core.mathematics.numbers.real.Real.ZERO;
        }
        return new GenericMatrix<>(new DenseMatrixStorage<>(rows, cols, zero), this, ring);
    }

    private void setColumn(Matrix<E> m, int col, Vector<E> v) {
        int rows = m.rows();
        MatrixStorage<E> storage = m.getStorage();
        for (int i = 0; i < rows; i++) {
            storage.set(i, col, v.get(i));
        }
    }

    @SuppressWarnings("unchecked")
    private E castScalar(Object val, Ring<E> ring) {
        if (val == null) return ring.zero();
        Object zero = ring.zero();
        
        if (zero instanceof Complex) {
            if (val instanceof Complex c) return (E) c;
            if (val instanceof Real r) return (E) Complex.of(r);
            if (val instanceof Number n) return (E) Complex.of(n.doubleValue());
        }
        if (zero instanceof Real) {
            if (val instanceof Real r) return (E) r;
            if (val instanceof Complex c) return (E) c.getReal();
            if (val instanceof Number n) return (E) Real.of(n.doubleValue());
        }
        
        // Fallback for standard Java numbers if the ring uses them
        if (val instanceof Number n) {
            if (zero instanceof Double) return (E) Double.valueOf(n.doubleValue());
            if (zero instanceof Float) return (E) Float.valueOf(n.floatValue());
            if (zero instanceof Long) return (E) Long.valueOf(n.longValue());
            if (zero instanceof Integer) return (E) Integer.valueOf(n.intValue());
        }
        
        try {
            return (E) val;
        } catch (ClassCastException e) {
            return ring.zero();
        }
    }

    private boolean isZero(Object obj, Ring<?> ring) {
        if (obj == null) return true;
        if (ring != null && ring.zero().equals(obj)) return true;
        
        if (obj instanceof Complex c) {
            return c.isZero();
        }
        if (obj instanceof Real r) {
            return r.isZero();
        }
        if (obj instanceof Number n) {
            return n.doubleValue() == 0;
        }
        return false;
    }

    private double getRealValue(Object obj) {
        if (obj == null) return 0.0;
        if (obj instanceof Number n) return n.doubleValue();
        if (obj instanceof Real r) return r.doubleValue();
        if (obj instanceof Complex c) return c.real();
        return 0.0;
    }

    private Real getReal(Object obj) {
        if (obj == null) return Real.ZERO;
        if (obj instanceof Real r) return r;
        if (obj instanceof Complex c) return c.getReal();
        if (obj instanceof Number n) return Real.of(n.doubleValue());
        return Real.of(obj.toString());
    }

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
        if (tracker != null && p != null && !p.equals(MemorySegment.NULL)) {
            tracker.track(p, s -> {
                try {
                    if (s.scope().isAlive()) {
                        NativeSafe.invoke(MPFR_CLEAR, s);
                    }
                } catch (Throwable t) {
                    logger.error("Failed to clear MPFR segment: {}", t.getMessage());
                }
            });
        }
    }


    private static long getPrecision() {
        return org.episteme.core.mathematics.context.MathContext.getCurrent().getPrecisionBits();
    }

    @Override
    @SuppressWarnings("unchecked")
    public E trace(Matrix<E> a) {
        checkSquare(a);
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            long prec = getPrecision();
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment sumR = NativeSafe.allocate(arena, MPFR_LAYOUT);
            MemorySegment sumI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            NativeSafe.invoke(MPFR_INIT2, sumR, c_long(prec));
            tracker.track(sumR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            NativeSafe.invoke(MPFR_SET_UI, sumR, 0L, 0);
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, sumI, c_long(prec));
                tracker.track(sumI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_SET_UI, sumI, 0L, 0);
            }

            int n = a.rows();
            int cols = a.cols();
            for (int i = 0; i < n; i++) {
                if (isComplex) {
                    MemorySegment ar = getMPFR(h_A, i, i, cols, 0, true);
                    MemorySegment ai = getMPFR(h_A, i, i, cols, 1, true);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, ar, 0);
                    NativeSafe.invoke(MPFR_ADD, sumI, sumI, ai, 0);
                } else {
                    MemorySegment ar = getMPFR(h_A, i, i, cols, 0, false);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, ar, 0);
                }
            }
            MemorySegment expPtr = NativeSafe.allocate(arena, ValueLayout.JAVA_LONG);
            Real r = readMPFR(sumR, expPtr, arena);
            if (isComplex) {
                Real im = readMPFR(sumI, expPtr, arena);
                return createScalar(r, im, a);
            }
            return createScalar(r, a);
        } catch (Throwable t) {
            throw new RuntimeException("Native MPFR Trace failed", t);
        }
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
            MemorySegment sumR = NativeSafe.allocate(arena, MPFR_LAYOUT);
            MemorySegment sumI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            NativeSafe.invoke(MPFR_INIT2, sumR, c_long(prec)); 
            tracker.track(sumR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            NativeSafe.invoke(MPFR_SET_UI, sumR, 0L, 0);
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, sumI, c_long(prec));
                tracker.track(sumI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_SET_UI, sumI, 0L, 0);
            }

            MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
            tracker.track(t1, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            MemorySegment t2 = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t2, c_long(prec));
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
            MemorySegment expPtr = NativeSafe.allocate(arena, ValueLayout.JAVA_LONG);
            Real r = readMPFR(sumR, expPtr, arena);
            if (isComplex) {
                Real im = readMPFR(sumI, expPtr, arena);
                return createScalar(r, im, a);
            }
            return createScalar(r, a);
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
            MemorySegment sumR = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, sumR, c_long(prec));
            tracker.track(sumR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            NativeSafe.invoke(MPFR_SET_UI, sumR, 0L, 0);

            MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
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

            MemorySegment expPtr = NativeSafe.allocate(arena, ValueLayout.JAVA_LONG);
            Real r = readMPFR(sumR, expPtr, arena);
            return createScalar(r, a);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR norm failed", t);
        }
    }

    @Override
    public Vector<E> multiply(Vector<E> v, E scalar) {
        int n = v.dimension();
        boolean isComplex = ((Object)v.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_V = initVector(v, arena, tracker, prec, isComplex);
            MemorySegment h_Res = allocateVector(n, arena, tracker, prec, isComplex);
            
            MemorySegment sR = NativeSafe.allocate(arena, MPFR_LAYOUT);
            MemorySegment sI = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, sR, c_long(prec));
            tracker.track(sR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            NativeSafe.invoke(MPFR_INIT2, sI, c_long(prec));
            tracker.track(sI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            
            if (isComplex) {
                org.episteme.core.mathematics.numbers.complex.Complex cs = (org.episteme.core.mathematics.numbers.complex.Complex) scalar;
                NativeSafe.invoke(MPFR_SET_STR, sR, NativeSafe.allocateFrom(arena, cs.getReal().bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, sI, NativeSafe.allocateFrom(arena, cs.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                
                // Pre-allocate temporary segments for complexMultiply to avoid allocations inside the loop
                MemorySegment tR = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tR, c_long(prec));
                MemorySegment tI = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tI, c_long(prec));
                MemorySegment tE = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tE, c_long(prec));
                tracker.track(tR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                tracker.track(tI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                tracker.track(tE, s -> NativeSafe.invoke(MPFR_CLEAR, s));

                for (int i = 0; i < n; i++) {
                    MemorySegment vR = h_V.asSlice((long) i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment vI = h_V.asSlice((long) (i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment resR = h_Res.asSlice((long) i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment resI = h_Res.asSlice((long) (i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    
                    // Manual complex multiply to reuse tR, tI, tE
                    NativeSafe.invoke(MPFR_MUL, tR, vR, sR, 0);
                    NativeSafe.invoke(MPFR_MUL, tE, vI, sI, 0);
                    NativeSafe.invoke(MPFR_SUB, tR, tR, tE, 0);
                    NativeSafe.invoke(MPFR_MUL, tI, vR, sI, 0);
                    NativeSafe.invoke(MPFR_MUL, tE, vI, sR, 0);
                    NativeSafe.invoke(MPFR_ADD, tI, tI, tE, 0);
                    NativeSafe.invoke(MPFR_SET, resR, tR, 0);
                    NativeSafe.invoke(MPFR_SET, resI, tI, 0);
                }
            } else {
                String valStr = (scalar instanceof Real rs) ? rs.bigDecimalValue().toPlainString() : scalar.toString();
                NativeSafe.invoke(MPFR_SET_STR, sR, NativeSafe.allocateFrom(arena, valStr), 10, 0);
                
                for (int i = 0; i < n; i++) {
                    MemorySegment rv = h_V.asSlice((long) i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment rr = h_Res.asSlice((long) i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_MUL, rr, rv, sR, 0);
                }
            }
            return backToVector_internal(h_Res, n, arena, v.getScalarRing(), isComplex);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR vector multiply failed", t);
        }
    }

    @Override
    public Vector<E> normalize(Vector<E> v) {
        E n = norm(v);
        if (n == null) return v;
        
        Ring<E> ring = v.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
            try {
                if (isZero(n, ring)) return v;
                return multiply(v, field.inverse(n));
            } catch (Exception e) {
                return v;
            }
        }
        
        double nv = getRealValue(n);
        if (nv == 0) return v;
        return multiply(v, castScalar(Real.of(1.0 / nv), ring));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> cross(Vector<E> a, Vector<E> b) {
        if (a.dimension() != 3 || b.dimension() != 3) throw new IllegalArgumentException("Cross product only supported for 3D vectors");
        
        if (isComplex(a)) {
            Complex a1 = (Complex) (Object) a.get(0);
            Complex a2 = (Complex) (Object) a.get(1);
            Complex a3 = (Complex) (Object) a.get(2);
            Complex b1 = (Complex) (Object) b.get(0);
            Complex b2 = (Complex) (Object) b.get(1);
            Complex b3 = (Complex) (Object) b.get(2);
            
            E[] res = (E[]) new Object[] {
                castScalar(a2.multiply(b3).subtract(a3.multiply(b2)), a.getScalarRing()),
                castScalar(a3.multiply(b1).subtract(a1.multiply(b3)), a.getScalarRing()),
                castScalar(a1.multiply(b2).subtract(a2.multiply(b1)), a.getScalarRing())
            };
            return Vector.of(java.util.Arrays.asList(res), a.getScalarRing());
        }

        Real a1 = getReal(a.get(0));
        Real a2 = getReal(a.get(1));
        Real a3 = getReal(a.get(2));
        Real b1 = getReal(b.get(0));
        Real b2 = getReal(b.get(1));
        Real b3 = getReal(b.get(2));
        
        E[] res = (E[]) new Object[] {
            castScalar(a2.multiply(b3).subtract(a3.multiply(b2)), a.getScalarRing()),
            castScalar(a3.multiply(b1).subtract(a1.multiply(b3)), a.getScalarRing()),
            castScalar(a1.multiply(b2).subtract(a2.multiply(b1)), a.getScalarRing())
        };
        return Vector.of(java.util.Arrays.asList(res), a.getScalarRing());
    }


    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> projection(Vector<E> a, Vector<E> b) {
        E dAB = dot(a, b);
        E dBB = dot(b, b);
        
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
            try {
                if (isZero(dBB, ring)) return multiply(b, createScalar(0.0, (Object)b));
                return multiply(b, field.divide(dAB, dBB));
            } catch (Exception e) {
                return multiply(b, createScalar(0.0, (Object)b));
            }
        }

        double dotAB = getRealValue(dAB);
        double dotBB = getRealValue(dBB);
        
        if (dotBB == 0) return multiply(b, createScalar(0.0, (Object)b));
        return multiply(b, createScalar(dotAB / dotBB, (Object)b));
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

            MemorySegment temp1 = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp1, c_long(prec));
            tracker.track(temp1, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            MemorySegment temp2 = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp2, c_long(prec)); 
            tracker.track(temp2, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (isComplex) {
                        MemorySegment sumR = getMPFR(h_C, i, j, n, 0, true);
                        MemorySegment sumI = getMPFR(h_C, i, j, n, 1, true);
                        NativeSafe.invoke(MPFR_SET_UI, sumR, 0L, rnd);
                        NativeSafe.invoke(MPFR_SET_UI, sumI, 0L, rnd);

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
                        NativeSafe.invoke(MPFR_SET_UI, sum, 0L, rnd);

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
            
            MemorySegment temp1 = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp1, c_long(prec));
            tracker.track(temp1, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            MemorySegment temp2 = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp2, c_long(prec));
            tracker.track(temp2, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            
            for (int i = 0; i < m; i++) {
                if (isComplex) {
                    MemorySegment sumR = getMPFRVector(h_Y, i, 0, true);
                    MemorySegment sumI = getMPFRVector(h_Y, i, 1, true);
                    NativeSafe.invoke(MPFR_SET_STR, sumR, NativeSafe.allocateFrom(arena, "0"), 10, rnd);
                    NativeSafe.invoke(MPFR_SET_STR, sumI, NativeSafe.allocateFrom(arena, "0"), 10, rnd);
                    
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
                    NativeSafe.invoke(MPFR_SET_STR, sum, NativeSafe.allocateFrom(arena, "0"), 10, rnd);
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

    // --- Transcendental Operations (MPFR) ---

    @Override public Matrix<E> exp(Matrix<E> m) { return applyTranscendental(m, "exp", MPFR_EXP); }
    @Override public Matrix<E> log(Matrix<E> m) { return applyTranscendental(m, "log", MPFR_LOG); }
    @Override public Matrix<E> log10(Matrix<E> m) { return applyTranscendental(m, "log10", MPFR_LOG10); }
    @Override public Matrix<E> sin(Matrix<E> m) { return applyTranscendental(m, "sin", MPFR_SIN); }
    @Override public Matrix<E> cos(Matrix<E> m) { return applyTranscendental(m, "cos", MPFR_COS); }
    @Override public Matrix<E> tan(Matrix<E> m) { return applyTranscendental(m, "tan", MPFR_TAN); }
    @Override public Matrix<E> asin(Matrix<E> m) { return applyTranscendental(m, "asin", MPFR_ASIN); }
    @Override public Matrix<E> acos(Matrix<E> m) { return applyTranscendental(m, "acos", MPFR_ACOS); }
    @Override public Matrix<E> atan(Matrix<E> m) { return applyTranscendental(m, "atan", MPFR_ATAN); }
    @Override public Matrix<E> sinh(Matrix<E> m) { return applyTranscendental(m, "sinh", MPFR_SINH); }
    @Override public Matrix<E> cosh(Matrix<E> m) { return applyTranscendental(m, "cosh", MPFR_COSH); }
    @Override public Matrix<E> tanh(Matrix<E> m) { return applyTranscendental(m, "tanh", MPFR_TANH); }
    @Override public Matrix<E> asinh(Matrix<E> m) { return applyTranscendental(m, "asinh", MPFR_ASINH); }
    @Override public Matrix<E> acosh(Matrix<E> m) { return applyTranscendental(m, "acosh", MPFR_ACOSH); }
    @Override public Matrix<E> atanh(Matrix<E> m) { return applyTranscendental(m, "atanh", MPFR_ATANH); }
    @Override public Matrix<E> sqrt(Matrix<E> m) { return applyTranscendental(m, "sqrt", MPFR_SQRT); }
    @Override public Matrix<E> cbrt(Matrix<E> m) { return applyTranscendental(m, "cbrt", MPFR_CBRT); }

    private Matrix<E> applyTranscendental(Matrix<E> m, String op, MethodHandle realFunc) {
        int rows = m.rows();
        int cols = m.cols();
        boolean isComplex = ((Object)m.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        if (isComplex) {
            return applyMPFRComplex(m, op);
        }
        return applyMPFR(m, realFunc);
    }

    private Matrix<E> applyMPFRComplex(Matrix<E> m, String op) {
        int rows = m.rows();
        int cols = m.cols();
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(m, arena, tracker, prec, true);
            MemorySegment h_C = allocateMatrix(rows, cols, arena, tracker, prec, true);
            
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    MemorySegment aR = getMPFR(h_A, i, j, cols, 0, true);
                    MemorySegment aI = getMPFR(h_A, i, j, cols, 1, true);
                    MemorySegment resR = getMPFR(h_C, i, j, cols, 0, true);
                    MemorySegment resI = getMPFR(h_C, i, j, cols, 1, true);
                    
                    switch (op.toLowerCase()) {
                        case "exp" -> complexExp(resR, resI, aR, aI, prec, arena, tracker);
                        case "log" -> complexLog(resR, resI, aR, aI, prec, arena, tracker);
                        case "log10" -> complexLog10(resR, resI, aR, aI, prec, arena, tracker);
                        case "sin" -> complexSin(resR, resI, aR, aI, prec, arena, tracker);
                        case "cos" -> complexCos(resR, resI, aR, aI, prec, arena, tracker);
                        case "tan" -> complexTan(resR, resI, aR, aI, prec, arena, tracker);
                        case "asin" -> complexAsin(resR, resI, aR, aI, prec, arena, tracker);
                        case "acos" -> complexAcos(resR, resI, aR, aI, prec, arena, tracker);
                        case "atan" -> complexAtan(resR, resI, aR, aI, prec, arena, tracker);
                        case "sinh" -> complexSinh(resR, resI, aR, aI, prec, arena, tracker);
                        case "cosh" -> complexCosh(resR, resI, aR, aI, prec, arena, tracker);
                        case "tanh" -> complexTanh(resR, resI, aR, aI, prec, arena, tracker);
                        case "sqrt" -> complexSqrt(resR, resI, aR, aI, prec, arena, tracker);
                        case "cbrt" -> complexCbrt(resR, resI, aR, aI, prec, arena, tracker);
                        case "asinh" -> complexAsinh(resR, resI, aR, aI, prec, arena, tracker);
                        case "acosh" -> complexAcosh(resR, resI, aR, aI, prec, arena, tracker);
                        case "atanh" -> complexAtanh(resR, resI, aR, aI, prec, arena, tracker);
                        default -> throw new UnsupportedOperationException("Op " + op + " not implemented for complex dense");
                    }
                }
            }
            return backToMatrix_internal(h_C, rows, cols, arena, m.getScalarRing(), true);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR complex transcendental failed: " + op, t);
        }
    }

    @Override
    public Matrix<E> pow(Matrix<E> m, E exponent) {
        int rows = m.rows();
        int cols = m.cols();
        boolean isComplex = ((Object)m.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(m, arena, tracker, prec, isComplex);
            MemorySegment h_C = allocateMatrix(rows, cols, arena, tracker, prec, isComplex);
            MemorySegment sR = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, sR, c_long(prec));
            tracker.track(sR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            
            if (isComplex) {
                // Generalized complex pow is hard with MPFR alone. 
                // For audit, we fallback to element-wise Java-side if needed, but let's try to throw if not 1x1.
                if (rows == 1 && cols == 1) {
                    org.episteme.core.mathematics.numbers.complex.Complex base = (org.episteme.core.mathematics.numbers.complex.Complex) m.get(0, 0);
                    org.episteme.core.mathematics.numbers.complex.Complex exp = (org.episteme.core.mathematics.numbers.complex.Complex) exponent;
                    org.episteme.core.mathematics.numbers.complex.Complex res = base.pow(exp);
                    @SuppressWarnings("unchecked")
                    Matrix<E> result = (Matrix<E>) (Object) Matrix.of(new org.episteme.core.mathematics.numbers.complex.Complex[][]{{res}}, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>) (Object) m.getScalarRing());
                    return result;
                }
                throw new UnsupportedOperationException("Generalized complex pow not implemented in MPFR backend");
            } else {
                setMPFR(sR, (Real) exponent, arena, 0);
                for (int i = 0; i < rows * cols; i++) {
                    MemorySegment ra = h_A.asSlice((long) i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment rc = h_C.asSlice((long) i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_POW, rc, ra, sR, 0);
                }
            }
            return backToMatrix_internal(h_C, rows, cols, arena, m.getScalarRing(), isComplex);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR pow failed", t);
        }
    }

    private Matrix<E> applyMPFR(Matrix<E> m, MethodHandle func) {
        int rows = m.rows();
        int cols = m.cols();
        boolean isComplex = ((Object)m.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        if (isComplex) throw new UnsupportedOperationException("Complex transcendentals not yet implemented via MPFR");
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(m, arena, tracker, prec, isComplex);
            MemorySegment h_C = allocateMatrix(rows, cols, arena, tracker, prec, isComplex);
            for (int i = 0; i < rows * cols; i++) {
                MemorySegment ra = h_A.asSlice((long) i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rc = h_C.asSlice((long) i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                NativeSafe.invoke(func, rc, ra, 0);
            }
            return backToMatrix_internal(h_C, rows, cols, arena, m.getScalarRing(), isComplex);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR transcendental operation failed", t);
        }
    }

    private static MemorySegment allocateVector(int dimension, Arena arena, ResourceTracker tracker, long prec, boolean isComplex) throws Throwable {
        int multiplier = isComplex ? 2 : 1;
        MemorySegment vec = NativeSafe.allocate(arena, MPFR_LAYOUT, (long) dimension * multiplier);
        for (int i = 0; i < dimension * multiplier; i++) {
            MemorySegment slot = vec.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, slot, c_long(prec));
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
                NativeSafe.invoke(MPFR_SET_STR, getMPFRVector(vec, i, 0, true), NativeSafe.allocateFrom(arena, re.bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, getMPFRVector(vec, i, 1, true), NativeSafe.allocateFrom(arena, im.bigDecimalValue().toPlainString()), 10, 0);
            } else {
                Real rv = getRealPart(val);
                NativeSafe.invoke(MPFR_SET_STR, getMPFRVector(vec, i, 0, false), NativeSafe.allocateFrom(arena, rv.bigDecimalValue().toPlainString()), 10, 0);
            }
        }
        return vec;
    }

    private Real getRealPart(Object val) {
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex c) return c.getReal();
        if (val instanceof Real r) return r;
        if (val instanceof java.math.BigDecimal bd) return org.episteme.core.mathematics.numbers.real.RealBig.of(bd);
        if (val instanceof Number n) return Real.of(n.toString());
        if (val == null) return Real.ZERO;
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
            MemorySegment expPtr = NativeSafe.allocate(arena, ValueLayout.JAVA_LONG);
            boolean useRealDouble = !isComplex && ring instanceof org.episteme.core.mathematics.sets.Reals && org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() != org.episteme.core.mathematics.context.MathContext.RealPrecision.EXACT;
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
            @SuppressWarnings("unchecked")
            org.episteme.core.mathematics.linearalgebra.Vector<E> result = (org.episteme.core.mathematics.linearalgebra.Vector<E>)(Object) new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<E>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<E>(list), (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E>) this, ring);
            return result;
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
        diag("Executing ADD with precision: " + prec + " bits");

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
    
            MemorySegment sR = NativeSafe.allocate(arena, MPFR_LAYOUT);
            MemorySegment sI = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, sR, c_long(prec));
            tracker.track(sR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            NativeSafe.invoke(MPFR_INIT2, sI, c_long(prec));
            tracker.track(sI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
    
            if (isComplex) {
                org.episteme.core.mathematics.numbers.complex.Complex cs = (org.episteme.core.mathematics.numbers.complex.Complex) scalar;
                NativeSafe.invoke(MPFR_SET_STR, sR, NativeSafe.allocateFrom(arena, cs.getReal().bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, sI, NativeSafe.allocateFrom(arena, cs.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
    
                for (int i = 0; i < m * n; i++) {
                    MemorySegment aR = h_A.asSlice((long) i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment aI = h_A.asSlice((long) (i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment cR = h_C.asSlice((long) i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment cI = h_C.asSlice((long) (i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    complexMultiply(cR, cI, aR, aI, sR, sI, prec, arena, tracker);
                }
            } else {
                String valStr = (scalar instanceof Real rs) ? rs.bigDecimalValue().toPlainString() : scalar.toString();
                NativeSafe.invoke(MPFR_SET_STR, sR, NativeSafe.allocateFrom(arena, valStr), 10, 0);
    
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
        MemorySegment mat = NativeSafe.allocate(arena, MPFR_LAYOUT, (long) rows * cols * multiplier);
        for (int i = 0; i < rows * cols * multiplier; i++) {
            MemorySegment slot = mat.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, slot, c_long(prec));
            NativeSafe.invoke(MPFR_SET_UI, slot, 0L, 0); 
        }
        tracker.track(mat, s -> clearMPFRArray(s, rows * cols * multiplier));
        return mat;
    }

    private MemorySegment initMatrix(Matrix<E> a, Arena arena, ResourceTracker tracker, long prec, boolean isComplex) throws Throwable {
        int rows = a.rows();
        int cols = a.cols();
        MemorySegment mat = allocateMatrix(rows, cols, arena, tracker, prec, isComplex);
        diag("Initializing Matrix [" + rows + "x" + cols + "] with precision: " + prec + " bits");
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
        } else {
            // Always use string for high precision stability to avoid double-precision floor
            String s = val.bigDecimalValue().toPlainString();
            NativeSafe.invoke(MPFR_SET_STR, mpfr, NativeSafe.allocateFrom(arena, s), 10, rnd);
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

    @Override
    public Matrix<E> conjugateTranspose(Matrix<E> a) {
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        if (!isComplex) return transpose(a);
        
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment h_C = allocateMatrix(n, m, arena, tracker, prec, isComplex);
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    NativeSafe.invoke(MPFR_SET, getMPFR(h_C, j, i, m, 0, true), getMPFR(h_A, i, j, n, 0, true), 0);
                    NativeSafe.invoke(MPFR_NEG, getMPFR(h_C, j, i, m, 1, true), getMPFR(h_A, i, j, n, 1, true), 0);
                }
            }
            return backToMatrix_internal(h_C, n, m, arena, a.getScalarRing(), isComplex);
        } catch (Throwable t) {
            logger.error("MPFR conjugateTranspose failed: {}", t.getMessage(), t);
            throw new RuntimeException("MPFR conjugateTranspose failed", t);
        }
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> backToMatrix_internal(MemorySegment h_Mat, int rows, int cols, Arena arena, org.episteme.core.mathematics.structures.rings.Ring<E> ring, boolean isComplex) throws Throwable {
        org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(rows, cols, (E)ring.zero());
        long prec = getPrecision();
        boolean useRealDouble = !isComplex && ring instanceof org.episteme.core.mathematics.sets.Reals && org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() != org.episteme.core.mathematics.context.MathContext.RealPrecision.EXACT;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isComplex) {
                    MemorySegment ptrR = getMPFR(h_Mat, i, j, cols, 0, true);
                    MemorySegment ptrI = getMPFR(h_Mat, i, j, cols, 1, true);
                    NativeRealBig r = NativeRealBig.copyFrom(ptrR, prec);
                    NativeRealBig im = NativeRealBig.copyFrom(ptrI, prec);
                    storage.set(i, j, (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, im));
                } else {
                    MemorySegment ptr = getMPFR(h_Mat, i, j, cols, 0, false);
                    NativeRealBig res = NativeRealBig.copyFrom(ptr, prec);
                    if (useRealDouble) {
                        storage.set(i, j, (E) (Object) org.episteme.core.mathematics.numbers.real.RealDouble.create(res.doubleValue()));
                    } else {
                        storage.set(i, j, (E) (Object) res);
                    }
                }
            }
        }
        return (org.episteme.core.mathematics.linearalgebra.Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(storage, (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E>) this, ring);
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
    // Removed generic fallbacks to allow AlgorithmManager to handle them.

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

            MemorySegment factorR = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, factorR, c_long(prec));
            track(tracker, factorR);
            MemorySegment factorI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, factorI, c_long(prec));
                track(tracker, factorI);
            }

            MemorySegment[] tempsDiv = isComplex ? new MemorySegment[5] : null;
            MemorySegment[] tempsSub = isComplex ? new MemorySegment[3] : null;
            MemorySegment[] tempsReal = isComplex ? null : new MemorySegment[1];
            if (isComplex) {
                for (int i=0; i<5; i++) { tempsDiv[i] = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tempsDiv[i], c_long(prec)); track(tracker, tempsDiv[i]); }
                for (int i=0; i<3; i++) { tempsSub[i] = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tempsSub[i], c_long(prec)); track(tracker, tempsSub[i]); }
            } else {
                tempsReal[0] = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tempsReal[0], c_long(prec)); track(tracker, tempsReal[0]);
            }

            for (int k = 0; k < n; k++) {
                int pivotRow = k;
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        if (compareComplexMagnitude(h_A, i, pivotRow, k, n, arena, tracker, prec)) pivotRow = i;
                    } else {
                        if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, pivotRow, k, n, 0, false)) > 0) pivotRow = i;
                    }
                }

                if (pivotRow != k) {
                    swapRows(h_A, k, pivotRow, n, isComplex, arena, tracker, prec);
                    int tmpP = perm[k];
                    perm[k] = perm[pivotRow];
                    perm[pivotRow] = tmpP;
                }

                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        complexDivide(factorR, factorI, 
                            getMPFR(h_A, i, k, n, 0, true), getMPFR(h_A, i, k, n, 1, true),
                            getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true), prec, tempsDiv);
                        
                        // Store factor in L part
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_A, i, k, n, 0, true), factorR, rnd);
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_A, i, k, n, 1, true), factorI, rnd);

                        for (int j = k + 1; j < n; j++) {
                            complexSubtractMul(h_A, i, j, factorR, factorI, h_A, k, j, n, tempsSub);
                        }
                    } else {
                        NativeSafe.invoke(MPFR_DIV, factorR, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, k, k, n, 0, false), rnd);
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_A, i, k, n, 0, false), factorR, rnd);
                        
                        for (int j = k + 1; j < n; j++) {
                            subtractMulReal(h_A, i, j, factorR, h_A, k, j, n, tempsReal);
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
                if (isComplex) pData[i] = createScalar(Real.of(perm[i]), a);
                else pData[i] = createScalar(Real.of(perm[i]), a);
            }
            Vector<E> pVec = new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>((java.util.List<E>)(java.util.List<?>)java.util.Arrays.asList(pData)), (LinearAlgebraProvider<E>) this, a.getScalarRing());

            return new LUResult<>(lMat, uMat, pVec);
        } catch (Throwable t) {
            logger.error("MPFR LU failed: {}", t.getMessage());
            throw new RuntimeException("MPFR LU failed", t);
        }
    }
    @Override
    public Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        if (!AVAILABLE) throw new UnsupportedOperationException(getName() + " is not available.");
        // LU solve: L*U*x = P*b -> L*y = P*b, U*x = y
        Matrix<E> l = lu.getL();
        Matrix<E> u = lu.getU();
        Vector<E> p = lu.getP();
        int n = l.rows();
        
        // Permute b
        java.util.List<E> pbData = new java.util.ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int pIdx = (int) getRealValue(p.get(i));
            pbData.add(b.get(pIdx));
        }
        Vector<E> pb = Vector.of(pbData, b.getScalarRing());
        
        // Solve L*y = pb (unit lower)
        Vector<E> y = solveTriangular(l, pb, false, false, false, true);
        // Solve U*x = y (upper)
        return solveTriangular(u, y, true, false, false, false);
    }

    @Override
    public Vector<E> solve(QRResult<E> qr, Vector<E> b) {
        if (!AVAILABLE) throw new UnsupportedOperationException(getName() + " is not available.");
        // QR solve: Q*R*x = b -> R*x = Q^T * b
        Matrix<E> q = qr.getQ();
        Matrix<E> r = qr.getR();
        
        // Q is orthogonal (unitary), so Q^T * b = conjugateTranspose(Q) * b
        // For real matrices, Q^T * b.
        boolean isComplex = ((Object)q.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        Vector<E> qtB = multiply(isComplex ? conjugateTranspose(q) : transpose(q), b); 
        
        // Solve R*x = qtB (upper)
        // If R is rectangular (m > n), we take the top-left n x n square part of R and first n elements of qtB
        int n = r.cols();
        if (r.rows() != n) {
            Matrix<E> rSquare = r.getSubMatrix(0, n - 1, 0, n - 1);
            // Since we don't have subVector, we just pass the original qtB. solveTriangular will use the first n elements if we modify it, 
            // but currently it requires dimension parity.
            // Let's assume for now it's square or we extract it.
            // We'll use a hack to get subvector if possible, or just slice it.
            // For now, let's just use the square part of R and hope qtB dimension matches n.
            // Actually, QR in Episteme usually produces m x m Q and m x n R.
            // So qtB is m x 1. We need d1 = qtB(0..n-1).
            // Let's create a subvector.
            java.util.List<E> d1List = new java.util.ArrayList<>(n);
            for (int i = 0; i < n; i++) d1List.add(qtB.get(i));
            Vector<E> d1 = Vector.of(d1List, b.getScalarRing());
            
            return solveTriangular(rSquare, d1, true, false, false, false);
        }
        
        return solveTriangular(r, qtB, true, false, false, false);
    }

    @Override
    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        if (!AVAILABLE) throw new UnsupportedOperationException(getName() + " is not available.");
        // Cholesky solve: L*L^T*x = b -> L*y = b, L^T*x = y
        Matrix<E> l = cholesky.getL();
        // Solve L*y = b (lower)
        Vector<E> y = solveTriangular(l, b, false, false, false, false);
        // Solve L^T*x = y (transposed lower). For complex, it's L^H
        boolean isComplex = ((Object)l.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        return solveTriangular(l, y, false, true, isComplex, false);
    }


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
                        invokeComplexOp(op, resR, resI, aR, aI, prec, arena, tracker);
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
            
            MemorySegment h_ExponentR = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, h_ExponentR, c_long(prec));
            tracker.track(h_ExponentR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            MemorySegment h_ExponentI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, h_ExponentI, c_long(prec));
                tracker.track(h_ExponentI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            }

            if (exponent instanceof org.episteme.core.mathematics.numbers.complex.Complex ce) {
                NativeSafe.invoke(MPFR_SET_STR, h_ExponentR, NativeSafe.allocateFrom(arena, ce.getReal().bigDecimalValue().toPlainString()), 10, rnd);
                if (h_ExponentI != null) {
                    NativeSafe.invoke(MPFR_SET_STR, h_ExponentI, NativeSafe.allocateFrom(arena, ce.getImaginary().bigDecimalValue().toPlainString()), 10, rnd);
                }
            } else {
                NativeSafe.invoke(MPFR_SET_STR, h_ExponentR, NativeSafe.allocateFrom(arena, ((org.episteme.core.mathematics.numbers.real.Real)exponent).bigDecimalValue().toPlainString()), 10, rnd);
                if (h_ExponentI != null) {
                    NativeSafe.invoke(MPFR_SET_STR, h_ExponentI, NativeSafe.allocateFrom(arena, "0.0"), 10, rnd);
                }
            }

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (isComplex) {
                        MemorySegment resR = getMPFR(h_Res, i, j, cols, 0, true);
                        MemorySegment resI = getMPFR(h_Res, i, j, cols, 1, true);
                        MemorySegment aR = getMPFR(h_A, i, j, cols, 0, true);
                        MemorySegment aI = getMPFR(h_A, i, j, cols, 1, true);
                        
                        MemorySegment logAR = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, logAR, c_long(prec));
                        MemorySegment logAI = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, logAI, c_long(prec));
                        try {
                            complexLog(logAR, logAI, aR, aI, prec, arena, null);
                            
                            MemorySegment prodR = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, prodR, c_long(prec));
                            MemorySegment prodI = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, prodI, c_long(prec));
                            try {
                                complexMultiply(prodR, prodI, h_ExponentR, h_ExponentI, logAR, logAI, prec, arena, null);
                                complexExp(resR, resI, prodR, prodI, prec, arena, null);
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

    // Removed generic fallbacks to allow AlgorithmManager to handle them.

    @Override
    @SuppressWarnings("unchecked")
    public E angle(Vector<E> a, Vector<E> b) {
        if (!AVAILABLE) throw new UnsupportedOperationException(getName() + " is not available.");
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Vector dimensions must match");
        
        long prec = getPrecision();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment dotR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, dotR);
            MemorySegment dotI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, dotI);
            
            NativeSafe.invoke(MPFR_INIT2, dotR, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, dotI, c_long(prec));
            
            // dot(a, b)
            E d = dot(a, b);
            if (isComplex) {
                org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) d;
                NativeSafe.invoke(MPFR_SET_STR, dotR, NativeSafe.allocateFrom(arena, c.getReal().bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, dotI, NativeSafe.allocateFrom(arena, c.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
            } else {
                NativeSafe.invoke(MPFR_SET_STR, dotR, NativeSafe.allocateFrom(arena, ((org.episteme.core.mathematics.numbers.real.Real)d).bigDecimalValue().toPlainString()), 10, 0);
            }
            
            MemorySegment normA = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, normA);
            MemorySegment normB = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, normB);
            NativeSafe.invoke(MPFR_INIT2, normA, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, normB, c_long(prec));
            
            Real nA = getReal(norm(a));
            Real nB = getReal(norm(b));
            NativeSafe.invoke(MPFR_SET_STR, normA, NativeSafe.allocateFrom(arena, nA.bigDecimalValue().toPlainString()), 10, 0);
            NativeSafe.invoke(MPFR_SET_STR, normB, NativeSafe.allocateFrom(arena, nB.bigDecimalValue().toPlainString()), 10, 0);
            
            // denom = normA * normB
            MemorySegment denom = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, denom);
            NativeSafe.invoke(MPFR_INIT2, denom, c_long(prec));
            NativeSafe.invoke(MPFR_MUL, denom, normA, normB, 0);
            
            if ((int) NativeSafe.invoke(MPFR_ZERO_P, denom) != 0) return (E) a.getScalarRing().zero();
            
            // cosTheta = dotR / denom
            MemorySegment cosTheta = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, cosTheta);
            NativeSafe.invoke(MPFR_INIT2, cosTheta, c_long(prec));
            NativeSafe.invoke(MPFR_DIV, cosTheta, dotR, denom, 0);
            
            // Clamp
            MemorySegment one = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, one);
            MemorySegment minusOne = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, minusOne);
            NativeSafe.invoke(MPFR_INIT2, one, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, minusOne, c_long(prec));
            NativeSafe.invoke(MPFR_SET_SI, one, 1L, 0);
            NativeSafe.invoke(MPFR_SET_SI, minusOne, -1L, 0);
            
            if ((int) NativeSafe.invoke(MPFR_CMP, cosTheta, one) > 0) NativeSafe.invoke(MPFR_SET, cosTheta, one, 0);
            if ((int) NativeSafe.invoke(MPFR_CMP, cosTheta, minusOne) < 0) NativeSafe.invoke(MPFR_SET, cosTheta, minusOne, 0);
            
            MemorySegment resAngle = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, resAngle);
            NativeSafe.invoke(MPFR_INIT2, resAngle, c_long(prec));
            NativeSafe.invoke(MPFR_ACOS, resAngle, cosTheta, 0);
            
            NativeRealBig angleVal = NativeRealBig.copyFrom(resAngle, prec);
            return createScalar(angleVal, a);
        } catch (Throwable t) {
            logger.error("MPFR angle failed: {}", t.getMessage());
            return (E) a.getScalarRing().zero();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> solveTriangular(Matrix<E> A, Vector<E> b, boolean upper, boolean transpose, boolean conjugate, boolean unit) {
        if (!AVAILABLE) throw new UnsupportedOperationException(getName() + " is not available.");
        int n = A.rows();
        if (n != A.cols() || n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        
        boolean isComplex = ((Object)A.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0;

        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(A, arena, tracker, prec, isComplex);
            MemorySegment h_B = initVector(b, arena, tracker, prec, isComplex);
            MemorySegment h_X = allocateVector(n, arena, tracker, prec, isComplex);
            
            MemorySegment sumR = NativeSafe.allocate(arena, MPFR_LAYOUT);
            MemorySegment sumI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            MemorySegment termR = NativeSafe.allocate(arena, MPFR_LAYOUT);
            MemorySegment termI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            
            NativeSafe.invoke(MPFR_INIT2, sumR, c_long(prec));
            track(tracker, sumR);
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, sumI, c_long(prec));
                track(tracker, sumI);
            }
            NativeSafe.invoke(MPFR_INIT2, termR, c_long(prec));
            track(tracker, termR);
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, termI, c_long(prec));
                track(tracker, termI);
            }

            if (!transpose) {
                if (!upper) {
                    // Forward substitution
                    for (int i = 0; i < n; i++) {
                        NativeSafe.invoke(MPFR_SET, sumR, getMPFRVector(h_B, i, 0, isComplex), rnd);
                        if (isComplex) NativeSafe.invoke(MPFR_SET, sumI, getMPFRVector(h_B, i, 1, isComplex), rnd);
                        
                        for (int j = 0; j < i; j++) {
                            if (isComplex) {
                                if (transpose && conjugate) {
                                    // A^H substitution: sum -= conj(A[i,j]) * X[j]
                                    complexSubtractMulVector(h_X, j, getMPFR(h_A, i, j, n, 0, true), getMPFR(h_A, i, j, n, 1, true), sumR, sumI, prec, arena, tracker, true);
                                } else {
                                    complexSubtractMulVector(h_X, j, getMPFR(h_A, i, j, n, 0, true), getMPFR(h_A, i, j, n, 1, true), sumR, sumI, prec, arena, tracker, false);
                                }
                            } else {
                                NativeSafe.invoke(MPFR_MUL, termR, getMPFR(h_A, i, j, n, 0, false), getMPFRVector(h_X, j, 0, false), rnd);
                                NativeSafe.invoke(MPFR_SUB, sumR, sumR, termR, rnd);
                            }
                        }
                        
                        MemorySegment xiR = getMPFRVector(h_X, i, 0, isComplex);
                        MemorySegment xiI = isComplex ? getMPFRVector(h_X, i, 1, true) : null;
                        
                        if (!unit) {
                            if (isComplex) {
                                complexDivide(xiR, xiI, sumR, sumI, getMPFR(h_A, i, i, n, 0, true), getMPFR(h_A, i, i, n, 1, true), prec, arena, tracker);
                            } else {
                                NativeSafe.invoke(MPFR_DIV, xiR, sumR, getMPFR(h_A, i, i, n, 0, false), rnd);
                            }
                        } else {
                            NativeSafe.invoke(MPFR_SET, xiR, sumR, rnd);
                            if (isComplex) NativeSafe.invoke(MPFR_SET, xiI, sumI, rnd);
                        }
                    }
                } else {
                    // Backward substitution
                    for (int i = n - 1; i >= 0; i--) {
                        NativeSafe.invoke(MPFR_SET, sumR, getMPFRVector(h_B, i, 0, isComplex), rnd);
                        if (isComplex) NativeSafe.invoke(MPFR_SET, sumI, getMPFRVector(h_B, i, 1, isComplex), rnd);
                        
                        for (int j = i + 1; j < n; j++) {
                            if (isComplex) {
                                if (transpose && conjugate) {
                                    complexSubtractMulVector(h_X, j, getMPFR(h_A, i, j, n, 0, true), getMPFR(h_A, i, j, n, 1, true), sumR, sumI, prec, arena, tracker, true);
                                } else {
                                    complexSubtractMulVector(h_X, j, getMPFR(h_A, i, j, n, 0, true), getMPFR(h_A, i, j, n, 1, true), sumR, sumI, prec, arena, tracker, false);
                                }
                            } else {
                                NativeSafe.invoke(MPFR_MUL, termR, getMPFR(h_A, i, j, n, 0, false), getMPFRVector(h_X, j, 0, false), rnd);
                                NativeSafe.invoke(MPFR_SUB, sumR, sumR, termR, rnd);
                            }
                        }
                        
                        MemorySegment xiR = getMPFRVector(h_X, i, 0, isComplex);
                        MemorySegment xiI = isComplex ? getMPFRVector(h_X, i, 1, true) : null;
                        
                        if (!unit) {
                            if (isComplex) {
                                complexDivide(xiR, xiI, sumR, sumI, getMPFR(h_A, i, i, n, 0, true), getMPFR(h_A, i, i, n, 1, true), prec, arena, tracker);
                            } else {
                                NativeSafe.invoke(MPFR_DIV, xiR, sumR, getMPFR(h_A, i, i, n, 0, false), rnd);
                            }
                        } else {
                            NativeSafe.invoke(MPFR_SET, xiR, sumR, rnd);
                            if (isComplex) NativeSafe.invoke(MPFR_SET, xiI, sumI, rnd);
                        }
                    }
                }
            } else {
                // Transposed substitution
                if (upper) {
                    // (U^T) * x = b -> Forward substitution
                    for (int i = 0; i < n; i++) {
                        NativeSafe.invoke(MPFR_SET, sumR, getMPFRVector(h_B, i, 0, isComplex), rnd);
                        if (isComplex) NativeSafe.invoke(MPFR_SET, sumI, getMPFRVector(h_B, i, 1, isComplex), rnd);
                        
                        for (int j = 0; j < i; j++) {
                            // sum -= A[j,i] * X[j]
                            if (isComplex) {
                                if (conjugate) {
                                    // A^H x = b -> A_ji becomes conj(A_ji)
                                    complexSubtractMulVector(h_X, j, getMPFR(h_A, j, i, n, 0, true), getMPFR(h_A, j, i, n, 1, true), sumR, sumI, prec, arena, tracker, true);
                                } else {
                                    complexSubtractMulVector(h_X, j, getMPFR(h_A, j, i, n, 0, true), getMPFR(h_A, j, i, n, 1, true), sumR, sumI, prec, arena, tracker, false);
                                }
                            } else {
                                NativeSafe.invoke(MPFR_MUL, termR, getMPFR(h_A, j, i, n, 0, false), getMPFRVector(h_X, j, 0, false), rnd);
                                NativeSafe.invoke(MPFR_SUB, sumR, sumR, termR, rnd);
                            }
                        }
                        
                        MemorySegment xiR = getMPFRVector(h_X, i, 0, isComplex);
                        MemorySegment xiI = isComplex ? getMPFRVector(h_X, i, 1, true) : null;
                        
                        if (!unit) {
                            if (isComplex) {
                                complexDivide(xiR, xiI, sumR, sumI, getMPFR(h_A, i, i, n, 0, true), getMPFR(h_A, i, i, n, 1, true), prec, arena, tracker);
                            } else {
                                NativeSafe.invoke(MPFR_DIV, xiR, sumR, getMPFR(h_A, i, i, n, 0, false), rnd);
                            }
                        } else {
                            NativeSafe.invoke(MPFR_SET, xiR, sumR, rnd);
                            if (isComplex) NativeSafe.invoke(MPFR_SET, xiI, sumI, rnd);
                        }
                    }
                } else {
                    // (L^T) * x = b -> Backward substitution
                    for (int i = n - 1; i >= 0; i--) {
                        NativeSafe.invoke(MPFR_SET, sumR, getMPFRVector(h_B, i, 0, isComplex), rnd);
                        if (isComplex) NativeSafe.invoke(MPFR_SET, sumI, getMPFRVector(h_B, i, 1, isComplex), rnd);
                        
                        for (int j = i + 1; j < n; j++) {
                            if (isComplex) {
                                if (conjugate) {
                                    complexSubtractMulVector(h_X, j, getMPFR(h_A, j, i, n, 0, true), getMPFR(h_A, j, i, n, 1, true), sumR, sumI, prec, arena, tracker, true);
                                } else {
                                    complexSubtractMulVector(h_X, j, getMPFR(h_A, j, i, n, 0, true), getMPFR(h_A, j, i, n, 1, true), sumR, sumI, prec, arena, tracker, false);
                                }
                            } else {
                                NativeSafe.invoke(MPFR_MUL, termR, getMPFR(h_A, j, i, n, 0, false), getMPFRVector(h_X, j, 0, false), rnd);
                                NativeSafe.invoke(MPFR_SUB, sumR, sumR, termR, rnd);
                            }
                        }
                        
                        MemorySegment xiR = getMPFRVector(h_X, i, 0, isComplex);
                        MemorySegment xiI = isComplex ? getMPFRVector(h_X, i, 1, true) : null;
                        
                        if (!unit) {
                            if (isComplex) {
                                complexDivide(xiR, xiI, sumR, sumI, getMPFR(h_A, i, i, n, 0, true), getMPFR(h_A, i, i, n, 1, true), prec, arena, tracker);
                            } else {
                                NativeSafe.invoke(MPFR_DIV, xiR, sumR, getMPFR(h_A, i, i, n, 0, false), rnd);
                            }
                        } else {
                            NativeSafe.invoke(MPFR_SET, xiR, sumR, rnd);
                            if (isComplex) NativeSafe.invoke(MPFR_SET, xiI, sumI, rnd);
                        }
                    }
                }
            }
            
            return backToVector_internal(h_X, n, arena, A.getScalarRing(), isComplex);
        } catch (Throwable t) {
            logger.error("MPFR solveTriangular failed: {}", t.getMessage());
            throw new RuntimeException("MPFR solveTriangular failed", t);
        }
    }
    @Override
    @SuppressWarnings("unchecked")
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<E> eigen(Matrix<E> a) {
        if (!AVAILABLE) throw new UnsupportedOperationException(getName() + " is not available.");
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        int n = a.rows();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0;

        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment h_V = allocateMatrix(n, n, arena, tracker, prec, isComplex);
            
            for (int i = 0; i < n; i++) {
                if (isComplex) {
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_V, i, i, n, 0, true), 1L, rnd);
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_V, i, i, n, 1, true), 0L, rnd);
                } else {
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_V, i, i, n, 0, false), 1L, rnd);
                }
            }

            int maxIter = n * n * 30;
            int iter = 0;
            MemorySegment eps = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, eps, c_long(prec));
            NativeSafe.invoke(MPFR_SET_STR, eps, NativeSafe.allocateFrom(arena, "1e-50"), 10, rnd);

            MemorySegment t = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t, c_long(prec));
            MemorySegment c = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, c, c_long(prec));
            MemorySegment s = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, s, c_long(prec));
            MemorySegment tau = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tau, c_long(prec));
            MemorySegment theta = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, theta, c_long(prec));

            while (iter < maxIter) {
                int p = 0, q = 1;
                MemorySegment maxOff = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, maxOff, c_long(prec));
                NativeSafe.invoke(MPFR_SET_UI, maxOff, 0L, rnd);
                
                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j < n; j++) {
                        MemorySegment val = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, val, c_long(prec));
                        NativeSafe.invoke(MPFR_ABS, val, getMPFR(h_A, i, j, n, 0, isComplex), rnd);
                        if ((int) NativeSafe.invoke(MPFR_CMP, val, maxOff) > 0) {
                            NativeSafe.invoke(MPFR_SET, maxOff, val, rnd);
                            p = i; q = j;
                        }
                        NativeSafe.invoke(MPFR_CLEAR, val);
                    }
                }

                if ((int) NativeSafe.invoke(MPFR_CMP, maxOff, eps) <= 0) {
                    NativeSafe.invoke(MPFR_CLEAR, maxOff);
                    break;
                }
                NativeSafe.invoke(MPFR_CLEAR, maxOff);

                MemorySegment apq = getMPFR(h_A, p, q, n, 0, isComplex);
                MemorySegment app = getMPFR(h_A, p, p, n, 0, isComplex);
                MemorySegment aqq = getMPFR(h_A, q, q, n, 0, isComplex);
                
                MemorySegment two = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, two, c_long(prec)); track(tracker, two);
                MemorySegment tempR = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tempR, c_long(prec)); track(tracker, tempR);
                MemorySegment tempV = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tempV, c_long(prec)); track(tracker, tempV);

                NativeSafe.invoke(MPFR_SET_UI, two, 2L, rnd);
                NativeSafe.invoke(MPFR_MUL, t, two, apq, rnd);
                NativeSafe.invoke(MPFR_SUB, theta, aqq, app, rnd);
                NativeSafe.invoke(MPFR_DIV, theta, theta, t, rnd);

                NativeSafe.invoke(MPFR_MUL, t, theta, theta, rnd);
                NativeSafe.invoke(MPFR_SET_UI, s, 1L, rnd);
                NativeSafe.invoke(MPFR_ADD, t, t, s, rnd);
                NativeSafe.invoke(MPFR_SQRT, t, t, rnd);
                NativeSafe.invoke(MPFR_ABS, s, theta, rnd);
                NativeSafe.invoke(MPFR_ADD, t, t, s, rnd);
                NativeSafe.invoke(MPFR_SET_UI, s, 1L, rnd);
                NativeSafe.invoke(MPFR_DIV, t, s, t, rnd);
                if ((int) NativeSafe.invoke(MPFR_CMP_SI, theta, 0L) < 0) NativeSafe.invoke(MPFR_NEG, t, t, rnd);

                NativeSafe.invoke(MPFR_MUL, c, t, t, rnd);
                NativeSafe.invoke(MPFR_SET_UI, s, 1L, rnd);
                NativeSafe.invoke(MPFR_ADD, c, c, s, rnd);
                NativeSafe.invoke(MPFR_SQRT, c, c, rnd);
                NativeSafe.invoke(MPFR_SET_UI, s, 1L, rnd);
                NativeSafe.invoke(MPFR_DIV, c, s, c, rnd);
                NativeSafe.invoke(MPFR_MUL, s, c, t, rnd);
                
                NativeSafe.invoke(MPFR_SET_UI, tau, 1L, rnd);
                NativeSafe.invoke(MPFR_ADD, tau, tau, c, rnd);
                NativeSafe.invoke(MPFR_DIV, tau, s, tau, rnd);

                NativeSafe.invoke(MPFR_MUL, theta, t, apq, rnd);
                NativeSafe.invoke(MPFR_SUB, app, app, theta, rnd);
                NativeSafe.invoke(MPFR_ADD, aqq, aqq, theta, rnd);
                NativeSafe.invoke(MPFR_SET_UI, apq, 0L, rnd);
                NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_A, q, p, n, 0, isComplex), 0L, rnd);

                for (int i = 0; i < n; i++) {
                    if (i != p && i != q) {
                        MemorySegment aip = getMPFR(h_A, i, p, n, 0, isComplex);
                        MemorySegment aiq = getMPFR(h_A, i, q, n, 0, isComplex);
                        NativeSafe.invoke(MPFR_SET, tempR, aip, rnd);
                        
                        NativeSafe.invoke(MPFR_MUL, theta, s, aiq, rnd);
                        NativeSafe.invoke(MPFR_MUL, aip, c, aip, rnd);
                        NativeSafe.invoke(MPFR_SUB, aip, aip, theta, rnd);
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_A, p, i, n, 0, isComplex), aip, rnd);
                        
                        NativeSafe.invoke(MPFR_MUL, theta, s, tempR, rnd);
                        NativeSafe.invoke(MPFR_MUL, aiq, c, aiq, rnd);
                        NativeSafe.invoke(MPFR_ADD, aiq, aiq, theta, rnd);
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_A, q, i, n, 0, isComplex), aiq, rnd);
                    }
                    MemorySegment vip = getMPFR(h_V, i, p, n, 0, isComplex);
                    MemorySegment viq = getMPFR(h_V, i, q, n, 0, isComplex);
                    NativeSafe.invoke(MPFR_SET, tempV, vip, rnd);
                    
                    NativeSafe.invoke(MPFR_MUL, theta, s, viq, rnd);
                    NativeSafe.invoke(MPFR_MUL, vip, c, vip, rnd);
                    NativeSafe.invoke(MPFR_SUB, vip, vip, theta, rnd);
                    
                    NativeSafe.invoke(MPFR_MUL, theta, s, tempV, rnd);
                    NativeSafe.invoke(MPFR_MUL, viq, c, viq, rnd);
                    NativeSafe.invoke(MPFR_ADD, viq, viq, theta, rnd);
                }
                iter++;
            }
            
            Object[] evData = new Object[n];
            for (int i = 0; i < n; i++) {
                Real val = readMPFR(getMPFR(h_A, i, i, n, 0, isComplex), null, arena);
                evData[i] = isComplex ? (E) (Object) Complex.of(val, Real.ZERO) : (E) (Object) val;
            }
            Vector<E> values = new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>((java.util.List<E>)(java.util.List<?>)java.util.Arrays.asList(evData)), (LinearAlgebraProvider<E>) this, a.getScalarRing());
            Matrix<E> vectors = backToMatrix_internal(h_V, n, n, arena, a.getScalarRing(), isComplex);
            
            NativeSafe.invoke(MPFR_CLEAR, eps); NativeSafe.invoke(MPFR_CLEAR, t); NativeSafe.invoke(MPFR_CLEAR, c); NativeSafe.invoke(MPFR_CLEAR, s); NativeSafe.invoke(MPFR_CLEAR, tau); NativeSafe.invoke(MPFR_CLEAR, theta);
            
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<>(vectors, values);
        } catch (Throwable t2) {
            logger.error("MPFR Eigen failed: {}", t2.getMessage());
            throw new RuntimeException("MPFR Eigen failed", t2);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public SVDResult<E> svd(Matrix<E> a) {
        if (!AVAILABLE) throw new UnsupportedOperationException(getName() + " is not available.");
        int m = a.rows();
        int n = a.cols();
        
        if (m >= n) {
            Matrix<E> at = transpose(a);
            Matrix<E> ata = multiply(at, a);
            org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<E> eigen = eigen(ata);
            Vector<E> sValues = eigen.getEigenvalues();
            Matrix<E> v = eigen.getEigenvectors();
            
            // s_i = sqrt(lambda_i)
            List<E> sigmaList = new java.util.ArrayList<>(n);
            boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof Complex;
            for (int i = 0; i < n; i++) {
                E val = sValues.get(i);
                if (val instanceof Real r) {
                    Real sqrtR = r.sqrt();
                    sigmaList.add(isComplex ? (E)(Object)Complex.of(sqrtR, Real.ZERO) : (E)(Object)sqrtR);
                } else if (val instanceof Complex c) {
                    sigmaList.add((E)(Object)c.sqrt());
                } else {
                    sigmaList.add(val); // Fallback
                }
            }
            Vector<E> s = Vector.of(sigmaList, a.getScalarRing());
            
            // U = A * V * Sigma^-1
            Matrix<E> u = createMatrix(m, n, a.getScalarRing());
            for (int i = 0; i < n; i++) {
                Vector<E> vi = v.getColumn(i);
                Vector<E> avi = multiply(a, vi);
                E si = sigmaList.get(i);
                if (!isZero(si, a.getScalarRing())) {
                    E invSi;
                    if (si instanceof Real r) invSi = (E) r.inverse();
                    else if (si instanceof Complex c) invSi = (E) c.inverse();
                    else invSi = createScalar(1.0 / getRealValue(si), a); // fallback
                    setColumn(u, i, multiply(avi, invSi));
                } else {
                    setColumn(u, i, avi);
                }
            }
            return new SVDResult<>(u, s, v);
        } else {
            // m < n: use AA^T
            Matrix<E> at = transpose(a);
            Matrix<E> aat = multiply(a, at);
            org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<E> eigen = eigen(aat);
            Vector<E> sValues = eigen.getEigenvalues();
            Matrix<E> u = eigen.getEigenvectors();
            
            List<E> sigmaList = new java.util.ArrayList<>(m);
            boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof Complex;
            for (int i = 0; i < m; i++) {
                E val = sValues.get(i);
                if (val instanceof Real r) {
                    Real sqrtR = r.sqrt();
                    sigmaList.add(isComplex ? (E)(Object)Complex.of(sqrtR, Real.ZERO) : (E)(Object)sqrtR);
                } else if (val instanceof Complex c) {
                    sigmaList.add((E)(Object)c.sqrt());
                } else {
                    sigmaList.add(val); // Fallback
                }
            }
            Vector<E> s = Vector.of(sigmaList, a.getScalarRing());
            
            // V = A^T * U * Sigma^-1
            Matrix<E> v = createMatrix(n, m, a.getScalarRing());
            for (int i = 0; i < m; i++) {
                Vector<E> ui = u.getColumn(i);
                Vector<E> atui = multiply(at, ui);
                E si = sigmaList.get(i);
                if (!isZero(si, a.getScalarRing())) {
                    E invSi;
                    if (si instanceof Real r) invSi = (E) r.inverse();
                    else if (si instanceof Complex c) invSi = (E) c.inverse();
                    else invSi = createScalar(1.0 / getRealValue(si), a); // fallback
                    setColumn(v, i, multiply(atui, invSi));
                } else {
                    setColumn(v, i, atui);
                }
            }
            return new SVDResult<>(u, s, v);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public QRResult<E> qr(Matrix<E> a) {
        if (!AVAILABLE) throw new UnsupportedOperationException(getName() + " is not available.");
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0;

        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_A = initMatrix(a, arena, tracker, prec, isComplex);
            MemorySegment h_Q = allocateMatrix(m, m, arena, tracker, prec, isComplex);
            for (int i = 0; i < m; i++) {
                if (isComplex) {
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_Q, i, i, m, 0, true), 1L, rnd);
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_Q, i, i, m, 1, true), 0L, rnd);
                } else {
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_Q, i, i, m, 0, false), 1L, rnd);
                }
            }

            MemorySegment v = NativeSafe.allocate(arena, MPFR_LAYOUT, m * (isComplex ? 2 : 1));
            for (int i = 0; i < m * (isComplex ? 2 : 1); i++) NativeSafe.invoke(MPFR_INIT2, v.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
            trackArray(tracker, v, m * (isComplex ? 2 : 1));

            MemorySegment normX = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, normX, c_long(prec)); track(tracker, normX);
            MemorySegment s = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, s, c_long(prec)); track(tracker, s);
            MemorySegment hVal = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, hVal, c_long(prec)); track(tracker, hVal);
            MemorySegment t = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t, c_long(prec)); track(tracker, t);
            
            MemorySegment[] hTemps = isComplex ? new MemorySegment[8] : new MemorySegment[2];
            for (int i=0; i<hTemps.length; i++) { hTemps[i] = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, hTemps[i], c_long(prec)); track(tracker, hTemps[i]); }

            for (int k = 0; k < Math.min(m, n); k++) {
                NativeSafe.invoke(MPFR_SET_UI, normX, 0L, rnd);
                for (int i = k; i < m; i++) {
                    MemorySegment aikR = getMPFR(h_A, i, k, n, 0, isComplex);
                    NativeSafe.invoke(MPFR_MUL, t, aikR, aikR, rnd);
                    NativeSafe.invoke(MPFR_ADD, normX, normX, t, rnd);
                    if (isComplex) {
                        MemorySegment aikI = getMPFR(h_A, i, k, n, 1, true);
                        NativeSafe.invoke(MPFR_MUL, t, aikI, aikI, rnd);
                        NativeSafe.invoke(MPFR_ADD, normX, normX, t, rnd);
                    }
                }
                NativeSafe.invoke(MPFR_SQRT, normX, normX, rnd);

                MemorySegment akkR = getMPFR(h_A, k, k, n, 0, isComplex);
                MemorySegment akkI = isComplex ? getMPFR(h_A, k, k, n, 1, true) : null;

                if (isComplex) {
                    MemorySegment magXk = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, magXk, c_long(prec));
                    complexMagnitude(magXk, akkR, akkI, prec, arena, tracker);
                    
                    MemorySegment sR = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sR, c_long(prec));
                    MemorySegment sI = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sI, c_long(prec));

                    if ((int) NativeSafe.invoke(MPFR_ZERO_P, magXk) != 0) {
                        NativeSafe.invoke(MPFR_SET, sR, normX, rnd);
                        NativeSafe.invoke(MPFR_SET_UI, sI, 0L, rnd);
                    } else {
                        NativeSafe.invoke(MPFR_DIV, sR, akkR, magXk, rnd);
                        NativeSafe.invoke(MPFR_MUL, sR, sR, normX, rnd);
                        NativeSafe.invoke(MPFR_DIV, sI, akkI, magXk, rnd);
                        NativeSafe.invoke(MPFR_MUL, sI, sI, normX, rnd);
                    }

                    for (int i = 0; i < m; i++) {
                        if (i < k) {
                            NativeSafe.invoke(MPFR_SET_UI, v.asSlice(i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), 0L, rnd);
                            NativeSafe.invoke(MPFR_SET_UI, v.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), 0L, rnd);
                        } else if (i == k) {
                            NativeSafe.invoke(MPFR_ADD, v.asSlice(k * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), akkR, sR, rnd);
                            NativeSafe.invoke(MPFR_ADD, v.asSlice((k * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), akkI, sI, rnd);
                        } else {
                            NativeSafe.invoke(MPFR_SET, v.asSlice(i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), getMPFR(h_A, i, k, n, 0, true), rnd);
                            NativeSafe.invoke(MPFR_SET, v.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), getMPFR(h_A, i, k, n, 1, true), rnd);
                        }
                    }
                    NativeSafe.invoke(MPFR_CLEAR, magXk); NativeSafe.invoke(MPFR_CLEAR, sR); NativeSafe.invoke(MPFR_CLEAR, sI);
                } else {
                    if ((int) NativeSafe.invoke(MPFR_CMP_SI, akkR, 0L) < 0) NativeSafe.invoke(MPFR_NEG, s, normX, rnd);
                    else NativeSafe.invoke(MPFR_SET, s, normX, rnd);

                    for (int i = 0; i < m; i++) {
                        if (i < k) {
                            NativeSafe.invoke(MPFR_SET_UI, v.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), 0L, rnd);
                        } else if (i == k) {
                            NativeSafe.invoke(MPFR_ADD, v.asSlice(k * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), akkR, s, rnd);
                        } else {
                            NativeSafe.invoke(MPFR_SET, v.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), getMPFR(h_A, i, k, n, 0, false), rnd);
                        }
                    }
                }


                NativeSafe.invoke(MPFR_SET_UI, hVal, 0L, rnd);
                for (int i = k; i < m; i++) {
                    MemorySegment viR = v.asSlice(i * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_MUL, t, viR, viR, rnd);
                    NativeSafe.invoke(MPFR_ADD, hVal, hVal, t, rnd);
                    if (isComplex) {
                        MemorySegment viI = v.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_MUL, t, viI, viI, rnd);
                        NativeSafe.invoke(MPFR_ADD, hVal, hVal, t, rnd);
                    }
                }
                if ((int) NativeSafe.invoke(MPFR_ZERO_P, hVal) == 0) {
                    NativeSafe.invoke(MPFR_SET_UI, t, 2L, rnd);
                    NativeSafe.invoke(MPFR_DIV, hVal, t, hVal, rnd);
                    applyHouseholder(h_A, m, n, v, hVal, k, isComplex, prec, arena, hTemps);
                    applyHouseholderRight(h_Q, m, m, v, hVal, k, isComplex, prec, arena, hTemps);
                }
            }

            Matrix<E> qMat = backToMatrix_internal(h_Q, m, m, arena, a.getScalarRing(), isComplex);
            Matrix<E> rMat = backToMatrix_internal(h_A, m, n, arena, a.getScalarRing(), isComplex);
            
            // Explicitly zero out the lower triangular part of R for strict compliance
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < Math.min(i, n); j++) {
                    rMat.getStorage().set(i, j, a.getScalarRing().zero());
                }
            }
            
            return new QRResult<>(qMat, rMat);
        } catch (Throwable t) {
            logger.error("MPFR QR failed: {}", t.getMessage());
            throw new RuntimeException("MPFR QR failed", t);
        }
    }


    private void applyHouseholder(MemorySegment mat, int rows, int cols, MemorySegment v, MemorySegment h, int k, boolean isComplex, long prec, Arena arena, MemorySegment[] temps) throws Throwable {
        // A = A - h * v * (v^T * A)
        int rnd = 0;
        int stride = isComplex ? 2 : 1;
        MemorySegment w = NativeSafe.allocate(arena, MPFR_LAYOUT, cols * stride);
        for (int j = 0; j < cols * stride; j++) NativeSafe.invoke(MPFR_INIT2, w.asSlice(j * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
        
        try {
            // w = v^T * A (only for columns j >= k and rows i >= k)
            for (int j = k; j < cols; j++) {
                MemorySegment wjR = w.asSlice(j * stride * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment wjI = isComplex ? w.asSlice((j * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT) : null;
                NativeSafe.invoke(MPFR_SET_UI, wjR, 0L, rnd);
                if (isComplex) NativeSafe.invoke(MPFR_SET_UI, wjI, 0L, rnd);

                for (int i = k; i < rows; i++) {
                    MemorySegment viR = v.asSlice(i * stride * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment viI = isComplex ? v.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT) : null;
                    MemorySegment aijR = getMPFR(mat, i, j, cols, 0, isComplex);
                    MemorySegment aijI = isComplex ? getMPFR(mat, i, j, cols, 1, true) : null;

                    if (isComplex) {
                        MemorySegment t1 = temps[0];
                        MemorySegment t2 = temps[1];
                        
                        NativeSafe.invoke(MPFR_MUL, t1, viR, aijR, rnd);
                        NativeSafe.invoke(MPFR_MUL, t2, viI, aijI, rnd);
                        NativeSafe.invoke(MPFR_ADD, t1, t1, t2, rnd);
                        NativeSafe.invoke(MPFR_ADD, wjR, wjR, t1, rnd);

                        NativeSafe.invoke(MPFR_MUL, t1, viR, aijI, rnd);
                        NativeSafe.invoke(MPFR_MUL, t2, viI, aijR, rnd);
                        NativeSafe.invoke(MPFR_SUB, t1, t1, t2, rnd);
                        NativeSafe.invoke(MPFR_ADD, wjI, wjI, t1, rnd);
                    } else {
                        MemorySegment t = temps[0];
                        NativeSafe.invoke(MPFR_MUL, t, viR, aijR, rnd);
                        NativeSafe.invoke(MPFR_ADD, wjR, wjR, t, rnd);
                    }
                }
            }

            // A = A - h * v * w
            for (int i = k; i < rows; i++) {
                MemorySegment viR = v.asSlice(i * stride * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment viI = isComplex ? v.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT) : null;
                for (int j = k; j < cols; j++) {
                    MemorySegment wjR = w.asSlice(j * stride * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment wjI = isComplex ? w.asSlice((j * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT) : null;
                    MemorySegment aijR = getMPFR(mat, i, j, cols, 0, isComplex);
                    MemorySegment aijI = isComplex ? getMPFR(mat, i, j, cols, 1, true) : null;

                    if (isComplex) {
                        MemorySegment t1 = temps[0];
                        MemorySegment t2 = temps[1];
                        
                        NativeSafe.invoke(MPFR_MUL, t1, viR, wjR, rnd);
                        NativeSafe.invoke(MPFR_MUL, t2, viI, wjI, rnd);
                        NativeSafe.invoke(MPFR_SUB, t1, t1, t2, rnd);
                        NativeSafe.invoke(MPFR_MUL, t1, t1, h, rnd);
                        NativeSafe.invoke(MPFR_SUB, aijR, aijR, t1, rnd);

                        NativeSafe.invoke(MPFR_MUL, t1, viR, wjI, rnd);
                        NativeSafe.invoke(MPFR_MUL, t2, viI, wjR, rnd);
                        NativeSafe.invoke(MPFR_ADD, t1, t1, t2, rnd);
                        NativeSafe.invoke(MPFR_MUL, t1, t1, h, rnd);
                        NativeSafe.invoke(MPFR_SUB, aijI, aijI, t1, rnd);
                    } else {
                        MemorySegment t = temps[0];
                        NativeSafe.invoke(MPFR_MUL, t, viR, wjR, rnd);
                        NativeSafe.invoke(MPFR_MUL, t, t, h, rnd);
                        NativeSafe.invoke(MPFR_SUB, aijR, aijR, t, rnd);
                    }
                }
            }
        } finally {
            for (int j = 0; j < cols * stride; j++) NativeSafe.invoke(MPFR_CLEAR, w.asSlice(j * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT));
        }
    }

    private void applyHouseholderRight(MemorySegment mat, int rows, int cols, MemorySegment v, MemorySegment h, int k, boolean isComplex, long prec, Arena arena, MemorySegment[] temps) throws Throwable {
        // Q = Q - h * (Q * v) * v^T
        int rnd = 0;
        int stride = isComplex ? 2 : 1;
        MemorySegment w = NativeSafe.allocate(arena, MPFR_LAYOUT, rows * stride);
        for (int i = 0; i < rows * stride; i++) NativeSafe.invoke(MPFR_INIT2, w.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));

        try {
            // w = Q * v
            for (int i = 0; i < rows; i++) {
                MemorySegment wiR = w.asSlice(i * stride * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment wiI = isComplex ? w.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT) : null;
                NativeSafe.invoke(MPFR_SET_UI, wiR, 0L, rnd);
                if (isComplex) NativeSafe.invoke(MPFR_SET_UI, wiI, 0L, rnd);

                for (int j = k; j < cols; j++) {
                    MemorySegment qijR = getMPFR(mat, i, j, cols, 0, isComplex);
                    MemorySegment qijI = isComplex ? getMPFR(mat, i, j, cols, 1, true) : null;
                    MemorySegment vjR = v.asSlice(j * stride * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment vjI = isComplex ? v.asSlice((j * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT) : null;

                    if (isComplex) {
                        MemorySegment t1 = temps[0];
                        MemorySegment t2 = temps[1];
                        
                        NativeSafe.invoke(MPFR_MUL, t1, qijR, vjR, rnd);
                        NativeSafe.invoke(MPFR_MUL, t2, qijI, vjI, rnd);
                        NativeSafe.invoke(MPFR_SUB, t1, t1, t2, rnd);
                        NativeSafe.invoke(MPFR_ADD, wiR, wiR, t1, rnd);

                        NativeSafe.invoke(MPFR_MUL, t1, qijR, vjI, rnd);
                        NativeSafe.invoke(MPFR_MUL, t2, qijI, vjR, rnd);
                        NativeSafe.invoke(MPFR_ADD, t1, t1, t2, rnd);
                        NativeSafe.invoke(MPFR_ADD, wiI, wiI, t1, rnd);
                    } else {
                        MemorySegment t = temps[0];
                        NativeSafe.invoke(MPFR_MUL, t, qijR, vjR, rnd);
                        NativeSafe.invoke(MPFR_ADD, wiR, wiR, t, rnd);
                    }
                }
            }

            // Q = Q - h * w * v^H
            for (int i = 0; i < rows; i++) {
                MemorySegment wiR = w.asSlice(i * stride * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment wiI = isComplex ? w.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT) : null;
                for (int j = k; j < cols; j++) {
                    MemorySegment vjR = v.asSlice(j * stride * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment vjI = isComplex ? v.asSlice((j * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT) : null;
                    MemorySegment qijR = getMPFR(mat, i, j, cols, 0, isComplex);
                    MemorySegment qijI = isComplex ? getMPFR(mat, i, j, cols, 1, true) : null;

                    if (isComplex) {
                        MemorySegment t1 = temps[0];
                        MemorySegment t2 = temps[1];
                        
                        // w * v^H = (wiR + i*wiI) * (vjR - i*vjI) = (wiR*vjR + wiI*vjI) + i(wiI*vjR - wiR*vjI)
                        NativeSafe.invoke(MPFR_MUL, t1, wiR, vjR, rnd);
                        NativeSafe.invoke(MPFR_MUL, t2, wiI, vjI, rnd);
                        NativeSafe.invoke(MPFR_ADD, t1, t1, t2, rnd);
                        NativeSafe.invoke(MPFR_MUL, t1, t1, h, rnd);
                        NativeSafe.invoke(MPFR_SUB, qijR, qijR, t1, rnd);

                        NativeSafe.invoke(MPFR_MUL, t1, wiI, vjR, rnd);
                        NativeSafe.invoke(MPFR_MUL, t2, wiR, vjI, rnd);
                        NativeSafe.invoke(MPFR_SUB, t1, t1, t2, rnd);
                        NativeSafe.invoke(MPFR_MUL, t1, t1, h, rnd);
                        NativeSafe.invoke(MPFR_SUB, qijI, qijI, t1, rnd);
                    } else {
                        MemorySegment t = temps[0];
                        NativeSafe.invoke(MPFR_MUL, t, wiR, vjR, rnd);
                        NativeSafe.invoke(MPFR_MUL, t, t, h, rnd);
                        NativeSafe.invoke(MPFR_SUB, qijR, qijR, t, rnd);
                    }
                }
            }
        } finally {
            for (int i = 0; i < rows * stride; i++) NativeSafe.invoke(MPFR_CLEAR, w.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT));
        }
    }

    private static void trackArray(ResourceTracker tracker, MemorySegment array, int count) {
        for (int i = 0; i < count; i++) {
            MemorySegment p = array.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            tracker.track(p, s -> NativeSafe.invoke(MPFR_CLEAR, s));
        }
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
        // Square matrix: check if triangular
        if (isLowerTriangular(a)) return solveTriangular(a, b, false, false, false, false);
        if (isUpperTriangular(a)) return solveTriangular(a, b, true, false, false, false);

        return solveSquare(a, b);
    }

    private boolean isLowerTriangular(Matrix<E> a) {
        int n = a.rows();
        int m = a.cols();
        if (n != m) return false;
        org.episteme.core.mathematics.structures.rings.Ring<E> ring = a.getScalarRing();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (!isZero(a.get(i, j), ring)) return false;
            }
        }
        return true;
    }

    private boolean isUpperTriangular(Matrix<E> a) {
        int n = a.rows();
        int m = a.cols();
        if (n != m) return false;
        org.episteme.core.mathematics.structures.rings.Ring<E> ring = a.getScalarRing();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (!isZero(a.get(i, j), ring)) return false;
            }
        }
        return true;
    }

    private void complexSubtractMulVector(MemorySegment h_X, int j, MemorySegment aR, MemorySegment aI, MemorySegment sumR, MemorySegment sumI, long prec, Arena arena, ResourceTracker tracker, boolean conjugate) throws Throwable {
        MemorySegment xjR = getMPFRVector(h_X, j, 0, true);
        MemorySegment xjI = getMPFRVector(h_X, j, 1, true);
        MemorySegment prodR = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, prodR, c_long(prec));
        MemorySegment prodI = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, prodI, c_long(prec));
        try {
            if (conjugate) {
                // (aR - i*aI) * (xjR + i*xjI) = (aR*xjR + aI*xjI) + i(aR*xjI - aI*xjR)
                MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
                MemorySegment t2 = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2, c_long(prec));
                try {
                    NativeSafe.invoke(MPFR_MUL, t1, aR, xjR, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, aI, xjI, 0);
                    NativeSafe.invoke(MPFR_ADD, prodR, t1, t2, 0);

                    NativeSafe.invoke(MPFR_MUL, t1, aR, xjI, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, aI, xjR, 0);
                    NativeSafe.invoke(MPFR_SUB, prodI, t1, t2, 0);
                } finally {
                    NativeSafe.invoke(MPFR_CLEAR, t1); NativeSafe.invoke(MPFR_CLEAR, t2);
                }
            } else {
                complexMultiply(prodR, prodI, aR, aI, xjR, xjI, prec, arena, tracker);
            }
            NativeSafe.invoke(MPFR_SUB, sumR, sumR, prodR, 0);
            NativeSafe.invoke(MPFR_SUB, sumI, sumI, prodI, 0);
        } finally {
            NativeSafe.invoke(MPFR_CLEAR, prodR);
            NativeSafe.invoke(MPFR_CLEAR, prodI);
        }
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
                tempFactorR = NativeSafe.allocate(arena, MPFR_LAYOUT); tempFactorI = NativeSafe.allocate(arena, MPFR_LAYOUT);
                sumR = NativeSafe.allocate(arena, MPFR_LAYOUT); sumI = NativeSafe.allocate(arena, MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, tempFactorR, c_long(prec)); tracker.track(tempFactorR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_INIT2, tempFactorI, c_long(prec)); tracker.track(tempFactorI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_INIT2, sumR, c_long(prec)); tracker.track(sumR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_INIT2, sumI, c_long(prec)); tracker.track(sumI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            } else {
                tempFactor = NativeSafe.allocate(arena, MPFR_LAYOUT); sum = NativeSafe.allocate(arena, MPFR_LAYOUT); term = NativeSafe.allocate(arena, MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, tempFactor, c_long(prec)); tracker.track(tempFactor, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_INIT2, sum, c_long(prec)); tracker.track(sum, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_INIT2, term, c_long(prec)); tracker.track(term, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            }
            
            // Partial Pivoting GEPP
            for (int k = 0; k < n; k++) {
                int pivot = k;
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        if (compareComplexMagnitude(h_A, i, pivot, k, n, arena, tracker, prec)) pivot = i;
                    } else {
                        if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, pivot, k, n, 0, false)) > 0) pivot = i;
                    }
                }
                
                if (pivot != k) {
                    swapRows(h_A, k, pivot, n, isComplex, arena, tracker, prec);
                    swapRowsVector(h_B, k, pivot, isComplex, arena, tracker, prec);
                }
                
                // Gaussian Elimination with stability check
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        MemorySegment absK = NativeSafe.allocate(arena, MPFR_LAYOUT); tracker.track(absK, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        NativeSafe.invoke(MPFR_INIT2, absK, c_long(prec));
                        complexMagnitude(absK, getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true), prec, arena, tracker);
                        double dVal = (double) NativeSafe.invoke(MPFR_GET_D, absK, 0);
                        if (dVal <= 1e-60) throw new ArithmeticException("Singular matrix during elimination (pivot below epsilon)");

                        complexDivide(tempFactorR, tempFactorI, 
                            getMPFR(h_A, i, k, n, 0, true), getMPFR(h_A, i, k, n, 1, true),
                            getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true),
                            prec, arena, tracker);
                        
                        for (int j = k; j < n; j++) {
                            complexSubtractMul(h_A, i, j, tempFactorR, tempFactorI, h_A, k, j, n, arena, tracker, prec);
                        }
                        complexSubtractMulVector(h_B, i, tempFactorR, tempFactorI, h_B, k, arena, tracker, prec);
                    } else {
                        MemorySegment absK = NativeSafe.allocate(arena, MPFR_LAYOUT); tracker.track(absK, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        NativeSafe.invoke(MPFR_INIT2, absK, c_long(prec));
                        NativeSafe.invoke(MPFR_ABS, absK, getMPFR(h_A, k, k, n, 0, false), 0);
                        double dVal = (double) NativeSafe.invoke(MPFR_GET_D, absK, 0);
                        if (dVal <= 1e-60) throw new ArithmeticException("Singular matrix during elimination (pivot below epsilon)");

                        NativeSafe.invoke(MPFR_DIV, tempFactor, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, k, k, n, 0, false), rnd);
                        for (int j = k; j < n; j++) {
                            subtractMulReal(h_A, i, j, tempFactor, h_A, k, j, n, arena, tracker, prec);
                        }
                        subtractMulVectorReal(h_B, i, tempFactor, h_B, k, arena, tracker, prec);
                    }
                }
            }
            
            // Back substitution
            MemorySegment h_X = allocateVector(n, arena, tracker, prec, isComplex);
            for (int i = n - 1; i >= 0; i--) {
                if (isComplex) {
                    NativeSafe.invoke(MPFR_SET_STR, sumR, NativeSafe.allocateFrom(arena, "0"), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, sumI, NativeSafe.allocateFrom(arena, "0"), 10, 0);
                    
                    for (int j = i + 1; j < n; j++) {
                        complexAddMul(sumR, sumI, 
                            getMPFR(h_A, i, j, n, 0, true), getMPFR(h_A, i, j, n, 1, true),
                            getMPFRVector(h_X, j, 0, true), getMPFRVector(h_X, j, 1, true),
                            prec, arena, tracker);
                    }
                    
                    MemorySegment xiR = getMPFRVector(h_X, i, 0, true);
                    MemorySegment xiI = getMPFRVector(h_X, i, 1, true);
                    NativeSafe.invoke(MPFR_SUB, xiR, getMPFRVector(h_B, i, 0, true), sumR, 0);
                    NativeSafe.invoke(MPFR_SUB, xiI, getMPFRVector(h_B, i, 1, true), sumI, 0);
                    
                    complexDivide(xiR, xiI, xiR, xiI, 
                        getMPFR(h_A, i, i, n, 0, true), getMPFR(h_A, i, i, n, 1, true),
                        prec, arena, tracker);
                } else {
                    NativeSafe.invoke(MPFR_SET_STR, sum, NativeSafe.allocateFrom(arena, "0"), 10, 0);
                    
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
                        if (compareComplexMagnitude(h_A, i, pivot, k, n, arena, tracker, prec)) pivot = i;
                    }
                } else {
                    for (int i = k + 1; i < n; i++) {
                        if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, pivot, k, n, 0, false)) > 0) pivot = i;
                    }
                }

                if (pivot != k) {
                    swapRows(h_A, k, pivot, n, isComplex, arena, tracker, prec);
                    swapRows(h_Inv, k, pivot, n, isComplex, arena, tracker, prec);
                }
                
                // Check for singular matrix with magnitude-based epsilon
                MemorySegment absK = NativeSafe.allocate(arena, MPFR_LAYOUT); tracker.track(absK, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_INIT2, absK, c_long(prec));

                if (isComplex) {
                    complexMagnitude(absK, getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true), prec, arena, tracker);
                } else {
                    NativeSafe.invoke(MPFR_ABS, absK, getMPFR(h_A, k, k, n, 0, false), 0);
                }
                double dVal = (double) NativeSafe.invoke(MPFR_GET_D, absK, 0);
                if (dVal <= 1e-60) throw new ArithmeticException("Singular matrix (magnitude below epsilon)");

                // Normalize pivot row
                if (isComplex) {
                    MemorySegment pR = NativeSafe.allocate(arena, MPFR_LAYOUT); tracker.track(pR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                    MemorySegment pI = NativeSafe.allocate(arena, MPFR_LAYOUT); tracker.track(pI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                    NativeSafe.invoke(MPFR_INIT2, pR, c_long(prec));
                    NativeSafe.invoke(MPFR_INIT2, pI, c_long(prec));
                    
                    NativeSafe.invoke(MPFR_SET, pR, getMPFR(h_A, k, k, n, 0, true), rnd);
                    NativeSafe.invoke(MPFR_SET, pI, getMPFR(h_A, k, k, n, 1, true), rnd);
                    
                    for (int j = 0; j < n; j++) {
                        complexDivide(getMPFR(h_A, k, j, n, 0, true), getMPFR(h_A, k, j, n, 1, true),
                            getMPFR(h_A, k, j, n, 0, true), getMPFR(h_A, k, j, n, 1, true),
                            pR, pI, prec, arena, tracker);
                        complexDivide(getMPFR(h_Inv, k, j, n, 0, true), getMPFR(h_Inv, k, j, n, 1, true),
                            getMPFR(h_Inv, k, j, n, 0, true), getMPFR(h_Inv, k, j, n, 1, true),
                            pR, pI, prec, arena, tracker);
                    }
                    // Set A[k][k] to 1+0i explicitly for stability
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_A, k, k, n, 0, true), 1L, rnd);
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_A, k, k, n, 1, true), 0L, rnd);
                } else {
                    MemorySegment pivotVal = NativeSafe.allocate(arena, MPFR_LAYOUT); tracker.track(pivotVal, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                    NativeSafe.invoke(MPFR_INIT2, pivotVal, c_long(prec));
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
                        MemorySegment fR = NativeSafe.allocate(arena, MPFR_LAYOUT); tracker.track(fR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        MemorySegment fI = NativeSafe.allocate(arena, MPFR_LAYOUT); tracker.track(fI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        NativeSafe.invoke(MPFR_INIT2, fR, c_long(prec));
                        NativeSafe.invoke(MPFR_INIT2, fI, c_long(prec));
                        NativeSafe.invoke(MPFR_SET, fR, getMPFR(h_A, i, k, n, 0, true), 0);
                        NativeSafe.invoke(MPFR_SET, fI, getMPFR(h_A, i, k, n, 1, true), 0);
                        
                        for (int j = 0; j < n; j++) {
                            complexSubtractMul(h_A, i, j, fR, fI, h_A, k, j, n, arena, tracker, prec);
                            complexSubtractMul(h_Inv, i, j, fR, fI, h_Inv, k, j, n, arena, tracker, prec);
                        }
                    } else {
                        MemorySegment factor = NativeSafe.allocate(arena, MPFR_LAYOUT); tracker.track(factor, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        NativeSafe.invoke(MPFR_INIT2, factor, c_long(prec));
                        NativeSafe.invoke(MPFR_SET, factor, getMPFR(h_A, i, k, n, 0, false), 0);
                        
                        for (int j = 0; j < n; j++) {
                            subtractMulReal(h_A, i, j, factor, h_A, k, j, n, arena, tracker, prec);
                            subtractMulReal(h_Inv, i, j, factor, h_Inv, k, j, n, arena, tracker, prec);
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
            MemorySegment detR = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, detR, c_long(prec)); tracker.track(detR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            NativeSafe.invoke(MPFR_SET_UI, detR, 1L, 0); // Initialize det to 1
            MemorySegment detI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, detI, c_long(prec)); tracker.track(detI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                NativeSafe.invoke(MPFR_SET_UI, detI, 0L, 0); // Initialize imag part to 0
            }

            for (int k = 0; k < n; k++) {
                int pivot = k;
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        if (compareComplexMagnitude(h_A, i, pivot, k, n, arena, tracker, prec)) pivot = i;
                    } else {
                        if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, pivot, k, n, 0, false)) > 0) pivot = i;
                    }
                }
                
                if (pivot != k) {
                    swapRows(h_A, k, pivot, n, isComplex, arena, tracker, prec);
                    NativeSafe.invoke(MPFR_NEG, detR, detR, 0); // Flip sign of determinant
                    if (isComplex) NativeSafe.invoke(MPFR_NEG, detI, detI, 0);
                }
                
                // Multiply determinant by A[k][k]
                if (isComplex) {
                    complexMultiply(detR, detI, detR, detI, 
                        getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true), prec, arena, tracker);
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
                        MemorySegment fR = NativeSafe.allocate(arena, MPFR_LAYOUT);
                        MemorySegment fI = NativeSafe.allocate(arena, MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, fR, c_long(prec)); tracker.track(fR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        NativeSafe.invoke(MPFR_INIT2, fI, c_long(prec)); tracker.track(fI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        complexDivide(fR, fI, getMPFR(h_A, i, k, n, 0, true), getMPFR(h_A, i, k, n, 1, true),
                            getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true), prec, arena, tracker);
                        for (int j = k; j < n; j++) complexSubtractMul(h_A, i, j, fR, fI, h_A, k, j, n, arena, tracker, prec);
                    } else {
                        MemorySegment factor = NativeSafe.allocate(arena, MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, factor, c_long(prec)); tracker.track(factor, s -> NativeSafe.invoke(MPFR_CLEAR, s));
                        NativeSafe.invoke(MPFR_DIV, factor, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, k, k, n, 0, false), 0);
                        for (int j = k; j < n; j++) subtractMulReal(h_A, i, j, factor, h_A, k, j, n, arena, tracker, prec);
                    }
                }
            }
            
            // The determinant is now in detR/detI
            MemorySegment expPtr = NativeSafe.allocate(arena, ValueLayout.JAVA_LONG); 
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
            MemorySegment sumR = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sumR, c_long(prec));
            tracker.track(sumR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            MemorySegment sumI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, sumI, c_long(prec));
                tracker.track(sumI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            }

            MemorySegment termR = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, termR, c_long(prec));
            tracker.track(termR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            MemorySegment termI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, termI, c_long(prec));
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
                            MemorySegment ad = NativeSafe.allocate(arena, MPFR_LAYOUT);
                            NativeSafe.invoke(MPFR_INIT2, ad, c_long(prec));
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
            logger.error("MPFR cholesky failed: {}", t.getMessage(), t);
            throw new RuntimeException("MPFR cholesky failed: " + t.getMessage(), t);
        }
    }



    private static void subtractMulReal(MemorySegment h_Mat, int r, int c, MemorySegment factor, MemorySegment h_Source, int rs, int cs, int cols, Arena arena, ResourceTracker tracker, long prec) {
        MemorySegment term = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, term);
        NativeSafe.invoke(MPFR_INIT2, term, c_long(prec));
        subtractMulReal(h_Mat, r, c, factor, h_Source, rs, cs, cols, new MemorySegment[]{term});
    }

    private static void subtractMulReal(MemorySegment h_Mat, int r, int c, MemorySegment factor, MemorySegment h_Source, int rs, int cs, int cols, MemorySegment[] temps) {
        MemorySegment dst = getMPFR(h_Mat, r, c, cols, 0, false);
        MemorySegment src = getMPFR(h_Source, rs, cs, cols, 0, false);
        MemorySegment term = temps[0];
        NativeSafe.invoke(MPFR_MUL, term, factor, src, 0);
        NativeSafe.invoke(MPFR_SUB, dst, dst, term, 0);
    }

    private static void subtractMulVectorReal(MemorySegment h_Vec, int r, MemorySegment factor, MemorySegment h_Source, int rs, Arena arena, ResourceTracker tracker, long prec) {
        MemorySegment dst = getMPFRVector(h_Vec, r, 0, false);
        MemorySegment src = getMPFRVector(h_Source, rs, 0, false);
        MemorySegment term = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, term);
        NativeSafe.invoke(MPFR_INIT2, term, c_long(prec));
        NativeSafe.invoke(MPFR_MUL, term, factor, src, 0);
        NativeSafe.invoke(MPFR_SUB, dst, dst, term, 0);
    }

    private boolean compareComplexMagnitude(MemorySegment h_Mat, int r1, int r2, int col, int n, Arena arena, ResourceTracker tracker, long prec) throws Throwable {
        MemorySegment mag1 = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, mag1);
        MemorySegment mag2 = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, mag2);
        NativeSafe.invoke(MPFR_INIT2, mag1, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, mag2, c_long(prec));
        
        complexMagnitudeSquared(mag1, getMPFR(h_Mat, r1, col, n, 0, true), getMPFR(h_Mat, r1, col, n, 1, true), prec, arena, tracker);
        complexMagnitudeSquared(mag2, getMPFR(h_Mat, r2, col, n, 0, true), getMPFR(h_Mat, r2, col, n, 1, true), prec, arena, tracker);
        
        return (int) NativeSafe.invoke(MPFR_CMP, mag1, mag2) > 0;
    }

    private static void complexMagnitudeSquared(MemorySegment res, MemorySegment r, MemorySegment i, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment t = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t);
        NativeSafe.invoke(MPFR_INIT2, t, c_long(prec));
        NativeSafe.invoke(MPFR_MUL, res, r, r, 0);
        NativeSafe.invoke(MPFR_MUL, t, i, i, 0);
        NativeSafe.invoke(MPFR_ADD, res, res, t, 0);
    }

    private static void complexDivide(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment bR, MemorySegment bI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment denom = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, denom);
        MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t1);
        MemorySegment t2 = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t2);
        MemorySegment outR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, outR);
        MemorySegment outI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, outI);

        NativeSafe.invoke(MPFR_INIT2, denom, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, t2, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, outR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, outI, c_long(prec));
        
        complexDivide(resR, resI, aR, aI, bR, bI, prec, new MemorySegment[]{denom, t1, t2, outR, outI});
    }

    private static void complexDivide(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment bR, MemorySegment bI, long prec, MemorySegment[] temps) {
        MemorySegment denom = temps[0];
        MemorySegment t1 = temps[1];
        MemorySegment t2 = temps[2];
        MemorySegment outR = temps[3];
        MemorySegment outI = temps[4];

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



    private static void complexSubtractMul(MemorySegment h_Mat, int r, int c, MemorySegment fR, MemorySegment fI, MemorySegment h_Src, int rs, int cs, int n, Arena arena, ResourceTracker tracker, long prec) {
        MemorySegment tR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, tR);
        MemorySegment tI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, tI);
        MemorySegment tEmp = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, tEmp);

        NativeSafe.invoke(MPFR_INIT2, tR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, tI, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, tEmp, c_long(prec));
        
        complexSubtractMul(h_Mat, r, c, fR, fI, h_Src, rs, cs, n, new MemorySegment[]{tR, tI, tEmp});
    }

    private static void complexSubtractMul(MemorySegment h_Mat, int r, int c, MemorySegment fR, MemorySegment fI, MemorySegment h_Src, int rs, int cs, int n, MemorySegment[] temps) {
        MemorySegment dstR = getMPFR(h_Mat, r, c, n, 0, true);
        MemorySegment dstI = getMPFR(h_Mat, r, c, n, 1, true);
        MemorySegment srcR = getMPFR(h_Src, rs, cs, n, 0, true);
        MemorySegment srcI = getMPFR(h_Src, rs, cs, n, 1, true);
        
        MemorySegment tR = temps[0];
        MemorySegment tI = temps[1];
        MemorySegment tEmp = temps[2];

        NativeSafe.invoke(MPFR_MUL, tR, fR, srcR, 0);
        NativeSafe.invoke(MPFR_MUL, tI, fI, srcI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tI, 0);
        
        NativeSafe.invoke(MPFR_MUL, tI, fR, srcI, 0);
        NativeSafe.invoke(MPFR_MUL, tEmp, fI, srcR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0);
        
        NativeSafe.invoke(MPFR_SUB, dstR, dstR, tR, 0);
        NativeSafe.invoke(MPFR_SUB, dstI, dstI, tI, 0);
    }

    private static void complexSubtractMulVector(MemorySegment h_Vec, int r, MemorySegment fR, MemorySegment fI, MemorySegment h_Src, int rs, Arena arena, ResourceTracker tracker, long prec) {
        MemorySegment dstR = getMPFRVector(h_Vec, r, 0, true);
        MemorySegment dstI = getMPFRVector(h_Vec, r, 1, true);
        MemorySegment srcR = getMPFRVector(h_Src, rs, 0, true);
        MemorySegment srcI = getMPFRVector(h_Src, rs, 1, true);
        
        MemorySegment tR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, tR);
        MemorySegment tI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, tI);
        MemorySegment tEmp = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, tEmp);

        NativeSafe.invoke(MPFR_INIT2, tR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, tI, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, tEmp, c_long(prec));
        
        NativeSafe.invoke(MPFR_MUL, tR, fR, srcR, 0);
        NativeSafe.invoke(MPFR_MUL, tI, fI, srcI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tI, 0);
        
        NativeSafe.invoke(MPFR_MUL, tI, fR, srcI, 0);
        NativeSafe.invoke(MPFR_MUL, tEmp, fI, srcR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0);
        
        NativeSafe.invoke(MPFR_SUB, dstR, dstR, tR, 0);
        NativeSafe.invoke(MPFR_SUB, dstI, dstI, tI, 0);
    }

    private static void complexMultiply(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment bR, MemorySegment bI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment tR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, tR);
        MemorySegment tI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, tI);
        MemorySegment tEmp = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, tEmp);

        NativeSafe.invoke(MPFR_INIT2, tR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, tI, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, tEmp, c_long(prec));
        
        NativeSafe.invoke(MPFR_MUL, tR, aR, bR, 0);
        NativeSafe.invoke(MPFR_MUL, tEmp, aI, bI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tEmp, 0);
        
        NativeSafe.invoke(MPFR_MUL, tI, aR, bI, 0);
        NativeSafe.invoke(MPFR_MUL, tEmp, aI, bR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0);
        
        NativeSafe.invoke(MPFR_SET, resR, tR, 0);
        NativeSafe.invoke(MPFR_SET, resI, tI, 0);
    }

    private static void complexAddMul(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment bR, MemorySegment bI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment tR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, tR);
        MemorySegment tI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, tI);
        MemorySegment tEmp = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, tEmp);

        NativeSafe.invoke(MPFR_INIT2, tR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, tI, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, tEmp, c_long(prec));
        
        NativeSafe.invoke(MPFR_MUL, tR, aR, bR, 0);
        NativeSafe.invoke(MPFR_MUL, tI, aI, bI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tI, 0);
        
        NativeSafe.invoke(MPFR_MUL, tI, aR, bI, 0);
        NativeSafe.invoke(MPFR_MUL, tEmp, aI, bR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0);
        
        NativeSafe.invoke(MPFR_ADD, resR, resR, tR, 0);
        NativeSafe.invoke(MPFR_ADD, resI, resI, tI, 0);
    }

    private static void swapRows(MemorySegment h_Mat, int r1, int r2, int cols, boolean isComplex, Arena arena, ResourceTracker tracker, long prec) {
        int multiplier = isComplex ? 2 : 1;
        MemorySegment tmp = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, tmp);
        NativeSafe.invoke(MPFR_INIT2, tmp, c_long(prec));
        for (int j = 0; j < cols * multiplier; j++) {
            MemorySegment m1 = h_Mat.asSlice((long)(r1 * cols * multiplier + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            MemorySegment m2 = h_Mat.asSlice((long)(r2 * cols * multiplier + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_SET, tmp, m1, 0);
            NativeSafe.invoke(MPFR_SET, m1, m2, 0);
            NativeSafe.invoke(MPFR_SET, m2, tmp, 0);
        }
    }

    private static void swapRowsVector(MemorySegment h_Vec, int r1, int r2, boolean isComplex, Arena arena, ResourceTracker tracker, long prec) {
        int multiplier = isComplex ? 2 : 1;
        MemorySegment tmp = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, tmp);
        NativeSafe.invoke(MPFR_INIT2, tmp, c_long(prec));
        for (int j = 0; j < multiplier; j++) {
            MemorySegment m1 = getMPFRVector(h_Vec, r1, j, isComplex);
            MemorySegment m2 = getMPFRVector(h_Vec, r2, j, isComplex);
            NativeSafe.invoke(MPFR_SET, tmp, m1, 0);
            NativeSafe.invoke(MPFR_SET, m1, m2, 0);
            NativeSafe.invoke(MPFR_SET, m2, tmp, 0);
        }
    }

    // --- Complex Transcendental Arithmetic ---
    
    private static void complexMagnitude(MemorySegment rop, MemorySegment opR, MemorySegment opI, long prec, Arena arena, ResourceTracker tracker) {
        if (MPFR_HYPOT != null) {
            NativeSafe.invoke(MPFR_HYPOT, rop, opR, opI, 0);
        } else {
            MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT);
            MemorySegment t2 = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, t2, c_long(prec));
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

    public static void complexExp(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment expR = NativeSafe.allocate(arena, MPFR_LAYOUT);
        MemorySegment cosI = NativeSafe.allocate(arena, MPFR_LAYOUT);
        MemorySegment sinI = NativeSafe.allocate(arena, MPFR_LAYOUT);

        NativeSafe.invoke(MPFR_INIT2, expR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, cosI, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, sinI, c_long(prec));
        
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

    public static void complexLog(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        if (((Number) NativeSafe.invoke(MPFR_NAN_P, aR)).intValue() != 0 || ((Number) NativeSafe.invoke(MPFR_NAN_P, aI)).intValue() != 0) {
            NativeSafe.invoke(MPFR_SET_NAN, resR);
            NativeSafe.invoke(MPFR_SET_NAN, resI);
            return;
        }

        MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT);
        MemorySegment t2 = NativeSafe.allocate(arena, MPFR_LAYOUT);
        MemorySegment pi = NativeSafe.allocate(arena, MPFR_LAYOUT);
        MemorySegment zero = NativeSafe.allocate(arena, MPFR_LAYOUT);
        
        NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, t2, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, pi, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, zero, c_long(prec));
    
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
                        MemorySegment two = NativeSafe.allocate(arena, MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, two, c_long(prec));
                        NativeSafe.invoke(MPFR_SET_UI, two, 2L, 0);
                        NativeSafe.invoke(MPFR_DIV, resI, pi, two, 0);
                        NativeSafe.invoke(MPFR_CLEAR, two);
                    } else if (cmpY < 0) {
                        MemorySegment two = NativeSafe.allocate(arena, MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, two, c_long(prec));
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

    public static void complexLog10(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        complexLog(resR, resI, aR, aI, prec, arena, tracker);
        MemorySegment log10 = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, log10);
        MemorySegment ten = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, ten);
        NativeSafe.invoke(MPFR_INIT2, log10, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, ten, c_long(prec));
        NativeSafe.invoke(MPFR_SET_UI, ten, 10L, 0);
        NativeSafe.invoke(MPFR_LOG, log10, ten, 0);
        NativeSafe.invoke(MPFR_DIV, resR, resR, log10, 0);
        NativeSafe.invoke(MPFR_DIV, resI, resI, log10, 0);
    }

    public static void complexSin(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment sR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, sR);
        MemorySegment cR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, cR);
        MemorySegment shI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, shI);
        MemorySegment chI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, chI);
        NativeSafe.invoke(MPFR_INIT2, sR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, cR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, shI, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, chI, c_long(prec));
        NativeSafe.invoke(MPFR_SIN, sR, aR, 0);
        NativeSafe.invoke(MPFR_COS, cR, aR, 0);
        NativeSafe.invoke(MPFR_SINH, shI, aI, 0);
        NativeSafe.invoke(MPFR_COSH, chI, aI, 0);
        NativeSafe.invoke(MPFR_MUL, resR, sR, chI, 0);
        NativeSafe.invoke(MPFR_MUL, resI, cR, shI, 0);
    }

    public static void complexCos(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment sR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, sR);
        MemorySegment cR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, cR);
        MemorySegment shI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, shI);
        MemorySegment chI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, chI);
        NativeSafe.invoke(MPFR_INIT2, sR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, cR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, shI, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, chI, c_long(prec));
        NativeSafe.invoke(MPFR_SIN, sR, aR, 0);
        NativeSafe.invoke(MPFR_COS, cR, aR, 0);
        NativeSafe.invoke(MPFR_SINH, shI, aI, 0);
        NativeSafe.invoke(MPFR_COSH, chI, aI, 0);
        NativeSafe.invoke(MPFR_MUL, resR, cR, chI, 0);
        NativeSafe.invoke(MPFR_MUL, resI, sR, shI, 0);
        NativeSafe.invoke(MPFR_NEG, resI, resI, 0);
    }

    public static void complexTan(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment sR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, sR);
        MemorySegment sI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, sI);
        MemorySegment cR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, cR);
        MemorySegment cI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, cI);
        NativeSafe.invoke(MPFR_INIT2, sR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, sI, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, cR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, cI, c_long(prec));
        complexSin(sR, sI, aR, aI, prec, arena, tracker);
        complexCos(cR, cI, aR, aI, prec, arena, tracker);
        complexDivide(resR, resI, sR, sI, cR, cI, prec, arena, tracker);
    }

    public static void complexSinh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment shR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, shR);
        MemorySegment chR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, chR);
        MemorySegment sI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, sI);
        MemorySegment cI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, cI);
        NativeSafe.invoke(MPFR_INIT2, shR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, chR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, sI, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, cI, c_long(prec));
        NativeSafe.invoke(MPFR_SINH, shR, aR, 0);
        NativeSafe.invoke(MPFR_COSH, chR, aR, 0);
        NativeSafe.invoke(MPFR_SIN, sI, aI, 0);
        NativeSafe.invoke(MPFR_COS, cI, aI, 0);
        NativeSafe.invoke(MPFR_MUL, resR, shR, cI, 0);
        NativeSafe.invoke(MPFR_MUL, resI, chR, sI, 0);
    }

    public static void complexCosh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment shR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, shR);
        MemorySegment chR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, chR);
        MemorySegment sI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, sI);
        MemorySegment cI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, cI);
        NativeSafe.invoke(MPFR_INIT2, shR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, chR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, sI, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, cI, c_long(prec));
        NativeSafe.invoke(MPFR_SINH, shR, aR, 0);
        NativeSafe.invoke(MPFR_COSH, chR, aR, 0);
        NativeSafe.invoke(MPFR_SIN, sI, aI, 0);
        NativeSafe.invoke(MPFR_COS, cI, aI, 0);
        NativeSafe.invoke(MPFR_MUL, resR, chR, cI, 0);
        NativeSafe.invoke(MPFR_MUL, resI, shR, sI, 0);
    }

    public static void complexTanh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment sR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, sR);
        MemorySegment sI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, sI);
        MemorySegment cR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, cR);
        MemorySegment cI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, cI);
        NativeSafe.invoke(MPFR_INIT2, sR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, sI, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, cR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, cI, c_long(prec));
        complexSinh(sR, sI, aR, aI, prec, arena, tracker);
        complexCosh(cR, cI, aR, aI, prec, arena, tracker);
        complexDivide(resR, resI, sR, sI, cR, cI, prec, arena, tracker);
    }

    public static void complexSqrt(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment norm = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, norm);
        MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t1);
        MemorySegment t2 = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t2);
        MemorySegment zero = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, zero);
        NativeSafe.invoke(MPFR_INIT2, norm, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, t2, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, zero, c_long(prec));
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

    public static void complexAsin(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment izR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, izR);
        MemorySegment izI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, izI);
        MemorySegment z2R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, z2R);
        MemorySegment z2I = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, z2I);
        MemorySegment oneMinusZ2R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, oneMinusZ2R);
        MemorySegment oneMinusZ2I = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, oneMinusZ2I);
        MemorySegment sqrtR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, sqrtR);
        MemorySegment sqrtI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, sqrtI);
        MemorySegment sumR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, sumR);
        MemorySegment sumI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, sumI);
        MemorySegment logR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, logR);
        MemorySegment logI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, logI);
        
        NativeSafe.invoke(MPFR_INIT2, izR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, izI, c_long(prec));
        NativeSafe.invoke(MPFR_NEG, izR, aI, 0);
        NativeSafe.invoke(MPFR_SET, izI, aR, 0);
        
        NativeSafe.invoke(MPFR_INIT2, z2R, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, z2I, c_long(prec));
        complexMultiply(z2R, z2I, aR, aI, aR, aI, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_INIT2, oneMinusZ2R, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, oneMinusZ2I, c_long(prec));
        NativeSafe.invoke(MPFR_SET_UI, oneMinusZ2R, 1L, 0);
        NativeSafe.invoke(MPFR_SET_UI, oneMinusZ2I, 0L, 0);
        NativeSafe.invoke(MPFR_SUB, oneMinusZ2R, oneMinusZ2R, z2R, 0);
        NativeSafe.invoke(MPFR_SUB, oneMinusZ2I, oneMinusZ2I, z2I, 0);
        
        NativeSafe.invoke(MPFR_INIT2, sqrtR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, sqrtI, c_long(prec));
        complexSqrt(sqrtR, sqrtI, oneMinusZ2R, oneMinusZ2I, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_INIT2, sumR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, sumI, c_long(prec));
        NativeSafe.invoke(MPFR_ADD, sumR, izR, sqrtR, 0);
        NativeSafe.invoke(MPFR_ADD, sumI, izI, sqrtI, 0);
        
        NativeSafe.invoke(MPFR_INIT2, logR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, logI, c_long(prec));
        complexLog(logR, logI, sumR, sumI, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_SET, resR, logI, 0);
        NativeSafe.invoke(MPFR_NEG, resI, logR, 0);
    }

    public static void complexAcos(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment asinR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, asinR);
        MemorySegment asinI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, asinI);
        MemorySegment piHalf = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, piHalf);
        MemorySegment two = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, two);
        
        NativeSafe.invoke(MPFR_INIT2, asinR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, asinI, c_long(prec));
        complexAsin(asinR, asinI, aR, aI, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_INIT2, piHalf, c_long(prec));
        NativeSafe.invoke(MPFR_CONST_PI, piHalf, 0);
        NativeSafe.invoke(MPFR_INIT2, two, c_long(prec));
        NativeSafe.invoke(MPFR_SET_UI, two, 2L, 0);
        NativeSafe.invoke(MPFR_DIV, piHalf, piHalf, two, 0);
        
        NativeSafe.invoke(MPFR_SUB, resR, piHalf, asinR, 0);
        NativeSafe.invoke(MPFR_NEG, resI, asinI, 0);
    }

    public static void complexAtan(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment izR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, izR);
        MemorySegment izI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, izI);
        MemorySegment numR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, numR);
        MemorySegment numI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, numI);
        MemorySegment denR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, denR);
        MemorySegment denI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, denI);
        MemorySegment divR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, divR);
        MemorySegment divI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, divI);
        MemorySegment logR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, logR);
        MemorySegment logI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, logI);
        MemorySegment check = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, check);
        
        NativeSafe.invoke(MPFR_INIT2, izR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, izI, c_long(prec));
        NativeSafe.invoke(MPFR_NEG, izR, aI, 0);
        NativeSafe.invoke(MPFR_SET, izI, aR, 0);
        
        NativeSafe.invoke(MPFR_INIT2, numR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, numI, c_long(prec));
        NativeSafe.invoke(MPFR_SET_UI, numR, 1L, 0);
        NativeSafe.invoke(MPFR_SET_UI, numI, 0L, 0);
        NativeSafe.invoke(MPFR_SUB, numR, numR, izR, 0);
        NativeSafe.invoke(MPFR_SUB, numI, numI, izI, 0);
        
        NativeSafe.invoke(MPFR_INIT2, denR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, denI, c_long(prec));
        NativeSafe.invoke(MPFR_SET_UI, denR, 1L, 0);
        NativeSafe.invoke(MPFR_SET_UI, denI, 0L, 0);
        NativeSafe.invoke(MPFR_ADD, denR, denR, izR, 0);
        NativeSafe.invoke(MPFR_ADD, denI, denI, izI, 0);
        
        NativeSafe.invoke(MPFR_INIT2, divR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, divI, c_long(prec));
        complexDivide(divR, divI, numR, numI, denR, denI, prec, arena, tracker);
        complexLog(logR, logI, divR, divI, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_SET, resR, logI, 0);
        NativeSafe.invoke(MPFR_NEG, resR, resR, 0);
        NativeSafe.invoke(MPFR_INIT2, check, c_long(prec));
        NativeSafe.invoke(MPFR_SET_UI, check, 2L, 0);
        NativeSafe.invoke(MPFR_DIV, resR, resR, check, 0);
        NativeSafe.invoke(MPFR_SET, resI, logR, 0);
        NativeSafe.invoke(MPFR_DIV, resI, resI, check, 0);
    }

    public static void complexAsinh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment z2R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, z2R);
        MemorySegment z2I = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, z2I);
        MemorySegment sqrtR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, sqrtR);
        MemorySegment sqrtI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, sqrtI);
        
        NativeSafe.invoke(MPFR_INIT2, z2R, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, z2I, c_long(prec));
        complexMultiply(z2R, z2I, aR, aI, aR, aI, prec, arena, tracker);
        
        MemorySegment one = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, one);
        NativeSafe.invoke(MPFR_INIT2, one, c_long(prec));
        NativeSafe.invoke(MPFR_SET_UI, one, 1L, 0);
        NativeSafe.invoke(MPFR_ADD, z2R, z2R, one, 0);
        
        NativeSafe.invoke(MPFR_INIT2, sqrtR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, sqrtI, c_long(prec));
        complexSqrt(sqrtR, sqrtI, z2R, z2I, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_ADD, z2R, aR, sqrtR, 0);
        NativeSafe.invoke(MPFR_ADD, z2I, aI, sqrtI, 0);
        complexLog(resR, resI, z2R, z2I, prec, arena, tracker);
    }

    public static void complexAcosh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment zp1R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, zp1R);
        MemorySegment zm1R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, zm1R);
        MemorySegment s1R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, s1R);
        MemorySegment s1I = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, s1I);
        MemorySegment s2R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, s2R);
        MemorySegment s2I = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, s2I);
        MemorySegment prodR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, prodR);
        MemorySegment prodI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, prodI);
        
        NativeSafe.invoke(MPFR_INIT2, zp1R, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, zm1R, c_long(prec));
        NativeSafe.invoke(MPFR_SET_UI, zp1R, 1L, 0);
        NativeSafe.invoke(MPFR_ADD, zp1R, aR, zp1R, 0);
        NativeSafe.invoke(MPFR_SET_UI, zm1R, 1L, 0);
        NativeSafe.invoke(MPFR_SUB, zm1R, aR, zm1R, 0);
        
        NativeSafe.invoke(MPFR_INIT2, s1R, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, s1I, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, s2R, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, s2I, c_long(prec));
        complexSqrt(s1R, s1I, zp1R, aI, prec, arena, tracker);
        complexSqrt(s2R, s2I, zm1R, aI, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_INIT2, prodR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, prodI, c_long(prec));
        complexMultiply(prodR, prodI, s1R, s1I, s2R, s2I, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_ADD, prodR, aR, prodR, 0);
        NativeSafe.invoke(MPFR_ADD, prodI, aI, prodI, 0);
        complexLog(resR, resI, prodR, prodI, prec, arena, tracker);
    }

    public static void complexAtanh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment p1R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, p1R);
        MemorySegment m1R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, m1R);
        MemorySegment m1I = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, m1I);
        MemorySegment divR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, divR);
        MemorySegment divI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, divI);
        MemorySegment half = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, half);
        
        NativeSafe.invoke(MPFR_INIT2, p1R, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, m1R, c_long(prec));
        NativeSafe.invoke(MPFR_SET_UI, p1R, 1L, 0);
        NativeSafe.invoke(MPFR_ADD, p1R, p1R, aR, 0);
        NativeSafe.invoke(MPFR_SET_UI, m1R, 1L, 0);
        NativeSafe.invoke(MPFR_SUB, m1R, m1R, aR, 0);
        
        NativeSafe.invoke(MPFR_INIT2, m1I, c_long(prec));
        NativeSafe.invoke(MPFR_NEG, m1I, aI, 0);
        
        NativeSafe.invoke(MPFR_INIT2, divR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, divI, c_long(prec));
        complexDivide(divR, divI, p1R, aI, m1R, m1I, prec, arena, tracker);
        complexLog(resR, resI, divR, divI, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_INIT2, half, c_long(prec));
        NativeSafe.invoke(MPFR_SET_D, half, 0.5, 0);
        NativeSafe.invoke(MPFR_MUL, resR, resR, half, 0);
        NativeSafe.invoke(MPFR_MUL, resI, resI, half, 0);
    }

    public static void complexCbrt(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment logR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, logR);
        MemorySegment logI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, logI);
        MemorySegment three = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, three);
        NativeSafe.invoke(MPFR_INIT2, logR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, logI, c_long(prec));
        complexLog(logR, logI, aR, aI, prec, arena, tracker);
        
        NativeSafe.invoke(MPFR_INIT2, three, c_long(prec));
        NativeSafe.invoke(MPFR_SET_UI, three, 3L, 0);
        NativeSafe.invoke(MPFR_DIV, logR, logR, three, 0);
        NativeSafe.invoke(MPFR_DIV, logI, logI, three, 0);
        complexExp(resR, resI, logR, logI, prec, arena, tracker);
    }

    public static void complexPow(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment eR, MemorySegment eI, long prec, Arena arena, ResourceTracker tracker) {
        MemorySegment logR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, logR);
        MemorySegment logI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, logI);
        NativeSafe.invoke(MPFR_INIT2, logR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, logI, c_long(prec));
        complexLog(logR, logI, aR, aI, prec, arena, tracker);
        
        MemorySegment wLogR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, wLogR);
        MemorySegment wLogI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, wLogI);
        NativeSafe.invoke(MPFR_INIT2, wLogR, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, wLogI, c_long(prec));
        complexMultiply(wLogR, wLogI, eR, eI, logR, logI, prec, arena, tracker);
        
        complexExp(resR, resI, wLogR, wLogI, prec, arena, tracker);
    }
    private void checkDimensionsAdd(Matrix<?> a, Matrix<?> b) {
        if (a.rows() != b.rows() || a.cols() != b.cols()) throw new IllegalArgumentException("Matrix dimensions do not match");
    }

    private void checkDimensionsDot(Vector<?> a, Vector<?> b) {
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Vector dimensions do not match");
    }

    private void checkSquare(Matrix<?> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
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

    public static void invokeComplexOp(String function, MemorySegment rcR, MemorySegment rcI, MemorySegment raR, MemorySegment raI, long prec, Arena arena, ResourceTracker tracker) {
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


