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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jscience.economics.money.Money;
import org.jscience.sociology.Person;
import org.jscience.util.identity.AbstractIdentifiedEntity;
import org.jscience.util.identity.SimpleIdentification;

/**
 * Represents an organized sports team within a specific discipline.
 * Extends AbstractIdentifiedEntity to support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Team extends AbstractIdentifiedEntity {

    private static final long serialVersionUID = 1L;

    private final Sport sport;
    private final List<Person> members = new ArrayList<>();
    private Money budget;

    /**
     * Creates a new Team.
     * 
     * @param name  the unique name of the team
     * @param sport the sport they compete in
     * @throws NullPointerException if any argument is null
     */
    public Team(String name, Sport sport) {
        super(new SimpleIdentification(name));
        setName(name);
        this.sport = Objects.requireNonNull(sport, "Sport cannot be null");
    }

    public Sport getSport() {
        return sport;
    }

    /**
     * Adds an individual athlete or staff member to the team.
     * @param person the person to add
     */
    public void addMember(Person person) {
        if (person != null) {
            members.add(person);
        }
    }

    /** Returns an unmodifiable view of team members. */
    public List<Person> getMembers() {
        return Collections.unmodifiableList(members);
    }
    
    public Money getBudget() {
        return budget;
    }

    public void setBudget(Money budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", getName(), sport.getName());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team team)) return false;
        return Objects.equals(getName(), team.getName()) && Objects.equals(sport, team.sport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), sport);
    }
}
