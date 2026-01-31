/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.linearalgebra.backends;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.backends.CPUDenseLinearAlgebraProvider;
import org.jscience.core.technical.backend.math.MatrixBackend;
import org.jscience.nativ.technical.backend.math.PanamaBlasBackend;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.nativ.mathematics.linearalgebra.matrices.NativeMatrix;
import org.jscience.core.mathematics.linearalgebra.vectors.RealDoubleVector;
import org.jscience.nativ.mathematics.linearalgebra.vectors.NativeVector;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage;
import org.jscience.core.mathematics.linearalgebra.vectors.storage.RealDoubleVectorStorage;

/**
 * Native implementation of LinearAlgebraProvider using BLASBackend.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class NativeLinearAlgebraProvider extends CPUDenseLinearAlgebraProvider<Real> {

    private final MatrixBackend blas;

    public NativeLinearAlgebraProvider() {
        super(Reals.getInstance());
        this.blas = new PanamaBlasBackend();
    }

    @Override
    public String getId() {
        return "native_blas";
    }

    @Override
    public String getName() {
        return "Native BLAS Linear Algebra Provider";
    }

    @Override
    public boolean isAvailable() {
        return blas.isAvailable();
    }

    @Override
    public boolean isCompatible(org.jscience.core.mathematics.structures.rings.Ring<?> ring) {
        return ring instanceof org.jscience.core.mathematics.sets.Reals;
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (blas.isAvailable() && a instanceof RealDoubleMatrix && b instanceof RealDoubleMatrix) {
            RealDoubleMatrix ma = (RealDoubleMatrix) a;
            RealDoubleMatrix mb = (RealDoubleMatrix) b;
            
            if (ma.cols() != mb.rows()) {
                throw new IllegalArgumentException("Matrix dimensions mismatch");
            }

            // Create result matrix (Direct/Native)
            NativeMatrix res = new NativeMatrix(ma.rows(), mb.cols());
            
            // Call BLAS
            // We need to bridge RealDoubleMatrix buffers to NativeMatrix/BLAS
            blas.dgemm(
                ma.rows(), ma.cols(), mb.cols(),
                ma.getBuffer(), ma.cols(),
                mb.getBuffer(), mb.cols(),
                res.getBuffer(), mb.cols(),
                1.0, 0.0
            );
            
            // Wrap in RealDoubleMatrix
            return RealDoubleMatrix.of(res);
        }
        return super.multiply(a, b);
    }

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        if (blas.isAvailable() && a instanceof RealDoubleMatrix && b instanceof RealDoubleVector) {
            RealDoubleMatrix ma = (RealDoubleMatrix) a;
            RealDoubleVector vb = (RealDoubleVector) b;

            if (ma.cols() != vb.dimension()) {
                throw new IllegalArgumentException("Matrix-Vector dimension mismatch");
            }

            NativeVector res = new NativeVector(ma.rows());
            
            blas.dgemv(
                ma.rows(), ma.cols(),
                ma.getBuffer(), ma.cols(),
                vb.getBuffer(), 1,
                res.getBuffer(), 1,
                1.0, 0.0
            );

            return RealDoubleVector.of(res);
        }
        return super.multiply(a, b);
    }

    @Override
    public Real norm(Vector<Real> a) {
        if (blas.isAvailable() && a instanceof RealDoubleVector) {
            RealDoubleVector v = (RealDoubleVector) a;
            return Real.of(((PanamaBlasBackend) blas).dnrm2(v.dimension(), v.getBuffer(), 1));
        }
        return super.norm(a);
    }

    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) {
        if (blas.isAvailable() && a instanceof RealDoubleVector && b instanceof RealDoubleVector) {
            RealDoubleVector va = (RealDoubleVector) a;
            RealDoubleVector vb = (RealDoubleVector) b;
            return Real.of(((PanamaBlasBackend) blas).ddot(va.dimension(), va.getBuffer(), 1, vb.getBuffer(), 1));
        }
        return super.dot(a, b);
    }

    @Override
    public Vector<Real> multiply(Vector<Real> vector, Real scalar) {
        if (blas.isAvailable() && vector instanceof RealDoubleVector) {
            RealDoubleVector v = (RealDoubleVector) vector;
            RealDoubleVector res = RealDoubleVector.of((RealDoubleVectorStorage) v.getRealStorage().copy()); // Copy first
            ((PanamaBlasBackend) blas).dscal(res.dimension(), scalar.doubleValue(), res.getBuffer(), 1);
            return res;
        }
        return super.multiply(vector, scalar);
    }

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if (blas.isAvailable() && a instanceof RealDoubleMatrix && b instanceof RealDoubleVector) {
            RealDoubleMatrix ma = (RealDoubleMatrix) a;
            RealDoubleVector vb = (RealDoubleVector) b;
            int n = ma.rows();
            
            // LAPACK dgesv modifies A and B. We must copy.
            RealDoubleMatrix targetA = RealDoubleMatrix.of((RealDoubleMatrixStorage) ma.getDoubleStorage().clone());
            RealDoubleVector targetB = RealDoubleVector.of((RealDoubleVectorStorage) vb.getRealStorage().copy());
            
            IntBuffer ipiv = IntBuffer.allocate(n);
            
            int info = ((PanamaBlasBackend) blas).dgesv(n, 1, targetA.getBuffer(), n, ipiv, targetB.getBuffer(), 1);
            if (info != 0) throw new RuntimeException("LAPACK dgesv failed with info=" + info);
            
            return targetB;
        }
        return super.solve(a, b);
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        if (blas.isAvailable() && a instanceof RealDoubleMatrix) {
            RealDoubleMatrix ma = (RealDoubleMatrix) a;
            int n = ma.rows();
            
            RealDoubleMatrix targetA = RealDoubleMatrix.of((RealDoubleMatrixStorage) ma.getDoubleStorage().clone());
            IntBuffer ipiv = IntBuffer.allocate(n);
            
            PanamaBlasBackend b = (PanamaBlasBackend) blas;
            int info = b.dgetrf(n, n, targetA.getBuffer(), n, ipiv);
            if (info != 0) throw new RuntimeException("LAPACK dgetrf failed with info=" + info);
            
            info = b.dgetri(n, targetA.getBuffer(), n, ipiv);
            if (info != 0) throw new RuntimeException("LAPACK dgetri failed with info=" + info);
            
            return targetA;
        }
        return super.inverse(a);
    }
}


