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

package org.jscience.social.sociology;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import org.jscience.natural.biology.Individual;
import org.jscience.natural.earth.Place;
import org.jscience.core.util.Positioned;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a social interaction context where individuals assume specific roles.
 * Situations often occur at dedicated physical locations and involve common activities or conflicts.
 * Modernized to implement ComprehensiveIdentification and use RoleKind.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Situation implements ComprehensiveIdentification, Positioned<Place> {



    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Role> roles = new HashSet<>();

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
        this.id = new SimpleIdentification(name + ":" + System.currentTimeMillis());
        setName(Objects.requireNonNull(name, "Name cannot be null"));
        setComments(Objects.requireNonNull(comments, "Comments cannot be null"));
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
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
     * @throws NullPointerException if individual, roleName or kind is null
     */
    public Role addParticipant(Individual individual, String roleName, RoleKind kind) {
        Role role = new Role(individual, roleName, this, kind);
        roles.add(role);
        return role;
    }

    /**
     * Legacy method for integer-based role kinds.
     */
    @Deprecated
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

    @Deprecated
    public Identification getIdentification() {
        return id;
    }

    @Override
    public String toString() {
        return getName() + " at " + (place != null ? place.getName() : "Unknown location");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Situation that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

