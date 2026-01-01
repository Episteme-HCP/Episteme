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

package org.jscience.physics.astronomy;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Temperature;
import org.jscience.measure.quantity.Pressure;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents a Planet.
 * Modernized to JScience.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Planet extends CelestialBody {

    private boolean habitable;

    private Quantity<Temperature> surfaceTemperature;
    private Quantity<Pressure> surfacePressure;
    private Map<String, Double> atmosphereComposition = new HashMap<>();

    public Planet(String name, Quantity<Mass> mass, Quantity<Length> radius, Vector<Real> position,
            Vector<Real> velocity) {
        super(name, mass, radius, position, velocity);
    }

    public boolean isHabitable() {
        return habitable;
    }

    public void setHabitable(boolean habitable) {
        this.habitable = habitable;
    }

    public Quantity<Temperature> getSurfaceTemperature() {
        return surfaceTemperature;
    }

    public void setSurfaceTemperature(Quantity<Temperature> surfaceTemperature) {
        this.surfaceTemperature = surfaceTemperature;
    }

    public Quantity<Pressure> getSurfacePressure() {
        return surfacePressure;
    }

    public void setSurfacePressure(Quantity<Pressure> surfacePressure) {
        this.surfacePressure = surfacePressure;
    }

    public Map<String, Double> getAtmosphereComposition() {
        return atmosphereComposition;
    }

    public void setAtmosphereComposition(Map<String, Double> atmosphereComposition) {
        this.atmosphereComposition = atmosphereComposition;
    }

    @Override
    public String toString() {
        return "Planet " + super.toString();
    }
}


