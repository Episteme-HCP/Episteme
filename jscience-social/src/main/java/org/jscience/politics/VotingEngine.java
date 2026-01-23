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

import java.util.*;
import java.util.stream.Collectors;

/**
 * An engine for calculating election winners using various social choice algorithms.
 * Supported methods include Plurality, Borda Count, Instant Runoff (IRV), and Majority Judgment.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class VotingEngine {

    private VotingEngine() {}

    /**
     * Calculates the winner using Plurality (First-Past-The-Post).
     * The candidate with the most first-choice votes wins.
     * 
     * @param ballots List of ballots containing at least one choice
     * @return List of winners (usually one, multiple in case of a tie)
     */
    public static List<String> calculatePluralityWinner(List<Ballot> ballots) {
        if (ballots == null || ballots.isEmpty()) return Collections.emptyList();

        Map<String, Long> tallies = ballots.stream()
            .filter(b -> b.rankedChoices() != null && !b.rankedChoices().isEmpty())
            .map(b -> b.rankedChoices().get(0))
            .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        
        return getHeighestRanked(tallies);
    }

    /**
     * Calculates the winner using the Borda Count method.
     * Voters rank candidates. For n candidates, 1st place gets n-1 points, 2nd gets n-2, etc.
     * The candidate with the most points wins.
     * 
     * @param ballots    List of ballots with rankings
     * @param candidates List of all valid candidates (determines max points)
     * @return List of winners
     */
    public static List<String> calculateBordaWinner(List<Ballot> ballots, List<String> candidates) {
        if (ballots == null || candidates == null || candidates.isEmpty()) return Collections.emptyList();

        Map<String, Long> scores = new HashMap<>();
        candidates.forEach(c -> scores.put(c, 0L));
        int n = candidates.size();

        for (Ballot b : ballots) {
            List<String> ranking = b.rankedChoices();
            if (ranking == null) continue;
            
            // Standard Borda: points = (N - 1) - rankIndex
            for (int i = 0; i < ranking.size(); i++) {
                String candidate = ranking.get(i);
                if (scores.containsKey(candidate)) {
                    // Only award points if the rank is within the number of candidates
                    if (i < n) {
                        scores.put(candidate, scores.get(candidate) + (long)(n - 1 - i));
                    }
                }
            }
        }
        return getHeighestRanked(scores);
    }

    /**
     * Calculates the winner using Instant Runoff Voting (IRV) / Alternative Vote.
     * Candidates with the fewest first-preference votes are eliminated, and their votes transferred.
     * This repeats until a candidate has a majority (>50%).
     * 
     * @param ballots    List of ranked ballots
     * @param candidates Set of all eligible candidates
     * @return List containing the winner (or winners if a tie persists in the final round)
     */
    public static List<String> calculateIRVWinner(List<Ballot> ballots, Set<String> candidates) {
        if (ballots == null || candidates == null || candidates.isEmpty()) return Collections.emptyList();

        Set<String> activeCandidates = new HashSet<>(candidates);
        
        while (activeCandidates.size() > 1) {
            final Set<String> currentActive = activeCandidates; // effective final for lambda
            
            // Count first choices among currently active candidates
            Map<String, Long> firstChoices = ballots.stream()
                .map(b -> b.rankedChoices().stream()
                    .filter(currentActive::contains) // Find highest preferred that is still active
                    .findFirst()
                    .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
            
            // Validate if we have votes left
            if (firstChoices.isEmpty()) break;

            long totalVotes = firstChoices.values().stream().mapToLong(L -> L).sum();
            
            // Check for Majority (> 50%)
            String majorityCandidate = firstChoices.entrySet().stream()
                .filter(e -> e.getValue() > totalVotes / 2)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
            
            if (majorityCandidate != null) return List.of(majorityCandidate);

            // Eliminate lowest
            long minVotes = firstChoices.values().stream().min(Long::compareTo).orElse(0L);
            List<String> candidatesToRemove = firstChoices.entrySet().stream()
                .filter(e -> e.getValue() == minVotes)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
            
            // Tie-breaking edge case: If all active candidates tied for last, stop.
            // Or if removing them leaves no one.
            if (candidatesToRemove.size() >= activeCandidates.size()) {
                // Return all tied candidates
                return new ArrayList<>(activeCandidates);
            }

            activeCandidates.removeAll(candidatesToRemove);
        }
        
        return new ArrayList<>(activeCandidates);
    }

    /**
     * Grading scale for Majority Judgment.
     */
    public enum Grade {
        EXCELLENT, VERY_GOOD, GOOD, ACCEPTABLE, POOR, REJECT
    }

    /**
     * Calculates the winner using Majority Judgment.
     * Voters grade each candidate (Excellent to Reject).
     * The winner is the candidate with the best median grade.
     * 
     * @param ballots    List of ballots with ratings
     * @param candidates List of candidates to evaluate
     * @return List of winners (candidates with the highest median grade)
     */
    public static List<String> calculateMajorityJudgmentWinner(List<Ballot> ballots, List<String> candidates) {
        if (ballots == null || candidates == null || candidates.isEmpty()) return Collections.emptyList();

        Map<String, List<Grade>> candidateGrades = new HashMap<>();
        candidates.forEach(c -> candidateGrades.put(c, new ArrayList<>()));

        for (Ballot b : ballots) {
            Map<String, Integer> ratings = b.ratedChoices();
            if (ratings == null) continue;
            
            for (String candidate : candidates) {
                Integer score = ratings.get(candidate);
                // Assume score corresponds to Grade ordinal (0=Excellent, 5=Reject)
                // If not rated or invalid, default to REJECT
                if (score != null && score >= 0 && score < Grade.values().length) {
                    candidateGrades.get(candidate).add(Grade.values()[score]);
                } else {
                    candidateGrades.get(candidate).add(Grade.REJECT);
                }
            }
        }

        Map<String, Grade> medians = new HashMap<>();
        for (String candidate : candidates) {
            List<Grade> grades = candidateGrades.get(candidate);
            if (grades.isEmpty()) {
                medians.put(candidate, Grade.REJECT);
                continue;
            }
            Collections.sort(grades); // Sorts based on Enum ordinal (Excellent=0 < Reject=5)? 
            // NOTE: Usually Excellent is "Highest" quality but "Lowest" ordinal depending on declaration.
            // If defined EXCELLENT(0), VERY_GOOD(1)... then 0 is "smaller" (min) but "better".
            
            // Majority Judgment median is typically the value where >=50% are better or equal.
            // Simple approach: standard list median
            Grade median = grades.get((grades.size()) / 2); // Simple middle index
            medians.put(candidate, median);
        }

        // Find "Best" median. Assuming EXCELLENT (ordinal 0) is best, we look for MIN ordinal.
        Grade bestGrade = medians.values().stream().min(Comparator.naturalOrder()).orElse(Grade.REJECT);
        
        return medians.entrySet().stream()
            .filter(e -> e.getValue() == bestGrade)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
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
