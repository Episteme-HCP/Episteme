package org.jscience.politics;

import java.util.List;
import java.util.Map;

/**
 * Represents a voter's choices in an election.
 */
public record Ballot(String voterId, List<String> rankedChoices, Map<String, Integer> ratedChoices) {
    
    /**
     * Creates a simple single-choice ballot.
     */
    public static Ballot singleChoice(String voterId, String choice) {
        return new Ballot(voterId, List.of(choice), null);
    }
}
