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

import org.jscience.biology.Individual;

import org.jscience.economics.Organization;
import org.jscience.economics.Worker;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * The Doctor class provides some useful information for people whose job
 * is to cure individuals.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//may be we should extend WorkSituation to provide a MedicalSituation
public class Doctor extends Worker {
    /** DOCUMENT ME! */
    private Set patients;

/**
     * Creates a new Doctor object.
     *
     * @param individual       DOCUMENT ME!
     * @param medicalSituation DOCUMENT ME!
     * @param function         DOCUMENT ME!
     * @param organization     DOCUMENT ME!
     */
    public Doctor(Individual individual, MedicalSituation medicalSituation,
        String function, Organization organization) {
        super(individual, medicalSituation, function, organization);
        patients = new HashSet();
    }

/**
     * Creates a new Doctor object.
     *
     * @param individual       DOCUMENT ME!
     * @param medicalSituation DOCUMENT ME!
     * @param organization     DOCUMENT ME!
     */
    public Doctor(Individual individual, MedicalSituation medicalSituation,
        Organization organization) {
        super(individual, medicalSituation, "Doctor", organization);
        patients = new HashSet();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getPatients() {
        return patients;
    }

    //all members of the Set should be patients
    /**
     * DOCUMENT ME!
     *
     * @param patients DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setPatients(Set patients) {
        Iterator iterator;
        boolean valid;

        if (patients != null) {
            iterator = patients.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Patient;
            }

            if (valid) {
                this.patients = patients;
            } else {
                throw new IllegalArgumentException(
                    "The Set of patients should contain only Patients.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of patients shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param patient DOCUMENT ME!
     */
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    /**
     * DOCUMENT ME!
     *
     * @param patient DOCUMENT ME!
     */
    public void removePatient(Patient patient) {
        patients.remove(patient);
    }
}
