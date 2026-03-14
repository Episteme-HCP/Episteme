/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.matrices.solvers.sparse;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import java.util.Arrays;

/**
 * BiCGSTAB (BiConjugate Gradient Stabilized) solver.
 * <p>
 * This class now delegates to the active {@link LinearAlgebraProvider} via {@link AlgorithmManager}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BiCGSTAB {

    /**
     * Solves Ax = b using BiCGSTAB.
     */
    @SuppressWarnings("unchecked")
    public static Real[] solve(Matrix<Real> A, Real[] b, Real[] x0, Real tolerance, int maxIterations) {
        LinearAlgebraProvider<Real> provider = (LinearAlgebraProvider<Real>) AlgorithmManager.getProvider(LinearAlgebraProvider.class);
        Vector<Real> bVec = org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(Arrays.asList(b), Reals.getInstance());
        Vector<Real> x0Vec = org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(Arrays.asList(x0), Reals.getInstance());
        Vector<Real> result = provider.bicgstab(A, bVec, x0Vec, tolerance, maxIterations);
        
        Real[] res = new Real[result.dimension()];
        for (int i = 0; i < res.length; i++) res[i] = result.get(i);
        return res;
    }
}
