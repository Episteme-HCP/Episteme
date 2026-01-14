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
import java.io.RandomAccessFile;
import java.util.Iterator;

/**
 * ***********************************************************************
 * Represents a FitsFile which can be read from a RandomAccessFile data source.
 * A random access data source allows you to skip over HDUs and then go back
 * to read them later. So this class can take full advantage of the
 * hints given to the getHDU methods. Note however that the "NEED_DATA_LATER"
 * option is not currently well tested and may contain bugs.
 * ************************************************************************
 */
public class RandomAccessFitsFile extends FitsFile {
    RandomAccessFile file;
    long first_new_byte;

    /**
     * ***********************************************************************
     * Create a FITS file object which can be read from the given RandomAccessFile.
     *
     * @param file the data source
     * @throws IOException if there was trouble creating the FitsFile
     *                     ************************************************************************
     */
    public RandomAccessFitsFile(RandomAccessFile file)
            throws IOException {
        super();

        this.file = file;
        first_new_byte = 0;
    } // end of constructor

    /**
     * ***********************************************************************
     * read an HDU starting at the current file pointer position. This method
     * 9nitialized the data part of the HDU, but does not read it. That much be done
     * with one of the fillInData methods.
     *
     * @return the HDU just read
     * @throws IOException if there was trouble in the underlying I/O or the data
     *                     do not follow proper FITS format.
     *                     ************************************************************************
     */
    private FitsHDU readNextHDU() throws IOException {
        /*************************************************************
         * set the file pointer to the begining of the unread portion *
         *************************************************************/
        file.seek(first_new_byte);

        /*****************
         * create the HDU *
         *****************/
        FitsHDU hdu = new FitsHDU();

        /******************
         * read the header *
         ******************/
        FitsHeader header = hdu.getHeader();

        for (byte[] data = new byte[FitsFile.BLOCK_SIZE];
             (file.read(data) == data.length) && !header.add(data);)
            ;

        /************************************************
         * check to be sure the header was read properly *
         ************************************************/
        if (!header.isComplete()) {
            throw new FitsException("Invalid FITS format");
        }

        /*********************************************
         * initialize the data, but don't read it yet *
         *********************************************/
        hdu.initData();

        /*******************************************************
         * update the new data indicator to just after this HDU *
         *******************************************************/
        first_new_byte += (FitsFile.BLOCK_SIZE * hdu.blockCount());

        if (first_new_byte >= file.length()) {
            isComplete = true;
        }

        /*****************
         * return the HDU *
         *****************/
        return hdu;
    } // end of readHDU method

    /**
     * ***********************************************************************
     * reads the data for a given HDU at the specified time.
     *
     * @param hdu  the HDU whose data will be read.
     * @param when the hint flag telling when to read the data
     *             this must be either {@linkcFitsFile#NEED_DATA_NOW} or
     *             {@linkcFitsFile#NEED_DATA_LATER}
     *             for the former it will call {@link #fillInData(FitsHDU)} in the current
     *             thread. For the latter it will do the same in a separate thread and
     *             return immediately.
     *             <p/>
     *             ************************************************************************
     */
    private void fillInData(FitsHDU hdu, int when) throws IOException {
        if (when == FitsFile.NEED_DATA_NOW) {
            fillInData(hdu);
        } else if (when == FitsFile.NEED_DATA_LATER) {
            new DataFiller(hdu).start();
        }
    } // end of fillInData method

    /**
     * ***********************************************************************
     * read the data for a given HDU.
     *
     * @param hdu the HDU whose data will be read.
     * @throws IOException if there was trouble with the underlying I/O or the
     *                     the FITS format.
     *                     ************************************************************************
     */
    synchronized private void fillInData(FitsHDU hdu) throws IOException {
        /************************************************
         * do nothing if the data have already been read *
         ************************************************/
        FitsData data = hdu.getData();

        if (data.isComplete()) {
            return;
        }

        /**********************************
         * find the first byte of the data *
         **********************************/
        long start_byte = 0;

        for (Iterator it = hdus.iterator(); it.hasNext();) {
            FitsHDU current = (FitsHDU) it.next();

            if (current == hdu) {
                break;
            }

            start_byte += current.blockCount();
        }

        start_byte += hdu.getHeader().blockCount();

        // shouldn't we be seeking the byte offset right here?

        /********************
         * now read the data *
         ********************/
        byte[] buffer = data.data();

        if (file.read(buffer) != buffer.length) {
            throw new FitsException("Couldn't read all data for " + data);
        }

        data.setValidBytes(buffer.length);
    } // end of fillInData method

    /**
     * ***********************************************************************
     * returns an HDU specified by number. The data for this HDU will have been
     * read before this method returns. This method is the same as calling
     * {@link #getHDU(int,int)} with {@link FitsFile#NEED_DATA_NOW}.
     *
     * @param number the index of HDU to be read. The primary HDU is numbered 0.
     *               ************************************************************************
     */
    public FitsHDU getHDU(int number) throws IOException {
        return getHDU(number, FitsFile.NEED_DATA_NOW);
    }

    /**
     * ***************************************************************************
     * Returns the given HDU, specifying a hint as to when to read the data.
     * Note that HDUs are not read from the underlying file until needed.
     *
     * @param number the index of HDU to be read. The primary HDU is numbered 0.
     * @param when   a hint as to when to read the data part of the HDU.
     *               <ul>
     *               <li> {@link FitsFile#NEED_DATA_NOW} - read the data before returning
     *               <li> {@link FitsFile#NEED_DATA_LATER} - read the data in a separate thread
     *               after returning
     *               <li> {@link FitsFile#DATA_NOT_NEEDED} - don't read the data
     *               </ul>
     * @return the specified HDU.
     *         ****************************************************************************
     * @throws IOException            if there was trouble with the underlying I/O or with the
     *                                FITS formating.
     * @throws NoSuchFitsHDUException if the file does not have the specified HDU.
     */
    public FitsHDU getHDU(int number, int when) throws IOException {
        /******************************************************
         * read more data if we haven't gotten to this HDU yet *
         ******************************************************/
        for (int i = hdus.size(); (i <= number) && !isComplete(); ++i) {
            add(readNextHDU());
        }

        /******************************************
         * return the hdu if we have it
         * go back and read the data if we have to
         ******************************************/
        if (hdus.size() > number) {
            FitsHDU hdu = (FitsHDU) hdus.get(number);
            fillInData(hdu, when);

            return hdu;
        }

        /***************************************************
         * if we get here, the HDU requested does not exist *
         ***************************************************/
        throw new NoSuchFitsHDUException("Can't get HDU " + number + " in " +
                this);
    } // end of getHDU method

    /**
     * ***********************************************************************
     * returns an HDU specified by name. This is the same as calling
     * {@link #getHDU(String,int)} with {@link FitsFile#NEED_DATA_NOW}
     *
     * @param name the EXTNAME value for the desired HDU or "PRIMARY" for the
     *             primary HDU.
     * @throws IOEXCEPTION if there was a problem with the underlying I/O or
     *                     the FITS format.
     *                     ************************************************************************
     */
    public FitsHDU getHDU(String name) throws IOException {
        return getHDU(name, FitsFile.NEED_DATA_NOW);
    } // end of getHDU method

    /**
     * ***********************************************************************
     * returns an HDU specified by name with a hint as to when to read the data.
     *
     * @param name the EXTNAME value for the desired HDU or "PRIMARY" for the
     *             primary HDU.
     * @param when a hint as to when to read the data part of the HDU.
     *             <ul>
     *             <li> {@link FitsFile#NEED_DATA_NOW} - read the data before returning
     *             <li> {@link FitsFile#NEED_DATA_LATER} - read the data in a separate thread
     *             after returning
     *             <li> {@link FitsFile#DATA_NOT_NEEDED} - don't read the data
     *             </ul>
     * @throws IOEXCEPTION            if there was a problem with the underlying I/O or
     *                                the FITS format.
     * @throws NoSuchFitsHDUException if the file does not have the named HDU
     *                                ************************************************************************
     */
    public FitsHDU getHDU(String name, int when) throws IOException {
        /******************************************************
         * read more data if we haven't gotten to this HDU yet *
         ******************************************************/
        for (int i = hdus.size(); !index.containsKey(name) && !isComplete();
             ++i) {
            add(readNextHDU());
        }

        /******************************************
         * return the hdu if we have it
         * go back and read the data if we have to
         ******************************************/
        if (index.containsKey(name)) {
            FitsHDU hdu = (FitsHDU) index.get(name);
            fillInData(hdu, when);

            return hdu;
        }

        /***************************************************
         * if we get here, the HDU requested does not exist *
         ***************************************************/
        throw new NoSuchFitsHDUException("Can't get HDU" + name + " in " +
                this);
    } // end of getHDU method

    /**
     * **********************************************************************
     * This embedded class implements a thread for reading the data
     * part of an HDU in the background.
     * ***********************************************************************
     */
    private class DataFiller extends Thread {
        FitsHDU hdu;

        /**
         * ***************************************************************
         * create a new object which will read the data for the given HDU
         * ****************************************************************
         */
        public DataFiller(FitsHDU hdu) {
            this.hdu = hdu;
        }

        /**
         * *******************************************************************
         * call {@link #fillInData(FitsHDU)} for the HDU specified in the
         * constructor. Exceptions are caught and reported to stderr.
         * *************************************************************************
         */
        public void run() {
            try {
                fillInData(hdu);
            } catch (IOException e) {
                System.err.println("Error while reading data for " + hdu +
                        "in background");
                e.printStackTrace();
            }
        } // end of run method
    } // end of DataFille embedded class *************************************
} // end of RandomAccessFitsFile class
