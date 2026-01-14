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

/*
 * Basis.java
 *
 * Created on July 25, 2004, 10:36 AM
 */
package org.jscience.chemistry.quantum.basis;

import java.util.HashMap;


/**
 * Represents an entire basis set (say sto-3g ... etc.)
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class BasisSet {
    /** Holds value of property name. */
    private String name;

    /** Holds the collection of atomic basis, with the symbol as the key */
    private HashMap basisSet;

/**
     * Creates a new instance of Basis
     *
     * @param name - the name of this basis set (say "sto-3g")
     */
    public BasisSet(String name) {
        this.name = name;

        basisSet = new HashMap(80);
    }

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Add a relevent atomic basis to this basis set
     *
     * @param basis the instance of AtomicBasis to be added to this basis set
     */
    public void addAtomicBasis(AtomicBasis basis) {
        basisSet.put(basis.getSymbol(), basis);
    }

    /**
     * Returns appropriate basis for a given atomic symbol. Will throw
     * <code>BasisNotFoundException</code> if the basis set does not contain
     * atomic basis for the requested atomic symbol.
     *
     * @param symbol for which the basis is requested
     *
     * @return instance of AtomicBasis
     *
     * @throws BasisNotFoundException DOCUMENT ME!
     */
    public AtomicBasis getAtomicBasis(String symbol) {
        Object basis = basisSet.get(symbol);

        if (basis == null) {
            throw new BasisNotFoundException("Basis for atom '" + symbol +
                "' is not defined in : " + name);
        } // end if

        return (AtomicBasis) basis;
    }
} // end of class BasisSet
