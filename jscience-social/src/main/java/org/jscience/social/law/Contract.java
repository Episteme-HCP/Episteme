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

package org.jscience.social.law;

import org.jscience.social.economics.Organization;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;
import org.jscience.social.sociology.Person;
import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.social.history.time.FuzzyTimePoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDate;

/**
 * A class representing a legal contract or agreement between organizations or individuals.
 * It describes the terms, clauses, and identifiers of the deal.
 * Modernized to implement ComprehensiveIdentification and support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Contract implements ComprehensiveIdentification {

    private static final long serialVersionUID = 3L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Organization firstParty;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Organization secondParty;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private TimeCoordinate signingDate;

    @Attribute
    private final List<String> contents = new ArrayList<>();

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Person> parties = new HashSet<>();

    /**
     * Minimal constructor.
     */
    public Contract(String title, TimeCoordinate date) {
        this.id = new SimpleIdentification("Contract:" + UUID.randomUUID());
        setName(Objects.requireNonNull(title, "Title cannot be null"));
        this.signingDate = Objects.requireNonNull(date, "Signing date cannot be null");
    }

    /**
     * Helper constructor for LocalDate.
     */
    public Contract(String title, LocalDate date) {
        this(title, FuzzyTimePoint.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
    }

    /**
     * Creates a new Contract with parties and contents.
     */
    public Contract(Organization firstParty, Organization secondParty,
                    String title, TimeCoordinate date, List<String> contents) {
        this(title, date);
        this.firstParty = firstParty;
        this.secondParty = secondParty;
        if (contents != null) {
            this.contents.addAll(contents);
        }
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public Organization getFirstParty() {
        return firstParty;
    }

    public void setFirstParty(Organization firstParty) {
        this.firstParty = firstParty;
    }

    public Organization getSecondParty() {
        return secondParty;
    }

    public void setSecondParty(Organization secondParty) {
        this.secondParty = secondParty;
    }

    public TimeCoordinate getSigningDate() {
        return signingDate;
    }

    public void setSigningDate(TimeCoordinate signingDate) {
        this.signingDate = signingDate;
    }

    public List<String> getContents() {
        return Collections.unmodifiableList(contents);
    }

    public void addClause(String clause) {
        if (clause != null) {
            contents.add(clause);
        }
    }

    public void addParty(Person person) {
        if (person != null) {
            parties.add(person);
        }
    }

    public Set<Person> getParties() {
        return Collections.unmodifiableSet(parties);
    }

    public boolean isValid() {
        return (!parties.isEmpty() || (firstParty != null && secondParty != null)) && !contents.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contract that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getName() + " (" + signingDate + ")";
    }
}

