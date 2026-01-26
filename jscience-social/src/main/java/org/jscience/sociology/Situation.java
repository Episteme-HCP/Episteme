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
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.biology.Individual;
import org.jscience.earth.Place;
import org.jscience.util.Commented;
import org.jscience.util.Named;
import org.jscience.util.Positioned;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a social interaction context where individuals assume specific roles.
 * Situations often occur at dedicated physical locations and involve common activities or conflicts.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class Situation implements Named, Commented, Positioned<Place>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Identification identification;

    @Attribute
    private String name;

    @Attribute
    private String comments;

    private final java.util.Map<String, Object> traits = new java.util.HashMap<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Role> roles;

    @Attribute
    private Place place;

    /**
     * Creates a new social situation.
     *
     * @param name     the identifying name of the situation
     * @param comments descriptive details about the situation
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if name is empty
     */
    public Situation(String name, String comments) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
        this.comments = Objects.requireNonNull(comments, "Comments cannot be null");
        
        this.identification = new SimpleIdentification(name + ":" + System.currentTimeMillis());
        this.roles = new HashSet<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getComments() {
        return comments;
    }

    @Override
    public void setComments(String comments) {
        this.comments = Objects.requireNonNull(comments, "Comments cannot be null");
    }

    @Override
    public java.util.Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public Place getPosition() {
        return place;
    }

    public void setPosition(Place place) {
        this.place = place;
    }

    /**
     * Returns an unmodifiable set of roles currently participating in this situation.
     * @return the participants' roles
     */
    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    /**
     * Adds an individual in a specific role to this situation.
     *
     * @param individual the individual participating
     * @param roleName   the name of the role assumed
     * @param kind       the archetypal classification of the role
     * @return the created role instance
     * @throws NullPointerException if individual or roleName is null
     */
    public Role addParticipant(Individual individual, String roleName, int kind) {
        Role role = new Role(individual, roleName, this, kind);
        roles.add(role);
        return role;
    }

    /**
     * Directly adds a role to this situation.
     * @param role the role to add
     */
    public void addRole(Role role) {
        roles.add(Objects.requireNonNull(role, "Role cannot be null"));
    }

    /**
     * Removes a role participant from the situation.
     * @param role the role to remove
     * @throws NullPointerException if role is null
     */
    public void removeParticipant(Role role) {
        roles.remove(Objects.requireNonNull(role, "Role cannot be null"));
    }

    /**
     * Returns the persistent identification for this situation.
     * @return the identification
     */
    public Identification getIdentification() {
        return identification;
    }

    @Override
    public String toString() {
        return name + " at " + (place != null ? place.getName() : "Unknown location");
    }
}
