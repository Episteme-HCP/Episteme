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

package org.jscience.natural.earth.seismology;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.quantity.Length;
import org.jscience.core.measure.Units;
import java.util.Objects;

/**
 * Seismological epicenter triangulation utility.
 */
public final class EpicenterTriangulator {

    private EpicenterTriangulator() {}

    /**
     * Station location and its distance to the epicenter.
     */
    public record StationRecord(Real x, Real y, Quantity<Length> distance) {}

    /**
     * Estimates the epicenter (X, Y) coordinate given three station records.
     * Uses simplified trilateration on a 2D plane.
     */
    public static Real[] triangulate(StationRecord s1, StationRecord s2, StationRecord s3) {
        Objects.requireNonNull(s1);
        Objects.requireNonNull(s2);
        Objects.requireNonNull(s3);

        Real r1 = Real.of(s1.distance().to(Units.KILOMETER).getValue().doubleValue());
        Real r2 = Real.of(s2.distance().to(Units.KILOMETER).getValue().doubleValue());
        Real r3 = Real.of(s3.distance().to(Units.KILOMETER).getValue().doubleValue());

        // Linear system for 2D trilateration
        Real two = Real.of(2.0);
        Real A = two.multiply(s2.x()).subtract(two.multiply(s1.x()));
        Real B = two.multiply(s2.y()).subtract(two.multiply(s1.y()));
        Real C = r1.pow(2).subtract(r2.pow(2)).subtract(s1.x().pow(2)).add(s2.x().pow(2)).subtract(s1.y().pow(2)).add(s2.y().pow(2));
        
        Real D = two.multiply(s3.x()).subtract(two.multiply(s2.x()));
        Real E = two.multiply(s3.y()).subtract(two.multiply(s2.y()));
        Real F = r2.pow(2).subtract(r3.pow(2)).subtract(s2.x().pow(2)).add(s3.x().pow(2)).subtract(s2.y().pow(2)).add(s3.y().pow(2));

        Real denominator = A.multiply(E).subtract(D.multiply(B));
        Real x = C.multiply(E).subtract(F.multiply(B)).divide(denominator);
        Real y = A.multiply(F).subtract(D.multiply(C)).divide(denominator);

        return new Real[] { x, y };
    }
}

