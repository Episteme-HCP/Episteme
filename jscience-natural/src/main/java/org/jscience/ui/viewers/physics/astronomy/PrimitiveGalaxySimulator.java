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

package org.jscience.ui.viewers.physics.astronomy;

import java.util.List;

/**
 * Primitive Matrix/Array-less implementation.
 * Operates directly on StarParticle POJOs using double math.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PrimitiveGalaxySimulator implements GalaxySimulator {

    private List<StarParticle> stars;

    // Internal state for cores if needed, but Viewer manages G2 pos currently.
    // Interface asks update to take G2 pos.
    // But simulator needs to update G2 velocity?
    // The current viewer code updates G2 inside update().
    // So the Simulator should manage G2 physics too if collisionMode is on.
    // However, the interface I designed only takes g2x, g2y as input?
    // Taking a step back: G2 state (position/velocity) IS part of the simulation.
    // So logic for G2 physics should be here.

    // Changing interface design decision:
    // Access methods for G2 state or pass a "GalaxyCore" object?
    // Ideally the Viewer just renders. The Simulator holds ALL state.
    // But `g2x/g2y` were fields in Viewer.

    // For this refactor, I will keep it simple:
    // The Simulator updates the particles.
    // The G2 Core physics... I'll execute it here, but need to sync back to Viewer?
    // OR Viewer delegates G2 management to Simulator completely.
    // Let's delegate completely.

    private double g2x, g2y;
    private double g2vx, g2vy;

    @Override
    public void init(List<StarParticle> stars) {
        this.stars = stars;
    }

    public void setGalaxy2State(double x, double y, double vx, double vy) {
        this.g2x = x;
        this.g2y = y;
        this.g2vx = vx;
        this.g2vy = vy;
    }

    public double getG2X() {
        return g2x;
    }

    public double getG2Y() {
        return g2y;
    }

    @Override
    public void update(boolean collisionMode, double unusedX, double unusedY) {
        if (collisionMode) {
            // Move Galaxy Cores
            double dx = g2x - 0;
            double dy = g2y - 0;
            double dist = Math.sqrt(dx * dx + dy * dy);
            double force = 10000.0 / (dist * dist + 100); // Softened gravity

            // Integrate Cores (Simplified 2-body)
            g2vx -= (force * dx / dist);
            g2vy -= (force * dy / dist);
            g2x += g2vx;
            g2y += g2vy;

            // Perturb stars
            for (StarParticle s : stars) {
                // Attractor 1 (0,0)
                double d1 = Math.sqrt(s.x * s.x + s.y * s.y);
                double f1 = 500.0 / (d1 * d1 + 100);
                s.vx -= (f1 * s.x / d1);
                s.vy -= (f1 * s.y / d1);

                // Attractor 2 (g2x, g2y)
                double d2x = s.x - g2x;
                double d2y = s.y - g2y;
                double d2 = Math.sqrt(d2x * d2x + d2y * d2y);
                double f2 = 300.0 / (d2 * d2 + 100);
                s.vx -= (f2 * d2x / d2);
                s.vy -= (f2 * d2y / d2);

                s.x += s.vx;
                s.y += s.vy;
            }
        } else {
            // Static Rotation (Visual only)
            for (StarParticle s : stars) {
                // Rotate around 0,0
                double x = s.x;
                double y = s.y;
                double r = Math.sqrt(x * x + y * y);
                double ang = Math.atan2(y, x);
                double v = (1.0 / (r / 200.0 + 0.1)) * 0.05; // Speed
                ang += v;
                s.x = Math.cos(ang) * r;
                s.y = Math.sin(ang) * r;
            }
        }
    }

    @Override
    public String getName() {
        return "Primitive (double)";
    }
}


