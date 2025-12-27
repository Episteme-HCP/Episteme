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
package org.jscience.medicine;

import java.util.*;

/**
 * Represents a medical treatment plan.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
