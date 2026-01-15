package org.jscience.io.fits;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 * ***********************************************************************
 * Represents the data part of a FITS HDU.
 * The data are stored internally as a byte array, and this class provides
 * I/O streams for reading and writing formatted data in that array.
 * This way the user has random access to the data, but does not need to parse
 * the entire HDU if they are only interested in a small part of it.
 * The problem with this approach is that it can require a large amount of memory,
 * and it may be impossible to read particularly large FITS files. At some
 * point I should modify this class to keep the raw storage on disk.
 * However, there are many uses (e.g. in an Applet), where you may
 * not be allowed to write to a local disk, so I'm not sure how useful that
 * would be in practice.
 * <p/>
 * This class does not understand the structure
 * of the data. Therefore this class is amost never instantiated. Instead,
 * you should typically use {@link FitsImageData} or {@link FitsBinTableData}.
 * <p/>
 * ************************************************************************
 */
public class FitsData {
    /**
     * *******************************************************************
     * value to use when padding out partial blocks. This is zero according to the
     * FITS standard
     * *********************************************************************
     */
    public static final int PADDING = 0;

    /**
     * the internal data array
     */
    protected byte[] data;

    /**
     * the number of bytes which have been read from a data source
     */
    protected int valid_bytes;

    /**
     * flag indicating if all the data bytes have been read
     */
    protected boolean isComplete;

    /**
     * *************************************************************
     * used for reading formatted data from the internal data array
     * **************************************************************
     */
    protected DataInputStream interpreter;
    private ByteArrayInputStream reader;

    /**
     * **********************************************************
     * for writing formatted values into the internal data array
     * ***********************************************************
     */
    protected DataOutputStream setter;
    private InternalOutputStream writer;

    /**
     * ***********************************************************************
     * Creates a data object with the internal data array initialized
     * to hold the given number of bytes.
     *
     * @param size the number of bytes in the data, not including padding.
     *             ************************************************************************
     */
    public FitsData(int size) {
        data = new byte[size];

        reader = new ByteArrayInputStream(data);
        interpreter = new DataInputStream(reader);

        writer = new InternalOutputStream();
        setter = new DataOutputStream(writer);
    } // end of constructor

    /**
     * ***********************************************************************
     * Create a data object with the correct size for the given header.
     * Note that this just creates a generic data object. Use
     * {@link #createFrom(FitsHeader) } if you want to create a data object
     * of the appropriate type
     *
     * @param header - the header to use to derive the size of the data.
     * @throws FitsCardException if the header is not properly formed.
     * @see #createFrom(FitsHeader)
     *      ************************************************************************
     */
    public FitsData(FitsHeader header) throws FitsCardException {
        this(header.dataSize());
    }

    /**
     * ************************************************************************
     * returns the number of blocks (including partial blocks) in the data
     * *************************************************************************
     */
    public int blockCount() {
        return ((data.length + FitsFile.BLOCK_SIZE) - 1) / FitsFile.BLOCK_SIZE;
    }

    /**
     * ************************************************************************
     * returns the raw data array
     * *************************************************************************
     */
    protected byte[] data() {
        return data;
    }

    /**
     * ************************************************************************
     * update the number of bytes which have been set in the data array.
     * This method is used by the subclasses of {@link FitsFile} to indicate
     * the fraction of the raw data array which has been read from a data source.
     * This method should not be called by the general user.
     *
     * @param valid_bytes the new number of valid bytes in the raw data array
     * @throws FitsException never, however subclasses may do so.
     * @see FitsFile#getHDU(int,int)
     *      *************************************************************************
     */
    public void setValidBytes(int valid_bytes) throws FitsException {
        if (valid_bytes >= data.length) {
            this.valid_bytes = data.length;
            isComplete = true;
        } else {
            this.valid_bytes = valid_bytes;
        }
    } // end of setValidBytes method

    /**
     * ************************************************************************
     * adjust the number of valid bytes by a given increment.
     *
     * @param new_bytes the change in the number of valid bytes
     * @throws FitsException never, however subclasses may do so.
     * @see #setValidBytes(int)
     *      *************************************************************************
     */
    public void incrementValidBytes(int new_bytes) throws FitsException {
        setValidBytes(valid_bytes + new_bytes);
    } // end of incrementValidBytes method

    /**
     * ************************************************************************
     * returns true if the data array has been completely read. This will only
     * return false if {@link FitsFile#getHDU(int,int)} was called with the
     * {@link FitsFile#NEED_DATA_LATER} hint.
     * *************************************************************************
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * *************************************************************************
     * This is a factory method to produce a FitsData object of the appropriate
     * type corresponding to a given header
     *
     * @param header the header specifying the size and type of the data
     * @return a new FitsData structure of the proper type. Usually this will
     *         be a subclass of FitsData.
     *         **************************************************************************
     * @throws FitsException if the header was malformed.
     */
    public static FitsData createFrom(FitsHeader header)
            throws FitsException {
        /*******************************
         * determine the extension type *
         *******************************/
        String type;

        try {
            type = header.card("XTENSION").stringValue();
        } catch (NoSuchFitsCardException e) {
            type = "IMAGE";
        } catch (FitsCardException e) {
            type = "";
        }

        /*********************************************
         * return a structure of the appropriate type *
         *********************************************/
        if (type.equals("IMAGE")) {
            return new FitsImageData(header);
        } else if (type.equals("BINTABLE")) {
            return new FitsBinTableData(header);
        } else if (type.equals("TABLE")) {
            return new FitsASCIITableData(header);
        } else {
            return new FitsData(header);
        }
    } // end of createFrom factory method

    /**
     * *************************************************************************
     * reposition the data reader and writer streams to a given offset in the
     * raw data array.
     * **************************************************************************
     */
    public void goToByte(long new_position) {
        long position = data.length - reader.available();

        if (new_position < position) {
            /*********************
             * skipping backwards *
             *********************/
            reader.reset();
            position = reader.skip(new_position);
        } else {
            /********************
             * skipping forwards *
             ********************/
            reader.skip(new_position - position);
        }

        /*********************************
         * set the position in the writer *
         *********************************/
        writer.goTo((int) new_position);
    } // end of goToByte method

    /**
     * **************************************************************************
     * returns the number of bytes remaining in the raw data array between the
     * current position and the end of the array.
     * ***************************************************************************
     */
    protected int available() {
        return reader.available();
    }

    /**
     * *************************************************************************
     * this embedded class implements an OutputStream which writes into
     * the data array.
     * **************************************************************************
     */
    private class InternalOutputStream extends OutputStream {
        int position;

        /**
         * *******************************************
         * write a single byte into the internal array
         * ********************************************
         */
        public void write(int b) {
            data[position] = (byte) b;
            ++position;
        }

        /**
         * ***********************************************************
         * set the offset at which the next byte will be written.
         * *************************************************************
         */
        public void goTo(int position) {
            this.position = position;
        }
    } // end of InternalOutputStream embedded class
} // end of FitsData class
