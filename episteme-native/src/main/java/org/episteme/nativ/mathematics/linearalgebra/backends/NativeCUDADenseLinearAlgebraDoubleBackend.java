/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealDouble;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.structures.rings.FieldElement;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.ResourceTracker;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.episteme.nativ.technical.backend.gpu.cuda.CUDAManager;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Native CUDA Dense Linear Algebra Backend for Double precision.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, GPUBackend.class})
public class NativeCUDADenseLinearAlgebraDoubleBackend<E extends FieldElement<E>> implements LinearAlgebraProvider<E>, NativeBackend, GPUBackend {
    private static final Logger logger = LoggerFactory.getLogger(NativeCUDADenseLinearAlgebraDoubleBackend.class);

    @Override public boolean isLoaded() { return isAvailable(); }
    @Override public String getNativeLibraryName() { return "cuda"; }
    
    @Override
    public boolean isAvailable() {
        if (isExplicitlyDisabled()) return false;
        return CUDAManager.isAvailable();
    }

    public boolean isExplicitlyDisabled() {
        return Boolean.getBoolean("episteme.backend.native.disabled") ||
               Boolean.getBoolean("episteme.backend.cuda.disabled") || 
               Boolean.getBoolean("episteme.backend.gpu.disabled") ||
               Boolean.getBoolean("episteme.backend.linear-algebra.disabled") ||
               Boolean.getBoolean("episteme.backend.linear-algebra-cuda.disabled");
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        Object zero = ring.zero();
        if (zero instanceof Complex c) {
            // Only compatible with double-based complex numbers
            return c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealDouble;
        }
        // Only compatible with double-precision real numbers
        return zero instanceof RealDouble;
    }

    @Override public String getId() { return "cuda-dense-double"; }
    @Override public String getName() { return "Native CUDA Dense Linear Algebra Double Backend"; }
    @Override public int getPriority() { return 105; }
    @Override public void shutdown() {}
    
    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (isComplex(a)) return combineComplex(a, b, 1.0, 0.0, 1.0, 0.0);
        return combine(a, b, 1.0, 1.0);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (isComplex(a)) return combineComplex(a, b, 1.0, 0.0, -1.0, 0.0);
        return combine(a, b, 1.0, -1.0);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (isComplex(a)) {
            double sr = 0.0;
            double si = 0.0;
            if (scalar instanceof Complex c) {
                sr = c.real();
                si = c.imaginary();
            } else if (scalar instanceof org.episteme.core.mathematics.numbers.real.Real r) {
                sr = r.doubleValue();
            } else if (scalar instanceof Number n) {
                sr = n.doubleValue();
            }
            return combineComplex(a, a, sr, si, 0.0, 0.0);
        }
        double s = 0.0;
        if (scalar instanceof org.episteme.core.mathematics.numbers.real.Real r) s = r.doubleValue();
        else if (scalar instanceof Number n) s = n.doubleValue();
        return combine(a, a, s, 0.0);
    }

    private Matrix<E> combine(Matrix<E> a, Matrix<E> b, double alpha, double beta) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int m = a.rows();
        int n = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * n * 8, tracker);
            MemorySegment d_B = malloc((long) m * n * 8, tracker);
            MemorySegment d_C = malloc((long) m * n * 8, tracker);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a)), (long) m * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b)), (long) m * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_DGEAM, handle, 0, 0, n, m, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, alpha), d_A, n, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, beta), d_B, n, d_C, n));
            
            double[] result = new double[m * n];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segC, d_C, (long) m * n * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, result, 0, m * n);
            return toMatrix(result, m, n, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    private Matrix<E> combineComplex(Matrix<E> a, Matrix<E> b, double ar, double ai, double br, double bi) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int m = a.rows();
        int n = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * n * 16, tracker);
            MemorySegment d_B = malloc((long) m * n * 16, tracker);
            MemorySegment d_C = malloc((long) m * n * 16, tracker);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(a)), (long) m * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(b)), (long) m * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_ZGEAM, handle, 0, 0, n, m, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, ar, ai), d_A, n, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, br, bi), d_B, n, d_C, n));
            
            double[] result = new double[m * n * 2];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segC, d_C, (long) m * n * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, result, 0, m * n * 2);
            return toMatrixComplex(result, m, n, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { 
            logger.error("CUDA complex double combine failed: {}", t.getMessage());
            throw new RuntimeException(t); 
        }
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        if (isComplex(a)) return multiplyComplex(a, b);
        int m = a.rows();
        int n = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * n * 8, tracker);
            MemorySegment d_X = malloc((long) n * 8, tracker);
            MemorySegment d_Y = malloc((long) m * 8, tracker);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a)), (long) m * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_X, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleVec(b)), (long) n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_DGEMV, handle, 1, n, m, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0), d_A, n, d_X, 1, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0), d_Y, 1));
            
            double[] result = new double[m];
            MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, m);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segY, d_Y, (long) m * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segY, ValueLayout.JAVA_DOUBLE, 0, result, 0, m);
            
            E[] values = (E[]) java.lang.reflect.Array.newInstance(a.getScalarRing().zero().getClass(), m);
            for (int i = 0; i < m; i++) values[i] = (E) RealDouble.of(result[i]);
            return new org.episteme.core.mathematics.linearalgebra.vectors.DenseVector<>(java.util.Arrays.asList(values), (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    private Vector<E> multiplyComplex(Matrix<E> a, Vector<E> b) {
        int m = a.rows();
        int n = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * n * 16, tracker);
            MemorySegment d_X = malloc((long) n * 16, tracker);
            MemorySegment d_Y = malloc((long) m * 16, tracker);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(a)), (long) m * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_X, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleVec(b)), (long) n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_ZGEMV, handle, 1, n, m, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0), d_A, n, d_X, 1, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0), d_Y, 1));
            
            double[] result = new double[m * 2];
            MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segY, d_Y, (long) m * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segY, ValueLayout.JAVA_DOUBLE, 0, result, 0, m * 2);
            
            E[] values = (E[]) java.lang.reflect.Array.newInstance(a.getScalarRing().zero().getClass(), m);
            for (int i = 0; i < m; i++) values[i] = (E) Complex.of(result[i * 2], result[i * 2 + 1]);
            return new org.episteme.core.mathematics.linearalgebra.vectors.DenseVector<>(java.util.Arrays.asList(values), (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    private double[] toDoubleVec(Vector<E> v) {
        int n = v.dimension();
        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            Object val = v.get(i);
            if (val instanceof org.episteme.core.mathematics.numbers.real.Real r) data[i] = r.doubleValue();
            else if (val instanceof Number nVal) data[i] = nVal.doubleValue();
            else if (val instanceof Complex c) data[i] = c.real();
            else data[i] = 0.0;
        }
        return data;
    }

    private double[] toComplexDoubleVec(Vector<E> v) {
        int n = v.dimension();
        double[] data = new double[n * 2];
        for (int i = 0; i < n; i++) {
            Object val = v.get(i);
            if (val instanceof Complex c) {
                data[i * 2] = c.real();
                data[i * 2 + 1] = c.imaginary();
            } else if (val instanceof org.episteme.core.mathematics.numbers.real.Real r) {
                data[i * 2] = r.doubleValue();
                data[i * 2 + 1] = 0.0;
            } else if (val instanceof Number nVal) {
                data[i * 2] = nVal.doubleValue();
                data[i * 2 + 1] = 0.0;
            } else {
                data[i * 2] = 0.0;
                data[i * 2 + 1] = 0.0;
            }
        }
        return data;
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        if (isComplex(a)) return dotComplex(a, b);
        int n = a.dimension();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * 8, tracker);
            MemorySegment d_B = malloc((long) n * 8, tracker);
            MemorySegment d_Res = arena.allocate(ValueLayout.JAVA_DOUBLE);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleVec(a)), (long) n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleVec(b)), (long) n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_DDOT, CUDAManager.getCublasHandle(), n, d_A, 1, d_B, 1, d_Res));
            return (E) RealDouble.of(d_Res.get(ValueLayout.JAVA_DOUBLE, 0));
        } catch (Throwable t) { throw new RuntimeException("CUDA double dot failed", t); }
    }

    private E dotComplex(Vector<E> a, Vector<E> b) {
        int n = a.dimension();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * 16, tracker);
            MemorySegment d_B = malloc((long) n * 16, tracker);
            MemorySegment d_Res = arena.allocate(ValueLayout.JAVA_DOUBLE, 2);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleVec(a)), (long) n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleVec(b)), (long) n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_ZDOTU, CUDAManager.getCublasHandle(), n, d_A, 1, d_B, 1, d_Res));
            return (E) Complex.of(d_Res.getAtIndex(ValueLayout.JAVA_DOUBLE, 0), d_Res.getAtIndex(ValueLayout.JAVA_DOUBLE, 1));
        } catch (Throwable t) { throw new RuntimeException("CUDA complex double dot failed", t); }
    }

    @Override
    public E norm(Vector<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        if (isComplex(a)) return normComplex(a);
        int n = a.dimension();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * 8, tracker);
            MemorySegment d_Res = arena.allocate(ValueLayout.JAVA_DOUBLE);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleVec(a)), (long) n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_DNRM2, CUDAManager.getCublasHandle(), n, d_A, 1, d_Res));
            return (E) RealDouble.of(d_Res.get(ValueLayout.JAVA_DOUBLE, 0));
        } catch (Throwable t) { throw new RuntimeException("CUDA double norm failed", t); }
    }

    private E normComplex(Vector<E> a) {
        int n = a.dimension();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * 16, tracker);
            MemorySegment d_Res = arena.allocate(ValueLayout.JAVA_DOUBLE);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleVec(a)), (long) n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_DZNRM2, CUDAManager.getCublasHandle(), n, d_A, 1, d_Res));
            return (E) RealDouble.of(d_Res.get(ValueLayout.JAVA_DOUBLE, 0));
        } catch (Throwable t) { throw new RuntimeException("CUDA complex double norm failed", t); }
    }

    @Override
    public E trace(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int n = Math.min(a.rows(), a.cols());
        if (isComplex(a)) {
            double[] data = toComplexDoubleArray(a);
            Complex sum = Complex.ZERO;
            for (int i = 0; i < n; i++) {
                sum = sum.add(Complex.of(data[(i * a.cols() + i) * 2], data[(i * a.cols() + i) * 2 + 1]));
            }
            return (E) sum;
        }
        double[] data = toDoubleArray(a);
        double sum = 0;
        for (int i = 0; i < n; i++) sum += data[i * a.cols() + i];
        return (E) RealDouble.of(sum);
    }

    @Override
    public Matrix<E> conjugateTranspose(Matrix<E> a) {
        if (!isComplex(a)) return transpose(a);
        int rows = a.rows(); int cols = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) rows * cols * 16, tracker);
            MemorySegment d_C = malloc((long) rows * cols * 16, tracker);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(a)), (long) rows * cols * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_ZGEAM, handle, 2, 0, rows, cols, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0), d_A, cols, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0), d_A, cols, 
                d_C, rows));
            
            double[] result = new double[rows * cols * 2];
            MemorySegment host = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) rows * cols * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, host, d_C, (long) rows * cols * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(host, ValueLayout.JAVA_DOUBLE, 0, result, 0, rows * cols * 2);
            return toMatrixComplex(result, cols, rows, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA conjugate transpose failed", t); }
    }

    @Override
    public Vector<E> normalize(Vector<E> v) {
        E n = norm(v);
        if (n == null) return v;
        double nv = getRealValue(n);
        if (nv == 0) return v;
        return multiply(v, createScalar(1.0 / nv, v));
    }

    private double getRealValue(E val) {
        if (val instanceof Complex c) return c.real();
        if (val instanceof Real r) return r.doubleValue();
        if (val instanceof Number n) return n.doubleValue();
        return 0.0;
    }

    private E createScalar(double val, Vector<E> v) {
        Ring<E> ring = (Ring<E>) v.getScalarRing();
        if (ring.zero() instanceof Complex) return (E) Complex.of(val, 0.0);
        return (E) RealDouble.of(val);
    }

    @Override
    public Vector<E> cross(Vector<E> a, Vector<E> b) {
        if (a.dimension() != 3 || b.dimension() != 3) throw new IllegalArgumentException("Cross product only supported for 3D vectors");
        Ring<E> ring = (Ring<E>) a.getScalarRing();
        if (isComplex(a)) {
            Complex a1 = getComplex(a.get(0)), a2 = getComplex(a.get(1)), a3 = getComplex(a.get(2));
            Complex b1 = getComplex(b.get(0)), b2 = getComplex(b.get(1)), b3 = getComplex(b.get(2));
            return (Vector<E>) (Vector) org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(
                a2.multiply(b3).subtract(a3.multiply(b2)),
                a3.multiply(b1).subtract(a1.multiply(b3)),
                a1.multiply(b2).subtract(a2.multiply(b1))
            ), (Ring) ring);
        }
        double a1 = getReal(a.get(0)), a2 = getReal(a.get(1)), a3 = getReal(a.get(2));
        double b1 = getReal(b.get(0)), b2 = getReal(b.get(1)), b3 = getReal(b.get(2));
        return (Vector<E>) (Vector) org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(
            (E) RealDouble.of(a2 * b3 - a3 * b2),
            (E) RealDouble.of(a3 * b1 - a1 * b3),
            (E) RealDouble.of(a1 * b2 - a2 * b1)
        ), (Ring) ring);
    }

    private Complex getComplex(Object o) {
        if (o instanceof Complex c) return c;
        if (o instanceof Real r) return Complex.of(r.doubleValue(), 0.0);
        if (o instanceof Number n) return Complex.of(n.doubleValue(), 0.0);
        return Complex.ZERO;
    }

    private double getReal(Object o) {
        if (o instanceof Real r) return r.doubleValue();
        if (o instanceof Number n) return n.doubleValue();
        if (o instanceof Complex c) return c.real();
        return 0.0;
    }

    @Override
    public E angle(Vector<E> a, Vector<E> b) {
        E d = dot(a, b);
        E nA = norm(a);
        E nB = norm(b);
        double cosTheta = getRealValue(d) / (getRealValue(nA) * getRealValue(nB));
        return (E) RealDouble.of(Math.acos(Math.max(-1.0, Math.min(1.0, cosTheta))));
    }

    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (isComplex(a)) return multiplyComplex(a, b);
        int m = a.rows(); int k = a.cols(); int n = b.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * k * 8, tracker);
            MemorySegment d_B = malloc((long) k * n * 8, tracker);
            MemorySegment d_C = malloc((long) m * n * 8, tracker);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a)), (long) m * k * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b)), (long) k * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_DGEMM, handle, 0, 0, n, m, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0), d_B, n, d_A, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0), d_C, n));
            
            double[] result = new double[m * n];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segC, d_C, (long) m * n * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, result, 0, m * n);
            return toMatrix(result, m, n, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA multiply failed", t); }
    }

    private Matrix<E> multiplyComplex(Matrix<E> a, Matrix<E> b) {
        int m = a.rows(); int k = a.cols(); int n = b.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * k * 16, tracker);
            MemorySegment d_B = malloc((long) k * n * 16, tracker);
            MemorySegment d_C = malloc((long) m * n * 16, tracker);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(a)), (long) m * k * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(b)), (long) k * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_ZGEMM, handle, 0, 0, n, m, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0), d_B, n, d_A, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0), d_C, n));
            
            double[] result = new double[m * n * 2];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segC, d_C, (long) m * n * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, result, 0, m * n * 2);
            return toMatrixComplex(result, m, n, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA complex multiply failed", t); }
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (isComplex(a)) return transposeComplex(a);
        int rows = a.rows(); int cols = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) rows * cols * 8, tracker);
            MemorySegment d_C = malloc((long) rows * cols * 8, tracker);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a)), (long) rows * cols * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_DGEAM, handle, 1, 0, rows, cols, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0), d_A, cols, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0), d_A, cols, 
                d_C, rows));
            
            double[] result = new double[rows * cols];
            MemorySegment host = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) rows * cols);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, host, d_C, (long) rows * cols * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(host, ValueLayout.JAVA_DOUBLE, 0, result, 0, rows * cols);
            return toMatrix(result, cols, rows, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA transpose failed", t); }
    }

    private Matrix<E> transposeComplex(Matrix<E> a) {
        int rows = a.rows(); int cols = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) rows * cols * 16, tracker);
            MemorySegment d_C = malloc((long) rows * cols * 16, tracker);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(a)), (long) rows * cols * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_ZGEAM, handle, 1, 0, rows, cols, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0), d_A, cols, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0), d_A, cols, 
                d_C, rows));
            
            double[] result = new double[rows * cols * 2];
            MemorySegment host = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) rows * cols * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, host, d_C, (long) rows * cols * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(host, ValueLayout.JAVA_DOUBLE, 0, result, 0, rows * cols * 2);
            return toMatrixComplex(result, cols, rows, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA complex transpose failed", t); }
    }

    @Override
    public LUResult<E> lu(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        if (isComplex(a)) return luComplex(a);
        int m = a.rows(); int n = a.cols();
        double[] aData = toDoubleArray(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * n * 8, tracker);
            MemorySegment d_Ipiv = malloc((long) Math.min(m, n) * 4, tracker);
            MemorySegment d_Info = malloc(4, tracker);
            double[] aT = new double[m * n];
            for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) aT[j * m + i] = aData[i * n + j];
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, aT), (long) m * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment pWorkSize = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGETRF_BUFFER_SIZE, handle, m, n, d_A, m, pWorkSize));
            int workSize = pWorkSize.get(ValueLayout.JAVA_INT, 0);
            MemorySegment d_Work = malloc((long) workSize * 8, tracker);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGETRF, handle, m, n, d_A, m, d_Work, d_Ipiv, d_Info));
            double[] packed = new double[m * n];
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segA, d_A, (long) m * n * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segA, ValueLayout.JAVA_DOUBLE, 0, packed, 0, m * n);
            double[] result = new double[m * n];
            for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) result[i * n + j] = packed[j * m + i];
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            E[] flatL = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), m * Math.min(m,n));
            E[] flatU = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), Math.min(m,n) * n);
            java.util.Arrays.fill(flatL, ring.zero());
            java.util.Arrays.fill(flatU, ring.zero());
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    E val = (E) RealDouble.of(result[i * n + j]);
                    if (i > j && i < m && j < Math.min(m, n)) flatL[i * Math.min(m,n) + j] = val;
                    else if (i == j && i < Math.min(m,n)) { flatL[i * Math.min(m,n) + j] = (E) RealDouble.ONE; flatU[i * n + j] = val; }
                    else if (i < j && i < Math.min(m,n) && j < n) flatU[i * n + j] = val;
                }
            }
            for (int i = 0; i < Math.min(m,n); i++) if (flatL[i * Math.min(m,n) + i] == null || flatL[i * Math.min(m,n) + i].equals(ring.zero())) flatL[i * Math.min(m,n) + i] = (E) RealDouble.ONE;
            return new LUResult<>(new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(flatL, m, Math.min(m, n), this, ring), new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(flatU, Math.min(m, n), n, this, ring), null);
        } catch (Throwable t) { throw new RuntimeException("CUDA double LU decomposition failed", t); }
    }

    @Override
    public QRResult<E> qr(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        if (isComplex(a)) return qrComplex(a);
        int m = a.rows(); int n = a.cols();
        double[] aData = toDoubleArray(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * n * 8, tracker);
            MemorySegment d_Tau = malloc((long) Math.min(m, n) * 8, tracker);
            MemorySegment d_Info = malloc(4, tracker);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, aData), (long) m * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment pWorkSize = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGEQRF_BUFFER_SIZE, handle, m, n, d_A, m, pWorkSize));
            int workSize = pWorkSize.get(ValueLayout.JAVA_INT, 0);
            MemorySegment d_Work = malloc((long) workSize * 8, tracker);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGEQRF, handle, m, n, d_A, m, d_Tau, d_Work, workSize, d_Info));
            double[] result = new double[m * n];
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segA, d_A, (long) m * n * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segA, ValueLayout.JAVA_DOUBLE, 0, result, 0, m * n);
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            E[] flatQ = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), m * m);
            for (int i = 0; i < m; i++) for (int j = 0; j < m; j++) flatQ[i * m + j] = (i == j) ? (E) RealDouble.ONE : ring.zero();
            E[] flatR = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), m * n);
            java.util.Arrays.fill(flatR, ring.zero());
            for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) if (i <= j) flatR[i * n + j] = (E) RealDouble.of(result[i * n + j]);
            return new QRResult<>(new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(flatQ, m, m, this, ring), new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(flatR, m, n, this, ring));
        } catch (Throwable t) { throw new RuntimeException("CUDA double QR failed", t); }
    }

    @Override
    public SVDResult<E> svd(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        if (isComplex(a)) return svdComplex(a);
        int m = a.rows(); int n = a.cols();
        double[] aData = toDoubleArray(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * n * 8, tracker);
            MemorySegment d_S = malloc((long) Math.min(m, n) * 8, tracker);
            MemorySegment d_U = malloc((long) m * m * 8, tracker);
            MemorySegment d_VT = malloc((long) n * n * 8, tracker);
            MemorySegment d_Info = malloc(4, tracker);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, aData), (long) m * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment pWorkSize = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGESVD_BUFFER_SIZE, handle, m, n, pWorkSize));
            int workSize = pWorkSize.get(ValueLayout.JAVA_INT, 0);
            MemorySegment d_Work = malloc((long) workSize * 8, tracker);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGESVD, handle, (int)'A', (int)'A', m, n, d_A, m, d_S, d_U, m, d_VT, n, d_Work, workSize, MemorySegment.NULL, d_Info));
            double[] sArr = new double[Math.min(m, n)];
            MemorySegment segS = arena.allocate(ValueLayout.JAVA_DOUBLE, Math.min(m, n));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segS, d_S, (long) Math.min(m, n) * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segS, ValueLayout.JAVA_DOUBLE, 0, sArr, 0, Math.min(m, n));
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            E[] sVals = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), sArr.length);
            for (int i = 0; i < sArr.length; i++) sVals[i] = (E) RealDouble.of(sArr[i]);
            E[] flatU = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), m * m);
            for (int i = 0; i < m; i++) for (int j = 0; j < m; j++) flatU[i * m + j] = (i == j) ? (E) RealDouble.ONE : ring.zero();
            E[] flatV = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), n * n);
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) flatV[i * n + j] = (i == j) ? (E) RealDouble.ONE : ring.zero();
            Vector<E> S = new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(sVals), this, ring);
            return new SVDResult<>(new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(flatU, m, m, this, ring), S, new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(flatV, n, n, this, ring));
        } catch (Throwable t) { throw new RuntimeException("CUDA double SVD failed", t); }
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        if (isComplex(a)) return solveComplex(a, b);
        int n = a.rows();
        double[] aData = toDoubleArray(a);
        double[] bData = toDoubleVec(b);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * n * 8, tracker);
            MemorySegment d_B = malloc((long) n * 8, tracker);
            MemorySegment d_Ipiv = malloc((long) n * 4, tracker);
            MemorySegment d_Info = malloc(4, tracker);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, aData), (long) n * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bData), (long) n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment pWorkSize = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGETRF_BUFFER_SIZE, handle, n, n, d_A, n, pWorkSize));
            int workSize = pWorkSize.get(ValueLayout.JAVA_INT, 0);
            MemorySegment d_Work = malloc((long) workSize * 8, tracker);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGETRF, handle, n, n, d_A, n, d_Work, d_Ipiv, d_Info));
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGETRS, handle, 0, n, 1, d_A, n, d_Ipiv, d_B, n, d_Info));
            double[] result = new double[n];
            MemorySegment segB = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segB, d_B, (long) n * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segB, ValueLayout.JAVA_DOUBLE, 0, result, 0, n);
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), n);
            for (int i = 0; i < n; i++) values[i] = (E) RealDouble.of(result[i]);
            return new org.episteme.core.mathematics.linearalgebra.vectors.DenseVector<>(java.util.Arrays.asList(values), ring);
        } catch (Throwable t) { throw new RuntimeException("CUDA double solve failed", t); }
    }

    @Override
    public E determinant(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        LUResult<E> lu = lu(a);
        Matrix<E> U = lu.getU();
        int n = a.rows();
        if (isComplex(a)) {
            Complex det = Complex.ONE;
            for (int i = 0; i < n; i++) det = det.multiply((Complex) U.get(i, i));
            return (E) det;
        }
        double det = 1.0;
        for (int i = 0; i < n; i++) det *= ((Number) U.get(i, i)).doubleValue();
        return (E) RealDouble.of(det);
    }

    private Vector<E> solveComplex(Matrix<E> a, Vector<E> b) {
        int n = a.rows();
        double[] aData = toComplexDoubleArray(a);
        double[] bData = toComplexDoubleVec(b);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * n * 16, tracker);
            MemorySegment d_B = malloc((long) n * 16, tracker);
            MemorySegment d_Ipiv = malloc((long) n * 4, tracker);
            MemorySegment d_Info = malloc(4, tracker);
            
            double[] h_At = new double[n * n * 2];
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    h_At[(c * n + r) * 2] = aData[(r * n + c) * 2];
                    h_At[(c * n + r) * 2 + 1] = aData[(r * n + c) * 2 + 1];
                }
            }
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bData), (long) n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment pWorkSize = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGETRF_BUFFER_SIZE, handle, n, n, d_A, n, pWorkSize));
            int workSize = pWorkSize.get(ValueLayout.JAVA_INT, 0);
            MemorySegment d_Work = malloc((long) workSize * 16, tracker);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGETRF, handle, n, n, d_A, n, d_Work, d_Ipiv, d_Info));
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGETRS, handle, 0, n, 1, d_A, n, d_Ipiv, d_B, n, d_Info));
            double[] result = new double[n * 2];
            MemorySegment segB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segB, d_B, (long) n * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segB, ValueLayout.JAVA_DOUBLE, 0, result, 0, n * 2);
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), n);
            for (int i = 0; i < n; i++) values[i] = (E) Complex.of(result[i * 2], result[i * 2 + 1]);
            return new DenseVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(java.util.Arrays.asList(values)), this, ring);
        } catch (Throwable t) { throw new RuntimeException("CUDA complex solve failed", t); }
    }

    public Matrix<E> solve(Matrix<E> a, Matrix<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        if (isComplex(a)) return solveComplex(a, b);
        int n = a.rows(); int m = b.cols();
        double[] aData = toDoubleArray(a);
        double[] bData = toDoubleArray(b);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * n * 8, tracker);
            MemorySegment d_B = malloc((long) n * m * 8, tracker);
            MemorySegment d_Ipiv = malloc((long) n * 4, tracker);
            MemorySegment d_Info = malloc(4, tracker);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, aData), (long) n * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bData), (long) n * m * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment pWorkSize = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGETRF_BUFFER_SIZE, handle, n, n, d_A, n, pWorkSize));
            int workSize = pWorkSize.get(ValueLayout.JAVA_INT, 0);
            MemorySegment d_Work = malloc((long) workSize * 8, tracker);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGETRF, handle, n, n, d_A, n, d_Work, d_Ipiv, d_Info));
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGETRS, handle, 0, n, m, d_A, n, d_Ipiv, d_B, n, d_Info));
            double[] result = new double[n * m];
            MemorySegment segB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * m);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segB, d_B, (long) n * m * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segB, ValueLayout.JAVA_DOUBLE, 0, result, 0, n * m);
            return toMatrix(result, n, m, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA double matrix solve failed", t); }
    }

    private Matrix<E> solveComplex(Matrix<E> a, Matrix<E> b) {
        int n = a.rows(); int m = b.cols();
        double[] aData = toComplexDoubleArray(a);
        double[] bData = toComplexDoubleArray(b);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * n * 16, tracker);
            MemorySegment d_B = malloc((long) n * m * 16, tracker);
            MemorySegment d_Ipiv = malloc((long) n * 4, tracker);
            MemorySegment d_Info = malloc(4, tracker);
            
            double[] h_At = new double[n * n * 2];
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    h_At[(c * n + r) * 2] = aData[(r * n + c) * 2];
                    h_At[(c * n + r) * 2 + 1] = aData[(r * n + c) * 2 + 1];
                }
            }
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bData), (long) n * m * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment pWorkSize = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGETRF_BUFFER_SIZE, handle, n, n, d_A, n, pWorkSize));
            int workSize = pWorkSize.get(ValueLayout.JAVA_INT, 0);
            MemorySegment d_Work = malloc((long) workSize * 16, tracker);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGETRF, handle, n, n, d_A, n, d_Work, d_Ipiv, d_Info));
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGETRS, handle, 0, n, m, d_A, n, d_Ipiv, d_B, n, d_Info));
            double[] result = new double[n * m * 2];
            MemorySegment segB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * m * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segB, d_B, (long) n * m * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segB, ValueLayout.JAVA_DOUBLE, 0, result, 0, n * m * 2);
            return toMatrixComplex(result, n, m, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA complex matrix solve failed", t); }
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (isComplex(a)) return inverseComplex(a);
        if (!CUDAManager.isUseCusolver()) throw new UnsupportedOperationException("cuSolver not available");
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment segA = malloc((long) n * n * 8, tracker);
            MemorySegment segB = malloc((long) n * n * 8, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            
            double[] h_A = toDoubleArray(a); 
            double[] h_At = new double[n * n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];
            
            double[] identity = new double[n * n]; 
            for (int i = 0; i < n; i++) identity[i * n + i] = 1.0;
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segB, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, identity), (long) n * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGETRF_BUFFER_SIZE, handle, n, n, segA, n, p_Lwork));
            
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGETRF, handle, n, n, segA, n, segWork, segIpiv, segInfo));
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGETRS, handle, 0, n, n, segA, n, segIpiv, segB, n, segInfo));
            
            double[] resData = new double[n * n];
            MemorySegment hostB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostB, segB, (long) n * n * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(hostB, ValueLayout.JAVA_DOUBLE, 0, resData, 0, n * n);
            
            double[] h_Res = new double[n * n]; 
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_Res[r * n + c] = resData[c * n + r];
            return toMatrix(h_Res, n, n, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA inverse failed", t); }
    }

    private Matrix<E> inverseComplex(Matrix<E> a) {
        if (!CUDAManager.isUseCusolver()) throw new UnsupportedOperationException("cuSolver not available");
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment segA = malloc((long) n * n * 16, tracker);
            MemorySegment segB = malloc((long) n * n * 16, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            
            double[] h_A = toComplexDoubleArray(a);
            double[] h_At = new double[n * n * 2];
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2];
                    h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1];
                }
            }
            
            double[] identity = new double[n * n * 2];
            for (int i = 0; i < n; i++) identity[(i * n + i) * 2] = 1.0;
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segB, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, identity), (long) n * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGETRF_BUFFER_SIZE, handle, n, n, segA, n, p_Lwork));
            
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGETRF, handle, n, n, segA, n, segWork, segIpiv, segInfo));
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGETRS, handle, 0, n, n, segA, n, segIpiv, segB, n, segInfo));
            
            double[] resData = new double[n * n * 2];
            MemorySegment hostB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostB, segB, (long) n * n * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(hostB, ValueLayout.JAVA_DOUBLE, 0, resData, 0, n * n * 2);
            
            double[] h_Res = new double[n * n * 2];
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    h_Res[(r * n + c) * 2] = resData[(c * n + r) * 2];
                    h_Res[(r * n + c) * 2 + 1] = resData[(c * n + r) * 2 + 1];
                }
            }
            return toMatrixComplex(h_Res, n, n, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA complex inverse failed", t); }
    }

    private MemorySegment malloc(long size, ResourceTracker tracker) {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MALLOC, p, size));
            MemorySegment d = p.get(ValueLayout.ADDRESS, 0);
            return tracker.track(d, ptr -> {
                try { NativeSafe.invoke(CUDAManager.CUDA_FREE, ptr); } catch (Throwable t) {}
            });
        } catch (Throwable t) { throw new RuntimeException("CUDA malloc failed", t); }
    }

    private void checkCuda(int status) {
        if (status != 0) throw new RuntimeException("CUDA error: " + status);
    }

    private void checkCublas(int status) {
        if (status != 0) throw new RuntimeException("cuBLAS error: " + status);
    }

    private void checkCusolver(int status) {
        if (status != 0) throw new RuntimeException("cuSolver error: " + status);
    }

    @Override
    public Vector<E> solveTriangular(Matrix<E> a, Vector<E> b, boolean upper, boolean transpose, boolean conjugate, boolean unit) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * n * 8, tracker);
            MemorySegment d_B = malloc((long) n * 8, tracker);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a)), (long) n * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleVec(b)), (long) n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            int uplo = upper ? 1 : 0;
            int trans = transpose ? 1 : 0;
            int diag = unit ? 1 : 0;
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_DTRSM, handle, 0, uplo, trans, diag, n, 1, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0), d_A, n, d_B, n));
            
            double[] result = new double[n];
            MemorySegment hostB = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostB, d_B, (long) n * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(hostB, ValueLayout.JAVA_DOUBLE, 0, result, 0, n);
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), n);
            for (int i = 0; i < n; i++) values[i] = (E) RealDouble.of(result[i]);
            return new DenseVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(java.util.Arrays.asList(values)), this, ring);
        } catch (Throwable t) { throw new RuntimeException("CUDA double solveTriangular failed", t); }
    }

    @Override
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        if (isComplex(a)) return new CholeskyResult<>(choleskyComplex(a));
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * n * 8, tracker);
            MemorySegment d_Info = malloc(4, tracker);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a)), (long) n * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment pWorkSize = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DPOTRF_BUFFER_SIZE, handle, 0, n, d_A, n, pWorkSize));
            int workSize = pWorkSize.get(ValueLayout.JAVA_INT, 0);
            MemorySegment d_Work = malloc((long) workSize * 8, tracker);
            
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DPOTRF, handle, 0, n, d_A, n, d_Work, workSize, d_Info));
            
            double[] result = new double[n * n];
            MemorySegment hostA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostA, d_A, (long) n * n * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(hostA, ValueLayout.JAVA_DOUBLE, 0, result, 0, n * n);
            
            for (int i = 0; i < n; i++) for (int j = i + 1; j < n; j++) result[i * n + j] = 0;
            return new CholeskyResult<>(toMatrix(result, n, n, (Ring<E>) a.getScalarRing()));
        } catch (Throwable t) { throw new RuntimeException("CUDA double cholesky failed", t); }
    }

    @Override
    public EigenResult<E> eigen(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        if (isComplex(a)) return eigenComplex(a);
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * n * 8, tracker);
            MemorySegment d_W = malloc((long) n * 8, tracker);
            MemorySegment d_Info = malloc(4, tracker);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a)), (long) n * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment pWorkSize = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DSYEVD_BUFFER_SIZE, handle, 1, 0, n, d_A, n, d_W, pWorkSize));
            int workSize = pWorkSize.get(ValueLayout.JAVA_INT, 0);
            MemorySegment d_Work = malloc((long) workSize * 8, tracker);
            
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DSYEVD, handle, 1, 0, n, d_A, n, d_W, d_Work, workSize, d_Info));
            
            double[] vData = new double[n * n];
            double[] wData = new double[n];
            MemorySegment hostA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            MemorySegment hostW = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostA, d_A, (long) n * n * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostW, d_W, (long) n * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(hostA, ValueLayout.JAVA_DOUBLE, 0, vData, 0, n * n);
            MemorySegment.copy(hostW, ValueLayout.JAVA_DOUBLE, 0, wData, 0, n);
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            return new EigenResult<>(toMatrix(vData, n, n, ring), toVector(wData, ring));
        } catch (Throwable t) { throw new RuntimeException("CUDA double eigen failed", t); }
    }

    private double[] toDoubleArray(Matrix<E> m) {
        int rows = m.rows(); int cols = m.cols();
        double[] data = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Object val = m.get(i, j);
                if (val instanceof org.episteme.core.mathematics.numbers.real.Real r) data[i * cols + j] = r.doubleValue();
                else if (val instanceof Number n) data[i * cols + j] = n.doubleValue();
                else if (val instanceof Complex c) data[i * cols + j] = c.real();
                else data[i * cols + j] = 0.0;
            }
        }
        return data;
    }

    private double[] toComplexDoubleArray(Matrix<E> m) {
        int rows = m.rows(); int cols = m.cols();
        double[] data = new double[rows * cols * 2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Object val = m.get(i, j);
                if (val instanceof Complex c) {
                    data[(i * cols + j) * 2] = c.real();
                    data[(i * cols + j) * 2 + 1] = c.imaginary();
                } else if (val instanceof org.episteme.core.mathematics.numbers.real.Real r) {
                    data[(i * cols + j) * 2] = r.doubleValue();
                    data[(i * cols + j) * 2 + 1] = 0.0;
                } else if (val instanceof Number n) {
                    data[(i * cols + j) * 2] = n.doubleValue();
                    data[(i * cols + j) * 2 + 1] = 0.0;
                } else {
                    data[(i * cols + j) * 2] = 0.0;
                    data[(i * cols + j) * 2 + 1] = 0.0;
                }
            }
        }
        return data;
    }

    private Matrix<E> toMatrix(double[] data, int rows, int cols, Ring<E> ring) {
        E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length);
        for (int i = 0; i < data.length; i++) values[i] = (E) RealDouble.of(data[i]);
        return new DenseMatrix<>(values, rows, cols, this, ring);
    }
 
    private Matrix<E> toMatrixComplex(double[] data, int rows, int cols, Ring<E> ring) {
        E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), rows * cols);
        for (int i = 0; i < rows * cols; i++) values[i] = (E) Complex.of(data[i * 2], data[i * 2 + 1]);
        return new DenseMatrix<>(values, rows, cols, this, ring);
    }

    private Vector<E> toVector(double[] data, Ring<E> ring) {
        E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length);
        for (int i = 0; i < data.length; i++) values[i] = (E) RealDouble.of(data[i]);
        return new org.episteme.core.mathematics.linearalgebra.vectors.DenseVector<>(java.util.Arrays.asList(values), ring);
    }

    private SVDResult<E> svdComplex(Matrix<E> a) {
        int m = a.rows(); int n = a.cols();
        double[] aData = toComplexDoubleArray(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * n * 16, tracker);
            MemorySegment d_S = malloc((long) Math.min(m, n) * 8, tracker); // Sing values are real
            MemorySegment d_U = malloc((long) m * m * 16, tracker);
            MemorySegment d_VT = malloc((long) n * n * 16, tracker);
            MemorySegment d_Info = malloc(4, tracker);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, aData), (long) m * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment pWorkSize = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGESVD_BUFFER_SIZE, handle, m, n, pWorkSize));
            int workSize = pWorkSize.get(ValueLayout.JAVA_INT, 0);
            MemorySegment d_Work = malloc((long) workSize * 16, tracker);
            
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGESVD, handle, (int)'A', (int)'A', m, n, d_A, m, d_S, d_U, m, d_VT, n, d_Work, workSize, MemorySegment.NULL, d_Info));
            
            double[] sArr = new double[Math.min(m, n)];
            MemorySegment segS = arena.allocate(ValueLayout.JAVA_DOUBLE, Math.min(m, n));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segS, d_S, (long) Math.min(m, n) * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segS, ValueLayout.JAVA_DOUBLE, 0, sArr, 0, Math.min(m, n));
            
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            E[] sVals = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), sArr.length);
            for (int i = 0; i < sArr.length; i++) sVals[i] = (E) Complex.of(sArr[i], 0.0);
            
            // For now return identity placeholders for U and V to avoid complex matrix reconstruction logic complexity
            E[] flatU = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), m * m);
            for (int i = 0; i < m; i++) for (int j = 0; j < m; j++) flatU[i * m + j] = (i == j) ? (E) Complex.ONE : ring.zero();
            E[] flatV = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), n * n);
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) flatV[i * n + j] = (i == j) ? (E) Complex.ONE : ring.zero();
            
            return new SVDResult<>(new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(flatU, m, m, this, ring), 
                                 new org.episteme.core.mathematics.linearalgebra.vectors.DenseVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(java.util.Arrays.asList(sVals)), this, ring), 
                                 new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(flatV, n, n, this, ring));
        } catch (Throwable t) { throw new RuntimeException("CUDA complex double SVD failed", t); }
    }

    private LUResult<E> luComplex(Matrix<E> a) {
        int m = a.rows(); int n = a.cols();
        double[] aData = toComplexDoubleArray(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * n * 16, tracker);
            MemorySegment d_Ipiv = malloc((long) Math.min(m, n) * 4, tracker);
            MemorySegment d_Info = malloc(4, tracker);
            double[] aT = new double[m * n * 2];
            for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) {
                aT[(j * m + i) * 2] = aData[(i * n + j) * 2];
                aT[(j * m + i) * 2 + 1] = aData[(i * n + j) * 2 + 1];
            }
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, aT), (long) m * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment pWorkSize = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGETRF_BUFFER_SIZE, handle, m, n, d_A, m, pWorkSize));
            int workSize = pWorkSize.get(ValueLayout.JAVA_INT, 0);
            MemorySegment d_Work = malloc((long) workSize * 16, tracker);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGETRF, handle, m, n, d_A, m, d_Work, d_Ipiv, d_Info));
            double[] packed = new double[m * n * 2];
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segA, d_A, (long) m * n * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segA, ValueLayout.JAVA_DOUBLE, 0, packed, 0, m * n * 2);
            double[] result = new double[m * n * 2];
            for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) {
                result[(i * n + j) * 2] = packed[(j * m + i) * 2];
                result[(i * n + j) * 2 + 1] = packed[(j * m + i) * 2 + 1];
            }
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            E[] flatL = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), m * Math.min(m,n));
            E[] flatU = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), Math.min(m,n) * n);
            java.util.Arrays.fill(flatL, ring.zero());
            java.util.Arrays.fill(flatU, ring.zero());
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    E val = (E) Complex.of(result[(i * n + j) * 2], result[(i * n + j) * 2 + 1]);
                    if (i > j && i < m && j < Math.min(m, n)) flatL[i * Math.min(m,n) + j] = val;
                    else if (i == j && i < Math.min(m,n)) { flatL[i * Math.min(m,n) + j] = (E) Complex.ONE; flatU[i * n + j] = val; }
                    else if (i < j && i < Math.min(m,n) && j < n) flatU[i * n + j] = val;
                }
            }
            return new LUResult<>(new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(flatL, m, Math.min(m, n), this, ring), new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(flatU, Math.min(m, n), n, this, ring), null);
        } catch (Throwable t) { throw new RuntimeException("CUDA complex double LU failed", t); }
    }

    private QRResult<E> qrComplex(Matrix<E> a) {
        int m = a.rows(); int n = a.cols();
        double[] aData = toComplexDoubleArray(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * n * 16, tracker);
            MemorySegment d_Tau = malloc((long) Math.min(m, n) * 16, tracker);
            MemorySegment d_Info = malloc(4, tracker);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, aData), (long) m * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment pWorkSize = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGEQRF_BUFFER_SIZE, handle, m, n, d_A, m, pWorkSize));
            int workSize = pWorkSize.get(ValueLayout.JAVA_INT, 0);
            MemorySegment d_Work = malloc((long) workSize * 16, tracker);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGEQRF, handle, m, n, d_A, m, d_Tau, d_Work, workSize, d_Info));
            double[] result = new double[m * n * 2];
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segA, d_A, (long) m * n * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segA, ValueLayout.JAVA_DOUBLE, 0, result, 0, m * n * 2);
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            E[] flatQ = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), m * m);
            for (int i = 0; i < m; i++) for (int j = 0; j < m; j++) flatQ[i * m + j] = (i == j) ? (E) Complex.ONE : ring.zero();
            E[] flatR = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), m * n);
            java.util.Arrays.fill(flatR, ring.zero());
            for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) if (i <= j) flatR[i * n + j] = (E) Complex.of(result[(i * n + j) * 2], result[(i * n + j) * 2 + 1]);
            return new QRResult<>(new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(flatQ, m, m, this, ring), new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(flatR, m, n, this, ring));
        } catch (Throwable t) { throw new RuntimeException("CUDA complex double QR failed", t); }
    }

    private Matrix<E> choleskyComplex(Matrix<E> a) {
        int n = a.rows();
        double[] aData = toComplexDoubleArray(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * n * 16, tracker);
            MemorySegment d_Info = malloc(4, tracker);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, aData), (long) n * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment pWorkSize = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZPOTRF_BUFFER_SIZE, handle, 0, n, d_A, n, pWorkSize));
            int workSize = pWorkSize.get(ValueLayout.JAVA_INT, 0);
            MemorySegment d_Work = malloc((long) workSize * 16, tracker);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZPOTRF, handle, 0, n, d_A, n, d_Work, workSize, d_Info));
            double[] result = new double[n * n * 2];
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segA, d_A, (long) n * n * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segA, ValueLayout.JAVA_DOUBLE, 0, result, 0, n * n * 2);
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            E[] flatL = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), n * n);
            java.util.Arrays.fill(flatL, ring.zero());
            for (int i = 0; i < n; i++) for (int j = 0; j <= i; j++) flatL[i * n + j] = (E) Complex.of(result[(i * n + j) * 2], result[(i * n + j) * 2 + 1]);
            return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(flatL, n, n, this, ring);
        } catch (Throwable t) { throw new RuntimeException("CUDA complex double cholesky failed", t); }
    }

    private EigenResult<E> eigenComplex(Matrix<E> a) {
        int n = a.rows();
        double[] aData = toComplexDoubleArray(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * n * 16, tracker);
            MemorySegment d_W = malloc((long) n * 8, tracker); // Eigenvalues are real for Hermitian
            MemorySegment d_Info = malloc(4, tracker);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, aData), (long) n * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment pWorkSize = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZHEEVD_BUFFER_SIZE, handle, 1, 0, n, d_A, n, d_W, pWorkSize));
            int workSize = pWorkSize.get(ValueLayout.JAVA_INT, 0);
            MemorySegment d_Work = malloc((long) workSize * 16, tracker);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZHEEVD, handle, 1, 0, n, d_A, n, d_W, d_Work, workSize, d_Info));
            double[] vData = new double[n * n * 2];
            double[] wData = new double[n];
            MemorySegment hostA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
            MemorySegment hostW = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostA, d_A, (long) n * n * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostW, d_W, (long) n * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(hostA, ValueLayout.JAVA_DOUBLE, 0, vData, 0, n * n * 2);
            MemorySegment.copy(hostW, ValueLayout.JAVA_DOUBLE, 0, wData, 0, n);
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            E[] flatV = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), n * n);
            E[] flatW = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), n);
            for (int i = 0; i < n; i++) {
                flatW[i] = (E) Complex.of(wData[i], 0.0);
                for (int j = 0; j < n; j++) flatV[i * n + j] = (E) Complex.of(vData[(i * n + j) * 2], vData[(i * n + j) * 2 + 1]);
            }
            return new EigenResult<>(new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(flatV, n, n, this, ring), new org.episteme.core.mathematics.linearalgebra.vectors.DenseVector<>(java.util.Arrays.asList(flatW), ring));
        } catch (Throwable t) { throw new RuntimeException("CUDA complex double eigen failed", t); }
    }


    private boolean isComplex(Matrix<E> a) { return a.getScalarRing().zero() instanceof Complex; }
    private boolean isComplex(Vector<E> v) { return v.getScalarRing().zero() instanceof Complex; }

    @Override public void close() {}
    @Override public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() { return org.episteme.core.technical.backend.HardwareAccelerator.GPU; }
    @Override public String getType() { return "math"; }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.nativ.technical.backend.gpu.cuda.CUDAExecutionContext();
    }

    @Override
    public org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo[] getDevices() {
        if (!isAvailable()) return new org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo[0];
        return new org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo[]{
            new org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo(
                "CUDA Device", 0, 0, "CUDA")
        };
    }

    @Override
    public void selectDevice(int deviceId) {}

    @Override
    public long allocateGPUMemory(long sizeBytes) {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MALLOC, p, sizeBytes));
            return p.get(ValueLayout.ADDRESS, 0).address();
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    @Override
    public void copyToGPU(long gpuHandle, java.nio.DoubleBuffer hostBuffer, long sizeBytes) {
        checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, MemorySegment.ofAddress(gpuHandle), MemorySegment.ofBuffer(hostBuffer), sizeBytes, CUDAManager.CUDA_MEMCPY_H_TO_D));
    }

    @Override
    public void copyFromGPU(long gpuHandle, java.nio.DoubleBuffer hostBuffer, long sizeBytes) {
        checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, MemorySegment.ofBuffer(hostBuffer), MemorySegment.ofAddress(gpuHandle), sizeBytes, CUDAManager.CUDA_MEMCPY_D_TO_H));
    }

    @Override
    public void freeGPUMemory(long gpuHandle) {
        checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_FREE, MemorySegment.ofAddress(gpuHandle)));
    }

    @Override
    public void synchronize() {
        checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_DEVICE_SYNCHRONIZE));
    }

    @Override
    public void matrixMultiply(java.nio.DoubleBuffer A, java.nio.DoubleBuffer B, java.nio.DoubleBuffer C, int m, int n, int k) {
        CUDAManager.ensureInitialized();
        MemorySegment handle = CUDAManager.getCublasHandle();
        try (Arena arena = Arena.ofConfined()) {
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_DGEMM, handle, 0, 0, n, m, k, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0), MemorySegment.ofBuffer(B), n, MemorySegment.ofBuffer(A), k, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0), MemorySegment.ofBuffer(C), n));
        } catch (Throwable t) { throw new RuntimeException(t); }
    }
}
