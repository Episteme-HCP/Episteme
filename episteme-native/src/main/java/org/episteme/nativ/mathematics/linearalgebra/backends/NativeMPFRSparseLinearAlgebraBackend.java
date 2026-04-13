/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;

import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.episteme.nativ.mathematics.numbers.real.NativeRealBig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.episteme.nativ.mathematics.numbers.real.backends.NativeMPFRNumbers;
import static org.episteme.nativ.mathematics.numbers.real.backends.NativeMPFRNumbers.*;

/**
 * Arbitrary-precision Sparse Linear Algebra backend using MPFR (via Panama).
 * Optimized for CSR storage.
 */
@com.google.auto.service.AutoService({LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class})
@SuppressWarnings("unchecked")
public class NativeMPFRSparseLinearAlgebraBackend<E> implements SparseLinearAlgebraProvider<E>, NativeBackend, CPUBackend {
    private static final Logger logger = LoggerFactory.getLogger(NativeMPFRSparseLinearAlgebraBackend.class);
    private static final NativeMPFRDenseLinearAlgebraBackend<?> SHARED_DENSE = new NativeMPFRDenseLinearAlgebraBackend<>();

    // Redundant handles removed, centralized in NativeMPFRNumbers
    private static final MemoryLayout MPFR_LAYOUT = NativeMPFRNumbers.MPFR_LAYOUT;
    private static final boolean AVAILABLE = NativeMPFRNumbers.AVAILABLE;

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

    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");

    private Real readMPFR(MemorySegment val, Arena arena) {
        return NativeRealBig.copyFrom(val, getPrecision());
    }


    private void setMPFR(MemorySegment dest, E value, Arena arena) throws Throwable {
        if (value instanceof NativeRealBig nrb) {
            NativeSafe.invoke(MPFR_SET, dest, nrb.getPtr(), 0);
            return;
        }
        
        String s;
        if (value instanceof Real r) {
            s = r.bigDecimalValue().toPlainString();
        } else if (value instanceof Complex c) {
            s = c.getReal().bigDecimalValue().toPlainString();
        } else {
            s = value.toString();
        }
        NativeSafe.invoke(MPFR_SET_STR, dest, arena.allocateFrom(s), 10, 0);
    }

    private MemorySegment initVector(Matrix<E> a, Arena arena, int prec, boolean isComplex) throws Throwable {
        int n = a.rows() * a.cols();
        int multiplier = isComplex ? 2 : 1;
        MemorySegment h_v = arena.allocate(MPFR_LAYOUT, (long) n * multiplier);
        for (int i = 0; i < n * multiplier; i++) {
            MemorySegment rc = h_v.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, rc, (int) prec);
        }
        
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
        
        NativeSafe.invoke(MPFR_SET_UI, sumR, 0L, 0);
        if (isComplex) NativeSafe.invoke(MPFR_SET_UI, sumI, 0L, 0);

        MemorySegment t1 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1, (int) prec);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2, (int) prec);

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
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (a.cols() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        int prec = (int) getPrecision();
        boolean isComplex = isComplex(sa.getScalarRing());
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_b = initVector(b.toMatrix(), arena, prec, isComplex);
            MemorySegment res = arena.allocate(MPFR_LAYOUT, sa.rows() * (isComplex ? 2 : 1));
            for (int i=0; i<sa.rows() * (isComplex ? 2 : 1); i++) NativeSafe.invoke(MPFR_INIT2, res.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (int) prec);
            
            spmv_internal(sa, h_b, res, (int) prec, arena, isComplex);
            
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

    private void axpy_internal(MemorySegment y, E alpha, MemorySegment x, int n, int prec, Arena arena, boolean isComplex) throws Throwable {
        MemorySegment aR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, aR, (int) prec);
        MemorySegment aI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
        if (isComplex) NativeSafe.invoke(MPFR_INIT2, aI, (int) prec);
        
        if (isComplex) {
            Complex c = (alpha instanceof Complex cv) ? cv : Complex.of((Real) alpha);
            NativeSafe.invoke(MPFR_SET_STR, aR, arena.allocateFrom(c.getReal().bigDecimalValue().toPlainString()), 10, 0);
            NativeSafe.invoke(MPFR_SET_STR, aI, arena.allocateFrom(c.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
        } else {
            NativeSafe.invoke(MPFR_SET_STR, aR, arena.allocateFrom(((Real)alpha).bigDecimalValue().toPlainString()), 10, 0);
        }

        MemorySegment t1 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1, (int) prec);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2, (int) prec);

        long layoutSize = MPFR_LAYOUT.byteSize();
        int stride = isComplex ? 2 : 1;
        for (int i = 0; i < n; i++) {
            MemorySegment xiR = x.asSlice(i * stride * layoutSize, MPFR_LAYOUT);
            MemorySegment yiR = y.asSlice(i * stride * layoutSize, MPFR_LAYOUT);
            
            if (isComplex) {
                MemorySegment xiI = x.asSlice((i * 2 + 1) * layoutSize, MPFR_LAYOUT);
                MemorySegment yiI = y.asSlice((i * 2 + 1) * layoutSize, MPFR_LAYOUT);
                
                // y = a*x + y: (aR+i*aI)*(xR+i*xI) + (yR+i*yI)
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

    private E dot_internal(MemorySegment x, MemorySegment y, int n, int prec, Arena arena, boolean isComplex, Ring<E> ring) throws Throwable {
        MemorySegment sumR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sumR, (int) prec); NativeSafe.invoke(MPFR_SET_UI, sumR, 0, 0);
        MemorySegment sumI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
        if (isComplex) { NativeSafe.invoke(MPFR_INIT2, sumI, (int) prec); NativeSafe.invoke(MPFR_SET_UI, sumI, 0, 0); }
        
        MemorySegment t1 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1, (int) prec);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2, (int) prec);

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

    private E norm_internal(MemorySegment x, int n, int prec, Arena arena, boolean isComplex, Ring<E> ring) throws Throwable {
        MemorySegment sumSq = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sumSq, (int) prec); NativeSafe.invoke(MPFR_SET_UI, sumSq, 0, 0);
        MemorySegment t1 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1, (int) prec);

        int stride = isComplex ? 2 : 1;
        long layoutSize = MPFR_LAYOUT.byteSize();
        for (int i = 0; i < n * stride; i++) {
            MemorySegment val = x.asSlice(i * layoutSize, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_MUL, t1, val, val, 0);
            NativeSafe.invoke(MPFR_ADD, sumSq, sumSq, t1, 0);
        }
        
        MemorySegment res = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, res, (int) prec);
        NativeSafe.invoke(MPFR_SQRT, res, sumSq, 0);
        
        Real val = readMPFR(res, arena);
        if (isComplex) {
            return (E) (Object) Complex.of(val, Real.ZERO);
        } else {
            return (E) (Object) val;
        }
    }

    private void copy_internal(MemorySegment dest, MemorySegment src, int n, boolean isComplex) throws Throwable {
        int stride = isComplex ? 2 : 1;
        long layoutSize = MPFR_LAYOUT.byteSize();
        for (int i = 0; i < n * stride; i++) {
            NativeSafe.invoke(MPFR_SET, dest.asSlice(i * layoutSize, MPFR_LAYOUT), src.asSlice(i * layoutSize, MPFR_LAYOUT), 0);
        }
    }

    private void scale_internal(MemorySegment x, E alpha, int n, long prec, Arena arena, boolean isComplex) throws Throwable {
        MemorySegment aR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, aR, (int) prec);
        MemorySegment aI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
        if (isComplex) NativeSafe.invoke(MPFR_INIT2, aI, (int) prec);
        
        if (isComplex) {
            Complex c = (alpha instanceof Complex cv) ? cv : Complex.of((Real) alpha);
            NativeSafe.invoke(MPFR_SET_STR, aR, arena.allocateFrom(c.getReal().bigDecimalValue().toPlainString()), 10, 0);
            NativeSafe.invoke(MPFR_SET_STR, aI, arena.allocateFrom(c.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
        } else {
            NativeSafe.invoke(MPFR_SET_STR, aR, arena.allocateFrom(((Real)alpha).bigDecimalValue().toPlainString()), 10, 0);
        }

        MemorySegment t1 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1, (int) prec);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2, (int) prec);

        int stride = isComplex ? 2 : 1;
        for (int i = 0; i < n * stride; i += stride) {
            MemorySegment xiR = x.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            if (isComplex) {
                MemorySegment xiI = x.asSlice((i + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                // Complex mul: (aR+i*aI)*(xiR+i*xiI)
                NativeSafe.invoke(MPFR_MUL, t1, aR, xiR, 0);
                NativeSafe.invoke(MPFR_MUL, t2, aI, xiI, 0);
                NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0); // Reuse t1 for real part
                
                NativeSafe.invoke(MPFR_MUL, t2, aR, xiI, 0);
                NativeSafe.invoke(MPFR_MUL, xiI, aI, xiR, 0); // Reuse xiI for imag part temp
                NativeSafe.invoke(MPFR_ADD, xiI, t2, xiI, 0);
                NativeSafe.invoke(MPFR_SET, xiR, t1, 0);
            } else {
                NativeSafe.invoke(MPFR_MUL, xiR, xiR, aR, 0);
            }
        }
    }

    private MemorySegment initNativeValues(Object[] vals, int prec, Arena arena, boolean isComplex) throws Throwable {
        int n = vals.length;
        int stride = isComplex ? 2 : 1;
        long layoutSize = MPFR_LAYOUT.byteSize();
        MemorySegment nativeVals = arena.allocate(MPFR_LAYOUT, n * stride);
        for (int i = 0; i < n; i++) {
            Object v = vals[i];
            MemorySegment vR = nativeVals.asSlice(i * stride * layoutSize, MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, vR, (int) prec);
            if (isComplex) {
                MemorySegment vI = nativeVals.asSlice((i * stride + 1) * layoutSize, MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, vI, (int) prec);
                
                Real re = getRealPart(v);
                Real im = getImagPart(v);
                NativeSafe.invoke(MPFR_SET_STR, vR, arena.allocateFrom(re.bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, vI, arena.allocateFrom(im.bigDecimalValue().toPlainString()), 10, 0);
            } else {
                Real rv = getRealPart(v);
                NativeSafe.invoke(MPFR_SET_STR, vR, arena.allocateFrom(rv.bigDecimalValue().toPlainString()), 10, 0);
            }
        }
        return nativeVals;
    }

    private void spmv_internal(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa, MemorySegment h_vals, MemorySegment h_b, MemorySegment res, int prec, Arena arena, boolean isComplex) throws Throwable {
        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        long layoutSize = MPFR_LAYOUT.byteSize();
        int stride = isComplex ? 2 : 1;

        MemorySegment sumR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sumR, (int) prec);
        MemorySegment sumI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
        if (isComplex) NativeSafe.invoke(MPFR_INIT2, sumI, (int) prec);
        
        MemorySegment t1 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1, (int) prec);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2, (int) prec);

        for (int i = 0; i < sa.rows(); i++) {
            NativeSafe.invoke(MPFR_SET_UI, sumR, 0L, 0);
            if (isComplex) NativeSafe.invoke(MPFR_SET_UI, sumI, 0L, 0);
            
            for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                int col = colIdx[k];
                MemorySegment valR = h_vals.asSlice(k * stride * layoutSize, MPFR_LAYOUT);
                
                if (isComplex) {
                    MemorySegment valI = h_vals.asSlice((k * 2 + 1) * layoutSize, MPFR_LAYOUT);
                    MemorySegment bR = h_b.asSlice(col * 2 * layoutSize, MPFR_LAYOUT);
                    MemorySegment bI = h_b.asSlice((col * 2 + 1) * layoutSize, MPFR_LAYOUT);
                    
                    NativeSafe.invoke(MPFR_MUL, t1, valR, bR, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, valI, bI, 0);
                    NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                    
                    NativeSafe.invoke(MPFR_MUL, t1, valR, bI, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, valI, bR, 0);
                    NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, sumI, sumI, t1, 0);
                } else {
                    MemorySegment bval = h_b.asSlice(col * layoutSize, MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_MUL, t1, valR, bval, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                }
            }
            NativeSafe.invoke(MPFR_SET, res.asSlice(i * stride * layoutSize, MPFR_LAYOUT), sumR, 0);
            if (isComplex) NativeSafe.invoke(MPFR_SET, res.asSlice((i * 2 + 1) * layoutSize, MPFR_LAYOUT), sumI, 0);
        }
    }

    private void spmv_internal(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa, MemorySegment h_b, MemorySegment res, int prec, Arena arena, boolean isComplex) throws Throwable {
        MemorySegment h_vals = initNativeValues(sa.getValues(), prec, arena, isComplex);
        spmv_internal(sa, h_vals, h_b, res, prec, arena, isComplex);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        long prec = getPrecision();
        org.episteme.core.mathematics.structures.rings.Ring<E> ring = (org.episteme.core.mathematics.structures.rings.Ring<E>) sa.getScalarRing();
        boolean isComplex = isComplex(sa.getScalarRing());
        
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(sa.rows(), sa.cols(), (E) ring.zero());
        
        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        Object[] vals = sa.getValues();
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment sR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sR, (int) prec);
            MemorySegment sI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, sI, (int) prec);
            
            if (isComplex) {
                Complex cs = (scalar instanceof Complex cv) ? cv : Complex.of((Real) scalar);
                NativeSafe.invoke(MPFR_SET_STR, sR, arena.allocateFrom(cs.getReal().bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, sI, arena.allocateFrom(cs.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
            } else {
                NativeSafe.invoke(MPFR_SET_STR, sR, arena.allocateFrom(((Real)scalar).bigDecimalValue().toPlainString()), 10, 0);
            }

            MemorySegment t1 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1, (int) prec);
            MemorySegment t2 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2, (int) prec);
            MemorySegment valR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, valR, (int) prec);
            MemorySegment valI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, valI, (int) prec);
            
            MemorySegment resR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, resR, (int) prec);
            MemorySegment resI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, resI, (int) prec);

            for (int i = 0; i < sa.rows(); i++) {
                for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                    Object val = vals[k];
                    if (isComplex) {
                        if (val instanceof Complex cv) {
                            NativeSafe.invoke(MPFR_SET_STR, valR, arena.allocateFrom(cv.getReal().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, valI, arena.allocateFrom(cv.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                        } else {
                            NativeSafe.invoke(MPFR_SET_STR, valR, arena.allocateFrom(((Real)val).bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_UI, valI, 0L, 0);
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
                        NativeSafe.invoke(MPFR_SET_STR, valR, arena.allocateFrom(((Real)val).bigDecimalValue().toPlainString()), 10, 0);
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
        boolean isComplex = isComplex(field);
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment sR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sR, (int) prec);
            MemorySegment sI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, sI, (int) prec);
            
            if (isComplex) {
                Complex cs = (scalar instanceof Complex cv) ? cv : Complex.of((Real) scalar);
                NativeSafe.invoke(MPFR_SET_STR, sR, arena.allocateFrom(cs.getReal().bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, sI, arena.allocateFrom(cs.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
            } else {
                NativeSafe.invoke(MPFR_SET_STR, sR, arena.allocateFrom(((Real)scalar).bigDecimalValue().toPlainString()), 10, 0);
            }

            MemorySegment t1 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1, (int) prec);
            MemorySegment t2 = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2, (int) prec);
            MemorySegment valR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, valR, (int) prec);
            MemorySegment valI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, valI, (int) prec);
            
            MemorySegment resR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, resR, (int) prec);
            MemorySegment resI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, resI, (int) prec);

            E[] resultArr = (E[]) java.lang.reflect.Array.newInstance(Object.class, n);
            for (int i = 0; i < n; i++) {
                E val = a.get(i);
                if (isComplex) {
                    Complex cv = (val instanceof Complex c) ? c : Complex.of((Real) val);
                    NativeSafe.invoke(MPFR_SET_STR, valR, arena.allocateFrom(cv.getReal().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, valI, arena.allocateFrom(cv.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                    
                    NativeSafe.invoke(MPFR_MUL, t1, valR, sR, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, valI, sI, 0);
                    NativeSafe.invoke(MPFR_SUB, resR, t1, t2, 0);
                    
                    NativeSafe.invoke(MPFR_MUL, t1, valR, sI, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, valI, sR, 0);
                    NativeSafe.invoke(MPFR_ADD, resI, t1, t2, 0);
                    
                    resultArr[i] = (E) (Object) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena));
                } else {
                    NativeSafe.invoke(MPFR_SET_STR, valR, arena.allocateFrom(((Real)val).bigDecimalValue().toPlainString()), 10, 0);
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
        long prec = getPrecision();
        boolean isComplex = isComplex(a);
        
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sb = toSparse(b);
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(sa.rows(), sa.cols(), (E) sa.getScalarRing().zero());
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment t1R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1R, (int) prec);
            MemorySegment t1I = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t1I, (int) prec);
            
            MemorySegment t2R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2R, (int) prec);
            MemorySegment t2I = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t2I, (int) prec);
            
            MemorySegment resR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, resR, (int) prec);
            MemorySegment resI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, resI, (int) prec);

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
                            Complex ca = (valA instanceof Complex c) ? c : Complex.of((Real) valA);
                            Complex cb = (valB instanceof Complex c) ? c : Complex.of((Real) valB);
                            NativeSafe.invoke(MPFR_SET_STR, t1R, arena.allocateFrom(ca.getReal().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t1I, arena.allocateFrom(ca.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t2R, arena.allocateFrom(cb.getReal().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t2I, arena.allocateFrom(cb.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_ADD, resR, t1R, t2R, 0);
                            NativeSafe.invoke(MPFR_ADD, resI, t1I, t2I, 0);
                            rowValues.put(col, (E) (Object) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena)));
                        } else {
                            NativeSafe.invoke(MPFR_SET_STR, t1R, arena.allocateFrom(((Real)valA).bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t2R, arena.allocateFrom(((Real)valB).bigDecimalValue().toPlainString()), 10, 0);
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
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment t1R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1R, (int) prec);
            MemorySegment t1I = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t1I, (int) prec);
            
            MemorySegment t2R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2R, (int) prec);
            MemorySegment t2I = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t2I, (int) prec);
            
            MemorySegment resR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, resR, (int) prec);
            MemorySegment resI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, resI, (int) prec);

            E[] resultArr = (E[]) java.lang.reflect.Array.newInstance(Object.class, v1.dimension());
            for (int i = 0; i < v1.dimension(); i++) {
                E valA = v1.get(i);
                E valB = v2.get(i);
                if (isComplex) {
                    Complex ca = (valA instanceof Complex c) ? c : Complex.of((Real) valA);
                    Complex cb = (valB instanceof Complex c) ? c : Complex.of((Real) valB);
                    NativeSafe.invoke(MPFR_SET_STR, t1R, arena.allocateFrom(ca.getReal().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t1I, arena.allocateFrom(ca.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t2R, arena.allocateFrom(cb.getReal().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t2I, arena.allocateFrom(cb.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_ADD, resR, t1R, t2R, 0);
                    NativeSafe.invoke(MPFR_ADD, resI, t1I, t2I, 0);
                    resultArr[i] = (E) (Object) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena));
                } else {
                    NativeSafe.invoke(MPFR_SET_STR, t1R, arena.allocateFrom(((Real)valA).bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t2R, arena.allocateFrom(((Real)valB).bigDecimalValue().toPlainString()), 10, 0);
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
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment t1R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1R, (int) prec);
            MemorySegment t1I = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t1I, (int) prec);
            
            MemorySegment t2R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2R, (int) prec);
            MemorySegment t2I = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t2I, (int) prec);
            
            MemorySegment resR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, resR, (int) prec);
            MemorySegment resI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, resI, (int) prec);

            E[] resultArr = (E[]) java.lang.reflect.Array.newInstance(Object.class, v1.dimension());
            for (int i = 0; i < v1.dimension(); i++) {
                E valA = v1.get(i);
                E valB = v2.get(i);
                if (isComplex) {
                    Complex ca = (valA instanceof Complex c) ? c : Complex.of((Real) valA);
                    Complex cb = (valB instanceof Complex c) ? c : Complex.of((Real) valB);
                    NativeSafe.invoke(MPFR_SET_STR, t1R, arena.allocateFrom(ca.getReal().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t1I, arena.allocateFrom(ca.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t2R, arena.allocateFrom(cb.getReal().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t2I, arena.allocateFrom(cb.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SUB, resR, t1R, t2R, 0);
                    NativeSafe.invoke(MPFR_SUB, resI, t1I, t2I, 0);
                    resultArr[i] = (E) (Object) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena));
                } else {
                    NativeSafe.invoke(MPFR_SET_STR, t1R, arena.allocateFrom(((Real)valA).bigDecimalValue().toPlainString()), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, t2R, arena.allocateFrom(((Real)valB).bigDecimalValue().toPlainString()), 10, 0);
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
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment t1R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1R, (int) prec);
            MemorySegment t1I = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t1I, (int) prec);
            
            MemorySegment t2R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2R, (int) prec);
            MemorySegment t2I = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t2I, (int) prec);
            
            MemorySegment resR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, resR, (int) prec);
            MemorySegment resI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, resI, (int) prec);

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
                            NativeSafe.invoke(MPFR_SET_STR, t1R, arena.allocateFrom(ca.getReal().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t1I, arena.allocateFrom(ca.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t2R, arena.allocateFrom(cb.getReal().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t2I, arena.allocateFrom(cb.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SUB, resR, t1R, t2R, 0);
                            NativeSafe.invoke(MPFR_SUB, resI, t1I, t2I, 0);
                            rowValues.put(col, (E) (Object) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena)));
                        } else {
                            NativeSafe.invoke(MPFR_SET_STR, t1R, arena.allocateFrom(((Real)valA).bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t2R, arena.allocateFrom(((Real)valB).bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SUB, resR, t1R, t2R, 0);
                            rowValues.put(col, (E) (Object) readMPFR(resR, arena));
                        }
                    } else {
                        // Subtracting valB from zero: 0 - valB
                        if (isComplex) {
                            Complex cb = (Complex) valB;
                            NativeSafe.invoke(MPFR_SET_STR, t1R, arena.allocateFrom(cb.getReal().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t1I, arena.allocateFrom(cb.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_NEG, resR, t1R, 0);
                            NativeSafe.invoke(MPFR_NEG, resI, t1I, 0);
                            rowValues.put(col, (E) (Object) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena)));
                        } else {
                            NativeSafe.invoke(MPFR_SET_STR, t1R, arena.allocateFrom(((Real)valB).bigDecimalValue().toPlainString()), 10, 0);
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
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, (Ring<E>)sa.getScalarRing());
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
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment t1R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t1R, (int) prec);
            MemorySegment t1I = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t1I, (int) prec);
            
            MemorySegment t2R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, t2R, (int) prec);
            MemorySegment t2I = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, t2I, (int) prec);
            
            MemorySegment resR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, resR, (int) prec);
            MemorySegment resI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, resI, (int) prec);

            MemorySegment accR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, accR, (int) prec);
            MemorySegment accI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, accI, (int) prec);

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
                            NativeSafe.invoke(MPFR_SET_STR, t1R, arena.allocateFrom(ca.getReal().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t1I, arena.allocateFrom(ca.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t2R, arena.allocateFrom(cb.getReal().bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t2I, arena.allocateFrom(cb.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                            
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
                                    NativeSafe.invoke(MPFR_SET_STR, t1R, arena.allocateFrom(c1.getReal().bigDecimalValue().toPlainString()), 10, 0);
                                    NativeSafe.invoke(MPFR_SET_STR, t1I, arena.allocateFrom(c1.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                                    NativeSafe.invoke(MPFR_SET_STR, t2R, arena.allocateFrom(c2.getReal().bigDecimalValue().toPlainString()), 10, 0);
                                    NativeSafe.invoke(MPFR_SET_STR, t2I, arena.allocateFrom(c2.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                                    NativeSafe.invoke(MPFR_ADD, resR, t1R, t2R, 0);
                                    NativeSafe.invoke(MPFR_ADD, resI, t1I, t2I, 0);
                                    return (E) Complex.of(readMPFR(resR, arena), readMPFR(resI, arena));
                                } catch (Throwable te) { throw new RuntimeException(te); }
                            });
                        } else {
                            NativeSafe.invoke(MPFR_SET_STR, t1R, arena.allocateFrom(((Real)valA).bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_SET_STR, t2R, arena.allocateFrom(((Real)valB).bigDecimalValue().toPlainString()), 10, 0);
                            NativeSafe.invoke(MPFR_MUL, resR, t1R, t2R, 0);
                            
                            Real prod = readMPFR(resR, arena);
                            rowAccumulator.merge(colB, (E) prod, (v1, v2) -> {
                                Real r1 = (Real) v1;
                                Real r2 = (Real) v2;
                                try {
                                    NativeSafe.invoke(MPFR_SET_STR, t1R, arena.allocateFrom(r1.bigDecimalValue().toPlainString()), 10, 0);
                                    NativeSafe.invoke(MPFR_SET_STR, t2R, arena.allocateFrom(r2.bigDecimalValue().toPlainString()), 10, 0);
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
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, (Ring<E>)sa.getScalarRing());
    }

    @Override
    public E dot(Vector<E> v1, Vector<E> v2) {
        if (v1.dimension() != v2.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " is not available.");
        
        int prec = (int) getPrecision();
        try (Arena arena = Arena.ofConfined()) {
            boolean isComplex = isComplex(v1.getScalarRing());
            MemorySegment h_v1 = initVector(v1.toMatrix(), arena, prec, isComplex);
            MemorySegment h_v2 = initVector(v2.toMatrix(), arena, prec, isComplex);
            
            int resSize = isComplex ? 2 : 1;
            MemorySegment res = arena.allocate(MPFR_LAYOUT, resSize);
            for (int i=0; i<resSize; i++) NativeSafe.invoke(MPFR_INIT2, res.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (int) prec);
            
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
        if (a.rows() == a.cols()) {
            // Square system
            return bicgstab(a, b, Vector.zeros(b.dimension(), (org.episteme.core.mathematics.structures.rings.Ring<E>) b.getScalarRing()), (E)Real.of("1e-20"), 1000);
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

    public Vector<E> solve(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> a, Vector<E> b) {
        return solve((Matrix<E>)a, b);
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
        int prec = (int) getPrecision();
        boolean isComplex = isComplex(a.getScalarRing());
        
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
                    result = (E) complexTranscendental((org.episteme.core.mathematics.numbers.complex.Complex) val, op, (int) prec, arena, args);
                } else {
                    result = (E) realTranscendental((Real) val, op, prec, arena, args);
                }
                storage.set(r, c, result);
            }
        }
        return sa;
    }

    @Override
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        int n = sa.rows();
        int prec = (int) getPrecision();
        boolean isComplex = isComplex(sa.getScalarRing());
        Ring<E> ring = sa.getScalarRing();
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_vals = initNativeValues(sa.getValues(), prec, arena, isComplex);
            MemorySegment h_b = initVector(b.toMatrix(), arena, prec, isComplex);
            MemorySegment h_x = initVector(x0.toMatrix(), arena, prec, isComplex);
            
            MemorySegment r = arena.allocate(MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment p = arena.allocate(MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment q = arena.allocate(MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            for (int i=0; i<n*(isComplex?2:1); i++) {
                NativeSafe.invoke(MPFR_INIT2, r.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (int) prec);
                NativeSafe.invoke(MPFR_INIT2, p.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (int) prec);
                NativeSafe.invoke(MPFR_INIT2, q.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (int) prec);
            }

            spmv_internal(sa, h_vals, h_x, r, prec, arena, isComplex);
            axpy_internal(r, negate(isComplex, (E)(Object)(isComplex?Complex.of(Real.ONE,Real.ZERO):Real.ONE)), h_b, n, prec, arena, isComplex);
            scale_internal(r, negate(isComplex, (E)(Object)(isComplex?Complex.of(Real.ONE,Real.ZERO):Real.ONE)), n, prec, arena, isComplex);
            
            copy_internal(p, r, n, isComplex);
            E rho = dot_internal(r, r, n, prec, arena, isComplex, ring);
            
            for (int k = 0; k < maxIterations; k++) {
                spmv_internal(sa, h_vals, p, q, prec, arena, isComplex);
                E pq = dot_internal(p, q, n, prec, arena, isComplex, ring);
                E alpha = divide(isComplex, rho, pq);
                
                axpy_internal(h_x, alpha, p, n, prec, arena, isComplex);
                axpy_internal(r, negate(isComplex, alpha), q, n, prec, arena, isComplex);
                
                E normR = norm_internal(r, n, prec, arena, isComplex, ring);
                if (compare(normR, tolerance) < 0) break;
                
                E rhoNew = dot_internal(r, r, n, prec, arena, isComplex, ring);
                E beta = divide(isComplex, rhoNew, rho);
                scale_internal(p, beta, n, prec, arena, isComplex);
                axpy_internal(p, (E)(Object)(isComplex?Complex.of(Real.ONE,Real.ZERO):Real.ONE), r, n, prec, arena, isComplex);
                rho = rhoNew;
            }
            return backToVector(h_x, n, isComplex, ring, arena);
        } catch (Throwable t) {
            throw new RuntimeException("Native MPFR Conjugate Gradient failed", t);
        }
    }

    @Override
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        int n = sa.rows();
        int prec = (int) getPrecision();
        boolean isComplex = isComplex(sa.getScalarRing());
        Ring<E> ring = sa.getScalarRing();
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_vals = initNativeValues(sa.getValues(), prec, arena, isComplex);
            MemorySegment h_b = initVector(b.toMatrix(), arena, prec, isComplex);
            MemorySegment h_x = initVector(x0.toMatrix(), arena, prec, isComplex);
            
            MemorySegment r0hat = arena.allocate(MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment r = arena.allocate(MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment p = arena.allocate(MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment v = arena.allocate(MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment s = arena.allocate(MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment t = arena.allocate(MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            
            for (int i=0; i<n*(isComplex?2:1); i++) {
                NativeSafe.invoke(MPFR_INIT2, r0hat.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (int) prec);
                NativeSafe.invoke(MPFR_INIT2, r.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (int) prec);
                NativeSafe.invoke(MPFR_INIT2, p.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (int) prec);
                NativeSafe.invoke(MPFR_INIT2, v.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (int) prec);
                NativeSafe.invoke(MPFR_INIT2, s.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (int) prec);
                NativeSafe.invoke(MPFR_INIT2, t.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (int) prec);
            }

            spmv_internal(sa, h_vals, h_x, r, prec, arena, isComplex);
            axpy_internal(r, negate(isComplex, (E)(Object)(isComplex?Complex.of(Real.ONE,Real.ZERO):Real.ONE)), h_b, n, prec, arena, isComplex);
            scale_internal(r, negate(isComplex, (E)(Object)(isComplex?Complex.of(Real.ONE,Real.ZERO):Real.ONE)), n, prec, arena, isComplex);
            copy_internal(r0hat, r, n, isComplex);
            
            E rho = (E)(Object)(isComplex?Complex.of(Real.ONE,Real.ZERO):Real.ONE);
            E alpha = (E)(Object)(isComplex?Complex.of(Real.ONE,Real.ZERO):Real.ONE);
            E omega = (E)(Object)(isComplex?Complex.of(Real.ONE,Real.ZERO):Real.ONE);
            
            for (int i = 0; i < maxIterations; i++) {
                E rhoNew = dot_internal(r0hat, r, n, prec, arena, isComplex, ring);
                E beta = multiply(isComplex, divide(isComplex, rhoNew, rho), divide(isComplex, alpha, omega));
                
                axpy_internal(p, negate(isComplex, omega), v, n, prec, arena, isComplex);
                scale_internal(p, beta, n, prec, arena, isComplex);
                axpy_internal(p, (E)(Object)(isComplex?Complex.of(Real.ONE,Real.ZERO):Real.ONE), r, n, prec, arena, isComplex);
                
                spmv_internal(sa, h_vals, p, v, prec, arena, isComplex);
                alpha = divide(isComplex, rhoNew, dot_internal(r0hat, v, n, prec, arena, isComplex, ring));
                
                copy_internal(s, r, n, isComplex);
                axpy_internal(s, negate(isComplex, alpha), v, n, prec, arena, isComplex);
                
                if (compare(norm_internal(s, n, prec, arena, isComplex, ring), tolerance) < 0) {
                    axpy_internal(h_x, alpha, p, n, prec, arena, isComplex);
                    break;
                }
                
                spmv_internal(sa, h_vals, s, t, prec, arena, isComplex);
                omega = divide(isComplex, dot_internal(t, s, n, prec, arena, isComplex, ring), dot_internal(t, t, n, prec, arena, isComplex, ring));
                
                axpy_internal(h_x, alpha, p, n, prec, arena, isComplex);
                axpy_internal(h_x, omega, s, n, prec, arena, isComplex);
                
                copy_internal(r, s, n, isComplex);
                axpy_internal(r, negate(isComplex, omega), t, n, prec, arena, isComplex);
                
                if (compare(norm_internal(r, n, prec, arena, isComplex, ring), tolerance) < 0) break;
                rho = rhoNew;
            }
            return backToVector(h_x, n, isComplex, ring, arena);
        } catch (Throwable th) {
            throw new RuntimeException("Native MPFR BiCGSTAB failed", th);
        }
    }

    @Override
    public Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        int n = sa.rows();
        int m = restarts;
        int prec = (int) getPrecision();
        boolean isComplex = isComplex(sa.getScalarRing());
        Ring<E> ring = sa.getScalarRing();
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_vals = initNativeValues(sa.getValues(), prec, arena, isComplex);
            MemorySegment h_x = initVector(x0.toMatrix(), arena, prec, isComplex);
            MemorySegment h_b = initVector(b.toMatrix(), arena, prec, isComplex);
            
            MemorySegment V = arena.allocate(MPFR_LAYOUT, (m + 1) * n * (isComplex ? 2 : 1));
            for (int i = 0; i < (m + 1) * n * (isComplex ? 2 : 1); i++) {
                NativeSafe.invoke(MPFR_INIT2, V.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (int) prec);
            }
            
            MemorySegment r = arena.allocate(MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            MemorySegment w = arena.allocate(MPFR_LAYOUT, n * (isComplex ? 2 : 1));
            for (int i = 0; i < n * (isComplex ? 2 : 1); i++) {
                NativeSafe.invoke(MPFR_INIT2, r.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (int) prec);
                NativeSafe.invoke(MPFR_INIT2, w.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (int) prec);
            }

            for (int iter = 0; iter < maxIterations / m + 1; iter++) {
                spmv_internal(sa, h_vals, h_x, r, prec, arena, isComplex);
                axpy_internal(r, negate(isComplex, (E)(Object)(isComplex?Complex.of(Real.ONE, Real.ZERO):Real.ONE)), h_b, n, prec, arena, isComplex);
                scale_internal(r, negate(isComplex, (E)(Object)(isComplex?Complex.of(Real.ONE, Real.ZERO):Real.ONE)), n, prec, arena, isComplex);
                
                E beta = norm_internal(r, n, prec, arena, isComplex, ring);
                if (compare(beta, tolerance) < 0) break;
                
                E invBeta = divide(isComplex, (E)(Object)(isComplex?Complex.of(Real.ONE, Real.ZERO):Real.ONE), beta);
                copy_internal(V.asSlice(0, n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize()), r, n, isComplex);
                scale_internal(V.asSlice(0, n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize()), invBeta, n, prec, arena, isComplex);
                
            // Create H with explicit interface type to avoid ArrayStoreException when storing NativeRealBig
            E[][] H = (E[][]) java.lang.reflect.Array.newInstance(Object.class, m + 1, m);
            for (int i = 0; i <= m; i++) {
                H[i] = (E[]) java.lang.reflect.Array.newInstance(Object.class, m);
            }
                for (int i = 0; i <= m; i++) {
                    java.util.Arrays.fill(H[i], ring.zero());
                }
                
                int k;
                for (k = 0; k < m; k++) {
                    MemorySegment vk = V.asSlice(k * n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize(), n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize());
                    spmv_internal(sa, h_vals, vk, w, prec, arena, isComplex);
                    
                    for (int j = 0; j <= k; j++) {
                        MemorySegment vj = V.asSlice(j * n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize(), n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize());
                        H[j][k] = dot_internal(vj, w, n, prec, arena, isComplex, ring);
                        axpy_internal(w, negate(isComplex, H[j][k]), vj, n, prec, arena, isComplex);
                    }
                    
                    H[k + 1][k] = norm_internal(w, n, prec, arena, isComplex, ring);
                    
                    if (compare(H[k + 1][k], tolerance) < 0) {
                        solveSmallAndCheck(H, beta, V, h_x, n, k + 1, isComplex, ring, prec, arena);
                        return backToVector(h_x, n, isComplex, ring, arena);
                    }
                    
                    E invH = divide(isComplex, (E)(Object)(isComplex?Complex.of(Real.ONE, Real.ZERO):Real.ONE), H[k + 1][k]);
                    MemorySegment vkplus1 = V.asSlice((k + 1) * n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize(), n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize());
                    copy_internal(vkplus1, w, n, isComplex);
                    scale_internal(vkplus1, invH, n, prec, arena, isComplex);
                }
                
                solveSmallAndCheck(H, beta, V, h_x, n, m, isComplex, ring, prec, arena);
                
                spmv_internal(sa, h_vals, h_x, r, prec, arena, isComplex);
                axpy_internal(r, negate(isComplex, (E)(Object)(isComplex?Complex.of(Real.ONE, Real.ZERO):Real.ONE)), h_b, n, prec, arena, isComplex);
                scale_internal(r, negate(isComplex, (E)(Object)(isComplex?Complex.of(Real.ONE, Real.ZERO):Real.ONE)), n, prec, arena, isComplex);
                if (compare(norm_internal(r, n, prec, arena, isComplex, ring), tolerance) < 0) break;
            }
            
            return backToVector(h_x, n, isComplex, ring, arena);
        } catch (Throwable th) {
            String msg = "Native MPFR GMRES failed at iteration " + (maxIterations / m + 1);
            logger.error(msg, th);
            throw new RuntimeException(msg + ": " + th.getMessage(), th);
        }
    }

    private void solveSmallAndCheck(E[][] H, E beta, MemorySegment V, MemorySegment h_x, int n, int k, boolean isComplex, Ring<E> ring, int prec, Arena arena) throws Throwable {
        Matrix<E> hMat = Matrix.of(java.util.Arrays.stream(H).limit(k + 1).map(row -> java.util.Arrays.asList(row).subList(0, k)).collect(java.util.stream.Collectors.toList()), ring);
        
        E[] e1Data = (E[]) java.lang.reflect.Array.newInstance(Object.class, k + 1);
        java.util.Arrays.fill(e1Data, ring.zero());
        e1Data[0] = beta;
        Vector<E> e1 = Vector.of(e1Data, ring);
        
        // Use GenericQR for small Hessenberg system solve as Native Dense doesn't override qr() yet.
        if (hMat == null || e1 == null) throw new NullPointerException("hMat or e1 is null in solveSmallAndCheck");
        QRResult<E> qr = GenericQR.decompose(hMat, (Field<E>) ring, (LinearAlgebraProvider<E>) SHARED_DENSE);
        Vector<E> y = GenericQR.solve(qr, e1, (Field<E>) ring, (LinearAlgebraProvider<E>) SHARED_DENSE);
        
        for (int j = 0; j < k; j++) {
            MemorySegment vj = V.asSlice(j * n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize(), n * (isComplex ? 2 : 1) * MPFR_LAYOUT.byteSize());
            axpy_internal(h_x, y.get(j), vj, n, prec, arena, isComplex);
        }
    }

    private int compare(E a, E b) {
        if (a instanceof Real ra && b instanceof Real rb) return ra.compareTo(rb);
        if (a instanceof Complex ca && b instanceof Complex cb) return ca.abs().compareTo(cb.abs());
        
        // Mixed comparison via absolute values
        Real absA = (a instanceof Complex ca) ? ca.abs() : (Real) a;
        Real absB = (b instanceof Complex cb) ? cb.abs() : (Real) b;
        return absA.compareTo(absB);
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
        E[] resultArr = (E[]) new Object[n];
        boolean useRealDouble = !isComplex && ring instanceof org.episteme.core.mathematics.sets.Reals;
        for (int i = 0; i < n; i++) {
            if (isComplex) {
                Real re = readMPFR(h_x.asSlice(i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), arena);
                Real im = readMPFR(h_x.asSlice((i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), arena);
                resultArr[i] = (E) (Object) Complex.of(re, im);
            } else {
                Real res = readMPFR(h_x.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), arena);
                if (useRealDouble) {
                    resultArr[i] = (E) (Object) org.episteme.core.mathematics.numbers.real.RealDouble.create(res.doubleValue());
                } else {
                    resultArr[i] = (E) (Object) res;
                }
            }
        }
        return Vector.of(java.util.Arrays.asList(resultArr), ring);
    }



    private org.episteme.core.mathematics.numbers.complex.Complex complexTranscendental(org.episteme.core.mathematics.numbers.complex.Complex z, String op, int prec, Arena arena, Object... args) {
         try {
             MemorySegment resR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, resR, prec);
             MemorySegment resI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, resI, prec);
             MemorySegment aR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, aR, prec);
             MemorySegment aI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, aI, prec);
             
             NativeSafe.invoke(MPFR_SET_STR, aR, arena.allocateFrom(z.getReal().bigDecimalValue().toPlainString()), 10, 0);
             NativeSafe.invoke(MPFR_SET_STR, aI, arena.allocateFrom(z.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
             
             
             switch (op.toLowerCase()) {
                 case "exp" -> NativeMPFRDenseLinearAlgebraBackend.complexExp(resR, resI, aR, aI, prec, arena);
                 case "log" -> NativeMPFRDenseLinearAlgebraBackend.complexLog(resR, resI, aR, aI, prec, arena);
                 case "log10" -> NativeMPFRDenseLinearAlgebraBackend.complexLog10(resR, resI, aR, aI, prec, arena);
                 case "sin" -> NativeMPFRDenseLinearAlgebraBackend.complexSin(resR, resI, aR, aI, prec, arena);
                 case "cos" -> NativeMPFRDenseLinearAlgebraBackend.complexCos(resR, resI, aR, aI, prec, arena);
                 case "tan" -> NativeMPFRDenseLinearAlgebraBackend.complexTan(resR, resI, aR, aI, prec, arena);
                 case "asin" -> NativeMPFRDenseLinearAlgebraBackend.complexAsin(resR, resI, aR, aI, prec, arena);
                 case "acos" -> NativeMPFRDenseLinearAlgebraBackend.complexAcos(resR, resI, aR, aI, prec, arena);
                 case "atan" -> NativeMPFRDenseLinearAlgebraBackend.complexAtan(resR, resI, aR, aI, prec, arena);
                 case "sinh" -> NativeMPFRDenseLinearAlgebraBackend.complexSinh(resR, resI, aR, aI, prec, arena);
                 case "cosh" -> NativeMPFRDenseLinearAlgebraBackend.complexCosh(resR, resI, aR, aI, prec, arena);
                 case "tanh" -> NativeMPFRDenseLinearAlgebraBackend.complexTanh(resR, resI, aR, aI, prec, arena);
                 case "sqrt" -> NativeMPFRDenseLinearAlgebraBackend.complexSqrt(resR, resI, aR, aI, prec, arena);
                 case "cbrt" -> NativeMPFRDenseLinearAlgebraBackend.complexCbrt(resR, resI, aR, aI, prec, arena);
                 case "pow" -> {
                     if (args.length > 0 && args[0] instanceof org.episteme.core.mathematics.numbers.complex.Complex exp) {
                         MemorySegment eR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, eR, prec);
                         MemorySegment eI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, eI, prec);
                         NativeSafe.invoke(MPFR_SET_STR, eR, arena.allocateFrom(exp.getReal().bigDecimalValue().toPlainString()), 10, 0);
                         NativeSafe.invoke(MPFR_SET_STR, eI, arena.allocateFrom(exp.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
                         NativeMPFRDenseLinearAlgebraBackend.complexPow(resR, resI, aR, aI, eR, eI, prec, arena);
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

    private Real realTranscendental(Real v, String op, int prec, Arena arena, Object... args) {
        try {
            MemorySegment res = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, res, (int) prec);
            MemorySegment val = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, val, (int) prec);
            NativeSafe.invoke(MPFR_SET_STR, val, arena.allocateFrom(v.bigDecimalValue().toPlainString()), 10, 0);
            
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
                MemorySegment e = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, e, (int) prec);
                NativeSafe.invoke(MPFR_SET_STR, e, arena.allocateFrom(exp.bigDecimalValue().toPlainString()), 10, 0);
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
        int prec = (int) getPrecision();
        try (Arena arena = Arena.ofConfined()) {
            boolean isComplex = isComplex(v.getScalarRing());
            MemorySegment h_v = initVector(v.toMatrix(), arena, (int) prec, isComplex);
            MemorySegment res = arena.allocate(MPFR_LAYOUT, isComplex ? 2 : 1);
            for (int i=0; i<(isComplex?2:1); i++) NativeSafe.invoke(MPFR_INIT2, res.asSlice(i*MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), (int) prec);
            dotProduct(h_v, h_v, v.dimension(), res, (int) prec, arena, isComplex);
            MemorySegment realPart = res.asSlice(0, MPFR_LAYOUT.byteSize());
            if (isComplex) {
                // For complex norm, we have |z|^2 = re^2 + im^2 in the real part if dotProduct is adjusted,
                // or we need to sum re and im if they are separated.
                // dotProduct for complex (aR-i*aI)*(aR+i*aI) = aR^2 + aI^2, which is stored in sumR.
            }
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

    @Override public E determinant(Matrix<E> a) { return GenericLU.determinant(a, (Field<E>) a.getScalarRing(), this); }
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
        return GenericLU.inverse(a, (Field<E>) a.getScalarRing(), this);
    }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<E> lu(Matrix<E> a) { return GenericLU.decompose(a, (Field<E>) a.getScalarRing(), this); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<E> qr(Matrix<E> a) { return GenericQR.decompose(a, (Field<E>) a.getScalarRing(), this); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<E> svd(Matrix<E> a) { return GenericSVD.decompose(a, (Field<E>) a.getScalarRing(), this); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<E> cholesky(Matrix<E> a) { return GenericCholesky.decompose(a, (Field<E>) a.getScalarRing(), this); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<E> eigen(Matrix<E> a) { return GenericEigen.decompose(a, (Field<E>) a.getScalarRing(), this); }
}
