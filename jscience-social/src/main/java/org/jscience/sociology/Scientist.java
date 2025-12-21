/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a scientist or researcher.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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