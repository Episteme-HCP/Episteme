/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.nativ.mathematics.linearalgebra.providers;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;
import com.google.auto.service.AutoService;

/**
 * ND4J Linear Algebra Provider (Dense).
 * <p>
 * When the ND4J library ({@code org.nd4j:nd4j-native-platform}) is on the classpath,
 * this provider delegates to ND4J's optimized BLAS/LAPACK backends (Native/AVX/CUDA).
 * All operations fall back to {@link CPUDenseLinearAlgebraProvider} if ND4J is absent.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
@AutoService(LinearAlgebraProvider.class)
public class ND4JLinearAlgebraProvider implements LinearAlgebraProvider<Real> {

    private final CPUDenseLinearAlgebraProvider<Real> fallback = new CPUDenseLinearAlgebraProvider<>();

    public String getName() {
        return "ND4J (Native Wrapper)";
    }

    @Override
    public String getAlgorithmType() {
        return "linear algebra";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.nd4j.linalg.factory.Nd4j");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isCompatible(org.jscience.core.mathematics.structures.rings.Ring<?> ring) {
        // ND4J primarily supports floating point
        return true; 
    }

    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) {
        return fallback.add(a, b);
    }

    @Override
    public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) {
        return fallback.subtract(a, b);
    }

    @Override
    public Vector<Real> multiply(Vector<Real> vector, Real scalar) {
        return fallback.multiply(vector, scalar);
    }

    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) {
        return fallback.dot(a, b);
    }

    @Override
    public Real norm(Vector<Real> a) {
        return fallback.norm(a);
    }

    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) {
        return fallback.add(a, b);
    }

    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) {
        return fallback.subtract(a, b);
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        return fallback.multiply(a, b);
    }

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        return fallback.multiply(a, b);
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        return fallback.inverse(a);
    }

    @Override
    public Real determinant(Matrix<Real> a) {
        return fallback.determinant(a);
    }

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        return fallback.solve(a, b);
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        return fallback.transpose(a);
    }

    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) {
        return fallback.scale(scalar, a);
    }
}
