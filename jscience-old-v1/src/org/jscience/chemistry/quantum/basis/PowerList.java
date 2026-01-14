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
 * PowerList.java
 *
 * Created on July 22, 2004, 8:57 PM
 */
package org.jscience.chemistry.quantum.basis;

import java.lang.ref.WeakReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Orbital symbol to power list map. Follows a singleton pattern.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class PowerList {
    /** DOCUMENT ME! */
    private static WeakReference _powerList = null;

    /** DOCUMENT ME! */
    private HashMap thePowerList;

/**
     * Creates a new instance of PowerList
     */
    private PowerList() {
        // put in the standard powers for the orbital symbols
        thePowerList = new HashMap(5);

        // the S orbital
        ArrayList sList = new ArrayList(1);
        sList.add(new Power(0, 0, 0));
        thePowerList.put("S", sList);

        // the P orbital
        ArrayList pList = new ArrayList(3);
        pList.add(new Power(1, 0, 0));
        pList.add(new Power(0, 1, 0));
        pList.add(new Power(0, 0, 1));
        thePowerList.put("P", pList);

        // the D orbital
        ArrayList dList = new ArrayList(6);
        dList.add(new Power(2, 0, 0));
        dList.add(new Power(0, 2, 0));
        dList.add(new Power(0, 0, 2));
        dList.add(new Power(1, 1, 0));
        dList.add(new Power(0, 1, 1));
        dList.add(new Power(1, 0, 1));
        thePowerList.put("D", dList);

        // the F orbital
        ArrayList fList = new ArrayList(10);
        fList.add(new Power(3, 0, 0));
        fList.add(new Power(2, 1, 0));
        fList.add(new Power(2, 0, 1));
        fList.add(new Power(1, 2, 0));
        fList.add(new Power(1, 1, 1));
        fList.add(new Power(1, 0, 2));
        fList.add(new Power(0, 3, 0));
        fList.add(new Power(0, 2, 1));
        fList.add(new Power(0, 1, 2));
        fList.add(new Power(0, 0, 3));
        thePowerList.put("F", fList);
    }

    /**
     * Get an instance (and the only one) of PowerList
     *
     * @return PowerList instance
     */
    public static PowerList getInstance() {
        if (_powerList == null) {
            _powerList = new WeakReference(new PowerList());
        } // end if

        PowerList powerList = (PowerList) _powerList.get();

        if (powerList == null) {
            powerList = new PowerList();
            _powerList = new WeakReference(powerList);
        } // end if

        return powerList;
    }

    /**
     * get the power list for the specified orbital symbol ('S', 'P',
     * 'D' or 'F' .. no explicit error checking done, but will throw a
     * RuntimeException if the arguments are incorrect)
     *
     * @param orbital - 'S', 'P', 'D' or 'F'
     *
     * @return Iterator of Power object representing the powers
     */
    public Iterator getPowerList(String orbital) {
        return ((ArrayList) thePowerList.get(orbital)).iterator();
    }
} // end of class PowerList
