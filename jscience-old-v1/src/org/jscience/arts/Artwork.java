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

package org.jscience.arts;

import org.jscience.biology.Individual;
import org.jscience.economics.Community;
import org.jscience.economics.money.Money;
import org.jscience.economics.resources.Artifact;
import org.jscience.geography.Place;
import org.jscience.measure.Amount;
import org.jscience.measure.Analysis;
import org.jscience.measure.Identification;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a piece of art.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//we also could extend org.jscience.economics.resources.Object instead of org.jscience.economics.resources.Artifact
//should you want to represent a TV program, you should use a timeline of Performances or a more complicated system
//we do not provide an identification system because they are usually art dependant and highly specific
//although we do not provide any direct support for the actual location of the piece of art (or artwork), the owners field can provide a hint
//moreoever you can actually define a museum as a Collection, and museum don't move (usually)
//stolen, destroyed or lost artwork could be described using an extra field not given here
//as well as the estimated price (with date and currency) for the artwork
public class Artwork extends Artifact {
    /**
     * DOCUMENT ME!
     */
    private Set authors; //a set of Humans

    /**
     * DOCUMENT ME!
     */
    private int art; //the major category if any

    /**
     * DOCUMENT ME!
     */
    private Set analysis; //a set of Analysis for example proving that this is the original piece of art

    /**
     * DOCUMENT ME!
     */
    private Set restorations; //a set of Restorations

    /**
     * Creates a new Artwork object.
     *
     * @param name            DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @param amount          DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     * @param identification  DOCUMENT ME!
     * @param value           DOCUMENT ME!
     */
    public Artwork(String name, String description, Amount amount,
                   Community producer, Place productionPlace, Date productionDate,
                   Identification identification, Amount<Money> value) {
        this(name, description, amount, producer, productionPlace,
                productionDate, identification, value, producer.getIndividuals(),
                ArtsConstants.UNKNOWN);
    }

    /**
     * Creates a new Artwork object.
     *
     * @param name            DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @param amount          DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     * @param identification  DOCUMENT ME!
     * @param value           DOCUMENT ME!
     * @param authors         DOCUMENT ME!
     */
    public Artwork(String name, String description, Amount amount,
                   Community producer, Place productionPlace, Date productionDate,
                   Identification identification, Amount<Money> value, Set authors) {
        this(name, description, amount, producer, productionPlace,
                productionDate, identification, value, authors,
                ArtsConstants.UNKNOWN);
    }

    //the Set of authors should consist only of individuals
    //yes it happens that there is animal artwork
    public Artwork(String name, String description, Amount amount,
                   Community producer, Place productionPlace, Date productionDate,
                   Identification identification, Amount<Money> value, Set authors, int art) {
        super(name, description, amount, producer, productionPlace,
                productionDate, identification, value);

        Iterator iterator;
        boolean valid;

        if ((authors != null) && (authors.size() > 0)) {
            iterator = authors.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Individual;
            }

            if (valid) {
                this.authors = authors;
                this.art = art;
                this.analysis = Collections.EMPTY_SET;
                this.restorations = Collections.EMPTY_SET;
            } else {
                throw new IllegalArgumentException(
                        "The Set of authors should contain only Individuals.");
            }
        } else {
            throw new IllegalArgumentException(
                    "The Artwork constructor can't have null arguments (and name and authors and owners shouldn't be empty).");
        }
    }

    //a Set of Individuals
    public Set getAuthors() {
        return authors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param authors DOCUMENT ME!
     */
    public void setAuthors(Set authors) {
        Iterator iterator;
        boolean valid;

        if ((authors != null) && (authors.size() > 0)) {
            iterator = authors.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Individual;
            }

            if (valid) {
                this.authors = authors;
            } else {
                throw new IllegalArgumentException(
                        "The Set of authors should contain only Individuals.");
            }
        } else {
            throw new IllegalArgumentException(
                    "You can't set null or empty authors.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getArt() {
        return art;
    }

    //a Set of Analysis
    public Set getAnalysis() {
        return analysis;
    }

    /**
     * DOCUMENT ME!
     *
     * @param analysis DOCUMENT ME!
     */
    public void addAnalysis(Analysis analysis) {
        this.analysis.add(analysis);
    }

    /**
     * DOCUMENT ME!
     *
     * @param analysis DOCUMENT ME!
     */
    public void removeAnalysis(Analysis analysis) {
        this.analysis.remove(analysis);
    }

    //a Set of Analysis
    public void setAnalysis(Set analysis) {
        Iterator iterator;
        boolean valid;

        if (analysis != null) {
            iterator = analysis.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Analysis;
            }

            if (valid) {
                this.analysis = analysis;
            } else {
                throw new IllegalArgumentException(
                        "The Set of analysis should contain only Analysis.");
            }
        } else {
            throw new IllegalArgumentException(
                    "The Set of analysis can't be null.");
        }
    }

    //a Set of Restorations
    public Set getRestorations() {
        return restorations;
    }

    /**
     * DOCUMENT ME!
     *
     * @param restoration DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addRestoration(Restoration restoration) {
        if (restoration != null) {
            restorations.add(restoration);
        } else {
            throw new IllegalArgumentException(
                    "You can't add a null restoration.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param restoration DOCUMENT ME!
     */
    public void removeRestoration(Restoration restoration) {
        restorations.remove(restoration);
    }

    //a Set of Restorations
    public void setRestorations(Set restorations) {
        Iterator iterator;
        boolean valid;

        if (restorations != null) {
            iterator = restorations.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Restoration;
            }

            if (valid) {
                this.restorations = restorations;
            } else {
                throw new IllegalArgumentException(
                        "The Set of restorations should contain only Restorations.");
            }
        } else {
            throw new IllegalArgumentException(
                    "The Set of restorations can't be null.");
        }
    }

    //checks identifications correspond and only identification
    public boolean equals(Object o) {
        if ((o != null) && (o instanceof Artwork)) {
            return this.getIdentification().equals(((Artwork) o).getIdentification());
        } else {
            return false;
        }
    }
}
