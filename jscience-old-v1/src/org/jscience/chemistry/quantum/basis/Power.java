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
 * Power.java
 *
 * Created on July 23, 2004, 6:51 AM
 */
package org.jscience.chemistry.quantum.basis;

/**
 * Represents the powers on orbitals. <br>
 * They are also the magnetic quantum numbers.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class Power {
    /** Holds value of property l. */
    private int l;

    /** Holds value of property m. */
    private int m;

    /** Holds value of property n. */
    private int n;

/**
     * Creates a new instance of Power
     *
     * @param l DOCUMENT ME!
     * @param m DOCUMENT ME!
     * @param n DOCUMENT ME!
     */
    public Power(int l, int m, int n) {
        this.l = l;
        this.m = m;
        this.n = n;
    }

    /**
     * Getter for property px.
     *
     * @return Value of property px.
     */
    public int getL() {
        return this.l;
    }

    /**
     * Setter for property px.
     *
     * @param l New value of property px.
     */
    public void setL(int l) {
        this.l = l;
    }

    /**
     * Getter for property py.
     *
     * @return Value of property py.
     */
    public int getM() {
        return this.m;
    }

    /**
     * Setter for property py.
     *
     * @param m New value of property py.
     */
    public void setM(int m) {
        this.m = m;
    }

    /**
     * Getter for property pz.
     *
     * @return Value of property pz.
     */
    public int getN() {
        return this.n;
    }

    /**
     * Setter for property pz.
     *
     * @param n New value of property pz.
     */
    public void setN(int n) {
        this.n = n;
    }

    /**
     * overloaded toString()
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "[" + l + ", " + m + ", " + n + "]";
    }
} // end of class Power
