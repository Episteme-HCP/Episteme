package org.jscience.physics.classical.waves.electromagnetism.fields;

import org.jscience.physics.relativity.Vector4D;

/**
 * Interface for solvers of Maxwell's equations.
 * <p>
 * $\partial_\mu F^{\mu\nu} = \mu_0 J^\nu$
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public interface MaxwellSolver {

    /**
     * Computes the electromagnetic field at a given point in spacetime given a
     * source.
     * 
     * @param point Spacetime coordinate to evaluate field at
     * @return ElectromagneticTensor at that point
     */
    ElectromagneticTensor solve(Vector4D point);

    // Future: solve(Vector4D[] grid, CurrentDensitySource source)
}
