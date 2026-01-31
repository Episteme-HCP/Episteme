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

package org.jscience.social.geography;

import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Length;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Utility for modeling spatial interaction, migration flows, and urban gravitation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class SpatialInteraction {

    private SpatialInteraction() {}

    /**
     * Gravity Model for interactions between two locations.
     * I = (P1 * P2) / dist^beta
     * 
     * @param p1 population of start location
     * @param p2 population of end location
     * @param distance distance between them
     * @param beta friction coefficient (distance decay)
     * @return interaction intensity
     */
    public static Real calculateGravityIntensity(double p1, double p2, Quantity<Length> distance, double beta) {
        double d = distance.to(Units.KILOMETER).getValue().doubleValue();
        if (d <= 0) return Real.ZERO;
        return Real.of((p1 * p2) / Math.pow(d, beta));
    }

    /**
     * Reilly's Law of Retail Gravitation: breaking point between two market areas.
     * 
     * @param distance distance between centroids
     * @param p1 population of center 1
     * @param p2 population of center 2
     * @return breaking point distance from center 1
     */
    public static Quantity<Length> calculateBreakingPoint(Quantity<Length> distance, double p1, double p2) {
        double d = distance.to(Units.METER).getValue().doubleValue();
        double bp = d / (1 + Math.sqrt(p2 / p1));
        return org.jscience.core.measure.Quantities.create(bp, Units.METER);
    }
}

