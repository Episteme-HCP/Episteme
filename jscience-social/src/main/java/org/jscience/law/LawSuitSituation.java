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
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;
import org.jscience.politics.Administration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the process and context of a lawsuit or trial.
 * Since legal processes vary greatly between jurisdictions, this class provides 
 * a generalized structure for organizing evidence, transcripts, roles, and outcomes.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 */
public class LawSuitSituation extends WorkSituation implements Identified {
    
    private Identification identification;
    private Set<MaterialResource> evidences;
    private List<String> transcripts;
    private String sentence;

    /**
     * Creates a new LawSuitSituation with a specific identification.
     *
     * @param identification the unique identifier for this situation
     */
    public LawSuitSituation(Identification identification) {
        this(identification.getName(), identification.getComments(), identification);
    }

    /**
     * Creates a new LawSuitSituation with specific name, comments, and identification.
     *
     * @param name the name of the lawsuit
     * @param comments additional comments or context
     * @param identification the unique identification object
     */
    public LawSuitSituation(String name, String comments, Identification identification) {
        super(name, comments);
        this.identification = identification;
        this.evidences = new HashSet<>();
        this.transcripts = new ArrayList<>();
        this.sentence = null;
    }

    @Override
    public Identification getIdentification() {
        return identification;
    }

    /**
     * Adds a judge to the lawsuit.
     *
     * @param individual the individual taking the role
     * @param administration the judicial administration
     */
    public void addJudge(Individual individual, Administration administration) {
        super.addRole(new Judge(individual, this, administration));
    }

    /**
     * Adds a prosecutor to the lawsuit.
     *
     * @param individual the individual taking the role
     * @param administration the judicial administration
     */
    public void addProsecutor(Individual individual, Administration administration) {
        super.addRole(new Prosecutor(individual, this, administration));
    }

    /**
     * Adds a lawyer to the lawsuit.
     *
     * @param individual the individual taking the role
     * @param administration the judicial administration
     */
    public void addLawyer(Individual individual, Administration administration) {
        super.addRole(new Lawyer(individual, this, administration));
    }

    /**
     * Adds a jury member to the lawsuit.
     *
     * @param individual the individual taking the role
     * @param administration the judicial administration
     */
    public void addJuryMember(Individual individual, Administration administration) {
        super.addRole(new JuryMember(individual, this, administration));
    }

    /**
     * Adds a plaintiff to the lawsuit.
     *
     * @param individual the individual taking the role
     */
    public void addPlaintiff(Individual individual) {
        super.addRole(new Plaintiff(individual, this));
    }

    /**
     * Adds a defendant to the lawsuit.
     *
     * @param individual the individual taking the role
     */
    public void addDefendant(Individual individual) {
        super.addRole(new Defendant(individual, this));
    }

    /**
     * Adds a witness to the lawsuit.
     *
     * @param individual the individual taking the role
     */
    public void addWitness(Individual individual) {
        super.addRole(new Witness(individual, this));
    }

    /**
     * Returns the set of evidences (material resources) associated with the case.
     * @return a set of evidences
     */
    public Set<MaterialResource> getEvidences() {
        return evidences;
    }

    /**
     * Sets the set of evidences for this case.
     *
     * @param evidences a set of MaterialResource objects
     * @throws IllegalArgumentException if the set is null or contains non-MaterialResource elements
     */
    public void setEvidences(Set<MaterialResource> evidences) {
        if (evidences == null) {
            throw new IllegalArgumentException("Evidences cannot be null.");
        }
        
        for (Object evidence : evidences) {
            if (!(evidence instanceof MaterialResource)) {
                throw new IllegalArgumentException("The set of evidences must contain only MaterialResource objects.");
            }
        }
        
        this.evidences = new HashSet<>(evidences);
    }

    /**
     * Returns the list of transcripts for the proceeding.
     * @return the list of transcript strings
     */
    public List<String> getTranscripts() {
        return transcripts;
    }

    /**
     * Adds a new transcript to the record.
     *
     * @param transcript the transcript content
     * @throws IllegalArgumentException if transcript is null
     */
    public void addTranscript(String transcript) {
        if (transcript == null) {
            throw new IllegalArgumentException("Cannot add a null transcript.");
        }
        transcripts.add(transcript);
    }

    /**
     * Removes a specific transcript from the record.
     * @param transcript the transcript to remove
     */
    public void removeTranscript(String transcript) {
        transcripts.remove(transcript);
    }

    /**
     * Removes the most recent transcript from the record.
     */
    public void removeLastTranscript() {
        if (!transcripts.isEmpty()) {
            transcripts.remove(transcripts.size() - 1);
        }
    }

    /**
     * Sets the complete list of transcripts.
     *
     * @param transcripts a list of transcript strings
     * @throws IllegalArgumentException if the list is null or contains non-string elements
     */
    public void setTranscripts(List<String> transcripts) {
        if (transcripts == null) {
            throw new IllegalArgumentException("Transcripts list cannot be null.");
        }
        
        for (Object s : transcripts) {
            if (!(s instanceof String)) {
                throw new IllegalArgumentException("Transcripts list must contain only String objects.");
            }
        }
        
        this.transcripts = new ArrayList<>(transcripts);
    }

    /**
     * Returns the final sentence or outcome of the lawsuit.
     * @return the sentence string
     */
    public String getSentence() {
        return sentence;
    }

    /**
     * Sets the final sentence or outcome. This can only be called once if the case is not yet over.
     *
     * @param sentence the final outcome description
     * @throws IllegalArgumentException if the sentence is empty or already set
     */
    public void setSentence(String sentence) {
        if (isOver()) {
            throw new IllegalArgumentException("The sentence has already been set.");
        }
        
        if (sentence == null || sentence.isEmpty()) {
            throw new IllegalArgumentException("The sentence cannot be null or empty.");
        }
        
        this.sentence = sentence;
    }

    /**
     * Checks if the lawsuit or trial is over (i.e., a sentence has been set).
     * @return true if the case is concluded
     */
    public boolean isOver() {
        return sentence != null;
    }
}
