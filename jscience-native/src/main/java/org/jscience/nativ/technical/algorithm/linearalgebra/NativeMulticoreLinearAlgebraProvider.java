/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.technical.algorithm.linearalgebra;

import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.nativ.mathematics.linearalgebra.matrices.backends.NativeBLASBackend;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;

/**
 * Native multicore linear algebra provider using BLAS/LAPACK.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@AutoService(LinearAlgebraProvider.class)
public class NativeMulticoreLinearAlgebraProvider implements LinearAlgebraProvider<Real> {

    private final NativeBLASBackend backend = new NativeBLASBackend();

    @Override
    public String getName() {
        return "JScience Native (OpenBLAS)";
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
        throw new UnsupportedOperationException("Only RealDoubleMatrix supported by Native provider");
    }

    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) { throw new UnsupportedOperationException(); }
    @Override
    public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) { throw new UnsupportedOperationException(); }
    @Override
    public Vector<Real> multiply(Vector<Real> vector, Real scalar) { throw new UnsupportedOperationException(); }
    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) { throw new UnsupportedOperationException(); }
    @Override
    public Real norm(Vector<Real> a) { throw new UnsupportedOperationException(); }
    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) { throw new UnsupportedOperationException(); }
    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) { throw new UnsupportedOperationException(); }
    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) { throw new UnsupportedOperationException(); }
    @Override
    public Matrix<Real> inverse(Matrix<Real> a) { throw new UnsupportedOperationException(); }
    @Override
    public Real determinant(Matrix<Real> a) { throw new UnsupportedOperationException(); }
    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) { throw new UnsupportedOperationException(); }
    @Override
    public Matrix<Real> transpose(Matrix<Real> a) { throw new UnsupportedOperationException(); }
    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) { throw new UnsupportedOperationException(); }

    @Override
    public boolean isAvailable() {
        return backend != null && backend.isAvailable();
    }
}
