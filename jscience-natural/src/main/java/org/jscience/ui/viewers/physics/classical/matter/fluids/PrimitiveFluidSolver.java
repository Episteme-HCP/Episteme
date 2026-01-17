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

package org.jscience.ui.viewers.physics.classical.matter.fluids;

/**
 * Primitive double-based implementation of Fluid Simulation.
 * Optimized for raw performance using primitive arrays and math.
 *
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * Prim, R. C. (1957). Shortest connection networks and some generalizations. <i>Bell System Technical Journal</i>, 36(6), 1389-1401.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PrimitiveFluidSolver implements FluidSolver {

    // private int N;
    private int SCALE;

    // For this simple demo, the "field" is procedurally generated noise
    // rather than a stored grid, to match the original implementation's behavior.
    // However, to support "solving", we might want to store it?
    // The original code calculated flow *on the fly* using Perlin-ish noise.
    // To strictly follow the "Solver" pattern, we should probably stick to that
    // or upgrade it to a real grid.
    // For faithful benchmarking of the *original* code, we keep calculations
    // on-the-fly.

    // External forces can be stored or applied transiently.
    // The original code applied mouse force *during* the getFlowAt call.

    private double reactionMouseX = -1;
    private double reactionMouseY = -1;
    // Unused fields removed

    @Override
    public void initialize(int size, int scale) {
        // this.N = size;
        this.SCALE = scale;
    }

    @Override
    public double[] getFlowAt(double px, double py) {
        // Simplified flow field based on simplex-like noise (from original)
        double scalePos = SCALE;
        double x = px / scalePos;
        double y = py / scalePos;

        // We need zOff here, but it's passed in step().
        // We should store state if we want to be clean.
        // Let's assume zOff is managed by the caller or passed here?
        // The interface defines step() for time evolution, lets store time/zOff.

        // Wait, the original code computed zOff in animation timer and passed it
        // implicitly?
        // No, `zOff` was a field in the Viewer.
        // I will add zOff setter or manage it internally?
        // Better: step() assumes it updates internal state.

        // RE-READING ORIGINAL CODE:
        // getFlowAt used `zOff`.

        // I will rely on an internal zOff updated via step(),
        // OR better, since zOff is just time...

        return calculateFlow(x, y);
    }

    private double _zOff = 0;
    private double _viscosity = 0.0001;

    @Override
    public void step(double speed, double viscosity, double zOff) {
        this._zOff = zOff;
        this._viscosity = viscosity;
        // Reset transient forces each frame? Or decay them?
        // Original code: "if (mousePressed)" inside getFlowAt.
        // We will handle interaction storage.
    }

    private double[] calculateFlow(double x, double y) {
        double zOff = _zOff;

        double vx = Math.sin(x * 0.1 + zOff) * Math.cos(y * 0.08 + zOff * 0.7);
        double vy = Math.cos(x * 0.08 + zOff * 0.6) * Math.sin(y * 0.1 + zOff);

        // Apply Stored Force
        if (reactionMouseX > 0) {
            double dx = (x * SCALE) - reactionMouseX;
            double dy = (y * SCALE) - reactionMouseY;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < 100) {
                double influence = (100 - dist) / 100.0;
                // Direction is away from mouse? Or drag?
                // Original: "vx += influence * 2 * Math.signum(-dx)" -> towards mouse?
                // Wait, dx = px - mouseX. -dx = mouseX - px.
                vx += influence * 2 * Math.signum(-dx);
                vy += influence * 2 * Math.signum(-dy);
            }
        }

        // Dampen by viscosity
        vx *= (1.0 - _viscosity);
        vy *= (1.0 - _viscosity);

        return new double[] { vx, vy };
    }

    @Override
    public void addForce(double x, double y, double dx, double dy) {
        // For the procedural version, we just store the "interaction point"
        // simulating the original behavior where we check mouse pos.
        // We don't use vector force (dx,dy) yet in the original logic, just position.
        this.reactionMouseX = x;
        this.reactionMouseY = y;
    }

    public void clearForce() {
        this.reactionMouseX = -1;
    }

    @Override
    public String getName() {
        return "Primitive (double[])";
    }
}


