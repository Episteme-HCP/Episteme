/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2014 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.economics.resources;

import org.jscience.economics.Community;
import org.jscience.geography.Place;
import org.jscience.measure.Quantity;

import java.time.Instant;

/**
 * A class representing Minerals. This includes stones, rocks, and fossils 
 * not identified as such.
 * 
 * <p>Note: Also see {@code org.jscience.earth.SoilComposition} for related concepts.</p>
 *
 * @author Silvere Martin-Michiellot
 * @version 1.2
 */
public class Mineral extends Solid {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new Mineral object.
     *
     * @param name            the name, not null.
     * @param description     the description, not null.
     * @param amount          the quantity, not null.
     * @param producer        the producer, not null.
     * @param productionPlace the place of production, not null.
     * @param productionDate  the date of production, not null.
     */
    public Mineral(String name, String description, Quantity<?> amount,
        Community producer, Place productionPlace, Instant productionDate) {
        super(name, description, amount, producer, productionPlace,
            productionDate);
    }
}
