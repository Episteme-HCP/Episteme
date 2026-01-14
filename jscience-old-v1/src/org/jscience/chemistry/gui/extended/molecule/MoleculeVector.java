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

package org.jscience.chemistry.gui.extended.molecule;

import java.util.NoSuchElementException;
import java.util.Vector;


/**
 * Tripos MoleculeVector class
 *
 * @author Zhidong Xie (zxie@org.jscience.chemistry.gui.extended.com) Orignal
 *         Version date: 8/14/97
 *
 * @see Molecule
 */
public class MoleculeVector extends Vector {
/**
     * Default constructor
     */
    public MoleculeVector() {
        super();
    }

/**
     * Capacity constructor
     *
     * @param cap initial capacity of vector
     */
    public MoleculeVector(int cap) {
        super(cap, cap);
    }

/**
     * Capacity & increment constructor
     *
     * @param cap initial capacity of vector
     * @param inc increment factor
     */
    public MoleculeVector(int cap, int inc) {
        super(cap, inc);
    }

/**
     * Copy constructor.
     *
     * @param mv MoleculeVector to be copied
     */
    public MoleculeVector(MoleculeVector mv) {
        super(mv.size(), mv.size());

        for (int i = 0; i < mv.size(); i++) {
            Molecule mol = new Molecule(mv.getMolecule(i));
            append(mol);
        }
    }

    /**
     * Append a molecule to the end of vector
     *
     * @param mol molecule to be appended
     */
    public final synchronized void append(Molecule mol) {
        addElement(mol);
    }

    /**
     * Append a molecule to the end of vector only if the molecule is
     * not already present in the MoleculeVector
     *
     * @param mol molecule to be appended
     */
    public final synchronized void appendUnique(Molecule mol) {
        if (!contains(mol)) {
            addElement(mol);
        }
    }

    /**
     * Returns molecule at specified index
     *
     * @param i index of molecule
     *
     * @return DOCUMENT ME!
     *
     * @throws ArrayIndexOutOfBoundsException if index >= capacity()
     */
    public final synchronized Molecule getMolecule(int i)
        throws ArrayIndexOutOfBoundsException {
        return (Molecule) elementAt(i);
    }

    /**
     * Set a molecule at specified index; molecule at index is replaced
     *
     * @param mol molecule to be set
     * @param i index to place molecule
     */
    public final synchronized void set(Molecule mol, int i) {
        setElementAt(mol, i);
    }

    /**
     * Insert a molecule at specified index; other molecules are
     * shifted over
     *
     * @param mol molecule to be inserted
     * @param i index of insertion
     */
    public final synchronized void insert(Molecule mol, int i) {
        insertElementAt(mol, i);
    }

    /**
     * Remove a molecule; other molecules are shifted down
     *
     * @param mol molecule to be removed
     *
     * @return <tt>true</tt> if molecule was removed, else <tt>false</tt>
     */
    public final synchronized boolean remove(Molecule mol) {
        if (contains(mol)) {
            return removeElement(mol);
        } else {
            return false;
        }
    }

    /**
     * Remove molecule at specified index; other molecules are shifted
     * down
     *
     * @param i index of molecule
     *
     * @throws ArrayIndexOutOfBoundsException if index >= capacity()
     */
    public final synchronized void removeMolecule(int i)
        throws ArrayIndexOutOfBoundsException {
        removeElementAt(i);
    }

    /**
     * Returns first molecule in MoleculeVector
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException if size() == 0
     */
    public final synchronized Molecule firstMolecule()
        throws NoSuchElementException {
        return (Molecule) firstElement();
    }

    /**
     * Returns last molecule in MoleculeVector
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException if size() == 0
     */
    public final synchronized Molecule lastMolecule()
        throws NoSuchElementException {
        return (Molecule) lastElement();
    }

    /**
     * Compares this MoleculeVector with another
     *
     * @param av MoleculeVector to compare with
     *
     * @return <tt>true</tt> if they are equal, else <tt>false</tt>
     */
    public boolean equals(MoleculeVector av) {
        if (av == null) {
            return false;
        }

        if (size() != av.size()) {
            return false;
        }

        for (int i = 0; i < size(); i++) {
            Molecule a1 = getMolecule(i);
            Molecule a2 = av.getMolecule(i);

            if (!a1.equals(a2)) {
                return false;
            }
        }

        return true;
    }
} // end of class MoleculeVector
