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

package org.jscience.sports;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A persistent record container for sporting events, competitions, and match results.
 * Acts as a historical archive for sports data analysis.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Almanac implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Set<Object> records = new HashSet<>();

    /**
     * Adds a match result to the almanac.
     * @param match the match to record
     */
    public void addResult(Match match) {
        if (match != null) {
            records.add(match);
        }
    }

    /**
     * Adds a competition result to the almanac.
     * @param competition the competition to record
     */
    public void addResult(Competition competition) {
        if (competition != null) {
            records.add(competition);
        }
    }

    /**
     * Removes a specific record from the almanac.
     * @param result the record object (Match or Competition)
     */
    public void removeResult(Object result) {
        records.remove(result);
    }

    /**
     * Retrieves all recorded matches.
     * @return an unmodifiable set of matches
     */
    public Set<Match> getMatches() {
        return records.stream()
                .filter(r -> r instanceof Match)
                .map(r -> (Match) r)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Retrieves all recorded competitions.
     * @return an unmodifiable set of competitions
     */
    public Set<Competition> getCompetitions() {
        return records.stream()
                .filter(r -> r instanceof Competition)
                .map(r -> (Competition) r)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Retrieves all records stored in the almanac.
     * @return an unmodifiable set of all records
     */
    public Set<Object> getAllRecords() {
        return Collections.unmodifiableSet(records);
    }
}
