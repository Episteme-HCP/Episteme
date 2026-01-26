/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2014-2026 - JScience (http://jscience.org/)
 * All rights reserved.
 */
package org.jscience.economics;

import org.jscience.earth.Place;
import org.jscience.measure.Quantity;
import org.jscience.util.Positioned;
import org.jscience.util.Temporal;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.UUIDIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;


import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A class representing a physical or virtual resource with ownership, position, and temporal attributes.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.4
 */
@Persistent
public class Resource extends PotentialResource implements Positioned<Place>, Temporal<Instant> {

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
    private final Instant productionDate;

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
             community.getPosition(), Instant.now());
    }

    /**
     * Creates a new Resource object.
     */
    public Resource(String name, String description, Quantity<?> amount,
        Community producer, Place productionPlace, Instant productionDate) {
        this(new UUIDIdentification(UUID.randomUUID().toString()), name, description, amount, 
             producer, productionPlace, productionDate);
    }

    public Resource(Identification id, String name, String description, Quantity<?> amount,
        Community producer, Place productionPlace, Instant productionDate) {
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
    public Instant getWhen() {
        return productionDate;
    }

    @Deprecated
    public Instant getTimestamp() {
        return productionDate;
    }

    public Instant getProductionDate() {
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
