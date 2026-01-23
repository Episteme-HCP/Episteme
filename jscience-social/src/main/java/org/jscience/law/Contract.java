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

import org.jscience.economics.Organization;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A class representing a legal contract or agreement between two organizations or parties.
 * It describes the terms, clauses, and identifiers of the deal.
 *
 * <p>Note: Licensing for software (e.g., GPL) should generally be handled 
 * via the License class.</p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 */
public class Contract implements Identified {
    
    private Organization firstParty;
    private Organization secondParty;
    private Identification identification;
    private Date date; // when signed by both parties
    private List<String> contents; // clauses or terms

    /**
     * Creates a new Contract object.
     *
     * @param firstParty the first organization or party involved
     * @param secondParty the second organization or party involved
     * @param identification the identifier of the contract (can be null for hand-to-hand deals)
     * @param date the date of signing
     * @param contents the terms or clauses of the contract
     * @throws IllegalArgumentException if parties, date or contents are null/empty
     */
    public Contract(Organization firstParty, Organization secondParty,
        Identification identification, Date date, List<String> contents) {

        if (firstParty == null || secondParty == null || date == null ||
            contents == null || contents.isEmpty()) {
            throw new IllegalArgumentException("Contract arguments cannot be null or empty.");
        }

        this.firstParty = firstParty;
        this.secondParty = secondParty;
        this.identification = identification;
        this.date = date;
        this.contents = new ArrayList<>(contents);
    }

    /**
     * Returns the first party (organization) of the contract.
     * @return the first party
     */
    public Organization getFirstParty() {
        return firstParty;
    }

    /**
     * Returns the second party (organization) of the contract.
     * @return the second party
     */
    public Organization getSecondParty() {
        return secondParty;
    }

    /**
     * Returns the identification of this contract.
     * @return the identification, or null
     */
    @Override
    public Identification getIdentification() {
        return identification;
    }

    /**
     * Returns the date the contract was signed.
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the list of clauses or content strings in the contract.
     * @return the contents
     */
    public List<String> getContents() {
        return contents;
    }
}
