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

import java.util.*;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents an educational institution.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class School {

    public enum Type {
        PRIMARY, SECONDARY, HIGH_SCHOOL, COLLEGE, UNIVERSITY,
        VOCATIONAL, ONLINE, PRIVATE, PUBLIC, CHARTER
    }

    public enum Level {
        PRESCHOOL, ELEMENTARY, MIDDLE, HIGH, UNDERGRADUATE, GRADUATE, DOCTORAL
    }

    private final String name;
    private Type type;
    private Level level;
    private String location;
    private int foundedYear;
    private long studentCount;
    private int facultyCount;
    private final List<String> programs = new ArrayList<>();
    private Real acceptanceRate;

    public School(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Level getLevel() {
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
    public void setType(Type type) {
        this.type = type;
    }

    public void setLevel(Level level) {
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
        programs.add(program);
    }

    /**
     * Returns student-to-faculty ratio.
     */
    public Real getStudentFacultyRatio() {
        return facultyCount > 0 ? Real.of((double) studentCount / facultyCount) : Real.ZERO;
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %d students", name, type, studentCount);
    }

    // Notable institutions
    public static School mit() {
        School s = new School("MIT", Type.UNIVERSITY);
        s.setLevel(Level.UNDERGRADUATE);
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


