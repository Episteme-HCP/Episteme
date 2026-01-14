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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ****************************************************************************
 * Represents an entire fits file. This particular class is mostly useful for
 * FITS files created in memory.
 * It allows access to the component HDUs, and
 * it has a method for writing the file.
 * <p/>
 * Subclasses like
 * {@link InputStreamFitsFile} and
 * {@link RandomAccessFitsFile} provide the functionality to read from a FITS
 * data source. The HDU access methods in this class allow you to read only
 * part of a FITS file in order to minimize memory usage and I/O, even though
 * only the subclasses can take advantage of them.
 * <p/>
 * *****************************************************************************
 */
public class FitsFile {
    /**
     * ******************************************
     * The number of bytes in a single FITS block -
     * 2880 according to the FITS standard
     * ********************************************
     */
    public static final int BLOCK_SIZE = 2880;

    /**
     * ***************************************************************************
     * Specifies that the data part of an HDU should be read before returning an HDU
     * *****************************************************************************
     */
    public static final int NEED_DATA_NOW = 0;

    /**
     * **************************************************************************
     * specifies that the data need not be read before returning the HDU, but that
     * the ability to read the data later should be preserved.
     * ****************************************************************************
     */
    public static final int NEED_DATA_LATER = 1;

    /**
     * ***************************************************************************
     * Specifies that the data need not be read, and that the ability to read the
     * data later need not be preserved.
     * ****************************************************************************
     */
    public static final int DATA_NOT_NEEDED = 2;

    /**
     * an ordered list of the HDUs which have been read
     */
    protected List hdus;

    /**
     * a collection of the HDUs indexed by EXTNAME
     */
    protected Map index;

    /**
     * a flag indicating whether all the HDUs have been read
     */
    protected boolean isComplete;

    /**
     * ***********************************************************************
     * Create a new empty FITS file with no HDUs (not even the primary one).
     * Use {@link #createEmpty() }  to make an empty file with a default
     * primary HDU.
     * ************************************************************************
     */
    public FitsFile() {
        isComplete = false;

        hdus = new ArrayList();
        index = new HashMap();
    } // end of constructor

    /**
     * ***********************************************************************
     * returns true if all HDUs in this file have been found. This class always
     * returns true, but subclasses may return other values.
     *
     * @return the value of {@link #isComplete }.
     *         ************************************************************************
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * ***********************************************************************
     * add an HDU to this file
     *
     * @param hdu the HDU to be added to this file.
     *            ************************************************************************
     */
    public void add(FitsHDU hdu) {
        hdus.add(hdu);
        index.put(hdu.name(), hdu);
    } // end of add HDU method

    /**
     * ***********************************************************************
     * returns one of the  HDUs in this file.
     *
     * @param number the index of the HDU. The primary HDU is "0".
     * @return The object representing the specified HDU.
     * @throws IOException if there was trouble reading the specified HDU from a
     *                     file.
     *                     ************************************************************************
     */
    public FitsHDU getHDU(int number) throws IOException {
        if (number >= hdus.size()) {
            throw new NoSuchFitsHDUException("Can't get HDU " + number +
                    " in " + this);
        }

        return (FitsHDU) hdus.get(number);
    } // end of getHDU method

    /**
     * ***********************************************************************
     * return an HDU structure with the corresponding EXTNAME. If multiple HDUs have
     * the specified EXTNAME, then it is undefined which one will be returned.
     *
     * @param name the value of the EXTNAME keyword in the desired HDU
     * @return The named HDU.
     * @throws IOException if there was trouble reading the specified HDU from a
     *                     file.
     *                     ************************************************************************
     */
    public FitsHDU getHDU(String name) throws IOException {
        FitsHDU hdu = (FitsHDU) index.get(name);

        if (hdu == null) {
            throw new NoSuchFitsHDUException("Can't get HDU " + name + " in " +
                    this);
        }

        return hdu;
    } // end of getHDU by name method

    /**
     * ***********************************************************************
     * return a given HDU structure, specifying a hint about whether to read
     * the HDU data.
     * A {@link FitsHDU} class holds the entire data content of the HDU in
     * memory. So this method allows you to save memory and the time it takes to
     * read the HDU contents if all you are interested in is the header keywords.
     * Note that only certain types of FITS files (in particular
     * {@link RandomAccessFitsFile}) can take advantage of the hint. The header
     * keywords are always read.
     *
     * @param number the index of the HDU with zero being the primary HDU.
     * @param when   the code indicating when the data will be needed.
     *               It must have one of the values:
     *               {@link #NEED_DATA_NOW},
     *               {@link #NEED_DATA_LATER}, or
     *               {@link #DATA_NOT_NEEDED}.
     * @return The named HDU.
     * @throws IOException if there was trouble reading the specified HDU from a
     *                     file.
     *                     <p/>
     *                     ************************************************************************
     */
    public FitsHDU getHDU(int number, int when) throws IOException {
        return getHDU(number);
    }

    /**
     * ***********************************************************************
     * write the file to an output stream.
     *
     * @param stream The output stream where the file will be written.
     * @throws IOException is there was trouble writing the file.
     *                     ************************************************************************
     */
    public void write(OutputStream stream) throws IOException {
        /*********************
         * loop over all HDUs *
         *********************/
        try {
            for (int i = 0; ; ++i) {
                FitsHDU hdu = getHDU(i);

                /*************************
                 * write the header cards *
                 *************************/
                FitsHeader header = hdu.getHeader();
                int ncards = header.cardCount();

                for (int j = 0; j < ncards; ++j) {
                    stream.write(header.card(j).data());
                }

                /**********************************
                 * pad out to the end of the block *
                 **********************************/
                int padding_cards = (header.blockCount() * FitsCard.CARDS_PER_BLOCK) -
                        ncards;

                for (int j = 0; j < padding_cards; ++j) {
                    stream.write(FitsCard.PADDING);
                }

                /*********************
                 * now write the data *
                 *********************/
                FitsData data = hdu.getData();
                stream.write(data.data());

                int padding_bytes = (data.blockCount() * BLOCK_SIZE) -
                        data.data().length;

                for (int j = 0; j < padding_bytes; ++j) {
                    stream.write(FitsData.PADDING);
                }
            } // end of loop over HDUs
        } catch (NoSuchFitsHDUException e) {
        }

        stream.close();
    } // end of write method

    /**
     * *************************************************************************
     * factory method to create a new empty FITS file.
     *
     * @throws FitsException if there was trouble creating the file, although this
     *                       is unlikely to ever happen.
     *                       **************************************************************************
     */
    public static FitsFile createEmpty() throws FitsException {
        FitsFile fits = new FitsFile();

        FitsImageData data = new FitsImageData();
        FitsHeader header = data.createHeader("PRIMARY");

        FitsHDU hdu = new FitsHDU(header, data);

        fits.add(hdu);

        return fits;
    } // end of createEmpty method
} // end of FitsFile class
