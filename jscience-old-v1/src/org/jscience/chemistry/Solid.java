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
