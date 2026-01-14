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

package org.jscience.ml.sbml;

/**
 * This class defines a modifier that takes part in a {@link Reaction}.
 * This code is licensed under the DARPA BioCOMP Open Source License.  See
 * LICENSE for more details.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */
public class ModifierSpeciesReference extends SBase {
    /** DOCUMENT ME! */
    private String species;

/**
     * Creates a new instance of ModifierSpeciesReference
     */
    public ModifierSpeciesReference() {
    }

    /**
     * Getter for property species.
     *
     * @return Value of property species.
     */
    public String getSpecies() {
        return species;
    }

    /**
     * Sets the name of the species to reference.
     *
     * @param species New value of property species.
     */
    public void setSpecies(String species) {
        this.species = species;
    }

    /**
     * Get the SBML representation for this class.
     *
     * @return The class's SBML representation
     */
    public String toString() {
        StringBuffer s = new StringBuffer(
                "<modifierSpeciesReference species=\"" + species + "\"");
        printShortForm(s, "</modifierSpeciesReference>");

        return s.toString();
    }
}
