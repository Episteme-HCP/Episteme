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

import java.util.*;
import java.util.stream.Collectors;

/**
 * An engine for calculating election winners using various social choice algorithms.
 * Acts as a bridge between the data models (Ballot, Election) and 
 * the algorithmic implementations in ElectoralSimulations.
 * Refactored to use the extensible {@link VotingMethod} registry.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.1
 * @since 1.0
 */
public final class VotingEngine {

    private VotingEngine() {}

    /**
     * Resolves an election using the specified method and list of cast ballots.
     * 
     * @param ballots    the cast ballots
     * @param electionId the ID of the choice/election to process
     * @param method     the voting algorithm
     * @param seats      number of seats to fill
     * @return list of winners
     */
    public static List<String> resolve(List<Ballot> ballots, String electionId, VotingMethod method, int seats) {
        if (ballots == null || ballots.isEmpty()) return Collections.emptyList();

        // Prepare ranked data if needed
        List<List<String>> rankedBallots = ballots.stream()
            .map(b -> b.selections() != null ? b.selections().get(electionId) : null)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        // Prepare cardinal data if needed
        List<Map<String, Integer>> ratedBallots = ballots.stream()
            .map(b -> {
                Map<String, org.jscience.core.mathematics.numbers.real.Real> r = (b.ratings() != null) ? b.ratings().get(electionId) : null;
                if (r == null) return null;
                Map<String, Integer> intMap = new HashMap<>();
                r.forEach((k, v) -> intMap.put(k, (int) Math.round(v.doubleValue())));
                return intMap;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        if (method == VotingMethod.FIRST_PAST_THE_POST) {
            return calculatePluralityWinner(ballots, electionId);
        } else if (method == VotingMethod.TWO_ROUND) {
            return List.of(ElectoralSimulations.resolveTwoRound(rankedBallots));
        } else if (method == VotingMethod.INSTANT_RUNOFF) {
            return List.of(ElectoralSimulations.resolveInstantRunoff(rankedBallots));
        } else if (method == VotingMethod.CONDORCET) {
            return List.of(ElectoralSimulations.resolveCondorcet(rankedBallots));
        } else if (method == VotingMethod.SCHULZE) {
            return List.of(ElectoralSimulations.resolveSchulze(rankedBallots));
        } else if (method == VotingMethod.COPELAND) {
            return List.of(ElectoralSimulations.resolveCopeland(rankedBallots));
        } else if (method == VotingMethod.BORDA) {
            return List.of(ElectoralSimulations.resolveBorda(rankedBallots));
        } else if (method == VotingMethod.APPROVAL) {
            return List.of(ElectoralSimulations.resolveApproval(rankedBallots));
        } else if (method == VotingMethod.RANGE) {
            return List.of(ElectoralSimulations.resolveRange(ratedBallots));
        } else if (method == VotingMethod.STAR) {
            return List.of(ElectoralSimulations.resolveSTAR(ratedBallots));
        } else if (method == VotingMethod.STV) {
            return mapToWinnerList(ElectoralSimulations.resolveSTV(rankedBallots, seats));
        } else if (method == VotingMethod.DHONDT || method == VotingMethod.SAINTE_LAGUE || method == VotingMethod.LARGEST_REMAINDER_HARE) {
            return VotingSystem.determineWinners(aggregateFirstPreferences(rankedBallots), method, seats);
        } else {
            throw new UnsupportedOperationException("Method " + method + " not directly supported by Engine yet.");
        }
    }

    /**
     * Calculates the winner using Plurality (First-Past-The-Post).
     */
    public static List<String> calculatePluralityWinner(List<Ballot> ballots, String electionId) {
        if (ballots == null || ballots.isEmpty()) return Collections.emptyList();

        Map<String, Long> tallies = ballots.stream()
            .map(b -> b.getTopChoice(electionId))
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        
        return getHeighestRanked(tallies);
    }

    private static Map<String, Long> aggregateFirstPreferences(List<List<String>> rankedBallots) {
        Map<String, Long> counts = new HashMap<>();
        for (List<String> b : rankedBallots) {
        if (!b.isEmpty()) counts.merge(b.get(0), 1L, (a, v) -> a + v);
        }
        return counts;
    }

    private static List<String> mapToWinnerList(Map<String, Integer> allocation) {
        List<String> winners = new ArrayList<>();
        allocation.forEach((name, seats) -> {
            if (seats > 0) winners.add(name);
        });
        return winners;
    }

    private static List<String> getHeighestRanked(Map<String, Long> tallies) {
        if (tallies.isEmpty()) return Collections.emptyList();
        long max = Collections.max(tallies.values());
        return tallies.entrySet().stream()
            .filter(e -> e.getValue() == max)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
}

