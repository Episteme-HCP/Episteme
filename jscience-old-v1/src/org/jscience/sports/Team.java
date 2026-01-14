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

package org.jscience.sports;

import org.jscience.biology.Individual;

import org.jscience.util.Named;

import java.util.Date;


/**
 * A class representing a group of competitors going together.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Team extends Object implements Named {
    /** DOCUMENT ME! */
    private String name; //the given name

    /** DOCUMENT ME! */
    private String description; //a lenghty description of the category

    /** DOCUMENT ME! */
    private Date season;

    /** DOCUMENT ME! */
    private Individual[] members;

/**
     * Creates a new Team object.
     *
     * @param name        DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param season      DOCUMENT ME!
     * @param members     DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Team(String name, String description, Date season,
        Individual[] members) {
        if ((name != null) && (description != null) && (season != null) &&
                (members != null)) {
            this.name = name;
            this.description = description;
            this.season = season;
            this.members = members;
        } else {
            throw new IllegalArgumentException(
                "The Team constructor can't have null arguments.");
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
    public String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getDate() {
        return season;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Individual[] getMembers() {
        return members;
    }
}
