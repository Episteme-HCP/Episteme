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

package org.jscience.law;

import org.jscience.biology.Individual;

import org.jscience.economics.MaterialResource;
import org.jscience.economics.WorkSituation;

import org.jscience.measure.Identification;
import org.jscience.measure.Identified;

import org.jscience.politics.Administration;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * A class representing the act of justice in modern countries. As the real
 * process is different from country to country and may be fairly complicated
 * only the raw outline is described here. Also note that an actual justice
 * action may consist of several lawsuits.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//may be we could tell this is a theatrical representation (performance) although, well, not that funny (you wouln't pay to go)
//normally one of the judge is on top of the others although this really depends on the country
//may be we should provide something less formal for primitive societies
public class LawSuitSituation extends WorkSituation implements Identified {
    /** DOCUMENT ME! */
    private Identification identification;

    //the facts
    /** DOCUMENT ME! */
    private Set evidences; //the material brought on which facts are built, a Set of Object, probably of Strings: "7, a long sharp knife."

    //what will we know if we look at it after it is closed
    /** DOCUMENT ME! */
    private Vector transcripts; //a Vector of String

    //the result
    /** DOCUMENT ME! */
    private String sentence;

/**
     * Creates a new LawSuitSituation object.
     *
     * @param identification DOCUMENT ME!
     */
    public LawSuitSituation(Identification identification) {
        super(new String(), new String());
        this.identification = identification;
        evidences = Collections.EMPTY_SET;
        transcripts = new Vector();
        sentence = null;
    }

/**
     * Creates a new LawSuitSituation object.
     *
     * @param name           DOCUMENT ME!
     * @param comments       DOCUMENT ME!
     * @param identification DOCUMENT ME!
     */
    public LawSuitSituation(String name, String comments,
        Identification identification) {
        super(name, comments);
        this.identification = identification;
        evidences = Collections.EMPTY_SET;
        transcripts = new Vector();
        sentence = null;
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
     * @param individual DOCUMENT ME!
     * @param administration DOCUMENT ME!
     */
    public void addJudge(Individual individual, Administration administration) {
        super.addRole(new Judge(individual, this, administration));
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     * @param administration DOCUMENT ME!
     */
    public void addProsecutor(Individual individual,
        Administration administration) {
        super.addRole(new Prosecutor(individual, this, administration));
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     * @param administration DOCUMENT ME!
     */
    public void addLawyer(Individual individual, Administration administration) {
        super.addRole(new Lawyer(individual, this, administration));
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     * @param administration DOCUMENT ME!
     */
    public void addJuryMember(Individual individual,
        Administration administration) {
        super.addRole(new Prosecutor(individual, this, administration));
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void addPlaintiff(Individual individual) {
        super.addRole(new Plaintiff(individual, this));
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void addDefendant(Individual individual) {
        super.addRole(new Defendant(individual, this));
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void addWitness(Individual individual) {
        super.addRole(new Witness(individual, this));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getEvidences() {
        return evidences;
    }

    /**
     * DOCUMENT ME!
     *
     * @param evidences DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //as usually evidences re part of the crime sometimes onece for all may be we should treat them as Artwork or similar
    public void setEvidences(Set evidences) {
        Iterator iterator;
        boolean valid;

        if (evidences != null) {
            iterator = evidences.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof MaterialResource;
            }

            if (valid) {
                this.evidences = evidences;
            } else {
                throw new IllegalArgumentException(
                    "The Set of evidences should contain only MaterialResource.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Vector of evidences shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getTranscripts() {
        return transcripts;
    }

    /**
     * DOCUMENT ME!
     *
     * @param transcript DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addTranscript(String transcript) {
        if (transcript != null) {
            transcripts.add(transcript);
        } else {
            throw new IllegalArgumentException(
                "You can't add a null transcript.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param transcript DOCUMENT ME!
     */
    public void removeTranscript(String transcript) {
        transcripts.remove(transcript);
    }

    /**
     * DOCUMENT ME!
     */
    public void removeLastTranscript() {
        if (transcripts.size() > 0) {
            transcripts.removeElementAt(transcripts.size() - 1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param transcripts DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setTranscripts(Vector transcripts) {
        Iterator iterator;
        boolean valid;

        if (transcripts != null) {
            iterator = transcripts.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof String;
            }

            if (valid) {
                this.transcripts = transcripts;
            } else {
                throw new IllegalArgumentException(
                    "The Vector of transcripts should contain only Strings.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Vector of transcripts shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSentence() {
        return sentence;
    }

    //should be set only once
    /**
     * DOCUMENT ME!
     *
     * @param sentence DOCUMENT ME!
     */
    public void setSentence(String sentence) {
        if (!isOver()) {
            if ((sentence != null) && (sentence.length() > 0)) {
                this.sentence = sentence;
            } else {
                throw new IllegalArgumentException(
                    "The sentence shouldn't be null or empty.");
            }
        } else {
            throw new IllegalArgumentException(
                "The sentence has already been set.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOver() {
        return sentence != null;
    }
}
