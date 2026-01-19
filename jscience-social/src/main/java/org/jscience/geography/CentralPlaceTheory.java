package org.jscience.geography;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Christaller's Central Place Theory for urban hierarchy.
 */
public final class CentralPlaceTheory {

    private CentralPlaceTheory() {}

    public enum SettlementType {
        HAMLET,
        VILLAGE,
        TOWN,
        CITY,
        REGIONAL_CAPITAL;
    }

    /**
     * Estimates number of settlements of a given rank below a higher rank center.
     * N = k^(n-1) where n is rank difference.
     */
    public static int settlementCount(int k, int higherRank, int lowerRank) {
        if (higherRank <= lowerRank) return 1;
        return (int) Math.pow(k, higherRank - lowerRank);
    }

    /**
     * Calculates the "Range" of a good.
     * R = sqrt(A / pi) where A is threshold area.
     */
    public static Real calculateRange(double thresholdPopulation, double populationDensity) {
        double area = thresholdPopulation / populationDensity;
        return Real.of(Math.sqrt(area / Math.PI));
    }

    /**
     * Predicts hierarchical distribution of settlements in a region.
     */
    public static Map<SettlementType, Integer> predictHierarchy(int totalHighRankSettlements, int k) {
        Map<SettlementType, Integer> hierarchy = new EnumMap<>(SettlementType.class);
        SettlementType[] types = SettlementType.values();
        
        for (int i = types.length - 1; i >= 0; i--) {
            int count = totalHighRankSettlements * settlementCount(k, types.length, i + 1);
            hierarchy.put(types[i], count);
        }
        
        return hierarchy;
    }
}
