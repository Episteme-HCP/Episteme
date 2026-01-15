package org.jscience.awt;

/**
 * This is a generic interface for sending data to 2D graphs.
 *
 * @author Mark Hale
 * @version 1.0
 */
public interface Graph2DModel {
    /**
     * Add a listener.
     *
     * @param l DOCUMENT ME!
     */
    void addGraphDataListener(GraphDataListener l);

    /**
     * Remove a listener.
     *
     * @param l DOCUMENT ME!
     */
    void removeGraphDataListener(GraphDataListener l);

    /**
     * Returns the x coordinate for the ith point.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    float getXCoord(int i);

    /**
     * Returns the y coordinate for the ith point.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    float getYCoord(int i);

    /**
     * Returns the number of data points in the current series.
     *
     * @return DOCUMENT ME!
     */
    int seriesLength();

    /**
     * Selects the first data series.
     */
    void firstSeries();

    /**
     * Selects the next data series. Returns false if there is no next
     * series.
     *
     * @return DOCUMENT ME!
     */
    boolean nextSeries();
}
