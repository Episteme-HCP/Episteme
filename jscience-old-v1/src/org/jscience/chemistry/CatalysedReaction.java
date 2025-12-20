package org.jscience.chemistry;

import java.util.Iterator;
import java.util.Set;

/**
 * The CatalysedReaction class is the superclass for all catalysed chemical reactions.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//this is a normal chemical reactions but one of the reactants is regenerated after use
//catalyzed reaction
public class CatalysedReaction extends ChemicalReaction {

    private Set catalysts;

    //we don't check that catalysts are in the set of reactants and in the set of products
    //use isValid()
    public CatalysedReaction(Set reactants, Set products, Set catalysts) {

        //we check that catalysts are in the set of reactants and in the set of products
        Iterator iterator;
        boolean valid;

        super(reactants, products);
        if ((catalysts != null) && (catalysts.size() > 0)) {
            iterator = catalysts.iterator();
            valid = true;
            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Molecule;
            }
            if (valid) {
                this.catalysts = catalysts;
            } else
                throw new IllegalArgumentException("Chemical catalysts set must contain only Molecules.");
        } else
            throw new IllegalArgumentException("Chemical catalysts can't be empty sets.");

    }

    //the catalyst used in the reaction
    public Set getCatalysts() {

        return catalysts;

    }

    //gets the reaction without catalyst
    //it builds out the same parameters but may produce a different equilibrium as energy involved for the reaction to occur may be too high
    public ChemicalReaction getUncatalyzedReaction() {

        return new ChemicalReaction(getReactants(), getProducts());

    }

    //the catalysts have to be present in both sides of the equation
    //the reaction may return !valid and still be valid as a chemical reaction
    public boolean isValid() {

        return super.isValid() && getReactants().containsAll(catalysts) && getReactants().containsAll(catalysts);
        XXX
    }

}
