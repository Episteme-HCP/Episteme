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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.jscience.util.Named;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a formal or informal social group of individuals.
 * Provides mechanisms for managing membership, roles, and leadership hierarchies.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class Group implements Identified<String>, Named, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Common categories for social groupings.
     */
    public enum Type {
        FAMILY, COMMUNITY, ORGANIZATION, NATION, TRIBE, TEAM, CLASS, NETWORK
    }

    @Id
    private final String id;
    
    @Attribute
    private final String name;
    
    @Attribute
    private final Type type;
    
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
     * @param type the category of the group
     * @throws NullPointerException if name or type is null
     * @throws IllegalArgumentException if name is empty
     */
    public Group(String name, Type type) {
        this.id = UUID.randomUUID().toString();
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.type = Objects.requireNonNull(type, "Type cannot be null");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the group category.
     * @return the group type
     */
    public Type getType() {
        return type;
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
}
