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

package org.jscience.geography;

import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Temperature;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

import java.io.Serializable;

/**
 * Represents a simplified Köppen climate classification and associated metrics.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public record ClimateZone(
        @Attribute Type type,
        @Attribute Quantity<Temperature> averageTemp,
        @Attribute Quantity<Length> annualRainfall) implements Serializable {

    public enum Type {
        TROPICAL, ARID, TEMPERATE, CONTINENTAL, POLAR
    }

    /**
     * Checks if the climate supports permanent human settlement.
     */
    public boolean isHabitable() {
        if (type == Type.POLAR) return false;
        if (type == Type.ARID && annualRainfall.to(Units.MILLIMETER).getValue() < 100) return false;
        return true;
    }

    /**
     * Checks if the climate supports outdoor agriculture.
     */
    public boolean supportsAgriculture() {
        return annualRainfall.to(Units.MILLIMETER).getValue() > 400 && 
               averageTemp.to(Units.CELSIUS).getValue() > 12;
    }

    @Override
    public String toString() {
        return String.format("%s Climate (Avg. Temp: %s, Rainfall: %s)", type, averageTemp, annualRainfall);
    }
}
