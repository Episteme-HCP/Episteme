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

package org.episteme.social.politics;

import java.util.HashMap;
import java.util.Map;
import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Provides algorithms for various electoral systems and seat allocation methods.
 * Includes implementations of D'Hondt and Sainte-LaguÃ« proportional representation methods.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class ElectoralSystems {

    private ElectoralSystems() {}

    /**
     * Allocates seats using the D'Hondt method (highest averages).
     * <p>
     * Favors larger parties slightly more than other PR methods.
     * Divisors: 1, 2, 3, 4...
     * </p>
     * 
     * @param votes      Map of party names to their total votes
     * @param totalSeats Total seats available for allocation
     * @return Map of party names to allocated seats
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
     * Allocates seats using the Sainte-LaguÃ« method (Webster method).
     * <p>
     * Generally provides a more proportional outcome than D'Hondt, better for smaller parties.
     * Divisors: 1, 3, 5, 7... (2s + 1)
     * </p>
     * 
     * @param votes      Map of party names to their total votes
     * @param totalSeats Total seats available for allocation
     * @return Map of party names to allocated seats
     */
    public static Map<String, Integer> allocateSeatsSainteLague(Map<String, Long> votes, int totalSeats) {
        Map<String, Integer> seats = new HashMap<>();
        votes.keySet().forEach(p -> seats.put(p, 0));

        for (int i = 0; i < totalSeats; i++) {
            String bestParty = null;
            double maxRatio = -1.0;

            for (Map.Entry<String, Long> entry : votes.entrySet()) {
                String party = entry.getKey();
                // Divisor is 2s + 1
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
     * Implements a simple "Uniform Swing" model for predicting election outcomes.
     * Applies a uniform percentage change to previous results.
     * 
     * @param previousResults Map of parties to previous vote percentage (e.g., 0.45 for 45%)
     * @param swing           Predicted swing in percentage points (e.g., +0.02 for +2%)
     * @return Map of predicted vote percentages
     */
    public static Map<String, Real> predictSwing(Map<String, Real> previousResults, Map<String, Real> swing) {
        Map<String, Real> predicted = new HashMap<>();
        for (String party : previousResults.keySet()) {
            Real prev = previousResults.get(party);
            Real s = swing.getOrDefault(party, Real.ZERO);
            // Ensure percentage doesn't go below 0 or (implicitly) above 1, though simplified here
            Real outcome = prev.add(s);
            if (outcome.doubleValue() < 0) outcome = Real.ZERO;
            predicted.put(party, outcome);
        }
        return predicted;
    }
}

