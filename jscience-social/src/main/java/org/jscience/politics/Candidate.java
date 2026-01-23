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

package org.jscience.politics;

import java.util.Objects;
import org.jscience.biology.Individual;
import org.jscience.sociology.Role;
import org.jscience.sociology.Situation;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents the role of a candidate within a political situation (e.g., an election).
 * A candidate is an individual seeking a specific office or representation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.0
 * @since 2.0
 */
@Persistent
public class Candidate extends Role {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Party party;

    @Attribute
    private String officeSought;

    @Attribute
    private String platformSummary;

    /**
     * Creates a new Candidate role for an individual.
     *
     * @param individual the individual running for office
     * @param situation  the political context (usually an Election)
     * @param office     the position being sought
     * @throws NullPointerException if individual, situation or office is null
     */
    public Candidate(Individual individual, Situation situation, String office) {
        super(Objects.requireNonNull(individual, "Individual cannot be null"), 
              "Candidate", 
              Objects.requireNonNull(situation, "Situation cannot be null"), 
              Role.SERVER); // A candidate provides a potential service of representation
        this.officeSought = Objects.requireNonNull(office, "Office sought cannot be null");
    }

    /**
     * Returns the political party the candidate represents.
     * @return the party, or null if independent
     */
    public Party getParty() {
        return party;
    }

    /**
     * Sets the political party for this candidate.
     * @param party the party
     */
    public void setParty(Party party) {
        this.party = party;
    }

    /**
     * Returns the office or position the candidate is seeking.
     * @return the office name
     */
    public String getOfficeSought() {
        return officeSought;
    }

    /**
     * Returns a summary of the candidate's platform.
     * @return the platform summary
     */
    public String getPlatformSummary() {
        return platformSummary;
    }

    /**
     * Sets the platform summary for the candidate.
     * @param platformSummary the summary
     */
    public void setPlatformSummary(String platformSummary) {
        this.platformSummary = platformSummary;
    }

    @Override
    public String toString() {
        return String.format("%s - Candidate for %s%s", 
            getIndividual().getName(), 
            officeSought, 
            (party != null ? " (" + party.getAbbreviation() + ")" : ""));
    }
}
