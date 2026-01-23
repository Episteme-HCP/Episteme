package org.jscience.medicine;

import org.jscience.biology.Individual;

import org.jscience.economics.Organization;
import org.jscience.economics.Worker;

import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * The Doctor class provides some useful information for people whose job
 * is to cure individuals.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//may be we should extend WorkSituation to provide a MedicalSituation
@Persistent
public class Doctor extends Worker {
    /** The set of patients currently under this doctor's care. */
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Patient> patients;

    /**
     * Initializes a new Doctor instance with a specific medical function.
     *
     * @param individual       the underlying human individual
     * @param medicalSituation the medical context (situation) the doctor is involved in
     * @param function         the specific title or function of the doctor
     * @param organization     the medical organization (hospital, clinic, etc.) they work for
     * @throws NullPointerException if any required argument is null
     */
    public Doctor(Individual individual, MedicalSituation medicalSituation,
        String function, Organization organization) {
        super(Objects.requireNonNull(individual, "Individual cannot be null"), 
              Objects.requireNonNull(medicalSituation, "Situation cannot be null"), 
              Objects.requireNonNull(function, "Function cannot be null"), 
              Objects.requireNonNull(organization, "Organization cannot be null"));
        this.patients = new HashSet<>();
    }

    /**
     * Initializes a new Doctor instance with the default function "Doctor".
     *
     * @param individual       the underlying human individual
     * @param medicalSituation the medical context (situation)
     * @param organization     the medical organization they work for
     * @throws NullPointerException if any required argument is null
     */
    public Doctor(Individual individual, MedicalSituation medicalSituation,
        Organization organization) {
        this(individual, medicalSituation, "Doctor", organization);
    }

    /**
     * Returns the set of patients under this doctor's care.
     *
     * @return an unmodifiable view of the patient set
     */
    public Set<Patient> getPatients() {
        return Collections.unmodifiableSet(patients);
    }

    /**
     * Replaces the current set of patients. Each element must be a Patient instance.
     *
     * @param patients the new set of patients
     * @throws NullPointerException if the input set is null
     */
    public void setPatients(Set<Patient> patients) {
        this.patients = new HashSet<>(Objects.requireNonNull(patients, "Patients set cannot be null"));
    }

    /**
     * Adds a patient to this doctor's care.
     *
     * @param patient the patient to add
     * @throws NullPointerException if the patient is null
     */
    public void addPatient(Patient patient) {
        patients.add(Objects.requireNonNull(patient, "Patient cannot be null"));
    }

    /**
     * Removes a patient from this doctor's care.
     *
     * @param patient the patient to remove
     */
    public void removePatient(Patient patient) {
        patients.remove(patient);
    }
}
