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

package org.jscience.medicine;

import java.util.*;

/**
 * Represents a disease or medical condition.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Disease {

    public enum Origin {
        VIRAL, BACTERIAL, FUNGAL, PARASITIC, PRION, GENETIC,
        AUTOIMMUNE, ENVIRONMENTAL, NUTRITIONAL, UNKNOWN
    }

    public enum Transmission {
        AIRBORNE, DROPLET, CONTACT, BLOOD, SEXUAL, FECAL_ORAL,
        VECTOR, VERTICAL, FOOD, WATER, NONE
    }

    public enum Severity {
        MILD, MODERATE, SEVERE, CRITICAL, FATAL
    }

    private final String name;
    private String icdCode; // ICD-10 code
    private Origin origin;
    private final Set<Transmission> transmissionModes = EnumSet.noneOf(Transmission.class);
    private Severity severity;
    private String description;
    private final List<String> symptoms = new ArrayList<>();
    private int incubationDays;
    private boolean chronic;
    private boolean curable;

    public Disease(String name) {
        this.name = name;
        this.origin = Origin.UNKNOWN;
    }

    public Disease(String name, Origin origin) {
        this(name);
        this.origin = origin;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getIcdCode() {
        return icdCode;
    }

    public Origin getOrigin() {
        return origin;
    }

    public Set<Transmission> getTransmissionModes() {
        return Collections.unmodifiableSet(transmissionModes);
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getSymptoms() {
        return Collections.unmodifiableList(symptoms);
    }

    public int getIncubationDays() {
        return incubationDays;
    }

    public boolean isChronic() {
        return chronic;
    }

    public boolean isCurable() {
        return curable;
    }

    // Setters
    public void setIcdCode(String code) {
        this.icdCode = code;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIncubationDays(int days) {
        this.incubationDays = days;
    }

    public void setChronic(boolean chronic) {
        this.chronic = chronic;
    }

    public void setCurable(boolean curable) {
        this.curable = curable;
    }

    public void addTransmissionMode(Transmission mode) {
        transmissionModes.add(mode);
    }

    public void addSymptom(String symptom) {
        symptoms.add(symptom);
    }

    public boolean isInfectious() {
        return !transmissionModes.isEmpty() && transmissionModes.stream()
                .anyMatch(t -> t != Transmission.NONE);
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s)", name, origin, severity);
    }

    // Common diseases
    public static Disease influenza() {
        Disease d = new Disease("Influenza", Origin.VIRAL);
        d.setIcdCode("J10");
        d.addTransmissionMode(Transmission.AIRBORNE);
        d.addTransmissionMode(Transmission.DROPLET);
        d.setSeverity(Severity.MILD);
        d.setIncubationDays(2);
        d.setCurable(true);
        d.addSymptom("Fever");
        d.addSymptom("Cough");
        d.addSymptom("Fatigue");
        return d;
    }

    public static Disease covid19() {
        Disease d = new Disease("COVID-19", Origin.VIRAL);
        d.setIcdCode("U07.1");
        d.addTransmissionMode(Transmission.AIRBORNE);
        d.addTransmissionMode(Transmission.DROPLET);
        d.setSeverity(Severity.MODERATE);
        d.setIncubationDays(5);
        d.addSymptom("Fever");
        d.addSymptom("Cough");
        d.addSymptom("Loss of taste/smell");
        return d;
    }
}


