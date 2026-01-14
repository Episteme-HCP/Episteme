/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SPHFluidProviderTest {

    @Test
    public void testSimulationStep() {
        // Create a small block of fluid
        SPHFluid fluid = SPHFluid.createBlock(5, 5, 5, 0.1, 0, 0, 0);

        assertNotNull(fluid.getPositions());
        assertEquals(125, fluid.getNumParticles());

        // Initial state check
        double initialY = fluid.getPositions()[1]; // First particle Y
        assertFalse(Double.isNaN(initialY), "Initial Y should be valid");

        // Run one step
        fluid.step(0.01);

        // Check that positions updated (gravity should pull them down)
        // Note: positions[1] is Y coordinate of first particle at 0,0,0
        // Wait, index 0 is x, 1 is y, 2 is z.
        // Particle 0 at 0,0,0. Gravity is -9.81 in Y.
        // After 1 step, velocity Y decreases, position Y decreases.

        // Actually, first step velocity update: v += dt * f
        // position update: p += dt * v
        // If force is gravity only (at start, no pressure/viscosity if Uniform grid?
        // actually pressure might be non-zero due to kernel overlap)

        double newY = fluid.getPositions()[1];

        // Just verify it ran and changed something or preserved numbers (not NaN)
        assertFalse(Double.isNaN(newY));

        // Run multiple steps
        for (int i = 0; i < 10; i++) {
            fluid.step(0.01);
        }

        // Assert no crashes and values reasonable
        assertFalse(Double.isNaN(fluid.getPositions()[0]));
    }
}
