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
 * AtomInfo.java
 *
 * Created on November 23, 2003, 2:51 PM
 */

package org.jscience.chemistry.quantum;

import java.util.Hashtable;

/**
 * The default AtomProperty configuration.
 * <p/>
 * .. follows a singleton pattern.
 * .. and an observer pattern for notifying the registered classes of the
 * changes at runtime.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class AtomInfo {

    private static AtomInfo _atomInfo;

    private static final int DEFAULT_TABLE_SIZE = 90;

    private String element = new String("element");
    private String symbol = new String("symbol");

    /**
     * Holds value of property nameTable.
     */
    private Hashtable nameTable;

    /**
     * Holds value of property atomicNumberTable.
     */
    private Hashtable atomicNumberTable;

    /**
     * Holds value of property atomicWeightTable.
     */
    private Hashtable atomicWeightTable;

    /**
     * Holds value of property covalentRadiusTable.
     */
    private Hashtable covalentRadiusTable;

    /**
     * Holds value of property vdwRadiusTable.
     */
    private Hashtable vdwRadiusTable;

    /**
     * Holds value of property defaultValencyTable.
     */
    private Hashtable defaultValencyTable;

    /**
     * Holds value of property weakBondAngleTable.
     */
    private Hashtable weakBondAngleTable;

    /**
     * Holds value of property colorTable.
     */
    private Hashtable colorTable;

    /**
     * Utility field used by event firing mechanism.
     */
    private javax.swing.event.EventListenerList listenerList = null;

    /**
     * Holds value of property doubleBondOverlapTable.
     */
    private Hashtable doubleBondOverlapTable;

    /**
     * Creates a new instance of AtomInfo
     */
    public AtomInfo() {
        nameTable = new Hashtable(DEFAULT_TABLE_SIZE);
        atomicNumberTable = new Hashtable(DEFAULT_TABLE_SIZE);
        atomicWeightTable = new Hashtable(DEFAULT_TABLE_SIZE);
        covalentRadiusTable = new Hashtable(DEFAULT_TABLE_SIZE);

        // the initial parameters
        setDefaultParams();
    }

    /**
     * Obtain an instance of this ...
     */
    public static AtomInfo getInstance() {
        if (_atomInfo == null) {
            _atomInfo = new AtomInfo();
        } // end if

        return _atomInfo;
    }

    /**
     * method to resent the instance of AtomInfo .. so that default values
     * are loaded. Please note that no event is fired here.
     */
    public static void reset() {
        if (_atomInfo == null) return;

        // re-initilize the parameters
        _atomInfo.setDefaultParams();
    }

    /**
     * private method to set the default parameters
     */
    private void setDefaultParams() {
        // Right now only bare minimum is done here!

        nameTable.put("H", "Hydrogen");
        atomicNumberTable.put("H", new Integer(1));

        nameTable.put("C", "Carbon");
        atomicNumberTable.put("C", new Integer(6));

        nameTable.put("N", "Nitrogen");
        atomicNumberTable.put("N", new Integer(7));

        nameTable.put("O", "Oxygen");
        atomicNumberTable.put("O", new Integer(8));
    }

    /**
     * Getter for property nameTable.
     *
     * @return Value of property nameTable.
     */
    public Hashtable getNameTable() {
        return this.nameTable;
    }

    /**
     * Setter for property nameTable.
     *
     * @param nameTable New value of property nameTable.
     */
    public void setNameTable(Hashtable nameTable) {
        this.nameTable = nameTable;
    }

    /**
     * Getter for property atomicNumberTable.
     *
     * @return Value of property atomicNumberTable.
     */
    public Hashtable getAtomicNumberTable() {
        return this.atomicNumberTable;
    }

    /**
     * Setter for property atomicNumberTable.
     *
     * @param atomicNumberTable New value of property atomicNumberTable.
     */
    public void setAtomicNumberTable(Hashtable atomicNumberTable) {
        this.atomicNumberTable = atomicNumberTable;
    }

    /**
     * Getter for property atomicWeightTable.
     *
     * @return Value of property atomicWeightTable.
     */
    public Hashtable getAtomicWeightTable() {
        return this.atomicWeightTable;
    }

    /**
     * Setter for property atomicWeightTable.
     *
     * @param atomicWeightTable New value of property atomicWeightTable.
     */
    public void setAtomicWeightTable(Hashtable atomicWeightTable) {
        this.atomicWeightTable = atomicWeightTable;
    }

    /**
     * Getter for property covalentRadiusTable.
     *
     * @return Value of property covalentRadiusTable.
     */
    public Hashtable getCovalentRadiusTable() {
        return this.covalentRadiusTable;
    }

    /**
     * Setter for property covalentRadiusTable.
     *
     * @param covalentRadiusTable New value of property covalentRadiusTable.
     */
    public void setCovalentRadiusTable(Hashtable covalentRadiusTable) {
        this.covalentRadiusTable = covalentRadiusTable;
    }

    /**
     * Getter for property vdwRadiusTable.
     *
     * @return Value of property vdwRadiusTable.
     */
    public Hashtable getVdwRadiusTable() {
        return this.vdwRadiusTable;
    }

    /**
     * Setter for property vdwRadiusTable.
     *
     * @param vdwRadiusTable New value of property vdwRadiusTable.
     */
    public void setVdwRadiusTable(Hashtable vdwRadiusTable) {
        this.vdwRadiusTable = vdwRadiusTable;
    }

    /**
     * Getter for property defaultValencyTable.
     *
     * @return Value of property defaultValencyTable.
     */
    public Hashtable getDefaultValencyTable() {
        return this.defaultValencyTable;
    }

    /**
     * Setter for property defaultValencyTable.
     *
     * @param defaultValencyTable New value of property defaultValencyTable.
     */
    public void setDefaultValencyTable(Hashtable defaultValencyTable) {
        this.defaultValencyTable = defaultValencyTable;
    }

    /**
     * Getter for property weakBondAngleTable.
     *
     * @return Value of property weakBondAngleTable.
     */
    public Hashtable getWeakBondAngleTable() {
        return this.weakBondAngleTable;
    }

    /**
     * Setter for property weakBondAngleTable.
     *
     * @param weakBondAngleTable New value of property weakBondAngleTable.
     */
    public void setWeakBondAngleTable(Hashtable weakBondAngleTable) {
        this.weakBondAngleTable = weakBondAngleTable;
    }

    /**
     * Getter for property doubleBondOverlap.
     *
     * @return Value of property doubleBondOverlap.
     */
    public Hashtable getDoubleBondOverlapTable() {
        return this.doubleBondOverlapTable;
    }

    /**
     * Setter for property doubleBondOverlap.
     *
     * @param doubleBondOverlap New value of property doubleBondOverlap.
     */
    public void setDoubleBondOverlapTable(Hashtable doubleBondOverlapTable) {
        this.doubleBondOverlapTable = doubleBondOverlapTable;
    }

    /**
     * Getter for property colorTable.
     *
     * @return Value of property colorTable.
     */
    public Hashtable getColorTable() {
        return this.colorTable;
    }

    /**
     * Setter for property colorTable.
     *
     * @param colorTable New value of property colorTable.
     */
    public void setColorTable(Hashtable colorTable) {
        this.colorTable = colorTable;
    }

    /**
     * Getter for property atomicNumber.
     *
     * @param symbol - the atom symbol, IUPAC name!
     * @return Value of property atomicNumber for the specified symbol
     */
    public int getAtomicNumber(String symbol) {
        return ((Integer) atomicNumberTable.get(symbol)).intValue();
    }

    /**
     * Setter for property atomicNumber.
     *
     * @param symbol       - the atom symbol, IUPAC name!
     * @param atomicNumber New value of property atomicNumber.
     */
    public void setAtomicNumber(String symbol, int atomicNumber) {
        atomicNumberTable.put(symbol, new Integer(atomicNumber));
    }

    /**
     * Getter for property atomicWeight.
     *
     * @param symbol - the atom symbol, IUPAC name!
     * @return Value of property atomicWeight for the specified symbol
     */
    public double getAtomicWeight(String symbol) {
        return ((Double) atomicWeightTable.get(symbol)).doubleValue();
    }

    /**
     * Setter for property atomicWeight.
     *
     * @param symbol       - the atom symbol, IUPAC name!
     * @param atomicWeight New value of property atomicWeight.
     */
    public void setAtomicWeight(String symbol, double atomicWeight) {
        atomicWeightTable.put(symbol, new Double(atomicWeight));
    }

    /**
     * Getter for property defaultValency.
     *
     * @param symbol - the atom symbol, IUPAC name!
     * @return Value of property defaultValency for the specified symbol
     */
    public int getDefaultValency(String symbol) {
        return ((Integer) defaultValencyTable.get(symbol)).intValue();
    }

    /**
     * Setter for property defaultValency.
     *
     * @param symbol         - the atom symbol, IUPAC name!
     * @param defaultValency New value of property defaultValency.
     */
    public void setDefaultValency(String symbol, int defaultValency) {
        defaultValencyTable.put(symbol, new Integer(defaultValency));
    }

    /**
     * Getter for property covalentRadius.
     *
     * @param symbol - the atom symbol, IUPAC name!
     * @return Value of property covalentRadius for the specified symbol
     */
    public double getCovalentRadius(String symbol) {
        return ((Double) covalentRadiusTable.get(symbol)).doubleValue();
    }

    /**
     * Setter for property covalentRadius.
     *
     * @param symbol         - the atom symbol, IUPAC name!
     * @param covalentRadius New value of property covalentRadius.
     */
    public void setCovalentRadius(String symbol, double covalentRadius) {
        covalentRadiusTable.put(symbol, new Double(covalentRadius));
    }

    /**
     * Getter for property vdwRadius.
     *
     * @param symbol - the atom symbol, IUPAC name!
     * @return Value of property vdwRadius for the specified symbol
     */
    public double getVdwRadius(String symbol) {
        return ((Double) vdwRadiusTable.get(symbol)).doubleValue();
    }

    /**
     * Setter for property vdwRadius.
     *
     * @param symbol    - the atom symbol, IUPAC name!
     * @param vdwRadius New value of property vdwRadius.
     */
    public void setVdwRadius(String symbol, double vdwRadius) {
        vdwRadiusTable.put(symbol, new Double(vdwRadius));
    }

    /**
     * Getter for property name.
     *
     * @param symbol - the atom symbol, IUPAC name!
     * @return Value of property name for the specified symbol
     */
    public String getName(String symbol) {
        return (String) nameTable.get(symbol);
    }

    /**
     * Setter for property weakBondAngle.
     *
     * @param symbol - the atom symbol, IUPAC name!
     * @param name   New value of property name.
     */
    public void setName(String symbol, String name) {
        nameTable.put(symbol, name);
    }
} // end of class AtomInfo
