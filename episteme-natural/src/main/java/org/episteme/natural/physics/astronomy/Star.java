/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.physics.astronomy;

import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;

import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.quantity.Mass;
import org.episteme.core.measure.quantity.Length;
import org.episteme.core.measure.quantity.Temperature;
import org.episteme.core.measure.quantity.Power;

/**
 * Represents a Star.
 * Modernized to Episteme.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Star extends CelestialBody {

    private String catalogId;
    private String spectralType;
    private Quantity<Power> luminosity;
    private Quantity<Temperature> temperature;
    private double distanceLightYears;

    public Star(String name, String catalogId, Quantity<Mass> mass, Quantity<Length> radius, Vector<Real> position,
            Vector<Real> velocity) {
        super(name, mass, radius, position, velocity);
        this.catalogId = catalogId;
    }

    // Constructor without ID for backward compatibility or when ID is unknown
    public Star(String name, Quantity<Mass> mass, Quantity<Length> radius, Vector<Real> position,
            Vector<Real> velocity) {
        this(name, null, mass, radius, position, velocity);
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public double getDistanceLightYears() {
        return distanceLightYears;
    }

    public void setDistanceLightYears(double distanceLightYears) {
        this.distanceLightYears = distanceLightYears;
    }


    public String getSpectralType() {
        return spectralType;
    }

    public void setSpectralType(String spectralType) {
        this.spectralType = spectralType;
    }

    public Quantity<Power> getLuminosity() {
        return luminosity;
    }

    public void setLuminosity(Quantity<Power> luminosity) {
        this.luminosity = luminosity;
    }

    public Quantity<Temperature> getTemperature() {
        return temperature;
    }

    public void setTemperature(Quantity<Temperature> temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Star " + super.toString();
    }
}



