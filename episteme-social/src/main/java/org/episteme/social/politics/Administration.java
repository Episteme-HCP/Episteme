/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.politics;


import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import org.episteme.social.economics.Organization;
import org.episteme.social.economics.money.Account;
import org.episteme.social.geography.BusinessPlace;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.SimpleIdentification;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

/**
 * Represents a state-managed administrative organization (agency, ministry, police, etc.).
 * Administrations are structured hierarchies of individuals serving a Country.
 * * @version 1.2
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Administration extends Organization {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Country country;

    /**
     * Creates an Administration using a default identification strategy.
     *
     * @param name     the name of the administration
     * @param country  the country of affiliation
     * @param place    the headquarters location
     * @param accounts financial accounts for operation
     * @throws NullPointerException if any argument is null
     */
    public Administration(String name, Country country, BusinessPlace place, Set<Account> accounts) {
        super(Objects.requireNonNull(name, "Name cannot be null"),
            new SimpleIdentification(name),
            Collections.emptySet(), 
            place, accounts);
        this.country = country;
    }

    /**
     * Creates an Administration with explicit identification.
     *
     * @param name           the name
     * @param identification unique identifier
     * @param country        the country
     * @param place          the location
     * @param accounts       financial accounts
     * @throws NullPointerException if any argument is null
     */
    public Administration(String name, Identification identification,
        Country country, BusinessPlace place, Set<Account> accounts) {
        super(Objects.requireNonNull(name, "Name cannot be null"), 
            Objects.requireNonNull(identification, "Identification cannot be null"), 
            Collections.emptySet(),
            place, accounts);
        this.country = country;
    }

    /**
     * Returns the country associated with this administration.
     * @return the country
     */
    public Country getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return getName() + " [" + country.getName() + "]";
    }
}

