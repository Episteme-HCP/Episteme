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

package org.jscience.sociology;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

/**
 * Represents an educational institution, such as a school, university, or college.
 * Provides details about the institution's type, level, size, and location.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;

@Persistent
public class School implements Identified<Identification>, org.jscience.util.Named, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;
    @Attribute
    private final String name;
    @Attribute
    private SchoolType type;
    @Attribute
    private SchoolLevel level;
    @Attribute
    private String location;
    @Attribute
    private int foundedYear;
    @Attribute
    private long studentCount;
    @Attribute
    private int facultyCount;
    @Attribute
    private final List<String> programs = new ArrayList<>();
    @Attribute
    private Real acceptanceRate;

    /**
     * Creates a new School.
     *
     * @param name identifying name
     * @param type institution type
     * @throws NullPointerException if inputs are null
     * @throws IllegalArgumentException if name is empty
     */
    public School(String name, SchoolType type) {
        this.id = new SimpleIdentification(UUID.randomUUID().toString());
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
        this.type = Objects.requireNonNull(type, "Type cannot be null");
    }

    // Getters
    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public SchoolType getType() {
        return type;
    }

    public SchoolLevel getLevel() {
        return level;
    }

    public String getLocation() {
        return location;
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    public long getStudentCount() {
        return studentCount;
    }

    public int getFacultyCount() {
        return facultyCount;
    }

    public Real getAcceptanceRate() {
        return acceptanceRate;
    }

    public List<String> getPrograms() {
        return Collections.unmodifiableList(programs);
    }

    // Setters
    public void setType(SchoolType type) {
        this.type = type;
    }

    public void setLevel(SchoolLevel level) {
        this.level = level;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFoundedYear(int year) {
        this.foundedYear = year;
    }

    public void setStudentCount(long count) {
        this.studentCount = count;
    }

    public void setFacultyCount(int count) {
        this.facultyCount = count;
    }

    public void setAcceptanceRate(Real rate) {
        this.acceptanceRate = rate;
    }

    public void addProgram(String program) {
        if (program != null) {
            programs.add(program);
        }
    }

    /**
     * Returns student-to-faculty ratio.
     * @return ratio, or ZERO if no faculty
     */
    public Real getStudentFacultyRatio() {
        return facultyCount > 0 ? Real.of((double) studentCount / facultyCount) : Real.ZERO;
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %d students", name, type, studentCount);
    }

    // Notable institutions
    // Notable institutions
    public static School mit() {
        School s = new School("MIT", SchoolType.UNIVERSITY);
        s.setLevel(SchoolLevel.UNDERGRADUATE);
        s.setLocation("Cambridge, MA, USA");
        s.setFoundedYear(1861);
        s.setStudentCount(11500);
        s.setFacultyCount(1000);
        s.setAcceptanceRate(Real.of(0.04));
        s.addProgram("Computer Science");
        s.addProgram("Engineering");
        return s;
    }
}
