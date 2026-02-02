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

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import org.jscience.natural.biology.taxonomy.Species;
import org.jscience.natural.earth.Place;
import org.jscience.core.util.Positioned;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.persistence.Attribute;

import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.natural.engineering.eventdriven.EventDrivenEngine;
import org.jscience.natural.engineering.eventdriven.Event;


/**
 * Represents an individual organism - a single instance of a species.
 */
@Persistent
public class Individual extends SocialEntity implements Positioned<Place> {

    private static final long serialVersionUID = 2L;

    @Attribute
    private final Species species;
    
    @Attribute
    private final BiologicalSex sex;
    
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
     * Creates a new individual organism with an associated simulation engine.
     */
    public Individual(Identification id, Species species, BiologicalSex sex, LocalDate birthDate, EventDrivenEngine engine) {
        super(id, engine);
        this.species = Objects.requireNonNull(species, "Species cannot be null");
        this.sex = sex != null ? sex : BiologicalSex.UNKNOWN;
        this.birthDate = birthDate;
        this.lifeStage = LifeStage.JUVENILE;
    }

    /**
     * Helper constructor for String IDs.
     */
    public Individual(String id, Species species, BiologicalSex sex, LocalDate birthDate) {
        this(new SimpleIdentification(id), species, sex, birthDate, null);
    }

    public Individual(Identification id, Species species, BiologicalSex sex) {
        this(id, species, sex, null, null);
    }

    public Individual(String id, Species species, BiologicalSex sex) {
        this(new SimpleIdentification(id), species, sex, null, null);
    }

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

    public boolean isDescendantOf(Individual suspectedAncestor) {
        return getAncestors().contains(suspectedAncestor);
    }

    public boolean isAncestorOf(Individual suspectedDescendant) {
        return getDescendants().contains(suspectedDescendant);
    }

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

    public Species getSpecies() {
        return species;
    }

    public BiologicalSex getSex() {
        return sex;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }

    public LifeStage getLifeStage() {
        return lifeStage;
    }

    public void setLifeStage(LifeStage stage) {
        this.lifeStage = stage;
    }

    public ReproductionMode getReproductionMode() {
        return reproductionMode;
    }

    public void setReproductionMode(ReproductionMode mode) {
        this.reproductionMode = mode;
    }

    @Override
    public Place getPosition() {
        return place;
    }

    public void setPosition(Place place) {
        this.place = place;
    }

    public Place getTerritory() {
        return territory;
    }

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

    public Set<Individual> getParents() {
        return Collections.unmodifiableSet(parents);
    }

    public void addParent(Individual parent) {
        if (parent != null && !parents.contains(parent)) {
            parents.add(parent);
            parent.addChild(this);
        }
    }

    public Set<Individual> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public void addChild(Individual child) {
        if (child != null && !children.contains(child)) {
            children.add(child);
            child.addParent(this);
        }
    }

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

    @SuppressWarnings("unchecked")
    public Set<String> getPathologies() {
        return (Set<String>) getTrait("pathologies", Set.class);
    }

    public String getBiographySummary() {
        return (String) getTrait("biography_summary");
    }

    public void setBiographySummary(String summary) {
        setTrait("biography_summary", summary);
    }

    public void addAvailableBehavior(Behavior behavior) {
        if (behavior != null) {
            availableBehaviors.add(behavior);
        }
    }

    public Set<Behavior> getAvailableBehaviors() {
        return Collections.unmodifiableSet(availableBehaviors);
    }

    public void removeAvailableBehavior(Behavior behavior) {
        availableBehaviors.remove(behavior);
    }

    public boolean isAlive() {
        return deathDate == null;
    }

    public int getAge() {
        LocalDate endDate = deathDate != null ? deathDate : LocalDate.now();
        return birthDate != null ? Period.between(birthDate, endDate).getYears() : 0;
    }

    public void die(LocalDate date) {
        this.deathDate = date;
        this.lifeStage = LifeStage.SENESCENT;
    }

    public boolean isSiblingOf(Individual other) {
        if (this == other || other == null) return false;
        return !Collections.disjoint(parents, other.parents);
    }

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

    public Individual clone(String newId) {
        Individual clone = new Individual(newId, species, sex, LocalDate.now());
        clone.reproductionMode = ReproductionMode.ASEXUAL;
        clone.addParent(this);
        this.children.add(clone);
        clone.getTraits().putAll(this.getTraits());
        return clone;
    }

    public Real calculateInbreedingCoefficient() {
        if (parents.size() < 2) return Real.ZERO;
        
        List<Individual> parentList = new ArrayList<>(parents);
        Individual p1 = parentList.get(0);
        Individual p2 = parentList.get(1);
        
        Set<Individual> ancestors1 = p1.getAncestors();
        ancestors1.add(p1);
        Set<Individual> ancestors2 = p2.getAncestors();
        ancestors2.add(p2);
        
        Set<Individual> commonAncestors = new HashSet<>(ancestors1);
        commonAncestors.retainAll(ancestors2);
        
        if (commonAncestors.isEmpty()) return Real.ZERO;
        
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

    public Real calculateRelationshipCoefficient(Individual other) {
        Objects.requireNonNull(other, "Other individual cannot be null");
        
        Set<Individual> ancestorsA = this.getAncestors();
        ancestorsA.add(this);
        Set<Individual> ancestorsB = other.getAncestors();
        ancestorsB.add(other);
        
        Set<Individual> commonAncestors = new HashSet<>(ancestorsA);
        commonAncestors.retainAll(ancestorsB);
        
        if (commonAncestors.isEmpty()) return Real.ZERO;
        
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
    
    public List<Individual> calculateSuccessionOrder(boolean malePriority) {
        List<Individual> order = new ArrayList<>();
        collectSuccessors(this, order, malePriority);
        return Collections.unmodifiableList(order);
    }

    private void collectSuccessors(Individual current, List<Individual> order, boolean malePriority) {
        List<Individual> currentChildren = new ArrayList<>(current.getChildren());
        
        currentChildren.sort((c1, c2) -> {
            if (malePriority) {
                if (c1.getSex() == BiologicalSex.MALE && c2.getSex() != BiologicalSex.MALE) return -1;
                if (c2.getSex() == BiologicalSex.MALE && c1.getSex() != BiologicalSex.MALE) return 1;
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

    @Override
    public void processEvent(Event event) {
        // Default: no-op or basic logging
    }
}
