/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Individual {

    public enum Sex {
        MALE, FEMALE, HERMAPHRODITE, ASEXUAL, UNKNOWN
    }

    public enum LifeStage {
        EMBRYO, JUVENILE, ADULT, SENESCENT
    }

    private final String id;
    private final Species species;
    private final Sex sex;
    private final LocalDate birthDate;
    private LocalDate deathDate;
    private LifeStage lifeStage;
    private Individual mother;
    private Individual father;
    private final List<Individual> offspring = new ArrayList<>();
    private final Map<String, Object> traits = new HashMap<>();

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

    public Individual getMother() {
        return mother;
    }

    public Individual getFather() {
        return father;
    }

    public List<Individual> getOffspring() {
        return Collections.unmodifiableList(offspring);
    }

    // ========== Setters ==========
    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }

    public void setLifeStage(LifeStage stage) {
        this.lifeStage = stage;
    }

    public void setMother(Individual mother) {
        this.mother = mother;
    }

    public void setFather(Individual father) {
        this.father = father;
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
        if (sex == Sex.FEMALE) {
            child.setMother(this);
        } else if (sex == Sex.MALE) {
            child.setFather(this);
        }
    }

    public boolean isSiblingOf(Individual other) {
        if (this == other)
            return false;
        return (mother != null && mother.equals(other.mother)) ||
                (father != null && father.equals(other.father));
    }

    public List<Individual> getSiblings() {
        List<Individual> siblings = new ArrayList<>();
        if (mother != null) {
            for (Individual sibling : mother.offspring) {
                if (!sibling.equals(this))
                    siblings.add(sibling);
            }
        }
        return siblings;
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
