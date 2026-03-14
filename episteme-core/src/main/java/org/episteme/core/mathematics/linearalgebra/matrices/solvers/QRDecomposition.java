/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.matrices.solvers;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.technical.algorithm.AlgorithmManager;

/**
 * QR Decomposition: A = QR where Q is orthogonal, R is upper triangular.
 * <p>
 * This class delegates to the active {@link LinearAlgebraProvider} via {@link AlgorithmManager}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class QRDecomposition {

    /**
     * Computes QR decomposition using the best available LinearAlgebraProvider.
     *
     * @param matrix the matrix to decompose
     * @return the result containing Q and R
     */
    @SuppressWarnings("unchecked")
    public static QRResult<Real> decompose(Matrix<Real> matrix) {
        LinearAlgebraProvider<Real> provider = (LinearAlgebraProvider<Real>) AlgorithmManager.getProvider(LinearAlgebraProvider.class);
        return provider.qr(matrix);
    }
}
