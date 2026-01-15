package org.jscience.awt;

/**
 * This is a generic interface for sending data to 2D category graphs.
 *
 * @author Mark Hale
 * @version 1.0
 */
public interface CategoryGraph2DModel {
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
     * Returns the ith category.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    String getCategory(int i);

    /**
     * Returns the value for the ith category.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    float getValue(int i);

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
