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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides algorithms for simulating complex electoral systems such as 
 * Instant Runoff Voting (IRV) and Single Transferable Vote (STV).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class ElectoralSimulations {

    private ElectoralSimulations() {}

    /**
     * Resolves an Instant Runoff (Ranked Choice) election.
     * Voters' preferences are processed iteratively until a majority is reached.
     * 
     * @param ballots List of voter preferences (each list is a ranked list of candidate names)
     * @return the name of the winning candidate, or "No Winner"
     */
    public static String resolveInstantRunoff(List<List<String>> ballots) {
        if (ballots == null || ballots.isEmpty()) return "No Winner";

        Set<String> activeCandidates = new HashSet<>();
        for (List<String> b : ballots) activeCandidates.addAll(b);

        while (activeCandidates.size() > 1) {
            Map<String, Integer> counts = new HashMap<>();
            for (List<String> b : ballots) {
                for (String candidate : b) {
                    if (activeCandidates.contains(candidate)) {
                        counts.put(candidate, counts.getOrDefault(candidate, 0) + 1);
                        break;
                    }
                }
            }

            // Check for absolute majority
            int total = ballots.size();
            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                if (entry.getValue() > total / 2) {
                    return entry.getKey();
                }
            }

            // Eliminate the candidate with the fewest votes
            String loser = null;
            int minVotes = Integer.MAX_VALUE;
            for (String c : activeCandidates) {
                int v = counts.getOrDefault(c, 0);
                if (v < minVotes) {
                    minVotes = v;
                    loser = c;
                }
            }
            if (loser == null) break;
            activeCandidates.remove(loser);
        }

        return activeCandidates.stream().findFirst().orElse("No Winner");
    }

    /**
     * Resolves an election using the Condorcet method.
     * A candidate is a Condorcet winner if they defeat every other candidate in pairwise comparisons.
     * 
     * @param ballots List of voter preferences (each list is a ranked list of candidate names)
     * @return the name of the Condorcet winner, or "No Condorcet Winner"
     */
    public static String resolveCondorcet(List<List<String>> ballots) {
        if (ballots == null || ballots.isEmpty()) return "No Winner";

        Set<String> candidates = new HashSet<>();
        for (List<String> b : ballots) candidates.addAll(b);

        List<String> candidateList = new ArrayList<>(candidates);

        for (String c1 : candidateList) {
            boolean isCondorcetWinner = true;
            for (String c2 : candidateList) {
                if (c1.equals(c2)) continue;

                int wins = 0;
                int losses = 0;
                for (List<String> b : ballots) {
                    int pos1 = b.indexOf(c1);
                    int pos2 = b.indexOf(c2);

                    if (pos1 != -1 && (pos2 == -1 || pos1 < pos2)) {
                        wins++;
                    } else if (pos2 != -1 && (pos1 == -1 || pos2 < pos1)) {
                        losses++;
                    }
                }

                if (wins <= losses) {
                    isCondorcetWinner = false;
                    break;
                }
            }
            if (isCondorcetWinner) return c1;
        }

        return "No Condorcet Winner";
    }

    /**
     * Resolves an election using the Borda Count method.
     * Candidates are assigned points based on their rank in each ballot.
     * For N candidates, 1st place gets N-1 points, 2nd gets N-2, etc.
     * 
     * @param ballots List of voter preferences
     * @return the name of the winner
     */
    public static String resolveBorda(List<List<String>> ballots) {
        if (ballots == null || ballots.isEmpty()) return "No Winner";

        Set<String> candidates = new HashSet<>();
        for (List<String> b : ballots) candidates.addAll(b);
        int n = candidates.size();

        Map<String, Integer> points = new HashMap<>();
        // Initialize all candidates with 0 points
        for (String c : candidates) points.put(c, 0);

        for (List<String> b : ballots) {
            for (int i = 0; i < b.size(); i++) {
                String candidate = b.get(i);
                if (candidates.contains(candidate)) {
                    int p = n - 1 - i;
                    points.merge(candidate, Math.max(0, p), (x, y) -> x + y);
                }
            }
        }

        return points.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Winner");
    }

    /**
     * Calculates seat distribution using Single Transferable Vote (STV) algorithm with Droop Quota.
     * 
     * @param ballots preference lists
     * @param seats   number of seats to fill
     * @return map of winners to the number of seats they won (typically 1 in STV)
     */
    public static Map<String, Integer> resolveSTV(List<List<String>> ballots, int seats) {
        if (ballots == null || ballots.isEmpty() || seats <= 0) return Map.of();
        
        double quota = Math.floor(ballots.size() / (seats + 1.0)) + 1.0;
        Map<String, Integer> winners = new HashMap<>();
        Map<String, Double> votes = new HashMap<>();

        // Initial count of first preferences
        for (List<String> b : ballots) {
            if (!b.isEmpty()) {
                String first = b.get(0);
                votes.put(first, votes.getOrDefault(first, 0.0) + 1.0);
            }
        }

        // Simplified STV: check who meets the initial quota
        for (Map.Entry<String, Double> entry : votes.entrySet()) {
            if (entry.getValue() >= quota && winners.size() < seats) {
                winners.put(entry.getKey(), 1);
            }
        }

        return winners;
    }

    // ==================== CONDORCET COMPLETION METHODS ====================

    /**
     * Resolves an election using the Schulze method (Beatpath).
     * This is a Condorcet completion method that finds the strongest paths between candidates.
     * Used by Debian, Ubuntu, and many open-source organizations.
     * 
     * @param ballots List of voter preferences
     * @return the name of the Schulze winner
     */
    public static String resolveSchulze(List<List<String>> ballots) {
        if (ballots == null || ballots.isEmpty()) return "No Winner";

        Set<String> candidateSet = new HashSet<>();
        for (List<String> b : ballots) candidateSet.addAll(b);
        List<String> candidates = new ArrayList<>(candidateSet);
        int n = candidates.size();
        if (n == 0) return "No Winner";

        // Build pairwise preference matrix d[i][j] = number of voters preferring i over j
        int[][] d = new int[n][n];
        for (List<String> ballot : ballots) {
            for (int i = 0; i < ballot.size(); i++) {
                int idxI = candidates.indexOf(ballot.get(i));
                if (idxI == -1) continue;
                for (int j = i + 1; j < ballot.size(); j++) {
                    int idxJ = candidates.indexOf(ballot.get(j));
                    if (idxJ != -1) {
                        d[idxI][idxJ]++;
                    }
                }
                // Candidates not in ballot are considered ranked below all listed ones
                for (String c : candidates) {
                    if (!ballot.contains(c)) {
                        int idxC = candidates.indexOf(c);
                        d[idxI][idxC]++;
                    }
                }
            }
        }

        // Calculate strongest path strengths using Floyd-Warshall variant
        int[][] p = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    p[i][j] = d[i][j] > d[j][i] ? d[i][j] : 0;
                }
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (i == k) continue;
                for (int j = 0; j < n; j++) {
                    if (j == i || j == k) continue;
                    p[i][j] = Math.max(p[i][j], Math.min(p[i][k], p[k][j]));
                }
            }
        }

        // Find winner: candidate who has stronger paths to all others
        for (int i = 0; i < n; i++) {
            boolean isWinner = true;
            for (int j = 0; j < n; j++) {
                if (i != j && p[j][i] > p[i][j]) {
                    isWinner = false;
                    break;
                }
            }
            if (isWinner) return candidates.get(i);
        }

        return "No Winner";
    }

    /**
     * Resolves an election using Copeland's method.
     * Candidates earn 1 point for each pairwise win, 0.5 for ties.
     * The candidate with the most points wins.
     * 
     * @param ballots List of voter preferences
     * @return the name of the Copeland winner
     */
    public static String resolveCopeland(List<List<String>> ballots) {
        if (ballots == null || ballots.isEmpty()) return "No Winner";

        Set<String> candidateSet = new HashSet<>();
        for (List<String> b : ballots) candidateSet.addAll(b);
        List<String> candidates = new ArrayList<>(candidateSet);
        int n = candidates.size();
        if (n == 0) return "No Winner";

        // Build pairwise comparison matrix
        int[][] pairwise = new int[n][n];
        for (List<String> ballot : ballots) {
            for (int i = 0; i < ballot.size(); i++) {
                int idxI = candidates.indexOf(ballot.get(i));
                if (idxI == -1) continue;
                for (int j = i + 1; j < ballot.size(); j++) {
                    int idxJ = candidates.indexOf(ballot.get(j));
                    if (idxJ != -1) {
                        pairwise[idxI][idxJ]++;
                    }
                }
            }
        }

        // Calculate Copeland scores
        double[] scores = new double[n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (pairwise[i][j] > pairwise[j][i]) {
                    scores[i] += 1.0;
                } else if (pairwise[i][j] < pairwise[j][i]) {
                    scores[j] += 1.0;
                } else {
                    scores[i] += 0.5;
                    scores[j] += 0.5;
                }
            }
        }

        // Find winner with highest score
        int winnerIdx = 0;
        for (int i = 1; i < n; i++) {
            if (scores[i] > scores[winnerIdx]) {
                winnerIdx = i;
            }
        }

        return candidates.get(winnerIdx);
    }

    // ==================== CARDINAL VOTING METHODS ====================

    /**
     * Resolves an election using Approval Voting.
     * Each voter approves of multiple candidates, and the candidate with most approvals wins.
     * Input format: each list contains the candidates approved by that voter.
     * 
     * @param approvals List of approval sets (each inner list = candidates approved by one voter)
     * @return the name of the winner
     */
    public static String resolveApproval(List<List<String>> approvals) {
        if (approvals == null || approvals.isEmpty()) return "No Winner";

        Map<String, Integer> counts = new HashMap<>();
        for (List<String> voterApprovals : approvals) {
            for (String candidate : voterApprovals) {
                counts.merge(candidate, 1, (a, b) -> a + b);
            }
        }

        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Winner");
    }

    /**
     * Resolves an election using Range/Score Voting.
     * Voters assign scores to candidates, and the candidate with highest average score wins.
     * Input format: List of maps, where each map represents a voter's scores for candidates.
     * 
     * @param scores List of score maps (Candidate -> Score)
     * @return the name of the winner
     */
    public static String resolveRange(List<Map<String, Integer>> scores) {
        if (scores == null || scores.isEmpty()) return "No Winner";

        Map<String, Integer> totalScores = new HashMap<>();
        Map<String, Integer> voteCounts = new HashMap<>();

        for (Map<String, Integer> voterScores : scores) {
            for (Map.Entry<String, Integer> entry : voterScores.entrySet()) {
                totalScores.merge(entry.getKey(), entry.getValue(), (a, b) -> a + b);
                voteCounts.merge(entry.getKey(), 1, (a, b) -> a + b);
            }
        }

        // Calculate average scores
        Map<String, Double> averages = new HashMap<>();
        for (String candidate : totalScores.keySet()) {
            int count = voteCounts.getOrDefault(candidate, 1);
            averages.put(candidate, (double) totalScores.get(candidate) / count);
        }

        return averages.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Winner");
    }

    /**
     * Resolves an election using STAR Voting (Score Then Automatic Runoff).
     * First round: candidates scored 0-5, top two advance.
     * Second round: automatic runoff between top two based on ballot preferences.
     * 
     * @param scores List of score maps (Candidate -> Score, typically 0-5)
     * @return the name of the STAR winner
     */
    public static String resolveSTAR(List<Map<String, Integer>> scores) {
        if (scores == null || scores.isEmpty()) return "No Winner";

        // First round: sum scores to find top two
        Map<String, Integer> totalScores = new HashMap<>();
        for (Map<String, Integer> voterScores : scores) {
            for (Map.Entry<String, Integer> entry : voterScores.entrySet()) {
                totalScores.merge(entry.getKey(), entry.getValue(), (a, b) -> a + b);
            }
        }

        // Find top two candidates
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(totalScores.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        if (sorted.size() < 2) {
            return sorted.isEmpty() ? "No Winner" : sorted.get(0).getKey();
        }

        String first = sorted.get(0).getKey();
        String second = sorted.get(1).getKey();

        // Second round: automatic runoff based on preferences
        int firstVotes = 0;
        int secondVotes = 0;
        for (Map<String, Integer> voterScores : scores) {
            int scoreFirst = voterScores.getOrDefault(first, 0);
            int scoreSecond = voterScores.getOrDefault(second, 0);
            if (scoreFirst > scoreSecond) {
                firstVotes++;
            } else if (scoreSecond > scoreFirst) {
                secondVotes++;
            }
            // Ties don't count for either
        }

        return firstVotes >= secondVotes ? first : second;
    }

    // ==================== PROPORTIONAL REPRESENTATION METHODS ====================

    /**
     * Allocates seats using the D'Hondt method (Jefferson method).
     * Favors larger parties, used in many European countries.
     * 
     * @param votes Map of Party -> Vote Count
     * @param seats Number of seats to allocate
     * @return Map of Party -> Seats Won
     */
    public static Map<String, Integer> allocateDHondt(Map<String, Long> votes, int seats) {
        if (votes == null || votes.isEmpty() || seats <= 0) return Map.of();

        Map<String, Integer> allocation = new HashMap<>();
        for (String party : votes.keySet()) {
            allocation.put(party, 0);
        }

        for (int i = 0; i < seats; i++) {
            String winner = null;
            double maxQuotient = -1;

            for (Map.Entry<String, Long> entry : votes.entrySet()) {
                String party = entry.getKey();
                int currentSeats = allocation.get(party);
                double quotient = (double) entry.getValue() / (currentSeats + 1);

                if (quotient > maxQuotient) {
                    maxQuotient = quotient;
                    winner = party;
                }
            }

            if (winner != null) {
                allocation.merge(winner, 1, (a, b) -> a + b);
            }
        }

        return allocation;
    }

    /**
     * Allocates seats using the Sainte-LaguÃ« method (Webster method).
     * More proportional than D'Hondt, uses odd number divisors (1, 3, 5, 7...).
     * 
     * @param votes Map of Party -> Vote Count
     * @param seats Number of seats to allocate
     * @return Map of Party -> Seats Won
     */
    public static Map<String, Integer> allocateSainteLague(Map<String, Long> votes, int seats) {
        if (votes == null || votes.isEmpty() || seats <= 0) return Map.of();

        Map<String, Integer> allocation = new HashMap<>();
        for (String party : votes.keySet()) {
            allocation.put(party, 0);
        }

        for (int i = 0; i < seats; i++) {
            String winner = null;
            double maxQuotient = -1;

            for (Map.Entry<String, Long> entry : votes.entrySet()) {
                String party = entry.getKey();
                int currentSeats = allocation.get(party);
                // Sainte-LaguÃ« uses odd divisors: 1, 3, 5, 7, etc.
                double divisor = 2 * currentSeats + 1;
                double quotient = (double) entry.getValue() / divisor;

                if (quotient > maxQuotient) {
                    maxQuotient = quotient;
                    winner = party;
                }
            }

            if (winner != null) {
                allocation.merge(winner, 1, (a, b) -> a + b);
            }
        }

        return allocation;
    }

    /**
     * Allocates seats using Modified Sainte-LaguÃ« method.
     * Uses 1.4 as the first divisor instead of 1, reducing fragmentation.
     * Used in Norway, Sweden, and other Nordic countries.
     * 
     * @param votes Map of Party -> Vote Count
     * @param seats Number of seats to allocate
     * @return Map of Party -> Seats Won
     */
    public static Map<String, Integer> allocateModifiedSainteLague(Map<String, Long> votes, int seats) {
        if (votes == null || votes.isEmpty() || seats <= 0) return Map.of();

        Map<String, Integer> allocation = new HashMap<>();
        for (String party : votes.keySet()) {
            allocation.put(party, 0);
        }

        for (int i = 0; i < seats; i++) {
            String winner = null;
            double maxQuotient = -1;

            for (Map.Entry<String, Long> entry : votes.entrySet()) {
                String party = entry.getKey();
                int currentSeats = allocation.get(party);
                // Modified: first divisor is 1.4 instead of 1
                double divisor = currentSeats == 0 ? 1.4 : 2 * currentSeats + 1;
                double quotient = (double) entry.getValue() / divisor;

                if (quotient > maxQuotient) {
                    maxQuotient = quotient;
                    winner = party;
                }
            }

            if (winner != null) {
                allocation.merge(winner, 1, (a, b) -> a + b);
            }
        }

        return allocation;
    }

    /**
     * Allocates seats using the Huntington-Hill method.
     * Used for apportioning seats in the U.S. House of Representatives.
     * Uses geometric mean as divisor.
     * 
     * @param votes Map of State/Party -> Population/Votes
     * @param seats Number of seats to allocate
     * @return Map of State/Party -> Seats
     */
    public static Map<String, Integer> allocateHuntingtonHill(Map<String, Long> votes, int seats) {
        if (votes == null || votes.isEmpty() || seats <= 0) return Map.of();

        Map<String, Integer> allocation = new HashMap<>();
        // Each state/party starts with 1 seat (as per U.S. law)
        for (String party : votes.keySet()) {
            allocation.put(party, 1);
        }

        int remainingSeats = seats - allocation.size();
        if (remainingSeats < 0) {
            // Not enough seats for everyone to get one
            allocation.clear();
            for (String party : votes.keySet()) {
                allocation.put(party, 0);
            }
            remainingSeats = seats;
        }

        for (int i = 0; i < remainingSeats; i++) {
            String winner = null;
            double maxPriority = -1;

            for (Map.Entry<String, Long> entry : votes.entrySet()) {
                String party = entry.getKey();
                int currentSeats = allocation.get(party);
                // Huntington-Hill uses geometric mean: sqrt(n * (n+1))
                double divisor = Math.sqrt((double) currentSeats * (currentSeats + 1));
                double priority = entry.getValue() / divisor;

                if (priority > maxPriority) {
                    maxPriority = priority;
                    winner = party;
                }
            }

            if (winner != null) {
                allocation.merge(winner, 1, (a, b) -> a + b);
            }
        }

        return allocation;
    }

    /**
     * Allocates seats using the Largest Remainder method with Hare quota.
     * Also known as Hamilton's method.
     * 
     * @param votes Map of Party -> Vote Count
     * @param seats Number of seats to allocate
     * @return Map of Party -> Seats Won
     */
    public static Map<String, Integer> allocateLargestRemainderHare(Map<String, Long> votes, int seats) {
        if (votes == null || votes.isEmpty() || seats <= 0) return Map.of();

        long totalVotes = votes.values().stream().mapToLong(Long::longValue).sum();
        double quota = (double) totalVotes / seats; // Hare quota

        Map<String, Integer> allocation = new HashMap<>();
        Map<String, Double> remainders = new HashMap<>();
        int seatsAllocated = 0;

        // First pass: allocate seats based on quotas
        for (Map.Entry<String, Long> entry : votes.entrySet()) {
            double exactSeats = entry.getValue() / quota;
            int wholeSeats = (int) Math.floor(exactSeats);
            allocation.put(entry.getKey(), wholeSeats);
            remainders.put(entry.getKey(), exactSeats - wholeSeats);
            seatsAllocated += wholeSeats;
        }

        // Second pass: allocate remaining seats by largest remainder
        int remainingSeats = seats - seatsAllocated;
        List<Map.Entry<String, Double>> sortedRemainders = new ArrayList<>(remainders.entrySet());
        sortedRemainders.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (int i = 0; i < remainingSeats && i < sortedRemainders.size(); i++) {
            String party = sortedRemainders.get(i).getKey();
            allocation.merge(party, 1, (a, b) -> a + b);
        }

        return allocation;
    }

    /**
     * Allocates seats using the Largest Remainder method with Droop quota.
     * Droop quota = (total votes / (seats + 1)) + 1
     * 
     * @param votes Map of Party -> Vote Count
     * @param seats Number of seats to allocate
     * @return Map of Party -> Seats Won
     */
    public static Map<String, Integer> allocateLargestRemainderDroop(Map<String, Long> votes, int seats) {
        if (votes == null || votes.isEmpty() || seats <= 0) return Map.of();

        long totalVotes = votes.values().stream().mapToLong(Long::longValue).sum();
        double quota = Math.floor((double) totalVotes / (seats + 1)) + 1; // Droop quota

        Map<String, Integer> allocation = new HashMap<>();
        Map<String, Double> remainders = new HashMap<>();
        int seatsAllocated = 0;

        for (Map.Entry<String, Long> entry : votes.entrySet()) {
            double exactSeats = entry.getValue() / quota;
            int wholeSeats = (int) Math.floor(exactSeats);
            allocation.put(entry.getKey(), wholeSeats);
            remainders.put(entry.getKey(), exactSeats - wholeSeats);
            seatsAllocated += wholeSeats;
        }

        int remainingSeats = seats - seatsAllocated;
        List<Map.Entry<String, Double>> sortedRemainders = new ArrayList<>(remainders.entrySet());
        sortedRemainders.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (int i = 0; i < remainingSeats && i < sortedRemainders.size(); i++) {
            String party = sortedRemainders.get(i).getKey();
            allocation.merge(party, 1, (a, b) -> a + b);
        }

        return allocation;
    }

    // ==================== TWO-ROUND/RUNOFF SYSTEMS ====================

    /**
     * Simulates a Two-Round System (TRS) election.
     * If no candidate gets >50% in first round, top two go to runoff.
     * 
     * @param ballots Ranked preferences
     * @return the winner after one or two rounds
     */
    public static String resolveTwoRound(List<List<String>> ballots) {
        if (ballots == null || ballots.isEmpty()) return "No Winner";

        // First round: count first preferences
        Map<String, Integer> firstRoundCounts = new HashMap<>();
        for (List<String> ballot : ballots) {
            if (!ballot.isEmpty()) {
                firstRoundCounts.merge(ballot.get(0), 1, (a, b) -> a + b);
            }
        }

        int total = ballots.size();
        
        // Check for first-round winner (>50%)
        for (Map.Entry<String, Integer> entry : firstRoundCounts.entrySet()) {
            if (entry.getValue() > total / 2) {
                return entry.getKey();
            }
        }

        // Find top two for runoff
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(firstRoundCounts.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        if (sorted.size() < 2) {
            return sorted.isEmpty() ? "No Winner" : sorted.get(0).getKey();
        }

        String first = sorted.get(0).getKey();
        String second = sorted.get(1).getKey();

        // Second round: count preferences between top two
        int firstVotes = 0;
        int secondVotes = 0;
        for (List<String> ballot : ballots) {
            int firstIdx = ballot.indexOf(first);
            int secondIdx = ballot.indexOf(second);

            if (firstIdx != -1 && (secondIdx == -1 || firstIdx < secondIdx)) {
                firstVotes++;
            } else if (secondIdx != -1 && (firstIdx == -1 || secondIdx < firstIdx)) {
                secondVotes++;
            }
        }

        return firstVotes >= secondVotes ? first : second;
    }

    // ==================== ADDITIONAL RESEARCH METHODS ====================

    /**
     * Resolves an election using the Minimax method (Simpson-Kramer).
     * The winner is the candidate whose greatest pairwise defeat is the smallest.
     * 
     * @param ballots List of ranked preferences
     * @return the name of the Minimax winner
     */
    public static String resolveMinimax(List<List<String>> ballots) {
        if (ballots == null || ballots.isEmpty()) return "No Winner";

        Set<String> candidateSet = new HashSet<>();
        for (List<String> b : ballots) candidateSet.addAll(b);
        List<String> candidates = new ArrayList<>(candidateSet);
        int n = candidates.size();
        if (n == 0) return "No Winner";

        int[][] d = computePairwiseMatrix(ballots, candidates);

        // For each candidate, find their worst pairwise defeat
        double[] worstDefeat = new double[n];
        for (int i = 0; i < n; i++) {
            double maxDefeat = -1;
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                // Defeat is when j is preferred over i
                if (d[j][i] > d[i][j]) {
                    maxDefeat = Math.max(maxDefeat, d[j][i]);
                }
            }
            worstDefeat[i] = maxDefeat == -1 ? 0 : maxDefeat;
        }

        int winnerIdx = 0;
        for (int i = 1; i < n; i++) {
            if (worstDefeat[i] < worstDefeat[winnerIdx]) {
                winnerIdx = i;
            }
        }

        return candidates.get(winnerIdx);
    }

    /**
     * Resolves an election using the Anti-plurality method (Veto).
     * Each voter awards one point to all candidates except their least favorite.
     * The candidate with the most points wins.
     * 
     * @param ballots List of ranked preferences
     * @return the name of the winner
     */
    public static String resolveAntiPlurality(List<List<String>> ballots) {
        if (ballots == null || ballots.isEmpty()) return "No Winner";

        Set<String> candidateSet = new HashSet<>();
        for (List<String> b : ballots) candidateSet.addAll(b);
        
        Map<String, Integer> counts = new HashMap<>();
        for (String c : candidateSet) counts.put(c, 0);

        for (List<String> ballot : ballots) {
            if (ballot.isEmpty()) continue;
            // Everyone except the last one listed gets a point
            // If some candidates are not listed, they are tied for last? 
            // In standard anti-plurality, you just don't pick your 1 least favorite.
            String leastFavorite = ballot.get(ballot.size() - 1);
            for (String c : candidateSet) {
                if (!c.equals(leastFavorite)) {
                    counts.merge(c, 1, (a, b) -> a + b);
                }
            }
        }

        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Winner");
    }

    /**
     * Resolves an election using Bucklin Voting.
     * Check 1st choices; if no majority, add 2nd choices, etc., until someone has a majority.
     * 
     * @param ballots List of ranked preferences
     * @return the name of the winner
     */
    public static String resolveBucklin(List<List<String>> ballots) {
        if (ballots == null || ballots.isEmpty()) return "No Winner";

        Set<String> candidates = new HashSet<>();
        for (List<String> b : ballots) candidates.addAll(b);
        int totalVoters = ballots.size();
        int majorityThreshold = totalVoters / 2;

        Map<String, Integer> accumulatedVotes = new HashMap<>();
        int maxRank = 0;
        for (List<String> b : ballots) maxRank = Math.max(maxRank, b.size());

        for (int rank = 0; rank < maxRank; rank++) {
            for (List<String> ballot : ballots) {
                if (rank < ballot.size()) {
                    accumulatedVotes.merge(ballot.get(rank), 1, (a, b) -> a + b);
                }
            }

            // Check if anyone has reached majority
            List<String> majorityWinners = new ArrayList<>();
            int maxVotes = -1;
            for (Map.Entry<String, Integer> entry : accumulatedVotes.entrySet()) {
                if (entry.getValue() > majorityThreshold) {
                    if (entry.getValue() > maxVotes) {
                        maxVotes = entry.getValue();
                        majorityWinners.clear();
                        majorityWinners.add(entry.getKey());
                    } else if (entry.getValue() == maxVotes) {
                        majorityWinners.add(entry.getKey());
                    }
                }
            }

            if (!majorityWinners.isEmpty()) {
                // Return the one with highest votes in this round
                return majorityWinners.get(0); 
            }
        }

        // If no majority after all ranks, return the one with most accumulated votes
        return accumulatedVotes.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Winner");
    }

    /**
     * Resolves an election using Coombs' Method.
     * Similar to IRV, but eliminates the candidate with the most LAST place votes.
     * 
     * @param ballots List of ranked preferences
     * @return the name of the winner
     */
    public static String resolveCoombs(List<List<String>> ballots) {
        if (ballots == null || ballots.isEmpty()) return "No Winner";

        Set<String> activeCandidates = new HashSet<>();
        for (List<String> b : ballots) activeCandidates.addAll(b);

        while (activeCandidates.size() > 1) {
            Map<String, Integer> firstPref = new HashMap<>();
            Map<String, Integer> lastPref = new HashMap<>();
            
            for (List<String> b : ballots) {
                String first = null;
                String last = null;
                for (String c : b) {
                    if (activeCandidates.contains(c)) {
                        if (first == null) first = c;
                        last = c;
                    }
                }
                if (first != null) firstPref.merge(first, 1, (a, v) -> a + v);
                if (last != null) lastPref.merge(last, 1, (a, v) -> a + v);
            }

            // Check for absolute majority of first preferences
            int total = ballots.size();
            for (Map.Entry<String, Integer> entry : firstPref.entrySet()) {
                if (entry.getValue() > total / 2) return entry.getKey();
            }

            // Eliminate candidate with most last-place votes
            String toEliminate = null;
            int maxLastVotes = -1;
            for (String c : activeCandidates) {
                int lastVotes = lastPref.getOrDefault(c, 0);
                if (lastVotes > maxLastVotes) {
                    maxLastVotes = lastVotes;
                    toEliminate = c;
                }
            }

            if (toEliminate == null) break;
            activeCandidates.remove(toEliminate);
        }

        return activeCandidates.stream().findFirst().orElse("No Winner");
    }

    /**
     * Resolves an election using the Kemeny-Young method.
     * Finds the ranking that maximizes pairwise agreement with voters.
     * Note: This implementation uses a simple permutation search, limited to small sets.
     * 
     * @param ballots List of ranked preferences
     * @return the name of the Kemeny-Young winner
     */
    public static String resolveKemenyYoung(List<List<String>> ballots) {
        if (ballots == null || ballots.isEmpty()) return "No Winner";

        Set<String> candidateSet = new HashSet<>();
        for (List<String> b : ballots) candidateSet.addAll(b);
        List<String> candidates = new ArrayList<>(candidateSet);
        
        if (candidates.size() > 8) {
            // Kemeny-Young is NP-hard, return Minimax as fallback or warn
            return resolveMinimax(ballots);
        }

        int[][] d = computePairwiseMatrix(ballots, candidates);
        List<List<Integer>> permutations = new ArrayList<>();
        generatePermutations(candidates.size(), new ArrayList<>(), permutations);

        long maxScore = -1;
        List<Integer> bestRanking = null;

        for (List<Integer> perm : permutations) {
            long score = 0;
            for (int i = 0; i < perm.size(); i++) {
                for (int j = i + 1; j < perm.size(); j++) {
                    score += d[perm.get(i)][perm.get(j)];
                }
            }
            if (score > maxScore) {
                maxScore = score;
                bestRanking = perm;
            }
        }

        if (bestRanking != null && !bestRanking.isEmpty()) {
            return candidates.get(bestRanking.get(0));
        }

        return "No Winner";
    }

    private static void generatePermutations(int n, List<Integer> current, List<List<Integer>> result) {
        if (current.size() == n) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = 0; i < n; i++) {
            if (!current.contains(i)) {
                current.add(i);
                generatePermutations(n, current, result);
                current.remove(current.size() - 1);
            }
        }
    }

    /**
     * Resolves an election using Tideman's Ranked Pairs method.
     * Pairs are "locked in" based on their margin of victory, avoiding cycles.
     * 
     * @param ballots List of ranked preferences
     * @return the name of the winner
     */
    public static String resolveRankedPairs(List<List<String>> ballots) {
        if (ballots == null || ballots.isEmpty()) return "No Winner";

        Set<String> candidateSet = new HashSet<>();
        for (List<String> b : ballots) candidateSet.addAll(b);
        List<String> candidates = new ArrayList<>(candidateSet);
        int n = candidates.size();

        int[][] d = computePairwiseMatrix(ballots, candidates);

        // Create list of all pairs and their margins
        class Pair {
            int winner, loser, margin;
            Pair(int w, int l, int m) { winner = w; loser = l; margin = m; }
        }
        List<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && d[i][j] > d[j][i]) {
                    pairs.add(new Pair(i, j, d[i][j] - d[j][i]));
                }
            }
        }

        // Sort pairs by margin of victory descending
        pairs.sort((p1, p2) -> p2.margin - p1.margin);

        // Lock in pairs without creating cycles
        boolean[][] graph = new boolean[n][n];
        for (Pair p : pairs) {
            if (!createsCycle(graph, p.winner, p.loser, n)) {
                graph[p.winner][p.loser] = true;
            }
        }

        // The winner is the source of the graph (no incoming edges)
        for (int i = 0; i < n; i++) {
            boolean incoming = false;
            for (int j = 0; j < n; j++) {
                if (graph[j][i]) {
                    incoming = true;
                    break;
                }
            }
            if (!incoming) return candidates.get(i);
        }

        return "No Winner";
    }

    private static boolean createsCycle(boolean[][] graph, int start, int end, int n) {
        if (start == end) return true;
        for (int i = 0; i < n; i++) {
            if (graph[end][i]) {
                if (createsCycle(graph, start, i, n)) return true;
            }
        }
        return false;
    }

    /**
     * Resolves an election using a simplified Dodgson's method.
     * Approximated by Tideman's score (n of wins) or simplified swap count.
     * 
     * @param ballots List of ranked preferences
     * @return the name of the winner
     */
    public static String resolveDodgson(List<List<String>> ballots) {
        // Simplified Dodgson: count total swaps needed to make candidate a Condorcet winner
        if (ballots == null || ballots.isEmpty()) return "No Winner";

        Set<String> candidateSet = new HashSet<>();
        for (List<String> b : ballots) candidateSet.addAll(b);
        List<String> candidates = new ArrayList<>(candidateSet);
        int n = candidates.size();

        int[][] d = computePairwiseMatrix(ballots, candidates);
        int[] totalSwapsNeeded = new int[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                if (d[i][j] <= d[j][i]) {
                    // Needs swaps to beat j
                    // Simplified: each swap in a ballot can gain 1 vote difference
                    totalSwapsNeeded[i] += (d[j][i] - d[i][j]) / 2 + 1;
                }
            }
        }

        int winnerIdx = 0;
        for (int i = 1; i < n; i++) {
            if (totalSwapsNeeded[i] < totalSwapsNeeded[winnerIdx]) {
                winnerIdx = i;
            }
        }

        return candidates.get(winnerIdx);
    }

    /**
     * Resolves an election using Majority Judgment.
     * Voters assign median-based grades (Excellent=6, Very Good=5, ..., Poor=1).
     * 
     * @param grades List of voter grades (Candidate -> Grade)
     * @return the name of the winner
     */
    public static String resolveMajorityJudgment(List<Map<String, Integer>> grades) {
        if (grades == null || grades.isEmpty()) return "No Winner";

        Map<String, List<Integer>> candidateGrades = new HashMap<>();
        for (Map<String, Integer> voterGrades : grades) {
            for (Map.Entry<String, Integer> entry : voterGrades.entrySet()) {
                candidateGrades.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add(entry.getValue());
            }
        }

        Map<String, Double> medians = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : candidateGrades.entrySet()) {
            List<Integer> g = entry.getValue();
            Collections.sort(g);
            double median;
            int n = g.size();
            if (n % 2 == 0) {
                median = (g.get(n / 2 - 1) + g.get(n / 2)) / 2.0;
            } else {
                median = g.get(n / 2);
            }
            medians.put(entry.getKey(), median);
        }

        return medians.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Winner");
    }

    /**
     * Resolves an election using Single Non-Transferable Vote (SNTV).
     * Voters have one vote; candidates with highest counts win multiple seats.
     * 
     * @param votes   Map of Candidate -> Vote Count
     * @param seats   Number of seats to fill
     * @return Map of winners to seats won (always 1 in SNTV)
     */
    public static Map<String, Integer> resolveSNTV(Map<String, Long> votes, int seats) {
        if (votes == null || votes.isEmpty() || seats <= 0) return Map.of();

        List<Map.Entry<String, Long>> sorted = new ArrayList<>(votes.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        Map<String, Integer> winners = new HashMap<>();
        for (int i = 0; i < seats && i < sorted.size(); i++) {
            winners.put(sorted.get(i).getKey(), 1);
        }

        return winners;
    }

    /**
     * Resolves an election using Cumulative Voting.
     * Voters can distribute multiple votes among candidates.
     * 
     * @param totalVotes Map of Candidate -> Total Votes received from all voters
     * @param seats      Number of seats to fill
     * @return Map of winners to seats
     */
    public static Map<String, Integer> resolveCumulative(Map<String, Long> totalVotes, int seats) {
        // Functionally same as SNTV but input is the sum of all distributed votes
        return resolveSNTV(totalVotes, seats);
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Computes the pairwise comparison matrix from ranked ballots.
     * 
     * @param ballots List of ranked preferences
     * @param candidates List of all candidates
     * @return 2D array where [i][j] = number of ballots preferring candidate i over j
     */
    public static int[][] computePairwiseMatrix(List<List<String>> ballots, List<String> candidates) {
        int n = candidates.size();
        int[][] matrix = new int[n][n];

        for (List<String> ballot : ballots) {
            for (int i = 0; i < ballot.size(); i++) {
                int idxI = candidates.indexOf(ballot.get(i));
                if (idxI == -1) continue;
                for (int j = i + 1; j < ballot.size(); j++) {
                    int idxJ = candidates.indexOf(ballot.get(j));
                    if (idxJ != -1) {
                        matrix[idxI][idxJ]++;
                    }
                }
                // Candidates not in ballot are assumed ranked below those in ballot
                for (String c : candidates) {
                    if (!ballot.contains(c)) {
                        int idxC = candidates.indexOf(c);
                        matrix[idxI][idxC]++;
                    }
                }
            }
        }

        return matrix;
    }

    /**
     * Checks if a Condorcet winner exists (beats all others in pairwise comparisons).
     * 
     * @param ballots List of ranked preferences
     * @return Optional containing the Condorcet winner, or empty if none exists
     */
    public static java.util.Optional<String> findCondorcetWinner(List<List<String>> ballots) {
        String result = resolveCondorcet(ballots);
        if ("No Winner".equals(result) || "No Condorcet Winner".equals(result)) {
            return java.util.Optional.empty();
        }
        return java.util.Optional.of(result);
    }
}



