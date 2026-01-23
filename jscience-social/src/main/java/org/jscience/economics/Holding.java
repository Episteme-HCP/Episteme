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

package org.jscience.economics;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.biology.Human;
import org.jscience.economics.money.Account;
import org.jscience.geography.BusinessPlace;
import org.jscience.util.identity.Identification;

/**
 * An organization that owns and controls multiple other companies (organizations).
 * Models conglomerate and holding structures.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class Holding extends Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<Organization> organizations;

    /**
     * Initializes a holding company.
     * 
     * @param organizations the fleet of companies initially held
     */
    public Holding(String name, Identification id, Set<Human> owners,
            BusinessPlace place, Set<Account> accounts, Set<Organization> organizations) {
        super(name, id, owners, place, accounts);
        this.organizations = new HashSet<>(Objects.requireNonNull(organizations, "Controlled organizations cannot be null"));
        if (organizations.isEmpty()) throw new IllegalArgumentException("Holding must contain at least one organization");
    }

    /** Returns unmodifiable set of owned organizations. */
    public Set<Organization> getOrganizations() {
        return Collections.unmodifiableSet(organizations);
    }

    public void addOrganization(Organization org) {
        if (org != null) organizations.add(org);
    }

    public void removeOrganization(Organization org) {
        if (organizations.size() > 1) {
            organizations.remove(org);
        } else {
            throw new IllegalArgumentException("Cannot remove last controlled organization from a holding");
        }
    }
}
