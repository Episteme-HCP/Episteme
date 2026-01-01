/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.jscience.util.identity.Identifiable;
import org.jscience.util.Temporal;

/**
 * Represents a political election.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Election implements Identifiable<String>, Temporal {

    private final String id;
    private final String title;
    private final LocalDate date;
    private final Country country;
    private final Map<String, Integer> results = new HashMap<>(); // Candidate/Party -> Votes

    public Election(String title, Country country, LocalDate date) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.country = country;
        this.date = date;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public java.time.Instant getTimestamp() {
        return java.time.Instant.from(date.atStartOfDay(java.time.ZoneId.of("UTC")));
    }

    public String getTitle() {
        return title;
    }

    public Country getCountry() {
        return country;
    }

    public void addVote(String candidate, int count) {
        results.merge(candidate, count, (a, b) -> a + b);
    }

    public Map<String, Integer> getResults() {
        return results;
    }

    public String getWinner() {
        return results.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}


