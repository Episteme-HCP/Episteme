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

package org.jscience.biology;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import org.jscience.biology.taxonomy.Species;
import org.jscience.earth.Place;
import org.jscience.util.Positioned;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents an individual organism - a single instance of a species.
 * <p>
 * Connects biological taxonomy with social science modeling by providing
 * a base class that can be extended for specific organisms (e.g., Human).
 * Supports multiple reproduction modes including sexual and asexual.
 * </p>
 * Implements ComprehensiveIdentification to support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Individual implements ComprehensiveIdentification, Positioned<Place> {

    private static final long serialVersionUID = 1L;

    @Id
    protected final Identification id;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();

    /** Biological sex of the individual. */
    public enum Sex {
        MALE, FEMALE, HERMAPHRODITE, ASEXUAL, UNKNOWN
    }

    /** Major life stages of the organism. */
    public enum LifeStage {
        EMBRYO, JUVENILE, ADULT, SENESCENT
    }

    /** Reproduction modes for different organisms. */
    public enum ReproductionMode {
        SEXUAL, ASEXUAL, BUDDING, FRAGMENTATION, PARTHENOGENESIS, BINARY_FISSION
    }

    @Attribute
    private final Species species;
    
    @Attribute
    private final Sex sex;
    
    @Attribute
    private final LocalDate birthDate;
    
    @Attribute
    private LocalDate deathDate;
    
    @Attribute
    private LifeStage lifeStage;
    
    @Attribute
    private ReproductionMode reproductionMode;
    
    @Attribute
    private Place place;
    
    @Attribute
    private Place territory;
    
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Individual> parents = new HashSet<>();
    
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Individual> children = new HashSet<>();
    
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Behavior> availableBehaviors = new HashSet<>();

    /**
     * Creates a new individual organism.
     *
     * @param id        unique identifier
     * @param species   biological species
     * @param sex       organism sex
     * @param birthDate birth or creation date (can be null for historical/unknown cases)
     * @throws NullPointerException if id or species is null
     */
    public Individual(Identification id, Species species, Sex sex, LocalDate birthDate) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.species = Objects.requireNonNull(species, "Species cannot be null");
        this.sex = sex != null ? sex : Sex.UNKNOWN;
        this.birthDate = birthDate;
        this.lifeStage = LifeStage.JUVENILE;
    }

    /**
     * Helper constructor for String IDs.
     */
    public Individual(String id, Species species, Sex sex, LocalDate birthDate) {
        this(new SimpleIdentification(id), species, sex, birthDate);
    }

    /**
     * Creates a new individual with the current date as birth date.
     */
    public Individual(Identification id, Species species, Sex sex) {
        this(id, species, sex, null);
    }

    /**
     * Helper constructor for String IDs.
     */
    public Individual(String id, Species species, Sex sex) {
        this(new SimpleIdentification(id), species, sex, null);
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
     * Finds all descendants of this individual.
     * 
     * @return a set of descendants
     */
    public Set<Individual> getDescendants() {
        Set<Individual> descendants = new HashSet<>();
        collectDescendants(this, descendants);
        descendants.remove(this);
        return descendants;
    }

    private void collectDescendants(Individual current, Set<Individual> visited) {
        if (current == null || !visited.add(current)) return;
        for (Individual child : current.getChildren()) {
            collectDescendants(child, visited);
        }
    }

    /**
     * Finds all ancestors of this individual.
     * 
     * @return a set of ancestors
     */
    public Set<Individual> getAncestors() {
        Set<Individual> ancestors = new HashSet<>();
        collectAncestors(this, ancestors);
        ancestors.remove(this);
        return ancestors;
    }

    private void collectAncestors(Individual current, Set<Individual> visited) {
        if (current == null || !visited.add(current)) return;
        for (Individual parent : current.getParents()) {
            collectAncestors(parent, visited);
        }
    }

    /**
     * Checks if this individual is a descendant of another.
     */
    public boolean isDescendantOf(Individual suspectedAncestor) {
        return getAncestors().contains(suspectedAncestor);
    }

    /**
     * Checks if this individual is an ancestor of another.
     */
    public boolean isAncestorOf(Individual suspectedDescendant) {
        return getDescendants().contains(suspectedDescendant);
    }

    /**
     * Calculates the genealogical distance between two individuals.
     * 
     * @return distance in generations (1 for parent-child, 2 for grandparents/siblings), or -1 if no relation
     */
    public int getGenealogicalDistance(Individual other) {
        if (this.equals(other)) return 0;
        
        Map<Individual, Integer> distances = new HashMap<>();
        Queue<Individual> queue = new LinkedList<>();
        
        queue.add(this);
        distances.put(this, 0);
        
        while (!queue.isEmpty()) {
            Individual current = queue.poll();
            int currentDist = distances.get(current);
            
            List<Individual> neighbors = new ArrayList<>();
            neighbors.addAll(current.getParents());
            neighbors.addAll(current.getChildren());
            
            for (Individual neighbor : neighbors) {
                if (!distances.containsKey(neighbor)) {
                    distances.put(neighbor, currentDist + 1);
                    if (neighbor.equals(other)) return currentDist + 1;
                    queue.add(neighbor);
                }
            }
        }
        return -1;
    }

    /**
     * Returns the species of this individual.
     * @return the species
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * Returns the biological sex.
     * @return the sex
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * Returns the birth or creation date.
     * @return the birth date
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Returns the death date, or null if alive.
     * @return the death date
     */
    public LocalDate getDeathDate() {
        return deathDate;
    }

    /**
     * Sets the death date of the individual.
     * @param deathDate the date of death
     */
    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }

    /**
     * Returns the current life stage.
     * @return the life stage
     */
    public LifeStage getLifeStage() {
        return lifeStage;
    }

    /**
     * Sets the life stage of the individual.
     * @param stage the stage to set
     */
    public void setLifeStage(LifeStage stage) {
        this.lifeStage = stage;
    }

    /**
     * Returns the reproduction mode used to create this individual.
     * @return the reproduction mode
     */
    public ReproductionMode getReproductionMode() {
        return reproductionMode;
    }

    /**
     * Sets the reproduction mode.
     * @param mode the mode to set
     */
    public void setReproductionMode(ReproductionMode mode) {
        this.reproductionMode = mode;
    }

    /**
     * Returns the current location of the individual.
     * @return the place
     */
    @Override
    public Place getPosition() {
        return place;
    }

    /**
     * Sets the current location.
     * @param place the place to set
     */
    public void setPosition(Place place) {
        this.place = place;
    }

    /**
     * Returns the territory associated with this individual.
     * @return the territory place
     */
    public Place getTerritory() {
        return territory;
    }

    /**
     * Sets the territory.
     * @param territory the territory to set
     */
    public void setTerritory(Place territory) {
        this.territory = territory;
    }

    @SuppressWarnings("unchecked")
    public void addRole(Object role) {
        if (role != null) {
            java.util.List<Object> roles = (java.util.List<Object>) getTrait("social_roles");
            if (roles == null) {
                roles = new java.util.ArrayList<>();
                setTrait("social_roles", roles);
            }
            roles.add(role);
        }
    }

    /**
     * Returns an unmodifiable set of parents.
     * @return set of parents
     */
    public Set<Individual> getParents() {
        return Collections.unmodifiableSet(parents);
    }

    /**
     * Adds a parent to this individual and adds this as its child (double link).
     * @param parent the parent to add
     */
    public void addParent(Individual parent) {
        if (parent != null && !parents.contains(parent)) {
            parents.add(parent);
            parent.addChild(this);
        }
    }

    /**
     * Returns an unmodifiable set of children.
     * @return set of children
     */
    public Set<Individual> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    /**
     * Adds a child to this individual and sets this as its parent (double link).
     * @param child the child to add
     */
    public void addChild(Individual child) {
        if (child != null && !children.contains(child)) {
            children.add(child);
            child.addParent(this);
        }
    }

    /**
     * Adds a pathology name to the individual's history.
     * @param pathologyName the pathology to add
     */
    @SuppressWarnings("unchecked")
    public void addPathology(String pathologyName) {
        if (pathologyName != null) {
            Set<String> set = (Set<String>) getTrait("pathologies");
            if (set == null) {
                set = new HashSet<>();
                setTrait("pathologies", set);
            }
            set.add(pathologyName);
        }
    }

    /**
     * Returns the set of pathology names.
     * @return set of pathologies
     */
    @SuppressWarnings("unchecked")
    public Set<String> getPathologies() {
        return (Set<String>) getTrait("pathologies", Set.class);
    }

    /**
     * Returns the biography summary.
     * @return summary or null
     */
    public String getBiographySummary() {
        return (String) getTrait("biography_summary");
    }

    /**
     * Sets the biography summary.
     * @param summary the summary
     */
    public void setBiographySummary(String summary) {
        setTrait("biography_summary", summary);
    }

    /**
     * Registers a behavior that this individual is capable of exhibiting.
     * @param behavior behavior to add
     */
    public void addAvailableBehavior(Behavior behavior) {
        if (behavior != null) {
            availableBehaviors.add(behavior);
        }
    }

    /**
     * Returns an unmodifiable set of behaviors available to this individual.
     * @return set of behaviors
     */
    public Set<Behavior> getAvailableBehaviors() {
        return Collections.unmodifiableSet(availableBehaviors);
    }

    /**
     * Removes a behavior from the individual's available set.
     * @param behavior behavior to remove
     */
    public void removeAvailableBehavior(Behavior behavior) {
        availableBehaviors.remove(behavior);
    }

    /**
     * Checks if the individual is still alive.
     * @return true if deathDate is null
     */
    public boolean isAlive() {
        return deathDate == null;
    }

    /**
     * Calculates the age of the individual in years.
     * @return age in years
     */
    public int getAge() {
        LocalDate endDate = deathDate != null ? deathDate : LocalDate.now();
        return Period.between(birthDate, endDate).getYears();
    }

    /**
     * Marks the individual as deceased.
     * @param date the date of death
     */
    public void die(LocalDate date) {
        this.deathDate = date;
        this.lifeStage = LifeStage.SENESCENT;
    }

    /**
     * Checks if this individual is a sibling of another individual.
     * @param other the other individual
     * @return true if they share at least one parent
     */
    public boolean isSiblingOf(Individual other) {
        if (this == other || other == null) return false;
        return !Collections.disjoint(parents, other.parents);
    }

    /**
     * Returns a list of siblings.
     * @return list of siblings
     */
    public List<Individual> getSiblings() {
        Set<Individual> siblings = new HashSet<>();
        for (Individual parent : parents) {
            for (Individual sibling : parent.children) {
                if (!sibling.equals(this)) {
                    siblings.add(sibling);
                }
            }
        }
        return new ArrayList<>(siblings);
    }

    /**
     * Clones this individual.
     * @param newId identifier for the clone
     * @return the new clone instance
     */
    public Individual clone(String newId) {
        Individual clone = new Individual(newId, species, sex, LocalDate.now());
        clone.reproductionMode = ReproductionMode.ASEXUAL;
        clone.addParent(this);
        this.children.add(clone);
        clone.getTraits().putAll(this.getTraits());
        return clone;
    }

    /**
     * Calculates the Wright's coefficient of inbreeding (F) for this individual.
     * F = Σ (1/2)^(n1+n2+1) × (1 + FA)
     * 
     * @return inbreeding coefficient as a {@link Real} number
     */
    public Real calculateInbreedingCoefficient() {
        if (parents.size() < 2) {
            return Real.ZERO;
        }
        
        List<Individual> parentList = new ArrayList<>(parents);
        Individual p1 = parentList.get(0);
        Individual p2 = parentList.get(1);
        
        Set<Individual> ancestors1 = p1.getAncestors();
        ancestors1.add(p1);
        Set<Individual> ancestors2 = p2.getAncestors();
        ancestors2.add(p2);
        
        Set<Individual> commonAncestors = new HashSet<>(ancestors1);
        commonAncestors.retainAll(ancestors2);
        
        if (commonAncestors.isEmpty()) {
            return Real.ZERO;
        }
        
        double fValue = 0.0;
        for (Individual ancestor : commonAncestors) {
            List<Integer> paths1 = getAllPathLengths(p1, ancestor);
            List<Integer> paths2 = getAllPathLengths(p2, ancestor);
            
            for (int n1 : paths1) {
                for (int n2 : paths2) {
                    double fa = ancestor.calculateInbreedingCoefficient().doubleValue();
                    fValue += Math.pow(0.5, n1 + n2 + 1) * (1 + fa);
                }
            }
        }
        
        return Real.of(fValue);
    }

    /**
     * Calculates the coefficient of relationship (r) between this individual and another.
     * 
     * @param other the other individual
     * @return relationship coefficient as a {@link Real} number
     */
    public Real calculateRelationshipCoefficient(Individual other) {
        Objects.requireNonNull(other, "Other individual cannot be null");
        
        Set<Individual> ancestorsA = this.getAncestors();
        ancestorsA.add(this);
        Set<Individual> ancestorsB = other.getAncestors();
        ancestorsB.add(other);
        
        Set<Individual> commonAncestors = new HashSet<>(ancestorsA);
        commonAncestors.retainAll(ancestorsB);
        
        if (commonAncestors.isEmpty()) {
            return Real.ZERO;
        }
        
        double rValue = 0.0;
        for (Individual ancestor : commonAncestors) {
            List<Integer> pathsA = getAllPathLengths(this, ancestor);
            List<Integer> pathsB = getAllPathLengths(other, ancestor);
            for (int n1 : pathsA) {
                for (int n2 : pathsB) {
                    rValue += Math.pow(0.5, n1 + n2);
                }
            }
        }
        
        return Real.of(rValue);
    }
    
    /**
     * Calculates the order of succession based on primogeniture starting from this individual.
     * 
     * @param malePriority if true, males take priority over females
     * @return unmodifiable ordered list of successors
     */
    public List<Individual> calculateSuccessionOrder(boolean malePriority) {
        List<Individual> order = new ArrayList<>();
        collectSuccessors(this, order, malePriority);
        return Collections.unmodifiableList(order);
    }

    private void collectSuccessors(Individual current, List<Individual> order, boolean malePriority) {
        List<Individual> currentChildren = new ArrayList<>(current.getChildren());
        
        currentChildren.sort((c1, c2) -> {
            if (malePriority) {
                if (c1.getSex() == Sex.MALE && c2.getSex() != Sex.MALE) return -1;
                if (c2.getSex() == Sex.MALE && c1.getSex() != Sex.MALE) return 1;
            }
            if (c1.getBirthDate() != null && c2.getBirthDate() != null) {
                return c1.getBirthDate().compareTo(c2.getBirthDate());
            }
            return 0; 
        });
        
        for (Individual child : currentChildren) {
            order.add(child);
            collectSuccessors(child, order, malePriority);
        }
    }

    private static List<Integer> getAllPathLengths(Individual from, Individual to) {
        List<Integer> paths = new ArrayList<>();
        findPaths(from, to, 0, paths, new HashSet<>());
        return paths;
    }

    private static void findPaths(Individual current, Individual target, int depth, List<Integer> paths, Set<Individual> visited) {
        if (current == null) return;
        if (current.equals(target)) {
            paths.add(depth);
            return;
        }
        
        for (Individual parent : current.getParents()) {
            findPaths(parent, target, depth + 1, paths, visited);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Individual individual)) return false;
        return Objects.equals(id, individual.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s of %s", getName(), species.getScientificName());
    }
}
