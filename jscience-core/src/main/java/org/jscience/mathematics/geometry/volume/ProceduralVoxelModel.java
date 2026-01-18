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

package org.jscience.mathematics.geometry.volume;

import org.jscience.mathematics.numbers.real.Real;

/**
 * A VoxelModel that generates data procedurally using 'Real' math.
 */
public class ProceduralVoxelModel implements VoxelModel {
    private final int w, h, d;
    private final String name;

    public ProceduralVoxelModel(String name, int w, int h, int d) {
        this.name = name;
        this.w = w;
        this.h = h;
        this.d = d;
    }

    @Override public int getWidth() { return w; }
    @Override public int getHeight() { return h; }
    @Override public int getDepth() { return d; }

    @Override
    public Real getValue(int x, int y, int z) {
        // High-precision coordinates
        Real dx = Real.of(x - w/2.0).divide(Real.of(w/2.0));
        Real dy = Real.of(y - h/2.0).divide(Real.of(h/2.0));
        Real dz = Real.of(z - d/2.0).divide(Real.of(d/2.0));
        
        Real distSq = dx.multiply(dx).add(dy.multiply(dy)).add(dz.multiply(dz));
        Real dist = distSq.sqrt();
        
        if (dist.doubleValue() > 0.9) return Real.ZERO;
        
        // Procedural noise approximation with Reals
        double structure = Math.sin(x * 0.25) * Math.cos(y * 0.25) * Math.sin(z * 0.25);
        
        return Real.ONE.subtract(dist).multiply(Real.of(0.6 + 0.4 * structure));
    }

    @Override public String getName() { return name; }
}
