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

package org.jscience.io.fits;

/**
 * represents a column in an ASCII table extension
 */
public class FitsASCIIColumn extends FitsColumn {
    /** DOCUMENT ME! */
    int start_byte;

    /** DOCUMENT ME! */
    int decimals;

/**
     * Create a new column object which will represent the given column in the
     * given FITS header.
     *
     * @param header A FITS header describing the column
     * @param col    the column to read from the FITS file. Note: this value
     *               counts from "1" unlike most other column indices in this
     *               package.
     * @throws FitsException DOCUMENT ME!
     */
    public FitsASCIIColumn(FitsHeader header, int col)
        throws FitsException {
        super(header, col);
/**
         * no vectors in ASCII tables
         */
        count = 1;
/**
         * parse the form string
         */
        type = form.charAt(0);

        try {
            if (type == 'I') {
                representation = Class.forName("java.lang.Integer");
            } else if (type == 'A') {
                representation = Class.forName("java.lang.String");
            } else if (type == 'E') {
                representation = Class.forName("java.lang.Float");
            } else if (type == 'F') {
                representation = Class.forName("java.lang.Float");
            } else if (type == 'D') {
                representation = Class.forName("java.lang.Double");
            } else {
                throw new FitsException("unknown ASCII table data type " +
                    type);
            }
        } catch (ClassNotFoundException e) {
            System.err.println(e);
            throw new FitsException("Unknown class in " + this);
        }

/**
         * now parse the width and decimal count
         */
        try {
            int dot = form.indexOf('.');

            if (dot >= 0) {
                bytes = Integer.parseInt(form.substring(1, dot));
                decimals = Integer.parseInt(form.substring(dot + 1));
            } else {
                bytes = Integer.parseInt(form.substring(1));
                decimals = 0;
            }
        } catch (Exception e) {
            throw new FitsException("Can't parse TFORM" + col + " - " + form +
                " " + e);
        }

/**
         * get the start byte
         */
        start_byte = header.card("TBCOL" + col).intValue() - 1; // convert to zero offset
    } // end of constructor

    /**
     * Returns the byte offset of the first byte in this column with
     * respect to the beginning of a row.
     *
     * @return the byte offset of this column.
     */
    public int getStartByte() {
        return start_byte;
    }

    /**
     * Returns the number of decimal places used to represent floating
     * point numbers in this column.
     *
     * @return the number of decimal places in this column or zero if this is
     *         not a floating point column.
     */
    public int getDecimals() {
        return decimals;
    }
} // end of FitsBinaryColumn class
