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

package org.jscience.law;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.jscience.util.identity.Identifiable;
import org.jscience.util.Temporal;
import org.jscience.sociology.Person;

/**
 * Represents a legal contract between parties.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Contract implements Identifiable<String>, Temporal {

    private final String id;
    private final String title;
    private final LocalDate date;
    private final List<Person> parties = new ArrayList<>();
    private final List<String> clauses = new ArrayList<>();
    private boolean valid;

    public Contract(String title, LocalDate date) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.date = date;
        this.valid = true;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public java.time.Instant getTimestamp() {
        return date != null ? java.time.Instant.ofEpochSecond(date.toEpochDay() * 86400) : java.time.Instant.MIN;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void addParty(Person person) {
        parties.add(person);
    }

    public List<Person> getParties() {
        return Collections.unmodifiableList(parties);
    }

    public void addClause(String clause) {
        clauses.add(clause);
    }

    public List<String> getClauses() {
        return Collections.unmodifiableList(clauses);
    }

    @Override
    public String toString() {
        return String.format("Contract: %s (%s)", title, date);
    }
}


