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

import org.jscience.biology.Individual;

import org.jscience.geography.Place;

import org.jscience.measure.Amount;

import org.jscience.util.Positioned;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing the stuff that is extracted from the soil (coil,
 * fruits...), a final product or human labor. A product is usually a resource
 * for another factory/consumer.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//They can be people, equipment, facilities, funding, or anything else capable of definition
//(usually other than labour) required for the completion of a project activity. The lack of
//a resource will therefore be a constraint on the completion of the project activity.
//Resources may be storable or non storable. Storable resources remain available unless
//depeleted by usage, and may be replenished by project tasks which produce them. Non-storable
//resources must be renewed for each time period, even if not utilised in previous time periods.
public class Resource extends PotentialResource implements Positioned {
    /** DOCUMENT ME! */
    private Community producer;

    /** DOCUMENT ME! */
    private Set owners;

    /** DOCUMENT ME! */
    private Place productionPlace;

    /** DOCUMENT ME! */
    private Place place;

    /** DOCUMENT ME! */
    private Date productionDate;

/**
     * Creates a new Resource object.
     *
     * @param name        DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param amount      DOCUMENT ME!
     * @param community   DOCUMENT ME!
     */
    public Resource(String name, String description, Amount amount,
        Community community) {
        this(name, description, amount, community, community.getPosition(),
            new Date());
    }

/**
     * Creates a new Resource object.
     *
     * @param name            DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @param amount          DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     */
    public Resource(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate) {
        super(name, description, amount);

        if ((producer != null) && (productionPlace != null) &&
                (productionDate != null)) {
            this.producer = producer;
            this.owners = producer.getIndividuals();
            this.productionPlace = productionPlace;
            this.place = productionPlace;
            this.productionDate = productionDate;
        } else {
            throw new IllegalArgumentException(
                "The Resource constructor can't have null arguments and name and description can't be empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Community getProducer() {
        return producer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getOwners() {
        return owners;
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addOwner(Individual owner) {
        if (owner != null) {
            owners.add(owner);
        } else {
            throw new IllegalArgumentException("You can't add a null owner.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeOwner(Individual owner) {
        if ((owners.size() > 1)) {
            owners.remove(owner);
        } else {
            throw new IllegalArgumentException("You can't remove last owner.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param owners DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setOwners(Set owners) {
        Iterator iterator;
        boolean valid;

        if ((owners != null) && (owners.size() > 0)) {
            iterator = owners.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Individual;
            }

            if (valid) {
                this.owners = owners;
            } else {
                throw new IllegalArgumentException(
                    "The owners Set must contain only Individual.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null or empty owners set.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getProductionPlace() {
        return productionPlace;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getPosition() {
        return place;
    }

    /**
     * DOCUMENT ME!
     *
     * @param place DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setPosition(Place place) {
        if (place != null) {
            this.place = place;
        } else {
            throw new IllegalArgumentException("You can't set a null place.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getProductionDate() {
        return productionDate;
    }

    //equality on all but identification (which should never be the same anyway)
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        Resource resource;

        if ((o != null) && (o instanceof Resource)) {
            resource = (Resource) o;

            return this.getName().equals(resource.getName()) &&
            this.getDescription().equals(resource.getDescription()) &&
            this.getAmount().equals(resource.getAmount()) &&
            this.getProducer().equals(resource.getProducer()) &&
            this.getOwners().equals(resource.getOwners()) &&
            this.getProductionPlace().equals(resource.getProductionPlace()) &&
            this.getPosition().equals(resource.getPosition()) &&
            this.getProductionDate().equals(resource.getProductionDate()) &&
            (this.getDecayTime() == resource.getDecayTime()) &&
            (this.getKind() == resource.getKind());
        } else {
            return false;
        }
    }
}
