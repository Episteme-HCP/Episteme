package org.jscience.politics;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Algorithms for determining election winners.
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
