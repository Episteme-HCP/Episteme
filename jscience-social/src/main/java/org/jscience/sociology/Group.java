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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a formal or informal social group of individuals.
 * Provides mechanisms for managing membership, roles, and leadership hierarchies.
 * Modernized to implement ComprehensiveIdentification and use GroupKind.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
@Persistent
public class Group implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    /**
     * Common categories for social groupings.
     */
    @Deprecated
    public enum Type {
        @Deprecated FAMILY,
        @Deprecated COMMUNITY,
        @Deprecated ORGANIZATION,
        @Deprecated NATION,
        @Deprecated TRIBE,
        @Deprecated TEAM,
        @Deprecated CLASS,
        @Deprecated NETWORK;

        @Deprecated
        public GroupKind toGroupKind() {
            try {
                return GroupKind.valueOf(this.name());
            } catch (Exception e) {
                return GroupKind.OTHER;
            }
        }
    }

    @Id
    private final Identification id;
    
    @Attribute
    private final Map<String, Object> traits = new HashMap<>();
    
    @Attribute
    private GroupKind kind;
    
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Person> members = new ArrayList<>();
    
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Person leader;
    
    @Attribute
    private final Map<Person, String> internalRoles = new HashMap<>();

    /**
     * Creates a new social group.
     *
     * @param name the name of the group
     * @param kind the category of the group
     * @throws NullPointerException if name or kind is null
     * @throws IllegalArgumentException if name is empty
     */
    public Group(String name, GroupKind kind) {
        this.id = new org.jscience.util.identity.UUIDIdentification(UUID.randomUUID());
        setName(Objects.requireNonNull(name, "Name cannot be null"));
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.kind = Objects.requireNonNull(kind, "Kind cannot be null");
    }

    @Deprecated
    public Group(String name, Type type) {
        this(name, type.toGroupKind());
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
     * Returns the group category.
     * @return the group kind
     */
    public GroupKind getKind() {
        return kind;
    }

    public void setKind(GroupKind kind) {
        this.kind = Objects.requireNonNull(kind);
    }

    @Deprecated
    public Type getType() {
        try {
            return Type.valueOf(kind.name());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns the current leader of the group, if any.
     * @return the leader person, or null
     */
    public Person getLeader() {
        return leader;
    }

    /**
     * Sets the leader of the group. If the person is not a member, they are added.
     * @param leader the person to set as leader
     */
    public void setLeader(Person leader) {
        this.leader = leader;
        if (leader != null && !members.contains(leader)) {
            members.add(leader);
        }
    }

    /**
     * Adds a new member to the group.
     * @param person the person to add
     * @throws NullPointerException if person is null
     */
    public void addMember(Person person) {
        Objects.requireNonNull(person, "Member cannot be null");
        if (!members.contains(person)) {
            members.add(person);
        }
    }

    /**
     * Removes a member from the group.
     * @param person the person to remove
     */
    public void removeMember(Person person) {
        members.remove(person);
        internalRoles.remove(person);
        if (leader == person) {
            leader = null;
        }
    }

    /**
     * Assigns a specific role title to a member.
     * @param person the member
     * @param role   the role description
     */
    public void assignRole(Person person, String role) {
        if (members.contains(person)) {
            internalRoles.put(person, role);
        }
    }

    /**
     * Returns the role assigned to a specific member.
     * @param person the member to check
     * @return the role title, or null if none assigned
     */
    public String getRole(Person person) {
        return internalRoles.get(person);
    }

    /**
     * Returns an unmodifiable view of the group members.
     * @return list of members
     */
    public List<Person> getMembers() {
        return Collections.unmodifiableList(members);
    }

    /**
     * Returns the current number of members in the group.
     * @return group size
     */
    public int size() {
        return members.size();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
