/*
 * MoleculeImpl.java
 *
 * Created on September 7, 2003, 8:07 PM
 */
package org.jscience.chemistry.quantum;

import org.jscience.chemistry.quantum.math.util.Point3D;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * The Molecule class
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class Molecule {
    /** DOCUMENT ME! */
    private String title;

    /** DOCUMENT ME! */
    private ArrayList atomList;

    /** DOCUMENT ME! */
    private AtomInfo atomInfo;

    /** Holds value of property numberOfElectrons. */
    private int numberOfElectrons;

/**
     * Creates a new instance of MoleculeImpl
     */
    public Molecule() {
        this("Molecule");
    }

/**
     * Creates a new Molecule object.
     *
     * @param title DOCUMENT ME!
     */
    public Molecule(String title) {
        this.title = title;

        // init values
        atomList = new ArrayList();

        numberOfElectrons = 0;

        atomInfo = AtomInfo.getInstance();
    }

    /**
     * Returns the title.
     *
     * @return String identifying the title of the Molecule object
     */
    public String getTitle() {
        return title;
    }

    /**
     * Overloaded addAtom() method.
     *
     * @param atom the instance of atom class
     */
    public void addAtom(Atom atom) {
        atomList.add(atom);

        // and numberOfElectrons
        numberOfElectrons += atomInfo.getAtomicNumber(atom.getSymbol());
    }

    /**
     * remove an atom from this Molecule at a given index May throw
     * runtime exception, if invalid index
     *
     * @param atomIndex the atom index to be removed
     */
    public void removeAtomAt(int atomIndex) {
        Atom atom = (Atom) atomList.get(atomIndex);

        // and numberOfElectrons
        numberOfElectrons -= atomInfo.getAtomicNumber(atom.getSymbol());

        // remove the atom
        atomList.remove(atomIndex);

        // then reindex the atoms
        Iterator atoms = atomList.iterator();
        int index = 0;
        Atom atom1;

        while (atoms.hasNext()) {
            atom1 = ((Atom) atoms.next());
            atom1.setIndex(index++);
        } // emd while
    }

    /**
     * remove an atom from this Molecule
     *
     * @param atom the instance of atom class
     */
    public void removeAtom(Atom atom) {
        // adjust the molecular weight
        numberOfElectrons -= atomInfo.getAtomicNumber(atom.getSymbol());

        // remove it
        atomList.remove(atom);
    }

    /**
     * Overloaded addAtom() method.
     *
     * @param symbol the atom symbol
     * @param atomCenter the cartesian coordinates of the atom stored as
     *        Point3D object
     */
    public void addAtom(String symbol, Point3D atomCenter) {
        addAtom(new Atom(symbol, 0.0, atomCenter));
    }

    /**
     * Overloaded addAtom() method.
     *
     * @param symbol the atom symbol
     * @param charge is the charge on the atom (the atomic number in many
     *        cases)
     * @param atomCenter the cartesian coordinates of the atom stored as
     *        Point3D object
     */
    public void addAtom(String symbol, double charge, Point3D atomCenter) {
        addAtom(new Atom(symbol, charge, atomCenter));
    }

    /**
     * Adds an atom to this molecule object.
     *
     * @param symbol the atom symbol
     * @param x y, z the cartesian coordinates of the atom
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     */
    public void addAtom(String symbol, double x, double y, double z) {
        addAtom(new Atom(symbol, 0.0, new Point3D(x, y, z)));
    }

    /**
     * Adds an atom to this molecule object.
     *
     * @param symbol the atom symbol
     * @param x y, z the cartesian coordinates of the atom
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     * @param index the atom index of this atom
     */
    public void addAtom(String symbol, double x, double y, double z, int index) {
        addAtom(new Atom(symbol, 0.0, new Point3D(x, y, z), index));
    }

    /**
     * Overloaded addAtom() method.
     *
     * @param symbol the atom symbol
     * @param charge is the charge on the atom (the atomic number in many
     *        cases)
     * @param x y, z the cartesian coordinates of the atom
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     */
    public void addAtom(String symbol, double charge, double x, double y,
        double z) {
        addAtom(new Atom(symbol, charge, new Point3D(x, y, z)));
    }

    /**
     * Method to get a particular atom from the lists of atoms.
     *
     * @param atomIndex DOCUMENT ME!
     *
     * @return Atom the instance of atom class
     */
    public Atom getAtom(int atomIndex) {
        return (Atom) atomList.get(atomIndex);
    }

    /**
     * Method to get a particular atom from the lists of atoms.
     *
     * @return an iterator object containing a linear list of atoms in the
     *         Molecule!
     */
    public Iterator getAtoms() {
        return atomList.iterator();
    }

    /**
     * sets the title.
     *
     * @param title identifying the title of the Molecule object
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Method to get total number of atoms in the molecule.
     *
     * @return number of atoms in the Molecule
     */
    public int getNumberOfAtoms() {
        return atomList.size();
    }

    /**
     * Getter for property numberOfElectrons.
     *
     * @return Value of property numberOfElectrons.
     */
    public int getNumberOfElectrons() {
        return this.numberOfElectrons;
    }
} // end of class MoleculeImpl
