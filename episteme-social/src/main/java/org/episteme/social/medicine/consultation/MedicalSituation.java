/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.medicine.consultation;

import org.episteme.natural.biology.Individual;

import org.episteme.social.economics.Organization;
import org.episteme.social.economics.WorkSituation;


/**
 * A class representing the interaction of people around a common activity
 * or conflict. Situations happen usually at dedicated places.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.1
 * @since 1.0
 */

//you may prefer this class to org.episteme.social.sociology.Situations.WORKING
//may be we should also define a Nurse class
public class MedicalSituation extends WorkSituation {
    //use the organization name as the name, or a part of it if your organization is big
    /**
     * Creates a new MedicalSituation object.
     *
     * @param name the name of the medical situation
     * @param comments additional comments or observations
     */
    public MedicalSituation(String name, String comments) {
        super(name, comments);
    }

    //builds out a doctor
    /**
     * Adds a doctor to the medical situation.
     *
     * @param individual the individual taking the doctor role
     * @param organization the organization the doctor belongs to
     */
    public void addDoctor(Individual individual, Organization organization) {
        super.addRole(new Doctor(individual, this, organization));
    }

    //builds out a patient
    /**
     * Adds a patient to the medical situation.
     *
     * @param individual the individual taking the patient role
     */
    public void addPatient(Individual individual) {
        super.addRole(new Patient(individual, this));
    }
}

