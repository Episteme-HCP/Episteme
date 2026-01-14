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
import java.util.*;

import org.jscience.biology.taxonomy.Species;

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
 * @since 1.0
 */
public class Individual {

    public enum Sex {
        MALE, FEMALE, HERMAPHRODITE, ASEXUAL, UNKNOWN
    }

    public enum LifeStage {
        EMBRYO, JUVENILE, ADULT, SENESCENT
    }

    /**
     * Reproduction modes for different organisms.
     */
    public enum ReproductionMode {
        /** Sexual reproduction (two parents) */
        SEXUAL,
        /** Asexual reproduction (single parent, exact clone) */
        ASEXUAL,
        /** Budding (single parent, offspring grows from parent) */
        BUDDING,
        /** Fragmentation (parent splits into multiple offspring) */
        FRAGMENTATION,
        /** Parthenogenesis (development from unfertilized egg) */
        PARTHENOGENESIS,
        /** Binary fission (single-celled organisms) */
        BINARY_FISSION
    }

    private final String id;
    private final Species species;
    private final Sex sex;
    private final LocalDate birthDate;
    private LocalDate deathDate;
    private LifeStage lifeStage;
    private final List<Individual> parents = new ArrayList<>();
    private final List<Individual> offspring = new ArrayList<>();
    private final Map<String, Object> traits = new HashMap<>();
    private ReproductionMode reproductionMode = ReproductionMode.SEXUAL;

    public Individual(String id, Species species, Sex sex, LocalDate birthDate) {
        this.id = Objects.requireNonNull(id);
        this.species = Objects.requireNonNull(species);
        this.sex = sex;
        this.birthDate = birthDate;
        this.lifeStage = LifeStage.JUVENILE;
    }

    public Individual(String id, Species species, Sex sex) {
        this(id, species, sex, LocalDate.now());
    }

    // ========== Getters ==========
    public String getId() {
        return id;
    }

    public Species getSpecies() {
        return species;
    }

    public Sex getSex() {
        return sex;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public LifeStage getLifeStage() {
        return lifeStage;
    }

    public ReproductionMode getReproductionMode() {
        return reproductionMode;
    }

    /**
     * Returns all parents of this individual.
     * For sexual reproduction, typically contains mother and father.
     * For asexual reproduction, contains a single parent.
     *
     * @return unmodifiable list of parents
     */
    public List<Individual> getParents() {
        return Collections.unmodifiableList(parents);
    }

    // ========== Setters ==========
    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }

    public void setLifeStage(LifeStage stage) {
        this.lifeStage = stage;
    }

    public void setReproductionMode(ReproductionMode mode) {
        this.reproductionMode = mode;
    }

    public List<Individual> getOffspring() {
        return Collections.unmodifiableList(offspring);
    }

    /**
     * Adds a parent to this individual.
     *
     * @param parent the parent individual
     */
    public void addParent(Individual parent) {
        if (!parents.contains(parent)) {
            parents.add(parent);
        }
    }

    // ========== Lifecycle ==========
    public boolean isAlive() {
        return deathDate == null;
    }

    public int getAge() {
        LocalDate endDate = deathDate != null ? deathDate : LocalDate.now();
        return java.time.Period.between(birthDate, endDate).getYears();
    }

    public void die(LocalDate date) {
        this.deathDate = date;
        this.lifeStage = LifeStage.SENESCENT;
    }

    // ========== Reproduction ==========
    public void addOffspring(Individual child) {
        offspring.add(child);
        child.addParent(this);
    }

    public boolean isSiblingOf(Individual other) {
        if (this == other)
            return false;
        // Check if any parents are shared
        for (Individual parent : parents) {
            if (other.parents.contains(parent)) {
                return true;
            }
        }
        return false;
    }

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
     * Creates a clone of this individual (asexual reproduction).
     *
     * @param newId ID for the clone
     * @return the cloned individual
     */
    public Individual clone(String newId) {
        Individual clone = new Individual(newId, species, sex, LocalDate.now());
        clone.reproductionMode = ReproductionMode.ASEXUAL;
        clone.addParent(this);
        this.offspring.add(clone);
        // Copy traits
        clone.traits.putAll(this.traits);
        return clone;
    }

    // ========== Traits ==========
    public void setTrait(String name, Object value) {
        traits.put(name, value);
    }

    public Object getTrait(String name) {
        return traits.get(name);
    }

    public <T> T getTrait(String name, Class<T> type) {
        Object value = traits.get(name);
        return type.isInstance(value) ? type.cast(value) : null;
    }

    @Override
    public String toString() {
        return String.format("Individual[%s: %s, %s, %d years, %s]",
                id, species.getScientificName(), sex, getAge(),
                isAlive() ? "alive" : "deceased");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Individual other))
            return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


