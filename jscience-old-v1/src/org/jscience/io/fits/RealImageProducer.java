package org.jscience.io.fits;

/**
 * This interface is analogous to ImageProducer, except that it handles pixels
 * of type double. This paradigm for handling images may get scrapped.
 */
public interface RealImageProducer {
    /**
     * register a consumer to receive pixels from this producer.
     *
     * @param ic DOCUMENT ME!
     */
    public void addConsumer(RealImageConsumer ic);

    /**
     * returns true if the given consumer is registered with this
     * producer
     *
     * @param ic DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isConsumer(RealImageConsumer ic);

    /**
     * unregister a consumer
     *
     * @param ic DOCUMENT ME!
     */
    public void removeConsumer(RealImageConsumer ic);

    /**
     * register the given consumer and begin sending pixels
     *
     * @param ic DOCUMENT ME!
     */
    public void startProduction(RealImageConsumer ic);

    /**
     * register the given consumer and request that this producer start
     * sending pixels in TDLR order. This method is not required to do
     * anything
     *
     * @param ic DOCUMENT ME!
     */
    public void requestTopDownLeftRightResend(RealImageConsumer ic);
} // end of RealImageProducer interface
