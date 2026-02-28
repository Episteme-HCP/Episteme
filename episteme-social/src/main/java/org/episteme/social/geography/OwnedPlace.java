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

import org.episteme.social.economics.money.Money;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.episteme.natural.earth.PlaceType;
import org.episteme.social.economics.EconomicAgent;
import org.episteme.social.economics.Property;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

/**
 * Represents a piece of land or building that is owned by one or more economic agents.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class OwnedPlace extends TimedPlace implements Property {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<EconomicAgent> owners = new HashSet<>();

    @Attribute
    private Money value;

    public OwnedPlace(String name, Set<EconomicAgent> initialOwners) {
        super(name, PlaceType.OTHER);
        if (initialOwners == null || initialOwners.isEmpty()) {
            throw new IllegalArgumentException("Property must have at least one owner");
        }
        this.owners.addAll(initialOwners);
    }

    @Override
    public Set<EconomicAgent> getOwners() {
        return Collections.unmodifiableSet(owners);
    }

    public void addOwner(EconomicAgent owner) {
        if (owner != null) owners.add(owner);
    }

    public void removeOwner(EconomicAgent owner) {
        if (owners.size() > 1) { // Prevent ownerless property
            owners.remove(owner);
        }
    }

    @Override
    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getName() + " (Owned by " + owners.size() + ")";
    }
}

