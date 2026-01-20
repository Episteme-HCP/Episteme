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
