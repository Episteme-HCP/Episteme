package org.jscience.politics;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Engine for calculating election winners using various social choice algorithms.
 */
public final class VotingEngine {

    private VotingEngine() {}

    /**
     * Calculates the winner using Plurality (First-past-the-post).
     * 
     * @param ballots List of ballots containing at least one choice.
     * @return List of winners (could be tied).
     */
    public static List<String> calculatePluralityWinner(List<Ballot> ballots) {
        Map<String, Long> tallies = ballots.stream()
            .filter(b -> b.rankedChoices() != null && !b.rankedChoices().isEmpty())
            .map(b -> b.rankedChoices().get(0))
            .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        
        return getHeighestRanked(tallies);
    }

    /**
     * Calculates the winner using the Borda Count method.
     * Each rank is assigned a point value.
     */
    public static List<String> calculateBordaWinner(List<Ballot> ballots, List<String> candidates) {
        Map<String, Long> scores = new HashMap<>();
        candidates.forEach(c -> scores.put(c, 0L));
        int n = candidates.size();

        for (Ballot b : ballots) {
            List<String> ranking = b.rankedChoices();
            if (ranking == null) continue;
            for (int i = 0; i < ranking.size(); i++) {
                String candidate = ranking.get(i);
                if (scores.containsKey(candidate)) {
                    scores.put(candidate, scores.get(candidate) + (long)(n - 1 - i));
                }
            }
        }
        return getHeighestRanked(scores);
    }

    /**
     * Instant Runoff Voting (IRV).
     * Progressively eliminates the candidate with the lowest number of first-choice votes.
     */
    public static List<String> calculateIRVWinner(List<Ballot> ballots, Set<String> candidates) {
        Set<String> activeCandidates = new HashSet<>(candidates);
        
        while (activeCandidates.size() > 1) {
            final Set<String> currentActive = activeCandidates;
            Map<String, Long> firstChoices = ballots.stream()
                .map(b -> b.rankedChoices().stream()
                    .filter(currentActive::contains)
                    .findFirst()
                    .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
            
            if (firstChoices.isEmpty()) break;

            long total = firstChoices.values().stream().mapToLong(L -> L).sum();
            String majorityCandidate = firstChoices.entrySet().stream()
                .filter(e -> e.getValue() > total / 2)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
            
            if (majorityCandidate != null) return List.of(majorityCandidate);

            long minVotes = firstChoices.values().stream().min(Long::compareTo).orElse(0L);
            List<String> candidatesToRemove = firstChoices.entrySet().stream()
                .filter(e -> e.getValue() == minVotes)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
            
            // If all active candidates are tied for last, it's a tie
            if (candidatesToRemove.size() >= activeCandidates.size()) break;

            activeCandidates.removeAll(candidatesToRemove);
        }
        
        return new ArrayList<>(activeCandidates);
    }

    /**
     * Majority Judgment.
     * Voters assign a grade to each candidate. The winner is the one with the highest median grade.
     */
    public enum Grade {
        EXCELLENT, VERY_GOOD, GOOD, ACCEPTABLE, POOR, REJECT
    }

    public static List<String> calculateMajorityJudgmentWinner(List<Ballot> ballots, List<String> candidates) {
        Map<String, List<Grade>> candidateGrades = new HashMap<>();
        candidates.forEach(c -> candidateGrades.put(c, new ArrayList<>()));

        for (Ballot b : ballots) {
            if (b.ratedChoices() == null) continue;
            for (String candidate : candidates) {
                Integer score = b.ratedChoices().get(candidate);
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
            Collections.sort(grades);
            Grade median = grades.get((grades.size() - 1) / 2);
            medians.put(candidate, median);
        }

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
