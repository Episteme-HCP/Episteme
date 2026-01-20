package org.jscience.law;

import org.jscience.biology.Individual;

import org.jscience.measure.Report;

import org.jscience.sociology.Role;

import java.util.Iterator;
import java.util.Vector;


/**
 * A class representing an individual in an organized country basic facts.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//don't expect any of these records to be complete until the death of the individual
public class ResponsibleIndividual extends Role {
    /** DOCUMENT ME! */
    private Vector schoolRecords; //including diplomas, Vector of strings, use identification or equivalent to define each String

    /** DOCUMENT ME! */
    private Vector policeRecords; //Set of strings, use identification or equivalent to define each String

    /** DOCUMENT ME! */
    private Biometrics biometrics;

/**
     * Creates a new ResponsibleIndividual object.
     *
     * @param individual DOCUMENT ME!
     * @param situation  DOCUMENT ME!
     */
    public ResponsibleIndividual(Individual individual,
        StreetSituation situation) {
        super(individual, "Responsible Individual", situation, Role.CLIENT);
        this.schoolRecords = new Vector();
        this.policeRecords = new Vector();
        this.biometrics = null;
    }

    //returns the complete list of school records sorted by date
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getSchoolRecords() {
        return schoolRecords;
    }

    /**
     * DOCUMENT ME!
     *
     * @param schoolRecord DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addSchoolRecord(License schoolRecord) {
        if (schoolRecords != null) {
            schoolRecords.add(schoolRecord);
        } else {
            throw new IllegalArgumentException(
                "You can't add a null school record.");
        }
    }

    //this is very rare
    /**
     * DOCUMENT ME!
     *
     * @param schoolRecord DOCUMENT ME!
     */
    public void removeSchoolRecord(License schoolRecord) {
        schoolRecords.remove(schoolRecord);
    }

    //this is very rare
    /**
     * DOCUMENT ME!
     */
    public void removeLastSchoolRecord() {
        schoolRecords.remove(schoolRecords.size() - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param schoolRecords DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setSchoolRecords(Vector schoolRecords) {
        Iterator iterator;
        boolean valid;

        if (schoolRecords != null) {
            iterator = schoolRecords.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof License;
            }

            if (valid) {
                this.schoolRecords = schoolRecords;
            } else {
                throw new IllegalArgumentException(
                    "The Vector of school records should contain only Licences.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Vector of school records shouldn't be null.");
        }
    }

    //returns the complete list of police records sorted by date
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getPoliceRecords() {
        return policeRecords;
    }

    /**
     * DOCUMENT ME!
     *
     * @param policeRecord DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addPoliceRecord(Report policeRecord) {
        if (policeRecord != null) {
            policeRecords.add(policeRecord);
        } else {
            throw new IllegalArgumentException(
                "You can't add a null police record.");
        }
    }

    //this is very rare
    /**
     * DOCUMENT ME!
     *
     * @param policeRecord DOCUMENT ME!
     */
    public void removePoliceRecord(Report policeRecord) {
        policeRecords.remove(policeRecord);
    }

    //this is very rare
    /**
     * DOCUMENT ME!
     */
    public void removeLastPoliceRecord() {
        policeRecords.remove(policeRecords.size() - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param policeRecords DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setPoliceRecords(Vector policeRecords) {
        Iterator iterator;
        boolean valid;

        if (policeRecords != null) {
            iterator = policeRecords.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Report;
            }

            if (valid) {
                this.policeRecords = policeRecords;
            } else {
                throw new IllegalArgumentException(
                    "The Vector of police records should contain only Reports.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Vector of police records shouldn't be null.");
        }
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Biometrics getBiometrics() {
        return biometrics;
    }

    //the current biometrics
    /**
     * DOCUMENT ME!
     *
     * @param biometrics DOCUMENT ME!
     */
    public void setBiometrics(Biometrics biometrics) {
        this.biometrics = biometrics;
    }
}
