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

import java.util.Vector;


/**
 * This class represents a reaction, which is some transformation,
 * transport or binding process that can change the amount of one or more
 * {@link Species}. This code is licensed under the DARPA BioCOMP Open Source
 * License.  See LICENSE for more details.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */
public class Reaction extends SBaseId {
    /** DOCUMENT ME! */
    private boolean fast = false;

    /** DOCUMENT ME! */
    private boolean reversible = true;

    /** DOCUMENT ME! */
    private KineticLaw kineticLaw;

    /** DOCUMENT ME! */
    private SBase modifiersElement = new SBase();

    /** DOCUMENT ME! */
    private SBase productsElement = new SBase();

    /** DOCUMENT ME! */
    private SBase reactantsElement = new SBase();

    /** DOCUMENT ME! */
    private Vector modifier = new Vector();

    /** DOCUMENT ME! */
    private Vector product = new Vector();

    /** DOCUMENT ME! */
    private Vector reactant = new Vector();

/**
     * Creates a new Reaction object.
     *
     * @param id   DOCUMENT ME!
     * @param name DOCUMENT ME!
     */
    public Reaction(String id, String name) {
        super(id, name);
    }

/**
     * Creates a new instance of Reaction
     */
    public Reaction() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param ref DOCUMENT ME!
     */
    public void addProduct(Species ref) {
        product.add(new SpeciesReference(ref));
    }

    /**
     * DOCUMENT ME!
     *
     * @param ref DOCUMENT ME!
     */
    public void addProduct(SpeciesReference ref) {
        product.add(ref);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ref DOCUMENT ME!
     */
    public void addReactant(Species ref) {
        reactant.add(new SpeciesReference(ref));
    }

    /**
     * DOCUMENT ME!
     *
     * @param ref DOCUMENT ME!
     */
    public void addReactant(SpeciesReference ref) {
        reactant.add(ref);
    }

    /**
     * Getter for property kineticLaw.
     *
     * @return Value of property kineticLaw.
     */
    public KineticLaw getKineticLaw() {
        return kineticLaw;
    }

    /**
     * Getter for property modifier.
     *
     * @return Value of property modifier.
     */
    public Vector getModifier() {
        return modifier;
    }

    /**
     * Getter for property modifiersElement.
     *
     * @return Value of property modifiersElement.
     */
    public SBase getModifiersElement() {
        return modifiersElement;
    }

    /**
     * Getter for property product.
     *
     * @return Value of property product.
     */
    public Vector getProduct() {
        return product;
    }

    /**
     * Getter for property productsElement.
     *
     * @return Value of property productsElement.
     */
    public SBase getProductsElement() {
        return productsElement;
    }

    /**
     * Getter for property reactant.
     *
     * @return Value of property reactant.
     */
    public Vector getReactant() {
        return reactant;
    }

    /**
     * Getter for property reactantsElement.
     *
     * @return Value of property reactantsElement.
     */
    public SBase getReactantsElement() {
        return reactantsElement;
    }

    /**
     * Getter for property fast.
     *
     * @return Value of property fast.
     */
    public boolean isFast() {
        return fast;
    }

    /**
     * Getter for property reversible.
     *
     * @return Value of property reversible.
     */
    public boolean isReversible() {
        return reversible;
    }

    /**
     * Setter for property fast.
     *
     * @param fast New value of property fast.
     */
    public void setFast(boolean fast) {
        this.fast = fast;
    }

    /**
     * Setter for property kineticLaw.
     *
     * @param kineticLaw New value of property kineticLaw.
     */
    public void setKineticLaw(KineticLaw kineticLaw) {
        this.kineticLaw = kineticLaw;
    }

    /**
     * Setter for property modifiersElement.
     *
     * @param modifiersElement New value of property modifiersElement.
     */
    public void setModifiersElement(SBase modifiersElement) {
        this.modifiersElement = modifiersElement;
    }

    /**
     * Setter for property productsElement.
     *
     * @param productsElement New value of property productsElement.
     */
    public void setProductsElement(SBase productsElement) {
        this.productsElement = productsElement;
    }

    /**
     * Setter for property reactantsElement.
     *
     * @param reactantsElement New value of property reactantsElement.
     */
    public void setReactantsElement(SBase reactantsElement) {
        this.reactantsElement = reactantsElement;
    }

    /**
     * Sets whether the reaction is reversible.
     *
     * @param reversible New value of property reversible.
     */
    public void setReversible(boolean reversible) {
        this.reversible = reversible;
    }

    /**
     * Get the SBML representation for this class.
     *
     * @return The class's SBML representation
     */
    public String toString() {
        StringBuffer s = new StringBuffer("<reaction id=\"" + id + "\"");

        if (!reversible) {
            s.append(" reversible=\"" + reversible + "\"");
        }

        if (fast) {
            s.append(" fast=\"" + fast + "\"");
        }

        if (name != null) {
            s.append(" name=\"" + name + "\"");
        }

        s.append(">\n");
        s.append(super.toString());
        printList(s, reactant, "<listOfReactants>" + reactantsElement,
            "</listOfReactants>");
        printList(s, product, "<listOfProducts>" + productsElement,
            "</listOfProducts>");
        printList(s, modifier, "<listOfModifiers>" + modifiersElement,
            "</listOfModifiers>");

        if (kineticLaw != null) {
            s.append(kineticLaw.toString());
        }

        s.append("</reaction>\n");

        return s.toString();
    }
}
