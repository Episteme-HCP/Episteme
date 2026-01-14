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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * ****************************************************************************
 * This class converts a stream of double-valued pixels from a
 * {@link RealImageProducer} into a stream of integer pixels. This class
 * implements the Java ImageProducer interface, so it can be used to create
 * a Java Image.
 * <p/>
 * The output image will first be gamma corrected, meaning the pixel values
 * are raised to the power of 1/&gamma;, where &gamma; is set by
 * {@link #setGamma(double)}. Then it is scaled so that the smallest real pixel
 * value corresponds to the smallest ingeger or byte value and the
 * largest real value corresponds to the largest ingeter or byte value.
 * <p/>
 * Since digitization can take time, you can register ChangeListeners to
 * monitor the progress of the digitization.
 * *****************************************************************************
 */
public class ImageDigitizer implements RealImageConsumer, ImageProducer {
    RealImageProducer source;
    ColorModel color;
    int height;
    int width;
    double min;
    double max;
    Set consumers;
    boolean sending;
    int int_NaN_value;
    byte byte_NaN_value;
    double gamma;
    Set listeners;
    int total_pixels;
    int pixels_done;
    double change_amount;
    int change_amount_pixels;

    /**
     * ***********************************************************************
     * Create an ImageDigitizer using a given color model and which will
     * digitize the data from a given source.
     *
     * @param source the source of double valued pixel data.
     * @param color  The color model to use in the digitized image.
     *               ************************************************************************
     */
    public ImageDigitizer(RealImageProducer source, ColorModel color) {
        sending = false;

        this.source = source;
        this.color = color;

        consumers = new HashSet();

        byte_NaN_value = 0;
        int_NaN_value = 0;

        gamma = 1.;

        listeners = new HashSet();
        change_amount = 0.01;
        pixels_done = 0;
    } // end of constructor

    /**
     * ***********************************************************************
     * set the RealImageProducer source for the digitizer. This can be used to
     * change the source after the digitizer has been created.
     * ************************************************************************
     */
    public void setImageSource(RealImageProducer source) {
        abortSend();
        this.source = source;
    }

    /**
     * ***********************************************************************
     * set the digitized int pixel value to use for NaN pixels
     * ************************************************************************
     */
    public void setNaNvalue(int value) {
        int_NaN_value = value;
    }

    /**
     * ***********************************************************************
     * set the digitized byte pixel value to use for NaN pixels
     * ************************************************************************
     */
    public void setNaNvalue(byte value) {
        byte_NaN_value = value;
    }

    /**
     * ***********************************************************************
     * set the color model for the digitized pixels. This can be used to
     * change the color model after creating the digitizer.
     * ************************************************************************
     */
    public void setColorModel(ColorModel color) {
        this.color = color;
    }

    /**
     * ***********************************************************************
     * get the color model for the digitized pixels
     * ************************************************************************
     */
    public ColorModel getColorModel() {
        return color;
    }

    /**
     * ***********************************************************************
     * set the gamma correction factor to used in the digitization.
     * where digitized is proportional to real^1./gamma
     * ************************************************************************
     */
    public void setGamma(double gamma) {
        this.gamma = 1. / gamma;
    }

    /**
     * ***********************************************************************
     * add a change listener to receive change events when significant progress
     * has been made digitizing the image
     * ************************************************************************
     */
    public void addChangeListener(ChangeListener l) {
        listeners.add(l);
    }

    /**
     * ***********************************************************************
     * remove a change listener
     * ************************************************************************
     */
    public void removeChangeListener(ChangeListener l) {
        listeners.remove(l);
    }

    /**
     * ***********************************************************************
     * set the interval at which change events are fired in terms of
     * fraction of completion
     * ************************************************************************
     */
    public void setChangeIncrement(double increment) {
        change_amount = increment;
        change_amount_pixels = (int) (total_pixels * increment);
    } // end of setChangeIncrement

    /**
     * ***********************************************************************
     * check how far along we are digitizing the image and fire a change event
     * if we have passed a milestone .
     * ************************************************************************
     */
    private void checkProgress() {
        /****************************************************
         * don't bother if nobody is listening
         * or if the number of pixels hasn't changed by much
         ****************************************************/
        if ((listeners.size() == 0) ||
                ((pixels_done % change_amount_pixels) != 0)) {
            return;
        }

        fireChangeEvent();
    } // end of checkProgress method

    /**
     * ***********************************************************************
     * send a change event to all listeners
     * ************************************************************************
     */
    private void fireChangeEvent() {
        Set set = new HashSet(listeners);

        for (Iterator it = set.iterator(); it.hasNext();) {
            ChangeListener l = (ChangeListener) it.next();

            l.stateChanged(new ChangeEvent(this));
        }
    } // end of fireChangeEvent method

    /**
     * ***********************************************************************
     * returns the fraction of pixels which have been processed
     * ************************************************************************
     */
    public double getProgress() {
        return (double) pixels_done / (double) total_pixels;
    } // end of getProgress method

    /**
     * ***********************************************************************
     * abort the image we are sending if we are actually sending an image
     * ************************************************************************
     */
    public void abortSend() {
        if (!sending) {
            return;
        }

        imageComplete(ImageConsumer.IMAGEABORTED);
    } // end of abortSend method

    // consumer methods:

    /**
     * ***********************************************************************
     * pass along the image complete message to the integer image.
     * ************************************************************************
     */
    public void imageComplete(int status) {
        /***********************************
         * remove us from the real producer *
         ***********************************/
        source.removeConsumer((RealImageConsumer) this);

        /************************************************************
         * pass on the message to the consumers
         * note we iterate over a copy of the consumer set so that
         * the consumers can call removeConsumer without getting
         * a ConcurrentModificationException
         ************************************************************/
        Set temp = new HashSet(consumers);

        for (Iterator it = temp.iterator(); it.hasNext();) {
            ImageConsumer ic = (ImageConsumer) it.next();

            ic.imageComplete(status);
        }

        sending = false;

        pixels_done = 0;
        fireChangeEvent();
    } // end of imageComplete method

    /**
     * ***********************************************************************
     * pass this information along to the integer image stream.
     * ************************************************************************
     */
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;

        total_pixels = width * height;
        change_amount_pixels = (int) (total_pixels * change_amount);

        if (change_amount_pixels < 1) {
            change_amount_pixels = 1;
        }

        for (Iterator it = consumers.iterator(); it.hasNext();) {
            ImageConsumer ic = (ImageConsumer) it.next();

            ic.setDimensions(width, height);
            ic.setColorModel(color);
        }
    } // end of setDimensions method

    /**
     * ***********************************************************************
     * The RealImageProducer calls this method to indicate the pixel limits.
     * ************************************************************************
     */
    public void setMinMax(double min, double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * ***********************************************************************
     * pass this information along to the integer image stream.
     * ************************************************************************
     */
    public void setHints(int hintflags) {
        for (Iterator it = consumers.iterator(); it.hasNext();) {
            ImageConsumer ic = (ImageConsumer) it.next();

            ic.setHints(hintflags);
        }
    } // end of setHints

    /**
     * ***********************************************************************
     * the RealImageProducer calls this method to deliver a set of pixels.
     * This method scales the pixels and converts them to integers or bytes
     * as appropriate for the color model.
     * ************************************************************************
     */
    public void setPixels(int x, int y, int w, int h, double[] pixels,
                          int offset, int scansize) {
        int pixel_size = color.getPixelSize();

        if (pixel_size == 8) {
            /**************
             * byte pixels *
             **************/
            byte[] buffer = new byte[w * h];

            for (int i = 0; i < buffer.length; ++i) {
                buffer[i] = digitizeToByte(pixels[i + offset]);

                ++pixels_done;

                checkProgress();
            }

            /***********************************
             * send the pixels to the consumers *
             ***********************************/
            for (Iterator it = consumers.iterator(); it.hasNext();) {
                ImageConsumer ic = (ImageConsumer) it.next();

                ic.setPixels(x, y, w, h, color, buffer, 0, scansize);
            }
        } else if (pixel_size == 32) {
            /*****************
             * integer pixels *
             *****************/
            int[] buffer = new int[w * h];

            for (int i = 0; i < buffer.length; ++i) {
                buffer[i] = digitizeToInt(pixels[i + offset]);
                ++pixels_done;
                checkProgress();
            }

            /***********************************
             * send the pixels to the consumers *
             ***********************************/
            for (Iterator it = consumers.iterator(); it.hasNext();) {
                ImageConsumer ic = (ImageConsumer) it.next();

                ic.setPixels(x, y, w, h, color, buffer, 0, scansize);
            }
        } // end if integer pixels
    } // end of setPixels method

    /**
     * ***********************************************************************
     * return a pixel value scaled so that min -> 0 and max -> 1
     * and with gamma correction applied.
     * ************************************************************************
     */
    public double scalePixel(double pixel) {
        double hat = (pixel - min) / (max - min);

        if (gamma != 1.0) {
            hat = Math.pow(hat, gamma);
        }

        return hat;
    } // end of scalePixel method

    /**
     * ***********************************************************************
     * this is where the actual digitization happens for byte color models
     * ************************************************************************
     */
    public byte digitizeToByte(double pixel) {
        if (Double.isNaN(pixel)) {
            return byte_NaN_value;
        }

        double hat = scalePixel(pixel);

        int digitized = (int) (hat * 255);

        if (digitized >= 0) {
            return (byte) digitized;
        } else {
            return (byte) (digitized - Byte.MAX_VALUE + Byte.MIN_VALUE);
        }
    } // end of digitizeToByte method

    /**
     * ***********************************************************************
     * this is where the actual digitization happens for int color models
     * ************************************************************************
     */
    public int digitizeToInt(double pixel) {
        if (Double.isNaN(pixel)) {
            return int_NaN_value;
        }

        double hat = scalePixel(pixel);

        return (int) ((hat * (Integer.MAX_VALUE - Integer.MIN_VALUE)) -
                Integer.MIN_VALUE);
    } // end of digitizeToInt method

    // Producer methods:

    /**
     * ***********************************************************************
     * register an integer image consumer.
     * ************************************************************************
     */
    public void addConsumer(ImageConsumer ic) {
        consumers.add(ic);
    }

    /**
     * ***********************************************************************
     * returns true if the given integer consumer is registered.
     * ************************************************************************
     */
    public boolean isConsumer(ImageConsumer ic) {
        return consumers.contains(ic);
    }

    /**
     * ***********************************************************************
     * unregister the given integer consumer.
     * ************************************************************************
     */
    public void removeConsumer(ImageConsumer ic) {
        consumers.remove(ic);
    }

    /**
     * ***********************************************************************
     * Begin producing pixels for the integer consumers. This just calls
     * {@link RealImageProducer#startProduction(RealImageConsumer)}.
     * ************************************************************************
     */
    public void startProduction(ImageConsumer ic) {
        sending = true;

        addConsumer(ic);
        source.startProduction(this);
    }

    /**
     * ***********************************************************************
     * passes the request up to the {@link RealImageProducer }
     * ************************************************************************
     */
    public void requestTopDownLeftRightResend(ImageConsumer ic) {
        sending = true;

        addConsumer(ic);
        source.requestTopDownLeftRightResend(this);
    }
} // end of ImageDigitizer class
