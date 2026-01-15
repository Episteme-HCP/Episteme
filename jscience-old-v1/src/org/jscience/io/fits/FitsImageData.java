package org.jscience.io.fits;

import java.awt.image.ImageConsumer;
import java.io.EOFException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * ***********************************************************************
 * Represents the data in the primary HDU of an image extension.
 * The philosophy of this package is to represent FITS data in ways that are
 * natural to Java. So for example, a table is represented as a Swing TableModel.
 * Although the representation of images in Java has evolved over the years, this
 * class follows the old ImageProducer/ImageConsumer paradigm of treating
 * images a data streams. The problem is that that paradigm only handled
 * integer pixel values. So I invented a similar set of interfaces:
 * {@link RealImageProducer} and {@link RealImageConsumer}, and an
 * {@link ImageDigitizer} class which can convert from real to integer images.
 * <p/>
 * Another issue is that displayable images are two-dimensional, while FITS
 * image can have any number of dimensions. So we need a mechanism for making
 * two dimensional slices through the FITS data array.
 * <p/>
 * So you use the following proceedure to display an image:
 * <ol>
 * <li> create a {@link RealImageProducer}
 * with {@link #createView(int,int,int[]) } or
 * {@link #createView() }
 * <li> create an {@link ImageDigitizer} and register it as a consumer of
 * the producer you just created.
 * <li> Get a graphics Toolkit by calling the getToolkit() method of
 * an AWT Containter.
 * <li> Call the createImage(ImageProducer) method of the toolkit, using the
 * digitizer as the producer.
 * <li> Finally, you can display the image by putting it in an ImageIcon in a
 * JLabel, or by doing custom painting of a subclass of JPanel.
 * </ol>
 * <p/>
 * It made sense to treat images as data streams back when
 * computers had less memory
 * and connections to the internet had less bandwidth.
 * However, Java has evolved
 * to the simpler BufferedImage paradigm, where the pixel data are held in
 * memory. So at some point I plan to modify this class to be more like a
 * DataBuffer.
 * It also might be nice to make an image implement the TableModel interface,
 * to give tabular access to the pixels.
 * ************************************************************************
 */
public class FitsImageData extends FitsData {
    private int naxes;
    private int[] dimen;
    private int[] base;
    private int bitpix;
    private int bytepix;
    private boolean isReal;
    private double min;
    private double max;
    private boolean min_known;
    private boolean max_known;
    private boolean scaled;
    private double bzero;
    private double bscale;
    private boolean null_flag_defined;
    private int null_flag;
    private int valid_pixels;
    private Set views;

    /**
     * ***********************************************************************
     * Create an image as specified by the given HDU header
     *
     * @param header the HDU header which defined the image
     * @throws FitsException if there is a problem with the FITS format
     *                       ************************************************************************
     */
    public FitsImageData(FitsHeader header) throws FitsException {
        /**************************
         * allocate the data array *
         **************************/
        super(header.dataSize());

        /**************************
         * read the NAXIS keywords *
         **************************/
        naxes = header.card("NAXIS").intValue();

        int[] dimen = new int[naxes];
        ;

        for (int i = 0; i < naxes; ++i) {
            dimen[i] = header.card("NAXIS" + (i + 1)).intValue();
        }

        /**************************
         * read the BITPIX keyword *
         **************************/
        bitpix = header.card("BITPIX").intValue();

        if (bitpix < 0) {
            isReal = true;
            bitpix = -bitpix;
        } else {
            isReal = false;
        }

        /*********************
         * set the dimensions *
         *********************/
        initializeDimensions(bitpix, isReal, dimen);

        /***************************
         * read DATAMIN and DATAMAX *
         ***************************/
        try {
            min = header.card("DATAMIN").doubleValue();
            min_known = true;
        } catch (FitsCardException e) {
            min_known = false;
        }

        try {
            max = header.card("DATAMAX").doubleValue();
            max_known = true;
        } catch (FitsCardException e) {
            max_known = false;
        }

        /************************
         * read BZER0 and BSCALE *
         ************************/
        scaled = false;

        try {
            bzero = header.card("BZERO").doubleValue();
            scaled = true;
        } catch (NoSuchFitsCardException e) {
            bzero = 0.;
        }

        try {
            bscale = header.card("BSCALE").doubleValue();
            scaled = true;
        } catch (NoSuchFitsCardException e) {
            bscale = 1.;
        }
    } // end of constructor

    /**
     * ************************************************************************
     * Create an image from a set of image characteristics
     *
     * @param bitpix the number of bits per pixel. Note this should always be positive
     *               even though FITS uses a negative BITPIX to indicate real valued pixels.
     * @param isReal true if the pixels should have floating point values.
     * @param dimen  an array of image dimensions
     * @throws FitsException if invalid values are given. For example only certain
     *                       values of bitpix are allowed.
     *                       *************************************************************************
     */
    public FitsImageData(int bitpix, boolean isReal, int[] dimen)
            throws FitsException {
        super(sizeFromDimensions(bitpix, dimen));

        initializeDimensions(bitpix, isReal, dimen);
    } // end of constructor from dimensions

    /**
     * ************************************************************************
     * make an empty image. Useful for empty primary HDUs
     * *************************************************************************
     */
    public FitsImageData() throws FitsException {
        this(8, false, null);
    }

    /**
     * ************************************************************************
     * this method is called by the constructors to set the dimension
     * and bitpix information.
     *
     * @param bitpix the number of bits per pixel. Note this should always be positive
     *               even though FITS uses a negative BITPIX to indicate real valued pixels.
     * @param isReal true if the pixels should have floating point values.
     * @param dimen  an array of image dimensions
     * @throws FitsException if invalid values are given. For example only certain
     *                       values of bitpix are allowed.
     *                       *************************************************************************
     */
    private void initializeDimensions(int bitpix, boolean isReal, int[] dimen)
            throws FitsException {
        /*******
         * axes *
         *******/
        if (dimen != null) {
            naxes = dimen.length;
        } else {
            naxes = 0;
        }

        if (naxes > 0) {
            this.dimen = new int[naxes];

            for (int i = 0; i < naxes; ++i) {
                this.dimen[i] = dimen[i];
            }

            base = new int[naxes];
            base[0] = 1;

            for (int i = 1; i < naxes; ++i) {
                base[i] = base[i - 1] * dimen[i - 1];
            }
        }

        /*********
         * BITPIX *
         *********/
        if ((isReal && (bitpix != 32) && (bitpix != 64)) ||
                ((bitpix != 8) && (bitpix != 16) && (bitpix != 32))) {
            /***********************
             * illegal bitpix value *
             ***********************/
            throw new FitsException("BITPIX=" + bitpix + " not supported");
        }

        this.bitpix = bitpix;
        this.isReal = isReal;
        this.bytepix = bitpix / 8;

        /******************************
         * initialize the set of views *
         ******************************/
        views = new HashSet();
    } // end of initializeDimensions method

    /**
     * ************************************************************************
     * return the data array size for a given bitpix and set of dimensions
     * This is used by constructor from dimensions
     *
     * @param bitpix the number of bits per pixel. Note this should always be positive
     *               even though FITS uses a negative BITPIX to indicate real valued pixels.
     * @param dimen  an array of image dimensions
     *               *************************************************************************
     */
    private static int sizeFromDimensions(int bitpix, int[] dimen) {
        if ((dimen == null) || (dimen.length == 0)) {
            return 0;
        }

        int size = bitpix / 8;

        for (int i = 0; i < dimen.length; ++i) {
            size *= dimen[i];
        }

        return size;
    } // end of sizeFromDimensions method

    /**
     * ***********************************************************************
     * returns the total number of pixels in the image
     * ************************************************************************
     */
    public int pixelCount() {
        int count = 1;

        for (int i = 0; i < naxes; ++i) {
            count *= dimen[i];
        }

        return count;
    } // end of pixelCount method;

    /**
     * ***********************************************************************
     * returns the minimum pixel value in the image.
     * This is read from the DATAMIN keyword if it exists, otherwise it
     * is calculated from the image
     * ************************************************************************
     */
    public double min() throws FitsException {
        if (!min_known) {
            calculateMinMax();
        }

        return min;
    }

    /**
     * ***********************************************************************
     * returns the maximum pixel value in the image.
     * This is read from the DATAMAX keyword if it exists, otherwise it
     * is calculated from the image
     * ************************************************************************
     */
    public double max() throws FitsException {
        if (!max_known) {
            calculateMinMax();
        }

        return max;
    }

    /**
     * ***********************************************************************
     * update the number of valid bytes.
     * This overrides the parent class method to also track the number of valid
     * full pixels. This would be nice if we actually had a FitsFile class
     * which read pixels a few at a time, but we don't. At some point this
     * whole setValidBytes framework may get stripped out.
     * ************************************************************************
     */
    public void setValidBytes(int valid_bytes) throws FitsException {
        /*********************
         * do inherited stuff *
         *********************/
        super.setValidBytes(valid_bytes);

        /******************************************
         * cast the number in terms of full pixels *
         ******************************************/
        valid_pixels = valid_bytes / bytepix;

        /************************************************
         * tell all the views that there more pixels now *
         ************************************************/
        for (Iterator it = views.iterator(); it.hasNext();) {
            View view = (View) it.next();

            view.morePixelsAvailable();
        }
    } // end of setValidBytes

    /**
     * ***********************************************************************
     * create a new view of this image. A view is a two dimensional slice
     * of the FITS image data cube.
     *
     * @param x_axis the index of the horizontal axis in the slice.
     *               This index counts from zero.
     * @param y_axis the index of the vertical axis in the slice.
     *               This index counts from zero.
     * @param plane  an array indicating the coordinates at which to slide the data
     *               cube. For instance for a three dimensional image data array, if
     *               x_axis=0 and Y_axis=1, then if plane = {0, 0, 10} then the slice
     *               will be those pixels values with the third coordinates equals to 10.
     *               In this case the first two elements of plane are ignored.
     *               ************************************************************************
     */
    public RealImageProducer createView(int x_axis, int y_axis, int[] plane)
            throws FitsException {
        if (x_axis >= naxes) {
            throw new FitsException("View X axis " + x_axis + " invalid");
        }

        if (y_axis >= naxes) {
            throw new FitsException("View Y axis " + y_axis + " invalid");
        }

        View view = new View(x_axis, y_axis, plane);
        views.add(view);

        return view;
    } // end of create view method

    /**
     * ***********************************************************************
     * create a new view of this image which is the first plane of the first
     * two axes.
     * This method is mostly for convenience with two dimensional images.
     * ************************************************************************
     */
    public RealImageProducer createView() throws FitsException {
        int[] plane = new int[naxes];

        return createView(0, 1, plane);
    } // end of createView default method

    /**
     * ***********************************************************************
     * remove a view from the internal registry.
     * Such a view will no longer be updated when new pixels are read.
     * We don't actually have any methods which read pixels incrementally, though.
     * ************************************************************************
     */
    public void removeView(View view) {
        views.remove(view);
    }

    /**
     * ***********************************************************************
     * returns the pixel offset for a given set of coordinates
     * param coord the coordinates of a pixel. The coordinates all count from zero.
     * ************************************************************************
     */
    private long pixelNumber(int[] coord) {
        long offset = 0;

        for (int i = 0; i < naxes; ++i) {
            offset += (coord[i] * base[i]);
        }

        return offset;
    } // end of pixelNumber method

    /**
     * ***********************************************************************
     * set the internal pointer so the interpreter will read a given pixel next
     * ************************************************************************
     */
    public void goToPixel(int[] coord) {
        goToByte(bytepix * pixelNumber(coord));
    } // end of goToPixel method

    /**
     * ***********************************************************************
     * set the internal pointer to the first pixel in the image
     * ************************************************************************
     */
    public void goToPixel() {
        goToByte(0);
    } // end of goToPixel method

    /**
     * ***********************************************************************
     * returns the unscaled value of the pixel currently pointed to by the reader
     * ************************************************************************
     */
    public Number getNextRawPixel() throws FitsException {
        Number pixel;

        try {
            if (isReal) {
                /**************
                 * real pixels *
                 **************/
                if (bitpix == 32) {
                    pixel = new Float(interpreter.readFloat());
                } else if (bitpix == 64) {
                    pixel = new Double(interpreter.readDouble());
                } else {
                    throw new FitsException("BITPIX=" + (-bitpix) +
                            " not supported");
                }
            } else {
                /*****************
                 * integer pixels *
                 *****************/
                if (bitpix == 8) {
                    pixel = new Integer(interpreter.readUnsignedByte());
                } else if (bitpix == 16) {
                    pixel = new Short(interpreter.readShort());
                } else if (bitpix == 32) {
                    pixel = new Integer(interpreter.readInt());
                } else {
                    throw new FitsException("BITPIX=" + bitpix +
                            " not supported");
                }
            }
        } catch (EOFException e) {
            throw new FitsException("No such pixel in " + this);
        } catch (IOException e) {
            throw new FitsException("Couldn't get pixel in " + this);
        }

        return pixel;
    } // end of getNextRawPixel method

    /**
     * ***********************************************************************
     * returns the scaled value of the pixel currently pointed to by the reader
     * ************************************************************************
     */
    public double getNextPixel() throws FitsException {
        Number raw = getNextRawPixel();

        if (null_flag_defined && !isReal && (raw.intValue() == null_flag)) {
            return Double.NaN;
        }

        if (scaled) {
            return (raw.doubleValue() * bscale) + bzero;
        }

        return raw.doubleValue();
    } // end of getNextPixel method

    /**
     * ***********************************************************************
     * calculate the minimum and maximum pixel values from the image
     * if they are not already known (e.g. by reading the DATAMIN/DATAMAX
     * keywords in the constructor. Once we set these values they won't be
     * recalculated even if the pixel values are changed.
     * For completeness we should have a method for marking the limits as
     * unknown.
     * ************************************************************************
     */
    private void calculateMinMax() throws FitsException {
        /************************************************
         * don't calculate if we already know the answer *
         ************************************************/
        if (min_known && max_known) {
            return;
        }

        double min = Double.NaN;
        double max = Double.NaN;

        goToPixel();

        for (int i = 0; i < pixelCount(); ++i) {
            double pixel = getNextPixel();

            if (Double.isNaN(pixel)) {
                continue;
            }

            if ((pixel < min) || Double.isNaN(min)) {
                min = pixel;
            }

            if ((pixel > max) || Double.isNaN(max)) {
                max = pixel;
            }
        }

        /*****************************************
         * set the values if not previously known *
         *****************************************/
        if (!min_known) {
            this.min = min;
            min_known = true;
        }

        if (!max_known) {
            this.max = max;
            max_known = true;
        }
    } // end of calculateMinMax method

    /**
     * **************************************************************************
     * create the minimum header needed for this image. This is useful if you want
     * to create an image in memory and write the result to a FITS file. Only
     * problem is that we don't currently have a convenient way to set pixels.
     * ***************************************************************************
     */
    public FitsHeader createHeader(String name) {
        FitsHeader header = new FitsHeader();

        boolean primary = name.equalsIgnoreCase("PRIMARY");

        /*********************
         * SIMPLE or XTENSION *
         *********************/
        if (primary) {
            header.add(new FitsCard("SIMPLE", true,
                    "file conforms to FITS standard"));
        } else {
            header.add(new FitsCard("XTENSION", "IMAGE", "image extension"));
        }

        /*********
         * BITPIX *
         *********/
        if (isReal) {
            header.add(new FitsCard("BITPIX", -bitpix,
                    "number of bits per pixel"));
        } else {
            header.add(new FitsCard("BITPIX", bitpix, "number of bits per pixel"));
        }

        /********
         * NAXIS *
         ********/
        header.add(new FitsCard("NAXIS", naxes, "number of data axes"));

        for (int i = 0; i < naxes; ++i) {
            header.add(new FitsCard("NAXIS" + (i + 1), dimen[i],
                    "length of data axis " + (i + 1)));
        }

        /****************************
         * EXTEND or PCOUNT & GCOUNT *
         ****************************/
        if (primary) {
            header.add(new FitsCard("EXTEND", true,
                    "file may contain extensions"));
        } else {
            header.add(new FitsCard("PCOUNT", 0,
                    "number of random group parameters"));
            header.add(new FitsCard("GCOUNT", 0, "number of random groups"));
        }

        /**********
         * EXTNAME *
         **********/
        if (!primary && (name.length() > 0)) {
            header.add(new FitsCard("EXTNAME", name, "name of extension"));
        }

        /****************
         * pixel scaling *
         ****************/
        if (scaled) {
            header.add(new FitsCard("BZERO", bzero, "pixel scale zero point"));
            header.add(new FitsCard("BSCALE", bscale, "pixel scale"));
        }

        /**********
         * min/max *
         **********/
        if (min_known) {
            header.add(new FitsCard("DATAMIN", min, "minimum pixel value"));
        }

        if (max_known) {
            header.add(new FitsCard("DATAMAX", max, "maximum pixel value"));
        }

        /******
         * END *
         ******/
        header.add(new FitsCard("END"));

        return header;
    } // end of createHeader method

    /**
     * ************************************************************************
     * this embedded class represents a two dimensional slice through the image.
     * It implements the RealImageProducer interface so it can be fed to an
     * {@link ImageDigitizer} to get an ImageProducer.
     * *************************************************************************
     */
    private class View implements RealImageProducer {
        private int x_axis;
        private int y_axis;
        private int[] coord;
        private long first_image_pixel;
        private long last_image_pixel;
        private double[] pixels;
        private int npixels; // number of pixels decoded
        private Set real_consumers;
        private int lines_sent;

        /**
         * ************************************************************************
         * Create a new view which slices the image in the specified way.
         *
         * @param x_axis the index of the horizontal axis in the slice.
         *               This index counts from zero.
         * @param y_axis the index of the vertical axis in the slice.
         *               This index counts from zero.
         * @param plane  an array indicating the coordinates at which to slide the data
         *               cube. For instance for a three dimensional image data array, if
         *               x_axis=0 and Y_axis=1, then if plane = {0, 0, 10} then the slice
         *               will be those pixels values with the third coordinates equals to 10.
         *               In this case the first two elements of plane are ignored.
         *               *************************************************************************
         */
        public View(int x_axis, int y_axis, int[] plane)
                throws FitsException {
            this.x_axis = x_axis;
            this.y_axis = y_axis;
            this.coord = plane;

            coord[x_axis] = 0;
            coord[y_axis] = 0;
            first_image_pixel = pixelNumber(coord);

            coord[x_axis] = dimen[x_axis];
            coord[y_axis] = dimen[y_axis];
            last_image_pixel = pixelNumber(coord);

            real_consumers = new HashSet();

            /*************************************
             * initialize the scaled pixel buffer *
             *************************************/
            pixels = new double[dimen[x_axis] * dimen[y_axis]];
            npixels = 0;

            decodeImagePixels();
        } // end of constructor

        /**
         * **********************************************************************
         * read the pixel data from the iomage and feed it to all the registered
         * consumers.
         * **********************************************************************
         */
        private void decodeImagePixels() throws FitsException {
            for (int i = npixels; i < pixels.length; ++i) {
                /*******************************************
                 * find the current view pixel in the image *
                 *******************************************/
                coord[x_axis] = i % dimen[x_axis];
                coord[y_axis] = dimen[y_axis] - (i / dimen[x_axis]) - 1; // flip y axis

                if (pixelNumber(coord) >= valid_pixels) {
                    /**************************************************************
                     * this pixel hasn't been read into the image yet so stop here *
                     **************************************************************/
                    npixels = i;

                    return;
                }

                /*******************
                 * decode the pixel *
                 *******************/
                goToPixel(coord);
                pixels[i] = getNextPixel();
            } // end of loop over pixels

            npixels = pixels.length;
        } // end of decodeImagePixels method

        /**
         * *****************************************************************
         * the image calls this method when a new set of pixels has been read
         * ******************************************************************
         */
        public void morePixelsAvailable() throws FitsException {
            decodeImagePixels();
            sendAvailablePixels();
        } // end of morePixelsAvailable method

        // real image production methods -------------------------------------

        /**
         * *********************************************************************
         * regiater an image consumer to receive pixels from this image producer.
         * **********************************************************************
         */
        public void addConsumer(RealImageConsumer ic) {
            real_consumers.add(ic);
        }

        /**
         * ****************************************************************
         * returns true if the specified consumer is registered.
         * *****************************************************************
         */
        public boolean isConsumer(RealImageConsumer ic) {
            return real_consumers.contains(ic);
        }

        /**
         * ****************************************************************
         * remove a consumer if it has been previously registered.
         * *****************************************************************
         */
        public void removeConsumer(RealImageConsumer ic) {
            real_consumers.remove(ic);
        }

        /**
         * *****************************************************************
         * register the given consumer and begin delivering pixels
         * ******************************************************************
         */
        public void startProduction(RealImageConsumer new_ic) {
            addConsumer(new_ic);

            /*************************
             * feed all the consumers *
             *************************/
            for (Iterator it = real_consumers.iterator(); it.hasNext();) {
                RealImageConsumer ic = (RealImageConsumer) it.next();

                /*******************
                 * image dimensions *
                 *******************/
                ic.setDimensions(dimen[x_axis], dimen[y_axis]);

                try {
                    ic.setMinMax(min(), max());
                } catch (FitsException e) {
                }

                /***********************
                 * pixel delivery hints *
                 ***********************/
                int hints = 0;
                hints |= ImageConsumer.SINGLEFRAME;
                hints |= ImageConsumer.SINGLEPASS;
                hints |= ImageConsumer.TOPDOWNLEFTRIGHT;
                hints |= ImageConsumer.COMPLETESCANLINES;

                ic.setHints(hints);

                /******************************
                 * send all the pixels we have *
                 ******************************/
                lines_sent = 0;
                sendAvailablePixels();
            } // end of loop over consumers
        } // end of startProduction method

        /**
         * ************************************************************************
         * this doesn't do anything.
         * *************************************************************************
         */
        public void requestTopDownLeftRightResend(RealImageConsumer ic) {
        }

        /**
         * ************************************************************************
         * send the pixels in a separate background thread
         * *************************************************************************
         */
        public void sendAvailablePixels() {
            new PixelDeliverer().start();
        }

        /**
         * ************************************************************************
         * Send all available pixels which have not already been sent to
         * all the consumers
         * *************************************************************************
         */
        synchronized public void actuallySendAvailablePixels() {
            /*************************************
             * don't bother if nobody's listening *
             *************************************/
            if (real_consumers.size() == 0) {
                return;
            }

            /**********************************************
             * don't bother if there's nothing new to send *
             **********************************************/
            int pixels_sent = lines_sent * dimen[x_axis];
            int last_complete_line = (npixels / dimen[x_axis]) - 1;
            int nlines = last_complete_line - lines_sent + 1;

            if (nlines <= 0) {
                return;
            }

            /***************************************
             * send the pixels to all the consumers *
             ***************************************/
            for (Iterator it = real_consumers.iterator(); it.hasNext();) {
                RealImageConsumer ic = (RealImageConsumer) it.next();

                ic.setPixels(0, lines_sent, dimen[x_axis], nlines, pixels,
                        pixels_sent, dimen[x_axis]);
            } // end of loop over consumers

            lines_sent = last_complete_line;

            /***********************************
             * check if we have sent all pixels *
             ***********************************/
            if (npixels == pixels.length) {
                /*********************************
                 * send the image complete method *
                 *********************************/
                Set temp = new HashSet(real_consumers);

                for (Iterator it = temp.iterator(); it.hasNext();) {
                    RealImageConsumer ic = (RealImageConsumer) it.next();

                    ic.imageComplete(ImageConsumer.STATICIMAGEDONE);
                }
            }
        } // end of sendAvailablePixels method

        /**
         * ***********************************************************************
         * yet another imbedded class which implements a thread for
         * sending pixels in the background
         * ************************************************************************
         */
        private class PixelDeliverer extends Thread {
            public void run() {
                actuallySendAvailablePixels();
            }
        } // end of PixelDeliverer class
    } // end of view embedded class *******************************************
} // end of FitsImageData class
