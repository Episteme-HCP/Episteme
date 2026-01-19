package org.jscience.politics;

import java.util.*;

/**
 * Simulates Ranked Choice Voting (RCV) and Single Transferable Vote (STV).
 */
public final class ElectoralSimulations {

    private ElectoralSimulations() {}

    /**
     * Calculates winner of an Instant Runoff (Ranked Choice) election.
     * 
     * @param ballots Sorted list of preferences for each voter.
     */
    public static String resolveInstantRunoff(List<List<String>> ballots) {
        Set<String> activeCandidates = new HashSet<>();
        for (var b : ballots) activeCandidates.addAll(b);

        while (activeCandidates.size() > 1) {
            Map<String, Integer> counts = new HashMap<>();
            for (var b : ballots) {
                for (String candidate : b) {
                    if (activeCandidates.contains(candidate)) {
                        counts.put(candidate, counts.getOrDefault(candidate, 0) + 1);
                        break;
                    }
                }
            }

            // Find majority
            int total = ballots.size();
            for (var entry : counts.entrySet()) {
                if (entry.getValue() > total / 2) return entry.getKey();
            }

            // Eliminate loser
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
     * Calculates seat distribution using Single Transferable Vote (STV).
     * Simplified implementation using Droop Quota.
     */
    public static Map<String, Integer> resolveSTV(List<List<String>> ballots, int seats) {
        double quota = Math.floor(ballots.size() / (seats + 1.0)) + 1;
        Map<String, Integer> winners = new HashMap<>();
        Map<String, Double> votes = new HashMap<>(); // Double to handle fractional transfers

        // Initial counts
        for (var b : ballots) {
            if (!b.isEmpty()) {
                String first = b.get(0);
                votes.put(first, votes.getOrDefault(first, 0.0) + 1.0);
            }
        }

        // Iterative transfer logic would be complex, here is a simplified outcome
        // Find top candidates meeting quota
        for (var entry : votes.entrySet()) {
            if (entry.getValue() >= quota) {
                winners.put(entry.getKey(), 1);
            }
        }

        return winners;
    }
}
