package org.jscience.physics.classical.matter.fluids;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.tensors.Tensor;

/**
 * Represents a fluid field on an Eulerian grid.
 * <p>
 * Stores state variables at discrete grid points:
 * <ul>
 * <li>Density ($\rho$)</li>
 * <li>Pressure ($p$)</li>
 * <li>Velocity field ($\mathbf{u}$)</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class FluidField {

    private final int width;
    private final int height;
    private final int depth;

    private Tensor<Real> density;
    private Tensor<Real> pressure;
    private Tensor<Real> velocityX;
    private Tensor<Real> velocityY;
    private Tensor<Real> velocityZ;

    public FluidField(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public void step(Real dt) {
        // Placeholder for advection/diffusion steps
    }

    // --- Accessors ---
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public Tensor<Real> getDensity() {
        return density;
    }

    public Tensor<Real> getPressure() {
        return pressure;
    }

    public Tensor<Real> getVelocityX() {
        return velocityX;
    }

    public Tensor<Real> getVelocityY() {
        return velocityY;
    }

    public Tensor<Real> getVelocityZ() {
        return velocityZ;
    }
}
