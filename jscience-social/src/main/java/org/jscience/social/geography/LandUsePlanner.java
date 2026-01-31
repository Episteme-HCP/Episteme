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
import org.jscience.core.measure.quantity.Area;

import java.util.List;

/**
 * Utility for optimizing land use and urban planning distribution.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class LandUsePlanner {

    private LandUsePlanner() {}

    public record LandZone(String zoneType, Quantity<Area> area, double utilityScore) {}

    /**
     * Suggests a default allocation for a new urban development.
     * 
     * @param totalArea the total available territory
     * @return list of recommended zones
     */
    public static List<LandZone> suggestDefaultAllocation(Quantity<Area> totalArea) {
        double areaVal = totalArea.to(Units.SQUARE_METER).getValue().doubleValue();
        
        return List.of(
            new LandZone("RESIDENTIAL", org.jscience.core.measure.Quantities.create(areaVal * 0.6, Units.SQUARE_METER), 0.7),
            new LandZone("COMMERCIAL", org.jscience.core.measure.Quantities.create(areaVal * 0.2, Units.SQUARE_METER), 0.9),
            new LandZone("INDUSTRIAL", org.jscience.core.measure.Quantities.create(areaVal * 0.1, Units.SQUARE_METER), 0.5),
            new LandZone("RECREATIONAL", org.jscience.core.measure.Quantities.create(areaVal * 0.1, Units.SQUARE_METER), 1.0)
        );
    }
}

