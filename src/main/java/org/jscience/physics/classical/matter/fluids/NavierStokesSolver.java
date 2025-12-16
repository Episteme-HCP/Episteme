package org.jscience.physics.classical.matter.fluids;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface for Navier-Stokes solvers.
 * <p>
 * Solves the incompressible Navier-Stokes equations:
 * <br>
 * $\frac{\partial \mathbf{u}}{\partial t} + (\mathbf{u} \cdot \nabla)
 * \mathbf{u} = -\frac{1}{\rho} \nabla p + \nu \nabla^2 \mathbf{u} + \mathbf{f}$
 * <br>
 * $\nabla \cdot \mathbf{u} = 0$
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public interface NavierStokesSolver {

    /**
     * Advances the fluid simulation by one time step.
     * 
     * @param field the fluid field state
     * @param dt    time step size
     */
    void solve(FluidField field, Real dt);

}
