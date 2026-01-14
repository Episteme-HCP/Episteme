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

package org.jscience.chemistry;

/**
 * The ChemicalConditions class is the class that defines a chemical environment at a time.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

public class ChemicalConditions extends Object {

    public final static double DEFAULT_TEMPERATURE = ChemistryConstants.ZERO_CELCIUS + 25;
    public final static double DEFAULT_VOLUME = ChemistryConstants.MOLAR_VOLUME_25;
    public final static double DEFAULT_PRESSURE = ChemistryConstants.ATMOSPHERE;
    public final static double DEFAULT_POTENTIAL = ChemistryConstants
    .1e-7;//probably only valid for water

    private double temperature;
    private double volume;
    private double pressure;
    private double potential;//the ionisation potential of this solution, pH for water

    public ChemicalConditions(double t, double v, double p, double c) {

        this.temperature = t;
        this.volume = v;
        this.pressure = p;
        this.potential = c;

    }

    public double getTemperature() {

        return temperature;
    }

    public void setTemperature(double t) {

        temperature = t;

    }

    public double getVolume() {

        return volume;
    }

    public void setVolume(double v) {

        volume = v;

    }

    public double getPressure() {

        return pressure;
    }

    public void setPressure(double p) {

        pressure = p;

    }

    public double getPotential() {

        return potential;
    }

    public void setPotential(double c) {

        this.potential = c;

    }

    public static ChemicalConditions getDefaultChemicalConditions() {

        return new ChemicalConditions(DEFAULT_TEMPERATURE, DEFAULT_VOLUME, DEFAULT_PRESSURE, DEFAULT_POTENTIAL);

    }

}
