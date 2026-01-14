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

import org.jscience.util.Commented;
import org.jscience.util.Named;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * The Pathology class provides a common ancestor class for all possible
 * medicine related troubles.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//pathologies exist even if there are no patient to be ill, this is why we haven't put any Patient callback
//disease, allergy, bone break, psychological...
public class Pathology extends Object implements Named, Commented {
    /** DOCUMENT ME! */
    private String name; //the scientific name

    /** DOCUMENT ME! */
    private String cause; //how the Pathology appeared

    /** DOCUMENT ME! */
    private String comments; //how the Pathology evolves, etc

    /** DOCUMENT ME! */
    private Set treatments;

/**
     * Creates a new Pathology object.
     *
     * @param name DOCUMENT ME!
     */
    public Pathology(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            this.cause = null;
            this.comments = null;
            this.treatments = new HashSet();
        } else {
            throw new IllegalArgumentException(
                "The Pathology constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCause() {
        return cause;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cause DOCUMENT ME!
     */
    public void setCause(String cause) {
        this.cause = cause;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComments() {
        return comments;
    }

    /**
     * DOCUMENT ME!
     *
     * @param comments DOCUMENT ME!
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getTreatments() {
        return treatments;
    }

    /**
     * DOCUMENT ME!
     *
     * @param treatments DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //we don't call Treatment.setPathology(this) for every member of the Set although this should normally be consistent
    //all members of the Set should be treatments
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
}
