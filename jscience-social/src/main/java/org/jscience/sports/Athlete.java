/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.sports;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a professional athlete.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Athlete {

    private final String name;
    private Sport sport;
    private String nationality;
    private LocalDate birthDate;
    private String position;
    private String currentTeam;
    private int jerseyNumber;
    private double heightCm;
    private double weightKg;
    private final List<String> achievements = new ArrayList<>();
    private boolean active;

    public Athlete(String name) {
        this.name = name;
        this.active = true;
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

    public double getHeightCm() {
        return heightCm;
    }

    public double getWeightKg() {
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

    public void setHeightCm(double height) {
        this.heightCm = height;
    }

    public void setWeightKg(double weight) {
        this.weightKg = weight;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addAchievement(String achievement) {
        achievements.add(achievement);
    }

    /**
     * Returns age in years.
     */
    public int getAge() {
        if (birthDate == null)
            return 0;
        return java.time.Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * Returns BMI.
     */
    public double getBMI() {
        if (heightCm <= 0)
            return 0;
        double heightM = heightCm / 100.0;
        return weightKg / (heightM * heightM);
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s) - %s", name, sport != null ? sport.getName() : "N/A", nationality,
                currentTeam);
    }
}
