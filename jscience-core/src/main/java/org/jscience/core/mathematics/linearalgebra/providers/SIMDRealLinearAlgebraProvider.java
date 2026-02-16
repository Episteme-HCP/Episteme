/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.providers;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * SIMD-accelerated Linear Algebra Provider for Real numbers using JDK Vector API.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({LinearAlgebraProvider.class, AlgorithmProvider.class})
public class SIMDRealLinearAlgebraProvider implements LinearAlgebraProvider<Real> {

    private final StandardLinearAlgebraProvider<Real> fallback = new StandardLinearAlgebraProvider<>();

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
    public boolean isCompatible(Ring<?> ring) {
        return ring instanceof Reals;
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public String getName() {
        return "SIMD (Vector API) - Real";
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        try {
            SIMDRealDoubleMatrix sa = asSIMD(a);
            SIMDRealDoubleMatrix sb = asSIMD(b);
            return sa.multiply(sb);
        } catch (Exception e) {
            return fallback.multiply(a, b);
        }
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
        if(!(a.getScalarRing() instanceof Reals)) return fallback.solve(a, b);
        
        // SIMD Gaussian Elimination
        SIMDRealDoubleMatrix simdA = asSIMD(a);
        int n = simdA.rows();
        if (n != simdA.cols()) throw new IllegalArgumentException("Matrix must be square");
        if (b.dimension() != n) throw new IllegalArgumentException("Dimension mismatch");
        
        double[] x = new double[n];
        for(int i=0; i<n; i++) x[i] = b.get(i).doubleValue();
        
        double[] data = simdA.getInternalData();
        var species = jdk.incubator.vector.DoubleVector.SPECIES_PREFERRED;
        
        for (int k = 0; k < n; k++) {
            // Pivot
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
            
            // Eliminate
            for (int i = k + 1; i < n; i++) {
                double factor = data[i*n + k] / data[k*n + k];
                x[i] -= factor * x[k];
                data[i*n + k] = 0;
                
                int j = k + 1;
                for (; j < species.loopBound(n); j += species.length()) {
                    var vRowK = jdk.incubator.vector.DoubleVector.fromArray(species, data, k*n + j);
                    var vRowI = jdk.incubator.vector.DoubleVector.fromArray(species, data, i*n + j);
                    vRowI.sub(vRowK.mul(factor)).intoArray(data, i*n + j);
                }
                for (; j < n; j++) {
                    data[i*n + j] -= factor * data[k*n + j];
                }
            }
        }
        
        // Back Substitution
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            int j = i + 1;
            var vSum = jdk.incubator.vector.DoubleVector.zero(species);
            for (; j < species.loopBound(n); j += species.length()) {
                 var vA = jdk.incubator.vector.DoubleVector.fromArray(species, data, i*n + j);
                 var vX = jdk.incubator.vector.DoubleVector.fromArray(species, x, j);
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
        return new org.jscience.core.mathematics.linearalgebra.vectors.GenericVector<Real>(
                 new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<Real>(res),
                 (org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider<Real>) this,
                 org.jscience.core.mathematics.sets.Reals.getInstance()
            );
    }

    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) { return fallback.add(a, b); }
    @Override
    public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) { return fallback.subtract(a, b); }
    @Override
    public Vector<Real> multiply(Vector<Real> vector, Real scalar) { return fallback.multiply(vector, scalar); }
    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) { return fallback.dot(a, b); }
    @Override
    public Real norm(Vector<Real> a) { return fallback.norm(a); }
    @Override
    public Matrix<Real> inverse(Matrix<Real> a) { return fallback.inverse(a); }
    @Override
    public Real determinant(Matrix<Real> a) { return fallback.determinant(a); }

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
