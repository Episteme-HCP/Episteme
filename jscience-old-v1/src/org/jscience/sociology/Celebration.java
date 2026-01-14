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

package org.jscience.sociology;

import org.jscience.biology.Individual;

import org.jscience.economics.Organization;
import org.jscience.economics.WorkSituation;

import org.jscience.geography.Place;

import org.jscience.util.Positioned;

import java.util.Date;


/**
 * A class representing a cultural event (fair, happening, feast...).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this also accounts for many traditional behaviors, rites, etc.
//there is usually a master of ceremony for celebrations (role)
public class Celebration extends WorkSituation implements Positioned {
    /** DOCUMENT ME! */
    public final static int BIRTH = 1;

    /** DOCUMENT ME! */
    public final static int INITIATION = 2;

    /** DOCUMENT ME! */
    public final static int PUBERTY = 4; //(see bar mitzvah, bat mitzvah)

    /** DOCUMENT ME! */
    public final static int SOCIAL_ADULTHOOD = 8;

    /** DOCUMENT ME! */
    public final static int GRADUATION = 16;

    /** DOCUMENT ME! */
    public final static int BAPTISM = 32;

    /** DOCUMENT ME! */
    public final static int MARRIAGE = 64;

    /** DOCUMENT ME! */
    public final static int DEATH = 128;

    /** DOCUMENT ME! */
    public final static int BURIAL = 256;

    /** DOCUMENT ME! */
    public final static int PEACE = 512;

    /** DOCUMENT ME! */
    public final static int VICTORY = 1024;

    /** DOCUMENT ME! */
    public final static int WAR = 2048;

    /** DOCUMENT ME! */
    public final static int CORONATION = 4096;

    /** DOCUMENT ME! */
    public final static int SUN = 8192;

    /** DOCUMENT ME! */
    public final static int MOON = 16384;

    /** DOCUMENT ME! */
    public final static int EARTH = 32768;

    /** DOCUMENT ME! */
    public final static int SEASON = 65536; //vernal equinox, winter solstice

    /** DOCUMENT ME! */
    public final static int SUCCESS = 65536 * 2;

    /** DOCUMENT ME! */
    public final static int PLEASURE = 65536 * 4;

    /** DOCUMENT ME! */
    public final static int BUSINESS = 65536 * 8;

    /** DOCUMENT ME! */
    public final static int CALENDAR_SPECIFIC = 65536 * 16; //  occasions in a liturgical year or "feasts" in a calendar of saints

    /** DOCUMENT ME! */
    public final static int EVENT_SPECIFIC = 65536 * 32; //grand opening

    /** DOCUMENT ME! */
    public final static int OTHER = 65536 * 64;

    // weekly Sabbath day
    //inauguration of an elected office-holder
    /** DOCUMENT ME! */
    private Place place;

    /** DOCUMENT ME! */
    private int kind;

    /** DOCUMENT ME! */
    private Date start;

    /** DOCUMENT ME! */
    private Date end;

    /** DOCUMENT ME! */
    private Celebration celebration;

/**
     * Creates a new Celebration object.
     *
     * @param name     DOCUMENT ME!
     * @param place    DOCUMENT ME!
     * @param kind     DOCUMENT ME!
     * @param start    DOCUMENT ME!
     * @param end      DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public Celebration(String name, Place place, int kind, Date start,
        Date end, String comments) {
        super(name, comments);

        if ((place != null) && (start != null) && (end != null)) {
            this.place = place;
            this.kind = kind;
            this.start = start;
            this.end = end;
        } else {
            throw new IllegalArgumentException(
                "The Celebration constructor doesn't accept null or empty arguments (apart from people).");
        }
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
    public int getKind() {
        return kind;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getStart() {
        return start;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getEnd() {
        return end;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Celebration getNext() {
        return celebration;
    }

    //when the next similar event occurs if any
    /**
     * DOCUMENT ME!
     *
     * @param celebration DOCUMENT ME!
     */
    public void setNext(Celebration celebration) {
        this.celebration = celebration;
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     * @param organization DOCUMENT ME!
     */
    public void addMasterOfCeremony(Individual individual,
        Organization organization) {
        super.addRole(new MasterOfCeremony(individual, this, organization));
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void addMember(Individual individual) {
        super.addRole(new Member(individual, this));
    }
}
