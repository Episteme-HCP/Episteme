/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.core.technical.backend.simd.SIMDBackend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;
import org.episteme.core.technical.backend.Operation;

/**
 * SIMD-accelerated Linear Algebra Backend for Real numbers using JDK Vector API.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({Backend.class, ComputeBackend.class, SIMDBackend.class, LinearAlgebraProvider.class, NativeBackend.class, CPUBackend.class})
public class NativeSIMDLinearAlgebraBackend implements SIMDBackend, CPUBackend, NativeBackend, LinearAlgebraProvider<Real> {

    private static final Logger logger = LoggerFactory.getLogger(NativeSIMDLinearAlgebraBackend.class);
    
    private static VectorSpecies<Double> getSpecies() {
        return DoubleVector.SPECIES_PREFERRED;
    }

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
        return getSpecies().toString();
    }

    @Override
    public int getPreferredVectorWidth() {
        return getSpecies().vectorBitSize();
    }

    @Override
    public void shutdown() {
        // No-op. Vector API does not require explicit shutdown.
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        logger.debug("Entering SIMD multiply: [{}x{}] * [{}x{}]", a.rows(), a.cols(), b.rows(), b.cols());
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
        var species = getSpecies();
        
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
        return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(x);
    }

    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) {
        int n = a.dimension();
        if (b.dimension() != n) throw new IllegalArgumentException("Dimension mismatch");
        
        double[] aData = toDoubleArray(a);
        double[] bData = toDoubleArray(b);
        double[] cData = new double[n];
        
        int i = 0;
        int loopBound = getSpecies().loopBound(n);
        for (; i < loopBound; i += getSpecies().length()) {
            DoubleVector va = DoubleVector.fromArray(getSpecies(), aData, i);
            DoubleVector vb = DoubleVector.fromArray(getSpecies(), bData, i);
            va.add(vb).intoArray(cData, i);
        }
        for (; i < n; i++) {
            cData[i] = aData[i] + bData[i];
        }
        return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(cData);
    }
    @Override
    public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) {
        int n = a.dimension();
        if (b.dimension() != n) throw new IllegalArgumentException("Dimension mismatch");
        
        double[] aData = toDoubleArray(a);
        double[] bData = toDoubleArray(b);
        double[] cData = new double[n];
        
        int i = 0;
        int loopBound = getSpecies().loopBound(n);
        for (; i < loopBound; i += getSpecies().length()) {
            DoubleVector va = DoubleVector.fromArray(getSpecies(), aData, i);
            DoubleVector vb = DoubleVector.fromArray(getSpecies(), bData, i);
            va.sub(vb).intoArray(cData, i);
        }
        for (; i < n; i++) {
            cData[i] = aData[i] - bData[i];
        }
        return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(cData);
    }
    @Override
    public Vector<Real> multiply(Vector<Real> vector, Real scalar) {
        int n = vector.dimension();
        double s = scalar.doubleValue();
        double[] aData = toDoubleArray(vector);
        double[] cData = new double[n];
        
        int i = 0;
        int loopBound = getSpecies().loopBound(n);
        for (; i < loopBound; i += getSpecies().length()) {
            DoubleVector va = DoubleVector.fromArray(getSpecies(), aData, i);
            va.mul(s).intoArray(cData, i);
        }
        for (; i < n; i++) {
            cData[i] = aData[i] * s;
        }
        return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(cData);
    }
    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) {
        int n = a.dimension();
        if (b.dimension() != n) throw new IllegalArgumentException("Dimension mismatch");
        
        double[] aData = toDoubleArray(a);
        double[] bData = toDoubleArray(b);
        
        int i = 0;
        int loopBound = getSpecies().loopBound(n);
        DoubleVector sum = DoubleVector.zero(getSpecies());
        
        for (; i < loopBound; i += getSpecies().length()) {
            DoubleVector va = DoubleVector.fromArray(getSpecies(), aData, i);
            DoubleVector vb = DoubleVector.fromArray(getSpecies(), bData, i);
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
    
    private Matrix<Real> fromDoubleArray(double[] data, int rows, int cols) {
        Real[] reals = new Real[data.length];
        for(int i=0; i<data.length; i++) reals[i] = Real.of(data[i]);
        return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<Real>(reals, rows, cols, Reals.getInstance());
    }
    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        
        // Solve AX = I using Gaussian elimination with partial pivoting
        SIMDRealDoubleMatrix simdA = asSIMD(a);
        double[] data = simdA.getInternalData();
        double[] inv = new double[n * n];
        for (int i = 0; i < n; i++) inv[i * n + i] = 1.0;
        
        var species = getSpecies();
        
        for (int k = 0; k < n; k++) {
            // Pivoting
            int max = k;
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(data[i * n + k]) > Math.abs(data[max * n + k])) max = i;
            }
            if (k != max) {
                // Swap rows in A
                for (int j = k; j < n; j++) {
                    double temp = data[k * n + j];
                    data[k * n + j] = data[max * n + j];
                    data[max * n + j] = temp;
                }
                // Swap rows in Inverse
                for (int j = 0; j < n; j++) {
                    double temp = inv[k * n + j];
                    inv[k * n + j] = inv[max * n + j];
                    inv[max * n + j] = temp;
                }
            }
            
            double pivot = data[k * n + k];
            if (Math.abs(pivot) < 1e-15) throw new ArithmeticException("Matrix is singular");
            
            // Normalize row k
            for (int j = k + 1; j < n; j++) data[k * n + j] /= pivot;
            for (int j = 0; j < n; j++) inv[k * n + j] /= pivot;
            data[k * n + k] = 1.0;
            
            // Eliminate other rows
            for (int i = 0; i < n; i++) {
                if (i != k) {
                    double factor = data[i * n + k];
                    int j = k + 1;
                    // Vectorized elimination for A
                    for (; j + species.length() <= n; j += species.length()) {
                        var vRowK = DoubleVector.fromArray(species, data, k * n + j);
                        var vRowI = DoubleVector.fromArray(species, data, i * n + j);
                        vRowI.sub(vRowK.mul(factor)).intoArray(data, i * n + j);
                    }
                    for (; j < n; j++) data[i * n + j] -= factor * data[k * n + j];
                    
                    // Vectorized elimination for Inverse
                    j = 0;
                    for (; j + species.length() <= n; j += species.length()) {
                        var vInvK = DoubleVector.fromArray(species, inv, k * n + j);
                        var vInvI = DoubleVector.fromArray(species, inv, i * n + j);
                        vInvI.sub(vInvK.mul(factor)).intoArray(inv, i * n + j);
                    }
                    for (; j < n; j++) inv[i * n + j] -= factor * inv[k * n + j];
                }
            }
        }
        return fromDoubleArray(inv, n, n);
    }

    @Override
    public Real determinant(Matrix<Real> a) {
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        
        SIMDRealDoubleMatrix simdA = asSIMD(a);
        double[] data = simdA.getInternalData();
        double[] inv = new double[n * n];
        double det = 1.0;
        var species = getSpecies();
        
        for (int k = 0; k < n; k++) {
            int max = k;
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(data[i * n + k]) > Math.abs(data[max * n + k])) max = i;
            }
            if (k != max) {
                for (int j = k; j < n; j++) {
                    double temp = data[k * n + j];
                    data[k * n + j] = data[max * n + j];
                    data[max * n + j] = temp;
                }
                det = -det;
            }
            
            double pivot = data[k * n + k];
            det *= pivot;
            if (Math.abs(det) < 1e-100) return Real.ZERO;
            
            for (int i = k + 1; i < n; i++) {
                double factor = data[i * n + k] / pivot;
                int j = k + 1;
                for (; j + species.length() <= n; j += species.length()) {
                    var vRowK = DoubleVector.fromArray(species, data, k * n + j);
                    var vRowI = DoubleVector.fromArray(species, data, i * n + j);
                    vRowI.sub(vRowK.mul(factor)).intoArray(data, i * n + j);
                }
                for (; j < n; j++) data[i * n + j] -= factor * data[k * n + j];
            }
        }
        return Real.of(det);
    }

    private SIMDRealDoubleMatrix asSIMD(Matrix<Real> m) {
        if (m instanceof SIMDRealDoubleMatrix) return (SIMDRealDoubleMatrix) m;
        if (m instanceof RealDoubleMatrix) {
            RealDoubleMatrix rdm = (RealDoubleMatrix) m;
            return new SIMDRealDoubleMatrix(rdm.rows(), rdm.cols(), rdm.toDoubleArray());
        }
        int rows = m.rows();
        int cols = m.cols();
        double[] data = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i * cols + j] = m.get(i, j).doubleValue();
            }
        }
        return new SIMDRealDoubleMatrix(rows, cols, data);
    }

    private double[] toMatrixDoubleArray(Matrix<Real> m) {
        int r = m.rows(), c = m.cols();
        double[] d = new double[r * c];
        for (int i = 0; i < r; i++)
            for (int j = 0; j < c; j++)
                d[i * c + j] = m.get(i, j).doubleValue();
        return d;
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<Real> lu(Matrix<Real> a) {
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square for LU");
        double[] data = toMatrixDoubleArray(a);
        double[] L = new double[n * n];
        double[] U = java.util.Arrays.copyOf(data, data.length);
        int[] piv = new int[n];
        for (int i = 0; i < n; i++) piv[i] = i;
        var species = getSpecies();

        for (int k = 0; k < n; k++) {
            // Partial pivoting
            int maxRow = k;
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(U[i * n + k]) > Math.abs(U[maxRow * n + k])) maxRow = i;
            }
            if (maxRow != k) {
                for (int j = 0; j < n; j++) { double t = U[k*n+j]; U[k*n+j] = U[maxRow*n+j]; U[maxRow*n+j] = t; }
                for (int j = 0; j < k; j++) { double t = L[k*n+j]; L[k*n+j] = L[maxRow*n+j]; L[maxRow*n+j] = t; }
                int t = piv[k]; piv[k] = piv[maxRow]; piv[maxRow] = t;
            }
            L[k * n + k] = 1.0;
            double pivot = U[k * n + k];
            if (Math.abs(pivot) < 1e-15) continue;
            for (int i = k + 1; i < n; i++) {
                double factor = U[i * n + k] / pivot;
                L[i * n + k] = factor;
                int j = k;
                for (; j + species.length() <= n; j += species.length()) {
                    var vK = DoubleVector.fromArray(species, U, k * n + j);
                    var vI = DoubleVector.fromArray(species, U, i * n + j);
                    vI.sub(vK.mul(factor)).intoArray(U, i * n + j);
                }
                for (; j < n; j++) U[i * n + j] -= factor * U[k * n + j];
            }
        }
        double[] pivD = new double[n];
        for (int i = 0; i < n; i++) pivD[i] = piv[i];
        return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<>(
            fromDoubleArray(L, n, n), fromDoubleArray(U, n, n),
            org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(pivD)
        );
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<Real> qr(Matrix<Real> a) {
        int m = a.rows(), n = a.cols();
        double[] R = toMatrixDoubleArray(a);
        double[] Q = new double[m * m];
        for (int i = 0; i < m; i++) Q[i * m + i] = 1.0;

        int min = Math.min(m, n);
        for (int k = 0; k < min; k++) {
            // Compute Householder vector
            double normx = 0;
            for (int i = k; i < m; i++) normx += R[i * n + k] * R[i * n + k];
            normx = Math.sqrt(normx);
            if (normx < 1e-15) continue;
            double sign = R[k * n + k] >= 0 ? 1.0 : -1.0;
            double alpha = -sign * normx;
            double[] v = new double[m];
            v[k] = R[k * n + k] - alpha;
            for (int i = k + 1; i < m; i++) v[i] = R[i * n + k];
            double vnorm = 0;
            for (int i = k; i < m; i++) vnorm += v[i] * v[i];
            if (vnorm < 1e-30) continue;
            double beta = 2.0 / vnorm;

            // Apply H to R: R = R - beta * v * (v^T * R)
            for (int j = k; j < n; j++) {
                double dot = 0;
                for (int i = k; i < m; i++) dot += v[i] * R[i * n + j];
                for (int i = k; i < m; i++) R[i * n + j] -= beta * v[i] * dot;
            }
            // Apply H to Q: Q = Q - beta * (Q * v) * v^T
            for (int i = 0; i < m; i++) {
                double dot = 0;
                for (int j2 = k; j2 < m; j2++) dot += Q[i * m + j2] * v[j2];
                for (int j2 = k; j2 < m; j2++) Q[i * m + j2] -= beta * dot * v[j2];
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<>(
            fromDoubleArray(Q, m, m), fromDoubleArray(R, m, n)
        );
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<Real> svd(Matrix<Real> a) {
        // Delegate to Apache Commons Math for SVD (always on classpath)
        int m = a.rows(), n = a.cols();
        double[][] data = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                data[i][j] = a.get(i, j).doubleValue();
        org.apache.commons.math3.linear.SingularValueDecomposition svd =
            new org.apache.commons.math3.linear.SingularValueDecomposition(
                org.apache.commons.math3.linear.MatrixUtils.createRealMatrix(data));
        double[] sVals = svd.getSingularValues();
        org.apache.commons.math3.linear.RealMatrix uMat = svd.getU();
        org.apache.commons.math3.linear.RealMatrix vMat = svd.getV();
        double[] uData = new double[uMat.getRowDimension() * uMat.getColumnDimension()];
        for (int i = 0; i < uMat.getRowDimension(); i++)
            for (int j = 0; j < uMat.getColumnDimension(); j++)
                uData[i * uMat.getColumnDimension() + j] = uMat.getEntry(i, j);
        double[] vData = new double[vMat.getRowDimension() * vMat.getColumnDimension()];
        for (int i = 0; i < vMat.getRowDimension(); i++)
            for (int j = 0; j < vMat.getColumnDimension(); j++)
                vData[i * vMat.getColumnDimension() + j] = vMat.getEntry(i, j);
        return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<>(
            fromDoubleArray(uData, uMat.getRowDimension(), uMat.getColumnDimension()),
            org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(sVals),
            fromDoubleArray(vData, vMat.getRowDimension(), vMat.getColumnDimension())
        );
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<Real> cholesky(Matrix<Real> a) {
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square for Cholesky");
        double[] L = new double[n * n];
        var species = getSpecies();

        for (int j = 0; j < n; j++) {
            double sum = 0;
            for (int k = 0; k < j; k++) sum += L[j * n + k] * L[j * n + k];
            double diag = a.get(j, j).doubleValue() - sum;
            if (diag <= 0) throw new ArithmeticException("Matrix is not positive definite");
            L[j * n + j] = Math.sqrt(diag);

            for (int i = j + 1; i < n; i++) {
                double s = 0;
                for (int k = 0; k < j; k++) s += L[i * n + k] * L[j * n + k];
                L[i * n + j] = (a.get(i, j).doubleValue() - s) / L[j * n + j];
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<>(
            fromDoubleArray(L, n, n)
        );
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<Real> eigen(Matrix<Real> a) {
        // Delegate to Apache Commons Math for Eigen (always on classpath)
        int n = a.rows();
        double[][] data = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                data[i][j] = a.get(i, j).doubleValue();
        org.apache.commons.math3.linear.EigenDecomposition eig =
            new org.apache.commons.math3.linear.EigenDecomposition(
                org.apache.commons.math3.linear.MatrixUtils.createRealMatrix(data));
        double[] eigenvals = eig.getRealEigenvalues();
        org.apache.commons.math3.linear.RealMatrix vMat = eig.getV();
        double[] vData = new double[n * n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                vData[i * n + j] = vMat.getEntry(i, j);
        return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<>(
            fromDoubleArray(vData, n, n),
            org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(eigenvals)
        );
    }
}
