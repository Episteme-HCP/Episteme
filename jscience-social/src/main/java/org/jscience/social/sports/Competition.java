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

package org.jscience.social.sports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jscience.social.economics.money.Money;

/**
 * Represents a sports competition, such as a tournament, league, or championship.
 * Aggregates matches and manages prize distribution.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Competition implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<Match> matches = new ArrayList<>();
    private final List<Money> prizes = new ArrayList<>();
    private String description;

    /**
     * Initializes a new Competition.
     * @param name the name (e.g., "World Cup 2026")
     * @throws NullPointerException if name is null
     */
    public Competition(String name) {
        this.name = Objects.requireNonNull(name, "Competition name cannot be null");
    }

    public String getName() {
        return name;
    }

    /**
     * Adds a match to the competition registry.
     * @param match the match to add
     */
    public void addMatch(Match match) {
        if (match != null) {
            matches.add(match);
            match.setCompetition(this);
        }
    }

    /** Returns an unmodifiable view of all matches in this competition. */
    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches);
    }

    /** Registers a prize for winners or participants. */
    public void addPrize(Money prize) {
        if (prize != null) {
            prizes.add(prize);
        }
    }

    /** Returns an unmodifiable list of prizes. */
    public List<Money> getPrizes() {
        return Collections.unmodifiableList(prizes);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}

