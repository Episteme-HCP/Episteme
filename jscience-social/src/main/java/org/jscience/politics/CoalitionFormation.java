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

package org.jscience.politics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Models political coalition formation using game theory principles.
 * Identifies winning coalitions and potential power structures between parties.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class CoalitionFormation {

    private CoalitionFormation() {}

    /**
     * Identifies all winning coalitions where the sum of seats exceeds a given threshold.
     * A winning coalition is a set of parties that can collectively form a majority.
     * 
     * @param parties   Map of party names to their respective seat counts
     * @param threshold the minimum seats required for a majority (e.g., total/2 + 1)
     * @return a list of winning coalitions (each coalition is a Set of party names)
     */
    public static List<Set<String>> findWinningCoalitions(Map<String, Integer> parties, int threshold) {
        List<Set<String>> winners = new ArrayList<>();
        if (parties == null || parties.isEmpty()) return winners;

        List<String> partyNames = new ArrayList<>(parties.keySet());
        int n = partyNames.size();
        
        // Power set calculation (limit to 20 parties to avoid exponential blowup)
        if (n > 20) return winners;

        for (long i = 1; i < (1L << n); i++) {
            Set<String> coalition = new HashSet<>();
            int totalSeats = 0;
            for (int j = 0; j < n; j++) {
                if ((i & (1L << j)) != 0) {
                    String partyName = partyNames.get(j);
                    coalition.add(partyName);
                    totalSeats += parties.get(partyName);
                }
            }
            if (totalSeats >= threshold) {
                winners.add(coalition);
            }
        }
        return winners;
    }
}
