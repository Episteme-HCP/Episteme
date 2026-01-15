package org.jscience.io.fits;

/**
 * This is the similar to an ImageConsumer, except that it handles double
 * valued pixels. This whole paradigm for handling images may get scrapped.
 */
public interface RealImageConsumer {
    /**
     * called by the image producer when it is done sending pixels.
     *
     * @param status DOCUMENT ME!
     */
    public void imageComplete(int status);

    /**
     * called by the image producer to indicate the dimensions of the
     * image.
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void setDimensions(int width, int height);

    /**
     * Called by the producer to indicate the pixel value limits.
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     */
    public void setMinMax(double min, double max);

    /**
     * Called by the image producer to indicate hints about how the
     * pixels will be delivered. The hint flags are the same as for an
     * ImageConsumer.
     *
     * @param hintflags DOCUMENT ME!
     */
    public void setHints(int hintflags);

    /**
     * the image producer calls this method to deliver a batch of
     * pixels
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param offset DOCUMENT ME!
     * @param scansize DOCUMENT ME!
     */
    public void setPixels(int x, int y, int w, int h, double[] pixels,
        int offset, int scansize);
} // end of RealImageConsumer interface
