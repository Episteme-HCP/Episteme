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

package org.jscience.earth.geophysics;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PlateVector {

    private final String siteId;
    private final org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> latitude;
    private final org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> longitude;

    // Velocity components
    private final org.jscience.measure.Quantity<org.jscience.measure.quantity.Velocity> northVelocity; // mm/yr usually
    private final org.jscience.measure.Quantity<org.jscience.measure.quantity.Velocity> eastVelocity; // mm/yr usually
    // Up component usually ignored for 2D plate motion but could be added

    public PlateVector(String siteId,
            org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> latitude,
            org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> longitude,
            org.jscience.measure.Quantity<org.jscience.measure.quantity.Velocity> northVelocity,
            org.jscience.measure.Quantity<org.jscience.measure.quantity.Velocity> eastVelocity) {
        this.siteId = siteId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.northVelocity = northVelocity;
        this.eastVelocity = eastVelocity;
    }

    public String getSiteId() {
        return siteId;
    }

    public org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> getLatitude() {
        return latitude;
    }

    public org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> getLongitude() {
        return longitude;
    }

    public org.jscience.measure.Quantity<org.jscience.measure.quantity.Velocity> getNorthVelocity() {
        return northVelocity;
    }

    public org.jscience.measure.Quantity<org.jscience.measure.quantity.Velocity> getEastVelocity() {
        return eastVelocity;
    }

    @Override
    public String toString() {
        return String.format("Site: %s [%s, %s] Vel: N=%s E=%s", siteId, latitude, longitude, northVelocity,
                eastVelocity);
    }
}
