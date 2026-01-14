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

import java.util.HashSet;
import java.util.Set;


/**
 * The Bond class defines the invisible link that exists between two Atoms
 * of the same Molecule
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//this is the basic covalent bond
//bonds can only occur beween atoms that are not already fully bonded
    //http://en.wikipedia.org/wiki/Chemical_bond
public class Bond extends Object {
    /** DOCUMENT ME! */
    public final static int SIMPLE = 1;

    /** DOCUMENT ME! */
    public final static int DOUBLE = 2;

    /** DOCUMENT ME! */
    public final static int TRIPLE = 3;

    /** DOCUMENT ME! */
    public final static int QUADRUPLE = 4;

    /** DOCUMENT ME! */
    public final static int QUINTUPLE = 5;

    /** DOCUMENT ME! */
    public final static int SEXTUPLE = 6; 

    /** DOCUMENT ME! */
    private Atom atom1;

    /** DOCUMENT ME! */
    private Atom atom2;

    /** DOCUMENT ME! */
    private double length;

    /** DOCUMENT ME! */
    private int kind;

    /** DOCUMENT ME! */
    private double energy; //to break the bond

    /** DOCUMENT ME! */
    private double moment;

/**
     * Creates a new Bond object.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     */
    public Bond(Atom a, Atom b) {
        this(a, b, SIMPLE);
    }

/**
     * Creates a new Bond object.
     *
     * @param a    DOCUMENT ME!
     * @param b    DOCUMENT ME!
     * @param kind DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Bond(Atom a, Atom b, int kind) {
        if ((a != null) && (b != null)) {
            //check this are not the same atom
            if (a != b) {
                //check there is not already a Bond between a and b
                if (a.getNeighbors().contains(b)) {
                    //check each atom can still be bonded
                    //if (a.hasFreeElectrons() && b.hasFreeElectrons()) {
                    atom1 = a;
                    atom2 = b;
                    setKind(kind);
                    energy = 0;
                    moment = 0;
                    atom1.addBond(this);
                    atom2.addBond(this);

                    //} else throw new IllegalArgumentException("Trying to Bond completly bonded Atoms.");
                } else {
                    throw new IllegalArgumentException(
                        "There is already a Bond between these Atoms.");
                }
            } else {
                throw new IllegalArgumentException(
                    "Trying to Bond an Atom with itself.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Bond constructor doesn't accept null arguments.");
        }
    }

    //a two elements set
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAtoms() {
        HashSet result;

        result = new HashSet();
        result.add(atom1);
        result.add(atom2);

        return result;
    }

    //the number here is purely fictuous
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Atom getAtom1() {
        return atom1;
    }

    //the number here is purely fictuous
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Atom getAtom2() {
        return atom2;
    }

    //uses data from tables
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double computeCovalentLength() {
        return atom1.getCorrespondingElement().getCovalentRadius() +
        atom2.getCorrespondingElement().getCovalentRadius();
    }

    //using positions
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double computeLength() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getLength() {
        return length;
    }

    //the length of the bond in meters
    /**
     * DOCUMENT ME!
     *
     * @param length DOCUMENT ME!
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getKind() {
        return kind;
    }

    //the quantic kind of bond (: is it a simple, a double or triple bond ?)
    /**
     * DOCUMENT ME!
     *
     * @param kind DOCUMENT ME!
     */
    public void setKind(int kind) {
        this.kind = kind;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getEnergy() {
        return energy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param energy DOCUMENT ME!
     */
    public void setEnergy(double energy) {
        this.energy = energy;
    }

    //in Debye
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDipoleMoment() {
        return moment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param moment DOCUMENT ME!
     */
    public void setDipoleMoment(double moment) {
        this.moment = moment;
    }
}
