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

package org.jscience.social.law;

import org.jscience.natural.biology.Individual;
import org.jscience.core.methodology.ScientificReport;
import org.jscience.social.sociology.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an individual's legal and administrative status within a jurisdiction.
 * This includes their educational history, legal record, and biometric data.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ResponsibleIndividual extends Role {

    private final List<License> schoolRecords;
    private final List<ScientificReport> policeRecords;
    private Biometrics biometrics;

    /**
     * Creates a new ResponsibleIndividual for a given person in a specific legal context.
     *
     * @param individual the individual being represented
     * @param situation the street situation context (e.g., citizenship or residence context)
     */
    public ResponsibleIndividual(Individual individual, StreetSituation situation) {
        super(individual, "Responsible Individual", situation, Role.CLIENT);
        this.schoolRecords = new ArrayList<>();
        this.policeRecords = new ArrayList<>();
        this.biometrics = null;
    }

    /**
     * Returns the list of educational degrees and certifications (school records).
     * @return the list of school records
     */
    public List<License> getSchoolRecords() {
        return new ArrayList<>(schoolRecords);
    }

    /**
     * Adds a new educational degree or certificate.
     *
     * @param schoolRecord the license/diploma to add
     * @throws IllegalArgumentException if the record is null
     */
    public void addSchoolRecord(License schoolRecord) {
        if (schoolRecord == null) {
            throw new IllegalArgumentException("School record cannot be null.");
        }
        schoolRecords.add(schoolRecord);
    }

    /**
     * Removes an educational degree or certificate.
     * @param schoolRecord the record to remove
     */
    public void removeSchoolRecord(License schoolRecord) {
        schoolRecords.remove(schoolRecord);
    }

    /**
     * Removes the most recent educational record.
     */
    public void removeLastSchoolRecord() {
        if (!schoolRecords.isEmpty()) {
            schoolRecords.remove(schoolRecords.size() - 1);
        }
    }

    /**
     * Sets the complete list of educational records.
     *
     * @param schoolRecords the list of License objects
     * @throws IllegalArgumentException if the list is null or contains non-License elements
     */
    public void setSchoolRecords(List<License> schoolRecords) {
        if (schoolRecords == null) {
            throw new IllegalArgumentException("School records list cannot be null.");
        }
        
        for (Object record : schoolRecords) {
            if (!(record instanceof License)) {
                throw new IllegalArgumentException("The list of school records must contain only License objects.");
            }
        }
        
        this.schoolRecords.clear();
        this.schoolRecords.addAll(schoolRecords);
    }

    /**
     * Returns the list of legal and police reports.
     * @return the list of police records
     */
    public List<ScientificReport> getPoliceRecords() {
        return new ArrayList<>(policeRecords);
    }

    /**
     * Adds a new police report or record.
     *
     * @param policeRecord the report to add
     * @throws IllegalArgumentException if the record is null
     */
    public void addPoliceRecord(ScientificReport policeRecord) {
        if (policeRecord == null) {
            throw new IllegalArgumentException("Police record cannot be null.");
        }
        policeRecords.add(policeRecord);
    }

    /**
     * Removes a specific police report or record.
     * @param policeRecord the report to remove
     */
    public void removePoliceRecord(ScientificReport policeRecord) {
        policeRecords.remove(policeRecord);
    }

    /**
     * Removes the most recent police record.
     */
    public void removeLastPoliceRecord() {
        if (!policeRecords.isEmpty()) {
            policeRecords.remove(policeRecords.size() - 1);
        }
    }

    /**
     * Sets the complete list of police records.
     *
     * @param policeRecords the list of Report objects
     * @throws IllegalArgumentException if the list is null or contains non-Report elements
     */
    public void setPoliceRecords(List<ScientificReport> policeRecords) {
        if (policeRecords == null) {
            throw new IllegalArgumentException("Police records list cannot be null.");
        }
        
        for (Object record : policeRecords) {
            if (!(record instanceof ScientificReport)) {
                throw new IllegalArgumentException("The list of police records must contain only ScientificReport objects.");
            }
        }
        
        this.policeRecords.clear();
        this.policeRecords.addAll(policeRecords);
    }

    /**
     * Returns the biometric data for the individual.
     * @return the biometric data, or null if not available
     */
    public Biometrics getBiometrics() {
        return biometrics;
    }

    /**
     * Sets the biometric data for the individual.
     * @param biometrics the biometric data to set
     */
    public void setBiometrics(Biometrics biometrics) {
        this.biometrics = biometrics;
    }
}

