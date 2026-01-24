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

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Queue;
import java.util.LinkedList;
import org.jscience.biology.taxonomy.Species;
import org.jscience.medicine.Pathology;
import org.jscience.psychology.social.Biography;
import org.jscience.psychology.Behavior;
import org.jscience.geography.Place;
import org.jscience.util.Named;
import org.jscience.util.identity.Identified;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents an individual organism - a single instance of a species.
 * <p>
 * Connects biological taxonomy with social science modeling by providing
 * a base class that can be extended for specific organisms (e.g., Human).
 * Supports multiple reproduction modes including sexual and asexual.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class Individual implements Identified<Identification>, Named, Positioned<Place>, Serializable {

    private static final long serialVersionUID = 1L;

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

    @Id
    private final Identification id;
    
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
    
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Pathology> pathologies = new HashSet<>();
    
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Behavior> availableBehaviors = new HashSet<>();
    
    @Attribute
    private Biography biography;
    
    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

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
     * Helper constructor for String IDs (creates SimpleIdentification).
     */
    public Individual(String id, Species species, Sex sex, LocalDate birthDate) {
        this(new SimpleIdentification(id), species, sex, birthDate);
    }

    /**
     * Creates a new individual with the current date as birth date.
     *
     * @param id      unique identifier
     * @param species biological species
     * @param sex     organism sex
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
    public String getName() {
        return (String) traits.getOrDefault("name", id);
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
     * Adds a pathology (disease, allergy, etc.) to the individual's history.
     * @param pathology the pathology to add
     */
    public void addPathology(Pathology pathology) {
        if (pathology != null) {
            pathologies.add(pathology);
        }
    }

    /**
     * Returns the set of pathologies.
     * @return set of pathologies
     */
    public Set<Pathology> getPathologies() {
        return Collections.unmodifiableSet(pathologies);
    }

    /**
     * Returns the structured biography.
     * @return the structured biography object
     */
    public Biography getStructuredBiography() {
        return biography;
    }

    /**
     * Sets the structured biography.
     * @param biography the biography to set
     */
    public void setStructuredBiography(Biography biography) {
        this.biography = biography;
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
        clone.traits.putAll(this.traits);
        return clone;
    }

    /**
     * Sets a custom trait.
     * @param name  trait name
     * @param value trait value
     */
    public void setTrait(String name, Object value) {
        traits.put(name, value);
    }

    /**
     * Retrieves a custom trait.
     * @param name trait name
     * @return trait value, or null
     */
    public Object getTrait(String name) {
        return traits.get(name);
    }

    /**
     * Retrieves a typed custom trait.
     * @param name trait name
     * @param type trait class
     * @param <T>  type parameter
     * @return typed trait value, or null
     */
    public <T> T getTrait(String name, Class<T> type) {
        Object value = traits.get(name);
        return type.isInstance(value) ? type.cast(value) : null;
    }
}
