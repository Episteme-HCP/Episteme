/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.numerical.ode;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import java.util.function.BiFunction;

/**
 * Interface for Ordinary Differential Equation (ODE) solvers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface ODEProvider extends AlgorithmProvider {

    double[] solve(BiFunction<Double, double[], double[]> f, double[] y0, double t0, double t1, int steps);

    Real[] solveReal(BiFunction<Real, Real[], Real[]> f, Real[] y0, Real t0, Real t1, int steps);

    @Override
    default String getName() {
        return "ODE Provider";
    }

    @Override
    default String getAlgorithmType() {
        return "ODE Solver";
    }
}
