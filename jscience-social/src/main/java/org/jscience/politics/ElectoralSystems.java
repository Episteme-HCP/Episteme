package org.jscience.politics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Calculators for various electoral systems and seat allocation methods.
 */
public final class ElectoralSystems {

    private ElectoralSystems() {}

    /**
     * Allocates seats using the D'Hondt method (highest averages).
     * 
     * @param votes Map of party names to their total votes.
     * @param totalSeats Total seats to allocate.
     * @return Map of party names to allocated seats.
     */
    public static Map<String, Integer> allocateSeatsDHondt(Map<String, Long> votes, int totalSeats) {
        Map<String, Integer> seats = new HashMap<>();
        votes.keySet().forEach(p -> seats.put(p, 0));

        for (int i = 0; i < totalSeats; i++) {
            String bestParty = null;
            double maxRatio = -1.0;

            for (Map.Entry<String, Long> entry : votes.entrySet()) {
                String party = entry.getKey();
                double ratio = entry.getValue() / (double) (seats.get(party) + 1);
                if (ratio > maxRatio) {
                    maxRatio = ratio;
                    bestParty = party;
                }
            }
            if (bestParty != null) {
                seats.put(bestParty, seats.get(bestParty) + 1);
            }
        }
        return seats;
    }

    /**
     * Allocates seats using the Sainte-Laguë method.
     * Slightly more favorable to smaller parties than D'Hondt.
     */
    public static Map<String, Integer> allocateSeatsSainteLague(Map<String, Long> votes, int totalSeats) {
        Map<String, Integer> seats = new HashMap<>();
        votes.keySet().forEach(p -> seats.put(p, 0));

        for (int i = 0; i < totalSeats; i++) {
            String bestParty = null;
            double maxRatio = -1.0;

            for (Map.Entry<String, Long> entry : votes.entrySet()) {
                String party = entry.getKey();
                double ratio = entry.getValue() / (double) (2 * seats.get(party) + 1);
                if (ratio > maxRatio) {
                    maxRatio = ratio;
                    bestParty = party;
                }
            }
            if (bestParty != null) {
                seats.put(bestParty, seats.get(bestParty) + 1);
            }
        }
        return seats;
    }

    /**
     * Implements a simple "Swing" model for predicting election outcomes.
     * 
     * @param previousResults Map of parties to previous vote percentage.
     * @param swing Predicted swing (positive or negative) in percentage points.
     * @return Map of predicted vote percentages.
     */
    public static Map<String, Real> predictSwing(Map<String, Real> previousResults, Map<String, Real> swing) {
        Map<String, Real> predicted = new HashMap<>();
        for (String party : previousResults.keySet()) {
            Real prev = previousResults.get(party);
            Real s = swing.getOrDefault(party, Real.of(0.0));
            predicted.put(party, prev.add(s));
        }
        return predicted;
    }
}
