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

package org.jscience.social.sports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Simulates sports drafts and recruitment processes.
 * Includes serpentine draft logic and lottery-weighted selection order.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class DraftSimulator {

    private DraftSimulator() {}

    /** Data model for a draft prospect. */
    public record Prospect(
        String name,
        String position,
        double overallRating,
        double potential,
        int age,
        Map<String, Double> attributes
    ) implements Serializable {}

    /** Details of a specific draft pick. */
    public record DraftPick(
        int round, 
        int pickNumber, 
        String team, 
        Prospect prospect
    ) implements Serializable {}

    /**
     * Simulates a serpentine draft over a specified number of rounds.
     * 
     * @param teamOrder initial team selection order
     * @param prospects available players
     * @param rounds    total rounds to simulate
     * @return the sequence of draft picks
     */
    public static List<DraftPick> simulateSerpentineDraft(
            List<String> teamOrder, List<Prospect> prospects, int rounds) {
        
        List<DraftPick> results = new ArrayList<>();
        List<Prospect> available = new ArrayList<>(prospects);
        available.sort((a, b) -> Double.compare(b.overallRating(), a.overallRating()));
        
        for (int round = 1; round <= rounds; round++) {
            List<String> roundOrder = new ArrayList<>(teamOrder);
            if (round % 2 == 0) {
                Collections.reverse(roundOrder);
            }
            
            int pickInRound = 1;
            for (String team : roundOrder) {
                if (available.isEmpty()) break;
                
                int index = selectProspect(available, team, round);
                Prospect pick = available.remove(index);
                
                int overallPick = (round - 1) * teamOrder.size() + pickInRound;
                results.add(new DraftPick(round, overallPick, team, pick));
                pickInRound++;
            }
        }
        return results;
    }

    /**
     * Simulates a weighted lottery to determine draft order.
     * 
     * @param teamStandings mapping of teams to their standings (1 = best)
     * @param lotteryPicks  number of picks determined by lottery
     * @return the resulting selection order
     */
    public static List<String> simulateLotteryOrder(Map<String, Integer> teamStandings, 
            int lotteryPicks) {
        
        List<String> teams = new ArrayList<>(teamStandings.keySet());
        int totalTeams = teams.size();
        
        Map<String, Integer> weights = new HashMap<>();
        for (String team : teams) {
            int standing = teamStandings.get(team);
            weights.put(team, totalTeams - standing + 1);
        }
        
        List<String> lotteryResults = new ArrayList<>();
        Random random = new Random();
        List<String> remaining = new ArrayList<>(teams);
        
        for (int i = 0; i < lotteryPicks && !remaining.isEmpty(); i++) {
            int totalWeight = remaining.stream().mapToInt(weights::get).sum();
            int pick = random.nextInt(totalWeight);
            
            int cumulative = 0;
            for (String team : remaining) {
                cumulative += weights.get(team);
                if (pick < cumulative) {
                    lotteryResults.add(team);
                    remaining.remove(team);
                    break;
                }
            }
        }
        
        remaining.sort((a, b) -> teamStandings.get(b) - teamStandings.get(a));
        lotteryResults.addAll(remaining);
        
        return lotteryResults;
    }

    /** Calculates the estimated trade value of a specific pick. */
    public static Real calculatePickValue(int pickNumber, int totalPicks) {
        double value = 1000.0 * Math.exp(-0.05 * (pickNumber - 1));
        return Real.of(value);
    }

    /** Evaluates if a trade proposal between two teams is fair. */
    public static boolean isFairTrade(List<Integer> teamAPicks, List<Integer> teamBPicks, 
            int totalPicks, double maxImbalance) {
        
        double valueA = teamAPicks.stream()
            .mapToDouble(p -> calculatePickValue(p, totalPicks).doubleValue())
            .sum();
        double valueB = teamBPicks.stream()
            .mapToDouble(p -> calculatePickValue(p, totalPicks).doubleValue())
            .sum();
        
        double imbalance = Math.abs(valueA - valueB) / Math.max(valueA, valueB);
        return imbalance <= maxImbalance;
    }

    private static int selectProspect(List<Prospect> available, String team, int round) {
        Random random = new Random();
        int reachRange = Math.min(3, available.size());
        return random.nextInt(reachRange);
    }
}

