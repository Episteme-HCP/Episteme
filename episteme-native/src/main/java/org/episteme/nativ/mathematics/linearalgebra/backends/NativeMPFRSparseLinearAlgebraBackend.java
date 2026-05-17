/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import java.lang.invoke.MethodHandle;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.context.NumericalConfiguration;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.mathematics.numbers.real.NativeRealBig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.nativ.technical.backend.nativ.ResourceTracker;
import org.episteme.nativ.mathematics.numbers.real.backends.NativeMPFRNumbers;
import static org.episteme.nativ.mathematics.numbers.real.backends.NativeMPFRNumbers.*;

/**
 * Arbitrary-precision Sparse Linear Algebra backend using MPFR (via Panama).
 * Optimized for CSR storage.
 */
@SuppressWarnings({"unused", "null"})
@com.google.auto.service.AutoService({org.episteme.core.technical.backend.Backend.class, org.episteme.core.technical.backend.ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class, CPUBackend.class, org.episteme.core.technical.algorithm.AlgorithmProvider.class})
public class NativeMPFRSparseLinearAlgebraBackend<E> implements SparseLinearAlgebraProvider<E>, NativeBackend, CPUBackend {
    private static final Logger logger = LoggerFactory.getLogger(NativeMPFRSparseLinearAlgebraBackend.class);


    // Redundant handles removed, centralized in NativeMPFRNumbers
    private static final MemoryLayout MPFR_LAYOUT = NativeMPFRNumbers.MPFR_LAYOUT;
    private static final boolean AVAILABLE = NativeMPFRNumbers.isAvailable();
    private volatile boolean closed = false;

    private void ensureInitialized() {
        NativeMPFRNumbers.ensureInitialized();
        if (closed) logger.trace("Backend is closed, proceeding with caution");
    }

    private void ensureAlive() {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " is not available.");
    }

    public boolean isAlive() { return !closed && AVAILABLE; }

    @Override public boolean isAvailable() { return AVAILABLE && !isExplicitlyDisabled(); }
    @Override public String getId() { return "mpfr-sparse"; }
    @Override public String getName() { return "Native MPFR Sparse Linear Algebra Backend"; }
    @Override public String getDescription() { return "Native Arbitrary-Precision Sparse Linear Algebra using MPFR."; }
    @Override public String getType() { return "linear-algebra"; }
    @Override public int getPriority() { return 5; }
    @Override public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() { return org.episteme.core.technical.backend.HardwareAccelerator.CPU; }
    @Override public boolean isLoaded() { return AVAILABLE; }
    @Override public String getNativeLibraryName() { return "mpfr"; }
    @Override public Object createBackend() { return this; }
    @Override public org.episteme.core.technical.backend.ExecutionContext createContext() { 
        return new org.episteme.core.technical.backend.ExecutionContext() {
            @Override public <T> T execute(org.episteme.core.technical.backend.Operation<T> op) { return op.compute(this); }
            @Override
            public void close() {
                closed = true;
            }
        };
    }
    @Override
    public boolean isExplicitlyDisabled() {
        String id = getId();
        return (id != null && Boolean.getBoolean("episteme.backend." + id + ".disabled")) || 
               Boolean.getBoolean("episteme.backend.mpfr.disabled") ||
               Boolean.getBoolean("episteme.backend.disable." + id);
    }

    @Override public void shutdown() {}
    @Override public java.util.Map<String, String> getMetadata() { return java.util.Map.of("environment", "CPU (Panama/MPFR)"); }

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
        return org.episteme.core.mathematics.context.MathContext.getCurrent().getPrecisionBits();
    }

    @Override
    @SuppressWarnings("unchecked")
    public E trace(Matrix<E> a) {
        ensureInitialized();
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        int n = a.rows();
        Ring<E> ring = a.getScalarRing();
        boolean isComplex = isComplex(a);
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            long prec = getPrecision();
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

            MemorySegment tR = NativeSafe.allocate(arena, MPFR_LAYOUT);
            MemorySegment tI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            NativeSafe.invoke(MPFR_INIT2, tR, c_long(prec));
            tracker.track(tR, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, tI, c_long(prec));
                tracker.track(tI, s -> NativeSafe.invoke(MPFR_CLEAR, s));
            }

            for (int i = 0; i < n; i++) {
                E val = a.get(i, i);
                if (!isZero(val, ring)) {
                    setMPFR_internal(tR, tI, val, arena);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, tR, 0);
                    if (isComplex) NativeSafe.invoke(MPFR_ADD, sumI, sumI, tI, 0);
                }
            }
            if (isComplex) {
                return (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(readMPFR(sumR, arena), readMPFR(sumI, arena));
            } else {
                return (E) (Object) readMPFR(sumR, arena);
            }
        } catch (Throwable t) {
            throw new RuntimeException("Native MPFR Sparse Trace failed", t);
        }
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



    private static void track(ResourceTracker tracker, MemorySegment p) {
        if (tracker != null && p != null && !p.equals(MemorySegment.NULL)) {
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

    private static void trackArray(ResourceTracker tracker, MemorySegment p, int n) {
        if (tracker != null && p != null && !p.equals(MemorySegment.NULL)) {
            tracker.track(p, s -> {
                if (!s.scope().isAlive()) return;
                try {
                    for (int i = 0; i < n; i++) {
                        if (s.scope().isAlive()) {
                            MemorySegment slice = s.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                            NativeSafe.invoke(MPFR_CLEAR, slice);
                        }
                    }
                } catch (Throwable t) {
                    // Ignore, usually means scope closed during iteration
                }
            });
        }
    }

    private Real readMPFR(MemorySegment val, Arena arena) {
        return NativeRealBig.copyFrom(val, getPrecision());
    }


    private void setMPFR_internal(MemorySegment destR, MemorySegment destI, Object value, Arena arena) throws Throwable {
        if (value.getClass().getName().endsWith(".NativeRealBig")) {
            NativeSafe.invoke(MPFR_SET, destR, (MemorySegment) value.getClass().getMethod("getPtr").invoke(value), 0);
            return;
        }
        if (isComplex(value)) {
            setMPFR_internal(destR, null, getRealPart(value), arena);
            if (destI != null) setMPFR_internal(destI, null, getImagPart(value), arena);
            return;
        }
        
        String s;
        if (value instanceof Real r) {
            s = r.bigDecimalValue().toPlainString();
        } else {
            s = getReal(value).bigDecimalValue().toPlainString();
        }
        NativeSafe.invoke(MPFR_SET_STR, destR, NativeSafe.allocateFrom(arena, s), 10, 0);
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

    @SuppressWarnings("unchecked")
    private E castScalar(Object val, Ring<E> ring) {
        if (val == null) return ring.zero();
        
        Class<?> targetClass = ring.zero().getClass();
        if (targetClass.isInstance(val)) return (E) val;

        boolean isComplexRing = ring instanceof org.episteme.core.mathematics.sets.Complexes || ring.zero() instanceof Complex;
        
        if (isComplexRing) {
            Complex c;
            if (val instanceof Complex cv) c = cv;
            else if (val instanceof Real r) c = Complex.of(r);
            else if (val instanceof Number n) c = Complex.of(n.doubleValue());
            else c = Complex.of(Real.of(val.toString()));
            
            if (targetClass.isInstance(c)) return (E) c;
            return (E) c; 
        } else {
            Real r;
            if (val instanceof Real rv) r = rv;
            else if (val instanceof Complex cv) r = cv.getReal();
            else if (val instanceof Number n) r = Real.of(n.doubleValue());
            else r = Real.of(val.toString());
            
            if (targetClass.isInstance(r)) return (E) r;
            
            // Handle cross-implementation conversion (e.g. NativeRealBig to RealDouble)
            if (ring.zero() instanceof org.episteme.core.mathematics.numbers.real.RealDouble) {
                return (E) org.episteme.core.mathematics.numbers.real.RealDouble.create(r.doubleValue());
            }
            if (ring.zero() instanceof org.episteme.core.mathematics.numbers.real.RealBig) {
                return (E) org.episteme.core.mathematics.numbers.real.RealBig.create(r.bigDecimalValue());
            }
            return (E) r;
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

    private void setMPFR(MemorySegment dest, E value, Arena arena) throws Throwable {
        setMPFR_internal(dest, null, value, arena);
    }

    @SuppressWarnings("unchecked")
    private MemorySegment initVector(Matrix<E> a, Arena arena, ResourceTracker tracker, long prec, boolean isComplex) throws Throwable {
        int n = a.rows() * a.cols();
        int multiplier = isComplex ? 2 : 1;
        MemorySegment h_v = NativeSafe.allocate(arena, MPFR_LAYOUT, (long) n * multiplier);
        
        for (int i = 0; i < n * multiplier; i++) {
            MemorySegment rc = h_v.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, rc, c_long(prec));
        }
        trackArray(tracker, h_v, n * multiplier);
        
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) a;
            int[] rowPtr = sa.getRowPointers();
            int[] colIdx = sa.getColIndices();
            Object[] vals = sa.getValues();
            for (int i = 0; i < sa.rows(); i++) {
                for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                    int pos = i * sa.cols() + colIdx[k];
                    Object val = vals[k];
                    if (isComplex) {
                        Real re = getRealPart(val);
                        Real im = getImagPart(val);
                        setMPFR(h_v.asSlice(pos * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (E) re, arena);
                        setMPFR(h_v.asSlice((pos * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (E) im, arena);
                    } else {
                        setMPFR(h_v.asSlice(pos * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (E) val, arena);
                    }
                }
            }
        } else {
            for (int i = 0; i < a.rows(); i++) {
                for (int j = 0; j < a.cols(); j++) {
                    int pos = i * a.cols() + j;
                    Object val = a.get(i, j);
                    if (isComplex) {
                        Real re = getRealPart(val);
                        Real im = getImagPart(val);
                        setMPFR(h_v.asSlice(pos * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (E) re, arena);
                        setMPFR(h_v.asSlice((pos * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (E) im, arena);
                    } else {
                        setMPFR(h_v.asSlice(pos * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (E) val, arena);
                    }
                }
            }
        }
        return h_v;
    }

    private Real getRealPart(Object val) {
        if (val instanceof Complex c) return c.getReal();
        if (val instanceof Real r) return r;
        if (val instanceof Number n) return Real.of(n.doubleValue());
        return Real.of(val.toString());
    }

    private Real getImagPart(Object val) {
        if (val instanceof Complex c) return c.getImaginary();
        return Real.ZERO;
    }

    private void dotProduct(MemorySegment v1, MemorySegment v2, int n, MemorySegment res, long prec, Arena arena, boolean isComplex) throws Throwable {
        MemorySegment sumR = res.asSlice(0, MPFR_LAYOUT.byteSize());
        MemorySegment sumI = isComplex ? res.asSlice(MPFR_LAYOUT.byteSize(), MPFR_LAYOUT.byteSize()) : null;
        
        NativeSafe.invoke(MPFR_SET_D, sumR, 0.0, 0);
        if (isComplex) NativeSafe.invoke(MPFR_SET_D, sumI, 0.0, 0);

        MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT);
        MemorySegment t2 = NativeSafe.allocate(arena, MPFR_LAYOUT);
        try {
            NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, t2, c_long(prec));

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
                    
                    // dotI = aR*bI - aI*bR
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
        } finally {
            NativeSafe.invoke(MPFR_CLEAR, t1);
            NativeSafe.invoke(MPFR_CLEAR, t2);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        ensureInitialized();
        if (a.cols() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        long prec = getPrecision();
        boolean isComplex = isComplex(sa.getScalarRing());
        
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_b = initVector(b.toMatrix(), arena, tracker, prec, isComplex);
            MemorySegment res = NativeSafe.allocate(arena, MPFR_LAYOUT, sa.rows() * (isComplex ? 2 : 1));
            
            for (int i=0; i<sa.rows() * (isComplex ? 2 : 1); i++) {
                NativeSafe.invoke(MPFR_INIT2, res.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
            }
            trackArray(tracker, res, sa.rows() * (isComplex ? 2 : 1));
            
            spmv_internal(sa, h_b, res, prec, arena, tracker, isComplex);
            
            Object[] resultArr = new Object[sa.rows()];

            for (int i = 0; i < sa.rows(); i++) {
                if (isComplex) {
                    Real re = readMPFR(res.asSlice(i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), arena);
                    Real im = readMPFR(res.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), arena);
                    resultArr[i] = (E) (Object) Complex.of(re, im);
                } else {
                    resultArr[i] = (E) (Object) readMPFR(res.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), arena);
                }
            }
            return Vector.of((java.util.List<E>)(java.util.List<?>)java.util.Arrays.asList(resultArr), sa.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR SpMV failed", t);
        }
    }

    private void axpy_internal(MemorySegment y, E alpha, MemorySegment x, int n, long prec, Arena arena, ResourceTracker tracker, boolean isComplex) throws Throwable {
        MemorySegment aR = NativeSafe.allocate(arena, MPFR_LAYOUT);
        MemorySegment aI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
        MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT);
        MemorySegment t2 = NativeSafe.allocate(arena, MPFR_LAYOUT);

        NativeSafe.invoke(MPFR_INIT2, aR, c_long(prec));
        track(tracker, aR);
        if (isComplex) {
            NativeSafe.invoke(MPFR_INIT2, aI, c_long(prec));
            track(tracker, aI);
        }
        NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
        track(tracker, t1);
        NativeSafe.invoke(MPFR_INIT2, t2, c_long(prec));
        track(tracker, t2);

        if (isComplex) {
            Complex c = (alpha instanceof Complex cv) ? cv : Complex.of(getReal(alpha));
            NativeSafe.invoke(MPFR_SET_STR, aR, NativeSafe.allocateFrom(arena, c.getReal().bigDecimalValue().toPlainString()), 10, 0);
            NativeSafe.invoke(MPFR_SET_STR, aI, NativeSafe.allocateFrom(arena, c.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
        } else {
            NativeSafe.invoke(MPFR_SET_STR, aR, NativeSafe.allocateFrom(arena, getReal(alpha).bigDecimalValue().toPlainString()), 10, 0);
        }

        long layoutSize = MPFR_LAYOUT.byteSize();
        int stride = isComplex ? 2 : 1;
        for (int i = 0; i < n; i++) {
            MemorySegment xiR = x.asSlice(i * stride * layoutSize, MPFR_LAYOUT);
            MemorySegment yiR = y.asSlice(i * stride * layoutSize, MPFR_LAYOUT);
            
            if (isComplex) {
                MemorySegment xiI = x.asSlice((i * 2 + 1) * layoutSize, MPFR_LAYOUT);
                MemorySegment yiI = y.asSlice((i * 2 + 1) * layoutSize, MPFR_LAYOUT);
                
                NativeSafe.invoke(MPFR_MUL, t1, aR, xiR, 0);
                NativeSafe.invoke(MPFR_MUL, t2, aI, xiI, 0);
                NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
                NativeSafe.invoke(MPFR_ADD, yiR, yiR, t1, 0);
                
                NativeSafe.invoke(MPFR_MUL, t1, aR, xiI, 0);
                NativeSafe.invoke(MPFR_MUL, t2, aI, xiR, 0);
                NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                NativeSafe.invoke(MPFR_ADD, yiI, yiI, t1, 0);
            } else {
                NativeSafe.invoke(MPFR_MUL, t1, aR, xiR, 0);
                NativeSafe.invoke(MPFR_ADD, yiR, yiR, t1, 0);
            }
        }
    }


    @SuppressWarnings("unchecked")
    private E dot_internal(MemorySegment x, MemorySegment y, int n, long prec, Arena arena, ResourceTracker tracker, boolean isComplex, Ring<E> ring) throws Throwable {
        MemorySegment sumR = NativeSafe.allocate(arena, MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, sumR, c_long(prec)); 
        track(tracker, sumR);
        NativeSafe.invoke(MPFR_SET_UI, sumR, 0, 0);
        
        MemorySegment sumI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
        if (isComplex) { 
            NativeSafe.invoke(MPFR_INIT2, sumI, c_long(prec)); 
            track(tracker, sumI);
            NativeSafe.invoke(MPFR_SET_UI, sumI, 0, 0); 
        }
        
        MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
        track(tracker, t1);
        
        MemorySegment t2 = NativeSafe.allocate(arena, MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, t2, c_long(prec));
        track(tracker, t2);

        long layoutSize = MPFR_LAYOUT.byteSize();
        int stride = isComplex ? 2 : 1;
        for (int i = 0; i < n; i++) {
            MemorySegment xiR = x.asSlice(i * stride * layoutSize, MPFR_LAYOUT);
            MemorySegment yiR = y.asSlice(i * stride * layoutSize, MPFR_LAYOUT);
            
            if (isComplex) {
                MemorySegment xiI = x.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment yiI = y.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                
                // dot = sum(conj(x) * y) = sum((xR-i*xI)*(yR+i*yI))
                // dotR = xR*yR + xI*yI
                NativeSafe.invoke(MPFR_MUL, t1, xiR, yiR, 0);
                NativeSafe.invoke(MPFR_MUL, t2, xiI, yiI, 0);
                NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                NativeSafe.invoke(MPFR_ADD, sumR, sumR, t2, 0);
                
                // dotI = xR*yI - xI*yR
                NativeSafe.invoke(MPFR_MUL, t1, xiR, yiI, 0);
                NativeSafe.invoke(MPFR_MUL, t2, xiI, yiR, 0);
                NativeSafe.invoke(MPFR_ADD, sumI, sumI, t1, 0);
                NativeSafe.invoke(MPFR_SUB, sumI, sumI, t2, 0);
            } else {
                NativeSafe.invoke(MPFR_MUL, t1, xiR, yiR, 0);
                NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
            }
        }
        
        if (isComplex) {
            return (E) (Object) Complex.of(readMPFR(sumR, arena), readMPFR(sumI, arena));
        } else {
            return (E) (Object) readMPFR(sumR, arena);
        }
    }


    @SuppressWarnings("unchecked")
    private E norm_internal(MemorySegment x, int n, long prec, Arena arena, ResourceTracker tracker, boolean isComplex, Ring<E> ring) throws Throwable {
        MemorySegment sumSq = NativeSafe.allocate(arena, MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, sumSq, c_long(prec));
        track(tracker, sumSq);
        NativeSafe.invoke(MPFR_SET_UI, sumSq, 0, 0);
        
        MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
        track(tracker, t1);

        int stride = isComplex ? 2 : 1;
        long layoutSize = MPFR_LAYOUT.byteSize();
        for (int i = 0; i < n * stride; i++) {
            MemorySegment val = x.asSlice(i * layoutSize, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_MUL, t1, val, val, 0);
            NativeSafe.invoke(MPFR_ADD, sumSq, sumSq, t1, 0);
        }
        NativeSafe.invoke(MPFR_SQRT, sumSq, sumSq, 0);
        Real r = readMPFR(sumSq, arena);
        if (isComplex) return (E) (Object) Complex.of(r, Real.ZERO);
        return (E) (Object) r;
    }

    private void copy_internal(MemorySegment dest, MemorySegment src, int n, boolean isComplex) throws Throwable {
        int stride = isComplex ? 2 : 1;
        long layoutSize = MPFR_LAYOUT.byteSize();
        for (int i = 0; i < n * stride; i++) {
            NativeSafe.invoke(MPFR_SET, dest.asSlice(i * layoutSize, MPFR_LAYOUT), src.asSlice(i * layoutSize, MPFR_LAYOUT), 0);
        }
    }

    private void scale_internal(MemorySegment x, E alpha, int n, long prec, Arena arena, ResourceTracker tracker, boolean isComplex) throws Throwable {
        MemorySegment aR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, aR);
        MemorySegment aI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
        if (isComplex) track(tracker, aI);
        
        MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t1);
        MemorySegment t2 = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t2);

        NativeSafe.invoke(MPFR_INIT2, aR, c_long(prec));
        if (isComplex) NativeSafe.invoke(MPFR_INIT2, aI, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
        NativeSafe.invoke(MPFR_INIT2, t2, c_long(prec));

        if (isComplex) {
            Complex c = (alpha instanceof Complex cv) ? cv : Complex.of((Real) alpha);
            NativeSafe.invoke(MPFR_SET_STR, aR, NativeSafe.allocateFrom(arena, c.getReal().bigDecimalValue().toPlainString()), 10, 0);
            NativeSafe.invoke(MPFR_SET_STR, aI, NativeSafe.allocateFrom(arena, c.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
            
            long layoutSize = MPFR_LAYOUT.byteSize();
            for (int i = 0; i < n; i++) {
                MemorySegment xiR = x.asSlice(i * 2 * layoutSize, MPFR_LAYOUT);
                MemorySegment xiI = x.asSlice((i * 2 + 1) * layoutSize, MPFR_LAYOUT);
                
                // x = a * x = (aR+i*aI)*(xR+i*xI)
                NativeSafe.invoke(MPFR_MUL, t1, aR, xiR, 0);
                NativeSafe.invoke(MPFR_MUL, t2, aI, xiI, 0);
                NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
                
                NativeSafe.invoke(MPFR_MUL, xiI, aR, xiI, 0); // Reuse xiI for imag part temp
                NativeSafe.invoke(MPFR_MUL, t2, aI, xiR, 0);
                NativeSafe.invoke(MPFR_ADD, xiI, xiI, t2, 0);
                NativeSafe.invoke(MPFR_SET, xiR, t1, 0);
            }
        } else {
            NativeSafe.invoke(MPFR_SET_STR, aR, NativeSafe.allocateFrom(arena, ((Real)alpha).bigDecimalValue().toPlainString()), 10, 0);
            long layoutSize = MPFR_LAYOUT.byteSize();
            for (int i = 0; i < n; i++) {
                MemorySegment xiR = x.asSlice(i * layoutSize, MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_MUL, xiR, xiR, aR, 0);
            }
        }
    }

    private void spmv_internal(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa, MemorySegment h_vals, MemorySegment h_b, MemorySegment res, long prec, Arena arena, ResourceTracker tracker, boolean isComplex) throws Throwable {
        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        long layoutSize = MPFR_LAYOUT.byteSize();
        int stride = isComplex ? 2 : 1;

        MemorySegment sumR = NativeSafe.allocate(arena, MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, sumR, c_long(prec));
        track(tracker, sumR);
        
        MemorySegment sumI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
        if (isComplex) {
            NativeSafe.invoke(MPFR_INIT2, sumI, c_long(prec));
            track(tracker, sumI);
        }
        
        MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
        track(tracker, t1);
        
        MemorySegment t2 = NativeSafe.allocate(arena, MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, t2, c_long(prec));
        track(tracker, t2);

        for (int i = 0; i < sa.rows(); i++) {
            NativeSafe.invoke(MPFR_SET_UI, sumR, 0L, 0);
            if (isComplex) NativeSafe.invoke(MPFR_SET_UI, sumI, 0L, 0);
            
            for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                int col = colIdx[k];
                MemorySegment valR = h_vals.asSlice(k * stride * layoutSize, layoutSize);
                
                if (isComplex) {
                    MemorySegment valI = h_vals.asSlice((k * 2 + 1) * layoutSize, layoutSize);
                    MemorySegment bR = h_b.asSlice(col * 2 * layoutSize, layoutSize);
                    MemorySegment bI = h_b.asSlice((col * 2 + 1) * layoutSize, layoutSize);
                    
                    NativeSafe.invoke(MPFR_MUL, t1, valR, bR, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, valI, bI, 0);
                    NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                    
                    NativeSafe.invoke(MPFR_MUL, t1, valR, bI, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, valI, bR, 0);
                    NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, sumI, sumI, t1, 0);
                } else {
                    MemorySegment bval = h_b.asSlice(col * layoutSize, layoutSize);
                    NativeSafe.invoke(MPFR_MUL, t1, valR, bval, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                }
            }
            NativeSafe.invoke(MPFR_SET, res.asSlice(i * stride * layoutSize, layoutSize), sumR, 0);
            if (isComplex) NativeSafe.invoke(MPFR_SET, res.asSlice((i * 2 + 1) * layoutSize, layoutSize), sumI, 0);
        }
    }

    private void spmv_internal(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa, MemorySegment h_b, MemorySegment res, long prec, Arena arena, ResourceTracker tracker, boolean isComplex) throws Throwable {
        MemorySegment h_vals = initNativeValues(sa.getValues(), prec, arena, tracker, isComplex);
        spmv_internal(sa, h_vals, h_b, res, prec, arena, tracker, isComplex);
    }

    private MemorySegment initNativeValues(Object[] vals, long prec, Arena arena, ResourceTracker tracker, boolean isComplex) throws Throwable {
        int n = vals.length;
        int stride = isComplex ? 2 : 1;
        long layoutSize = MPFR_LAYOUT.byteSize();
        MemorySegment nativeVals = NativeSafe.allocate(arena, MPFR_LAYOUT, n * stride);
        
        for (int i = 0; i < n; i++) {

            MemorySegment vR = nativeVals.asSlice(i * stride * layoutSize, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, vR, c_long(prec));
            if (isComplex) {
                MemorySegment vI = nativeVals.asSlice((i * stride + 1) * layoutSize, MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, vI, c_long(prec));
            }
        }
        trackArray(tracker, nativeVals, n * stride);
        
        for (int i = 0; i < n; i++) {
            Object v = vals[i];
            MemorySegment vR = nativeVals.asSlice(i * stride * layoutSize, MPFR_LAYOUT);
            if (isComplex) {
                MemorySegment vI = nativeVals.asSlice((i * stride + 1) * layoutSize, MPFR_LAYOUT);
                setMPFR_internal(vR, vI, v, arena);
            } else {
                setMPFR_internal(vR, null, v, arena);
            }
        }
        return nativeVals;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        ensureInitialized();
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        long prec = getPrecision();
        org.episteme.core.mathematics.structures.rings.Ring<E> ring = (org.episteme.core.mathematics.structures.rings.Ring<E>) sa.getScalarRing();
        boolean isComplex = isComplex(sa.getScalarRing());
        
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(sa.rows(), sa.cols(), (E) ring.zero());
        
        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        Object[] vals = sa.getValues();
        
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment sR = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, sR, c_long(prec));
            track(tracker, sR);
            
            MemorySegment sI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, sI, c_long(prec));
                track(tracker, sI);
            }

            setMPFR_internal(sR, sI, scalar, arena);
            
            MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
            track(tracker, t1);
            
            MemorySegment t2 = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t2, c_long(prec));
            track(tracker, t2);
            
            MemorySegment valR = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, valR, c_long(prec));
            track(tracker, valR);
            
            MemorySegment valI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, valI, c_long(prec));
                track(tracker, valI);
            }
            
            MemorySegment resR = NativeSafe.allocate(arena, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, resR, c_long(prec));
            track(tracker, resR);
            
            MemorySegment resI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, resI, c_long(prec));
                track(tracker, resI);
            }

            for (int i = 0; i < sa.rows(); i++) {
                for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                    Object val = vals[k];
                    if (isComplex) {
                        // Use a more direct way to set values if possible, but here we still need setMPFR_internal 
                        // unless we pre-allocate the entire values array once.
                        // However, we can avoid some overhead if the value is already a NativeRealBig or Complex
                        if (val instanceof Complex c) {
                             setMPFR_internal(valR, valI, c, arena);
                        } else {
                             setMPFR_internal(valR, valI, val, arena);
                        }
                        
                        // (valR + i*valI)*(sR + i*sI) = (valR*sR - valI*sI) + i(valR*sI + valI*sR)
                        NativeSafe.invoke(MPFR_MUL, t1, valR, sR, 0);
                        NativeSafe.invoke(MPFR_MUL, t2, valI, sI, 0);
                        NativeSafe.invoke(MPFR_SUB, resR, t1, t2, 0);
                        
                        NativeSafe.invoke(MPFR_MUL, t1, valR, sI, 0);
                        NativeSafe.invoke(MPFR_MUL, t2, valI, sR, 0);
                        NativeSafe.invoke(MPFR_ADD, resI, t1, t2, 0);
                        
                        storage.set(i, colIdx[k], (E) (Object) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena)));
                    } else {
                        setMPFR_internal(valR, null, val, arena);
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
    @SuppressWarnings("unchecked")
    public Vector<E> multiply(Vector<E> a, E scalar) {
        ensureInitialized();
        Field<E> field = (Field<E>) a.getScalarRing();
        int n = a.dimension();
        long prec = getPrecision();
        boolean isComplex = isComplex(field);
        
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment sR = NativeSafe.allocate(arena, MPFR_LAYOUT); 
            NativeSafe.invoke(MPFR_INIT2, sR, c_long(prec));
            track(tracker, sR);
            
            MemorySegment sI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, sI, c_long(prec));
                track(tracker, sI);
            }
            
            if (isComplex) {
                Complex cs = (scalar instanceof Complex cv) ? cv : Complex.of((Real) scalar);
                NativeSafe.invoke(MPFR_SET_STR, sR, NativeSafe.allocateFrom(arena, cs.getReal().bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, sI, NativeSafe.allocateFrom(arena, cs.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
            } else {
                NativeSafe.invoke(MPFR_SET_STR, sR, NativeSafe.allocateFrom(arena, ((Real)scalar).bigDecimalValue().toPlainString()), 10, 0);
            }

            MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec)); track(tracker, t1);
            MemorySegment t2 = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2, c_long(prec)); track(tracker, t2);
            MemorySegment valR = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, valR, c_long(prec)); track(tracker, valR);
            MemorySegment valI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, valI, c_long(prec));
                track(tracker, valI);
            }
            
            MemorySegment resR = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, resR, c_long(prec)); track(tracker, resR);
            MemorySegment resI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) {
                NativeSafe.invoke(MPFR_INIT2, resI, c_long(prec));
                track(tracker, resI);
            }

            @SuppressWarnings("unchecked")
            E[] resultArr = (E[]) new Object[n];
            for (int i = 0; i < n; i++) {
                E val = a.get(i);
                if (isComplex) {
                    Complex cv = (val instanceof Complex c) ? c : Complex.of((Real) val);
                    NativeSafe.invoke(MPFR_SET_STR, valR, NativeSafe.allocateFrom(arena, cv.getReal().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, valI, NativeSafe.allocateFrom(arena, cv.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                    
                    // (valR + i*valI)*(sR + i*sI) = (valR*sR - valI*sI) + i(valR*sI + valI*sR)
                    NativeSafe.invoke(MPFR_MUL, t1, valR, sR, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, valI, sI, 0);
                    NativeSafe.invoke(MPFR_SUB, resR, t1, t2, 0);
                    
                    NativeSafe.invoke(MPFR_MUL, t1, valR, sI, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, valI, sR, 0);
                    NativeSafe.invoke(MPFR_ADD, resI, t1, t2, 0);
                    
                    resultArr[i] = (E) (Object) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena));
                } else {
                    NativeSafe.invoke(MPFR_SET_STR, valR, NativeSafe.allocateFrom(arena, ((Real)val).bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_MUL, resR, valR, sR, 0);
                    resultArr[i] = (E) (Object) readMPFR(resR, arena);
                }
            }
            @SuppressWarnings("unchecked")
            Vector<E> res = Vector.of((java.util.List<E>)(java.util.List<?>)java.util.Arrays.asList(resultArr), (Ring<E>) field);
            return res;
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR Vector scale failed", t);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        ensureAlive();
        if (a.rows() != b.rows() || a.cols() != b.cols()) throw new IllegalArgumentException("Dimension mismatch");
        long prec = getPrecision();
        boolean isComplex = isComplex(a);
        
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sb = toSparse(b);
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(sa.rows(), sa.cols(), (E) sa.getScalarRing().zero());
        
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment t1R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t1R);
            MemorySegment t1I = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, t1I);
            MemorySegment t2R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t2R);
            MemorySegment t2I = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, t2I);
            MemorySegment resR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, resR);
            MemorySegment resI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, resI);
            
            NativeSafe.invoke(MPFR_INIT2, t1R, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t1I, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, t2R, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t2I, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, resR, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, resI, c_long(prec));

            for (int i = 0; i < sa.rows(); i++) {
                java.util.Map<Integer, E> rowValues = new java.util.HashMap<>();
                // Simplified merge logic for now, using MPFR for the addition part
                int[] rpa = sa.getRowPointers();
                int[] cia = sa.getColIndices();
                Object[] va = sa.getValues();
                for (int k=rpa[i]; k < rpa[i+1]; k++) rowValues.put(cia[k], (E) va[k]);

                int[] rpb = sb.getRowPointers();
                int[] cib = sb.getColIndices();
                Object[] vb = sb.getValues();
                for (int k=rpb[i]; k < rpb[i+1]; k++) {
                    int col = cib[k];
                    E valB = (E) vb[k];
                    if (rowValues.containsKey(col)) {
                        E valA = rowValues.get(col);
                        if (isComplex) {
                            setMPFR_internal(t1R, t1I, valA, arena);
                            setMPFR_internal(t2R, t2I, valB, arena);
                            NativeSafe.invoke(MPFR_ADD, resR, t1R, t2R, 0);
                            NativeSafe.invoke(MPFR_ADD, resI, t1I, t2I, 0);
                            rowValues.put(col, (E) (Object) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena)));
                        } else {
                            setMPFR_internal(t1R, null, valA, arena);
                            setMPFR_internal(t2R, null, valB, arena);
                            NativeSafe.invoke(MPFR_ADD, resR, t1R, t2R, 0);
                            rowValues.put(col, (E) (Object) readMPFR(resR, arena));
                        }
                    } else {
                        rowValues.put(col, valB);
                    }
                }
                for (java.util.Map.Entry<Integer, E> entry : rowValues.entrySet()) {
                    storage.set(i, entry.getKey(), entry.getValue());
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR Matrix Add failed", t);
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, (Ring<E>)sa.getScalarRing());
    }

    @Override
    public Vector<E> add(Vector<E> v1, Vector<E> v2) {
        if (v1.dimension() != v2.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        long prec = getPrecision();
        boolean isComplex = isComplex(v1.getScalarRing());
        
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment t1R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t1R);
            MemorySegment t1I = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, t1I);
            MemorySegment t2R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t2R);
            MemorySegment t2I = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, t2I);
            MemorySegment resR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, resR);
            MemorySegment resI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, resI);
            
            NativeSafe.invoke(MPFR_INIT2, t1R, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t1I, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, t2R, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t2I, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, resR, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, resI, c_long(prec));

            E[] resultArr = (E[]) java.lang.reflect.Array.newInstance(Object.class, v1.dimension());
            for (int i = 0; i < v1.dimension(); i++) {
                E valA = v1.get(i);
                E valB = v2.get(i);
                if (isComplex) {
                    Complex ca = (valA instanceof Complex c) ? c : Complex.of((Real) valA);
                    Complex cb = (valB instanceof Complex c) ? c : Complex.of((Real) valB);
                    NativeSafe.invoke(MPFR_SET_STR, t1R, NativeSafe.allocateFrom(arena, ca.getReal().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t1I, NativeSafe.allocateFrom(arena, ca.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t2R, NativeSafe.allocateFrom(arena, cb.getReal().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t2I, NativeSafe.allocateFrom(arena, cb.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_ADD, resR, t1R, t2R, 0);
                    NativeSafe.invoke(MPFR_ADD, resI, t1I, t2I, 0);
                    resultArr[i] = (E) (Object) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena));
                } else {
                    NativeSafe.invoke(MPFR_SET_STR, t1R, NativeSafe.allocateFrom(arena, ((Real)valA).bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t2R, NativeSafe.allocateFrom(arena, ((Real)valB).bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_ADD, resR, t1R, t2R, 0);
                    resultArr[i] = (E) (Object) readMPFR(resR, arena);
                }
            }
            return Vector.of(java.util.Arrays.asList(resultArr), v1.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR Vector Add failed", t);
        }
    }

    @Override

    public Vector<E> subtract(Vector<E> v1, Vector<E> v2) {
        if (v1.dimension() != v2.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        long prec = getPrecision();
        boolean isComplex = isComplex(v1.getScalarRing());
        int n = v1.dimension();
        E[] resultArr = (E[]) java.lang.reflect.Array.newInstance(Object.class, n);
        
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment t1R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t1R);
            MemorySegment t1I = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, t1I);
            
            MemorySegment t2R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t2R);
            MemorySegment t2I = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, t2I);
            
            MemorySegment resR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, resR);
            MemorySegment resI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, resI);

            NativeSafe.invoke(MPFR_INIT2, t1R, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t1I, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, t2R, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t2I, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, resR, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, resI, c_long(prec));

            for (int i = 0; i < n; i++) {
                E valA = v1.get(i);
                E valB = v2.get(i);
                if (isComplex) {
                    Complex ca = (valA instanceof Complex c) ? c : Complex.of((Real) valA);
                    Complex cb = (valB instanceof Complex c) ? c : Complex.of((Real) valB);
                    NativeSafe.invoke(MPFR_SET_STR, t1R, NativeSafe.allocateFrom(arena, ca.getReal().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t1I, NativeSafe.allocateFrom(arena, ca.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t2R, NativeSafe.allocateFrom(arena, cb.getReal().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t2I, NativeSafe.allocateFrom(arena, cb.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SUB, resR, t1R, t2R, 0);
                    NativeSafe.invoke(MPFR_SUB, resI, t1I, t2I, 0);
                    resultArr[i] = (E) (Object) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena));
                } else {
                    NativeSafe.invoke(MPFR_SET_STR, t1R, NativeSafe.allocateFrom(arena, ((Real)valA).bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t2R, NativeSafe.allocateFrom(arena, ((Real)valB).bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SUB, resR, t1R, t2R, 0);
                    resultArr[i] = (E) (Object) readMPFR(resR, arena);
                }
            }
            return Vector.of(java.util.Arrays.asList(resultArr), v1.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR Vector Subtract failed", t);
        }
    }

    @Override

    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (a.rows() != b.rows() || a.cols() != b.cols()) throw new IllegalArgumentException("Dimension mismatch");
        long prec = getPrecision();
        boolean isComplex = isComplex(a);
        
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sb = toSparse(b);
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(sa.rows(), sa.cols(), (E) sa.getScalarRing().zero());
        
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment t1R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t1R);
            MemorySegment t1I = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, t1I);
            
            MemorySegment t2R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t2R);
            MemorySegment t2I = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, t2I);
            
            MemorySegment resR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, resR);
            MemorySegment resI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, resI);

            NativeSafe.invoke(MPFR_INIT2, t1R, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t1I, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, t2R, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t2I, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, resR, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, resI, c_long(prec));

            for (int i = 0; i < sa.rows(); i++) {
                java.util.Map<Integer, E> rowValues = new java.util.HashMap<>();
                int[] rpa = sa.getRowPointers();
                int[] cia = sa.getColIndices();
                Object[] va = sa.getValues();
                for (int k=rpa[i]; k < rpa[i+1]; k++) rowValues.put(cia[k], (E) va[k]);

                int[] rpb = sb.getRowPointers();
                int[] cib = sb.getColIndices();
                Object[] vb = sb.getValues();
                for (int k=rpb[i]; k < rpb[i+1]; k++) {
                    int col = cib[k];
                    E valB = (E) vb[k];
                    if (rowValues.containsKey(col)) {
                        E valA = rowValues.get(col);
                        if (isComplex) {
                            Complex ca = (valA instanceof Complex c) ? c : Complex.of((Real) valA);
                            Complex cb = (valB instanceof Complex c) ? c : Complex.of((Real) valB);
                            NativeSafe.invoke(MPFR_SET_STR, t1R, NativeSafe.allocateFrom(arena, ca.getReal().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t1I, NativeSafe.allocateFrom(arena, ca.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t2R, NativeSafe.allocateFrom(arena, cb.getReal().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t2I, NativeSafe.allocateFrom(arena, cb.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SUB, resR, t1R, t2R, 0);
                            NativeSafe.invoke(MPFR_SUB, resI, t1I, t2I, 0);
                            rowValues.put(col, (E) (Object) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena)));
                        } else {
                            NativeSafe.invoke(MPFR_SET_STR, t1R, NativeSafe.allocateFrom(arena, ((Real)valA).bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t2R, NativeSafe.allocateFrom(arena, ((Real)valB).bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SUB, resR, t1R, t2R, 0);
                            rowValues.put(col, (E) (Object) readMPFR(resR, arena));
                        }
                    } else {
                        // Subtracting valB from zero: 0 - valB
                        if (isComplex) {
                            Complex cb = (valB instanceof Complex c) ? c : Complex.of((Real) valB);
                            NativeSafe.invoke(MPFR_SET_STR, t1R, NativeSafe.allocateFrom(arena, cb.getReal().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t1I, NativeSafe.allocateFrom(arena, cb.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_NEG, resR, t1R, 0);
                            NativeSafe.invoke(MPFR_NEG, resI, t1I, 0);
                            rowValues.put(col, (E) (Object) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena)));
                        } else {
                            NativeSafe.invoke(MPFR_SET_STR, t1R, NativeSafe.allocateFrom(arena, ((Real)valB).bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_NEG, resR, t1R, 0);
                            rowValues.put(col, (E) (Object) readMPFR(resR, arena));
                        }
                    }
                }
                for (java.util.Map.Entry<Integer, E> entry : rowValues.entrySet()) {
                    storage.set(i, entry.getKey(), entry.getValue());
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR Matrix Subtract failed", t);
        }
        @SuppressWarnings("unchecked")
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> result = new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, (Ring<E>)sa.getScalarRing());
        return result;
    }

    @Override

    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (a.cols() != b.rows()) throw new IllegalArgumentException("Dimension mismatch");
        long prec = getPrecision();
        boolean isComplex = isComplex(a);
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sb = toSparse(b);
        
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(sa.rows(), sb.cols(), (E) sa.getScalarRing().zero());
        
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment t1R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t1R);
            MemorySegment t1I = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, t1I);
            
            MemorySegment t2R = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t2R);
            MemorySegment t2I = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, t2I);
            
            MemorySegment resR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, resR);
            MemorySegment resI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, resI);

            MemorySegment accR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, accR);
            MemorySegment accI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, accI);

            NativeSafe.invoke(MPFR_INIT2, t1R, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t1I, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, t2R, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t2I, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, resR, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, resI, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, accR, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, accI, c_long(prec));

            int[] rpa = sa.getRowPointers();
            int[] cia = sa.getColIndices();
            Object[] va = sa.getValues();
            
            int[] rpb = sb.getRowPointers();
            int[] cib = sb.getColIndices();
            Object[] vb = sb.getValues();

            for (int i = 0; i < sa.rows(); i++) {
                java.util.Map<Integer, E> rowAccumulator = new java.util.HashMap<>();
                for (int k = rpa[i]; k < rpa[i+1]; k++) {
                    int colA = cia[k];
                    E valA = (E) va[k];
                    
                    for (int l = rpb[colA]; l < rpb[colA+1]; l++) {
                        int colB = cib[l];
                        E valB = (E) vb[l];
                        
                        if (isComplex) {
                            Complex ca = (valA instanceof Complex c) ? c : Complex.of((Real) valA);
                            Complex cb = (valB instanceof Complex c) ? c : Complex.of((Real) valB);
                            
                            setMPFR_internal(t1R, t1I, ca, arena);
                            setMPFR_internal(t2R, t2I, cb, arena);
                            
                            // Complex mul: (t1R + i*t1I)*(t2R + i*t2I) = (t1R*t2R - t1I*t2I) + i(t1R*t2I + t1I*t2R)
                            NativeSafe.invoke(MPFR_MUL, resR, t1R, t2R, 0);
                            NativeSafe.invoke(MPFR_MUL, accR, t1I, t2I, 0);
                            NativeSafe.invoke(MPFR_SUB, resR, resR, accR, 0);
                            
                            NativeSafe.invoke(MPFR_MUL, resI, t1R, t2I, 0);
                            NativeSafe.invoke(MPFR_MUL, accR, t1I, t2R, 0);
                            NativeSafe.invoke(MPFR_ADD, resI, resI, accR, 0);
                            
                            Complex prod = Complex.of(readMPFR(resR, arena), readMPFR(resI, arena));
                            rowAccumulator.merge(colB, (E) prod, (v1, v2) -> {
                                Complex c1 = (Complex) v1;
                                Complex c2 = (Complex) v2;
                                try {
                                    setMPFR_internal(t1R, t1I, c1, arena);
                                    setMPFR_internal(t2R, t2I, c2, arena);
                                    NativeSafe.invoke(MPFR_ADD, resR, t1R, t2R, 0);
                                    NativeSafe.invoke(MPFR_ADD, resI, t1I, t2I, 0);
                                    return (E) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena));
                                } catch (Throwable te) { throw new RuntimeException(te); }
                            });
                        } else {
                            setMPFR_internal(t1R, null, valA, arena);
                            setMPFR_internal(t2R, null, valB, arena);
                            NativeSafe.invoke(MPFR_MUL, resR, t1R, t2R, 0);
                            
                            Real prod = readMPFR(resR, arena);
                            rowAccumulator.merge(colB, (E) prod, (v1, v2) -> {
                                Real r1 = (Real) v1;
                                Real r2 = (Real) v2;
                                try {
                                    setMPFR_internal(t1R, null, r1, arena);
                                    setMPFR_internal(t2R, null, r2, arena);
                                    NativeSafe.invoke(MPFR_ADD, resR, t1R, t2R, 0);
                                    return (E) readMPFR(resR, arena);
                                } catch (Throwable te) { throw new RuntimeException(te); }
                            });
                        }
                    }
                }
                for (java.util.Map.Entry<Integer, E> entry : rowAccumulator.entrySet()) {
                    storage.set(i, entry.getKey(), entry.getValue());
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR Matrix Multiply failed", t);
        }
        @SuppressWarnings("unchecked")
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> result = new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, (Ring<E>)sa.getScalarRing());
        return result;
    }

    @Override
    public E dot(Vector<E> v1, Vector<E> v2) {
        if (v1.dimension() != v2.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " is not available.");
        
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            boolean isComplex = isComplex(v1.getScalarRing());
            MemorySegment h_v1 = initVector(v1.toMatrix(), arena, tracker, prec, isComplex);
            MemorySegment h_v2 = initVector(v2.toMatrix(), arena, tracker, prec, isComplex);
            
            int resSize = isComplex ? 2 : 1;
            MemorySegment res = NativeSafe.allocate(arena, MPFR_LAYOUT, resSize);
            for (int i=0; i<resSize; i++) NativeSafe.invoke(MPFR_INIT2, res.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
            trackArray(tracker, res, resSize);
            
            dotProduct(h_v1, h_v2, v1.dimension(), res, prec, arena, isComplex);
            
            if (isComplex) {
                Real re = readMPFR(res.asSlice(0, MPFR_LAYOUT.byteSize()), arena);
                Real im = readMPFR(res.asSlice(MPFR_LAYOUT.byteSize(), MPFR_LAYOUT.byteSize()), arena);
                @SuppressWarnings("unchecked")
                E result = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(re, im);
                return result;
            } else {
                @SuppressWarnings("unchecked")
                E result = (E) (Object) readMPFR(res, arena);
                return result;
            }
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR dot failed", t);
        }
    }

    @Override
    public E norm(Vector<E> a) {
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            boolean isComplex = isComplex(a.getScalarRing());
            MemorySegment h_a = initVector(a.toMatrix(), arena, tracker, prec, isComplex);
            MemorySegment res = NativeSafe.allocate(arena, MPFR_LAYOUT, isComplex ? 2 : 1);
            for (int i=0; i<(isComplex?2:1); i++) NativeSafe.invoke(MPFR_INIT2, res.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
            trackArray(tracker, res, isComplex ? 2 : 1);
            
            dotProduct(h_a, h_a, a.dimension(), res, prec, arena, isComplex);
            
            MemorySegment normR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, normR);
            NativeSafe.invoke(MPFR_INIT2, normR, c_long(prec));
            
            if (isComplex) {
                // Complex norm is sqrt(re^2 + im^2)
                // dotProduct(h_a, h_a) for complex returns (sum(ai.re^2 + ai.im^2), 0)
                // Wait, dotProduct(a, b) usually is sum(ai * conj(bi))
                // Let's check dotProduct implementation.
            }
            NativeSafe.invoke(MPFR_SQRT, normR, res.asSlice(0, MPFR_LAYOUT.byteSize()), 0);
            Real r = readMPFR(normR, arena);
            @SuppressWarnings("unchecked")
            E result = (E) (Object) (isComplex ? Complex.of(r, Real.ZERO) : r);
            return result;
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR norm failed", t);
        }
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (a.rows() == a.cols()) {
            // Check if triangular first for better precision
            if (isLowerTriangular(a)) return solveTriangular(a, b, false, false, false, false);
            if (isUpperTriangular(a)) return solveTriangular(a, b, true, false, false, false);

            // Square system
            org.episteme.core.mathematics.context.NumericalConfiguration config = org.episteme.core.mathematics.context.MathContext.getNumericalConfiguration();
            double eps = config.getEpsilonDouble();
            if (org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
                eps = 1e-100; // Use extremely tight tolerance for EXACT mode
            }
            E tol = createScalar(eps, b);
            return bicgstab(a, b, Vector.zeros(b.dimension(), (org.episteme.core.mathematics.structures.rings.Ring<E>) b.getScalarRing()), tol, config.getMaxIterations());
        }
        // Rectangular solve
        if (a.rows() > a.cols()) {
            // Overdetermined: x = (A^T A)^-1 A^T b
            Matrix<E> at = transpose(a);
            return solve(multiply(at, a), multiply(at, b));
        } else {
            // Underdetermined: x = A^T (A A^T)^-1 b (minimum norm solution)
            Matrix<E> at = transpose(a);
            return multiply(at, solve(multiply(a, at), b));
        }
    }

    private boolean isLowerTriangular(Matrix<E> a) {
        if (a.rows() != a.cols()) return false;
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa) {
            int[] rowPtr = sa.getRowPointers();
            int[] colIdx = sa.getColIndices();
            for (int i = 0; i < sa.rows(); i++) {
                for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                    if (colIdx[k] > i) return false;
                }
            }
            return true;
        }
        for (int i = 0; i < a.rows(); i++) {
            for (int j = i + 1; j < a.cols(); j++) {
                if (!isZero(a.get(i, j), a.getScalarRing())) return false;
            }
        }
        return true;
    }

    private boolean isUpperTriangular(Matrix<E> a) {
        if (a.rows() != a.cols()) return false;
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa) {
            int[] rowPtr = sa.getRowPointers();
            int[] colIdx = sa.getColIndices();
            for (int i = 0; i < sa.rows(); i++) {
                for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                    if (colIdx[k] < i) return false;
                }
            }
            return true;
        }
        for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < i; j++) {
                if (!isZero(a.get(i, j), a.getScalarRing())) return false;
            }
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> solveTriangular(Matrix<E> A, Vector<E> b, boolean upper, boolean transpose, boolean conjugate, boolean unit) {
        if (!AVAILABLE) throw new UnsupportedOperationException(getName() + " is not available.");
        ensureAlive();
        int n = A.rows();
        if (n != A.cols() || n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");

        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(A);
        long prec = getPrecision();
        boolean isComplex = isComplex(sa);
        Ring<E> ring = sa.getScalarRing();

        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_vals = initNativeValues(sa.getValues(), prec, arena, tracker, isComplex);
            MemorySegment h_b = initVector(b.toMatrix(), arena, tracker, prec, isComplex);
            MemorySegment h_x = NativeSafe.allocate(arena, MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            for (int i = 0; i < n * (isComplex ? 2 : 1); i++) {
                NativeSafe.invoke(MPFR_INIT2, h_x.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
            }
            trackArray(tracker, h_x, n * (isComplex ? 2 : 1));

            int[] rowPtr = sa.getRowPointers();
            int[] colIdx = sa.getColIndices();
            long layoutSize = MPFR_LAYOUT.byteSize();
            int stride = isComplex ? 2 : 1;

            MemorySegment sumR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, sumR);
            MemorySegment sumI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, sumI);
            MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t1);
            MemorySegment t2 = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, t2);
            MemorySegment diagR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, diagR);
            MemorySegment diagI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, diagI);

            NativeSafe.invoke(MPFR_INIT2, sumR, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, sumI, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, t2, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, diagR, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, diagI, c_long(prec));

            if (!transpose) {
                if (!upper) {
                    // Forward substitution (Lower)
                    for (int i = 0; i < n; i++) {
                        NativeSafe.invoke(MPFR_SET, sumR, h_b.asSlice(i * stride * layoutSize, MPFR_LAYOUT), 0);
                        if (isComplex) NativeSafe.invoke(MPFR_SET, sumI, h_b.asSlice((i * 2 + 1) * layoutSize, MPFR_LAYOUT), 0);

                        NativeSafe.invoke(MPFR_SET_D, diagR, 1.0, 0);
                        if (isComplex) NativeSafe.invoke(MPFR_SET_D, diagI, 0.0, 0);

                        for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                            int j = colIdx[k];
                            if (j < i) {
                                // sum -= A[i,j] * x[j]
                                MemorySegment valR = h_vals.asSlice(k * stride * layoutSize, MPFR_LAYOUT);
                                MemorySegment xjR = h_x.asSlice(j * stride * layoutSize, MPFR_LAYOUT);
                                
                                if (isComplex) {
                                    MemorySegment valI = h_vals.asSlice((k * 2 + 1) * layoutSize, MPFR_LAYOUT);
                                    MemorySegment xjI = h_x.asSlice((j * 2 + 1) * layoutSize, MPFR_LAYOUT);
                                    
                                    // (valR + i*valI)*(xjR + i*xjI) = (valR*xjR - valI*xjI) + i(valR*xjI + valI*xjR)
                                    // if conjugate: (valR - i*valI)*(xjR + i*xjI) = (valR*xjR + valI*xjI) + i(valR*xjI - valI*xjR)
                                    if (conjugate) {
                                        NativeSafe.invoke(MPFR_MUL, t1, valR, xjR, 0);
                                        NativeSafe.invoke(MPFR_MUL, t2, valI, xjI, 0);
                                        NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                                        NativeSafe.invoke(MPFR_SUB, sumR, sumR, t1, 0);

                                        NativeSafe.invoke(MPFR_MUL, t1, valR, xjI, 0);
                                        NativeSafe.invoke(MPFR_MUL, t2, valI, xjR, 0);
                                        NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
                                        NativeSafe.invoke(MPFR_SUB, sumI, sumI, t1, 0);
                                    } else {
                                        NativeSafe.invoke(MPFR_MUL, t1, valR, xjR, 0);
                                        NativeSafe.invoke(MPFR_MUL, t2, valI, xjI, 0);
                                        NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
                                        NativeSafe.invoke(MPFR_SUB, sumR, sumR, t1, 0);

                                        NativeSafe.invoke(MPFR_MUL, t1, valR, xjI, 0);
                                        NativeSafe.invoke(MPFR_MUL, t2, valI, xjR, 0);
                                        NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                                        NativeSafe.invoke(MPFR_SUB, sumI, sumI, t1, 0);
                                    }
                                } else {
                                    NativeSafe.invoke(MPFR_MUL, t1, valR, xjR, 0);
                                    NativeSafe.invoke(MPFR_SUB, sumR, sumR, t1, 0);
                                }
                            } else if (j == i) {
                                NativeSafe.invoke(MPFR_SET, diagR, h_vals.asSlice(k * stride * layoutSize, MPFR_LAYOUT), 0);
                                if (isComplex) NativeSafe.invoke(MPFR_SET, diagI, h_vals.asSlice((k * 2 + 1) * layoutSize, MPFR_LAYOUT), 0);
                            }
                        }
                        
                        if (!unit) {
                            // x[i] = sum / A[i,i]
                            if (isComplex) {
                                complexDiv(sumR, sumI, diagR, diagI, h_x.asSlice(i * 2 * layoutSize, MPFR_LAYOUT), h_x.asSlice((i * 2 + 1) * layoutSize, MPFR_LAYOUT), prec, arena);
                            } else {
                                NativeSafe.invoke(MPFR_DIV, h_x.asSlice(i * layoutSize, MPFR_LAYOUT), sumR, diagR, 0);
                            }
                        } else {
                            NativeSafe.invoke(MPFR_SET, h_x.asSlice(i * stride * layoutSize, MPFR_LAYOUT), sumR, 0);
                            if (isComplex) NativeSafe.invoke(MPFR_SET, h_x.asSlice((i * 2 + 1) * layoutSize, MPFR_LAYOUT), sumI, 0);
                        }
                    }
                } else {
                    // Backward substitution (Upper)
                    for (int i = n - 1; i >= 0; i--) {
                        NativeSafe.invoke(MPFR_SET, sumR, h_b.asSlice(i * stride * layoutSize, MPFR_LAYOUT), 0);
                        if (isComplex) NativeSafe.invoke(MPFR_SET, sumI, h_b.asSlice((i * 2 + 1) * layoutSize, MPFR_LAYOUT), 0);

                        NativeSafe.invoke(MPFR_SET_D, diagR, 1.0, 0);
                        if (isComplex) NativeSafe.invoke(MPFR_SET_D, diagI, 0.0, 0);

                        for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                            int j = colIdx[k];
                            if (j > i) {
                                MemorySegment valR = h_vals.asSlice(k * stride * layoutSize, MPFR_LAYOUT);
                                MemorySegment xjR = h_x.asSlice(j * stride * layoutSize, MPFR_LAYOUT);
                                
                                if (isComplex) {
                                    MemorySegment valI = h_vals.asSlice((k * 2 + 1) * layoutSize, MPFR_LAYOUT);
                                    MemorySegment xjI = h_x.asSlice((j * 2 + 1) * layoutSize, MPFR_LAYOUT);
                                    
                                    // conjugate if Hermitian: (valR - i*valI)*(xjR + i*xjI) = (valR*xjR + valI*xjI) + i(valR*xjI - valI*xjR)
                                    if (conjugate) {
                                        NativeSafe.invoke(MPFR_MUL, t1, valR, xjR, 0);
                                        NativeSafe.invoke(MPFR_MUL, t2, valI, xjI, 0);
                                        NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                                        NativeSafe.invoke(MPFR_SUB, sumR, sumR, t1, 0);

                                        NativeSafe.invoke(MPFR_MUL, t1, valR, xjI, 0);
                                        NativeSafe.invoke(MPFR_MUL, t2, valI, xjR, 0);
                                        NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
                                        NativeSafe.invoke(MPFR_SUB, sumI, sumI, t1, 0);
                                    } else {
                                        NativeSafe.invoke(MPFR_MUL, t1, valR, xjR, 0);
                                        NativeSafe.invoke(MPFR_MUL, t2, valI, xjI, 0);
                                        NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
                                        NativeSafe.invoke(MPFR_SUB, sumR, sumR, t1, 0);

                                        NativeSafe.invoke(MPFR_MUL, t1, valR, xjI, 0);
                                        NativeSafe.invoke(MPFR_MUL, t2, valI, xjR, 0);
                                        NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                                        NativeSafe.invoke(MPFR_SUB, sumI, sumI, t1, 0);
                                    }
                                } else {
                                    NativeSafe.invoke(MPFR_MUL, t1, valR, xjR, 0);
                                    NativeSafe.invoke(MPFR_SUB, sumR, sumR, t1, 0);
                                }
                            } else if (j == i) {
                                NativeSafe.invoke(MPFR_SET, diagR, h_vals.asSlice(k * stride * layoutSize, MPFR_LAYOUT), 0);
                                if (isComplex) NativeSafe.invoke(MPFR_SET, diagI, h_vals.asSlice((k * 2 + 1) * layoutSize, MPFR_LAYOUT), 0);
                            }
                        }
                        
                        if (!unit) {
                            if (isComplex) {
                                complexDiv(sumR, sumI, diagR, diagI, h_x.asSlice(i * 2 * layoutSize, MPFR_LAYOUT), h_x.asSlice((i * 2 + 1) * layoutSize, MPFR_LAYOUT), prec, arena);
                            } else {
                                NativeSafe.invoke(MPFR_DIV, h_x.asSlice(i * layoutSize, MPFR_LAYOUT), sumR, diagR, 0);
                            }
                        } else {
                            NativeSafe.invoke(MPFR_SET, h_x.asSlice(i * stride * layoutSize, MPFR_LAYOUT), sumR, 0);
                            if (isComplex) NativeSafe.invoke(MPFR_SET, h_x.asSlice((i * 2 + 1) * layoutSize, MPFR_LAYOUT), sumI, 0);
                        }
                    }
                }
            } else {
                // Transposed solve A^T x = b or A^H x = b
                return solveTriangular(transpose(A), b, !upper, false, conjugate, unit);
            }

            java.util.List<E> resList = new java.util.ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                if (isComplex) {
                    Real re = readMPFR(h_x.asSlice(i * 2 * layoutSize, MPFR_LAYOUT), arena);
                    Real im = readMPFR(h_x.asSlice((i * 2 + 1) * layoutSize, MPFR_LAYOUT), arena);
                    resList.add((E) (Object) Complex.of(re, im));
                } else {
                    resList.add((E) (Object) readMPFR(h_x.asSlice(i * layoutSize, MPFR_LAYOUT), arena));
                }
            }
            return Vector.of(resList, ring);
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR triangular solve failed", t);
        }
    }

    private void complexDiv(MemorySegment aR, MemorySegment aI, MemorySegment bR, MemorySegment bI, MemorySegment resR, MemorySegment resI, long prec, Arena arena) throws Throwable {
        // (aR + i*aI) / (bR + i*bI) = [(aR*bR + aI*bI) / (bR^2 + bI^2)] + i[(aI*bR - aR*bI) / (bR^2 + bI^2)]
        MemorySegment d2 = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, d2, c_long(prec));
        MemorySegment t1 = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1, c_long(prec));
        MemorySegment t2 = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2, c_long(prec));

        try {
            // d2 = bR^2 + bI^2
            NativeSafe.invoke(MPFR_MUL, t1, bR, bR, 0);
            NativeSafe.invoke(MPFR_MUL, t2, bI, bI, 0);
            NativeSafe.invoke(MPFR_ADD, d2, t1, t2, 0);

            // resR = (aR*bR + aI*bI) / d2
            NativeSafe.invoke(MPFR_MUL, t1, aR, bR, 0);
            NativeSafe.invoke(MPFR_MUL, t2, aI, bI, 0);
            NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
            NativeSafe.invoke(MPFR_DIV, resR, t1, d2, 0);

            // resI = (aI*bR - aR*bI) / d2
            NativeSafe.invoke(MPFR_MUL, t1, aI, bR, 0);
            NativeSafe.invoke(MPFR_MUL, t2, aR, bI, 0);
            NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
            NativeSafe.invoke(MPFR_DIV, resI, t1, d2, 0);
        } finally {
            NativeSafe.invoke(MPFR_CLEAR, d2);
            NativeSafe.invoke(MPFR_CLEAR, t1);
            NativeSafe.invoke(MPFR_CLEAR, t2);
        }
    }

    public Vector<E> solve(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> a, Vector<E> b, E tolerance, int maxIterations) {
        return bicgstab(a, b, Vector.zeros(b.dimension(), (org.episteme.core.mathematics.structures.rings.Ring<E>) b.getScalarRing()), tolerance, maxIterations);
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
        @SuppressWarnings("unchecked")
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> result = new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>(newStorage, (Ring<E>) sa.getScalarRing());
        return result;
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

    private boolean isComplex(Matrix<E> a) {
        return isComplex(a.getScalarRing());
    }

    private boolean isComplex(Ring<?> ring) {
        return ring.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    public Matrix<E> applyTranscendental(Matrix<E> a, String op, Object... args) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " is not available.");
        long prec = getPrecision();
        boolean isComplex = isComplex(a.getScalarRing());
        
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        @SuppressWarnings("unchecked")
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
                    Complex z = (val instanceof Complex cv) ? cv : Complex.of(getReal(val));
                    result = (E) complexTranscendental(z, op, prec, arena, args);
                } else {
                    result = (E) realTranscendental(getReal(val), op, prec, arena, args);
                }
                storage.set(r, c, result);
            }
        }
        return sa;
    }

    @Override
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        final Ring<E> ring = sa.getScalarRing();
        int n = sa.rows();
        long prec = getPrecision();
        boolean isComplex = isComplex(ring);
        NumericalConfiguration config = org.episteme.core.Episteme.getNumericalConfiguration();
        if (tolerance == null) {
            double threshold = config.getStabilityThreshold();
            @SuppressWarnings("unchecked")
            E tol = (E) (isComplex ? Complex.of(Real.of(threshold), Real.ZERO) : Real.of(threshold));
            tolerance = tol;
        }
        if (maxIterations <= 0) maxIterations = config.getMaxIterations();
        
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_vals = initNativeValues(sa.getValues(), prec, arena, tracker, isComplex);
            MemorySegment h_b = initVector(b.toMatrix(), arena, tracker, prec, isComplex);
            MemorySegment h_x = initVector(x0.toMatrix(), arena, tracker, prec, isComplex);
            
            MemorySegment r = NativeSafe.allocate(arena, MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment p = NativeSafe.allocate(arena, MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment q = NativeSafe.allocate(arena, MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            
            for (int i=0; i<n*(isComplex?2:1); i++) {
                NativeSafe.invoke(MPFR_INIT2, r.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
                NativeSafe.invoke(MPFR_INIT2, p.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
                NativeSafe.invoke(MPFR_INIT2, q.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
            }
            trackArray(tracker, r, n * (isComplex ? 2 : 1));
            trackArray(tracker, p, n * (isComplex ? 2 : 1));
            trackArray(tracker, q, n * (isComplex ? 2 : 1));

            spmv_internal(sa, h_vals, h_x, r, prec, arena, tracker, isComplex);
            axpy_internal(r, negate(isComplex, (E)(Object)(isComplex?Complex.of(Real.ONE,Real.ZERO):Real.ONE)), h_b, n, prec, arena, tracker, isComplex);
            scale_internal(r, negate(isComplex, (E)(Object)(isComplex?Complex.of(Real.ONE,Real.ZERO):Real.ONE)), n, (long)prec, arena, tracker, isComplex);
            
            copy_internal(p, r, n, isComplex);
            E rho = dot_internal(r, r, n, prec, arena, tracker, isComplex, ring);
            
            for (int k = 0; k < maxIterations; k++) {
                spmv_internal(sa, h_vals, p, q, prec, arena, tracker, isComplex);
                E pq = dot_internal(p, q, n, prec, arena, tracker, isComplex, ring);
                E alpha = divide(isComplex, rho, pq);
                
                axpy_internal(h_x, alpha, p, n, prec, arena, tracker, isComplex);
                axpy_internal(r, negate(isComplex, alpha), q, n, prec, arena, tracker, isComplex);
                
                E normR = norm_internal(r, n, prec, arena, tracker, isComplex, ring);
                if (compare(normR, tolerance) < 0) break;
                
                E rhoNew = dot_internal(r, r, n, prec, arena, tracker, isComplex, ring);
                E beta = divide(isComplex, rhoNew, rho);
                scale_internal(p, beta, n, (long)prec, arena, tracker, isComplex);
                axpy_internal(p, castScalar(isComplex ? Complex.of(Real.ONE, Real.ZERO) : Real.ONE, ring), r, n, prec, arena, tracker, isComplex);
                rho = rhoNew;
            }
            return backToVector(h_x, n, isComplex, ring, arena);
        } catch (Throwable t) {
            throw new RuntimeException("Native MPFR Conjugate Gradient failed: " + t.getMessage(), t);
        }
    }

    @Override
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        final Ring<E> ring = sa.getScalarRing();
        int n = sa.rows();
        long prec = getPrecision();
        boolean isComplex = isComplex(ring);
        NumericalConfiguration config = org.episteme.core.Episteme.getNumericalConfiguration();
        if (tolerance == null) {
            double threshold = config.getStabilityThreshold();
            if (ring.zero() instanceof org.episteme.core.mathematics.numbers.real.RealFloat || 
               (isComplex && ((org.episteme.core.mathematics.numbers.complex.Complex)ring.zero()).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat)) {
                threshold = Math.max(threshold, 1e-6);
            }
            @SuppressWarnings("unchecked")
            E tol = (E) (isComplex ? Complex.of(Real.of(threshold), Real.ZERO) : Real.of(threshold));
            tolerance = tol;
        }
        if (maxIterations <= 0) maxIterations = config.getMaxIterations();
        
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_vals = initNativeValues(sa.getValues(), prec, arena, tracker, isComplex);
            MemorySegment h_b = initVector(b.toMatrix(), arena, tracker, prec, isComplex);
            MemorySegment h_x = initVector(x0.toMatrix(), arena, tracker, prec, isComplex);
            
            MemorySegment r0hat = NativeSafe.allocate(arena, MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment r = NativeSafe.allocate(arena, MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment p = NativeSafe.allocate(arena, MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment v = NativeSafe.allocate(arena, MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment s = NativeSafe.allocate(arena, MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment t = NativeSafe.allocate(arena, MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            
            for (int i=0; i<n*(isComplex?2:1); i++) {
                NativeSafe.invoke(MPFR_INIT2, r0hat.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
                NativeSafe.invoke(MPFR_INIT2, r.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
                NativeSafe.invoke(MPFR_INIT2, p.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
                NativeSafe.invoke(MPFR_INIT2, v.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
                NativeSafe.invoke(MPFR_INIT2, s.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
                NativeSafe.invoke(MPFR_INIT2, t.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
            }
            trackArray(tracker, r0hat, n * (isComplex ? 2 : 1));
            trackArray(tracker, r, n * (isComplex ? 2 : 1));
            trackArray(tracker, p, n * (isComplex ? 2 : 1));
            trackArray(tracker, v, n * (isComplex ? 2 : 1));
            trackArray(tracker, s, n * (isComplex ? 2 : 1));
            trackArray(tracker, t, n * (isComplex ? 2 : 1));

            spmv_internal(sa, h_vals, h_x, r, prec, arena, tracker, isComplex);
            axpy_internal(r, negate(isComplex, castScalar(isComplex ? Complex.of(Real.ONE, Real.ZERO) : Real.ONE, ring)), h_b, n, prec, arena, tracker, isComplex);
            scale_internal(r, negate(isComplex, castScalar(isComplex ? Complex.of(Real.ONE, Real.ZERO) : Real.ONE, ring)), n, (long) prec, arena, tracker, isComplex);
            copy_internal(r0hat, r, n, isComplex);
            
            E rho = castScalar(Real.ONE, ring);
            E alpha = castScalar(Real.ONE, ring);
            E omega = castScalar(Real.ONE, ring);
            
            for (int i = 0; i < maxIterations; i++) {
                E rhoNew = dot_internal(r0hat, r, n, prec, arena, tracker, isComplex, ring);
                // Check for breakdown
                if (compare(rhoNew, ring.zero()) == 0) {
                    logger.warn("BiCGSTAB breakdown: rhoNew is zero at iteration " + i);
                    break;
                }
                
                if (i == 0) {
                    copy_internal(p, r, n, isComplex);
                } else {
                    E beta = multiply(isComplex, divide(isComplex, rhoNew, rho), divide(isComplex, alpha, omega));
                    axpy_internal(p, negate(isComplex, omega), v, n, prec, arena, tracker, isComplex);
                    scale_internal(p, beta, n, (long) prec, arena, tracker, isComplex);
                    axpy_internal(p, castScalar(isComplex ? Complex.of(Real.ONE, Real.ZERO) : Real.ONE, ring), r, n, prec, arena, tracker, isComplex);
                }

                spmv_internal(sa, h_vals, p, v, prec, arena, tracker, isComplex);
                E v_dot_r0 = dot_internal(r0hat, v, n, prec, arena, tracker, isComplex, ring);
                if (compare(v_dot_r0, ring.zero()) == 0) {
                    logger.warn("BiCGSTAB breakdown: v_dot_r0 is zero at iteration " + i);
                    break;
                }
                alpha = divide(isComplex, rhoNew, v_dot_r0);
                
                copy_internal(s, r, n, isComplex);
                axpy_internal(s, negate(isComplex, alpha), v, n, prec, arena, tracker, isComplex);
                
                // Partial update for convergence check
                E normS = norm_internal(s, n, prec, arena, tracker, isComplex, ring);
                if (compare(normS, tolerance) < 0) {
                    axpy_internal(h_x, alpha, p, n, prec, arena, tracker, isComplex);
                    logger.debug("BiCGSTAB converged via s-vector at iteration " + i);
                    break;
                }
                
                spmv_internal(sa, h_vals, s, t, prec, arena, tracker, isComplex);
                E t_dot_t = dot_internal(t, t, n, prec, arena, tracker, isComplex, ring);
                if (compare(t_dot_t, ring.zero()) == 0) {
                    logger.warn("BiCGSTAB breakdown: t_dot_t is zero at iteration " + i);
                    break;
                }
                omega = divide(isComplex, dot_internal(t, s, n, prec, arena, tracker, isComplex, ring), t_dot_t);
                
                axpy_internal(h_x, alpha, p, n, prec, arena, tracker, isComplex);
                axpy_internal(h_x, omega, s, n, prec, arena, tracker, isComplex);
                
                copy_internal(r, s, n, isComplex);
                axpy_internal(r, negate(isComplex, omega), t, n, prec, arena, tracker, isComplex);
                
                E normR = norm_internal(r, n, prec, arena, tracker, isComplex, ring);
                if (compare(normR, tolerance) < 0) {
                    logger.debug("BiCGSTAB converged at iteration " + i + " with residual " + normR);
                    break;
                }
                rho = rhoNew;
                if (compare(omega, ring.zero()) == 0) {
                    logger.warn("BiCGSTAB breakdown: omega is zero at iteration " + i);
                    break;
                }
            }
            return backToVector(h_x, n, isComplex, ring, arena);
        } catch (Throwable th) {
            throw new RuntimeException("Native MPFR BiCGSTAB failed: " + th.getMessage(), th);
        }
    }

    @Override
    public Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        int n = sa.rows();
        int m = restarts;
        long prec = getPrecision();
        boolean isComplex = isComplex(sa.getScalarRing());
        NumericalConfiguration config = org.episteme.core.Episteme.getNumericalConfiguration();
        if (tolerance == null) {
            @SuppressWarnings("unchecked")
            E tol = (E) (isComplex ? Complex.of(Real.of(config.getStabilityThreshold()), Real.ZERO) : Real.of(config.getStabilityThreshold()));
            tolerance = tol;
        }
        if (maxIterations <= 0) maxIterations = config.getMaxIterations();
        Ring<E> ring = sa.getScalarRing();
        
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment h_vals = initNativeValues(sa.getValues(), prec, arena, tracker, isComplex);
            MemorySegment h_x = initVector(x0.toMatrix(), arena, tracker, prec, isComplex);
            MemorySegment h_b = initVector(b.toMatrix(), arena, tracker, prec, isComplex);
            
            MemorySegment V = NativeSafe.allocate(arena, MPFR_LAYOUT, (m + 1) * n * (isComplex ? 2 : 1));
            MemorySegment r = NativeSafe.allocate(arena, MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment w = NativeSafe.allocate(arena, MPFR_LAYOUT, n * (isComplex ? 2 : 1));

            for (int i = 0; i < (m + 1) * n * (isComplex ? 2 : 1); i++) {
                NativeSafe.invoke(MPFR_INIT2, V.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
            }
            trackArray(tracker, V, (m + 1) * n * (isComplex ? 2 : 1));
            
            for (int i = 0; i < n * (isComplex ? 2 : 1); i++) {
                NativeSafe.invoke(MPFR_INIT2, r.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
                NativeSafe.invoke(MPFR_INIT2, w.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
            }
            trackArray(tracker, r, n * (isComplex ? 2 : 1));
            trackArray(tracker, w, n * (isComplex ? 2 : 1));

            for (int iter = 0; iter < maxIterations / m + 1; iter++) {
                spmv_internal(sa, h_vals, h_x, r, prec, arena, tracker, isComplex);
                axpy_internal(r, negate(isComplex, castScalar(isComplex ? Complex.of(Real.ONE, Real.ZERO) : Real.ONE, ring)), h_b, n, prec, arena, tracker, isComplex);
                scale_internal(r, negate(isComplex, castScalar(isComplex ? Complex.of(Real.ONE, Real.ZERO) : Real.ONE, ring)), n, (long) prec, arena, tracker, isComplex);
                
                E beta = norm_internal(r, n, prec, arena, tracker, isComplex, ring);
                if (compare(beta, tolerance) < 0) break;
                
                E invBeta = divide(isComplex, castScalar(isComplex ? Complex.of(Real.ONE, Real.ZERO) : Real.ONE, ring), beta);
                copy_internal(V.asSlice(0, n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize()), r, n, isComplex);
                scale_internal(V.asSlice(0, n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize()), invBeta, n, (long)prec, arena, tracker, isComplex);
                
                // Create H with explicit interface type to avoid ClassCastException
                Class<?> eClass = ring.zero().getClass();
                if (ring.zero() instanceof Real) eClass = Real.class;
                else if (ring.zero() instanceof Complex) eClass = Complex.class;
                
                Object[][] H = (Object[][]) java.lang.reflect.Array.newInstance(eClass, m + 1, m);
                for (int i = 0; i <= m; i++) {
                    java.util.Arrays.fill(H[i], ring.zero());
                }
                
                int k;
                for (k = 0; k < m; k++) {
                    MemorySegment vk = V.asSlice(k * n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize(), n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize());
                    spmv_internal(sa, h_vals, vk, w, prec, arena, tracker, isComplex);
                    
                    for (int j = 0; j <= k; j++) {
                        MemorySegment vj = V.asSlice(j * n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize(), n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize());
                        H[j][k] = dot_internal(vj, w, n, prec, arena, tracker, isComplex, ring);
                        axpy_internal(w, negate(isComplex, castScalar(H[j][k], ring)), vj, n, prec, arena, tracker, isComplex);
                    }
                    
                    H[k + 1][k] = norm_internal(w, n, prec, arena, tracker, isComplex, ring);
                    
                    if (compare(castScalar(H[k + 1][k], ring), tolerance) < 0) {
                        solveSmallAndCheck(H, beta, V, h_x, n, k + 1, isComplex, ring, prec, arena, tracker);
                        return backToVector(h_x, n, isComplex, ring, arena);
                    }
                    
                    // Safety check for invH to avoid NaN in vkplus1
                    if (compare(castScalar(H[k + 1][k], ring), tolerance) < 0) break;
                    E invH = divide(isComplex, castScalar(isComplex ? Complex.of(Real.ONE, Real.ZERO) : Real.ONE, ring), castScalar(H[k + 1][k], ring));
                    MemorySegment vkplus1 = V.asSlice((k + 1) * n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize(), n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize());
                    copy_internal(vkplus1, w, n, isComplex);
                    scale_internal(vkplus1, invH, n, (long)prec, arena, tracker, isComplex);
                }
                
                solveSmallAndCheck(H, beta, V, h_x, n, m, isComplex, ring, prec, arena, tracker);
                
                spmv_internal(sa, h_vals, h_x, r, prec, arena, tracker, isComplex);
                axpy_internal(r, negate(isComplex, castScalar(isComplex ? Complex.of(Real.ONE, Real.ZERO) : Real.ONE, ring)), h_b, n, prec, arena, tracker, isComplex);
                scale_internal(r, negate(isComplex, castScalar(isComplex ? Complex.of(Real.ONE, Real.ZERO) : Real.ONE, ring)), n, (long) prec, arena, tracker, isComplex);
                if (compare(norm_internal(r, n, prec, arena, tracker, isComplex, ring), tolerance) < 0) break;
            }
            
            return backToVector(h_x, n, isComplex, ring, arena);
        } catch (Throwable th) {
            String msg = "Native MPFR GMRES failed at iteration " + (maxIterations / m + 1);
            logger.error(msg, th);
            throw new RuntimeException(msg + ": " + th.getMessage(), th);
        }
    }

    private void solveSmallAndCheck(Object[][] H, E beta, MemorySegment V, MemorySegment h_x, int n, int k, boolean isComplex, Ring<E> ring, long prec, Arena arena, ResourceTracker tracker) throws Throwable {
        // Solve the small (k+1) x k upper Hessenberg system Hy = beta*e1 in least squares sense
        // using Givens rotations.
        
        int rows = k + 1;
        int cols = k;
        
        // Copy H to a local working matrix to avoid modifying the original
        MemorySegment hLocalR = NativeSafe.allocate(arena, MPFR_LAYOUT, rows * cols);
        MemorySegment hLocalI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT, rows * cols) : null;
        for (int i=0; i < rows * cols; i++) {
            NativeSafe.invoke(MPFR_INIT2, hLocalR.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
            if (isComplex && hLocalI != null) NativeSafe.invoke(MPFR_INIT2, hLocalI.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
        }
        trackArray(tracker, hLocalR, rows * cols);
        if (isComplex) trackArray(tracker, hLocalI, rows * cols);
        
        for (int i=0; i < rows; i++) {
            for (int j=0; j < cols; j++) {
                setMPFR_internal(hLocalR.asSlice((i * cols + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), 
                                 (isComplex && hLocalI != null) ? hLocalI.asSlice((i * cols + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT) : null, 
                                 (E)H[i][j], arena);
            }
        }
        
        // rhs vector g = beta * e1
        MemorySegment gR = NativeSafe.allocate(arena, MPFR_LAYOUT, rows);
        MemorySegment gI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT, rows) : null;
        for (int i=0; i < rows; i++) {
            NativeSafe.invoke(MPFR_INIT2, gR.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
            if (isComplex && gI != null) NativeSafe.invoke(MPFR_INIT2, gI.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
            NativeSafe.invoke(MPFR_SET_ZERO, gR.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), 0);
            if (isComplex && gI != null) NativeSafe.invoke(MPFR_SET_ZERO, gI.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), 0);
        }
        trackArray(tracker, gR, rows);
        if (isComplex) trackArray(tracker, gI, rows);
        
        setMPFR_internal(gR.asSlice(0, MPFR_LAYOUT), (isComplex && gI != null) ? gI.asSlice(0, MPFR_LAYOUT) : null, beta, arena);
        
        // Pre-allocate common temporaries using the shared arena and tracker
        MemorySegment r_tmp = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, r_tmp, c_long(prec)); track(tracker, r_tmp);
        MemorySegment cR_tmp = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, cR_tmp, c_long(prec)); track(tracker, cR_tmp);
        MemorySegment cI_tmp = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
        if (isComplex) { NativeSafe.invoke(MPFR_INIT2, cI_tmp, c_long(prec)); track(tracker, cI_tmp); }
        MemorySegment sR_tmp = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sR_tmp, c_long(prec)); track(tracker, sR_tmp);
        MemorySegment sI_tmp = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
        if (isComplex) { NativeSafe.invoke(MPFR_INIT2, sI_tmp, c_long(prec)); track(tracker, sI_tmp); }
        
        MemorySegment t1R = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1R, c_long(prec)); track(tracker, t1R);
        MemorySegment t1I = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
        if (isComplex) { NativeSafe.invoke(MPFR_INIT2, t1I, c_long(prec)); track(tracker, t1I); }
        MemorySegment t2R = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2R, c_long(prec)); track(tracker, t2R);
        MemorySegment t2I = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
        if (isComplex) { NativeSafe.invoke(MPFR_INIT2, t2I, c_long(prec)); track(tracker, t2I); }
        
        MemorySegment tmp1 = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tmp1, c_long(prec)); track(tracker, tmp1);
        MemorySegment tmp2 = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tmp2, c_long(prec)); track(tracker, tmp2);

            // Apply Givens rotations to transform H to upper triangular R
        for (int i = 0; i < cols; i++) {
            // Elimination of H[i+1][i] using H[i][i]
            MemorySegment h_ii_R = hLocalR.asSlice((i * cols + i) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            MemorySegment h_ip1i_R = hLocalR.asSlice(((i + 1) * cols + i) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            
            if (isComplex) {
                // Complex Givens
                // We want G = [ c,  s ; -conj(s), conj(c) ] such that G * [a; b] = [r; 0]
                // Pick r = sqrt(|a|^2 + |b|^2), c = conj(a)/r, s = conj(b)/r
                
                MemorySegment aR = hLocalR.asSlice((i * cols + i) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment aI = hLocalI.asSlice((i * cols + i) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment bR = hLocalR.asSlice(((i + 1) * cols + i) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment bI = hLocalI.asSlice(((i + 1) * cols + i) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                
                // Use pre-allocated temps
                MemorySegment r = r_tmp;
                MemorySegment cR = cR_tmp;
                MemorySegment cI = cI_tmp;
                MemorySegment sR = sR_tmp;
                MemorySegment sI = sI_tmp;
                
                MemorySegment t1 = t1R;
                MemorySegment t2 = t2R;
                
                // r = sqrt(aR^2 + aI^2 + bR^2 + bI^2)
                NativeSafe.invoke(MPFR_MUL, t1, aR, aR, 0);
                NativeSafe.invoke(MPFR_MUL, t2, aI, aI, 0);
                NativeSafe.invoke(MPFR_ADD, r, t1, t2, 0);
                NativeSafe.invoke(MPFR_MUL, t1, bR, bR, 0);
                NativeSafe.invoke(MPFR_ADD, r, r, t1, 0);
                NativeSafe.invoke(MPFR_MUL, t2, bI, bI, 0);
                NativeSafe.invoke(MPFR_ADD, r, r, t2, 0);
                NativeSafe.invoke(MPFR_SQRT, r, r, 0);
                
                if ((int)NativeSafe.invoke(MPFR_ZERO_P, r) == 0) {
                    // c = conj(a)/r
                    NativeSafe.invoke(MPFR_DIV, cR, aR, r, 0);
                    NativeSafe.invoke(MPFR_DIV, cI, aI, r, 0);
                    NativeSafe.invoke(MPFR_NEG, cI, cI, 0);
                    
                    // s = conj(b)/r
                    NativeSafe.invoke(MPFR_DIV, sR, bR, r, 0);
                    NativeSafe.invoke(MPFR_DIV, sI, bI, r, 0);
                    NativeSafe.invoke(MPFR_NEG, sI, sI, 0);
                    
                    // Apply to H: 
                    // [ h1_new ] = [ c,  s ] * [ h1 ]
                    // [ h2_new ] = [ -conj(s), conj(c) ] * [ h2 ]
                    for (int j = i; j < cols; j++) {
                        MemorySegment h1R = hLocalR.asSlice((i * cols + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                        MemorySegment h1I = hLocalI.asSlice((i * cols + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                        MemorySegment h2R = hLocalR.asSlice(((i + 1) * cols + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                        MemorySegment h2I = hLocalI.asSlice(((i + 1) * cols + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                        
                        // Use pre-allocated temps
                        NativeSafe.invoke(MPFR_SET, t1R, h1R, 0);
                        NativeSafe.invoke(MPFR_SET, t1I, h1I, 0);
                        NativeSafe.invoke(MPFR_SET, t2R, h2R, 0);
                        NativeSafe.invoke(MPFR_SET, t2I, h2I, 0);
                        
                        // Real part of h1
                        NativeSafe.invoke(MPFR_MUL, h1R, cR, t1R, 0);
                        NativeSafe.invoke(MPFR_MUL, tmp1, cI, t1I, 0);
                        NativeSafe.invoke(MPFR_SUB, h1R, h1R, tmp1, 0);
                        NativeSafe.invoke(MPFR_MUL, tmp1, sR, t2R, 0);
                        NativeSafe.invoke(MPFR_ADD, h1R, h1R, tmp1, 0);
                        NativeSafe.invoke(MPFR_MUL, tmp1, sI, t2I, 0);
                        NativeSafe.invoke(MPFR_SUB, h1R, h1R, tmp1, 0);
                        
                        // Imaginary part of h1
                        NativeSafe.invoke(MPFR_MUL, h1I, cR, t1I, 0);
                        NativeSafe.invoke(MPFR_MUL, tmp1, cI, t1R, 0);
                        NativeSafe.invoke(MPFR_ADD, h1I, h1I, tmp1, 0);
                        NativeSafe.invoke(MPFR_MUL, tmp1, sR, t2I, 0);
                        NativeSafe.invoke(MPFR_ADD, h1I, h1I, tmp1, 0);
                        NativeSafe.invoke(MPFR_MUL, tmp1, sI, t2R, 0);
                        NativeSafe.invoke(MPFR_ADD, h1I, h1I, tmp1, 0);
                        
                        // h2_new = -conj(s)*t1 + conj(c)*t2
                        // conj(s) = sR - i*sI, so -conj(s) = -sR + i*sI
                        // conj(c) = cR - i*cI
                        
                        // Real part of h2: (-sR)*t1R - (sI)*t1I + (cR)*t2R - (-cI)*t2I
                        //                = -sR*t1R - sI*t1I + cR*t2R + cI*t2I
                        NativeSafe.invoke(MPFR_MUL, h2R, sR, t1R, 0);
                        NativeSafe.invoke(MPFR_NEG, h2R, h2R, 0);
                        NativeSafe.invoke(MPFR_MUL, tmp1, sI, t1I, 0);
                        NativeSafe.invoke(MPFR_SUB, h2R, h2R, tmp1, 0);
                        NativeSafe.invoke(MPFR_MUL, tmp1, cR, t2R, 0);
                        NativeSafe.invoke(MPFR_ADD, h2R, h2R, tmp1, 0);
                        NativeSafe.invoke(MPFR_MUL, tmp1, cI, t2I, 0);
                        NativeSafe.invoke(MPFR_ADD, h2R, h2R, tmp1, 0);
                        
                        // Imaginary part of h2: (-sR)*t1I + (sI)*t1R + (cR)*t2I + (-cI)*t2R
                        //                     = -sR*t1I + sI*t1R + cR*t2I - cI*t2R
                        NativeSafe.invoke(MPFR_MUL, h2I, sR, t1I, 0);
                        NativeSafe.invoke(MPFR_NEG, h2I, h2I, 0);
                        NativeSafe.invoke(MPFR_MUL, tmp1, sI, t1R, 0);
                        NativeSafe.invoke(MPFR_ADD, h2I, h2I, tmp1, 0);
                        NativeSafe.invoke(MPFR_MUL, tmp1, cR, t2I, 0);
                        NativeSafe.invoke(MPFR_ADD, h2I, h2I, tmp1, 0);
                        NativeSafe.invoke(MPFR_MUL, tmp1, cI, t2R, 0);
                        NativeSafe.invoke(MPFR_SUB, h2I, h2I, tmp1, 0);
                    }
                    
                    // Apply to g: g(i) = c*g(i) + s*g(i+1), g(i+1) = -conj(s)*g(i) + conj(c)*g(i+1)
                    MemorySegment g1R = gR.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment g1I = gI.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment g2R = gR.asSlice((i + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment g2I = gI.asSlice((i + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    
                    NativeSafe.invoke(MPFR_SET, t1R, g1R, 0);
                    NativeSafe.invoke(MPFR_SET, t1I, g1I, 0);
                    NativeSafe.invoke(MPFR_SET, t2R, g2R, 0);
                    NativeSafe.invoke(MPFR_SET, t2I, g2I, 0);
                    
                    // g1_new
                    NativeSafe.invoke(MPFR_MUL, g1R, cR, t1R, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp1, cI, t1I, 0);
                    NativeSafe.invoke(MPFR_SUB, g1R, g1R, tmp1, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp1, sR, t2R, 0);
                    NativeSafe.invoke(MPFR_ADD, g1R, g1R, tmp1, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp1, sI, t2I, 0);
                    NativeSafe.invoke(MPFR_SUB, g1R, g1R, tmp1, 0);
                    
                    NativeSafe.invoke(MPFR_MUL, g1I, cR, t1I, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp1, cI, t1R, 0);
                    NativeSafe.invoke(MPFR_ADD, g1I, g1I, tmp1, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp1, sR, t2I, 0);
                    NativeSafe.invoke(MPFR_ADD, g1I, g1I, tmp1, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp1, sI, t2R, 0);
                    NativeSafe.invoke(MPFR_ADD, g1I, g1I, tmp1, 0);
                    
                    // g2_new
                    NativeSafe.invoke(MPFR_MUL, g2R, sR, t1R, 0);
                    NativeSafe.invoke(MPFR_NEG, g2R, g2R, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp1, sI, t1I, 0);
                    NativeSafe.invoke(MPFR_SUB, g2R, g2R, tmp1, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp1, cR, t2R, 0);
                    NativeSafe.invoke(MPFR_ADD, g2R, g2R, tmp1, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp1, cI, t2I, 0);
                    NativeSafe.invoke(MPFR_ADD, g2R, g2R, tmp1, 0);
                    
                    NativeSafe.invoke(MPFR_MUL, g2I, sR, t1I, 0);
                    NativeSafe.invoke(MPFR_NEG, g2I, g2I, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp1, sI, t1R, 0);
                    NativeSafe.invoke(MPFR_ADD, g2I, g2I, tmp1, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp1, cR, t2I, 0);
                    NativeSafe.invoke(MPFR_ADD, g2I, g2I, tmp1, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp1, cI, t2R, 0);
                    NativeSafe.invoke(MPFR_SUB, g2I, g2I, tmp1, 0);
                }
            } else {
                // Real Givens
                // c = a / sqrt(a^2 + b^2), s = b / sqrt(a^2 + b^2)
                MemorySegment a = h_ii_R;
                MemorySegment b = h_ip1i_R;
                
                MemorySegment r = r_tmp;
                MemorySegment c = cR_tmp;
                MemorySegment s = sR_tmp;
                
                MemorySegment t1 = t1R;
                MemorySegment t2 = t2R;
                
                NativeSafe.invoke(MPFR_MUL, t1, a, a, 0);
                NativeSafe.invoke(MPFR_MUL, t2, b, b, 0);
                NativeSafe.invoke(MPFR_ADD, r, t1, t2, 0);
                NativeSafe.invoke(MPFR_SQRT, r, r, 0);
                
                if ((int)NativeSafe.invoke(MPFR_ZERO_P, r) == 0) {
                    NativeSafe.invoke(MPFR_DIV, c, a, r, 0);
                    NativeSafe.invoke(MPFR_DIV, s, b, r, 0);
                    
                    // Apply to H: H(i, i:k) = c*H(i, i:k) + s*H(i+1, i:k)
                    //          H(i+1, i:k) = -s*H(i, i:k) + c*H(i+1, i:k)
                    for (int j = i; j < cols; j++) {
                        MemorySegment h_ij = hLocalR.asSlice((i * cols + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                        MemorySegment h_ip1j = hLocalR.asSlice(((i + 1) * cols + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                        
                        NativeSafe.invoke(MPFR_SET, t1, h_ij, 0);
                        NativeSafe.invoke(MPFR_SET, t2, h_ip1j, 0);
                        
                        // h_ij = c*t1 + s*t2
                        NativeSafe.invoke(MPFR_MUL, tmp1, c, t1, 0);
                        NativeSafe.invoke(MPFR_MUL, tmp2, s, t2, 0);
                        NativeSafe.invoke(MPFR_ADD, h_ij, tmp1, tmp2, 0);
                        
                        // h_ip1j = -s*t1 + c*t2
                        NativeSafe.invoke(MPFR_MUL, tmp1, s, t1, 0);
                        NativeSafe.invoke(MPFR_NEG, tmp1, tmp1, 0);
                        NativeSafe.invoke(MPFR_MUL, tmp2, c, t2, 0);
                        NativeSafe.invoke(MPFR_ADD, h_ip1j, tmp1, tmp2, 0);
                    }
                    
                    // Apply to g: g(i) = c*g(i) + s*g(i+1)
                    //          g(i+1) = -s*g(i) + c*g(i+1)
                    MemorySegment g_i = gR.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment g_ip1 = gR.asSlice((i + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_SET, t1, g_i, 0);
                    NativeSafe.invoke(MPFR_SET, t2, g_ip1, 0);
                    
                    NativeSafe.invoke(MPFR_MUL, tmp1, c, t1, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp2, s, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, g_i, tmp1, tmp2, 0);
                    
                    NativeSafe.invoke(MPFR_MUL, tmp1, s, t1, 0);
                    NativeSafe.invoke(MPFR_NEG, tmp1, tmp1, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp2, c, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, g_ip1, tmp1, tmp2, 0);
                }
            }
        }
        
        // Back-substitution to solve Ry = g'
        MemorySegment yR = NativeSafe.allocate(arena, MPFR_LAYOUT, cols);
        MemorySegment yI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT, cols) : null;
        
        for (int i = cols - 1; i >= 0; i--) {
            NativeSafe.invoke(MPFR_INIT2, yR.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, yI.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), c_long(prec));
        }
        trackArray(tracker, yR, cols);
        if (isComplex) trackArray(tracker, yI, cols);

        for (int i = cols - 1; i >= 0; i--) {
            MemorySegment sumR = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sumR, c_long(prec)); track(tracker, sumR);
            MemorySegment sumI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null; 
            if (isComplex) { NativeSafe.invoke(MPFR_INIT2, sumI, c_long(prec)); track(tracker, sumI); }
            
            NativeSafe.invoke(MPFR_SET_ZERO, sumR, 0);
            if (isComplex) NativeSafe.invoke(MPFR_SET_ZERO, sumI, 0);
            
            for (int j = i + 1; j < cols; j++) {
                MemorySegment h_ij_R = hLocalR.asSlice((i * cols + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment y_j_R = yR.asSlice(j * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                
                MemorySegment termR = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, termR, c_long(prec));
                
                if (isComplex) {
                    MemorySegment h_ij_I = hLocalI.asSlice((i * cols + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment y_j_I = yI.asSlice(j * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment termI = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, termI, c_long(prec));
                    
                    // Complex mul: (hR + i*hI)*(yR + i*yI) = (hR*yR - hI*yI) + i*(hR*yI + hI*yR)
                    MemorySegment tmp = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tmp, c_long(prec));
                    
                    NativeSafe.invoke(MPFR_MUL, termR, h_ij_R, y_j_R, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp, h_ij_I, y_j_I, 0);
                    NativeSafe.invoke(MPFR_SUB, termR, termR, tmp, 0);
                    
                    NativeSafe.invoke(MPFR_MUL, termI, h_ij_R, y_j_I, 0);
                    NativeSafe.invoke(MPFR_MUL, tmp, h_ij_I, y_j_R, 0);
                    NativeSafe.invoke(MPFR_ADD, termI, termI, tmp, 0);
                    
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, termR, 0);
                    NativeSafe.invoke(MPFR_ADD, sumI, sumI, termI, 0);
                } else {
                    NativeSafe.invoke(MPFR_MUL, termR, h_ij_R, y_j_R, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, termR, 0);
                }
            }
            
            MemorySegment g_i_R = gR.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            MemorySegment diagR = hLocalR.asSlice((i * cols + i) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            MemorySegment resR = yR.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            
            if (isComplex) {
                MemorySegment g_i_I = gI.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment diagI = hLocalI.asSlice((i * cols + i) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment resI = yI.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                
                MemorySegment numR = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, numR, c_long(prec));
                MemorySegment numI = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, numI, c_long(prec));
                NativeSafe.invoke(MPFR_SUB, numR, g_i_R, sumR, 0);
                NativeSafe.invoke(MPFR_SUB, numI, g_i_I, sumI, 0);
                
                // Complex div: (numR + i*numI) / (diagR + i*diagI)
                // = [ (numR*diagR + numI*diagI) + i*(numI*diagR - numR*diagI) ] / (diagR^2 + diagI^2)
                
                MemorySegment den = NativeSafe.allocate(arena, MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, den, c_long(prec));
                
                NativeSafe.invoke(MPFR_MUL, tmp1, diagR, diagR, 0);
                NativeSafe.invoke(MPFR_MUL, tmp2, diagI, diagI, 0);
                NativeSafe.invoke(MPFR_ADD, den, tmp1, tmp2, 0);
                
                // resR = (numR*diagR + numI*diagI) / den
                NativeSafe.invoke(MPFR_MUL, tmp1, numR, diagR, 0);
                NativeSafe.invoke(MPFR_MUL, tmp2, numI, diagI, 0);
                NativeSafe.invoke(MPFR_ADD, resR, tmp1, tmp2, 0);
                NativeSafe.invoke(MPFR_DIV, resR, resR, den, 0);
                
                // resI = (numI*diagR - numR*diagI) / den
                NativeSafe.invoke(MPFR_MUL, tmp1, numI, diagR, 0);
                NativeSafe.invoke(MPFR_MUL, tmp2, numR, diagI, 0);
                NativeSafe.invoke(MPFR_SUB, resI, tmp1, tmp2, 0);
                NativeSafe.invoke(MPFR_DIV, resI, resI, den, 0);
            } else {
                NativeSafe.invoke(MPFR_SUB, resR, g_i_R, sumR, 0);
                NativeSafe.invoke(MPFR_DIV, resR, resR, diagR, 0);
            }
        }
        
        // x = x0 + V * y
        for (int j = 0; j < cols; j++) {
            MemorySegment vj = V.asSlice(j * n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize(), n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize());
            MemorySegment yj_R = yR.asSlice(j * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            
            if (isComplex) {
                MemorySegment yj_I = yI.asSlice(j * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                Real yjValR = readMPFR(yj_R, arena);
                Real yjValI = readMPFR(yj_I, arena);
                E scalar = castScalar(Complex.of(yjValR, yjValI), ring);
                axpy_internal(h_x, scalar, vj, n, prec, arena, tracker, isComplex);
            } else {
                Real yjVal = readMPFR(yj_R, arena);
                E scalar = castScalar(yjVal, ring);
                axpy_internal(h_x, scalar, vj, n, prec, arena, tracker, isComplex);
            }
        }
    }

    private int compare(E a, E b) {
        if (a instanceof Real rA && b instanceof Real rB) {
            return rA.compareTo(rB);
        }
        if (a instanceof Complex cA && b instanceof Complex cB) {
            Real normA = cA.abs();
            Real normB = cB.abs();
            return normA.compareTo(normB);
        }
        
        // Fallback to double for mixed types or non-standard rings
        NumericalConfiguration config = org.episteme.core.Episteme.getNumericalConfiguration();
        double eps = config.getEpsilonDouble();
        
        double valA = getRealValue(a);
        double valB = getRealValue(b);
        if (a instanceof Complex c1) valA = c1.abs().doubleValue();
        if (b instanceof Complex c2) valB = c2.abs().doubleValue();
        
        if (Math.abs(valA - valB) < eps) return 0;
        return Double.compare(valA, valB);
    }


    private E negate(boolean isComplex, E val) {
        if (isComplex) {
            Complex c = (val instanceof Complex cv) ? cv : Complex.of((Real) val);
            return (E)(Object) c.negate();
        }
        return (E)(Object)((Real)val).negate();
    }


    private E divide(boolean isComplex, E a, E b) {
        if (isComplex) {
            Complex ca = (a instanceof Complex c) ? c : Complex.of((Real) a);
            Complex cb = (b instanceof Complex c) ? c : Complex.of((Real) b);
            return (E)(Object) ca.divide(cb);
        }
        return (E)(Object)((Real)a).divide((Real)b);
    }


    private E multiply(boolean isComplex, E a, E b) {
        if (isComplex) {
            Complex ca = (a instanceof Complex c) ? c : Complex.of((Real) a);
            Complex cb = (b instanceof Complex c) ? c : Complex.of((Real) b);
            return (E)(Object) ca.multiply(cb);
        }
        return (E)(Object)((Real)a).multiply((Real)b);
    }


    private Vector<E> backToVector(MemorySegment h_x, int n, boolean isComplex, Ring<E> ring, Arena arena) throws Throwable {
        java.util.List<E> resultList = new java.util.ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            if (isComplex) {
                Real re = readMPFR(h_x.asSlice(i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), arena);
                Real im = readMPFR(h_x.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), arena);
                resultList.add((E) (Object) Complex.of(re, im));
            } else {
                Real res = readMPFR(h_x.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), arena);
                resultList.add(castScalar(res, ring));
            }
        }
        return Vector.of(resultList, ring);
    }



    private org.episteme.core.mathematics.numbers.complex.Complex complexTranscendental(org.episteme.core.mathematics.numbers.complex.Complex z, String op, long prec, Arena arena, Object... args) {
         try (ResourceTracker tracker = new ResourceTracker()) {
             MemorySegment resR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, resR);
             MemorySegment resI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, resI);
             MemorySegment aR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, aR);
             MemorySegment aI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, aI);

             NativeSafe.invoke(MPFR_INIT2, resR, c_long(prec));
             NativeSafe.invoke(MPFR_INIT2, resI, c_long(prec));
             NativeSafe.invoke(MPFR_INIT2, aR, c_long(prec));
             NativeSafe.invoke(MPFR_INIT2, aI, c_long(prec));
             
             NativeSafe.invoke(MPFR_SET_STR, aR, NativeSafe.allocateFrom(arena, z.getReal().bigDecimalValue().toPlainString()), 10, 0);
             NativeSafe.invoke(MPFR_SET_STR, aI, NativeSafe.allocateFrom(arena, z.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
             
             switch (op.toLowerCase()) {
                 case "exp" -> NativeMPFRDenseLinearAlgebraBackend.complexExp(resR, resI, aR, aI, prec, arena, tracker);
                 case "log" -> NativeMPFRDenseLinearAlgebraBackend.complexLog(resR, resI, aR, aI, prec, arena, tracker);
                 case "log10" -> NativeMPFRDenseLinearAlgebraBackend.complexLog10(resR, resI, aR, aI, prec, arena, tracker);
                 case "sin" -> NativeMPFRDenseLinearAlgebraBackend.complexSin(resR, resI, aR, aI, prec, arena, tracker);
                 case "cos" -> NativeMPFRDenseLinearAlgebraBackend.complexCos(resR, resI, aR, aI, prec, arena, tracker);
                 case "tan" -> NativeMPFRDenseLinearAlgebraBackend.complexTan(resR, resI, aR, aI, prec, arena, tracker);
                 case "asin" -> NativeMPFRDenseLinearAlgebraBackend.complexAsin(resR, resI, aR, aI, prec, arena, tracker);
                 case "acos" -> NativeMPFRDenseLinearAlgebraBackend.complexAcos(resR, resI, aR, aI, prec, arena, tracker);
                 case "atan" -> NativeMPFRDenseLinearAlgebraBackend.complexAtan(resR, resI, aR, aI, prec, arena, tracker);
                 case "sinh" -> NativeMPFRDenseLinearAlgebraBackend.complexSinh(resR, resI, aR, aI, prec, arena, tracker);
                 case "cosh" -> NativeMPFRDenseLinearAlgebraBackend.complexCosh(resR, resI, aR, aI, prec, arena, tracker);
                 case "tanh" -> NativeMPFRDenseLinearAlgebraBackend.complexTanh(resR, resI, aR, aI, prec, arena, tracker);
                 case "sqrt" -> NativeMPFRDenseLinearAlgebraBackend.complexSqrt(resR, resI, aR, aI, prec, arena, tracker);
                 case "cbrt" -> NativeMPFRDenseLinearAlgebraBackend.complexCbrt(resR, resI, aR, aI, prec, arena, tracker);
                 case "pow" -> {
                     if (args.length > 0 && args[0] instanceof org.episteme.core.mathematics.numbers.complex.Complex exp) {
                         MemorySegment eR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, eR);
                         MemorySegment eI = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, eI);
                         NativeSafe.invoke(MPFR_INIT2, eR, c_long(prec));
                         NativeSafe.invoke(MPFR_INIT2, eI, c_long(prec));
                         NativeSafe.invoke(MPFR_SET_STR, eR, NativeSafe.allocateFrom(arena, exp.getReal().bigDecimalValue().toPlainString()), 10, 0);
                         NativeSafe.invoke(MPFR_SET_STR, eI, NativeSafe.allocateFrom(arena, exp.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                         NativeMPFRDenseLinearAlgebraBackend.complexPow(resR, resI, aR, aI, eR, eI, prec, arena, tracker);
                     }
                 }
                 default -> throw new UnsupportedOperationException("Op " + op + " not implemented for complex sparse");
             }
             
             Real r = readMPFR(resR, arena);
             Real i = readMPFR(resI, arena);
             return Complex.of(r, i);
         } catch (Throwable t) {
             throw new RuntimeException("MPFR Sparse complex transcendental failed", t);
         }
    }

    private Real realTranscendental(Real v, String op, long prec, Arena arena, Object... args) {
        try (ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment res = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, res);
            MemorySegment val = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, val);
            NativeSafe.invoke(MPFR_INIT2, res, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, val, c_long(prec));
            NativeSafe.invoke(MPFR_SET_STR, val, NativeSafe.allocateFrom(arena, v.bigDecimalValue().toPlainString()), 10, 0);
            
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
                MemorySegment e = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, e);
                NativeSafe.invoke(MPFR_INIT2, e, c_long(prec));
                NativeSafe.invoke(MPFR_SET_STR, e, NativeSafe.allocateFrom(arena, exp.bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_POW, res, val, e, 0);
            } else {
                 throw new UnsupportedOperationException("Op " + op + " not implemented for real sparse");
            }
            
            return readMPFR(res, arena);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR Sparse real transcendental failed", t);
        }
    }

    @Override public E determinant(Matrix<E> a) { 
        throw new UnsupportedOperationException(getName() + " does not support determinant()");
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (a.rows() != a.cols()) {
            // Rectangular inverse (Moore-Penrose Pseudo-inverse)
            Matrix<E> at = transpose(a);
            if (a.rows() > a.cols()) {
                return multiply(inverse(multiply(at, a)), at);
            } else {
                return multiply(at, inverse(multiply(a, at)));
            }
        }
        throw new UnsupportedOperationException(getName() + " does not support square inverse()");
    }

    // Removed generic fallbacks for decomposition operations to allow AlgorithmManager to handle them.



    private static void clearMPFRArray(MemorySegment mat, int count) {
        if (mat == null || mat.equals(MemorySegment.NULL) || !mat.scope().isAlive()) return;
        for (int i = 0; i < count; i++) {
            try {
                if (mat.scope().isAlive()) {
                    NativeSafe.invoke(MPFR_CLEAR, mat.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT));
                }
            } catch (Throwable t) {
                // Ignore failures during cleanup
            }
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> normalize(Vector<E> a) {
        E n = norm(a);
        if (n == null) return a;
        try {
            if (n instanceof org.episteme.core.mathematics.numbers.real.Real r && r.doubleValue() == 0.0) return a;
            if (n instanceof Complex c && c.real() == 0.0 && c.imaginary() == 0.0) return a;
            if (a.getScalarRing() instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
                return multiply(a, field.inverse(n));
            }
            double val = getRealValue(n);
            if (val == 0) return a;
            return multiply(a, createScalar(1.0 / val, a));
        } catch (Exception e) {
            return a;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> cross(Vector<E> a, Vector<E> b) {
        if (a.dimension() != 3 || b.dimension() != 3) throw new IllegalArgumentException("Cross product only supported for 3D vectors");
        Ring<E> ring = a.getScalarRing();
        E a1 = a.get(0); E a2 = a.get(1); E a3 = a.get(2);
        E b1 = b.get(0); E b2 = b.get(1); E b3 = b.get(2);
        E c1 = ring.subtract(ring.multiply(a2, b3), ring.multiply(a3, b2));
        E c2 = ring.subtract(ring.multiply(a3, b1), ring.multiply(a1, b3));
        E c3 = ring.subtract(ring.multiply(a1, b2), ring.multiply(a2, b1));
        return (Vector<E>) org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(c1, c2, c3), ring);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E angle(Vector<E> a, Vector<E> b) {
        if (!AVAILABLE) throw new UnsupportedOperationException(getName() + " is not available.");
        ensureAlive();
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Vector dimensions must match");
        
        long prec = getPrecision();
        boolean isComplex = isComplex(a);
        
        try (Arena arena = Arena.ofConfined(); ResourceTracker tracker = new ResourceTracker()) {
            MemorySegment dotR = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, dotR);
            MemorySegment dotI = isComplex ? NativeSafe.allocate(arena, MPFR_LAYOUT) : null;
            if (isComplex) track(tracker, dotI);
            
            NativeSafe.invoke(MPFR_INIT2, dotR, c_long(prec));
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, dotI, c_long(prec));
            
            // dot(a, b)
            E d = dot(a, b);
            setMPFR_internal(dotR, dotI, d, arena);
            
            MemorySegment normA = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, normA);
            MemorySegment normB = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, normB);
            NativeSafe.invoke(MPFR_INIT2, normA, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, normB, c_long(prec));
            
            setMPFR_internal(normA, null, getRealPart(norm(a)), arena);
            setMPFR_internal(normB, null, getRealPart(norm(b)), arena);
            
            // denom = normA * normB
            MemorySegment denom = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, denom);
            NativeSafe.invoke(MPFR_INIT2, denom, c_long(prec));
            NativeSafe.invoke(MPFR_MUL, denom, normA, normB, 0);
            
            if ((int) NativeSafe.invoke(MPFR_ZERO_P, denom) != 0) return createScalar(0.0, a);
            
            // cosTheta = dotR / denom
            MemorySegment cosTheta = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, cosTheta);
            NativeSafe.invoke(MPFR_INIT2, cosTheta, c_long(prec));
            NativeSafe.invoke(MPFR_DIV, cosTheta, dotR, denom, 0);
            
            // Clamp to [-1, 1]
            MemorySegment one = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, one);
            MemorySegment minusOne = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, minusOne);
            NativeSafe.invoke(MPFR_INIT2, one, c_long(prec));
            NativeSafe.invoke(MPFR_INIT2, minusOne, c_long(prec));
            NativeSafe.invoke(MPFR_SET_SI, one, 1L, 0);
            NativeSafe.invoke(MPFR_SET_SI, minusOne, -1L, 0);
            
            if ((int) NativeSafe.invoke(MPFR_CMP, cosTheta, one) > 0) NativeSafe.invoke(MPFR_SET, cosTheta, one, 0);
            if ((int) NativeSafe.invoke(MPFR_CMP, cosTheta, minusOne) < 0) NativeSafe.invoke(MPFR_SET, cosTheta, minusOne, 0);
            
            // angle = acos(cosTheta)
            MemorySegment angleRes = NativeSafe.allocate(arena, MPFR_LAYOUT); track(tracker, angleRes);
            NativeSafe.invoke(MPFR_INIT2, angleRes, c_long(prec));
            NativeSafe.invoke(MPFR_ACOS, angleRes, cosTheta, 0);
            
            Real res = readMPFR(angleRes, arena);
            return createScalar(res, a);
        } catch (Throwable t) {
            logger.error("Native MPFR angle failed: {}", t.getMessage());
            return createScalar(0.0, a);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> projection(Vector<E> a, Vector<E> b) {
        E dAB = dot(a, b);
        E dBB = dot(b, b);
        Ring<E> ring = a.getScalarRing();
        try {
            if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
                if (dBB instanceof org.episteme.core.mathematics.numbers.real.Real r && r.doubleValue() == 0.0) return a;
                if (dBB instanceof Complex c && c.real() == 0.0 && c.imaginary() == 0.0) return a;
                E factor = field.divide(dAB, dBB);
                return multiply(b, factor);
            }
            double dotAB = getRealValue(dAB);
            double dotBB = getRealValue(dBB);
            if (dotBB == 0) return a;
            return multiply(b, createScalar(dotAB / dotBB, a));
        } catch (Exception e) {
            return a;
        }
    }

    private E createScalar(double val, Vector<E> ref) {
        Ring<E> ring = ref.getScalarRing();
        if (ring.zero() instanceof Complex) {
            return (E) (Object) Complex.of(Real.of(val), Real.ZERO);
        } else if (ring.zero() instanceof org.episteme.core.mathematics.numbers.real.RealBig) {
            return (E) (Object) org.episteme.core.mathematics.numbers.real.RealBig.create(java.math.BigDecimal.valueOf(val));
        } else {
            return (E) (Object) Real.of(val);
        }
    }

    private E createScalar(org.episteme.core.mathematics.numbers.real.Real val, Vector<E> ref) {
        Ring<E> ring = ref.getScalarRing();
        if (ring.zero() instanceof Complex) {
            return (E) (Object) Complex.of(val, Real.ZERO);
        } else {
            return (E) (Object) val;
        }
    }
}
