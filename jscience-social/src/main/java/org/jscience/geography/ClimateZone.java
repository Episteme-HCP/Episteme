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

package org.jscience.geography;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a simplified Köppen climate classification.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ClimateZone {

    public enum Type {
        TROPICAL, ARID, TEMPERATE, CONTINENTAL, POLAR
    }

    private final Type type;
    private final Real averageTempCelsius;
    private final Real annualRainfallMm;

    public ClimateZone(Type type, Real avgTemp, Real rainfall) {
        this.type = type;
        this.averageTempCelsius = avgTemp;
        this.annualRainfallMm = rainfall;
    }

    public ClimateZone(Type type, double avgTemp, double rainfall) {
        this(type, Real.of(avgTemp), Real.of(rainfall));
    }

    public Type getType() {
        return type;
    }

    public Real getAverageTemp() {
        return averageTempCelsius;
    }

    public Real getAnnualRainfall() {
        return annualRainfallMm;
    }

    public boolean isHabitable() {
        if (type == Type.POLAR)
            return false;
        if (type == Type.ARID && annualRainfallMm.doubleValue() < 50)
            return false;
        return true;
    }

    public boolean supportsAgriculture() {
        return annualRainfallMm.doubleValue() > 300 && averageTempCelsius.doubleValue() > 10;
    }
}
