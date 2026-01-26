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

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import org.jscience.biology.Individual;
import org.jscience.util.Commented;
import org.jscience.util.Named;
import org.jscience.util.Temporal;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a social role assumed by an individual within a specific situation.
 * Roles define expected behaviors and relations (e.g., Client/Server, Supervisor, Observer).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
@Persistent
public class Role implements Named, Identified<Identification>, Commented, Temporal<Instant>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Identification identification;

    /** Primary role of receiving a service (e.g., student, customer, patient). */
    public final static int CLIENT = 1;

    /** Primary role of providing a service (e.g., teacher, waiter, doctor). */
    public final static int SERVER = 2;

    /** Role of monitoring or managing others (e.g., administrator, manager). */
    public final static int SUPERVISOR = 4;

    /** Role of passive observation (e.g., narrator, audience). */
    public final static int OBSERVER = 8;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Individual individual;

    @Attribute
    private String name;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Situation situation;

    @Attribute
    private String comments;

    private final java.util.Map<String, Object> traits = new java.util.HashMap<>();

    @Attribute
    private Instant timestamp;

    @Attribute
    private int kind;

    /**
     * Creates a new Role for an individual within a specific situation.
     *
     * @param individual the individual taking on the role
     * @param name       the identifying name of the role
     * @param situation  the context for the role
     * @param kind       the categorical classification of the role
     * @throws NullPointerException if any argument is null
     */
    public Role(Individual individual, String name, Situation situation, int kind) {
        this.individual = Objects.requireNonNull(individual, "Individual cannot be null");
        this.name = Objects.requireNonNull(name, "Role name cannot be null");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be empty");
        }
        this.situation = Objects.requireNonNull(situation, "Situation cannot be null");
        this.kind = kind;
        this.identification = new SimpleIdentification(individual.getId() + ":" + name);
        this.timestamp = Instant.now();
        this.individual.addRole(this);
    }

    /**
     * Creates a standalone Role template.
     *
     * @param name the identifying name of the role
     * @param kind the categorical classification
     * @throws NullPointerException if name is null
     */
    public Role(String name, int kind) {
        this.name = Objects.requireNonNull(name, "Role name cannot be null");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be empty");
        }
        this.kind = kind;
        this.identification = new SimpleIdentification("Template:" + name);
        this.timestamp = Instant.now();
    }

    @Override
    public String getName() {
        return name;
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
    public int getKind() {
        return kind;
    }

    @Override
    public Identification getId() {
        return identification;
    }

    @Override
    public String getComments() {
        return comments;
    }

    /**
     * Sets the comments for this role.
     * @param comments the comments to set
     */
    @Override
    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public java.util.Map<String, Object> getTraits() {
        return traits;
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
    public String toString() {
        return name + " (" + (individual != null ? individual.getName() : "None") + ")";
    }
}
