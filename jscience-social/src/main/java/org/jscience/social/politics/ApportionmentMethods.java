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

package org.jscience.social.politics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Provides mathematical methods for electoral seat apportionment.
 * Includes implementations of D'Hondt, Sainte-LaguÃ«, Hamilton, and Huntington-Hill methods.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class ApportionmentMethods {

    private ApportionmentMethods() {}

    /** Simple party representation for apportionment. */
    public record Party(String name, long votes) implements Serializable {}
    
    /** Aggregated results of an apportionment calculation. */
    public record ApportionmentResult(
        Map<String, Integer> seats,
        String method,
        double gallagherIndex
    ) implements Serializable {}

    /**
     * D'Hondt method - tends to favor larger parties.
     * 
     * @param parties    list of parties and their vote counts
     * @param totalSeats number of seats to allocate
     * @return the apportionment result
     */
    public static ApportionmentResult dHondt(List<Party> parties, int totalSeats) {
        Map<String, Integer> seats = new HashMap<>();
        parties.forEach(p -> seats.put(p.name(), 0));
        
        for (int i = 0; i < totalSeats; i++) {
            String winner = null;
            double maxQuotient = -1;
            
            for (Party party : parties) {
                double quotient = (double) party.votes() / (seats.get(party.name()) + 1);
                if (quotient > maxQuotient) {
                    maxQuotient = quotient;
                    winner = party.name();
                }
            }
            
            if (winner != null) {
                seats.merge(winner, 1, (a, b) -> a + b);
            }
        }
        
        double gallagher = calculateGallagher(parties, seats, totalSeats);
        return new ApportionmentResult(seats, "D'Hondt", gallagher);
    }

    /**
     * Sainte-LaguÃ« method - generally considered more proportional than D'Hondt.
     * 
     * @param parties    list of parties and their vote counts
     * @param totalSeats number of seats to allocate
     * @return the apportionment result
     */
    public static ApportionmentResult sainteLague(List<Party> parties, int totalSeats) {
        Map<String, Integer> seats = new HashMap<>();
        parties.forEach(p -> seats.put(p.name(), 0));
        
        for (int i = 0; i < totalSeats; i++) {
            String winner = null;
            double maxQuotient = -1;
            
            for (Party party : parties) {
                double divisor = 2.0 * seats.get(party.name()) + 1.0;
                double quotient = (double) party.votes() / divisor;
                if (quotient > maxQuotient) {
                    maxQuotient = quotient;
                    winner = party.name();
                }
            }
            
            if (winner != null) {
                seats.merge(winner, 1, (a, b) -> a + b);
            }
        }
        
        double gallagher = calculateGallagher(parties, seats, totalSeats);
        return new ApportionmentResult(seats, "Sainte-LaguÃ«", gallagher);
    }

    /**
     * Hamilton (Largest Remainder) method with Hare quota.
     * 
     * @param parties    list of parties and their vote counts
     * @param totalSeats number of seats to allocate
     * @return the apportionment result
     */
    public static ApportionmentResult hamilton(List<Party> parties, int totalSeats) {
        Map<String, Integer> seats = new HashMap<>();
        long totalVotes = parties.stream().mapToLong(Party::votes).sum();
        if (totalVotes == 0) return new ApportionmentResult(seats, "Hamilton", 0);
        
        double quota = (double) totalVotes / totalSeats;
        Map<String, Double> remainders = new HashMap<>();
        int seatsAssigned = 0;
        
        for (Party party : parties) {
            double exactSeats = party.votes() / quota;
            int floorSeats = (int) exactSeats;
            seats.put(party.name(), floorSeats);
            remainders.put(party.name(), exactSeats - floorSeats);
            seatsAssigned += floorSeats;
        }
        
        List<Party> sortedByRemainder = new ArrayList<>(parties);
        sortedByRemainder.sort((a, b) -> 
            Double.compare(remainders.get(b.name()), remainders.get(a.name())));
        
        for (int i = 0; i < totalSeats - seatsAssigned && i < sortedByRemainder.size(); i++) {
            seats.merge(sortedByRemainder.get(i).name(), 1, (a, b) -> a + b);
        }
        
        double gallagher = calculateGallagher(parties, seats, totalSeats);
        return new ApportionmentResult(seats, "Hamilton", gallagher);
    }

    /**
     * Huntington-Hill method, used for the US House of Representatives.
     * 
     * @param parties          list of parties and their vote counts
     * @param totalSeats       number of seats to allocate
     * @param minimumPerParty  minimum seats guaranteed per party
     * @return the apportionment result
     */
    public static ApportionmentResult huntingtonHill(List<Party> parties, int totalSeats,
            int minimumPerParty) {
        
        Map<String, Integer> seats = new HashMap<>();
        int remaining = totalSeats;
        for (Party party : parties) {
            seats.put(party.name(), minimumPerParty);
            remaining -= minimumPerParty;
        }
        
        for (int i = 0; i < remaining; i++) {
            String winner = null;
            double maxPriority = -1;
            
            for (Party party : parties) {
                int n = seats.get(party.name());
                double divisor = Math.sqrt((double) n * (n + 1));
                double priority = party.votes() / divisor;
                
                if (priority > maxPriority) {
                    maxPriority = priority;
                    winner = party.name();
                }
            }
            
            if (winner != null) {
                seats.merge(winner, 1, (a, b) -> a + b);
            }
        }
        
        double gallagher = calculateGallagher(parties, seats, totalSeats);
        return new ApportionmentResult(seats, "Huntington-Hill", gallagher);
    }

    /**
     * Compares all implemented methods for a given set of data.
     * 
     * @param parties    list of parties and their vote counts
     * @param totalSeats number of seats to allocate
     * @return map of method names to results
     */
    public static Map<String, ApportionmentResult> compareAllMethods(List<Party> parties, 
            int totalSeats) {
        
        Map<String, ApportionmentResult> results = new LinkedHashMap<>();
        results.put("D'Hondt", dHondt(parties, totalSeats));
        results.put("Sainte-LaguÃ«", sainteLague(parties, totalSeats));
        results.put("Hamilton", hamilton(parties, totalSeats));
        results.put("Huntington-Hill", huntingtonHill(parties, totalSeats, 0));
        
        return results;
    }

    /**
     * Calculates the Gallagher index of disproportionality.
     * Lower values indicate more proportional representation.
     * 
     * @param parties    parties involved
     * @param seats      allocated seats
     * @param totalSeats total pool of seats
     * @return the calculated index
     */
    public static double calculateGallagher(List<Party> parties, Map<String, Integer> seats,
            int totalSeats) {
        
        long totalVotes = parties.stream().mapToLong(Party::votes).sum();
        if (totalVotes == 0 || totalSeats == 0) return 0;
        
        double sumSquares = 0;
        for (Party party : parties) {
            double voteShare = (double) party.votes() / totalVotes * 100.0;
            double seatShare = (double) seats.getOrDefault(party.name(), 0) / totalSeats * 100.0;
            sumSquares += Math.pow(voteShare - seatShare, 2);
        }
        return Math.sqrt(sumSquares / 2.0);
    }

    /**
     * Calculates the effective electoral threshold.
     * 
     * @param totalSeats total seats in the district/election
     * @return the threshold percentage as a Real number
     */
    public static Real effectiveThreshold(int totalSeats) {
        return Real.of(50.0 / (totalSeats + 1));
    }
}

