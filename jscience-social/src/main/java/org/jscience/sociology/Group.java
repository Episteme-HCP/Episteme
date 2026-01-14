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

import java.util.*;

/**
 * Represents a social group of persons.
 * <p>
 * Provides group-level analysis for sociology: size, roles, hierarchy.
 * Can integrate with biology Population for human groups.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Group {

    public enum Type {
        FAMILY, COMMUNITY, ORGANIZATION, NATION, TRIBE, TEAM, CLASS, NETWORK
    }

    private final String name;
    private final Type type;
    private final List<Person> members = new ArrayList<>();
    private Person leader;
    private final Map<Person, String> roles = new HashMap<>();

    public Group(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Person getLeader() {
        return leader;
    }

    public void setLeader(Person leader) {
        this.leader = leader;
        if (!members.contains(leader)) {
            members.add(leader);
        }
    }

    public void addMember(Person person) {
        if (!members.contains(person)) {
            members.add(person);
        }
    }

    public void addMember(Person person, String role) {
        addMember(person);
        roles.put(person, role);
    }

    public void removeMember(Person person) {
        members.remove(person);
        roles.remove(person);
        if (person.equals(leader)) {
            leader = null;
        }
    }

    public List<Person> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public int size() {
        return members.size();
    }

    public String getRole(Person person) {
        return roles.get(person);
    }

    public List<Person> getMembersByRole(String role) {
        return members.stream()
                .filter(p -> role.equals(roles.get(p)))
                .toList();
    }

    // ========== Statistics ==========

    public double getAverageAge() {
        return members.stream()
                .mapToInt(Person::getAge)
                .average()
                .orElse(0);
    }

    public Map<Person.Gender, Long> getGenderDistribution() {
        Map<Person.Gender, Long> dist = new EnumMap<>(Person.Gender.class);
        for (Person.Gender g : Person.Gender.values()) {
            dist.put(g, members.stream().filter(p -> p.getGender() == g).count());
        }
        return dist;
    }

    public Set<String> getNationalities() {
        Set<String> nationalities = new HashSet<>();
        for (Person p : members) {
            if (p.getNationality() != null) {
                nationalities.add(p.getNationality());
            }
        }
        return nationalities;
    }

    @Override
    public String toString() {
        return String.format("Group '%s' (%s): %d members", name, type, size());
    }
}


