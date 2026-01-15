package org.jscience.chemistry.gui.extended.molecule;

import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * Tripos AtomVector class
 *
 * @author Jason Plurad (jplurad@org.jscience.chemistry.gui.extended.com)
 *         Orignal Version
 * @author Mike Brusati (brusati@org.jscience.chemistry.gui.extended.com)
 *         Converted to JMol, the new Tripos Java molecule format.
 *         <p/>
 *         Modified: Zhidong Xie (zxie@org.jscience.chemistry.gui.extended.com)
 *         Replaced molecule package, from jmol package
 *         Added one method removeAllAtoms()
 *         date: 30 Jul 97
 * @see Atom
 */
public class AtomVector extends Vector {
    /**
     * Default constructor
     */
    public AtomVector() {
        super();
    }

    /**
     * Capacity constructor
     *
     * @param cap initial capacity of vector
     */
    public AtomVector(int cap) {
        super(cap, cap);
    }

    /**
     * Capacity and increment constructor
     *
     * @param cap initial capacity of vector
     * @param inc increment factor
     */
    public AtomVector(int cap, int inc) {
        super(cap, inc);
    }

    /**
     * Copy constructor
     *
     * @param av AtomVector to be copied
     */
    public AtomVector(AtomVector av) {
        super(av.size(), av.size());

        for (int i = 0; i < av.size(); i++) {
            Atom a = new Atom(av.getAtom(i));
            append(a);
        }
    }

    /**
     * Append an atom to the end of vector
     *
     * @param a atom to be appended
     */
    public final synchronized void append(Atom a) {
        addElement(a);
    }

    /**
     * Returns atom at specified index
     *
     * @param i index of atom
     * @throws ArrayIndexOutOfBoundsException if index >= capacity()
     */
    public final synchronized Atom getAtom(int i)
            throws ArrayIndexOutOfBoundsException {
        return (Atom) elementAt(i);
    }

    /**
     * Set an atom at specified index; atom at index is replaced
     *
     * @param a atom to be set
     * @param i index to place atom
     */
    public final synchronized void set(Atom a, int i) {
        setElementAt(a, i);
    }

    /**
     * Insert an atom at specified index; other atoms are shifted over
     *
     * @param a atom to be inserted
     * @param i index of insertion
     */
    public final synchronized void insert(Atom a, int i) {
        insertElementAt(a, i);
    }

    /**
     * Remove an atom; other atoms are shifted down
     *
     * @param a atom to be removed
     * @return <tt>true</tt> if atom was removed, else <tt>false</tt>
     */
    public final synchronized boolean removeAtom(Atom a) {
        return removeElement(a);
    }

    /**
     * Remove atom at specified index; other atoms are shifted down
     *
     * @param i index of atom
     * @throws ArrayIndexOutOfBoundsException if index >= capacity()
     */
    public final synchronized void removeAtom(int i)
            throws ArrayIndexOutOfBoundsException {
        removeElementAt(i);
    }

    /**
     * Remove all atoms
     *
     * @param i index of atom
     * @throws ArrayIndexOutOfBoundsException if index >= capacity()
     */
    public final synchronized void removeAllAtoms()
            throws ArrayIndexOutOfBoundsException {
        removeAllElements();
    }

    /**
     * Returns first atom in AtomVector
     *
     * @throws NoSuchElementException if size() == 0
     */
    public final synchronized Atom firstAtom() throws NoSuchElementException {
        return (Atom) firstElement();
    }

    /**
     * Returns last atom in AtomVector
     *
     * @throws NoSuchElementException if size() == 0
     */
    public final synchronized Atom lastAtom() throws NoSuchElementException {
        return (Atom) lastElement();
    }

    /**
     * Compares this AtomVector with another
     *
     * @param av AtomVector to compare with
     * @return <tt>true</tt> if they are equal, else <tt>false</tt>
     */
    public boolean equals(AtomVector av) {
        if (av == null) {
            return false;
        }

        if (size() != av.size()) {
            return false;
        }

        for (int i = 0; i < size(); i++) {
            Atom a1 = getAtom(i);
            Atom a2 = av.getAtom(i);

            if (!a1.equals(a2)) {
                return false;
            }
        }

        return true;
    }
} // end of class AtomVector
