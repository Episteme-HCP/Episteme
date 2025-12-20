package org.jscience.chemistry;

import java.util.Iterator;
import java.util.Set;

/**
 * The ChemicalReaction class is the superclass for all chemical reactions.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//see for example http://webreactions.net/index.html for a database of reactions
public class ChemicalReaction extends Reaction {

    private Set reactants;
    private Set products;

    public ChemicalReaction(Set reactants, Set products) {

        //here all reactants and products are molecules
        Iterator iterator;
        boolean valid;

        if ((reactants != null) && (reactants.size() > 0) && (products != null) && (products.size() > 0)) {
            iterator = reactants.iterator();
            valid = true;
            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Molecule;
            }
            iterator = products.iterator();
            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Molecule;
            }
            if (valid) {
                this.reactants = reactants;
                this.products = products;
            } else
                throw new IllegalArgumentException("Chemical reactants and products set must contain only Molecules.");
        } else
            throw new IllegalArgumentException("Chemical reactants and products can't be empty sets.");

    }

    public Set getReactants() {

        return reactants;

    }

    public Set getProducts() {

        return products;

    }

    //check that the reactants can actually produce the products (equation is balanced)
    //note that a valid reaction may still not occur because its cinectic processes are too slow
    public boolean isValid() {

        //check elements and number
        XXX
        //check charge

    }

    public double computeEnergy() {

        xXX
    }

}
