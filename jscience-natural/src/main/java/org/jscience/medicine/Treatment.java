/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.medicine;

import java.util.*;

/**
 * Represents a medical treatment plan.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Treatment {

    public enum Type {
        MEDICATION, SURGERY, THERAPY, RADIATION, CHEMOTHERAPY,
        IMMUNOTHERAPY, PHYSICAL_THERAPY, LIFESTYLE, SUPPORTIVE
    }

    private final String name;
    private Type type;
    private Disease targetDisease;
    private String description;
    private int durationDays;
    private final List<Medication> medications = new ArrayList<>();
    private String frequency;
    private double successRate; // 0.0 to 1.0

    public Treatment(String name, Type type) {
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

    public Disease getTargetDisease() {
        return targetDisease;
    }

    public String getDescription() {
        return description;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public String getFrequency() {
        return frequency;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public List<Medication> getMedications() {
        return Collections.unmodifiableList(medications);
    }

    // Setters
    public void setType(Type type) {
        this.type = type;
    }

    public void setTargetDisease(Disease disease) {
        this.targetDisease = disease;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDurationDays(int days) {
        this.durationDays = days;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setSuccessRate(double rate) {
        this.successRate = Math.max(0, Math.min(1, rate));
    }

    public void addMedication(Medication med) {
        medications.add(med);
    }

    @Override
    public String toString() {
        return String.format("Treatment '%s' (%s) - %d days", name, type, durationDays);
    }
}
