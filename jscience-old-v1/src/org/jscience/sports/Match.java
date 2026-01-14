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

import java.util.Date;


/**
 * A class representing one meeting in a sport competition.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//may be we should provide a way to store matches in XML
public class Match extends Object {
    /** DOCUMENT ME! */
    private Sport sport;

    /** DOCUMENT ME! */
    private Category category;

    /** DOCUMENT ME! */
    private Date date;

    /** DOCUMENT ME! */
    private Individual[] opponentsIndiviuals;

    /** DOCUMENT ME! */
    private Team[] opponentsTeams;

    /** DOCUMENT ME! */
    private double[] scores;

    //when singles are competing against each others
/**
     * Creates a new Match object.
     *
     * @param sport     DOCUMENT ME!
     * @param category  DOCUMENT ME!
     * @param date      DOCUMENT ME!
     * @param opponents DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Match(Sport sport, Category category, Date date,
        Individual[] opponents) {
        if ((sport != null) && (category != null) && (date != null) &&
                (opponents != null)) {
            this.sport = sport;
            this.category = category;
            this.date = date;
            this.opponentsIndiviuals = opponents;
            this.opponentsTeams = null;
            this.scores = null;
        } else {
            throw new IllegalArgumentException(
                "The Match constructor can't have null arguments.");
        }
    }

    //an array of teams (itself array of individuals)
/**
     * Creates a new Match object.
     *
     * @param sport     DOCUMENT ME!
     * @param category  DOCUMENT ME!
     * @param date      DOCUMENT ME!
     * @param opponents DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Match(Sport sport, Category category, Date date, Team[] opponents) {
        if ((sport != null) && (category != null) && (date != null) &&
                (opponents != null)) {
            this.sport = sport;
            this.category = category;
            this.date = date;
            this.opponentsIndiviuals = null;
            this.opponentsTeams = opponents;
            this.scores = null;
        } else {
            throw new IllegalArgumentException(
                "The Match constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Sport getSport() {
        return sport;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Category getCategory() {
        return category;
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
    public boolean isTeam() {
        return opponentsTeams != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getOpponents() {
        if (opponentsIndiviuals != null) {
            return opponentsIndiviuals;
        } else {
            return opponentsTeams;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFinished() {
        return scores != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getScores() {
        return scores;
    }

    //the successives scores of each individual or team according to the rules set for the sport
    /**
     * DOCUMENT ME!
     *
     * @param scores DOCUMENT ME!
     */
    public void setScore(double[] scores) {
        this.scores = scores;
    }
}
