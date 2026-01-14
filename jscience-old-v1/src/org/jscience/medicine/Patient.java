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

import org.jscience.measure.Report;

import org.jscience.sociology.Role;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * The Patient class provides some useful information about the health of
 * an individual.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//some variables are relevant only for vertebrates (but this is mostly the kind of cases a veterinary or physics has)
public class Patient extends Role {
    //http://en.wikipedia.org/wiki/Vital_signs
    /** DOCUMENT ME! */
    private float bloodPressure; //using standard international unit Pascal

    /** DOCUMENT ME! */
    private float cardiacRate; //beat per seconds, mean over short period of seconds

    /** DOCUMENT ME! */
    private float temperature; //kelvin degrees

    /** DOCUMENT ME! */
    private float normalTemperature; //kelvin degrees

    /** DOCUMENT ME! */
    private float respiratoryRate; //beats per seconds

    /** DOCUMENT ME! */
    private float painScale; //0 to 10

    /** DOCUMENT ME! */
    private float bloodOxygen; //percent of normal

    /** DOCUMENT ME! */
    private Set currentPathologies; //diseases, defects,etc a Set of Pathologies

    /** DOCUMENT ME! */
    private Set treatments;

    /** DOCUMENT ME! */
    private Vector medicalRecords; //Set of Records

/**
     * Creates a new Patient object.
     *
     * @param individual DOCUMENT ME!
     * @param situation  DOCUMENT ME!
     */
    public Patient(Individual individual, MedicalSituation situation) {
        super(individual, "Patient", situation, Role.CLIENT);
        this.bloodPressure = 0;
        this.cardiacRate = 0;
        this.temperature = 0;
        this.normalTemperature = 0;
        this.respiratoryRate = 0;
        this.painScale = 0;
        this.bloodOxygen = 0;
        this.currentPathologies = Collections.EMPTY_SET;
        this.treatments = Collections.EMPTY_SET;
        this.medicalRecords = new Vector();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getBloodPressure() {
        return bloodPressure;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pressure DOCUMENT ME!
     */
    public void setBloodPressure(float pressure) {
        this.bloodPressure = pressure;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getCardiacRate() {
        return cardiacRate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rate DOCUMENT ME!
     */
    public void setCardiacRate(float rate) {
        this.cardiacRate = rate;
    }

    //we could also have a field for normal cardiac rate of an adult individual from this Species at rest
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getTemperature() {
        return temperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @param temperature DOCUMENT ME!
     */
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    //the normal temperature for this Species, assuming a healthy adult individual
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getNormalTemperature() {
        return normalTemperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @param normalTemperature DOCUMENT ME!
     */
    public void setNormalTemperature(float normalTemperature) {
        this.normalTemperature = normalTemperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getRespiratoryRate() {
        return respiratoryRate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param respiratoryRate DOCUMENT ME!
     */
    public void setRespiratoryRate(float respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getPainScale() {
        return painScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @param painScale DOCUMENT ME!
     */
    public void setPainScale(float painScale) {
        this.painScale = painScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getBloodOxygen() {
        return bloodOxygen;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bloodOxygen DOCUMENT ME!
     */
    public void setBloodOxygen(float bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getCurrentPathologies() {
        return currentPathologies;
    }

    //all members of the Set should be pathologies
    /**
     * DOCUMENT ME!
     *
     * @param pathologies DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setCurrentPathologies(Set pathologies) {
        Iterator iterator;
        boolean valid;

        if (pathologies != null) {
            iterator = pathologies.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Pathology;
            }

            if (valid) {
                this.currentPathologies = pathologies;
            } else {
                throw new IllegalArgumentException(
                    "The Set of pathologies should contain only Pathologies.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of pathologies shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param pathology DOCUMENT ME!
     */
    public void addPathology(Pathology pathology) {
        currentPathologies.add(pathology);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pathology DOCUMENT ME!
     */
    public void removePathology(Pathology pathology) {
        currentPathologies.remove(pathology);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getTreatments() {
        return treatments;
    }

    //all members of the Set should be treatments
    /**
     * DOCUMENT ME!
     *
     * @param treatments DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setTreatments(Set treatments) {
        Iterator iterator;
        boolean valid;

        if (treatments != null) {
            iterator = treatments.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Treatment;
            }

            if (valid) {
                this.treatments = treatments;
            } else {
                throw new IllegalArgumentException(
                    "The Set of treatments should contain only Treatments.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of treatments shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param treatment DOCUMENT ME!
     */
    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
    }

    /**
     * DOCUMENT ME!
     *
     * @param treatment DOCUMENT ME!
     */
    public void removeTreatment(Treatment treatment) {
        treatments.remove(treatment);
    }

    //returns the complete list of medical records sorted by date
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getMedicalRecords() {
        return medicalRecords;
    }

    /**
     * DOCUMENT ME!
     *
     * @param medicalRecord DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addMedicalRecord(Report medicalRecord) {
        if (medicalRecords != null) {
            medicalRecords.add(medicalRecord);
        } else {
            throw new IllegalArgumentException(
                "You can't add a null medical record.");
        }
    }

    //this almost should not happen
    /**
     * DOCUMENT ME!
     *
     * @param medicalRecord DOCUMENT ME!
     */
    public void removeMedicalRecord(Report medicalRecord) {
        medicalRecords.remove(medicalRecord);
    }

    //this is very rare
    /**
     * DOCUMENT ME!
     */
    public void removeLastMedicalRecord() {
        medicalRecords.remove(medicalRecords.size() - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param medicalRecords DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setMedicalRecords(Vector medicalRecords) {
        Iterator iterator;
        boolean valid;

        if (medicalRecords != null) {
            iterator = medicalRecords.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Report;
            }

            if (valid) {
                this.medicalRecords = medicalRecords;
            } else {
                throw new IllegalArgumentException(
                    "The Vector of medical records should contain only Reports.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Vector of medical records shouldn't be null.");
        }
    }
}
