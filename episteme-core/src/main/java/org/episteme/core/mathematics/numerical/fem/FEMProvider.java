/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.numerical.fem;

import org.episteme.core.mathematics.analysis.fem.Mesh;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * Interface for Finite Element Method (FEM) providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface FEMProvider extends AlgorithmProvider {

    Vector<Real> solvePoisson(Mesh mesh, org.episteme.core.mathematics.analysis.Function<Vector<Real>, Real> sourceTerm);
    double[] solvePoisson(Mesh mesh, java.util.function.ToDoubleFunction<double[]> sourceTerm);

    @Override
    default String getName() {
        return "FEM Provider";
    }

    @Override
    default String getAlgorithmType() {
        return "Finite Element Method";
    }
}
