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
import org.jscience.measure.Quantity;

/**
 * A class representing the Earth as an autonomous organism that produces, 
 * stores, and recycles materials.
 * Represents the primary source of natural resources in the economic model.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
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
