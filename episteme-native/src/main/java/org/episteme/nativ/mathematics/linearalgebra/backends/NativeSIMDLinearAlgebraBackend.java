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
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;
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

/**
 * SIMD-accelerated Linear Algebra Backend for Real numbers using JDK Vector API.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, CPUBackend.class, SIMDBackend.class, org.episteme.core.technical.algorithm.AlgorithmProvider.class})
@SuppressWarnings("unchecked")
public class NativeSIMDLinearAlgebraBackend<E> implements LinearAlgebraProvider<E>, NativeBackend, CPUBackend, SIMDBackend {

    private static final Logger logger = LoggerFactory.getLogger(NativeSIMDLinearAlgebraBackend.class);
    
    private static VectorSpecies<Double> getDoubleSpecies() {
        return DoubleVector.SPECIES_PREFERRED;
    }
    
    private static VectorSpecies<Float> getFloatSpecies() {
        return FloatVector.SPECIES_PREFERRED;
    }

    private boolean isFastPrecision() {
        return MathContext.getCurrent().isFastPrecision();
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
        if (ring == null) return false;
        Object zero = ring.zero();
        return ring instanceof Reals || ring instanceof org.episteme.core.mathematics.sets.Complexes ||
               zero instanceof org.episteme.core.mathematics.numbers.real.Real ||
               zero instanceof org.episteme.core.mathematics.numbers.complex.Complex;
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

    private boolean isComplex(Object o) {
        if (o instanceof Matrix) return ((Matrix<?>) o).getScalarRing().one() instanceof Complex;
        if (o instanceof Vector) return ((Vector<?>) o).getScalarRing().one() instanceof Complex;
        return o instanceof Complex;
    }

    // --- Vector Operations ---

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (isComplex(a)) return (Vector<E>) (Object) executeComplexVectorAdd((Vector<Complex>) (Object) a, (Vector<Complex>) (Object) b);
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
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (isComplex(a)) return (Vector<E>) (Object) executeComplexVectorSubtract((Vector<Complex>) (Object) a, (Vector<Complex>) (Object) b);
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
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (isComplex(vector)) return (Vector<E>) (Object) executeComplexVectorScale((Vector<Complex>) (Object) vector, (Complex) (Object) scalar);
        int n = vector.dimension();
        double s = ((Real) (Object) scalar).doubleValue();
        
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
    public E dot(Vector<E> a, Vector<E> b) {
        if (isComplex(a)) return (E) (Object) executeComplexVectorDot((Vector<Complex>) (Object) a, (Vector<Complex>) (Object) b);
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
            return (E) (Object) Real.of(res);
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
        
        return (E) (Object) Real.of(res);
    }

    @Override
    public E norm(Vector<E> a) {
        if (isComplex(a)) return (E) (Object) executeComplexVectorNorm((Vector<Complex>) (Object) a);
        return (E) (Object) Real.of(Math.sqrt(((Real) (Object) dot(a, a)).doubleValue()));
    }

    @Override
    public Vector<E> normalize(Vector<E> a) {
        if (isComplex(a)) return LinearAlgebraProvider.super.normalize(a);
        double n = ((Real) (Object) norm(a)).doubleValue();
        if (n == 0) return a;
        return multiply(a, (E) (Object) Real.of(1.0 / n));
    }

    @Override
    public Vector<E> cross(Vector<E> a, Vector<E> b) {
        // Fallback to default SPI for cross, angle, projection as they are geometric helpers
        return LinearAlgebraProvider.super.cross(a, b);
    }

    @Override
    public E angle(Vector<E> a, Vector<E> b) {
        return LinearAlgebraProvider.super.angle(a, b);
    }

    @Override
    public Vector<E> projection(Vector<E> a, Vector<E> b) {
        return LinearAlgebraProvider.super.projection(a, b);
    }

    // --- Matrix Operations ---

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (isComplex(a)) return (Matrix<E>) (Object) executeComplexMultiply((Matrix<Complex>) (Object) a, (Matrix<Complex>) (Object) b);
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
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (isComplex(a)) return (Matrix<E>) (Object) executeComplexAdd((Matrix<Complex>) (Object) a, (Matrix<Complex>) (Object) b);
        
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
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (isComplex(a)) return (Matrix<E>) (Object) executeComplexSubtract((Matrix<Complex>) (Object) a, (Matrix<Complex>) (Object) b);
        
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
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (isComplex(a)) return (Matrix<E>) (Object) executeComplexScale((Complex) (Object) scalar, (Matrix<Complex>) (Object) a);
        
        if (isFastPrecision()) {
            SIMDRealFloatMatrix sa = SIMDRealFloatMatrix.from((Matrix<Real>) (Object) a);
            return (Matrix<E>) (Object) sa.scale(((Real) (Object) scalar).floatValue());
        }

        SIMDRealDoubleMatrix sa = SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a);
        return (Matrix<E>) (Object) sa.scale(((Real) (Object) scalar).doubleValue());
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (isComplex(a)) return (Matrix<E>) (Object) executeComplexTranspose((Matrix<Complex>) (Object) a);
        return (Matrix<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).transpose();
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (isComplex(a)) return (Vector<E>) (Object) executeComplexMatVec((Matrix<Complex>) (Object) a, (Vector<Complex>) (Object) b);
        return (Vector<E>) (Object) SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a).multiply((Vector<Real>) (Object) b);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (isComplex(a)) throw new UnsupportedOperationException("SIMD solve not yet implemented for Complex.");
        
        SIMDRealDoubleMatrix simdA = SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a);
        int m = simdA.rows();
        int n = simdA.cols();
        
        if (m != n) {
             // Rectangular solve (Least Squares) via QR: A = Q R => R x = Q^T b
            logger.debug("Rectangular SIMD solve via QR Decomposition: [{}x{}]", m, n);
            QRResult<E> qr = qr(a);
            Matrix<Real> Q = (Matrix<Real>) (Object) qr.getQ();
            Matrix<Real> R = (Matrix<Real>) (Object) qr.getR();
            
            // QtB = Q^T * b
            Vector<Real> QtB = (Vector<Real>) (Object) Q.transpose().multiply((Vector<Real>) (Object) b);
            
            // Solve R x = QtB via back-substitution (R is economy upper triangular n x n)
            double[] rData = toMatrixDoubleArray(R);
            double[] bData = new double[n];
            for (int i = 0; i < n; i++) bData[i] = ((Real) (Object) QtB.get(i)).doubleValue();
            
            double[] x = new double[n];
            var species = getDoubleSpecies();
            for (int i = n - 1; i >= 0; i--) {
                double sum = 0.0;
                int j = i + 1;
                var vSum = DoubleVector.zero(species);
                for (; j + species.length() <= n; j += species.length()) {
                    var vA = DoubleVector.fromArray(species, rData, i * n + j);
                    var vX = DoubleVector.fromArray(species, x, j);
                    vSum = vSum.add(vA.mul(vX));
                }
                sum = vSum.reduceLanes(ADD);
                for (; j < n; j++) sum += rData[i * n + j] * x[j];
                
                if (Math.abs(rData[i * n + i]) < 1e-15) x[i] = 0.0;
                else x[i] = (bData[i] - sum) / rData[i * n + i];
            }
            return (Vector<E>) (Object) org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(x);
        }

        // Standard Square Case
        double[] x = new double[n];
        for (int i = 0; i < n; i++) x[i] = ((Real) (Object) b.get(i)).doubleValue();
        
        double[] data = simdA.getInternalData();
        var species = getDoubleSpecies();
        
        // Gaussian Elimination
        for (int k = 0; k < n; k++) {
            int max = k;
            for (int i = k + 1; i < n; i++) if (Math.abs(data[i * n + k]) > Math.abs(data[max * n + k])) max = i;
            if (k != max) {
                for (int j = k; j < n; j++) {
                    double temp = data[k * n + j];
                    data[k * n + j] = data[max * n + j];
                    data[max * n + j] = temp;
                }
                double t = x[k]; x[k] = x[max]; x[max] = t;
            }
            
            if (Math.abs(data[k * n + k]) < 1e-15) throw new ArithmeticException("Matrix is singular");
            
            for (int i = k + 1; i < n; i++) {
                double factor = data[i * n + k] / data[k * n + k];
                x[i] -= factor * x[k];
                data[i * n + k] = 0;
                int j = k + 1;
                for (; j + species.length() <= n; j += species.length()) {
                    var vK = DoubleVector.fromArray(species, data, k * n + j);
                    var vI = DoubleVector.fromArray(species, data, i * n + j);
                    vI.sub(vK.mul(factor)).intoArray(data, i * n + j);
                }
                for (; j < n; j++) data[i * n + j] -= factor * data[k * n + j];
            }
        }
        
        // Back-substitution
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            int j = i + 1;
            var vSum = DoubleVector.zero(species);
            for (; j + species.length() <= n; j += species.length()) {
                var vA = DoubleVector.fromArray(species, data, i * n + j);
                var vX = DoubleVector.fromArray(species, x, j);
                vSum = vSum.add(vA.mul(vX));
            }
            sum = vSum.reduceLanes(ADD);
            for (; j < n; j++) sum += data[i * n + j] * x[j];
            x[i] = (x[i] - sum) / data[i * n + i];
        }
        
        return (Vector<E>) (Object) org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(x);
    }

    @Override
    public E determinant(Matrix<E> a) {
        if (isComplex(a)) throw new UnsupportedOperationException("SIMD determinant not yet implemented for Complex.");
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        
        LUResult<E> lu = lu(a);
        Matrix<Real> U = (Matrix<Real>) (Object) lu.getU();
        double det = 1.0;
        for (int i = 0; i < n; i++) det *= ((Real) (Object) U.get(i, i)).doubleValue();
        
        Vector<Real> P = (Vector<Real>) (Object) lu.getP();
        int swaps = 0;
        for (int i = 0; i < n; i++) if (Math.abs(((Real) (Object) P.get(i)).doubleValue() - i) > 0.1) swaps++;
        if ((swaps / 2) % 2 != 0) det = -det;
        
        return (E) (Object) Real.of(det);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (isComplex(a)) throw new UnsupportedOperationException("SIMD inverse not yet implemented for Complex.");
        int m = a.rows();
        int n = a.cols();
        
        if (m != n) {
            // Pseudo-inverse via SVD
            SVDResult<E> svd = svd(a);
            Matrix<Real> U = (Matrix<Real>) (Object) svd.getU();
            Vector<Real> S = (Vector<Real>) (Object) svd.getS();
            Matrix<Real> V = (Matrix<Real>) (Object) svd.getV();
            
            int k = Math.min(m, n);
            double[] sInv = new double[k];
            for (int i = 0; i < k; i++) {
                double val = ((Real) (Object) S.get(i)).doubleValue();
                sInv[i] = val > 1e-12 ? 1.0 / val : 0.0;
            }
            
            Matrix<Real> Sinv = org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix.diagonal(sInv);
            Matrix<Real> V_Sinv = V.multiply(Sinv);
            return (Matrix<E>) (Object) V_Sinv.multiply(U.transpose());
        }
        
        // Square Inverse via Gaussian
        SIMDRealDoubleMatrix simdA = SIMDRealDoubleMatrix.from((Matrix<Real>) (Object) a);
        double[] data = simdA.getInternalData();
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
            if (Math.abs(pivot) < 1e-15) throw new ArithmeticException("Matrix is singular");
            
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
    }

    @Override
    public LUResult<E> lu(Matrix<E> a) {
        if (isComplex(a)) throw new UnsupportedOperationException("SIMD LU not yet implemented for Complex.");
        int n = a.rows();
        double[] data = toMatrixDoubleArray((Matrix<Real>) (Object) a);
        double[] lData = new double[n * n];
        double[] uData = new double[n * n];
        double[] p = new double[n];
        for (int i = 0; i < n; i++) { p[i] = i; lData[i * n + i] = 1.0; }
        
        for (int k = 0; k < n; k++) {
            int max = k;
            for (int i = k + 1; i < n; i++) if (Math.abs(data[i * n + k]) > Math.abs(data[max * n + k])) max = i;
            if (k != max) {
                for (int j = 0; j < n; j++) {
                    double t = data[k * n + j]; data[k * n + j] = data[max * n + j]; data[max * n + j] = t;
                }
                double t = p[k]; p[k] = p[max]; p[max] = t;
            }
            uData[k * n + k] = data[k * n + k];
            for (int i = k + 1; i < n; i++) {
                lData[i * n + k] = data[i * n + k] / uData[k * n + k];
                uData[k * n + i] = data[k * n + i];
                for (int j = k + 1; j < n; j++) data[i * n + j] -= lData[i * n + k] * data[k * n + j];
            }
        }
        
        return (LUResult<E>) (Object) new LUResult<Real>(
            fromDoubleArray(lData, n, n),
            fromDoubleArray(uData, n, n),
            org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(p)
        );
    }

    @Override
    public QRResult<E> qr(Matrix<E> a) {
        if (isComplex(a)) throw new UnsupportedOperationException("SIMD QR not yet implemented for Complex.");
        int m = a.rows();
        int n = a.cols();
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
        
        return (QRResult<E>) (Object) new QRResult<Real>(
            fromDoubleArray(q, m, m).transpose(),
            fromDoubleArray(rData, m, n)
        );
    }

    @Override
    public SVDResult<E> svd(Matrix<E> a) {
        if (isComplex(a)) throw new UnsupportedOperationException("SIMD SVD not yet implemented for Complex.");
        int m = a.rows();
        int n = a.cols();
        boolean transposed = false;
        Matrix<Real> target = (Matrix<Real>) (Object) a;
        if (m < n) { transposed = true; target = target.transpose(); int t = m; m = n; n = t; }
        
        double[] data = toMatrixDoubleArray(target);
        org.ejml.data.DMatrixRMaj ejmlA = new org.ejml.data.DMatrixRMaj(m, n, true, data);
        var svdEjml = org.ejml.dense.row.factory.DecompositionFactory_DDRM.svd(m, n, true, true, false);
        if (!svdEjml.decompose(ejmlA)) throw new RuntimeException("SVD decomposition failed");
        
        Matrix<Real> U = fromDoubleArray(svdEjml.getU(null, false).data, m, m);
        Matrix<Real> V = fromDoubleArray(svdEjml.getV(null, false).data, n, n);
        double[] s = svdEjml.getSingularValues();
        
        if (transposed) return (SVDResult<E>) (Object) new SVDResult<Real>(V, org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(s), U);
        return (SVDResult<E>) (Object) new SVDResult<Real>(U, org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(s), V);
    }

    @Override
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        if (isComplex(a)) throw new UnsupportedOperationException("SIMD Cholesky not yet implemented for Complex.");
        int n = a.rows();
        double[] data = toMatrixDoubleArray((Matrix<Real>) (Object) a);
        double[] l = new double[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                double sum = 0;
                for (int k = 0; k < j; k++) sum += l[i * n + k] * l[j * n + k];
                if (i == j) {
                    double val = data[i * n + i] - sum;
                    if (val <= 0) throw new ArithmeticException("Matrix is not positive definite");
                    l[i * n + i] = Math.sqrt(val);
                } else {
                    l[i * n + j] = (data[i * n + j] - sum) / l[j * n + j];
                }
            }
        }
        return (CholeskyResult<E>) (Object) new CholeskyResult<Real>(fromDoubleArray(l, n, n));
    }

    private Matrix<Real> fromDoubleArray(double[] data, int rows, int cols) {
        return new SIMDRealDoubleMatrix(rows, cols, data);
    }

    // --- Complex Helpers (Internal Fallback) ---

    private Matrix<Complex> executeComplexAdd(Matrix<Complex> a, Matrix<Complex> b) {
        Complex[][] res = new Complex[a.rows()][a.cols()];
        for (int i = 0; i < a.rows(); i++) for (int j = 0; j < a.cols(); j++) res[i][j] = a.get(i, j).add(b.get(i, j));

        Ring<Complex> ring = (Ring<Complex>) (Object) a.getScalarRing();
        return Matrix.of(res, ring);
    }

    private Matrix<Complex> executeComplexSubtract(Matrix<Complex> a, Matrix<Complex> b) {
        Complex[][] res = new Complex[a.rows()][a.cols()];
        for (int i = 0; i < a.rows(); i++) for (int j = 0; j < a.cols(); j++) res[i][j] = a.get(i, j).subtract(b.get(i, j));

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
                    sum = sum.add(a.get(i, k).multiply(b.get(i, k)));
                }
                res[i][j] = sum;
            }
        }
        return Matrix.of(res, (Ring<Complex>) (Object) a.getScalarRing());
    }

    private Matrix<Complex> executeComplexScale(Complex s, Matrix<Complex> a) {
        Complex[][] res = new Complex[a.rows()][a.cols()];
        for (int i = 0; i < a.rows(); i++) for (int j = 0; j < a.cols(); j++) res[i][j] = s.multiply(a.get(i, j));

        Ring<Complex> ring = (Ring<Complex>) (Object) a.getScalarRing();
        return Matrix.of(res, ring);
    }

    private Matrix<Complex> executeComplexTranspose(Matrix<Complex> a) {
        Complex[][] res = new Complex[a.cols()][a.rows()];
        for (int i = 0; i < a.rows(); i++) for (int j = 0; j < a.cols(); j++) res[j][i] = a.get(i, j);

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
        
        var species = getFloatSpecies();
        int upperBound = species.loopBound(n);
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
        int n = v.dimension();
        double[] d = new double[n];
        for (int i = 0; i < n; i++) d[i] = v.get(i).doubleValue();
        return d;
    }
}
