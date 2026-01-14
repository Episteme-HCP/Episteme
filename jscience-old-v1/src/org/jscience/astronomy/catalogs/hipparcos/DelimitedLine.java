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

package org.jscience.astronomy.catalogs.hipparcos;

import java.util.StringTokenizer;


//this package is rebundled from Hipparcos Java package from
//William O'Mullane
//http://astro.estec.esa.nl/Hipparcos/hipparcos_java.html
//mailto:hipparcos@astro.estec.esa.nl
/**
 * Wraps up a line in s Stringtokenizeer using a given delimiter provide
 * usefull casting methods to get back doubles etc.
 */
class DelimitedLine {
    /** DOCUMENT ME! */
    protected StringTokenizer line;

/**
     * Creates a new DelimitedLine object.
     *
     * @param line  DOCUMENT ME!
     * @param delim DOCUMENT ME!
     */
    public DelimitedLine(String line, char delim) {
        this.line = new StringTokenizer(line, new String(delim + ""));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected String getNext() throws Exception {
        return (line.nextToken().trim());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public String getNextString() throws Exception {
        return (getNext());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public int getNextInt() throws Exception {
        return (new Integer(getNext()).intValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public double getNextDouble() throws Exception {
        String tmp = getNext();

        if (tmp.length() == 0) {
            //System.err.println ("Not a valid double|"+tmp+".");
            throw (new Exception());
        }

        return (new Double(tmp).doubleValue());
    }

    /**
     * parse the string as a set of doubles and return it in an array
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public double[] getDoubleArray() throws Exception {
        if (line.countTokens() == 0) {
            throw new Exception("Empty String for array");
        }

        double[] dnums = new double[line.countTokens()];

        for (int t = 0; t < dnums.length; t++) {
            dnums[t] = getNextDouble();
        }

        return dnums;
    }

    /**
     * parse the string as a set of ints and return it in an array
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public int[] getIntArray() throws Exception {
        if (line.countTokens() == 0) {
            throw new Exception("Empty String for array");
        }

        int[] inums = new int[line.countTokens()];

        for (int t = 0; t < inums.length; t++) {
            inums[t] = getNextInt();
        }

        return inums;
    }
}
