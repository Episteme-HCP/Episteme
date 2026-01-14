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

package org.jscience.measure;

import org.jscience.geography.Place;

import org.jscience.util.Commented;
import org.jscience.util.Positioned;

import java.util.Date;


/**
 * A class representing a piece of something
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

// we could also define an Experiment class as a collection of Measures but Analysis, Description, Sample and Report are here for this purpose
public class Sample extends Object implements java.io.Serializable, Identified,
    Commented, Positioned {
    /** DOCUMENT ME! */
    private String name; //name of test

    /** DOCUMENT ME! */
    private Identification identification; //unique identification value

    /** DOCUMENT ME! */
    private Date date;

    /** DOCUMENT ME! */
    private Place place;

    /** DOCUMENT ME! */
    private Object sample; //the thing that is tested, for example an Artwork, an Item, an Individual, a Tissue

    /** DOCUMENT ME! */
    private String comments;

    //there is no set method because you don't change your sample, you perform a new one
    /**
     * Creates a new Sample object.
     *
     * @param name DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param date DOCUMENT ME!
     * @param place DOCUMENT ME!
     * @param sample DOCUMENT ME!
     */
    public Sample(String name, Identification identification, Date date,
        Place place, Object sample) {
        if ((name != null) && (name.length() > 0) && (identification != null) &&
                (date != null) && (place != null) && (sample != null)) {
            this.name = name;
            this.identification = identification;
            this.date = date;
            this.place = place;
            this.sample = sample;
            this.comments = null;
        } else {
            throw new IllegalArgumentException(
                "The Sample constructor can't have null arguments (and name can't be empty).");
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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Identification getIdentification() {
        return identification;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getDate() {
        return date;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getPosition() {
        return place;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getSample() {
        return sample;
    }

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
}
