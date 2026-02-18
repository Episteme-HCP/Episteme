/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.technical.algorithm.linearalgebra;

import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.nativ.mathematics.linearalgebra.matrices.backends.NativeCPULinearAlgebraBackend;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;

/**
 * Native multicore linear algebra provider using BLAS/LAPACK.
 * <p>
 * Delegates to native BLAS for {@link RealDoubleMatrix} operations and
 * falls back to {@link CPUDenseLinearAlgebraProvider} for generic operations.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@SuppressWarnings("rawtypes") // LinearAlgebraProvider.class is a raw type reference (unavoidable in @AutoService)
@AutoService({LinearAlgebraProvider.class, AlgorithmProvider.class})
public class NativeMulticoreLinearAlgebraProvider implements LinearAlgebraProvider<Real> {

    private final NativeCPULinearAlgebraBackend backend = new NativeCPULinearAlgebraBackend();
    private final CPUDenseLinearAlgebraProvider<Real> fallback = new CPUDenseLinearAlgebraProvider<>();

    public String getName() {
        return "Native JScience (OpenBLAS)";
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (a instanceof RealDoubleMatrix && b instanceof RealDoubleMatrix) {
            RealDoubleMatrix adm = (RealDoubleMatrix) a;
            RealDoubleMatrix bdm = (RealDoubleMatrix) b;
            RealDoubleMatrix cdm = RealDoubleMatrix.of(new double[a.rows() * b.cols()], a.rows(), b.cols());
            
            backend.dgemm(adm.rows(), adm.cols(), bdm.cols(), 
                adm.getBuffer(), adm.cols(), 
                bdm.getBuffer(), bdm.cols(), 
                cdm.getBuffer(), cdm.cols(), 
                1.0, 0.0);
            return cdm;
        }
        return fallback.multiply(a, b);
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
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) { return fallback.add(a, b); }
    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) { return fallback.subtract(a, b); }
    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) { return fallback.multiply(a, b); }
    @Override
    public Matrix<Real> inverse(Matrix<Real> a) { return fallback.inverse(a); }
    @Override
    public Real determinant(Matrix<Real> a) { return fallback.determinant(a); }
    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) { return fallback.solve(a, b); }
    @Override
    public Matrix<Real> transpose(Matrix<Real> a) { return fallback.transpose(a); }
    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) { return fallback.scale(scalar, a); }

    @Override
    public boolean isAvailable() {
        return backend != null && backend.isAvailable();
    }
}
