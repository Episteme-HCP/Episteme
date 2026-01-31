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

package org.jscience.social.medicine.consultation;

import org.jscience.natural.biology.Individual;
import org.jscience.core.methodology.ScientificReport;
import org.jscience.social.sociology.Role;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Relation;
import org.jscience.natural.medicine.Pathology;
import org.jscience.natural.medicine.Treatment;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;


/**
 * The Patient class provides some useful information about the health of
 * an individual.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.1
 * @since 1.0
 */

//some variables are relevant only for vertebrates (but this is mostly the kind of cases a veterinary or physics has)
@Persistent
public class Patient extends Role {
    //http://en.wikipedia.org/wiki/Vital_signs
    @Attribute
    private float bloodPressure; //using standard international unit Pascal

    @Attribute
    private float cardiacRate; //beat per seconds, mean over short period of seconds

    @Attribute
    private float temperature; //kelvin degrees

    @Attribute
    private float normalTemperature; //kelvin degrees

    @Attribute
    private float respiratoryRate; //beats per seconds

    @Attribute
    private float painScale; //0 to 10

    @Attribute
    private float bloodOxygen; //percent of normal

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Pathology> currentPathologies; //diseases, defects,etc a Set of Pathologies

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Treatment> treatments;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<ScientificReport> medicalReports; //List of Records

/**
     * Creates a new Patient object.
     *
     * @param individual the individual who is a patient
     * @param situation  the medical situation context
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
        this.currentPathologies = Collections.emptySet();
        this.treatments = Collections.emptySet();
        this.medicalReports = new ArrayList<>();
    }

    public Patient(Individual individual) {
        super(individual, "Patient", null, Role.CLIENT); // Assuming a default MedicalSituation or null is acceptable
        this.bloodPressure = 0;
        this.cardiacRate = 0;
        this.temperature = 0;
        this.normalTemperature = 0;
        this.respiratoryRate = 0;
        this.painScale = 0;
        this.bloodOxygen = 0;
        this.currentPathologies = Collections.emptySet();
        this.treatments = Collections.emptySet();
        this.medicalReports = new ArrayList<>();
    }

    /**
     * Returns the blood pressure of the patient.
     *
     * @return the blood pressure in Pascals
     */
    public float getBloodPressure() {
        return bloodPressure;
    }

    /**
     * Sets the blood pressure of the patient.
     *
     * @param pressure the blood pressure in Pascals
     */
    public void setBloodPressure(float pressure) {
        this.bloodPressure = pressure;
    }

    /**
     * Returns the cardiac rate of the patient.
     *
     * @return the cardiac rate in beats per second
     */
    public float getCardiacRate() {
        return cardiacRate;
    }

    /**
     * Sets the cardiac rate of the patient.
     *
     * @param rate the cardiac rate in beats per second
     */
    public void setCardiacRate(float rate) {
        this.cardiacRate = rate;
    }

    //we could also have a field for normal cardiac rate of an adult individual from this Species at rest
    /**
     * Returns the body temperature of the patient.
     *
     * @return the temperature in Kelvin
     */
    public float getTemperature() {
        return temperature;
    }

    /**
     * Sets the body temperature of the patient.
     *
     * @param temperature the temperature in Kelvin
     */
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    //the normal temperature for this Species, assuming a healthy adult individual
    /**
     * Returns the normal body temperature for this patient's species.
     *
     * @return the normal temperature in Kelvin
     */
    public float getNormalTemperature() {
        return normalTemperature;
    }

    /**
     * Sets the normal body temperature for this patient's species.
     *
     * @param normalTemperature the normal temperature in Kelvin
     */
    public void setNormalTemperature(float normalTemperature) {
        this.normalTemperature = normalTemperature;
    }

    /**
     * Returns the respiratory rate of the patient.
     *
     * @return the respiratory rate in breaths per second
     */
    public float getRespiratoryRate() {
        return respiratoryRate;
    }

    /**
     * Sets the respiratory rate of the patient.
     *
     * @param respiratoryRate the respiratory rate in breaths per second
     */
    public void setRespiratoryRate(float respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    /**
     * Returns the pain scale level of the patient.
     *
     * @return the pain scale value (0 to 10)
     */
    public float getPainScale() {
        return painScale;
    }

    /**
     * Sets the pain scale level of the patient.
     *
     * @param painScale the pain scale value (0 to 10)
     */
    public void setPainScale(float painScale) {
        this.painScale = painScale;
    }

    /**
     * Returns the blood oxygen level of the patient.
     *
     * @return the blood oxygen percentage
     */
    public float getBloodOxygen() {
        return bloodOxygen;
    }

    /**
     * Sets the blood oxygen level of the patient.
     *
     * @param bloodOxygen the blood oxygen percentage
     */
    public void setBloodOxygen(float bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }

    /**
     * Returns the set of current pathologies affecting the patient.
     *
     * @return an unmodifiable set of current pathologies
     */
    public Set<Pathology> getCurrentPathologies() {
        return Collections.unmodifiableSet(currentPathologies);
    }

    /**
     * Updates the set of current pathologies. Each element in the set must be a Pathology instance.
     *
     * @param pathologies the new set of pathologies
     * @throws NullPointerException if the input set is null
     */
    public void setCurrentPathologies(Set<Pathology> pathologies) {
        this.currentPathologies = new HashSet<>(Objects.requireNonNull(pathologies, "Pathologies set cannot be null"));
    }

    /**
     * Adds a pathology to the patient's current condition.
     *
     * @param pathology the pathology to add
     * @throws NullPointerException if the pathology is null
     */
    public void addPathology(Pathology pathology) {
        currentPathologies.add(Objects.requireNonNull(pathology, "Pathology cannot be null"));
    }

    /**
     * Removes a pathology from the patient's current condition.
     *
     * @param pathology the pathology to remove
     */
    public void removePathology(Pathology pathology) {
        currentPathologies.remove(pathology);
    }

    /**
     * Returns the set of medical treatments being administered.
     *
     * @return an unmodifiable set of treatments
     */
    public Set<Treatment> getTreatments() {
        return Collections.unmodifiableSet(treatments);
    }

    /**
     * Updates the set of current treatments.
     *
     * @param treatments the new set of treatments
     * @throws NullPointerException if the input set is null
     */
    public void setTreatments(Set<Treatment> treatments) {
        this.treatments = new HashSet<>(Objects.requireNonNull(treatments, "Treatments set cannot be null"));
    }

    /**
     * Records a new medical treatment for the patient.
     *
     * @param treatment the treatment to record
     * @throws NullPointerException if the treatment is null
     */
    public void addTreatment(Treatment treatment) {
        treatments.add(Objects.requireNonNull(treatment, "Treatment cannot be null"));
    }

    /**
     * Removes a recorded treatment.
     *
     * @param treatment the treatment to remove
     */
    public void removeTreatment(Treatment treatment) {
        treatments.remove(treatment);
    }

    /**
     * Returns the complete chronologically ordered list of medical records.
     *
     * @return an unmodifiable view of medical records
     */
    public List<ScientificReport> getMedicalReports() {
        return new ArrayList<>(medicalReports);
    }

    public void addMedicalReport(ScientificReport report) {
        if (report != null) {
            medicalReports.add(report);
        }
    }

    public void removeMedicalReport(ScientificReport report) {
        medicalReports.remove(report);
    }

    public void setMedicalReports(List<ScientificReport> reports) {
        if (reports != null) {
            this.medicalReports.clear();
            this.medicalReports.addAll(reports);
        }
    }

    /**
     * Removes a medical record from the history.
     *
     * @param medicalRecord the record to remove
     */
    public void removeMedicalRecord(ScientificReport medicalRecord) {
        medicalReports.remove(medicalRecord);
    }

    /**
     * Removes the most recently added medical record.
     *
     * @throws IndexOutOfBoundsException if there are no records to remove
     */
    public void removeLastMedicalRecord() {
        if (!medicalReports.isEmpty()) {
            medicalReports.remove(medicalReports.size() - 1);
        }
    }
}

