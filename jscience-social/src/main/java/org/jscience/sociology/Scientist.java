/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.sociology;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a scientist or researcher.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Scientist {

    public enum Field {
        PHYSICS, CHEMISTRY, BIOLOGY, MATHEMATICS, ASTRONOMY,
        MEDICINE, COMPUTER_SCIENCE, ENGINEERING, GEOLOGY,
        PSYCHOLOGY, SOCIOLOGY, ECONOMICS, LINGUISTICS
    }

    private final String name;
    private final Set<Field> fields = EnumSet.noneOf(Field.class);
    private String nationality;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String institution;
    private final List<String> publications = new ArrayList<>();
    private final List<String> awards = new ArrayList<>();
    private boolean nobelLaureate;

    public Scientist(String name) {
        this.name = name;
    }

    public Scientist(String name, Field primaryField) {
        this(name);
        this.fields.add(primaryField);
    }

    // Getters
    public String getName() {
        return name;
    }

    public Set<Field> getFields() {
        return Collections.unmodifiableSet(fields);
    }

    public String getNationality() {
        return nationality;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public String getInstitution() {
        return institution;
    }

    public boolean isNobelLaureate() {
        return nobelLaureate;
    }

    public List<String> getPublications() {
        return Collections.unmodifiableList(publications);
    }

    public List<String> getAwards() {
        return Collections.unmodifiableList(awards);
    }

    // Setters
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setBirthDate(LocalDate date) {
        this.birthDate = date;
    }

    public void setDeathDate(LocalDate date) {
        this.deathDate = date;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public void setNobelLaureate(boolean laureate) {
        this.nobelLaureate = laureate;
    }

    public void addField(Field field) {
        fields.add(field);
    }

    public void addPublication(String pub) {
        publications.add(pub);
    }

    public void addAward(String award) {
        awards.add(award);
    }

    public boolean isAlive() {
        return deathDate == null;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)%s", name, fields, nobelLaureate ? " [Nobel]" : "");
    }

    // Famous scientists
    public static Scientist einstein() {
        Scientist s = new Scientist("Albert Einstein", Field.PHYSICS);
        s.setNationality("German-American");
        s.setBirthDate(LocalDate.of(1879, 3, 14));
        s.setDeathDate(LocalDate.of(1955, 4, 18));
        s.setNobelLaureate(true);
        s.addAward("Nobel Prize in Physics (1921)");
        s.addPublication("On the Electrodynamics of Moving Bodies");
        return s;
    }

    public static Scientist curie() {
        Scientist s = new Scientist("Marie Curie", Field.PHYSICS);
        s.addField(Field.CHEMISTRY);
        s.setNationality("Polish-French");
        s.setBirthDate(LocalDate.of(1867, 11, 7));
        s.setDeathDate(LocalDate.of(1934, 7, 4));
        s.setNobelLaureate(true);
        s.addAward("Nobel Prize in Physics (1903)");
        s.addAward("Nobel Prize in Chemistry (1911)");
        return s;
    }

    public static Scientist turing() {
        Scientist s = new Scientist("Alan Turing", Field.MATHEMATICS);
        s.addField(Field.COMPUTER_SCIENCE);
        s.setNationality("British");
        s.setBirthDate(LocalDate.of(1912, 6, 23));
        s.setDeathDate(LocalDate.of(1954, 6, 7));
        s.addPublication("On Computable Numbers");
        return s;
    }
}
