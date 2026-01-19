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

package org.jscience.medicine.anatomy;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.geometry.volume.VoxelModel;

/**
 * A mock MRI model for brain anatomy visualization.
 */
public class BrainMRIModel implements VoxelModel {
    private final int w=64, h=64, d=64;

    @Override public int getWidth() { return w; }
    @Override public int getHeight() { return h; }
    @Override public int getDepth() { return d; }

    @Override
    public Real getValue(int x, int y, int z) {
        // Simple ellipsoid model for brain lobes
        double dx = (x - 32.0) / 25.0;
        double dy = (y - 32.0) / 30.0;
        double dz = (z - 32.0) / 20.0;
        
        double distSq = dx*dx + dy*dy + dz*dz;
        if (distSq > 1.0) return Real.ZERO;
        
        // Add "sulci" folds using sine waves
        double folds = Math.sin(x*1.5) * Math.cos(z*1.5) * 0.1;
        return Real.of(1.0 - distSq + folds);
    }

    @Override public String getName() { return "Human Brain MRI (Simulated)"; }
    @Override public String getUnit() { return "Normalized Density"; }
}
