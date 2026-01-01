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

package org.jscience.politics.military;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.jscience.geography.Place;
import org.jscience.politics.Country;
import org.jscience.util.identity.Identifiable;
import org.jscience.geography.Locatable;
import org.jscience.util.Temporal;

/**
 * Represents a military conflict, war, or battle.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Conflict implements Identifiable<String>, Locatable, Temporal {

    private final String id;
    private final String name;
    private final Place location;
    private final LocalDate startDate;
    private LocalDate endDate;
    private final Set<Country> belligerents = new HashSet<>();

    public Conflict(String name, Place location, LocalDate startDate) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.location = location;
        this.startDate = startDate;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public Place getLocation() {
        return location;
    }

    @Override
    public java.time.Instant getTimestamp() {
        return java.time.Instant.from(startDate.atStartOfDay(java.time.ZoneId.of("UTC")));
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return endDate == null;
    }

    public void addBelligerent(Country country) {
        belligerents.add(country);
    }

    public Set<Country> getBelligerents() {
        return Collections.unmodifiableSet(belligerents);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, startDate.getYear());
    }
}


