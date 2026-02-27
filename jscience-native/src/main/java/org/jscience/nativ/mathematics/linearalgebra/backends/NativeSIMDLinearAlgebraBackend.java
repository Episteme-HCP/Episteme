/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.linearalgebra.backends;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.HardwareAccelerator;
import org.jscience.core.technical.backend.simd.SIMDBackend;
import org.jscience.core.technical.backend.cpu.CPUBackend;
import org.jscience.nativ.technical.backend.nativ.NativeBackend;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;
import org.jscience.core.mathematics.linearalgebra.vectors.GenericVector;
import org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage;

import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.Operation;

/**
 * SIMD-accelerated Linear Algebra Backend for Real numbers using JDK Vector API.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({Backend.class, ComputeBackend.class, SIMDBackend.class, LinearAlgebraProvider.class, NativeBackend.class, CPUBackend.class})
public class NativeSIMDLinearAlgebraBackend implements SIMDBackend, CPUBackend, NativeBackend, LinearAlgebraProvider<Real> {

    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("jdk.incubator.vector.VectorSpecies");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
    
    @Override
    public boolean isLoaded() {
        return isAvailable();
    }

    @Override
    public String getType() {
        return SIMDBackend.super.getType();
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return SIMDBackend.super.getAcceleratorType();
    }

    @Override
    public String getNativeLibraryName() {
        return "jdk.incubator.vector";
    }

    @Override
    public ExecutionContext createContext() {
        return new SIMDExecutionContext();
    }

    private class SIMDExecutionContext implements ExecutionContext {
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
        return ring instanceof Reals;
    }

    @Override
    public int getPriority() {
        return 90; // Higher than standard CPU, lower than BLAS
    }

    @Override
    public String getName() {
        return "Native SIMD Linear Algebra Backend";
    }

    @Override
    public String getSimdLevel() {
        return SPECIES.toString();
    }

    @Override
    public int getPreferredVectorWidth() {
        return SPECIES.vectorBitSize();
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        SIMDRealDoubleMatrix sa = asSIMD(a);
        SIMDRealDoubleMatrix sb = asSIMD(b);
        return sa.multiply(sb);
    }

    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) {
        SIMDRealDoubleMatrix sa = asSIMD(a);
        SIMDRealDoubleMatrix sb = asSIMD(b);
        return sa.add(sb);
    }
    
    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) {
        SIMDRealDoubleMatrix sa = asSIMD(a);
        SIMDRealDoubleMatrix sb = asSIMD(b);
        return sa.subtract(sb);
    }

    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) {
        SIMDRealDoubleMatrix sa = asSIMD(a);
        return sa.scale(scalar.doubleValue());
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        return asSIMD(a).transpose();
    }
    
    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        return asSIMD(a).multiply(b);
    }

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if(!(a.getScalarRing() instanceof Reals)) throw new UnsupportedOperationException("SIMD solve only supports Real field.");
        
        SIMDRealDoubleMatrix simdA = asSIMD(a);
        int n = simdA.rows();
        if (n != simdA.cols()) throw new IllegalArgumentException("Matrix must be square");
        if (b.dimension() != n) throw new IllegalArgumentException("Dimension mismatch");
        
        double[] x = new double[n];
        for(int i=0; i<n; i++) x[i] = b.get(i).doubleValue();
        
        double[] data = simdA.getInternalData();
        var species = DoubleVector.SPECIES_PREFERRED;
        
        for (int k = 0; k < n; k++) {
            int max = k;
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(data[i*n + k]) > Math.abs(data[max*n + k])) max = i;
            }
            if (k != max) {
                 for (int j = k; j < n; j++) {
                    double temp = data[k*n + j];
                    data[k*n + j] = data[max*n + j];
                    data[max*n + j] = temp;
                }
                double t = x[k]; x[k] = x[max]; x[max] = t;
            }
            if (Math.abs(data[k*n + k]) < 1e-12) throw new ArithmeticException("Singular matrix");
            
            for (int i = k + 1; i < n; i++) {
                double factor = data[i*n + k] / data[k*n + k];
                x[i] -= factor * x[k];
                data[i*n + k] = 0;
                
                int j = k + 1;
                for (; j + species.length() <= n; j += species.length()) {
                    var vRowK = DoubleVector.fromArray(species, data, k*n + j);
                    var vRowI = DoubleVector.fromArray(species, data, i*n + j);
                    vRowI.sub(vRowK.mul(factor)).intoArray(data, i*n + j);
                }
                for (; j < n; j++) {
                    data[i*n + j] -= factor * data[k*n + j];
                }
            }
        }
        
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            int j = i + 1;
            var vSum = DoubleVector.zero(species);
            for (; j + species.length() <= n; j += species.length()) {
                 var vA = DoubleVector.fromArray(species, data, i*n + j);
                 var vX = DoubleVector.fromArray(species, x, j);
                 vSum = vSum.add(vA.mul(vX));
            }
            sum = vSum.reduceLanes(jdk.incubator.vector.VectorOperators.ADD);
            for (; j < n; j++) {
                sum += data[i*n + j] * x[j];
            }
            x[i] = (x[i] - sum) / data[i*n + i];
        }
        
        Real[] res = new Real[n];
        for(int i=0; i<n; i++) res[i] = Real.of(x[i]);
        return new GenericVector<Real>(
                 new DenseVectorStorage<Real>(res),
                 (LinearAlgebraProvider<Real>) this,
                 Reals.getInstance()
            );
    }

    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) {
        int n = a.dimension();
        if (b.dimension() != n) throw new IllegalArgumentException("Dimension mismatch");
        
        double[] aData = toDoubleArray(a);
        double[] bData = toDoubleArray(b);
        double[] cData = new double[n];
        
        int i = 0;
        int loopBound = SPECIES.loopBound(n);
        for (; i < loopBound; i += SPECIES.length()) {
            DoubleVector va = DoubleVector.fromArray(SPECIES, aData, i);
            DoubleVector vb = DoubleVector.fromArray(SPECIES, bData, i);
            va.add(vb).intoArray(cData, i);
        }
        for (; i < n; i++) {
            cData[i] = aData[i] + bData[i];
        }
        return fromDoubleArray(cData);
    }
    @Override
    public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) {
        int n = a.dimension();
        if (b.dimension() != n) throw new IllegalArgumentException("Dimension mismatch");
        
        double[] aData = toDoubleArray(a);
        double[] bData = toDoubleArray(b);
        double[] cData = new double[n];
        
        int i = 0;
        int loopBound = SPECIES.loopBound(n);
        for (; i < loopBound; i += SPECIES.length()) {
            DoubleVector va = DoubleVector.fromArray(SPECIES, aData, i);
            DoubleVector vb = DoubleVector.fromArray(SPECIES, bData, i);
            va.sub(vb).intoArray(cData, i);
        }
        for (; i < n; i++) {
            cData[i] = aData[i] - bData[i];
        }
        return fromDoubleArray(cData);
    }
    @Override
    public Vector<Real> multiply(Vector<Real> vector, Real scalar) {
        int n = vector.dimension();
        double s = scalar.doubleValue();
        double[] aData = toDoubleArray(vector);
        double[] cData = new double[n];
        
        int i = 0;
        int loopBound = SPECIES.loopBound(n);
        for (; i < loopBound; i += SPECIES.length()) {
            DoubleVector va = DoubleVector.fromArray(SPECIES, aData, i);
            va.mul(s).intoArray(cData, i);
        }
        for (; i < n; i++) {
            cData[i] = aData[i] * s;
        }
        return fromDoubleArray(cData);
    }
    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) {
        int n = a.dimension();
        if (b.dimension() != n) throw new IllegalArgumentException("Dimension mismatch");
        
        double[] aData = toDoubleArray(a);
        double[] bData = toDoubleArray(b);
        
        int i = 0;
        int loopBound = SPECIES.loopBound(n);
        DoubleVector sum = DoubleVector.zero(SPECIES);
        
        for (; i < loopBound; i += SPECIES.length()) {
            DoubleVector va = DoubleVector.fromArray(SPECIES, aData, i);
            DoubleVector vb = DoubleVector.fromArray(SPECIES, bData, i);
            sum = sum.add(va.mul(vb));
        }
        double res = sum.reduceLanes(jdk.incubator.vector.VectorOperators.ADD);
        
        for (; i < n; i++) {
            res += aData[i] * bData[i];
        }
        return Real.of(res);
    }
    @Override
    public Real norm(Vector<Real> a) {
        return Real.of(Math.sqrt(dot(a, a).doubleValue()));
    }

    private double[] toDoubleArray(Vector<Real> v) {
        int n = v.dimension();
        double[] data = new double[n];
        for(int i=0; i<n; i++) data[i] = v.get(i).doubleValue();
        return data; 
    }
    
    private Vector<Real> fromDoubleArray(double[] data) {
        int n = data.length;
        Real[] reals = new Real[n];
        for(int i=0; i<n; i++) reals[i] = Real.of(data[i]);
        return new GenericVector<Real>(
             new DenseVectorStorage<Real>(reals),
             this,
             Reals.getInstance()
        );
    }
    @Override
    public Matrix<Real> inverse(Matrix<Real> a) { 
        return new org.jscience.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider<Real>().inverse(a); 
    }
    @Override
    public Real determinant(Matrix<Real> a) { throw new UnsupportedOperationException("SIMD determinant not implemented yet."); }

    private SIMDRealDoubleMatrix asSIMD(Matrix<Real> m) {
        if (m instanceof SIMDRealDoubleMatrix) return (SIMDRealDoubleMatrix) m;
        int rows = m.rows();
        int cols = m.cols();
        double[] data = new double[rows * cols];
        for(int i=0; i<rows; i++) {
             for(int j=0; j<cols; j++) {
                 data[i*cols + j] = m.get(i, j).doubleValue();
             }
        }
        return new SIMDRealDoubleMatrix(rows, cols, data);
    }
}
