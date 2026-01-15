package org.jscience.chemistry;

import java.util.HashMap;

/**
 * The Solid class defines a medium for chemical reactions in glasses or crystals (solid repetitive structure).
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//when temperature is low, all compounds are in solid State
//subclasses should be amorphous solid or crystals
public class Solid extends ChemicalSubstrate {
    //really don't know how to treat solids
    //may be there is nothing special
    public Solid(Molecule molecule, double mass,
                 ChemicalConditions chemicalConditions) {
        super(molecule, mass, chemicalConditions);
    }

    //the molecule/mass for each of the components of the Gas
    //each molecule must be before liquefaction point, this is unchecked
    public Solid(HashMap contents, ChemicalConditions chemicalConditions) {
        super(contents, chemicalConditions);
    }

    //http://en.wikipedia.org/wiki/Newton%27s_law_of_cooling
    //http://en.wikipedia.org/wiki/Heat_equation
    //http://en.wikipedia.org/wiki/Thermal_conductivity
    is perhaps
    also valid
    for
    liquid and
    gaz
            XXX

    //http://en.wikipedia.org/wiki/Electrical_conduction
    //http://en.wikipedia.org/wiki/Electrical_conductivity
    //http://en.wikipedia.org/wiki/Electrical_resistance
    XXX
}
