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

package org.jscience.history.archeology;

import org.jscience.geography.Place;

import org.jscience.sociology.Culture;

import org.jscience.util.Positioned;

import java.util.Date;


/**
 * A class representing a civilization, mostly a culture seen though space
 * and time.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this is more than a tribe since it is made not only of living but also of dead individuals
public class Civilization extends Object implements Positioned {
    /** DOCUMENT ME! */
    private Culture culture;

    /** DOCUMENT ME! */
    private Place place;

    /** DOCUMENT ME! */
    private Date startDate;

    /** DOCUMENT ME! */
    private Date endDate;

    /** DOCUMENT ME! */
    private String description; //main characteristics

/**
     * Creates a new Civilization object.
     *
     * @param culture   DOCUMENT ME!
     * @param place     DOCUMENT ME!
     * @param startDate DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Civilization(Culture culture, Place place, Date startDate) {
        if ((culture != null) && (place != null) && (startDate != null)) {
            this.culture = culture;
            this.place = place;
            this.startDate = startDate;
            this.endDate = null;
            this.description = new String();
        } else {
            throw new IllegalArgumentException(
                "The Civilization constructor can't have null arguments (and name can't be empty).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Culture getCulture() {
        return culture;
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
    public Date getStartDate() {
        return startDate;
    }

    //returns null if the civilization still exists (circa 2000)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param endDate DOCUMENT ME!
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @param description DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        } else {
            throw new IllegalArgumentException("The description can't be null.");
        }
    }

    //we could also list events on a typical day (org.jscience.history.Timeline)
}
