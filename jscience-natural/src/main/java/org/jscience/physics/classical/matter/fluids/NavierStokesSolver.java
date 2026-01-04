/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.physics.classical.matter.fluids;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.technical.backend.algorithms.MulticoreNavierStokesProvider;
import org.jscience.technical.backend.algorithms.NavierStokesProvider;

/**
 * Solves the incompressible Navier-Stokes equations using a backend provider.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NavierStokesSolver {

    private NavierStokesProvider provider;

    public NavierStokesSolver() {
        this.provider = new MulticoreNavierStokesProvider();
    }

    public void setProvider(NavierStokesProvider provider) {
        this.provider = provider;
    }

    /**
     * Advances the fluid simulation by one time step.
     * 
     * @param field the fluid field state
     * @param dt    time step size
     */
    /**
     * Advances the fluid simulation by one time step.
     * 
     * @param field the fluid field state
     * @param dt    time step size
     */
    public void solve(FluidField field, Real dt) {
        // 1. Pack data from FluidField to arrays
        int width = field.getWidth();
        int height = field.getHeight();
        int depth = field.getDepth();
        int size = width * height * depth;

        double[] density = new double[size];
        double[] u = new double[size];
        double[] v = new double[size];
        double[] w = new double[size];

        if (field.getDensity() != null)
            pack(field.getDensity(), density, width, height, depth);
        if (field.getVelocityX() != null)
            pack(field.getVelocityX(), u, width, height, depth);
        if (field.getVelocityY() != null)
            pack(field.getVelocityY(), v, width, height, depth);
        if (field.getVelocityZ() != null)
            pack(field.getVelocityZ(), w, width, height, depth);

        // 2. Delegate to Provider
        // Viscosity arbitrarily set to 0.001 for now, or should be retrieved from field
        // properties if available
        double viscosity = 0.001;
        provider.solve(density, u, v, w, dt.doubleValue(), viscosity, width, height, depth);

        // 3. Unpack
        field.setDensity(unpack(density, width, height, depth));
        field.setVelocityX(unpack(u, width, height, depth));
        field.setVelocityY(unpack(v, width, height, depth));
        field.setVelocityZ(unpack(w, width, height, depth));
    }

    private void pack(org.jscience.mathematics.linearalgebra.tensors.Tensor<Real> tensor, double[] target, int w, int h,
            int d) {
        int idx = 0;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                for (int z = 0; z < d; z++) {
                    target[idx++] = tensor.get(x, y, z).doubleValue();
                }
            }
        }
    }

    private org.jscience.mathematics.linearalgebra.tensors.Tensor<Real> unpack(double[] source, int w, int h, int d) {
        Real[] data = new Real[source.length];
        for (int i = 0; i < source.length; i++) {
            data[i] = Real.of(source[i]);
        }
        return new org.jscience.mathematics.linearalgebra.tensors.DenseTensor<>(data, w, h, d);
    }
}
