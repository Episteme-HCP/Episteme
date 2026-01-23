/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2014 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.economics;

import org.jscience.biology.Individual;
import org.jscience.geography.Place;
import org.jscience.measure.Quantity;
import org.jscience.util.Positioned;
import org.jscience.util.Temporal;
import org.jscience.util.identity.Identification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A class representing the stuff that is extracted from the soil (coal,
 * fruits...), a final product or human labor. A product is usually a resource
 * for another factory/consumer.
 * 
 * <p>Resources can be people, equipment, facilities, funding, or anything else 
 * capable of definition (usually other than labour) required for the completion 
 * of a project activity. The lack of a resource will therefore be a constraint 
 * on the completion of the project activity.</p>
 * 
 * <p>Resources may be storable or non storable. Storable resources remain 
 * available unless depleted by usage, and may be replenished by project tasks 
 * which produce them. Non-storable resources must be renewed for each time 
 * period, even if not utilised in previous time periods.</p>
 *
 * @author Silvere Martin-Michiellot
 * @version 1.2
 */
@Persistent
public class Resource extends PotentialResource implements Positioned<Place>, Temporal {

    private static final long serialVersionUID = 1L;

    @Id
    private Identification identification;

    @Attribute
    private Community producer;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Individual> owners;

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
        this(name, description, amount, community, community.getPosition(),
            Instant.now());
    }

    /**
     * Creates a new Resource object.
     *
     * @param name            the name, not null.
     * @param description     the description, not null.
     * @param amount          the quantity, not null.
     * @param producer        the producer, not null.
     * @param productionPlace the place of production, not null.
     * @param productionDate  the date of production, not null.
     */
    public Resource(String name, String description, Quantity<?> amount,
        Community producer, Place productionPlace, Instant productionDate) {
        super(name, description, amount);

        this.producer = Objects.requireNonNull(producer, "Producer cannot be null");
        this.productionPlace = Objects.requireNonNull(productionPlace, "Production place cannot be null");
        this.productionDate = Objects.requireNonNull(productionDate, "Production date cannot be null");
        
        this.place = productionPlace;
        // Use getIndividuals() which returns a Collection/Set of Individuals
        this.owners = new HashSet<>(producer.getIndividuals());
    }

    public Community getProducer() {
        return producer;
    }

    public Set<Individual> getOwners() {
        return Collections.unmodifiableSet(owners);
    }

    public void addOwner(Individual owner) {
        owners.add(Objects.requireNonNull(owner, "Owner cannot be null"));
    }

    public void removeOwner(Individual owner) {
        if (owners.size() <= 1) {
             throw new IllegalArgumentException("Cannot remove last owner");
        }
        owners.remove(owner);
    }

    public void setOwners(Set<Individual> owners) {
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

    /**
     * Returns the production timestamp.
     * @return the production instant
     */
    @Override
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
        if (!super.equals(o)) return false;
        Resource resource = (Resource) o;
        return Objects.equals(producer, resource.producer) &&
                Objects.equals(owners, resource.owners) &&
                Objects.equals(productionPlace, resource.productionPlace) &&
                Objects.equals(place, resource.place) &&
                Objects.equals(productionDate, resource.productionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), producer, owners, productionPlace, place, productionDate);
    }
}
