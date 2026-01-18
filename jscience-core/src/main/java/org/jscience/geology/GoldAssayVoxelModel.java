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

package org.jscience.geology;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.geometry.volume.VoxelModel;

/**
 * A VoxelModel simulating a mineral deposit (e.g., Gold Assay) for Geoscience visualization.
 */
public class GoldAssayVoxelModel implements VoxelModel {
    private final int w=80, h=40, d=80; // Large but shallow area

    @Override public int getWidth() { return w; }
    @Override public int getHeight() { return h; }
    @Override public int getDepth() { return d; }

    @Override
    public Real getValue(int x, int y, int z) {
        // Horizontal strata
        double depthFactor = (double)y / h;
        
        // Simulating a rich "vein" using a wave function
        double vein = Math.sin(x * 0.1) * Math.cos(z * 0.1) * (1.0 - depthFactor);
        
        // Base rock density
        double density = 0.2 + depthFactor * 0.3;
        
        // Random "hotspots" of gold
        double noise = Math.sin(x * 0.5 + y * 0.5) * Math.sin(z * 0.5);
        if (noise > 0.8 && y > 15) {
            density += 0.4;
        }

        return Real.of(Math.min(1.0, Math.max(0.0, density + vein * 0.2)));
    }

    @Override public String getName() { return "Gold Deposit Assay (Simulated)"; }
    @Override public String getUnit() { return "g/t (Gold Grade)"; }
}
