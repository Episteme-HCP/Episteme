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
import java.io.InputStream;


/**
 * Represents a FITS file to be read from a serial data source. This class
 * assumes no buffering in the underlying stream, so there is no way to skip
 * an HDU and then go back to read it later.
 */
public class InputStreamFitsFile extends FitsFile {
    /**
     * DOCUMENT ME!
     */
    InputStream file;

    /**
     * Create a new object specifiying the data source
     *
     * @param file the data source
     * @throws IOException if there was a problem creating the file.
     */
    public InputStreamFitsFile(InputStream file) throws IOException {
        super();

        this.file = file;
    } // end of constructor

    /**
     * read a buffer-full of data from the file - usually one 2880 byte block's
     * worth. If EOF is reached, it sets the isComplete flag to true and
     * returns false. It throws an exception if EOF was reached before the
     * buffer was filled.
     *
     * @param buffer the byte array to be filled.
     * @return true if there are more data to read from the file or false if we
     *         have reached EOF
     * @throws IOException   iof there was aproblem with the underlying IO
     * @throws FitsException if the file does not contain an integeral number
     *                       of blocks.
     */
    private boolean readBlock(byte[] buffer) throws IOException {
        int nread = 0;

        for (int i = 0; i < buffer.length; i += nread) {
            nread = file.read(buffer, i, buffer.length - i);

            if (nread == -1) {
                /**
                 * End of File
                 */
                isComplete = true;

                if (i != 0) {
                    throw new FitsException("Incomplete data block in " + this);
                } else {
                    return false;
                }
            }
        } // end of read loop

        return true;
    } // end of readBlock method

    /**
     * read an HDU starting at the current file pointer position. This method
     * reads the data part of the HDU as well as the header.
     *
     * @return the HDU read
     * @throws IOException if there was trouble with the underlying I/O
     */
    private FitsHDU readNextHDU() throws IOException {
        /** create the HDU */
        FitsHDU hdu = new FitsHDU();

        /** read the header */
        FitsHeader header = hdu.getHeader();

        /** read the header one block at a time */
        byte[] buffer = new byte[FitsFile.BLOCK_SIZE];

        while (readBlock(buffer) && !header.add(buffer))
            ;

        /**
         * check for EOF and return null if we tried to read an HDU where there
         * wasn't one
         */
        if (isComplete) {
            return null;
        }

        /**
         * initialize and read the data
         */
        hdu.initData();
        fillInData(hdu);

        /**
         * return the HDU
         */
        return hdu;
    } // end of readHDU method

    /**
     * reads the data for a given HDU at the specified time
     *
     * @param hdu DOCUMENT ME!
     * @param hdu the hdu whose data will be read
     * @param hdu the hdu whose data will be read
     * @param hdu the hdu whose data will be read
     * @throws IOException DOCUMENT ME!
     *                     <p/>
     *                     reads the data for a given HDU
     * @throws IOException if there was trouble with the underlying I/O
     *                     <p/>
     *                     reads the data for a given HDU
     * @throws IOException if there was trouble with the underlying I/O
     *                     <p/>
     *                     reads the data for a given HDU
     * @throws IOException if there was trouble with the underlying I/O
     */

    // private void fillInData(FitsHDU hdu, int when) throws IOException{
    //
    // if(     when==FitsFile.NEED_DATA_NOW  ) fillInData(hdu);
    // else if(when==FitsFile.NEED_DATA_LATER) new DataFiller(hdu).start();
    //
    // } // end of fillInData method

    /**
     * reads the data for a given HDU
     *
     * @param hdu the hdu whose data will be read
     *
     * @throws IOException if there was trouble with the underlying I/O
     */
    synchronized private void fillInData(FitsHDU hdu) throws IOException {
        /** do nothing if the data have already been read */
        FitsData data = hdu.getData();

        if (data.isComplete()) {
            return;
        }

        /** now read the data */
        byte[] buffer = data.data();
        readBlock(buffer);
        data.setValidBytes(buffer.length);

        /** now read the padding out of the data stream */
        int size = buffer.length;
        int nblocks = ((size + BLOCK_SIZE) - 1) / BLOCK_SIZE;
        byte[] padding = new byte[(nblocks * BLOCK_SIZE) - size];
        readBlock(padding);
    } // end of fillInData method

    /**
     * this embedded class implements a thread for reading the data part of an
     * HDU in the background
     *
     * @param number DOCUMENT ME!
     * @param number DOCUMENT ME!
     * @param number DOCUMENT ME!
     * @param number DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     *                     <p/>
     *                     returns an HDU specified by number.
     * @throws IOException DOCUMENT ME!
     *                     <p/>
     *                     returns an HDU specified by number.
     * @throws IOException DOCUMENT ME!
     *                     <p/>
     *                     returns an HDU specified by number.
     * @throws IOException DOCUMENT ME!
     */

    // public class DataFiller extends Thread {
    //
    // FitsHDU hdu;
    //
    // public DataFiller(FitsHDU hdu) { this.hdu=hdu; }
    // public void run() {
    //
    // try {fillInData(hdu);}
    // catch(IOException e) {
    //
    // }
    // } // end of run method
    //
    // } // end of DataFille embedded class *************************************

    /**
     * returns an HDU specified by number.
     *
     * @param number DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public FitsHDU getHDU(int number) throws IOException {
        return getHDU(number, FitsFile.NEED_DATA_NOW);
    }

    /**
     * returns an HDU specified by number. The "when" hint is ignored and all
     * data are read before returning. This could probably be changed to
     * accomodate DATA_NOT_NEEDED.
     *
     * @param number the index of the HDU to read. The primary HDU is numbered
     *               "0".
     * @param when   This parameter is ignored. The data part of the HDU is
     *               always read.
     * @return DOCUMENT ME!
     * @throws IOException            DOCUMENT ME!
     * @throws NoSuchFitsHDUException DOCUMENT ME!
     */
    public FitsHDU getHDU(int number, int when) throws IOException {
        /**
         * read more data if we haven't gotten to this HDU yet
         */
        for (int i = hdus.size(); (i <= number) && !isComplete(); ++i) {
            FitsHDU hdu = readNextHDU();

            if (hdu != null) {
                add(hdu);
            }
        }

        /**
         * return the hdu if we have it
         */
        if (hdus.size() > number) {
            FitsHDU hdu = (FitsHDU) hdus.get(number);

            return hdu;
        }

        /**
         * if we get here, the HDU requested does not exist
         */
        throw new NoSuchFitsHDUException("Can't get HDU " + number + " in " +
                this);
    } // end of getHDU method

    /**
     * returns an HDU specified by EXTNAME
     *
     * @param name the EXTNAME of the HDU or "PRIMARY" for the primary HDU.
     * @return DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public FitsHDU getHDU(String name) throws IOException {
        return getHDU(name, FitsFile.NEED_DATA_NOW);
    } // end of getHDU method

    /**
     * returns an HDU specified by name with a hint for when to read the data.
     *
     * @param name the EXTNAME of the HDU or "PRIMARY" for the primary HDU.
     * @param when this argument is ignored and the data part of the HDU is
     *             always read.
     * @return DOCUMENT ME!
     * @throws IOException            if there was trouble with the underlying I/O
     * @throws NoSuchFitsHDUException if the named HDU is not present in the
     *                                file.
     */
    public FitsHDU getHDU(String name, int when) throws IOException {
        /**
         * read more data if we haven't gotten to this HDU yet
         */
        for (int i = hdus.size(); !index.containsKey(name) && !isComplete();
             ++i) {
            FitsHDU hdu = readNextHDU();

            if (hdu != null) {
                add(hdu);
            }
        }

        /**
         * return the hdu if we have it go back and read the data if we have to
         */
        if (index.containsKey(name)) {
            FitsHDU hdu = (FitsHDU) index.get(name);

            return hdu;
        }

        /**
         * if we get here, the HDU requested does not exist
         */
        throw new NoSuchFitsHDUException("Can't get HDU" + name + " in " +
                this);
    } // end of getHDU method
} // end of InputStreamFitsFile class
