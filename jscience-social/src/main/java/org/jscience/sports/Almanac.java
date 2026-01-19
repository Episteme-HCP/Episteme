package org.jscience.sports;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A record of past competitions and match results.
 */
public class Almanac {

    private final Set<Object> records = new HashSet<>();

    public void addResult(Match match) {
        if (match != null) {
            records.add(match);
        }
    }

    public void addResult(Competition competition) {
        if (competition != null) {
            records.add(competition);
        }
    }

    public void removeResult(Object result) {
        records.remove(result);
    }

    public Set<Match> getMatches() {
        return records.stream()
                .filter(r -> r instanceof Match)
                .map(r -> (Match) r)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Set<Competition> getCompetitions() {
        return records.stream()
                .filter(r -> r instanceof Competition)
                .map(r -> (Competition) r)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Set<Object> getAllRecords() {
        return Collections.unmodifiableSet(records);
    }
}
