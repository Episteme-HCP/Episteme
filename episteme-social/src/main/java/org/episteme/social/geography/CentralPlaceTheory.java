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

package org.episteme.social.geography;

import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Area;
import org.episteme.core.measure.quantity.Length;

import java.util.EnumMap;
import java.util.Map;

/**
 * Christaller's Central Place Theory for urban hierarchy and distribution analysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class CentralPlaceTheory {

    private CentralPlaceTheory() {}

    public enum SettlementRank {
        HAMLET, VILLAGE, TOWN, CITY, REGIONAL_CAPITAL
    }

    /**
     * Estimates number of settlements of a given rank below a higher rank center.
     * Calculated as N = k^(higher - lower).
     */
    public static int getHierarchyCount(int k, SettlementRank higher, SettlementRank lower) {
        int diff = higher.ordinal() - lower.ordinal();
        if (diff <= 0) return 1;
        return (int) Math.pow(k, diff);
    }

    /**
     * Calculates the "Range" (service radius) of a good based on threshold area.
     */
    public static Quantity<Length> calculateRange(Quantity<Area> thresholdArea) {
        double areaVal = thresholdArea.to(Units.SQUARE_METER).getValue().doubleValue();
        double radiusMeters = Math.sqrt(areaVal / Math.PI);
        return Quantities.create(radiusMeters, Units.METER);
    }

    /**
     * Predicts hierarchical distribution of settlements in a region.
     */
    public static Map<SettlementRank, Integer> predictRegionalHierarchy(int topLevelCenters, int kFactor) {
        Map<SettlementRank, Integer> hierarchy = new EnumMap<>(SettlementRank.class);
        SettlementRank[] ranks = SettlementRank.values();
        
        for (int i = 0; i < ranks.length; i++) {
            int count = topLevelCenters * getHierarchyCount(kFactor, ranks[ranks.length - 1], ranks[i]);
            hierarchy.put(ranks[i], count);
        }
        
        return hierarchy;
    }
}

