/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2014 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.economics;

import org.jscience.biology.Species;
import org.jscience.biology.human.HumanSpecies;
import org.jscience.geography.Places;
import org.jscience.measure.Quantity;

/**
 * A class representing the Earth as an autonomous organism that produces, 
 * stores, and recycles materials.
 * Represents the primary source of natural resources in the economic model.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.2
 */
public class EarthEcosource extends Community {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new EarthEcosource object with HumanSpecies as the default species.
     */
    public EarthEcosource() {
        super(new HumanSpecies(), Places.EARTH);
    }

    /**
     * Creates a new EarthEcosource object for a specific species.
     *
     * @param species the species inhabiting this ecosource
     */
    public EarthEcosource(Species species) {
        super(species, Places.EARTH);
    }

    /**
     * Generates a new resource from the Earth.
     *
     * @param name the name of the resource
     * @param description the description of the resource
     * @param amount the quantity of the resource available
     * @return a new Resource object linked to this source
     */
    public Resource generateResource(String name, String description, Quantity<?> amount) {
        return new Resource(name, description, amount, this);
    }
}
