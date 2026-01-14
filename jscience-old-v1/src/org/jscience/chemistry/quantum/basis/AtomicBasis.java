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
 * AtomicBasis.java
 *
 * Created on July 25, 2004, 10:40 AM
 */
package org.jscience.chemistry.quantum.basis;

import java.util.ArrayList;


/**
 * Represents a single entity in a BasisSet
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class AtomicBasis {
    /** Holds value of property symbol. */
    private String symbol;

    /** Holds value of property atomicNumber. */
    private int atomicNumber;

    /** Holds value of property orbitals. */
    private ArrayList orbitals;

/**
     * Creates a new instance of AtomicBasis
     *
     * @param symbol       the atomic symbol, of whose this is basis
     * @param atomicNumber its atomic number
     */
    public AtomicBasis(String symbol, int atomicNumber) {
        this.symbol = symbol;
        this.atomicNumber = atomicNumber;

        orbitals = new ArrayList(6);
    }

    /**
     * Getter for property symbol.
     *
     * @return Value of property symbol.
     */
    public String getSymbol() {
        return this.symbol;
    }

    /**
     * Setter for property symbol.
     *
     * @param symbol New value of property symbol.
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Getter for property atomicNumber.
     *
     * @return Value of property atomicNumber.
     */
    public int getAtomicNumber() {
        return this.atomicNumber;
    }

    /**
     * Setter for property atomicNumber.
     *
     * @param atomicNumber New value of property atomicNumber.
     */
    public void setAtomicNumber(int atomicNumber) {
        this.atomicNumber = atomicNumber;
    }

    /**
     * Getter for property orbitals.
     *
     * @return Value of property orbitals.
     */
    public ArrayList getOrbitals() {
        return this.orbitals;
    }

    /**
     * Setter for property orbitals.
     *
     * @param orbitals New value of property orbitals.
     */
    public void setOrbitals(ArrayList orbitals) {
        this.orbitals = orbitals;
    }

    /**
     * Add an orbital object to this atomic basis
     *
     * @param orbital the Orbital object to be added to this atomic basis
     */
    public void addOrbital(Orbital orbital) {
        orbitals.add(orbital);
    }
} // end of class AtomicBasis
