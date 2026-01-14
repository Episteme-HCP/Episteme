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
 * SCFType.java
 *
 * Created on August 5, 2004, 10:58 PM
 */
package org.jscience.chemistry.quantum;

/**
 * SCF Type enumeration.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class SCFType {
    /** The Hartree Fock method */
    public static final SCFType HARTREE_FOCK = new SCFType(1);

    /** The Moller Plesset method */
    public static final SCFType MOLLER_PLESSET = new SCFType(2);

    /** Holds value of property type. */
    private int type;

/**
     * Creates a new instance of SCFType
     *
     * @param type DOCUMENT ME!
     */
    private SCFType(int type) {
        this.type = type;
    }

    /**
     * Getter for property type.
     *
     * @return Value of property type.
     */
    public int getType() {
        return this.type;
    }

    /**
     * Returns a description of the SCF type
     *
     * @return a string indicating SCF type
     */
    public String toString() {
        String description = "";

        if (this.equals(HARTREE_FOCK)) {
            description = "Hartree Fock Method";
        } else if (this.equals(MOLLER_PLESSET)) {
            description = "Moller Plesset Method";
        } else {
            description = "No description available";
        } // end if

        return description;
    }

    /**
     * The method to check the equality of two objects of SCFType
     * class.
     *
     * @param obj DOCUMENT ME!
     *
     * @return true/ false specifying the equality or inequality
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        return ((obj != null) && (obj instanceof SCFType) &&
        (this.type == ((SCFType) obj).type));
    }
} // end of class SCFType
