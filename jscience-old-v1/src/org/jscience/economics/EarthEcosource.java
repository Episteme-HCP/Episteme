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
