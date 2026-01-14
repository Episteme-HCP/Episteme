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
 * ***********************************************************************
 * Represents the basic building block of a FITS file - the "Header Data Unit".
 * Each HDU consists of a header containing metadata and a data section
 * containing the actual data (e.g. a table or image).
 * <p/>
 * ************************************************************************
 */
public class FitsHDU {
    /**
     * the header for this HDU *
     */
    FitsHeader header;

    /**
     * the object representing the data for this HDU
     */
    FitsData data;

    /**
     * ***********************************************************************
     * Create an empty HDU with no data and no keywords in the header. This
     * constructor is useful when creating a FITS file in memory. The proceedure
     * is as follows:
     * <ol>
     * <li> Call this constructor
     * <li> use {@link #getHeader() } to obtain the header object
     * <li> set the required keywords in the header
     * <li> use {@link #initData() } to create the data section internally
     * <li> use {@link #getData() } to obtain the newly created data object
     * <li> set the data values
     * </ol>
     * ************************************************************************
     */
    public FitsHDU() {
        header = new FitsHeader();
    } // end of constructor

    /**
     * ***********************************************************************
     * Create an HDU with the given header and data. The caller is responsible for
     * ensuring that the header and data are compatible. If you are creating an HDU
     * in memory, it may be more convenient to use {@link #FitsHDU() } and then
     * {@link #initData() }.
     *
     * @param header the header object to use in this HDU
     * @param data   the data object to use in this HDU
     *               ************************************************************************
     */
    public FitsHDU(FitsHeader header, FitsData data) {
        this.header = header;
        this.data = data;
    } // end of constructor

    /**
     * ***********************************************************************
     * returns the header part of this HDU
     * ************************************************************************
     */
    public FitsHeader getHeader() {
        return header;
    }

    /**
     * ***********************************************************************
     * returns the data part of this HDU.
     * If the data section is uninitialized, then this method returns null.
     * The data can be uninitialized if the HDU is being created in memory and
     * you have not yet called {@link #initData() }, or you did not require the data
     * to be read when you obtained the HDU from the {@link FitsFile}.
     *
     * @see FitsFile#getHDU(int,int)
     *      ************************************************************************
     */
    public FitsData getData() {
        return data;
    }

    /**
     * ***********************************************************************
     * returns the result of calling {@link FitsHeader#getName() } for the header
     * of this HDU.
     * ************************************************************************
     */
    public String name() {
        return header.getName();
    } // end of name method

    /**
     * ***********************************************************************
     * returns the number of blocks in both the header and data sections
     *
     * @throws NullPointerException if the data part is not initialized.
     *                              ************************************************************************
     */
    public int blockCount() {
        return header.blockCount() + data.blockCount();
    }

    /**
     * ***********************************************************************
     * internally creates an object to represent the data part of the HDU which
     * corresponds to the header of this HDU. Any existing data in this HDU
     * are discarded.
     * This method does nothing if the header is not complete.
     *
     * @throws FitsException if the header is improperly formed.
     *                       ************************************************************************
     */
    public void initData() throws FitsException {
        if (!header.isComplete()) {
            return;
        }

        data = FitsData.createFrom(header);
    } // end of initData method
} // end of FitsHDU class
