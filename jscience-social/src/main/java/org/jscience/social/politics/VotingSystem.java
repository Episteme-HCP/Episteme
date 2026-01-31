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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines various voting systems and methods for determining winners in an election.
 * Refactored to use the extensible {@link VotingMethod} registry.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VotingSystem {

    private VotingSystem() {}

    /**
     * Determines winner(s) based on aggregated vote counts and the specified method.
     * 
     * @param votes          Map of Candidate/Party Name -> Vote Count
     * @param method         The voting method algorithm to apply
     * @param seatsAvailable Number of seats to fill (relevant for proportional representation)
     * @return List of winners (names or descriptions of allocations)
     */
    public static List<String> determineWinners(Map<String, Long> votes, VotingMethod method, int seatsAvailable) {
        List<String> winners = new ArrayList<>();

        if (votes == null || votes.isEmpty())
            return winners;

        if (method == VotingMethod.FIRST_PAST_THE_POST) {
            // Find single candidate with max votes
            votes.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .ifPresent(winnerEntry -> winners.add(winnerEntry.getKey()));
        } else if (method == VotingMethod.PROPORTIONAL) {
            return formatAllocation(votes, entry -> (double) entry.getValue() / votes.values().stream().mapToLong(Long::longValue).sum() * seatsAvailable);
        } else if (method == VotingMethod.DHONDT) {
            return mapToWinnerList(ElectoralSimulations.allocateDHondt(votes, seatsAvailable));
        } else if (method == VotingMethod.SAINTE_LAGUE) {
            return mapToWinnerList(ElectoralSimulations.allocateSainteLague(votes, seatsAvailable));
        } else if (method == VotingMethod.MODIFIED_SAINTE_LAGUE) {
            return mapToWinnerList(ElectoralSimulations.allocateModifiedSainteLague(votes, seatsAvailable));
        } else if (method == VotingMethod.HUNTINGTON_HILL) {
            return mapToWinnerList(ElectoralSimulations.allocateHuntingtonHill(votes, seatsAvailable));
        } else if (method == VotingMethod.LARGEST_REMAINDER_HARE) {
            return mapToWinnerList(ElectoralSimulations.allocateLargestRemainderHare(votes, seatsAvailable));
        } else if (method == VotingMethod.LARGEST_REMAINDER_DROOP) {
            return mapToWinnerList(ElectoralSimulations.allocateLargestRemainderDroop(votes, seatsAvailable));
        } else if (method == VotingMethod.SNTV) {
            return mapToWinnerList(ElectoralSimulations.resolveSNTV(votes, seatsAvailable));
        } else if (method == VotingMethod.CUMULATIVE) {
            return mapToWinnerList(ElectoralSimulations.resolveCumulative(votes, seatsAvailable));
        } else if (method == VotingMethod.INSTANT_RUNOFF || method == VotingMethod.CONDORCET || method == VotingMethod.BORDA
                || method == VotingMethod.SCHULZE || method == VotingMethod.COPELAND || method == VotingMethod.RANKED_PAIRS
                || method == VotingMethod.MINIMAX || method == VotingMethod.KEMENY_YOUNG || method == VotingMethod.DODGSON
                || method == VotingMethod.BUCKLIN || method == VotingMethod.COOMBS || method == VotingMethod.STV
                || method == VotingMethod.TWO_ROUND || method == VotingMethod.ANTI_PLURALITY) {
            throw new IllegalArgumentException("Method " + method + " requires full ranked ballots, not aggregated counts.");
        } else if (method == VotingMethod.APPROVAL) {
            throw new IllegalArgumentException("Method APPROVAL requires a list of approval sets, not aggregated counts.");
        } else if (method == VotingMethod.RANGE || method == VotingMethod.STAR || method == VotingMethod.MAJORITY_JUDGMENT) {
            throw new IllegalArgumentException("Method " + method + " requires cardinal scores/grades, not aggregated counts.");
        } else {
            throw new UnsupportedOperationException("Voting method not supported with aggregated counts: " + method);
        }
        return winners;
    }

    private static List<String> formatAllocation(Map<String, Long> votes, java.util.function.Function<Map.Entry<String, Long>, Double> seatCalc) {
        List<String> winners = new ArrayList<>();
        for (Map.Entry<String, Long> entry : votes.entrySet()) {
            int seats = (int) Math.round(seatCalc.apply(entry));
            if (seats > 0) winners.add(entry.getKey() + " (" + seats + " seats)");
        }
        return winners;
    }

    private static List<String> mapToWinnerList(Map<String, Integer> allocation) {
        List<String> winners = new ArrayList<>();
        allocation.forEach((name, seats) -> {
            if (seats > 0) winners.add(name + " (" + seats + " seats)");
        });
        return winners;
    }

    /**
     * Determines winner(s) based on full ranked ballots and the specified method.
     * 
     * @param ballots        List of voter preferences (each list is a ranked list of candidate names)
     * @param method         The voting method algorithm to apply
     * @param seatsAvailable Number of seats to fill
     * @return List of winners
     */
    public static List<String> determineWinnersFromBallots(List<List<String>> ballots, VotingMethod method, int seatsAvailable) {
        List<String> winners = new ArrayList<>();
        if (ballots == null || ballots.isEmpty()) return winners;

        if (method == VotingMethod.FIRST_PAST_THE_POST) {
            return determineWinners(aggregateFirstPreferences(ballots), method, seatsAvailable);
        } else if (method == VotingMethod.TWO_ROUND) {
            winners.add(ElectoralSimulations.resolveTwoRound(ballots));
        } else if (method == VotingMethod.INSTANT_RUNOFF) {
            winners.add(ElectoralSimulations.resolveInstantRunoff(ballots));
        } else if (method == VotingMethod.CONDORCET) {
            winners.add(ElectoralSimulations.resolveCondorcet(ballots));
        } else if (method == VotingMethod.SCHULZE) {
            winners.add(ElectoralSimulations.resolveSchulze(ballots));
        } else if (method == VotingMethod.COPELAND) {
            winners.add(ElectoralSimulations.resolveCopeland(ballots));
        } else if (method == VotingMethod.BORDA) {
            winners.add(ElectoralSimulations.resolveBorda(ballots));
        } else if (method == VotingMethod.ANTI_PLURALITY) {
            winners.add(ElectoralSimulations.resolveAntiPlurality(ballots));
        } else if (method == VotingMethod.BUCKLIN) {
            winners.add(ElectoralSimulations.resolveBucklin(ballots));
        } else if (method == VotingMethod.COOMBS) {
            winners.add(ElectoralSimulations.resolveCoombs(ballots));
        } else if (method == VotingMethod.RANKED_PAIRS) {
            winners.add(ElectoralSimulations.resolveRankedPairs(ballots));
        } else if (method == VotingMethod.MINIMAX) {
            winners.add(ElectoralSimulations.resolveMinimax(ballots));
        } else if (method == VotingMethod.DODGSON) {
            winners.add(ElectoralSimulations.resolveDodgson(ballots));
        } else if (method == VotingMethod.KEMENY_YOUNG) {
            winners.add(ElectoralSimulations.resolveKemenyYoung(ballots));
        } else if (method == VotingMethod.STV) {
            return mapToWinnerList(ElectoralSimulations.resolveSTV(ballots, seatsAvailable));
        } else if (method == VotingMethod.APPROVAL) {
            winners.add(ElectoralSimulations.resolveApproval(ballots));
        } else if (method == VotingMethod.PROPORTIONAL || method == VotingMethod.DHONDT || method == VotingMethod.SAINTE_LAGUE
                || method == VotingMethod.MODIFIED_SAINTE_LAGUE || method == VotingMethod.LARGEST_REMAINDER_HARE
                || method == VotingMethod.LARGEST_REMAINDER_DROOP || method == VotingMethod.SNTV || method == VotingMethod.CUMULATIVE
                || method == VotingMethod.HUNTINGTON_HILL) {
            return determineWinners(aggregateFirstPreferences(ballots), method, seatsAvailable);
        } else {
            throw new IllegalArgumentException("Voting method not supported with ballots: " + method);
        }
        return winners;
    }

    /**
     * Determines winner(s) based on cardinal scores/grades and the specified method.
     * 
     * @param data           List of voter scores or grades (each map is Candidate -> Value)
     * @param method         The voting method algorithm to apply
     * @return List of winners
     */
    public static List<String> determineWinnersFromCardinalData(List<Map<String, Integer>> data, VotingMethod method) {
        List<String> winners = new ArrayList<>();
        if (data == null || data.isEmpty()) return winners;

        if (method == VotingMethod.RANGE) {
            winners.add(ElectoralSimulations.resolveRange(data));
        } else if (method == VotingMethod.STAR) {
            winners.add(ElectoralSimulations.resolveSTAR(data));
        } else if (method == VotingMethod.MAJORITY_JUDGMENT) {
            winners.add(ElectoralSimulations.resolveMajorityJudgment(data));
        } else {
            throw new IllegalArgumentException("Method " + method + " not supported with cardinal data.");
        }
        return winners;
    }

    /**
     * Aggregates first-place preferences from ranked ballots.
     */
    private static Map<String, Long> aggregateFirstPreferences(List<List<String>> ballots) {
        Map<String, Long> counts = new HashMap<>();
        for (List<String> b : ballots) {
            if (!b.isEmpty()) counts.merge(b.get(0), 1L, (a, bVal) -> a + bVal);
        }
        return counts;
    }
}

