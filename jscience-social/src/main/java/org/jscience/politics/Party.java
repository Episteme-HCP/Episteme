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

package org.jscience.politics;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a political party, characterized by its ideology, leadership, and organization.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Party implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    private final String name;
    @Attribute
    private String abbreviation;
    @Attribute
    private Ideology ideology;
    @Attribute
    private LocalDate foundedDate;
    @Attribute
    private String leader;
    @Attribute
    private String headquarters;
    @Attribute
    private long memberCount;
    @Attribute
    private String color;

    /**
     * Creates a new Political Party.
     * @param name     the official name of the party
     * @param ideology the core ideology
     * @throws NullPointerException if name or ideology is null
     */
    public Party(String name, Ideology ideology) {
        this.name = Objects.requireNonNull(name, "Party name cannot be null");
        this.ideology = Objects.requireNonNull(ideology, "Ideology cannot be null");
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public Ideology getIdeology() {
        return ideology;
    }

    public LocalDate getFoundedDate() {
        return foundedDate;
    }

    public String getLeader() {
        return leader;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public long getMemberCount() {
        return memberCount;
    }

    public String getColor() {
        return color;
    }

    // Setters
    public void setAbbreviation(String abbr) {
        this.abbreviation = abbr;
    }

    public void setIdeology(Ideology ideology) {
        this.ideology = ideology;
    }

    public void setFoundedDate(LocalDate date) {
        this.foundedDate = date;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public void setHeadquarters(String hq) {
        this.headquarters = hq;
    }

    public void setMemberCount(long count) {
        this.memberCount = count;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Party party = (Party) o;
        return Objects.equals(name, party.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", name, abbreviation != null ? abbreviation : "?", ideology);
    }
}
