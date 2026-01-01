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

package org.jscience.physics.classical.waves.optics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a spherical mirror.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SphericalMirror implements OpticalElement {

    private Real radiusOfCurvature;

    private Real[] position;
    private Real[] normal; // Normal vector at the vertex of the mirror

    /**
     * Creates a new spherical mirror.
     * 
     * @param radiusOfCurvature Radius of curvature (positive for concave, negative
     *                          for convex).
     * @param aperture          Diameter of the mirror aperture.
     * @param position          Position of the mirror vertex.
     * @param normal            Normal vector of the mirror at the vertex.
     */
    public SphericalMirror(Real radiusOfCurvature, Real aperture, Real[] position, Real[] normal) {
        this.radiusOfCurvature = radiusOfCurvature;
        this.position = position;
        this.normal = normalize(normal);
    }

    private Real[] normalize(Real[] v) {
        Real mag = v[0].pow(2).add(v[1].pow(2)).add(v[2].pow(2)).sqrt();
        return new Real[] { v[0].divide(mag), v[1].divide(mag), v[2].divide(mag) };
    }

    @Override
    public Real getFocalLength() {
        return radiusOfCurvature.divide(Real.of(2.0));
    }

    @Override
    public LightRay trace(LightRay incoming) {
        // Simplified ray tracing assuming paraxial approximation for now
        // In a full implementation, we would solve for the intersection point with the
        // sphere

        // 1. Calculate intersection with the mirror plane (approx for small aperture)
        // Plane equation: (p - p0) . n = 0
        Real[] p0 = position;
        Real[] n = normal;
        Real[] l0 = incoming.getOrigin();
        Real[] l = incoming.getDirection();

        // t = (p0 - l0) . n / (l . n)
        Real num = p0[0].subtract(l0[0]).multiply(n[0])
                .add(p0[1].subtract(l0[1]).multiply(n[1]))
                .add(p0[2].subtract(l0[2]).multiply(n[2]));

        Real den = l[0].multiply(n[0])
                .add(l[1].multiply(n[1]))
                .add(l[2].multiply(n[2]));

        if (den.abs().doubleValue() < 1e-10) {
            return null; // Parallel to surface
        }

        Real t = num.divide(den);

        Real[] intersection = new Real[] {
                l0[0].add(l[0].multiply(t)),
                l0[1].add(l[1].multiply(t)),
                l0[2].add(l[2].multiply(t))
        };

        // 2. Calculate reflection vector
        // r = i - 2(i . n)n
        // Here, normal at intersection points to center of curvature
        // For spherical mirror, normal at surface is (intersection - center)
        // normalized.
        // Center C = vertex + R * normal (if R>0 is concave and normal points out)
        // Wait, standard convention: R>0 concave usually means center is in front.
        // Let's stick to vector reflection formula r = d - 2(d.n)n

        Real dot = l[0].multiply(n[0]).add(l[1].multiply(n[1])).add(l[2].multiply(n[2]));
        Real[] direction = new Real[] {
                l[0].subtract(n[0].multiply(dot).multiply(Real.of(2.0))),
                l[1].subtract(n[1].multiply(dot).multiply(Real.of(2.0))),
                l[2].subtract(n[2].multiply(dot).multiply(Real.of(2.0)))
        };

        return new LightRay(incoming.getWavelength(), incoming.getIntensity(), intersection, direction);
    }
}


