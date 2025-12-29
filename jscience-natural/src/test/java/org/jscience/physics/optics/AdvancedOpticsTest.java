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

package org.jscience.physics.optics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.classical.waves.optics.*;

public class AdvancedOpticsTest {

    @Test
    public void testLensMakerEquation() {
        // Example: Biconvex lens, radii 10cm, n=1.5
        Real n = Real.of(1.5);
        Real r1 = Real.of(0.1); // 10cm
        Real r2 = Real.of(-0.1); // -10cm (convex away)

        // 1/f = (1.5-1)*(1/0.1 - 1/-0.1) = 0.5 * (10 + 10) = 0.5 * 20 = 10
        // f = 1/10 = 0.1m
        Real f = Optics.lensMakerEquation(n, r1, r2);

        assertEquals(0.1, f.doubleValue(), 1e-6);
    }

    @Test
    public void testMirrorEquation() {
        // Concave mirror f=10cm, object at 20cm (center of curvature)
        // 1/10 = 1/20 + 1/di -> 1/di = 1/20 -> di = 20
        Real f = Real.of(10.0);
        Real doDist = Real.of(20.0);
        Real di = Optics.mirrorEquation(doDist, f);

        assertEquals(20.0, di.doubleValue(), 1e-6);
    }

    @Test
    public void testWaveOpticsDoubleSlit() {
        // d sin(theta) = m lambda -> max intensity
        // if d=2*lambda, sin(theta)=1/2 for m=1 -> theta=30 deg
        Real lambda = Real.of(500e-9); // 500nm
        Real d = Real.of(1000e-9); // 1 micron
        Real i0 = Real.ONE;

        Real theta = Real.of(Math.PI / 6.0); // 30 degrees

        // Intensity at max should be I0
        Real intensity = WaveOptics.doubleSlitIntensity(theta, d, lambda, i0);
        assertEquals(1.0, intensity.doubleValue(), 1e-6);

        // Minima at m=0.5 -> delta = pi -> cos(pi/2)=0
        // sin(theta) = 0.5 * lambda / d = 0.25
        Real thetaMin = Real.of(Math.asin(0.25));
        Real intensityMin = WaveOptics.doubleSlitIntensity(thetaMin, d, lambda, i0);
        assertEquals(0.0, intensityMin.doubleValue(), 1e-6);
    }

    @Test
    public void testSphericalMirrorTrace() {
        // Concave mirror at origin, facing +z... (same logic)

        SphericalMirror mirror = new SphericalMirror(
                Real.of(10.0), Real.of(5.0),
                new Real[] { Real.ZERO, Real.ZERO, Real.ZERO },
                new Real[] { Real.ZERO, Real.ZERO, Real.of(-1.0) });

        LightRay inc = new LightRay(
                Real.of(500.0), Real.ONE,
                new Real[] { Real.ZERO, Real.ZERO, Real.of(-10.0) },
                new Real[] { Real.ZERO, Real.ZERO, Real.ONE });

        LightRay out = mirror.trace(inc);

        assertNotNull(out);
        assertEquals(0.0, out.getDirection()[0].doubleValue(), 1e-6);
        assertEquals(0.0, out.getDirection()[1].doubleValue(), 1e-6);
        assertEquals(-1.0, out.getDirection()[2].doubleValue(), 1e-6);
    }
}
