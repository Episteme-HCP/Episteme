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

package org.episteme.social.geography;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.episteme.natural.biology.Individual;
import org.episteme.natural.earth.PlaceType;
import org.episteme.social.economics.EconomicAgent;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

/**
 * Represents a residence, a specific place where individuals live.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Home extends OwnedPlace {

    private static final long serialVersionUID = 1L;

    @Attribute
    private final Address address;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Individual> occupants = new HashSet<>();

    public Home(String name, Address address, Set<EconomicAgent> owners) {
        super(name, owners);
        this.address = Objects.requireNonNull(address, "Address cannot be null");
        this.setType(PlaceType.BUILDING);
        this.address.setPlace(this);
    }

    public Address getAddress() {
        return address;
    }

    public Set<Individual> getOccupants() {
        return Collections.unmodifiableSet(occupants);
    }

    public void addOccupant(Individual person) {
        if (person != null) occupants.add(person);
    }

    public void removeOccupant(Individual person) {
        occupants.remove(person);
    }

    @Override
    public String toString() {
        return "Home: " + address.toString();
    }
}

