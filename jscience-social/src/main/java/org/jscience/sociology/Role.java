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

package org.jscience.sociology;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jscience.biology.Individual;
import org.jscience.util.Temporal;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a social role assumed by an individual within a specific situation.
 * Roles define expected behaviors and relations (e.g., Client/Server, Supervisor, Observer).
 * * @version 2.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Role implements ComprehensiveIdentification, Temporal<Instant> {



    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    /** Primary role categories. */
    public static final RoleKind CLIENT = RoleKind.CLIENT;
    public static final RoleKind SERVER = RoleKind.SERVER;
    public static final RoleKind SUPERVISOR = RoleKind.SUPERVISOR;
    public static final RoleKind OBSERVER = RoleKind.OBSERVER;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Individual individual;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Situation situation;

    @Attribute
    private Instant timestamp;

    @Attribute
    private RoleKind kind;

    /**
     * Creates a new Role for an individual within a specific situation.
     *
     * @param individual the individual taking on the role
     * @param name       the identifying name of the role
     * @param situation  the context for the role
     * @param kind       the categorical classification of the role
     * @throws NullPointerException if any argument is null
     */
    public Role(Individual individual, String name, Situation situation, RoleKind kind) {
        this.id = new SimpleIdentification(individual.getId() + ":" + name);
        setName(name);
        this.individual = Objects.requireNonNull(individual, "Individual cannot be null");
        this.situation = Objects.requireNonNull(situation, "Situation cannot be null");
        this.kind = Objects.requireNonNull(kind, "RoleKind cannot be null");
        this.timestamp = Instant.now();
        this.individual.addRole(this);
    }

    @Deprecated
    public Role(Individual individual, String name, Situation situation, int kind) {
        this(individual, name, situation, mapLegacyKind(kind));
    }

    private static RoleKind mapLegacyKind(int legacyKind) {
        return switch (legacyKind) {
            case 1 -> RoleKind.CLIENT;
            case 2 -> RoleKind.SERVER;
            case 4 -> RoleKind.SUPERVISOR;
            case 8 -> RoleKind.OBSERVER;
            default -> RoleKind.OTHER;
        };
    }

    /**
     * Creates a standalone Role template.
     *
     * @param name the identifying name of the role
     * @param kind the categorical classification
     * @throws NullPointerException if name is null
     */
    public Role(String name, RoleKind kind) {
        this.id = new SimpleIdentification("Template:" + name);
        setName(name);
        this.kind = Objects.requireNonNull(kind, "RoleKind cannot be null");
        this.timestamp = Instant.now();
    }

    @Deprecated
    public Role(String name, int kind) {
        this(name, mapLegacyKind(kind));
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    /**
     * Returns the individual associated with this role.
     * @return the individual
     */
    public Individual getIndividual() {
        return individual;
    }

    /**
     * Returns the situation context for this role.
     * @return the situation
     */
    public Situation getSituation() {
        return situation;
    }

    /**
     * Returns the archetypal kind of this role.
     * @return the kind constant
     */
    public RoleKind getKind() {
        return kind;
    }

    public void setKind(RoleKind kind) {
        this.kind = Objects.requireNonNull(kind);
    }

    @Override
    public Instant getWhen() {
        return timestamp;
    }

    @Deprecated
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for this role.
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getName() + " (" + (individual != null ? individual.getName() : "None") + ")";
    }
}
