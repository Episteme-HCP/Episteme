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
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
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
import org.episteme.core.technical.backend.ExecutionContext;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;

/**
 * SIMD-accelerated Linear Algebra Backend for Real numbers using JDK Vector API.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, CPUBackend.class, SIMDBackend.class})
public class NativeSIMDLinearAlgebraBackend implements LinearAlgebraProvider<Real>, NativeBackend, CPUBackend, SIMDBackend {

    private static final Logger logger = LoggerFactory.getLogger(NativeSIMDLinearAlgebraBackend.class);
    
    private static VectorSpecies<Double> getSpecies() {
        return DoubleVector.SPECIES_PREFERRED;
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
        return jdk.incubator.vector.VectorSpecies.ofLargestShape(double.class).vectorBitSize() > 0 
            && !isExplicitlyDisabled();
    }
    
    @Override
    public boolean isLoaded() {
        return isAvailable();
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
        SIMDRealDoubleMatrix sa = SIMDRealDoubleMatrix.from(a);
        SIMDRealDoubleMatrix sb = SIMDRealDoubleMatrix.from(b);
        return sa.multiply(sb);
    }

    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) {
        SIMDRealDoubleMatrix sa = SIMDRealDoubleMatrix.from(a);
        SIMDRealDoubleMatrix sb = SIMDRealDoubleMatrix.from(b);
        return sa.add(sb);
    }
    
    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) {
        SIMDRealDoubleMatrix sa = SIMDRealDoubleMatrix.from(a);
        SIMDRealDoubleMatrix sb = SIMDRealDoubleMatrix.from(b);
        return sa.subtract(sb);
    }

    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) {
        SIMDRealDoubleMatrix sa = SIMDRealDoubleMatrix.from(a);
        return sa.scale(scalar.doubleValue());
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        return SIMDRealDoubleMatrix.from(a).transpose();
    }
    
    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        return SIMDRealDoubleMatrix.from(a).multiply(b);
    }

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if(!(a.getScalarRing() instanceof Reals)) throw new UnsupportedOperationException("SIMD solve only supports Real field.");
        
        SIMDRealDoubleMatrix simdA = SIMDRealDoubleMatrix.from(a);
        int m = simdA.rows();
        int n = simdA.cols();
        if (m == n) {
            if (b.dimension() != n) throw new IllegalArgumentException("Dimension mismatch");
        } else {
            // Rectangular solve (Least Squares) via Normal Equations: A^T A x = A^T b
            logger.debug("Rectangular SIMD solve via Normal Equations: [{}x{}]", m, n);
            Matrix<Real> at = transpose(a);
            Matrix<Real> ata = at.multiply(simdA);
            Vector<Real> atb = at.multiply(b);
            return solve(ata, atb); // ata is square
        }
        
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
        return new SIMDRealDoubleMatrix(rows, cols, data);
    }
    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        int m = a.rows();
        int n = a.cols();
        if (m != n) {
             // Rectangular inverse (Pseudo-inverse) via Normal Equations
             logger.debug("Rectangular SIMD pseudo-inverse via Normal Equations: [{}x{}]", m, n);
             if (m > n) {
                 // A+ = (A^T A)^-1 * A^T
                 SIMDRealDoubleMatrix simdA = asSIMD(a);
                 Matrix<Real> at = transpose(simdA);
                 Matrix<Real> ata = at.multiply(simdA);
                 return inverse(ata).multiply(at);
             } else {
                 // A+ = A^T * (A A^T)^-1
                 SIMDRealDoubleMatrix simdA = asSIMD(a);
                 Matrix<Real> at = transpose(simdA);
                 Matrix<Real> aat = simdA.multiply(at);
                 return at.multiply(inverse(aat));
             }
        }
        
        // Solve AX = I using Gaussian elimination with partial pivoting
        SIMDRealDoubleMatrix simdA = SIMDRealDoubleMatrix.from(a);
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
        
        SIMDRealDoubleMatrix simdA = SIMDRealDoubleMatrix.from(a);
        double[] data = simdA.getInternalData();
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
                    vRowI.lanewise(jdk.incubator.vector.VectorOperators.SUB, 
                        vRowK.lanewise(jdk.incubator.vector.VectorOperators.MUL, factor))
                        .intoArray(data, i * n + j);
                }
                for (; j < n; j++) data[i * n + j] -= factor * data[k * n + j];
            }
        }
        return Real.of(det);
    }

    private SIMDRealDoubleMatrix asSIMD(Matrix<Real> m) {
        return SIMDRealDoubleMatrix.from(m);
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
        return new LUResult<Real>(
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
        return new QRResult<Real>(
            fromDoubleArray(Q, m, m), fromDoubleArray(R, m, n)
        );
    }

    @Override
    public SVDResult<Real> svd(Matrix<Real> a) {
        // Economy SVD via Eigen decomposition of A^T A (for m >= n)
        // A = U S V^T  => A^T A = V S^2 V^T
        int m = a.rows();
        int n = a.cols();
        
        logger.debug("Entering SIMD SVD (Economy via A^T A): [{}x{}]", m, n);
        SIMDRealDoubleMatrix simdA = asSIMD(a);
        Matrix<Real> at = transpose(simdA);
        Matrix<Real> ata = at.multiply(simdA);
        EigenResult<Real> eigen = eigen(ata);
        
        Matrix<Real> V = eigen.V();
        Vector<Real> D = eigen.D();
        
        double[] sData = new double[n];
        for (int i = 0; i < n; i++) {
            sData[i] = Math.sqrt(Math.max(0, D.get(i).doubleValue()));
        }
        Vector<Real> S = org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(sData);
        
        // U = A * V * inv(S)
        Matrix<Real> AV = simdA.multiply(V);
        double[] avData = toMatrixDoubleArray(AV);
        for (int j = 0; j < n; j++) {
            double sVal = sData[j];
            if (sVal > 1e-12) {
                double invS = 1.0 / sVal;
                for (int i = 0; i < m; i++) avData[i * n + j] *= invS;
            } else {
                for (int i = 0; i < m; i++) avData[i * n + j] = 0.0;
            }
        }
        Matrix<Real> U = fromDoubleArray(avData, m, n);
        
        return new SVDResult<Real>(U, S, V);
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<Real> cholesky(Matrix<Real> a) {
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square for Cholesky");
        double[] L = new double[n * n];

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
        return new CholeskyResult<Real>(
            fromDoubleArray(L, n, n)
        );
    }

    @Override
    public EigenResult<Real> eigen(Matrix<Real> a) {
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        
        // Use a more numerically stable Cyclic Jacobi method for symmetric matrices
        double[] data = toMatrixDoubleArray(a);
        double[] vData = new double[n * n];
        for (int i = 0; i < n; i++) vData[i * n + i] = 1.0;
        
        int maxSweeps = 50;
        double eps = 1e-15;
        var species = getSpecies();
        
        for (int sweep = 0; sweep < maxSweeps; sweep++) {
            double offDiag = 0;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    offDiag += Math.abs(data[i * n + j]);
                }
            }
            if (offDiag < eps) break;
            
            for (int p = 0; p < n - 1; p++) {
                for (int q = p + 1; q < n; q++) {
                    double apq = data[p * n + q];
                    if (Math.abs(apq) < eps) continue;
                    
                    double app = data[p * n + p];
                    double aqq = data[q * n + q];
                    
                    double tau = (aqq - app) / (2.0 * apq);
                    double t = (tau >= 0) ? 1.0 / (tau + Math.sqrt(1.0 + tau * tau)) 
                                         : 1.0 / (tau - Math.sqrt(1.0 + tau * tau));
                    double c = 1.0 / Math.sqrt(1.0 + t * t);
                    double s = t * c;

                    // Update matrix A - Vectorized row rotations
                    int pIdx = p * n;
                    int qIdx = q * n;
                    int i = 0;
                    int loopBound = species.loopBound(n);
                    for (; i < loopBound; i += species.length()) {
                        DoubleVector vp = DoubleVector.fromArray(species, data, pIdx + i);
                        DoubleVector vq = DoubleVector.fromArray(species, data, qIdx + i);
                        DoubleVector vp_new = vp.mul(c).sub(vq.mul(s));
                        DoubleVector vq_new = vp.mul(s).add(vq.mul(c));
                        vp_new.intoArray(data, pIdx + i);
                        vq_new.intoArray(data, qIdx + i);
                    }
                    for (; i < n; i++) {
                        double tp = data[pIdx + i];
                        double tq = data[qIdx + i];
                        data[pIdx + i] = c * tp - s * tq;
                        data[qIdx + i] = s * tp + c * tq;
                    }
                    
                    // Mirror to columns for symmetry
                    for (int j = 0; j < n; j++) {
                        data[j * n + p] = data[p * n + j];
                        data[j * n + q] = data[q * n + j];
                    }
                    
                    // Reset diagonal and off-diagonal strictly correctly
                    data[p * n + p] = app - t * apq;
                    data[q * n + q] = aqq + t * apq;
                    data[p * n + q] = 0.0;
                    data[q * n + p] = 0.0;
                    
                    // Update eigenvectors V (all columns/rows)
                    i = 0;
                    for (; i + species.length() <= n; i += species.length()) {
                        DoubleVector vp = DoubleVector.fromArray(species, vData, pIdx + i);
                        DoubleVector vq = DoubleVector.fromArray(species, vData, qIdx + i);
                        DoubleVector vp_new = vp.mul(c).sub(vq.mul(s));
                        DoubleVector vq_new = vp.mul(s).add(vq.mul(c));
                        vp_new.intoArray(vData, pIdx + i);
                        vq_new.intoArray(vData, qIdx + i);
                    }
                    for (; i < n; i++) {
                        double vp = vData[pIdx + i];
                        double vq = vData[qIdx + i];
                        vData[pIdx + i] = c * vp - s * vq;
                        vData[qIdx + i] = s * vp + c * vq;
                    }
                }
            }
        }
        
        double[] eigenvalues = new double[n];
        for (int i = 0; i < n; i++) eigenvalues[i] = data[i * n + i];
        
        // Transpose vData because we rotated rows but JScience expects eigenvectors as columns
        double[] vDataCol = new double[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                vDataCol[j * n + i] = vData[i * n + j];
            }
        }

        return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<>(
            fromDoubleArray(vDataCol, n, n),
            org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(eigenvalues)
        );
    }
}
