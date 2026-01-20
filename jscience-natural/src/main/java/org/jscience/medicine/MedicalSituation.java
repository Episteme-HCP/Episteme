package org.jscience.medicine;

import org.jscience.biology.Individual;

import org.jscience.economics.Organization;
import org.jscience.economics.WorkSituation;


/**
 * A class representing the interaction of people around a common activity
 * or conflict. Situations happen usually at dedicated places.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you may prefer this class to org.jscience.sociology.Situations.WORKING
//may be we should also define a Nurse class
public class MedicalSituation extends WorkSituation {
    //use the organization name as the name, or a part of it if your organization is big
    /**
     * Creates a new MedicalSituation object.
     *
     * @param name DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public MedicalSituation(String name, String comments) {
        super(name, comments);
    }

    //builds out a doctor
    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     * @param organization DOCUMENT ME!
     */
    public void addDoctor(Individual individual, Organization organization) {
        super.addRole(new Doctor(individual, this, organization));
    }

    //builds out a patient
    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void addPatient(Individual individual) {
        super.addRole(new Patient(individual, this));
    }
}
