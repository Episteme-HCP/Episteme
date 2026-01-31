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

package org.jscience.social.economics;

import org.jscience.natural.earth.Place;
import org.jscience.core.measure.Quantity;
import org.jscience.core.util.Positioned;
import org.jscience.core.util.Temporal;
import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.social.history.time.TimePoint;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.UUIDIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;


import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A class representing a physical or virtual resource with ownership, position, and temporal attributes.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Resource extends PotentialResource implements Positioned<Place>, Temporal<TimeCoordinate> {

    private static final long serialVersionUID = 2L;

    @Attribute
    private Community producer;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<EconomicAgent> owners;

    @Attribute
    private Place productionPlace;

    @Attribute
    private Place place;

    @Attribute
    private final TimeCoordinate productionDate;

    /**
     * Creates a new Resource object.
     *
     * @param name        the name, not null.
     * @param description the description, not null.
     * @param amount      the quantity, not null.
     * @param community   the producer community, not null.
     */
    public Resource(String name, String description, Quantity<?> amount,
        Community community) {
        this(new UUIDIdentification(UUID.randomUUID().toString()), name, description, amount, community, 
             community.getPosition(), TimePoint.now());
    }

    /**
     * Creates a new Resource object.
     */
    public Resource(String name, String description, Quantity<?> amount,
        Community producer, Place productionPlace, TimeCoordinate productionDate) {
        this(new UUIDIdentification(UUID.randomUUID().toString()), name, description, amount, 
             producer, productionPlace, productionDate);
    }

    public Resource(Identification id, String name, String description, Quantity<?> amount,
        Community producer, Place productionPlace, TimeCoordinate productionDate) {
        super(id, name, description, amount);

        this.producer = Objects.requireNonNull(producer, "Producer cannot be null");
        this.productionPlace = Objects.requireNonNull(productionPlace, "Production place cannot be null");
        this.productionDate = Objects.requireNonNull(productionDate, "Production date cannot be null");
        
        this.place = productionPlace;
        this.owners = new HashSet<>();
    }

    public void setId(Identification identification) {
        throw new UnsupportedOperationException("ID is final and managed by identity system");
    }

    public Community getProducer() {
        return producer;
    }

    public Set<EconomicAgent> getOwners() {
        return Collections.unmodifiableSet(owners);
    }

    public void addOwner(EconomicAgent owner) {
        owners.add(Objects.requireNonNull(owner, "Owner cannot be null"));
    }

    public void removeOwner(EconomicAgent owner) {
        if (owners.size() <= 1) {
             throw new IllegalArgumentException("Cannot remove last owner");
        }
        owners.remove(owner);
    }

    public void setOwners(Set<EconomicAgent> owners) {
        Objects.requireNonNull(owners, "Owners set cannot be null");
        if (owners.isEmpty()) throw new IllegalArgumentException("Owners set cannot be empty");
        this.owners = new HashSet<>(owners);
    }

    public Place getProductionPlace() {
        return productionPlace;
    }

    @Override
    public Place getPosition() {
        return place;
    }

    public void setPosition(Place place) {
        this.place = Objects.requireNonNull(place, "Place cannot be null");
    }

    @Override
    public TimeCoordinate getWhen() {
        return productionDate;
    }

    @Deprecated
    public TimeCoordinate getTimestamp() {
        return productionDate;
    }

    public TimeCoordinate getProductionDate() {
        return productionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resource)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

