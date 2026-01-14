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
 * Atom.java
 *
 * Created on April 27, 2003, 2:47 PM
 */
package org.jscience.chemistry.quantum;

import org.jscience.chemistry.quantum.math.util.Point3D;


/**
 * This class define the structure of an Atom
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class Atom implements Cloneable {
    /** Holds value of property symbol. */
    private String symbol;

    /** Holds value of property charge. */
    private double charge;

    /** Holds value of property atomCenter. */
    private Point3D atomCenter;

    /** hash code cached here */
    private volatile int hashCode = 0;

    /** Holds value of property index. */
    private int index;

/**
     * Creates a new instance of Atom
     *
     * @param symbol     The atom symbol
     * @param charge     The charge on the atom
     * @param atomCenter The nuclear center of the atom in cartesian
     *                   coordinates
     */
    public Atom(String symbol, double charge, Point3D atomCenter) {
        this(symbol, charge, atomCenter, 0);
    }

/**
     * Creates a new instance of Atom
     *
     * @param symbol     The atom symbol
     * @param charge     The charge on the atom
     * @param atomCenter The nuclear center of the atom in cartesian
     *                   coordinates
     * @param atomIndex  DOCUMENT ME!
     */
    public Atom(String symbol, double charge, Point3D atomCenter, int atomIndex) {
        this.symbol = capitalise(symbol.toLowerCase());
        this.charge = charge;
        this.index = atomIndex;
        this.atomCenter = atomCenter;
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
     * Method to capitalise a string .. not the correct palce!, but
     * dumped here
     *
     * @param theString string to be capitalised
     *
     * @return the string which is capitalised
     */
    public String capitalise(String theString) {
        String firstChar = theString.substring(0, 1);
        String restString = theString.substring(1, theString.length());

        if (restString == null) {
            return firstChar.toUpperCase();
        } else {
            return (firstChar.toUpperCase() + restString);
        }
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
     * Getter for property charge.
     *
     * @return Value of property charge.
     */
    public double getCharge() {
        return this.charge;
    }

    /**
     * Setter for property charge.
     *
     * @param charge New value of property charge.
     */
    public void setCharge(double charge) {
        this.charge = charge;
    }

    /**
     * Getter for property atomCenter.
     *
     * @return Value of property atomCenter.
     */
    public Point3D getAtomCenter() {
        return this.atomCenter;
    }

    /**
     * Setter for property atomCenter.
     *
     * @param atomCenter New value of property atomCenter.
     */
    public void setAtomCenter(Point3D atomCenter) {
        this.atomCenter = atomCenter;
    }

    /**
     * Getter for property x.
     *
     * @return Value of property x.
     */
    public double getX() {
        return atomCenter.getX();
    }

    /**
     * Getter for property y.
     *
     * @return Value of property y.
     */
    public double getY() {
        return atomCenter.getY();
    }

    /**
     * Getter for property z.
     *
     * @return Value of property z.
     */
    public double getZ() {
        return atomCenter.getZ();
    }

    /**
     * overloaded toString() method.
     *
     * @return A description of Atom object
     */
    public String toString() {
        return symbol + " " + charge + " " + atomCenter.toString();
    }

    /**
     * overriden hashCode() method
     *
     * @return int - the hashCode
     */
    public int hashCode() {
        if (hashCode == 0) {
            int result = 17; // prime number!
            long c;

            result = (37 * result) + symbol.hashCode();
            result = (37 * result) + (int) charge;
            result = (37 * result) + atomCenter.hashCode();

            hashCode = result;
        } // end if

        return hashCode;
    }

    /**
     * overloaded equals() method
     *
     * @param obj The object to be compared with
     *
     * @return true : they are same else not
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (!(obj instanceof Atom))) {
            return false;
        } else {
            Atom o = (Atom) obj;

            return ((o.symbol.equals(symbol)) && (o.charge == charge) &&
            (o.atomCenter.equals(atomCenter)));
        } // end if
    }

    /**
     * Simple method to find the distance between two atom centers.
     *
     * @param atom - the atom from which the distance is to be found
     *
     * @return the distance between the two atom centers
     */
    public double distanceFrom(Atom atom) {
        return distanceFrom(atom.atomCenter);
    }

    /**
     * Simple method to find the distance between atom center and an
     * point.
     *
     * @param point - the point to which the distance is to be found
     *
     * @return the distance between the point and the atom center
     */
    public double distanceFrom(Point3D point) {
        return atomCenter.distanceFrom(point);
    }

    /**
     * i do some cloning business ;)
     *
     * @return A copy of the present object
     *
     * @throws CloneNotSupportedException If that isn't possible
     */
    public Object clone() throws CloneNotSupportedException {
        return new Atom(this.symbol, this.charge,
            (Point3D) this.atomCenter.clone(), index);
    }

    /**
     * Getter for property index.
     *
     * @return Value of property index.
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Setter for property index.
     *
     * @param index New value of property index.
     */
    public void setIndex(int index) {
        this.index = index;
    }
} // end of class Atom
