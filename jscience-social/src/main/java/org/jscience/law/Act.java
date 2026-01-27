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

import org.jscience.sociology.Human;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;
import org.jscience.history.time.TimeCoordinate;


import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A class representing a legal act or certificate (contract, deed, etc.) 
 * which defines social and legal relations such as ownership, citizenship, 
 * marriage, or nationality.
 * 
 * <p>Modernized to implement ComprehensiveIdentification and use ActKind extensible enum.</p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Act implements ComprehensiveIdentification {
    
    private static final long serialVersionUID = 3L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private ActKind kind;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private TimeCoordinate date;

    @Attribute
    private String object; // "you ... have the right to ... under these circumstances..."

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Human> subjects;

    /**
     * Creates a new Act object.
     *
     * @param id the unique identification of this act
     * @param kind the categorical classification of the act
     * @param date the date when the act was established or signed
     * @param object the primary content or legal consequences of the act
     * @param subjects the set of individuals concerned by this act
     * @throws NullPointerException if any required argument is null
     */
    public Act(Identification id, ActKind kind, TimeCoordinate date,
        String object, Set<Human> subjects) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.kind = Objects.requireNonNull(kind, "ActKind cannot be null");
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.object = Objects.requireNonNull(object, "Object cannot be null");
        this.subjects = Objects.requireNonNull(subjects, "Subjects cannot be null");
        
        setName(kind.name() + " (" + id + ")");
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public ActKind getKind() {
        return kind;
    }

    public void setKind(ActKind kind) {
        this.kind = Objects.requireNonNull(kind);
    }

    public TimeCoordinate getDate() {
        return date;
    }

    public void setDate(TimeCoordinate date) {
        this.date = Objects.requireNonNull(date);
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = Objects.requireNonNull(object);
    }

    public Set<Human> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Human> subjects) {
        this.subjects = Objects.requireNonNull(subjects);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Act that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getName();
    }
}
