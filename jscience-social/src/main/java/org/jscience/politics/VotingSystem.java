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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines various voting systems and methods for determining winners in an election.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class VotingSystem {

    /** Enumeration of supported voting methods. */
    public enum Method {
        // ===== PLURALITY/MAJORITY METHODS =====
        /** Winner-takes-all based on plurality of votes. */
        FIRST_PAST_THE_POST,
        /** Two-Round System: runoff between top two if no majority. */
        TWO_ROUND,
        /** Anti-plurality (Veto) - vote for everyone except least favorite. */
        ANTI_PLURALITY,

        // ===== RANKED CHOICE METHODS =====
        /** Instant Runoff Voting (Ranked Choice). */
        INSTANT_RUNOFF,
        /** Condorcet method (Pairwise comparisons). */
        CONDORCET,
        /** Borda Count method (Point-based ranking). */
        BORDA,
        /** Schulze method (Beatpath) - Condorcet completion. */
        SCHULZE,
        /** Copeland's method - Pairwise wins/losses. */
        COPELAND,
        /** Tideman's Ranked Pairs - Lock in pairwise wins. */
        RANKED_PAIRS,
        /** Minimax (Simpson-Kramer) - Minimize worst pairwise defeat. */
        MINIMAX,
        /** Kemeny-Young - Maximum agreement ranking. */
        KEMENY_YOUNG,
        /** Dodgson's method - Swaps to reach Condorcet winner. */
        DODGSON,
        /** Bucklin Voting - Accumulated majority ranking. */
        BUCKLIN,
        /** Coombs' Method - Eliminate most last-place votes. */
        COOMBS,
        /** Single Transferable Vote (multi-winner). */
        STV,

        // ===== CARDINAL/RATING METHODS =====
        /** Approval Voting - approve multiple candidates. */
        APPROVAL,
        /** Range/Score Voting - rate candidates on a scale. */
        RANGE,
        /** STAR Voting - Score Then Automatic Runoff. */
        STAR,
        /** Majority Judgment - Median-based grading. */
        MAJORITY_JUDGMENT,

        // ===== PROPORTIONAL REPRESENTATION =====
        /** Simple proportional based on vote share. */
        PROPORTIONAL,
        /** D'Hondt method (Jefferson) - favors larger parties. */
        DHONDT,
        /** Sainte-Laguë method (Webster) - more proportional. */
        SAINTE_LAGUE,
        /** Modified Sainte-Laguë - used in Nordic countries. */
        MODIFIED_SAINTE_LAGUE,
        /** Huntington-Hill - U.S. House apportionment. */
        HUNTINGTON_HILL,
        /** Largest Remainder with Hare quota (Hamilton's method). */
        LARGEST_REMAINDER_HARE,
        /** Largest Remainder with Droop quota. */
        LARGEST_REMAINDER_DROOP,

        // ===== MULTI-WINNER/OTHER =====
        /** Single Non-Transferable Vote. */
        SNTV,
        /** Cumulative Voting - distribute multiple votes. */
        CUMULATIVE
    }

    /**
     * Determines winner(s) based on aggregated vote counts and the specified method.
     * 
     * @param votes          Map of Candidate/Party Name -> Vote Count
     * @param method         The voting method algorithm to apply
     * @param seatsAvailable Number of seats to fill (relevant for proportional representation)
     * @return List of winners (names or descriptions of allocations)
     */
    public static List<String> determineWinners(Map<String, Long> votes, Method method, int seatsAvailable) {
        List<String> winners = new ArrayList<>();

        if (votes == null || votes.isEmpty())
            return winners;

        switch (method) {
            case FIRST_PAST_THE_POST:
                // Find single candidate with max votes
                votes.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .ifPresent(winnerEntry -> winners.add(winnerEntry.getKey()));
                break;

            case PROPORTIONAL:
                return formatAllocation(votes, entry -> (double) entry.getValue() / votes.values().stream().mapToLong(Long::longValue).sum() * seatsAvailable);

            case DHONDT:
                return mapToWinnerList(ElectoralSimulations.allocateDHondt(votes, seatsAvailable));

            case SAINTE_LAGUE:
                return mapToWinnerList(ElectoralSimulations.allocateSainteLague(votes, seatsAvailable));

            case MODIFIED_SAINTE_LAGUE:
                return mapToWinnerList(ElectoralSimulations.allocateModifiedSainteLague(votes, seatsAvailable));

            case HUNTINGTON_HILL:
                return mapToWinnerList(ElectoralSimulations.allocateHuntingtonHill(votes, seatsAvailable));

            case LARGEST_REMAINDER_HARE:
                return mapToWinnerList(ElectoralSimulations.allocateLargestRemainderHare(votes, seatsAvailable));

            case LARGEST_REMAINDER_DROOP:
                return mapToWinnerList(ElectoralSimulations.allocateLargestRemainderDroop(votes, seatsAvailable));

            case SNTV:
                return mapToWinnerList(ElectoralSimulations.resolveSNTV(votes, seatsAvailable));

            case CUMULATIVE:
                return mapToWinnerList(ElectoralSimulations.resolveCumulative(votes, seatsAvailable));

            case INSTANT_RUNOFF:
            case CONDORCET:
            case BORDA:
            case SCHULZE:
            case COPELAND:
            case RANKED_PAIRS:
            case MINIMAX:
            case KEMENY_YOUNG:
            case DODGSON:
            case BUCKLIN:
            case COOMBS:
            case STV:
            case TWO_ROUND:
            case ANTI_PLURALITY:
                throw new IllegalArgumentException("Method " + method + " requires full ranked ballots, not aggregated counts.");

            case APPROVAL:
                throw new IllegalArgumentException("Method APPROVAL requires a list of approval sets, not aggregated counts.");

            case RANGE:
            case STAR:
            case MAJORITY_JUDGMENT:
                throw new IllegalArgumentException("Method " + method + " requires cardinal scores/grades, not aggregated counts.");

            default:
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
    public static List<String> determineWinnersFromBallots(List<List<String>> ballots, Method method, int seatsAvailable) {
        List<String> winners = new ArrayList<>();
        if (ballots == null || ballots.isEmpty()) return winners;

        switch (method) {
            case FIRST_PAST_THE_POST:
                return determineWinners(aggregateFirstPreferences(ballots), method, seatsAvailable);

            case TWO_ROUND:
                winners.add(ElectoralSimulations.resolveTwoRound(ballots));
                break;

            case INSTANT_RUNOFF:
                winners.add(ElectoralSimulations.resolveInstantRunoff(ballots));
                break;

            case CONDORCET:
                winners.add(ElectoralSimulations.resolveCondorcet(ballots));
                break;

            case SCHULZE:
                winners.add(ElectoralSimulations.resolveSchulze(ballots));
                break;

            case COPELAND:
                winners.add(ElectoralSimulations.resolveCopeland(ballots));
                break;

            case BORDA:
                winners.add(ElectoralSimulations.resolveBorda(ballots));
                break;

            case ANTI_PLURALITY:
                winners.add(ElectoralSimulations.resolveAntiPlurality(ballots));
                break;

            case BUCKLIN:
                winners.add(ElectoralSimulations.resolveBucklin(ballots));
                break;

            case COOMBS:
                winners.add(ElectoralSimulations.resolveCoombs(ballots));
                break;

            case RANKED_PAIRS:
                winners.add(ElectoralSimulations.resolveRankedPairs(ballots));
                break;

            case MINIMAX:
                winners.add(ElectoralSimulations.resolveMinimax(ballots));
                break;

            case DODGSON:
                winners.add(ElectoralSimulations.resolveDodgson(ballots));
                break;

            case KEMENY_YOUNG:
                winners.add(ElectoralSimulations.resolveKemenyYoung(ballots));
                break;

            case STV:
                return mapToWinnerList(ElectoralSimulations.resolveSTV(ballots, seatsAvailable));

            case APPROVAL:
                winners.add(ElectoralSimulations.resolveApproval(ballots));
                break;

            case PROPORTIONAL:
            case DHONDT:
            case SAINTE_LAGUE:
            case MODIFIED_SAINTE_LAGUE:
            case LARGEST_REMAINDER_HARE:
            case LARGEST_REMAINDER_DROOP:
                return determineWinners(aggregateFirstPreferences(ballots), method, seatsAvailable);

            case SNTV:
            case CUMULATIVE:
                return determineWinners(aggregateFirstPreferences(ballots), method, seatsAvailable);

            default:
                throw new UnsupportedOperationException("Voting method not supported with ballots: " + method);
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
    public static List<String> determineWinnersFromCardinalData(List<Map<String, Integer>> data, Method method) {
        List<String> winners = new ArrayList<>();
        if (data == null || data.isEmpty()) return winners;

        switch (method) {
            case RANGE:
                winners.add(ElectoralSimulations.resolveRange(data));
                break;
            case STAR:
                winners.add(ElectoralSimulations.resolveSTAR(data));
                break;
            case MAJORITY_JUDGMENT:
                winners.add(ElectoralSimulations.resolveMajorityJudgment(data));
                break;
            default:
                throw new UnsupportedOperationException("Method " + method + " not supported with cardinal data.");
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

