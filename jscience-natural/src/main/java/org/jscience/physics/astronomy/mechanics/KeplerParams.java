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

package org.jscience.physics.astronomy.mechanics;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Angle;
import org.jscience.measure.quantity.Length;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class KeplerParams {

    private final Quantity<Length> semiMajorAxis; // a
    private final double eccentricity; // e
    private final Quantity<Angle> inclination; // i
    private final Quantity<Angle> longitudeAscendingNode; // Omega
    private final Quantity<Angle> argumentPeriapsis; // omega
    // trueAnomaly or meanAnomaly depends on state, usually kept separate

    public KeplerParams(Quantity<Length> semiMajorAxis, double eccentricity,
            Quantity<Angle> inclination, Quantity<Angle> longitudeAscendingNode,
            Quantity<Angle> argumentPeriapsis) {
        this.semiMajorAxis = semiMajorAxis;
        this.eccentricity = eccentricity;
        this.inclination = inclination;
        this.longitudeAscendingNode = longitudeAscendingNode;
        this.argumentPeriapsis = argumentPeriapsis;
    }

    public Quantity<Length> getSemiMajorAxis() {
        return semiMajorAxis;
    }

    public double getEccentricity() {
        return eccentricity;
    }

    public Quantity<Angle> getInclination() {
        return inclination;
    }

    public Quantity<Angle> getLongitudeAscendingNode() {
        return longitudeAscendingNode;
    }

    public Quantity<Angle> getArgumentPeriapsis() {
        return argumentPeriapsis;
    }

    @Override
    public String toString() {
        return "KeplerParams{" +
                "a=" + semiMajorAxis +
                ", e=" + eccentricity +
                ", i=" + inclination +
                '}';
    }
}
