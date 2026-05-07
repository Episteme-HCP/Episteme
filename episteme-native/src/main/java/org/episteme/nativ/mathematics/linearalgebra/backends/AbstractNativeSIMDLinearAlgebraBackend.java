/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Complexes;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.SIMDRealFloatMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.simd.SIMDBackend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;

import org.episteme.core.technical.backend.Operation;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorSpecies;
import static jdk.incubator.vector.VectorOperators.*;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.linearalgebra.vectors.RealFloatVector;
import org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector;

/**
 * SIMD-accelerated Linear Algebra Backend for Real numbers using JDK Vector API.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public abstract class AbstractNativeSIMDLinearAlgebraBackend<E> implements LinearAlgebraProvider<E>, NativeBackend, CPUBackend, SIMDBackend {

    private static final Logger logger = LoggerFactory.getLogger(AbstractNativeSIMDLinearAlgebraBackend.class);
    
    private static VectorSpecies<Double> getDoubleSpecies() {
        return DoubleVector.SPECIES_PREFERRED;
    }
    
    private static VectorSpecies<Float> getFloatSpecies() {
        return FloatVector.SPECIES_PREFERRED;
    }

    private boolean isFastPrecision() {
        return MathContext.getCurrent().isFastPrecision();
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

    private boolean isRealRing(Ring<?> ring) {
        return ring instanceof org.episteme.core.mathematics.sets.Reals;
    }

    private boolean isFloat(Object obj) {
        if (obj == null) return false;
        if (obj instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return true;
        if (obj instanceof org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) return false;
        if (obj instanceof org.episteme.nativ.mathematics.linearalgebra.matrices.NativeRealDoubleMatrix) return false;
        if (obj instanceof org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector) return false;

        if (obj instanceof Matrix<?> m) {
            if (m.rows() > 0 && m.cols() > 0) {
                try {
                    Object first = m.get(0, 0);
                    if (first instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return true;
                    if (first instanceof org.episteme.core.mathematics.numbers.real.RealDouble) return false;
                    if (first instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
                        if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return true;
                        if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealDouble) return false;
                    }
                } catch (Exception e) {}
            }
            return isFloatRing(m.getScalarRing());
        }
        if (obj instanceof Vector<?> v) {
            if (v.dimension() > 0) {
                try {
                    Object first = v.get(0);
                    if (first instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return true;
                    if (first instanceof org.episteme.core.mathematics.numbers.real.RealDouble) return false;
                    if (first instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
                        if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return true;
                        if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealDouble) return false;
                    }
                } catch (Exception e) {}
            }
            return isFloatRing(v.getScalarRing());
        }
        return false;
    }

    private boolean isFloatRing(Ring<?> ring) {
        if (ring == null) return false;
        Object zero = ring.zero();
        if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return true;
        if (zero instanceof org.episteme.core.mathematics.numbers.real.RealDouble) return false;
        if (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
            if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return true;
            if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealDouble) return false;
        }
        
        // Fallback to MathContext for generic 'Reals' ring which doesn't specify precision in its zero()
        if (ring instanceof org.episteme.core.mathematics.sets.Reals || ring.getClass().getName().contains("Reals")) {
            return org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() == org.episteme.core.mathematics.context.MathContext.RealPrecision.FAST;
        }
        if (ring instanceof org.episteme.core.mathematics.sets.Complexes || ring.getClass().getName().contains("Complexes")) {
            return org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() == org.episteme.core.mathematics.context.MathContext.RealPrecision.FAST;
        }
        
        return false;
    }

    @SuppressWarnings("unchecked")
    private E castScalar(Object val, Ring<E> ring) {
        if (val == null) return ring.zero();
        boolean isComplexRing = ring instanceof org.episteme.core.mathematics.sets.Complexes;
        boolean isRealRing = ring instanceof org.episteme.core.mathematics.sets.Reals;
        
        if (isComplexRing) {
            if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E) val;
            if (val instanceof org.episteme.core.mathematics.numbers.real.Real r) return (E) org.episteme.core.mathematics.numbers.complex.Complex.of(r);
            if (val instanceof Number n) return (E) org.episteme.core.mathematics.numbers.complex.Complex.of(n.doubleValue());
            return (E) org.episteme.core.mathematics.numbers.complex.Complex.of(getRealValue(val));
        }
        if (isRealRing) {
            if (val instanceof org.episteme.core.mathematics.numbers.real.Real) return (E) val;
            if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex c) return (E) c.getReal();
            if (val instanceof Number n) return (E) org.episteme.core.mathematics.numbers.real.Real.of(n.doubleValue());
        }
        
        if (val instanceof Real r) {
            if (ring.zero() instanceof Double) return (E) Double.valueOf(r.doubleValue());
            if (ring.zero() instanceof Float) return (E) Float.valueOf(r.floatValue());
            return (E) r;
        }
        if (val instanceof Complex c) {
            if (ring.zero() instanceof Double) return (E) Double.valueOf(c.real());
            if (ring.zero() instanceof Float) return (E) Float.valueOf((float)c.real());
            return (E) c;
        }
        if (val instanceof Number n) {
            if (ring.zero() instanceof Double) return (E) Double.valueOf(n.doubleValue());
            if (ring.zero() instanceof Float) return (E) Float.valueOf(n.floatValue());
        }
        return (E) val;
    }

    private boolean isZero(Object obj, Ring<?> ring) {
        if (obj == null) return true;
        if (ring != null && ring.zero().equals(obj)) return true;
        
        if (obj instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
            return isZero(c.getReal(), null) && isZero(c.getImaginary(), null);
        }
        if (obj instanceof org.episteme.core.mathematics.numbers.real.Real r) {
            return r.isZero();
        }
        if (obj instanceof Number n) {
            return n.doubleValue() == 0.0;
        }
        return false;
    }

    private double getRealValue(Object obj) {
        if (obj == null) return 0.0;
        if (obj instanceof Number n) return n.doubleValue();
        if (obj instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
            return getRealValue(c.getReal());
        }
        if (obj instanceof org.episteme.core.mathematics.numbers.real.Real r) {
            return r.doubleValue();
        }
        return 0.0;
    }

    private org.episteme.core.mathematics.numbers.real.Real getReal(Object obj) {
        if (obj == null) return org.episteme.core.mathematics.numbers.real.Real.ZERO;
        if (obj instanceof org.episteme.core.mathematics.numbers.real.Real r) return r;
        if (obj instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
            return getReal(c.getReal());
        }
        if (obj instanceof Number n) {
            return org.episteme.core.mathematics.numbers.real.Real.of(n.doubleValue());
        }
        return org.episteme.core.mathematics.numbers.real.Real.of(getRealValue(obj));
    }

    private E createScalar(double val, Object ref) {
        return createScalar(val, 0.0, ref);
    }

    private E createScalar(double real, double imag, Object ref) {
        if (isComplex(ref)) return (E) Complex.of(real, imag);
        if (isFloat(ref)) return (E) org.episteme.core.mathematics.numbers.real.RealFloat.of((float)real);
        E zero = (E) Real.of(real);
        try {
            return zero;
        } catch (ClassCastException e) {
            if (isComplex(ref)) return (E) Complex.of(real, imag);
            throw e;
        }
    }

    @Override
    public String getId() {
        return "simd";
    }

    @Override
    public String getType() {
        return "math";
    }

    @Override
    public String getAlgorithmType() {
        return "LinearAlgebra";
    }

    @Override
    public boolean isAvailable() {
        try {
            return jdk.incubator.vector.VectorSpecies.ofLargestShape(double.class).vectorBitSize() > 0 
                && !isExplicitlyDisabled();
        } catch (NoClassDefFoundError | Exception e) {
            return false;
        }
    }

    @Override
    public boolean isLoaded() {
        return isAvailable();
    }

    @Override
    public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.CPU;
    }

    @Override
    public String getNativeLibraryName() {
        return "jdk.incubator.vector";
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new SIMDExecutionContext();
    }

    private class SIMDExecutionContext implements org.episteme.core.technical.backend.ExecutionContext {
        @Override
        public <T> T execute(Operation<T> operation) {
            return operation.compute(this);
        }

        @Override
        public void close() {
            // No-op for SIMD context
        }
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        if (ring == null) return false;
        
        boolean res = false;
        // Strict domain matching
        if (this.ring instanceof Reals) {
            res = ring instanceof Reals || ring.zero() instanceof org.episteme.core.mathematics.numbers.real.Real;
        } else if (this.ring instanceof Complexes) {
            res = ring instanceof Complexes || ring.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        }
        
        System.out.println(String.format("[DEBUG] Backend %s isCompatible(%s)? %b (this.ring=%s)", 
            getName(), ring.getClass().getSimpleName(), res, this.ring.getClass().getSimpleName()));
        return res;
    }

    protected final Ring<E> ring;

    protected AbstractNativeSIMDLinearAlgebraBackend(Ring<E> ring) {
        this.ring = ring;
    }

    @Override
    public int getPriority() {
        return 90; // Higher than standard CPU, lower than BLAS
    }



    @Override
    public String getName() {
        return "Native SIMD (" + (ring instanceof Complexes ? "Complex" : "Real") + ")";
    }

    @Override
    public String getSimdLevel() {
        return isFastPrecision() ? getFloatSpecies().toString() : getDoubleSpecies().toString();
    }

    @Override
    public int getPreferredVectorWidth() {
        return isFastPrecision() ? getFloatSpecies().vectorBitSize() : getDoubleSpecies().vectorBitSize();
    }

    @Override
    public void shutdown() {
        // No-op. Vector API does not require explicit shutdown.
    }


    // --- Vector Operations ---

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Vector<E> res = (Vector<E>) (Object) executeComplexVectorAdd((Vector<Complex>) (Object) a, (Vector<Complex>) (Object) b);
            return res;
        }
        int n = a.dimension();
        if (b.dimension() != n) throw new IllegalArgumentException("Dimension mismatch");
        
        if (isFastPrecision()) {
            SIMDRealFloatMatrix sa = SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a);
            SIMDRealFloatMatrix sb = SIMDRealFloatMatrix.from((Matrix<Real>) (Object) b);
            return (Vector<E>) (Object) sa.add(sb);
        }

        double[] aData = toDoubleArray((Vector<Real>) (Object) a);
        double[] bData = toDoubleArray((Vector<Real>) (Object) b);
        double[] cData = new double[n];
        
        int i = 0;
        var species = getDoubleSpecies();
        int loopBound = species.loopBound(n);
        for (; i < loopBound; i += species.length()) {
            DoubleVector va = DoubleVector.fromArray(species, aData, i);
            DoubleVector vb = DoubleVector.fromArray(species, bData, i);
            va.add(vb).intoArray(cData, i);
        }
        for (; i < n; i++) cData[i] = aData[i] + bData[i];
        
        return (Vector<E>) (Object) org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(cData);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Vector<E> res = (Vector<E>) (Object) executeComplexVectorSubtract((Vector<Complex>) (Object) a, (Vector<Complex>) (Object) b);
            return res;
        }
        int n = a.dimension();
        if (b.dimension() != n) throw new IllegalArgumentException("Dimension mismatch");
        
        if (isFastPrecision()) {
            float[] aData = toFloatArray((Vector<Real>) (Object) a);
            float[] bData = toFloatArray((Vector<Real>) (Object) b);
            float[] cData = new float[n];
            
            int i = 0;
            var species = getFloatSpecies();
            int loopBound = species.loopBound(n);
            for (; i < loopBound; i += species.length()) {
                FloatVector va = FloatVector.fromArray(species, aData, i);
                FloatVector vb = FloatVector.fromArray(species, bData, i);
                va.sub(vb).intoArray(cData, i);
            }
            for (; i < n; i++) cData[i] = aData[i] - bData[i];
            return (Vector<E>) (Object) RealFloatVector.of(cData);
        }

        double[] aData = toDoubleArray((Vector<Real>) (Object) a);
        double[] bData = toDoubleArray((Vector<Real>) (Object) b);
        double[] cData = new double[n];
        
        int i = 0;
        var species = getDoubleSpecies();
        int loopBound = species.loopBound(n);
        for (; i < loopBound; i += species.length()) {
            DoubleVector va = DoubleVector.fromArray(species, aData, i);
            DoubleVector vb = DoubleVector.fromArray(species, bData, i);
            va.sub(vb).intoArray(cData, i);
        }
        for (; i < n; i++) cData[i] = aData[i] - bData[i];
        
        return (Vector<E>) (Object) org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(cData);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (isComplex(vector)) {
            org.episteme.core.mathematics.numbers.complex.Complex cScalar = (scalar instanceof org.episteme.core.mathematics.numbers.complex.Complex c) ? c : org.episteme.core.mathematics.numbers.complex.Complex.of(getRealValue(scalar));
            @SuppressWarnings("unchecked")
            Vector<E> res = (Vector<E>) (Object) executeComplexVectorScale((Vector<org.episteme.core.mathematics.numbers.complex.Complex>) (Object) vector, cScalar);
            return res;
        }
        int n = vector.dimension();
        double s = getRealValue(scalar);
        
        if (isFastPrecision()) {
            float sf = (float) s;
            float[] aData = toFloatArray((Vector<Real>) (Object) vector);
            float[] cData = new float[n];
            
            int i = 0;
            var species = getFloatSpecies();
            int loopBound = species.loopBound(n);
            for (; i < loopBound; i += species.length()) {
                FloatVector va = FloatVector.fromArray(species, aData, i);
                va.mul(sf).intoArray(cData, i);
            }
            for (; i < n; i++) cData[i] = aData[i] * sf;
            return (Vector<E>) (Object) RealFloatVector.of(cData);
        }

        double[] aData = toDoubleArray((Vector<Real>) (Object) vector);
        double[] cData = new double[n];
        
        int i = 0;
        var species = getDoubleSpecies();
        int loopBound = species.loopBound(n);
        for (; i < loopBound; i += species.length()) {
            DoubleVector va = DoubleVector.fromArray(species, aData, i);
            va.mul(s).intoArray(cData, i);
        }
        for (; i < n; i++) cData[i] = aData[i] * s;
        
        return (Vector<E>) (Object) org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(cData);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E dot(Vector<E> a, Vector<E> b) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            E res = (E) (Object) executeComplexVectorDot((Vector<org.episteme.core.mathematics.numbers.complex.Complex>) (Object) a, (Vector<org.episteme.core.mathematics.numbers.complex.Complex>) (Object) b);
            return res;
        }
        int n = a.dimension();
        if (b.dimension() != n) throw new IllegalArgumentException("Dimension mismatch");
        
        if (isFastPrecision()) {
            float[] aData = toFloatArray((Vector<Real>) (Object) a);
            float[] bData = toFloatArray((Vector<Real>) (Object) b);
            
            int i = 0;
            var species = getFloatSpecies();
            int loopBound = species.loopBound(n);
            FloatVector sum = FloatVector.zero(species);
            for (; i < loopBound; i += species.length()) {
                FloatVector va = FloatVector.fromArray(species, aData, i);
                FloatVector vb = FloatVector.fromArray(species, bData, i);
                sum = sum.add(va.mul(vb));
            }
            float res = sum.reduceLanes(ADD);
            for (; i < n; i++) res += aData[i] * bData[i];
            return createScalar(res, a);
        }

        double[] aData = toDoubleArray((Vector<Real>) (Object) a);
        double[] bData = toDoubleArray((Vector<Real>) (Object) b);
        
        int i = 0;
        var species = getDoubleSpecies();
        int loopBound = species.loopBound(n);
        DoubleVector sum = DoubleVector.zero(species);
        
        for (; i < loopBound; i += species.length()) {
            DoubleVector va = DoubleVector.fromArray(species, aData, i);
            DoubleVector vb = DoubleVector.fromArray(species, bData, i);
            sum = sum.add(va.mul(vb));
        }
        double res = sum.reduceLanes(ADD);
        
        for (; i < n; i++) res += aData[i] * bData[i];
        
        return createScalar(res, a);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E norm(Vector<E> a) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            E res = (E) (Object) executeComplexVectorNorm((Vector<org.episteme.core.mathematics.numbers.complex.Complex>) (Object) a);
            return res;
        }
        E d = dot(a, a);
        double val = getRealValue(d);
        return createScalar(Math.sqrt(val), a);
    }

    @Override
    public Vector<E> solveTriangular(Matrix<E> A, Vector<E> b, boolean upper, boolean transpose, boolean unit) {
        int n = A.rows();
        if (n != A.cols()) throw new IllegalArgumentException("Matrix must be square");
        if (n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");

        // Use standard substitution
        E[] x = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), n);
        
        if (upper) {
            if (transpose) {
                for (int i = 0; i < n; i++) {
                    E sum = ring.zero();
                    for (int j = 0; j < i; j++) {
                        sum = ring.add(sum, ring.multiply(A.get(j, i), x[j]));
                    }
                    E val = ring.subtract(b.get(i), sum);
                    if (!unit) {
                        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
                            val = field.divide(val, A.get(i, i));
                        } else {
                             throw new UnsupportedOperationException("Non-field triangular solve not supported");
                        }
                    }
                    x[i] = val;
                }
            } else {
                for (int i = n - 1; i >= 0; i--) {
                    E sum = ring.zero();
                    for (int j = i + 1; j < n; j++) {
                        sum = ring.add(sum, ring.multiply(A.get(i, j), x[j]));
                    }
                    E val = ring.subtract(b.get(i), sum);
                    if (!unit) {
                        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
                            val = field.divide(val, A.get(i, i));
                        } else {
                             throw new UnsupportedOperationException("Non-field triangular solve not supported");
                        }
                    }
                    x[i] = val;
                }
            }
        } else {
            if (transpose) {
                for (int i = n - 1; i >= 0; i--) {
                    E sum = ring.zero();
                    for (int j = i + 1; j < n; j++) {
                        sum = ring.add(sum, ring.multiply(A.get(j, i), x[j]));
                    }
                    E val = ring.subtract(b.get(i), sum);
                    if (!unit) {
                        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
                            val = field.divide(val, A.get(i, i));
                        } else {
                             throw new UnsupportedOperationException("Non-field triangular solve not supported");
                        }
                    }
                    x[i] = val;
                }
            } else {
                for (int i = 0; i < n; i++) {
                    E sum = ring.zero();
                    for (int j = 0; j < i; j++) {
                        sum = ring.add(sum, ring.multiply(A.get(i, j), x[j]));
                    }
                    E val = ring.subtract(b.get(i), sum);
                    if (!unit) {
                        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
                            val = field.divide(val, A.get(i, i));
                        } else {
                             throw new UnsupportedOperationException("Non-field triangular solve not supported");
                        }
                    }
                    x[i] = val;
                }
            }
        }

        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(x), this, ring);
    }

    @Override
    @SuppressWarnings("unchecked")
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
        return multiply(v, createScalar(1.0 / nv, v));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> cross(Vector<E> a, Vector<E> b) {
        if (a.dimension() != 3 || b.dimension() != 3) throw new IllegalArgumentException("Cross product only supported for 3D vectors");
        
        Ring<E> ring = a.getScalarRing();
        E a1 = a.get(0);
        E a2 = a.get(1);
        E a3 = a.get(2);
        E b1 = b.get(0);
        E b2 = b.get(1);
        E b3 = b.get(2);
        
        E c1 = ring.subtract(ring.multiply(a2, b3), ring.multiply(a3, b2));
        E c2 = ring.subtract(ring.multiply(a3, b1), ring.multiply(a1, b3));
        E c3 = ring.subtract(ring.multiply(a1, b2), ring.multiply(a2, b1));
        
        return (Vector<E>) org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(c1, c2, c3), ring);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E angle(Vector<E> a, Vector<E> b) {
        E d = dot(a, b);
        E nA = norm(a);
        E nB = norm(b);
        
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
            try {
                E denom = field.multiply(nA, nB);
                if (isZero(denom, ring)) return createScalar(0.0, (Object)a);
                E cosTheta = field.divide(d, denom);
                return castScalar(Math.acos(Math.max(-1.0, Math.min(1.0, getRealValue(cosTheta)))), ring);
            } catch (Exception e) {
                return createScalar(0.0, (Object)a);
            }
        }

        double dotVal = getRealValue(d);
        double nAVal = getRealValue(nA);
        double nBVal = getRealValue(nB);
        
        if (nAVal == 0 || nBVal == 0) return createScalar(0.0, a);
        return createScalar(Math.acos(Math.max(-1.0, Math.min(1.0, dotVal / (nAVal * nBVal)))), a);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> projection(Vector<E> a, Vector<E> b) {
        E dAB = dot(a, b);
        E dBB = dot(b, b);
        
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
            try {
                if (isZero(dBB, ring)) return a; // Or zero vector
                E factor = field.divide(dAB, dBB);
                return multiply(b, factor);
            } catch (Exception e) {
                return a;
            }
        }

        double dotAB = getRealValue(dAB);
        double dotBB = getRealValue(dBB);
        
        if (dotBB == 0) return b;
        return multiply(b, createScalar(dotAB / dotBB, b));
    }

    // --- Matrix Operations ---

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) (Object) executeComplexMultiply((Matrix<Complex>) (Object) a, (Matrix<Complex>) (Object) b);
            return res;
        }
        logger.debug("Entering SIMD multiply: [{}x{}] * [{}x{}]", a.rows(), a.cols(), b.rows(), b.cols());
        
        if (isFastPrecision()) {
            SIMDRealFloatMatrix sa = SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a);
            SIMDRealFloatMatrix sb = SIMDRealFloatMatrix.from((Matrix<Real>) (Object) b);
            return (Matrix<E>) (Object) sa.multiply(sb);
        }

        SIMDRealDoubleMatrix sa = SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a);
        SIMDRealDoubleMatrix sb = SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) b);
        return (Matrix<E>) (Object) sa.multiply(sb);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) (Object) executeComplexAdd((Matrix<Complex>) (Object) a, (Matrix<Complex>) (Object) b);
            return res;
        }
        
        if (isFastPrecision()) {
            SIMDRealFloatMatrix sa = SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a);
            SIMDRealFloatMatrix sb = SIMDRealFloatMatrix.from((Matrix<Real>) (Object) b);
            return (Matrix<E>) (Object) sa.add(sb);
        }

        SIMDRealDoubleMatrix sa = SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a);
        SIMDRealDoubleMatrix sb = SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) b);
        return (Matrix<E>) (Object) sa.add(sb);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) (Object) executeComplexSubtract((Matrix<Complex>) (Object) a, (Matrix<Complex>) (Object) b);
            return res;
        }
        
        if (isFastPrecision()) {
            SIMDRealFloatMatrix sa = SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a);
            SIMDRealFloatMatrix sb = SIMDRealFloatMatrix.from((Matrix<Real>) (Object) b);
            return (Matrix<E>) (Object) sa.subtract(sb);
        }

        SIMDRealDoubleMatrix sa = SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a);
        SIMDRealDoubleMatrix sb = SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) b);
        return (Matrix<E>) (Object) sa.subtract(sb);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) (Object) executeComplexScale((Complex) (Object) scalar, (Matrix<Complex>) (Object) a);
            return res;
        }
        
        if (isFastPrecision()) {
            SIMDRealFloatMatrix sa = SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a);
            return (Matrix<E>) (Object) sa.scale(((Real) (Object) scalar).floatValue());
        }

        SIMDRealDoubleMatrix sa = SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a);
        return (Matrix<E>) (Object) sa.scale(((Real) (Object) scalar).doubleValue());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> transpose(Matrix<E> a) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) (Object) executeComplexTranspose((Matrix<Complex>) (Object) a);
            return res;
        }
        if (isFastPrecision()) {
            return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).transpose();
        }
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).transpose();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> exp(Matrix<E> a) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) (Object) executeComplexUnaryOp((Matrix<Complex>) (Object) a, Complex::exp);
            return res;
        }
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).exp();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).exp();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> log(Matrix<E> a) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) (Object) executeComplexUnaryOp((Matrix<Complex>) (Object) a, Complex::log);
            return res;
        }
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).log();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).log();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> log10(Matrix<E> a) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) (Object) executeComplexUnaryOp((Matrix<Complex>) (Object) a, Complex::log10);
            return res;
        }
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).log10();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).log10();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> sin(Matrix<E> a) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) (Object) executeComplexUnaryOp((Matrix<Complex>) (Object) a, Complex::sin);
            return res;
        }
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).sin();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).sin();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> cos(Matrix<E> a) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) (Object) executeComplexUnaryOp((Matrix<Complex>) (Object) a, Complex::cos);
            return res;
        }
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).cos();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).cos();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> tan(Matrix<E> a) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) (Object) executeComplexUnaryOp((Matrix<Complex>) (Object) a, Complex::tan);
            return res;
        }
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).tan();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).tan();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> asin(Matrix<E> a) {
        if (isComplexRing(a.getScalarRing())) return (Matrix<E>) executeComplexUnaryOp((Matrix<Complex>) a, Complex::asin);
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).asin();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).asin();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> acos(Matrix<E> a) {
        if (isComplexRing(a.getScalarRing())) return (Matrix<E>) executeComplexUnaryOp((Matrix<Complex>) a, Complex::acos);
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).acos();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).acos();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> atan(Matrix<E> a) {
        if (isComplexRing(a.getScalarRing())) return (Matrix<E>) executeComplexUnaryOp((Matrix<Complex>) a, Complex::atan);
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).atan();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).atan();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> sinh(Matrix<E> a) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) (Object) executeComplexUnaryOp((Matrix<Complex>) (Object) a, Complex::sinh);
            return res;
        }
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).sinh();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).sinh();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> cosh(Matrix<E> a) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) (Object) executeComplexUnaryOp((Matrix<Complex>) (Object) a, Complex::cosh);
            return res;
        }
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).cosh();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).cosh();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> tanh(Matrix<E> a) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) (Object) executeComplexUnaryOp((Matrix<Complex>) (Object) a, Complex::tanh);
            return res;
        }
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).tanh();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).tanh();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> asinh(Matrix<E> a) {
        if (isComplexRing(a.getScalarRing())) return (Matrix<E>) executeComplexUnaryOp((Matrix<Complex>) a, Complex::asinh);
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).asinh();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).asinh();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> acosh(Matrix<E> a) {
        if (isComplexRing(a.getScalarRing())) return (Matrix<E>) executeComplexUnaryOp((Matrix<Complex>) a, Complex::acosh);
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).acosh();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).acosh();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> atanh(Matrix<E> a) {
        if (isComplexRing(a.getScalarRing())) return (Matrix<E>) executeComplexUnaryOp((Matrix<Complex>) a, Complex::atanh);
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).atanh();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).atanh();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> sqrt(Matrix<E> a) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) (Object) executeComplexUnaryOp((Matrix<Complex>) (Object) a, Complex::sqrt);
            return res;
        }
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).sqrt();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).sqrt();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> cbrt(Matrix<E> a) {
        if (isComplexRing(a.getScalarRing())) return (Matrix<E>) executeComplexUnaryOp((Matrix<Complex>) a, Complex::cbrt);
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).cbrt();
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).cbrt();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> pow(Matrix<E> a, E exponent) {
        if (isComplexRing(a.getScalarRing())) {
            Complex exp = (Complex) exponent;
            return (Matrix<E>) executeComplexUnaryOp((Matrix<Complex>) a, c -> c.pow(exp));
        }
        if (isFastPrecision()) return (Matrix<E>) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).powScalar(((Real) (Object) exponent).floatValue());
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).powScalar(((Real) (Object) exponent).doubleValue());
    }

    @SuppressWarnings("unchecked")
    private Matrix<Complex> executeComplexUnaryOp(Matrix<Complex> a, java.util.function.UnaryOperator<Complex> op) {
        Complex[][] res = new Complex[a.rows()][a.cols()];
        for (int i = 0; i < a.rows(); i++) for (int j = 0; j < a.cols(); j++) res[i][j] = op.apply(a.get(i, j));
        return Matrix.of(res, (Ring<Complex>) (Object) a.getScalarRing());
    }

    @Override
    @SuppressWarnings("unchecked")
    public E trace(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            E res = (E) (Object) executeComplexTrace((Matrix<Complex>) (Object) a);
            return res;
        }
        if (isFastPrecision()) {
            return (E) (Object) SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a).trace();
        }
        return (E) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).trace();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (isComplex(a)) {
            @SuppressWarnings("unchecked")
            Vector<E> res = (Vector<E>) (Object) executeComplexMatVec((Matrix<Complex>) (Object) a, (Vector<Complex>) (Object) b);
            return res;
        }
        return (Vector<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).multiply((Vector<Real>) (Object) b);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        Ring<E> ring = a.getScalarRing();
        int m = a.rows();
        int n = a.cols();
        
        if (m != n) {
            // Least Squares via QR
            QRResult<E> qr = qr(a);
            Matrix<E> Q = qr.getQ();
            Matrix<E> R = qr.getR();
            Vector<E> QtB = Q.transpose().multiply(b);
            
            // Solve R x = QtB (R is upper triangular n x n)
            return solveTriangular(R, QtB, true, false, false);
        }

        // Square Case: LU
        return solve(lu(a), b);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        Ring<E> ring = b.getScalarRing();
        int n = b.dimension();
        
        // 1. Permute b: b' = P * b
        Vector<E> P = lu.getP();
        E[] bDataArr = (E[]) new Object[n];
        for (int i = 0; i < n; i++) {
            int idx = (int) getRealValue(P.get(i));
            bDataArr[i] = b.get(idx);
        }
        Vector<E> pb = Vector.of(java.util.Arrays.asList(bDataArr), ring);
        
        // 2. Solve L y = Pb (Lower unit triangular)
        Vector<E> y = solveTriangular(lu.getL(), pb, false, false, true);
        
        // 3. Solve U x = y (Upper triangular)
        return solveTriangular(lu.getU(), y, true, false, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E determinant(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        
        LUResult<E> lu = lu(a);
        Matrix<E> U = lu.getU();
        
        E det = ring.one();
        for (int i = 0; i < n; i++) det = ring.multiply(det, U.get(i, i));
        
        Vector<E> P = lu.getP();
        int swaps = 0;
        for (int i = 0; i < n; i++) if (Math.abs(getRealValue(P.get(i)) - i) > 0.1) swaps++;
        if ((swaps / 2) % 2 != 0) {
            if (ring instanceof org.episteme.core.mathematics.sets.Reals) det = (E) Real.of(-getRealValue(det));
            else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) det = (E) ((Complex)det).negate();
        }
        
        return det;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> inverse(Matrix<E> a) {
        int m = a.rows();
        int n = a.cols();
        Ring<E> ring = a.getScalarRing();
        
        if (m != n) {
            // Pseudo-inverse via SVD
            SVDResult<E> svd = svd(a);
            Matrix<E> U = svd.getU();
            Vector<E> S = svd.getS();
            Matrix<E> V = svd.getV();
            
            int k = Math.min(m, n);
            double[] sData = new double[k];
            for (int i=0; i<k; i++) sData[i] = getRealValue(S.get(i));
            
            double[] sInvData = new double[k];
            for (int i = 0; i < k; i++) sInvData[i] = sData[i] > 1e-12 ? 1.0 / sData[i] : 0.0;
            
            Matrix<E> Sinv;
            if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
                Sinv = (Matrix<E>)(Object) RealDoubleMatrix.diagonal(sInvData);
            } else {
                Complex[] complexSInv = new Complex[k*k];
                for (int i=0; i<k; i++) {
                    for (int j=0; j<k; j++) complexSInv[i*k+j] = (i==j) ? Complex.of(sInvData[i]) : Complex.ZERO;
                }
                Sinv = (Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<>(k, k, complexSInv), this, ring);
            }

            Matrix<E> V_Sinv = V.multiply(Sinv);
            return V_Sinv.multiply(U.transpose());
        }

        // Square case: Gaussian Elimination for Reals (SIMD)
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            double[] data = toMatrixDoubleArray((Matrix<Real>) (Object) a);
            double[] inv = new double[n * n];
            for (int i = 0; i < n; i++) inv[i * n + i] = 1.0;
            
            var species = getDoubleSpecies();
            for (int k = 0; k < n; k++) {
                int max = k;
                for (int i = k + 1; i < n; i++) if (Math.abs(data[i * n + k]) > Math.abs(data[max * n + k])) max = i;
                if (k != max) {
                    for (int j = k; j < n; j++) {
                        double t = data[k * n + j]; data[k * n + j] = data[max * n + j]; data[max * n + j] = t;
                    }
                    for (int j = 0; j < n; j++) {
                        double t = inv[k * n + j]; inv[k * n + j] = inv[max * n + j]; inv[max * n + j] = t;
                    }
                }
                
                double pivot = data[k * n + k];
                if (Math.abs(pivot) < 1e-18) throw new ArithmeticException("Matrix is singular");
                
                for (int j = k + 1; j < n; j++) data[k * n + j] /= pivot;
                for (int j = 0; j < n; j++) inv[k * n + j] /= pivot;
                data[k * n + k] = 1.0;
                
                for (int i = 0; i < n; i++) {
                    if (i != k) {
                        double f = data[i * n + k];
                        int j = k + 1;
                        for (; j + species.length() <= n; j += species.length()) {
                            var vK = DoubleVector.fromArray(species, data, k * n + j);
                            var vI = DoubleVector.fromArray(species, data, i * n + j);
                            vI.sub(vK.mul(f)).intoArray(data, i * n + j);
                        }
                        for (; j < n; j++) data[i * n + j] -= f * data[k * n + j];
                        
                        j = 0;
                        for (; j + species.length() <= n; j += species.length()) {
                            var vIK = DoubleVector.fromArray(species, inv, k * n + j);
                            var vII = DoubleVector.fromArray(species, inv, i * n + j);
                            vII.sub(vIK.mul(f)).intoArray(inv, i * n + j);
                        }
                        for (; j < n; j++) inv[i * n + j] -= f * inv[k * n + j];
                    }
                }
            }
            return (Matrix<E>) (Object) fromDoubleArray(inv, n, n);
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            // Square case for Complexes: LU based
            LUResult<E> lu = lu(a);
            int dim = n;
            Complex[] invData = new Complex[dim * dim];
            for (int j = 0; j < dim; j++) {
                Complex[] e = new Complex[dim];
                for (int i=0; i<dim; i++) e[i] = (i==j) ? Complex.ONE : Complex.ZERO;
                Vector<E> b = (Vector<E>)(Object) Vector.of(java.util.Arrays.asList(e), ring);
                Vector<E> x = solve(lu, b);
                for (int i = 0; i < dim; i++) invData[i * dim + j] = (Complex) x.get(i);
            }
            return (Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<>(dim, dim, invData), this, ring);
        }
        throw new UnsupportedOperationException("NativeSIMD inverse failed for ring: " + ring);
    }

    @Override
    @SuppressWarnings("unchecked")
    public LUResult<E> lu(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        int n = a.rows();
        int m = a.cols();
        
        if (isRealRing(ring)) {
            double[] data = toMatrixDoubleArray((Matrix<Real>) (Object) a);
            double[] lData = new double[n * n];
            double[] uData = new double[n * n];
            double[] p = new double[n];
            for (int i = 0; i < n; i++) { p[i] = i; lData[i * n + i] = 1.0; }
            
            for (int k = 0; k < Math.min(n, m); k++) {
                int max = k;
                for (int i = k + 1; i < n; i++) if (Math.abs(data[i * n + k]) > Math.abs(data[max * n + k])) max = i;
                if (k != max) {
                    for (int j = 0; j < m; j++) {
                        double t = data[k * m + j]; data[k * m + j] = data[max * m + j]; data[max * m + j] = t;
                    }
                    double t = p[k]; p[k] = p[max]; p[max] = t;
                }
                
                uData[k * n + k] = data[k * n + k];
                for (int i = k + 1; i < n; i++) {
                    if (Math.abs(uData[k * n + k]) > 1e-18) {
                        lData[i * n + k] = data[i * n + k] / uData[k * n + k];
                        for (int j = k + 1; j < m; j++) data[i * m + j] -= lData[i * n + k] * data[k * m + j];
                    }
                }
                for (int j = k + 1; j < m; j++) uData[k * n + j] = data[k * n + j];
            }
            
            return (LUResult<E>) (Object) new LUResult<Real>(
                fromDoubleArray(lData, n, n),
                fromDoubleArray(uData, n, n),
                org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(p)
            );
        } else if (isComplexRing(ring)) {
            Complex[] data = new Complex[n * m];
            for (int i=0; i<n; i++) for (int j=0; j<m; j++) data[i*m + j] = (Complex) a.get(i, j);
            
            Complex[] lData = new Complex[n * n];
            Complex[] uData = new Complex[n * n];
            Complex[] p = new Complex[n];
            for (int i = 0; i < n; i++) { 
                p[i] = Complex.of(i); 
                for (int j=0; j<n; j++) {
                    lData[i*n + j] = (i == j) ? Complex.ONE : Complex.ZERO;
                    uData[i*n + j] = Complex.ZERO;
                }
            }
            
            for (int k = 0; k < Math.min(n, m); k++) {
                int max = k;
                for (int i = k + 1; i < n; i++) if (data[i * m + k].abs() > data[max * m + k].abs()) max = i;
                if (k != max) {
                    for (int j = 0; j < m; j++) {
                        Complex t = data[k * m + j]; data[k * m + j] = data[max * m + j]; data[max * m + j] = t;
                    }
                    Complex t = p[k]; p[k] = p[max]; p[max] = t;
                }
                
                uData[k * n + k] = data[k * m + k];
                for (int i = k + 1; i < n; i++) {
                    if (uData[k * n + k].abs() > 1e-18) {
                        lData[i * n + k] = data[i * m + k].divide(uData[k * n + k]);
                        for (int j = k + 1; j < m; j++) {
                            data[i * m + j] = data[i * m + j].subtract(lData[i * n + k].multiply(data[k * m + j]));
                        }
                    }
                }
                for (int j = k + 1; j < m; j++) uData[k * n + j] = data[k * m + j];
            }
            
            Matrix<E> L = (Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<>(n, n, lData), this, ring);
            Matrix<E> U = (Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<>(n, n, uData), this, ring);
            Vector<E> P = (Vector<E>)(Object) Vector.of(java.util.Arrays.asList(p), ring);

            return new LUResult<>(L, U, P);
        }
        throw new UnsupportedOperationException("NativeSIMD LU failed or not available for ring: " + ring);
    }

    @Override
    @SuppressWarnings("unchecked")
    public QRResult<E> qr(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        int m = a.rows();
        int n = a.cols();
        
        if (isRealRing(ring)) {
            double[] data = toMatrixDoubleArray((Matrix<Real>) (Object) a);
            double[] q = new double[m * m];
            for (int i = 0; i < m; i++) q[i * m + i] = 1.0;
            
            for (int k = 0; k < Math.min(m, n); k++) {
                double norm = 0;
                for (int i = k; i < m; i++) norm += data[i * n + k] * data[i * n + k];
                norm = Math.sqrt(norm);
                double v0 = data[k * n + k] + (data[k * n + k] >= 0 ? norm : -norm);
                double vNorm = v0 * v0 + norm * norm - data[k * n + k] * data[k * n + k];
                vNorm = Math.sqrt(vNorm);
                if (vNorm > 1e-15) {
                    double[] v = new double[m - k];
                    v[0] = v0 / vNorm;
                    for (int i = 1; i < m - k; i++) v[i] = data[(k + i) * n + k] / vNorm;
                    
                    for (int j = k; j < n; j++) {
                        double dot = 0;
                        for (int i = 0; i < m - k; i++) dot += v[i] * data[(k + i) * n + j];
                        for (int i = 0; i < m - k; i++) data[(k + i) * n + j] -= 2 * dot * v[i];
                    }
                    for (int j = 0; j < m; j++) {
                        double dot = 0;
                        for (int i = 0; i < m - k; i++) dot += v[i] * q[(k + i) * m + j];
                        for (int i = 0; i < m - k; i++) q[(k + i) * m + j] -= 2 * dot * v[i];
                    }
                }
            }
            
            double[] rData = new double[m * n];
            for (int i = 0; i < m; i++) for (int j = i; j < n; j++) rData[i * n + j] = data[i * n + j];
            
            QRResult<E> result = (QRResult<E>) (Object) new QRResult<Real>(
                fromDoubleArray(q, m, m).transpose(),
                fromDoubleArray(rData, m, n)
            );
            return result;
        } else if (isComplexRing(ring)) {
            org.ejml.data.ZMatrixRMaj ejmlA = toEJMLComplex(a);
            var qrEjml = org.ejml.dense.row.factory.DecompositionFactory_ZDRM.qr(m, n);
            if (!qrEjml.decompose(ejmlA)) throw new RuntimeException("QR decomposition failed");
            
            Matrix<E> Q = fromEJMLComplex(qrEjml.getQ(null, false));
            Matrix<E> R = fromEJMLComplex(qrEjml.getR(null, false));
            return new QRResult<>(Q, R);
        }
        throw new UnsupportedOperationException("NativeSIMD QR failed for ring: " + ring);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SVDResult<E> svd(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        int m = a.rows();
        int n = a.cols();
        
        if (isRealRing(ring)) {
            boolean transposed = false;
            Matrix<Real> target = (Matrix<Real>) (Object) a;
            if (m < n) { transposed = true; target = target.transpose(); int t = m; m = n; n = t; }
            
            double[] data = toMatrixDoubleArray(target);
            org.ejml.data.DMatrixRMaj ejmlA = new org.ejml.data.DMatrixRMaj(m, n, true, data);
            var svdEjml = org.ejml.dense.row.factory.DecompositionFactory_DDRM.svd(m, n, true, true, false);
            if (!svdEjml.decompose(ejmlA)) throw new RuntimeException("SVD decomposition failed");
            
            Matrix<Real> U = fromDoubleArray(svdEjml.getU(null, false).data, m, m);
            Matrix<Real> V = fromDoubleArray(svdEjml.getV(null, false).data, n, n);
            double[] sValues = svdEjml.getSingularValues();
            Vector<Real> S = org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(sValues);
            
            if (transposed) return (SVDResult<E>) (Object) new SVDResult<>(V, S, U);
            return (SVDResult<E>) (Object) new SVDResult<>(U, S, V);
        } else if (isComplexRing(ring)) {
            org.ejml.data.ZMatrixRMaj ejmlA = toEJMLComplex(a);
            var svdEjml = org.ejml.dense.row.factory.DecompositionFactory_ZDRM.svd(m, n, true, true, false);
            if (!svdEjml.decompose(ejmlA)) throw new RuntimeException("Complex SVD decomposition failed");
            
            Matrix<E> U = fromEJMLComplex(svdEjml.getU(null, false));
            Matrix<E> V = fromEJMLComplex(svdEjml.getV(null, false));
            double[] sValues = svdEjml.getSingularValues();
            Complex[] csValues = new Complex[sValues.length];
            for (int i=0; i<sValues.length; i++) csValues[i] = Complex.of(sValues[i]);
            Vector<E> S = (Vector<E>)(Object) Vector.of(java.util.Arrays.asList(csValues), ring);
            
            return new SVDResult<>(U, S, V);
        }
        throw new UnsupportedOperationException("NativeSIMD SVD failed for ring: " + ring);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        int n = a.rows();
        
        if (isRealRing(ring)) {
            double[] data = toMatrixDoubleArray((Matrix<Real>) (Object) a);
            org.ejml.data.DMatrixRMaj ejmlA = new org.ejml.data.DMatrixRMaj(n, n, true, data);
            var cholEjml = org.ejml.dense.row.factory.DecompositionFactory_DDRM.chol(n, true);
            if (!cholEjml.decompose(ejmlA)) throw new RuntimeException("Cholesky decomposition failed");
            
            Matrix<Real> L = fromDoubleArray(cholEjml.getT(null).data, n, n);
            return (CholeskyResult<E>) (Object) new CholeskyResult<>(L);
        } else if (isComplexRing(ring)) {
            org.ejml.data.ZMatrixRMaj ejmlA = toEJMLComplex(a);
            var cholEjml = org.ejml.dense.row.factory.DecompositionFactory_ZDRM.chol(n, true);
            if (!cholEjml.decompose(ejmlA)) throw new RuntimeException("Complex Cholesky decomposition failed");
            
            Matrix<E> L = fromEJMLComplex(cholEjml.getT(null));
            return new CholeskyResult<>(L);
        }
        throw new UnsupportedOperationException("NativeSIMD Cholesky failed for ring: " + ring);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EigenResult<E> eigen(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        int n = a.rows();
        
        if (isRealRing(ring)) {
            double[] data = toMatrixDoubleArray((Matrix<Real>) (Object) a);
            org.ejml.data.DMatrixRMaj ejmlA = new org.ejml.data.DMatrixRMaj(n, n, true, data);
            var eigenEjml = org.ejml.dense.row.factory.DecompositionFactory_DDRM.eig(n, true, true);
            if (!eigenEjml.decompose(ejmlA)) throw new RuntimeException("Eigen decomposition failed");
            
            Complex[] values = new Complex[n];
            Matrix<Real>[] vectors = new Matrix[n];
            for (int i = 0; i < n; i++) {
                org.ejml.data.Complex_F64 val = eigenEjml.getEigenvalue(i);
                values[i] = Complex.of(val.real, val.imaginary);
                vectors[i] = fromDoubleArray(eigenEjml.getEigenVector(i).data, n, 1);
            }

            // Combine vectors into a single matrix
            Real[][] vData = new Real[n][n];
            for (int j = 0; j < n; j++) {
                for (int i = 0; i < n; i++) {
                    vData[i][j] = (Real) vectors[j].get(i, 0);
                }
            }
            Matrix<Real> V = Matrix.of(vData, (Ring<Real>) ring);
            
            // Values (simplified to Real for now, or use a complex vector if E allows)
            Real[] dData = new Real[n];
            for (int i = 0; i < n; i++) dData[i] = Real.of(values[i].real());
            Vector<Real> D = Vector.of(java.util.Arrays.asList(dData), (Ring<Real>) ring);

            return (EigenResult<E>) (Object) new EigenResult<Real>(V, D);
        } else if (isComplexRing(ring)) {
            throw new UnsupportedOperationException("Complex Eigen decomposition not supported by EJML 0.43 for SIMD backend");
        }
        throw new UnsupportedOperationException("NativeSIMD Eigen failed for ring: " + ring);
    }

    private org.ejml.data.ZMatrixRMaj toEJMLComplex(Matrix<E> a) {
        int r = a.rows(), c = a.cols();
        org.ejml.data.ZMatrixRMaj ejml = new org.ejml.data.ZMatrixRMaj(r, c);
        for (int i=0; i<r; i++) {
            for (int j=0; j<c; j++) {
                Complex val = (Complex) a.get(i, j);
                ejml.set(i, j, val.real(), val.imaginary());
            }
        }
        return ejml;
    }

    private Matrix<E> fromEJMLComplex(org.ejml.data.ZMatrixRMaj ejml) {
        int r = ejml.numRows, c = ejml.numCols;
        Complex[] data = new Complex[r * c];
        for (int i=0; i<r; i++) {
            for (int j=0; j<c; j++) {
                data[i*c + j] = Complex.of(ejml.getReal(i, j), ejml.getImag(i, j));
            }
        }
        return (Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<>(r, c, data), (LinearAlgebraProvider<Complex>)(Object)this, org.episteme.core.mathematics.sets.Complexes.getInstance());
    }

    private Matrix<Real> fromDoubleArray(double[] data, int rows, int cols) {
        return new SIMDRealDoubleMatrix(rows, cols, data);
    }

    // --- Complex Helpers (Internal Fallback) ---

    private Matrix<Complex> executeComplexAdd(Matrix<Complex> a, Matrix<Complex> b) {
        Complex[][] res = new Complex[a.rows()][a.cols()];
        for (int i = 0; i < a.rows(); i++) for (int j = 0; j < a.cols(); j++) res[i][j] = a.get(i, j).add(b.get(i, j));

        @SuppressWarnings("unchecked")
        Ring<Complex> ring = (Ring<Complex>) (Object) a.getScalarRing();
        return Matrix.of(res, ring);
    }

    private Matrix<Complex> executeComplexSubtract(Matrix<Complex> a, Matrix<Complex> b) {
        Complex[][] res = new Complex[a.rows()][a.cols()];
        for (int i = 0; i < a.rows(); i++) for (int j = 0; j < a.cols(); j++) res[i][j] = a.get(i, j).subtract(b.get(i, j));

        @SuppressWarnings("unchecked")
        Ring<Complex> ring = (Ring<Complex>) (Object) a.getScalarRing();
        return Matrix.of(res, ring);
    }

    private Matrix<Complex> executeComplexMultiply(Matrix<Complex> a, Matrix<Complex> b) {
        int rows = a.rows();
        int cols = b.cols();
        int kSize = a.cols();
        Complex[][] res = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Complex sum = Complex.ZERO;
                for (int k = 0; k < kSize; k++) {
                    sum = sum.add(a.get(i, k).multiply(b.get(k, j)));
                }
                res[i][j] = sum;
            }
        }
        @SuppressWarnings("unchecked")
        Matrix<Complex> result = Matrix.of(res, (Ring<Complex>) (Object) a.getScalarRing());
        return result;
    }

    private Matrix<Complex> executeComplexScale(Complex s, Matrix<Complex> a) {
        Complex[][] res = new Complex[a.rows()][a.cols()];
        for (int i = 0; i < a.rows(); i++) for (int j = 0; j < a.cols(); j++) res[i][j] = s.multiply(a.get(i, j));

        @SuppressWarnings("unchecked")
        Ring<Complex> ring = (Ring<Complex>) (Object) a.getScalarRing();
        return Matrix.of(res, ring);
    }

    private Complex executeComplexTrace(Matrix<Complex> a) {
        Complex sum = Complex.ZERO;
        for (int i = 0; i < Math.min(a.rows(), a.cols()); i++) {
            sum = sum.add(a.get(i, i));
        }
        return sum;
    }

    private Matrix<Complex> executeComplexTranspose(Matrix<Complex> a) {
        Complex[][] res = new Complex[a.cols()][a.rows()];
        for (int i = 0; i < a.rows(); i++) for (int j = 0; j < a.cols(); j++) res[j][i] = a.get(i, j);

        @SuppressWarnings("unchecked")
        Ring<Complex> ring = (Ring<Complex>) (Object) a.getScalarRing();
        return Matrix.of(res, ring);
    }

    private Vector<Complex> executeComplexVectorAdd(Vector<Complex> a, Vector<Complex> b) {
        int n = a.dimension();
        double[] aData = toComplexDoubleArray(a);
        double[] bData = toComplexDoubleArray(b);
        double[] cData = new double[2 * n];
        
        int i = 0;
        var species = getDoubleSpecies();
        // n complexes = 2n doubles
        int loopBound = species.loopBound(2 * n);
        for (; i < loopBound; i += species.length()) {
            DoubleVector va = DoubleVector.fromArray(species, aData, i);
            DoubleVector vb = DoubleVector.fromArray(species, bData, i);
            va.add(vb).intoArray(cData, i);
        }
        for (; i < 2 * n; i++) cData[i] = aData[i] + bData[i];
        
        Complex[] res = new Complex[n];
        for (int j=0; j<n; j++) res[j] = Complex.of(cData[2*j], cData[2*j+1]);
        return Vector.of(java.util.Arrays.asList(res), org.episteme.core.mathematics.sets.Complexes.getInstance());
    }

    private Vector<Complex> executeComplexVectorSubtract(Vector<Complex> a, Vector<Complex> b) {
        int n = a.dimension();
        double[] aData = toComplexDoubleArray(a);
        double[] bData = toComplexDoubleArray(b);
        double[] cData = new double[2 * n];
        
        int i = 0;
        var species = getDoubleSpecies();
        int loopBound = species.loopBound(2 * n);
        for (; i < loopBound; i += species.length()) {
            DoubleVector va = DoubleVector.fromArray(species, aData, i);
            DoubleVector vb = DoubleVector.fromArray(species, bData, i);
            va.sub(vb).intoArray(cData, i);
        }
        for (; i < 2 * n; i++) cData[i] = aData[i] - bData[i];
        
        Complex[] res = new Complex[n];
        for (int j=0; j<n; j++) res[j] = Complex.of(cData[2*j], cData[2*j+1]);
        return Vector.of(java.util.Arrays.asList(res), org.episteme.core.mathematics.sets.Complexes.getInstance());
    }

    private Vector<Complex> executeComplexVectorScale(Vector<Complex> v, Complex s) {
        int n = v.dimension();
        double[] cData = new double[2 * n];
        double sre = s.real(), sim = s.imaginary();
        
        // Mask for re/im indexing: [re, im, re, im]
        // Complex mul: (reA*reS - imA*imS) + i(reA*imS + imA*reS)
        for (int j=0; j<n; j++) {
            Complex val = v.get(j);
            cData[2*j] = val.real() * sre - val.imaginary() * sim;
            cData[2*j+1] = val.real() * sim + val.imaginary() * sre;
        }
        
        Complex[] res = new Complex[n];
        for (int j=0; j<n; j++) res[j] = Complex.of(cData[2*j], cData[2*j+1]);
        return Vector.of(java.util.Arrays.asList(res), org.episteme.core.mathematics.sets.Complexes.getInstance());
    }

    private Complex executeComplexVectorDot(Vector<Complex> a, Vector<Complex> b) {
        int n = a.dimension();
        if (isFastPrecision()) {
             return executeComplexFloatVectorDot(a, b);
        }
        double[] aData = toComplexDoubleArray(a);
        double[] bData = toComplexDoubleArray(b);
        
        var species = getDoubleSpecies();
        int upperBound = species.loopBound(n);
        
        double re = 0, im = 0;
        int j = 0;
        if (upperBound > 0) {
            
            // Note: interlaced layout is [r0, i0, r1, i1, ...]
            // We can load two adjacent vectors to get [r0, i0, r1, i1] and [r2, i2, r3, i3] if species is 4.
            // Or use a more general approach: load species length * 2 and de-interlace.
            for (; j < upperBound; j += species.length()) {
                // Load 2*species.length elements
                double[] aBlock = java.util.Arrays.copyOfRange(aData, 2 * j, 2 * (j + species.length()));
                double[] bBlock = java.util.Arrays.copyOfRange(bData, 2 * j, 2 * (j + species.length()));
                
                for (int k=0; k<species.length(); k++) {
                    double ar = aBlock[2*k], ai = aBlock[2*k+1];
                    double br = bBlock[2*k], bi = bBlock[2*k+1];
                    re += ar * br + ai * bi;
                    im += ar * bi - ai * br;
                }
            }
        }
        
        for (; j < n; j++) {
            double ar = aData[2*j], ai = aData[2*j+1];
            double br = bData[2*j], bi = bData[2*j+1];
            re += ar * br + ai * bi;
            im += ar * bi - ai * br;
        }
        return Complex.of(re, im);
    }

    private Complex executeComplexFloatVectorDot(Vector<Complex> a, Vector<Complex> b) {
        int n = a.dimension();
        float[] aData = new float[2 * n];
        float[] bData = new float[2 * n];
        for (int i=0; i<n; i++) {
            Complex ca = a.get(i), cb = b.get(i);
            aData[2*i] = (float) ca.real(); aData[2*i+1] = (float) ca.imaginary();
            bData[2*i] = (float) cb.real(); bData[2*i+1] = (float) cb.imaginary();
        }
        


        float re = 0, im = 0;
        int j = 0;
        
        for (; j < n; j++) {
            float ar = aData[2*j], ai = aData[2*j+1];
            float br = bData[2*j], bi = bData[2*j+1];
            re += ar * br + ai * bi;
            im += ar * bi - ai * br;
        }
        return Complex.of(org.episteme.core.mathematics.numbers.real.RealFloat.of(re), org.episteme.core.mathematics.numbers.real.RealFloat.of(im));
    }

    private Complex executeComplexVectorNorm(Vector<Complex> a) {
        return Complex.of(Math.sqrt(executeComplexVectorDot(a, a).real()), 0);
    }

    private Vector<Complex> executeComplexMatVec(Matrix<Complex> a, Vector<Complex> b) {
        int m = a.rows();
        int n = a.cols();
        double[] bData = toComplexDoubleArray(b);
        Complex[] res = new Complex[m];
        
        for (int i=0; i<m; i++) {
            double re = 0, im = 0;
            for (int j=0; j<n; j++) {
                Complex aij = a.get(i, j);
                re += aij.real() * bData[2*j] - aij.imaginary() * bData[2*j+1];
                im += aij.real() * bData[2*j+1] + aij.imaginary() * bData[2*j];
            }
            res[i] = Complex.of(re, im);
        }
        return Vector.of(java.util.Arrays.asList(res), org.episteme.core.mathematics.sets.Complexes.getInstance());
    }

    // --- Data Utilities ---

    private double[] toMatrixDoubleArray(Matrix<Real> m) {
        if (m instanceof RealDoubleMatrix) {
            RealDoubleMatrix rdm = (RealDoubleMatrix) m;
            double[] data = rdm.getDoubleStorage().getData();
            if (data != null) return data;
            return rdm.toDoubleArray();
        }
        int r = m.rows(), c = m.cols();
        double[] d = new double[r * c];
        for (int i = 0; i < r; i++) for (int j = 0; j < c; j++) d[i * c + j] = m.get(i, j).doubleValue();
        return d;
    }

    private float[] toFloatArray(Vector<Real> v) {
        int n = v.dimension();
        float[] d = new float[n];
        for (int i = 0; i < n; i++) d[i] = v.get(i).floatValue();
        return d;
    }

    private double[] toComplexDoubleArray(Vector<Complex> v) {
        int n = v.dimension();
        double[] d = new double[2 * n];
        for (int i = 0; i < n; i++) {
            Complex c = v.get(i);
            d[2 * i] = c.real();
            d[2 * i + 1] = c.imaginary();
        }
        return d;
    }

    private double[] toDoubleArray(Vector<Real> v) {
        if (v instanceof org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector) {
            RealDoubleVector rdv = (RealDoubleVector) v;
            double[] data = rdv.getRealStorage().getData();
            if (data != null) return data;
            return rdv.toDoubleArray();
        }
        int n = v.dimension();
        double[] d = new double[n];
        for (int i = 0; i < n; i++) d[i] = v.get(i).doubleValue();
        return d;
    }
}
