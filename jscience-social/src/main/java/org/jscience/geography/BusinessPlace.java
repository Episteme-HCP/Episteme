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

package org.jscience.geography;

import org.jscience.earth.Place;
import org.jscience.economics.EconomicAgent;
import org.jscience.economics.Organization;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import java.util.Objects;
import java.util.Set;

/**
 * Represents a business location, office, or industrial site.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class BusinessPlace extends OwnedPlace {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Address address;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Organization organization;

    public BusinessPlace(String name, Address address, Set<EconomicAgent> owners) {
        super(name, owners);
        this.address = Objects.requireNonNull(address, "Address cannot be null");
        this.setType(Place.Type.BUILDING);
        this.address.setPlace(this);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        if (address != null) {
            address.setPlace(this);
        }
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) [%s]", 
            getName(), address != null ? address.getCity() : "Unknown", 
            organization != null ? organization.getName() : "Vacant");
    }
}
