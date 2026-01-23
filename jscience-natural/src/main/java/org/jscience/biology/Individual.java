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
import org.jscience.biology.taxonomy.Species;
import org.jscience.geography.Place;
import org.jscience.util.Named;
import org.jscience.util.identity.Identified;
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
public class Individual implements Identified<String>, Named, Serializable {

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
    private final String id;
    
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
    
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final List<Individual> parents = new ArrayList<>();
    
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final List<Individual> offspring = new ArrayList<>();
    
    private final Map<String, Object> traits = new HashMap<>();
    
    @Attribute
    private ReproductionMode reproductionMode = ReproductionMode.SEXUAL;

    private Place place;
    private Place territory;

    /**
     * Creates a new individual organism.
     *
     * @param id        unique identifier
     * @param species   biological species
     * @param sex       organism sex
     * @param birthDate birth or creation date
     * @throws NullPointerException if id or species is null
     */
    public Individual(String id, Species species, Sex sex, LocalDate birthDate) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.species = Objects.requireNonNull(species, "Species cannot be null");
        this.sex = sex != null ? sex : Sex.UNKNOWN;
        this.birthDate = birthDate != null ? birthDate : LocalDate.now();
        this.lifeStage = LifeStage.JUVENILE;
    }

    /**
     * Creates a new individual with the current date as birth date.
     *
     * @param id      unique identifier
     * @param species biological species
     * @param sex     organism sex
     */
    public Individual(String id, Species species, Sex sex) {
        this(id, species, sex, LocalDate.now());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return (String) traits.getOrDefault("name", id);
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
     * Returns an unmodifiable list of parents.
     * @return list of parents
     */
    public List<Individual> getParents() {
        return Collections.unmodifiableList(parents);
    }

    /**
     * Adds a parent to this individual.
     * @param parent the parent to add
     */
    public void addParent(Individual parent) {
        if (parent != null && !parents.contains(parent)) {
            parents.add(parent);
        }
    }

    /**
     * Returns an unmodifiable list of offspring.
     * @return list of children
     */
    public List<Individual> getOffspring() {
        return Collections.unmodifiableList(offspring);
    }

    /**
     * Adds an offspring to this individual and sets this as its parent.
     * @param child the offspring to add
     */
    public void addOffspring(Individual child) {
        if (child != null && !offspring.contains(child)) {
            offspring.add(child);
            child.addParent(this);
        }
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
            for (Individual sibling : parent.offspring) {
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
        this.offspring.add(clone);
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
