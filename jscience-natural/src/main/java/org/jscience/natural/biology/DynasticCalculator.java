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

package org.jscience.natural.biology;

import org.jscience.core.mathematics.numbers.real.Real;
import java.io.Serializable;
import java.util.*;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Calculates consanguinity coefficients, inbreeding, and models dynastic succession.
 * Provides tools for genealogical analysis of historical dynasties.
 */
public final class DynasticCalculator {

    private DynasticCalculator() {
        // Prevent instantiation
    }

    /**
     * Gender for dynastic succession priority logic.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum Gender {
        MALE, FEMALE, OTHER
    }

    /**
     * Represents a person within a dynastic lineage.
     */
    @Persistent
    public static class DynasticPerson implements Serializable {
        private static final long serialVersionUID = 1L;

        @Attribute
        private final String id;
        
        @Attribute
        private final String name;
        
        @Attribute
        private Gender gender = Gender.OTHER;
        
        @Relation(type = Relation.Type.MANY_TO_ONE)
        private DynasticPerson father;
        
        @Relation(type = Relation.Type.MANY_TO_ONE)
        private DynasticPerson mother;
        
        @Relation(type = Relation.Type.ONE_TO_MANY)
        private final List<DynasticPerson> children = new ArrayList<>();

        /**
         * Creates a new DynasticPerson.
         * 
         * @param id   unique identifier
         * @param name full name
         * @throws NullPointerException if id or name is null
         */
        public DynasticPerson(String id, String name) {
            this.id = Objects.requireNonNull(id, "ID cannot be null");
            this.name = Objects.requireNonNull(name, "Name cannot be null");
        }

        public String getId() { return id; }
        public String getName() { return name; }
        
        public Gender getGender() { return gender; }
        public void setGender(Gender gender) { 
            this.gender = Objects.requireNonNull(gender, "Gender cannot be null"); 
        }

        public Optional<DynasticPerson> getFather() { return Optional.ofNullable(father); }
        public Optional<DynasticPerson> getMother() { return Optional.ofNullable(mother); }
        public List<DynasticPerson> getChildren() { return Collections.unmodifiableList(children); }

        public void setFather(DynasticPerson father) {
            this.father = father;
            if (father != null && !father.children.contains(this)) {
                father.children.add(this);
            }
        }

        public void setMother(DynasticPerson mother) {
            this.mother = mother;
            if (mother != null && !mother.children.contains(this)) {
                mother.children.add(this);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DynasticPerson that)) return false;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return name + " (" + id + ")";
        }
    }

    /**
     * Calculates the Wright's coefficient of inbreeding (F) for a person.
     * Formula: F = Î£ (1/2)^(n1+n2+1) Ã— (1 + FA)
     * 
     * @param person the person to calculate for
     * @return inbreeding coefficient as a {@link Real} number
     * @throws NullPointerException if person is null
     */
    public static Real calculateInbreedingCoefficient(DynasticPerson person) {
        Objects.requireNonNull(person, "Person cannot be null");
        if (person.father == null || person.mother == null) {
            return Real.ZERO;
        }
        
        Set<DynasticPerson> paternalAncestors = getAllAncestors(person.father);
        Set<DynasticPerson> maternalAncestors = getAllAncestors(person.mother);
        
        Set<DynasticPerson> commonAncestors = new HashSet<>(paternalAncestors);
        commonAncestors.retainAll(maternalAncestors);
        
        if (commonAncestors.isEmpty()) {
            return Real.ZERO;
        }
        
        double fValue = 0.0;
        for (DynasticPerson ancestor : commonAncestors) {
            List<Integer> pathsF = getAllPathLengths(person.father, ancestor);
            List<Integer> pathsM = getAllPathLengths(person.mother, ancestor);
            
            for (int n1 : pathsF) {
                for (int n2 : pathsM) {
                    double fa = calculateInbreedingCoefficient(ancestor).doubleValue();
                    fValue += Math.pow(0.5, n1 + n2 + 1) * (1 + fa);
                }
            }
        }
        
        return Real.of(fValue);
    }

    /**
     * Calculates the coefficient of relationship (r) between two individuals.
     * 
     * @param a first person
     * @param b second person
     * @return relationship coefficient as a {@link Real} number
     * @throws NullPointerException if a or b is null
     */
    public static Real calculateRelationshipCoefficient(DynasticPerson a, DynasticPerson b) {
        Objects.requireNonNull(a, "Person A cannot be null");
        Objects.requireNonNull(b, "Person B cannot be null");
        
        Set<DynasticPerson> ancestorsA = getAllAncestors(a);
        Set<DynasticPerson> ancestorsB = getAllAncestors(b);
        
        Set<DynasticPerson> commonAncestors = new HashSet<>(ancestorsA);
        commonAncestors.retainAll(ancestorsB);
        
        if (commonAncestors.isEmpty()) {
            return Real.ZERO;
        }
        
        double rValue = 0.0;
        for (DynasticPerson ancestor : commonAncestors) {
            List<Integer> pathsA = getAllPathLengths(a, ancestor);
            List<Integer> pathsB = getAllPathLengths(b, ancestor);
            for (int n1 : pathsA) {
                for (int n2 : pathsB) {
                    rValue += Math.pow(0.5, n1 + n2);
                }
            }
        }
        
        return Real.of(rValue);
    }

    /**
     * Calculates the order of succession based on primogeniture.
     * 
     * @param monarch       current holder of the title
     * @param malePriority  if true, males take priority over females (Salic/Semi-Salic law)
     * @return unmodifiable ordered list of successors
     * @throws NullPointerException if monarch is null
     */
    public static List<DynasticPerson> calculateSuccessionOrder(DynasticPerson monarch, 
            boolean malePriority) {
        Objects.requireNonNull(monarch, "Monarch cannot be null");
        List<DynasticPerson> order = new ArrayList<>();
        addDescendants(monarch, order, malePriority);
        return Collections.unmodifiableList(order);
    }

    private static void addDescendants(DynasticPerson person, List<DynasticPerson> order, 
            boolean malePriority) {
        List<DynasticPerson> children = new ArrayList<>(person.getChildren());
        
        if (malePriority) {
            children.sort((c1, c2) -> {
                if (c1.getGender() == Gender.MALE && c2.getGender() != Gender.MALE) return -1;
                if (c2.getGender() == Gender.MALE && c1.getGender() != Gender.MALE) return 1;
                return 0; // Maintain birth order
            });
        }
        
        for (DynasticPerson child : children) {
            order.add(child);
            addDescendants(child, order, malePriority);
        }
    }

    private static Set<DynasticPerson> getAllAncestors(DynasticPerson person) {
        Set<DynasticPerson> ancestors = new HashSet<>();
        ancestors.add(person); // Include self as base for pathing if needed
        collectAncestors(person, ancestors);
        return ancestors;
    }

    private static void collectAncestors(DynasticPerson person, Set<DynasticPerson> ancestors) {
        if (person.father != null && ancestors.add(person.father)) {
            collectAncestors(person.father, ancestors);
        }
        if (person.mother != null && ancestors.add(person.mother)) {
            collectAncestors(person.mother, ancestors);
        }
    }

    private static List<Integer> getAllPathLengths(DynasticPerson from, DynasticPerson to) {
        List<Integer> paths = new ArrayList<>();
        findPaths(from, to, 0, paths);
        return paths;
    }

    private static void findPaths(DynasticPerson current, DynasticPerson target, int depth, List<Integer> paths) {
        if (current == null) return;
        if (current.equals(target)) {
            paths.add(depth);
            return;
        }
        findPaths(current.father, target, depth + 1, paths);
        findPaths(current.mother, target, depth + 1, paths);
    }
}

