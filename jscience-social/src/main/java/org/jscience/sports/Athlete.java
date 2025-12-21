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
package org.jscience.sports;

import java.time.LocalDate;
import java.util.*;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a professional athlete.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Athlete {

    private final String name;
    private Sport sport;
    private String nationality;
    private LocalDate birthDate;
    private String position;
    private String currentTeam;
    private int jerseyNumber;
    private Real heightCm;
    private Real weightKg;
    private final List<String> achievements = new ArrayList<>();
    private boolean active;

    public Athlete(String name) {
        this.name = name;
        this.active = true;
        this.heightCm = Real.ZERO;
        this.weightKg = Real.ZERO;
    }

    public Athlete(String name, Sport sport, String nationality) {
        this(name);
        this.sport = sport;
        this.nationality = nationality;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Sport getSport() {
        return sport;
    }

    public String getNationality() {
        return nationality;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPosition() {
        return position;
    }

    public String getCurrentTeam() {
        return currentTeam;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public Real getHeightCm() {
        return heightCm;
    }

    public Real getWeightKg() {
        return weightKg;
    }

    public boolean isActive() {
        return active;
    }

    public List<String> getAchievements() {
        return Collections.unmodifiableList(achievements);
    }

    // Setters
    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setBirthDate(LocalDate date) {
        this.birthDate = date;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setCurrentTeam(String team) {
        this.currentTeam = team;
    }

    public void setJerseyNumber(int number) {
        this.jerseyNumber = number;
    }

    public void setHeightCm(Real height) {
        this.heightCm = height;
    }

    public void setHeightCm(double height) {
        this.heightCm = Real.of(height);
    }

    public void setWeightKg(Real weight) {
        this.weightKg = weight;
    }

    public void setWeightKg(double weight) {
        this.weightKg = Real.of(weight);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addAchievement(String achievement) {
        achievements.add(achievement);
    }

    /** Age in years */
    public int getAge() {
        if (birthDate == null)
            return 0;
        return java.time.Period.between(birthDate, LocalDate.now()).getYears();
    }

    /** BMI calculation */
    public Real getBMI() {
        if (heightCm.isZero())
            return Real.ZERO;
        Real heightM = heightCm.divide(Real.of(100));
        return weightKg.divide(heightM.pow(2));
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s) - %s", name, sport != null ? sport.getName() : "N/A", nationality,
                currentTeam);
    }
}