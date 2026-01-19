package org.jscience.history;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Calculates consanguinity coefficients and models dynastic succession.
 */
public final class DynasticCalculator {

    private DynasticCalculator() {}

    /**
     * Represents a person in a dynasty.
     */
    public static class DynasticPerson {
        private final String id;
        private final String name;
        private DynasticPerson father;
        private DynasticPerson mother;
        private final List<DynasticPerson> children = new ArrayList<>();

        public DynasticPerson(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public Optional<DynasticPerson> getFather() { return Optional.ofNullable(father); }
        public Optional<DynasticPerson> getMother() { return Optional.ofNullable(mother); }
        public List<DynasticPerson> getChildren() { return Collections.unmodifiableList(children); }

        public void setFather(DynasticPerson father) {
            this.father = father;
            if (father != null) father.children.add(this);
        }

        public void setMother(DynasticPerson mother) {
            this.mother = mother;
            if (mother != null) mother.children.add(this);
        }
    }

    /**
     * Calculates the coefficient of inbreeding (F) for a person.
     * F = Σ (1/2)^(n1+n2+1) × (1 + FA)
     * where n1, n2 are path lengths to common ancestor A with inbreeding FA.
     */
    public static Real calculateInbreedingCoefficient(DynasticPerson person) {
        if (person.father == null || person.mother == null) {
            return Real.ZERO;
        }
        
        // Find common ancestors of parents
        Set<DynasticPerson> paternalAncestors = getAllAncestors(person.father);
        Set<DynasticPerson> maternalAncestors = getAllAncestors(person.mother);
        
        Set<DynasticPerson> commonAncestors = new HashSet<>(paternalAncestors);
        commonAncestors.retainAll(maternalAncestors);
        
        if (commonAncestors.isEmpty()) {
            return Real.ZERO;
        }
        
        double F = 0.0;
        for (DynasticPerson ancestor : commonAncestors) {
            int n1 = pathLength(person.father, ancestor);
            int n2 = pathLength(person.mother, ancestor);
            
            if (n1 >= 0 && n2 >= 0) {
                double Fa = calculateInbreedingCoefficient(ancestor).doubleValue();
                F += Math.pow(0.5, n1 + n2 + 1) * (1 + Fa);
            }
        }
        
        return Real.of(F);
    }

    /**
     * Calculates coefficient of relationship between two people.
     * r = 2 × F(hypothetical child) / sqrt((1+Fa)(1+Fb))
     * Simplified: r = Σ (1/2)^L for each path through common ancestors.
     */
    public static Real calculateRelationshipCoefficient(DynasticPerson a, DynasticPerson b) {
        Set<DynasticPerson> ancestorsA = getAllAncestors(a);
        Set<DynasticPerson> ancestorsB = getAllAncestors(b);
        
        Set<DynasticPerson> commonAncestors = new HashSet<>(ancestorsA);
        commonAncestors.retainAll(ancestorsB);
        
        if (commonAncestors.isEmpty()) {
            return Real.ZERO;
        }
        
        double r = 0.0;
        for (DynasticPerson ancestor : commonAncestors) {
            int pathA = pathLength(a, ancestor);
            int pathB = pathLength(b, ancestor);
            if (pathA >= 0 && pathB >= 0) {
                r += Math.pow(0.5, pathA + pathB);
            }
        }
        
        return Real.of(r);
    }

    /**
     * Determines succession order (simplified primogeniture).
     */
    public static List<DynasticPerson> calculateSuccessionOrder(DynasticPerson monarch, 
            boolean malePriority) {
        List<DynasticPerson> order = new ArrayList<>();
        addDescendants(monarch, order, malePriority);
        return order;
    }

    private static void addDescendants(DynasticPerson person, List<DynasticPerson> order, 
            boolean malePriority) {
        List<DynasticPerson> children = new ArrayList<>(person.getChildren());
        
        if (malePriority) {
            // Sort to put males first (simplified - would need gender field)
            // For now, just add in order
        }
        
        for (DynasticPerson child : children) {
            order.add(child);
            addDescendants(child, order, malePriority);
        }
    }

    private static Set<DynasticPerson> getAllAncestors(DynasticPerson person) {
        Set<DynasticPerson> ancestors = new HashSet<>();
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

    private static int pathLength(DynasticPerson from, DynasticPerson to) {
        return pathLengthHelper(from, to, 0, new HashSet<>());
    }

    private static int pathLengthHelper(DynasticPerson current, DynasticPerson target, 
            int depth, Set<DynasticPerson> visited) {
        if (current == null || !visited.add(current)) return -1;
        if (current == target) return depth;
        
        int pathF = pathLengthHelper(current.father, target, depth + 1, visited);
        if (pathF >= 0) return pathF;
        
        return pathLengthHelper(current.mother, target, depth + 1, visited);
    }
}
