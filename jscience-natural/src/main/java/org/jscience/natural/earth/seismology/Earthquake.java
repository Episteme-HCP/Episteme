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
import org.jscience.core.measure.quantity.Angle;
import org.jscience.core.measure.quantity.Length;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

/**
 * Represents a seismic event.
 * Standard domain model for geology applications.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Earthquake {


    @Attribute
    private final Quantity<Angle> latitude;
    @Attribute
    private final Quantity<Angle> longitude;
    @Attribute
    private final Real magnitude;
    @Attribute
    private final Quantity<Length> depth;

    public Earthquake(Quantity<Angle> latitude, Quantity<Angle> longitude, Real magnitude, Quantity<Length> depth) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.magnitude = magnitude;
        this.depth = depth;
    }

    public Earthquake(double latitude, double longitude, double magnitude, double depthKm) {
        this.latitude = org.jscience.core.measure.Quantities.create(latitude, org.jscience.core.measure.Units.DEGREE_ANGLE);
        this.longitude = org.jscience.core.measure.Quantities.create(longitude, org.jscience.core.measure.Units.DEGREE_ANGLE);
        this.magnitude = Real.of(magnitude);
        this.depth = org.jscience.core.measure.Quantities.create(depthKm, org.jscience.core.measure.Units.KILOMETER);
    }

    public Quantity<Angle> getLatitude() {
        return latitude;
    }

    public Quantity<Angle> getLongitude() {
        return longitude;
    }

    public Real getMagnitude() {
        return magnitude;
    }

    public Quantity<Length> getDepth() {
        return depth;
    }

    public double getLat() {
        return latitude.to(org.jscience.core.measure.Units.DEGREE_ANGLE).getValue().doubleValue();
    }

    public double getLon() {
        return longitude.to(org.jscience.core.measure.Units.DEGREE_ANGLE).getValue().doubleValue();
    }

    public double getMag() {
        return magnitude.doubleValue();
    }

    public double getDepthKm() {
        return depth.to(org.jscience.core.measure.Units.KILOMETER).getValue().doubleValue();
    }

    @Override
    public String toString() {
        return String.format("Earthquake[Lat=%s, Lon=%s, Mag=%s, Depth=%s]", 
            latitude, longitude, magnitude, depth);
    }
}

