package org.jscience.chemistry;

import java.util.HashMap;

/**
 * The Gas class defines a gaz, that is a mix of big quantities of molecules above ebullition point.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

public class Gas extends ChemicalSubstrate {

    public Gas(Molecule molecule, double mass, ChemicalConditions chemicalConditions) {

        super(molecule, mass, chemicalConditions);

    }

    //the molecule/mass for each of the components of the Gas
    //each molecule must be past the ebullition (vaporisation) point, this is unchecked
    public Gas(HashMap contents, ChemicalConditions chemicalConditions) {

        super(contents, chemicalConditions);

    }

    //compute partialPressure


    http://en.wikipedia.org/wiki/Equation_of_state
    XX


    http://en.wikipedia.org/wiki/Gas_laws

    http://en.wikipedia.org/wiki/Graham%27s_law

}
