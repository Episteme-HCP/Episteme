/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.numerical.fem;

import org.jscience.core.mathematics.analysis.Function;
import org.jscience.core.mathematics.analysis.fem.Mesh;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * Interface for Finite Element Method (FEM) providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface FEMProvider extends AlgorithmProvider {

    Vector<Real> solvePoisson(Mesh mesh, Function<Vector<Real>, Real> sourceTerm);

    @Override
    default String getName() {
        return "FEM Provider";
    }

    @Override
    default String getAlgorithmType() {
        return "Finite Element Method";
    }
}
