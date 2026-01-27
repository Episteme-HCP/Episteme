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

import org.jscience.economics.EconomicAgent;
import org.jscience.economics.Organization;
import org.jscience.economics.Property;
import org.jscience.economics.money.Money;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents an official license or certificate issued by an authority that 
 * grants specific rights to an owner. Examples include birth certificates, 
 * driving licenses, IDs, and professional diplomas.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class License implements Property, Identified<Identification> {

    private final EconomicAgent owner;
    private final Organization authority;
    private final Identification identification;
    private final List<String> rights;

    /**
     * Creates a new License object with a single initial right.
     *
     * @param owner the agent who owns the license
     * @param authority the organization that issued the license
     * @param identification the unique identification of the license document
     * @param right the initial right granted by this license
     * @throws IllegalArgumentException if any argument is null or empty
     */
    public License(EconomicAgent owner, Organization authority,
        Identification identification, String right) {
        if (owner == null || authority == null || identification == null ||
                right == null || right.isEmpty()) {
            throw new IllegalArgumentException(
                "License constructor arguments cannot be null or empty.");
        }
        this.owner = owner;
        this.authority = authority;
        this.identification = identification;
        this.rights = new ArrayList<>();
        this.rights.add(right);
    }

    /**
     * Creates a new License object with multiple rights.
     *
     * @param owner the agent who owns the license
     * @param authority the organization that issued the license
     * @param identification the unique identification of the license document
     * @param rights the list of rights granted by this license
     * @throws IllegalArgumentException if any argument is null, empty, or contains non-String elements
     */
    public License(EconomicAgent owner, Organization authority,
        Identification identification, List<String> rights) {
        if (owner == null || authority == null || identification == null ||
                rights == null || rights.isEmpty()) {
            throw new IllegalArgumentException(
                "License constructor arguments cannot be null or empty.");
        }
        
        for (Object r : rights) {
            if (!(r instanceof String)) {
                throw new IllegalArgumentException("The rights list must contain only String objects.");
            }
        }

        this.owner = owner;
        this.authority = authority;
        this.identification = identification;
        this.rights = new ArrayList<>(rights);
    }

    /**
     * Returns the set of owners of this license. Currently, only the primary owner is returned.
     * @return a set containing the license owner
     */
    @Override
    public Set<EconomicAgent> getOwners() {
        Set<EconomicAgent> result = new HashSet<>();
        result.add(owner);
        return result;
    }

    /**
     * Returns the authority that issued this license.
     * @return the issuing organization
     */
    public Organization getAuthority() {
        return authority;
    }

    public Identification getIdentification() {
        return identification;
    }

    @Override
    public Identification getId() {
        return identification;
    }

    /**
     * Returns the list of rights granted by this license.
     * @return a list of rights as strings
     */
    public List<String> getRights() {
        return new ArrayList<>(rights);
    }

    /**
     * Returns the economic value of this license. Unless overridden by a subclass, 
     * the default value is 0 USD.
     * @return the value of the license
     */
    @Override
    public Money getValue() {
        return Money.usd(0);
    }
}
