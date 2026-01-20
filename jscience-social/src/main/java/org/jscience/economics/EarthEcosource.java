package org.jscience.economics;

import org.jscience.biology.Species;
import org.jscience.biology.human.HumanSpecies;

import org.jscience.geography.Places;

import org.jscience.measure.Amount;


/**
 * A class representing the Earth as an autonomous organism that produces
 * (mostly stores and recycles) materials.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this factory produces resources "out of nothing" (in fact is has already a big storage, even if no capital)
//moreover it can (and pitifully is) used as the end of the cycle for many products.
public class EarthEcosource extends Community {
    //the species is the human species
    /**
     * Creates a new EarthEcosource object.
     */
    public EarthEcosource() {
        super(new HumanSpecies(), Places.EARTH);
    }

    /**
     * Creates a new EarthEcosource object.
     *
     * @param species DOCUMENT ME!
     */
    public EarthEcosource(Species species) {
        super(species, Places.EARTH);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param amount DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Resource generateResource(String name, String description,
        Amount amount) {
        return new Resource(name, description, amount, this);
    }
}
