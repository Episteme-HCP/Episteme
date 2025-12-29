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

package org.jscience.politics;

import java.util.*;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VotingSystem {

    public enum Method {
        FIRST_PAST_THE_POST, // Winner takes all
        PROPORTIONAL // Seats distributed by %
    }

    /**
     * Determines winner(s) based on vote counts.
     * 
     * @param votes          Map of Candidate/Party Name -> Vote Count
     * @param method         The voting method to use
     * @param seatsAvailable Number of seats to fill (relevant for proportional)
     * @return List of winners
     */
    public static List<String> determineWinners(Map<String, Long> votes, Method method, int seatsAvailable) {
        List<String> winners = new ArrayList<>();

        if (votes.isEmpty())
            return winners;

        switch (method) {
            case FIRST_PAST_THE_POST:
                // Find single candidate with max votes
                String winner = Collections.max(votes.entrySet(), Map.Entry.comparingByValue()).getKey();
                winners.add(winner);
                break;

            case PROPORTIONAL:
                // Simple largest remainder method or straight percentage for this demo
                long totalVotes = votes.values().stream().mapToLong(Long::longValue).sum();
                if (totalVotes == 0)
                    break;

                // Assign seats based on percentage
                for (Map.Entry<String, Long> entry : votes.entrySet()) {
                    double share = (double) entry.getValue() / totalVotes;
                    int seats = (int) Math.round(share * seatsAvailable);
                    if (seats > 0) {
                        winners.add(entry.getKey() + " (" + seats + " seats)");
                    }
                }
                break;
        }
        return winners;
    }
}
