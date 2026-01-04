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

package org.jscience.physics.classical.waves.electromagnetism.fields;

import org.jscience.mathematics.geometry.Vector4D;
import org.jscience.mathematics.linearalgebra.tensors.DenseTensor;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.technical.backend.algorithms.MaxwellProvider;
import org.jscience.technical.backend.algorithms.MulticoreMaxwellProvider;

/**
 * Solver for Maxwell's equations.
 * <p>
 * $\partial_\mu F^{\mu\nu} = \mu_0 J^\nu$
 * </p>
 * Delegates to {@link MaxwellProvider}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MaxwellSolver {

    private MaxwellProvider provider;

    public MaxwellSolver() {
        this.provider = new MulticoreMaxwellProvider();
    }

    public void setProvider(MaxwellProvider provider) {
        this.provider = provider;
    }

    /**
     * Computes the electromagnetic field at a given point in spacetime.
     * 
     * @param point Spacetime coordinate to evaluate field at
     * @return ElectromagneticTensor at that point
     */
    public ElectromagneticTensor solve(Vector4D point) {
        double[][] raw = provider.computeTensor(point);

        Real[] flats = new Real[16];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                flats[i * 4 + j] = Real.of(raw[i][j]);
            }
        }

        return new ElectromagneticTensor(new DenseTensor<>(flats, 4, 4));
    }
}
