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
