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

package org.jscience.chemistry.vapor;

/**
 * Data class to store component data.
 */
public class ComponentData {
    /**
     * DOCUMENT ME!
     */
    public int compId = -1;

    /**
     * DOCUMENT ME!
     */
    public String name = null;

    /**
     * DOCUMENT ME!
     */
    public double Tc = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public double Pc = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public double Zc = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public double Vc = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public double omega = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public double antA = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public double antB = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public double antC = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public boolean isCriticalDataAvailable = true;

    /**
     * DOCUMENT ME!
     */
    public boolean isAntoineDataAvailable = true;

    /**
     * DOCUMENT ME!
     */
    public void init() {
        if (Tc == Constants.DOUBLE_NULL) {
            isCriticalDataAvailable = false;
        }

        if (antA == Constants.DOUBLE_NULL) {
            isAntoineDataAvailable = false;
        }

        Vc = (Constants.R_J_PER_KMOL_K * Zc * Tc) / Pc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return name;
    }
}
