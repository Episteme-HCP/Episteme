package org.jscience.tests.io.fitsbrowser;

import org.jscience.io.fits.FitsException;
import org.jscience.io.fits.FitsImageData;
import org.jscience.io.fits.ImageDigitizer;
import org.jscience.io.fits.RealImageProducer;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * A component used for displaying an image in a FITS file. Is can only
 * display two dimensional images. The user may adjust the gamma correction of
 * the image for better viewing. The component can also be resized to show the
 * image at different resolutions. Neither of these adjustments affect the
 * underlying FITS data. This class is used by the {@link
 * eap.fitsbrowser.HDUDisplay} but it may also be embedded in any tool which
 * needs to display FITS image data.
 */
public class FitsImageViewer extends JPanel {
    /** DOCUMENT ME! */
    FitsImageData data;

    /** DOCUMENT ME! */
    RealImageProducer view;

    /** DOCUMENT ME! */
    ImageDigitizer digitizer;

    /** DOCUMENT ME! */
    Toolkit toolkit;

    /** DOCUMENT ME! */
    Image image;

    /** DOCUMENT ME! */
    MediaTracker tracker;

    /** DOCUMENT ME! */
    Picture picture;

    /** DOCUMENT ME! */
    GammaAdjuster gamma_adjuster;

    /** DOCUMENT ME! */
    Monitor monitor;

/**
     * Create a new viewer which will display the given image data.
     *
     * @param data the image data to display.
     * @throws FitsException DOCUMENT ME!
     */
    public FitsImageViewer(FitsImageData data) throws FitsException {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.red);

        this.data = data;

        view = data.createView();

        /** make color model and create a digitizer */
        ColorModel cm = makeColorModel(Color.black, Color.white);

        //ColorModel cm = makeColorModel(Color.red, Color.blue);
        digitizer = new ImageDigitizer(view, cm);
/**
         * create the image
         */
        toolkit = getToolkit();
        image = toolkit.createImage(digitizer);

        picture = new Picture();
        add(picture);
    } // end of constructor

    /**
     * Adds a slider to allow adjustment of the gamma correction
     */
    public void allowGammaCorrection() {
        if (gamma_adjuster != null) {
            return;
        }

        gamma_adjuster = new GammaAdjuster();
        add(gamma_adjuster);

        validate();
    } // end of allowGammaCorrection method

    /**
     * Add a progress bar to monitor the image production. This is
     * useful for large images where the gamma correction may take a while to
     * apply.
     */
    public void showProgress() {
        add(new Monitor());
        validate();
    } // end of showProgress method

    /**
     * change the gamma correction value of the digitization
     */
    private void updateImage() {
        tracker.removeImage(image);
        image = toolkit.createImage(digitizer);
        tracker.addImage(image, 0);

        //try {tracker.waitForAll();}
        //catch(InterruptedException e) {}
        picture.repaint();
    }

    /**
     * Change the image being displayed.
     *
     * @param data The new image to display.
     *
     * @throws FitsException DOCUMENT ME!
     */
    public void setImage(FitsImageData data) throws FitsException {
        this.data = data;
        view = data.createView();
        digitizer.setImageSource(view);

        updateImage();
    } // end of setImage method

    /**
     * change the gamma correction value of the digitization.
     *
     * @param gamma the new gamma correction value.
     */
    public void setGamma(double gamma) {
        digitizer.setGamma(gamma);
        updateImage();
    } // end of setGamma method

    /**
     * Create a color model intended for use with the digitizer. The
     * model will be an index color model sweeping from one color to another,
     * interpolating linearly in HSB color space
     *
     * @param from DOCUMENT ME!
     * @param to DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ColorModel makeColorModel(Color from, Color to) {
        /** get HSB values of initial and final colors */
        float[] hsb0 = Color.RGBtoHSB(from.getRed(), from.getGreen(),
                from.getBlue(), null);

        float[] hsb1 = Color.RGBtoHSB(to.getRed(), to.getGreen(), to.getBlue(),
                null);

        /** initialize byte arrays */
        byte[] r = new byte[256];
        byte[] g = new byte[256];
        byte[] b = new byte[256];

        for (int i = 0; i <= (Byte.MAX_VALUE - Byte.MIN_VALUE); ++i) {
            float hat = (float) i / (float) 255.;

            float hue = ((hsb1[0] - hsb0[0]) * hat) + hsb0[0];
            float saturation = ((hsb1[1] - hsb0[1]) * hat) + hsb0[1];
            float brightness = ((hsb1[2] - hsb0[2]) * hat) + hsb0[2];

            Color c = Color.getHSBColor(hue, saturation, brightness);

            int value;
/**
             * convert red to a byte
             */
            value = c.getRed();

            if ((value >= 0) && (value <= Byte.MAX_VALUE)) {
                r[i] = (byte) value;
            } else {
                r[i] = (byte) (value - 256);
            }

/**
             * convert green to a byte
             */
            value = c.getGreen();

            if ((value >= 0) && (value <= Byte.MAX_VALUE)) {
                g[i] = (byte) value;
            } else {
                g[i] = (byte) (value - 256);
            }

/**
             * convert blue to a byte
             */
            value = c.getBlue();

            if ((value >= 0) && (value <= Byte.MAX_VALUE)) {
                b[i] = (byte) value;
            } else {
                b[i] = (byte) (value - 256);
            }
        } //end of loop over color values

        return new IndexColorModel(8, 256, r, g, b);
    } // ends of makeColorModel method

    /**
     * this embedded class displays the actual picture
     */
    private class Picture extends JPanel {
        /** DOCUMENT ME! */
        int image_width;

        /** DOCUMENT ME! */
        int image_height;

/**
         * create a new image display object.
         */
        public Picture() {
            tracker = new MediaTracker(this);
            tracker.addImage(image, 0);
            tracker.checkAll(true);

            image_width = image.getWidth(this);
            image_height = image.getHeight(this);

            setPreferredSize(new Dimension(image_width, image_height));
        } // end of constructor

        /**
         * Paint the image
         *
         * @param g DOCUMENT ME!
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            /** maintain the image's natural aspect ratio */
            int width = getWidth();
            int height = getHeight();

            if ((width * image_height) >= (image_width * height)) {
                width = (image_width * height) / image_height;
            } else {
                height = (image_height * width) / image_width;
            }

/**
             * paint the image
             */
            g2.drawImage(image, 0, 0, width, height, this);
        } // end of paint method

        /**
         * DOCUMENT ME!
         *
         * @param img DOCUMENT ME!
         * @param infoflags DOCUMENT ME!
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         * @param width DOCUMENT ME!
         * @param height DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean imageUpdate(Image img, int infoflags, int x, int y,
            int width, int height) {
            boolean value = super.imageUpdate(img, infoflags, x, y, width,
                    height);

            return value;
        }
    } // end of Picture embedded class *****************************************

    /**
     * Inner class to handle the image delivery progress monitor
     */
    private class Monitor extends JProgressBar implements ChangeListener {
/**
         * Create a new progress monitor handler.
         */
        public Monitor() {
            digitizer.addChangeListener(this);
        } // end of constructor

        /**
         * Respond to a change event from the digitizer by updating
         * the progress bar
         *
         * @param e DOCUMENT ME!
         */
        public void stateChanged(ChangeEvent e) {
            setValue((int) (digitizer.getProgress() * 100));
        } // end of stateChanged method
    } // end of Monitor embedded class ******************************************

    /**
     * embedded class to adjust the gamma correction
     */
    private class GammaAdjuster extends JPanel implements ChangeListener {
        /** DOCUMENT ME! */
        JSlider slider;

        /** DOCUMENT ME! */
        JLabel label;

        /** DOCUMENT ME! */
        DecimalFormat format;

        /** DOCUMENT ME! */
        int slider_granularity;

/**
         * construct a new slider for setting the gamma correction
         */
        public GammaAdjuster() {
            setLayout(new BorderLayout());

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            slider_granularity = 1000;
            slider = new JSlider(0, slider_granularity, slider_granularity / 2);
            slider.addChangeListener(this);
            add(slider, BorderLayout.CENTER);

            //format = new DecimalFormat();
            format = (DecimalFormat) NumberFormat.getInstance();

            //format.setMaximumFractionDigits(3);
            //format.setMinimumFractionDigits(3);
            //format.setMaximumIntegerDigits(3);
            label = new JLabel();
            showGammaValue(getGamma());
            add(label, BorderLayout.EAST);
        } // end of constructor

        /**
         * Respond to a change in the slider state
         *
         * @param e DOCUMENT ME!
         */
        public void stateChanged(ChangeEvent e) {
            double gamma = getGamma();
            showGammaValue(gamma);

            if (!slider.getValueIsAdjusting()) {
                setGamma(gamma);
            }
        } // end of stateChanged method

        /**
         * Calculate the scaled gamma value from the current slider
         * position
         *
         * @return DOCUMENT ME!
         */
        private double getGamma() {
            double raw = (double) slider.getValue() / (double) slider_granularity;
            double gamma = Math.tan(raw * Math.PI * .5);

            //double gamma = Math.pow(gamma_limit,(double)(raw-50)/50. );
            return gamma;
        } // end of getGamma method

        /**
         * show a given gamma value in the label
         *
         * @param gamma DOCUMENT ME!
         */
        private void showGammaValue(double gamma) {
            label.setText("gamma=" + format.format(gamma));
        } // end of showGammaValue method
    } // end of GammaAdjuster embedded class *************************************
} // end of FitsImageViewer class
