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

import org.jscience.biology.Individual;
import org.jscience.economics.EconomicAgent;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a human home or residence.
 * Occupants are the individuals actually living there.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Home extends OwnedPlace {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Address address;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Individual> occupants = new HashSet<>();

    public Home(String name, Address address, Set<EconomicAgent> owners) {
        super(name, owners);
        this.address = Objects.requireNonNull(address, "Address cannot be null");
        this.setType(Type.BUILDING);
        this.address.setPlace(this);
    }

    public Home(String name, Address address, EconomicAgent singleOwner) {
        this(name, address, Set.of(Objects.requireNonNull(singleOwner)));
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

    public Set<Individual> getOccupants() {
        return Collections.unmodifiableSet(occupants);
    }

    public void addOccupant(Individual occupant) {
        occupants.add(Objects.requireNonNull(occupant));
    }

    public boolean removeOccupant(Individual occupant) {
        return occupants.remove(occupant);
    }

    @Override
    public String toString() {
        return String.format("Home: %s (Occupants: %d)", getName(), occupants.size());
    }
}
