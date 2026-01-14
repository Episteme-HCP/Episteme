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

package org.jscience.physics.fluids.dynamics.util;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Vector2DFastInt {
    /** DOCUMENT ME! */
    int[] data;

    /** DOCUMENT ME! */
    int[] pos;

    /** DOCUMENT ME! */
    int imax;

    /** DOCUMENT ME! */
    int jmax;

    /** DOCUMENT ME! */
    int incr;

/**
     * constructor
     *
     * @param imax DOCUMENT ME!
     * @param jtam DOCUMENT ME!
     * @param incr DOCUMENT ME!
     */
    public Vector2DFastInt(int imax, int jtam, int incr) {
        this.incr = incr;
        this.imax = imax;
        this.jmax = jtam;
        data = new int[imax * jmax];
        pos = new int[imax];
        reset();
    }

    /**
     * add an element.
     *
     * @param i DOCUMENT ME!
     * @param valor DOCUMENT ME!
     */
    public void addElement(int i, int valor) {
        data[(i * jmax) + pos[i]] = valor;
        pos[i]++;

        if (pos[i] == jmax) {
            int[] d2 = new int[imax * (jmax + incr)];

            for (int k = 0; k < imax; k++)
                for (int j = 0; j < jmax; j++)
                    d2[(k * (jmax + incr)) + j] = data[(k * jmax) + j];

            jmax += incr;

            data = d2;

            System.out.println("Redimensioning Vector2DFastInt -> " + jmax);
        }
    }

    /**
     * add an element only if not contained yet.
     *
     * @param i DOCUMENT ME!
     * @param valor DOCUMENT ME!
     */
    public void addElementNoRep(int i, int valor) {
        if (isElement(i, valor)) {
            return;
        }

        addElement(i, valor);
    }

    /**
     * returns the data contained in the container as an array of
     * int's.
     *
     * @return DOCUMENT ME!
     */
    public int[] array() {
        return data;
    }

    /**
     * returns the element at the given position
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int elementAt(int i, int j) {
        return data[(i * jmax) + j];
    }

    /**
     * returns the initial position in array() of the row i.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getStartOfRow(int i) {
        return i * jmax;
    }

    /**
     * Sorts all elements of all rows.
     *
     * @param type 1 for going from lower to bigger, -1 for going from bigger
     *        to lower.
     */
    public void sortArrayRows(int type) {
        int t;

        for (int r = 0; r < imax; r++) {
            // Bubble sort
            for (int j = 0; j < (pos[r] - 1); j++)
                for (int i = j + 1; i < pos[r]; i++)
                    if ((type * (data[(r * jmax) + j] - data[(r * jmax) + i])) > 0) {
                        t = data[(r * jmax) + j];
                        data[(r * jmax) + j] = data[(r * jmax) + i];
                        data[(r * jmax) + i] = t;
                    }
        }
    }

    /**
     * returns <code>true</code> if the element is contained in the
     * row.
     *
     * @param i DOCUMENT ME!
     * @param valor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isElement(int i, int valor) {
        for (int j = 0; j < pos[i]; j++)
            if (data[(i * jmax) + j] == valor) {
                return true;
            }

        return false;
    }

    /**
     * Empty the vector quickly, but keeping the memory RAM. This
     * method es useful to reuse the array without freeing it and reserving
     * another.
     */
    public void reset() {
        for (int j = 0; j < pos.length; j++)
            pos[j] = 0;
    }

    /**
     * returns the length of the row i.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int size(int i) {
        return pos[i];
    }
}
