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
 * A class defining a reference to a {@link Species} for use in a {@link
 * Reaction}. This code is licensed under the DARPA BioCOMP Open Source
 * License.  See LICENSE for more details.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */
public class SpeciesReference extends SBase {
    /** DOCUMENT ME! */
    private double stoichiometry;

    /** DOCUMENT ME! */
    private StoichiometryMath stoichiometryMath;

    /** DOCUMENT ME! */
    private String species;

/**
     * Creates a new instance of SpeciesReference
     */
    public SpeciesReference() {
        this(null);
    }

/**
     * Creates a new SpeciesReference object.
     *
     * @param species DOCUMENT ME!
     */
    public SpeciesReference(Species species) {
        setSpecies(species);
        setStoichiometry(1.0);
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
     * Getter for property stoichiometry.
     *
     * @return Value of property stoichiometry.
     */
    public String getStoichiometry() {
        return Double.isNaN(stoichiometry) ? null : String.valueOf(stoichiometry);
    }

    /**
     * Getter for property stoichiometryMath.
     *
     * @return Value of property stoichiometryMath.
     */
    public StoichiometryMath getStoichiometryMath() {
        return stoichiometryMath;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ref DOCUMENT ME!
     */
    public void setSpecies(Species ref) {
        setSpecies((ref == null) ? null : ref.getId());
    }

    /**
     * Setter for property species.
     *
     * @param species Setter for property species.
     */
    public void setSpecies(String species) {
        this.species = species;
    }

    /**
     * Sets the rational stoichiometry of a reaction.  NaN indicates
     * that the stoichiometry is not set.  If the stoichiometry is not NaN,
     * the stoichiometry math will be cleared.  Stoichiometries must be
     * non-negative.  For maximum compatibility with other software, the
     * stoichiometry should be an integer.
     *
     * @param stoichiometry Stoichiometry
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setStoichiometry(double stoichiometry) {
        if (stoichiometry < 0.0) {
            throw new IllegalArgumentException(
                "Stoichiometry must be non-negative.");
        }

        this.stoichiometry = stoichiometry;

        if (!Double.isNaN(stoichiometry)) {
            setStoichiometryMath(null);
        }
    }

    /**
     * Sets the stoichiometry of a reaction.  If the stoichiometry math
     * is not null, the rational stoichiometry will be cleared.  For maximum
     * compatibility with other software, use a rational stoichiometry instead
     * of stoichiometry math.
     *
     * @param stoichiometryMath Stoichiometry math
     */
    public void setStoichiometryMath(StoichiometryMath stoichiometryMath) {
        this.stoichiometryMath = stoichiometryMath;

        if (stoichiometryMath != null) {
            stoichiometry = Double.NaN;
        }
    }

    /**
     * Get the SBML representation for this class.
     *
     * @return The class's SBML representation
     */
    public String toString() {
        StringBuffer s = new StringBuffer("<speciesReference species=\"" +
                species + "\"");

        if (stoichiometryMath != null) {
            s.append(">\n");
            s.append(stoichiometryMath.toString());
            s.append(super.toString());
            s.append("</speciesReference>\n");

            return s.toString();
        }

        if (!Double.isNaN(stoichiometry) && (stoichiometry != 1.0)) {
            s.append(" stoichiometry=\"" + stoichiometry + "\"");
        }

        printShortForm(s, "</speciesReference>");

        return s.toString();
    }
}
